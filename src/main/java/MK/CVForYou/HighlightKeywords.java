package MK.CVForYou;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

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

    private void init()
    {
        try {
			String data = IOUtils.readFile(filename);
            parseHighlightMap(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void parseHighlightMap(String data)
    {
        highlights.add("SQL");
    }

    public String createHighlight(String text)
    {
        return getHighlight(text);
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

