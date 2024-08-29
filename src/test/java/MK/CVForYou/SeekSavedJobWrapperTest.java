package MK.CVForYou;


import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SeekSavedJobWrapperTest 
{
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
