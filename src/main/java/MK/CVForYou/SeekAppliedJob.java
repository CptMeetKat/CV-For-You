package MK.CVForYou;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public boolean applied_with_cv;
    public boolean applied_with_cover;
    
    

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

    public static String toCSV(List<SeekAppliedJob> applied_jobs)
    {
        StringBuilder sb = new StringBuilder();
        String[] fields = {"job_id", "job_title", "active", "company_name", "company_id", "status", "applied_at", "created_at", "applied_with_cover", "applied_with_cv"}; 
        for(String f : fields)
            sb.append(String.format("'%s',", f));
        sb.append("\n");

        for (SeekAppliedJob job : applied_jobs) 
        {
            sb.append( csvFormat(job.job_id));
            sb.append( csvFormat(job.job_title));
            sb.append( csvFormat(job.active));
            sb.append( csvFormat(job.company_name));
            sb.append( csvFormat(job.company_id));
            sb.append( csvFormat(job.getLastestStatus()));
            sb.append( csvFormat(job.applied_at));
            sb.append( csvFormat(job.created_at));
            sb.append( csvFormat(job.applied_with_cover));
            sb.append( csvFormat(job.applied_with_cv));
            sb.append("\n");
        }

        return sb.toString();
    }

    private static String csvFormat(Object value)
    {
        return String.format("'%s',", value.toString());
    }

}
