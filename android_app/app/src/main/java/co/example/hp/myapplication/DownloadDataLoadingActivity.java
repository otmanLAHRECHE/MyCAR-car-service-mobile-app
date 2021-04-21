package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadDataLoadingActivity extends AppCompatActivity {

    private int id_user;
    private DatabaseHelper databaseHelper;
    private LoginRepository loginRepository ;
    private JSONObject object;
    private DownloadDataRepository downloadDataRepository;
    private JSONArray responseCars,responseServiceCenters,responseVerification,responseReparation;
    private int index;
    private TextView textView,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data_loading);


        databaseHelper = new DatabaseHelper(this);
        id_user = databaseHelper.getUserLogedInId();

        textView2 = this.findViewById(R.id.textview_progress_download_data);
        textView = this.findViewById(R.id.textview_progress_download_data_2);


        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));

        loginRepository = new LoginRepository(null);

        loginRepository.restoreLogin(databaseHelper.getUserLogedIn().getEmail().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
            @Override
            public void onSuccess() {
                object = loginRepository.jsonObjectReturn;

                if (object == null){

                }else {

                    int id = databaseHelper.getUserLogedInId();
                    try {
                        databaseHelper.updateUser(id,object.getString("first_name"),object.getString("last_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    downloadDataRepository.downloadCars(new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            responseCars = downloadDataRepository.responseToReturn;

                             index = 0;

                            while (responseCars.length() > index){
                                try {
                                    JSONObject carJson = (JSONObject)responseCars.get(index);
                                    databaseHelper.addCar(carJson.getString("id"),carJson.getString("company"),carJson.getString("model"),carJson.getInt("year"),carJson.getString("car_type"),carJson.getString("serial"),carJson.getInt("registration"),id_user);
                                    databaseHelper.syncCar(carJson.getString("id"));
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
                                            databaseHelper.addServiceCenter(serviceCenterJson.getString("id"),serviceCenterJson.getString("name"),serviceCenterJson.getInt("phoneNumber"),id_user,serviceCenterJson.getString("location"));
                                            databaseHelper.syncServiceCenter(serviceCenterJson.getString("id"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            break;
                                        }
                                        index++;

                                    }

                                    if (responseCars.length() == 0){

                                        textView.setVisibility(View.GONE);
                                        textView2.setText("done");

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra("registration_update","ok");
                                                startActivity(intent);
                                                finish();

                                            }
                                        },3000);

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

                                            System.out.println(car.getId());

                                            downloadDataRepository.downloadVerifications(car, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {

                                                        responseVerification = downloadDataRepository.responseToReturn;
                                                    System.out.println(responseVerification);
                                                        int indexv = 0;
                                                        while (responseVerification.length() > indexv){
                                                            try {
                                                                JSONObject verificationJson = (JSONObject)responseVerification.get(indexv);
                                                                JSONObject serviceCenterLocalJson = (JSONObject)verificationJson.get("service_center");
                                                                JSONObject engineOilLocalJson = (JSONObject)verificationJson.get("engine_oil");
                                                                JSONObject changesLocalJson = (JSONObject)verificationJson.get("changes");
                                                                JSONObject stateLocalJson = (JSONObject)verificationJson.get("states");
                                                                System.out.println(engineOilLocalJson);
                                                                String ideo ;


                                                                System.out.println(engineOilLocalJson.getString("type"));

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
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                break;
                                                            }
                                                            indexv++;

                                                        }

                                                }
                                            });

                                                        downloadDataRepository.downloadReparations(car, new VolleyCallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                                responseReparation = downloadDataRepository.responseToReturn;
                                                                int indexr = 0;
                                                                while (responseReparation.length() > indexr){
                                                                    try {
                                                                        JSONObject reparationJson = (JSONObject)responseReparation.get(indexr);

                                                                        JSONObject serviceCenterLocalJson = (JSONObject)reparationJson.get("service_center");
                                                                        databaseHelper.addReparation(reparationJson.getString("id"),reparationJson.getLong("date"),reparationJson.getInt("odometer"),reparationJson.getString("what_repared"),reparationJson.getString("note"),car.getId(),serviceCenterLocalJson.getString("id"));
                                                                        databaseHelper.syncReparation(reparationJson.getString("id"));
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                        break;
                                                                    }
                                                                    indexr++;

                                                                }


                                                                textView.setVisibility(View.GONE);
                                                                textView2.setText("done");

                                                                Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                        intent.putExtra("registration_update","ok");
                                                                        startActivity(intent);
                                                                        finish();

                                                                    }
                                                                },3000);



                                                            }
                                                        });







                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            break;
                                        }
                                        index++;



                                    }

















                                }
                            });









                        }
                    });


                    ////


                }
            }
        });






    }


}