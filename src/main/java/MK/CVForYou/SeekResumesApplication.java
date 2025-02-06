package MK.CVForYou;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekResumesApplication.class);
    SeekSessionManager session_manager;

    int mode;
    ArrayList<File> files;
    ArrayList<String> ids;
    boolean deleteAll;


    public SeekResumesApplication(SeekResumesArgs args) 
    {
        this.session_manager = SeekSessionManager.getManager();
        mode = args.getMode();
        files = args.files;
        ids = args.ids;
        deleteAll = args.deleteAll;
    }

	@Override
	public void run() {
        if(mode == 1)
            uploadFiles();
        else if(mode == 2)
            deleteCVs();
        else if(mode == 3)
            printUploadedResumes();
	}

    private HashSet<String> getExcluded()
    {
        return new HashSet<String>();//TODO: should read from whitelist.file
    }

    private ArrayList<String> fetchIDsToDelete()
    {
        ArrayList<SeekResumesResponse> seek_resumes = fetchAllResumes();
        HashSet<String> excluded = getExcluded();
        ArrayList<String> ids_to_delete = new ArrayList<>();
        for(SeekResumesResponse resume : seek_resumes)
        {
            if(!excluded.contains(resume.id))
                ids_to_delete.add(resume.id);
        }

        return ids_to_delete;
    }

    public void deleteCVs()
    {
        if(deleteAll)
        {
            ArrayList<String> ids_to_delete = fetchIDsToDelete();
            deleteByIds(ids_to_delete);
        }
        else
            deleteByIds(ids);
        printUploadedResumes();
    }

    private static void deleteByIds(ArrayList<String> ids)
    {
        for(String id : ids)
        {
            logger.info("Deleteing CV file '{}' from Seek", id);
            SeekResumeDeleteRequest request = new SeekResumeDeleteRequest(id);
            request.deleteSeekResume();
            Utils.sleep(3);
        }
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
                apply_process.run();
                printUploadedResumes();

            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            Utils.sleep(3);
        }
    }

    private void printUploadedResumes()
    {
        ArrayList<SeekResumesResponse> uploaded = fetchAllResumes();

        logger.info("{} Resumes on SEEK", uploaded.size());
        for(SeekResumesResponse resume : uploaded)
            logger.info("\t{} {} {} {}", String.format("%-20s", resume.name),
                    String.format("%-8s", resume.size),
                    resume.created,
                    String.format("%39s", resume.id));
    }

    private ArrayList<SeekResumesResponse> fetchAllResumes()
    {
        return new SeekResumeWrapper().getSeekResumes();
    }

}
