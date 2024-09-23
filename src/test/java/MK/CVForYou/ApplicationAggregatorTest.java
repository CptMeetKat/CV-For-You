package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ApplicationAggregatorTest
{
    //read true/false

    
    @Test
    public void shouldParseStringFromCSVIntoString()
    {
        String csv = "'job_id'\n'job_id_value'";
        String expected = "job_id_value";

        List<SeekAppliedJob> result = ApplicationAggregator.readFromString(SeekAppliedJob.class, csv);

        String job_id = result.get(0).job_id;
        assertEquals(expected, job_id);
    }

    @Test
    public void shouldParseEmptyStringArrayIntoArrayList()
    {
        String csv = "'status'\n'[]'";
        String[] expected = {};

        List<SeekAppliedJob> result = ApplicationAggregator.readFromString(SeekAppliedJob.class, csv);

        List<String> status = result.get(0).status;
        
        assertEquals(expected.length, status.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], status.get(i));
        }
    }

    @Test
    public void emptyStringCellShouldCreateObjectWithEmptyStringValue()
    {
        String csv = "'job_id'\n''";
        String expected = "";

        List<SeekAppliedJob> result = ApplicationAggregator.readFromString(SeekAppliedJob.class, csv);
        SeekAppliedJob element = result.get(0);

        assertEquals(expected, element.job_id);
    }
    
    @Test
    public void justHeadersShouldCreateEmptyObject()
    {
        String csv = "'job_id'\n";
        int expected = 0;

        List<SeekAppliedJob> result = ApplicationAggregator.readFromString(SeekAppliedJob.class, csv);
        
        assertEquals(expected, result.size());
    }
    

    @Test
    public void shouldParseStringArrayIntoArrayList()
    {
        String csv = "'status'\n'[value1, value2]'";
        String[] expected = {"value1", "value2"};

        List<SeekAppliedJob> result = ApplicationAggregator.readFromString(SeekAppliedJob.class, csv);

        List<String> status = result.get(0).status;
        
        assertEquals(expected.length, status.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], status.get(i));
        }
    }

    class NoConstructorObject
    {
        private NoConstructorObject(){}
    }

    @Test
    public void returnNothingWhenNoDefaultConstructor()
    {
        String csv = "'field1'\n'data'";
        List<NoConstructorObject> items = ApplicationAggregator.readFromString(NoConstructorObject.class, csv);
        
        assertEquals(0, items.size());
    }
}
