
package MK.CVForYou;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekSessionManager
{
    static SeekSessionManager manager;
    
    static final Logger logger = LoggerFactory.getLogger(App.class);

    String JobseekerSessionId;
    String access_token;
    String refresh_token;
    String client_id;

    final String auth_file = "auth";

    private SeekSessionManager()
    {
        readAuthFromFile();
    }

    public static synchronized SeekSessionManager getManager()
    {
        if (manager == null)
            manager = new SeekSessionManager();
        return manager;
    }

    public JSONObject makeRequest(Requestable requestable)
    {
        JSONObject response = null;
        try
        {
            response = requestable.request(access_token);
            if( SeekSessionManager.responseHasAuthError(response)) 
            {
                logger.info("Access token invalid, trying to refresh token...");
                tryRefreshToken();
                response = requestable.request(access_token);
            }

            if( responseHasAuthError(response) )
            {
                logger.error(response.toString());
                throw new BadAuthenticationException("Unable to refresh access token, please refresh auth file"); 
            }
        }
        catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        catch (BadAuthenticationException e) {
            logger.error("Authentication token invalid");
        }

        return response;
    }


    private void tryRefreshToken()
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://login.seek.com/oauth/token"))
            .header("Content-Type", "application/json")
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("auth0-client", "eyJuYW1lIjoiYXV0aDAtc3BhLWpzIiwidmVyc2lvbiI6IjEuMjIuMyJ9") //This is hardcoded as {"name":"auth0-spa-js","version":"1.22.3"}
            .header("origin", "https://www.seek.com.au")
            .header("priority", "u=1, i")
            .header("referer", "https://www.seek.com.au/")
            //    .header("sec-ch-ua", ""Not)A;Brand";v="99", "Google Chrome";v="127", "Chromium";v="127"")
            .header("sec-ch-ua-mobile", "?0")
            //    .header("sec-ch-ua-platform", ""Linux"")
            .header("sec-fetch-dest", "empty")
            .header("sec-fetch-mode", "cors")
            .header("sec-fetch-site", "cross-site")
            .header("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"redirect_uri\":\"https://www.seek.com.au/oauth/callback/\",\"initial_scope\":\"openid profile email offline_access\",\"JobseekerSessionId\":\"" + JobseekerSessionId + "\",\"identity_sdk_version\":\"7.0.0\",\"refresh_href\":\"https://www.seek.com.au/my-activity/saved-jobs\",\"client_id\":\"" + client_id + "\",\"grant_type\":\"refresh_token\",\"refresh_token\":\"" + refresh_token + "\"}" ))
            .build();

        try {
            HttpResponse<String> response;
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            logger.debug(response.body());
            
            JSONObject auth = new JSONObject(response.body());
            String access_token = auth.optString("access_token");
            String refresh_token = auth.optString("refresh_token");
            if(!access_token.equals(""))
                setAccessToken(access_token);
            if(!refresh_token.equals(""))
                setRefreshToken(refresh_token);
            writeAuthToFile();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setAccessToken(String access_token)
    {
        this.access_token = access_token;
    }

    private void setRefreshToken(String refresh_token)
    {
        this.refresh_token = refresh_token;
    }

    private void writeAuthToFile()
    {
        JSONObject auth = new JSONObject();
        auth.put("access_token", access_token);
        auth.put("refresh_token", refresh_token);
        auth.put("JobseekerSessionId", JobseekerSessionId);
        auth.put("client_id", client_id);

        IOUtils.writeToFile(auth.toString(), auth_file);
    }

    public static boolean responseHasAuthError(JSONObject jobs_data)
    {
        //TODO: Limit to auth error, instead of all errors
        boolean hasError = jobs_data.has("errors");
        return hasError;
    }


    private void readAuthFromFile()
    {
        String file = auth_file;
        JSONObject auth = null;
        try {
            String data = IOUtils.readFile(file);
            auth = new JSONObject(data);
		} catch (IOException e) {
			e.printStackTrace();
            System.err.println("Could not get refresh params from file");
		}


        JobseekerSessionId = auth.optString("JobseekerSessionId");
        setAccessToken(auth.optString("access_token"));
        refresh_token = auth.optString("refresh_token");
        client_id = auth.optString("client_id");
    }
}
