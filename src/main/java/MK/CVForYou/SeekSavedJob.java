package MK.CVForYou;

import org.json.JSONObject;

public class SeekSavedJob
{
    String job_id;
    Boolean isActive;
    String notes;
    Boolean isExternal;
    String title;
    String location;

    String job_abstract;
    String company;
    String salary;
    public SeekSavedJob(JSONObject node)
    {
        job_id = node.optString("id");
        isActive =  node.optBooleanObject("isActive", null);
        notes = node.optString(notes);
        isExternal =  node.optBooleanObject("isExternal", null);


        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job == null)
            return;
        title = job.optString("title");
        location = job.optString("location");
        job_abstract = job.optString("abstract");

        JSONObject advertiser = (JSONObject) job.optJSONObject("advertiser");
        if(advertiser == null)
            return;
        company = advertiser.optString("name");


        JSONObject salaryJson = job.optJSONObject("salary");
        if (salaryJson != null) {
            salary = salaryJson.optString("label");
        }
    }

    public String getID()
    {
        return job_id;
    }

}

