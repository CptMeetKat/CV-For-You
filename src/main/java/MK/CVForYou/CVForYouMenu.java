package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CVForYouMenu implements Menu
{
    private String example_command_prefix;
    private HashMap<String, Menu> menus = new HashMap<String,Menu>();

    private static final String EXAMPLE_USAGE = "./CVForYou -cv";
    private static final Logger logger = LoggerFactory.getLogger(CVForYouMenu.class);

    public CVForYouMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
        registerMenus();
    }

    public CVForYouMenu()
    {
        this.example_command_prefix = "";
        registerMenus();
    }

    private void printExampleCommand()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        //try
        //{
        //    CommandLineParser parser = new DefaultParser();
        //    parser.parse(getOptions(), args, true);
        //    CommandLine cmd = parser.parse(getOptions(), args, true);
        //    if(args.length == 0) {
        //        printExampleCommand();
        //        return null;
        //    }

        //    String top_level_entry = args[0];
        //    if (menus.containsKey(top_level_entry)) {
        //        Menu m = menus.get(top_level_entry);
        //        return m.parse(ArrayUtils.popCopy(args));
        //    }
        //    else if(cmd.hasOption("help")) {
        //        printExampleCommand();
        //    }
        //    else {
        //        throw new ParseException("Unrecognised options: " + String.join(" ", cmd.getArgs()));
        //    }
        //}
        //catch(ParseException e) {
        //    logger.error(e.getMessage());
        //    printExampleCommand();
        //}
        //
    
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args, true);

            if(args.length == 0) {
                printExampleCommand();
                return null;
            }

            String top_level_entry = args[0];
            if (menus.containsKey(top_level_entry)) {
                Menu m = menus.get(top_level_entry);
                return m.parse(ArrayUtils.popCopy(args));
            }
            else if(cmd.hasOption("help")) {
                printExampleCommand();
            }
            else
                throw new ParseException("No args provided");
        }
        catch(ParseException e)
        {
            logger.error(e.getMessage());
            printExampleCommand();
        }

        return null;
    }

    private void registerMenus()
    {
        Menu cv_generator_menu = new CVGeneratorMenu(example_command_prefix + " --cv_generator");
        menus.put("--cv-generator", cv_generator_menu);
        menus.put("-cv", cv_generator_menu);


        Menu seek_stats_menu = new SeekStatsMenu(example_command_prefix + " --seek-stats");
        menus.put("--seek-stats", seek_stats_menu);
        menus.put("-ss", seek_stats_menu);


        Menu seek_resumes_menu = new SeekResumesMenu(example_command_prefix + " --seek-resumes");
        menus.put("--seek-resumes", seek_resumes_menu);
        menus.put("-sr", seek_resumes_menu);
    }

    private static Options getOptions()
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
}
