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
        public SeekAppliedJobWrapperMock(String toReturn)
        {

        }

		@Override
		public ArrayList<SeekAppliedJob> getAppliedJobsStats() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'getAppliedJobsStats'");
		}
    }


    class SeekAppliedJobInsightsWrapperMock implements SeekAppliedJobInsightsSource
    {
        public SeekAppliedJobInsightsWrapperMock(String toReturn)
        {

        }

		@Override
		public SeekAppliedJobInsights getInsights() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'getInsights'");
		}

		@Override
		public void setTargetJob(String job_id) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'setTargetJob'");
		}
    }
    
    @Test
    public void shouldGenerateNewCSVFromMockedData()
    {
        String[] args = {"--seek-stats", "-a"};
        App app = new App(args);
        Application program = app.getApplication();
        //program.setDependency(service, serviceType);
        //program.setDependency(service, serviceType);
    }


    @Test
    public void shouldMergeDataWithAlreadyExistingCSV()
    {
    }
}
