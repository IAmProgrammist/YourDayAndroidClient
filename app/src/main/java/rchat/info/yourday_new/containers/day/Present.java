package rchat.info.yourday_new.containers.day;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Present {
    public String name;
    public List<Description> description = new ArrayList<>();

    public Present(JSONObject o) throws JSONException {
        name = o.getString("name");
        JSONArray array = o.getJSONArray("description");
        for (int i = 0; i < array.length(); i++) {
            description.add(new Description(array.getJSONObject(i)));
        }
    }

    public boolean hasDesc() {
        return description.get(0).type != Type.NO_INFO;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("name", name);
        JSONArray array = new JSONArray();
        for (Description d : description) {
            array.put(d.createJSON());
        }
        o.put("description", array);
        return o;
    }
}
