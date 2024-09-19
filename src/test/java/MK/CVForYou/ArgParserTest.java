package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

//TODO: it would better, if each test created the fielstructure it needed and then ran tests

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
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }

    @Test
    public void shouldReturnFalseWhenMissingCompareFlag()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-s", "section1.json", "section2.json",
                                     };

        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }


    @Test
    public void shouldReturnFalseWhenMissingDocumentFlag()
    {
        String[] args = new String[]{"-c", "compare_file.txt",
                                     "-s", "section1.json", "section2.json",
                                     };

        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }


    @Test
    public void shouldReturnFalseWhenMissingSectionFlag()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     };

        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }
    


    @Test
    public void sectionFlagShouldReturnAFileWhenItExists()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     "-s", "src/test/test_files/ArgParser/directory1/A.json",
                                     };


        String expected = "src/test/test_files/ArgParser/directory1/A.json";


        ArgParser ap = new ArgParser();
        ap.parseArgs(args);
        Path[] result = ap.getSections();
        assertEquals(result.length, 1);
        assertEquals(expected, result[0].toString());

    }

    @Test
    public void sectionDirectoryAndSectionFlagShouldReturnACombinedArrayOfJSONFiles()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     "-s", "src/test/test_files/ArgParser/directory2/C.json",
                                     "-sd", "src/test/test_files/ArgParser/directory1/",
                                     };

        Set<String> expected = new HashSet<>(Arrays.asList(
                    "src/test/test_files/ArgParser/directory1/A.json",
                    "src/test/test_files/ArgParser/directory1/B.json",
                    "src/test/test_files/ArgParser/directory2/C.json"
                    ));



        ArgParser ap = new ArgParser();
        ap.parseArgs(args);
        Path[] result = ap.getSections();

        if(expected.size() != result.length)
            fail("Expected <" + expected.size() + "> and result <" + result.length + "> differ in total files");
        for(int i = 0; i < result.length; i++)
            assertTrue(expected.contains(result[i].toString()));
    }



    @Test
    public void sectionDirectoryFlagShouldReturnNothingIfDirectoryDoesNotExist()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     "-sd", "src/test/test_files/ArgParser/DOESNOTEXIST/",
                                     };

        ArgParser ap = new ArgParser();
        ap.parseArgs(args);
        Path[] result = ap.getSections();
        
        assertEquals(result.length, 0);
    }



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
        Path[] result = ap.getSections();
        
        if(expected.length != result.length)
            fail("Expected <" + expected.length + "> and result <" + result.length + "> differ in total files");
        for(int i = 0; i < expected.length; i++)
            assertEquals(expected[i], result[i].toString());
    }


    @Test
    public void sectionDirectoryFlagShouldReturnAllJSONFilesInMultipleDirectories()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-c", "compare_file.txt",
                                     "-sd", "src/test/test_files/ArgParser/directory1/",
                                            "src/test/test_files/ArgParser/directory2/",
                                     };

        String[] expected = new String[]{"src/test/test_files/ArgParser/directory1/A.json",
                                         "src/test/test_files/ArgParser/directory1/B.json",
                                         "src/test/test_files/ArgParser/directory2/C.json"
        };

        ArgParser ap = new ArgParser();
        ap.parseArgs(args);
        Path[] result = ap.getSections();
        
        if(expected.length != result.length)
            fail("Expected <" + expected.length + "> and result <" + result.length + "> differ in total files");
        for(int i = 0; i < expected.length; i++)
            assertEquals(expected[i], result[i].toString());
    }

    @Test
    public void parseArgsShouldReturnFalseWhenBadArgsAreProvided()
    {
        String[] args = new String[]{};

        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }


    @Test
    public void parseArgsShouldReturnFalseWhenHelpIsFlagged()
    {
        String[] args = new String[]{"-h"};

        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        assertEquals(mode, 0);
    }
}
