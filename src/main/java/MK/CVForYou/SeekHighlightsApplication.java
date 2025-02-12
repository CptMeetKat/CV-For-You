package MK.CVForYou;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekHighlightsApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekHighlightsApplication.class);
	@Override
	public void run() {
        JobFromSeekSaved seek = new JobFromSeekSaved();
        ArrayList<InputJob> saved_jobs = seek.getJobModel();
        for(InputJob job : saved_jobs)
        {
            System.out.println(String.format("Title: %s (%s)", job.job_title, job.name)); //TODO: Works but inappropriate use of InputJob?
            List<Integer> position = getMatches(job.job_description, "years");


            for(Integer i : position) {
                printAroundMatch(job.job_description, i.intValue());
            }
            System.out.println();
        }
        logger.trace("highlighting");
	}


    private void printAroundMatch(String text, int position)
    {
        int left = Math.max(position-20, 0);
        int right = Math.min(position+25, text.length());

        String text_to_print = text.substring(left,right).replace("\n","");
        System.out.println(text_to_print);
    }


    private List<Integer> getMatches(String text, String pattern)
    {
        List<Integer> positions = new ArrayList<Integer>();
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(text);

        while (matcher.find()) {
            positions.add(matcher.start());
        }

        return positions;
    }

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

