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
import android.widget.Toast;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.database.DatabaseHelper;

public class TabCarsFragment extends Fragment {

    private ListView listView;
    private LinearLayout linearLayout;
    private ArrayList<Car> carArrayList;
    private AdapterItemsCars customAdapter;
    private DatabaseHelper databaseHelper;
    private TextView textView;
    private Car car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cars, container, false);



        listView = (ListView) view.findViewById(R.id.lv_final);
        linearLayout = (LinearLayout) view.findViewById(R.id.there_is_cars);
        textView = (TextView) view.findViewById(R.id.there_is_no_car);




        databaseHelper = new DatabaseHelper(getContext());

        int id_user = databaseHelper.getUserLogedInId();

        if(databaseHelper.thereIsNoCar(id_user)==Boolean.TRUE){
            textView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);

        }else {
            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);

            GetCarsList getCarsList = new GetCarsList(databaseHelper);
            getCarsList.execute();

        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                car =(Car) carArrayList.get(position);


                Intent intent = new Intent(getContext(), Main2Activity.class);
                intent.putExtra("car", car);
                startActivity(intent);




            }
        });




        return view;

    }


    public class GetCarsList extends AsyncTask<Void, Void, AdapterItemsCars> {
        DatabaseHelper databaseHelper;

        public GetCarsList(DatabaseHelper databaseHelper){
            this.databaseHelper = databaseHelper;
        }

        @Override
        protected AdapterItemsCars doInBackground(Void... params) {

            ArrayList<Car> b = databaseHelper.getAllCars(databaseHelper.getUserLogedInId());


            customAdapter = new AdapterItemsCars(getContext(),b);

            carArrayList = b;

            return customAdapter;
        }

        @Override
        protected void onPostExecute(AdapterItemsCars result) {
            super.onPostExecute(result);

            customAdapter = result;



            listView.setAdapter(customAdapter);


        }
    }

}
