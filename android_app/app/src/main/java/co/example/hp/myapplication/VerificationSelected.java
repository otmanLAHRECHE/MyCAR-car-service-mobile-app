package co.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class VerificationSelected extends AppCompatActivity {
    private Car car;
    private Verification verification;
    private DatabaseHelper databaseHelper;
    private Service_center service_center;
    private TextView date,odo,serv_center,oiltype,oilnextodo,engine_state,light_state,tires_state,air_cond_state,note;
    private LinearLayout ll_engine_oil,ll_oil_filter,ll_air_filter,ll_cabin_filter,ll_brakes,ll_auto_oil,ll_light,ll_wheel,ll_battery,ll_tires,ll_fuel,ll_glass,ll_mount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_selected);

        databaseHelper = new DatabaseHelper(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if(intent.hasExtra("carkey")){
            car =(Car) intent.getSerializableExtra("carkey");
            verification = (Verification) intent.getSerializableExtra("verif");
        }else {
            verification = (Verification) intent.getSerializableExtra("verif");
            car = databaseHelper.getCarSelected(verification.getId());
        }

        service_center = (Service_center) databaseHelper.getServiceCenterSelected(verification.getId());
        Log.d("/////////////////////////////////////////verification_id",String.valueOf(verification.getId()));

        date = this.findViewById(R.id.verification_date_details);
        odo = this.findViewById(R.id.verification_odometer_details);
        serv_center = this.findViewById(R.id.verification_service_details);
        oiltype = this.findViewById(R.id.verf_details_oil_type);
        oilnextodo = this.findViewById(R.id.verf_details_next_odo);
        engine_state = this.findViewById(R.id.verification_details_state_engine);
        light_state = this.findViewById(R.id.verification_details_state_lights);
        tires_state = this.findViewById(R.id.verification_details_state_tires);
        air_cond_state = this.findViewById(R.id.verification_details_state_air_condition);
        note = this.findViewById(R.id.verification_details_note);

        ll_engine_oil = this.findViewById(R.id.verification_details_engine_oil_visible);
        ll_oil_filter = this.findViewById(R.id.verification_details_oil_filter_visible);
        ll_air_filter = this.findViewById(R.id.verification_details_air_filter_visible);
        ll_cabin_filter = this.findViewById(R.id.verification_details_cabin_filter_visible);
        ll_brakes = this.findViewById(R.id.verification_details_brakes_visible);
        ll_auto_oil = this.findViewById(R.id.verification_details_auto_oil_visible);
        ll_light = this.findViewById(R.id.verification_details_light_replace_visible);
        ll_wheel = this.findViewById(R.id.verification_details_wheel_al_visible);
        ll_battery = this.findViewById(R.id.verification_details_battery_visible);
        ll_tires = this.findViewById(R.id.verification_details_tires_visible);
        ll_fuel = this.findViewById(R.id.verification_details_fuel_filter_visible);
        ll_glass = this.findViewById(R.id.verification_details_glass_visible);
        ll_mount = this.findViewById(R.id.verification_details_mount_visible);






        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(verification.getDate());

        date.setText(calendar.getTime().toString());

        odo.setText(String.valueOf(verification.getOdometer()));

        serv_center.setText(service_center.getName());

        if(databaseHelper.therIsChangeOilInThisVerf(verification.getId())==true){
            ll_engine_oil.setVisibility(View.VISIBLE);
            Engine_oil engine_oil= (Engine_oil) databaseHelper.getEngineOilSelected(verification.getId());
            Toast.makeText(getApplicationContext(),engine_oil.getId().toString(),Toast.LENGTH_LONG).show();
            oiltype.setText(engine_oil.getType());
            oilnextodo.setText(String.valueOf(engine_oil.getNextOdometer()));
        }else {ll_engine_oil.setVisibility(View.GONE);}

        Changes changes = (Changes) databaseHelper.getChangeSelected(verification.getId());
        States states = (States) databaseHelper.getStatesSelected(verification.getId());

        if (changes.isOil_filter() == true){
            ll_oil_filter.setVisibility(View.VISIBLE);
        }
        if (changes.isAir_filter() == true){
            ll_air_filter.setVisibility(View.VISIBLE);

        }
        if (changes.isCabine_filter() == true){
            ll_cabin_filter.setVisibility(View.VISIBLE);
        }
        if (changes.isBrakes() == true){
            ll_brakes.setVisibility(View.VISIBLE);
        }
        if (changes.isTransmission_oil()== true){
            ll_auto_oil.setVisibility(View.VISIBLE);
        }if (changes.isWheel_alignment()== true){
            ll_wheel.setVisibility(View.VISIBLE);
        }if (changes.isBattery_replace()== true){
            ll_battery.setVisibility(View.VISIBLE);
        }if (changes.isTransmission_oil()== true){
            ll_auto_oil.setVisibility(View.VISIBLE);
        }if (changes.isLight_replace()== true){
            ll_light.setVisibility(View.VISIBLE);
        }if (changes.isTires_change()== true){
            ll_tires.setVisibility(View.VISIBLE);
        }if (changes.isGlass_change()== true){
            ll_glass.setVisibility(View.VISIBLE);
        }if (changes.isMount_balance()== true){
            ll_mount.setVisibility(View.VISIBLE);
        }if (changes.isFuel_filter_change()== true){
            ll_fuel.setVisibility(View.VISIBLE);
        }if (changes.isWheel_alignment()== true){
            ll_wheel.setVisibility(View.VISIBLE);
        }if (changes.isBattery_replace()== true){
            ll_battery.setVisibility(View.VISIBLE);
        }

        engine_state.setText(states.getEngineState());
        light_state.setText(states.getLightsState());
        tires_state.setText(states.getTiresState());
        air_cond_state.setText(states.getAirConditioningState());
        note.setText(verification.getNote());










    }

    public void deleteDialog(){
        AlertDialog.Builder deletealert = new AlertDialog.Builder(this);
        deletealert.setTitle("Confirm delete!!!");
        deletealert.setIcon(R.drawable.delete);
        deletealert.setMessage("Are you sure ypu want to delete this verification? ");
        deletealert.setCancelable(false);

        deletealert.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteverification(verification.getId());
                Intent intent = new Intent();
                intent.setClass(VerificationSelected.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        deletealert.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplication(),"delete canceled....",Toast.LENGTH_LONG).show();
            }
        });


        AlertDialog dialog = deletealert.create();
        dialog.show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_delete_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:

                finish();
                return true;
            case R.id.update_item:
                Intent intent = new Intent(VerificationSelected.this,UpdateVerification.class);
                intent.putExtra("verification",verification);
                startActivity(intent);
                return true;
            case R.id.delete_item:

                deleteDialog();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
