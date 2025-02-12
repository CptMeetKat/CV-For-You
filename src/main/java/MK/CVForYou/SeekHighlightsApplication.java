package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekHighlightsApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekHighlightsApplication.class);
    
    private HashMap<String, SeekSavedJob> jobs;

	@Override
	public void run() {
        jobs = getJobsMap();

        JobFromSeekSaved seek = new JobFromSeekSaved();
        ArrayList<InputJob> saved_jobs = seek.getJobModel();
        for(InputJob job : saved_jobs)
        {
            List<Integer> position = getMatches(job.job_description, "year");
            List<Integer> line_breaks_positions = getMatches(job.job_description, "\n");

            StringBuilder sb = new StringBuilder(); 
            for(Integer i : position) {
                int left = firstPositionBefore(i, line_breaks_positions, 50);
                int right = firstPositionAfter(i, line_breaks_positions, 50, job.job_description.length());

                String note = job.job_description.substring(left+1,right);
                if(note.length() > 0)
                    sb.append(note + "\\n");
            }
            
            
            if(!roleContainsNotes(job.name) && sb.length() > 0)
                writeNoteToRole(job.name, sb.toString());
        }
	}

    private HashMap<String, SeekSavedJob> getJobsMap()
    {
        HashMap<String, SeekSavedJob> map = new HashMap<>();
        SeekSavedJobWrapper wrapper = new SeekSavedJobWrapper();
        ArrayList<SeekSavedJob> jobs = wrapper.getSavedJobs();
        for(SeekSavedJob job : jobs)
        {
            map.put(job.job_id, job);
        }
        return map;
    }

    private boolean roleContainsNotes(String role_id)
    {
        SeekSavedJob job = jobs.get(role_id);
        return job != null && job.notes != null && !job.notes.equals("");
    }
    
    private void writeNoteToRole(String job_id, String note)
    {
        logger.info("Writing note to role {}\n", job_id);
        SeekNotesUploadNoteRequest request = new SeekNotesUploadNoteRequest(job_id, note);
        request.uploadNote();
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

