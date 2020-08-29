package rchat.info.yourday_new.containers.events;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public abstract class Event {
    String eventName;
    EventType eventType;
    Date date;
    Uri pic;

    public Event(String eventName, EventType eventType, Date date, Uri pic) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.date = date;
        this.pic = pic;
    }

    public Event(JSONObject o) throws JSONException {
        this.eventName = o.getString("name");
        this.eventType = EventType.valueOf(o.getString("event"));
        this.date = new Date(o.getLong("date"));
        if (o.has("image")) {
            String input = o.getString("image");
            pic = Uri.parse(input);
        } else {
            pic = null;
        }
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Uri getPic() {
        return pic;
    }

    public void setPic(Uri pic) {
        this.pic = pic;
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("name", eventName);
        res.put("event", eventType.toString());
        res.put("date", date.getTime());
        if (pic != null) {
            String imageEncoded = pic.toString();
            res.put("image", imageEncoded);
        }
        return res;
    }
}
