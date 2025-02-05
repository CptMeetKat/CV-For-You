package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesMenu implements Menu
{
    private String example_command_prefix;
    private static final String SEEK_RESUMES_USAGE = "--upload -i <file>";

    private HashMap<String, Menu> menus = new HashMap<String,Menu>();
    private HelpFormatter formatter = new HelpFormatter();
    private CommandLineParser parser = new DefaultParser();
    private static final Logger logger = LoggerFactory.getLogger(SeekResumesMenu.class);

    public SeekResumesMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
        registerMenus();
    }

    private void printExampleCommand()
    {
        formatter.printHelp(example_command_prefix + " " + SEEK_RESUMES_USAGE, getOptions());
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        try
        {
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
    }

    private static Options getOptions()
    {
        Options options = new Options();

        Option upload = Option.builder("u")
            .longOpt("upload")
            .desc("Upload CV to SEEK")
            .build();

        Option remove = Option.builder("d")
            .longOpt("delete")
            .desc("Delete CV from SEEK")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(upload);
        options.addOption(remove);

        return options;
    }
}
