package MK.CVForYou;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekJobDescriptionTokenizer
{
    static final Logger logger = LoggerFactory.getLogger(SeekJobDescriptionTokenizer.class);
    public static List<String> tokenize(String text)
    {
        ArrayList<String> result = new ArrayList<>();
        text = text.replaceAll("[^\\w\\s+#-]", " ").toLowerCase();
        String[] split_text = text.split("[-., \n]"); //a bit redundant?
        for(String word : split_text) {
            String normalised_word = word.strip();
            result.add(normalised_word);
        }
        return result;
    }
}

