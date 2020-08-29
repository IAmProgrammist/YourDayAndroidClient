package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

public class Update {
    public String version;
    public String desc;

    public Update(JSONObject o) {
        try {
            version = o.getString("ver");
            desc = o.getString("desc");
        } catch (JSONException ignore) {

        }
    }
}
