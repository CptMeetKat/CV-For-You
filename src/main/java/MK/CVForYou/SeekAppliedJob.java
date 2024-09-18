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
    public String applied_at;
    public String created_at;
    
    

    public SeekAppliedJob(JSONObject node)
    {
        job_id = node.optString("id");
        active = node.optBoolean("isActive");

        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job != null)
        {
            job_title = job.optString("title");
            JSONObject advertiser = (JSONObject) job.optQuery("/advertiser");
            if(advertiser != null)
            {
                company_name = advertiser.optString("name");
                company_id = advertiser.optString("id");
            }

            JSONObject createdJSON = (JSONObject) job.optQuery("/createdAt");
            if(createdJSON != null)
            {
                created_at = createdJSON.optString("dateTimeUtc");
            }
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

        JSONObject appliedJSON = (JSONObject) node.optQuery("/appliedAt");
        if(appliedJSON != null)
        {
            applied_at = appliedJSON.optString("dateTimeUtc");
        }
    }


    public String getLastestStatus()
    {
        if(status.size() > 0)
            return status.get(status.size()-1);
        return null;
    }

    public String toCSV()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("'%s',", job_id));
        sb.append(String.format("'%s',", job_title));
        sb.append(String.format("'%s',", active));
        sb.append(String.format("'%s',", company_name));
        sb.append(String.format("'%s',", company_id));
        sb.append(String.format("'%s',", getLastestStatus()));
        sb.append(String.format("'%s',", applied_at));
        sb.append(String.format("'%s',", created_at));
        sb.append("\n");

        return sb.toString();
    }

}
