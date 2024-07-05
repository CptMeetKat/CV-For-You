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
}
