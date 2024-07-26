
package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeekJobDescriptionWrapperTest 
{
    @Test
    public void wrapperWithGoodURLShouldReturnJobID()
    {
        String expected = "77111111";
        String url = "https://www.seek.com.au/job/77111111";
        SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url);
        String result = wrapper.getSeekJobID();

        assertEquals(result, expected);
    }


    @Test
    public void wrapperWithBadURLShouldReturnJobIDAsNull()
    {
        String expected = null;
        String url = "_bad_url_";
        SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url);
        String result = wrapper.getSeekJobID();

        assertEquals(result, expected);
    }



    //TODO: Complete this test case
    //@Test
    //public void xxx()
    //{
    //    String expected = "77111111";
    //    String url = "https://www.seek.com.au/job/77111111/";
    //    SeekJobDescriptionWrapper wrapper = new SeekJobDescriptionWrapper(url);
    //    String result = wrapper.getSeekJobID();

    //    assertEquals(result, expected);
    //}
}
