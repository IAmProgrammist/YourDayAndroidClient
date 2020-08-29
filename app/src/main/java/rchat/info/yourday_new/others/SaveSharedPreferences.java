package rchat.info.yourday_new.others;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rchat.info.yourday_new.containers.events.BirthDayEvent;
import rchat.info.yourday_new.containers.events.BirthDayNoDateEvent;
import rchat.info.yourday_new.containers.events.Event;
import rchat.info.yourday_new.containers.events.EventType;
import rchat.info.yourday_new.containers.events.Reminder;
import rchat.info.yourday_new.containers.events.UsualEvent;
import rchat.info.yourday_new.containers.recipe.NewRecipe;
import rchat.info.yourday_new.containers.recipe.NewRecipeType;
import rchat.info.yourday_new.containers.recipe.NewRecipes;

import static rchat.info.yourday_new.others.Types.TODAY;

public class SaveSharedPreferences {
    static final String SIGN = "sign";
    static final String DAYS_INFO = "days_info";
    static final String WEATHER_INFO = "wthr_info";
    static final String ACCESED = "get_accesed_rows";
    static final String RECIPE_MODES = "recipe_modes";
    static final String NEW_RECIPE = "new_recipe";
    static final String FAVORITE_RECIPE = "favorite_recipes";
    static final String LAST_PUSH_UP = "last_push_up";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getLastPushUp(Context ctx) {
        String a = getSharedPreferences(ctx).getString(LAST_PUSH_UP, "");
        return a;
    }

    public static void setLastPushUp(Context ctx, String pushDate) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LAST_PUSH_UP, pushDate);
        editor.commit();
    }

    private static JSONObject getAllFavoriteRecipes(Context ctx) throws JSONException {
        String a = getSharedPreferences(ctx).getString(FAVORITE_RECIPE, "");
        if (a.equals("")) {
            throw new NullPointerException("No favorite recipes");
        }
        return new JSONObject(a);
    }

    public static NewRecipes getAllFavoriteNewRecipes(Context ctx) throws JSONException {
        JSONObject a = getAllFavoriteRecipes(ctx);
        JSONArray array = a.getJSONArray("recipes");
        NewRecipes recipes = new NewRecipes();
        for (int i = 0; i < array.length(); i++) {
            NewRecipe recipe = new NewRecipe(array.getJSONObject(i).getString("name"), array.getJSONObject(i).getString("url"), array.getJSONObject(i).getString("everything"), NewRecipeType.valueOf(array.getJSONObject(i).getString("type")));
            recipes.addRecipe(recipe);
        }
        return recipes;
    }

    public static void addFavoriteRecipe(Context ctx, NewRecipe recipe) {
        try {
            JSONObject object = getAllFavoriteRecipes(ctx);
            JSONArray recipes = object.getJSONArray("recipes");
            JSONObject whatToAdd = recipe.createJSON();
            recipes.put(whatToAdd);
            object.remove("recipes");
            object.put("recipes", recipes);
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(FAVORITE_RECIPE, object.toString());
            editor.commit();
        } catch (NullPointerException e) {
            try {
                JSONObject root = new JSONObject();
                JSONArray array = new JSONArray().put(recipe.createJSON());
                root.put("recipes", array);
                SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
                editor.putString(FAVORITE_RECIPE, root.toString());
                editor.commit();
            } catch (JSONException ee) {

            }
        } catch (JSONException e) {
            //wha... HOW?
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void removeFavoriteRecipe(Context ctx, NewRecipe recipe) {
        try {
            JSONObject root = getAllFavoriteRecipes(ctx);
            JSONArray array = root.getJSONArray("recipes");
            JSONObject rec = recipe.createJSON();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                if (o.getString("name").equalsIgnoreCase(rec.getString("name")) && o.getString("everything").equalsIgnoreCase(rec.getString("everything"))) {
                    array.remove(i);
                    break;
                }
            }
            root.remove("recipes");
            root.put("recipes", array);
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(FAVORITE_RECIPE, root.toString());
            editor.commit();
        } catch (NullPointerException e) {
            return;
        } catch (JSONException e) {
            return;
        }
    }

    public static boolean isFavorite(Context ctx, NewRecipe recipe) {
        try {
            JSONObject root = getAllFavoriteRecipes(ctx);
            JSONArray array = root.getJSONArray("recipes");
            JSONObject rec = recipe.createJSON();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                if (o.getString("name").equalsIgnoreCase(rec.getString("name")) && o.getString("everything").equalsIgnoreCase(rec.getString("everything"))) {
                    return true;
                }
            }
            return false;
        } catch (NullPointerException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }
    }

    public static void setIsNewRecipeMode(Context ctx, Boolean b) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(RECIPE_MODES, b.toString());
        editor.commit();
    }

    public static void setNewRecipe(Context ctx, String a) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(NEW_RECIPE, a);
        editor.commit();
    }

    public static JSONObject getNewRecipe(Context ctx) throws JSONException {
        String a = getSharedPreferences(ctx).getString(NEW_RECIPE, "");
        if (a.equals("")) {
            throw new NullPointerException("No recipe wasnt generated yet");
        }
        return new JSONObject(a);
    }

    public static Boolean getIsNewRecipeMode(Context ctx) throws NullPointerException {
        String a = getSharedPreferences(ctx).getString(RECIPE_MODES, "");
        if (a.equals("")) {
            throw new NullPointerException("No recipe mode setted");
        }
        return Boolean.parseBoolean(a);
    }

    public static void setSign(Context ctx, String sign) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SIGN, sign);
        editor.commit();
    }

    public static void clearSign(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(SIGN, "");
        editor.commit();
    }

    public static String getSign(Context ctx) {
        String a = getSharedPreferences(ctx).getString(SIGN, "");
        if (a.equals("") || a.equals("unchosen")) {
            return null;
        }
        return a;
    }

    public static void setWeatherInfo(Context ctx, String cityName, LatLng latLng) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            JSONObject object = new JSONObject();
            object.put("cityname", cityName);
            object.put("latitude", latLng.latitude);
            object.put("longitude", latLng.longitude);
            editor.putString(WEATHER_INFO, object.toString());
            editor.commit();
        } catch (JSONException e) {

        }
    }

    public static void clearWeatherInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(WEATHER_INFO, "");
        editor.commit();
    }

    public static String getCityName(Context ctx) {
        try {
            JSONObject o = new JSONObject(getSharedPreferences(ctx).getString(WEATHER_INFO, ""));
            if (o.getString("cityname").equals("") || o.getString("cityname").equals("unchosen")) {
                throw new JSONException("naah, too lazy to write");
            }
            return o.getString("cityname");
        } catch (JSONException e) {
            return null;
        }
    }

    public static LatLng getLatLon(Context ctx) {
        try {
            JSONObject o = new JSONObject(getSharedPreferences(ctx).getString(WEATHER_INFO, ""));
            return new LatLng(o.getDouble("latitude"), o.getDouble("longitude"));
        } catch (JSONException e) {
            return null;
        }
    }

    public static void addDate(Context ctx, Event event) {
        try {
            JSONObject object = new JSONObject();
            try {
                object = getAllDays(ctx);
            } catch (JSONException e) {
                object = new JSONObject();
            }
            JSONArray res = new JSONArray();
            try {
                res = object.getJSONArray("days");
            } catch (JSONException e) {
            }
            res.put(event.getJSONObject());
            object.remove("days");
            object.put("days", res
            );
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(DAYS_INFO, object.toString());
            editor.apply();
        } catch (JSONException e) {
            Log.e("YourDayLogs", "Adding event error: " + e.toString());
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void removeDate(Context ctx, Event event) {
        try {
            JSONObject object = new JSONObject();
            try {
                object = getAllDays(ctx);
            } catch (JSONException e) {
                object = new JSONObject();
            }
            JSONArray res = new JSONArray();
            try {
                res = object.getJSONArray("days");
            } catch (JSONException e) {
            }
            int delete = -1;
            for (int i = 0; i < res.length(); i++) {
                JSONObject tmp = res.getJSONObject(i);
                String thisName = event.getEventName();
                String name = tmp.getString("name");
                Long thisDate = event.getDate().getTime();
                Long date = tmp.getLong("date");
                String thisType = event.getEventType().toString();
                String type = tmp.get("event").toString();
                if (thisName.equals(name) && thisDate.equals(date) && thisType.equals(type)) {
                    delete = i;
                    break;
                }
            }
            res.remove(delete);
            object.remove("days");
            object.put("days", res
            );
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(DAYS_INFO, object.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.e("YourDayLogs", "Adding event error: " + e.toString());
            e.printStackTrace();
        }
    }

    private static JSONObject getAllDays(Context ctx) throws JSONException {
        return new JSONObject(getSharedPreferences(ctx).getString(DAYS_INFO, ""));
    }

    public static List<Event> getAllEvents(Context ctx) {
        try {
            JSONObject obj = getAllDays(ctx);
            List<Event> events = new ArrayList<>();
            JSONArray array = obj.getJSONArray("days");
            for (int i = 0; i < array.length(); i++) {
                JSONObject preres = array.getJSONObject(i);
                switch (EventType.valueOf(preres.getString("event"))) {
                    case BIRTHDAY:
                        events.add(new BirthDayEvent(preres));
                        break;
                    case BIRTHDAY_NO_DATE:
                        events.add(new BirthDayNoDateEvent(preres));
                        break;
                    case EVENT:
                        events.add(new UsualEvent(preres));
                        break;
                    case REMINDER:
                        events.add(new Reminder(preres));
                        break;
                }
            }
            return events;
        } catch (JSONException e) {
            return new ArrayList<Event>();
        }
    }

    public static List<Event> getRequiredEvents(Context ctx, Date date) {
        try {
            JSONObject obj = getAllDays(ctx);
            List<Event> events = new ArrayList<>();
            JSONArray array = obj.getJSONArray("days");
            DateFormat df = new SimpleDateFormat("d M");
            long ranko1 = Long.parseLong(df.format(date).split(" ")[0]) + Long.parseLong(df.format(date).split(" ")[1]) * 100;
            String ress = df.format(date);
            for (int i = 0; i < array.length(); i++) {
                long ranko2 = 0;
                if (EventType.valueOf(array.getJSONObject(i).getString("event")) == EventType.REMINDER) {
                    long tmpRanko1 = ranko1 + date.getYear() * 10000;
                    Date o1 = new Date(array.getJSONObject(i).getLong("date"));
                    String res = df.format(o1);
                    ranko2 = Integer.parseInt(res.split(" ")[0]) + (Integer.parseInt(res.split(" ")[1]) * 100);
                    ranko2 += o1.getYear() * 10000;
                    if (tmpRanko1 == ranko2) {
                        JSONObject preres = array.getJSONObject(i);
                        events.add(new Reminder(preres));
                    }
                } else {
                    Date o1 = new Date(array.getJSONObject(i).getLong("date"));
                    String res = df.format(o1);
                    ranko2 = Integer.parseInt(res.split(" ")[0]) + (Integer.parseInt(res.split(" ")[1]) * 100);
                    if (ranko1 == ranko2) {
                        JSONObject preres = array.getJSONObject(i);
                        switch (EventType.valueOf(preres.getString("event"))) {
                            case BIRTHDAY:
                                events.add(new BirthDayEvent(preres));
                                break;
                            case BIRTHDAY_NO_DATE:
                                events.add(new BirthDayNoDateEvent(preres));
                                break;
                            case EVENT:
                                events.add(new UsualEvent(preres));
                                break;
                            case REMINDER:
                                events.add(new Reminder(preres));
                                break;
                        }
                    }
                }
            }
            return events;
        } catch (JSONException e) {
            return new ArrayList<Event>();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void deleteUnused(Context ctx, Date date) {
        try {
            JSONObject object = new JSONObject();
            try {
                object = getAllDays(ctx);
            } catch (JSONException e) {
                object = new JSONObject();
            }
            JSONArray res = new JSONArray();
            try {
                res = object.getJSONArray("days");
            } catch (JSONException e) {
            }
            List<Integer> thingsToDelete = new ArrayList<>();
            for (int i = 0; i < res.length(); i++) {
                JSONObject tmp = res.getJSONObject(i);
                long time = tmp.getLong("date");
                String type = tmp.get("event").toString();
                if (type.equals("REMINDER") && date.getTime() > time) {
                    thingsToDelete.add(i);
                }
            }
            for (int i = 0; i < thingsToDelete.size(); i++) {
                res.remove(thingsToDelete.get(i) - i);
            }
            object.remove("days");
            object.put("days", res
            );
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(DAYS_INFO, object.toString());
            editor.commit();
        } catch (JSONException e) {
            Log.e("YourDayLogs", "Exception during removing unused remindings.");
        }
    }

    public static void setAccesed(Context ctx, Map<Types, Boolean> map) throws JSONException {
        JSONObject root = new JSONObject();
        for (Map.Entry<Types, Boolean> a : map.entrySet()) {
            root.put(String.valueOf(a.getKey()), a.getValue());
        }
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ACCESED, root.toString());
        editor.commit();
    }

    public static Map<Types, Boolean> getAccesed(Context ctx) throws JSONException {
        JSONObject root = new JSONObject(getSharedPreferences(ctx).getString(ACCESED, ""));
        Map<Types, Boolean> res = new HashMap<>();
        for (Types t : Types.values()) {
            if (root.has(t.toString().toUpperCase())) {
                res.put(t, root.getBoolean(t.toString().toUpperCase()));
            }
        }
        res.put(TODAY, true);
        return res;
    }
}