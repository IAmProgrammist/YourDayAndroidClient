package rchat.info.yourday_new.activities;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.FactAdapter;
import rchat.info.yourday_new.containers.Fact;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class FactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ListView listView = findViewById(R.id.main_list);
        ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.fact_bg);
        listView.setDivider(null);
        new LoadFact().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.update) {
            Connection connection = Connection.getConnection();
            for (int i = 0; i < connection.props.size(); i++) {
                if (connection.props.get(i) instanceof Fact) {
                    connection.props.remove(i);
                    break;
                }
            }
            new LoadFact().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public class LoadFact extends AsyncTask<Void, Object, Void> {
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
                    Fact quote = (Fact) values[1];
                    FactAdapter adapter = new FactAdapter(FactActivity.this, quote);
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(FactActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
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
                request.put("type", "fact");
                connection.send(request);
                Fact fact = null;
                boolean foundd = false;
                try {
                    for (Object a : connection.props) {
                        if (a instanceof Fact) {
                            fact = (Fact) a;
                            foundd = true;
                        }
                    }
                } catch (ConcurrentModificationException ignore) {

                }
                if (!foundd) {
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    boolean found = false;
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : connection.props) {
                                if (a instanceof Fact) {
                                    fact = (Fact) a;
                                    found = true;
                                }
                            }
                        } catch (ConcurrentModificationException ignore) {

                        }
                        try {
                            diffTime = new Date().getTime();
                        } catch (Exception ignore) {

                        }
                    }
                }
                if (fact == null) {
                    publishProgress(LoadStates.CONN_ERROR);
                    connection.disconnected();
                } else {
                    publishProgress(LoadStates.FINISHED, fact);
                }
            } catch (JSONException ignore) {

            }
            return null;
        }
    }
}
