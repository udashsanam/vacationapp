package net.pradhan.vacationapp.notification;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Schedular {

    public void scheduleToast(Context context, int month,int year, int day, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ToastReceiver.class);
        intent.putExtra("toast_message", message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        LocalDateTime x = LocalDateTime.of(year, month, day,
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getMinute()
        ).plusMinutes(1);
        if(LocalDateTime.now().isAfter(x)){
            return;
        }

        long triggerAt = x.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("Alarm set for: " + new Date(triggerAt));

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                x.atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli(),
                pendingIntent
        );
    }

}
