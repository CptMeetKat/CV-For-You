
package MK.CVForYou;

import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SeekAppliedJob1Test 
{
        private String json_string;

        public SeekAppliedJob1Test(String input) {
            this.json_string = input;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                { "{\"id\":\"1111\",\"job\":{\"id\":\"2222\"}}" },
                { "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":{}}}" },
                { "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":null}}" },
                { "{\"id\":\"1111\",\"job\":{\"id\":\"2222\",\"advertiser\":{\"name\":null}}}" }
            });
        }

        @Test
        public void shouldCreateAppliedJobWithAdvertiseNameAsNull0()
        {
            SeekAppliedJob applied_job = new SeekAppliedJob(json_string);
            assertNull(applied_job.company_name);
        }
}
