package MK.CVForYou;
import org.apache.commons.cli.*;

public class App 
{
    public static void main( String[] args )
    {
        String input_document = null; 
        String[] section_definition_paths = null;
        String compare_document_path = null;


        Options options = new Options();
     
        Option option_section = Option.builder("s").hasArgs()
                                      .longOpt("section")
                                      .desc("path to section definition files")
                                      .build();

        options.addOption(option_section);
        options.addOption("d", "document", true, "path to the dynamic document");
        options.addOption("c", "compare", true, "file to compare keywords to");
        options.addOption("h", "help", false, "print this message");

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
                //System.out.println("value: " + value);
            }

            if (cmd.hasOption("c")) {
                compare_document_path = cmd.getOptionValue("c");
                //System.out.println("value: " + value);
            }

            if (cmd.hasOption("s")) {
                section_definition_paths = cmd.getOptionValues("s");
                //System.out.println("value: " + String.join(", ", value));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("java App -d <document_path> -c <compare_path> -s <section_paths>",
                                    options);
            System.exit(1);
        }

        DocumentGenerator generator = new DocumentGenerator(input_document, section_definition_paths, compare_document_path);
        generator.generateDocument();
    }


}
