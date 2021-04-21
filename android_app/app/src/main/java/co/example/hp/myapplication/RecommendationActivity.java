package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RecommendationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private AdapterItemRecommendation adapterItemRecommendation;
    private ArrayList<PredictedMaintenance> predictedMaintenanceArrayListGlobal;
    private GetAllRecommendation getAllRecommendation;
    private LinearLayout linearLayout_there_is_no_connection,linearLayout_there_is_connection;

    private DownloadDataRepository downloadDataRepository;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) this.findViewById(R.id.lv_recommendation);
        linearLayout_there_is_no_connection = this.findViewById(R.id.no_connection_to_the_server);
        linearLayout_there_is_connection = this.findViewById(R.id.there_is_connection_to_the_server);
        linearLayout_there_is_no_connection.setVisibility(View.GONE);

        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));

        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refrech_recpmmendation);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        swipeLayout.setRefreshing(true);

        getAllRecommendation = new GetAllRecommendation(downloadDataRepository);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);

                if (isNetworkConnected()){

                    getAllRecommendation.execute();




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

    public class GetAllRecommendation extends AsyncTask<Void, Void, ArrayList<PredictedMaintenance>> {

        DownloadDataRepository downloadDataRepository;
        JSONArray recommendationResponse;
        ArrayList<PredictedMaintenance> predictedMaintenanceArrayList = new ArrayList<PredictedMaintenance>();
        Semaphore s = new Semaphore(0);


        public GetAllRecommendation(DownloadDataRepository downloadDataRepository){
            this.downloadDataRepository = downloadDataRepository;
        }

        @Override
        protected ArrayList<PredictedMaintenance> doInBackground(Void... params) {

            downloadDataRepository.downloadAllPredictedMaintenance(new VolleyCallBack() {
                @Override
                public void onSuccess() {

                    recommendationResponse = downloadDataRepository.responseToReturn;

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

            adapterItemRecommendation = new AdapterItemRecommendation(getApplicationContext(),predictedMaintenanceArrayListGlobal);

            listView.setAdapter(adapterItemRecommendation);



        }
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
                    new GetAllRecommendation(downloadDataRepository).execute();


                }else {
                    linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                    linearLayout_there_is_connection.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"no connection to the server", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }




            }
        },2000);


    }



}