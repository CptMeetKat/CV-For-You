package MK.CVForYou;

import java.util.ArrayList;
import java.util.Comparator;

public class DocumentGenerator
{
    String document;
    ArrayList<DynamicSection> sections = new ArrayList<DynamicSection>();
    String compare_text;


    public DocumentGenerator(String document_path, String[] section_file_paths,
                             String compare_document_path)
    {
        compare_text = IOUtils.readFile(compare_document_path);
        document = IOUtils.readFile(document_path);
        for(String path : section_file_paths)
        {
           sections.add(new DynamicSection(path));
        }
    }

    public void run()
    {
        Comparator<DynamicHTMLElement> sorter = new CosineSimilarityComparator(compare_text);

        for(DynamicSection section : sections)
        {
            section.sort(sorter);

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

