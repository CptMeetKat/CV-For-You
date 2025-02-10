package MK.CVForYou;

import java.util.HashMap;

import org.apache.commons.cli.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekNotesMenu implements Menu
{
    private String example_command_prefix;

    private static final String EXAMPLE_USAGE = "--note <note> -id <id>";
    private static final Logger logger = LoggerFactory.getLogger(SeekNotesMenu.class);

    public SeekNotesMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
    }

    @Override
    public Application parse(String args[]) throws ParseException
    {
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args);
            if(cmd.hasOption("h") || args.length == 0) {
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
                return null;
            }

            if (!cmd.hasOption("n"))
                throw new ParseException("-n must be provided");
            if (!cmd.hasOption("id"))
                throw new ParseException("-id must be provided");
            else if ( cmd.hasOption("n") )
            {
                SeekNotesArgs seek_note_args = new SeekNotesArgs();
                seek_note_args.setMode(1);
                seek_note_args.note = cmd.getOptionValue("n");
                seek_note_args.job_id = cmd.getOptionValue("id");
                return new SeekNotesApplication(seek_note_args);
            }
        }
        catch(ParseException e) {
            logger.error(e.getMessage());
            MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
        }

        return null;
    }


    private static Options getOptions()
    {
        Options options = new Options();


        Option id = Option.builder("id").hasArg()
            .longOpt("id")
            .desc("ID of the role to add note too")
            .build();

        Option note = Option.builder("n").hasArg()
            .longOpt("note")
            .desc("Note to write to SEEK role")
            .build();


        options.addOption("h", "help", false, "print this message");
        options.addOption(id);
        options.addOption(note);

        return options;
    }
}
