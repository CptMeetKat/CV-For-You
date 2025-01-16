package MK.CVForYou;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekCVUploaderApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekCVUploaderApplication.class);
    SeekSessionManager session_manager;

    int mode;
    ArrayList<File> files;


    public SeekCVUploaderApplication(CVUploaderArgs args) 
    {
        this.session_manager = SeekSessionManager.getManager();
        mode = args.getMode();
        files = args.files;
    }

	@Override
	public void run() {
        if(mode == 1)
            uploadFiles();
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

    public void uploadFiles()
    {
        for(File file : files)
        {
            logger.info("Uploading CV file '{}' to Seek", file.getName());

            SeekDocumentUploadFormData params = new SeekCVUploaderParamsWrapper().getUploadParams(); 
            try {
                SeekUploadFileWrapper.uploadFile(params, file);
                SeekApplyProcessUploadedResume apply_process = new SeekApplyProcessUploadedResume(params.key); //TODO: this class name is not great
                apply_process.run(); //TODO: List all uploaded and the recently uploaded file
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Utils.sleep(5);
        }
    }
}
