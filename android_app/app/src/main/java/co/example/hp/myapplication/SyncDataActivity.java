package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.ReparationToUpload;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.classes.VerificationToUpload;
import co.example.hp.myapplication.data.DataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.work_manager.SyncWorker;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SyncDataActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ProgressBar progressBarDataSync,progressBarCar,progressBarServiceCenter,progressBarVerification,progressBarReparation;
    private TextView textViewCar,textViewServiceCenter,textViewVerification,textViewReparation,textViewDataSync;
    private LinearLayout linearLayoutData;
    private ArrayList<Car> carArrayList = new ArrayList<Car>();
    private ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();
    private ArrayList<Service_center> serviceCenterArrayList = new ArrayList<Service_center>();
    private ArrayList<Repare> repareArrayList = new ArrayList<Repare>();
    private ArrayList<Car> carArrayList_update = new ArrayList<Car>();
    private ArrayList<Verification> verificationArrayList_update = new ArrayList<Verification>();
    private ArrayList<Service_center> serviceCenterArrayList_update = new ArrayList<Service_center>();
    private ArrayList<Repare> repareArrayList_update = new ArrayList<Repare>();
    private DataRepository dataRepository;
    private int idUser,carindex,verificationindex,servicecenterindex,reparationIndex;
    private ToggleButton toggleButton;
    private LinearLayout linearLayout;
    public Button sync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);

        databaseHelper = new DatabaseHelper(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        linearLayout = this.findViewById(R.id.sync_manual_ll);
        toggleButton = this.findViewById(R.id.sync_data_toggle);

        idUser = databaseHelper.getUserLogedInId();

        carArrayList = databaseHelper.getNotSyncedCars(idUser);
        carArrayList_update = databaseHelper.getNotUpdatedCars(idUser);

        verificationArrayList = databaseHelper.getAllNotSyncedVerifications(idUser);
        verificationArrayList_update = databaseHelper.getAllNotUpdatedVerifications(idUser);

        serviceCenterArrayList = databaseHelper.getAllNotSyncedServiceCenters(idUser);
        serviceCenterArrayList_update = databaseHelper.getAllNotUpdatedServiceCenters(idUser);

        repareArrayList = databaseHelper.getAllNotSyncedReparation(idUser);
        repareArrayList_update = databaseHelper.getAllNotUpdatedReparation(idUser);

        if (SplashActivity.sharedPreferences_settings.getString("auto_sync","empty").equals("off")){
            linearLayout.setVisibility(View.VISIBLE);
            checkIfSynced();
            toggleButton.setChecked(false);
            //BackgroundSyncDataService.shouldContinue = false;
            Log.d("/////////////////////////////////////shared_is","off");

        }else {
            linearLayout.setVisibility(View.GONE);
            toggleButton.setChecked(true);
            //BackgroundSyncDataService.shouldContinue = true;
            Log.d("/////////////////////////////////////shared_is","on");
        }



        Log.d("////////////////////////////////////////////////////////auth",SplashActivity.sharedPreferences.getString("authtoken","empty"));
        dataRepository = new DataRepository(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken","empty"));

        linearLayoutData = this.findViewById(R.id.data_is_synced);



        progressBarDataSync = this.findViewById(R.id.progress_data);
        progressBarCar = this.findViewById(R.id.progress_cars);
        progressBarServiceCenter = this.findViewById(R.id.progress_service_center);
        progressBarVerification = this.findViewById(R.id.progress_verification);
        progressBarReparation = this.findViewById(R.id.progress_reparation);


        textViewDataSync = this.findViewById(R.id.data_syncro_text);
        textViewCar = this.findViewById(R.id.loading_cars);
        textViewServiceCenter = this.findViewById(R.id.loading_services_centers);
        textViewVerification = this.findViewById(R.id.loading_verification);
        textViewReparation = this.findViewById(R.id.loading_reparation);


        sync = (Button) this.findViewById(R.id.button_sync_data);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    linearLayout.setVisibility(View.GONE);
                    SplashActivity.sharedPreferences_settings.edit().putString("auto_sync","on").commit();
                    Constraints constraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();

                    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncWorker.class,1, TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .addTag("sync_worker")
                            .build();


                    WorkManager.getInstance().enqueue(periodicWorkRequest);

                    Toast.makeText(getApplicationContext(),"auto sync enabled",Toast.LENGTH_LONG).show();
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                    checkIfSynced();
                    SplashActivity.sharedPreferences_settings.edit().putString("auto_sync","off").commit();
                    WorkManager.getInstance().cancelAllWorkByTag("sync_worker");

                    Toast.makeText(getApplicationContext(),"manual sync enabled",Toast.LENGTH_LONG).show();
                }
            }
        });

        sync.setClickable(false);


        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualSync();
            }

        });

    }


    ////////////////////////////////////////////////////////////////////////////////////



    public void manualSync(){

        progressBarCar.setVisibility(View.VISIBLE);
        progressBarServiceCenter.setVisibility(View.VISIBLE);
        progressBarVerification.setVisibility(View.VISIBLE);

        carindex = 0;
        verificationindex = 0;
        servicecenterindex = 0;
        reparationIndex = 0;

        if (carArrayList.size() == 0){
            progressBarCar.setVisibility(View.GONE);
            textViewCar.setText("done");
        }
        if (serviceCenterArrayList.size() == 0){
            progressBarServiceCenter.setVisibility(View.GONE);
            textViewServiceCenter.setText("done");

        }
        if (verificationArrayList.size() == 0){
            progressBarVerification.setVisibility(View.GONE);
            textViewVerification.setText("done");
        }
        if (repareArrayList.size() == 0){
            progressBarReparation.setVisibility(View.GONE);
            textViewReparation.setText("done");

        }

        if (carArrayList.size() == 0) {
            if (serviceCenterArrayList.size()==0){
                while(verificationindex < verificationArrayList.size()){

                    Verification verification = new Verification();
                    verification = verificationArrayList.get(verificationindex);
                    final VerificationToUpload verificationToUpload = new VerificationToUpload();
                    Changes changes = new Changes();
                    Service_center service_center = new Service_center();
                    States states = new States();
                    Engine_oil engine_oil = new Engine_oil();
                    Car car = new Car();

                    changes = databaseHelper.getChangeSelected(verification.getId());
                    states = databaseHelper.getStatesSelected(verification.getId());
                    service_center = databaseHelper.getServiceCenterSelected(verification.getId());
                    car = databaseHelper.getCarSelected(verification.getId());

                    verificationToUpload.setId(verification.getId().toString());
                    verificationToUpload.setDate(verification.getDate());
                    verificationToUpload.setOdometer(verification.getOdometer());
                    verificationToUpload.setNote(verification.getNote());
                    verificationToUpload.setCar(car);
                    verificationToUpload.setChanges(changes);
                    verificationToUpload.setService_center(service_center);
                    verificationToUpload.setStates(states);

                    if(databaseHelper.therIsChangeOilInThisVerf(verification.getId().toString())){
                        engine_oil = databaseHelper.getEngineOilSelected(verification.getId());
                        verificationToUpload.setEngine_oil(engine_oil);

                    }else {
                        engine_oil = new Engine_oil();
                        engine_oil.setId(UUID.randomUUID().toString());
                        engine_oil.setNextOdometer(0);
                        engine_oil.setType("empty");
                        verificationToUpload.setEngine_oil(engine_oil);
                    }

                    final Engine_oil finalEngine_oil = engine_oil;
                    final Changes finalChanges = changes;
                    final States finalStates = states;
                    final Verification finalVerification = verification;


                    try{
                        dataRepository.uploadEngineOil(finalEngine_oil, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                                dataRepository.uploadChange(finalChanges, new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {

                                        dataRepository.uploadStatus(finalStates, new VolleyCallBack() {
                                            @Override
                                            public void onSuccess() {

                                                dataRepository.uploadVerification(verificationToUpload, new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {

                                                        databaseHelper.syncVerification(finalVerification.getId());
                                                        progressBarVerification.setVisibility(View.GONE);
                                                        textViewVerification.setText("done");

                                                    }
                                                });

                                            }
                                        });
                                    }
                                });

                            }
                        });

                    }catch (Exception e){

                        Log.d(LoginActivity.TAG,e.getMessage());
                        Toast.makeText(getApplicationContext(),"error syncing verifications",Toast.LENGTH_LONG).show();
                        textViewVerification.setText("error");

                    }

                    verificationindex = verificationindex+1;
                }
                progressBarVerification.setVisibility(View.GONE);
                textViewVerification.setText("done");

                while(reparationIndex < repareArrayList.size()){

                    Repare repare = new Repare();
                    repare = repareArrayList.get(reparationIndex);
                    final ReparationToUpload reparationToUpload = new ReparationToUpload();
                    Service_center service_center = new Service_center();
                    Car car = new Car();

                    service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                    car = databaseHelper.getCarSelectedFromReparation(repare.getId());

                    reparationToUpload.setId(repare.getId().toString());
                    reparationToUpload.setDate(repare.getDate());
                    reparationToUpload.setOdometer(repare.getOdometer());
                    reparationToUpload.setNote(repare.getNote());
                    reparationToUpload.setCar(car);
                    reparationToUpload.setWhatRepared(repare.getWhatRepared());
                    reparationToUpload.setService_center(service_center);

                    final Repare finalRepare = repare;

                    try{
                        dataRepository.uploadReparation(reparationToUpload, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                databaseHelper.syncReparation(finalRepare.getId());
                                progressBarReparation.setVisibility(View.GONE);
                                textViewReparation.setText("done");
                            }
                        });

                    }catch (Exception e){

                        Log.d(LoginActivity.TAG,e.getMessage());
                        Toast.makeText(getApplicationContext(),"error syncing reparation",Toast.LENGTH_LONG).show();
                        textViewReparation.setText("error");

                    }

                    reparationIndex = reparationIndex+1;
                }
                progressBarReparation.setVisibility(View.GONE);
                textViewReparation.setText("done");

            }else {
                while (servicecenterindex < serviceCenterArrayList.size()){
                    final Service_center service_center = serviceCenterArrayList.get(servicecenterindex);
                    try {
                        dataRepository.uploadServiceCenter(service_center, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                databaseHelper.syncServiceCenter(service_center.getId());

                                if (servicecenterindex == serviceCenterArrayList.size()){
                                    progressBarServiceCenter.setVisibility(View.GONE);
                                    textViewServiceCenter.setText("done");

                                    while(verificationindex < verificationArrayList.size()){

                                        Verification verification = new Verification();
                                        verification = verificationArrayList.get(verificationindex);
                                        final VerificationToUpload verificationToUpload = new VerificationToUpload();
                                        Changes changes = new Changes();
                                        Service_center service_center = new Service_center();
                                        States states = new States();
                                        Engine_oil engine_oil = new Engine_oil();
                                        Car car = new Car();

                                        changes = databaseHelper.getChangeSelected(verification.getId());
                                        states = databaseHelper.getStatesSelected(verification.getId());
                                        service_center = databaseHelper.getServiceCenterSelected(verification.getId());
                                        car = databaseHelper.getCarSelected(verification.getId());

                                        verificationToUpload.setId(verification.getId().toString());
                                        verificationToUpload.setDate(verification.getDate());
                                        verificationToUpload.setOdometer(verification.getOdometer());
                                        verificationToUpload.setNote(verification.getNote());
                                        verificationToUpload.setCar(car);
                                        verificationToUpload.setChanges(changes);
                                        verificationToUpload.setService_center(service_center);
                                        verificationToUpload.setStates(states);

                                        if(databaseHelper.therIsChangeOilInThisVerf(verification.getId().toString())){
                                            engine_oil = databaseHelper.getEngineOilSelected(verification.getId());
                                            verificationToUpload.setEngine_oil(engine_oil);

                                        }else {
                                            engine_oil = new Engine_oil();
                                            engine_oil.setId(UUID.randomUUID().toString());
                                            engine_oil.setNextOdometer(0);
                                            engine_oil.setType("empty");
                                            verificationToUpload.setEngine_oil(engine_oil);
                                        }

                                        final Engine_oil finalEngine_oil = engine_oil;
                                        final Changes finalChanges = changes;
                                        final States finalStates = states;
                                        final Verification finalVerification = verification;

                                        try{
                                            dataRepository.uploadEngineOil(finalEngine_oil, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {

                                                    dataRepository.uploadChange(finalChanges, new VolleyCallBack() {
                                                        @Override
                                                        public void onSuccess() {

                                                            dataRepository.uploadStatus(finalStates, new VolleyCallBack() {
                                                                @Override
                                                                public void onSuccess() {

                                                                    dataRepository.uploadVerification(verificationToUpload, new VolleyCallBack() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            databaseHelper.syncVerification(finalVerification.getId());
                                                                            progressBarVerification.setVisibility(View.GONE);
                                                                            textViewVerification.setText("done");
                                                                                 }
                                                                    });

                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            Toast.makeText(getApplicationContext(),"error syncing verifications",Toast.LENGTH_LONG).show();
                                            textViewVerification.setText("error");

                                        }

                                        verificationindex = verificationindex+1;
                                    }
                                    progressBarVerification.setVisibility(View.GONE);
                                    textViewVerification.setText("done");

                                    while(reparationIndex < repareArrayList.size()){

                                        Repare repare = new Repare();
                                        repare = repareArrayList.get(reparationIndex);
                                        final ReparationToUpload reparationToUpload = new ReparationToUpload();
                                        Service_center service_center = new Service_center();
                                        Car car = new Car();

                                        service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                                        car = databaseHelper.getCarSelectedFromReparation(repare.getId());

                                        reparationToUpload.setId(repare.getId().toString());
                                        reparationToUpload.setDate(repare.getDate());
                                        reparationToUpload.setOdometer(repare.getOdometer());
                                        reparationToUpload.setNote(repare.getNote());
                                        reparationToUpload.setCar(car);
                                        reparationToUpload.setWhatRepared(repare.getWhatRepared());
                                        reparationToUpload.setService_center(service_center);

                                        final Repare finalRepare = repare;

                                        try{
                                            dataRepository.uploadReparation(reparationToUpload, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    databaseHelper.syncReparation(finalRepare.getId());
                                                    progressBarReparation.setVisibility(View.GONE);
                                                    textViewReparation.setText("done");
                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            Toast.makeText(getApplicationContext(),"error syncing reparation",Toast.LENGTH_LONG).show();
                                            textViewReparation.setText("error");

                                        }

                                        reparationIndex = reparationIndex+1;
                                    }
                                    progressBarReparation.setVisibility(View.GONE);
                                    textViewReparation.setText("done");
                                }

                            }
                        });

                    }catch (Exception e){

                        Log.d(LoginActivity.TAG,e.getMessage());
                        Toast.makeText(getApplicationContext(),"error syncing service centers",Toast.LENGTH_LONG).show();
                        textViewServiceCenter.setText("error");

                        break;
                    }
                    servicecenterindex++;
                }

            }
        }else {
            while (carindex < carArrayList.size()){
                final Car car = carArrayList.get(carindex);

                try{
                    dataRepository.uploadCar(car, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            databaseHelper.syncCar(car.getId());
                            if (carindex == carArrayList.size()){
                                progressBarCar.setVisibility(View.GONE);
                                textViewCar.setText("done");
                                if (serviceCenterArrayList.size()==0){

                                    while(verificationindex < verificationArrayList.size()){

                                        Verification verification = new Verification();
                                        verification = verificationArrayList.get(verificationindex);
                                        final VerificationToUpload verificationToUpload = new VerificationToUpload();
                                        Changes changes = new Changes();
                                        Service_center service_center = new Service_center();
                                        States states = new States();
                                        Engine_oil engine_oil = new Engine_oil();
                                        Car car = new Car();

                                        changes = databaseHelper.getChangeSelected(verification.getId());
                                        states = databaseHelper.getStatesSelected(verification.getId());
                                        service_center = databaseHelper.getServiceCenterSelected(verification.getId());
                                        car = databaseHelper.getCarSelected(verification.getId());

                                        verificationToUpload.setId(verification.getId().toString());
                                        verificationToUpload.setDate(verification.getDate());
                                        verificationToUpload.setOdometer(verification.getOdometer());
                                        verificationToUpload.setNote(verification.getNote());
                                        verificationToUpload.setCar(car);
                                        verificationToUpload.setChanges(changes);
                                        verificationToUpload.setService_center(service_center);
                                        verificationToUpload.setStates(states);

                                        if(databaseHelper.therIsChangeOilInThisVerf(verification.getId().toString())){
                                            engine_oil = databaseHelper.getEngineOilSelected(verification.getId());
                                            verificationToUpload.setEngine_oil(engine_oil);

                                        }else {
                                            engine_oil = new Engine_oil();
                                            engine_oil.setId(UUID.randomUUID().toString());
                                            engine_oil.setNextOdometer(0);
                                            engine_oil.setType("empty");
                                            verificationToUpload.setEngine_oil(engine_oil);
                                        }

                                        final Engine_oil finalEngine_oil = engine_oil;
                                        final Changes finalChanges = changes;
                                        final States finalStates = states;
                                        final Verification finalVerification = verification;

                                        try{
                                            dataRepository.uploadEngineOil(finalEngine_oil, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {

                                                    dataRepository.uploadChange(finalChanges, new VolleyCallBack() {
                                                        @Override
                                                        public void onSuccess() {

                                                            dataRepository.uploadStatus(finalStates, new VolleyCallBack() {
                                                                @Override
                                                                public void onSuccess() {

                                                                    dataRepository.uploadVerification(verificationToUpload, new VolleyCallBack() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            databaseHelper.syncVerification(finalVerification.getId());
                                                                            progressBarVerification.setVisibility(View.GONE);
                                                                            textViewVerification.setText("done");
                                                                                 }
                                                                    });

                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            Toast.makeText(getApplicationContext(),"error syncing verifications",Toast.LENGTH_LONG).show();
                                            textViewVerification.setText("error");

                                        }

                                        verificationindex = verificationindex+1;
                                    }
                                    progressBarVerification.setVisibility(View.GONE);
                                    textViewVerification.setText("done");

                                    while(reparationIndex < repareArrayList.size()){

                                        Repare repare = new Repare();
                                        repare = repareArrayList.get(reparationIndex);
                                        final ReparationToUpload reparationToUpload = new ReparationToUpload();
                                        Service_center service_center = new Service_center();
                                        Car car = new Car();

                                        service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                                        car = databaseHelper.getCarSelectedFromReparation(repare.getId());

                                        reparationToUpload.setId(repare.getId().toString());
                                        reparationToUpload.setDate(repare.getDate());
                                        reparationToUpload.setOdometer(repare.getOdometer());
                                        reparationToUpload.setNote(repare.getNote());
                                        reparationToUpload.setCar(car);
                                        reparationToUpload.setWhatRepared(repare.getWhatRepared());
                                        reparationToUpload.setService_center(service_center);

                                        final Repare finalRepare = repare;

                                        try{
                                            dataRepository.uploadReparation(reparationToUpload, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    databaseHelper.syncReparation(finalRepare.getId());
                                                    progressBarReparation.setVisibility(View.GONE);
                                                    textViewReparation.setText("done");
                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            Toast.makeText(getApplicationContext(),"error syncing reparation",Toast.LENGTH_LONG).show();
                                            textViewReparation.setText("error");

                                        }

                                        reparationIndex = reparationIndex+1;
                                    }
                                    progressBarReparation.setVisibility(View.GONE);
                                    textViewReparation.setText("done");

                                }else {
                                    while (servicecenterindex < serviceCenterArrayList.size()){
                                        final Service_center service_center = serviceCenterArrayList.get(servicecenterindex);
                                        try {
                                            dataRepository.uploadServiceCenter(service_center, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    databaseHelper.syncServiceCenter(service_center.getId());

                                                    if (servicecenterindex == serviceCenterArrayList.size()){
                                                        progressBarServiceCenter.setVisibility(View.GONE);
                                                        textViewServiceCenter.setText("done");

                                                        while(verificationindex < verificationArrayList.size()){

                                                            Verification verification = new Verification();
                                                            verification = verificationArrayList.get(verificationindex);
                                                            final VerificationToUpload verificationToUpload = new VerificationToUpload();
                                                            Changes changes = new Changes();
                                                            Service_center service_center = new Service_center();
                                                            States states = new States();
                                                            Engine_oil engine_oil = new Engine_oil();
                                                            Car car = new Car();

                                                            changes = databaseHelper.getChangeSelected(verification.getId());
                                                            states = databaseHelper.getStatesSelected(verification.getId());
                                                            service_center = databaseHelper.getServiceCenterSelected(verification.getId());
                                                            car = databaseHelper.getCarSelected(verification.getId());

                                                            verificationToUpload.setId(verification.getId().toString());
                                                            verificationToUpload.setDate(verification.getDate());
                                                            verificationToUpload.setOdometer(verification.getOdometer());
                                                            verificationToUpload.setNote(verification.getNote());
                                                            verificationToUpload.setCar(car);
                                                            verificationToUpload.setChanges(changes);
                                                            verificationToUpload.setService_center(service_center);
                                                            verificationToUpload.setStates(states);

                                                            if(databaseHelper.therIsChangeOilInThisVerf(verification.getId().toString())){
                                                                engine_oil = databaseHelper.getEngineOilSelected(verification.getId());
                                                                verificationToUpload.setEngine_oil(engine_oil);

                                                            }else {
                                                                engine_oil = new Engine_oil();
                                                                engine_oil.setId(UUID.randomUUID().toString());
                                                                engine_oil.setNextOdometer(0);
                                                                engine_oil.setType("empty");
                                                                verificationToUpload.setEngine_oil(engine_oil);
                                                            }

                                                            final Engine_oil finalEngine_oil = engine_oil;
                                                            final Changes finalChanges = changes;
                                                            final States finalStates = states;
                                                            final Verification finalVerification = verification;

                                                            try{
                                                                dataRepository.uploadEngineOil(finalEngine_oil, new VolleyCallBack() {
                                                                    @Override
                                                                    public void onSuccess() {

                                                                        dataRepository.uploadChange(finalChanges, new VolleyCallBack() {
                                                                            @Override
                                                                            public void onSuccess() {

                                                                                dataRepository.uploadStatus(finalStates, new VolleyCallBack() {
                                                                                    @Override
                                                                                    public void onSuccess() {

                                                                                        dataRepository.uploadVerification(verificationToUpload, new VolleyCallBack() {
                                                                                            @Override
                                                                                            public void onSuccess() {
                                                                                                databaseHelper.syncVerification(finalVerification.getId());
                                                                                                progressBarVerification.setVisibility(View.GONE);
                                                                                                textViewVerification.setText("done");
                                                                                                       }
                                                                                        });

                                                                                    }
                                                                                });
                                                                            }
                                                                        });

                                                                    }
                                                                });

                                                            }catch (Exception e){

                                                                Log.d(LoginActivity.TAG,e.getMessage());
                                                                Toast.makeText(getApplicationContext(),"error syncing verifications",Toast.LENGTH_LONG).show();
                                                                textViewVerification.setText("error");

                                                            }

                                                            verificationindex = verificationindex+1;
                                                        }
                                                        progressBarVerification.setVisibility(View.GONE);
                                                        textViewVerification.setText("done");

                                                        while(reparationIndex < repareArrayList.size()){

                                                            Repare repare = new Repare();
                                                            repare = repareArrayList.get(reparationIndex);
                                                            final ReparationToUpload reparationToUpload = new ReparationToUpload();
                                                            Service_center service_center = new Service_center();
                                                            Car car = new Car();

                                                            service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                                                            car = databaseHelper.getCarSelectedFromReparation(repare.getId());

                                                            reparationToUpload.setId(repare.getId().toString());
                                                            reparationToUpload.setDate(repare.getDate());
                                                            reparationToUpload.setOdometer(repare.getOdometer());
                                                            reparationToUpload.setNote(repare.getNote());
                                                            reparationToUpload.setCar(car);
                                                            reparationToUpload.setWhatRepared(repare.getWhatRepared());
                                                            reparationToUpload.setService_center(service_center);

                                                            final Repare finalRepare = repare;

                                                            try{
                                                                dataRepository.uploadReparation(reparationToUpload, new VolleyCallBack() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        databaseHelper.syncReparation(finalRepare.getId());
                                                                        progressBarReparation.setVisibility(View.GONE);
                                                                        textViewReparation.setText("done");
                                                                    }
                                                                });

                                                            }catch (Exception e){

                                                                Log.d(LoginActivity.TAG,e.getMessage());
                                                                Toast.makeText(getApplicationContext(),"error syncing reparation",Toast.LENGTH_LONG).show();
                                                                textViewReparation.setText("error");

                                                            }

                                                            reparationIndex = reparationIndex+1;
                                                        }
                                                        progressBarReparation.setVisibility(View.GONE);
                                                        textViewReparation.setText("done");
                                                    }

                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            Toast.makeText(getApplicationContext(),"error syncing service centers",Toast.LENGTH_LONG).show();
                                            textViewServiceCenter.setText("error");

                                            break;
                                        }
                                        servicecenterindex++;
                                    }
                                }

                            }
                        }
                    });

                    textViewDataSync.setText("synced");
                    //Toast.makeText(getApplicationContext(),String.valueOf(car.getModel().toString()),Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    Log.d(LoginActivity.TAG,e.getMessage());
                    textViewCar.setText("error");
                    break;

                }
                carindex++;
            }

        }

        if (!(carArrayList_update.size()==0)){
            int index=0;
            while (index < carArrayList_update.size()) {
                final Car car = carArrayList_update.get(index);
                dataRepository.updateCar(car, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        databaseHelper.syncCar(car.getId());
                    }
                });
                index++;
            }

        }

        if (!(serviceCenterArrayList_update.size()==0)){
            int index=0;
            while (index < serviceCenterArrayList_update.size()) {
                final Service_center service_center = serviceCenterArrayList_update.get(index);
                dataRepository.updateServiceCenter(service_center, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        databaseHelper.syncServiceCenter(service_center.getId());

                    }
                });
                index++;
            }
        }

        if (!(verificationArrayList_update.size()==0)){
            int index=0;
            while (index < verificationArrayList_update.size()) {
                final Verification verification = verificationArrayList_update.get(index);
                final VerificationToUpload verificationToUpload = new VerificationToUpload();
                Changes changes = new Changes();
                Service_center service_center = new Service_center();
                States states = new States();
                Engine_oil engine_oil = new Engine_oil();
                Car car = new Car();

                changes = databaseHelper.getChangeSelected(verification.getId());
                states = databaseHelper.getStatesSelected(verification.getId());
                service_center = databaseHelper.getServiceCenterSelected(verification.getId());
                car = databaseHelper.getCarSelected(verification.getId());

                verificationToUpload.setId(verification.getId().toString());
                verificationToUpload.setDate(verification.getDate());
                verificationToUpload.setOdometer(verification.getOdometer());
                verificationToUpload.setNote(verification.getNote());
                verificationToUpload.setCar(car);
                verificationToUpload.setChanges(changes);
                verificationToUpload.setService_center(service_center);
                verificationToUpload.setStates(states);

                if(databaseHelper.therIsChangeOilInThisVerf(verification.getId().toString())){
                    engine_oil = databaseHelper.getEngineOilSelected(verification.getId());
                    verificationToUpload.setEngine_oil(engine_oil);

                }else {
                    engine_oil = new Engine_oil();
                    engine_oil.setId(UUID.randomUUID().toString());
                    engine_oil.setNextOdometer(0);
                    engine_oil.setType("empty");
                    verificationToUpload.setEngine_oil(engine_oil);
                }

                final Engine_oil finalEngine_oil = engine_oil;
                final Changes finalChanges = changes;
                final States finalStates = states;
                final Verification finalVerification = verification;


                    dataRepository.updateEngineOil(finalEngine_oil, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {

                            dataRepository.updateChange(finalChanges, new VolleyCallBack() {
                                @Override
                                public void onSuccess() {

                                    dataRepository.updateStatus(finalStates, new VolleyCallBack() {
                                        @Override
                                        public void onSuccess() {

                                            dataRepository.updateVerification(verificationToUpload, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    databaseHelper.syncVerification(finalVerification.getId());
                                                }
                                            });

                                        }
                                    });
                                }
                            });

                        }
                    });
                index++;
            }

        }

        if (!(repareArrayList_update.size()==0)){

            int index=0;
            while (index < repareArrayList_update.size()) {
                final Repare repare = repareArrayList_update.get(index);

                final ReparationToUpload reparationToUpload = new ReparationToUpload();
                Service_center service_center = new Service_center();
                Car car = new Car();

                service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                car = databaseHelper.getCarSelectedFromReparation(repare.getId());

                reparationToUpload.setId(repare.getId().toString());
                reparationToUpload.setDate(repare.getDate());
                reparationToUpload.setOdometer(repare.getOdometer());
                reparationToUpload.setNote(repare.getNote());
                reparationToUpload.setCar(car);
                reparationToUpload.setWhatRepared(repare.getWhatRepared());
                reparationToUpload.setService_center(service_center);

                final Repare finalRepare = repare;

                try{
                    dataRepository.uploadReparation(reparationToUpload, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            databaseHelper.syncReparation(finalRepare.getId());
                        }
                    });

                }catch (Exception e){

                    Log.d(LoginActivity.TAG,e.getMessage());
                    Toast.makeText(getApplicationContext(),"error syncing reparation",Toast.LENGTH_LONG).show();

                }


            }
            index++;

        }

    }


    public void checkIfSynced(){
        if (carArrayList.size()>0 || verificationArrayList.size()>0 || serviceCenterArrayList.size()>0 || repareArrayList.size()>0 ||carArrayList_update.size()>0 || verificationArrayList_update.size()>0 || serviceCenterArrayList_update.size()>0 || repareArrayList_update.size()>0){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBarDataSync.setVisibility(View.GONE);
                    linearLayoutData.setVisibility(View.VISIBLE);
                    textViewDataSync.setText("not synced");
                    sync.setClickable(true);
                    Toast.makeText(getApplicationContext(),String.valueOf(carArrayList.size())+"  "+String.valueOf(serviceCenterArrayList.size())+"  "+String.valueOf(verificationArrayList.size())+"   "+String.valueOf(repareArrayList.size())+"  "+String.valueOf(carArrayList_update.size())+"  "+String.valueOf(serviceCenterArrayList_update.size())+"  "+String.valueOf(verificationArrayList_update.size())+"   "+String.valueOf(repareArrayList_update.size()),Toast.LENGTH_LONG).show();

                }
            },4000);

        }else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBarDataSync.setVisibility(View.GONE);
                    linearLayoutData.setVisibility(View.VISIBLE);
                    textViewDataSync.setText("synced");
                    sync.setClickable(false);
                }
            },4000);

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
