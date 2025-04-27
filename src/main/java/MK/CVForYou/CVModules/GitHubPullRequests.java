package MK.CVForYou.CVModules;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import MK.CVForYou.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GitHubPullRequests
{
    static final Logger logger = LoggerFactory.getLogger(GitHubPullRequests.class);
    public GitHubPullRequests()
    {
        System.out.println("Hello from PRs module!");
        readConfig();
    }

    private void readConfig()
    {
        try {
            System.out.println(System.getProperty("user.dir"));
			String username = IOUtils.readFile("./target/classes/GithubPullRequestsConfig");
            logger.info("Obtaining GitHub information for user {}", username);

            fetchPullRequests(username);

		} catch (IOException e) {
            logger.warn("Unable to read username of target GitHub account: {}", e.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void fetchPullRequests(String username) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/search/issues?q=author:" + username + "+type:pr+is:merged"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("content-type", "application/json") 
            .build(); 

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);
        System.out.println(response.body());
    }
}
