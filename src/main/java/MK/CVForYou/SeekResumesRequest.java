package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesRequest implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekResumesRequest.class);
    SeekSessionManager session_manager;

    public SeekResumesRequest() 
    {
        this.session_manager = SeekSessionManager.getManager();
    }

    public ArrayList<SeekResume> getSeekResumes()
    {//TODO: should throw if unobtainable

        JSONObject response = session_manager.makeRequest(this); //TODO: this could return null and crash
        JSONArray resumes = (JSONArray) response.query("/data/viewer/resumes"); //TODO: this could return null and crash

        Iterator<Object> resume_itr = resumes.iterator();

        ArrayList<SeekResume> arr = new ArrayList<SeekResume>();
        while(resume_itr.hasNext())
        {
            JSONObject job = (JSONObject)resume_itr.next();
            arr.add(new SeekResume(job));
        }

        return arr;
    }

    public JSONObject fetchSeekResumes(String access_token) throws IOException, InterruptedException
    {
        String operation = "GetResumes";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"" + operation + "\",\"variables\":{\"languageCode\":\"en\"},\"query\":\"query GetResumes($languageCode: LanguageCodeIso\u0021) {\\n  viewer {\\n    _id\\n    resumes(languageCode: $languageCode) {\\n      id\\n      createdDateUtc\\n      isDefault\\n      fileMetadata {\\n        name\\n        size\\n        virusScanStatus\\n        sensitiveDataInfo {\\n          isDetected\\n          __typename\\n        }\\n        uri\\n        __typename\\n      }\\n      origin {\\n        type\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 

            logger.info("Fetching seek resumes...");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("({} {} {}) {}", response.request().method(), response.uri(), operation, response.statusCode());

            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchSeekResumes(access_token);
	}
}
