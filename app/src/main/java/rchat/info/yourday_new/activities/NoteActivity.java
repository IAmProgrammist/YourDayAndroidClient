package rchat.info.yourday_new.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ConcurrentModificationException;
import java.util.Date;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.adapters.NoteAdapter;
import rchat.info.yourday_new.containers.day.Day;
import rchat.info.yourday_new.containers.events.BirthDayEvent;
import rchat.info.yourday_new.containers.events.BirthDayNoDateEvent;
import rchat.info.yourday_new.containers.events.Event;
import rchat.info.yourday_new.containers.events.Reminder;
import rchat.info.yourday_new.containers.events.UsualEvent;
import rchat.info.yourday_new.others.Connection;
import rchat.info.yourday_new.others.LoadStates;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class NoteActivity extends AppCompatActivity {

    public RequestType requestType = null;
    public String descForRem = null;
    public String pickedNameBg = null;
    public DialogInterface currentDialog = null;
    public Date current = null;
    NoteAdapter curAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ImageView imageView = findViewById(R.id.bg);
        imageView.setImageResource(R.drawable.note_bg);
        ListView listView = findViewById(R.id.main_list);
        new LoadNotes().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.events_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выберите тип")
                        .setItems(new String[]{"День рождения", "Событие", "Напоминание"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        createFirstPageBDDialog();
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        createFirstPageEventDialog();
                                        dialog.dismiss();
                                        break;
                                    case 2:
                                        createFirstPageReminderDialog();
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                currentDialog.cancel();
                Uri resultUri = result.getUri();
                switch (requestType) {
                    case BIRTHDAY:
                        createFirstPageBDDialog(resultUri);
                        break;
                    case EVENT:
                        createFirstPageEventDialog(resultUri);
                        break;
                    case REMINDER:
                        createFirstPageReminderDialog(resultUri);
                        break;
                    case CHANGE_PHOTO:
                        curAdapter.changePhoto(resultUri);
                        break;
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void createFirstPageBDDialog() {
        View v = getLayoutInflater().inflate(R.layout.first_page_bday_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSecondPageBD(((EditText) v.findViewById(R.id.name)).getText().toString(), null);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requestType = RequestType.BIRTHDAY;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });

    }

    private void createSecondPageBD(String name, Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.second_page_bday_dialog, null);
        DatePicker picker = v.findViewById(R.id.calendar);
        picker.setMaxDate(current.getTime());
        CheckBox checkBox = v.findViewById(R.id.no_date);
        TextView error = v.findViewById(R.id.error);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    picker.findViewById(getResources().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
                } else {
                    picker.findViewById(getResources().getIdentifier("year", "id", "android")).setVisibility(View.VISIBLE);
                }
            }
        });
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (checkBox.isChecked()) {
                    Date date = new Date(0, picker.getMonth(), picker.getDayOfMonth());
                    BirthDayNoDateEvent event = new BirthDayNoDateEvent(name, date, pic);
                    new LoadEvent().execute(event);
                    SaveSharedPreferences.addDate(NoteActivity.this, event);
                    new LoadNotes().execute();
                } else {
                    Date date = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                    BirthDayEvent event = new BirthDayEvent(name, date, pic);
                    SaveSharedPreferences.addDate(NoteActivity.this, event);
                    new LoadNotes().execute();
                }
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder1.create();
        dialog.show();
    }

    public void createFirstPageBDDialog(Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.first_page_bday_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        ((EditText) v.findViewById(R.id.name)).setText(pickedNameBg);
        v.findViewById(R.id.photo_cont).setVisibility(View.VISIBLE);
        Picasso.get().load(pic).into((ImageView) v.findViewById(R.id.chosen_photo));
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSecondPageBD(((EditText) v.findViewById(R.id.name)).getText().toString(), pic);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requestType = RequestType.BIRTHDAY;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    public void createFirstPageEventDialog() {
        View v = getLayoutInflater().inflate(R.layout.first_page_event_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSecondPageEvent(((EditText) v.findViewById(R.id.name)).getText().toString(), null);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requestType = RequestType.EVENT;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    public void createFirstPageEventDialog(Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.first_page_event_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        ((EditText) v.findViewById(R.id.name)).setText(pickedNameBg);
        v.findViewById(R.id.photo_cont).setVisibility(View.VISIBLE);
        Picasso.get().load(pic).into((ImageView) v.findViewById(R.id.chosen_photo));
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createSecondPageEvent(((EditText) v.findViewById(R.id.name)).getText().toString(), pic);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                requestType = RequestType.EVENT;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    private void createSecondPageEvent(String name, Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.second_page_event_dialog, null);
        DatePicker picker = v.findViewById(R.id.calendar);
        picker.setMaxDate(current.getTime());
        CheckBox checkBox = v.findViewById(R.id.no_date);
        TextView error = v.findViewById(R.id.error);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    picker.findViewById(getResources().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
                } else {
                    picker.findViewById(getResources().getIdentifier("year", "id", "android")).setVisibility(View.VISIBLE);
                }
            }
        });
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (checkBox.isChecked()) {
                    Date date = new Date(0, picker.getMonth(), picker.getDayOfMonth());
                    BirthDayNoDateEvent event = new BirthDayNoDateEvent(name, date, pic);
                    new LoadEvent().execute(event);
                    SaveSharedPreferences.addDate(NoteActivity.this, event);
                    new LoadNotes().execute();
                } else {
                    Date date = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                    UsualEvent event = new UsualEvent(name, date, pic);
                    SaveSharedPreferences.addDate(NoteActivity.this, event);
                    new LoadNotes().execute();
                }
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder1.create();
        dialog.show();
    }

    public void createFirstPageReminderDialog() {
        View v = getLayoutInflater().inflate(R.layout.first_page_reminder_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText desc = v.findViewById(R.id.desc);
                String dsc = desc.getText().toString();
                createSecondPageReminder(((EditText) v.findViewById(R.id.name)).getText().toString(), dsc, null);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                EditText desc = v.findViewById(R.id.desc);
                descForRem = desc.getText().toString();
                requestType = RequestType.REMINDER;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    public void createFirstPageReminderDialog(Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.first_page_reminder_dialog, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        ((EditText) v.findViewById(R.id.name)).setText(pickedNameBg);
        ((EditText) v.findViewById(R.id.desc)).setText(descForRem);
        v.findViewById(R.id.photo_cont).setVisibility(View.VISIBLE);
        Picasso.get().load(pic).into((ImageView) v.findViewById(R.id.chosen_photo));
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText desc = v.findViewById(R.id.desc);
                String dsc = desc.getText().toString();
                createSecondPageReminder(((EditText) v.findViewById(R.id.name)).getText().toString(), dsc, pic);
                dialog.dismiss();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder1.create();
        v.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                currentDialog = dialog;
                pickedNameBg = ((EditText) v.findViewById(R.id.name)).getText().toString();
                EditText desc = v.findViewById(R.id.desc);
                descForRem = desc.getText().toString();
                requestType = RequestType.REMINDER;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(NoteActivity.this);
            }
        });
        dialog.show();
        EditText editText = v.findViewById(R.id.name);
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(android.app.AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    private void createSecondPageReminder(String name, String desc, Uri pic) {
        View v = getLayoutInflater().inflate(R.layout.second_page_reminder_dialog, null);
        DatePicker picker = v.findViewById(R.id.calendar);
        Date dt = new Date(current.getTime());
        picker.setMinDate(dt.getTime());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
        builder1.setTitle("Добавить");
        builder1.setView(v);
        builder1.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                Reminder reminder = new Reminder(name, date, desc, pic);
                SaveSharedPreferences.addDate(NoteActivity.this, reminder);
                new LoadNotes().execute();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder1.create();
        dialog.show();
    }

    public enum RequestType {
        BIRTHDAY,
        EVENT,
        REMINDER,
        CHANGE_PHOTO
    }

    class LoadNotes extends AsyncTask<View, Object, Void> {
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
                    NoteAdapter adapter = (NoteAdapter) values[1];
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    curAdapter = adapter;
                    break;
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(final View... views) {
            publishProgress(LoadStates.STARTED);
            Day d = null;
            int res = 0;
            String datee = "";
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
            NoteAdapter adapter = null;
            current = new Date(d.getDate().getTime());
            current.setHours(23);
            current.setMinutes(59);
            current.setSeconds(59);
            try {
                adapter = new NoteAdapter(NoteActivity.this, d.getDate());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            publishProgress(LoadStates.FINISHED, adapter);
            return null;
        }
    }

    private class LoadEvent extends AsyncTask<Event, Object, Void> {

        @Override
        protected Void doInBackground(Event... events) {
            Event a = events[0];
            SaveSharedPreferences.addDate(NoteActivity.this, a);
            return null;
        }
    }
}
