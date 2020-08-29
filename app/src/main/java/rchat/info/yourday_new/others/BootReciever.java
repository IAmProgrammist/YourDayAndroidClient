package rchat.info.yourday_new.others;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rchat.info.yourday_new.R;
import rchat.info.yourday_new.activities.NoteActivity;
import rchat.info.yourday_new.containers.events.BirthDayEvent;
import rchat.info.yourday_new.containers.events.Event;
import rchat.info.yourday_new.containers.events.Reminder;

public class BootReciever extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    String CHANNEL_ID = "push_on_boot_yourday";
    int norificationId = 4123;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            createNotificationChannel(context);
            List<Event> eventList = SaveSharedPreferences.getRequiredEvents(context, new Date(System.currentTimeMillis()));
            String lastPushUp = SaveSharedPreferences.getLastPushUp(context);
            String currentPushUp = new SimpleDateFormat("d M yyyy").format(new Date(System.currentTimeMillis()));
            if (eventList.size() != 0 && !lastPushUp.equals(currentPushUp)) {
                String mainDesc = "";
                mainDesc += "Не забудьте: " + "\n";
                List<BirthDayEvent> birthDayEvents = new ArrayList<>();
                List<Event> usualEvents = new ArrayList<>();
                List<Reminder> reminderEvents = new ArrayList<>();
                for (Event a : eventList) {
                    if (a instanceof BirthDayEvent) {
                        birthDayEvents.add((BirthDayEvent) a);
                    } else if (a instanceof Reminder) {
                        reminderEvents.add((Reminder) a);
                    } else {
                        usualEvents.add(a);
                    }
                }
                if (birthDayEvents.size() != 0) {
                    mainDesc += "Дни рождение(я): ";
                    for (BirthDayEvent a : birthDayEvents) {
                        mainDesc += a.getEventName() + ", ";
                    }
                    mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                    mainDesc += "." + "\n";
                }
                if (usualEvents.size() != 0) {
                    mainDesc += "Событие(я): ";
                    for (Event a : usualEvents) {
                        mainDesc += a.getEventName() + ", ";
                    }
                    mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                    mainDesc += "." + "\n";
                }
                if (reminderEvents.size() != 0) {
                    mainDesc += "Напоминание(я): ";
                    for (Reminder a : reminderEvents) {
                        mainDesc += a.getEventName() + ", ";
                    }
                    mainDesc = mainDesc.substring(0, mainDesc.length() - 2);
                    mainDesc += "." + "\n";
                }
                mainDesc += "Хорошего дня!";
                Intent notificationIntent = new Intent(context, NoteActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                builder.setSmallIcon(R.drawable.notes);
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_no_background));
                builder.setContentTitle("Напомнание");
                builder.setContentText(mainDesc);
                builder.setContentIntent(contentIntent);
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(mainDesc));
                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(context);
                notificationManager.notify(norificationId, builder.build());
                SaveSharedPreferences.setLastPushUp(context, currentPushUp);
            }
        }
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
