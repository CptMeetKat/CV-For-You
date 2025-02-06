package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesViewMenu implements Menu
{
    private String example_command_prefix;

    private static final Logger logger = LoggerFactory.getLogger(SeekResumesViewMenu.class);
    
    public SeekResumesViewMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
    }

	@Override
	public Application parse(String[] args) throws ParseException {
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args);
            if(cmd.hasOption("h")) {
                printExampleCommand();
                return null;
            }

            SeekResumesArgs seek_resumes_args = new SeekResumesArgs();
            seek_resumes_args.setMode(3);
            return new SeekResumesApplication(seek_resumes_args);
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

        options.addOption("h", "help", false, "print this message");
        return options;
    }


    private void printExampleCommand()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(example_command_prefix, getOptions());
    }
}
