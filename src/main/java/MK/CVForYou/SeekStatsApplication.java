package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        //TODO: if file dosent exist, then do not read from it

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
        ArrayList<SeekAppliedJob> applied_jobs = new SeekAppliedJobsWrapper().getAppliedJobsStats();
        List<SeekAppliedJob> history = ApplicationAggregator.readFromFile(SeekAppliedJob.class, "data.csv"); 
        
        List<SeekAppliedJob> latest_stats = updateHistoricalStats(history, applied_jobs);

        String data = CSVGenerator.makeCSV(latest_stats, SeekAppliedJob.class);
        IOUtils.writeToFile(data, "data.csv");
    }
}
