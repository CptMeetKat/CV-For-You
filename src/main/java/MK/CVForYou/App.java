package MK.CVForYou;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;





public class App 
{
    public static void main( String[] args )
    {
        //CosineCalculator.calculate("hello, neo the matrix has you", 
        //        "neo, there is a glitch in the matrix");

        //CosineCalculator.calculate("the best data science course",
        //        "data science is popular");
        //
        //
        String document = readFile("sample_components/document.html");
        System.out.println(document);


        String element = readFile("sample_components/projects.json");
        System.out.println(element);

        

    

        JSONArray array = new JSONArray(element);
        System.out.println("Values array: "+ array);

        //We convert that array into a JSONObject, but first, we need the labels, so we need another JSONArray with the labels.
        //Here we will use an auxiliary function to get one for the example.

        JSONArray list = listNumberArray(array.length());
        System.out.println("Label Array: "+ list.toString());
        //Now, we construct the JSONObject using both the  value array and the label array.
        JSONObject object = array.toJSONObject(list);
        System.out.println("Final JSONOBject: " + object);
        
        JSONObject x = object.getJSONObject("0");

        //System.out.println(x.get("keywords"));
        System.out.println(x.get("element"));


        String newHTML = x.get("element").toString();
                                                   
        document = document.replace("{$projects}", newHTML);


        System.out.println("\n\n\n\n" + document);


        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static String readFile(String path)
    {
        String result = null;
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            result = String.join("", lines);
            //for (String line : lines) {
            //    System.out.println(line);
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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
