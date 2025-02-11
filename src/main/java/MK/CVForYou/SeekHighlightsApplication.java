package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekHighlightsApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekHighlightsApplication.class);
	@Override
	public void run() {
        logger.trace("highlighting");
	}

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}

}

