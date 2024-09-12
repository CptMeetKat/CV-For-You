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
        InputJob work_item = new InputJob();
        work_item.name = "generated_document";
        work_item.job_description = getDocument(filePath);
        jobs.add(work_item);

        return jobs;
	}

    public static String getDocument(Path document_path)
    {
        String document = null;
        try {
            document = IOUtils.readFile(document_path.toString());
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(1); //TOOO: Can this be made more graceful
        }
        return document;
    }
}
