package MK.CVForYou;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeekSavedJobWrapper
{
    String secret = "";
    String timezone;

    public SeekSavedJobWrapper()
    {
        secret = getBearerTokenFromFile("auth");
    }

    private static String buildJobUrl(String id)
    {
        return "https://www.seek.com.au/job/" + id;
    }

    public void checkResponseForError(JSONObject jobs_data)
        throws BadAuthenticationException
    {
        try {
            jobs_data.get("errors");
            throw new BadAuthenticationException("Unable to retrieve 'Saved Jobs' from Seek");
        } catch (JSONException e) {
        }
    }
    
    public ArrayList<String> getSavedJobURLs()
    {
        ArrayList<String> job_urls = new ArrayList<String>();

		try
        {
			JSONObject jobs_data = getSavedJobsAsJson();
            checkResponseForError(jobs_data);

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
    

    public ArrayList<SeekSavedJob> deserializeSavedJobs(JSONObject object)
    {
        ArrayList<SeekSavedJob> arr = new ArrayList<>();

        JSONArray saved_jobs = (JSONArray) object.query("/data/viewer/savedJobs/edges");
        Iterator<Object> job_itr = saved_jobs.iterator();
        while(job_itr.hasNext())
        {
            JSONObject job = (JSONObject)job_itr.next();
            arr.add(new SeekSavedJob(job.getJSONObject("node")));
        }

        return arr; 
    }

    public JSONObject getSavedJobsAsJson() throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", secret)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"GetSavedJobs\",\"variables\":{\"first\":100,\"locale\":\"en-AU\",\"timezone\":\"Australia/Sydney\",\"zone\":\"anz-1\"},\"query\":\"query GetSavedJobs($first: Int, $locale: Locale\\u0021, $timezone: Timezone\\u0021, $zone: Zone\\u0021) {\\n  viewer {\\n    id\\n    savedJobs(first: $first) {\\n      edges {\\n        node {\\n          id\\n          isActive\\n          notes\\n          isExternal\\n          createdAt {\\n            dateTimeUtc\\n            shortAbsoluteLabel(timezone: $timezone, locale: $locale)\\n            __typename\\n          }\\n          job {\\n            id\\n            title\\n            location {\\n              label(locale: $locale, type: SHORT)\\n              __typename\\n            }\\n            abstract\\n            createdAt {\\n              dateTimeUtc\\n              label(context: JOB_POSTED, length: SHORT, timezone: $timezone, locale: $locale)\\n              __typename\\n            }\\n            advertiser {\\n              id\\n              name(locale: $locale)\\n              __typename\\n            }\\n            salary {\\n              label\\n              currencyLabel(zone: $zone)\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build();


            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.headers()); 
            //System.out.println();
            //System.out.println(response.body()); 

            return new JSONObject(response.body());
    }

    private String getBearerTokenFromFile(String file)
    {
        String token = null;
        try {
			token = IOUtils.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
            System.err.println("Could not get auth token from file");
		}

        return token;
    }

}
