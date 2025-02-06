package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekStatsSummaryMenu implements Menu
{
    private String example_command_prefix;
    private static final String EXAMPLE_USAGE = "-i <file>";

    private static final Logger logger = LoggerFactory.getLogger(SeekStatsSummaryMenu.class);
    
    public SeekStatsSummaryMenu(String example_command_prefix)
    {
        this.example_command_prefix = example_command_prefix;
    }

	@Override
	public Application parse(String[] args) throws ParseException {
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(getOptions(), args);
            

            SeekStatsArgs seek_stats_args = new SeekStatsArgs();
            seek_stats_args.setMode(2);
            
            if( cmd.hasOption("help") )
            {
                printExampleCommand();
                return null;
            }
            else if(args.length == 0)
                return new SeekStatsApplication(seek_stats_args);
            else if ( cmd.hasOption("i") )
                seek_stats_args.setInput(cmd.getOptionValue("i"));

            return new SeekStatsApplication(seek_stats_args);
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

        Option input = Option.builder("i").hasArg()
            .longOpt("input")
            .desc("CSV input location")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(input);
        return options;
    }


    private void printExampleCommand()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
    }
}
