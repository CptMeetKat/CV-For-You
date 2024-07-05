package MK.CVForYou;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

public class CosineCalculatorTest 
{
    @Test
    public void shouldCalculateCosineSimilarityFromText()
    {
        double expected = 0.4472;
        double result = CosineCalculator.calculate("the best data science course",
                "data science is popular");
        
        long a = Math.round(expected * 10000.0);
        long b = Math.round(result * 10000.0);
        assertTrue( a == b );
    }


    @Test
    public void shouldCalculateMagnitude()
    {
        int [] vector = new int[]{1,1,1,1,1,0,0};
        double result = CosineCalculator.magnitude(vector);
        double expected = 2.2360679775;

        long a = Math.round(expected * 10000.0);
        long b = Math.round(result * 10000.0);

        assertTrue( a == b );
    }


    @Test
    public void shouldAccumalateFrequencyOfWords()
    {
        HashMap<String, Integer> map = 
            CosineCalculator.wordsToMap("neo there is a glitch a glitch");

        assertEquals(map.get("neo").intValue(), 1);
        assertEquals(map.get("glitch").intValue(), 2);
        assertEquals(map.get("there").intValue(), 1);
        assertEquals(map.get("is").intValue(), 1);
        assertEquals(map.get("a").intValue(), 2);
    }

//removePunctuation
//dotProduct
//calculateCosineSimilarityFromVectors



}
