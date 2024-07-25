package MK.CVForYou;

import java.util.ArrayList;

public class JobDescriptionFromSaved implements JobDescriptionSource
{

    public JobDescriptionFromSaved(){}

	@Override
	public ArrayList<InputJob> getJobDescriptions()
    {
        ArrayList<InputJob> jobs = new ArrayList<>();

        SeekSavedJobWrapper ssj = new SeekSavedJobWrapper();
        ArrayList<String> job_urls = ssj.getSavedJobURLs();

        for (String url : job_urls) {
            //e.g. url https://www.seek.com.au/job/77111111
            int idStart = url.lastIndexOf("/")+1; //Use whole URL can't extract id
            if(idStart == -1)
            {
                System.out.printf("Error: Bad URL can't process %s\n", url);
                continue;
            }
            String id = url.substring(idStart); 
            jobs.add(new InputJob(id, new SeekJobDescriptionWrapper(url).getJD()));
        }

        return jobs;
	}
}
