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

import co.example.hp.myapplication.classes.PredictedMaintenance;
import co.example.hp.myapplication.classes.Recommendation;
import co.example.hp.myapplication.classes.RecommendationItem;

public class AdapterItemRecommendationByCar extends BaseAdapter {
    private Context context;
    private ArrayList<PredictedMaintenance> predictedMaintenanceArrayList;
    private LinearLayout ll_brakes,ll_engin_oil,ll_battery,ll_lights,ll_tires,ll_trans,ll_wheel_alig,ll_mount;
    private TextView date;


    public AdapterItemRecommendationByCar(Context context, ArrayList<PredictedMaintenance> predictedMaintenanceArrayList) {

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
        final View newView = inflater.inflate(R.layout.item_recommendation_by_car,null);

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

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(predictedMaintenanceArrayList.get(position).getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");

        date.setText(formatter.format(calendar.getTime()));


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
