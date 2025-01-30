
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

    class BasicPrivateObject
    {
        @SuppressWarnings("unused")
		private String field1;
    }

    
    @Test
    public void makeCSVWithNullObjectsShouldReturnEmptyStringAsValues()
    {
        String expected = "'field1','items'\n'',''";

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


    @Test
    public void makeCSVOnEmptyListReturnsSquareBrackets()
    {
        String expected = "'items'\n'[]'";
        String[] fields = {"items"};
        
        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        BasicPublicObject record = new BasicPublicObject();
        record.items = new ArrayList<String>();
        records.add(record);

        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class, fields);

        assertEquals(expected, csv);
    }


    @Test
    public void makeCSVWithSelectedFieldsOnlyReturnsSelectedFields()
    {
        String expected = "'field1'\n'test_value'";
        String[] fields = {"field1"};
        
        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        BasicPublicObject record = new BasicPublicObject();
        record.field1 = "test_value";
        records.add(record);

        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class, fields);

        assertEquals(expected, csv);
    }


    @Test
    public void makeCSVWithNoFieldsReturnsNothing()
    {
        String expected = "";
        String[] fields = {};
        
        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        BasicPublicObject record = new BasicPublicObject();
        records.add(record);

        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class, fields);

        assertEquals(expected, csv);
    }


    @Test
    public void makeCSVOnPrivateFieldsWillInsertNull()
    {
        String expected = "'field1'\n'null'";
        
        String[] fields = {"field1"};
        ArrayList<BasicPrivateObject> records = new ArrayList<BasicPrivateObject>();
        records.add(new BasicPrivateObject());

        String csv = CSVGenerator.makeCSV(records, BasicPrivateObject.class, fields);

        assertEquals(expected, csv);
    }


    @Test
    public void makeCSVOnNonExistantFieldWillInsertNull()
    {
        String expected = "'does_not_exist'\n'null'";
        
        String[] fields = {"does_not_exist"};
        ArrayList<BasicPrivateObject> records = new ArrayList<BasicPrivateObject>();
        records.add(new BasicPrivateObject());

        String csv = CSVGenerator.makeCSV(records, BasicPrivateObject.class, fields);

        assertEquals(expected, csv);
    }

    @Test
    public void makeCSVWithStringThatContainsQuoteShouldWriteNull()
    {
        String expected = "'field1','items'\n'null',''";

        BasicPublicObject record = new BasicPublicObject();
        record.field1 = "I contain a quote (')";
        ArrayList<BasicPublicObject> records = new ArrayList<BasicPublicObject>();
        records.add(record);
        
        String csv = CSVGenerator.makeCSV(records, BasicPublicObject.class);

        assertEquals(expected, csv);
    }

}

