package net.pradhan.vacationapp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// Alarm triggers BroadcastReceiver
public class ToastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "Scheduled Toast!", Toast.LENGTH_LONG).show();
    }
}

