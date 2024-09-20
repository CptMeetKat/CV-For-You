package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;

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
        String[] fields = {"job_id", "job_title", "active", "company_name", "company_id", "status", "status_times", "latest_status", "latest_status_time", "applied_at", "created_at", "applied_with_cover", "applied_with_cv"}; 
        String result = CSVGenerator.makeCSV(fields, applied_jobs, SeekAppliedJob.class);
        System.out.println(result);

        ArrayList<SeekAppliedJob> data_from_file = ApplicationAggregator.readData();
        for (SeekAppliedJob j : data_from_file) {
            //System.out.println(j.job_title);
        }
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
