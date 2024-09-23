package MK.CVForYou;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import java.io.StringReader;

public class ApplicationAggregator
{
    static final Logger logger = LoggerFactory.getLogger(App.class);


    public static <T> List<T> readFromFile(Class<T> type, String filePath)
    {
        List<T> objects = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            objects = readData(type, reader);
		} catch (IOException e) {
			logger.error(e.getMessage()); 
		}
        return objects;
    }

    public static <T> List<T> readFromString(Class<T> type, String csv)
    {
        List<T> objects = null;
        Reader reader = new BufferedReader(new StringReader(csv));
        objects = readData(type, reader);
        return objects;
    }

    private static <T> List<T> readData(Class<T> type, Reader reader) //parseCSVData
    {
        ArrayList<T> applied_jobs = new ArrayList<T>();

        try (
            CSVParser csvParser = new CSVParser(reader, CSVFormat.Builder.create()
                                                .setQuote('\'') 
                                                .setIgnoreSurroundingSpaces(true) 
                                                .setHeader() 
                                                .setSkipHeaderRecord(true) 
                                                .build())
        ) {
            List<String> headers = csvParser.getHeaderNames();

            for (CSVRecord row : csvParser) {
                applied_jobs.add(createRecord(row, headers, type));
            }
        } catch(IOException e) {
            logger.error(e.getMessage());
        }

        return applied_jobs;
    }

    private static <T> T createRecord(CSVRecord row, List<String> headers, Class<T> type)
    {
        T record; 

        try {
            record = type.getDeclaredConstructor().newInstance();  
        }
        catch(InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            logger.error("Cannot convert CSV row to class, no available default constructor: {}", e.getMessage()); //TODO: Investigate - TBH if this happens this should just exit
            return null;
        }

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
        //System.out.println(record);

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
