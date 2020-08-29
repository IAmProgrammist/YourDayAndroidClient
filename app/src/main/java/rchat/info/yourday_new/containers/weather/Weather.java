package rchat.info.yourday_new.containers.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weather {

    public CurrentWeather currentWeather;
    public ArrayList<HourlyWeather> hourly;
    public ArrayList<DailyWeather> daily;

    public Weather(JSONObject o) throws JSONException {
        hourly = new ArrayList<>();
        daily = new ArrayList<>();
        currentWeather = new CurrentWeather(o.getJSONObject("current"));
        JSONArray hourly = o.getJSONArray("hourly");
        for (int i = 0; i < hourly.length(); i++) {
            this.hourly.add(new HourlyWeather(hourly.getJSONObject(i)));
        }
        JSONArray daily = o.getJSONArray("daily");
        for (int i = 0; i < daily.length(); i++) {
            this.daily.add(new DailyWeather(daily.getJSONObject(i)));
        }
    }
}
