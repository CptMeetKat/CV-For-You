package MK.CVForYou;

import org.json.JSONException;
import org.json.JSONObject;
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
    String job_description;
    String job_title;

    Path cache_directory = Paths.get("./cache/");

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
        Document page = getJDPageFromCache();
        if(page == null)
        {
            page = initJDPage();
            cachePage(page, getSeekJobID(), this.cache_directory); //TODO: this getSeekJobID() function smells funny
            sleep(); //Avoid flagging seek systems
        }
        job_description = extractJobSectionFromHTML(page);
        job_title = extractJobTitleFromHTML(page);
        logger.trace("Job title detected: {}", job_title);
    }

    private String extractJobTitleFromHTML(Document doc)
    {
        String job_title = null;
        String server_state = extractServerState(doc, "SK_DL");
        try {
            JSONObject json = new JSONObject(server_state);
            job_title = json.optString("jobTitle");
        } catch (JSONException e) {
            logger.warn("Unable to parse Seek server state as JSON: {}", server_state);
        }
        return job_title;
    }

    //OPTIONS: SEEK_APOLLO_DATA, SEEK_CONFIG, SK_DL, SEEK_APP_CONFIG, SEEK_REDUX_DATA
    //TODO: Make this more dynamic in how it obtains fields
    private static String extractServerState(Document doc, String field)
    {
        Element divElement = doc.select("script[data-automation=server-state]").first();
        if (divElement != null)
        {
            String temp = divElement.data();

            temp = insertNewLineBefore(temp, "window.SEEK_APOLLO_DATA");
            temp = insertNewLineBefore(temp, "window.SEEK_CONFIG");
            temp = insertNewLineBefore(temp, "window.SK_DL");
            temp = insertNewLineBefore(temp, "window.SEEK_APP_CONFIG");
            temp = insertNewLineBefore(temp, "window.SEEK_REDUX_DATA");

            String[] states = temp.split("\n");

            for (int i = 0; i < states.length; i++) {
                if(states[i].startsWith("window."+field))
                {
                    int json_start = states[i].indexOf("{");
                    return states[i].substring(json_start);
                }
            }
            logger.warn("Unable to obtain {} from HTML", field);
        }
        else 
        {
            logger.warn("The <script> element with data-automation='server-state' was not found.");
        }

        return null;

    }

    private static String insertNewLineBefore(String text, String before)
    {
        return text.replace(before, "\n"+before);
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


    private Document getJDPageFromCache() {
        Document result = null;
        String job_id = getSeekJobID();

        Path directoryPath = Paths.get(cache_directory.toString(), job_id); 
        logger.trace(directoryPath.toString());

        try {
            String html = IOUtils.readFile(directoryPath.toString());
            result = Jsoup.parse(html);
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
}

