package co.example.hp.myapplication;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;


public class AdapterMultipleServiceCenters extends BaseAdapter {
    private Context context;
    private ArrayList<Service_center> serviceCenterArrayList;
    private  SparseBooleanArray mSelectedItemsIds;

    public AdapterMultipleServiceCenters(Context context, ArrayList<Service_center> serviceCenterArrayList) {
        this.context = context;
        mSelectedItemsIds = new  SparseBooleanArray();
        this.serviceCenterArrayList = serviceCenterArrayList;
    }

    private DatabaseHelper databaseHelper;


    @Override
    public int getCount() {
        return serviceCenterArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceCenterArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        final View newView = inflater.inflate(R.layout.layout_service_center_item,null);

        databaseHelper = new DatabaseHelper(context);

        final Service_center sc = serviceCenterArrayList.get(position);

        TextView tvservicename = (TextView) newView.findViewById(R.id.servicename);
        tvservicename.setText(sc.getName());

        TextView tvservicephone = (TextView) newView.findViewById(R.id.servicenumber);
        int nbr = sc.getPhoneNumber();
        tvservicephone.setText(String.valueOf(nbr));

        TextView tvservicelocation = (TextView) newView.findViewById(R.id.servicelocation);
        tvservicelocation.setText(sc.getLocation());

        return newView;
    }



    // get List after update or delete

    public  ArrayList<Service_center> getMyList() {

        return serviceCenterArrayList;

    }



    public void  toggleSelection(int position) {

        selectView(position, !mSelectedItemsIds.get(position));

    }



    // Remove selection after unchecked

    public void  removeSelection() {

        mSelectedItemsIds = new  SparseBooleanArray();

        notifyDataSetChanged();

    }



    // Item checked on selection

    public void selectView(int position, boolean value) {

        if (value)

            mSelectedItemsIds.put(position,  value);

        else

            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();

    }



    // Get number of selected item

    public int  getSelectedCount() {

        return mSelectedItemsIds.size();

    }



    public  SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;

    }
}
