package MK.CVForYou;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import org.jsoup.select.Elements;


public class SeekWrapper
{
    String job_url;
    public SeekWrapper(String job_url)
    {
        this.job_url = job_url;
    }


    public String getJD()
    {
        //Seek does not respond to jsoup default useragent
        StringBuilder job_description = new StringBuilder();
        String useragent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36";
        try {
            Document doc = Jsoup.connect(job_url)
                            .userAgent(useragent)
                            .get();
            
            Element divElement = doc.select("div[data-automation=jobAdDetails]").first();
            //String innerHTML = divElement.html();
            //System.out.println(doc);
            
            // Print the content of the selected <div> element
            if (divElement != null) {
                //System.out.println(divElement.outerHtml()); 
                //System.out.println(divElement.wholeText());

                Elements elements = divElement.getAllElements();
                for(Element e : elements) {
                    System.out.println(e.ownText());
                    job_description.append(e.ownText());
                }
            }
            else
            {
                System.out.println("The <div> element with data-automation='jobAdDetails' was not found.");
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }

        return job_description.toString();
    }
}
