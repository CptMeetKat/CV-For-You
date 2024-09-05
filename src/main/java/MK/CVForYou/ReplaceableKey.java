
package MK.CVForYou;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceableKey
{
    String section_name;
    ArrayList<String> fields = new ArrayList<String>();

    public ReplaceableKey(String key)
    {
        extractFields(key);
        extractSectionName(key);
        if(fields.isEmpty())
            fields.add("job_description");
    }

    public String getSectionName()
    {
        return section_name;
    }

    public ArrayList<String> getFields()
    {
        return fields;
    }

    private void extractSectionName(String key)
    {
        String result = key.replaceAll("{}$", "");
        result = result.replaceAll("\\(.*?\\)", "");
        section_name = result.trim();
    }

    private void extractFields(String key)
    {
        String regex = "\\(.*?\\)"; //e.g. (job_description, title)"

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);
        
        if(matcher.find())
        {
            String segment = matcher.group();
            segment = segment.substring(1, segment.length() - 1); //Remove first and last char
            String[] atoms = segment.split(",");
            
            for (String s : atoms) {
                fields.add(s.trim());
            }
        }
    }

}
