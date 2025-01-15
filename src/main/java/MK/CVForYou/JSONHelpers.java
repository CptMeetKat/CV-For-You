
package MK.CVForYou;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelpers
{
    /**   
     * @return JSON field value if field is accessible, otherwise null if field cannot
     * be accessed or dosen't exist be accessed
     */
    public static String getStringInObject(JSONObject node, String sub_object, String field)
    { 
        String result = null;
        if(node != null)
            try {
                JSONObject json = node.getJSONObject(sub_object);
                result = json.getString(field);
            } catch (JSONException e) {} 
        return result;
    }

    public static String getString(JSONObject node, String field)
    { 
        String result = null;
        if(node != null)
            try {
                result = node.getString(field);
            } catch (JSONException e) {} 
        return result;
    }
}
