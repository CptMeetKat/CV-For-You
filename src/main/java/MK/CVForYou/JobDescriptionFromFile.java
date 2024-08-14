package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class JobDescriptionFromFile implements JobDescriptionSource
{
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
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return document;
    }
}
