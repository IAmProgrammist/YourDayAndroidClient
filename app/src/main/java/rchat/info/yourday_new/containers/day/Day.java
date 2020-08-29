package rchat.info.yourday_new.containers.day;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Day{
    private Date date;
    private boolean isWeekend;
    private String presentName;
    private Present present;
    private String presentChurchName;

    public Day(JSONObject object) throws JSONException {
        date = new Date(object.getLong("long"));
        isWeekend = object.getBoolean("isWeekend");
        presentName = object.getString("presentName");
        present = new Present(object.getJSONObject("present"));
        presentChurchName = object.getString("presentChurchName");
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("long", date.getTime());
        result.put("isWeekend", isWeekend);
        result.put("presentName", presentName);
        result.put("presentChurchName", presentChurchName);
        result.put("present", present.createJSON());
        return result;
    }

}
