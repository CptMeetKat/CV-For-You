package MK.CVForYou;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ArgParserTest 
{
    @Test
    public void shouldNotHaveMoreThanOneCompareSources()
    {
        String[] args = new String[]{"-d", "CV_template.html",
                                     "-cs", "https://www.seek.com.au/job/00000001",
                                     "-c", "compare_file.txt",
                                     "-s", "section1.json", "section2.json",
                                     "-o", "output.out/"}; //TODO: This looks wrong as -o is output directory not name

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

}
