package MK.CVForYou;

import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVGenerator
{
    static final Logger logger = LoggerFactory.getLogger(CSVGenerator.class);

    //TODO: this has potential to return a BIG string, maybe consider a buffered approach
    //
    public static <T> String makeCSV(List<T> items, Class<T> type)
    {
        Field[] headers = type.getFields();

        String[] columns = new String[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columns[i] = headers[i].getName();
        }

        return makeCSV(items, type, columns);
    }

    public static <T> String makeCSV(List<T> items, Class<T> type, String[] columns)
    {
        StringBuilder sb = new StringBuilder();

        for (String column : columns) {
            sb.append(String.format("'%s',", column));
        }
        sb.delete(sb.length()-1, sb.length()); //Remove trailing comma
        sb.append("\n");

        for (T item : items) {
            for (int i = 0; i < columns.length; i++) {
                String column = columns[i];

                try {
                    Field field = type.getField(column);
                    String value = field.get(item).toString();
                    sb.append(String.format("'%s'", value));
                } catch (IllegalAccessException e) {
                    sb.append("'null'");
                    logger.warn("Unable to read field to generate CSV: {}", e.getMessage());
                }
                catch(NoSuchFieldException e)
                {
                    sb.append("'null'");
                    logger.warn("Field to generate CSV does not exist: {}", e.getMessage());
                }
                if(i < columns.length-1) //if not last elt
                    sb.append(",");
            }
            sb.append("\n");
        }

        return sb.toString().trim();
    }
}
