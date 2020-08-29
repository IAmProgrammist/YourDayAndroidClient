package rchat.info.yourday_new.containers.events;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Reminder extends Event {
    public String desc;

    public Reminder(String eventName, Date date, String desc, Uri pic) {
        super(eventName, EventType.REMINDER, date, pic);
        this.desc = desc;
    }

    public Reminder(JSONObject o) throws JSONException {
        super(o);
        if (o.has("desc")) {
            desc = o.getString("desc");
        } else {
            desc = "";
        }
    }

    @Override
    public JSONObject getJSONObject() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("name", eventName);
        res.put("event", eventType.toString());
        res.put("date", date.getTime());
        if (desc.equals("")) {
        } else {
            res.put("desc", desc);
        }
        if (pic != null) {
            String imageEncoded = pic.toString();
            res.put("image", imageEncoded);
        }
        return res;
    }
}
