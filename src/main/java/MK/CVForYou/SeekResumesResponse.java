package MK.CVForYou;

import org.json.JSONObject;

public class SeekResumesResponse
{
    public String name;
    public String created;
    public String size;
    public String id;
    public boolean isDefault;
    public String virusScanStatus;
    public String uri;

    public SeekResumesResponse(JSONObject response)
    {
        id = JSONHelpers.getString(response, "id");
        created = JSONHelpers.getString(response, "createdDateUtc");
        isDefault = response.optBoolean("isDefault");
        name = JSONHelpers.getStringInObject(response, "fileMetadata", "name");
        size = JSONHelpers.getStringInObject(response, "fileMetadata", "size");
        virusScanStatus = JSONHelpers.getStringInObject(response, "fileMetadata", "virusScanStatus");
        uri = JSONHelpers.getStringInObject(response, "fileMetadata", "uri");
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("name: %s\n",name));
        sb.append(String.format("created: %s\n",created));
        sb.append(String.format("size: %s\n",size));
        sb.append(String.format("id: %s\n", id));
        sb.append(String.format("isDefault: %b\n",isDefault));
        sb.append(String.format("virusScanStatus: %s\n",virusScanStatus));
        sb.append(String.format("uri: %s\n",uri));

        return sb.toString();
    }
}



