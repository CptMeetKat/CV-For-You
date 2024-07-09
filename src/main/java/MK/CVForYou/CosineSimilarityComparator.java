package MK.CVForYou;

import java.util.Comparator;

public class CosineSimilarityComparator implements Comparator<DynamicHTMLElement>
{
    String text;

    public CosineSimilarityComparator(String primary_text)
    {
        text = primary_text;
    }

	@Override
	public int compare(DynamicHTMLElement d1, DynamicHTMLElement d2) {
        
        double d1_score = CosineCalculator.calculate(text, d1.getKeywords());
        double d2_score = CosineCalculator.calculate(text, d2.getKeywords());

        if(d1_score > d2_score) //Most similar is first
            return -1;
        else if(d2_score < d1_score)
            return 1;

        return 0;
	}
}
