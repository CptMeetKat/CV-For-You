package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesMenu implements Menu
{
    private String example_command_prefix;
    private HashMap<String, Menu> menus = new HashMap<String,Menu>();

    private static final String EXAMPLE_USAGE = "--upload -i <file>";
    private static final Logger logger = LoggerFactory.getLogger(SeekResumesMenu.class);

    public SeekResumesMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
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
        try
        {
            CommandLineParser parser = new DefaultParser();
            parser.parse(getOptions(), args, true);
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
        catch(ParseException e) {
            logger.error(e.getMessage());
            printExampleCommand();
        }

        return null;
    }

    private void registerMenus()
    {
        Menu upload_menu = new SeekResumesUploadMenu(example_command_prefix + " --upload");
        menus.put("--upload", upload_menu);
        menus.put("-u", upload_menu);

        Menu delete_menu = new SeekResumesDeleteMenu(example_command_prefix + " --delete"); //TODO Maybe make this two args instead of one joined monstorsity
        menus.put("--delete", delete_menu);
        menus.put("-d", delete_menu);

        Menu view_menu = new SeekResumesViewMenu(example_command_prefix + " --view");
        menus.put("--view", view_menu);
        menus.put("-v", view_menu);
    }

    private static Options getOptions()
    {
        Options options = new Options();

        Option upload = Option.builder("u")
            .longOpt("upload")
            .desc("Upload CVs to SEEK")
            .build();

        Option remove = Option.builder("d")
            .longOpt("delete")
            .desc("Delete CVs from SEEK")
            .build();

        Option view = Option.builder("v")
            .longOpt("view")
            .desc("View CVs uploaded on SEEK")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(upload);
        options.addOption(remove);
        options.addOption(view);

        return options;
    }
}
