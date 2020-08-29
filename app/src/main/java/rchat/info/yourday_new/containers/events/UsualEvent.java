package rchat.info.yourday_new.containers.events;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class UsualEvent extends Event {
    public UsualEvent(String eventName, Date date, Uri pic) {
        super(eventName, EventType.EVENT, date, pic);
    }

    public UsualEvent(JSONObject o) throws JSONException {
        super(o);
    }
}
