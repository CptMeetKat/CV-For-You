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


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.UUID;

public class SeekCVUploaderParamsWrapper implements Requestable
{
    SeekSessionManager session_manager;
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploaderParamsWrapper.class);

    public SeekCVUploaderParamsWrapper()
    {
        this.session_manager = SeekSessionManager.getManager();
    }

    public void getUploadParams()
    {
        JSONObject upload_params = session_manager.makeRequest(this);
        
        JSONObject document_upload_form_data = null;
        try {
            document_upload_form_data = upload_params.getJSONObject("data")
                .getJSONObject("viewer")
                .getJSONObject("documentUploadFormData");
        } catch (JSONException e) {
            logger.error("Unable to parse the parameters required to upload CV to seek: {}", e.getMessage());
        }

        if(document_upload_form_data == null) //TODO: clean this //TODO move all below this into one step on stack higher
            return;


        SeekDocumentUploadFormData params = new SeekDocumentUploadFormData(document_upload_form_data);
        try {
            
			uploadFile(params);
            SeekApplyProcessUploadedResume apply_process = new SeekApplyProcessUploadedResume(params.key);
            try {
                logger.info("Sleeping for 5 seconds....");
                Thread.sleep(5000); // Sleep for 1000 milliseconds (1 second)
                apply_process.run();
            } catch (InterruptedException e) {
                logger.info("Thread was interrupted");
            }
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

            logger.info("Fetching upload params...");
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(response.body());
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchDocumentUploadParams(access_token);
	}
    

    private static void writeTextFormField(DataOutputStream writer, String field_name, String value) throws IOException
    {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"; // Define a unique boundary
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        writer.writeBytes(twoHyphens + boundary + lineEnd);
        writer.writeBytes("Content-Disposition: form-data; name=\"" + field_name + "\"" + lineEnd);
        writer.writeBytes(lineEnd);
        writer.writeBytes(value + lineEnd);
    }

    public static void uploadFile(SeekDocumentUploadFormData form_data) throws IOException {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"; // Define a unique boundary
        String lineEnd = "\r\n";
        String twoHyphens = "--";

        URL url = new URL(form_data.link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream outputStream = connection.getOutputStream();
                DataOutputStream writer = new DataOutputStream(outputStream)) {

            writeTextFormField(writer, "key", form_data.key);
            writeTextFormField(writer, "X-Amz-Algorithm", form_data.x_amz_algorithm);
            writeTextFormField(writer, "X-Amz-Credential", form_data.x_amz_credential);
            writeTextFormField(writer, "X-Amz-Date", form_data.x_amz_date);
            writeTextFormField(writer, "X-Amz-Security-Token", form_data.x_amz_security_token);
            writeTextFormField(writer, "Policy", form_data.policy);
            writeTextFormField(writer, "X-Amz-Signature", form_data.x_amz_signature);
            writeTextFormField(writer, "x-amz-meta-filename", form_data.x_amz_meta_filename);
            writeTextFormField(writer, "x-amz-meta-candidateId", form_data.x_amz_meta_candidateid);

            
            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"example.pdf\"" + lineEnd);
            writer.writeBytes("Content-Type: application/pdf" + lineEnd);
            writer.writeBytes(lineEnd);

            File file = new File("example.pdf");
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }
            }
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
