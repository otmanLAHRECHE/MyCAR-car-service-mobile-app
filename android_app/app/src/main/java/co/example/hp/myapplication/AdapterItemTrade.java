package co.example.hp.myapplication;

import android.content.Context;
import android.graphics.Color;
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
import co.example.hp.myapplication.classes.TradeHistory;
import co.example.hp.myapplication.database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterItemTrade extends BaseAdapter {
    private Context context;
    private ArrayList<TradeHistory> tradeHistoryArrayList;
    private CircleImageView imglogo;
    private TextView date,car_model,role;
    private DatabaseHelper databaseHelper;

    public AdapterItemTrade(Context context, ArrayList<TradeHistory> tradeHistoryArrayList) {

        this.context = context;
        this.tradeHistoryArrayList = tradeHistoryArrayList;
    }

    @Override
    public int getCount() {
        return this.tradeHistoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tradeHistoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.item_trade_history,null);

        databaseHelper = new DatabaseHelper(newView.getContext());

        date = newView.findViewById(R.id.date_all_trade);
        car_model = newView.findViewById(R.id.car_model_all_trade);
        imglogo = newView.findViewById(R.id.car_logo_all_trade);
        role = newView.findViewById(R.id.trade_role);


        switch (tradeHistoryArrayList.get(position).getCar().getCompany()){
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

        calendar.setTimeInMillis(tradeHistoryArrayList.get(position).getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");

        date.setText(formatter.format(calendar.getTime()));

        car_model.setText(tradeHistoryArrayList.get(position).getCar().getModel());


        if (tradeHistoryArrayList.get(position).getRole().equals("buyer")){
            role.setText(R.string.Buying);
            role.setTextColor(Color.GREEN);
        }else {
            role.setText(R.string.Selling);
            role.setTextColor(Color.RED);
        }





        return newView;
    }
}
