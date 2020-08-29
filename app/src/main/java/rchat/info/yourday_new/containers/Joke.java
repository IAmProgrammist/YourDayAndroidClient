package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

public class Joke {
    public String joke;

    public Joke(JSONObject o) throws JSONException {
        joke = o.getString("joke");
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("joke", joke);
        return object;
    }
}
