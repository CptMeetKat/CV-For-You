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

        if(args.length == 0)
            return;
        else if(args[0].equals("-a"))
        {
            SeekAppliedJobsWrapper appliedJobWrapper = new SeekAppliedJobsWrapper();
            ArrayList<SeekAppliedJob> applied = appliedJobWrapper.getAppliedJobsStats();
            System.exit(0);
        }

        ArgParser ap = new ArgParser();
        if ( ap.parseArgs(args) )
            new App(ap);
    }

    public App(ArgParser ap)
    {
        HashMap<String, InputJob> job_descriptions = getJobs(ap.getJDSource());


        for (String job_id: job_descriptions.keySet()) 
        {
            DocumentGenerator generator = new DocumentGenerator(ap.getInputDocument(),
                                    ap.getSections(),
                                    ap.getOutputFolder());
            generator.generateDocument(job_descriptions.get(job_id), job_id); //UGLY! FIX!
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
