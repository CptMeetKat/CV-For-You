package MK.CVForYou;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public class GenerateDocumentAcceptanceTest 
{
    private static final String BASE_DIR = "./target/test-artefacts/";
    private File testDirectory;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() {
        // Create a directory for all acceptance test method
        String methodName = testName.getMethodName();
        testDirectory = new File(BASE_DIR, methodName);
        if (!testDirectory.exists()) {
            testDirectory.mkdirs();
        }
    }


    @Test
    public void shouldGeneratePDFWithDynamicSections()
    throws IOException
    {
        String[] args = new String[]{
                "-d", "src/test/test_files/AcceptanceTest/document.html",
                "-c", "src/test/test_files/AcceptanceTest/description.txt",
                "-sd", "src/test/test_files/AcceptanceTest/sections/", 
                "-o", testDirectory.toString()
        };
        App.main(args);

        String expected = "Projects\nName: Project-C\nLanguage: C++\nName: Project-P\nLanguage: Python\n" 
                 + "Name: Project-J\nLanguage: Java\nTags\nC++\nPYTHON\nJAVA\n";

        File file = new File(testDirectory.toString(), "cvgenerated_document.pdf");
        PDDocument document = Loader.loadPDF(file);

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        System.out.println(text);
        System.out.println(expected);

        assertEquals(expected, text); 
    }
}
