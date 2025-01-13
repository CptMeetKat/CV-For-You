package MK.CVForYou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SeekStatsAnalyser
{
    List<SeekAppliedJobCSVRow> applications; 

    int total_applications;
    int total_internal_applications;
    int total_external_application;

    long total_open_applications;
    long total_unopened;
    long total_viewed;
    long total_rejected;
    long total_opened;

    double total_opened_percentage;
    double total_unopened_percentage;
    double total_viewed_percentage;
    double total_rejected_percentage;

    int day_since_start;

    HashMap<String, Integer> internal_application_frequencies;

    public SeekStatsAnalyser(List<SeekAppliedJobCSVRow> applied_jobs)
    {
        applications = applied_jobs;
        generateStats();
    }

    public void generateStats()
    {
        total_applications = applications.size();
        ArrayList<SeekAppliedJobCSVRow> internal_applications = new ArrayList<SeekAppliedJobCSVRow>();

        for(SeekAppliedJobCSVRow row : applications) {
            if(!row.isExternal)
                internal_applications.add(row);
        }

        total_internal_applications = internal_applications.size();
        total_external_application = total_applications - total_internal_applications;

        total_open_applications = applications.stream().filter(row -> row.active == true).count();
        total_unopened = applications.stream().filter(row -> row.latest_status.equals("Applied") == true).count();
        total_viewed = applications.stream().filter(row -> row.latest_status.equals("Viewed") == true).count();
        total_rejected = applications.stream().filter(row -> row.latest_status.equals("NotSuitable") == true).count();
        total_opened = total_internal_applications - total_unopened;

        total_unopened_percentage = (double) total_unopened / total_internal_applications;

        total_opened_percentage = (double) total_opened / total_internal_applications;
        total_viewed_percentage = (double) total_viewed / total_internal_applications;
        total_rejected_percentage = (double) total_rejected / total_internal_applications;

        internal_application_frequencies = getApplicationFrequencies(internal_applications);

        day_since_start = getDaysSinceFirstApplication(applications);
    }

    private int getDaysSinceFirstApplication(List<SeekAppliedJobCSVRow> applications)
    {
        if (applications.isEmpty())
            return 0;
        
        Instant best = Instant.parse(applications.get(0).applied_at);
        for (SeekAppliedJobCSVRow application : applications) {

            Instant temp = Instant.parse(application.applied_at);
            if(best.compareTo(temp) > 0)
                best = temp;
        }

        Instant now = Instant.now();
        long daysSince = ChronoUnit.DAYS.between(best, now);

        return (int) daysSince;
    }

    private HashMap<String,String> mapCompanyIdToName()
    {
        HashMap<String, String> id_to_name = new HashMap<String,String>();
        for (SeekAppliedJobCSVRow application : applications) {
            id_to_name.put(application.company_id, application.company_name);
        }
        return id_to_name;
    }

    private HashMap<String, Integer> getApplicationFrequencies(List<SeekAppliedJobCSVRow> applications)
    {
        HashMap<String, Integer> frequencies = new HashMap<String, Integer>(); 
        for(SeekAppliedJobCSVRow application : applications) 
        {
            String id = application.company_id;
            if(frequencies.containsKey(id))
            {
                int frequency = frequencies.get(application.company_id);
                frequencies.put(id, frequency+1);
            }
            else
                frequencies.put(application.company_id, 1);
        }
        return frequencies;
    }

    private class ApplicationFrequency implements Comparable<ApplicationFrequency>
    {
        public String id;
        public int frequency;
        public ApplicationFrequency(String id, int frequency)
        {
            this.id = id;
            this.frequency = frequency;
        }
		@Override
		public int compareTo(ApplicationFrequency other) {
            if (this.frequency < other.frequency)
                return -1;
            else if (this.frequency > other.frequency)
                return 1;
            else
                return 0;
		}
    }

    private List<ApplicationFrequency> findMostAppliedToCompanies(HashMap<String, Integer> applications)
    {
        ArrayList<ApplicationFrequency> most_applied = new ArrayList<ApplicationFrequency>();
        for(String key : applications.keySet()) {
            most_applied.add(new ApplicationFrequency(key, applications.get(key)));
        }

        Collections.sort(most_applied);
        Collections.reverse(most_applied);

        return most_applied;
    }

    public void printStats() //TODO: should this use logger or just system.out?
    {
        System.out.println("\n****Seek Job Application Stats****");
        System.out.printf("\tDays Elapsed: %d%n", day_since_start);
        System.out.printf("\tTotal Applications: %d%n", total_applications);
        System.out.printf("\tInternal Applications: %d%n", total_internal_applications);
        System.out.printf("\tExternal Applications: %d%n", total_external_application);

        System.out.printf("%n****Internal Stats****%n");
        System.out.printf("\tPending Applications: %d / %d%n", total_open_applications, total_internal_applications);

        System.out.printf("\tUnopened: %d / %d (%d%%)%n", total_unopened, total_internal_applications, (int)(total_unopened_percentage*100));
        System.out.printf("\tOpened: %d / %d (%d%%)%n", total_opened, total_internal_applications, (int)(total_opened_percentage*100));
        System.out.printf("\tViewed: %d / %d (%d%%)%n", total_viewed, total_internal_applications, (int)(total_viewed_percentage*100));
        System.out.printf("\tRejected: %d / %d (%d%%)%n", total_rejected, total_internal_applications, (int)(total_rejected_percentage*100));

        System.out.printf("%n****Muliple Application****%n");
        

        List<ApplicationFrequency> top_applied_companies = findMostAppliedToCompanies(internal_application_frequencies);
        HashMap<String, String> company_id_to_name = mapCompanyIdToName();
        for(ApplicationFrequency row : top_applied_companies)
        {
            if ( row.frequency > 1 )
                System.out.printf("\t%s - %d\n", company_id_to_name.get(row.id), row.frequency);
        }
    }
}
