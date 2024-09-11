package MK.CVForYou;

import java.util.ArrayList;

public class JobFromSeekSaved implements JobSource
{

    public JobFromSeekSaved(){}

	@Override
	public ArrayList<InputJob> getJobModel()
    {
        ArrayList<InputJob> jobs = new ArrayList<>();

        SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
        ArrayList<String> job_urls = ssj.getSavedJobURLs();

        for (String url : job_urls) {
            SeekJobWrapper wrapper = new SeekJobWrapper(url, true);
            jobs.add(InputJobFactory.createWorkItem(wrapper));
        }

        return jobs;
	}
}
