package co.example.hp.myapplication.work_manager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import co.example.hp.myapplication.LoginActivity;
import co.example.hp.myapplication.SplashActivity;
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

public class SyncWorker extends Worker {
    private DatabaseHelper databaseHelper;
    private int idUser,carindex,verificationindex,servicecenterindex,reparationIndex;
    private ArrayList<Car> carArrayList = new ArrayList<Car>();
    private ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();
    private ArrayList<Service_center> serviceCenterArrayList = new ArrayList<Service_center>();
    private ArrayList<Repare> repareArrayList = new ArrayList<Repare>();

    private ArrayList<Car> carArrayList_update = new ArrayList<Car>();
    private ArrayList<Verification> verificationArrayList_update = new ArrayList<Verification>();
    private ArrayList<Service_center> serviceCenterArrayList_update = new ArrayList<Service_center>();
    private ArrayList<Repare> repareArrayList_update = new ArrayList<Repare>();
    private DataRepository dataRepository;
    private boolean result = true;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        databaseHelper = new DatabaseHelper(getApplicationContext());

        dataRepository = new DataRepository(getApplicationContext(), SplashActivity.sharedPreferences.getString("authtoken","empty"));


        syncData();

        if (result){
            return Result.success();

        }else {
            return Result.failure();
        }

    }


    public boolean syncData(){

        Log.d("///////////////////////////////////////////sync worker start working","running...");


        idUser = databaseHelper.getUserLogedInId();

        carArrayList = databaseHelper.getNotSyncedCars(idUser);
        carArrayList_update = databaseHelper.getNotUpdatedCars(idUser);

        verificationArrayList = databaseHelper.getAllNotSyncedVerifications(idUser);
        verificationArrayList_update = databaseHelper.getAllNotUpdatedVerifications(idUser);

        serviceCenterArrayList = databaseHelper.getAllNotSyncedServiceCenters(idUser);
        serviceCenterArrayList_update = databaseHelper.getAllNotUpdatedServiceCenters(idUser);

        repareArrayList = databaseHelper.getAllNotSyncedReparation(idUser);
        repareArrayList_update = databaseHelper.getAllNotUpdatedReparation(idUser);

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

                        result = false;

                    }

                    verificationindex = verificationindex+1;
                }

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
                            }
                        });

                    }catch (Exception e){

                        Log.d(LoginActivity.TAG,e.getMessage());
                        result = false;

                    }

                    reparationIndex = reparationIndex+1;
                }

            }else {
                while (servicecenterindex < serviceCenterArrayList.size()){
                    final Service_center service_center = serviceCenterArrayList.get(servicecenterindex);
                    try {
                        dataRepository.uploadServiceCenter(service_center, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                databaseHelper.syncServiceCenter(service_center.getId());

                                if (servicecenterindex == serviceCenterArrayList.size()){

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
                                            result = false;

                                        }

                                        verificationindex = verificationindex+1;
                                    }

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
                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            result = false;

                                        }

                                        reparationIndex = reparationIndex+1;
                                    }
                                }

                            }
                        });

                    }catch (Exception e){

                        Log.d(LoginActivity.TAG,e.getMessage());

                        result = false;
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
                                                                        }
                                                                    });

                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }catch (Exception e){

                                            result = false;
                                            Log.d(LoginActivity.TAG,e.getMessage());

                                        }

                                        verificationindex = verificationindex+1;
                                    }

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
                                                }
                                            });

                                        }catch (Exception e){
                                            result = false;
                                            Log.d(LoginActivity.TAG,e.getMessage());
                                        }

                                        reparationIndex = reparationIndex+1;
                                    }
                                }else {
                                    while (servicecenterindex < serviceCenterArrayList.size()){
                                        final Service_center service_center = serviceCenterArrayList.get(servicecenterindex);
                                        try {
                                            dataRepository.uploadServiceCenter(service_center, new VolleyCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    databaseHelper.syncServiceCenter(service_center.getId());

                                                    if (servicecenterindex == serviceCenterArrayList.size()){

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
                                                                                            }
                                                                                        });

                                                                                    }
                                                                                });
                                                                            }
                                                                        });

                                                                    }
                                                                });

                                                            }catch (Exception e){
                                                                result = false;
                                                                Log.d(LoginActivity.TAG,e.getMessage());

                                                            }

                                                            verificationindex = verificationindex+1;
                                                        }

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
                                                                    }
                                                                });

                                                            }catch (Exception e){
                                                                result = false;
                                                                Log.d(LoginActivity.TAG,e.getMessage());
                                                            }

                                                            reparationIndex = reparationIndex+1;
                                                        }
                                                    }

                                                }
                                            });

                                        }catch (Exception e){

                                            Log.d(LoginActivity.TAG,e.getMessage());
                                            result = false;
                                            break;
                                        }
                                        servicecenterindex++;
                                    }
                                }
                            }
                        }
                    });

                }catch (Exception e){
                    result = false;
                    Log.d(LoginActivity.TAG,e.getMessage());
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

                }


            }

        }




        Log.d("//////////////////////////////////////////////sync worker start working","...done");
        return result;

    }
}
