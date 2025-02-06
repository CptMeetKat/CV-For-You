package MK.CVForYou;
import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgParser
{
    static final Logger logger = LoggerFactory.getLogger(ArgParser.class);

    public Application parseArgs(String[] args) //TODO: make this static
    {
        Application application = null;
        try
        {
            Menu root_menu = new CVForYouMenu();
            application = root_menu.parse(args);
        }
        catch (ParseException e){}

        return application;
    } 
}
