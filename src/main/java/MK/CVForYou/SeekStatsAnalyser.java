package MK.CVForYou;

import java.util.ArrayList;
import java.util.List;

public class SeekStatsAnalyser
{
    List<SeekAppliedJobCSVRow> applications; 

    int total_applications;
    int total_internal_applications;
    int total_external_application;

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

        //long count = applications.stream().filter(row -> row.field == 1).count();
    }

    public void printStats() //TODO: should this use logger or just system.out?
    {
        System.out.println("****Seek Job Application Stats****");
        System.out.printf("Total Application: %d%n", total_applications);
        System.out.printf("Internal Applications: %d%n", total_internal_applications);
        System.out.printf("External Applications: %d%n", total_external_application);

    }
}
