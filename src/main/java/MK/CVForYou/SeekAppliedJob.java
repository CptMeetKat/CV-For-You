package MK.CVForYou;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeekAppliedJob
{
    public String job_id;
    public String job_title;
    public ArrayList<String> status;
    public boolean active;
    public String company_name;
    public String company_id;

    public SeekAppliedJob(JSONObject node)
    {
        job_id = node.optString("id");

        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job != null)
        {
            job_title = job.optString("title");
        }

        active = node.optBoolean("isActive");

        JSONObject advertiser = (JSONObject) job.optQuery("/advertiser");
        if(advertiser != null)
        {
            company_name = advertiser.optString("name");
            company_id = advertiser.optString("id");
        }

        status = new ArrayList<String>(3);


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
            }
        }

    }
}

