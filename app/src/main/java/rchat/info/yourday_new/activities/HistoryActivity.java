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
import rchat.info.yourday_new.adapters.HistoryAdapter;
import rchat.info.yourday_new.containers.historical.HistDay;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ImageView imageView = findViewById(R.id.bg);
        imageView.setImageResource(R.drawable.hist_bg);
        new LoadHistory().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class LoadHistory extends AsyncTask<View, Object, Void> {
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
                    HistoryAdapter adapter = (HistoryAdapter) values[1];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(HistoryActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            HistDay d = null;
            Connection connection = Connection.getConnection();
            boolean foundd = false;
            try {
                for (Object a : connection.props) {
                    if (a instanceof HistDay) {
                        d = (HistDay) a;
                        foundd = true;

                    }
                }
            } catch (ConcurrentModificationException e) {

            }
            if (!foundd) {
                try {
                    JSONObject request = new JSONObject();
                    request.put("type", "history");
                    connection.send(request);
                    boolean found = false;
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : connection.props) {
                                if (a instanceof HistDay) {
                                    d = (HistDay) a;
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
            if (d == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                HistoryAdapter adapter = new HistoryAdapter(HistoryActivity.this, d);
                publishProgress(LoadStates.FINISHED, adapter);
            }
            return null;
        }
    }
}
