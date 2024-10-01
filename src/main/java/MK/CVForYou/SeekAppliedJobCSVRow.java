package MK.CVForYou;

import java.util.ArrayList;

public class SeekAppliedJobCSVRow
{
    public String job_id;
    public String job_title;
    public ArrayList<String> status;
    public ArrayList<String> status_times;
    public String latest_status;
    public String latest_status_time;
    public boolean active;
    public String company_name;
    public String company_id;
    public String applied_at;
    public String created_at;
    public boolean applied_with_cv;
    public boolean applied_with_cover;
    public boolean isExternal;
    public String salary;

    public int applicant_count;
    public int applicants_with_resume_percentage;
    public int applicants_with_cover_percentage;


    public SeekAppliedJobCSVRow() { }

    private static String nullToEmptyString(String text)
    {
        if(text == null)
            return "";
        return text;
    }

    public SeekAppliedJobCSVRow(SeekAppliedJob job, SeekAppliedJobInsights insights)
    {
        job_id = nullToEmptyString(job.job_id);
        job_title = nullToEmptyString(job.job_title);
        status = job.status;
        status_times = job.status_times;
        latest_status = nullToEmptyString(job.latest_status);
        latest_status_time = job.latest_status_time;
        active = job.active;
        company_name = nullToEmptyString(job.company_name);
        company_id = nullToEmptyString(job.company_id);
        applied_at = nullToEmptyString(job.applied_at);
        created_at = nullToEmptyString(job.created_at);
        applied_with_cv = job.applied_with_cv;
        applied_with_cover = job.applied_with_cover;
        isExternal = job.isExternal;
        salary = nullToEmptyString(job.salary);

        applicant_count = insights.applicant_count; 
        applicants_with_resume_percentage = insights.applicants_with_resume_percentage;
        applicants_with_cover_percentage = insights.applicants_with_cover_percentage;
    }


    public String getIdentifer()
    {
        return job_id + "|" + created_at + "|" + applied_at;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(job_id+", ");
        sb.append(job_title+", ");
        sb.append(status+", ");
        sb.append(status_times+", ");
        sb.append(latest_status+", ");
        sb.append(latest_status_time+", ");
        sb.append(active+", ");
        sb.append(company_name+", ");
        sb.append(company_id+", ");
        sb.append(applied_at+", ");
        sb.append(created_at+", ");
        sb.append(applied_with_cv+", ");
        sb.append(applied_with_cover+", ");
        sb.append(isExternal+", ");
        sb.append(salary+", ");

        sb.append(applicant_count+", ");
        sb.append(applicants_with_resume_percentage+", ");
        sb.append(applicants_with_cover_percentage);

        return sb.toString();
    }
}
