package co.example.hp.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.classes.Recommendation;
import co.example.hp.myapplication.classes.RecommendationItem;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendationMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private LinearLayout linearLayout_there_is_connection,linearLayout_there_is_no_connection,ll_brakes,ll_engin_oil,ll_battery,ll_lights,ll_tires,ll_trans,ll_wheel_alig,ll_mount;
    private SwipeRefreshLayout swipeLayout;
    private DownloadDataRepository downloadDataRepository;
    private GetLastRecommendation getLastRecommendation;
    private DatabaseHelper databaseHelper;
    private Button button_see_all;
    private CircleImageView imglogo;
    private TextView date,car_model,ll_no_recom;
    private Recommendation recommendation = new Recommendation();



    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_main, container, false);





        downloadDataRepository = DownloadDataRepository.getInstance(getContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));

        databaseHelper = new DatabaseHelper(getContext());

        linearLayout_there_is_connection = view.findViewById(R.id.there_is_connection_to_the_server);
        linearLayout_there_is_no_connection = view.findViewById(R.id.no_connection_to_the_server);
        ll_no_recom = view.findViewById(R.id.no_recommendations);
        linearLayout_there_is_connection.setVisibility(View.GONE);
        linearLayout_there_is_no_connection.setVisibility(View.GONE);
        ll_no_recom.setVisibility(View.GONE);
        button_see_all = view.findViewById(R.id.recom_see_all_main);



        ll_brakes = view.findViewById(R.id.recom_engine_brakes_main);
        ll_brakes.setVisibility(View.GONE);
        ll_engin_oil = view.findViewById(R.id.recom_engine_oil_main);
        ll_engin_oil.setVisibility(View.GONE);
        ll_battery = view.findViewById(R.id.recom_engine_battery_main);
        ll_battery.setVisibility(View.GONE);
        ll_lights = view.findViewById(R.id.recom_lights_main);
        ll_lights.setVisibility(View.GONE);
        ll_mount = view.findViewById(R.id.recom_mount_main);
        ll_mount.setVisibility(View.GONE);
        ll_tires = view.findViewById(R.id.recom_tires_main);
        ll_tires.setVisibility(View.GONE);
        ll_wheel_alig = view.findViewById(R.id.recom_wheel_algn_main);
        ll_wheel_alig.setVisibility(View.GONE);
        ll_trans = view.findViewById(R.id.recom_trans_main);
        ll_trans.setVisibility(View.GONE);
        date = view.findViewById(R.id.recom_date_main);
        car_model = view.findViewById(R.id.recom_car_model_main);
        imglogo = view.findViewById(R.id.recom_car_logo_main);





        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refrech_recpmmendation);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        swipeLayout.setRefreshing(true);

        getLastRecommendation = new GetLastRecommendation(downloadDataRepository);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                if (isNetworkConnected()){
                    linearLayout_there_is_no_connection.setVisibility(View.GONE);

                    getLastRecommendation.execute();

                }else {
                    linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                    linearLayout_there_is_connection.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"no connection to the server", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);

                }
            }
        },3000);



        button_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RecommendationActivity.class);
                startActivity(intent);
            }
        });




        return view;
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


    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isNetworkConnected()){
                    linearLayout_there_is_no_connection.setVisibility(View.GONE);
                    new GetLastRecommendation(downloadDataRepository).execute();


                }else {
                    linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                    linearLayout_there_is_connection.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"no connection to the server", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }




            }
        },2000);


    }






    public class GetLastRecommendation extends AsyncTask<Void, Void, PredictedMaintenance> {

        DownloadDataRepository downloadDataRepository;
        JSONArray response;
        JSONObject recommendationResponse;
        PredictedMaintenance predictedMaintenance = new PredictedMaintenance();
        Semaphore s = new Semaphore(0);


        public GetLastRecommendation(DownloadDataRepository downloadDataRepository){
            this.downloadDataRepository = downloadDataRepository;
        }

        @Override
        protected PredictedMaintenance doInBackground(Void... params) {



            downloadDataRepository.downloadAllPredictedMaintenance(new VolleyCallBack() {
                @Override
                public void onSuccess() {

                     response = downloadDataRepository.responseToReturn;
                        if (!(response.length()==0)){

                            Log.d("///////////////////////////////////if","ok");


                            try {
                                recommendationResponse = (JSONObject) response.get(0);
                                JSONObject jsonObject_car = new JSONObject();
                                jsonObject_car = recommendationResponse.getJSONObject("car");
                                predictedMaintenance.setId_car(jsonObject_car.getString("id"));
                                predictedMaintenance.setDate(recommendationResponse.getLong("date"));
                                predictedMaintenance.setPredictedMaintenance(recommendationResponse.getString("predictedMaintenance"));
                                recommendation = responseToRecommendation(predictedMaintenance.getPredictedMaintenance());
                                s.release();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                s.release();
                            }

                        }else {
                                Log.d("///////////////////////////////////else","ok");
                                predictedMaintenance = null;
                                s.release();

                        }



                }
            });


            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return predictedMaintenance;
        }

        @Override
        protected void onPostExecute(PredictedMaintenance result) {
            super.onPostExecute(result);

            swipeLayout.setRefreshing(false);




            if (result == null){
                ll_no_recom.setVisibility(View.VISIBLE);
            }else {
                ll_no_recom.setVisibility(View.GONE);
                    Car car = databaseHelper.getSelectedCar(result.getId_car());
                    switch (car.getCompany()){
                        case "chevrolet":
                            imglogo.setImageResource(R.drawable.chevrolet);
                            break;
                        case "mitsubishi":
                            imglogo.setImageResource(R.drawable.mitsubishi);
                            break;
                        case "landrover":
                            imglogo.setImageResource(R.drawable.landrover);
                            break;
                        case "audi":
                            imglogo.setImageResource(R.drawable.audi);
                            break;
                        case "bmw":
                            imglogo.setImageResource(R.drawable.bmw);
                            break;
                        case "cadillac":
                            imglogo.setImageResource(R.drawable.cadillac);
                            break;
                        case "ford":
                            imglogo.setImageResource(R.drawable.ford);
                            break;
                        case "gmc":
                            imglogo.setImageResource(R.drawable.gmc);
                            break;
                        case "honda":
                            imglogo.setImageResource(R.drawable.honda);
                            break;
                        case "hyundai":
                            imglogo.setImageResource(R.drawable.hyundai);
                            break;
                        case "kia":
                            imglogo.setImageResource(R.drawable.kia);
                            break;
                        case "lexus":
                            imglogo.setImageResource(R.drawable.lexus);
                            break;
                        case "fiat":
                            imglogo.setImageResource(R.drawable.fiat);
                            break;
                        case "mazda":
                            imglogo.setImageResource(R.drawable.mazda);
                            break;
                        case "mercedesbenz":
                            imglogo.setImageResource(R.drawable.mercedesbenz);
                            break;
                        case "minicoper":
                            imglogo.setImageResource(R.drawable.minicooper);
                            break;
                        case "nissan":
                            imglogo.setImageResource(R.drawable.nissancar);
                            break;
                        case "suzuki":
                            imglogo.setImageResource(R.drawable.suzuki);
                            break;
                        case "toyota":
                            imglogo.setImageResource(R.drawable.toyota);
                            break;
                        case "volkswagen":
                            imglogo.setImageResource(R.drawable.volkswagen);
                            break;
                        case "peugeot":
                            imglogo.setImageResource(R.drawable.peugeot);
                            break;
                        case "renault":
                            imglogo.setImageResource(R.drawable.renault);
                            break;
                    }
                    Calendar calendar = Calendar.getInstance();

                    calendar.setTimeInMillis(result.getDate());

                    SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");

                    date.setText(formatter.format(calendar.getTime()));

                    car_model.setText(car.getModel());

                    linearLayout_there_is_connection.setVisibility(View.VISIBLE);

                    for (int i=0;i<recommendation.getRecommendationItems().size();i++){
                        RecommendationItem recommendationItem = new RecommendationItem();
                        recommendationItem = recommendation.getRecommendationItems().get(i);
                        if (recommendationItem.getCoef()>4){
                            if (recommendationItem.getItem().contains("Mount And Balance")){
                                ll_mount.setVisibility(View.VISIBLE);
                            }else if (recommendationItem.getItem().contains("Oil Change")){
                                ll_engin_oil.setVisibility(View.VISIBLE);
                            }else if (recommendationItem.getItem().contains("Battery Replaced")){
                                ll_battery.setVisibility(View.VISIBLE);
                            }else if (recommendationItem.getItem().contains("Brake Job")){
                                ll_brakes.setVisibility(View.VISIBLE);
                            }else if (recommendationItem.getItem().contains("Brake Job")){
                                ll_lights.setVisibility(View.VISIBLE);
                            }else if (recommendationItem.getItem().contains("Tire Repair") || recommendationItem.getItem().contains("Tire Rotation")){
                                ll_tires.setVisibility(View.VISIBLE);;

                            }else if (recommendationItem.getItem().contains("Wheel Alignment")){
                            ll_wheel_alig.setVisibility(View.VISIBLE);

                        }else if (recommendationItem.getItem().contains("Transmission Service")){
                            ll_wheel_alig.setVisibility(View.VISIBLE);

                        }

                    }
                }
            }



        }
    }



    public Recommendation responseToRecommendation(String response){
        String follower[] ;
        String follower_item[] ;
        ArrayList<RecommendationItem> recommendationItemArrayList = new ArrayList<RecommendationItem>();
        Recommendation recommendation = new Recommendation();
        follower = response.split("\\(");

        for (int i=1;i<follower.length;i++){
            follower_item = follower[i].split(",");
            RecommendationItem recommendationItem = new RecommendationItem();
            recommendationItem.setItem(String.valueOf(follower_item[0]));
            Float f = Float.parseFloat(follower_item[1].split("\\)")[0].trim());
            recommendationItem.setCoef(Integer.valueOf(f.intValue()));
            recommendationItemArrayList.add(recommendationItem);
        }
        recommendation.setRecommendationItems(recommendationItemArrayList);

        return recommendation;
    }




}


