package co.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.database.DatabaseHelper;

public class ServiceCenterFragment extends Fragment {
    private FloatingActionButton addservicecenter;
    private ListView listView;
    private ArrayList<Service_center> serviceCenterArrayList;
    private AdapterItemServiceCenter customAdapter;
    private Car car;
    private int id_user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_center, container, false);


        car = (Car) getArguments().getSerializable("carkey");

        listView = (ListView) view.findViewById(R.id.lv_service_centers);

        final DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        id_user = databaseHelper.getUserLogedInId();

        serviceCenterArrayList = databaseHelper.getAllServiceCenters(id_user);

        customAdapter = new AdapterItemServiceCenter(getContext(),serviceCenterArrayList);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent(getActivity(),ServiceCenterSelected.class);
                intent.putExtra("servicekey",serviceCenterArrayList.get(position));
                intent.putExtra("carkey",car);
                getActivity().startActivity(intent);
            }
        });

        addservicecenter = view.findViewById(R.id.addservicecenterforlist);


        addservicecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddServiceCenter.class);
                intent.putExtra("car",car);
                getActivity().startActivity(intent);
            }
        });



        return view;
    }
}
