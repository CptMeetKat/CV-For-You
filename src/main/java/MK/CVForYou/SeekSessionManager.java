
package MK.CVForYou;

public class SeekSessionManager
{
    static SeekSessionManager manager;
    
    private SeekSessionManager()
    {
    }

    public static synchronized SeekSessionManager getManager()
    {
        if (manager == null)
        {
            manager = new SeekSessionManager();
        }
        return manager;
    }
}
