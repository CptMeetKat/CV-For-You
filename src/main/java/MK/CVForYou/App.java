package MK.CVForYou;

import java.io.IOException;

public class App 
{
    public static void main( String[] args )
    {
        ArgParser ap = new ArgParser(args);

        if(ap.getRunType() == 1)
        {
            String document;
            if(ap.document_source.equals("seek"))
                document = new SeekWrapper(ap.seek_url).getJD();
            else
                document = getDocument(ap.compare_document_path);


            DocumentGenerator generator = new DocumentGenerator(ap.input_document,
                    ap.section_definition_paths,
                    document,
                    ap.generated_document_path);
            generator.generateDocument();
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
