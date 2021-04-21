package co.example.hp.myapplication;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

public class ChoseServiceCenter extends AppCompatActivity {
    private FloatingActionButton addservicecenter;
    private ListView listView;
    private ArrayList<Service_center> serviceCenterArrayList;
    private AdapterItemServiceCenter customAdapter;
    private int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_service_center);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) this.findViewById(R.id.lv_chose_service_centers);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        id_user = databaseHelper.getUserLogedInId();

        serviceCenterArrayList = databaseHelper.getAllServiceCenters(id_user);

        customAdapter = new AdapterItemServiceCenter(this,serviceCenterArrayList);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Service_center sc = (Service_center) serviceCenterArrayList.get(position);
                SharedRef sharedRef = new SharedRef(getApplicationContext());
                sharedRef.saveServiceCenter(sc);

                finish();
            }
        });



        addservicecenter = this.findViewById(R.id.addservicecentertochoseforlist);


        addservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoseServiceCenter.this,AddSeviceCenterToChose.class);
                startActivity(intent);
                finish();
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
