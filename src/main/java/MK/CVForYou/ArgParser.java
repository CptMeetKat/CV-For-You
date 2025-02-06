package MK.CVForYou;
import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgParser
{
    Application application;

    static final Logger logger = LoggerFactory.getLogger(ArgParser.class);

    public Application getApplication()
    {
        return application;
    }

    public ArgParser()
    {
    }

    public void parseArgs(String[] args) //TODO: Replace with return Application
    {
        try
        {
            Menu root_menu = new CVForYouMenu();
            application = root_menu.parse(args);
        }
        catch (ParseException e){}
    } 
}
