package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
    static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {
        new App(args);
    }

    public App(String[] args)
    {
        ArgParser ap = new ArgParser();
        int mode = ap.parseArgs(args);
        if ( mode == 1 )
            new CVGeneratorApplication(ap);
        else if( mode == 2)
            new SeekStatsApplication();
    }
}
