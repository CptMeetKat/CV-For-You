package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekStatsMenu implements Menu
{
    private String example_command_prefix;
    private HashMap<String, Menu> menus = new HashMap<String,Menu>();

    private static final String SEEK_STATS_USAGE = "--upload -i <file>";
    private static final Logger logger = LoggerFactory.getLogger(SeekStatsMenu.class);

    public SeekStatsMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
        registerMenus();
    }

    private void printExampleCommand()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(example_command_prefix + " " + SEEK_STATS_USAGE, getOptions());
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        try
        {
            DefaultParser parser = new DefaultParser();
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
            else {
                throw new ParseException("Unrecognised options: " + String.join(" ", cmd.getArgs()));
            }
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
        Menu analysis_menu = new SeekStatsAnalysisMenu(example_command_prefix + " --analysis");
        menus.put("--analysis", analysis_menu);
        menus.put("-a", analysis_menu);

        Menu summary_menu = new SeekStatsSummaryMenu(example_command_prefix + " --summary"); 
        menus.put("--summary", summary_menu);
        menus.put("-s", summary_menu);
    }

    private static Options getOptions()
    {
        Options options = new Options();

        Option analysis = Option.builder("a")
            .longOpt("analysis") //TODO: rename analyse
            .desc("Aggregate stats from Seek")
            .build();

        Option seek_summary = Option.builder("s")
            .longOpt("summary")
            .desc("Summarise stats from Seek")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(seek_summary);
        options.addOption(analysis);
        return options;
    }
}
