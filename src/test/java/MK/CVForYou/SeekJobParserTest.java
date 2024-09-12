
package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jsoup.nodes.Document;

import org.junit.Assert;
import org.junit.Test;

public class SeekJobParserTest 
{
    @Test
    public void shouldExtractJobTitleFromCachedPage() throws IOException
    {
        String expected = "Software Developer";
        Path cached_page = Paths.get("src/test/test_files/SeekJobParser/78678834");
        
        Document page = SeekJobParser.getJobCacheFromFile(cached_page);
        String result = SeekJobParser.extractJobTitleFromHTML(page);


        Assert.assertEquals(expected, result);
    }
}
