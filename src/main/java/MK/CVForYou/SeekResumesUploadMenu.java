package MK.CVForYou;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekResumesUploadMenu implements Menu
{
    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();
    static final Logger logger = LoggerFactory.getLogger(SeekResumesUploadMenu.class);

	@Override
	public Application parse(String[] args) throws ParseException {


        CVUploaderArgs cv_uploader_args = new CVUploaderArgs();
        try
        {
            CommandLine cmd = parser.parse(getOptions(), args);
            if(cmd.hasOption("h") || args.length == 0)
            {
                formatter.printHelp("CV_UPLOADER_USAGE",  getOptions());
                return null;
            }

            if (!cmd.hasOption("i"))
                throw new ParseException("-i must be provided");
            else if ( cmd.hasOption("i") )
            {
                cv_uploader_args.setMode(1);
                cv_uploader_args.addFiles(cmd.getOptionValues("i"));
                return new SeekResumesApplication(cv_uploader_args);
            }
        }
        catch(ParseException e)
        {
            logger.error(e.getMessage());
            formatter.printHelp("CV_UPLOADER_USAGE", getOptions());
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
