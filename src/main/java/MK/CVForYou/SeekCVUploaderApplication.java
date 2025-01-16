package MK.CVForYou;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekCVUploaderApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploaderApplication.class);
    SeekSessionManager session_manager;
    public SeekCVUploaderApplication(CVUploaderArgs args) 
    {
        this.session_manager = SeekSessionManager.getManager();
    }

	@Override
	public void run() { //TODO: Set the file to upload
        logger.info("Running Seek uploader...");
        uploadFile();
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

    public void uploadFile()
    {
        SeekDocumentUploadFormData params = new SeekCVUploaderParamsWrapper().getUploadParams(); 

        try {
			SeekUploadFileWrapper.uploadFile(params);
            SeekApplyProcessUploadedResume apply_process = new SeekApplyProcessUploadedResume(params.key); //TODO: this class name is not great
                apply_process.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
