
package MK.CVForYou;

import java.nio.file.*;

public class CVGenerationArgs
{

    public JobSource jd_source; 
    public Path input_document; 
    public Path[] section_definition_paths;
    public Path output_directory;

    public JobSource getJDSource()
    {
        return jd_source;
    }

    public Path getInputDocument()
    {
        return input_document;
    }

    public Path[] getSections() {
        return section_definition_paths;
    }

    public Path getOutputFolder() {
        return output_directory;
    }

    public void setOutputFolder(String path) {
        output_directory = Paths.get(path);
    }
}
