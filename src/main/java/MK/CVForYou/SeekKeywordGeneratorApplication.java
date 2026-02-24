package MK.CVForYou;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        ArrayList<Path> seek_jobs = getAllFilesInFolder(cache_directory);
        JobFromCache cached_jobs = new JobFromCache(seek_jobs);
        ArrayList<InputJob> jobs = cached_jobs.getJobModel();
        List<String> unique_words = getAllUniqueWords(jobs);

        System.out.println(String.join("\n", unique_words));
	}

    private List<String> getAllUniqueWords(ArrayList<InputJob> jobs)
    {
        HashSet<String> word_map = new HashSet<>();
        for (InputJob job : jobs) {
            if(job.job_description != null)
            {
                String text = job.job_description.replaceAll("[^\\w\\s+-]", " ").toLowerCase();
                String[] split_text = text.split("[-., \n]");
                for(String word : split_text) {
                    if(!word.strip().isEmpty())
                        word_map.add(word.strip());
                }
            }
        }
        return new ArrayList<>(word_map);
    }
    
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

