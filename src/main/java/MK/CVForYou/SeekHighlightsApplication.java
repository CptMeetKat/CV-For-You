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

        logger.info("Writing role highlights to SEEK");

        int skipped = 0;
        int uploaded = 0;

        jobs = getJobsMap();

        JobFromSeekSaved seek = new JobFromSeekSaved();
        ArrayList<InputJob> saved_jobs = seek.getJobModel();
        for(InputJob job : saved_jobs)
        {
            String highlight = createHighlight(job.job_description);

            if(!roleContainsNotes(job.name) && !highlight.isBlank())
            {
                writeNoteToRole(job.name, highlight);
                uploaded++;
            }
            else
                skipped++;
        }

        logger.info("***Highlights Summary***\n{} highlights skipped\n{} highlights uploaded",
                                                String.format("%4d", skipped),
                                                String.format("%4d", uploaded));
	}

    private String createHighlight(String text)
    {
        List<Integer> position = getMatches(text, "year");
        List<Integer> line_breaks_positions = getMatches(text, "\n");

        StringBuilder sb = new StringBuilder(); 
        for(Integer i : position) {
            int left = firstPositionBefore(i, line_breaks_positions, 50);
            int right = firstPositionAfter(i, line_breaks_positions, 50, text.length());

            String note = text.substring(left+1,right);
            if(note.length() > 0)
                sb.append(note + "\\n");
        }

        return sb.toString();
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
        logger.info("Writing note to role {}", job_id);
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

