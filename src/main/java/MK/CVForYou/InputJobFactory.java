package MK.CVForYou;

public class InputJobFactory
{
    public static InputJob createWorkItem(SeekJobDescriptionWrapper wrapper)
    {
        InputJob work_item = new InputJob();
        work_item.name = wrapper.getSeekJobID();
        work_item.job_description = wrapper.getJobDescription();
        work_item.job_title = wrapper.getJobTitle();
        return work_item;
    }
}
