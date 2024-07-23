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
	public ArrayList<InputJob> getJobDescriptions() {
        
        ArrayList<InputJob> jobs = new ArrayList<>();

        String id = url.substring(url.lastIndexOf("/")+1); //TODO: Unsafe -1 return value
        jobs.add(  new InputJob(id, new SeekJobDescription(url).getJD()  )   );

        return jobs;
	}

}

