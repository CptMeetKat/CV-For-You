package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DocumentGenerator
{
    static final Logger logger = LoggerFactory.getLogger(DocumentGenerator.class);
    String template;
    ArrayList<DynamicSection> sections = new ArrayList<DynamicSection>();
    Path output_directory = Paths.get("./");

    public DocumentGenerator(Path template_file, Path[] component_paths,
                             Path output_directory )
    {
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

    private ArrayList<ReplaceableKey> getKeysToReplace()
    {
        ArrayList<ReplaceableKey> substitutes = new ArrayList<ReplaceableKey>();
        String regex = "\\{\\$.*?\\}"; //e.g. 1. "{$title(job_title)}", "{$job_description}"

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);

        String log_string = "";

        while (matcher.find()) {
            log_string += matcher.group() + " ";
            substitutes.add(new ReplaceableKey(matcher.group()));
        }

        logger.debug("Replaceable sections detected: {}", log_string);

        return substitutes;
    }

    public void generateDocument(InputJob model, String output_name)
    {
        ArrayList<ReplaceableKey> replaceableKeys = getKeysToReplace();
//        for(ReplaceableKey key : replaceableKeys)
//        {
//        }

        //If model has the field as NON-NULL value use that filed for the comparator

        Comparator<DynamicHTMLElement> sorter = new CosineSimilarityComparator(model.job_description);

        for(DynamicSection section : sections)
        {
            //if SECTION???
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

