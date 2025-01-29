package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesMenu implements Menu
{
    String help_string_prefix;

    HashMap<String, Menu> menus = new HashMap<String,Menu>();
    HelpFormatter formatter = new HelpFormatter();
    private static final String CV_UPLOADER_USAGE = "./CVForYou -sr (-u | -d) <arg>";
    CommandLineParser parser = new DefaultParser();
    static final Logger logger = LoggerFactory.getLogger(SeekResumesMenu.class);

    public SeekResumesMenu()
    {
        registerMenus();
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        try
        {
            CommandLine cmd = parser.parse(getOptions(), args, true);
            if(args.length == 0) {
                formatter.printHelp(CV_UPLOADER_USAGE, getOptions());
                return null;
            }

            String top_level_entry = args[0];
            if (menus.containsKey(top_level_entry)) {
                Menu m = menus.get(top_level_entry);
                return m.parse(ArrayUtils.popCopy(args));
            }
            else if(cmd.hasOption("help")) {
                formatter.printHelp(CV_UPLOADER_USAGE, getOptions());
            }
            else {
                throw new ParseException("Unrecognised options: " + String.join(" ", cmd.getArgs()));
            }
        }
        catch(ParseException e) {
            logger.error(e.getMessage());
            formatter.printHelp(CV_UPLOADER_USAGE, getOptions());
        }

        return null;
    }

    private void registerMenus()
    {
        menus.put("--upload", new SeekResumesUploadMenu());
        menus.put("-u", new SeekResumesUploadMenu());
        menus.put("--delete", null); //TODO WARNING NULLL
        menus.put("-d", null); //TODO WARNING NULLL
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
