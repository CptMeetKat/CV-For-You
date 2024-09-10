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
        jobs.add(InputJobFactory.createWorkItem(wrapper));
        return jobs;
	}

}

