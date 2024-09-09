package MK.CVForYou;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekJobDescriptionWrapper {
    static final Logger logger = LoggerFactory.getLogger(SeekJobDescriptionWrapper.class);
    String job_url;
    Document page;
    String job_description;


    public SeekJobDescriptionWrapper(String job_url, boolean initialise) { 
        this.job_url = job_url;
        if(initialise)
            initialise();
    }

    public SeekJobDescriptionWrapper(String job_url) { 
        this(job_url, false);
    }

    public String getSeekJobID() {

        int slash_position = job_url.lastIndexOf("/");
        if(slash_position == -1)
        {
            logger.warn("Unable to extract ID from {}\n", job_url);
            return null;
        }
        return job_url.substring(slash_position+1); //Unsafe if / is last character
    }

    public void initialise()
    {
        initJDPage();
        cachePage(page, getSeekJobID()); //TODO: this getSeekJobID() function smells funny
        job_description = extractJobSectionFromHTML(page);

        sleep(); //Avoid flagging seek systems
    }

    public String getJD() {
        String job_description = getJDFromCache();
        if (job_description == null)
        {
            job_description = getJDFromSeek();
            sleep(); //Avoid flagging Seek systems
        }

        return job_description;
    }

    private static void sleep()
    {
        try {
            Thread.sleep(1000); // Sleep for 1000 milliseconds (1 second)
        } catch (InterruptedException e) {
            logger.info("Thread was interrupted");
        }
    }

    private void initJDPage()
    {
        // Seek does not respond to jsoup default useragent
        String job_description = null;
        String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";
        try {
            logger.info("Obtaining job description from Seek: " + job_url);
            Document doc = Jsoup.connect(job_url)
                    .userAgent(useragent)
                    .get();
            this.page = doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cachePage(Document doc, String filename)
    {
        try {
            Path directoryPath = Paths.get("./cache/"); //WRITE TO CACHE
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            IOUtils.writeToFile(doc.toString(), directoryPath + "/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJDFromCache() {
        String result = null;
        String job_id = getSeekJobID();

        Path directoryPath = Paths.get("./cache/" + job_id);
        try {
            String html = IOUtils.readFile(directoryPath.toString());
            Document doc = Jsoup.parse(html);
            logger.info("Extract job from HTML: " + job_id + "");
            result = extractJobSectionFromHTML(doc);
            logger.info("JD cache found: " + job_id + "");
        } catch (Exception e) {
            logger.info("JD cache not found");
        }

        return result;
    }

    private static String extractJobSectionFromHTML(Document doc)
    {
        StringBuilder job_description = new StringBuilder();
        Element divElement = doc.select("div[data-automation=jobAdDetails]").first();

        if (divElement != null)
        {
            Elements elements = divElement.getAllElements();
            for (Element e : elements)
                job_description.append(e.ownText() + "\n");
        }
        else 
        {
            logger.warn("The <div> element with data-automation='jobAdDetails' was not found.");
        }
        return job_description.toString();
    }

    private String getJDFromSeek() {
        // Seek does not respond to jsoup default useragent
        String job_description = null;
        String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";
        try {
            logger.info("Obtaining job description from Seek: " + job_url);
            Document doc = Jsoup.connect(job_url)
                    .userAgent(useragent)
                    .get();

            Path directoryPath = Paths.get("./cache/"); //WRITE TO CACHE
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            IOUtils.writeToFile(doc.toString(), directoryPath + "/" + getSeekJobID());


            job_description = extractJobSectionFromHTML(doc);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return job_description;
    }
}

