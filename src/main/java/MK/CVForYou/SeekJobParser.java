
package MK.CVForYou;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekJobParser
{
    static final Logger logger = LoggerFactory.getLogger(SeekJobParser.class);
    public SeekJobParser()
    {

    }

    public SeekJobParser(Document doc)
    {

    }

    public SeekJobParser(Path path)
    {

    }

    public String extractJobTitleFromHTML(Document doc)
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
    public static String extractServerState(Document doc, String field)
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
}
