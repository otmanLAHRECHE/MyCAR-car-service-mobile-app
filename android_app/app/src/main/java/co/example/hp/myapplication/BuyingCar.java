package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Semaphore;

public class BuyingCar extends AppCompatActivity {
    private LinearLayout l_generating_code,l_code_is_generated,l_car_buyed;
    private TextView code,sync_text;
    private DownloadDataRepository downloadDataRepository;
    private JSONArray responseCars,responseServiceCenters,responseVerification,responseReparation;
    private DatabaseHelper databaseHelper;
    private int index;
    private int id_user;
    private int indexv,indexr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_car);






        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseHelper = new DatabaseHelper(this);
        id_user = databaseHelper.getUserLogedInId();
        l_generating_code = this.findViewById(R.id.generating_key);
        l_generating_code.setVisibility(View.VISIBLE);
        l_code_is_generated = this.findViewById(R.id.key_is_generated);
        l_code_is_generated.setVisibility(View.GONE);
        l_car_buyed = this.findViewById(R.id.car_buyed);
        l_car_buyed.setVisibility(View.GONE);
        code = this.findViewById(R.id.the_key);
        sync_text = this.findViewById(R.id.car_buyed_text);

        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));
        Intent intent = getIntent();
        if (intent.hasExtra("buying")){
            l_car_buyed.setVisibility(View.VISIBLE);
            l_generating_code.setVisibility(View.GONE);
            l_code_is_generated.setVisibility(View.GONE);
            sync_text.setText("Sync data");

            downloadDataRepository.downloadCars(new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    responseCars = downloadDataRepository.responseToReturn;

                    index = 0;

                    while (responseCars.length() > index){
                        try {
                            JSONObject carJson = (JSONObject)responseCars.get(index);
                            if (databaseHelper.isCarExisted(carJson.getString("id"))){

                            }else {
                                databaseHelper.addCar(carJson.getString("id"),carJson.getString("company"),carJson.getString("model"),carJson.getInt("year"),carJson.getString("car_type"),carJson.getString("serial"),carJson.getInt("registration"),id_user);
                                databaseHelper.syncCar(carJson.getString("id"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            break;
                        }
                        index++;

                    }

                    index = 0;
                    downloadDataRepository.downloadServiceCenters(new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            responseServiceCenters = downloadDataRepository.responseToReturn;


                            while (responseServiceCenters.length() > index){
                                try {
                                    JSONObject serviceCenterJson = (JSONObject)responseServiceCenters.get(index);
                                    if (databaseHelper.isServiceCenterExist(serviceCenterJson.getString("id"))){

                                    }else {
                                        databaseHelper.addServiceCenter(serviceCenterJson.getString("id"),serviceCenterJson.getString("name"),serviceCenterJson.getInt("phoneNumber"),id_user,serviceCenterJson.getString("location"));
                                        databaseHelper.syncServiceCenter(serviceCenterJson.getString("id"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    break;
                                }
                                index++;

                            }


                            index = 0;

                            while (responseCars.length() > index){
                                try {
                                    final JSONObject carJson = (JSONObject)responseCars.get(index);



                                    final Car car = new Car();

                                    car.setId(carJson.getString("id"));
                                    car.setCompany(carJson.getString("company"));
                                    car.setModel(carJson.getString("model"));
                                    car.setSerial(carJson.getString("serial"));
                                    car.setRegistration(carJson.getInt("registration"));

                                    downloadDataRepository.downloadReparations(car, new VolleyCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            responseReparation = downloadDataRepository.responseToReturn;
                                            indexr = 0;
                                            while (responseReparation.length() > indexr){
                                                try {
                                                    JSONObject reparationJson = (JSONObject)responseReparation.get(indexr);

                                                    JSONObject serviceCenterLocalJson = (JSONObject)reparationJson.get("service_center");
                                                    if (databaseHelper.isReparationExist(reparationJson.getString("id"))){

                                                    }else {
                                                        databaseHelper.addReparation(reparationJson.getString("id"),reparationJson.getLong("date"),reparationJson.getInt("odometer"),reparationJson.getString("what_repared"),reparationJson.getString("note"),car.getId(),serviceCenterLocalJson.getString("id"));
                                                        databaseHelper.syncReparation(reparationJson.getString("id"));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    break;
                                                }
                                                indexr++;

                                            }



                                        }
                                    });

                                    downloadDataRepository.downloadVerifications(car, new VolleyCallBack() {
                                        @Override
                                        public void onSuccess() {

                                            responseVerification = downloadDataRepository.responseToReturn;
                                            indexv = 0;

                                            Log.d("/////////////////////count",String.valueOf(responseVerification.length()));

                                            while (responseVerification.length() > indexv){
                                                try {
                                                    JSONObject verificationJson = (JSONObject)responseVerification.get(indexv);
                                                    JSONObject serviceCenterLocalJson = (JSONObject)verificationJson.get("service_center");
                                                    JSONObject engineOilLocalJson = (JSONObject)verificationJson.get("engine_oil");
                                                    JSONObject changesLocalJson = (JSONObject)verificationJson.get("changes");
                                                    JSONObject stateLocalJson = (JSONObject)verificationJson.get("states");
                                                    System.out.println(engineOilLocalJson);
                                                    String ideo ;


                                                    if (databaseHelper.isVerificationExist(verificationJson.getString("id"))){

                                                    }else {
                                                        if (engineOilLocalJson.getString("type").toString().equals("empty")){
                                                            ideo = "0";
                                                        }else {
                                                            databaseHelper.addEngineOil(engineOilLocalJson.getString("id"),engineOilLocalJson.getString("type"),engineOilLocalJson.getInt("nextOdometer"));
                                                            ideo = engineOilLocalJson.getString("id");
                                                        }

                                                        databaseHelper.addChanges(changesLocalJson.getString("id"),String.valueOf(changesLocalJson.getBoolean("oil_filter")),String.valueOf(changesLocalJson.getBoolean("air_filter")),String.valueOf(changesLocalJson.getBoolean("cabine_filter")),String.valueOf(changesLocalJson.getBoolean("brakes")),String.valueOf(changesLocalJson.getBoolean("transmission_oil")),String.valueOf(changesLocalJson.getBoolean("light_replace")),String.valueOf(changesLocalJson.getBoolean("wheel_alignment")),String.valueOf(changesLocalJson.getBoolean("battery_replace")),String.valueOf(changesLocalJson.getBoolean("tires_change")),String.valueOf(changesLocalJson.getBoolean("fuel_filter_change")),String.valueOf(changesLocalJson.getBoolean("glass_change")),String.valueOf(changesLocalJson.getBoolean("mount_and_balance")));
                                                        databaseHelper.addStates(stateLocalJson.getString("id"),stateLocalJson.getString("engineState"),stateLocalJson.getString("lightsState"),stateLocalJson.getString("tiresState"),stateLocalJson.getString("airConditioningState"));

                                                        databaseHelper.addVerification(verificationJson.getString("id"),verificationJson.getLong("date"),verificationJson.getInt("odometer"),verificationJson.getString("note"),car.getId(),serviceCenterLocalJson.getString("id"),ideo,stateLocalJson.getString("id"),changesLocalJson.getString("id"));
                                                        databaseHelper.syncVerification(verificationJson.getString("id"));
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    break;
                                                }
                                                indexv++;

                                            }






                                        }
                                    });









                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    break;
                                }
                                index++;

                            }






                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sync_text.setText("done");
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                },6000);















                        }
                    });









                }
            });

        }else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        downloadDataRepository.downloadBuyerKey(new VolleyCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                try {
                                                    code.setText(String.valueOf(downloadDataRepository.responseToReturnObject.getInt("user_key")*999));
                                                    l_generating_code.setVisibility(View.GONE);
                                                    l_code_is_generated.setVisibility(View.VISIBLE);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                    ,5000);
        }



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