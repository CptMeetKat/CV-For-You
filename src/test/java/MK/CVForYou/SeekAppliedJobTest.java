
package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


import org.junit.Test;

public class SeekAppliedJobTest 
{
    @Test
    public void shouldCreateAppliedJobWithAdvertiseNameAsNull0() //TODO Split out into inner parameterised test
    {
        String json0 = "{\"id\":\"1111\",\"job\":{\"id\":\"2222\"}}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json0);
        assertNull(applied_job.company_name);
    }

    @Test
    public void shouldCreateAppliedJobWithAdvertiseNameAsNull1() //TODO Split out into inner parameterised test
    {
        String json1 = "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":{}}}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json1);
        assertNull(applied_job.company_name);
    }

    @Test
    public void shouldCreateAppliedJobWithAdvertiseNameAsNull2() //TODO Split out into inner parameterised test
    {
        String json2 = "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":null}}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json2);
        assertNull(applied_job.company_name);
    }

    @Test
    public void shouldCreateAppliedJobWithAdvertiseNameAsNull3() //TODO Split out into inner parameterised test
    {
        String json3 = "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":{\"name\":null}}}";
        SeekAppliedJob applied_job = new SeekAppliedJob(json3);
        assertNull(applied_job.company_name);
    }

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
