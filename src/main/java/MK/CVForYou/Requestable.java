package MK.CVForYou;


import org.json.JSONObject;

public interface Requestable
{
    public JSONObject request(String access_token);
}
