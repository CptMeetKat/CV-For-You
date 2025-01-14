package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekCVUploader implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploader.class);
    SeekSessionManager session_manager;
    public SeekCVUploader() 
    {
        this.session_manager = SeekSessionManager.getManager();
        getUploadParams();
    }


    public void uploadFile(String file)
    {
    }

    public SeekDocumentUploadFormData getUploadParams()
    {
        JSONObject upload_params = session_manager.makeRequest(this);
        System.out.println(upload_params);
        //Fail if 403?
        return new SeekDocumentUploadFormData();
        //return new SeekDocumentUploadFormData(upload_params);
    }

    public JSONObject fetchDocumentUploadParams(String access_token) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("[{\"operationName\":\"GetDocumentUploadData\",\"variables\":{\"id\":\"245c61b3-9ea9-4013-bd28-929c7542b520\"},\"query\":\"query GetDocumentUploadData($id: UUID\u0021) {\\n  viewer {\\n    documentUploadFormData(id: $id) {\\n      link\\n      key\\n      formFields {\\n        key\\n        value\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}]"))
            .build(); 

            logger.info("Fetching upload params...");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            return new JSONObject("{'body':" + response.body() + "}"); //Force array in a JSONObject... bit awkward
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchDocumentUploadParams(access_token);
	}
}
