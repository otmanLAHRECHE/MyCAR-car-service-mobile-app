package co.example.hp.myapplication.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import co.example.hp.myapplication.R;
import co.example.hp.myapplication.SplashActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }


    public void showNotification(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 2020, intent, 0);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        int id_user = databaseHelper.getUserLogedInId();
        Verification verification = databaseHelper.getOldestVerificationWithOilChange(id_user);
        Log.d("///////////////////////////////////////receiver",String.valueOf(verification.getId()));
        if (verification.getId()==null){

        }else {
            Car car = databaseHelper.getCarSelected(verification.getId());
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle("Engine oil change reminder")
                    .setContentText("You need to verify the next engine oil change for your "+car.getCompany()+" car");
            mBuilder.setContentIntent(pi);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2020, mBuilder.build());
        }
    }
}
