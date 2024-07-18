package MK.CVForYou;

import java.io.IOException;

import org.apache.commons.cli.*;

public class ArgParser
{
    Options options;

    public String input_document = null; 
    public String[] section_definition_paths = null;
    public String document = null;
    public String generated_document_path = null;
    int runType = 0;

    public ArgParser()
    {
        options = getDefaultOptions();
    }

    public Options getOptions()
    {
        return options;
    }


    private static Options getDefaultOptions()
    {
        Options options = new Options();

        Option option_section = Option.builder("s").hasArgs()
                                      .longOpt("section")
                                      .desc("path to section definition files")
                                      .required()
                                      .build();
        options.addOption(option_section);
        options.addRequiredOption("d", "document", true, "path to the dynamic document");
        options.addOption("h", "help", false, "print this message");
        options.addOption("o", "output", true, "path of output");

        Option compare_from_file = Option.builder("c")
                                      .longOpt("compare").hasArg()
                                      .desc("file to compare keywords to")
                                      .build();

        Option compare_from_seek = Option.builder("cs")
                                      .longOpt("compare-seek").hasArg()
                                      .desc("pull JD from seek to compare")
                                      .build();

        OptionGroup compare_input = new OptionGroup();
        compare_input.setRequired(true);

        compare_input.addOption(compare_from_file);
        compare_input.addOption(compare_from_seek);

        options.addOptionGroup(compare_input);

        return options;
    }

    public void parseArgs(String[] args)
    {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("java App -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
                System.exit(0);
            }

            if (cmd.hasOption("d")) {
                input_document = cmd.getOptionValue("d");
            }

            if (cmd.hasOption("c")) {
                String compare_document_path = cmd.getOptionValue("c");
                document = getDocument(compare_document_path); //TODO: Move this out
            }
            if (cmd.hasOption("cs")) {
                String seek_url = cmd.getOptionValue("cs");
                document = new SeekWrapper(seek_url).getJD(); //TODO: Move this out
            }

            if (cmd.hasOption("s")) {
                section_definition_paths = cmd.getOptionValues("s");
            }

            if(cmd.hasOption("o")) {
                generated_document_path = cmd.getOptionValue("o");
            }
            runType = 1;

        } catch (ParseException e) {
            runType = -1;
            System.out.println(e.getMessage());
            formatter.printHelp("java App -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
        }
    }

    public int getRunType()
    {
        return runType;
    }


    public static String getDocument(String document_path)
    {
        String document = null;
        try {
            document = IOUtils.readFile(document_path);
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return document;
    }
}
