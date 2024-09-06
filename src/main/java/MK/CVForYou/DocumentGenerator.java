package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;


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

    private HashMap<String, ReplaceableKey> createMapOnKeysArray( ArrayList<ReplaceableKey> replaceable_keys )
    {
        HashMap<String, ReplaceableKey> map = new HashMap<String, ReplaceableKey>();

        for(ReplaceableKey key : replaceable_keys)
        {
            map.put(key.getSectionName(), key);
        }

        return map;
    }

    //TODO: Abstract and write tests 
    private String getPreferredValueToEvaluateSectionFrom(ReplaceableKey replaceable_key, InputJob job)
    {
        ArrayList<String> preferences = replaceable_key.getFields();

        for(String p : preferences) { 

            try {
				Field f1 = InputJob.class.getField(p);
				String value = (String)f1.get(job);
                if(value != null) {
                    logger.debug("The field '{}' will be used to generate section '{}'", f1.getName(), replaceable_key.getOriginalKey());
                    return value;
                }
			} 
            catch (NoSuchFieldException e) {
                logger.warn("The field '{}' can not be used to evaluate a dynamic section", p);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error(e.getMessage());
				e.printStackTrace();
			}
        }
        
        logger.warn("No field to evaluate section '{}' from", replaceable_key.getOriginalKey());
        return null;
    }

    public void generateDocument(InputJob model, String output_name)
    {
        HashMap<String, ReplaceableKey> replaceableKeys = createMapOnKeysArray(getKeysToReplace());
        
        for(DynamicSection section : sections)
        {
            ReplaceableKey key_to_replace = replaceableKeys.get(section.getSectionName());
            String evaluation_value = getPreferredValueToEvaluateSectionFrom(key_to_replace, model);
            Comparator<DynamicHTMLElement> sorter = new CosineSimilarityComparator(evaluation_value);
            section.sort(sorter);

            template = template.replace(key_to_replace.getOriginalKey(), section.compose());
        }

        String out_path = output_directory.resolve(output_name + ".html").toString(); 

        boolean success = IOUtils.writeToFile(template, out_path);
        if(success)
            logger.info("Document has been generated at: {}", out_path);
    }

}

