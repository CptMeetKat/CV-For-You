package MK.CVForYou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Write acceptance tests
public class SeekStatsApplication
{
    static final Logger logger = LoggerFactory.getLogger(SeekStatsApplication.class);
    public SeekStatsApplication()
    {
        aggregateStats();
    }

    private static List<SeekAppliedJobCSVRow> updateHistoricalStats(List<SeekAppliedJobCSVRow> historical_data, List<SeekAppliedJobCSVRow> current_data)
    {
        HashMap<String, SeekAppliedJobCSVRow> history_map = recordsToMap(historical_data);

        for (SeekAppliedJobCSVRow fresh : current_data) {
            String id = fresh.getIdentifer();
            if(history_map.containsKey(id))
            {
                SeekAppliedJobCSVRow history_record = history_map.get(id);
                if(!history_record.toString().equals(fresh.toString()))
                {
                    logger.info("Updating 1 with 2\n {} \n {}\n", history_record, fresh);

                    history_map.put(id, fresh);
                }
            }
            else
            {
                logger.info("Add new record\n{}\n", fresh);
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

    public static void aggregateStats()
    {
        ArrayList<SeekAppliedJob> current_applied_jobs = new SeekAppliedJobsWrapper().getAppliedJobsStats();
        ArrayList<SeekAppliedJobCSVRow> rows = new ArrayList<>();

        if(current_applied_jobs.size() > 0)
            logger.info("Requesting information for {} insight records...", current_applied_jobs.size());

        for (SeekAppliedJob application : current_applied_jobs) {
            logger.info("Fetching insights for '{}' ({})", application.job_title, application.job_id);

            SeekAppliedJobInsightsWrapper requester = new SeekAppliedJobInsightsWrapper(application.job_id);
            rows.add(new SeekAppliedJobCSVRow(application, requester.getInsights()));
        }
            
        String filename = "data.csv";
        List<SeekAppliedJobCSVRow> latest_stats = rows;
        try {
			List<SeekAppliedJobCSVRow> history = CSVReader.readFromFile(SeekAppliedJobCSVRow.class, filename);
            latest_stats = updateHistoricalStats(history, rows);
		} catch (IOException e) {
            logger.warn("Unable to read historical Seek statistics file: {}", e.getMessage()); //TODO: may lose historical data, if there is read problem
			e.printStackTrace();
		}

        String data = CSVGenerator.makeCSV(latest_stats, SeekAppliedJobCSVRow.class);

        IOUtils.writeToFile(data, filename);
    }
}

