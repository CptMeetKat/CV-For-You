package MK.CVForYou;

public class App 
{
    public static void main( String[] args )
    {
        String input_document = "sample_components/document.html";
        String input_elements = "sample_components/projects.json";

        
        DocumentGenerator generator = new DocumentGenerator(input_document, input_elements);
        generator.run();

        //CosineCalculator.calculate("hello, neo the matrix has you", 
        //        "neo, there is a glitch in the matrix");

        //CosineCalculator.calculate("the best data science course",
        //        "data science is popular");
        //
        //
    }


}
