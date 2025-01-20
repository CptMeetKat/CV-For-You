package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumeWrapper implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekResumeWrapper.class);
    SeekSessionManager session_manager;

    public SeekResumeWrapper() 
    {
        this.session_manager = SeekSessionManager.getManager();
    }

    public ArrayList<SeekResumesResponse> getSeekResumes()
    {//TODO: should throw if unobtainable

        JSONObject response = session_manager.makeRequest(this);
        JSONArray resumes = (JSONArray) response.query("/data/viewer/resumes");

        Iterator<Object> resume_itr = resumes.iterator();

        ArrayList<SeekResumesResponse> arr = new ArrayList<SeekResumesResponse>();
        while(resume_itr.hasNext())
        {
            JSONObject job = (JSONObject)resume_itr.next();
            arr.add(new SeekResumesResponse(job));
        }

        return arr;
    }

    public JSONObject fetchSeekResumes(String access_token) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"GetResumes\",\"variables\":{\"languageCode\":\"en\"},\"query\":\"query GetResumes($languageCode: LanguageCodeIso\u0021) {\\n  viewer {\\n    _id\\n    resumes(languageCode: $languageCode) {\\n      id\\n      createdDateUtc\\n      isDefault\\n      fileMetadata {\\n        name\\n        size\\n        virusScanStatus\\n        sensitiveDataInfo {\\n          isDetected\\n          __typename\\n        }\\n        uri\\n        __typename\\n      }\\n      origin {\\n        type\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 

            logger.info("Fetching seek resumes...");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            logger.trace("({} {} {}) {}", response.request().method(), response.uri(), "GetResumes", response.statusCode());

            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchSeekResumes(access_token);
	}
}
