
package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SeekAppliedJob2Test 
{
    @Test
    public void shouldCreateAppliedJobWithAdvertiseName()
    {
        String json = "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":{\"name\":\"Crowns Test\"}}}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json);

        assertEquals("Crowns Test", applied_job.company_name);
    }

    @Test
    public void shouldCreateObjectWithJobID()
    {
        String json = "{\"id\":\"test_id\"}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json);
        String expected = "test_id";

        assertEquals(expected, applied_job.job_id);
    }

    @Test
    public void shouldReturnLastStatus()
    {
        SeekAppliedJob applied_job = new SeekAppliedJob();

        applied_job.init();
        applied_job.status.add("VIEWED");
        String expected = "VIEWED";

        assertEquals(expected, applied_job.getLastestStatus());
    }


    @Test
    public void shouldReturnNullWhenStatusesEmpty()
    {
        SeekAppliedJob applied_job = new SeekAppliedJob();
        applied_job.init();

        String latest_status = applied_job.getLastestStatus();

        assertNull(latest_status);
    }
}
