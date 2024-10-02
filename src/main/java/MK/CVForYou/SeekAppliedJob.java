package MK.CVForYou;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//TODO: add tests

public class SeekAppliedJob
{
    public String job_id;
    public String job_title;
    public ArrayList<String> status;
    public ArrayList<String> status_times;
    public String latest_status;
    public String latest_status_time;
    public boolean active;
    public String company_name;
    public String company_id;
    public String applied_at;
    public String created_at;
    public boolean applied_with_cv;
    public boolean applied_with_cover;
    public boolean isExternal;
    public String salary;

    public SeekAppliedJob(){}

    public void init()
    {
        status = new ArrayList<String>(3);
        status_times = new ArrayList<String>(3);
    }
    

    /**   
     * @return JSON field value if field is accessible, otherwise null if field cannot
     * be accessed or dosen't exist be accessed
     */
    private static String getStringInObject(JSONObject node, String sub_object, String field)
    { 
        String result = null;
        if(node != null)
            try {
                JSONObject json = node.getJSONObject(sub_object);
                result = json.getString(field);
            } catch (JSONException e) {} 
        return result;
    }


    private static String getString(JSONObject node, String field)
    { 
        String result = null;
        if(node != null)
            try {
                result = node.getString(field);
            } catch (JSONException e) {} 
        return result;
    }

    public SeekAppliedJob(String json)
    {
        this(new JSONObject(json));
    }

    public SeekAppliedJob(JSONObject node)
    {
        init();
        job_id = node.optString("id");
        active = node.optBoolean("isActive");
        isExternal = node.optBoolean("isExternal");
        applied_with_cv = node.optBoolean("hasAppliedWithResume");
        applied_with_cover = node.optBoolean("hasAppliedWithCoverLetter");


        JSONObject job = node.optJSONObject("job");
        job_title = getString(job, "title");

        company_name = getStringInObject(job, "advertiser", "name");
        company_id = getStringInObject(job, "advertiser", "id");
        created_at = getStringInObject(job, "createdAt", "dateTimeUtc");
        salary = getStringInObject(job, "salary", "label");


        JSONArray events = (JSONArray) node.query("/events");
        if(events != null)
        {
            Iterator<Object> events_itr = events.iterator();
            while(events_itr.hasNext())
            {
                JSONObject event = (JSONObject)events_itr.next();
                String status_value = event.optString("status");
                if(status_value != null)
                    status.add( status_value  );


                JSONObject timestamps = (JSONObject) event.query("/timestamp");
                if(timestamps != null)
                    status_times.add(timestamps.optString("dateTimeUtc"));
            }
            setLatestStatus();
        }
        applied_at = getStringInObject(node, "appliedAt", "dateTimeUtc");
    }

    private void setLatestStatus()
    {
        latest_status = status.get(status.size() - 1);
        latest_status_time = status_times.get(status.size() - 1);
    }

    public String getLastestStatus()
    {
        if(status.size() > 0)
            return status.get(status.size()-1);
        return null;
    }
}
