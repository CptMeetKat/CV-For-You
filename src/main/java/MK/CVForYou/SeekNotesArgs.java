package MK.CVForYou;

public class SeekNotesArgs
{
    int mode;
    private String note;
    public String job_id;

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public int getMode()
    {
        return mode;
    }
}
