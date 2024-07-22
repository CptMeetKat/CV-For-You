package MK.CVForYou;

import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<String> job_descriptions = getJobDescriptions(ap);

        for (String jd : job_descriptions) 
        {
            DocumentGenerator generator = new DocumentGenerator(ap.input_document,
                                    ap.getSections(),
                                    ap.getOutput());
            generator.generateDocument(jd);
        }

    }

    public static ArrayList<String> getJobDescriptions(ArgParser ap)
    {
        ArrayList<String> job_descriptions = new ArrayList<String>();

        if(ap.document_source.equals("seek"))
        {
            job_descriptions.add( new SeekJobDescription(ap.seek_url).getJD() );
        }
        else if(ap.document_source.equals("file"))
        {
            job_descriptions.add( getDocument(ap.compare_document_path)   );
        }
        else if(ap.document_source.equals("seek_saved"))
        {
            SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
            ArrayList<String> job_urls = ssj.getSavedJobURLs();

            for (String url : job_urls)
            {
                job_descriptions.add( new SeekJobDescription(url).getJD() );
                sleep(); //Artificial delay to prevent flagging Seek
            }
            System.exit(0);
        }

        return job_descriptions;
    }

    public static void sleep()
    {
        try {
            Thread.sleep(5000); // Sleep for 3000 milliseconds (3 seconds)
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
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
