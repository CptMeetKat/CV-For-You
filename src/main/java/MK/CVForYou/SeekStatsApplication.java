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

    private static List<SeekAppliedJob> updateHistoricalStats(List<SeekAppliedJob> historical_data, List<SeekAppliedJob> current_data)
    {
        HashMap<String, SeekAppliedJob> history_map = recordsToMap(historical_data);

        for (SeekAppliedJob fresh : current_data) {
            String id = fresh.getIdentifer();
            if(history_map.containsKey(id))
            {
                SeekAppliedJob history_record = history_map.get(id);
                if(!history_record.toString().equals(fresh.toString()))
                    logger.info("Updating 1 with 2\n {} \n {}\n", history_record, fresh);
            }
            else
            {
                logger.info("Add new record\n{}\n", fresh);
                history_map.put(fresh.getIdentifer(), fresh);
            }
        }
        return mapToRecords(history_map);
    }

    private static HashMap<String, SeekAppliedJob> recordsToMap(List<SeekAppliedJob> records)
    {
        HashMap<String, SeekAppliedJob> map = new HashMap<>();

        for (SeekAppliedJob record : records) {
            map.put(record.getIdentifer(), record);
        }

        return map;
    }

    private static List<SeekAppliedJob> mapToRecords(HashMap<String, SeekAppliedJob> map)
    {
        ArrayList<SeekAppliedJob> records = new ArrayList<SeekAppliedJob>();
        for (String key : map.keySet()) {
            records.add(map.get(key));
        }
        return records;
    }

    public static void aggregateStats()
    {
        ArrayList<SeekAppliedJob> current_applied_jobs = new SeekAppliedJobsWrapper().getAppliedJobsStats();
        List<SeekAppliedJob> latest_stats = current_applied_jobs;

        try {
			List<SeekAppliedJob> history = CSVReader.readFromFile(SeekAppliedJob.class, "data.csv");
            latest_stats = updateHistoricalStats(history, latest_stats);
		} catch (IOException e) {
            logger.warn("Unable to read historical Seek statistics file: {}", e.getMessage()); //TODO: may lose historical data, if there is read problem
			e.printStackTrace();
		} 

        String data = CSVGenerator.makeCSV(latest_stats, SeekAppliedJob.class);
        IOUtils.writeToFile(data, "data.csv");
    }
}
