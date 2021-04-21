package co.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.UUID;

import co.example.hp.myapplication.classes.CarModels;
import co.example.hp.myapplication.database.DatabaseHelper;

public class AddCar extends AppCompatActivity {
    private String [] types ={"car","track","pickup"};
    private String car_type_final="",companySelected;
    private Button btnaddcar;
    private EditText etserial, etregistr,et_car_year;
    private AutoCompleteTextView etcompany, etmodel;
    private Spinner car_type;

    private DatabaseHelper databaseHelper;
    private HashMap<String, String[]> hmap = new HashMap<String, String[]>();
    private String []companies ={"chevrolet","mitsubishi","landrover","audi","bmw","cadillac","fiat","ford","gmc","honda","hyundai","kia","lexus","mazda","mercedesbenz","minicoper","nissan","suzuki","toyota","volkswagen","peugeot","renault"};
    private String []models={"p"};
    public AddCar() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CarModels md = new CarModels();
        hmap.put("chevrolet",md.chevrolet);
        hmap.put("mitsubishi",md.mitsubishi);
        hmap.put("landrover",md.landrover);
        hmap.put("audi",md.audi);
        hmap.put("bmw",md.bmw);
        hmap.put("cadillac",md.cadillac);
        hmap.put("fiat",md.fiat);
        hmap.put("ford",md.ford);
        hmap.put("gmc",md.gmc);
        hmap.put("honda",md.honda);
        hmap.put("hyundai",md.hyundai);
        hmap.put("kia",md.kia);
        hmap.put("lexus",md.lexus);
        hmap.put("mazda",md.mazda);
        hmap.put("mercedesbenz",md.mercedesbenz);
        hmap.put("minicoper",md.minicoper);
        hmap.put("nissan",md.nissan);
        hmap.put("suzuki",md.suzuki);
        hmap.put("toyota",md.toyota);
        hmap.put("volkswagen",md.volkswagen);
        hmap.put("peugeot",md.peugeot);
        hmap.put("renault",md.renault);

        final CarModels carModels = new CarModels();


        databaseHelper = new DatabaseHelper(this);

        btnaddcar = (Button) findViewById(R.id.btnaddcar);

        etcompany = (AutoCompleteTextView) findViewById(R.id.etcompany);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,companies);
        etcompany.setAdapter(adapter1);

        TextWatcher companyToModel = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                companySelected = s.toString();
                switch (companySelected){
                    case "chevrolet":
                        models = carModels.chevrolet;
                        break;
                    case "mitsubishi":
                        models = carModels.mitsubishi;
                        break;
                    case "landrover":
                        models = carModels.landrover;
                        break;
                    case "audi":
                        models = carModels.audi;
                        break;
                    case "bmw":
                        models = carModels.bmw;
                        break;
                    case "cadillac":
                        models = carModels.cadillac;
                        break;
                    case "ford":
                        models = carModels.ford;
                        break;
                    case "gmc":
                        models = carModels.gmc;
                        break;
                    case "honda":
                        models = carModels.honda;
                        break;
                    case "hyundai":
                        models = carModels.hyundai;
                        break;
                    case "kia":
                        models = carModels.kia;
                        break;
                    case "lexus":
                        models = carModels.lexus;
                        break;
                    case "fiat":
                        models = carModels.fiat;
                        break;
                    case "mazda":
                        models = carModels.mazda;
                        break;
                    case "mercedesbenz":
                        models = carModels.mercedesbenz;
                        break;
                    case "minicoper":
                        models = carModels.minicoper;
                        break;
                    case "nissan":
                        models = carModels.nissan;
                        break;
                    case "suzuki":
                        models = carModels.suzuki;
                        break;
                    case "toyota":
                        models = carModels.toyota;
                        break;
                    case "volkswagen":
                        models = carModels.volkswagen;
                        break;
                    case "peugeot":
                        models = carModels.peugeot;
                        break;
                    case "renault":
                        models = carModels.renault;
                        break;
                }
            }
        };

        etcompany.addTextChangedListener(companyToModel);

        etmodel = (AutoCompleteTextView) findViewById(R.id.etmodel);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, models);
        etmodel.setAdapter(adapter2);





        etserial = (EditText) findViewById(R.id.etserial);
        etregistr = (EditText) findViewById(R.id.etregistr);

        et_car_year = (EditText) findViewById(R.id.et_car_year);

        car_type = (Spinner) findViewById(R.id.add_car_car_type);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,types);
        car_type.setAdapter(adapter3);
        car_type.setSelection(0);
        car_type_final = types[0];
        car_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_type_final = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnaddcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etcompany.getText().toString().trim()) || TextUtils.isEmpty(etmodel.getText().toString().trim()) || TextUtils.isEmpty(etserial.getText().toString().trim()) ||TextUtils.isEmpty(etregistr.getText().toString().trim())){

                    Toast.makeText(AddCar.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();

                }else {
                    int id_user = databaseHelper.getUserLogedInId();
                    String id = UUID.randomUUID().toString();
                databaseHelper.addCar(id,etcompany.getText().toString(), etmodel.getText().toString(),Integer.valueOf(et_car_year.getText().toString()),car_type_final, etserial.getText().toString(),Integer.valueOf(etregistr.getText().toString()),id_user);
                etcompany.setText("");
                etmodel.setText("");
                etserial.setText("");
                etregistr.setText("");
                et_car_year.setText("");
                car_type.setSelection(0);
                Toast.makeText(AddCar.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddCar.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
