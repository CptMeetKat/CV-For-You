package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekScriptsMenu implements Menu
{
    private static final Logger logger = LoggerFactory.getLogger(SeekScriptsMenu.class);
    private String example_command_prefix;
    private HashMap<String, Menu> menus = new HashMap<String,Menu>();
    private static final String EXAMPLE_USAGE = "-sh";

    public SeekScriptsMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
        registerMenus();
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args, true);

            if(args.length == 0) {
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
                return null;
            }

            String top_level_entry = args[0];
            if (menus.containsKey(top_level_entry)) {
                Menu m = menus.get(top_level_entry);
                return m.parse(ArrayUtils.popCopy(args));
            }
            else if(cmd.hasOption("help")) {
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
            }
            else
                throw new ParseException("No args provided");
        }
        catch(ParseException e)
        {
            logger.error(e.getMessage());
            MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
        }

        return null;
    }


    private void registerMenus()
    {
        Menu seek_highlights = new SeekHighlightsMenu();
        menus.put("--seek-highlights", seek_highlights);
        menus.put("-sh", seek_highlights);
    }

    private static Options getOptions()
    {
        Option cv_generator = Option.builder("sh")
            .longOpt("seek-highlights")
            .desc("...")
            .build();


        Options options = new Options();
        options.addOption("h", "help", false, "print this message");

        options.addOption(cv_generator);
        return options;
    }
}
