package MK.CVForYou;

import java.util.ArrayList;

public class JobDescriptionFromSeekJob implements JobDescriptionSource
{
    String url;

    public JobDescriptionFromSeekJob(String url)
    {
        this.url = url;
    }

	@Override
	public ArrayList<InputJob> getJobModel() {
        
        ArrayList<InputJob> jobs = new ArrayList<>();

        //TODO: This section of code is duplicate with other JobDescriptionSource...
        SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url, true);

        String id = wrapper.getSeekJobID();

        InputJob work_item = new InputJob();
        work_item.name = id;
        work_item.job_description = wrapper.getJobDescription();
        work_item.job_title = wrapper.getJobTitle();
        if(id != null) //TODO: Can I check this downstream instead?
            jobs.add(work_item);

        return jobs;
	}

}

