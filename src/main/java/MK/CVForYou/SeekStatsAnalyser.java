package MK.CVForYou;

import java.util.ArrayList;
import java.util.List;

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
        total_opened = total_applications - total_unopened;

        total_unopened_percentage = (double) total_unopened / total_applications;

        total_opened_percentage = (double) total_opened / total_applications;
        total_viewed_percentage = (double) total_viewed / total_applications;
        total_rejected_percentage = (double) total_rejected / total_applications;
    }

    public void printStats() //TODO: should this use logger or just system.out?
    {
        System.out.println("\n****Seek Job Application Stats****");
        System.out.printf("\tTotal Application: %d%n", total_applications);
        System.out.printf("\tInternal Applications: %d%n", total_internal_applications);
        System.out.printf("\tExternal Applications: %d%n", total_external_application);

        System.out.printf("%n****Internal Stats****%n");
        System.out.printf("\tPending Applications: %d / %d%n", total_open_applications, total_applications);

        System.out.printf("\tUnopened: %d / %d (%f%%)%n", total_unopened, total_applications, total_unopened_percentage);
        System.out.printf("\tOpened: %d / %d (%f%%)%n", total_opened, total_applications, total_opened_percentage);
        System.out.printf("\tViewed: %d / %d (%f%%)%n", total_viewed, total_applications, total_viewed_percentage);
        System.out.printf("\tRejected: %d / %d (%f%%)%n", total_rejected, total_applications, total_rejected_percentage);
    }
}
