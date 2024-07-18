package MK.CVForYou;

import org.apache.commons.cli.*;

public class ArgParser
{
    Options options;

    public String input_document; 
    public String[] section_definition_paths;
    private String output_path;

    public String document_source;
    String compare_document_path;
    String seek_url; 

    int runType = 0;

    public ArgParser(String[] args)
    {
        options = getDefaultOptions();
        parseArgs(args);
    }

    public Options getOptions() {
        return options;
    }

    public String getOutput() {
        return output_path;
    }

    public String[] getSections() {
        return section_definition_paths;
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
                compare_document_path = cmd.getOptionValue("c");
                document_source = "file";
            }
            if (cmd.hasOption("cs")) {
                seek_url = cmd.getOptionValue("cs");
                document_source = "seek";
            }

            if (cmd.hasOption("s")) {
                section_definition_paths = cmd.getOptionValues("s");
            }

            if(cmd.hasOption("o")) {
                output_path = cmd.getOptionValue("o");
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
}
