package co.example.hp.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class AddReparation extends AppCompatActivity {
    private EditText etdate,etodo,etservicecenter,note;
    private MaterialButtonToggleGroup toggleGroup;
    private DatePickerDialog picker;
    private Button btnadd_reparation;
    private TextInputLayout tildate,tilcenter;
    private Service_center service_center;
    private DatabaseHelper databaseHelper;
    private Car car;
    public double odo  = 0;
    public String list_of_types="";
    public String dt = "";
    private int follower = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reparation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        Intent intent = getIntent();
        car =(Car) intent.getSerializableExtra("car");




        databaseHelper = new DatabaseHelper(this);


        etdate = this.findViewById(R.id.input_repare_date);
        etdate.setInputType(InputType.TYPE_NULL);
        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AddReparation.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }

        });

        etodo = this.findViewById(R.id.input_repare_odo);



        etservicecenter = this.findViewById(R.id.input_service_center_repare);


        etservicecenter.setInputType(InputType.TYPE_NULL);


        etservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChoseServiceCenter.class);
                startActivity(intent);

            }
        });



        note =this.findViewById(R.id.note_repare);

        btnadd_reparation = this.findViewById(R.id.btn_add_reparation);

        toggleGroup = (MaterialButtonToggleGroup) this.findViewById(R.id.toggle_group_repare_type);



        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked){
                    switch (checkedId){
                        case R.id.toggle_engine :{
                            if (follower == 0){list_of_types = list_of_types + "Engine";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Engine";
                                follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_clim :{
                            if (follower == 0){list_of_types = list_of_types + "Clim";
                                follower = follower +1;}
                            else {list_of_types = list_of_types + ",Clim";
                                follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_suspension :{
                            if (follower == 0){ list_of_types = list_of_types + "Suspension";
                                                    follower = follower +1;}
                            else {list_of_types = list_of_types + ",Suspension";
                                    follower = follower +1;}
                            break;
                        }
                        case R.id.toggle_paint :{
                            if (follower == 0){ list_of_types = list_of_types + "Structure/Paint";
                                                    follower = follower +1;}
                            else { list_of_types = list_of_types + ",Structure/Paint";
                                    follower = follower +1;}
                            break;
                        }
                    }
                }else {
                    switch (checkedId){
                        case R.id.toggle_engine :{
                            list_of_types.replace("Engine","");
                            break;
                        }
                        case R.id.toggle_clim :{
                            list_of_types.replace("Clim","");
                            break;
                        }
                        case R.id.toggle_suspension :{
                            list_of_types.replace("Suspension","");
                            break;
                        }
                        case R.id.toggle_paint :{
                            list_of_types.replace("Structure/Paint","");
                            break;
                        }
                    }

                }





            }
        });


        btnadd_reparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etdate.getText().toString().trim()) || TextUtils.isEmpty(etodo.getText().toString().trim()) || TextUtils.isEmpty(etservicecenter.getText().toString().trim()) || list_of_types.isEmpty()){

                    Toast.makeText(AddReparation.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();

                }else {



                Log.d("/////////////////////////repare",list_of_types.toString());

                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(etdate.getText().toString()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }



                long date = calendar.getTimeInMillis();


                String id_reparation = UUID.randomUUID().toString();
                databaseHelper.addReparation(id_reparation,date,Double.valueOf(etodo.getText().toString()),list_of_types,note.getText().toString(),car.getId(),service_center.getId());



                Intent intent = new Intent(AddReparation.this, Main2Activity.class);
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