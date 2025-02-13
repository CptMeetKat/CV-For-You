package MK.CVForYou;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SeekStatsAnalyser
{
    List<SeekAppliedJobCSVRow> applications; 
    List<SeekAppliedJobCSVRow> internal_applications; //shallow copy
                                                           
    int total_applications;
    int total_internal_applications;
    int total_external_application;

    long total_open_internal_applications;
    long total_unopened;
    long total_viewed;
    long total_rejected;
    long total_opened;

    double total_opened_percentage;
    double total_unopened_percentage;
    double total_viewed_percentage;
    double total_rejected_percentage;

    double mean_cover_letter_percentage;
    double mean_cv_percentage;
    double mean_total_applicants;

    int day_since_start;
    

    public SeekStatsAnalyser(List<SeekAppliedJobCSVRow> applied_jobs)
    {
        applications = applied_jobs;
        internal_applications = new ArrayList<SeekAppliedJobCSVRow>();
        generateStats();
    }

    public void generateStats()
    {
        total_applications = applications.size();

        for(SeekAppliedJobCSVRow row : applications) {
            if(!row.isExternal)
                internal_applications.add(row);
        }

        total_internal_applications = internal_applications.size();
        total_external_application = total_applications - total_internal_applications;

        total_open_internal_applications = internal_applications.stream().filter(row -> row.active == true).count();

        total_unopened = internal_applications.stream().filter(row -> row.latest_status.equals("Applied") == true).count();
        total_viewed = internal_applications.stream().filter(row -> row.latest_status.equals("Viewed") == true).count();
        total_rejected = internal_applications.stream().filter(row -> row.latest_status.equals("NotSuitable") == true).count();
        total_opened = total_internal_applications - total_unopened;

        total_unopened_percentage = (double) total_unopened / total_internal_applications;

        total_opened_percentage = (double) total_opened / total_internal_applications;
        total_viewed_percentage = (double) total_viewed / total_internal_applications;
        total_rejected_percentage = (double) total_rejected / total_internal_applications;


        day_since_start = getDaysSinceFirstApplication(applications);
        mean_cover_letter_percentage = getMeanCoverPercentage(internal_applications);
        mean_cv_percentage = getMeanCVPercentage(internal_applications);
        mean_total_applicants = getMeanTotalApplicants(internal_applications);
    }

    private double getMeanTotalApplicants(List<SeekAppliedJobCSVRow> applications)
    {
        if (applications.size() == 0)
            return 0;
            
        int applicant_total = 0;
        for (SeekAppliedJobCSVRow application : applications) {
            applicant_total += application.applicant_count;
        }

        return (double) applicant_total / applications.size();
    }

    private double getMeanCVPercentage(List<SeekAppliedJobCSVRow> applications)
    {
        if (applications.size() == 0)
            return 0;
            
        int cv_percentage_total = 0;
        for (SeekAppliedJobCSVRow application : applications) {
            cv_percentage_total += application.applicants_with_resume_percentage;
        }

        return (double)cv_percentage_total / applications.size();
    }

    private double getMeanCoverPercentage(List<SeekAppliedJobCSVRow> applications)
    {
        if (applications.size() == 0)
            return 0;
            
        int cover_percentage_total = 0;
        for (SeekAppliedJobCSVRow application : applications) {
            cover_percentage_total += application.applicants_with_cover_percentage;
        }

        return (double)cover_percentage_total / applications.size();
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

    private List<ApplicationFrequency> findMostAppliedToCompaniesByMe(HashMap<String, Integer> applications)
    {
        ArrayList<ApplicationFrequency> most_applied = new ArrayList<ApplicationFrequency>();
        for(String key : applications.keySet()) {
            most_applied.add(new ApplicationFrequency(key, applications.get(key)));
        }

        Collections.sort(most_applied);
        Collections.reverse(most_applied);

        return most_applied;
    }

    private static Comparator<SeekAppliedJobCSVRow> orderByApplicantCountCompartor()
    {
        return new Comparator<SeekAppliedJobCSVRow>() {
            @Override
            public int compare(SeekAppliedJobCSVRow s1, SeekAppliedJobCSVRow s2) {

                if (s1.applicant_count < s2.applicant_count)
                    return -1;
                else if (s1.applicant_count > s2.applicant_count)
                    return 1;
                else
                    return 0;
            }
        };
    }

    private List<SeekAppliedJobCSVRow> findRolesWithMostApplicants(List<SeekAppliedJobCSVRow> applications)
    {
        List<SeekAppliedJobCSVRow> ordered_by_applicant_count = new ArrayList<SeekAppliedJobCSVRow>();
        for(SeekAppliedJobCSVRow row : applications) {
            ordered_by_applicant_count.add(row);
        }
        Collections.sort(ordered_by_applicant_count, orderByApplicantCountCompartor());
        Collections.reverse(ordered_by_applicant_count);
        return ordered_by_applicant_count; 
    }


    public void printStats() //TODO: should this use logger or just system.out?
    {
        System.out.println("\n****Seek Job Application Stats****");
        System.out.printf("\tDays Elapsed: %d%n", day_since_start);
        System.out.printf("\tTotal Applications: %d%n", total_applications);
        System.out.printf("\tInternal Applications: %d%n", total_internal_applications);
        System.out.printf("\tExternal Applications: %d%n", total_external_application);

        System.out.printf("%n****Internal Stats****%n");
        System.out.printf("\tPending Applications: %d / %d%n", total_open_internal_applications, total_internal_applications);

        System.out.printf("\tUnopened: %d / %d (%d%%)%n", total_unopened, total_internal_applications, (int)(total_unopened_percentage*100));
        System.out.printf("\tOpened: %d / %d (%d%%)%n", total_opened, total_internal_applications, (int)(total_opened_percentage*100));
        System.out.printf("\tViewed: %d / %d (%d%%)%n", total_viewed, total_internal_applications, (int)(total_viewed_percentage*100));
        System.out.printf("\tRejected: %d / %d (%d%%)%n", total_rejected, total_internal_applications, (int)(total_rejected_percentage*100));


        System.out.printf("%n****All Internal Applicants****%n");
        System.out.printf("\tMean Cover Letters: %.2f%%%n",  mean_cover_letter_percentage);
        System.out.printf("\tMean CVs: %.2f%%%n",  mean_cv_percentage);
        System.out.printf("\tMean applicants: %.2f%n",  mean_total_applicants);


        System.out.printf("%n****Muliple Application****%n");
        
        HashMap<String, Integer> internal_application_frequencies = getApplicationFrequencies(internal_applications);
        List<ApplicationFrequency> top_applied_companies = findMostAppliedToCompaniesByMe(internal_application_frequencies);
        HashMap<String, String> company_id_to_name = mapCompanyIdToName();
        for(ApplicationFrequency row : top_applied_companies) {
            if ( row.frequency > 1 )
                System.out.printf("\t%d %s\n", row.frequency, company_id_to_name.get(row.id));
        }


        System.out.printf("%n****Highest Applicants****%n");
        List<SeekAppliedJobCSVRow> roles_with_most_applicants = findRolesWithMostApplicants(internal_applications);
        int count = 0;
        for(SeekAppliedJobCSVRow row : roles_with_most_applicants) {
            System.out.printf("\t%d %s%n", row.applicant_count, row.job_title);
            if( ++count >= 10 )
                break;
        }

    }
}
