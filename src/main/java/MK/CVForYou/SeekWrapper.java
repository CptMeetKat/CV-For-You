package MK.CVForYou;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.select.Elements;

public class SeekWrapper {
    String job_url;
    String job_description;

    public SeekWrapper(String job_url) {
        this.job_url = job_url;
    }

    public String getSeekJobID() {
        int start = job_url.lastIndexOf("/");
        return job_url.substring(start);
    }

    public String getJD() {
        String job_description = getJDFromCache();
        if (job_description == null)
            job_description = getJDFromSeek();

        return job_description;
    }

    private String getJDFromCache() {
        return null;
        // String id = getSeekJobID();
    }

    private String getJDFromSeek() {
        // Seek does not respond to jsoup default useragent
        StringBuilder job_description = new StringBuilder();
        String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";
        try {
            Document doc = Jsoup.connect(job_url)
                    .userAgent(useragent)
                    .get();

            Path directoryPath = Paths.get("./cache/");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            IOUtils.writeToFile(doc.toString(), directoryPath + getSeekJobID());

            Element divElement = doc.select("div[data-automation=jobAdDetails]").first();
            // String innerHTML = divElement.html();
            // System.out.println(doc);

            // Print the content of the selected <div> element
            if (divElement != null) {
                // System.out.println(divElement.outerHtml());
                // System.out.println(divElement.wholeText());

                Elements elements = divElement.getAllElements();
                for (Element e : elements) {
                    System.out.println(e.ownText());
                    job_description.append(e.ownText());
                }
            } else {
                System.out.println("The <div> element with data-automation='jobAdDetails' was not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return job_description.toString();
    }
}
