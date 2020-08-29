package rchat.info.yourday_new.activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.containers.Update;
import rchat.info.yourday_new.containers.day.Day;
import rchat.info.yourday_new.containers.events.BirthDayEvent;
import rchat.info.yourday_new.containers.events.Event;
import rchat.info.yourday_new.containers.events.Reminder;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;
import rchat.info.yourday_new.others.RunningTextView;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class MainActivity extends AppCompatActivity {
    static Day d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.presents).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.presents);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, PresentActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.weather);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.goroscope).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.goroscope);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, GoroscopeActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.omens).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.omens);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, OmenActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.moon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.moon);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, MoonDayActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.names).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.names);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, NamesActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.recipe);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.birthday);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, BirthdaysActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.history);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.notes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.notes);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.quote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.quote);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, QuoteActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.joke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.joke);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, JokeActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.int_facts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.int_facts);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, FactActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    View vv = findViewById(R.id.about);
                    if (vv != null) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, vv, "description_image");
                        bundle = options.toBundle();
                    }
                }
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                if (bundle == null) {
                    startActivity(intent);
                } else {
                    startActivity(intent, bundle);
                }
            }
        });
        Connection connection = Connection.getConnection();
    }

    @Override
    protected void onResume() {
        new LoadDay().execute();
        super.onResume();
    }

    class LoadDay extends AsyncTask<View, Object, Void> {

        @Override
        protected void onProgressUpdate(Object... values) {
            LoadStates state = (LoadStates) values[0];
            ProgressBar progressBar = findViewById(R.id.load);
            ImageView picture = findViewById(R.id.backg);
            TextView date = findViewById(R.id.date);
            RunningTextView runningTextView = findViewById(R.id.events);
            switch (state) {
                case STARTED:
                    progressBar.setVisibility(View.VISIBLE);
                    picture.setImageResource(R.color.empty);
                    date.setText("Загрузка...");
                    runningTextView.setText("");
                    runningTextView.setVisibility(View.INVISIBLE);
                    break;
                case FINISHED:
                    progressBar.setVisibility(View.GONE);
                    String datee = new SimpleDateFormat("d MMMM yyyy года", new DateFormatSymbols() {
                        @Override
                        public String[] getMonths() {
                            return new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
                        }
                    }).format(d.getDate());
                    SaveSharedPreferences.deleteUnused(MainActivity.this, d.getDate());
                    int a = Integer.parseInt(new SimpleDateFormat("MM").format(d.getDate()));
                    int res = R.color.empty;
                    switch (a) {
                        case 1:
                            res = R.drawable.january;
                            break;
                        case 2:
                            res = R.drawable.february;
                            break;
                        case 3:
                            res = R.drawable.march;
                            break;
                        case 4:
                            res = R.drawable.april;
                            break;
                        case 5:
                            res = R.drawable.may;
                            break;
                        case 6:
                            res = R.drawable.june;
                            break;
                        case 7:
                            res = R.drawable.july;
                            break;
                        case 8:
                            res = R.drawable.august;
                            break;
                        case 9:
                            res = R.drawable.september;
                            break;
                        case 10:
                            res = R.drawable.october;
                            break;
                        case 11:
                            res = R.drawable.november;
                            break;
                        case 12:
                            res = R.drawable.december;
                            break;
                    }
                    picture.setVisibility(View.VISIBLE);
                    picture.setImageResource(res);
                    date.setText(datee);
                    List<Event> eventList = SaveSharedPreferences.getRequiredEvents(MainActivity.this, d.getDate());
                    if (eventList.size() != 0) {
                        String mainDesc = "";
                        mainDesc += "Не забудьте: " + "\n";
                        List<BirthDayEvent> birthDayEvents = new ArrayList<>();
                        List<Event> usualEvents = new ArrayList<>();
                        List<Reminder> reminderEvents = new ArrayList<>();
                        for (Event aa : eventList) {
                            if (aa instanceof BirthDayEvent) {
                                birthDayEvents.add((BirthDayEvent) aa);
                            } else if (aa instanceof Reminder) {
                                reminderEvents.add((Reminder) aa);
                            } else {
                                usualEvents.add(aa);
                            }
                        }
                        if (birthDayEvents.size() != 0) {
                            mainDesc += "Дни рождение(я): ";
                            for (BirthDayEvent aa : birthDayEvents) {
                                mainDesc += aa.getEventName() + ", ";
                            }
                            mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                            mainDesc += "." + "\n";
                        }
                        if (usualEvents.size() != 0) {
                            mainDesc += "Событие(я): ";
                            for (Event aa : usualEvents) {
                                mainDesc += aa.getEventName() + ", ";
                            }
                            mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                            mainDesc += "." + "\n";
                        }
                        if (reminderEvents.size() != 0) {
                            mainDesc += "Напоминание(я): ";
                            for (Reminder aa : reminderEvents) {
                                mainDesc += aa.getEventName() + ", ";
                            }
                            mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                            mainDesc += "." + "\n";
                        }
                        mainDesc += "Хорошего дня! Нажмите здесь, чтобы открыть все заметки";
                        runningTextView.setVisibility(View.VISIBLE);
                        runningTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                startActivity(intent);
                            }
                        });
                        runningTextView.setText(mainDesc);
                    } else {
                        runningTextView.setVisibility(View.GONE);
                    }
                    break;
                case CONN_ERROR:
                    picture.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    runningTextView.setVisibility(View.INVISIBLE);
                    picture.setImageResource(R.drawable.error);
                    date.setText("Ошибка подключения");
                    Toast.makeText(MainActivity.this, "Невозможно установить соединение с сервером. Проверьте подключение к интернету и перезайдите в приложение.", Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_RECEIVED:
                    try {
                        if(Connection.launchedFirst) {
                            Connection.launchedFirst = false;
                            Update update = (Update) values[1];
                            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            String curAnd = update.version.substring(0, 3);
                            String curServ = versionName.substring(0, 3);
                            if (!curAnd.equals(curServ)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Доступно новое обновление! Что нового: " + update.desc + ".");
                                builder.setTitle("Вышло обновление!");
                                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.setPositiveButton("Обновиться", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=rchat.info.yourday_new"));
                                        startActivity(intent);
                                    }
                                });
                                builder.create().show();
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
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
            if (d == null) {
                publishProgress(LoadStates.CONN_ERROR);
                connection.disconnected();
            } else {
                publishProgress(LoadStates.FINISHED);
                Update update = null;
                try {
                    for (Object a : connection.props) {
                        if (a instanceof Update) {
                            update = (Update) a;
                        }
                    }
                } catch (ConcurrentModificationException e) {

                }
                if(update != null){
                    publishProgress(LoadStates.UPDATE_RECEIVED, update);
                }
            }
            return null;
        }
    }
}

