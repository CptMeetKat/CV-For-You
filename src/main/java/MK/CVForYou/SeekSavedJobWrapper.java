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
import org.json.JSONPointerException;

public class SeekSavedJobWrapper
{
    String timezone;

    String JobseekerSessionId;
    String access_token;
    String refresh_token;
    String client_id;


    public SeekSavedJobWrapper()
    {
        readAuthFromFile();
    }

    private static String buildJobUrl(String id)
    {
        return "https://www.seek.com.au/job/" + id;
    }

    private static boolean responseHasAuthError(JSONObject jobs_data)
    {
        //TODO: Limit to auth error, instead of all errors
        boolean hasError = jobs_data.has("errors"); 
        return hasError;
    }
    

    public ArrayList<String> getSavedJobURLs()
    {
        ArrayList<String> job_urls = new ArrayList<String>();

		try
        {
			JSONObject jobs_data = getSavedJobsAsJson();

            if( responseHasAuthError(jobs_data)) 
            {
                tryRefreshToken();
                jobs_data = getSavedJobsAsJson();
            }

            if( responseHasAuthError(jobs_data) )
            {
                throw new BadAuthenticationException("Unable to refresh access token, please refresh auth file"); 
            }

            ArrayList<SeekSavedJob> saved_jobs = deserializeSavedJobs(jobs_data);
            for ( SeekSavedJob job : saved_jobs)
                job_urls.add( buildJobUrl(job.getID()));
		
        }
        catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        catch (BadAuthenticationException e) {
            System.out.println("ERROR: Authentication token invalid");
        }
        return job_urls;
    }
    

    public static ArrayList<SeekSavedJob> deserializeSavedJobs(JSONObject object)
    {
        ArrayList<SeekSavedJob> arr = new ArrayList<>();
        try
        {
            JSONArray saved_jobs = (JSONArray) object.query("/data/viewer/savedJobs/edges");
            if(saved_jobs == null)
                throw new NullPointerException();

            Iterator<Object> job_itr = saved_jobs.iterator();
            while(job_itr.hasNext())
            {
                JSONObject job = (JSONObject)job_itr.next();
                arr.add(new SeekSavedJob(job.getJSONObject("node")));
            }
        } catch (JSONPointerException e) {
            System.out.println("Error: Unable to deserialise SavedJobs object");
        }
        catch(NullPointerException e) {
            System.out.println("Error: Null found when querying SavedJobs");
        }

        return arr; 
    }

    public JSONObject getSavedJobsAsJson() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"GetSavedJobs\",\"variables\":{\"first\":100,\"locale\":\"en-AU\",\"timezone\":\"Australia/Sydney\",\"zone\":\"anz-1\"},\"query\":\"query GetSavedJobs($first: Int, $locale: Locale\\u0021, $timezone: Timezone\\u0021, $zone: Zone\\u0021) {\\n  viewer {\\n    id\\n    savedJobs(first: $first) {\\n      edges {\\n        node {\\n          id\\n          isActive\\n          notes\\n          isExternal\\n          createdAt {\\n            dateTimeUtc\\n            shortAbsoluteLabel(timezone: $timezone, locale: $locale)\\n            __typename\\n          }\\n          job {\\n            id\\n            title\\n            location {\\n              label(locale: $locale, type: SHORT)\\n              __typename\\n            }\\n            abstract\\n            createdAt {\\n              dateTimeUtc\\n              label(context: JOB_POSTED, length: SHORT, timezone: $timezone, locale: $locale)\\n              __typename\\n            }\\n            advertiser {\\n              id\\n              name(locale: $locale)\\n              __typename\\n            }\\n            salary {\\n              label\\n              currencyLabel(zone: $zone)\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build();


            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.headers()); 
            //System.out.println();
            //System.out.println(response.body()); 
            System.out.println("Printing: SavedJobsAsJson...."); 

            return new JSONObject(response.body());
    }

    private void readAuthFromFile()
    {
        String file = "auth2";
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
            System.out.println(response.body());
            
            JSONObject auth = new JSONObject(response.body());
            setAccessToken(auth.optString("access_token"));
            setRefreshToken(auth.optString("refresh_token"));
            writeAuthToFile();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setAccessToken(String access_token)
    {
        this.access_token = "Bearer " + access_token;
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

        IOUtils.writeToFile(auth.toString(), "auth2");
    }
}
