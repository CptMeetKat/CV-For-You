package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekNotesApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekNotesApplication.class);
    SeekSessionManager session_manager;

    int mode;
    String job_id;
    String note;
    

    public SeekNotesApplication(SeekNotesArgs args) 
    {
        this.session_manager = SeekSessionManager.getManager();
        mode = args.getMode();
        this.job_id = args.job_id;
        this.note = args.getNote();
    }

	@Override
	public void run() {
        if(mode == 1)
            uploadNote();
	}

    public void uploadNote()
    {
        SeekNotesUploadNoteRequest request = new SeekNotesUploadNoteRequest(job_id, note);
        request.uploadNote();

    }

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

