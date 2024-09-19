package MK.CVForYou;

import java.lang.reflect.Field;
import java.util.List;

public class CSVGenerator
{
    public static <T> String makeCSV(String columns[], List<T> items, Class<T> type)
    {
        StringBuilder sb = new StringBuilder();

        for (String column : columns) {
            sb.append(String.format("'%s',", column));
        }
        sb.append("\n");

        for (T item : items) {
            for (String column : columns) {

                try {
                    Field field = type.getField(column);
                    String value = field.get(item).toString();
                    sb.append(String.format("'%s',", value));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    sb.append("'null,'");
                    e.printStackTrace();
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
