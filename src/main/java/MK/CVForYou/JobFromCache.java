package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class JobFromCache implements JobSource
{
    Path path;

    public JobFromCache(Path path)
    {
        this.path = path;
    }

	@Override
	public ArrayList<InputJob> getJobModel() {
        ArrayList<InputJob> jobs = new ArrayList<>();
        try {
			SeekJobParser parser = new SeekJobParser(path);
            InputJob work_item = new InputJob();

            work_item.name = path.getFileName().toString();
            work_item.job_title = parser.getJobTitle();
            work_item.job_description = parser.getJobDescription();

            jobs.add(work_item);
		} catch (IOException e) {
			e.printStackTrace(); //TODO: Add warning
		}
        return jobs;
	}

}

