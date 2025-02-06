package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
    static final Logger logger = LoggerFactory.getLogger(App.class);

    Application application;

    public static void main( String[] args ) {
        new App(args);
    }
    
    public App(String[] args)
    {
        this(args, true);
    }

    public App(String[] args, boolean execute)
    {
        ArgParser ap = new ArgParser();
        application = ap.parseArgs(args);
            
        if(execute)
            run();
    }


    public Application getApplication() {
        return application;
    }

    public void run()
    {
        if(application != null)
            application.run();
    }
}
