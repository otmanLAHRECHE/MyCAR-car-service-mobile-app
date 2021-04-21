package co.example.hp.myapplication;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import co.example.hp.myapplication.database.DatabaseHelper;

public class AddSeviceCenterToChose extends AppCompatActivity {
    private EditText etname,etphone,etlocation;
    private Button btnaddservicecenter;
    private DatabaseHelper databaseHelper;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sevice_center_to_chose);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        id_user = databaseHelper.getUserLogedInId();

        etname = this.findViewById(R.id.input_service_center_name_to_chose);
        etphone = this.findViewById(R.id.input_service_center_phone_to_chose);
        etlocation = this.findViewById(R.id.input_service_center_location_to_chose);
        btnaddservicecenter =this.findViewById(R.id.btnaddservicecenter_tochose);

        btnaddservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etname.getText().toString().trim()) || TextUtils.isEmpty(etphone.getText().toString().trim()) || TextUtils.isEmpty(etlocation.getText().toString().trim())){

                    Toast.makeText(AddSeviceCenterToChose.this, "Some fields are empty!!!!", Toast.LENGTH_SHORT).show();

                }else {
                    String id = UUID.randomUUID().toString();
                    databaseHelper.addServiceCenter(id,etname.getText().toString(),Integer.valueOf(etphone.getText().toString()),id_user,etlocation.getText().toString());
                    etname.setText("");
                    etphone.setText("");
                    etlocation.setText("");
                    Toast.makeText(AddSeviceCenterToChose.this, "Stored Successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddSeviceCenterToChose.this, ChoseServiceCenter.class);
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
