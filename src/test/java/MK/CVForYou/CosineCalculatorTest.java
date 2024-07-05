package MK.CVForYou;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CosineCalculatorTest 
{
    @Test
    public void shouldCalculateCosineSimilarity()
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
}
