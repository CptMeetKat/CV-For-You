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
            String id = url.substring(url.lastIndexOf("/")+1); //TODO: Unsafe -1 return value
            jobs.add(new InputJob(id, new SeekJobDescription(url).getJD()));
        }

        return jobs;
	}
}
