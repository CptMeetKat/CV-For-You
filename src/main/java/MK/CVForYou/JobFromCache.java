package MK.CVForYou;

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
        SeekJobParser parser = new SeekJobParser(path);
        //SeekJobWrapper wrapper = new SeekJobWrapper(url, true);
        //jobs.add(InputJobFactory.createWorkItem(wrapper));
        return jobs;
	}

}

