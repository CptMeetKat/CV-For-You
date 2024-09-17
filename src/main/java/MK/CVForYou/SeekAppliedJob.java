package MK.CVForYou;

import org.json.JSONObject;

public class SeekAppliedJob
{
    public String job_id;
    public String job_title;
    public String status;
    public boolean active;
    public String company;

    public SeekAppliedJob(JSONObject node)
    {

        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job == null)
            return;
        String title = job.optString("title");
        //System.out.printf("Title: %s", title);

        job_title = title;
    }
}

