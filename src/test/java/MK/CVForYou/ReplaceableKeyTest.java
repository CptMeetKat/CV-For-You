package MK.CVForYou;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class ReplaceableKeyTest 
{
    @Test
    public void keyWithMulipleFieldsShouldHaveMulipleFieldsAndSection()
    {
        ReplaceableKey key = new ReplaceableKey("{$title(job_title,job_description)}");
        ArrayList<String> fields = key.getFields();
        String section_name = key.getSectionName();

        assertEquals("title", section_name);
        assertEquals(fields.size(), 2);
        assertEquals(fields.get(0), "job_title");
        assertEquals(fields.get(1), "job_description");
    }

    @Test
    public void keyWithSingleFieldShouldHaveSingleFieldAndSection()
    {
        ReplaceableKey key = new ReplaceableKey("{$title(job_description)}");
        ArrayList<String> fields = key.getFields();
        String section_name = key.getSectionName();

        assertEquals("title", section_name);
        assertEquals(fields.size(), 1);
        assertEquals(fields.get(0), "job_description");
    }


    //{$section_name1()}
    //{$section_name1}
    //INVALID
    //""
}
