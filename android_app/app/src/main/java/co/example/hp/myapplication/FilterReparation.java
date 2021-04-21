package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.FilterReparationClass;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterReparation extends AppCompatActivity {
    private EditText startDate,endDate,serviceCenters;
    private MaterialButtonToggleGroup toggleGroup;
    private Chip oldest,newest,before,after,between,greater,less,estimated;
    private SeekBar odometerChose;
    private TextView odometerChosed;
    private Button buttonReset;
    private DatePickerDialog picker;
    private FilterReparationClass f;
    private DatabaseHelper databaseHelper ;
    private String list_of_types = "";
    private int follower = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_reparation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper= new DatabaseHelper(this);

        if (getIntent().hasExtra("filter_rep")){
            f = (FilterReparationClass) getIntent().getSerializableExtra("filter_rep");
        }
        if(getIntent().hasExtra("filter_service")){
            f = (FilterReparationClass) getIntent().getSerializableExtra("filter_service");
        }


        startDate = this.findViewById(R.id.edittext_filter_start_rep);
        startDate.setVisibility(View.GONE);
        endDate = this.findViewById(R.id.edittext_filter_end_rep);
        endDate.setVisibility(View.GONE);
        serviceCenters = this.findViewById(R.id.edittext_filter_service_centers_rep);
        oldest = this.findViewById(R.id.chip_oldest_rep);
        newest = this.findViewById(R.id.chip_newest_rep);
        before = this.findViewById(R.id.chip_befor_rep);
        between = this.findViewById(R.id.chip_between_rep);
        after = this.findViewById(R.id.chip_after_rep);
        greater = this.findViewById(R.id.chip_greater_rep);
        less = this.findViewById(R.id.chip_less_rep);
        estimated = this.findViewById(R.id.chip_estimated_rep);
        odometerChose = this.findViewById(R.id.seekBar_odometer_rep);
        odometerChosed = this.findViewById(R.id.textview_filter_odometer_rep);
        buttonReset = this.findViewById(R.id.filter_reset_all_rep);
        toggleGroup = this.findViewById(R.id.toggle_group_repare_type_rep_filter);


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

        if (f.getWhatRepared().contains("Engine") ){
            toggleGroup.check(R.id.toggle_engine_rep);
        }
        if (f.getWhatRepared().contains("Clim") ){
            toggleGroup.check(R.id.toggle_clim_rep);
        }
        if (f.getWhatRepared().contains("Suspension") ){
            toggleGroup.check(R.id.toggle_suspension_rep);
        }
        if (f.getWhatRepared().contains("Structure/Paint") ){
            toggleGroup.check(R.id.toggle_paint_rep);
        }




        ////////////////////////////////////////////////////////////////////////////////////////////////////



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

        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(FilterReparation.this,
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

                picker = new DatePickerDialog(FilterReparation.this,
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


        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    switch (checkedId){
                        case R.id.toggle_engine_rep :{
                            if (follower == 0){list_of_types = list_of_types + "Engine";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Engine";
                                follower = follower +1;}
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_clim_rep :{
                            if (follower == 0){list_of_types = list_of_types + "Clim";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Clim";
                                follower = follower +1;}
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_suspension_rep :{
                            if (follower == 0){ list_of_types = list_of_types + "Suspension";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Suspension";
                                follower = follower +1;}
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_paint_rep :{
                            if (follower == 0){ list_of_types = list_of_types + "Structure/Paint";
                                follower = follower +1;}
                            else { list_of_types = list_of_types + ",Structure/Paint";
                                follower = follower +1;}
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                    }
                }else {
                    switch (checkedId){
                        case R.id.toggle_engine_rep :{
                            list_of_types.replace("Engine","");
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_clim_rep :{
                            list_of_types.replace("Clim","");
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_suspension_rep :{
                            list_of_types.replace("Suspension","");
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                        case R.id.toggle_paint_rep :{
                            list_of_types.replace("Structure/Paint","");
                            f.setWhatRepared(list_of_types);
                            break;
                        }
                    }

                    if (group.getCheckedButtonId()==View.NO_ID){

                    }

                }





            }
        });







        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whatRepared = "";

                FilterReparationClass filterReparationClass = new FilterReparationClass(f.getCar(),"oldest",null,null,null,null,null,0,whatRepared);

                f = filterReparationClass;
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

                toggleGroup.clearChecked();

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
                Log.d("////////////////////////////////log_engine_oil",String.valueOf(f.getWhatRepared()));


                Intent intent = new Intent(this,Main2Activity.class);
                intent.putExtra("filter_rep",f);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        }

        return super.onOptionsItemSelected(item);
    }
}