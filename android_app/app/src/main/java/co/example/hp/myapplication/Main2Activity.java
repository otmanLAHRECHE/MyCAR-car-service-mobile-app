package co.example.hp.myapplication;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.Serializable;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.FilterReparationClass;
import co.example.hp.myapplication.classes.FilterVerificationClass;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Car car;
    private DrawerLayout dL;
    private ActionBarDrawerToggle abdt;
    private String where="";
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dL = (DrawerLayout)findViewById(R.id.drawer);
        abdt = new ActionBarDrawerToggle(this,dL,R.string.open,R.string.close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("dashboard");


        dL.addDrawerListener(abdt);
        abdt.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().hasExtra("filter_verf")) {
            Bundle verfArgs = new Bundle();
            FilterVerificationClass filterVerificationClass = (FilterVerificationClass) getIntent().getSerializableExtra("filter_verf");
            verfArgs.putSerializable("filter_verf",(Serializable) filterVerificationClass);
            car = filterVerificationClass.getCar();
            where="verif";

            Fragment verff = new VerificationFragment();
            verff.setArguments(verfArgs);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,verff).commit();
            getSupportActionBar().setTitle("Verifications");
            navigationView.setCheckedItem(R.id.verification);




        }else if (getIntent().hasExtra("filter_rep")){
            Bundle repArgs = new Bundle();
            FilterReparationClass filterReparationClass = (FilterReparationClass) getIntent().getSerializableExtra("filter_rep");
            repArgs.putSerializable("filter_rep",(Serializable) filterReparationClass);
            car = filterReparationClass.getCar();
            where="repare";

            Fragment rep_f = new ReparationFragment();
            rep_f.setArguments(repArgs);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,rep_f).commit();
            getSupportActionBar().setTitle("Reparations");
            navigationView.setCheckedItem(R.id.reparation);
        }else {

            Intent intent = getIntent();
            car =(Car) intent.getSerializableExtra("car");
            where="";

            if(savedInstanceState == null) {
                Bundle homeargs = new Bundle();
                homeargs.putSerializable("carkey",(Serializable) car);
                Fragment homef = new HomeFragment();
                homef.setArguments(homeargs);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homef).commit();
                navigationView.setCheckedItem(R.id.home);
            }
        }








    }


    @Override
    public void onBackPressed() {
        if (dL.isDrawerOpen(GravityCompat.START)){
            dL.closeDrawer(GravityCompat.START);
        }else if (where.isEmpty()){
            Intent intent = new Intent(Main2Activity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }else {
            Bundle homeargs = new Bundle();
            homeargs.putSerializable("carkey",(Serializable) car);
            Fragment homef = new HomeFragment();
            where="";
            homef.setArguments(homeargs);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homef).commit();
            getSupportActionBar().setTitle("Dashboard");
            navigationView.setCheckedItem(R.id.home);


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(abdt.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.home:
                Bundle homeargs = new Bundle();
                homeargs.putSerializable("carkey",(Serializable) car);
                Fragment homef = new HomeFragment();
                where="";
                homef.setArguments(homeargs);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homef).commit();
                getSupportActionBar().setTitle("Dashboard");
                break;
            case R.id.vehicle:
                Bundle carArgs = new Bundle();
                carArgs.putSerializable("carkey",(Serializable) car);
                Fragment carf = new VehicleFragment();
                where="home";
                carf.setArguments(carArgs);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,carf).commit();
                getSupportActionBar().setTitle("Vehicle");
                break;
            case R.id.verification:
                Bundle verfArgs = new Bundle();
                verfArgs.putSerializable("carkey",(Serializable) car);
                Fragment verff = new VerificationFragment();
                where="verif";
                verff.setArguments(verfArgs);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,verff).commit();
                getSupportActionBar().setTitle("Verifications");
                break;
            case R.id.reparation:
                Bundle repareArgs = new Bundle();
                repareArgs.putSerializable("carkey",(Serializable) car);
                Fragment repareF = new ReparationFragment();
                where="repare";
                repareF.setArguments(repareArgs);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,repareF).commit();
                getSupportActionBar().setTitle("Reparations");
                break;
            case R.id.exit:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
        }

        dL.closeDrawer(GravityCompat.START);
        return true;
    }



}
