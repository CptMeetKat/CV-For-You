package MK.CVForYou;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArgParser
{
    Options options;
    Options help_option;

    Path input_document; 
    Path[] section_definition_paths;
    Path output_directory;

    Path compare_document_path;
    String seek_url; 

    JobDescriptionSource jd_source; 

    private static final String BASIC_USAGE = "./CVForYou -d <document_path> -c <compare_path> -s <section_paths>";

    static final Logger logger = LoggerFactory.getLogger(App.class);

    public ArgParser()
    {
        options = getDefaultOptions();
        help_option = getHelpOption();
        output_directory = Paths.get("./");
    }

    private static Options getHelpOption()
    {
        Options options = new Options();
        
        options.addOption("h", "help", false, "print this message");
        return options;
    }

    public Path getInputDocument()
    {
        return input_document;
    }

    public Options getOptions() {
        return options;
    }

    public Path getOutputFolder() {
        return output_directory;
    }

    public void setOutputFolder(String path) {
        output_directory = Paths.get(path);
    }

    public Path[] getSections() {
        return section_definition_paths;
    }


    private static Options getDefaultOptions()
    {
        Option option_section_files = Option.builder("s").hasArgs()
                                      .longOpt("section")
                                      .desc("path to section definition files")
                                      .build();


        Option option_section_directory = Option.builder("sd").hasArgs()
                                      .longOpt("section directory") 
                                      .desc("directory of section definition files")
                                      .build();

        Options options = new Options();
        options.addOption(option_section_files);
        options.addOption(option_section_directory);



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

    public boolean parseArgs(String[] args) //TODO: Consider replaceing boolean with an enum Mode
    {
        boolean success = true;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try
        {
            CommandLine help_cmd = parser.parse(help_option, args, true);
            if(help_cmd.hasOption("h"))
            {
                formatter.printHelp(BASIC_USAGE,
                                    options);
                return false;
            }
        }
        catch (ParseException e) {}


        try {
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("sd") && !cmd.hasOption("s"))
                throw new ParseException("Either -sd or -s must be provided");

              handleDocumentFlags(cmd);
              handleCompareFlags(cmd);
              handleSectionFlags(cmd);
              handleOutputFlags(cmd);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(BASIC_USAGE,
                                    options);
            success = false;
        }
        return success;
    }

    private void handleOutputFlags(CommandLine cmd)
    {
        if (cmd.hasOption("o"))
            setOutputFolder(cmd.getOptionValue("o"));
    }

    private void handleDocumentFlags(CommandLine cmd)
    {
        if (cmd.hasOption("d"))
            input_document = Paths.get(cmd.getOptionValue("d"));
    }

    private void handleCompareFlags(CommandLine cmd)
    {
        if (cmd.hasOption("c")) {
            compare_document_path = Paths.get(cmd.getOptionValue("c"));
            jd_source = new JobDescriptionFromFile(compare_document_path);
        }
        if (cmd.hasOption("cs")) {
            seek_url = cmd.getOptionValue("cs");
            jd_source = new JobDescriptionFromSeekJob(seek_url);
        }
        if (cmd.hasOption("ca")) 
            jd_source = new JobDescriptionFromSaved();
    }


    private void handleSectionFlags(CommandLine cmd)
    {
        ArrayList<Path> files = new ArrayList<Path>();
        String section_files[] = cmd.getOptionValues("s");

        if(section_files != null)
            for(String file : section_files)
                files.add(Paths.get(file));



        String section_directories[] = cmd.getOptionValues("sd");
        if(section_directories != null)
        {
            for(String dir : section_directories)
            {
                String[] json_files = listJSONFilesInDirectory(dir); 
                if(json_files != null)
                    for(String file : json_files)
                        files.add(Paths.get(dir+file));
            }
        }

       section_definition_paths = files.toArray(new Path[0]);
    }

    private String[] listJSONFilesInDirectory(String directory_path)
    {
        Path directory = Paths.get(directory_path);
        String fileArray[] = null;

        try {
            ArrayList<String> fileNames = new ArrayList<>();

            Files.list(directory)
                 .filter(Files::isRegularFile)
                 .filter(path -> path.getFileName().toString().endsWith(".json")) 
                 .forEach(path -> fileNames.add(path.getFileName().toString()));

            fileArray = fileNames.toArray(new String[0]);

        } catch (NoSuchFileException e) {
            logger.warn("Unable to find files in %s\n", e.getMessage());
        } catch (IOException e) {
			e.printStackTrace();
		}

        return fileArray;
    }
}
