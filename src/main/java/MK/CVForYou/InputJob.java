package MK.CVForYou;


public class InputJob
{
    public String name;
    public String job_description;
    private String job_title;
    public InputJob(String name, String job_description)
    {
        this.name = name;
        this.job_description = job_description;
    }

    public String getTitle()
    {
        return job_title;
    }
}
