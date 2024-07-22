package MK.CVForYou;

import org.json.JSONObject;

public class SeekSavedJob
{
    String job_id;
    //isActive
    //notes
    //isExternal
    //title
    //location
    //abstract
    //recruiter
    //salary
    //
    public SeekSavedJob(JSONObject node)
    {
        job_id = (String) node.get("id");
        //System.out.println(job_id);
        //System.out.println((boolean)node.get("isActive"));
        
        ///System.out.println((boolean) null);
        //isActive = node.get(
    }

    public void printMe()
    {

    }


    public String getID()
    {
        return job_id;
    }
}

