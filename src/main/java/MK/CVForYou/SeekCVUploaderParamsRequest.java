package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.UUID;

public class SeekCVUploaderParamsRequest implements Requestable
{
    SeekSessionManager session_manager;
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploaderParamsRequest.class);

    public SeekCVUploaderParamsRequest()
    {
        this.session_manager = SeekSessionManager.getManager();
    }

    public SeekDocumentUploadFormData getUploadParams()
    {
        JSONObject upload_params = session_manager.makeRequest(this);
        
        SeekDocumentUploadFormData params = null;
        try {
            JSONObject document_upload_form_data = upload_params.getJSONObject("data")
                .getJSONObject("viewer")
                .getJSONObject("documentUploadFormData");
            params = new SeekDocumentUploadFormData(document_upload_form_data);
        } catch (JSONException e) {
            logger.error("Unable to parse the parameters required to upload CV to seek: {}", e.getMessage());
        }

        return params;
    }

    public JSONObject fetchDocumentUploadParams(String access_token) throws IOException, InterruptedException
    {
        UUID uuid = UUID.randomUUID();
        
        String body = "{\"operationName\":\"GetDocumentUploadData\",\"variables\":{\"id\":\"" + uuid.toString() + "\"},\"query\":\"query GetDocumentUploadData($id: UUID\u0021) {\\n  viewer {\\n    documentUploadFormData(id: $id) {\\n      link\\n      key\\n      formFields {\\n        key\\n        value\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}";
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString(body))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchDocumentUploadParams(access_token);
	}
}
