package MK.CVForYou;

public interface Application
{
    public void run();
    public <T> void setDependency(String service_name, T service);
}
