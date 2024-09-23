package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ApplicationAggregatorTest
{
    //read []
    //read ""
    //read true/false
    //read "test_value"
    //read just headers

    class PublicObject
    {
        public boolean isTest;
        public String data;
        public ArrayList<String> items;
        public PublicObject(){}
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
