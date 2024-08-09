package MK.CVForYou;

import org.apache.commons.cli.*;

public class ArgParser
{
    Options options;

    public String input_document; 
    private String[] section_definition_paths;
    private String output_path;

    String compare_document_path;
    String seek_url; 

    private JobDescriptionSource jd_source; 

    public ArgParser()
    {
        options = getDefaultOptions();
        output_path = "./";
    }

    public Options getOptions() {
        return options;
    }

    public String getOutputPath() {
        return output_path;
    }

    public String[] getSections() {
        return section_definition_paths;
    }


    private static Options getDefaultOptions()
    {





        Option option_section_files = Option.builder("s").hasArgs()
                                      .longOpt("section")
                                      .desc("path to section definition files")
//                                      .required()
                                      .build();


        Option option_section_directory = Option.builder("sd").hasArgs()
                                      .longOpt("section directory") //Check this valid??
                                      .desc("directory of section definition files")
//                                      .required()
                                      .build();

        OptionGroup section_options = new OptionGroup();
        section_options.setRequired(true);
        section_options.addOption(option_section_files);
        section_options.addOption(option_section_directory);





        Options options = new Options();
        options.addOptionGroup(section_options);



        options.addRequiredOption("d", "document", true, "path to the dynamic document");
        options.addOption("h", "help", false, "print this message");
        options.addOption("o", "output", true, "output directory");

        Option compare_from_file = Option.builder("c")
                                      .longOpt("compare").hasArg()
                                      .desc("file to compare keywords to")
                                      .build();

        Option compare_from_seek = Option.builder("cs")
                                      .longOpt("compare-seek").hasArg()
                                      .desc("pull JD from seek to compare")
                                      .build();

        Option compare_from_all_seek = Option.builder("ca")
                                      .longOpt("compare-seek-all")
                                      .desc("compare from your seek saved job")
                                      .build();

        OptionGroup compare_input = new OptionGroup();
        compare_input.setRequired(true);

        compare_input.addOption(compare_from_file);
        compare_input.addOption(compare_from_seek);
        compare_input.addOption(compare_from_all_seek);

        options.addOptionGroup(compare_input);

        return options;
    }

    public JobDescriptionSource getJDSource()
    {
        return jd_source;
    }

    public boolean parseArgs(String[] args) //TODO: Maybe should return a mode ???
    {
        boolean success = true;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("./CVForYou -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
                success = false; //TODO: This is never reached and technically should semantically be TRUE, however required to return FALSE to work
            }
            else
            {
                if (cmd.hasOption("d"))
                    input_document = cmd.getOptionValue("d");

                if (cmd.hasOption("c")) {
                    compare_document_path = cmd.getOptionValue("c");
                    jd_source = new JobDescriptionFromFile(compare_document_path);
                }

                if (cmd.hasOption("cs")) {
                    seek_url = cmd.getOptionValue("cs");
                    jd_source = new JobDescriptionFromSeekJob(seek_url);
                }

                if (cmd.hasOption("ca")) 
                    jd_source = new JobDescriptionFromSaved();
                if (cmd.hasOption("s")) 
                {
                    section_definition_paths = cmd.getOptionValues("s");
                    System.out.printf("___PATHS: %s\n", String.join(" ", section_definition_paths));
                }
                if (cmd.hasOption("o"))
                    output_path = cmd.getOptionValue("o");
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("./CVForYou -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
            success = false;
        }
        return success;
    }
}
