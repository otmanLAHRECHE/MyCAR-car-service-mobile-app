package co.example.hp.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.services.Receiver;
import co.example.hp.myapplication.services.ReceiverSync;
import co.example.hp.myapplication.work_manager.SyncWorker;

public class SplashActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ProgressBar progressBar;
    private TextView textView;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences sharedPreferences_settings;
    public static SharedPreferences sharedPreferences_firstRun;
    public static SharedPreferences sharedPreferences_FCM_token;
    public AlarmManager alarmManagerOilReminder,alarmManagerSyncReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences("auth",MODE_PRIVATE);
        sharedPreferences_settings = getSharedPreferences("settings",MODE_PRIVATE);
        sharedPreferences_firstRun =  getSharedPreferences("first_run",MODE_PRIVATE);
        sharedPreferences_FCM_token=  getSharedPreferences("FCM_token",MODE_PRIVATE);


        progressBar = this.findViewById(R.id.progress_splash);
        progressBar.setVisibility(View.GONE);
        textView = this.findViewById(R.id.textview_progress_splash);
        textView.setVisibility(View.GONE);
        databaseHelper = new DatabaseHelper(this);


        if (sharedPreferences_firstRun.getBoolean("firstrun", true)) {
            this.sharedPreferences_settings.edit().putString("auto_sync","on").commit();
            sharedPreferences_firstRun.edit().putBoolean("firstrun", false).commit();
            sharedPreferences_FCM_token.edit().putBoolean("FCM_token",false);
        }


        Log.d("///////////////////////////////if",sharedPreferences_settings.getString("auto_sync","on"));

        if (sharedPreferences_settings.getString("auto_sync","on").equals("on")){
            Log.d("/////////////////////////////////////startServicesSchedule","true");
            startWorkerManager();
        }
/*
        Intent intent = new Intent(SplashActivity.this, Receiver.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 2020, intent, 0);
        alarmManagerOilReminder = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManagerOilReminder.setRepeating(alarmManagerOilReminder.RTC_WAKEUP, System.currentTimeMillis(), alarmManagerOilReminder.INTERVAL_DAY*7, pendingIntent);

        Intent intent2 = new Intent(SplashActivity.this, ReceiverSync.class);
        intent2.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(SplashActivity.this, 2021, intent2, 0);
        alarmManagerSyncReminder = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManagerSyncReminder.setRepeating(alarmManagerSyncReminder.RTC_WAKEUP, System.currentTimeMillis(), alarmManagerSyncReminder.INTERVAL_HALF_DAY, pendingIntent2);


 */


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                CheckUser checkUser = new CheckUser(databaseHelper);
                checkUser.execute();
            }
        },3000);


    }



    public class CheckUser extends AsyncTask<Void, Void, Boolean> {
        DatabaseHelper databaseHelper;

        public CheckUser(DatabaseHelper databaseHelper){
            this.databaseHelper = databaseHelper;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean b = databaseHelper.therIsUserIn();

            return b;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result){

                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }



    }


    public void startWorkerManager() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncWorker.class,1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag("sync_worker")
                .build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);

    }



}
