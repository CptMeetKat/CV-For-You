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
            DynamicSection d = new DynamicSection("sample_components/tags.json"); //TODO: dont use sample_components, tests should be in test folder
            String expected = "tags";
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
            DynamicSection d = new DynamicSection("./src/test/test_files/tags");
            
            String expected = "tags";
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
