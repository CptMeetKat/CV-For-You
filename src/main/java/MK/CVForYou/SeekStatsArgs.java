
package MK.CVForYou;


import java.nio.file.*;
public class SeekStatsArgs
{
    Path seek_stats_output = Paths.get("data.csv");
    Path seek_stats_input = Paths.get("data.csv");
    int mode = 0;

    public Path getOutput()
    {
        return seek_stats_output;
    }

    public void setOutput(String output)
    {
        seek_stats_output = Paths.get(output);
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return mode;
    }

    public Path getInput()
    {
        return seek_stats_input;
    }

    public void setInput(String input)
    {
        seek_stats_input = Paths.get(input);
    }
}
