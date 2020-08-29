package rchat.info.yourday_new.containers.events;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class BirthDayEvent extends Event {
    public BirthDayEvent(String eventName, Date date, Uri pic) {
        super(eventName, EventType.BIRTHDAY, date, pic);
    }

    public BirthDayEvent(JSONObject o) throws JSONException {
        super(o);
    }
}
