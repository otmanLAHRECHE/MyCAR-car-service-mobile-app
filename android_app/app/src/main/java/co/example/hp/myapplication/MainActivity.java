package co.example.hp.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import co.example.hp.myapplication.data.DataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fb;
    private BottomNavigationView otmBottomNavigationView;
    private Toolbar toolbar;
    final Fragment fragmentDashboard = new DashboardMainFragment();
    final Fragment fragmentReport = new ReportMainFragment();
    final Fragment fragmentRecommendation = new RecommendationMainFragment();
    final Fragment fragmentMore = new MoreMainFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentDashboard;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = (Toolbar) this.findViewById(R.id.toolbar_mainact);
        setSupportActionBar(toolbar);

        otmBottomNavigationView = (BottomNavigationView) this.findViewById(R.id.customBottomBar);
        otmBottomNavigationView.inflateMenu(R.menu.bottom_navigation);

        Intent intent = getIntent();
        if (intent.hasExtra("register")){
            dataRepository = new DataRepository(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken","empty"));
            Log.d("/////////////////////////////////////////////////////////FB", "start FCM get token service");
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("/////////////////////////////////////////////////////////FB", "getInstanceId failed", task.getException());
                                return;
                            }

                            Log.d("/////////////////////////////////////////////////////////FB", "start FCM get token service");
                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("/////////////////////////////////////////////////////////FB", msg);
                            dataRepository.sendFCMToken(token, new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    Log.d("/////////////////////////////////////////////////////////FB", "device created");
                                    SplashActivity.sharedPreferences_FCM_token.edit().putBoolean("FCM_token",true);
                                }
                            });
                        }
                    });
        }else if (intent.hasExtra("registration_update")){
            dataRepository = new DataRepository(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken","empty"));
            Log.d("/////////////////////////////////////////////////////////FB", "start FCM get token service");
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("/////////////////////////////////////////////////////////FB", "getInstanceId failed", task.getException());
                                return;
                            }

                            Log.d("/////////////////////////////////////////////////////////FB", "start FCM get token service");
                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("/////////////////////////////////////////////////////////FB", msg);
                            dataRepository.updateFCMToken(token, new VolleyCallBack() {
                                @Override
                                public void onSuccess() {
                                    Log.d("/////////////////////////////////////////////////////////FB", "device updated");
                                    SplashActivity.sharedPreferences_FCM_token.edit().putBoolean("FCM_token",true);
                                }
                            });
                        }
                    });

        }




        if (getIntent().hasExtra("more")) {

            otmBottomNavigationView.setSelectedItemId(R.id.action_more_options);
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentMore, "4").hide(active).show(fragmentMore).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentRecommendation, "3").hide(fragmentRecommendation).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentReport, "2").hide(fragmentReport).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments,fragmentDashboard, "1").hide(fragmentDashboard).commit();
            active = fragmentMore;

        }else {
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentMore, "4").hide(fragmentMore).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentRecommendation, "3").hide(fragmentRecommendation).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments, fragmentReport, "2").hide(fragmentReport).commit();
            fm.beginTransaction().add(R.id.main_container_for_fragments,fragmentDashboard, "1").commit();
        }







        otmBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_dashboard:
                        fm.beginTransaction().hide(active).show(fragmentDashboard).commit();
                        active = fragmentDashboard;
                        return true;

                    case R.id.action_report:
                        fm.beginTransaction().hide(active).show(fragmentReport).commit();
                        active = fragmentReport;
                        return true;

                    case R.id.action_sync:
                        fm.beginTransaction().hide(active).show(fragmentRecommendation).commit();
                        active = fragmentRecommendation;
                        return true;
                    case R.id.action_more_options:
                        fm.beginTransaction().hide(active).show(fragmentMore).commit();
                        active = fragmentMore;
                        return true;
                }
                return false;
            }
        });




        fb = this.findViewById(R.id.fab_main_add);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddHome bottomSheetAddHome = new BottomSheetAddHome();
                bottomSheetAddHome.show(getSupportFragmentManager(),bottomSheetAddHome.getTag());
            }
        });






    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
