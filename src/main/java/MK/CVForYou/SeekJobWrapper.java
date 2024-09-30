package MK.CVForYou;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekJobWrapper {
    static final Logger logger = LoggerFactory.getLogger(SeekJobWrapper.class);
    String job_url;
    String job_description;
    String job_title;

    Path cache_directory = Paths.get("./cache/");

    public SeekJobWrapper(String job_url, boolean initialise) { 
        this.job_url = job_url;
        if(initialise)
            initialise();
    }

    public SeekJobWrapper(String job_url) { 
        this(job_url, false);
    }

    public String getSeekJobID() {
        
        String[] atoms = job_url.split("/");

        if(atoms.length < 1)
        {
            logger.warn("Unable to extract ID from {}\n", job_url);
            return null;
        }

        return atoms[atoms.length-1];
    }

    private Document fetchJob(String job_id) //TODO: Unsafe function
    {
        Document page;
		try {
            Path cache_path = Paths.get(cache_directory.toString(), job_id);
			page = SeekJobParser.getJobCacheFromFile(cache_path);
            logger.info("JD cache found: {}", job_id);
		}
        catch (IOException e) {
            page = initJDPage();
            cachePage(page, getSeekJobID(), this.cache_directory); //TODO: this getSeekJobID() function smells funny
            sleep(); //Avoid flagging seek systems
		}
        return page;
    }


    public void initialise()
    {
        String job_id = getSeekJobID();
        Document page = fetchJob(job_id); //TODO: This CAN cause a fatal error IF null 

        job_description = SeekJobParser.extractJobDescriptionFromHTML(page); //SeekJobPageParser //SeekJobDescriptionParser
        job_title = SeekJobParser.extractJobTitleFromHTML(page);
        logger.trace("Job title detected: {}", job_title);
    }

    public String getJobDescription()
    {
        return job_description;
    }

    public String getJobTitle()
    {
        return job_title;
    }

    private static void sleep()
    {
        try {
            Thread.sleep(1000); // Sleep for 1000 milliseconds (1 second)
        } catch (InterruptedException e) {
            logger.info("Thread was interrupted");
        }
    }

    private Document initJDPage()
    {
        // Seek does not respond to jsoup default useragent
        Document result = null;
        String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";
        try {
            logger.info("Obtaining job description from Seek: " + job_url);
            Document doc = Jsoup.connect(job_url)
                    .userAgent(useragent)
                    .get();
            result = doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void cachePage(Document doc, String filename, Path cache_directory)
    {
        try {
            if (!Files.exists(cache_directory)) {
                Files.createDirectories(cache_directory);
            }
            IOUtils.writeToFile(doc.toString(), cache_directory + "/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCacheDirectory(Path directory)
    {
        cache_directory = directory;
    }
}

