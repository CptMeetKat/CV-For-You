package MK.CVForYou;
import org.json.JSONObject;




public class App 
{
    public static void main( String[] args )
    {
        //CosineCalculator.calculate("hello, neo the matrix has you", 
        //        "neo, there is a glitch in the matrix");

        //CosineCalculator.calculate("the best data science course",
        //        "data science is popular");

       JSONObject jo = new JSONObject("{ \"abc\" : \"def\" }");
       System.out.println(jo);
    }
}
