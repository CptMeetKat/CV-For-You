package MK.CVForYou;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.Reader;
import java.util.Map;


public class ApplicationAggregator
{
    static String output = "data.csv";
    static final Logger logger = LoggerFactory.getLogger(App.class);


    public static List<SeekAppliedJob> readData_apache()
    {
        String filePath = "data.csv";

        ArrayList<SeekAppliedJob> applied_jobs = new ArrayList<SeekAppliedJob>();

        try (
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create()
                                                .setQuote('\'') 
                                                .setIgnoreSurroundingSpaces(true) 
                                                .setHeader() 
                                                .setSkipHeaderRecord(true) 
                                                .build())
        ) {
            Map<String, Integer> headerMap = csvParser.getHeaderMap();
            List<String> headers = csvParser.getHeaderNames();
            System.out.println("Headers: " + headerMap.keySet());

            for (CSVRecord row : csvParser) {
                applied_jobs.add(createRecord(row, headers));
            }
        } catch(IOException e)
        {
            logger.error(e.getMessage());
        }

        return applied_jobs;
    }

    private static SeekAppliedJob createRecord(CSVRecord row, List<String> headers)
    {
        SeekAppliedJob record = new SeekAppliedJob();

        for (String header : headers) {
            String cell = row.get(header);
            //logger.trace("data: {}", cell);
            
            try 
            {
                //logger.trace("Create record, writing field: {}\n", header);
                Field f = SeekAppliedJob.class.getField(header);

                if(f.getType() == Boolean.class || f.getType() == boolean.class)
                    f.setBoolean(record, Boolean.getBoolean(cell));
                else if(f.getType() == ArrayList.class)
                    f.set(record, cellToArrayList(cell));
                else
                    f.set(record, cell);
            }
            catch(IllegalAccessException | NoSuchFieldException e) {
                logger.error(e.getMessage());
            }

        }

        System.out.println(record);
        return record;
    }

//TODO: need to know if empty cell is null or "" or "null"
    private static ArrayList<String> cellToArrayList(String cell_data)
    {
        ArrayList<String> result = new ArrayList<String>();

        if(cell_data.length() > 2) //peel []
        {
            String[] atoms = cell_data.substring(1, cell_data.length() - 1)
                                .split(",");

            for (String a : atoms) {
                result.add(new String(a.trim()));
            }
        }

        return result;
    }
}
