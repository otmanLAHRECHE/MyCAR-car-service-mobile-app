package co.example.hp.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

public class AddVerification extends AppCompatActivity{
    private String [] state = {"not verified","god","bad"};
    private EditText etdate,etodo,etTypeoil,etnextodo,etservicecenter,note;
    private Switch sengineoil,soilfilter,sairfilter,scabinefilter,sbrakes,stransoil,s_light_replace,s_wheel_alignment,s_battery_replace,s_tires_change,s_fuel_filter_change,s_glass_change,s_mount_balance;
    private Spinner spengin,splight,sptire,spaircond;
    private DatePickerDialog picker;
    private Button btnaddverf;
    private TextInputLayout tildate,tilcenter;
    private Service_center service_center;
    private DatabaseHelper databaseHelper;
    private LinearLayout linly;
    private boolean oilfilter,airfilter,cabinfilter,brakes,transoil,vidange,light_replace,wheel_alignment,battery_replace,tires_change,fuel_filter_change,glass_change,mount_balance;
    private Engine_oil engine_oil;
    private Changes changes;
    private States states;
    private Car car;
    private String enginestate,lightstate,airconditionstate,tiresstate;
    private String oilvidange;
    public double odo  = 0;
    public String dt = "";





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_verification);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        Intent intent = getIntent();
        car =(Car) intent.getSerializableExtra("car");




        databaseHelper = new DatabaseHelper(this);
        oilfilter = false;
        airfilter = false;
        cabinfilter = false;
        brakes = false;
        transoil = false;
        vidange=false;





        etdate = this.findViewById(R.id.input_vir_date);
        etdate.setInputType(InputType.TYPE_NULL);
        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddVerification.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        etodo = this.findViewById(R.id.input_vir_odo);
        etTypeoil = this.findViewById(R.id.input_vir_type_oil_engine);
        etnextodo = this.findViewById(R.id.input_vir_nextodo);

        linly = (LinearLayout) this.findViewById(R.id.linlyvis);



        sengineoil = this.findViewById(R.id.engineoil);
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

        soilfilter = this.findViewById(R.id.oilfilter);
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

        sairfilter = this.findViewById(R.id.airfilter);
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

        scabinefilter = this.findViewById(R.id.cabinefilter);
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

        sbrakes = this.findViewById(R.id.brakes);
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

        stransoil = this.findViewById(R.id.transoil);
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
        s_light_replace = this.findViewById(R.id.light_replace);
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
        s_wheel_alignment = this.findViewById(R.id.wheel_alignment);
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
        s_battery_replace = this.findViewById(R.id.battery_replace);
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

        s_fuel_filter_change = this.findViewById(R.id.fuel_filter_change);
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
        s_tires_change = this.findViewById(R.id.tires_change);
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
        s_mount_balance = this.findViewById(R.id.mount_balance);
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
        s_glass_change = this.findViewById(R.id.glass_change);
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


        etservicecenter = this.findViewById(R.id.input_service_center);


        etservicecenter.setInputType(InputType.TYPE_NULL);


        etservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChoseServiceCenter.class);
                startActivity(intent);

            }
        });
















        spengin = this.findViewById(R.id.enginestate);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        spengin.setAdapter(adapter1);
        spengin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enginestate = state[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        splight = this.findViewById(R.id.lightstate);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        splight.setAdapter(adapter2);
        splight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                lightstate = state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sptire = this.findViewById(R.id.tiresstate);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        sptire.setAdapter(adapter3);
        sptire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tiresstate= state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spaircond =this.findViewById(R.id.aircondstate);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,state);
        spaircond.setAdapter(adapter4);
        spaircond.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                airconditionstate = state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        note =this.findViewById(R.id.noteverf);

        btnaddverf = this.findViewById(R.id.btnaddverification);







        btnaddverf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etdate.getText().toString().trim()) || TextUtils.isEmpty(etodo.getText().toString().trim()) || TextUtils.isEmpty(etservicecenter.getText().toString().trim())){

                    Toast.makeText(AddVerification.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();

                }else {



                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(etdate.getText().toString()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                long date = calendar.getTimeInMillis();

                changes = new Changes();
                String id_changes = UUID.randomUUID().toString();
                String id_state = UUID.randomUUID().toString();

                databaseHelper.addChanges(id_changes,String.valueOf(oilfilter),String.valueOf(airfilter),String.valueOf(cabinfilter),String.valueOf(brakes),String.valueOf(transoil),String.valueOf(light_replace),String.valueOf(wheel_alignment),String.valueOf(battery_replace),String.valueOf(tires_change),String.valueOf(fuel_filter_change),String.valueOf(glass_change),String.valueOf(mount_balance));
                databaseHelper.addStates(id_state,enginestate,lightstate,tiresstate,airconditionstate);

                Changes changes = databaseHelper.getLastChanges();
                States states = databaseHelper.getLastStates();

                if (vidange == true){

                    String id_engine_oil = UUID.randomUUID().toString();

                    databaseHelper.addEngineOil(id_engine_oil,etTypeoil.getText().toString(),Long.valueOf(etnextodo.getText().toString()));
                    Engine_oil eo = (Engine_oil) databaseHelper.getLastEngineOil();
                    oilvidange = eo.getId();


                }else {oilvidange = "0";}

                String id_verification = UUID.randomUUID().toString();
                databaseHelper.addVerification(id_verification,date,Double.valueOf(etodo.getText().toString()),note.getText().toString(),car.getId(),service_center.getId(), oilvidange,states.getId(),changes.getId());



                Intent intent = new Intent(AddVerification.this, Main2Activity.class);
                intent.putExtra("car",car);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();



                }




            }
        });



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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
