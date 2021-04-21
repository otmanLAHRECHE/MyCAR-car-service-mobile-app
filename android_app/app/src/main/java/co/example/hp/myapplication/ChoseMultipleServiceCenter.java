package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.shared_preferences.SharedRef;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChoseMultipleServiceCenter extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private AdapterMultipleServiceCenters adapterMultipleServiceCenters;
    private ArrayList<Service_center> serviceCenterArrayList;
    private ArrayList<Service_center> serviceCenterArrayListSelected;
    private int id_user;
    private FilterVerificationClass filterVerificationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_multiple_service_center);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        filterVerificationClass = (FilterVerificationClass) getIntent().getExtras().get("filter_service");

        databaseHelper = new DatabaseHelper(this);

        id_user = databaseHelper.getUserLogedInId();

        serviceCenterArrayList = databaseHelper.getAllServiceCenters(id_user);

        adapterMultipleServiceCenters = new AdapterMultipleServiceCenters(this,serviceCenterArrayList);

        listView = this.findViewById(R.id.listView_multiple_selection);
        serviceCenterArrayListSelected = new ArrayList<Service_center>();

        listView.setAdapter(adapterMultipleServiceCenters);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (serviceCenterArrayListSelected.contains(serviceCenterArrayList.get(position))){
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                    serviceCenterArrayListSelected.remove(serviceCenterArrayList.get(position));
                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    serviceCenterArrayListSelected.add(serviceCenterArrayList.get(position));
                }

            }
        });





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.done_chose:
                if (!(serviceCenterArrayListSelected.size()==0)){

                    filterVerificationClass.setService_centerArrayList(serviceCenterArrayListSelected);
                }
                Intent intent = new Intent(this,FilterVerification.class);
                intent.putExtra("filter_service",filterVerificationClass);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();

                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}