package MK.CVForYou;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFromSeekJob implements JobSource
{
    String url;
    static final Logger logger = LoggerFactory.getLogger(JobFromSeekJob.class);

    public JobFromSeekJob(String url)
    {
        this.url = url;
    }

	@Override
	public ArrayList<InputJob> getJobModel() {
        ArrayList<InputJob> jobs = new ArrayList<>();
        SeekJobWrapper wrapper = new SeekJobWrapper(url, true);
        jobs.add(InputJobFactory.createWorkItem(wrapper));

        if(wrapper.getCacheStatus() == 2)
            logger.info("Obtainde job descriptions from cache");

        return jobs;
	}

}

