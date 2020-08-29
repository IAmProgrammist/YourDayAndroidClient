package rchat.info.yourday_new.containers.historical;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistDay {
    public List<HistEvent> histEvents = new ArrayList<>();

    public HistDay(JSONObject o) {
        try {
            JSONArray array = o.getJSONArray("histEvents");
            for (int i = 0; i < array.length(); i++) {
                histEvents.add(new HistEvent(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e = e;
            e.printStackTrace();
        }
    }
}
