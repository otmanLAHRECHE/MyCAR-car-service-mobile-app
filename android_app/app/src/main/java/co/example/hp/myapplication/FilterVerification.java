package co.example.hp.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FilterVerification extends AppCompatActivity {
    private String [] state = {"all","nothing","good","bad"};
    private EditText startDate,endDate,serviceCenters;
    private Chip oldest,newest,before,after,between,greater,less,estimated,oilchange,oilfilter,airfilter,brakes,cabinfilter,transoil,light,wheel,battery,tires,fuel,glass,balance;
    private Spinner engineState,lightState,tiresState,airCondState;
    private SeekBar odometerChose;
    private TextView odometerChosed;
    private Button buttonReset;
    private DatePickerDialog picker;
    private FilterVerificationClass f;
    private DatabaseHelper databaseHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_verification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper= new DatabaseHelper(this);

        if (getIntent().hasExtra("filter_verf")){
            f = (FilterVerificationClass) getIntent().getSerializableExtra("filter_verf");
        }
        if(getIntent().hasExtra("filter_service")){
            f = (FilterVerificationClass) getIntent().getSerializableExtra("filter_service");

        }


        startDate = this.findViewById(R.id.edittext_filter_start);
        startDate.setVisibility(View.GONE);
        endDate = this.findViewById(R.id.edittext_filter_end);
        endDate.setVisibility(View.GONE);
        serviceCenters = this.findViewById(R.id.edittext_filter_service_centers);
        oldest = this.findViewById(R.id.chip_oldest);
        newest = this.findViewById(R.id.chip_newest);
        before = this.findViewById(R.id.chip_befor);
        between = this.findViewById(R.id.chip_between);
        after = this.findViewById(R.id.chip_after);
        greater = this.findViewById(R.id.chip_greater);
        less = this.findViewById(R.id.chip_less);
        estimated = this.findViewById(R.id.chip_estimated);
        oilchange = this.findViewById(R.id.chip_oil_change);
        oilfilter = this.findViewById(R.id.chip_oil_filter);
        airfilter = this.findViewById(R.id.chip_air_filter);
        brakes = this.findViewById(R.id.chip_brakes);
        cabinfilter = this.findViewById(R.id.chip_cabin_filter);
        transoil = this.findViewById(R.id.chip_trans_oil);
        light = this.findViewById(R.id.chip_light);
        wheel = this.findViewById(R.id.chip_wheel);
        battery = this.findViewById(R.id.chip_battery);
        tires = this.findViewById(R.id.chip_tires);
        fuel = this.findViewById(R.id.chip_fuel);
        glass = this.findViewById(R.id.chip_glass);
        balance = this.findViewById(R.id.chip_balance_check);
        engineState = this.findViewById(R.id.spinner_engine_state);
        lightState = this.findViewById(R.id.spinner_light_state);
        tiresState = this.findViewById(R.id.spinner_tires_state);
        airCondState = this.findViewById(R.id.spinner_air_cond_state);
        odometerChose = this.findViewById(R.id.seekBar_odometer);
        odometerChosed = this.findViewById(R.id.textview_filter_odometer);


        if (f.getOrder().equals("oldest")) {
            oldest.setSelected(true);
            newest.setSelected(false);
        } else {
            newest.setSelected(true);
            oldest.setSelected(false);
        }
        if (f.getDueDate() == null) {
        } else if (f.getDueDate().equals("before")) {
            before.setSelected(true);
            startDate.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(f.getStartDate());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            startDate.setText(formatter.format(calendar.getTime()));

        } else if (f.getDueDate().equals("after")) {
            after.setSelected(true);
            startDate.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(f.getStartDate());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            startDate.setText(formatter.format(calendar.getTime()));

        } else if (f.getDueDate().equals("between")) {
            between.setSelected(true);
            startDate.setVisibility(View.VISIBLE);
            endDate.setVisibility(View.VISIBLE);
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();

            calendar1.setTimeInMillis(f.getStartDate());
            calendar2.setTimeInMillis(f.getEndDate());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            startDate.setText(formatter.format(calendar1.getTime()));
            endDate.setText(formatter.format(calendar2.getTime()));
        }

        if (f.getDueOdometer() == null) {
            odometerChosed.setText("0");
            odometerChose.setClickable(false);
        } else if (f.getDueOdometer().equals("greater")) {
            greater.setSelected(true);
            odometerChosed.setText(String.valueOf(f.getOdometer()));

        } else if (f.getDueOdometer().equals("less")) {
            less.setSelected(true);
            odometerChosed.setText(String.valueOf(f.getOdometer()));
        } else if (f.getDueOdometer().equals("estimated")) {
            estimated.setSelected(true);
            odometerChosed.setText(String.valueOf(f.getOdometer()));
        }

        if (f.getEngineOil() == true) {
            oilchange.setSelected(true);
        }
        if (f.getChanges() == null) {
        }
        else {

        if (f.getChanges().isOil_filter()) {
            oilfilter.setSelected(true);
        }
        if (f.getChanges().isAir_filter()) {
            airfilter.setSelected(true);
        }
        if (f.getChanges().isBrakes()) {
            brakes.setSelected(true);
        }
        if (f.getChanges().isCabine_filter()) {
            cabinfilter.setSelected(true);
        }
        if (f.getChanges().isTransmission_oil()) {
            transoil.setSelected(true);
        }
            if (f.getChanges().isLight_replace()) {
                light.setSelected(true);
                light.setChecked(true);
            }
            if (f.getChanges().isWheel_alignment()) {
                wheel.setSelected(true);
                wheel.setChecked(true);
            }
            if (f.getChanges().isBattery_replace()) {
                battery.setSelected(true);
                battery.setChecked(true);
            }
            if (f.getChanges().isTires_change()) {
                tires.setSelected(true);
                tires.setChecked(true);
            }
            if (f.getChanges().isFuel_filter_change()) {
                fuel.setSelected(true);
                fuel.setChecked(true);
            }
            if (f.getChanges().isGlass_change()) {
                glass.setSelected(true);
                glass.setChecked(true);
            }
            if (f.getChanges().isMount_balance()) {
                balance.setSelected(true);
                balance.setChecked(true);
            }

         }

        if (f.getService_centerArrayList()==null){

        }else {
            int i = 0;
            String edit = "";
            while(i <f.getService_centerArrayList().size()){
                edit = edit +f.getService_centerArrayList().get(i).getName().toString()+ ", ";
                i++;
            }
            serviceCenters.setText(edit);
        }





        ///////////////////////////////////////////////////////////////




        odometerChose.setMin(0);
        odometerChose.setMax(500000);
        odometerChose.setProgress((int)f.getOdometer());
        odometerChose.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = (int)f.getOdometer();

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progressValue = progress;
            odometerChosed.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                odometerChosed.setText(String.valueOf(progressValue));
                f.setOdometer((double) progressValue);

            }
        });



        oldest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setOrder("oldest");
                    newest.setSelected(false);
                }else {oldest.setSelected(false);}
            }
        });
        newest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setOrder("newest");
                    oldest.setSelected(false);
                }else {newest.setSelected(false);}
            }
        });
        before.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueDate("before");
                    startDate.setVisibility(View.VISIBLE);
                    startDate.setHint("before");
                    endDate.setVisibility(View.GONE);
                }else{
                    startDate.setVisibility(View.GONE);
                    f.setDueDate(null);
                }
            }
        });

        after.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueDate("after");
                    startDate.setVisibility(View.VISIBLE);
                    startDate.setHint("after");
                    endDate.setVisibility(View.GONE);
                }else{
                    startDate.setVisibility(View.GONE);
                    f.setDueDate(null);
                }
            }
        });

        between.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueDate("between");
                    startDate.setVisibility(View.VISIBLE);
                    startDate.setHint("start");
                    endDate.setVisibility(View.VISIBLE);
                    endDate.setHint("end");

                }else{
                    startDate.setVisibility(View.GONE);
                    endDate.setVisibility(View.GONE);
                    f.setDueDate(null);
                }
            }
        });

        greater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueOdometer("greater");
                    odometerChose.setClickable(true);
                }
            }
        });

        less.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueOdometer("less");
                    odometerChose.setClickable(true);
                }
            }
        });
        estimated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setDueOdometer("estimated");
                    odometerChose.setClickable(true);
                }
            }
        });





        oilchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    f.setEngineOil(true);
                }else {
                    f.setEngineOil(false);
                }
            }
        });
        airfilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setAir_filter(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setAir_filter(false);
                    f.setChanges(changes);
                }
            }
        });
        oilfilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setOil_filter(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setOil_filter(false);
                    f.setChanges(changes);
                }
            }
        });

        brakes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setBrakes(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setBrakes(false);
                    f.setChanges(changes);
                }
            }
        });

        cabinfilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setCabine_filter(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setCabine_filter(false);
                    f.setChanges(changes);
                }
            }
        });

        transoil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setTransmission_oil(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setTransmission_oil(false);
                    f.setChanges(changes);
                }
            }
        });
        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setLight_replace(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setLight_replace(false);
                    f.setChanges(changes);
                }
            }
        });
        wheel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setWheel_alignment(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setWheel_alignment(false);
                    f.setChanges(changes);
                }
            }
        });
        battery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setBattery_replace(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setBattery_replace(false);
                    f.setChanges(changes);
                }
            }
        });
        tires.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setTires_change(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setTires_change(false);
                    f.setChanges(changes);
                }
            }
        });
        fuel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setFuel_filter_change(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setFuel_filter_change(false);
                    f.setChanges(changes);
                }
            }
        });
        glass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setGlass_change(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setGlass_change(false);
                    f.setChanges(changes);
                }
            }
        });
        balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes = f.getChanges();
                    changes.setMount_balance(true);
                    f.setChanges(changes);
                }else {
                    Changes changes ;
                    if (f.getChanges() == null){ changes =  new Changes();
                    }else { changes = f.getChanges();}
                    changes.setMount_balance(false);
                    f.setChanges(changes);
                }
            }
        });

        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(FilterVerification.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Calendar calendar = Calendar.getInstance();
                                try {
                                    calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(startDate.getText().toString()));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                f.setStartDate(calendar.getTimeInMillis());
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(FilterVerification.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                Calendar calendar = Calendar.getInstance();
                                try {
                                    calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(endDate.getText().toString()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                f.setEndDate(calendar.getTimeInMillis());
                            }
                        }, year, month, day);
                picker.show();
            }

        });




        serviceCenters.setInputType(InputType.TYPE_NULL);
        serviceCenters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChoseMultipleServiceCenter.class);
                intent.putExtra("filter_service",f);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);

        engineState.setAdapter(adapter1);
        if (f.getStates().getEngineState().equals("all")){
            engineState.setSelection(0);
        }else if (f.getStates().getEngineState().equals("good")){
            engineState.setSelection(2);

        }else if (f.getStates().getEngineState().equals("bad")){
            engineState.setSelection(3);

        }else if (f.getStates().getEngineState().equals("nothing")){
            engineState.setSelection(1);

        }
        engineState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                States states ;
                if (f.getStates() == null){ states =  new States();
                }else { states = f.getStates();}
                states.setEngineState(state[position]);
                f.setStates(states);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lightState.setAdapter(adapter1);
        if (f.getStates().getLightsState().equals("all")){
            lightState.setSelection(0);
        }else if (f.getStates().getLightsState().equals("good")){
            lightState.setSelection(2);

        }else if (f.getStates().getLightsState().equals("bad")){
            lightState.setSelection(3);
        }else if (f.getStates().getLightsState().equals("nothing")){
            lightState.setSelection(1);
        }
        lightState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                States states ;
                if (f.getStates() == null){ states =  new States();
                }else { states = f.getStates();}
                states.setLightsState(state[position]);
                f.setStates(states);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tiresState.setAdapter(adapter1);
        if (f.getStates().getTiresState().equals("all")){
            tiresState.setSelection(0);
        }else if (f.getStates().getTiresState().equals("good")){
            tiresState.setSelection(2);

        }else if (f.getStates().getTiresState().equals("bad")){
            tiresState.setSelection(3);
        }else if (f.getStates().getTiresState().equals("nothing")){
            tiresState.setSelection(1);
        }
        tiresState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                States states ;
                if (f.getStates() == null){ states =  new States();
                }else { states = f.getStates();}
                states.setTiresState(state[position]);
                f.setStates(states);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        airCondState.setAdapter(adapter1);
        if (f.getStates().getAirConditioningState().equals("all")){
            airCondState.setSelection(0);
        }else if (f.getStates().getAirConditioningState().equals("good")){
            airCondState.setSelection(2);
        }else if (f.getStates().getAirConditioningState().equals("bad")){
            airCondState.setSelection(3);
        }else if (f.getStates().getAirConditioningState().equals("nothing")){
            airCondState.setSelection(1);
        }
        airCondState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                States states ;
                if (f.getStates() == null){ states =  new States();
                }else { states = f.getStates();}
                states.setAirConditioningState(state[position]);
                f.setStates(states);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonReset = this.findViewById(R.id.filter_reset_all);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                States states = new States();
                states.setEngineState("all");
                states.setTiresState("all");
                states.setLightsState("all");
                states.setAirConditioningState("all");

                FilterVerificationClass filterVerificationClass = new FilterVerificationClass(f.getCar(),"oldest",null,null,null,null,null,0,false,null,states);
                f = filterVerificationClass;
                oldest.setSelected(true);
                newest.setSelected(false);
                newest.setChecked(false);
                before.setSelected(false);
                before.setChecked(false);
                after.setSelected(false);
                after.setChecked(false);
                between.setSelected(false);
                between.setChecked(false);
                startDate.setText("");
                startDate.setVisibility(View.GONE);
                endDate.setText("");
                endDate.setVisibility(View.GONE);
                serviceCenters.setText("all service centers");
                greater.setSelected(false);
                greater.setChecked(false);
                estimated.setSelected(false);
                estimated.setChecked(false);
                less.setSelected(false);
                less.setChecked(false);
                oilchange.setSelected(false);
                oilchange.setChecked(false);
                oilfilter.setSelected(false);
                oilfilter.setChecked(false);
                airfilter.setSelected(false);
                airfilter.setChecked(false);
                brakes.setSelected(false);
                brakes.setChecked(false);
                cabinfilter.setSelected(false);
                cabinfilter.setChecked(false);
                transoil.setSelected(false);
                transoil.setChecked(false);
                light.setSelected(false);
                light.setChecked(false);
                wheel.setSelected(false);
                wheel.setChecked(false);
                battery.setSelected(false);
                battery.setChecked(false);
                tires.setSelected(false);
                tires.setChecked(false);
                fuel.setSelected(false);
                fuel.setChecked(false);
                glass.setSelected(false);
                glass.setChecked(false);
                balance.setSelected(false);
                balance.setChecked(false);
                odometerChosed.setText("0");
                engineState.setSelection(0);
                lightState.setSelection(0);
                tiresState.setSelection(0);
                airCondState.setSelection(0);
                odometerChose.setProgress(0);
            }
        });


    }





    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                return true;
            case R.id.filter_apply:

                Log.d("////////////////////////////////log_order",f.getOrder().toString());
                Log.d("////////////////////////////////log_dueDate",String.valueOf(f.getDueDate()));
                Log.d("////////////////////////////////log_startDate",String.valueOf(f.getStartDate()));
                Log.d("////////////////////////////////log_endDate",String.valueOf(f.getEndDate()));
                Log.d("////////////////////////////////log_dueOdometer",String.valueOf(f.getDueOdometer()));
                Log.d("////////////////////////////////log_odometer",String.valueOf(f.getOdometer()));
                Log.d("////////////////////////////////log_engine_oil",String.valueOf(f.getEngineOil()));
                if (f.getChanges()==null){
                }else {
                    Log.d("////////////////////////////////log_change_air_filter",String.valueOf(f.getChanges().isAir_filter()));
                    Log.d("////////////////////////////////log_change_trans",String.valueOf(f.getChanges().isTransmission_oil()));
                    Log.d("////////////////////////////////log_change_cabin_filter",String.valueOf(f.getChanges().isCabine_filter()));
                    Log.d("////////////////////////////////log_change_oil_filter",String.valueOf(f.getChanges().isOil_filter()));
                    Log.d("////////////////////////////////log_change_brake",String.valueOf(f.getChanges().isBrakes()));
                }
                if (f.getStates()==null){
                }else {
                    Log.d("////////////////////////////////log_state_air_con_state",String.valueOf(f.getStates().getAirConditioningState()));
                    Log.d("////////////////////////////////log_state_engine_state",String.valueOf(f.getStates().getEngineState()));
                    Log.d("////////////////////////////////log_state_light_state",String.valueOf(f.getStates().getLightsState()));
                    Log.d("////////////////////////////////log_state_tires_state",String.valueOf(f.getStates().getTiresState()));
                }

                Intent intent = new Intent(this,Main2Activity.class);
                intent.putExtra("filter_verf",f);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }




}