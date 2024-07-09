package MK.CVForYou;

public class App 
{
    public static void main( String[] args )
    {
        String input_document = "sample_components/document.html";
        String[] section_definition_paths= new String[]{
            "sample_components/projects.json",
            "sample_components/tags.json"
        };
        String compare_document_path = "sample_components/description2.txt";

        DocumentGenerator generator = new DocumentGenerator(input_document, section_definition_paths, compare_document_path);
        generator.run();
    }


}
