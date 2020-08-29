package rchat.info.yourday_new.containers.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private String step;
    private List<String> images = new ArrayList<>();

    public Step(String step, List<String> images) {
        this.step = step;
        this.images = images;
    }

    public Step(JSONObject o) throws JSONException {
        step = o.getString("step");
        JSONArray array = o.getJSONArray("images");
        for (int i = 0; i < array.length(); i++) {
            images.add(array.getString(i));
        }
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("step", step);
        JSONArray array = new JSONArray();
        for (String img : images) {
            array.put(img);
        }
        res.put("images", array);
        return res;
    }


}
