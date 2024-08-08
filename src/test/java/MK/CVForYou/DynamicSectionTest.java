package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class DynamicSectionTest 
{
    @Test
    public void fileWithExtensionShouldHaveNoExtensionInName()
    {
        try
        {
            DynamicSection d = new DynamicSection("./src/test/test_files/DynamicSectionTest_tags.json"); 
            String expected = "DynamicSectionTest_tags";
            String result = d.getSectionName();
            assertEquals( expected, result);
        }
        catch(IOException e) {
             System.out.println(e.getMessage());
             fail("Unable to read file");
        }
        
    }


    @Test
    public void fileWithNoExtensionShouldHaveFileNameAsName()
    {
        try {
            DynamicSection d = new DynamicSection("./src/test/test_files/DynamicSectionTest_tags");
            
            String expected = "DynamicSectionTest_tags";
            String result = d.getSectionName();
            assertEquals( expected, result);
        }
        catch(IOException e)
        {
             System.out.println(e.getMessage());
             fail("Unable to read file");
        }
    }
}
