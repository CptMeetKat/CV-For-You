package MK.CVForYou;
import org.apache.commons.cli.*;

import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgParser
{
    Options help_option;

    int mode = 0;

    SeekStatsArgs seek_stats_args = new SeekStatsArgs();
    CVGenerationArgs cv_generation_args = new CVGenerationArgs();

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter(); //This should be global??

    Application application;

    private static final String TOP_LEVEL_USAGE = "./CVForYou -cv";

    static final Logger logger = LoggerFactory.getLogger(ArgParser.class);

    public Application getApplication()
    {
        return application;
    }

    public ArgParser()
    {
        help_option = getHelpOption();
        cv_generation_args.output_directory = Paths.get("./");
    }

    private static Options getHelpOption()
    {
        Options options = new Options();

        options.addOption("h", "help", false, "print this message");
        return options;
    }

    private static Options getBaseOptions()
    {
        Option cv_generator = Option.builder("cv")
            .longOpt("cv-generator")
            .desc("Generate a dynamic CV")
            .build();

        Option seek_profile_stats = Option.builder("ss")
            .longOpt("seek-stats")
            .desc("Aggregate stats from Seek")
            .build();

        Option seek_auto_uploader = Option.builder("sr")
            .longOpt("seek-resumes")
            .desc("Upload CV directly to SEEK")
            .build();

        Options options = new Options();
        options.addOption("h", "help", false, "print this message");

        options.addOption(cv_generator);
        options.addOption(seek_profile_stats);
        options.addOption(seek_auto_uploader);
        return options;

    }

    public int parseArgs(String[] args) //TODO: Replace with return Application
    {
        try
        {
            Menu root_menu = new CVForYouMenu("");
            application = root_menu.parse(args);
            //parseBase(args);
            //if(mode == 0)
            //    formatter.printHelp(TOP_LEVEL_USAGE, getBaseOptions());
            //else if(mode == 1)
            //{
            //    Menu cv_generator_menu = new CVGeneratorMenu("./CVForYou --cv-generator");
            //    String[] reduced_args = ArrayUtils.popCopy(args);
            //    application = cv_generator_menu.parse(reduced_args);
            //}
            //else if(mode == 2)
            //{
            //    Menu seek_stats_menu = new SeekStatsMenu("./CVForYou --seek-stats");
            //    String[] reduced_args = ArrayUtils.popCopy(args);
            //    application = seek_stats_menu.parse(reduced_args);
            //}
            //else if(mode == 3) 
            //{
            //    Menu seek_resumes_menu = new SeekResumesMenu("./CVForYou --seek-resumes");
            //    String[] reduced_args = ArrayUtils.popCopy(args);
            //    application = seek_resumes_menu.parse(reduced_args);
            //}
            //else
            //    logger.error("No mode selected");
        }
        catch (ParseException e){}

        return mode;
    } 


	public void parseBase(String[] args) throws ParseException
    {
        try
        {
            CommandLine cmd = parser.parse(getBaseOptions(), args, true);
            mode = 0;
            if(cmd.hasOption("cv"))
                mode = 1;
            else if(cmd.hasOption("ss"))
                mode = 2;
            else if(cmd.hasOption("sr"))
                mode = 3;
            else if(cmd.hasOption("h"))
                mode = 0;
            else
                throw new ParseException("No args provided");
        }
        catch(ParseException e)
        {
            logger.error(e.getMessage());
            formatter.printHelp(TOP_LEVEL_USAGE, getBaseOptions());
            throw e;
        }
    }
}
