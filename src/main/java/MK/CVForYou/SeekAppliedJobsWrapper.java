
package MK.CVForYou;

import java.io.IOException;

import org.json.JSONObject;

public class SeekAppliedJobsWrapper implements Requestable
{
    public SeekAppliedJobsWrapper()
    {
        System.out.println("I am alive!");
    }

	@Override
	public JSONObject request(String access_token) throws IOException, InterruptedException {
        return getAppliedJobs(access_token);
	}

    public JSONObject getAppliedJobs(String access_token) throws IOException, InterruptedException {

            JSONObject result = null;
            return result;
    }
}
