package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekCVUploaderApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploaderApplication.class);
    SeekSessionManager session_manager;
    public SeekCVUploaderApplication() 
    {
        this.session_manager = SeekSessionManager.getManager();
    }

	@Override
	public void run() {
        logger.info("Running Seek uploader...");
        new SeekCVUploaderParamsWrapper().getUploadParams(); 
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

    public void uploadFile(String file)
    {
    }
}
