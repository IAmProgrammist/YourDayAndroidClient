package rchat.info.yourday_new.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.WeatherAdapter;
import rchat.info.yourday_new.containers.weather.Weather;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class WeatherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.main_list);
        listView.setDivider(null);
        ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.ic_w01d_bg);
        if (SaveSharedPreferences.getCityName(this) != null) {
            new LoadWeather().execute(findViewById(R.id.main_list), findViewById(R.id.bg));
        } else {
            WeatherAdapter adapter = new WeatherAdapter(this, false);
            listView.setAdapter(adapter);
            ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.ic_w01d_bg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wthr_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_city:
                Intent intent = new Intent(this, ChooseCityActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class LoadWeather extends AsyncTask<View, Object, Void> {
        @Override
        protected void onProgressUpdate(Object... values) {
            LoadStates state = (LoadStates) values[0];
            ProgressBar progressBar = findViewById(R.id.load);
            ListView listView = findViewById(R.id.main_list);
            switch (state) {
                case STARTED:
                    progressBar.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    listView.setAdapter(null);
                    break;
                case FINISHED:
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    Weather weather = (Weather) values[1];
                    WeatherAdapter adapter = (WeatherAdapter) values[2];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    int resId;
                    resId = WeatherActivity.this.getResources().getIdentifier("ic_w" + weather.currentWeather.weather_ids.get(0) + "_bg", "drawable", WeatherActivity.this.getPackageName());
                    if (resId == 0) {
                        resId = WeatherActivity.this.getResources().getIdentifier("ic_w" + weather.currentWeather.weather_ids.get(0).substring(0, weather.currentWeather.weather_ids.get(0).length() - 1) + "_bg", "drawable", WeatherActivity.this.getPackageName());
                    }
                    ImageView bg = findViewById(R.id.bg);
                    bg.setImageResource(resId);
                    break;
                case CONN_ERROR:
                    Toast.makeText(WeatherActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            Connection connection = Connection.getConnection();
            ListView listView = (ListView) views[0];
            ImageView bg = (ImageView) views[1];
            JSONObject request = new JSONObject();
            LatLng latLng = SaveSharedPreferences.getLatLon(WeatherActivity.this);
            String location = SaveSharedPreferences.getCityName(WeatherActivity.this);
            try {
                request.put("type", "weather");
                request.put("lat", latLng.latitude);
                request.put("lng", latLng.longitude);
                request.put("cityName", location);
            } catch (JSONException e) {

            }
            connection.send(request);
            Weather weather = null;
            boolean found = false;
            Long startTime = new Date().getTime();
            Long diffTime = new Date().getTime();
            while (diffTime - startTime <= 10000 && !found) {
                try {
                    for (Object a : connection.props) {
                        if (a instanceof Weather) {
                            weather = (Weather) a;
                            found = true;
                        }
                    }
                } catch (ConcurrentModificationException e) {

                }
                diffTime = new Date().getTime();
            }
            if (weather == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                WeatherAdapter adapter = new WeatherAdapter(WeatherActivity.this, weather);
                publishProgress(LoadStates.FINISHED, weather, adapter);
                return null;
            }
            return null;
        }
    }
}
