package MK.CVForYou;

import java.lang.reflect.Field;

public class UpdatedRecordFormatter
{
    public static String format(SeekAppliedJobCSVRow former, SeekAppliedJobCSVRow updated)
    {
        //Class<?> clazz = SeekAppliedJobCSVRow.class;
        
        //TODO: Alphabetical order
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(" %-10s %s\n", "job_id", updated.job_id));
        sb.append(String.format(" %-10s %s\n", "title", updated.job_title));
        sb.append(String.format(" %-10s %s\n", "company", updated.company_name));
        sb.append(String.format(" %-10s %s; %s\n\n", "state", updated.latest_status, updated.active ? "Active":"Inactive"));
 

        for (Field field : SeekAppliedJobCSVRow.class.getDeclaredFields()) {
			try {
				String a = field.get(former).toString();
                String b = field.get(updated).toString();

                if(!a.equals(b))
                    sb.append(String.format(" %-35s %6s -> %s\n", field.getName(), a, b));

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
        }

        return sb.toString();
    }
}
