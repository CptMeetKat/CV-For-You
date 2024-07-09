package MK.CVForYou;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Iterator;



public class DynamicSection
{
    ArrayList<DynamicHTMLElement> dynamic_options;

    public DynamicSection(String section_path)
    {
        String elements = IOUtils.readFile(section_path);
        JSONObject object  = stringToJSON(elements);
        dynamic_options = deserializeDynamicHTMLElements(object);
        for(DynamicHTMLElement elt : dynamic_options)
            System.out.printf("%s %s\n", elt.keywords, elt.html);
    }

    private JSONObject stringToJSON(String data)
    {
        JSONArray array = new JSONArray(data);
        System.out.println("Values array: "+ array);

        JSONArray list = listNumberArray(array.length());
        System.out.println("Label Array: "+ list.toString());

        JSONObject object = array.toJSONObject(list);
        System.out.println("Final JSONOBject: " + object);
            
        return object;
    }

    private static JSONArray listNumberArray(int max){
    	JSONArray res = new JSONArray();
    	for (int i=0; i<max;i++) {
    		//The value of the labels must be an String in order to make it work
    		res.put(String.valueOf(i));
    	}
    	return res;
    }

    private static ArrayList<DynamicHTMLElement> deserializeDynamicHTMLElements(JSONObject object)
    {
        ArrayList<DynamicHTMLElement> arr = new ArrayList<>();
        Iterator<String> keys = object.keys();
        
        while(keys.hasNext())
        {
            String key = keys.next();
            JSONObject element = object.getJSONObject(key);

            String keywords = element.get("keywords").toString();
            String html = element.get("element").toString();
            
            arr.add(new DynamicHTMLElement(keywords, html));
        }

        return arr;
    }
}
