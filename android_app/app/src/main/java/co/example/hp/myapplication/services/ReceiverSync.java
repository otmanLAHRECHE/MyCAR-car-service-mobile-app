package co.example.hp.myapplication.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.work.WorkManager;
import co.example.hp.myapplication.R;
import co.example.hp.myapplication.SplashActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class ReceiverSync extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SplashActivity.sharedPreferences_settings.getString("auto_sync","on").equals("on")){
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            int id_user = databaseHelper.getUserLogedInId();
            if (!(databaseHelper.getNotSyncedCars(id_user).size()==0) || !(databaseHelper.getNotSyncedCars(id_user).size()==0) || !(databaseHelper.getNotSyncedCars(id_user).size()==0) || !(databaseHelper.getNotSyncedCars(id_user).size()==0)){
                showNotification(context);
            }
        }

    }

    public void showNotification(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 2021, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Sync reminder")
                    .setContentText("you need to sync local data manually or activate auto-sync");
            mBuilder.setContentIntent(pi);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2021, mBuilder.build());

    }


}
