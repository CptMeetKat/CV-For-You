package MK.CVForYou;

import org.json.JSONObject;

public class SeekAppliedJob
{
    public String job_id;
    public String job_title;
    public String status;
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

        //TODO: status
        
    }
}

