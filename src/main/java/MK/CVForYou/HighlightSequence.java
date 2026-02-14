package MK.CVForYou;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighlightSequence
{
    static final Logger logger = LoggerFactory.getLogger(HighlightSequence.class);
    
    public static String createHighlight(String highlight, String text)
    {
        List<Integer> position = getMatches(text, highlight);
        List<Integer> line_breaks_positions = getMatches(text, "\n");

        StringBuilder sb = new StringBuilder(); 
        for(Integer i : position) {
            int left = firstPositionBefore(i, line_breaks_positions, 50);
            int right = firstPositionAfter(i, line_breaks_positions, 50, text.length());

            String note = text.substring(left+1,right);
            if(note.length() > 0)
                sb.append(note + "\\n");
        }

        return sb.toString();
    }

    private static int firstPositionBefore(int target, List<Integer> positions, int min)
    {
        int result = 0;
        for(int i : positions)
        {
            if(i < target)
                result = i;
            else
                break;
        }
        result = Math.max(result, target-min);
        return result;
    }

    private static int firstPositionAfter(int target, List<Integer> positions, int max, int size)
    {
        int result = size;
        for(int i = positions.size()-1; i >= 0; i--)
        {
            if(positions.get(i) > target)
                result = positions.get(i);
            else
                break;
        }

        result = Math.min(target+max, result);
        return result;
    }

    private static List<Integer> getMatches(String text, String pattern)
    {
        List<Integer> positions = new ArrayList<Integer>();
        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(text);

        while (matcher.find()) {
            positions.add(matcher.start());
        }

        return positions;
    }
}

