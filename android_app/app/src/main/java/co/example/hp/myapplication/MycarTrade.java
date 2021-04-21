package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.classes.TradeHistory;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Semaphore;

public class MycarTrade extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Button selling,buying;
    private ListView listView_trades;
    private TextView no_trades;
    private SwipeRefreshLayout swipeLayout;
    private AdapterItemTrade adapterItemTrade;
    private LinearLayout linearLayout_there_is_no_connection,linearLayout_there_is_connection;
    private DownloadDataRepository downloadDataRepository;
    private GetTradeHistory getTradeHistory;


    private ArrayList<TradeHistory> tradeArrayListGlobal = new ArrayList<TradeHistory>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycar_trade);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buying = this.findViewById(R.id.my_car_trade_buying);
        selling = this.findViewById(R.id.my_car_trade_selling);
        listView_trades = this.findViewById(R.id.listview_mycar_trade_history);
        linearLayout_there_is_no_connection = this.findViewById(R.id.no_connection_to_the_server_trade);
        linearLayout_there_is_connection = this.findViewById(R.id.there_is_connection_to_the_server_trade);
        linearLayout_there_is_no_connection.setVisibility(View.GONE);
        no_trades = this.findViewById(R.id.no_trades);

        downloadDataRepository = DownloadDataRepository.getInstance(getApplicationContext(),SplashActivity.sharedPreferences.getString("authtoken", "empty"));



        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_refrech_trade);
        swipeLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        swipeLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        swipeLayout.setRefreshing(true);

        getTradeHistory = new GetTradeHistory(downloadDataRepository);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeLayout.setRefreshing(false);

                                    if (isNetworkConnected()){

                                        getTradeHistory.execute();

                                    }else {
                                        linearLayout_there_is_connection.setVisibility(View.GONE);
                                        linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                                    }



                                }
                            }
                ,3000);


        buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),BuyingCar.class);
                startActivity(intent);
            }
        });
        selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SellingCar.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trade_help, menu);

        return true;
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

    @Override
    public void onRefresh() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isNetworkConnected()){
                    linearLayout_there_is_no_connection.setVisibility(View.GONE);
                    new GetTradeHistory(downloadDataRepository).execute();


                }else {
                    linearLayout_there_is_no_connection.setVisibility(View.VISIBLE);
                    linearLayout_there_is_connection.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"no connection to the server", Toast.LENGTH_LONG).show();
                    swipeLayout.setRefreshing(false);
                }




            }
        },2000);

    }


    public class GetTradeHistory extends AsyncTask<Void, Void, ArrayList<TradeHistory>> {

        DownloadDataRepository downloadDataRepository;
        JSONArray tradeHistoryResponse;
        ArrayList<TradeHistory> tradeArrayList = new ArrayList<TradeHistory>();
        Semaphore s = new Semaphore(0);
        int user_key = 0;


        public GetTradeHistory(DownloadDataRepository downloadDataRepository){
            this.downloadDataRepository = downloadDataRepository;
        }

        @Override
        protected ArrayList<TradeHistory> doInBackground(Void... params) {

            downloadDataRepository.downloadBuyerKey(new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    try {
                        user_key = downloadDataRepository.responseToReturnObject.getInt("user_key");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    downloadDataRepository.downloadTradeHistory(new VolleyCallBack() {
                        @Override
                        public void onSuccess() {

                            tradeHistoryResponse = downloadDataRepository.responseToReturn;
                            if (tradeHistoryResponse.length()==0){
                                no_trades.setVisibility(View.VISIBLE);
                                listView_trades.setVisibility(View.GONE);
                            }else {
                                for (int i =0;i<tradeHistoryResponse.length();i++){
                                    try {
                                        TradeHistory tradeHistory = new TradeHistory();
                                        Car car = new Car();
                                        JSONObject jsonObject = (JSONObject) tradeHistoryResponse.get(i);
                                        JSONObject jsonObject_car = (JSONObject) jsonObject.get("car");
                                        car.setId(jsonObject_car.getString("id"));
                                        car.setCompany(jsonObject_car.getString("company"));
                                        car.setModel(jsonObject_car.getString("model"));
                                        car.setCar_type(jsonObject_car.getString("car_type"));

                                        tradeHistory.setCar(car);
                                        tradeHistory.setDate(jsonObject.getLong("date"));
                                        tradeHistory.setId_buyer(jsonObject.getInt("buyer"));
                                        if (user_key==tradeHistory.getId_buyer()){
                                            tradeHistory.setRole("buyer");
                                        }else {
                                            tradeHistory.setRole("seller");
                                        }

                                        tradeArrayList.add(tradeHistory);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        s.release();
                                    }
                                }
                            }

                            s.release();



                        }
                    });
                }
            });




            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return tradeArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<TradeHistory> result) {
            super.onPostExecute(result);

            swipeLayout.setRefreshing(false);



            tradeArrayListGlobal = result;

            adapterItemTrade = new AdapterItemTrade(getApplicationContext(),tradeArrayListGlobal);

            listView_trades.setAdapter(adapterItemTrade);



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
}