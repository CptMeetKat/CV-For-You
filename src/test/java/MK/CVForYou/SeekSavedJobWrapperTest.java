package MK.CVForYou;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SeekSavedJobWrapperTest 
{
    @Test
    public void shouldThrowAuthExceptionIfResponseReturnsErrors()
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
    public void shouldNotThrowWhenResponseHasNoError()
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
    public void shouldReturnNothingWhenCannotTraverseJSONToDeserialise()
    {
        String single_saved_job = "{}";
        JSONObject json = new JSONObject(single_saved_job);
        ArrayList<SeekSavedJob> jobs = SeekSavedJobWrapper.deserializeSavedJobs(json);
        Assert.assertEquals(jobs.size(), 0);
    }

    @Test
    public void shouldReturnNothingWhenKeyDoesNotExistInDeserialisedJSON()
    {
        String single_saved_job = "{\"data\": {\"viewer\": {\"savedJobs\": {}}}}";

        JSONObject json = new JSONObject(single_saved_job);
        ArrayList<SeekSavedJob> jobs = SeekSavedJobWrapper.deserializeSavedJobs(json);
        Assert.assertEquals(jobs.size(), 0);
    }


    @Test
    public void shouldCreateAnEmptySavedJobFromGoodResponse()
    {
        String empty_saved_job = "{\"data\":{\"viewer\":{\"id\":40911111,\"savedJobs\":{\"edges\":[{\"node\":{}}]}}}}";
        JSONObject json = new JSONObject(empty_saved_job);
        ArrayList<SeekSavedJob> jobs = SeekSavedJobWrapper.deserializeSavedJobs(json);
        Assert.assertEquals(jobs.size(), 1);
    }
}
