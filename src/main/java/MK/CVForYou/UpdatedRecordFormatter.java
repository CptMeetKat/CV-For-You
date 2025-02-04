package MK.CVForYou;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        sb.append(String.format(" %-10s %s; %s\n", "state", updated.latest_status, updated.active ? "Active":"Inactive"));

        long daysSince = getDaysSince(updated.applied_at);
        sb.append(String.format(" %-10s %d %s ago\n\n", "applied", daysSince, daysSince != 1L ? "days": "day"));


 

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


    private static long getDaysSince(String date)
    {
        Instant applied = Instant.parse(date);
        Instant now = Instant.now();
        long daysSince = ChronoUnit.DAYS.between(applied, now);
        return daysSince;
    }
}
