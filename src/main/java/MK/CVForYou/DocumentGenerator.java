package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentGenerator
{
    static final Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);
    String template;
    ArrayList<DynamicSection> sections = new ArrayList<DynamicSection>();
    Path output_directory = Paths.get("./");

    public DocumentGenerator(Path template_file, Path[] component_paths,
                             Path output_directory )
    {
        //this.model_text = model_text;
        if(output_directory != null)
            this.output_directory = output_directory;

        try {
            template = IOUtils.readFile(template_file.toString());
            for(Path path : component_paths)
               sections.add(new DynamicSection(path.toString()));
        }
        catch(IOException e) {
            logger.error(e.getMessage());
        }
    }

    
    public void generateDocument(String model_text)
    {
        generateDocument(model_text, "generated_document.html");
    }

    public void generateDocument(String model_text, String output_name)
    {
        Comparator<DynamicHTMLElement> sorter = new CosineSimilarityComparator(model_text);

        for(DynamicSection section : sections)
        {
            section.sort(sorter);

            //System.out.println(section.getSectionName());
            String section_marker = "{$" + section.getSectionName() + "}";

            template = template.replace(section_marker, section.compose());
        }

        String out_path = output_directory.resolve(output_name + ".html").toString(); 

        boolean success = IOUtils.writeToFile(template, out_path);
        if(success)
            logger.info("Document has been generated at: {}", out_path);
    }

}

