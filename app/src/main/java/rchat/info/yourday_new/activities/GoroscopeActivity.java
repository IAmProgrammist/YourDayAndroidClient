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

import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.GoroscopeAdapter;
import rchat.info.yourday_new.containers.Goroscope;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class GoroscopeActivity extends AppCompatActivity {
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
        ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.sign_unidified);
        if (SaveSharedPreferences.getSign(this) != null) {
            new LoadGoroscope().execute();
        } else {
            GoroscopeAdapter adapter = new GoroscopeAdapter(this, false);
            listView.setAdapter(adapter);
            ((ImageView) findViewById(R.id.bg)).setImageResource(R.drawable.sign_unidified);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goroscope_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_sign:
                Intent intent = new Intent(this, ChooseSign.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    class LoadGoroscope extends AsyncTask<View, Object, Void> {
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
                    Goroscope goroscope = (Goroscope) values[1];
                    GoroscopeAdapter adapter = (GoroscopeAdapter) values[2];
                    ImageView bg = findViewById(R.id.bg);
                    int resId = 0;
                    switch (goroscope.getSign()) {
                        case "Aries":
                            resId = R.drawable.oven_bg;
                            break;
                        case "Taurus":
                            resId = R.drawable.telec_bg;
                            break;
                        case "Gemini":
                            resId = R.drawable.bliznecy_bg;
                            break;
                        case "Cancer":
                            resId = R.drawable.rak_bg;
                            break;
                        case "Leo":
                            resId = R.drawable.lev_bg;
                            break;
                        case "Libra":
                            resId = R.drawable.vesy_bg;
                            break;
                        case "Scorpio":
                            resId = R.drawable.scorpion_bg;
                            break;
                        case "Sagittarius":
                            resId = R.drawable.strelec_bg;
                            break;
                        case "Сapricorn":
                            resId = R.drawable.kozerog_bg;
                            break;
                        case "Aquarius":
                            resId = R.drawable.vodichka_bg;
                            break;
                        case "Pisces":
                            resId = R.drawable.ryby_bg;
                            break;
                        case "Virgo":
                            resId = R.drawable.deva_bg;
                            break;
                    }
                    bg.setImageResource(resId);
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(GoroscopeActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            try {
                publishProgress(LoadStates.STARTED);
                Connection connection = Connection.getConnection();
                JSONObject request = new JSONObject();
                request.put("type", "goroscope");
                request.put("sign", SaveSharedPreferences.getSign(GoroscopeActivity.this));
                connection.send(request);
                Goroscope goroscope = null;
                boolean found = false;
                Long startTime = new Date().getTime();
                Long diffTime = new Date().getTime();
                while (diffTime - startTime <= 10000 && !found) {
                    try {
                        for (Object a : connection.props) {
                            if (a instanceof Goroscope) {
                                goroscope = (Goroscope) a;
                                found = true;
                            }
                        }
                    } catch (ConcurrentModificationException e) {

                    }
                    diffTime = new Date().getTime();
                }
                if (goroscope == null) {
                    publishProgress(LoadStates.CONN_ERROR);
                    connection.disconnected();
                } else {
                    GoroscopeAdapter adapter = new GoroscopeAdapter(GoroscopeActivity.this, goroscope);
                    publishProgress(LoadStates.FINISHED, goroscope, adapter);
                }
            } catch (Exception e) {

            }
            return null;
        }
    }
}

