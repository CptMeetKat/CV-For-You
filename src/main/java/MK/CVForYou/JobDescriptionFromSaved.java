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
            SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url, true); //TODO: This code is duplicate with other JobDescriptionSource

            InputJob work_item = new InputJob();
            work_item.name = wrapper.getSeekJobID();
            work_item.job_description = wrapper.getJobDescription();
            work_item.job_title = wrapper.getJobTitle();
            jobs.add(work_item);
        }

        return jobs;
	}
}
