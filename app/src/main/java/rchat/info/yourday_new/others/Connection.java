package rchat.info.yourday_new.others;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rchat.info.yourday_new.containers.Fact;
import rchat.info.yourday_new.containers.Goroscope;
import rchat.info.yourday_new.containers.Human;
import rchat.info.yourday_new.containers.Joke;
import rchat.info.yourday_new.containers.Names;
import rchat.info.yourday_new.containers.Omen;
import rchat.info.yourday_new.containers.Quote;
import rchat.info.yourday_new.containers.Update;
import rchat.info.yourday_new.containers.day.Day;
import rchat.info.yourday_new.containers.day.Present;
import rchat.info.yourday_new.containers.historical.HistDay;
import rchat.info.yourday_new.containers.moon_calend.MoonDay;
import rchat.info.yourday_new.containers.recipe.NewRecipes;
import rchat.info.yourday_new.containers.weather.Weather;

public class Connection {
    public static List<Object> props = Collections.synchronizedList(new ArrayList<>());
    private static Connection connection;
    private static String uri = "ws://37.230.114.103:4123";
    //37.230.114.103
    private static WebSocketClient cc;
    public static boolean launchedFirst = true;

    private Connection() {
        try {
            cc = new WebSocketClient(new URI(uri), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onMessage(String message) {
                    try {
                        final JSONObject o = new JSONObject(message);
                        if (o.get("type").equals("dayRecieve")) {
                            addTodayDay(o);
                        } else if (o.get("type").equals("goroscopeRecieve")) {
                            addTodayGoroscope(o);
                        } else if (o.get("type").equals("allPresentsAnswer")) {
                            addAllPresents(o);
                        } else if (o.get("type").equals("namesBirthdaysAnswer")) {
                            addTodayNameBirthDays(o);
                        } else if (o.get("type").equals("allFamousPeoplePresentsAnswer")) {
                            addAllHumans(o);
                        } else if (o.get("type").equals("quoteAnswer")) {
                            addQuote(o);
                        } else if (o.get("type").equals("factAnswer")) {
                            addFact(o);
                        } else if (o.get("type").equals("version")) {
                            addVersion(o);
                        } else if (o.getString("type").equals("newRecipeAnswer")) {
                            addTodayRecipe(o);
                        } else if (o.getString("type").equals("jokeAnswer")) {
                            addJoke(o);
                        } else if (o.getString("type").equals("omenAnswer")) {
                            addOmen(o);
                        } else if (o.getString("type").equals("moonDayAnswer")) {
                            addMoonDay(o);
                        } else if (o.getString("type").equals("weatherAnswer")) {
                            addTodayWeather(o);
                        } else if (o.getString("type").equals("histAnswer")) {
                            addTodayHist(o);
                        }
                    } catch (JSONException ignore) {
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {
                    //MainActivity.this.proceedError();
                }
            };
            cc.connectBlocking();
        } catch (URISyntaxException ignore) {
            //This is impossible!
        } catch (InterruptedException ignore) {
            //This is more than impossible...
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    public void send(JSONObject o) {
        try {
            if (cc.isOpen()) {
                cc.send(o.toString());
            } else {
                cc.reconnectBlocking();
                cc.send(o.toString());
            }

        } catch (WebsocketNotConnectedException e) {

        } catch (InterruptedException ignore) {

        }
    }

    public void disconnected() {
        cc.close();
    }

    private void addVersion(JSONObject o) {
        if (cc.isOpen()) {
            Update upd = new Update(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof Update) {
                    contain = i;
                }
            }
            if (contain == null) {
                props.add(upd);
            } else {
                props.set(contain, upd);
            }
        } else {
            cc.close();
        }
    }


    private void addFact(JSONObject o) {
        try {
            if (cc.isOpen()) {
                Fact quote = new Fact(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof Fact) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(quote);
                } else {
                    props.set(contain, quote);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addJoke(JSONObject o) {
        try {
            if (cc.isOpen()) {
                Joke quote = new Joke(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof Joke) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(quote);
                } else {
                    props.set(contain, quote);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addQuote(JSONObject o) {
        try {
            if (cc.isOpen()) {
                Quote quote = new Quote(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof HistDay) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(quote);
                } else {
                    props.set(contain, quote);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addTodayHist(JSONObject o) {
        if (cc.isOpen()) {
            HistDay histDay = new HistDay(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof HistDay) {
                    contain = i;
                }
            }
            if (contain == null) {
                props.add(histDay);
            } else {
                props.set(contain, histDay);
            }
        } else {
            cc.close();
        }
    }

    private void addAllHumans(JSONObject o) {
        try {
            List<Human> modelList = new ArrayList<>();
            JSONArray array = o.getJSONArray("humans");
            for (int i = 0; i < array.length(); i++) {
                modelList.add(new Human(array.getJSONObject(i)));
            }
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof List) {
                    try {
                        contain = i;
                    } catch (Exception ignore) {
                    }
                }
            }
            if (contain == null) {
                props.add(modelList);
            } else {
                props.set(contain, modelList);
            }
        } catch (JSONException ignore) {
            //Impossible!
        }
    }

    private void addTodayRecipe(JSONObject o) {
        try {
            if (cc.isOpen()) {
                NewRecipes newRecipes = new NewRecipes(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof NewRecipes) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(newRecipes);
                } else {
                    props.set(contain, newRecipes);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addTodayNameBirthDays(JSONObject o) {
        try {
            if (cc.isOpen()) {
                Names names = new Names(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof Names) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(names);
                } else {
                    props.set(contain, names);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addMoonDay(JSONObject o) {
        try {
            if (cc.isOpen()) {
                MoonDay moonDay = new MoonDay(o);
                Integer contain = null;
                for (int i = 0; i < props.size(); i++) {
                    if (props.get(i) instanceof MoonDay) {
                        contain = i;
                    }
                }
                if (contain == null) {
                    props.add(moonDay);
                } else {
                    props.set(contain, moonDay);
                }
            } else {
                cc.close();
            }
        } catch (JSONException ignore) {

        }
    }

    private void addOmen(JSONObject o) {
        try {
            Omen omen = new Omen(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof Omen) {
                    contain = i;
                }
            }
            if (contain == null) {
                props.add(omen);
            } else {
                props.set(contain, omen);
            }
        } catch (JSONException ignore) {
            //Impossible!
        }
    }

    private void addAllPresents(JSONObject o) {
        try {
            List<Present> modelList = new ArrayList<>();
            JSONArray array = o.getJSONArray("presents");
            for (int i = 0; i < array.length(); i++) {
                try {
                    modelList.add(new Present(array.getJSONObject(i)));
                } catch (JSONException ignore) {
                    modelList.add(new Present(new JSONObject("{\"name\":\"" + array.getString(i) + "\",\"description\":[{\"type\":\"NO_INFO\",\"desc\":\"NO_INFO\"}]}")));
                }
            }
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof List) {
                    try {
                        contain = i;
                    } catch (Exception ignore) {
                    }
                }
            }
            if (contain == null) {
                props.add(modelList);
            } else {
                props.set(contain, modelList);
            }
        } catch (JSONException ignore) {
            //Impossible!
        }
    }

    private void addTodayGoroscope(JSONObject o) {
        try {
            Goroscope goroscope = new Goroscope(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof Goroscope) {
                    contain = i;
                }
            }
            if (contain == null) {
                props.add(goroscope);
            } else {
                props.set(contain, goroscope);
            }
        } catch (JSONException ignore) {
            //Impossible!
        }
    }

    private void addTodayDay(JSONObject o) {
        try {
            Day day = new Day(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                if (props.get(i) instanceof Day) {
                    contain = i;
                }
            }
            if (contain == null) {
                props.add(day);
            } else {
                props.set(contain, day);
            }
        } catch (JSONException ignore) {

        }
    }

    private void addTodayWeather(JSONObject o) {
        try {
            Weather weather = new Weather(o);
            Integer contain = null;
            for (int i = 0; i < props.size(); i++) {
                Object a = props.get(i);
                if (a instanceof Weather) {
                    contain = i;
                    break;
                }
            }
            if (contain != null) {
                props.set(contain, weather);
            } else {
                props.add(weather);
            }
        } catch (Exception ignore) {

        }

    }
}