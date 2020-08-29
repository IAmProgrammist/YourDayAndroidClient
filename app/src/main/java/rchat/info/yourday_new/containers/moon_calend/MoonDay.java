package rchat.info.yourday_new.containers.moon_calend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rchat.info.yourday_new.containers.day.Description;

public class MoonDay {
    public MainDescription description;
    public List<Tip> tips;

    public MoonDay(JSONObject o) throws JSONException {
        description = new MainDescription(o.getJSONObject("description"));
        JSONArray array = o.getJSONArray("tips");
        tips = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject t = array.getJSONObject(i);
            addTip(t);
        }
    }

    public void addTip(JSONObject o) throws JSONException {
        tips.add(new Tip(o));
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("description", description.createJSON());
        JSONArray array = new JSONArray();
        for (Tip a : tips) {
            array.put(a.createJSON());
        }
        result.put("tips", array);
        return result;
    }
}
