package MK.CVForYou;

import java.util.ArrayList;

public class JobDescriptionFromSaved implements JobDescriptionSource
{

    public JobDescriptionFromSaved(){}

	@Override
	public ArrayList<InputJob> getJobModel()
    {
        ArrayList<InputJob> jobs = new ArrayList<>();

        SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
        ArrayList<String> job_urls = ssj.getSavedJobURLs();

        for (String url : job_urls) {
            SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url, true);
            jobs.add(InputJobFactory.createWorkItem(wrapper));
        }

        return jobs;
	}
}
