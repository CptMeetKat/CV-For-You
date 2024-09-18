package MK.CVForYou;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ApplicationAggregator
{
    static String output = "data.csv";

    //TODO: Make more dynamic
    public static ArrayList<SeekAppliedJob> readData()
    {
        ArrayList<SeekAppliedJob> data = new ArrayList<>();
        String csvFile = output;
        String line;
        String delimiter = ","; // Adjust if your CSV uses a different delimiter

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Split each line into values
                String[] values = line.split(delimiter);

                
                // Process values
                for (String value : values) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace(); //TODO: Add logger
        }
        return data;
    }
}
