package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobDescriptionFromFile implements JobDescriptionSource
{
    static final Logger logger = LoggerFactory.getLogger(JobDescriptionFromFile.class);
    Path filePath;
    public JobDescriptionFromFile(Path filePath)
    {
        this.filePath = filePath;
    }

	@Override
	public ArrayList<InputJob> getJobDescriptions() {
        ArrayList<InputJob> jobs = new ArrayList<>();
        jobs.add(  new InputJob("generated_document", getDocument(filePath))  ); //TODO: add post path slash to end of file name

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
            System.exit(1);
        }
        return document;
    }
}
