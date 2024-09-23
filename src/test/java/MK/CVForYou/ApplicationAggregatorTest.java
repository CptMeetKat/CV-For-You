package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ApplicationAggregatorTest
{
    //read []
    //read ""
    //read true/false
    //read "test_value"
    //read ["aaa","bbb"]
    //read just headers
    //program has no available constructor

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
