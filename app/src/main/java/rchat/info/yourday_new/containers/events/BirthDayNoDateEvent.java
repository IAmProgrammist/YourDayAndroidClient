package rchat.info.yourday_new.containers.events;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class BirthDayNoDateEvent extends Event {
    public BirthDayNoDateEvent(String eventName, Date date, Uri pic) {
        super(eventName, EventType.BIRTHDAY_NO_DATE, date, pic);
    }

    public BirthDayNoDateEvent(JSONObject o) throws JSONException {
        super(o);
    }
}
