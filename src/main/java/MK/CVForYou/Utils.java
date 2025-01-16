
package MK.CVForYou;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils
{
    static final Logger logger = LoggerFactory.getLogger(Utils.class);
    public static void sleep(int seconds)
    {
        try {
            Thread.sleep(1000 * seconds); // Sleep for 1000 milliseconds (1 second)
        } catch (InterruptedException e) {
            logger.info("Thread was interrupted");
        }
    }
}
