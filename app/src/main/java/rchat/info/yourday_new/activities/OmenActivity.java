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

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.OmenAdapter;
import rchat.info.yourday_new.containers.Omen;
import rchat.info.yourday_new.containers.day.Day;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class OmenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        new LoadOmen().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class LoadOmen extends AsyncTask<View, Object, Void> {

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
                    Day d = (Day) values[2];
                    OmenAdapter adapter = (OmenAdapter) values[1];
                    ImageView bg = findViewById(R.id.bg);
                    int res = 0;
                    int month = Integer.parseInt(new SimpleDateFormat("MM").format(d.getDate()));
                    if (month >= 3 && month <= 5) {
                        res = R.drawable.pr_spring_bg;
                    } else if (month >= 6 && month <= 8) {
                        res = R.drawable.pr_summer_bg;
                    } else if (month >= 9 && month <= 11) {
                        res = R.drawable.pr_autumn_bg;
                    } else {
                        res = R.drawable.pr_winter_bg;
                    }
                    bg.setImageResource(res);
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(OmenActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            Day d = null;
            Connection connection = Connection.getConnection();
            boolean foundd = false;
            try {
                for (Object a : connection.props) {
                    if (a instanceof Day) {
                        d = (Day) a;
                        foundd = true;
                    }
                }
            } catch (ConcurrentModificationException e) {

            }
            if (!foundd) {
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "day");
                    connection.send(req);
                    boolean found = false;
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : connection.props) {
                                if (a instanceof Day) {
                                    d = (Day) a;
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {

                        }
                        diffTime = new Date().getTime();
                    }
                } catch (JSONException e) {

                }
            }
            foundd = false;
            Omen omen = null;
            try {
                for (Object a : connection.props) {
                    if (a instanceof Omen) {
                        omen = (Omen) a;
                        foundd = true;
                    }
                }
            } catch (ConcurrentModificationException e) {

            }
            if (!foundd) {
                try {
                    JSONObject req = new JSONObject();
                    req.put("type", "omen");
                    connection.send(req);
                    boolean found = false;
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : connection.props) {
                                if (a instanceof Omen) {
                                    omen = (Omen) a;
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException e) {
                            //just ignore
                        }
                        diffTime = new Date().getTime();
                    }
                } catch (JSONException e) {

                }
            }
            if (omen == null || d == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                OmenAdapter adapter = new OmenAdapter(OmenActivity.this, omen);
                publishProgress(LoadStates.FINISHED, adapter, d);
            }
            return null;
        }
    }
}
