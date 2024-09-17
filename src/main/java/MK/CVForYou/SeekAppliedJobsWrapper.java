
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

public class SeekAppliedJobsWrapper implements Requestable
{

    static final Logger logger = LoggerFactory.getLogger(SeekAppliedJobsWrapper.class);
    SeekSessionManager session_manager;
    public SeekAppliedJobsWrapper()
    {
        this.session_manager = SeekSessionManager.getManager();
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return fetchAppliedJobs(access_token);
	}

    public JSONObject fetchAppliedJobs(String access_token) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.seek.com.au/graphql"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("authorization", "Bearer " + access_token)
            .header("content-type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\"operationName\":\"GetAppliedJobs\",\"variables\":{\"first\":100,\"locale\":\"en-AU\",\"timezone\":\"Australia/Sydney\",\"zone\":\"anz-1\",\"sortBy\":{\"applicationDate\":\"DESC\"}},\"query\":\"query GetAppliedJobs($first: Int, $locale: Locale!, $timezone: Timezone!, $zone: Zone!, $sortBy: AppliedJobSortByInput) {\\n  viewer {\\n    id\\n    appliedJobs(first: $first, locale: $locale, sortBy: $sortBy) {\\n      edges {\\n        node {\\n          id\\n          hasAppliedWithResume\\n          hasAppliedWithCoverLetter\\n          isExternal\\n          isActive\\n          notes\\n          events {\\n            status\\n            timestamp {\\n              dateTimeUtc\\n              shortAbsoluteLabel(timezone: $timezone, locale: $locale)\\n              __typename\\n            }\\n            __typename\\n          }\\n          appliedAt {\\n            dateTimeUtc\\n            shortAbsoluteLabel(timezone: $timezone, locale: $locale)\\n            __typename\\n          }\\n          job {\\n            id\\n            title\\n            location {\\n              label(locale: $locale, type: SHORT)\\n              __typename\\n            }\\n            abstract\\n            createdAt {\\n              dateTimeUtc\\n              label(context: JOB_POSTED, length: SHORT, timezone: $timezone, locale: $locale)\\n              __typename\\n            }\\n            advertiser {\\n              id\\n              name(locale: $locale)\\n              __typename\\n            }\\n            salary {\\n              label\\n              currencyLabel(zone: $zone)\\n              __typename\\n            }\\n            products {\\n              branding {\\n                logo {\\n                  url\\n                  __typename\\n                }\\n                __typename\\n              }\\n              __typename\\n            }\\n            tracking {\\n              hasRoleRequirements\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\"}"))
            .build(); 

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.headers()); 
            System.out.println(response.body()); 

            return new JSONObject(response.body());
    }

    public ArrayList<String> getAppliedJobsStats()
    {
        JSONObject applied_data = session_manager.makeRequest(this);
        
        ArrayList<SeekAppliedJob> applied_jobs = deserializeAppliedJobs(applied_data);

        for ( SeekAppliedJob job : applied_jobs)
        {
            System.out.println(job.job_title);
        }
		
        return null;
    }


    public static ArrayList<SeekAppliedJob> deserializeAppliedJobs(JSONObject object)
    {
        ArrayList<SeekAppliedJob> arr = new ArrayList<>();
        try
        {
            JSONArray saved_jobs = (JSONArray) object.query("/data/viewer/appliedJobs/edges");
            if(saved_jobs == null)
                throw new NullPointerException();

            Iterator<Object> job_itr = saved_jobs.iterator();
            while(job_itr.hasNext())
            {
                JSONObject job = (JSONObject)job_itr.next();
                arr.add(new SeekAppliedJob(job.getJSONObject("node")));
            }
        } catch (JSONPointerException e) {
            logger.error("Unable to deserialise AppliedJobs object");
        }
        catch(NullPointerException e) {
            logger.error("Null found when querying AppliedJobs");
        }

        return arr; 
    }


}
