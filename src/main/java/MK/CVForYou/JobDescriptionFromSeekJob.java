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

        SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url, true);
        String job_description = wrapper.getJobDescription();
        String id = wrapper.getSeekJobID();
        if(id != null)
            jobs.add(  new InputJob(id, job_description));

        return jobs;
	}

}

