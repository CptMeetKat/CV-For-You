package MK.CVForYou;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekHighlightsApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekHighlightsApplication.class);
    
    private HashMap<String, SeekSavedJob> jobs;
    private String highlight = "year";

	@Override
	public void run() {

        logger.info("Writing phrases that contain '{}' to SEEK job notes...", highlight);

        int skipped = 0;
        int uploaded = 0;

        jobs = getJobsMap();

        JobFromSeekSaved seek = new JobFromSeekSaved();
        ArrayList<InputJob> saved_jobs = seek.getJobModel();
        for(InputJob job : saved_jobs)
        {
            if(job.job_description == null)
            {
                logger.warn("Job contains no job description: {} {}", job.name, job.job_title);
                continue;
            }

            String highlight = HighlightSequence.createHighlight("year", job.job_description);

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

    private HashMap<String, SeekSavedJob> getJobsMap()
    {
        HashMap<String, SeekSavedJob> map = new HashMap<>();
        SeekSavedJobRequest wrapper = new SeekSavedJobRequest();
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
        SeekNotesUploadNoteRequest request = new SeekNotesUploadNoteRequest(job_id, note);
        request.uploadNote();
    }

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

