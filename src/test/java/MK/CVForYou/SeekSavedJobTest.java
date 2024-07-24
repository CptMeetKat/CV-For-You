package MK.CVForYou;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SeekSavedJobTest 
{
    @Test
    public void shouldReturnEmptyObjectWithEmptyJSON()
    {
        JSONObject empty_json = new JSONObject("{}");
        SeekSavedJob ssj = new SeekSavedJob(empty_json);

        Assert.assertEquals(ssj.job_id, "");
        Assert.assertEquals(ssj.isActive, null);
        Assert.assertEquals(ssj.notes, "");
        Assert.assertEquals(ssj.isExternal, null);
        Assert.assertEquals(ssj.title, "");
        Assert.assertEquals(ssj.location, "");
        Assert.assertEquals(ssj.job_abstract, "");
        Assert.assertEquals(ssj.company, "");
        Assert.assertEquals(ssj.salary, "");

    }

    @Test
    public void shouldReturnObjectWithPopulatedField()
    {

        JSONObject populated_json = new JSONObject("{\"id\":\"77510911\",\"isActive\":true,\"notes\":\"test note\",\"isExternal\":false,\"createdAt\":{\"dateTimeUtc\":\"2024-07-23T10:25:39.052Z\",\"shortAbsoluteLabel\":\"23 Jul 2024\",\"__typename\":\"SeekDateTime\"},\"job\":{\"id\":\"77510911\",\"title\":\"Technical Customer Support\",\"location\":{\"label\":\"Test Location Name, Test Suburb\"},\"abstract\":\"test job brief\",\"createdAt\":{\"dateTimeUtc\":\"2024-07-23T06:16:05.077Z\",\"label\":\"6h ago\",\"__typename\":\"SeekDateTime\"},\"advertiser\":{\"id\":\"44984069\",\"name\":\"test company name\"}, \"salary\":{\"label\":\"$75,000 - $85,000 per year\"}}}");


        SeekSavedJob ssj = new SeekSavedJob(populated_json);

        Assert.assertEquals(ssj.job_id, "77510911");
        Assert.assertEquals(ssj.isActive, true);
        Assert.assertEquals(ssj.notes, "test note");
        Assert.assertEquals(ssj.isExternal, false);
        Assert.assertEquals(ssj.title, "Technical Customer Support");
        Assert.assertEquals(ssj.location, "Test Location Name, Test Suburb");
        Assert.assertEquals(ssj.job_abstract, "test job brief");
        Assert.assertEquals(ssj.company, "test company name");
        Assert.assertEquals(ssj.salary, "$75,000 - $85,000 per year");
    }


}
