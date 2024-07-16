package MK.CVForYou;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class IOUtils
{
    public static String readFile(String path) 
    throws IOException
    {
        String result = null;
        
        List<String> lines = Files.readAllLines(Paths.get(path));
        result = String.join("", lines);

        return result;
    }

    public static boolean writeToFile(String data, String path)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
