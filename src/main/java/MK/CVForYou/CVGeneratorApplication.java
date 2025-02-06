package MK.CVForYou;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class CVGeneratorApplication implements Application
{
    JobSource job_source;
    Path input_document;
    Path[] sections;
    Path output_folder;

    CVGenerationArgs args;

    public CVGeneratorApplication(CVGenerationArgs ap) //TODO: ap?
    {
        job_source = ap.getJDSource();
        input_document = ap.getInputDocument();
        sections = ap.getSections();
        output_folder = ap.getOutputFolder();
        
        args = ap;
    }

    public CVGenerationArgs getCVGenerationArgs()
    {
        return args;
    }

    public static HashMap<String, InputJob> getJobs(JobSource jd_source)
    {
        HashMap<String, InputJob> job_descriptions = new HashMap<String, InputJob>();
        ArrayList<InputJob> jobs = jd_source.getJobModel(); 
        for (InputJob job : jobs) {
            if(job.name != null)
                job_descriptions.put(job.name, job);
        }

        return job_descriptions;
    }

	@Override
	public void run() {
        HashMap<String, InputJob> job_descriptions = getJobs(job_source);

        for (String job_id: job_descriptions.keySet()) 
        {
            DocumentGenerator generator = new DocumentGenerator(input_document,
                                    sections,
                                    output_folder);
            generator.generateDocument(job_descriptions.get(job_id), job_id);
            ExecuteChromePDFGenerator.run(job_id, output_folder);
        }
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

}
