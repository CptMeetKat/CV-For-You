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
                MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
                return null;
            }
            else if(args.length == 0)
                return new SeekStatsApplication(seek_stats_args);
            else if ( cmd.hasOption("i") )
                seek_stats_args.setInput(cmd.getOptionValue("i"));
            else if ( cmd.hasOption("d"))
            {
                if( isValidDaysAgo(cmd.getOptionValue("d")) )
                    seek_stats_args.setDaysAgo(Integer.parseInt(cmd.getOptionValue("d")));
            }

            return new SeekStatsApplication(seek_stats_args);
        }
        catch(ParseException e) {
            logger.error(e.getMessage());
            MenuUtils.printExampleCommand(example_command_prefix + " " + EXAMPLE_USAGE, getOptions());
        }

        return null;
	}

    private boolean isValidDaysAgo(String input) throws ParseException
    {
        if(isParsableToUnsignedInt(input))
        {
            int value = Integer.parseUnsignedInt(input);
            if(value >= 0)
                return true;
        }
        else
            throw new ParseException("-d must be a positive number");

        return false;
    }

    private static boolean isParsableToUnsignedInt(String str) {
        try {
            Integer.parseUnsignedInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static Options getOptions()
    {
        Options options = new Options();

        Option input = Option.builder("i").hasArg()
            .longOpt("input")
            .desc("CSV input location")
            .build();

        Option days_ago = Option.builder("d").hasArg()
            .longOpt("days-ago")
            .desc("Days since to report stats from")
            .build();

        options.addOption("h", "help", false, "print this message");
        options.addOption(input);
        options.addOption(days_ago);
        return options;
    }
}
