package net.pradhan.vacationapp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// Alarm triggers BroadcastReceiver
public class ToastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toast_message");
        if (message == null) message = "Alarm triggered!";
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}

