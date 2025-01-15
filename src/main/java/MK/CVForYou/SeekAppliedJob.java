package MK.CVForYou;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
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
    
    public SeekAppliedJob(String json)
    {
        this(new JSONObject(json));
    }

    private void parseEvents(JSONArray events)
    {
        if(events != null)
        {
            Iterator<Object> events_itr = events.iterator();
            while(events_itr.hasNext())
            {
                JSONObject event = (JSONObject)events_itr.next();

                status.add(JSONHelpers.getString(event, "status"));
                status_times.add(JSONHelpers.getStringInObject(event, "timestamp", "dateTimeUtc"));
            }
            updateLatestStatus();
        }
    }

    public SeekAppliedJob(JSONObject node)
    {
        init();
        job_id = JSONHelpers.getString(node, "id");
        active = node.optBoolean("isActive");
        isExternal = node.optBoolean("isExternal");
        applied_with_cv = node.optBoolean("hasAppliedWithResume");
        applied_with_cover = node.optBoolean("hasAppliedWithCoverLetter");


        JSONObject job = node.optJSONObject("job");
        job_title = JSONHelpers.getString(job, "title");

        company_name = JSONHelpers.getStringInObject(job, "advertiser", "name");
        company_id = JSONHelpers.getStringInObject(job, "advertiser", "id");
        created_at = JSONHelpers.getStringInObject(job, "createdAt", "dateTimeUtc");
        salary = JSONHelpers.getStringInObject(job, "salary", "label");

        applied_at = JSONHelpers.getStringInObject(node, "appliedAt", "dateTimeUtc");

        JSONArray events = (JSONArray) node.query("/events");
        parseEvents(events);
    }

    private void updateLatestStatus()
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
