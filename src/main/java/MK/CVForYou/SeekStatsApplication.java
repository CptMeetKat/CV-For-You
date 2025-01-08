package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekStatsApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekStatsApplication.class);
    SeekAppliedJobSource applied_job_source;
    SeekAppliedJobInsightsSource applied_job_insights_source;

    Path output_location;
    Path input_location;
    int mode = 0;
    
    public SeekStatsApplication(SeekStatsArgs ap)
    {
        mode = ap.getMode();
        
        if(mode == 1)
        {
            output_location = ap.getOutput();
            setDependency(new SeekAppliedJobsWrapper(), SeekAppliedJobSource.class);
            setDependency(new SeekAppliedJobInsightsWrapper(), SeekAppliedJobInsightsSource.class);
        }
        else if(mode == 2)
        {
            input_location = ap.getInput();
        }
    }


    private static List<SeekAppliedJobCSVRow> updateHistoricalStats(List<SeekAppliedJobCSVRow> historical_data, List<SeekAppliedJobCSVRow> current_data)
    {
        HashMap<String, SeekAppliedJobCSVRow> history_map = recordsToMap(historical_data);

        for (SeekAppliedJobCSVRow fresh : current_data) {
            String id = fresh.getIdentifer();
            if(history_map.containsKey(id))
            {
                SeekAppliedJobCSVRow history_record = history_map.get(id);
                if(!history_record.toString().equals(fresh.toString())) {
                    logger.info("Updating 1 with 2\n {} \n {}\n", history_record, fresh);
                    history_map.put(id, fresh);
                }
            }
            else
            {
                logger.info("Adding new record\n{}\n", fresh);
                history_map.put(fresh.getIdentifer(), fresh);
            }
        }
        return mapToRecords(history_map);
    }

    private static HashMap<String, SeekAppliedJobCSVRow> recordsToMap(List<SeekAppliedJobCSVRow> records)
    {
        HashMap<String, SeekAppliedJobCSVRow> map = new HashMap<>();

        for (SeekAppliedJobCSVRow record : records) {
            map.put(record.getIdentifer(), record);
        }

        return map;
    }

    private static List<SeekAppliedJobCSVRow> mapToRecords(HashMap<String, SeekAppliedJobCSVRow> map)
    {
        ArrayList<SeekAppliedJobCSVRow> records = new ArrayList<SeekAppliedJobCSVRow>();
        for (String key : map.keySet()) {
            records.add(map.get(key));
        }
        return records;
    }

    public void aggregateStats()
    {
        ArrayList<SeekAppliedJob> current_applied_jobs = applied_job_source.getAppliedJobsStats();
        ArrayList<SeekAppliedJobCSVRow> rows = new ArrayList<>();

        if(current_applied_jobs.size() > 0) 
            logger.info("Requesting insights for the following {} applied jobs...", current_applied_jobs.size());

        for (SeekAppliedJob application : current_applied_jobs) {
            logger.info("  {} ({})", application.job_title, application.job_id);

            applied_job_insights_source.setTargetJob(application.job_id);
            rows.add(new SeekAppliedJobCSVRow(application, applied_job_insights_source.getInsights()));
        }
            
        String filename = output_location.toString();
        List<SeekAppliedJobCSVRow> latest_stats = rows;
        try {
			List<SeekAppliedJobCSVRow> history = CSVReader.readFromFile(SeekAppliedJobCSVRow.class, filename);
            latest_stats = updateHistoricalStats(history, rows);
		} catch (IOException e) {
            logger.warn("Unable to read historical Seek statistics file: {}", e.getMessage()); //TODO: may lose historical data, if there is read problem
		}

        String data = CSVGenerator.makeCSV(latest_stats, SeekAppliedJobCSVRow.class);

        IOUtils.writeToFile(data, filename);
    }

	@Override
	public void run() {
        if(mode == 1)
            aggregateStats();
        else if(mode == 2)
            summariseStats();
	}

    public void summariseStats()
    {
        logger.trace("Doing Summarise....");
    }


    @Override
	public <T> void setDependency(T service, Class<T> serviceType) {
        if(serviceType == SeekAppliedJobSource.class)
            applied_job_source = (SeekAppliedJobSource) service;
        else if(serviceType == SeekAppliedJobInsightsSource.class)
            applied_job_insights_source = (SeekAppliedJobInsightsSource) service;
        else
            logger.warn("SeekStatsApplication dependency not set for {}", serviceType.toString());
    }
}

