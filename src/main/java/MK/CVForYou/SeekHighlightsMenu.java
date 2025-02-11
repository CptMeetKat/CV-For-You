package MK.CVForYou;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekHighlightsMenu implements Menu
{
    private static final Logger logger = LoggerFactory.getLogger(SeekHighlightsMenu.class);

    public SeekHighlightsMenu()
    {
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        return new SeekHighlightsApplication();
    }
}
