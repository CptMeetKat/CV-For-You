package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public class SeekStatsAcceptanceTest 
{
    private static final String BASE_DIR = "./target/test-artefacts/SeekStatsAcceptanceTest/";
    private File testDirectory;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() {
        // Create a directory for all acceptance test method
        String methodName = testName.getMethodName();
        testDirectory = new File(BASE_DIR, methodName);
        if (!testDirectory.exists()) {
            testDirectory.mkdirs();
        }
    }

    class SeekAppliedJobWrapperMock implements SeekAppliedJobSource
    {
        ArrayList<SeekAppliedJob> toReturn;
        public SeekAppliedJobWrapperMock(ArrayList<SeekAppliedJob> toReturn) {
            this.toReturn = toReturn;
        }

		@Override
		public ArrayList<SeekAppliedJob> getAppliedJobsStats() {
            return toReturn;
		}
    }


    class SeekAppliedJobInsightsWrapperMock implements SeekAppliedJobInsightsSource
    {
        String job_id;
        SeekAppliedJobInsights toReturn;
        public SeekAppliedJobInsightsWrapperMock(SeekAppliedJobInsights toReturn)
        {
            this.toReturn = toReturn;
        }

		@Override
		public SeekAppliedJobInsights getInsights() {
            return toReturn;
		}

		@Override
		public void setTargetJob(String job_id) {}
    }
    
    @Test
    public void shouldGenerateNewCSVFromMockedData() throws IOException
    {
        File file = new File(testDirectory.toString(), "data.csv");
        String[] args = {"--seek-stats", "-a", "-o", file.toString()};
        App app = new App(args, false);
        Application program = app.getApplication();

        ArrayList<SeekAppliedJob> applied_jobs = new ArrayList<>();
        SeekAppliedJobWrapperMock applied_jobs_mock = new SeekAppliedJobWrapperMock(applied_jobs);
        SeekAppliedJob application = new SeekAppliedJob(); //TODO: End to end test is more valuable if I utilise the JSONObject constructor
        application.job_id = "1111";
        application.created_at = "2024-08-23T05:46:20.186Z";
        application.applied_at = "2024-08-23T10:58:11.322Z";
        applied_jobs.add(application);
	

        SeekAppliedJobInsights insights = new SeekAppliedJobInsights(); //TODO: End to end test is more valuable if I utilise the JSONObject constructor
        insights.applicant_count = 55;
        insights.applicants_with_cover_percentage = 54;
        insights.applicants_with_resume_percentage = 55;
        SeekAppliedJobInsightsWrapperMock insights_mock = new SeekAppliedJobInsightsWrapperMock(insights);
        
        program.setDependency(applied_jobs_mock, SeekAppliedJobSource.class);
        program.setDependency(insights_mock, SeekAppliedJobInsightsSource.class);

        program.run();

        String written = Files.readString(Paths.get(file.toString()));

        String expected = "'job_id','job_title','status','status_times','latest_status','latest_status_time','active','company_name','company_id','applied_at','created_at','applied_with_cv','applied_with_cover','isExternal','salary','applicant_count','applicants_with_resume_percentage','applicants_with_cover_percentage'\n'1111','','','','','','false','','','2024-08-23T10:58:11.322Z','2024-08-23T05:46:20.186Z','false','false','false','','55','55','54'";
        assertEquals(expected, written);
    }


    @Test
    public void shouldMergeDataWithAlreadyExistingCSV() throws IOException
    {
        String initial_csv = "'job_id','job_title','status','status_times','latest_status','latest_status_time','active','company_name','company_id','applied_at','created_at','applied_with_cv','applied_with_cover','isExternal','salary','applicant_count','applicants_with_resume_percentage','applicants_with_cover_percentage'\n'1111','','','','','','false','','','2024-08-23T10:58:11.322Z','2024-08-23T05:46:20.186Z','false','false','false','','55','55','54'";
        File file = new File(testDirectory.toString(), "data.csv");
        Files.writeString(Paths.get(file.toString()), initial_csv);


        String[] args = {"--seek-stats", "-a", "-o", file.toString()};
        App app = new App(args, false);
        Application program = app.getApplication();

        ArrayList<SeekAppliedJob> applied_jobs = new ArrayList<>();
        SeekAppliedJobWrapperMock applied_jobs_mock = new SeekAppliedJobWrapperMock(applied_jobs);
        SeekAppliedJob application = new SeekAppliedJob(); //TODO: End to end test is more valuable if I utilise the JSONObject constructor
        application.job_id = "1111";
        application.created_at = "2024-08-23T05:46:20.186Z";
        application.applied_at = "2024-08-23T10:58:11.322Z";
        applied_jobs.add(application);
	

        SeekAppliedJobInsights insights = new SeekAppliedJobInsights(); //TODO: End to end test is more valuable if I utilise the JSONObject constructor
        insights.applicant_count = 1;
        insights.applicants_with_cover_percentage = 3;
        insights.applicants_with_resume_percentage = 2;
        SeekAppliedJobInsightsWrapperMock insights_mock = new SeekAppliedJobInsightsWrapperMock(insights);
        
        program.setDependency(applied_jobs_mock, SeekAppliedJobSource.class);
        program.setDependency(insights_mock, SeekAppliedJobInsightsSource.class);

        program.run();

        String written = Files.readString(Paths.get(file.toString()));

        String expected = "'job_id','job_title','status','status_times','latest_status','latest_status_time','active','company_name','company_id','applied_at','created_at','applied_with_cv','applied_with_cover','isExternal','salary','applicant_count','applicants_with_resume_percentage','applicants_with_cover_percentage'\n'1111','','','','','','false','','','2024-08-23T10:58:11.322Z','2024-08-23T05:46:20.186Z','false','false','false','','1','2','3'";
        assertEquals(expected, written);

    }
}
