package co.example.hp.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.data.DownloadDataRepository;
import co.example.hp.myapplication.database.DatabaseHelper;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterItemsCars extends BaseAdapter {
    private Context context;
    private ArrayList<Car> carArrayList;
    private Car s;


    private DatabaseHelper databaseHelper;


    public AdapterItemsCars(Context context, ArrayList<Car> carArrayList) {

        this.context = context;
        this.carArrayList = carArrayList;
    }



    @Override
    public int getCount() {
        return carArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return carArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.layout_car_item_final,null);

        databaseHelper = new DatabaseHelper(context);

        s = carArrayList.get(position);


        ImageView imglogo = (ImageView) newView.findViewById(R.id.list_image_final);



        switch (s.getCompany()){
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

        TextView tvcompany = (TextView)newView.findViewById(R.id.company_final);
        tvcompany.setText(s.getCompany());
        TextView tvmodel = (TextView)newView.findViewById(R.id.model_final);
        tvmodel.setText(s.getModel());


        return newView;

    }



}
