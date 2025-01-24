package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumeDeleteRequest implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekResumeWrapper.class);
    SeekSessionManager session_manager;

    String resume_id;

    public SeekResumeDeleteRequest(String resume_id) 
    {
        this.session_manager = SeekSessionManager.getManager();
        this.resume_id = resume_id;
    }

    public ArrayList<SeekResumesResponse> deleteSeekResume()
    {
        session_manager.makeRequest(this);
        return null;
    }

    public JSONObject deleteSeekResumes(String access_token) throws IOException, InterruptedException
    {
        String operation = "DeleteResume";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"" + operation + "\",\"variables\":{\"resumeId\":{\"id\":\"" + resume_id + "\"},\"languageCode\":\"en\"},\"query\":\"mutation DeleteResume($resumeId: DeleteResumeInput\u0021, $languageCode: LanguageCodeIso\u0021) {\\n  deleteResume(input: $resumeId) {\\n    ref\\n    viewer {\\n      _id\\n      resumes(languageCode: $languageCode) {\\n        id\\n        createdDateUtc\\n        isDefault\\n        fileMetadata {\\n          name\\n          size\\n          virusScanStatus\\n          sensitiveDataInfo {\\n            isDetected\\n            __typename\\n          }\\n          uri\\n          __typename\\n        }\\n        origin {\\n          type\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return deleteSeekResumes(access_token);
	}
}
