package rchat.info.yourday_new.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.PresentAdapter;
import rchat.info.yourday_new.containers.day.Present;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class PresentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ListView listView = findViewById(R.id.main_list);
        listView.setDivider(null);
        new LoadPresent().execute(listView);
    }

    public class LoadPresent extends AsyncTask<ListView, Object, Void> {

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
                    PresentAdapter adapter = (PresentAdapter) values[1];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(PresentActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final ListView... listViews) {
            publishProgress(LoadStates.STARTED);
            Connection connection = Connection.getConnection();
            JSONObject object = new JSONObject();
            try {
                object.put("type", "allNewPresents");
            } catch (JSONException e) {
                //how?
            }
            connection.send(object);
            List<Present> presents = new ArrayList<>();
            Long startTime = new Date().getTime();
            Long diffTime = new Date().getTime();
            boolean found = false;
            while (diffTime - startTime <= 10000 && !found) {
                try {
                    for (Object a : connection.props) {
                        if (a instanceof List) {
                            try {
                                Present p = (Present) ((List) a).get(0);
                                presents = (List<Present>) a;
                                found = true;
                                break;
                            } catch (Exception e) {

                            }
                        }
                    }
                } catch (ConcurrentModificationException e) {

                }
                try {
                    diffTime = new Date().getTime();
                } catch (Exception e) {

                }
            }
            if (presents.size() == 0) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                PresentAdapter adapter = new PresentAdapter(PresentActivity.this, presents);
                publishProgress(LoadStates.FINISHED, adapter);
            }
            return null;
        }
    }
}
