package MK.CVForYou;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekKeywordGeneratorMenu implements Menu
{
    private static final Logger logger = LoggerFactory.getLogger(SeekKeywordGeneratorMenu.class);

    public SeekKeywordGeneratorMenu()
    {
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        return new SeekKeywordGeneratorApplication();
    }
}
