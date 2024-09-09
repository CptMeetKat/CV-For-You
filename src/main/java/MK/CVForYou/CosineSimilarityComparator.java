package MK.CVForYou;

import java.util.Comparator;
import java.util.HashMap;

public class CosineSimilarityComparator implements Comparator<DynamicHTMLElement>
{
    final String text;
    final HashMap<String, Integer> word_table;

    public CosineSimilarityComparator(String primary_text)
    {
        text = primary_text;
        word_table = CosineCalculator.wordsToMap(primary_text);

    }

	@Override
	public int compare(DynamicHTMLElement d1, DynamicHTMLElement d2) {
        
        double d1_score = CosineCalculator.calculate(word_table, d1.getKeywords());
        double d2_score = CosineCalculator.calculate(word_table, d2.getKeywords());

        if(d1_score > d2_score) //Most similar is first
            return -1;
        else if(d2_score < d1_score)
            return 1;

        return 0;
	}
}
