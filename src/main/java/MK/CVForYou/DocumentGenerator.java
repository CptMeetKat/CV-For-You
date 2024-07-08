package MK.CVForYou;


import org.json.JSONObject;
import org.json.JSONArray;


public class DocumentGenerator
{
    String document;
    String element;

    public DocumentGenerator(String input_document, String input_elements)
    {
        document = IOUtils.readFile(input_document);
        element = IOUtils.readFile(input_elements);
    }

    private JSONObject stringToJSON(String data)
    {
        JSONArray array = new JSONArray(data);
        System.out.println("Values array: "+ array);

        JSONArray list = listNumberArray(array.length());
        System.out.println("Label Array: "+ list.toString());

        JSONObject object = array.toJSONObject(list);
        System.out.println("Final JSONOBject: " + object);

        return object;
    }

    public void run()
    {
        JSONObject object  = stringToJSON(element);
        JSONObject x = object.getJSONObject("0");
        //System.out.println(x.get("keywords"));
        //System.out.println(x.get("element"));


        String newHTML = x.get("element").toString();
        document = document.replace("{$projects}", newHTML);

        System.out.println("\n\n\n\n" + document);

        boolean success = IOUtils.writeToFile(document, "generated_document.html");
        if(success)
            System.out.printf("Document has been generated at: %s\n", "__TODO__");

        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }



    private static JSONArray listNumberArray(int max){
    	JSONArray res = new JSONArray();
    	for (int i=0; i<max;i++) {
    		//The value of the labels must be an String in order to make it work
    		res.put(String.valueOf(i));
    	}
    	return res;
    }
}

