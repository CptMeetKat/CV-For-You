package MK.CVForYou;

import org.json.JSONObject;

public class SeekSavedJob
{
    String job_id;
    boolean isActive;
    String notes;
    boolean isExternal;
    String title;
    String location;

    String job_abstract;
    String company;
    String salary;
    public SeekSavedJob(JSONObject node)
    {
        job_id = node.optString("id");
        isActive = (boolean) node.get("isActive");
        notes = node.optString(notes);
        isExternal = (boolean) node.get("isExternal");


        JSONObject job = (JSONObject) node.query("/job");
        title = job.optString("title");
        location = job.optString("location");
        job_abstract = job.optString("abstract");

        JSONObject advertiser = (JSONObject) job.getJSONObject("advertiser");
        company = advertiser.optString("name");


        JSONObject salaryJson = job.optJSONObject("salary");
        if (salaryJson != null) {
            salary = salaryJson.optString("label", null);
        }



    }

    public String getID()
    {
        return job_id;
    }
}

