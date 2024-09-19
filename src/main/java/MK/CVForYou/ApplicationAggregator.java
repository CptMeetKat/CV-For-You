package MK.CVForYou;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationAggregator
{
    static String output = "data.csv";
    static final Logger logger = LoggerFactory.getLogger(App.class);

    public static ArrayList<SeekAppliedJob> readData()
    {
        ArrayList<SeekAppliedJob> data = new ArrayList<>();
        String csvFile = output;
        String delimiter = ","; 

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            String line = br.readLine();
            if(line != null)
            {
                String[] csv_fields = line.split(delimiter);

                while ((line = br.readLine()) != null) {
                    String[] csv_data = line.split(delimiter);

                    SeekAppliedJob row = new SeekAppliedJob();
                    for (int i = 0; i < csv_fields.length; i++) {

                        String field = csv_fields[i];
                        if (field.length() > 2)
                        {
                            String field_trimmed = field.substring(1, field.length() - 1);

                            try {
                                Field f = SeekAppliedJob.class.getField(field_trimmed);
                                if(f.getType() == Boolean.class || f.getType() == boolean.class)
                                    f.setBoolean(row, Boolean.getBoolean(csv_data[i]));
                                else if(f.getType() == ArrayList.class)
                                    logger.warn("not implemented: did not populate array type"); //TODO: Incomplete
                                else
                                    f.set(row, csv_data[i]);
                            }
                            catch(IllegalAccessException | NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    data.add(row);
                }
            }
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }
        return data;
    }
}
