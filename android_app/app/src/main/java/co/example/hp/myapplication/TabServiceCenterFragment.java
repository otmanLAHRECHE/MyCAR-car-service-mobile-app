package co.example.hp.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;

public class TabServiceCenterFragment extends Fragment {

    private ListView listView;
    private ArrayList<Service_center> serviceCenterArrayList;
    private AdapterItemServiceCenter customAdapter;
    private LinearLayout linearLayout;
    private TextView textView;
    private int id_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_center_home, container, false);






        textView = view.findViewById(R.id.there_is_no_service_center);
        linearLayout = view.findViewById(R.id.there_is_service_center);


        listView = (ListView) view.findViewById(R.id.lv_service_centers_home);

        final DatabaseHelper databaseHelper = new DatabaseHelper(getContext());


        Log.d("////////////////////////////////////////////wait","here");

        id_user = databaseHelper.getUserLogedInId();

        if(databaseHelper.thereIsNoSereviceCenter(id_user)==Boolean.TRUE){
            textView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);

        }else {
            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            Log.d("//////////////////////async_service_centers","ok");

            GetAllServiceCenters getAllServiceCenters = new GetAllServiceCenters(databaseHelper);
            getAllServiceCenters.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //getAllServiceCenters.execute();

        }




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(getActivity(),ServiceCenterSelected.class);
                intent.putExtra("servicekey",serviceCenterArrayList.get(position));
                getActivity().startActivity(intent);

            }
        });


        return view;
    }




    public class GetAllServiceCenters extends AsyncTask<Void, Void, ArrayList<Service_center>> {
        DatabaseHelper databaseHelper;

        public GetAllServiceCenters(DatabaseHelper databaseHelper){
            this.databaseHelper = databaseHelper;
        }

        @Override
        protected ArrayList<Service_center> doInBackground(Void... params) {

            ArrayList<Service_center> b = databaseHelper.getAllServiceCenters(databaseHelper.getUserLogedInId());



            return b;
        }

        @Override
        protected void onPostExecute(ArrayList<Service_center> result) {
            super.onPostExecute(result);

            databaseHelper.close();
            customAdapter = new AdapterItemServiceCenter(getContext(),result);
            serviceCenterArrayList = result;
            listView.setAdapter(customAdapter);




        }
    }


}
