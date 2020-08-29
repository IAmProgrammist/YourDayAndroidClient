package rchat.info.yourday_new.containers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Omen {
    public List<String> omens = new ArrayList<>();

    public Omen(JSONObject o) throws JSONException {
        JSONArray array = o.getJSONArray("omens");
        for (int i = 0; i < array.length(); i++) {
            omens.add(array.getString(i));
        }
    }

    public JSONObject createJSON() throws JSONException {
        JSONArray array = new JSONArray();
        for (String a : omens) {
            array.put(a);
        }
        JSONObject res = new JSONObject();
        res.put("omens", array);
        return res;
    }
}
