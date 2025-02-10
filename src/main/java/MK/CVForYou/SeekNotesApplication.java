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
        this.note = args.note;
    }

	@Override
	public void run() {
        if(mode == 1)
            uploadNote();
	}

    public void uploadNote()
    {
        //new SeekNotesUploadNote(job_id, note);
        logger.warn("Unimplemented: Write note");
    }

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

