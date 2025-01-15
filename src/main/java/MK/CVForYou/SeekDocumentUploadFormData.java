
package MK.CVForYou;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class SeekDocumentUploadFormData
{
    public String link;
    public String key;
    public String x_amz_algorithm;
    public String x_amz_credential;
    public String x_amz_date;
    public String x_amz_security_token;
    public String policy;
    public String x_amz_signature;
    public String x_amz_meta_filename;
    public String x_amz_meta_candidateid;

    public SeekDocumentUploadFormData(JSONObject json)
    {
        link = JSONHelpers.getString(json, "link");
        JSONArray form_data = json.getJSONArray("formFields");

        Iterator<Object> form_itr = form_data.iterator();
        while(form_itr.hasNext())
        {
            JSONObject item = (JSONObject)form_itr.next();

            String key = item.getString("key");
            String value = item.getString("value");
            setField(key,value);
        }

        System.out.println(this);
    }
    
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();

        result.append("link: " + link + "\n");
        result.append("key: " + key + "\n");
        result.append("x_amz_algorithm: " + x_amz_algorithm + "\n");
        result.append("x_amz_credential: " + x_amz_credential + "\n");
        result.append("x_amz_date: " + x_amz_date + "\n");
        result.append("x_amz_security_token: " + x_amz_security_token + "\n");
        result.append("policy: " + policy + "\n");
        result.append("x_amz_signature: " + x_amz_signature + "\n");
        result.append("x_amz_meta_filename: " + x_amz_meta_filename + "\n");
        result.append("x_amz_meta_candidateid: " + x_amz_meta_candidateid + "\n");

        return result.toString();
    }

    public void setField(String key, String value)
    {
        if( key.equalsIgnoreCase("link") )
            link = value;
        else if( key.equalsIgnoreCase("key") )
            this.key = value;
        else if( key.equalsIgnoreCase("x-amz-algorithm") )
            x_amz_algorithm = value;
        else if( key.equalsIgnoreCase("x-amz-credential") )
            x_amz_credential = value;
        else if( key.equalsIgnoreCase("x-amz-date") )
            x_amz_date = value;
        else if( key.equalsIgnoreCase("x-amz-security-token") )
            x_amz_security_token = value;
        else if( key.equalsIgnoreCase("policy") )
            policy = value;
        else if( key.equalsIgnoreCase("x-amz-signature") )
            x_amz_signature = value;
        else if( key.equalsIgnoreCase("x-amz-meta-filename") )
            x_amz_meta_filename = value;
        else if( key.equalsIgnoreCase("x-amz-meta-candidateid") )
            x_amz_meta_candidateid = value;
    }
}
