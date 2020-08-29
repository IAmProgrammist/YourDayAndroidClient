package rchat.info.yourday_new.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.activities.NoteActivity;
import rchat.info.yourday_new.containers.events.BirthDayEvent;
import rchat.info.yourday_new.containers.events.BirthDayNoDateEvent;
import rchat.info.yourday_new.containers.events.Event;
import rchat.info.yourday_new.containers.events.EventType;
import rchat.info.yourday_new.containers.events.Reminder;
import rchat.info.yourday_new.containers.events.UsualEvent;
import rchat.info.yourday_new.others.SaveSharedPreferences;

public class NoteAdapter extends BaseAdapter {

    private final SparseBooleanArray mCollapsedStatus;
    public List<Event> events = new ArrayList<>();
    Date d = new Date();
    Context ctx;
    NoteActivity activity;
    private Event eventToChange;


    public NoteAdapter(Activity ctx, Date d) throws JSONException {
        SaveSharedPreferences.deleteUnused(ctx, d);
        this.d = d;
        this.ctx = ctx;
        this.activity = (NoteActivity) ctx;
        events = SaveSharedPreferences.getAllEvents(ctx);
        List<Diff> diffs = new ArrayList<>();
        for (Event a : events) {
            if (a instanceof BirthDayEvent || a instanceof UsualEvent || a instanceof BirthDayNoDateEvent) {
                Date date = new Date(a.getDate().getTime());
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                date.setYear(d.getYear());
                Date curDate = new Date(d.getTime());
                curDate.setHours(1);
                curDate.setMinutes(0);
                curDate.setSeconds(0);
                Date diff = null;
                if (new SimpleDateFormat("dd.MM.yyyy").format(d).equals(new SimpleDateFormat("dd.MM.yyyy").format(date))) {
                    diff = new Date(Math.abs(curDate.getTime() - date.getTime()));
                } else if (d.getTime() > date.getTime()) {
                    date.setYear(d.getYear() + 1);
                    diff = new Date(Math.abs(curDate.getTime() - date.getTime()));
                } else {
                    date.setYear(d.getYear());
                    diff = new Date(Math.abs(date.getTime() - curDate.getTime()));
                }
                long diffDays = diff.getTime() / (24 * 60 * 60 * 1000);
                diffs.add(new Diff(diffDays, a));
            } else if (a instanceof Reminder) {
                Date date = new Date(a.getDate().getTime());
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                Date curDate = new Date(d.getTime());
                curDate.setHours(1);
                curDate.setMinutes(0);
                curDate.setSeconds(0);
                Date diff = new Date(date.getTime() - curDate.getTime());
                long diffDays = diff.getTime() / (24 * 60 * 60 * 1000);
                diffs.add(new Diff(diffDays, a));
            }
        }
        Collections.sort(diffs, new Comparator<Diff>() {
            @Override
            public int compare(Diff o1, Diff o2) {
                return (int) (o2.diffDays - o1.diffDays);
            }
        });
        Collections.reverse(diffs);
        events = new ArrayList<>();
        for (Diff dd : diffs) {
            events.add(dd.event);
        }
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return events.size() + 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int a = events.size() + 3;
        if (position == 0) {
            DisplayMetrics display = ctx.getResources().getDisplayMetrics();
            int height = display.heightPixels;
            View v = View.inflate(ctx, R.layout.empty, null);
            v.setEnabled(false);
            v.setClickable(false);
            v.setPadding(0, 0, 0, ((int) height / 6));
            return v;
        } else if (position == 1) {
            View va = View.inflate(ctx, R.layout.note_heading, null);
            va.setEnabled(false);
            va.setClickable(false);
            return va;
        } else if (position == a) {
            View vs = View.inflate(ctx, R.layout.empty, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            vs.setPadding(0, 0, 0, 100);
            return vs;
        } else if (position == a - 1) {
            View vs = View.inflate(ctx, R.layout.recipes_end, null);
            vs.setEnabled(false);
            vs.setClickable(false);
            return vs;
        } else {
            Event event = events.get(position - 2);
            if (event.getEventType() == EventType.BIRTHDAY) {
                BirthDayEvent dayEvent = (BirthDayEvent) event;
                convertView = View.inflate(ctx, R.layout.event_row, null);
                ((TextView) convertView.findViewById(R.id.name)).setText(dayEvent.getEventName());
                if (dayEvent.getPic() == null) {
                    ((ImageView) convertView.findViewById(R.id.image)).setImageResource(R.drawable.no_icon);
                } else {
                    Picasso.get().load(dayEvent.getPic()).into((ImageView) convertView.findViewById(R.id.image));
                }
                Date cur = new Date(d.getTime());
                cur.setHours(1);
                cur.setMinutes(0);
                cur.setSeconds(0);
                Date bDate = new Date(event.getDate().getTime());
                bDate.setHours(0);
                bDate.setMinutes(0);
                bDate.setSeconds(0);
                long diff = cur.getTime() - bDate.getTime();
                long da = (long) (1000l * (60 * 60 * 24 * 365.25));
                long years = Math.round(diff / da);
                int age = (int) years;
                ((TextView) convertView.findViewById(R.id.age)).setText(String.valueOf(age));
                String date = new SimpleDateFormat("dd.MM.yyyy г.").format(bDate);
                String diffStr = howMuchInDays(cur, bDate);
                ((TextView) convertView.findViewById(R.id.when)).setText(diffStr);
                ((TextView) convertView.findViewById(R.id.date)).setText(date);
                convertView.setClickable(true);
                convertView.setLongClickable(true);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Выберите опцию");
                        builder.setItems(new String[]{"Изменить имя", "Изменить дату", "Изменить фото", "Удалить"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        eventToChange = dayEvent;
                                        changeName("Введите имя");
                                        break;
                                    case 1:
                                        eventToChange = dayEvent;
                                        changeDate(eventToChange.getDate());
                                        break;
                                    case 2:
                                        activity.currentDialog = dialog;
                                        activity.requestType = NoteActivity.RequestType.CHANGE_PHOTO;
                                        eventToChange = dayEvent;
                                        changePhoto(dayEvent.getPic());
                                        break;
                                    case 3:
                                        SaveSharedPreferences.removeDate(ctx, dayEvent);
                                        events.remove(position - 2);
                                        notifyDataSetChanged();
                                        break;
                                }
                            }
                        });
                        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                return convertView;
            } else if (event.getEventType() == EventType.REMINDER) {
                final ReminderViewHolder viewHolder;
                boolean isViewNull = convertView == null;
                boolean isTagNull = false;
                try {
                    isTagNull = convertView.getTag() == null;
                } catch (NullPointerException e) {
                    isTagNull = false;
                }
                if (isViewNull || isTagNull) {
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.event_row, parent, false);
                    viewHolder = new ReminderViewHolder();
                    viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ReminderViewHolder) convertView.getTag();
                }
                Reminder reminder = (Reminder) event;
                ((TextView) convertView.findViewById(R.id.name)).setText(reminder.getEventName());
                if (reminder.getPic() == null) {
                    ((ImageView) convertView.findViewById(R.id.image)).setImageResource(R.drawable.no_icon);
                } else {
                    Picasso.get().load(reminder.getPic()).into((ImageView) convertView.findViewById(R.id.image));
                }
                Date curRem = new Date(d.getTime());
                Date bDateRem = new Date(event.getDate().getTime());
                String dateRem = new SimpleDateFormat("dd.MM.yyyy г.").format(bDateRem);
                ((TextView) convertView.findViewById(R.id.when)).setText(howMuchInDaysReminder(curRem, bDateRem));
                ((TextView) convertView.findViewById(R.id.date)).setText(dateRem);
                convertView.findViewById(R.id.linearLayout5).setVisibility(View.GONE);
                viewHolder.expandableTextView.setText(reminder.desc, mCollapsedStatus, position - 2);
                convertView.setClickable(true);
                convertView.setLongClickable(true);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Выберите опцию");
                        builder.setItems(new String[]{"Изменить название и описание", "Изменить дату", "Изменить фото", "Удалить"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        eventToChange = reminder;
                                        changeName();
                                        break;
                                    case 1:
                                        eventToChange = reminder;
                                        changeDateReminder(eventToChange.getDate());
                                        break;
                                    case 2:
                                        activity.currentDialog = dialog;
                                        activity.requestType = NoteActivity.RequestType.CHANGE_PHOTO;
                                        eventToChange = reminder;
                                        changePhoto(reminder.getPic());
                                        break;
                                    case 3:
                                        SaveSharedPreferences.removeDate(ctx, reminder);
                                        events.remove(position - 2);
                                        notifyDataSetChanged();
                                }
                            }
                        });
                        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                return convertView;
            } else if (event.getEventType() == EventType.EVENT) {
                UsualEvent dayEvent = (UsualEvent) event;
                convertView = View.inflate(ctx, R.layout.event_row, null);
                ((TextView) convertView.findViewById(R.id.name)).setText(dayEvent.getEventName());
                if (dayEvent.getPic() == null) {
                    ((ImageView) convertView.findViewById(R.id.image)).setImageResource(R.drawable.no_icon);
                } else {
                    Picasso.get().load(dayEvent.getPic()).into((ImageView) convertView.findViewById(R.id.image));
                }
                Date cur = new Date(d.getTime());
                cur.setHours(1);
                cur.setMinutes(0);
                cur.setSeconds(0);
                Date bDate = new Date(event.getDate().getTime());
                bDate.setHours(0);
                bDate.setMinutes(0);
                bDate.setSeconds(0);
                long diff = cur.getTime() - bDate.getTime();
                long da = (long) (1000l * (60 * 60 * 24 * 365.25));
                long years = Math.round(diff / da);
                int age = (int) years;
                ((TextView) convertView.findViewById(R.id.age)).setText(String.valueOf(age));
                String date = new SimpleDateFormat("dd.MM.yyyy г.").format(bDate);
                String diffStr = howMuchInDays(cur, bDate);
                ((TextView) convertView.findViewById(R.id.when)).setText(diffStr);
                convertView.findViewById(R.id.age_head).setVisibility(View.GONE);
                ((TextView) convertView.findViewById(R.id.date)).setText(date);
                convertView.setClickable(true);
                convertView.setLongClickable(true);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Выберите опцию");
                        builder.setItems(new String[]{"Изменить название", "Изменить дату", "Изменить фото", "Удалить"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        eventToChange = dayEvent;
                                        changeName("Введите название");
                                        break;
                                    case 1:
                                        eventToChange = dayEvent;
                                        changeDate(eventToChange.getDate());
                                        break;
                                    case 2:
                                        activity.currentDialog = dialog;
                                        activity.requestType = NoteActivity.RequestType.CHANGE_PHOTO;
                                        eventToChange = dayEvent;
                                        changePhoto(dayEvent.getPic());
                                        break;
                                    case 3:
                                        SaveSharedPreferences.removeDate(ctx, dayEvent);
                                        events.remove(position - 2);
                                        notifyDataSetChanged();
                                        break;
                                }
                            }
                        });
                        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                return convertView;
            } else if (event.getEventType() == EventType.BIRTHDAY_NO_DATE) {
                BirthDayNoDateEvent dayEvent = (BirthDayNoDateEvent) event;
                convertView = View.inflate(ctx, R.layout.event_row, null);
                ((TextView) convertView.findViewById(R.id.name)).setText(dayEvent.getEventName());
                if (dayEvent.getPic() == null) {
                    ((ImageView) convertView.findViewById(R.id.image)).setImageResource(R.drawable.no_icon);
                } else {
                    Picasso.get().load(dayEvent.getPic()).into((ImageView) convertView.findViewById(R.id.image));
                }
                Date cur = new Date(d.getTime());
                cur.setHours(1);
                cur.setMinutes(0);
                cur.setSeconds(0);
                Date bDate = new Date(event.getDate().getTime());
                bDate.setHours(0);
                bDate.setMinutes(0);
                bDate.setSeconds(0);
                String date = new SimpleDateFormat("dd.MM").format(bDate);
                String diffStr = howMuchInDays(cur, bDate);
                ((TextView) convertView.findViewById(R.id.when)).setText(diffStr);
                convertView.findViewById(R.id.linearLayout5).setVisibility(View.GONE);
                ((TextView) convertView.findViewById(R.id.date)).setText(date);
                convertView.setClickable(true);
                convertView.setLongClickable(true);
                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                        builder.setTitle("Выберите опцию");
                        builder.setItems(new String[]{"Изменить название", "Изменить дату", "Изменить фото", "Удалить"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        eventToChange = dayEvent;
                                        changeName("Введите название");
                                        break;
                                    case 1:
                                        eventToChange = dayEvent;
                                        changeDateNoYear(eventToChange.getDate());
                                        break;
                                    case 2:
                                        activity.currentDialog = dialog;
                                        activity.requestType = NoteActivity.RequestType.CHANGE_PHOTO;
                                        eventToChange = dayEvent;
                                        changePhoto(dayEvent.getPic());
                                        break;
                                    case 3:
                                        SaveSharedPreferences.removeDate(ctx, dayEvent);
                                        events.remove(position - 2);
                                        notifyDataSetChanged();
                                }
                            }
                        });
                        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                        return true;
                    }
                });
                return convertView;
            }
        }
        return new View(ctx);
    }

    public String howMuchInDays(Date cur, Date bDay) {
        Date curDate = new Date(cur.getTime());
        Date date = new Date(bDay.getTime());
        date.setYear(curDate.getYear());
        Date diff = null;
        if (new SimpleDateFormat("dd.MM.yyyy").format(d).equals(new SimpleDateFormat("dd.MM.yyyy").format(date))) {
            diff = new Date(Math.abs(date.getTime() - curDate.getTime()));
        } else if (d.getTime() > date.getTime()) {
            date.setYear(d.getYear() + 1);
            diff = new Date(Math.abs(curDate.getTime() - date.getTime()));
        } else {
            date.setYear(d.getYear());
            diff = new Date(Math.abs(date.getTime() - curDate.getTime()));
        }
        long diffDays = diff.getTime() / (24 * 60 * 60 * 1000);
        if (diffDays == 0) {
            return "Сегодня";
        } else {
            return "Осталось " + String.valueOf(diffDays) + " " + plural(diffDays, "день", "дня", "дней") + "";
        }
    }

    public String howMuchInDaysReminder(Date cur, Date rem) {
        Date thisCur = new Date(cur.getTime());
        Date thisRem = new Date(rem.getTime());
        if (new SimpleDateFormat("d M yyyy").format(thisCur).equals(new SimpleDateFormat("d M yyyy").format(thisRem))) {
            return "Сегодня";
        } else {
            Date diff = new Date(thisRem.getTime() - thisCur.getTime());
            long diffDays = diff.getTime() / (24 * 60 * 60 * 1000);
            return "Осталось " + String.valueOf(diffDays) + " " + plural(diffDays, "день", "дня", "дней") + "";
        }
    }

    public String plural(long a, String form1, String form2, String form3) {
        int n = (int) (Math.abs(a) % 100);
        int n1 = n % 10;
        if (n > 10 && n < 20) return form3;
        if (n1 > 1 && n1 < 5) return form2;
        if (n1 == 1) return form1;
        return form3;
    }

    public void changePhoto(Uri pic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View root = LayoutInflater.from(ctx).inflate(R.layout.change_image_dialog, null);
        if (pic == null) {
            root.findViewById(R.id.photo_cont).setVisibility(View.GONE);
        } else {
            root.findViewById(R.id.photo_cont).setVisibility(View.VISIBLE);
            Picasso.get().load(pic).into(((ImageView) root.findViewById(R.id.chosen_photo)));
        }
        root.findViewById(R.id.choose_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setRequestedSize(300, 300).setAspectRatio(1, 1).start(activity);
            }
        });
        builder.setView(root);
        builder.setTitle("Изменить фото");
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Event event = eventToChange;
                SaveSharedPreferences.removeDate(ctx, eventToChange);
                event.setPic(pic);
                SaveSharedPreferences.addDate(ctx, event);
                notifyDataSetChanged();
            }
        });
        Dialog dialog = builder.create();
        root.findViewById(R.id.delete_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = eventToChange;
                SaveSharedPreferences.removeDate(ctx, eventToChange);
                event.setPic(null);
                SaveSharedPreferences.addDate(ctx, event);
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
        activity.currentDialog = dialog;
        dialog.show();
    }

    public void changeName(String hint) {
        View root = LayoutInflater.from(ctx).inflate(R.layout.change_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        EditText editText = ((EditText) root.findViewById(R.id.name));
        editText.setHint(hint);
        builder.setTitle("Изменить");
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(((EditText) root.findViewById(R.id.name)).getText().toString())) {
                    ((EditText) root.findViewById(R.id.name)).setError("Это поле не может быть пустым");
                } else {
                    Event event = eventToChange;
                    SaveSharedPreferences.removeDate(ctx, eventToChange);
                    event.setEventName(((EditText) root.findViewById(R.id.name)).getText().toString());
                    SaveSharedPreferences.addDate(ctx, event);
                    notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(root);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
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
                    dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    public void changeName() {
        View root = LayoutInflater.from(ctx).inflate(R.layout.change_name_and_desc, null);
        EditText desc = root.findViewById(R.id.desc);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        EditText editText = ((EditText) root.findViewById(R.id.name));
        builder.setTitle("Изменить");
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(((EditText) root.findViewById(R.id.name)).getText().toString())) {
                    ((EditText) root.findViewById(R.id.name)).setError("Это поле не может быть пустым");
                } else {
                    Event event = eventToChange;
                    SaveSharedPreferences.removeDate(ctx, eventToChange);
                    event.setEventName(((EditText) root.findViewById(R.id.name)).getText().toString());
                    ((Reminder) event).desc = desc.getText().toString();
                    SaveSharedPreferences.addDate(ctx, event);
                    notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setView(root);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (TextUtils.isEmpty(editText.getText().toString())) {
            dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
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
                    dialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                }
            }
        });
    }

    public void changeDate(Date cur) {
        View root = LayoutInflater.from(ctx).inflate(R.layout.second_page_bday_dialog, null);
        DatePicker picker = root.findViewById(R.id.calendar);
        Calendar c = Calendar.getInstance();
        c.setTime(cur);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        picker.init(year, month, day, null);
        picker.setMaxDate(d.getTime());
        CheckBox checkBox = root.findViewById(R.id.no_date);
        TextView error = root.findViewById(R.id.error);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    picker.findViewById(activity.getResources().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
                } else {
                    picker.findViewById(activity.getResources().getIdentifier("year", "id", "android")).setVisibility(View.VISIBLE);
                }
            }
        });
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle("Изменить");
        builder1.setView(root);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    Date date = new Date(0, picker.getMonth(), picker.getDayOfMonth());
                    BirthDayNoDateEvent event = new BirthDayNoDateEvent(eventToChange.getEventName(), date, eventToChange.getPic());
                    SaveSharedPreferences.removeDate(ctx, eventToChange);
                    SaveSharedPreferences.addDate(ctx, event);
                    events.remove(eventToChange);
                    events.add(event);
                    notifyDataSetChanged();
                } else {
                    Date date = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                    SaveSharedPreferences.removeDate(ctx, eventToChange);
                    eventToChange.setDate(date);
                    SaveSharedPreferences.addDate(ctx, eventToChange);
                    notifyDataSetChanged();
                }
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.create().show();
    }

    public void changeDateNoYear(Date cur) {
        View root = LayoutInflater.from(ctx).inflate(R.layout.second_page_reminder_dialog, null);
        DatePicker picker = root.findViewById(R.id.calendar);
        picker.findViewById(activity.getResources().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
        Calendar c = Calendar.getInstance();
        c.setTime(cur);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        picker.init(year, month, day, null);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle("Изменить");
        builder1.setView(root);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = new Date(0, picker.getMonth(), picker.getDayOfMonth());
                SaveSharedPreferences.removeDate(ctx, eventToChange);
                eventToChange.setDate(date);
                SaveSharedPreferences.addDate(ctx, eventToChange);
                notifyDataSetChanged();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.create().show();
    }

    public void changeDateReminder(Date cur) {
        View root = LayoutInflater.from(ctx).inflate(R.layout.second_page_reminder_dialog, null);
        DatePicker picker = root.findViewById(R.id.calendar);
        Calendar c = Calendar.getInstance();
        c.setTime(cur);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        picker.init(year, month, day, null);
        picker.setMinDate(d.getTime());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setTitle("Изменить");
        builder1.setView(root);
        builder1.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date date = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                SaveSharedPreferences.removeDate(ctx, eventToChange);
                eventToChange.setDate(date);
                SaveSharedPreferences.addDate(ctx, eventToChange);
                notifyDataSetChanged();
            }
        });
        builder1.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder1.create().show();
    }

    private class Diff {
        public long diffDays;
        public Event event;

        public Diff(long diffDays, Event event) {
            this.diffDays = diffDays;
            this.event = event;
        }
    }

    private class ReminderViewHolder {
        ExpandableTextView expandableTextView;
    }
}

