package rchat.info.yourday_new.containers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Names {
    public List<String> girls;
    public List<String> boys;

    public Names(JSONObject o) throws JSONException {
        boys = new ArrayList<>();
        girls = new ArrayList<>();
        JSONArray p = o.getJSONArray("boys");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            boys.add(k);
        }
        p = o.getJSONArray("girls");
        for (int i = 0; i < p.length(); i++) {
            String k = p.getString(i);
            girls.add(k);
        }
    }
}