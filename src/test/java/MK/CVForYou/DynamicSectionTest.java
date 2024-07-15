package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DynamicSectionTest 
{
    @Test
    public void shouldBeNamedAsFileNameWithoutExtension()
    {
        DynamicSection d = new DynamicSection("sample_components/tags.json");
        
        String expected = "tags";
        String result = d.getSectionName();
        assertEquals( expected, result);
    }
}
