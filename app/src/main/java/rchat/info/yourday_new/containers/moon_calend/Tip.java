package rchat.info.yourday_new.containers.moon_calend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rchat.info.yourday_new.containers.day.Description;

public class Tip {
    public String heading;
    public String descritpion;
    public TipTypes type;
    public List<Description> fullDesc;

    public Tip(String heading, String descritpion, List<Description> fullDesc, TipTypes type) {
        this.heading = heading;
        this.descritpion = descritpion;
        this.fullDesc = fullDesc;
        this.type = type;
    }

    public Tip(JSONObject o) throws JSONException {
        heading = o.getString("heading");
        descritpion = o.getString("description");
        JSONArray array = o.getJSONArray("fullDesc");
        fullDesc = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            fullDesc.add(new Description(array.getJSONObject(i)));
        }
        type = TipTypes.valueOf(o.getString("type"));
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("heading", heading);
        result.put("description", descritpion);
        JSONArray array = new JSONArray();
        for (Description a : fullDesc) {
            array.put(a.createJSON());
        }
        result.put("fullDesc", array);
        result.put("type", type);
        return result;
    }
}
