package co.example.hp.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.FilterReparationClass;
import co.example.hp.myapplication.classes.FilterVerificationClass;
import co.example.hp.myapplication.classes.Repare;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class ReparationFragment extends Fragment {
    private FloatingActionButton addrepare;
    private Car car;
    private ListView listView;
    private ArrayList<Repare> reparesArrayList;
    private AdapterItemReparation customAdapter;
    private FilterReparationClass filterReparationClass;
    private TextView textView;



    public ReparationFragment(){
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_reparation,container,false);

        car = (Car) getArguments().getSerializable("carkey");

        //Toast.makeText(getActivity(),String.valueOf(car.getId()),Toast.LENGTH_LONG).show();

        if (getArguments().containsKey("filter_rep")){
            filterReparationClass = (FilterReparationClass) getArguments().getSerializable("filter_rep");
            car = filterReparationClass.getCar();
            // filter on//


        }else {
            String whatRepared = "";

            filterReparationClass = new FilterReparationClass(car,"oldest",null,null,null,null,null,0,whatRepared);

        }

        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.lv_reparation);

        textView = view.findViewById(R.id.there_is_no_reparation);

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());




        GetFilteredReparation getFilteredReparation = new GetFilteredReparation(databaseHelper,filterReparationClass);
        getFilteredReparation.execute();

        /*
        reparesArrayList = databaseHelper.getAllReparations(car.getId());

        customAdapter = new AdapterItemReparation(getContext(),reparesArrayList);
        listView.setAdapter(customAdapter);

         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getActivity(),ReparationSelected.class);
                intent.putExtra("repare",reparesArrayList.get(position));
                intent.putExtra("carkey",car);
                getActivity().startActivity(intent);
            }
        });

        addrepare = view.findViewById(R.id.add_reparation_main);
        addrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddReparation.class);
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
                Intent intent = new Intent(getContext(),FilterReparation.class);
                intent.putExtra("filter_rep",filterReparationClass);
                startActivity(intent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }




    public class GetFilteredReparation extends AsyncTask<Void, Void, ArrayList<Repare>> {
        DatabaseHelper databaseHelper;
        FilterReparationClass filterReparationClass;

        public GetFilteredReparation(DatabaseHelper databaseHelper,FilterReparationClass filterReparationClass){
            this.databaseHelper = databaseHelper;
            this.filterReparationClass = filterReparationClass;
        }

        @Override
        protected ArrayList<Repare> doInBackground(Void... params) {

            ArrayList<Repare> b = databaseHelper.filterReparations(filterReparationClass);

            return b;
        }

        @Override
        protected void onPostExecute(ArrayList<Repare> result) {
            super.onPostExecute(result);

            if (result.size()==0){
                textView.setVisibility(View.VISIBLE);
            }

            customAdapter = new AdapterItemReparation(getContext(),result);
            reparesArrayList = result;
            listView.setAdapter(customAdapter);

        }
    }




}
