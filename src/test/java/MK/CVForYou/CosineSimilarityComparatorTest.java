
package MK.CVForYou;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

public class CosineSimilarityComparatorTest 
{
    @Test
    public void arrayOfTextShouldSortInDescendingCosineOrder()
    {
        String primary_text = "the dog saw a dog";
        String[] expected_order = { "dog dog", "the", "java"};
        DynamicHTMLElement[] input = {
                                        new DynamicHTMLElement("the", "_"),
                                        new DynamicHTMLElement("java", "_"),
                                        new DynamicHTMLElement("dog dog", "_")};

        CosineSimilarityComparator comparator = new CosineSimilarityComparator(primary_text);
        Arrays.sort(input, comparator);

        assertEquals(input[0].getKeywords(), expected_order[0]);
        assertEquals(input[1].getKeywords(), expected_order[1]);
        assertEquals(input[2].getKeywords(), expected_order[2]);
    }
}
