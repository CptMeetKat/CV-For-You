package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekAppliedJobInsightsWrapper implements Requestable, SeekAppliedJobInsightsSource
{
    static final Logger logger = LoggerFactory.getLogger(SeekAppliedJobInsightsWrapper.class);
    SeekSessionManager session_manager;
    
    String job_id;

    public SeekAppliedJobInsightsWrapper()
    {
        this.session_manager = SeekSessionManager.getManager();
    }

    public void setTargetJob(String job_id)
    {
        this.job_id = job_id;
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchInsights(access_token);
	}

    public SeekAppliedJobInsights getInsights()
    {
        JSONObject data = session_manager.makeRequest(this);
        
        JSONObject insights_node = (JSONObject) data.query("/data/jobDetails");
        SeekAppliedJobInsights insights = new SeekAppliedJobInsights(insights_node);

        return insights;
    }

    public JSONObject fetchInsights(String access_token) throws IOException, InterruptedException 
    {
        String body = "{\"operationName\":\"GetAppliedJobDetail\",\"variables\":{\"jobId\":\"" + job_id + "\",\"locale\":\"en-AU\"},\"query\":\"query GetAppliedJobDetail($jobId: ID!, $locale: Locale!) {\\n viewer {\\n id\\n appliedJob(jobId: $jobId) {\\n id\\n documents {\\n resume {\\n attachmentMetadata {\\n fileName\\n __typename\\n }\\n __typename\\n }\\n coverLetter {\\n attachmentMetadata {\\n fileName\\n __typename\\n }\\n __typename\\n }\\n __typename\\n }\\n __typename\\n }\\n __typename\\n  }\\n  jobDetails(id: $jobId) {\\n insights {\\n ... on ApplicantCount {\\n __typename\\n count\\n message(locale: $locale)\\n }\\n ... on ApplicantsWithResumePercentage {\\n __typename\\n percentage\\n message(locale: $locale)\\n }\\n ... on ApplicantsWithCoverLetterPercentage {\\n __typename\\n percentage\\n message(locale: $locale)\\n }\\n __typename\\n }\\n __typename\\n  }\\n}\"}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json") 
            .method("POST", HttpRequest.BodyPublishers.ofString(body))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            Utils.sleep(1); //Avoid flagging SEEK systems

            return new JSONObject(response.body());
    }
}
