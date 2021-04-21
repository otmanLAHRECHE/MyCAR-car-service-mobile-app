package co.example.hp.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class AdapterItemReparation extends BaseAdapter {
    private Context context;
    private ArrayList<Repare> repareArrayList;
    private DatabaseHelper databaseHelper;

    public AdapterItemReparation(Context context, ArrayList<Repare> repareArrayList){
        this.context = context;
        this.repareArrayList = repareArrayList;

    }


    @Override
    public int getCount() {
        return repareArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return repareArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.layout_reparation_item,null);

        databaseHelper = new DatabaseHelper(context);

        final Repare repare = repareArrayList.get(position);

        TextView tvdate = (TextView) newView.findViewById(R.id.date_repare);


        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(repare.getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");


        tvdate.setText(formatter.format(calendar.getTime()));


        TextView tvodoverif= (TextView) newView.findViewById(R.id.odometer_repare);
        double nbr = repare.getOdometer();
        tvodoverif.setText(String.valueOf(nbr));


        Service_center service_center = new Service_center();
        service_center = (Service_center) databaseHelper.getServiceCenterSelectedFromReparation(repare.getId());

        TextView tvservicewhere = (TextView) newView.findViewById(R.id.servicecenterwhere_repare);
        tvservicewhere.setText("service center: "+service_center.getName().toString());


        String repared = "";

        String f ="";
        f = repare.getWhatRepared();
        Log.d("/////////////////////////repare",String.valueOf(repare.getWhatRepared()));

        if (f.contains("Engine")){
            repared = repared + "engine,";
        }
        if (f.contains("Clim")){
            repared = repared + "clim,";
        }
        if (f.contains("Suspension")){
            repared = repared + "suspension,";
        }
        if (f.contains("Structure/Paint")){
            repared = repared + "structure,";
        }

        TextView textView_details = newView.findViewById(R.id.item_reparation_details);

        textView_details.setText(repared);




        return newView;
    }
}
