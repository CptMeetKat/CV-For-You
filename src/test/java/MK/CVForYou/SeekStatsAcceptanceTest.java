package MK.CVForYou;

import java.io.File;

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

    //Should generate CSV from mock server
    //Should generate CSV from mock server and merge with historical

    @Test
    public void shouldGeneratePDFWithDynamicSectionsWhenSourceIsFile()
    {
        //String[] args = new String[]{
        //        "--seek-stats",
        //        "-a"
        //};
        //App.main(args);
    }
}
