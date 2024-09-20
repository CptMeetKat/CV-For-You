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
    
    public SeekAppliedJob(){}

    public SeekAppliedJob(JSONObject node)
    {
        job_id = node.optString("id");
        active = node.optBoolean("isActive");
        applied_with_cv = node.optBoolean("hasAppliedWithResume");
        applied_with_cover = node.optBoolean("hasAppliedWithCoverLetter");

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
        status_times = new ArrayList<String>(3);
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

        JSONObject appliedJSON = (JSONObject) node.optQuery("/appliedAt");
        if(appliedJSON != null)
        {
            applied_at = appliedJSON.optString("dateTimeUtc");
        }
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
