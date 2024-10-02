package MK.CVForYou;

import java.io.File;
import java.io.IOException;

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
    private static final String BASE_DIR = "./target/test-artefacts/GenerateDocumentAcceptanceTest/";
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
    public void shouldGeneratePDFWithDynamicSectionsWhenSourceIsFile()
    throws IOException
    {
        String[] args = new String[]{
                "-cv",
                "-d", "src/test/test_files/AcceptanceTest/1/document.html",
                "-c", "src/test/test_files/AcceptanceTest/1/description.txt",
                "-sd", "src/test/test_files/AcceptanceTest/1/sections/", 
                "-o", testDirectory.toString()
        };
        App.main(args);

        String expected = "Projects\nName: Project-C\nLanguage: C++\nName: Project-P\nLanguage: Python\n" 
                 + "Name: Project-J\nLanguage: Java\nTags\nC++\nPYTHON\nJAVA\n";

        File file = new File(testDirectory.toString(), "cvgenerated_document.pdf");
        PDDocument document = Loader.loadPDF(file);

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);

        assertEquals(expected, text); 
    }

    @Test
    public void shouldGenerateDynamicPDFWhenSourceIsFromSeekAndCached()
    throws IOException
    {
        String cache_id = "78678834";
        String[] args = new String[]{
                "-cv",
                "-d", "src/test/test_files/AcceptanceTest/2/document.html",
                "-cc", "src/test/test_files/AcceptanceTest/2/cache/" + cache_id,
                "-sd", "src/test/test_files/AcceptanceTest/2/sections/", 
                "-o", testDirectory.toString()
        };
        App.main(args);

        String expected = "Professional Title\nSoftware Developer\nTags\nSQL\nC#\nJAVA\n";

        File file = new File(testDirectory.toString(), "cv" + cache_id + ".pdf");
        PDDocument document = Loader.loadPDF(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        assertEquals(expected, text); 
    }
}
