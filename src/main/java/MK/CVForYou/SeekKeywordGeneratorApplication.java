package MK.CVForYou;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeekKeywordGeneratorApplication implements Application
{
    static final Logger logger = LoggerFactory.getLogger(SeekKeywordGeneratorApplication.class);
    Path cache_directory = Paths.get("./cache/");

	@Override
	public void run() {
        logger.info("Hello world");
        ArrayList<Path> seek_jobs = getAllFilesInFolder(cache_directory);
        for(Path p : seek_jobs)
        {
            System.out.println(p.toString());
        }
        logger.info("end");
	}

   // private void getAllUniqueWordsInJobDescription(Path seek_job_html)
   // {
   //     ArrayList<String> paths = getAllSeekJobsInFolder(cache_directory);
   //     //SeekJobParser
   // }
    
    private ArrayList<Path> getAllFilesInFolder(Path folder)
    {
        ArrayList<Path> files = new ArrayList<>();
        try (Stream<Path> paths = Files.list(folder)) {
            paths.filter(Files::isRegularFile)
                .forEach(p -> {
                    System.out.println("File: " + p.getFileName());
                    Path job_location = folder.resolve(p.getFileName());
                    files.add(job_location);
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

	@Override
	public <T> void setDependency(T service, Class<T> serviceType) {
		throw new UnsupportedOperationException("Unimplemented method 'setDependency'");
	}
}

