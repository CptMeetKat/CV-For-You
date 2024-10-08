package MK.CVForYou;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CosineCalculator
{
    static final Logger logger = LoggerFactory.getLogger(CosineCalculator.class);
 
    public static double calculate(HashMap<String, Integer> word_table, String text)
    {
        return calculate(word_table, wordsToMap(text));
    }

    public static double calculate(String textA, String textB)
    {
        return calculate(wordsToMap(textA), wordsToMap(textB));
    }

    public static double calculate(HashMap<String, Integer> word_tableA, HashMap<String, Integer> word_tableB)
    {
        HashSet<String> keys = new HashSet<String>();
        keys.addAll(word_tableA.keySet());
        keys.addAll(word_tableB.keySet());

        int total_keys = keys.size();
        
        int v1[] = new int[total_keys];
        int v2[] = new int[total_keys];


        int next = 0;
        for(String key : keys)
        {
            if(word_tableA.containsKey(key))
                v1[next] = word_tableA.get(key);

            if(word_tableB.containsKey(key))
                v2[next] = word_tableB.get(key);

            next++;
        }

        double result = cosineSimilarity(v1,v2);
        return result;
    }

    public static double magnitude(int v[])
    {
        double result = 0;
        for (int i = 0; i < v.length; i++) {
            result += Math.pow(v[i], 2);
        }
        
        return Math.sqrt(result);
    }

    public static int dotProduct(int v1[], int v2[])
    {
        int dot_product = 0;
        for (int i = 0; i < v1.length; i++) {
            dot_product += v1[i] * v2[i];
        }
        return dot_product;
    }

    public static double cosineSimilarity(int v1[], int v2[])
    {
        if( v1.length != v2.length)
            return -1;

        int dot_product = dotProduct(v1,v2);

        double v1Magnitude = magnitude(v1);
        double v2Magnitude = magnitude(v2);


        double result = ((double)dot_product) / (v1Magnitude * v2Magnitude);

        return result;
    }

    public static String removePunctuation(String s)
    {
        String whitelist = "#+";
        StringBuilder sb = new StringBuilder(s);
        for (int i = sb.length()-1; i >= 0; i--) {
           char letter = sb.charAt(i); 

           if(  !Character.isAlphabetic(letter) && 
                   !Character.isSpaceChar(letter) &&
                   whitelist.indexOf(letter) == -1 )
               sb.deleteCharAt(i);

        }
        return sb.toString();
    }

    /**
     * @return mapping of a word to the words count
     */
    public static HashMap<String,Integer> wordsToMap(String text)
    {
        String formatted_text = text.replaceAll("\n", " ");
        formatted_text = removePunctuation(formatted_text).toLowerCase();


        String[] atoms = formatted_text.split(" "); 
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for(int i = 0; i < atoms.length; i++)
        {
            String key = atoms[i];
            if(map.containsKey(key))
                map.put(key, map.get(key)+1 );
            else
                map.put(atoms[i],1);
        }

       //logger.trace("Text: {}", text);
       //printOrderedWordsOrderedByCount(map);

       return map;
    }

    private static void printOrderedWordsOrderedByCount(HashMap<String, Integer> map)
    {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        String log_statement = "";
        for (Map.Entry<String, Integer> entry : list) {
            log_statement += String.format("\n%s %s", entry.getKey(), entry.getValue());
        }
        logger.trace(log_statement);
    }

}
