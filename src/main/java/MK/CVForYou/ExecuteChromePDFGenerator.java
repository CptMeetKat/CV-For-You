package MK.CVForYou;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecuteChromePDFGenerator {

    public static void run(String num, Path output_directory) {
        try {
            String base = output_directory.toAbsolutePath().toString();
            
            String pdf_name = "cv" + num + ".pdf";
            Path print_to_pdf = Paths.get(output_directory.toString(), pdf_name);
            Path input_file = Paths.get(base, num + ".html"); 

            ProcessBuilder processBuilder = new ProcessBuilder("google-chrome", "--no-sandbox", "--headless", "--disable-gpu", "--print-to-pdf-first-page" , "--no-pdf-header-footer", "--print-to-pdf=" + print_to_pdf , input_file.toString());

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
