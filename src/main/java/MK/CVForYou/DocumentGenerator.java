package MK.CVForYou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class DocumentGenerator
{
    String document;
    ArrayList<DynamicSection> sections = new ArrayList<DynamicSection>();
    String compare_text;
    String output_directory = "";

    public DocumentGenerator(String document_path, String[] section_file_paths,
                             String compare_text, String output_directory )
    {
        this.compare_text = compare_text;
        if(output_directory != null)
            this.output_directory = output_directory;

        try {
            document = IOUtils.readFile(document_path);
            for(String path : section_file_paths)
               sections.add(new DynamicSection(path));
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void generateDocument()
    {
        Comparator<DynamicHTMLElement> sorter = new CosineSimilarityComparator(compare_text);

        for(DynamicSection section : sections)
        {
            section.sort(sorter);

            //System.out.println(section.getSectionName());
            String section_marker = "{$" + section.getSectionName() + "}";

            document = document.replace(section_marker, section.compose());
        }

        String out_path = output_directory + "generated_document.html"; //TODO: / or no / ending case
        boolean success = IOUtils.writeToFile(document, out_path);
        if(success)
            System.out.printf("Document has been generated at: %s\n", out_path);

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

}

