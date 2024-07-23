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
        HashMap<String, String> job_descriptions = getJobDescriptions(ap);

        for (String job_id: job_descriptions.keySet()) 
        {
            DocumentGenerator generator = new DocumentGenerator(ap.input_document,
                                    ap.getSections(),
                                    ap.getOutputPath());
            generator.generateDocument(job_descriptions.get(job_id), job_id);
        }
    }

    public static HashMap<String, String> getJobDescriptions(ArgParser ap)
    {
        String source = ap.document_source;
        JobDescriptionSource jd_source = null;

        if(source.equals("seek"))
            jd_source = new JobDescriptionFromSeekJob(ap.seek_url);
        else if(source.equals("file"))
            jd_source = new JobDescriptionFromFile(ap.compare_document_path);
        else if(source.equals("seek_saved"))
            jd_source = new JobDescriptionFromSaved();

//TODO: Do we care about SRP here?
        HashMap<String, String> job_descriptions = new HashMap<String, String>();
        ArrayList<InputJob> jobs = jd_source.getJobDescriptions();
        for (InputJob job : jobs) {
            job_descriptions.put(job.name, job.description);
        }

        return job_descriptions;
    }
}
