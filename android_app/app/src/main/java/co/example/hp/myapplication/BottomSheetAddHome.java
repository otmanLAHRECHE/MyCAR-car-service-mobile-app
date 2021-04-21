package co.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;

public class BottomSheetAddHome extends BottomSheetDialogFragment {

    LinearLayout linearLayoutAddCar,linearLayoutAddServiceCenter,linearLayoutAddScanCar;


    public BottomSheetAddHome() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bottomsheet_add, container, false);

        linearLayoutAddCar = view.findViewById(R.id.linearLayout_add_car);

        linearLayoutAddScanCar = view.findViewById(R.id.linearLayout_add_scan_car);

        linearLayoutAddServiceCenter = view.findViewById(R.id.linearLayout_add_service_center);


        linearLayoutAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCar.class);
                startActivity(intent);
            }
        });

        linearLayoutAddServiceCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddServiceCenter.class);
                startActivity(intent);
            }
        });



        
        return view;
    }
}
