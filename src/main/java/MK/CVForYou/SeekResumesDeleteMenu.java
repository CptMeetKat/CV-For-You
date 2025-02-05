package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesDeleteMenu implements Menu
{
    private String example_command_prefix;
    private static final String SEEK_RESUMES_DELETE_USAGE = "-id ID1 [ID2 ID3 ...]";

    private HelpFormatter formatter = new HelpFormatter();
    private CommandLineParser parser = new DefaultParser();
    private static final Logger logger = LoggerFactory.getLogger(SeekResumesDeleteMenu.class);
    
    public SeekResumesDeleteMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
    }

	@Override
	public Application parse(String[] args) throws ParseException {
        try
        {
            CommandLine cmd = parser.parse(getOptions(), args);
            if(cmd.hasOption("h") || args.length == 0) {
                printExampleCommand();
                return null;
            }

            if (!cmd.hasOption("id"))
                throw new ParseException("-id must be provided");
            else if ( cmd.hasOption("id") )
            {
                CVUploaderArgs seek_resumes_args = new CVUploaderArgs(); //TODO: this class needs to be renamed
                seek_resumes_args.setMode(2);
                seek_resumes_args.addIDs(cmd.getOptionValues("id"));
                return new SeekResumesApplication(seek_resumes_args);
            }
        }
        catch(ParseException e) {
            logger.error(e.getMessage());
            printExampleCommand();
        }

        return null;
	}

    private static Options getOptions()
    {
        Options options = new Options();

        Option resumes = Option.builder("id").hasArgs()
            .longOpt("id")
            .desc("ID of CVs to delete")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(resumes);
        return options;
    }


    private void printExampleCommand()
    {
        formatter.printHelp(example_command_prefix + " " + SEEK_RESUMES_DELETE_USAGE, getOptions());
    }
}
