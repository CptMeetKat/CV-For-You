package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobFromFile implements JobSource
{
    static final Logger logger = LoggerFactory.getLogger(JobFromFile.class);
    Path filePath;
    public JobFromFile(Path filePath)
    {
        this.filePath = filePath;
    }

	@Override
	public ArrayList<InputJob> getJobModel() {
        ArrayList<InputJob> jobs = new ArrayList<>();
        try {
            InputJob work_item = new InputJob();
            work_item.name = "generated_document";
            work_item.job_description = getDocument(filePath);
            jobs.add(work_item);
            
        } catch (IOException e) {
            logger.error("Unable to process Job from file {}", e.getMessage());
        }

        return jobs;
	}

    public static String getDocument(Path document_path)
        throws IOException
    {
        String document = null;
        document = IOUtils.readFile(document_path.toString());
        return document;
    }
}
