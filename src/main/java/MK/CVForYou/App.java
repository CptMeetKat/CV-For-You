package MK.CVForYou;

import java.util.HashMap;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        int v1[] = new int[]{1,1,1,1,1,0,0};
        int v2[] = new int[]{0,0,1,1,0,1,1};

        cosineSimilarity(v1,v2);



        String test = "hello, neo the matrix has you";
        System.out.println(removePunctuation(test));
        String[] atoms = test.split(" ");



        //HashMap<String, int> word_table = wordsToMap();




    }

    public int calculateConsineSimarity(String a, String b)
    {
        HashMap<String, Integer> mapA = wordsToMap(a);
        HashMap<String, Integer> mapB = wordsToMap(b);


        for(String key: mapA.keySet()) {
            System.out.println(mapA.get(key));
        }

        for(String key: mapB.keySet()) {
            System.out.println(mapB.get(key));
        }

        return -1;
    }


    public static double magnitude(int v[])
    {
        double result = 0;
        for (int i = 0; i < v.length; i++) {
            result += Math.pow(v[i], 2);
        }
        
        return Math.sqrt(result);
    }

    public static int dotProduct(int v1[], int v2[])
    {
        int dot_product = 0;
        for (int i = 0; i < v1.length; i++) {
            dot_product += v1[i] * v2[i];
        }
        return dot_product;
    }

    public static double cosineSimilarity(int v1[], int v2[])
    {
        if( v1.length != v2.length)
            return -1;

        int dot_product = dotProduct(v1,v2);

        double v1Magnitude = magnitude(v1);
        double v2Magnitude = magnitude(v2);

        System.out.printf("Dot product: %d\n", dot_product);
        System.out.printf("Magnitude1: %.4f\n", v1Magnitude);
        System.out.printf("Magnitude2: %.4f\n", v2Magnitude);

        double result = ((double)dot_product) / (v1Magnitude * v2Magnitude);
        System.out.printf("Cosine Similarity: %.4f\n", result);

        return result;
    }

    public static String removePunctuation(String s)
    {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
           char letter = sb.charAt(i); 

           if(  !Character.isAlphabetic(letter) && 
                !Character.isSpaceChar(letter))
                sb.deleteCharAt(i);

        }
        return sb.toString();
    }

    public HashMap<String,Integer> wordsToMap(String text)
    {
        
        return new HashMap<String, Integer>();
    }
}
