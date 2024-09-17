
package MK.CVForYou;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

public class SeekAppliedJobsWrapper implements Requestable
{
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

            JSONObject result = null;

            return result;
    }

    public ArrayList<String> getAppliedJobsStats()
    {
        session_manager.makeRequest(this);

        return null;
    }
}
