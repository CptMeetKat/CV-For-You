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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekSavedJobWrapper implements Requestable
{
    static final Logger logger = LoggerFactory.getLogger(SeekSavedJobWrapper.class);

    String timezone = "Australia/Sydney";

    SeekSessionManager session_manager;


    public SeekSavedJobWrapper()
    {
        session_manager = SeekSessionManager.getManager();
    }

    private static String buildJobUrl(String id)
    {
        return "https://www.seek.com.au/job/" + id;
    }

    public ArrayList<String> getSavedJobURLs()
    {
        ArrayList<String> job_urls = new ArrayList<String>();

        JSONObject jobs_data = session_manager.makeRequest(this);

        ArrayList<SeekSavedJob> saved_jobs = deserializeSavedJobs(jobs_data);
        for ( SeekSavedJob job : saved_jobs)
            job_urls.add( buildJobUrl(job.job_id));
		
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
            logger.error("Unable to deserialise SavedJobs object");
        }
        catch(NullPointerException e) {
            logger.error("Null found when querying SavedJobs");
        }

        return arr; 
    }

    public JSONObject request(String access_token)
        throws IOException, InterruptedException
    {
        return fetchSavedJobs(access_token);
    }

    public JSONObject fetchSavedJobs(String access_token) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"GetSavedJobs\",\"variables\":{\"first\":100,\"locale\":\"en-AU\",\"timezone\":\"" + timezone + "\",\"zone\":\"anz-1\"},\"query\":\"query GetSavedJobs($first: Int, $locale: Locale\\u0021, $timezone: Timezone\\u0021, $zone: Zone\\u0021) {\\n  viewer {\\n    id\\n    savedJobs(first: $first) {\\n      edges {\\n        node {\\n          id\\n          isActive\\n          notes\\n          isExternal\\n          createdAt {\\n            dateTimeUtc\\n            shortAbsoluteLabel(timezone: $timezone, locale: $locale)\\n            __typename\\n          }\\n          job {\\n            id\\n            title\\n            location {\\n              label(locale: $locale, type: SHORT)\\n              __typename\\n            }\\n            abstract\\n            createdAt {\\n              dateTimeUtc\\n              label(context: JOB_POSTED, length: SHORT, timezone: $timezone, locale: $locale)\\n              __typename\\n            }\\n            advertiser {\\n              id\\n              name(locale: $locale)\\n              __typename\\n            }\\n            salary {\\n              label\\n              currencyLabel(zone: $zone)\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 


            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.headers()); 
            //System.out.println();
            //System.out.println(response.body()); 
            //logger.trace("Printing SavedJobsAsJson...."); //Where is this printing what?

            return new JSONObject(response.body());
    }
}
