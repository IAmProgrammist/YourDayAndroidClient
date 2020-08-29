package rchat.info.yourday_new.containers.day;

import org.json.JSONException;
import org.json.JSONObject;

public class Description {
    public Type type;
    public String desc;

    public Description() {
        type = Type.NO_INFO;
        desc = "NO_INFO";
    }

    public Description(JSONObject jsonObject) {
        try {
            type = Type.valueOf(jsonObject.getString("type"));
            desc = jsonObject.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("type", type);
        o.put("desc", desc);
        return o;
    }
}
