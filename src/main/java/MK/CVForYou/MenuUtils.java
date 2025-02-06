package MK.CVForYou;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class MenuUtils
{
    public static void printExampleCommand(String example_command, Options options)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(example_command, options);
    }
}
