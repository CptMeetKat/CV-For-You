package MK.CVForYou;
import java.io.IOException;

import org.apache.commons.cli.*;

public class App 
{
    public static Options getArgOptions()
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


    public static void main( String[] args )
    {
        String input_document = null; 
        String[] section_definition_paths = null;
        String document = null;
        String generated_document_path = null;
        
        Options options = getArgOptions();

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
                document = getDocument(compare_document_path);
            }
            if (cmd.hasOption("cs")) {
                String seek_url = cmd.getOptionValue("cs");
                document = new SeekWrapper(seek_url).getJD();
            }

            if (cmd.hasOption("s")) {
                section_definition_paths = cmd.getOptionValues("s");
            }

            if(cmd.hasOption("o")) {
                generated_document_path = cmd.getOptionValue("o");
            }
            DocumentGenerator generator = new DocumentGenerator(input_document,
                    section_definition_paths,
                    document,
                    generated_document_path);
            generator.generateDocument();

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java App -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
        }
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
