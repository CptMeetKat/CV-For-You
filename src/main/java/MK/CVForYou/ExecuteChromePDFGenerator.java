package MK.CVForYou;

public class ExecuteChromePDFGenerator {

    //TODO: This needs an acceptance test

    public static void run(String num, String output_directory) {
        try {
            String currentDir = System.getProperty("user.dir");
            String base = "file://" + currentDir + "/" + output_directory;

            ProcessBuilder processBuilder = new ProcessBuilder("google-chrome", "--no-sandbox", "--headless", "--disable-gpu", "--print-to-pdf-first-page" , "--no-pdf-header-footer", "--print-to-pdf=./" + output_directory + "cv" + num + ".pdf", base+num+".html");

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
