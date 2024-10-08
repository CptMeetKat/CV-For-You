package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicSectionTest 
{
    static final Logger logger = LoggerFactory.getLogger(DynamicSectionTest.class);
    //TODO: Refactor for test data to exist in this file, not in external file
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
             logger.error(e.getMessage());
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
             logger.error(e.getMessage());
             fail("Unable to read file");
        }
    }


    @Test
    public void maxComponentsShouldLimitComponentsRendered()
    {
        try {
            DynamicSection d = new DynamicSection("./src/test/test_files/DynamicSectionTest_tags_with_limit.json");
            String result = d.compose();
            String expected = "<div>Java1</div><div>C++1</div>";
            assertEquals( expected, result);
        }
        catch(IOException e)
        {
             logger.error(e.getMessage());
             fail("Unable to read file");
        }
    }


    @Test
    public void renderNothingWhenNegativeMaxComponents()
    {
        try {
            DynamicSection d = new DynamicSection("./src/test/test_files/DynamicSectionTest_tags_with_negative_limit.json");
            String result = d.compose();
            String expected = "";
            assertEquals( expected, result);
        }
        catch(IOException e)
        {
             logger.error(e.getMessage());
             fail("Unable to read file");
        }
    }


    @Test
    public void renderAllWhenNoMaxComponentsExist()
    {
        try {
            DynamicSection d = new DynamicSection("./src/test/test_files/DynamicSectionTest_tags_with_no_limit.json");
            String result = d.compose();
            String expected = "<div>Java1</div><div>C++1</div><div>Front-End1</div>";
            assertEquals( expected, result);
        }
        catch(IOException e)
        {
             logger.error(e.getMessage());
             fail("Unable to read file");
        }
    }
}

