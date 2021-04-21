package co.example.hp.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;

public class UpdateServiceCenter extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private Button btnupdateservice;
    private EditText name, phone,location;
    private Car car;
    private Service_center service_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service_center);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        service_center = (Service_center) intent.getSerializableExtra("servicekey");


        databaseHelper = new DatabaseHelper(this);

        name = (EditText) findViewById(R.id.etservice_name_update);
        name.setText(service_center.getName());
        phone = (EditText) findViewById(R.id.etservice_phone_update);
        int reg = service_center.getPhoneNumber();
        phone.setText(String.valueOf(reg));

        location = (EditText) findViewById(R.id.etservice_location_update);
        location.setText(service_center.getLocation());

        btnupdateservice = (Button) findViewById(R.id.btnupdateservice);

        btnupdateservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString().trim()) || TextUtils.isEmpty(location.getText().toString().trim()) || TextUtils.isEmpty(phone.getText().toString().trim()) ){

                    Toast.makeText(UpdateServiceCenter.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();

                }else {
                    databaseHelper.updateServiceCenter(service_center.getId(),name.getText().toString(),Integer.valueOf(phone.getText().toString()), location.getText().toString());
                    service_center.setName(name.getText().toString());
                    service_center.setLocation(location.getText().toString());
                    service_center.setPhoneNumber(Integer.valueOf(phone.getText().toString()));
                    name.setText("");
                    location.setText("");
                    phone.setText("");
                    Toast.makeText(UpdateServiceCenter.this, "update Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(UpdateServiceCenter.this,ServiceCenterSelected.class);
                    intent.putExtra("servicekey",service_center);
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
