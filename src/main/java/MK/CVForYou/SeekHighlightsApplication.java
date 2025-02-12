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
            List<Integer> line_breaks_positions = getMatches(job.job_description, "\n");


            for(Integer i : position) {
                int left = firstPositionBefore(i, line_breaks_positions, 50);
                int right = firstPositionAfter(i, line_breaks_positions, 50, job.job_description.length());
                System.out.println(job.job_description.substring(left+1,right));
            }
            System.out.println();
        }
	}

    private int firstPositionBefore(int target, List<Integer> positions, int min)
    {
        int result = 0;
        for(int i : positions)
        {
            if(i < target)
                result = i;
            else
                break;
        }
        result = Math.max(result, target-min);
        return result;
    }

    private int firstPositionAfter(int target, List<Integer> positions, int max, int size)
    {
        int result = size;
        for(int i = positions.size()-1; i >= 0; i--)
        {
            if(positions.get(i) > target)
                result = positions.get(i);
            else
                break;
        }

        result = Math.min(target+max, result);
        return result;
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

