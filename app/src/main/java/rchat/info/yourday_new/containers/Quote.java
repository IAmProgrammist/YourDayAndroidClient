package rchat.info.yourday_new.containers;

import org.json.JSONException;
import org.json.JSONObject;

public class Quote {
    String quote;
    String quoter;

    public Quote(JSONObject o) throws JSONException {
        quote = o.getString("quote");
        quoter = o.getString("quoter");
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public JSONObject createJSON() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("quoter", quoter);
        o.put("quote", quote);
        return o;
    }

    public String getQuoter() {
        return quoter;
    }
}
