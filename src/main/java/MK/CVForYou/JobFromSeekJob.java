package MK.CVForYou;

import java.util.ArrayList;

public class JobFromSeekJob implements JobSource
{
    String url;

    public JobFromSeekJob(String url)
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

