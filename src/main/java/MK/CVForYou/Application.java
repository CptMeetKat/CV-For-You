package MK.CVForYou;

public interface Application
{
    public void run();
	public <T> void setDependency(T service, Class<T> serviceType);
}
