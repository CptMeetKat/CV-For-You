package MK.CVForYou;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SeekSavedJobWrapperTest 
{
    @Test
    public void shouldThrowAuthExceptionIfJSONContainsError()
    {
        String error_response = "{\"errors\":[{\"message\":\"An error occurred\",\"path\":[\"viewer\",\"id\"],\"extensions\":{\"code\":\"UNAUTHENTICATED\"}}],\"data\":{\"viewer\":null}}";

        JSONObject json = new JSONObject(error_response);       
        try 
        {
            SeekSavedJobWrapper.checkResponseForError(json);
        }
        catch (BadAuthenticationException e)
        {
            return;
        }
        fail("Bad authentication error was not detected");
    }


    @Test
    public void jsonWithNoErrorShouldNotThrow()
    {
        String empty_response = "{}";

        JSONObject json = new JSONObject(empty_response);       
        try 
        {
            SeekSavedJobWrapper.checkResponseForError(json);
        }
        catch (BadAuthenticationException e)
        {
            fail("BadAuthenticationException was thrown");
        }
    }


    @Test
    public void shouldReturnNothingWhenCannotTraverseJSON()
    {
        String single_saved_job = "{}";
        JSONObject json = new JSONObject(single_saved_job);
        ArrayList<SeekSavedJob> jobs = SeekSavedJobWrapper.deserializeSavedJobs(json);
        Assert.assertEquals(jobs.size(), 0);
    }

    @Test
    public void shouldReturnNothingWhenKeyDoesNotExist()
    {
        String single_saved_job = "{\"data\": {\"viewer\": {\"savedJobs\": {}}}}";

        JSONObject json = new JSONObject(single_saved_job);
        ArrayList<SeekSavedJob> jobs = SeekSavedJobWrapper.deserializeSavedJobs(json);
        Assert.assertEquals(jobs.size(), 0);
    }


}
