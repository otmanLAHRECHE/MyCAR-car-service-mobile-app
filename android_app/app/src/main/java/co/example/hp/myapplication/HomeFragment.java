package co.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.getbase.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.database.DatabaseHelper;

public class HomeFragment extends Fragment {
    private TextView odo,date,serviceCenter,nextodo,info;
    private FloatingActionButton addverification,addreparation;
    private DatabaseHelper databaseHelper;
    private CardView cardLast,cardOil,car_recommendation,car_statistics;
    private LinearLayout verification_details;
    private Verification verification;
    private Car car;



    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.fragment_home,container,false);






        car = (Car) getArguments().getSerializable("carkey");

        car_recommendation = view.findViewById(R.id.car_recommendation);
        car_statistics = view.findViewById(R.id.car_stats);


         addverification = view.findViewById(R.id.add_verif_home);
         addreparation = view.findViewById(R.id.add_reparation_home);

         verification_details = view.findViewById(R.id.fragment_home_verification_details);

         addverification.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(),AddVerification.class);
                 intent.putExtra("car",car);
                 getActivity().startActivity(intent);

             }
         });



         addreparation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(),AddReparation.class);
                 intent.putExtra("car",car);
                 getActivity().startActivity(intent);
             }
         });



        odo = view.findViewById(R.id.odo_home);
        date = view.findViewById(R.id.date_home);
        serviceCenter = view.findViewById(R.id.service_home);
        nextodo = view.findViewById(R.id.nextodo_home);
        info = view.findViewById(R.id.info_last_home);
        cardLast = view.findViewById(R.id.last_verf_home);
        cardOil = view.findViewById(R.id.oil_home);

        car_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RecommendationByCarActivity.class);
                intent.putExtra("car",car);
                startActivity(intent);
            }
        });

        car_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),StatisticsCarActivity.class);
                intent.putExtra("car",car);
                startActivity(intent);
            }
        });


        databaseHelper = new DatabaseHelper(getContext());

        verification = databaseHelper.getLastVerification(car.getId());

        verification_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),VerificationSelected.class);
                intent.putExtra("verif",verification);
                intent.putExtra("carkey",car);
                startActivity(intent);

            }
        });

        Boolean b = databaseHelper.verificationIsEmpty(car.getId());

        if (b == Boolean.TRUE){
            cardOil.setVisibility(View.GONE);
            cardLast.setVisibility(View.GONE);
            info.setVisibility(View.GONE);

        }else {
            cardOil.setVisibility(View.GONE);

            odo.setText(String.valueOf(verification.getOdometer()));

            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(verification.getDate());

            SimpleDateFormat formatter = new SimpleDateFormat("EEE,dd MMM yyyy");

            date.setText(formatter.format(calendar.getTime()));

            Service_center service_center = (Service_center) databaseHelper.getServiceCenterSelected(verification.getId());

            serviceCenter.setText(service_center.getName());

            Boolean b2 = databaseHelper.therIsNoChangeOil(car.getId());
            if (b2 == Boolean.TRUE){

            }else {
                cardOil.setVisibility(View.VISIBLE);
                Engine_oil engine_oil = databaseHelper.lastOilChange(car.getId());
                nextodo.setText(String.valueOf(engine_oil.getNextOdometer()));
            }







        }






        return view;
    }
}
