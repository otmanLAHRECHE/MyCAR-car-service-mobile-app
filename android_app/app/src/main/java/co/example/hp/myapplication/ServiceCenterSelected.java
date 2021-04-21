package co.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;

public class ServiceCenterSelected extends AppCompatActivity {
    private TextView name,phone,location;
    private Car car;
    private Service_center service_center;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_center_selected);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);



        Intent intent = getIntent();
        service_center = (Service_center)intent.getSerializableExtra("servicekey");


        name = this.findViewById(R.id.servicecentername_details);
        phone = this.findViewById(R.id.servicecenterphone_details);
        location = this.findViewById(R.id.servicesenterlocation_details);

        name.setText(service_center.getName().toString());
        phone.setText(String.valueOf(service_center.getPhoneNumber()));
        location.setText(service_center.getLocation().toString());








    }


    public void deleteDialog(){
        AlertDialog.Builder deletealert = new AlertDialog.Builder(this);
        deletealert.setTitle("Confirm delete!!!");
        deletealert.setIcon(R.drawable.delete);
        deletealert.setMessage("Are you sure ypu want to delete this service center? ");
        deletealert.setCancelable(false);

        deletealert.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteServiceCenter(service_center.getId());
                Intent intent = new Intent();
                intent.setClass(ServiceCenterSelected.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        deletealert.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplication(),"delete canceled....",Toast.LENGTH_LONG).show();
            }
        });


        AlertDialog dialog = deletealert.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_delete_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent2 = new Intent(ServiceCenterSelected.this,MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.update_item:
                Intent intent = new Intent(ServiceCenterSelected.this,UpdateServiceCenter.class);
                intent.putExtra("servicekey",service_center);
                startActivity(intent);
                return true;
            case R.id.delete_item:

                deleteDialog();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
