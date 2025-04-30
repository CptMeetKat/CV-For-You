package MK.CVForYou;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import MK.CVForYou.CVModules.GitHubPullRequests;

public class CVGeneratorApplication implements Application
{
    JobSource job_source;
    Path input_document;
    Path[] sections;
    Path output_folder;

    CVGenerationArgs args;
    DocumentGenerator generator;

    public CVGeneratorApplication(CVGenerationArgs args)
    {
        job_source = args.getJDSource();
        input_document = args.getInputDocument();
        sections = args.getSections();
        output_folder = args.getOutputFolder();
        this.args = args;
        
        generator = new DocumentGenerator(input_document, sections, output_folder);
    }

    public CVGenerationArgs getCVGenerationArgs()
    {
        return args;
    }


    public void setDocumentStage(GitHubPullRequests pr)
    {
        generator.addStage(pr);
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
            generator.generateDocument(job_descriptions.get(job_id), job_id);
            ExecuteChromePDFGenerator.run(job_id, output_folder);
        }
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

}
