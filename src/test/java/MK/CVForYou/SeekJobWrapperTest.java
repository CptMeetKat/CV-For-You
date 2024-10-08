
package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeekJobWrapperTest 
{
    @Test
    public void wrapperWithGoodURLShouldReturnJobID()
    {
        String expected = "77111111";
        String url = "https://www.seek.com.au/job/77111111";
        SeekJobWrapper wrapper = new SeekJobWrapper(url);
        String result = wrapper.getSeekJobID();

        assertEquals(result, expected);
    }

    @Test
    public void wrapperWithURLEndinginSlashShouldReturnJobID()
    {
        String expected = "77111111";
        String url = "https://www.seek.com.au/job/77111111/";
        SeekJobWrapper wrapper = new SeekJobWrapper(url);
        String result = wrapper.getSeekJobID();

        assertEquals(expected, result);
    }

}
