package MK.CVForYou;

import org.json.JSONObject;

public class SeekSavedJob
{
    public String job_id = "";
    public Boolean isActive = null; //it would be nice if this was never null
    public String notes = "";
    public Boolean isExternal = null; //it would be nice if this was never null
    public String title = "";
    public String location = "";
    public String job_abstract = "";
    public String company = "";
    public String salary = "";

    public SeekSavedJob(JSONObject node)
    {
        job_id = node.optString("id");
        isActive =  node.optBooleanObject("isActive", null);
        notes = node.optString("notes");
        isExternal =  node.optBooleanObject("isExternal", null);


        JSONObject job = (JSONObject) node.optQuery("/job");
        if(job == null)
            return;
        title = job.optString("title");
        job_abstract = job.optString("abstract");
        


        JSONObject location_object = (JSONObject) job.optQuery("/location");
        if(location_object != null)
            location = location_object.optString("label");

        JSONObject advertiser = (JSONObject) job.optJSONObject("advertiser");
        if(advertiser == null)
            return; //TODO: This is valid but weird
        company = advertiser.optString("name");


        JSONObject salaryJson = job.optJSONObject("salary");
        if (salaryJson != null) {
            salary = salaryJson.optString("label");
        }
    }
}

