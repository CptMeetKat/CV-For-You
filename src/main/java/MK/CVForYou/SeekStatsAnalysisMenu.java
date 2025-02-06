package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekStatsAnalysisMenu implements Menu
{
    private String example_command_prefix;
    private static final String EXAMPLE_USAGE = "-o <file>";

    private static final Logger logger = LoggerFactory.getLogger(SeekStatsAnalysisMenu.class);
    
    public SeekStatsAnalysisMenu(String example_command_prefix)
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
            seek_stats_args.setMode(1);
            
            if( cmd.hasOption("help") )
            {
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
                return null;
            }
            else if(args.length == 0)
                return new SeekStatsApplication(seek_stats_args);
            else if ( cmd.hasOption("o") )
                seek_stats_args.setOutput(cmd.getOptionValue("o"));

            return new SeekStatsApplication(seek_stats_args);
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

        Option output = Option.builder("o").hasArg()
            .longOpt("output")
            .desc("CSV output location")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(output);
        return options;
    }
}
