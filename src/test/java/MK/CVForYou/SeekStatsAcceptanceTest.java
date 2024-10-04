package MK.CVForYou;

import java.io.File;
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
    public void shouldGenerateNewCSVFromMockedData()
    {
        String[] args = {"--seek-stats", "-a"};
        App app = new App(args);
        Application program = app.getApplication();


        ArrayList<SeekAppliedJob> applied_jobs = new ArrayList<>();
        SeekAppliedJobWrapperMock applied_jobs_mock = new SeekAppliedJobWrapperMock(applied_jobs);

        SeekAppliedJobInsights insights = new SeekAppliedJobInsights("{}");
        SeekAppliedJobInsightsWrapperMock insights_mock = new SeekAppliedJobInsightsWrapperMock(insights);
        
        program.setDependency(applied_jobs_mock, SeekAppliedJobSource.class);
        program.setDependency(insights_mock, SeekAppliedJobInsightsSource.class);

        //program.run();
        //TODO// VERIFY GENERATED CSV
    }


    @Test
    public void shouldMergeDataWithAlreadyExistingCSV()
    {
        String[] args = {"--seek-stats", "-a"};
        App app = new App(args);
        Application program = app.getApplication();


        ArrayList<SeekAppliedJob> applied_jobs = new ArrayList<>();
        SeekAppliedJobWrapperMock applied_jobs_mock = new SeekAppliedJobWrapperMock(applied_jobs);

        SeekAppliedJobInsights insights = new SeekAppliedJobInsights("{}");
        SeekAppliedJobInsightsWrapperMock insights_mock = new SeekAppliedJobInsightsWrapperMock(insights);
        
        program.setDependency(applied_jobs_mock, SeekAppliedJobSource.class);
        program.setDependency(insights_mock, SeekAppliedJobInsightsSource.class);

        //program.run();
        //TODO// VERIFY GENERATED CSV
    }
}
