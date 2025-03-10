
package MK.CVForYou;

import java.io.IOException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class SeekApplyResumeRequest implements Requestable
{
    SeekSessionManager session_manager;
    String uuid;
    static final Logger logger = LoggerFactory.getLogger(SeekApplyResumeRequest.class);

    public SeekApplyResumeRequest(String uuid)
    {
        this.session_manager = SeekSessionManager.getManager();
        this.uuid = uuid;
    }

    public JSONObject run() //TODO Return parsed response instead of JSONObject
    {
        return session_manager.makeRequest(this);
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        
        return processUploadedCV(access_token);
	}


    public JSONObject processUploadedCV(String access_token) throws IOException, InterruptedException
    {
        UUID parsing_context_uuid = UUID.randomUUID();
        String operation = "ApplyProcessUploadedResume";
        String body = "{\"operationName\":\"" + operation + "\",\"variables\":{\"input\":{\"id\":\"" + uuid + "\",\"isDefault\":false,\"parsingContext\":{\"id\":\"" + parsing_context_uuid + "\"},\"zone\":\"anz-1\"}},\"query\":\"mutation ApplyProcessUploadedResume($input: ProcessUploadedResumeInput\u0021) {\\n  processUploadedResume(input: $input) {\\n    resume {\\n      ...resume\\n      __typename\\n    }\\n    viewer {\\n      _id\\n      resumes {\\n        ...resume\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\\nfragment resume on Resume {\\n  id\\n  createdDateUtc\\n  isDefault\\n  fileMetadata {\\n    name\\n    size\\n    virusScanStatus\\n    sensitiveDataInfo {\\n      isDetected\\n      __typename\\n    }\\n    uri\\n    __typename\\n  }\\n  origin {\\n    type\\n    __typename\\n  }\\n  __typename\\n}\"}";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json") 
            .method("POST", HttpRequest.BodyPublishers.ofString(body))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug("({} {} {}) {}", response.request().method(), response.uri(), operation, response.statusCode());
            return new JSONObject(response.body());
    }
}
