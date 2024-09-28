package MK.CVForYou;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeekAppliedJobInsights
{
    int applicant_count;
    int applicants_with_resume_percentage;
    int applicants_with_cover_percentage;

    public SeekAppliedJobInsights(JSONObject job_details) throws JSONException
    {
        JSONArray insights = (JSONArray) job_details.query("/insights");

        Iterator<Object> insights_itr = insights.iterator();
        System.out.println("Printing some stats....");
        while(insights_itr.hasNext())
        {
            JSONObject stat = (JSONObject)insights_itr.next();
            String type = stat.optString("__typename");
            if(type.equals("ApplicantCount"))
                applicant_count = stat.getInt("count");
            else if(type.equals("ApplicantsWithCoverLetterPercentage"))
                applicants_with_cover_percentage = stat.getInt("percentage"); 
            else if(type.equals("ApplicantsWithResumePercentage"))
                applicants_with_resume_percentage = stat.getInt("percentage");

        }
        System.out.printf("stat: %d %d %d\n", applicant_count, applicants_with_cover_percentage, applicants_with_resume_percentage);
    }
}
