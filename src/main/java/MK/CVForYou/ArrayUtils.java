
package MK.CVForYou;


public class ArrayUtils
{
    public static String[] popCopy(String[] arr)
    {
        String[] copy = new String[arr.length-1];
        for(int i = 0; i < copy.length; i++)
        {
            copy[i] = arr[i+1];
        }
        return copy;
    }
}
