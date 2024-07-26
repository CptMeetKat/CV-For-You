package MK.CVForYou;

import java.util.ArrayList;

public class JobDescriptionFromSaved implements JobDescriptionSource
{

    public JobDescriptionFromSaved(){}

	@Override
	public ArrayList<InputJob> getJobDescriptions()
    {
        ArrayList<InputJob> jobs = new ArrayList<>();

        SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
        ArrayList<String> job_urls = ssj.getSavedJobURLs();

        for (String url : job_urls) {
            SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url);
            String job_description = wrapper.getJD();
            String id = wrapper.getSeekJobID();
            if(id != null)
                jobs.add(new InputJob(id, job_description));
        }

        return jobs;
	}
}
