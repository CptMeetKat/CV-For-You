package MK.CVForYou;

import java.util.List;

public class SeekStatsAnalyser
{
    List<SeekAppliedJobCSVRow> applications; 

    int total_applications;

    public SeekStatsAnalyser(List<SeekAppliedJobCSVRow> applied_jobs)
    {
        applications = applied_jobs;
        generateStats();
    }

    public void generateStats()
    {
        total_applications = applications.size();
       // for(SeekAppliedJobCSVRow row : applications)
       // {
       //     //System.out.println(row.job_title);
       // }


        //First remove isExternal
    }

    public void printStats() //TODO: should this use logger or just system.out?
    {
        System.out.println("****Seek Job Application Stats****");
        System.out.printf("Total Application: %d%n", total_applications);

    }
}
