package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;

public class App 
{
    public static void main( String[] args )
    {
        ArgParser ap = new ArgParser();
        if ( ap.parseArgs(args) )
            new App(ap);
    }

    public App(ArgParser ap)
    {
        HashMap<String, String> job_descriptions = getJobDescriptions(ap.getJDSource());

        for (String job_id: job_descriptions.keySet()) 
        {
            DocumentGenerator generator = new DocumentGenerator(ap.input_document,
                                    ap.getSections(),
                                    ap.getOutputPath());
            generator.generateDocument(job_descriptions.get(job_id), job_id);
            ExecuteChromePDFGenerator .run(job_id);
        }
    }

    public static HashMap<String, String> getJobDescriptions(JobDescriptionSource jd_source)
    {
        HashMap<String, String> job_descriptions = new HashMap<String, String>();
        ArrayList<InputJob> jobs = jd_source.getJobDescriptions();
        for (InputJob job : jobs) {
            job_descriptions.put(job.name, job.description);
        }

        return job_descriptions;
    }
}
