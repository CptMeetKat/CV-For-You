package MK.CVForYou;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFromSeekSaved implements JobSource
{
    static final Logger logger = LoggerFactory.getLogger(JobFromSeekSaved.class);

    public JobFromSeekSaved(){}

	@Override
	public ArrayList<InputJob> getJobModel()
    {
        ArrayList<InputJob> jobs = new ArrayList<>();

        SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
        ArrayList<String> job_urls = ssj.getSavedJobURLs();
        
        int total_cached = 0;
        for (String url : job_urls) {
            SeekJobWrapper wrapper = new SeekJobWrapper(url, true);
            jobs.add(InputJobFactory.createWorkItem(wrapper));
            if(wrapper.getCacheStatus() == 2)
                total_cached++;
        }

        logger.info("{} job descriptions obtained from cache", total_cached);

        return jobs;
	}
}
