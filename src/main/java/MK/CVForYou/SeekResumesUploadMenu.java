package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesUploadMenu implements Menu
{
    private String example_command_prefix;
    private static final String EXAMPLE_USAGE = "-i <file>";

    private static final Logger logger = LoggerFactory.getLogger(SeekResumesUploadMenu.class);
    
    public SeekResumesUploadMenu(String example_command_prefix)
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

            if (!cmd.hasOption("i"))
                throw new ParseException("-i must be provided");
            else if ( cmd.hasOption("i") )
            {
                SeekResumesArgs cv_uploader_args = new SeekResumesArgs();
                cv_uploader_args.setMode(1);
                cv_uploader_args.addFiles(cmd.getOptionValues("i"));
                return new SeekResumesApplication(cv_uploader_args);
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

        Option input = Option.builder("i").hasArgs()
            .longOpt("input")
            .desc("CV files to upload")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(input);
        return options;
    }
}
