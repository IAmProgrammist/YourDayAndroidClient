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

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.BirthdaysAdapter;
import rchat.info.yourday_new.containers.Human;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;

public class BirthdaysActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ImageView imageView = findViewById(R.id.bg);
        imageView.setImageResource(R.drawable.birthday_bg);
        new LoadBirthdays(this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressWarnings(value = "unchecked")
    private static class LoadBirthdays extends AsyncTask<View, Object, Void> {

        private WeakReference<BirthdaysActivity> activityReference;

        LoadBirthdays(BirthdaysActivity ctx) {
            activityReference = new WeakReference<>(ctx);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            BirthdaysActivity activity = activityReference.get();
            LoadStates state = (LoadStates) values[0];
            ProgressBar progressBar = activity.findViewById(R.id.load);
            ListView listView = activity.findViewById(R.id.main_list);
            switch (state) {
                case STARTED:
                    progressBar.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    listView.setAdapter(null);
                    break;
                case FINISHED:
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    BirthdaysAdapter adapter = (BirthdaysAdapter) values[1];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    break;
                case CONN_ERROR:
                    Toast.makeText(activity, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            List<Human> d = null;
            Connection connection = Connection.getConnection();
            boolean foundd = false;
            try {
                for (Object a : Connection.props) {
                    if (a instanceof List) {
                        List<Human> l = (List<Human>) a;
                        try {
                            if (l.get(0) instanceof Human) {
                                d = l;
                                foundd = true;
                            }
                        } catch (Exception ignore) {
                        }
                    }
                }
            } catch (ConcurrentModificationException ignore) {
            }
            if (!foundd) {
                try {
                    JSONObject request = new JSONObject();
                    request.put("type", "allFamousPeoplePresents");
                    connection.send(request);
                    boolean found = false;
                    Long startTime = new Date().getTime();
                    Long diffTime = new Date().getTime();
                    while (diffTime - startTime <= 10000 && !found) {
                        try {
                            for (Object a : Connection.props) {
                                if (a instanceof List) {
                                    List l = (List) a;
                                    try {
                                        Human aaa = (Human) l.get(0);
                                        d = l;
                                        found = true;
                                    } catch (Exception ignore) {
                                    }
                                }
                            }
                        } catch (ConcurrentModificationException ignore) {
                        }
                        diffTime = new Date().getTime();
                    }
                } catch (JSONException ignore) {
                }
            }
            if (d == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                BirthdaysAdapter adapter = new BirthdaysAdapter(activityReference.get(), d);
                publishProgress(LoadStates.FINISHED, adapter);
            }
            return null;
        }
    }
}
