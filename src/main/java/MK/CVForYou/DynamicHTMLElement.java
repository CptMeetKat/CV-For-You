package MK.CVForYou;

public class DynamicHTMLElement
{
    String keywords;
    String html;
    public DynamicHTMLElement(String keywords, String html)
    {
        this.keywords = keywords;
        this.html = html;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public String getHTML()
    {
        return html;
    }
}
