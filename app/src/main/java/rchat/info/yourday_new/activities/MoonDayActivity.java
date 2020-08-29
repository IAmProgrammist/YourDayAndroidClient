package rchat.info.yourday_new.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.MoonDayAdapter;
import rchat.info.yourday_new.containers.moon_calend.MoonDay;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class MoonDayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ListView listView = findViewById(R.id.main_list);
        ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.moonday_bg);
        listView.setDivider(null);
        new LoadMoonDay().execute();
    }

    public class LoadMoonDay extends AsyncTask<Void, Object, Void> {
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
                    MoonDay moonDay = (MoonDay) values[1];
                    MoonDayAdapter adapter = new MoonDayAdapter(MoonDayActivity.this, moonDay);
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(MoonDayActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... listViews) {

            try {
                publishProgress(LoadStates.STARTED);
                Connection connection = Connection.getConnection();
                JSONObject request = new JSONObject();
                request.put("type", "moonDay");
                connection.send(request);
                MoonDay moonDay = null;
                boolean foundd = false;
                try {
                    for (Object a : connection.props) {
                        if (a instanceof MoonDay) {
                            moonDay = (MoonDay) a;
                            foundd = true;
                        }
                    }
                } catch (ConcurrentModificationException e) {

                }
                if (!foundd) {
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    boolean found = false;
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : connection.props) {
                                if (a instanceof MoonDay) {
                                    moonDay = (MoonDay) a;
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {

                        }
                        try {
                            diffTime = new Date().getTime();
                        } catch (Exception e) {

                        }
                    }
                }
                if (moonDay == null) {
                    publishProgress(LoadStates.CONN_ERROR);
                    connection.disconnected();
                } else {
                    publishProgress(LoadStates.FINISHED, moonDay);
                }
            } catch (JSONException e) {

            }
            return null;
        }
    }
}
