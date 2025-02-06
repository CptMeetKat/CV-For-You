package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesDeleteMenu implements Menu
{
    private String example_command_prefix;
    private static final String EXAMPLE_USAGE = "-id ID1 [ID2 ID3 ...]";

    private static final Logger logger = LoggerFactory.getLogger(SeekResumesDeleteMenu.class);
    
    public SeekResumesDeleteMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
    }

	@Override
	public Application parse(String[] args) throws ParseException {
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args);
            if(cmd.hasOption("h") || args.length == 0) {
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
                return null;
            }

            SeekResumesArgs seek_resumes_args = new SeekResumesArgs();

            if (!cmd.hasOption("id") && !cmd.hasOption("all"))
                throw new ParseException("-id, or all must be provided");


            seek_resumes_args.setMode(2);
            if(cmd.hasOption("all"))
                seek_resumes_args.deleteAll = true;

            if ( cmd.hasOption("id") )
                seek_resumes_args.addIDs(cmd.getOptionValues("id"));


            return new SeekResumesApplication(seek_resumes_args);
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

        Option resumes = Option.builder("id").hasArgs()
            .longOpt("id")
            .desc("ID of CVs to delete")
            .build();

        Option delete_all = Option.builder("all")
            .longOpt("all")
            .desc("delete all (exclude whitelist)")
            .build();


        options.addOption("h", "help", false, "print this message");
        options.addOption(resumes);
        options.addOption(delete_all);
        return options;
    }
}
