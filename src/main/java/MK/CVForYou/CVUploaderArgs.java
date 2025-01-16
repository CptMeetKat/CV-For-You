
package MK.CVForYou;

import java.io.File;
import java.util.ArrayList;

public class CVUploaderArgs
{
    public ArrayList<File> files = new ArrayList<File>();
    public int mode;

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public void addFile(String path)
    {
        files.add(new File(path));
    }

    public void addFiles(String paths[])
    {
        for(String p : paths)
            files.add(new File(p));
    }
}
