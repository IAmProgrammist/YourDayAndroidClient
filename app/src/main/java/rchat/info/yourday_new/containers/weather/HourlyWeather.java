package rchat.info.yourday_new.containers.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HourlyWeather {
    public String date;
    public String imgName;
    int temp;
    int wind_speed;
    int wind_deg;
    int humidity;
    double osadki;

    public HourlyWeather(JSONObject o) throws JSONException {
        Date dt = new Date(o.getLong("dt") * 1000);
        TimeZone asiaSingapore = TimeZone.getDefault();
        Calendar current = Calendar.getInstance(asiaSingapore);
        current.setTime(dt);
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm; dd MMMM", Locale.getDefault());
        date = format1.format(current.getTime());
        if (o.has("temp")) {
            temp = (int) o.getDouble("temp");
        } else {
            temp = -300;
        }
        if (o.has("wind_speed")) {
            wind_speed = o.getInt("wind_speed");
        } else {
            wind_speed = -1;
        }
        if (o.has("wind_deg")) {
            wind_deg = o.getInt("wind_deg");
        } else {
            wind_deg = -1;
        }
        if (o.has("humidity")) {
            humidity = o.getInt("humidity");
        } else {
            humidity = -1;
        }
        if (o.has("rain")) {
            try {
                osadki = o.getDouble("rain");
            } catch (JSONException e) {
                if (o.getJSONObject("rain").has("1h")) {
                    osadki = o.getJSONObject("rain").getDouble("1h");
                } else {
                    osadki = 0;
                }
            }
        } else if (o.has("snow")) {
            try {
                osadki = o.getDouble("snow");
            } catch (JSONException e) {
                if (o.getJSONObject("snow").has("1h")) {
                    osadki = o.getJSONObject("snow").getDouble("1h");
                } else {
                    osadki = 0;
                }
            }
        } else {
            osadki = -1;
        }
        imgName = "w" + o.getJSONArray("weather").getJSONObject(0).getString("icon");
    }

    public String getTemp() {
        return String.valueOf((int) (temp)) + "°C";
    }

    public String getWindDesc() {
        String res = "";
        if (wind_speed != -1) {
            res += wind_speed + " м/с, ";
        }
        if (wind_deg != -1) {
            res += getDegName(wind_deg) + "  ";
        }
        if (res.equals("")) {
            return "Ветра нет";
        } else {
            return res.substring(0, res.length() - 2);
        }
    }

    private String getDegName(int a) {
        String res = "";
        if (a > 16 && a <= 67) {
            res = "СВ";
        } else if (a > 67 && a <= 112) {
            res = "В";
        } else if (a > 112 && a <= 157) {
            res = "ЮВ";
        } else if (a > 157 && a <= 187) {
            res = "Ю";
        } else if (a > 187 && a <= 247) {
            res = "ЮЗ";
        } else if (a > 247 && a <= 292) {
            res = "З";
        } else if (a > 292 && a <= 337) {
            res = "СЗ";
        } else {
            res = "С";
        }
        return res;
    }

    public String getWaterDesc() {
        String res = "";
        if (humidity != -1) {
            res += humidity + " %, ";
        }
        if (osadki != -1) {
            res += osadki + " мм; ";
        }
        return res.substring(0, res.length() - 2);
    }
}
