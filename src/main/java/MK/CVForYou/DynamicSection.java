package MK.CVForYou;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;


public class DynamicSection
{
    int max_display;

    ArrayList<DynamicHTMLElement> dynamic_options;
    String file_name;
    

    public String compose()
    {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < dynamic_options.size() && i < max_display; i++)
        {
            DynamicHTMLElement elt = dynamic_options.get(i);
            result.append(elt.getHTML());
        }

        return result.toString();
    }

    public String getSectionName()
    {
        int extension_pos = file_name.lastIndexOf(".");
        if(extension_pos == -1)
            return file_name;

        return file_name.substring(0, extension_pos);
    }

    public void sort(Comparator<DynamicHTMLElement> sorter)
    {
        dynamic_options.sort(sorter);
    }

    public DynamicSection(String section_path)
        throws IOException
    {
        file_name = Paths.get(section_path).getFileName().toString();

        String elements = IOUtils.readFile(section_path); 
        JSONObject object  = new JSONObject(elements);
        deserializeDynamicHTMLElements(object);
    }

    private void deserializeDynamicHTMLElements(JSONObject object)
    {
        ArrayList<DynamicHTMLElement> arr = new ArrayList<>();

        String container = (String)object.get("container");
        
        max_display = object.optInt("max", Integer.MAX_VALUE);

        JSONArray options = (JSONArray) object.query("/options");
        Iterator<Object> options_itr = options.iterator();
        while(options_itr.hasNext())
        {
            JSONObject option = (JSONObject)options_itr.next();
                           
            String keywords = (String) option.get("keywords");
            String html = new String(container);
            
            JSONArray elements = option.getJSONArray("elements");
            for(int i = 0; i < elements.length(); i++)
            {
                String key = "{$elements[" + i + "]}";
                html = html.replace(key, elements.get(i).toString());
            }

            arr.add(new DynamicHTMLElement(keywords, html));
        }

        this.dynamic_options = arr;   
    }
}
