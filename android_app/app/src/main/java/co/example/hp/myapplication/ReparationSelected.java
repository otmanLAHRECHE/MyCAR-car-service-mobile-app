package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Calendar;

public class ReparationSelected extends AppCompatActivity {

    private Car car;
    private Repare repare;
    private DatabaseHelper databaseHelper;
    private Service_center service_center;
    private TextView date,odo,serv_center,note;
    private Chip engineChip,climChip,suspChip,paintChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparation_selected);



        databaseHelper = new DatabaseHelper(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent.hasExtra("carkey")){
            car =(Car) intent.getSerializableExtra("carkey");
            repare = (Repare) intent.getSerializableExtra("repare");
        }else{
            repare = (Repare) intent.getSerializableExtra("repare");
            car = databaseHelper.getCarSelectedFromReparation(repare.getId());
        }

        car =(Car) intent.getSerializableExtra("carkey");
        repare = (Repare) intent.getSerializableExtra("repare");
        service_center = (Service_center) databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());

        date = this.findViewById(R.id.reparation_date_details);
        odo = this.findViewById(R.id.reparation_odometer_details);
        serv_center = this.findViewById(R.id.reparation_service_details);
        note = this.findViewById(R.id.reparation_details_note);

        engineChip = (Chip) this.findViewById(R.id.chip_engine_repare);
        climChip = (Chip) this.findViewById(R.id.chip_clim_repare);
        suspChip = (Chip) this.findViewById(R.id.chip_susp_repare);
        paintChip = (Chip) this.findViewById(R.id.chip_paint_repare);


        String f ;
        f = repare.getWhatRepared();


        if (f.contains("Engine")){
            engineChip.setVisibility(View.VISIBLE);
        }
        if (f.contains("Clim")){
            climChip.setVisibility(View.VISIBLE);
        }
        if (f.contains("Suspension")){
            suspChip.setVisibility(View.VISIBLE);
        }
        if (f.contains("Structure/Paint")){
            paintChip.setVisibility(View.VISIBLE);
        }


        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(repare.getDate());

        date.setText(calendar.getTime().toString());

        odo.setText(String.valueOf(repare.getOdometer()));

        serv_center.setText(service_center.getName());

        note.setText(repare.getNote());

    }

    public void deleteDialog(){
        AlertDialog.Builder deletealert = new AlertDialog.Builder(this);
        deletealert.setTitle("Confirm delete!!!");
        deletealert.setIcon(R.drawable.delete);
        deletealert.setMessage("Are you sure ypu want to delete this reparation? ");
        deletealert.setCancelable(false);

        deletealert.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.deleteReparation(repare.getId());
                Intent intent = new Intent();
                intent.setClass(ReparationSelected.this,MainActivity.class);
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

                finish();
                return true;
            case R.id.update_item:
                Intent intent = new Intent(ReparationSelected.this,UpdateReparation.class);
                intent.putExtra("reparation",repare);
                startActivity(intent);
                return true;
            case R.id.delete_item:

                deleteDialog();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }



}