package MK.CVForYou;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighlightKeywords
{
    static final Logger logger = LoggerFactory.getLogger(HighlightKeywords.class);

    private String filename; 
    private HashSet<String> highlights = new HashSet<String>();

    public HighlightKeywords(String filename)
    {
        this.filename = filename;
        init();
    }

    private static String readFile(String path) 
    throws IOException
    {
        String result = null;
        
        List<String> lines = Files.readAllLines(Paths.get(path));
        result = String.join("\n", lines);

        return result;
    }

    private void init()
    {
        try {
			String data = readFile(filename);
            parseHighlightMap(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void parseHighlightMap(String data)
    {
        String words[] = data.split("\n");
        for(String word : words)
        {
            System.out.println("Writing word:" + word);
            highlights.add(word.toLowerCase());
        }
    }

    public String createHighlight(String text)
    {
        return getHighlight(text.toLowerCase());
    }

    private String getHighlight(String text)
    {
        ArrayList<String> found = new ArrayList<String>();
        for (String highlight : highlights) {
            if(text.contains(highlight))
                found.add(highlight);
        }
        return String.join(", ", found);
    }
}

