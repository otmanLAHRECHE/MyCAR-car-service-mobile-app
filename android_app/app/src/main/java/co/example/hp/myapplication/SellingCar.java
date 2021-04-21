package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SellingCar extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ListView sell_car_list;
    private ArrayList<Car> carArrayList;
    private AdapterItemsCars customAdapter;
    private DatabaseHelper databaseHelper;
    private TextView textView;
    private Car car;
    private DownloadDataRepository downloadDataRepository;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_car);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sell_car_list = this.findViewById(R.id.sell_car_list);
        linearLayout = this.findViewById(R.id.there_is_cars_to_sell);
        textView = this.findViewById(R.id.there_is_no_car_to_sell);

        databaseHelper = new DatabaseHelper(this);

        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));



        int id_user = databaseHelper.getUserLogedInId();

        if(databaseHelper.thereIsNoCar(id_user)==Boolean.TRUE){
            textView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);

        }else {
            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

            carArrayList = databaseHelper.getAllCars(databaseHelper.getUserLogedInId());
            customAdapter = new AdapterItemsCars(getApplicationContext(),carArrayList);

            sell_car_list.setAdapter(customAdapter);

        }

        sell_car_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                car =(Car) carArrayList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(SellingCar.this);
                builder.setTitle(R.string.Enter_the_buyer_key);

// Set up the input
                final EditText input = new EditText(getApplicationContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

// Set up the butt  ons
                builder.setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        downloadDataRepository.sellCar(car, Integer.valueOf(m_Text.toString())/999, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                databaseHelper.deleteCar(car.getId());
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();





            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.trade_help:
                BottomSheetTradeHelp bottomSheetTradeHelp = new BottomSheetTradeHelp();
                bottomSheetTradeHelp.show(getSupportFragmentManager(),bottomSheetTradeHelp.getTag());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}