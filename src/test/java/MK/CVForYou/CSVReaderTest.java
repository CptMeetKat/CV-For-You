package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class CSVReaderTest
{
    @Test
    public void shouldParseMultipleRecordsFromCSVIntoList()
    {
        String csv = "job_id,job_title\n111,'title1'\n222,'title2'";
        
        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        SeekAppliedJob record1 = result.get(0);
        SeekAppliedJob record2 = result.get(1);

        assertEquals(record1.job_id, "111");
        assertEquals(record1.job_title, "title1");
        assertEquals(record2.job_id, "222");
        assertEquals(record2.job_title, "title2");
    }
    
    @Test
    public void shouldParseBooleanValueFromCSVIntoTrue()
    {
        String csv = "'active'\nfalse";

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        boolean isActive = result.get(0).active;

        assertFalse(isActive);
    }

    @Test
    public void shouldParseBooleanValueFromCSVIntoFalse()
    {
        String csv = "'active'\ntrue";

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        boolean isActive = result.get(0).active;

        assertTrue(isActive);
    }

    @Test
    public void shouldParseBooleanStringFromCSVIntoPrimitiveBoolean()
    {
        String csv = "'active'\n'true'";

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        boolean isActive = result.get(0).active;

        assertTrue(isActive);
    }
    
    @Test
    public void shouldParseStringFromCSVIntoString()
    {
        String csv = "'job_id'\n'job_id_value'";
        String expected = "job_id_value";

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);

        String job_id = result.get(0).job_id;
        assertEquals(expected, job_id);
    }

    @Test
    public void shouldParseEmptyStringArrayIntoArrayList()
    {
        String csv = "'status'\n'[]'";
        String[] expected = {};

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);

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

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        SeekAppliedJob element = result.get(0);

        assertEquals(expected, element.job_id);
    }
    
    @Test
    public void justHeadersShouldCreateEmptyObject()
    {
        String csv = "'job_id'\n";
        int expected = 0;

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);
        
        assertEquals(expected, result.size());
    }
    

    @Test
    public void shouldParseStringArrayIntoArrayList()
    {
        String csv = "'status'\n'[value1, value2]'";
        String[] expected = {"value1", "value2"};

        List<SeekAppliedJob> result = CSVReader.readFromString(SeekAppliedJob.class, csv);

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
        List<NoConstructorObject> items = CSVReader.readFromString(NoConstructorObject.class, csv);
        
        assertEquals(0, items.size());
    }
}
