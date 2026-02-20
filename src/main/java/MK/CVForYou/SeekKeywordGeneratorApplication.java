package MK.CVForYou;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekKeywordGeneratorApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekKeywordGeneratorApplication.class);

	@Override
	public void run() {
        logger.info("Hello world");
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

