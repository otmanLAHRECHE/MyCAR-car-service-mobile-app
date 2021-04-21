package co.example.hp.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.classes.Recommendation;
import co.example.hp.myapplication.classes.RecommendationItem;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterItemRecommendation extends BaseAdapter {
    private Context context;
    private ArrayList<PredictedMaintenance> predictedMaintenanceArrayList;
    private LinearLayout ll_brakes,ll_engin_oil,ll_battery,ll_lights,ll_tires,ll_trans,ll_wheel_alig,ll_mount;
    private CircleImageView imglogo;
    private TextView date,car_model;
    private DatabaseHelper databaseHelper;


    public AdapterItemRecommendation(Context context, ArrayList<PredictedMaintenance> predictedMaintenanceArrayList) {

        this.context = context;
        this.predictedMaintenanceArrayList = predictedMaintenanceArrayList;
    }


    @Override
    public int getCount() {
        return this.predictedMaintenanceArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.predictedMaintenanceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.item_recommendation,null);

        databaseHelper = new DatabaseHelper(newView.getContext());

        ll_brakes = newView.findViewById(R.id.recom_engine_brakes_all);
        ll_brakes.setVisibility(View.GONE);
        ll_engin_oil = newView.findViewById(R.id.recom_engine_oil_all);
        ll_engin_oil.setVisibility(View.GONE);
        ll_battery = newView.findViewById(R.id.recom_engine_battery_all);
        ll_battery.setVisibility(View.GONE);
        ll_lights = newView.findViewById(R.id.recom_lights_all);
        ll_lights.setVisibility(View.GONE);
        ll_mount = newView.findViewById(R.id.recom_mount_all);
        ll_mount.setVisibility(View.GONE);
        ll_tires = newView.findViewById(R.id.recom_tires_all);
        ll_tires.setVisibility(View.GONE);
        ll_wheel_alig = newView.findViewById(R.id.recom_wheel_algn_all);
        ll_wheel_alig.setVisibility(View.GONE);
        ll_trans = newView.findViewById(R.id.recom_trans_all);
        ll_trans.setVisibility(View.GONE);
        date = newView.findViewById(R.id.recom_date_all);
        car_model = newView.findViewById(R.id.recom_car_model_all);
        imglogo = newView.findViewById(R.id.recom_car_logo_all);

        Car car = databaseHelper.getSelectedCar(predictedMaintenanceArrayList.get(position).getId_car());
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

        calendar.setTimeInMillis(predictedMaintenanceArrayList.get(position).getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");

        date.setText(formatter.format(calendar.getTime()));

        car_model.setText(car.getModel());

        Recommendation recommendation = new Recommendation();
        recommendation = responseToRecommendation(predictedMaintenanceArrayList.get(position).getPredictedMaintenance());

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





        return newView;
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
