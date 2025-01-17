package MK.CVForYou;


import java.io.IOException;

import org.json.JSONObject;

public interface Requestable
{
    public JSONObject request(String access_token) throws IOException, InterruptedException; //TODO Requestable should have been written to return HTTPResponse<String> instead of just the bodys
}
