package co.example.hp.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateReparation extends AppCompatActivity {
    private EditText etdate,etodo,etservicecenter,note;
    private MaterialButtonToggleGroup toggleGroup;
    private DatePickerDialog picker;
    private Service_center service_center;
    private DatabaseHelper databaseHelper;
    private Car car;
    public String list_of_types="";
    private int follower = 0;
    private Repare reparation;
    private MaterialButton engine,clim,susp,structure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reparation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();

        reparation = (Repare) intent.getSerializableExtra("reparation");

        car = databaseHelper.getCarSelectedFromReparation(reparation.getId());

        etdate = this.findViewById(R.id.input_repare_date_update);
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(reparation.getDate());

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

                picker = new DatePickerDialog(UpdateReparation.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        etodo = this.findViewById(R.id.input_repare_odo_update);
        etodo.setText(String.valueOf(reparation.getOdometer()));



        etservicecenter = this.findViewById(R.id.input_service_center_repare_update);


        etservicecenter.setInputType(InputType.TYPE_NULL);

        service_center = (Service_center) databaseHelper.getServiceCenterSelectedFromReparation(reparation.getId());

        etservicecenter.setText(service_center.getName());


        etservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChoseServiceCenter.class);
                startActivity(intent);

            }
        });



        note =this.findViewById(R.id.note_repare_update);

        note.setText(reparation.getNote().toString());


        toggleGroup = (MaterialButtonToggleGroup) this.findViewById(R.id.toggle_group_repare_type_update);

        String f ;
        f = reparation.getWhatRepared();

        engine = this.findViewById(R.id.toggle_engine_update);
        clim = this.findViewById(R.id.toggle_clim_update);
        susp = this.findViewById(R.id.toggle_suspension_update);
        structure = this.findViewById(R.id.toggle_paint_update);

        if (f.contains("Engine")){
            engine.setChecked(true);
        }
        if (f.contains("Clim")){
            clim.setChecked(true);
        }
        if (f.contains("Suspension")){
            susp.setChecked(true);
        }
        if (f.contains("Structure/Paint")){
            structure.setChecked(true);
        }

        list_of_types = f;

        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    switch (checkedId){
                        case R.id.toggle_engine_update :{
                            if (follower == 0){list_of_types = list_of_types + "Engine";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Engine";
                                follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_clim_update :{
                            if (follower == 0){list_of_types = list_of_types + "Clim";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Clim";
                                follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_suspension_update :{
                            if (follower == 0){ list_of_types = list_of_types + "Suspension";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Suspension";
                                follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_paint_update :{
                            if (follower == 0){ list_of_types = list_of_types + "Structure/Paint";
                                follower = follower +1;}
                            else { list_of_types = list_of_types + ",Structure/Paint";
                                follower = follower +1;}
                            break;
                        }
                    }
                }else {
                    switch (checkedId){
                        case R.id.toggle_engine_update :{
                            list_of_types.replace("Engine","");
                            break;
                        }
                        case R.id.toggle_clim_update :{
                            list_of_types.replace("Clim","");
                            break;
                        }
                        case R.id.toggle_suspension_update :{
                            list_of_types.replace("Suspension","");
                            break;
                        }
                        case R.id.toggle_paint_update :{
                            list_of_types.replace("Structure/Paint","");
                            break;
                        }
                    }

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



                databaseHelper.updateReparation(reparation.getId(),date,Double.valueOf(etodo.getText().toString()),list_of_types,note.getText().toString(),car.getId(),service_center.getId());

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}