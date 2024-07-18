package MK.CVForYou;

public class App 
{
    public static void main( String[] args )
    {
        ArgParser ap = new ArgParser();
        ap.parseArgs(args);

        String input_document = ap.input_document; 
        String[] section_definition_paths = ap.section_definition_paths;
        String document = ap.document; 
        String generated_document_path = ap.generated_document_path;


        int run = ap.getRunType();
        if(run == 1)
        {
            DocumentGenerator generator = new DocumentGenerator(input_document,
                    section_definition_paths,
                    document,
                    generated_document_path);
            generator.generateDocument();
        }
    }




}
