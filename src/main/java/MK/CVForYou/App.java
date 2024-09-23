package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App 
{
    static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        if ( mode == 1 )
            new App(ap);
        else if( mode == 2)
            aggregateStats();
    }

    public static void aggregateStats()
    {
        SeekAppliedJobsWrapper appliedJobWrapper = new SeekAppliedJobsWrapper();
        ArrayList<SeekAppliedJob> applied_jobs = appliedJobWrapper.getAppliedJobsStats();
        List<SeekAppliedJob> history = ApplicationAggregator.readFromFile(SeekAppliedJob.class, "data.csv"); 

        HashMap<String, SeekAppliedJob> history_map = new HashMap<String, SeekAppliedJob>();
        for (SeekAppliedJob h : history) {
            history_map.put(h.getIdentifer(), h);
        }

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

    public App(ArgParser ap)
    {
        HashMap<String, InputJob> job_descriptions = getJobs(ap.getJDSource());


        for (String job_id: job_descriptions.keySet()) 
        {
            DocumentGenerator generator = new DocumentGenerator(ap.getInputDocument(),
                                    ap.getSections(),
                                    ap.getOutputFolder());
            generator.generateDocument(job_descriptions.get(job_id), job_id); //TODO: UGLY! FIX!
            ExecuteChromePDFGenerator.run(job_id, ap.getOutputFolder());
        }
    }

    public static HashMap<String, InputJob> getJobs(JobSource jd_source)
    {
        HashMap<String, InputJob> job_descriptions = new HashMap<String, InputJob>();
        ArrayList<InputJob> jobs = jd_source.getJobModel(); 
        for (InputJob job : jobs) {
            if(job.name != null)
                job_descriptions.put(job.name, job);
        }

        return job_descriptions;
    }
}
