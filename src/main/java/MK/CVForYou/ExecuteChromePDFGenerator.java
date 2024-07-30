package MK.CVForYou;

public class ExecuteChromePDFGenerator {

    public static void run(String num) {
        try {
            String currentDir = System.getProperty("user.dir");
            String base = "file://" + currentDir + "/prod_components/";
            ProcessBuilder processBuilder = new ProcessBuilder("google-chrome", "--no-sandbox", "--headless", "--disable-gpu", "--print-to-pdf-first-page" , "--no-pdf-header-footer", "--print-to-pdf=./prod_components/cv" + num + ".pdf", base+num+".html");

            Process process = processBuilder.start();
            
            //Print output
            //BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            //String line;
            //while ((line = reader.readLine()) != null) {
            //    System.out.println(line);
            //}

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
