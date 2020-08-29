package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Human implements Serializable {
    String name;
    String description;
    String imageUrl;

    public Human(JSONObject o) throws JSONException {
        name = o.getString("name");
        description = o.getString("description");
        imageUrl = o.getString("imageUrl");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject a = new JSONObject();
        a.put("name", name);
        a.put("description", description);
        a.put("imageUrl", imageUrl);
        return a;
    }

}
