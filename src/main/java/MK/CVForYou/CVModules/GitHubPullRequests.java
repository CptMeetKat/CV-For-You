package MK.CVForYou.CVModules;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import MK.CVForYou.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GitHubPullRequests
{
    static final Logger logger = LoggerFactory.getLogger(GitHubPullRequests.class);
    String username;
    public GitHubPullRequests()
    {
        System.out.println("Hello from PRs module!");
        readConfig();
        run();
    }

    private void run()
    {
        try {
			HttpResponse<String> response = fetchPullRequests(username);
            if( response.statusCode() != 200)
                logger.warn("{} response code received, unable to calculate pull requests");
            else
                calculatePullRequests(response.body());

		} catch (IOException e) {
            logger.warn("Unable to obtain GitHub pull requests");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    private void calculatePullRequests(String body)
    {
        JSONObject json = new JSONObject(body);
        JSONArray pull_requests = (JSONArray) json.query("/items");
        if(pull_requests == null)
            return;
            
        Iterator<Object> pr_itr = pull_requests.iterator();
        while(pr_itr.hasNext())
        {
            JSONObject pr = (JSONObject)pr_itr.next();
            String title = pr.getString("title");
            String author_association = pr.getString("author_association");
            String state = pr.getString("state");
            String repository_url = pr.getString("repository_url");
            String repo_name = repository_url.substring(repository_url.lastIndexOf("/")+1); //TODO: This could be made safer
            if(author_association.equals("CONTRIBUTOR"))
                System.out.printf("%s %s: %s\n", repo_name, state, title);
        }
    }

    private void readConfig()
    {
        try {
            System.out.println(System.getProperty("user.dir"));
			username = IOUtils.readFile("./target/classes/GithubPullRequestsConfig");
            logger.info("Obtaining GitHub information for user {}", username);
		} catch (IOException e) {
            logger.warn("Unable to read username of target GitHub account: {}", e.toString());
		}
    }

    private HttpResponse<String> fetchPullRequests(String username) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/search/issues?q=author:" + username + "+type:pr+is:merged"))
            .header("accept", "*/*")
            .header("accept-language", "en-GB,en-US;q=0.9,en;q=0.8")
            .header("content-type", "application/json") 
            .build(); 

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
