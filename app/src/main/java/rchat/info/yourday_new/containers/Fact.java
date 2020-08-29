package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

public class Fact {
    String fact;

    public Fact(JSONObject o) throws JSONException {
        this.fact = o.getString("fact");
    }

    public String getFact() {
        return fact;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("fact", fact);
        return object;
    }
}
