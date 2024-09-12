package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFromCache implements JobSource
{
    static final Logger logger = LoggerFactory.getLogger(JobFromCache.class);
    ArrayList<Path> paths;

    public JobFromCache(ArrayList<Path> paths)
    {
        this.paths = paths;
    }

	@Override
	public ArrayList<InputJob> getJobModel() {
        ArrayList<InputJob> jobs = new ArrayList<>();
        for(Path path : paths)
        {
            try {
                SeekJobParser parser = new SeekJobParser(path);
                InputJob work_item = new InputJob();

                work_item.name = path.getFileName().toString();
                work_item.job_title = parser.getJobTitle();
                work_item.job_description = parser.getJobDescription();

                jobs.add(work_item);
            } catch (IOException e) {
                logger.error("Unable to open cache file: {}", e.getMessage());
            }
        }
        return jobs;
	}

}

