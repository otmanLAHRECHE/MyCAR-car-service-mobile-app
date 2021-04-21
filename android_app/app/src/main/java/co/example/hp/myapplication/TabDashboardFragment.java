package co.example.hp.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;
import co.example.hp.myapplication.classes.Advice;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

public class TabDashboardFragment extends Fragment {
    private Button details_verif,buttonSeemore_repare,advice_more;
    private TextView date_verif,car_model_verif,service_center_verif,odometer_verif,date_repare,car_model_repare,service_center_repare,odometer_repare,advice_title,advice_sub_title;
    private LinearLayout linearLayout_verif,linearLayout_repare;
    private Chip oilchange,oilfilter,airfilter,brakes,cabinfilter,transoil,light,wheel,battery,tires,fuel,glass,balance,engine_repare,clim_repare,suspen_repare,struct_repare;
    private Verification verification;
    private Car car;
    private Service_center service_center;
    private Changes changes;
    private Repare reparation;
    private DatabaseHelper databaseHelper;
    private CardView cardView_verif,cardView_repare,cardView_advice;
    private DownloadDataRepository downloadDataRepository;
    private ImageView advice_image;
    private String content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Log.d("///////////////////////////////////////////tab_auth",SplashActivity.sharedPreferences.getString("authtoken","empty"));
        downloadDataRepository = DownloadDataRepository.getInstance(getContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));



        databaseHelper = new DatabaseHelper(getContext());


        cardView_verif = view.findViewById(R.id.fragment_main_verif_card);
        cardView_repare = view.findViewById(R.id.fragment_main_repare_card);

        cardView_advice = view.findViewById(R.id.fragment_main_advice_card);
        advice_title = view.findViewById(R.id.fragment_main_advice_title);
        advice_sub_title = view.findViewById(R.id.fragment_main_advice_subtext);
        advice_image = view.findViewById(R.id.fragment_main_advice_image);
        advice_more = view.findViewById(R.id.fragment_main_advice_more);

        oilchange = view.findViewById(R.id.chip_oil_change_main);
        oilchange.setVisibility(View.GONE);
        oilfilter = view.findViewById(R.id.chip_oil_filter_main);
        oilfilter.setVisibility(View.GONE);
        airfilter = view.findViewById(R.id.chip_air_filter_main);
        airfilter.setVisibility(View.GONE);
        brakes = view.findViewById(R.id.chip_brakes_main);
        brakes.setVisibility(View.GONE);
        cabinfilter = view.findViewById(R.id.chip_cabin_filter_main);
        cabinfilter.setVisibility(View.GONE);
        transoil = view.findViewById(R.id.chip_trans_oil_main);
        transoil.setVisibility(View.GONE);
        light = view.findViewById(R.id.chip_light_main);
        light.setVisibility(View.GONE);
        wheel = view.findViewById(R.id.chip_wheel_main);
        wheel.setVisibility(View.GONE);
        battery = view.findViewById(R.id.chip_battery_main);
        battery.setVisibility(View.GONE);
        tires = view.findViewById(R.id.chip_tires_main);
        tires.setVisibility(View.GONE);
        fuel = view.findViewById(R.id.chip_fuel_main);
        fuel.setVisibility(View.GONE);
        glass = view.findViewById(R.id.chip_glass_main);
        glass.setVisibility(View.GONE);
        balance = view.findViewById(R.id.chip_balance_check_main);
        balance.setVisibility(View.GONE);

        details_verif = view.findViewById(R.id.fragment_main_verif_details);
        linearLayout_verif = view.findViewById(R.id.linearLayout_verif_main);

        date_verif = view.findViewById(R.id.fragment_main_verif_date);
        car_model_verif = view.findViewById(R.id.fragment_main_verif_car_model);
        service_center_verif = view.findViewById(R.id.fragment_main_verif_service_center);
        odometer_verif = view.findViewById(R.id.fragment_main_verif_distance);

        date_repare = view.findViewById(R.id.fragment_main_repare_date);
        service_center_repare = view.findViewById(R.id.fragment_main_repare_service_center);
        odometer_repare = view.findViewById(R.id.fragment_main_repare_distance);
        car_model_repare = view.findViewById(R.id.fragment_main_repare_car_model);

        buttonSeemore_repare = view.findViewById(R.id.fragment_main_repare_details);
        linearLayout_repare = view.findViewById(R.id.linearLayout_repare_main);

        engine_repare = view.findViewById(R.id.chip_engine_repare_main);
        engine_repare.setVisibility(View.GONE);
        clim_repare = view.findViewById(R.id.chip_clim_repare_main);
        clim_repare.setVisibility(View.GONE);
        suspen_repare = view.findViewById(R.id.chip_suspension_repare_main);
        suspen_repare.setVisibility(View.GONE);
        struct_repare = view.findViewById(R.id.chip_car_structure_repare_main);
        struct_repare.setVisibility(View.GONE);

        advice_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advice_more.getText().toString().equals("more")){
                    TransitionManager.beginDelayedTransition(cardView_advice,
                            new AutoTransition());
                    advice_more.setText("less");
                    advice_sub_title.setText(content);


                }else{
                    TransitionManager.beginDelayedTransition(cardView_advice,
                            new AutoTransition());
                    advice_more.setText("more");
                    advice_sub_title.setText(content.substring(0,150)+"...");


                }
            }
        });

        details_verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),VerificationSelected.class);
                intent.putExtra("verif",verification);
                startActivity(intent);
            }
        });

        buttonSeemore_repare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ReparationSelected.class);
                intent.putExtra("repare",reparation);
                startActivity(intent);
            }
        });


        GetLastVerification getLastVerification = new GetLastVerification();
        getLastVerification.execute();

        GetLastReparation getLastReparation = new GetLastReparation();
        getLastReparation.execute();

        if (isNetworkConnected()){

            GetRandomAdvice getRandomAdvice = new GetRandomAdvice();
            getRandomAdvice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //getRandomAdvice.execute();

        }else {
            cardView_advice.setVisibility(View.GONE);
        }

        return view;
    }


    public class GetLastVerification extends AsyncTask<Void, Void, Object[]> {

        Object [] objects = {new Object(),new Object(),new Object(),new Object()};
        public GetLastVerification(){
        }

        @Override
        protected Object[] doInBackground(Void... params) {
            int id_user = databaseHelper.getUserLogedInId();
            Verification verification = databaseHelper.getLastVerificationAllTimes(id_user);
            if (verification ==null){
                return null;
            }else {

                Car car = (Car) databaseHelper.getCarSelected(verification.getId());
                Service_center service_center = (Service_center) databaseHelper.getServiceCenterSelected(verification.getId());
                Changes changes =(Changes) databaseHelper.getChangeSelected(verification.getId());
                objects[0] = verification;
                objects[1] = car;
                objects[2] = service_center;
                objects[3] = changes;

                return objects;
            }
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            if (result == null){
                cardView_verif.setVisibility(View.GONE);
            }else {
                verification =(Verification) result[0];
                car = (Car) result[1];
                service_center = (Service_center) result[2];
                changes = (Changes) result[3];

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(verification.getDate());

                SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");


                date_verif.setText(formatter.format(calendar.getTime()));
                car_model_verif.setText(car.getModel());
                service_center_verif.setText(service_center.getName());
                odometer_verif.setText(String.valueOf(verification.getOdometer()));

                if(databaseHelper.therIsChangeOilInThisVerf(verification.getId())==true){
                    oilchange.setVisibility(View.VISIBLE);
                }
                if (changes.isOil_filter() == true){
                    oilfilter.setVisibility(View.VISIBLE);
                }
                if (changes.isAir_filter() == true){
                    airfilter.setVisibility(View.VISIBLE);

                }
                if (changes.isCabine_filter() == true){
                    cabinfilter.setVisibility(View.VISIBLE);
                }
                if (changes.isBrakes() == true){
                    brakes.setVisibility(View.VISIBLE);
                }
                if (changes.isTransmission_oil()== true){
                    transoil.setVisibility(View.VISIBLE);
                }if (changes.isWheel_alignment()== true){
                    wheel.setVisibility(View.VISIBLE);
                }if (changes.isBattery_replace()== true){
                    battery.setVisibility(View.VISIBLE);
                }if (changes.isLight_replace()== true){
                    light.setVisibility(View.VISIBLE);
                }if (changes.isTires_change()== true){
                    tires.setVisibility(View.VISIBLE);
                }if (changes.isGlass_change()== true){
                    glass.setVisibility(View.VISIBLE);
                }if (changes.isMount_balance()== true){
                    balance.setVisibility(View.VISIBLE);
                }if (changes.isFuel_filter_change()== true){
                    fuel.setVisibility(View.VISIBLE);
                }



            }
        }
    }


    public class GetLastReparation extends AsyncTask<Void, Void, Object[]> {

        Object [] objects = {new Object(),new Object(),new Object()};
        public GetLastReparation(){
        }

        @Override
        protected Object[] doInBackground(Void... params) {
            int id_user = databaseHelper.getUserLogedInId();
            Repare repare = databaseHelper.getLastReparationAllTimes(id_user);
            if (repare ==null){
                return null;
            }else {

                Car car = databaseHelper.getCarSelectedFromReparation(repare.getId());
                Service_center service_center = databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());
                objects[0] = repare;
                objects[1] = car;
                objects[2] = service_center;
                return objects;
            }
        }

        @Override
        protected void onPostExecute(Object[] result) {
            super.onPostExecute(result);
            if (result == null){
                cardView_repare.setVisibility(View.GONE);
            }else {
                reparation =(Repare) result[0];
                car = (Car) result[1];
                service_center = (Service_center) result[2];

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(reparation.getDate());

                SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");


                date_repare.setText(formatter.format(calendar.getTime()));
                car_model_repare.setText(car.getModel());
                service_center_repare.setText(service_center.getName());
                odometer_repare.setText(String.valueOf(reparation.getOdometer()));

                String f = reparation.getWhatRepared();

                if (f.contains("Engine")){
                    engine_repare.setVisibility(View.VISIBLE);
                }
                if (f.contains("Clim")){
                    clim_repare.setVisibility(View.VISIBLE);
                }
                if (f.contains("Suspension")){
                    suspen_repare.setVisibility(View.VISIBLE);
                }
                if (f.contains("Structure/Paint")){
                    struct_repare.setVisibility(View.VISIBLE);
                }



            }
        }
    }



    public class GetRandomAdvice extends AsyncTask<Void, Void, Advice> {

        Advice advice = new Advice();
        JSONObject adviceResponse;
        Semaphore s = new Semaphore(0);

        public GetRandomAdvice(){
        }

        @Override
        protected Advice doInBackground(Void... params) {

            downloadDataRepository.downloadRandomAdvice(new VolleyCallBack() {
                @Override
                public void onSuccess() {

                    adviceResponse = downloadDataRepository.responseToReturnObject;
                    try {
                        if (!(adviceResponse.getString("id").equals(null))){


                            try {
                                advice.setId(adviceResponse.getString("id"));
                                advice.setTitle(adviceResponse.getString("title"));
                                advice.setContent(adviceResponse.getString("content"));
                                advice.setImageUrl(new URL(String.valueOf(adviceResponse.getString("url"))));
                                s.release();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                s.release();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                                s.release();
                            }

                        }else {
                                Log.d("///////////////////////////////////else","ok");
                                advice = null;
                                s.release();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return advice;
        }

        @Override
        protected void onPostExecute(Advice result) {
            super.onPostExecute(result);

            if (result == null){
                cardView_advice.setVisibility(View.GONE);

            }else {

                cardView_advice.setVisibility(View.VISIBLE);
                advice_title.setText(result.getTitle());
                advice_sub_title.setText(result.getContent().substring(0,150)+"....");
                content = result.getContent();
                new DownloadImageTask(advice_image)
                        .execute(advice.getImageUrl().toString());

            }

        }
    }


    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return (mNetworkInfo == null) ? false : true;

        }catch (NullPointerException e){
            return false;

        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
