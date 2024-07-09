package MK.CVForYou;

import java.util.ArrayList;

public class DocumentGenerator
{
    String document;
    ArrayList<DynamicSection> sections = new ArrayList<DynamicSection>();

    public DocumentGenerator(String document_path, String[] section_file_paths)
    {
        document = IOUtils.readFile(document_path);
        for(String path : section_file_paths)
        {
           sections.add(new DynamicSection(path));
        }
    }

    public void run()
    {
        for(DynamicSection section : sections)
        {
            System.out.println(section.getSectionName());
            String section_marker = "{$" + section.getSectionName() + "}";

            document = document.replace(section_marker, section.compose());
        }

        System.out.println("\n\n\n\n" + document);
        String out_path = "generated_document.html";
        boolean success = IOUtils.writeToFile(document, out_path);
        if(success)
            System.out.printf("Document has been generated at: %s\n", out_path);

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

}

