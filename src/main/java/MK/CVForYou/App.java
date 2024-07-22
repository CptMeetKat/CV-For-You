package MK.CVForYou;

import java.io.IOException;
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
                                    ap.getOutput());
            generator.generateDocument(job_descriptions.get(job_id), job_id);
        }

    }

    public static HashMap<String, String> getJobDescriptions(ArgParser ap)
    {
        HashMap<String, String> job_descriptions = new HashMap<String, String>();

        if(ap.document_source.equals("seek"))
        {
            job_descriptions.put(ap.seek_url, new SeekJobDescription(ap.seek_url).getJD() );
        }
        else if(ap.document_source.equals("file"))
        {
            job_descriptions.put(ap.seek_url, getDocument(ap.compare_document_path)   );
        }
        else if(ap.document_source.equals("seek_saved"))
        {
            SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
            ArrayList<String> job_urls = ssj.getSavedJobURLs();

            for (String url : job_urls)
            {
                String id = url.substring(url.lastIndexOf("/")+1);
                job_descriptions.put(id, new SeekJobDescription(url).getJD() );
            }
        }

        return job_descriptions;
    }

    public static String getDocument(String document_path)
    {
        String document = null;
        try
        {
            document = IOUtils.readFile(document_path);
        }
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return document;
    }
}
