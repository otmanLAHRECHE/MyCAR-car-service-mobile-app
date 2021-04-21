package co.example.hp.myapplication;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.database.DatabaseHelper;

public class Update_car extends AppCompatActivity {
    private String [] types ={"car","track","pickup"};
    private String []companies ={"chevrolet","mitsubishi","landrover","audi","bmw","cadillac","fiat","ford","gmc","honda","hyundai","kia","lexus","mazda","mercedesbenz","minicoper","nissan","suzuki","toyota","volkswagen","peugeot","renault"};
    private String []models={"p"};
    private DatabaseHelper databaseHelper;
    private Button btnupdatecar;
    private EditText etserial, etregistr,et_year;
    private AutoCompleteTextView etcompany, etmodel;
    private Spinner spinnerCarType;
    private Car car;
    private String car_type_final="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        car =(Car) intent.getSerializableExtra("cartoupdate");


        databaseHelper = new DatabaseHelper(this);

        btnupdatecar = (Button) findViewById(R.id.btnupdatecar);

        etcompany = (AutoCompleteTextView) findViewById(R.id.etcompanyupdate);
        etcompany.setText(car.getCompany());
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,companies);
        etcompany.setAdapter(adapter1);

        etmodel = (AutoCompleteTextView) findViewById(R.id.etmodelupdate);
        etmodel.setText(car.getModel());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, models);
        etmodel.setAdapter(adapter2);

        etserial = (EditText) findViewById(R.id.etserialupdate);
        etserial.setText(car.getSerial());
        etregistr = (EditText) findViewById(R.id.etregistrupdate);
        et_year = (EditText) findViewById(R.id.et_year_update);
        et_year.setText(String.valueOf(car.getYear()));

        spinnerCarType = (Spinner) this.findViewById(R.id.update_car_type);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);
        spinnerCarType.setAdapter(adapter3);
        car_type_final = car.getCar_type();

        if (car.getCar_type().equals("car")){
            spinnerCarType.setSelection(0);
        }else if (car.getCar_type().equals("track")){
            spinnerCarType.setSelection(1);
        }else{
            spinnerCarType.setSelection(2);
        }

        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_type_final = types[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int reg = car.getRegistration();
        etregistr.setText(String.valueOf(reg));

        btnupdatecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etcompany.getText().toString().trim()) || TextUtils.isEmpty(etmodel.getText().toString().trim()) || TextUtils.isEmpty(etserial.getText().toString().trim()) ||TextUtils.isEmpty(etregistr.getText().toString().trim())){

                    Toast.makeText(Update_car.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();
                }else {
                    databaseHelper.updateCar(car.getId(),etcompany.getText().toString(), etmodel.getText().toString(),Integer.valueOf(et_year.getText().toString()),car_type_final, etserial.getText().toString(),Integer.valueOf(etregistr.getText().toString()));
                    car.setCompany(etcompany.getText().toString());
                    car.setModel(etmodel.getText().toString());
                    car.setSerial(etserial.getText().toString());
                    car.setRegistration(Integer.valueOf(etregistr.getText().toString()));
                    etcompany.setText("");
                    etmodel.setText("");
                    etserial.setText("");
                    etregistr.setText("");
                    Toast.makeText(Update_car.this, "update Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(Update_car.this,Main2Activity.class);
                    intent.putExtra("car",car);
                    startActivity(intent);


                    finish();


                }
            }
        });
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
