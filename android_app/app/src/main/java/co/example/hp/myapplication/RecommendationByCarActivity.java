package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RecommendationByCarActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private AdapterItemRecommendation adapterItemRecommendation;
    private ArrayList<PredictedMaintenance> predictedMaintenanceArrayListGlobal;
    private GetAllRecommendationByCar getAllRecommendationByCar;
    private LinearLayout linearLayout_there_is_no_connection,linearLayout_there_is_connection;
    private DownloadDataRepository downloadDataRepository;
    private Car car;
    private TextView textView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_by_car);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        car =(Car) intent.getSerializableExtra("car");

        Log.d("///////////////////////////////////id",car.getId());

        listView = (ListView) this.findViewById(R.id.lv_recommendation_by_car);
        linearLayout_there_is_no_connection = this.findViewById(R.id.no_connection_to_the_server);
        linearLayout_there_is_connection = this.findViewById(R.id.there_is_connection_to_the_server);
        linearLayout_there_is_no_connection.setVisibility(View.GONE);
        textView = this.findViewById(R.id.no_recommendations);
        textView.setVisibility(View.GONE);

        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refrech_recpmmendation);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        swipeLayout.setRefreshing(true);

        getAllRecommendationByCar = new GetAllRecommendationByCar(downloadDataRepository);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeLayout.setRefreshing(false);

                                    if (isNetworkConnected()){
                                        getAllRecommendationByCar.execute();
                                    }else {
                                        linearLayout_there_is_connection.setVisibility(View.GONE);
                                        linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                                    }



                                }
                            }
                ,3000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    new GetAllRecommendationByCar(downloadDataRepository).execute();


                }else {
                    linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                    linearLayout_there_is_connection.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"no connection to the server", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }




            }
        },2000);


    }


    public class GetAllRecommendationByCar extends AsyncTask<Void, Void, ArrayList<PredictedMaintenance>> {

        DownloadDataRepository downloadDataRepository;
        JSONArray recommendationResponse;
        ArrayList<PredictedMaintenance> predictedMaintenanceArrayList = new ArrayList<PredictedMaintenance>();
        Semaphore s = new Semaphore(0);


        public GetAllRecommendationByCar(DownloadDataRepository downloadDataRepository){
            this.downloadDataRepository = downloadDataRepository;
        }

        @Override
        protected ArrayList<PredictedMaintenance> doInBackground(Void... params) {

            downloadDataRepository.downloadPredictedMaintenanceByCar(car,new VolleyCallBack() {
                @Override
                public void onSuccess() {

                    recommendationResponse = downloadDataRepository.responseToReturn;

                    Log.d("///////////////////////////////////id",String.valueOf(recommendationResponse.length()));

                    for (int i =0;i<recommendationResponse.length();i++){
                        try {
                            PredictedMaintenance predictedMaintenance = new PredictedMaintenance();
                            JSONObject jsonObject = (JSONObject) recommendationResponse.get(i);
                            JSONObject jsonObject_car = (JSONObject) jsonObject.get("car");
                            predictedMaintenance.setId_car(jsonObject_car.getString("id"));
                            predictedMaintenance.setDate(jsonObject.getLong("date"));
                            predictedMaintenance.setPredictedMaintenance(jsonObject.getString("predictedMaintenance"));
                            predictedMaintenanceArrayList.add(predictedMaintenance);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            s.release();
                        }


                    }
                    s.release();



                }
            });


            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return predictedMaintenanceArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<PredictedMaintenance> result) {
            super.onPostExecute(result);

            swipeLayout.setRefreshing(false);



            predictedMaintenanceArrayListGlobal = result;
            if (predictedMaintenanceArrayList.size()==0){
                textView.setVisibility(View.VISIBLE);
                linearLayout_there_is_connection.setVisibility(View.GONE);
            }else {
                adapterItemRecommendation = new AdapterItemRecommendation(getApplicationContext(),predictedMaintenanceArrayListGlobal);

                listView.setAdapter(adapterItemRecommendation);
            }





        }
    }
}