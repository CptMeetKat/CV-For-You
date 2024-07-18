package MK.CVForYou;

import java.io.IOException;
import org.apache.commons.cli.ParseException;

public class App 
{
    public static void main( String[] args )
    {
		try {
			ArgParser ap = new ArgParser(args);
            String job_description = getJobDescription(ap);

            DocumentGenerator generator = new DocumentGenerator(ap.input_document,
                    ap.getSections(),
                    job_description,
                    ap.getOutput());
            generator.generateDocument();

		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    public static String getJobDescription(ArgParser ap)
    {
        String job_description;
            if(ap.document_source.equals("seek"))
                job_description = new SeekWrapper(ap.seek_url).getJD();
            else
                job_description = getDocument(ap.compare_document_path);
        return job_description;
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
