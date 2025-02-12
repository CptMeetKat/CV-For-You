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

public class SeekNotesUploadNoteRequest implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekNotesUploadNoteRequest.class);
    SeekSessionManager session_manager;

    String job_id;
    String note;

    public SeekNotesUploadNoteRequest(String job_id, String note) 
    {
        this.session_manager = SeekSessionManager.getManager();
        this.job_id = job_id;
        this.note = note;
    }

    public ArrayList<SeekResumesResponse> uploadNote()
    {
        session_manager.makeRequest(this); 
        return null; //TODO: this returns null????
    }

    public JSONObject deleteSeekResumes(String access_token) throws IOException, InterruptedException
    {
        logger.debug("Upload note to role {}: {}", job_id, note);
        String operation = "SetSavedJobNotes";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{  \"operationName\": \"" + operation + "\",  \"variables\": {    \"input\": {      \"id\": \"" + job_id + "\",      \"notes\": \"" + note + "\"    },    \"locale\": \"en-AU\"  },  \"query\": \"mutation SetSavedJobNotes($input: SetSavedJobNotesInput!, $locale: Locale) {\\n  setSavedJobNotes(input: $input) {\\n    ... on SetSavedJobNotesSuccess {\\n      id\\n      __typename\\n    }\\n    ... on SetSavedJobNotesFailure {\\n      errors {\\n        message(locale: $locale)\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            logger.trace(response.body());
            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return deleteSeekResumes(access_token);
	}
}
