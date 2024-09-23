
package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class CSVGeneratorTest 
{
    class BasicPublicObject
    {
        public String field1;
        public List<String> items;
    }

    
    @Test
    public void makeCSVWithNullRecordsShouldReturn_____()
    {
        String expected = "'field1','items'\n'null','null'";

        BasicPublicObject record = new BasicPublicObject();
        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        records.add(record);
        
        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class);

        assertEquals(expected, csv);
    }


    @Test
    public void makeCSVWithNoDataShouldReturnClassHeaders()
    {
        String expected = "'field1','items'";

        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class);

        assertEquals(expected, csv);
    }
}



