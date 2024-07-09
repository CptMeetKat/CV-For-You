package MK.CVForYou;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class DocumentGenerator
{
    String document;
    ArrayList<DynamicSection> sections;

    public DocumentGenerator(String document_path, String[] section_file_paths)
    {
        document = IOUtils.readFile(document_path);
        for(String path : section_file_paths)
        {
           sections.add(new DynamicSection(path));
        }
    }


    private static ArrayList<DynamicHTMLElement> deserializeDynamicHTMLElements(JSONObject object)
    {
        ArrayList<DynamicHTMLElement> arr = new ArrayList<>();
        Iterator<String> keys = object.keys();
        
        while(keys.hasNext())
        {
            String key = keys.next();
            JSONObject element = object.getJSONObject(key);

            String keywords = element.get("keywords").toString();
            String html = element.get("element").toString();
            
            arr.add(new DynamicHTMLElement(keywords, html));
        }

        return arr;
    }

    public void run()
    {
        for(DynamicSection section : sections)
        {
            //section


        }
        //String newHTML = dynamic_options.get(0).html;
        //
        //document = document.replace("{$projects}", newHTML);

        //System.out.println("\n\n\n\n" + document);

        //String out_path = "generated_document.html";

        //boolean success = IOUtils.writeToFile(document, out_path);
        //if(success)
        //    System.out.printf("Document has been generated at: %s\n", out_path);

        //System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

}

