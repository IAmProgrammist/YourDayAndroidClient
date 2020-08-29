package rchat.info.yourday_new.containers.historical;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistEvent {
    public String urlImage;
    public String date;
    public List<String> desc;

    public HistEvent(JSONObject o) {
        try {
            urlImage = o.getString("urlImage");
            date = o.getString("date");
            JSONArray array = o.getJSONArray("desc");
            desc = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                desc.add(array.getString(i));
            }
        } catch (JSONException e) {

        }
    }
}
