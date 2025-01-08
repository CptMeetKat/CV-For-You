package MK.CVForYou;

import java.util.List;

public class SeekStatsAnalyser
{
    List<SeekAppliedJobCSVRow> applications; 
    public SeekStatsAnalyser(List<SeekAppliedJobCSVRow> applied_jobs)
    {
        applications = applied_jobs;
    }

    public void printStats()
    {
        for(SeekAppliedJobCSVRow row : applications)
        {
            System.out.println(row.job_title);
        }
    }
}
