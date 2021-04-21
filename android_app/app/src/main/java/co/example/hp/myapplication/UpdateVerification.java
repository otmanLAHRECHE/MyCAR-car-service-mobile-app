package co.example.hp.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class UpdateVerification extends AppCompatActivity {
    private String [] state = {"not verified","god","bad"};
    private EditText etdate,etodo,etTypeoil,etnextodo,etservicecenter,note;
    private Switch sengineoil,soilfilter,sairfilter,scabinefilter,sbrakes,stransoil,s_light_replace,s_wheel_alignment,s_battery_replace,s_tires_change,s_fuel_filter_change,s_glass_change,s_mount_balance;
    private Spinner spengin,splight,sptire,spaircond;
    private DatePickerDialog picker;
    private TextInputLayout tildate,tilcenter;
    private Service_center service_center;
    private DatabaseHelper databaseHelper;
    private LinearLayout linly;
    private boolean oilfilter,airfilter,cabinfilter,brakes,transoil,vidange,light_replace,wheel_alignment,battery_replace,tires_change,fuel_filter_change,glass_change,mount_balance;
    private Engine_oil engine_oil;
    private Changes changes;
    private States states;
    private Verification verification;
    private Car car;
    private String enginestate,lightstate,airconditionstate,tiresstate;
    private String oilvidange;
    public double odo  = 0;
    public String dt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_verification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        oilfilter = false;
        airfilter = false;
        cabinfilter = false;
        brakes = false;
        transoil = false;
        vidange=false;

        databaseHelper = new DatabaseHelper(this);
        oilfilter = false;
        airfilter = false;
        cabinfilter = false;
        brakes = false;
        transoil = false;
        vidange=false;



        Intent intent = getIntent();

        verification = (Verification) intent.getSerializableExtra("verification");


        car = databaseHelper.getCarSelected(verification.getId());






        etdate = this.findViewById(R.id.input_vir_date_update);
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(verification.getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        etdate.setText(formatter.format(calendar.getTime()));
        etdate.setInputType(InputType.TYPE_NULL);
        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(UpdateVerification.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        etodo = this.findViewById(R.id.input_vir_odo_update);
        etodo.setText(String.valueOf(verification.getOdometer()));



        etTypeoil = this.findViewById(R.id.input_vir_type_oil_engine_update);

        etnextodo = this.findViewById(R.id.input_vir_nextodo_update);

        linly = (LinearLayout) this.findViewById(R.id.linlyvis_update);



        sengineoil = this.findViewById(R.id.engineoil_update);

        if(databaseHelper.therIsChangeOilInThisVerf(verification.getId())==true){
            engine_oil= (Engine_oil) databaseHelper.getEngineOilSelected(verification.getId());
            sengineoil.setChecked(true);
            linly.setVisibility(View.VISIBLE);
            vidange = true;
            etTypeoil.setText(engine_oil.getType());
            etnextodo.setText(String.valueOf(engine_oil.getNextOdometer()));
        }else {
            linly.setVisibility(View.GONE);
            sengineoil.setChecked(false);}

        sengineoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sengineoil.isChecked()){
                    linly.setVisibility(View.VISIBLE);
                    vidange = true;
                }else {
                    linly.setVisibility(View.GONE);
                    vidange = false;
                }
            }
        });




        soilfilter = this.findViewById(R.id.oilfilter_update);
        soilfilter.setChecked(false);
        soilfilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (soilfilter.isChecked()){
                    oilfilter = true;
                }else {
                    oilfilter = false;
                }
            }
        });

        sairfilter = this.findViewById(R.id.airfilter_update);
        sairfilter.setChecked(false);
        sairfilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sairfilter.isChecked()){
                    airfilter = true;
                }else {
                    airfilter = false;
                }
            }
        });

        scabinefilter = this.findViewById(R.id.cabinefilter_update);
        scabinefilter.setChecked(false);
        scabinefilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (scabinefilter.isChecked()){
                    cabinfilter = true;
                }else {
                    cabinfilter = false;
                }
            }
        });

        sbrakes = this.findViewById(R.id.brakes_update);
        sbrakes.setChecked(false);
        sbrakes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sbrakes.isChecked()){
                    brakes = true;
                }else {
                    brakes = false;
                }
            }
        });

        stransoil = this.findViewById(R.id.transoil_update);
        stransoil.setChecked(false);
        stransoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (stransoil.isChecked()){
                    transoil = true;
                }else {
                    transoil = false;
                }
            }
        });
        s_light_replace = this.findViewById(R.id.light_replace_update);
        s_light_replace.setChecked(false);
        s_light_replace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_light_replace.isChecked()){
                    light_replace = true;
                }else {
                    light_replace = false;
                }
            }
        });
        s_wheel_alignment = this.findViewById(R.id.wheel_alignment_update);
        s_wheel_alignment.setChecked(false);
        s_wheel_alignment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_wheel_alignment.isChecked()){
                    wheel_alignment = true;
                }else {
                    wheel_alignment = false;
                }
            }
        });
        s_battery_replace = this.findViewById(R.id.battery_replace_update);
        s_battery_replace.setChecked(false);
        s_battery_replace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_battery_replace.isChecked()){
                    battery_replace = true;
                }else {
                    battery_replace = false;
                }
            }
        });

        s_fuel_filter_change = this.findViewById(R.id.fuel_filter_change_update);
        s_fuel_filter_change.setChecked(false);
        s_fuel_filter_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_fuel_filter_change.isChecked()){
                    fuel_filter_change = true;
                }else {
                    fuel_filter_change = false;
                }
            }
        });
        s_tires_change = this.findViewById(R.id.tires_change_update);
        s_tires_change.setChecked(false);
        s_tires_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_tires_change.isChecked()){
                    tires_change = true;
                }else {
                    tires_change = false;
                }
            }
        });
        s_mount_balance = this.findViewById(R.id.mount_balance_update);
        s_mount_balance.setChecked(false);
        s_mount_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_mount_balance.isChecked()){
                    mount_balance = true;
                }else {
                    mount_balance = false;
                }
            }
        });
        s_glass_change = this.findViewById(R.id.glass_change_update);
        s_glass_change.setChecked(false);
        s_glass_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s_glass_change.isChecked()){
                    glass_change = true;
                }else {
                    glass_change = false;
                }
            }
        });

        changes = (Changes) databaseHelper.getChangeSelected(verification.getId());
        states = (States) databaseHelper.getStatesSelected(verification.getId());

        if (changes.isOil_filter() == true){
            soilfilter.setChecked(true);
        }
        if (changes.isAir_filter() == true){
            sairfilter.setChecked(true);

        }
        if (changes.isCabine_filter() == true){
            scabinefilter.setChecked(true);
        }
        if (changes.isBrakes() == true){
            sbrakes.setChecked(true);
        }
        if (changes.isTransmission_oil()== true){
            stransoil.setChecked(true);
        }if (changes.isWheel_alignment()== true){
            s_wheel_alignment.setChecked(true);
        }if (changes.isBattery_replace()== true){
            s_battery_replace.setChecked(true);
        }if (changes.isLight_replace()== true){
            s_light_replace.setChecked(true);
        }if (changes.isTires_change()== true){
            s_tires_change.setChecked(true);
        }if (changes.isGlass_change()== true){
           s_glass_change.setChecked(true);
        }if (changes.isMount_balance()== true){
            s_mount_balance.setChecked(true);
        }if (changes.isFuel_filter_change()== true){
            s_fuel_filter_change.setChecked(true);
        }



        etservicecenter = this.findViewById(R.id.input_service_center_update);


        etservicecenter.setInputType(InputType.TYPE_NULL);

        service_center = (Service_center) databaseHelper.getServiceCenterSelected(verification.getId());

        etservicecenter.setText(service_center.getName());


        etservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChoseServiceCenter.class);
                startActivity(intent);

            }
        });
        
//"not verified","god","bad"


        spengin = this.findViewById(R.id.enginestate_update);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        spengin.setAdapter(adapter1);

        if (states.getEngineState().equals("not verified")){
            spengin.setSelection(0);
        }else if (states.getEngineState().equals("god")){
            spengin.setSelection(1);
        }else {
            spengin.setSelection(2);
        }

        spengin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enginestate = state[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        splight = this.findViewById(R.id.lightstate_update);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        splight.setAdapter(adapter2);

        if (states.getLightsState().equals("not verified")){
            splight.setSelection(0);
        }else if (states.getLightsState().equals("god")){
            splight.setSelection(1);
        }else {
            splight.setSelection(2);
        }

        splight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lightstate = state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sptire = this.findViewById(R.id.tiresstate_update);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        sptire.setAdapter(adapter3);

        if (states.getTiresState().equals("not verified")){
            sptire.setSelection(0);
        }else if (states.getTiresState().equals("god")){
            sptire.setSelection(1);
        }else {
            sptire.setSelection(2);
        }

        sptire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tiresstate= state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spaircond =this.findViewById(R.id.aircondstate_update);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        spaircond.setAdapter(adapter4);

        if (states.getAirConditioningState().equals("not verified")){
            spaircond.setSelection(0);
        }else if (states.getAirConditioningState().equals("god")){
            spaircond.setSelection(1);
        }else {
            spaircond.setSelection(2);
        }

        spaircond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                airconditionstate = state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        note =this.findViewById(R.id.noteverf_update);

        note.setText(verification.getNote());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        SharedRef sharedRef = new  SharedRef(this);



        if(sharedRef.loadServiceCenter()[1] != "no service center") {

            String id = sharedRef.loadServiceCenter()[0];
            String name = sharedRef.loadServiceCenter()[1];
            String location = sharedRef.loadServiceCenter()[2];
            int phone = Integer.valueOf(sharedRef.loadServiceCenter()[3]);


            service_center = new Service_center(id, phone, name, location);
            etservicecenter.setText(service_center.getName());
            sharedRef.deletefile(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_updates, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_updates:

                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(etdate.getText().toString()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                long date = calendar.getTimeInMillis();

                databaseHelper.updateChanges(changes.getId(),String.valueOf(oilfilter),String.valueOf(airfilter),String.valueOf(cabinfilter),String.valueOf(brakes),String.valueOf(transoil),String.valueOf(light_replace),String.valueOf(wheel_alignment),String.valueOf(battery_replace),String.valueOf(tires_change),String.valueOf(fuel_filter_change),String.valueOf(glass_change),String.valueOf(mount_balance));
                databaseHelper.updateStates(states.getId(),enginestate,lightstate,tiresstate,airconditionstate);


                if (vidange == true){

                    databaseHelper.updateEngineOil(engine_oil.getId(),etTypeoil.getText().toString(),Long.valueOf(etnextodo.getText().toString()));

                    oilvidange = engine_oil.getId();


                }else {oilvidange = "0";}

                databaseHelper.updateverification(verification.getId(),date,Double.valueOf(etodo.getText().toString()),note.getText().toString(),car.getId(),service_center.getId(), oilvidange,states.getId(),changes.getId());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}