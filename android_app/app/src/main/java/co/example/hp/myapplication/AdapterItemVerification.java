package co.example.hp.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class AdapterItemVerification extends BaseAdapter {
    private Context context;
    private ArrayList<Verification> verificationArrayList;
    private DatabaseHelper databaseHelper;

    public AdapterItemVerification(Context context, ArrayList<Verification> verificationArrayList) {
        this.context = context;
        this.verificationArrayList = verificationArrayList;
    }

    @Override
    public int getCount() {
        return verificationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return verificationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.layout_verification_item,null);

        databaseHelper = new DatabaseHelper(context);

        final Verification verification = verificationArrayList.get(position);

        TextView tvdate = (TextView) newView.findViewById(R.id.date_verf);


        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(verification.getDate());

        SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");


        tvdate.setText(formatter.format(calendar.getTime()));



        TextView tvodoverif= (TextView) newView.findViewById(R.id.odometerverif);
        double nbr = verification.getOdometer();

        tvodoverif.setText(String.valueOf(nbr));

        Service_center service_center = new Service_center();
        service_center = (Service_center) databaseHelper.getServiceCenterSelected(verification.getId());

        TextView tvservicewhere = (TextView) newView.findViewById(R.id.servicecenterwhere);
        tvservicewhere.setText("service center: "+service_center.getName().toString());


        TextView textViewDetails = (TextView) newView.findViewById(R.id.item_verification_details);

        String details = "";

        Changes changes = (Changes) databaseHelper.getChangeSelected(verification.getId());

        if(databaseHelper.therIsChangeOilInThisVerf(verification.getId())==true){
            details = "engine oil,";
        }

        if (changes.isOil_filter() == true){
                details = details + "oil filter,";
        }
        if (changes.isAir_filter() == true){

                details = details + "air filter,";
        }
        if (changes.isCabine_filter() == true){

                details = details + "cabin filter,";
        }
        if (changes.isBrakes() == true){

                details = details + "brakes,";
        }
        if (changes.isTransmission_oil()== true){

                details = details + "transmission oil,";
        }

        textViewDetails.setText(details);





        return newView;
    }
}
