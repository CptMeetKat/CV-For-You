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

    private void updateHistoricalStats(List<SeekAppliedJob> historical_data, List<SeekAppliedJob> current_data)
    {

    }

    private static HashMap<String, SeekAppliedJob> recordsToMap(List<SeekAppliedJob> records)
    {
        HashMap<String, SeekAppliedJob> map = new HashMap<>();

        for (SeekAppliedJob record : records) {
            map.put(record.getIdentifer(), record);
        }

        return map;
    }

    public static void aggregateStats()
    {
        ArrayList<SeekAppliedJob> applied_jobs = new SeekAppliedJobsWrapper().getAppliedJobsStats();
        List<SeekAppliedJob> history = ApplicationAggregator.readFromFile(SeekAppliedJob.class, "data.csv"); 

        HashMap<String, SeekAppliedJob> history_map = recordsToMap(history);

        //TODO: if file dosent exist, then do not read from it

        for (SeekAppliedJob fresh : applied_jobs) {
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

        ArrayList<SeekAppliedJob> new_history = new ArrayList<SeekAppliedJob>();
        for (String key : history_map.keySet()) {
            new_history.add(history_map.get(key));
        }

        String data = CSVGenerator.makeCSV(new_history, SeekAppliedJob.class);
        IOUtils.writeToFile(data, "data.csv");
    }
}
