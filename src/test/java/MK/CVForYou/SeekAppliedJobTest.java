
package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


import org.junit.Test;

public class SeekAppliedJobTest 
{
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
