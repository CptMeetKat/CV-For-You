package MK.CVForYou;

import java.util.HashMap;
import java.util.function.Predicate;

public class CosineSimilarityFilter implements Predicate<DynamicHTMLElement> {

    final HashMap<String, Integer> word_table;
    public CosineSimilarityFilter(String primary_text)
    {
        word_table = CosineCalculator.wordsToMap(primary_text);
    }

    @Override
    public boolean test(DynamicHTMLElement d) {

        double d1_score = CosineCalculator.calculate(word_table, d.getKeywords());
        if (d1_score == 0) {
            return false;
        }
        return true;
    }
}
