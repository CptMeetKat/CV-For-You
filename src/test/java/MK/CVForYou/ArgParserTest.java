package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ArgParserTest 
{
    @Test
    public void shouldNotHaveMoreThanOneCompareSources()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-cs", "https://www.seek.com.au/job/00000001",
                                     "-c", "compare_file.txt",
                                     "-s", "section1.json", "section2.json"
                                     };

        ArgParser ap = new ArgParser();
        boolean result = ap.parseArgs(args);
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenMissingCompareFlag()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-s", "section1.json", "section2.json",
                                     };

        ArgParser ap = new ArgParser();
        boolean result = ap.parseArgs(args);
        assertFalse(result);
    }


    @Test
    public void shouldReturnFalseWhenMissingDocumentFlag()
    {
        String[] args = new String[]{"-c", "compare_file.txt",
                                     "-s", "section1.json", "section2.json",
                                     };

        ArgParser ap = new ArgParser();
        boolean result = ap.parseArgs(args);
        assertFalse(result);
    }


    @Test
    public void shouldReturnFalseWhenMissingSectionFlag()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     };

        ArgParser ap = new ArgParser();
        boolean result = ap.parseArgs(args);
        assertFalse(result);
    }
    //Section -sd 1 directory

    //TODO: Section -s getSections checkResults
    //TODO Section -sd 2 directory
    //TODO: use both -sd and -s
    //TODO: SD Folder dosent exist????



    @Test
    public void sectionDirectoryFlagShouldReturnAllJSONFilesInDirectory()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     "-sd", "src/test/test_files/ArgParser/directory1/",
                                     };
        String[] expected = new String[]{"src/test/test_files/ArgParser/directory1/A.json",
                                         "src/test/test_files/ArgParser/directory1/B.json"};
        ArgParser ap = new ArgParser();
        ap.parseArgs(args);
        String[] result = ap.getSections();
        
        if(expected.length != result.length)
            fail("Expected and result differ in total files");
        for(int i = 0; i < expected.length; i++)
            assertEquals(expected[i], result[i]);
    }












    @Test
    public void shouldReturnFalseWhenHelp()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     };

        ArgParser ap = new ArgParser();
        boolean result = ap.parseArgs(args);
        assertFalse(result);
    }




}
