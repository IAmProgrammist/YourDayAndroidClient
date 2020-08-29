package rchat.info.yourday_new.containers.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentWeather {
    public Date date;
    public double temp;
    public double feels_like;
    public int pressure;
    public int visibility;
    public int humidity;
    public int clouds;
    public double wind_speed;
    public int wind_deg;
    public double osadki;
    public List<String> weather_ids;


    public CurrentWeather(JSONObject o) {
        try {
            weather_ids = new ArrayList<>();
            if (o.has("temp")) {
                temp = o.getDouble("temp");
            } else {
                temp = -300;
            }
            if (o.has("feels_like")) {
                feels_like = o.getDouble("feels_like");
            } else {
                feels_like = -300;
            }
            if (o.has("pressure")) {
                pressure = o.getInt("pressure");
            } else {
                pressure = -1;
            }
            if (o.has("visibility")) {
                visibility = o.getInt("visibility");
            } else {
                visibility = -1;
            }
            if (o.has("dt")) {
                long lng = o.getLong("dt") * 1000;
                date = new Date(lng);
                date.setMinutes(0);
                date.setSeconds(0);
            } else {
                date = new Date();
            }
            if (o.has("humidity")) {
                humidity = o.getInt("humidity");
            } else {
                humidity = -1;
            }
            if (o.has("clouds")) {
                clouds = o.getInt("clouds");
            } else {
                clouds = -1;
            }
            if (o.has("wind_speed")) {
                wind_speed = o.getDouble("wind_speed");
            } else {
                wind_speed = -1;
            }
            if (o.has("wind_deg")) {
                wind_deg = o.getInt("wind_deg");
            } else {
                wind_deg = -1;
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
            JSONArray wthr = o.getJSONArray("weather");
            for (int i = 0; i < wthr.length(); i++) {
                JSONObject object = wthr.getJSONObject(i);
                weather_ids.add(object.getString("icon"));
            }
        } catch (JSONException e) {
            e = e;
            e = e;
        }
    }

    public String getTemp() {
        return String.valueOf((int) (temp)) + "°C";
    }

    public String getFeelsLike() {
        return "Ощущается как " + String.valueOf((int) (feels_like)) + "°C";
    }

    public String getWindDesc() {
        String res = "";
        if (wind_speed != -1) {
            res += "Скорость ветра: " + wind_speed + " м/с; ";
        }
        if (wind_deg != -1) {
            res += "Направление: " + getDegName(wind_deg) + "; ";
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

    public String getCloudsDesc() {
        String res = "";
        if (clouds != -1) {
            res += "Облачность: " + clouds + " %; ";
        }
        if (visibility != -1) {
            res += "Видимость: " + visibility + " м; ";
        }
        if (res.equals("")) {
            return "Информации нет";
        } else {
            return res.substring(0, res.length() - 2);
        }
    }

    public String getWaterDesc() {
        String res = "";
        if (humidity != -1) {
            res += "Влажность: " + humidity + " %; ";
        }
        if (osadki != -1) {
            res += "Осадки: " + osadki + " мм; ";
        } else {
            res += "Осадки не предвидятся; ";
        }
        return res.substring(0, res.length() - 2);
    }

    public String getPressure() {
        if (pressure != -1) {
            return ((int) (pressure / 1.333)) + " мм. рт.ст.";
        } else {
            return "Информации нет";
        }
    }

}
