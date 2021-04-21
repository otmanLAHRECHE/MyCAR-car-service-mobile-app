package co.example.hp.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.FilterReparationClass;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.User_Info;
import co.example.hp.myapplication.classes.Verification;

import static co.example.hp.myapplication.LoginActivity.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "MyCar";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_USER = "user";
    private static final String TABLE_CAR = "cars";
    private static final String TABLE_VERIFICATION = "verification";
    private static final String TABLE_REPARE = "reparation";
    private static final String TABLE_SERVICE_CENTER = "servicecenter";
    private static final String TABLE_ENGINE_OIL = "engineoil";
    private static final String TABLE_CHANGES = "changes";
    private static final String TABLE_STATES = "states";
    private static final String KEY_ID = "id";

    private static final String CREATE_TABLE_users = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + "firstname TEXT,lastname TEXT,email TEXT,state TEXT" + ");";
    private static final String CREATE_TABLE_cars = "CREATE TABLE "
            + TABLE_CAR + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "company TEXT,model TEXT,serial TEXT,year INTEGER,car_type TEXT,registration INTEGER,id_user INTEGER,synced TEXT," +
            "FOREIGN KEY (id_user) REFERENCES "+TABLE_USER+"(id)" + ");";
    private static final String CREATE_TABLE_verification = "CREATE TABLE "
            + TABLE_VERIFICATION + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "date INTEGER,odometer REAL,note TEXT,id_car INTEGER,id_service_center INTEGER,id_engine_oil INTEGER,id_states INTEGER,id_changes INTEGER,synced TEXT,"
            +"FOREIGN KEY (id_car) REFERENCES "+TABLE_CAR+"(id),"
            +"FOREIGN KEY (id_service_center) REFERENCES "+TABLE_SERVICE_CENTER+"(id),"
            +"FOREIGN KEY (id_engine_oil) REFERENCES "+TABLE_ENGINE_OIL+"(id),"
            +"FOREIGN KEY (id_states) REFERENCES "+TABLE_STATES+"(id)," +
            "FOREIGN KEY (id_changes) REFERENCES "+TABLE_CHANGES+"(id)"
            + ");";
    private static final String CREATE_TABLE_repares = "CREATE TABLE "
            + TABLE_REPARE + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "date INTEGER,odometer REAL,note TEXT,what_repared TEXT,id_car INTEGER,id_service_center INTEGER,synced TEXT," +
            "FOREIGN KEY (id_car) REFERENCES "+TABLE_CAR+"(id)," +
            "FOREIGN KEY (id_service_center) REFERENCES "+TABLE_SERVICE_CENTER+"(id)" + ");";
    private static final String CREATE_TABLE_service_center = "CREATE TABLE "
            + TABLE_SERVICE_CENTER + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "name TEXT,phonenbr INTEGER,location TEXT,id_user INTEGER,synced TEXT," +
            "FOREIGN KEY (id_user) REFERENCES "+TABLE_USER+"(id)" + ");";
    private static final String CREATE_TABLE_engine_oil = "CREATE TABLE "
            + TABLE_ENGINE_OIL + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "oiltype TEXT,nextodometer REAL" + ");";
    private static final String CREATE_TABLE_changes = "CREATE TABLE "
            + TABLE_CHANGES + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "oil_filter TEXT,air_filter TEXT,cabine_filter TEXT,brakes TEXT,trans_oil TEXT,light_replace TEXT,wheel_alignment TEXT,battery_replace TEXT,tires_change TEXT,fuel_filter_change TEXT,glass_change TEXT,mount_balance TEXT" + ");";
    private static final String CREATE_TABLE_states = "CREATE TABLE "
            + TABLE_STATES + "(" + KEY_ID
            + " TEXT PRIMARY KEY," + "engineState TEXT,lightsState TEXT,tiresState TEXT,airConditioningState TEXT" + ");";







    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_users);
        db.execSQL(CREATE_TABLE_cars);
        db.execSQL(CREATE_TABLE_service_center);
        db.execSQL(CREATE_TABLE_engine_oil);
        db.execSQL(CREATE_TABLE_changes);
        db.execSQL(CREATE_TABLE_states);
        db.execSQL(CREATE_TABLE_verification);
        db.execSQL(CREATE_TABLE_repares);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CAR + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_STATES + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_CHANGES + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ENGINE_OIL + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_SERVICE_CENTER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_REPARE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_VERIFICATION + "'");
        onCreate(db);
    }

/////////////////////////////////////////////////////////////// user //////////////////////////////////////////////////////////////////


    public void registerUser(String firstname,String lastname,String email,String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("email",email);
        values.put("state", state);
        db.insert(TABLE_USER,null, values);
        //Log.d(TAG, );
    }


    public boolean therIsUserIn(){
        boolean st = false;
        String state = "nn";


        String selectQuery = "SELECT  * FROM " + TABLE_USER+" WHERE ("+TABLE_USER+".state like "+"'%nn%'"+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.getCount() > 0) {

            st = true;

        }

        return st;


    }

    public User_Info getFirstUser(){

        User_Info user = new User_Info();

        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (c.moveToFirst()) {



                    user.setFirstname(c.getString(c.getColumnIndex("firstname")));
                    user.setEmail(c.getString(c.getColumnIndex("email")));




        }
        return user;
    }




    public User_Info getUserLogedIn(){

        User_Info user = new User_Info();
        String state = "nn";

        String selectQuery = "SELECT  firstname,lastname,email FROM " + TABLE_USER+" WHERE ("+TABLE_USER+".state like "+"'%nn%'"+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,  null);
        // looping through all rows and adding to list

        if (c.moveToFirst()) {


                  user.setFirstname(c.getString(c.getColumnIndex("firstname")));
                  user.setLastname(c.getString(c.getColumnIndex("lastname")));
                  user.setEmail(c.getString(c.getColumnIndex("email")));

        }
        return user;
    }

    public int getUserLogedInId(){

        int id = 0;
        String state = "nn";

        String selectQuery = "SELECT  id FROM " + TABLE_USER+" WHERE ("+TABLE_USER+".state like "+"'%nn%'"+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,  null);
        // looping through all rows and adding to list

        if (c.moveToFirst()) {


                    id = c.getInt(c.getColumnIndex("id"));

        }
        return id;
    }





    public int getUserToLogin(String email){
        int id =0;

        //String selectQuery = "SELECT  id,email FROM " + TABLE_USER+" WHERE ("+TABLE_USER+".email like "+email+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});

        if (c.moveToFirst()) {


                    id = c.getInt(c.getColumnIndex("id"));

        }
        return id;


    }




    public void updateUserState(int id,String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("state", state);

        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean userExistLocaly(String email){
        boolean finder = false;

        //String selectQuery = "SELECT  id,email FROM " + TABLE_USER+" WHERE ("+TABLE_USER+".email like "+email+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});

        if (c.getCount()>0) {


            finder =  true;

        }else {finder = false;}

        return finder;


    }

    public void updateUser(int id,String fn,String ln){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("firstname", fn);
        values.put("lastname", ln);

        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void updateUserEmail(int id,String fn,String ln,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("firstname", fn);
        values.put("lastname", ln);
        values.put("email",email);

        db.update(TABLE_USER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }







////////////////////////////////////////////////////////////////  Car  //////////////////////////////////////////////////////////////////////

    //,year INTEGER,car_type TEXT,
    public void addCar(String id,String company, String model,int year,String car_type, String serial, int registration,int idUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("company", company);
        values.put("model", model);
        values.put("serial", serial);
        values.put("registration", registration);
        values.put("year",year);
        values.put("car_type",car_type);
        values.put("id_user", idUser);
        values.put("synced", "notSynced");
        db.insert(TABLE_CAR, null, values);
    }

    public Car getSelectedCar(String id_car) {
        Car firstcar = new Car();

        String selectQuery = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(id_car)});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {


                Car car = new Car();
                car.setId(c.getString(c.getColumnIndex("id")));
                car.setCompany(c.getString(c.getColumnIndex("company")));
                car.setModel(c.getString(c.getColumnIndex("model")));
                car.setSerial(c.getString(c.getColumnIndex("serial")));
                car.setRegistration(c.getInt(c.getColumnIndex("registration")));

                firstcar = (Car)car;




        }
        return firstcar;

    }

    public Boolean isCarExisted(String id_car) {

        String selectQuery = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(id_car)});
        // looping through all rows and adding to list
        if (c.getCount()==0) {

            return false;
        }else {

            return true;
        }

    }

    public ArrayList<Car> getAllCars(int id_user) {
        ArrayList<Car> carArrayList = new ArrayList<Car>();

        String selectQuery = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                    Car car = new Car();
                    car.setId(c.getString(c.getColumnIndex("id")));
                    car.setCompany(c.getString(c.getColumnIndex("company")));
                    car.setModel(c.getString(c.getColumnIndex("model")));
                    car.setSerial(c.getString(c.getColumnIndex("serial")));
                    car.setRegistration(c.getInt(c.getColumnIndex("registration")));
                    car.setYear(c.getInt(c.getColumnIndex("year")));
                    car.setCar_type(c.getString(c.getColumnIndex("car_type")));


                    carArrayList.add(car);



            } while (c.moveToNext());
        }
        return carArrayList;



    }


    public void deleteCar(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CAR, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }

    public boolean thereIsNoCar(int id_user){
        boolean car = Boolean.TRUE;

        String selectQuery = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list


        if (c.getCount() == 0) {
            car = Boolean.TRUE;

        }else {
            car = Boolean.FALSE;
        }
        return car;

    }

    public String stateSyncCar(String id_car){

        String state_sync="";

        String selectQuery = "SELECT  synced FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToFirst()){
            state_sync = c.getString(c.getColumnIndex("synced"));
        }
        return state_sync;

    }

    public void updateCar(String id,String company, String model,int year,String car_type, String serial, int registration){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("company", company);
        values.put("model", model);
        values.put("serial", serial);
        values.put("registration", registration);
        values.put("year",year);
        values.put("car_type",car_type);

        if (stateSyncCar(id).equals("notSynced")){

        }else{
            values.put("synced","not_updated");
        }

        db.update(TABLE_CAR, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public void syncCar(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("synced", "Synced");
        db.update(TABLE_CAR, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public ArrayList<Car> getNotSyncedCars(int id_user){

        ArrayList<Car> carArrayList = new ArrayList<Car>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_CAR+".synced = ?)", new String[]{"notSynced"});
        if (c.moveToFirst()) {
            do {

                Car car = new Car();
                car.setId(c.getString(c.getColumnIndex("id")));
                car.setCompany(c.getString(c.getColumnIndex("company")));
                car.setModel(c.getString(c.getColumnIndex("model")));
                car.setSerial(c.getString(c.getColumnIndex("serial")));
                car.setRegistration(c.getInt(c.getColumnIndex("registration")));
                car.setYear(c.getInt(c.getColumnIndex("year")));
                car.setCar_type(c.getString(c.getColumnIndex("car_type")));

                carArrayList.add(car);



            } while (c.moveToNext());
        }


        return carArrayList;

    }

    public ArrayList<Car> getNotUpdatedCars(int id_user){

        ArrayList<Car> carArrayList = new ArrayList<Car>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_CAR+".synced = ?)", new String[]{"not_updated"});
        if (c.moveToFirst()) {
            do {

                Car car = new Car();
                car.setId(c.getString(c.getColumnIndex("id")));
                car.setCompany(c.getString(c.getColumnIndex("company")));
                car.setModel(c.getString(c.getColumnIndex("model")));
                car.setSerial(c.getString(c.getColumnIndex("serial")));
                car.setRegistration(c.getInt(c.getColumnIndex("registration")));
                car.setYear(c.getInt(c.getColumnIndex("year")));
                car.setCar_type(c.getString(c.getColumnIndex("car_type")));

                carArrayList.add(car);



            } while (c.moveToNext());
        }


        return carArrayList;

    }


    public Car getCarSelected(String idverif) {
        Car carReturn = new Car();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT cars.id,cars.company,cars.model,cars.serial,cars.registration,cars.year,cars.car_type FROM " + TABLE_CAR+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_car like "+
                TABLE_CAR+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list

        Log.d("/////////////////////////////////////////car_id",String.valueOf(c.getCount()));
        if (c.moveToFirst()) {
            do {
                Car car = new Car();
                car.setId(c.getString(c.getColumnIndex("id")));
                car.setCompany(c.getString(c.getColumnIndex("company")));
                car.setModel(c.getString(c.getColumnIndex("model")));
                car.setSerial(c.getString(c.getColumnIndex("serial")));
                car.setRegistration(c.getInt(c.getColumnIndex("registration")));
                car.setYear(c.getInt(c.getColumnIndex("year")));
                car.setCar_type(c.getString(c.getColumnIndex("car_type")));

                carReturn = car;

            } while (c.moveToNext());
        }
        return carReturn;

    }

    public Car getCarSelectedFromReparation(String idrepare) {
        Car carReturn = new Car();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT cars.id,cars.company,cars.model,cars.serial,cars.registration,cars.year,cars.car_type FROM " + TABLE_CAR+" JOIN "+TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id = ? and "+TABLE_REPARE+".id_car like "+
                TABLE_CAR+".id" +
                ")", new String[]{idrepare});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Car car = new Car();
                car.setId(c.getString(c.getColumnIndex("id")));
                car.setCompany(c.getString(c.getColumnIndex("company")));
                car.setModel(c.getString(c.getColumnIndex("model")));
                car.setSerial(c.getString(c.getColumnIndex("serial")));
                car.setRegistration(c.getInt(c.getColumnIndex("registration")));
                car.setYear(c.getInt(c.getColumnIndex("year")));
                car.setCar_type(c.getString(c.getColumnIndex("car_type")));

                carReturn = car;

            } while (c.moveToNext());
        }
        return carReturn;

    }



/////////////////////////////////////////////////////////////////   service centers  ////////////////////////////////////////////////////////

    public void addServiceCenter(String id,String name, int phone,int id_user, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("phonenbr", phone);
        values.put("location", location);
        values.put("id_user", id_user);
        values.put("synced", "notSynced");
        db.insert(TABLE_SERVICE_CENTER, null, values);
    }


    public ArrayList<Service_center> getAllServiceCenters(int id_user) {
        ArrayList<Service_center> serviceCenterArrayList = new ArrayList<Service_center>();

        String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenterArrayList.add(sc);

            } while (c.moveToNext());
        }
        return serviceCenterArrayList;

    }




    public void deleteServiceCenter(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_SERVICE_CENTER, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }

    public String stateSyncServiceCenter(String id_service){

        String state_sync="";

        String selectQuery = "SELECT  synced FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{id_service});
        // looping through all rows and adding to list

        if (c.moveToFirst()){
            state_sync = c.getString(c.getColumnIndex("synced"));
        }
        return state_sync;

    }
    public void updateServiceCenter(String id,String name, int phone, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("phonenbr", phone);
        values.put("location", location);
        if (stateSyncServiceCenter(id).equals("notSynced")){

        }else{
            values.put("synced","not_updated");
        }
        db.update(TABLE_SERVICE_CENTER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Service_center getServiceCenterSelected(String idverif) {
        Service_center serviceCenter = new Service_center();

        //String selectQuery = "SELECT servicecenter.id,servicecenter.name FROM " + TABLE_SERVICE_CENTER+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_service_center like "+
         //       TABLE_SERVICE_CENTER+".id" +
          //      ")";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT servicecenter.id,servicecenter.name,servicecenter.location,servicecenter.phonenbr FROM " + TABLE_SERVICE_CENTER+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_service_center like "+
                TABLE_SERVICE_CENTER+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenter = sc;

            } while (c.moveToNext());
        }
        return serviceCenter;

    }

    public Boolean isServiceCenterExist(String idservice) {

        //String selectQuery = "SELECT servicecenter.id,servicecenter.name FROM " + TABLE_SERVICE_CENTER+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_service_center like "+
        //       TABLE_SERVICE_CENTER+".id" +
        //      ")";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+ TABLE_SERVICE_CENTER+".id = ?)", new String[]{idservice});
        // looping through all rows and adding to list
        if (c.getCount()==0) {

            return false;
        }else {

            return true;
        }

    }

    public boolean thereIsNoSereviceCenter(int id_user){

        Boolean serviceCenter = Boolean.TRUE;

        String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.getCount()>0) {

            serviceCenter = Boolean.FALSE;

        }else {
            serviceCenter = Boolean.TRUE;
        }
        return serviceCenter;




    }






    public void syncServiceCenter(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("synced", "Synced");
        db.update(TABLE_SERVICE_CENTER, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public String stateOfSyncServiceCenters(String id_service_center) {
        String syncState ="notSynced";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id = ?)", new String[]{String.valueOf(id_service_center)});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {


                syncState = c.getString(c.getColumnIndex("synced"));




        }
        return syncState;

    }


    public ArrayList<Service_center> getAllNotSyncedServiceCenters(int id_user) {
        ArrayList<Service_center> serviceCenterArrayList = new ArrayList<Service_center>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id_user like "+id_user+" and "+TABLE_SERVICE_CENTER+".synced = ?)", new String[]{"notSynced"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenterArrayList.add(sc);

            } while (c.moveToNext());
        }
        return serviceCenterArrayList;

    }

    public ArrayList<Service_center> getAllNotUpdatedServiceCenters(int id_user) {
        ArrayList<Service_center> serviceCenterArrayList = new ArrayList<Service_center>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id_user like "+id_user+" and "+TABLE_SERVICE_CENTER+".synced = ?)", new String[]{"not_updated"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenterArrayList.add(sc);

            } while (c.moveToNext());
        }
        return serviceCenterArrayList;

    }


    public Service_center getServiceCenterSelectedFromReparation(String idrepare) {
        Service_center serviceCenter = new Service_center();

        //String selectQuery = "SELECT servicecenter.id,servicecenter.name FROM " + TABLE_SERVICE_CENTER+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_service_center like "+
        //       TABLE_SERVICE_CENTER+".id" +
        //      ")";
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT servicecenter.id,servicecenter.name,servicecenter.location,servicecenter.phonenbr FROM " + TABLE_SERVICE_CENTER+" JOIN "+TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id = ? and "+TABLE_REPARE+".id_service_center like "+
                TABLE_SERVICE_CENTER+".id" +
                ")", new String[]{idrepare});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenter = sc;

            } while (c.moveToNext());
        }
        return serviceCenter;

    }


    public Service_center getServiceCenterSelectedById(String idserv) {
        Service_center serviceCenter = new Service_center();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("select * from "+TABLE_USER +" where email = ?", new String[]{email});
        Cursor c = db.rawQuery("SELECT servicecenter.id,servicecenter.name,servicecenter.location,servicecenter.phonenbr FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id = ? )", new String[]{idserv});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Service_center sc = new Service_center();
                sc.setId(c.getString(c.getColumnIndex("id")));
                sc.setName(c.getString(c.getColumnIndex("name")));
                sc.setPhoneNumber(c.getInt(c.getColumnIndex("phonenbr")));
                sc.setLocation(c.getString(c.getColumnIndex("location")));

                serviceCenter = sc;

            } while (c.moveToNext());
        }
        return serviceCenter;

    }






/////////////////////////////////////////////////////////////////   verification  ////////////////////////////////////////////////////////


    public void addVerification(String id,long date, double odometer, String note,String id_car,String id_service_center,String id_engine_oil,String id_states,String id_changes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("odometer", odometer);
        values.put("note", note);
        values.put("id_car", id_car);
        values.put("id_service_center", id_service_center);
        if (id_engine_oil == "0"){

            values.put("id_engine_oil", (String) null);

        }else {
            values.put("id_engine_oil", id_engine_oil);
        }

        values.put("id_states", id_states);
        values.put("id_changes", id_changes);
        values.put("synced", "notSynced");
        db.insert(TABLE_VERIFICATION, null, values);
    }


    public ArrayList<Verification> getAllVerifications(String id_car) {
        ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Verification verf = new Verification();
                verf.setId(c.getString(c.getColumnIndex("id")));
                verf.setDate(c.getLong(c.getColumnIndex("date")));
                verf.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                verf.setNote(c.getString(c.getColumnIndex("note")));

                verificationArrayList.add(verf);

            } while (c.moveToNext());
        }
        return verificationArrayList;

    }


    public Verification getLastVerification(String id_car) {

        Verification verification = new Verification();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            verification.setId(c.getString(c.getColumnIndex("id")));
            verification.setDate(c.getLong(c.getColumnIndex("date")));
            verification.setOdometer(c.getLong(c.getColumnIndex("odometer")));
            verification.setNote(c.getString(c.getColumnIndex("note")));



        }else {

        }
        return verification;

    }

    public Verification getLastVerificationAllTimes(int id_user) {

        Verification verification = new Verification();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  verification.id,verification.date,verification.odometer,verification.note FROM " + TABLE_VERIFICATION+" join "+TABLE_CAR+" WHERE ("+TABLE_VERIFICATION+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+") order by verification.date ASC", null);
        // looping through all rows and adding to list
        if (c.moveToLast()) {

            verification.setId(c.getString(c.getColumnIndex("id")));
            verification.setDate(c.getLong(c.getColumnIndex("date")));
            verification.setOdometer(c.getLong(c.getColumnIndex("odometer")));
            verification.setNote(c.getString(c.getColumnIndex("note")));

        }else {
            verification = null;

        }
        return verification;

    }

    public Verification getOldestVerificationWithOilChange(int id_user) {


        Verification verification = new Verification();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  verification.id,verification.date,verification.odometer,verification.note,verification.id_engine_oil FROM " + TABLE_VERIFICATION+" join "+TABLE_CAR+" WHERE ("+TABLE_VERIFICATION+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+")", null);
        // looping through all rows and adding to list
        Log.d("///////////////////////////////////////////////c.count",String.valueOf(c.getCount()));

        if (c.moveToFirst()) {

            Verification verf = new Verification();
            verf.setId(c.getString(c.getColumnIndex("id")));
            verf.setDate(c.getLong(c.getColumnIndex("date")));
            verf.setOdometer(c.getLong(c.getColumnIndex("odometer")));
            verf.setNote(c.getString(c.getColumnIndex("note")));

            if (therIsChangeOilInThisVerf(verf.getId())){
                verification =verf;
            }
            c.moveToNext();

            do {
                verf = new Verification();
                verf.setId(c.getString(c.getColumnIndex("id")));
                verf.setDate(c.getLong(c.getColumnIndex("date")));
                verf.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                verf.setNote(c.getString(c.getColumnIndex("note")));
                if (therIsChangeOilInThisVerf(verf.getId())){
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTimeInMillis(verification.getDate());
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeInMillis(verf.getDate());
                    if (calendar1.getTime().after(calendar2.getTime())){
                        verification = verf;
                    }
                }

            } while (c.moveToNext());
        }
        return verification;

    }

    public Boolean verificationIsEmpty(String id_car) {


           Boolean b = Boolean.TRUE;


        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            b = Boolean.FALSE;



        }else {

           b = Boolean.TRUE;

        }
        return b;

    }




    public void deleteverification(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_VERIFICATION, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }

    public String stateSyncVerification(String id_verif){

        String state_sync="";

        String selectQuery = "SELECT  synced FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{id_verif});
        // looping through all rows and adding to list

        if (c.moveToFirst()){
            state_sync = c.getString(c.getColumnIndex("synced"));
        }
        return state_sync;

    }

    public void updateverification(String id,long date, double odometer, String note,String id_car,String id_service_center,String id_engine_oil,String id_states,String id_changes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", date);
        values.put("odometer", odometer);
        values.put("note", note);
        values.put("id_car", id_car);
        values.put("id_service_center", id_service_center);
        values.put("id_engine_oil", id_engine_oil);
        values.put("id_states", id_states);
        values.put("id_changes", id_changes);
        if (stateSyncVerification(id).equals("notSynced")){

        }else{
            values.put("synced","not_updated");
        }

        db.update(TABLE_VERIFICATION, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public void syncVerification(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("synced", "Synced");
        db.update(TABLE_VERIFICATION, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public ArrayList<Verification> getAllNotSyncedVerifications(int id_user) {
        ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  verification.id,verification.date,verification.odometer,verification.note FROM " + TABLE_VERIFICATION+" join "+TABLE_CAR+" WHERE ("+TABLE_VERIFICATION+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_VERIFICATION+".synced = ?)", new String[]{"notSynced"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Verification verf = new Verification();
                verf.setId(c.getString(c.getColumnIndex("id")));
                verf.setDate(c.getLong(c.getColumnIndex("date")));
                verf.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                verf.setNote(c.getString(c.getColumnIndex("note")));

                verificationArrayList.add(verf);

            } while (c.moveToNext());
        }
        return verificationArrayList;

    }

    public ArrayList<Verification> getAllNotUpdatedVerifications(int id_user) {
        ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  verification.id,verification.date,verification.odometer,verification.note FROM " + TABLE_VERIFICATION+" join "+TABLE_CAR+" WHERE ("+TABLE_VERIFICATION+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_VERIFICATION+".synced = ?)", new String[]{"not_updated"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Verification verf = new Verification();
                verf.setId(c.getString(c.getColumnIndex("id")));
                verf.setDate(c.getLong(c.getColumnIndex("date")));
                verf.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                verf.setNote(c.getString(c.getColumnIndex("note")));

                verificationArrayList.add(verf);

            } while (c.moveToNext());
        }
        return verificationArrayList;

    }


    public Boolean isVerificationExist(String idverif) {


        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ?)", new String[]{idverif});
        // looping through all rows and adding to list
        if (c.getCount()==0) {
            return false;
        }else {
            return true;
        }

    }


//////////////////////////////////////////////////////////////////   Engene oil ////////////////////////////////////////////////////////////////


    public void addEngineOil(String id,String oiltype, double nextodometer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("oiltype", oiltype);
        values.put("nextodometer", nextodometer);

        db.insert(TABLE_ENGINE_OIL, null, values);
    }


    public Engine_oil getLastEngineOil() {

        Engine_oil engine_oil = new Engine_oil();

        String selectQuery = "SELECT  * FROM " + TABLE_ENGINE_OIL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToLast()) {


            engine_oil.setId(c.getString(c.getColumnIndex("id")));
            engine_oil.setType(c.getString(c.getColumnIndex("oiltype")));
            engine_oil.setNextOdometer(c.getLong(c.getColumnIndex("nextodometer")));



        }else {

        }
        return engine_oil;

    }

    public Boolean therIsNoChangeOil(String id_car) {


        Boolean b = Boolean.TRUE;


        //String selectQuery = "SELECT engineoil.id FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like " +id_car+" and "+TABLE_VERIFICATION+".id_engine_oil like "+
            //    TABLE_ENGINE_OIL+".id" +
            //    ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT engineoil.id FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? and "+TABLE_VERIFICATION+".id_engine_oil like "+
                TABLE_ENGINE_OIL+".id" +
                ")", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            b = Boolean.FALSE;



        }else {

            b = Boolean.TRUE;

        }
        return b;

    }

    public Engine_oil lastOilChange(String id_car) {


        Engine_oil engine_oil = new Engine_oil();


        //String selectQuery = "SELECT engineoil.id,engineoil.oiltype,engineoil.nextodometer FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like " +id_car+" and "+TABLE_VERIFICATION+".id_engine_oil like "+
          //      TABLE_ENGINE_OIL+".id" +
         //       ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT engineoil.id,engineoil.oiltype,engineoil.nextodometer FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car  = ? and "+TABLE_VERIFICATION+".id_engine_oil like "+
                TABLE_ENGINE_OIL+".id" +
                ")", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            engine_oil.setId(c.getString(c.getColumnIndex("id")));
            engine_oil.setType(c.getString(c.getColumnIndex("oiltype")));
            engine_oil.setNextOdometer(c.getLong(c.getColumnIndex("nextodometer")));



        }else {



        }
        return engine_oil;

    }

    public Engine_oil getEngineOilSelected(String idverif) {
        Engine_oil engine_oil = new Engine_oil();

        //String selectQuery = "SELECT engineoil.id,engineoil.oiltype,engineoil.nextodometer FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_engine_oil like "+
          //      TABLE_ENGINE_OIL+".id" +
            //    ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT engineoil.id,engineoil.oiltype,engineoil.nextodometer FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_engine_oil like "+
                TABLE_ENGINE_OIL+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                engine_oil.setId(c.getString(c.getColumnIndex("id")));
                engine_oil.setType(c.getString(c.getColumnIndex("oiltype")));
                engine_oil.setNextOdometer(c.getLong(c.getColumnIndex("nextodometer")));



            } while (c.moveToNext());
        }
        return engine_oil;

    }

    public Boolean therIsChangeOilInThisVerf(String idverif) {


        Boolean b = Boolean.FALSE;


        //String selectQuery = "SELECT engineoil.id FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_engine_oil like "+
          //      TABLE_ENGINE_OIL+".id" +
            //    ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT engineoil.id FROM " + TABLE_ENGINE_OIL+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_engine_oil like "+
                TABLE_ENGINE_OIL+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            b = Boolean.TRUE;



        }else {

            b = Boolean.FALSE;

        }
        return b;

    }

    public void deleteEngineOil(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ENGINE_OIL, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }


    public void updateEngineOil(String id,String oiltype, double nextodometer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("oiltype", oiltype);
        values.put("nextodometer", nextodometer);

        db.update(TABLE_ENGINE_OIL, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }




//////////////////////////////////////////////////////////////////   changes ////////////////////////////////////////////////////////////////



    public void addChanges(String id,String oil_filter, String air_filter,String cabine_filter, String brakes,String trans_oil,String light_replace,String wheel_alignment,String battery_replace,String tires_change,String fuel_filter_change,String glass_change,String mount_balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("oil_filter", oil_filter);
        values.put("air_filter", air_filter);
        values.put("cabine_filter", cabine_filter);
        values.put("brakes", brakes);
        values.put("trans_oil", trans_oil);
        values.put("light_replace", light_replace);
        values.put("wheel_alignment", wheel_alignment);
        values.put("battery_replace", battery_replace);
        values.put("tires_change", tires_change);
        values.put("fuel_filter_change", fuel_filter_change);
        values.put("glass_change", glass_change);
        values.put("mount_balance", mount_balance);

        db.insert(TABLE_CHANGES, null, values);
    }


    public Changes getLastChanges() {

        Changes changes= new Changes();

        String selectQuery = "SELECT  * FROM " + TABLE_CHANGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToLast()) {


            changes.setId(c.getString(c.getColumnIndex("id")));
            changes.setOil_filter(Boolean.valueOf(c.getString(c.getColumnIndex("oil_filter"))));
            changes.setAir_filter(Boolean.valueOf(c.getString(c.getColumnIndex("air_filter"))));
            changes.setBrakes(Boolean.valueOf(c.getString(c.getColumnIndex("brakes"))));
            changes.setCabine_filter(Boolean.valueOf(c.getString(c.getColumnIndex("cabine_filter"))));
            changes.setTransmission_oil(Boolean.valueOf(c.getString(c.getColumnIndex("trans_oil"))));
            changes.setLight_replace(Boolean.valueOf(c.getString(c.getColumnIndex("light_replace"))));
            changes.setWheel_alignment(Boolean.valueOf(c.getString(c.getColumnIndex("wheel_alignment"))));
            changes.setBattery_replace(Boolean.valueOf(c.getString(c.getColumnIndex("battery_replace"))));
            changes.setTires_change(Boolean.valueOf(c.getString(c.getColumnIndex("tires_change"))));
            changes.setFuel_filter_change(Boolean.valueOf(c.getString(c.getColumnIndex("fuel_filter_change"))));
            changes.setMount_balance(Boolean.valueOf(c.getString(c.getColumnIndex("mount_balance"))));
            changes.setGlass_change(Boolean.valueOf(c.getString(c.getColumnIndex("glass_change"))));



        }else {

        }
        return changes;

    }

    public Changes getChangeSelected(String idverif) {
        Changes changes = new Changes();
        //oil_filter TEXT,air_filter TEXT,cabine_filter TEXT,brakes TEXT,trans_oil

        //String selectQuery = "SELECT changes.id,changes.oil_filter,changes.air_filter,changes.cabine_filter,changes.brakes,changes.trans_oil FROM " + TABLE_CHANGES+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id like " +idverif+" and "+TABLE_VERIFICATION+".id_changes like "+
          //      TABLE_CHANGES+".id" +
            //    ")";
        SQLiteDatabase db = this.getReadableDatabase();
                                                                                                                        //light_replace,wheel_alignment,battery_replace,tires_change,fuel_filter_change,glass_change,mount_balance
        Cursor c = db.rawQuery("SELECT changes.id,changes.oil_filter,changes.air_filter,changes.cabine_filter,changes.brakes,changes.trans_oil,changes.light_replace,changes.wheel_alignment,changes.battery_replace,changes.tires_change,changes.fuel_filter_change,changes.glass_change,changes.mount_balance FROM " + TABLE_CHANGES+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_changes like "+
                TABLE_CHANGES+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                changes.setId(c.getString(c.getColumnIndex("id")));
                changes.setOil_filter(Boolean.valueOf(c.getString(c.getColumnIndex("oil_filter"))));
                changes.setAir_filter(Boolean.valueOf(c.getString(c.getColumnIndex("air_filter"))));
                changes.setBrakes(Boolean.valueOf(c.getString(c.getColumnIndex("brakes"))));
                changes.setCabine_filter(Boolean.valueOf(c.getString(c.getColumnIndex("cabine_filter"))));
                changes.setTransmission_oil(Boolean.valueOf(c.getString(c.getColumnIndex("trans_oil"))));
                changes.setLight_replace(Boolean.valueOf(c.getString(c.getColumnIndex("light_replace"))));
                changes.setWheel_alignment(Boolean.valueOf(c.getString(c.getColumnIndex("wheel_alignment"))));
                changes.setBattery_replace(Boolean.valueOf(c.getString(c.getColumnIndex("battery_replace"))));
                changes.setTires_change(Boolean.valueOf(c.getString(c.getColumnIndex("tires_change"))));
                changes.setFuel_filter_change(Boolean.valueOf(c.getString(c.getColumnIndex("fuel_filter_change"))));
                changes.setMount_balance(Boolean.valueOf(c.getString(c.getColumnIndex("mount_balance"))));
                changes.setGlass_change(Boolean.valueOf(c.getString(c.getColumnIndex("glass_change"))));



            } while (c.moveToNext());
        }
        return changes;

    }

    public void deleteChanges(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CHANGES, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }
    public void updateChanges(String id,String oil_filter, String air_filter,String cabine_filter, String brakes,String trans_oil,String light_replace,String wheel_alignment,String battery_replace,String tires_change,String fuel_filter_change,String glass_change,String mount_balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("oil_filter", oil_filter);
        values.put("air_filter", air_filter);
        values.put("cabine_filter", cabine_filter);
        values.put("brakes", brakes);
        values.put("trans_oil", trans_oil);
        values.put("light_replace", light_replace);
        values.put("wheel_alignment", wheel_alignment);
        values.put("battery_replace", battery_replace);
        values.put("tires_change", tires_change);
        values.put("fuel_filter_change", fuel_filter_change);
        values.put("glass_change", glass_change);
        values.put("mount_balance", mount_balance);

        db.update(TABLE_CHANGES, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }



//////////////////////////////////////////////////////////////////   states ////////////////////////////////////////////////////////////////



    public void addStates(String id,String engineState, String lightsState,String tiresState, String airConditioningState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("engineState", engineState);
        values.put("lightsState", lightsState);
        values.put("tiresState", tiresState);
        values.put("airConditioningState", airConditioningState);

        db.insert(TABLE_STATES, null, values);
    }

    public States getLastStates() {

        States states = new States();

        String selectQuery = "SELECT  * FROM " + TABLE_STATES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToLast()) {


            states.setId(c.getString(c.getColumnIndex("id")));
            states.setEngineState(c.getString(c.getColumnIndex("engineState")));
            states.setAirConditioningState(c.getString(c.getColumnIndex("airConditioningState")));
            states.setLightsState(c.getString(c.getColumnIndex("lightsState")));
            states.setTiresState(c.getString(c.getColumnIndex("tiresState")));



        }else {

        }
        return states;

    }

    public States getStatesSelected(String idverif) {
        States states = new States();
        //engineState TEXT,lightsState TEXT,tiresState TEXT,airConditioningState TEXT

        //String selectQuery = "SELECT states.id,states.engineState,states.lightsState,states.tiresState,states.airConditioningState FROM " + TABLE_STATES+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_states like "+
          //      TABLE_STATES+".id" +
            //    ")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT states.id,states.engineState,states.lightsState,states.tiresState,states.airConditioningState FROM " + TABLE_STATES+" JOIN "+TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id = ? and "+TABLE_VERIFICATION+".id_states like "+
                TABLE_STATES+".id" +
                ")", new String[]{idverif});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                states.setId(c.getString(c.getColumnIndex("id")));
                states.setEngineState(c.getString(c.getColumnIndex("engineState")));
                states.setAirConditioningState(c.getString(c.getColumnIndex("airConditioningState")));
                states.setLightsState(c.getString(c.getColumnIndex("lightsState")));
                states.setTiresState(c.getString(c.getColumnIndex("tiresState")));



            } while (c.moveToNext());
        }
        return states;

    }

    public void deleteStates(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_STATES, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }
    public void updateStates(String id,String engineState, String lightsState,String tiresState, String airConditioningState){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("engineState", engineState);
        values.put("lightsState", lightsState);
        values.put("tiresState", tiresState);
        values.put("airConditioningState", airConditioningState);

        db.update(TABLE_STATES, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }










    ///////////////////////////////////////////////////////////   reparation   //////////////////////////////////////////////////////////


    public void addReparation(String id,long date, double odometer,String typerepare, String note,String id_car,String id_service_center) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("date", date);
        values.put("odometer", odometer);
        values.put("note", note);
        values.put("what_repared",typerepare);
        values.put("id_car", id_car);
        values.put("id_service_center", id_service_center);
        values.put("synced", "notSynced");
        db.insert(TABLE_REPARE, null, values);
    }


    public ArrayList<Repare> getAllReparations(String id_car) {
        ArrayList<Repare> repareArrayList = new ArrayList<Repare>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Repare repare = new Repare();
                repare.setId(c.getString(c.getColumnIndex("id")));
                repare.setDate(c.getLong(c.getColumnIndex("date")));
                repare.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                repare.setNote(c.getString(c.getColumnIndex("note")));

                String route = c.getString(c.getColumnIndex("what_repared"));

                repare.setWhatRepared(route);

                repareArrayList.add(repare);

            } while (c.moveToNext());
        }
        return repareArrayList;

    }


    public Repare getLastReparation(String id_car) {

        Repare repare = new Repare();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            repare.setId(c.getString(c.getColumnIndex("id")));
            repare.setDate(c.getLong(c.getColumnIndex("date")));
            repare.setOdometer(c.getLong(c.getColumnIndex("odometer")));
            repare.setNote(c.getString(c.getColumnIndex("note")));
            String route = c.getString(c.getColumnIndex("what_repared"));



            repare.setWhatRepared(route);



        }else {

        }
        return repare;

    }

    public Repare getLastReparationAllTimes(int id_user) {

        Repare repare = new Repare();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  reparation.id,reparation.date,reparation.odometer,reparation.note,reparation.what_repared FROM " + TABLE_REPARE+" join "+TABLE_CAR+" WHERE ("+TABLE_REPARE+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+")", null);
        // looping through all rows and adding to list

        if (c.moveToPosition(c.getCount() -1)) {


            repare.setId(c.getString(c.getColumnIndex("id")));
            repare.setDate(c.getLong(c.getColumnIndex("date")));
            repare.setOdometer(c.getLong(c.getColumnIndex("odometer")));
            repare.setNote(c.getString(c.getColumnIndex("note")));
            String route = c.getString(c.getColumnIndex("what_repared"));
            repare.setWhatRepared(route);



        }else {
            repare = null;
        }
        return repare;

    }

    public Boolean reparationIsEmpty(String id_car) {


        Boolean b = Boolean.TRUE;


        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ?)", new String[]{id_car});
        // looping through all rows and adding to list

        if (c.moveToLast()) {


            b = Boolean.FALSE;



        }else {

            b = Boolean.TRUE;

        }
        return b;

    }




    public void deleteReparation(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_REPARE, KEY_ID + " = ?",new String[]{String.valueOf(id)});

    }

    public String stateSyncReparation(String id_repare){

        String state_sync="";

        String selectQuery = "SELECT  synced FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id = ?)";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{id_repare});
        // looping through all rows and adding to list

        if (c.moveToFirst()){
            state_sync = c.getString(c.getColumnIndex("synced"));
        }
        return state_sync;

    }


    public void updateReparation(String id,long date, double odometer,String typerepare, String note,String id_car,String id_service_center){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", date);
        values.put("odometer", odometer);
        values.put("note", note);
        values.put("id_car", id_car);
        values.put("id_service_center", id_service_center);
        values.put("what_repared", typerepare);
        if (stateSyncReparation(id).equals("notSynced")){

        }else{
            values.put("synced","not_updated");
        }


        db.update(TABLE_REPARE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public void syncReparation(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("synced", "Synced");
        db.update(TABLE_REPARE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }

    public ArrayList<Repare> getAllNotSyncedReparation(int id_user) {
        ArrayList<Repare> repareArrayList = new ArrayList<Repare>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  reparation.id,reparation.date,reparation.odometer,reparation.note,reparation.what_repared FROM " + TABLE_REPARE+" join "+TABLE_CAR+" WHERE ("+TABLE_REPARE+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_REPARE+".synced = ?)", new String[]{"notSynced"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Repare repare = new Repare();
                repare.setId(c.getString(c.getColumnIndex("id")));
                repare.setDate(c.getLong(c.getColumnIndex("date")));
                repare.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                repare.setNote(c.getString(c.getColumnIndex("note")));
                String route = c.getString(c.getColumnIndex("what_repared"));



                repare.setWhatRepared(route);

                repareArrayList.add(repare);

            } while (c.moveToNext());
        }
        return repareArrayList;

    }

    public ArrayList<Repare> getAllNotUpdatedReparation(int id_user) {
        ArrayList<Repare> repareArrayList = new ArrayList<Repare>();

        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  reparation.id,reparation.date,reparation.odometer,reparation.note,reparation.what_repared FROM " + TABLE_REPARE+" join "+TABLE_CAR+" WHERE ("+TABLE_REPARE+".id_car = "+TABLE_CAR+".id and "+TABLE_CAR+".id_user like "+id_user+" and "+TABLE_REPARE+".synced = ?)", new String[]{"not_updated"});
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Repare repare = new Repare();
                repare.setId(c.getString(c.getColumnIndex("id")));
                repare.setDate(c.getLong(c.getColumnIndex("date")));
                repare.setOdometer(c.getLong(c.getColumnIndex("odometer")));
                repare.setNote(c.getString(c.getColumnIndex("note")));
                String route = c.getString(c.getColumnIndex("what_repared"));



                repare.setWhatRepared(route);

                repareArrayList.add(repare);

            } while (c.moveToNext());
        }
        return repareArrayList;

    }


    public Boolean isReparationExist(String idrepare) {


        //String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car like "+id_car+")" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id = ?)", new String[]{idrepare});
        // looping through all rows and adding to list
        if (c.getCount()==0) {
            return false;
        }else {
            return true;
        }

    }


    ///////////////////////////////////////////////////////// verification filter //////////////////////////////////////////////////////

    public ArrayList<Verification> filterVerification(FilterVerificationClass filterVerificationClass){
        ArrayList<Verification> verificationArrayList = new ArrayList<Verification>();
        String order="";

        
        Cursor c;


        SQLiteDatabase db = this.getReadableDatabase();

        //Log.d("//////////////////////////////////////////////////////////////////database_order", filterVerificationClass.getOrder().toString());
        if (filterVerificationClass.getOrder().equals("oldest")){
            order = "ORDER BY "+TABLE_VERIFICATION+".odometer ASC ";
            //Log.d("/////////////////////////////////////////database_oldest","oldest");

        }else if (filterVerificationClass.getOrder().equals("newest")){
            order = "ORDER BY "+TABLE_VERIFICATION+".odometer DESC ";

           // Log.d("/////////////////////////////////////////database_newest","newest");
        }

        if (filterVerificationClass.getService_centerArrayList()==null){

            c = db.rawQuery("SELECT "+TABLE_VERIFICATION+".id as idver,"+TABLE_VERIFICATION+".date as datever,"+TABLE_VERIFICATION+".odometer as odometerver,"+TABLE_VERIFICATION+".note as notever,"+TABLE_VERIFICATION+".id_engine_oil,"+TABLE_STATES+".engineState,"+TABLE_STATES+".lightsState,"+TABLE_STATES+".tiresState,"+TABLE_STATES+".airConditioningState,"+TABLE_CHANGES+".oil_filter,"+TABLE_CHANGES+".air_filter,"+TABLE_CHANGES+".cabine_filter,"+TABLE_CHANGES+".brakes,"+TABLE_CHANGES+".trans_oil,"+TABLE_CHANGES+".light_replace,"+TABLE_CHANGES+".wheel_alignment,"+TABLE_CHANGES+".battery_replace,"+TABLE_CHANGES+".tires_change,"+TABLE_CHANGES+".fuel_filter_change,"+TABLE_CHANGES+".glass_change,"+TABLE_CHANGES+".mount_balance  FROM " + TABLE_VERIFICATION+" INNER JOIN "+TABLE_CHANGES+" ON "+TABLE_CHANGES+".id = "+TABLE_VERIFICATION+".id_changes INNER JOIN "+TABLE_STATES+" ON "+TABLE_STATES+".id = "+TABLE_VERIFICATION+".id_states WHERE ("+TABLE_VERIFICATION+".id_car = ? ) "+order+"", new String[]{filterVerificationClass.getCar().getId()});

        }else {

            String []edit = new String[filterVerificationClass.getService_centerArrayList().size()+1];
            edit[0]= filterVerificationClass.getCar().getId();

            int i = 0;

            while(i <filterVerificationClass.getService_centerArrayList().size()){
                
                    edit[i+1] = filterVerificationClass.getService_centerArrayList().get(i).getId().toString();
                
                //Log.d("/////////////////////////////////////////database",edit[i].toString());
                i++;
            }

            //serviceCenter="AND "+TABLE_VERIFICATION+".id_service_senter IN "+edit;                                                                                                                                                                                                                                                                                                                                                                                                                                    light_replace TEXT,wheel_alignment TEXT,battery_replace TEXT,tires_change TEXT,fuel_filter_change TEXT,glass_change TEXT,mount_balance TEXT


            c = db.rawQuery("SELECT "+TABLE_VERIFICATION+".id as idver,"+TABLE_VERIFICATION+".date as datever,"+TABLE_VERIFICATION+".odometer as odometerver,"+TABLE_VERIFICATION+".note as notever,"+TABLE_VERIFICATION+".id_engine_oil,"+TABLE_STATES+".engineState,"+TABLE_STATES+".lightsState,"+TABLE_STATES+".tiresState,"+TABLE_STATES+".airConditioningState,"+TABLE_CHANGES+".oil_filter,"+TABLE_CHANGES+".air_filter,"+TABLE_CHANGES+".cabine_filter,"+TABLE_CHANGES+".brakes,"+TABLE_CHANGES+".trans_oil,"+TABLE_CHANGES+".light_replace,"+TABLE_CHANGES+".wheel_alignment,"+TABLE_CHANGES+".battery_replace,"+TABLE_CHANGES+".tires_change,"+TABLE_CHANGES+".fuel_filter_change,"+TABLE_CHANGES+".glass_change,"+TABLE_CHANGES+".mount_balance  FROM " + TABLE_VERIFICATION+" INNER JOIN "+TABLE_CHANGES+" ON "+TABLE_CHANGES+".id = "+TABLE_VERIFICATION+".id_changes INNER JOIN "+TABLE_STATES+" ON "+TABLE_STATES+".id = "+TABLE_VERIFICATION+".id_states WHERE ("+TABLE_VERIFICATION+".id_car = ? and "+TABLE_VERIFICATION+".id_service_center IN ("+placeHolder(filterVerificationClass.getService_centerArrayList().size())+")) "+order+"", edit);



        }


        // looping through all rows and adding to list


        if (c.moveToFirst()) {
            do {
                boolean pass = true;
                Verification verf = new Verification();
                verf.setId(c.getString(c.getColumnIndex("idver")));
                verf.setDate(c.getLong(c.getColumnIndex("datever")));
                verf.setOdometer(c.getLong(c.getColumnIndex("odometerver")));
                verf.setNote(c.getString(c.getColumnIndex("notever")));


                //Log.d("////////////////////////////////database_odometer",String.valueOf(verf.getOdometer()));

                if(!(filterVerificationClass.getDueDate()==null)){
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar1.setTimeInMillis(filterVerificationClass.getStartDate().longValue());
                    calendar2.setTimeInMillis(verf.getDate());

                    if (filterVerificationClass.getDueDate().equals("before")){
                        //Log.d("////////////////////////////////log_before",String.valueOf(calendar1.getTime())+"  "+String.valueOf(calendar2.getTime()));
                        if (calendar2.getTime().before(calendar1.getTime())){
                            pass = true;

                        }else {
                            pass = false;
                        }

                    }else if (filterVerificationClass.getDueDate().equals("after")){
                        if (calendar2.getTime().after(calendar1.getTime())){ pass = true;
                        }else {pass = false;}
                    }else if (filterVerificationClass.getDueDate().equals("between")){
                        Calendar calendar3 = Calendar.getInstance();
                        calendar1.setTimeInMillis(filterVerificationClass.getEndDate());

                        if (calendar2.getTime().after(calendar1.getTime()) && calendar2.getTime().before(calendar3.getTime())){ pass = true;
                        }else {pass = false;}
                    }

                }

                if (!(filterVerificationClass.getDueOdometer()==null)){
                    if (filterVerificationClass.getDueOdometer().equals("greater")){
                        if (verf.getOdometer()>filterVerificationClass.getOdometer()){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }else if (filterVerificationClass.getDueOdometer().equals("less")){
                        if (verf.getOdometer()<filterVerificationClass.getOdometer()){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }else if (filterVerificationClass.getDueOdometer().equals("estimated")){
                        if ((verf.getOdometer()- filterVerificationClass.getOdometer())>1000 || (filterVerificationClass.getOdometer() - verf.getOdometer())>1000){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }

                }




                if (!(filterVerificationClass.getChanges()==null)){
                    if (filterVerificationClass.getChanges().isOil_filter()){

                        if (filterVerificationClass.getChanges().isOil_filter()==Boolean.valueOf(c.getString(c.getColumnIndex("oil_filter")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isAir_filter()){

                        if (filterVerificationClass.getChanges().isAir_filter()==Boolean.valueOf(c.getString(c.getColumnIndex("air_filter")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isTransmission_oil()){

                        if (filterVerificationClass.getChanges().isTransmission_oil()==Boolean.valueOf(c.getString(c.getColumnIndex("trans_oil")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isCabine_filter()){

                        if (filterVerificationClass.getChanges().isCabine_filter()==Boolean.valueOf(c.getString(c.getColumnIndex("cabine_filter")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isLight_replace()){

                        if (filterVerificationClass.getChanges().isLight_replace()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }if (filterVerificationClass.getChanges().isBrakes()){

                        if (filterVerificationClass.getChanges().isBrakes()==Boolean.valueOf(c.getString(c.getColumnIndex("brakes")))){
                            pass = true;
                        }else {pass = false;}
                    }

                }




                if (filterVerificationClass.getEngineOil()==true){
                    if (c.getString(c.getColumnIndex("id_engine_oil"))==null){
                        pass = false;
                    }else {
                        pass = true;
                    }
                }

                if (!(filterVerificationClass.getStates()==null)){
                    if (filterVerificationClass.getStates().getEngineState().equals("all") && filterVerificationClass.getStates().getAirConditioningState().equals("all") && filterVerificationClass.getStates().getTiresState().equals("all") && filterVerificationClass.getStates().getLightsState().equals("all")){

                    }else {
                        if (!filterVerificationClass.getStates().getEngineState().equals("all")){
                            if (filterVerificationClass.getStates().getEngineState().equals(c.getString(c.getColumnIndex("engineState")))){
                                pass = true;
                            }else {
                                pass=false;
                            }

                        } if (!filterVerificationClass.getStates().getAirConditioningState().equals("all")){
                            if (filterVerificationClass.getStates().getAirConditioningState().equals(c.getString(c.getColumnIndex("airConditioningState")))){
                                pass = true;
                            }else {
                                pass=false;
                            }

                        } if (!filterVerificationClass.getStates().getTiresState().equals("all")){
                            if (filterVerificationClass.getStates().getTiresState().equals(c.getString(c.getColumnIndex("tiresState")))){
                                pass = true;
                            }else {
                                pass=false;
                            }

                        } if (!filterVerificationClass.getStates().getLightsState().equals("all")){
                            if (filterVerificationClass.getStates().getLightsState().equals(c.getString(c.getColumnIndex("lightsState")))){
                                pass = true;
                            }else {
                                pass=false;
                            }

                        }
                    }
                }


                    if (pass == true){
                        verificationArrayList.add(verf);
                    }
                    
            } while (c.moveToNext());
        }
        return verificationArrayList;



    }


    String placeHolder(int length){
        StringBuilder ph = new StringBuilder();
        ph.append("?");
        int i = 1;
        while(i<length){
            ph.append(",?");
            i++;
        }
        return ph.toString();
    }



    ///////////////////////////////////////////////////////// verification filter //////////////////////////////////////////////////////

    public ArrayList<Repare> filterReparations(FilterReparationClass filterReparationClass){
        ArrayList<Repare> reparationArrayList = new ArrayList<Repare>();
        String order="";


        Cursor c;


        SQLiteDatabase db = this.getReadableDatabase();

        //Log.d("//////////////////////////////////////////////////////////////////database_order", filterVerificationClass.getOrder().toString());
        if (filterReparationClass.getOrder().equals("oldest")){
            order = "ORDER BY "+TABLE_REPARE+".odometer ASC ";
            //Log.d("/////////////////////////////////////////database_oldest","oldest");

        }else if (filterReparationClass.getOrder().equals("newest")){
            order = "ORDER BY "+TABLE_REPARE+".odometer DESC ";

            // Log.d("/////////////////////////////////////////database_newest","newest");
        }

        if (filterReparationClass.getService_centerArrayList()==null){

            c = db.rawQuery("SELECT "+TABLE_REPARE+".id as idrep,"+TABLE_REPARE+".date as daterep,"+TABLE_REPARE+".odometer as odometerrep,"+TABLE_REPARE+".note as noterepkes,"+TABLE_REPARE+".what_repared  FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ? ) "+order+"", new String[]{filterReparationClass.getCar().getId()});

        }else {

            String []edit = new String[filterReparationClass.getService_centerArrayList().size()+1];
            edit[0]= filterReparationClass.getCar().getId();

            int i = 0;

            while(i <filterReparationClass.getService_centerArrayList().size()){

                edit[i+1] = filterReparationClass.getService_centerArrayList().get(i).getId().toString();

                //Log.d("/////////////////////////////////////////database",edit[i].toString());
                i++;
            }

            //serviceCenter="AND "+TABLE_VERIFICATION+".id_service_senter IN "+edit;

            c = db.rawQuery("SELECT "+TABLE_REPARE+".id as idrep,"+TABLE_REPARE+".date as daterep,"+TABLE_REPARE+".odometer as odometerrep,"+TABLE_REPARE+".note as noterepkes,"+TABLE_REPARE+".what_repared  FROM " + TABLE_REPARE+"  WHERE ("+TABLE_REPARE+".id_car = ? and "+TABLE_REPARE+".id_service_center IN ("+placeHolder(filterReparationClass.getService_centerArrayList().size())+")) "+order+"", edit);


        }


        // looping through all rows and adding to list


        if (c.moveToFirst()) {
            do {
                boolean pass = true;
                Repare repare = new Repare();
                repare.setId(c.getString(c.getColumnIndex("idrep")));
                repare.setDate(c.getLong(c.getColumnIndex("daterep")));
                repare.setOdometer(c.getLong(c.getColumnIndex("odometerrep")));
                repare.setNote(c.getString(c.getColumnIndex("noterepkes")));

                String route = c.getString(c.getColumnIndex("what_repared"));

                repare.setWhatRepared(route);


                //Log.d("////////////////////////////////database_odometer",String.valueOf(verf.getOdometer()));

                if(!(filterReparationClass.getDueDate()==null)){
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar1.setTimeInMillis(filterReparationClass.getStartDate().longValue());
                    calendar2.setTimeInMillis(repare.getDate());

                    if (filterReparationClass.getDueDate().equals("before")){
                        //Log.d("////////////////////////////////log_before",String.valueOf(calendar1.getTime())+"  "+String.valueOf(calendar2.getTime()));
                        if (calendar2.getTime().before(calendar1.getTime())){
                            pass = true;

                        }else {
                            pass = false;
                        }

                    }else if (filterReparationClass.getDueDate().equals("after")){
                        if (calendar2.getTime().after(calendar1.getTime())){ pass = true;
                        }else {pass = false;}
                    }else if (filterReparationClass.getDueDate().equals("between")){
                        Calendar calendar3 = Calendar.getInstance();
                        calendar1.setTimeInMillis(filterReparationClass.getEndDate());

                        if (calendar2.getTime().after(calendar1.getTime()) && calendar2.getTime().before(calendar3.getTime())){ pass = true;
                        }else {pass = false;}
                    }

                }

                if (!(filterReparationClass.getDueOdometer()==null)){
                    if (filterReparationClass.getDueOdometer().equals("greater")){
                        if (repare.getOdometer()>filterReparationClass.getOdometer()){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }else if (filterReparationClass.getDueOdometer().equals("less")){
                        if (repare.getOdometer()<filterReparationClass.getOdometer()){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }else if (filterReparationClass.getDueOdometer().equals("estimated")){
                        if ((repare.getOdometer()- filterReparationClass.getOdometer())>1000 || (filterReparationClass.getOdometer() - repare.getOdometer())>1000){
                            pass = true;
                        }else {
                            pass = false;
                        }

                    }

                }


                if (!filterReparationClass.getWhatRepared().isEmpty()){
                    if (filterReparationClass.getWhatRepared().contains("Engine") && repare.getWhatRepared().contains("Engine")){
                        pass = true;
                    }else if (filterReparationClass.getWhatRepared().contains("Engine") && !repare.getWhatRepared().contains("Engine")){
                        pass = false;
                    }
                    if (filterReparationClass.getWhatRepared().contains("Clim") && repare.getWhatRepared().contains("Clim")){
                        pass = true;
                    }else if (filterReparationClass.getWhatRepared().contains("Clim") && !repare.getWhatRepared().contains("Clim")){
                        pass = false;
                    }
                    if (filterReparationClass.getWhatRepared().contains("Suspension") && repare.getWhatRepared().contains("Suspension")){
                        pass = true;
                    }else if (filterReparationClass.getWhatRepared().contains("Suspension") && !repare.getWhatRepared().contains("Suspension")){
                        pass = false;
                    }
                    if (filterReparationClass.getWhatRepared().contains("Structure/Paint") && repare.getWhatRepared().contains("Structure/Paint")){
                        pass = true;
                    }else if (filterReparationClass.getWhatRepared().contains("Structure/Paint") && !repare.getWhatRepared().contains("Structure/Paint")){
                        pass = false;
                    }

                }





                if (pass == true){
                    reparationArrayList.add(repare);
                }

            } while (c.moveToNext());
        }
        return reparationArrayList;



    }




    /////////////////////////////////////////////////////////report////////////////////////////////////////////////////////////

    public int getCarsCount(int id_user){

        String selectQuery = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        // looping through all rows and adding to list

        return c.getCount();
    }

    public int getServiceCentersCount(int id_user){

        String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_CENTER+" WHERE ("+TABLE_SERVICE_CENTER+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        // looping through all rows and adding to list

        return c.getCount();
    }

    public int getVerificationCount(int id_user){

        String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" JOIN "+TABLE_CAR+" WHERE ("+TABLE_CAR+".id = "+TABLE_VERIFICATION+".id_car AND "+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        // looping through all rows and adding to list

        return c.getCount();
    }

    public int getReparationsCount(int id_user){

        String selectQuery = "SELECT  * FROM " + TABLE_REPARE+" JOIN "+TABLE_CAR+" WHERE ("+TABLE_CAR+".id = "+TABLE_REPARE+".id_car AND "+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        // looping through all rows and adding to list

        return c.getCount();
    }

    public double getGlobalDistance(int id_user){
        double d = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);
        // looping through all rows and adding to list
        if (c2.moveToFirst()){
            do {
                String id_car = c2.getString(c2.getColumnIndex("id"));
                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToLast()){
                    d = d +c.getLong(c.getColumnIndex("odometer"));
                }
            }while (c2.moveToNext());
        }


        return d;
    }

    public double getYearDistance(int id_user,int current_year){
        double d = 0;
        double d_year = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);
        // looping through all rows and adding to list
        if (c2.moveToFirst()){
            do {
                d=0;
                String id_car = c2.getString(c2.getColumnIndex("id"));

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToFirst()){

                   do {

                       Calendar calendar1 = Calendar.getInstance();
                       calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                       int year = calendar1.get(Calendar.YEAR);
                       if (current_year == year){
                           long distance = c.getLong(c.getColumnIndex("odometer"));
                           if (d<distance){
                               d = distance;
                           }
                       }

                   }while (c.moveToNext());
                   d_year = d_year+d;
                }
            }while (c2.moveToNext());
        }


        return d_year;
    }

    public double getAvgDistanceBetweenVerification(int id_user){
        double d_between = 0;
        double d_between_global = 0;
        double d = 0;
        double d_year = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);
        // looping through all rows and adding to list
        if (c2.moveToFirst()){
            do {
                d=0;
                String id_car = c2.getString(c2.getColumnIndex("id"));

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? ) order by odometer asc";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                d_between = 0;
                int i =0;
                if (c.moveToFirst()){

                    do {
                            if (c.isFirst()){
                                long distance = c.getLong(c.getColumnIndex("odometer"));
                                d = distance - d;
                                d_between = 0;

                            }else {

                                long distance = c.getLong(c.getColumnIndex("odometer"));
                                d = distance - (distance-d);
                                d_between = d_between +d;
                            }
                            i++;




                    }while (c.moveToNext());
                }
                d_between = d_between/i;
                d_between_global = d_between_global+d_between;
            }while (c2.moveToNext());
        }


        return d_between_global;
    }


    public double getAvgVerificationYear(int id_user){
        double one = 0;
        double global_global = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);

        if (c2.moveToFirst()){
            do {

                String id_car = c2.getString(c2.getColumnIndex("id"));

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                one = 0;
                if (c.moveToFirst()){

                    do {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        int current_year = Calendar.getInstance().get(Calendar.YEAR);
                        if (current_year == year){
                            one++;
                        }

                    }while (c.moveToNext());
                    global_global = global_global+one;
                }
            }while (c2.moveToNext());
        }

        if (c2.getCount()==0){
            return 0;
        }else {

            return global_global/c2.getCount();
        }


    }


    public double getAvgReparationYear(int id_user){
        double one = 0;
        double global_global = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);

        if (c2.moveToFirst()){
            do {

                String id_car = c2.getString(c2.getColumnIndex("id"));

                String selectQuery = "SELECT  * FROM " + TABLE_REPARE+"  WHERE ("+TABLE_REPARE+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                one = 0;
                if (c.moveToFirst()){

                    do {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        int current_year = Calendar.getInstance().get(Calendar.YEAR);
                        if (current_year == year){
                            one++;
                        }

                    }while (c.moveToNext());
                    global_global = global_global+one;
                }
            }while (c2.moveToNext());
        }


        if (c2.getCount()==0){
            return 0;
        }else {

            return global_global/c2.getCount();
        }
    }



    public int getVerificationCountYear(int id_user,int current_year){
        int count = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);


        if (c2.moveToFirst()){
            do {

                String id_car = c2.getString(c2.getColumnIndex("id"));



                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ?)";
                Cursor c = db.rawQuery(selectQuery,new String[]{String.valueOf(id_car)});

                if (c.moveToFirst()){
                    do {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);

                        if (current_year == year){
                            count++;
                            }

                    }while (c.moveToNext());
                }
            }while (c2.moveToNext());
        }

        if (c2.getCount()==0){
            return 0;
        }else {

            return count;
        }


    }


    public int getReparationCountYear(int id_user,int current_year){
        int count = 0;

        String selectQueryCars = "SELECT  * FROM " + TABLE_CAR+" WHERE ("+TABLE_CAR+".id_user like "+id_user+")";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c2 = db.rawQuery(selectQueryCars,null);

        if (c2.moveToFirst()){
            do {

                String id_car = c2.getString(c2.getColumnIndex("id"));

                String selectQuery = "SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToFirst()){

                    do {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        if (current_year == year){
                            count++;
                        }

                    }while (c.moveToNext());
                }
            }while (c2.moveToNext());
        }


        if (c2.getCount()==0){
            return 0;
        }else {

            return count;
        }
    }



    //////////////////////////////////////////////////////////////stats by car/////////////////////////////////////////////////////////





    public int getVerificationCountByCar(String id_car){

        String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
        // looping through all rows and adding to list

        return c.getCount();
    }

    public int getReparationsCountByCar(String id_car){

        String selectQuery = "SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ? )";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,new String[]{id_car});


        return c.getCount();
    }

    public double getGlobalDistanceByCar(String id_car){
        double d = 0;

        SQLiteDatabase db = this.getReadableDatabase();

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToLast()){
                    d = d +c.getLong(c.getColumnIndex("odometer"));
                }


        return d;
    }


    public double getAvgDistanceBetweenVerificationBycar(String id_car){
        double d_between = 0;
        double d = 0;

        SQLiteDatabase db = this.getReadableDatabase();


                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? ) order by odometer asc";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                d_between = 0;
                int i =0;
                if (c.moveToFirst()){
                    do {
                        if (c.isFirst()){
                            long distance = c.getLong(c.getColumnIndex("odometer"));
                            d = distance - d;
                            d_between = 0;
                        }else {
                            long distance = c.getLong(c.getColumnIndex("odometer"));
                            d = distance - (distance-d);
                            d_between = d_between +d;
                        }
                        i++;
                    }while (c.moveToNext());
                }
                d_between = d_between/i;



        return d_between;
    }

    public double getAvgDistanceBetweenReparationBycar(String id_car){
        double d_between = 0;
        double d = 0;

        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ? ) order by odometer asc";
        Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
        d_between = 0;
        int i =0;
        if (c.moveToFirst()){
            do {
                if (c.isFirst()){
                    long distance = c.getLong(c.getColumnIndex("odometer"));
                    d = distance - d;
                    d_between = 0;
                }else {
                    long distance = c.getLong(c.getColumnIndex("odometer"));
                    d = distance - (distance-d);
                    d_between = d_between +d;
                }
                i++;
            }while (c.moveToNext());
        }
        d_between = d_between/i;



        return d_between;
    }


    public double getAvgVerificationYearByCar(String id_car){
        double one = 0;
        double global_global = 0;

        SQLiteDatabase db = this.getReadableDatabase();

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                one = 0;
                if (c.moveToFirst()){

                    do {

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        int current_year = Calendar.getInstance().get(Calendar.YEAR);
                        if (current_year == year){
                            one++;
                        }

                    }while (c.moveToNext());
                    global_global = global_global+one;
                }

                return one/c.getCount();

    }


    public double getAvgReparationYearByCar(String id_car){
        double one = 0;
        double global_global = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_REPARE+"  WHERE ("+TABLE_REPARE+".id_car = ? )";
        Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
        one = 0;
        if (c.moveToFirst()){

            do {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                int year = calendar1.get(Calendar.YEAR);
                int current_year = Calendar.getInstance().get(Calendar.YEAR);
                if (current_year == year){
                    one++;
                }

            }while (c.moveToNext());
            global_global = global_global+one;
        }

        return one/c.getCount();

    }

    public double getYearDistanceByCar(String id_car,int current_year){
        double d = 0;

        SQLiteDatabase db = this.getReadableDatabase();

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+" WHERE ("+TABLE_VERIFICATION+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToFirst()){
                    do {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        if (current_year == year){
                            long distance = c.getLong(c.getColumnIndex("odometer"));
                            if (d<distance){
                                d = distance;
                            }
                        }

                    }while (c.moveToNext());
                }

        return d;
    }


    public int getVerificationCountYearByCar(String id_car,int current_year){
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();

                String selectQuery = "SELECT  * FROM " + TABLE_VERIFICATION+"  WHERE ("+TABLE_VERIFICATION+".id_car = ?)";
                Cursor c = db.rawQuery(selectQuery,new String[]{String.valueOf(id_car)});

                if (c.moveToFirst()){
                    do {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);

                        if (current_year == year){
                            count++;
                        }

                    }while (c.moveToNext());
                }

            return count;


    }


    public int getReparationCountYearByCar(String id_car,int current_year){
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();

                String selectQuery = "SELECT  * FROM " + TABLE_REPARE+" WHERE ("+TABLE_REPARE+".id_car = ? )";
                Cursor c = db.rawQuery(selectQuery,new String[]{id_car});
                if (c.moveToFirst()){
                    do {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTimeInMillis(c.getLong(c.getColumnIndex("date")));
                        int year = calendar1.get(Calendar.YEAR);
                        if (current_year == year){
                            count++;
                        }

                    }while (c.moveToNext());
                }


            return count;

    }



















}


