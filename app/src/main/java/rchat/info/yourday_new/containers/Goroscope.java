package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

public class Goroscope {
    private String sign;
    private String prediction;

    public Goroscope(JSONObject o) throws JSONException {
        this.sign = o.getString("sign");
        this.prediction = o.getString("prediction");
    }

    public String getSign() {
        return sign;
    }

    public String getPrediction() {
        return prediction;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject res = new JSONObject();
        res.put("sign", sign);
        res.put("prediction", prediction);
        return res;
    }

}
