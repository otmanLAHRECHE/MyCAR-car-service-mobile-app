package co.example.hp.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getbase.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class VerificationFragment extends Fragment {
    private FloatingActionButton addverf;
    private Car car;
    private ListView listView;
    private ArrayList<Verification> verificationsArrayList;
    private AdapterItemVerification customAdapter;
    private FilterVerificationClass filterVerificationClass;
    private TextView textView;


    public VerificationFragment(){
        setHasOptionsMenu(true);
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_verification,container,false);

        car = (Car) getArguments().getSerializable("carkey");


        if (getArguments().containsKey("filter_verf")){
            filterVerificationClass = (FilterVerificationClass) getArguments().getSerializable("filter_verf");
            car = filterVerificationClass.getCar();
            // filter on//


        }else {
            States states = new States();
            states.setEngineState("all");
            states.setTiresState("all");
            states.setLightsState("all");
            states.setAirConditioningState("all");

            filterVerificationClass = new FilterVerificationClass(car,"oldest",null,null,null,null,null,0,false,null,states);

        }

        setHasOptionsMenu(true);


        listView = (ListView) view.findViewById(R.id.lv_verification);

        textView = view.findViewById(R.id.there_is_no_verification);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());


        GetFilteredVerification getFilteredVerification = new GetFilteredVerification(databaseHelper,filterVerificationClass);
        getFilteredVerification.execute();
       //verificationsArrayList = databaseHelper.getAllVerifications(car.getId());
        /*
       verificationsArrayList = databaseHelper.filterVerification(filterVerificationClass);

        customAdapter = new AdapterItemVerification(getContext(),verificationsArrayList);
        listView.setAdapter(customAdapter);

         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),VerificationSelected.class);
                intent.putExtra("verif",verificationsArrayList.get(position));
                intent.putExtra("carkey",car);
                getActivity().startActivity(intent);
            }
        });

        addverf = view.findViewById(R.id.addverification);
        addverf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddVerification.class);
                intent.putExtra("car",car);
                getActivity().startActivity(intent);



            }
        });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.filter_data:
                Intent intent = new Intent(getContext(),FilterVerification.class);
                intent.putExtra("filter_verf",filterVerificationClass);
                startActivity(intent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    public class GetFilteredVerification extends AsyncTask<Void, Void, ArrayList<Verification>> {
        DatabaseHelper databaseHelper;
        FilterVerificationClass filterVerificationClass;

        public GetFilteredVerification(DatabaseHelper databaseHelper,FilterVerificationClass filterVerificationClass){
            this.databaseHelper = databaseHelper;
            this.filterVerificationClass = filterVerificationClass;
        }

        @Override
        protected ArrayList<Verification> doInBackground(Void... params) {

            ArrayList<Verification> b = databaseHelper.filterVerification(filterVerificationClass);

            return b;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(ArrayList<Verification> result) {
            super.onPostExecute(result);

            if (result.size()==0){
                textView.setVisibility(View.VISIBLE);
            }

            customAdapter = new AdapterItemVerification(getContext(),result);
            verificationsArrayList = result;
            listView.setAdapter(customAdapter);

        }
    }
}
