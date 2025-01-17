
package MK.CVForYou;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class SeekUploadFileWrapper
{
    static final String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW"; // Define a unique boundary TODO: may be appropriate to change between uploads
    static final String lineEnd = "\r\n";
    static final String twoHyphens = "--";
    private static void writeTextFormField(DataOutputStream writer, String field_name, String value) throws IOException
    {
        writer.writeBytes(twoHyphens + boundary + lineEnd);
        writer.writeBytes("Content-Disposition: form-data; name=\"" + field_name + "\"" + lineEnd);
        writer.writeBytes(lineEnd);
        writer.writeBytes(value + lineEnd);
    }

    public static int uploadFile(SeekDocumentUploadFormData form_data, File file) throws IOException {

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
                    writer.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + lineEnd);
                    writer.writeBytes("Content-Type: application/pdf" + lineEnd);
                    writer.writeBytes(lineEnd);
        
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

        return connection.getResponseCode();
    }
}
