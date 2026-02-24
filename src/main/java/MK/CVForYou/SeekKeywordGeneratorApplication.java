package MK.CVForYou;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
        List< Entry<String, Integer>> unique_words = getAllUniqueWords(jobs);
        unique_words.sort(Entry.<String, Integer>comparingByValue().reversed());

        StringBuilder sb = new StringBuilder();

        for (Entry<String,Integer> e : unique_words) {
            System.out.println(e.getValue() + " " + e.getKey() ); 
            sb.append(e.getValue() + " " + e.getKey() + "\n");
        }

        IOUtils.writeToFile(sb.toString().strip(), "./wordlist.txt");
	}

    private ArrayList<Entry<String, Integer>> getAllUniqueWords(ArrayList<InputJob> jobs)
    {
        HashMap<String, Integer> word_map = new HashMap<>();
        for (InputJob job : jobs) {
            if(job.job_description != null)
            {
                List<String> tokens = SeekJobDescriptionTokenizer.tokenize(job.job_description);

                for(String word : tokens) {
                    if(!word.isEmpty()) {
                        if(word_map.containsKey(word))
                            word_map.put(word, word_map.get(word) + 1);
                        else
                            word_map.put(word, 1);
                    }
                }
            }
        }
        return new ArrayList<Map.Entry<String, Integer>>(word_map.entrySet());
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

