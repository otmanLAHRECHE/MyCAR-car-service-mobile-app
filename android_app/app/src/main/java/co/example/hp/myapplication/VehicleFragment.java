package co.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.database.DatabaseHelper;

public class VehicleFragment extends Fragment {
    private TextView company,model,serial,registrnum,car_year,car_type;
    private Button cardelete,carUpdate;
    private Car car;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View newview = inflater.inflate(R.layout.fragment_vehicle,container,false);
        car = (Car) getArguments().getSerializable("carkey");


        final DatabaseHelper databaseHelper = new DatabaseHelper(this.getContext());

        ImageView imglogo = (ImageView) newview.findViewById(R.id.vehicleimage);

        switch (car.getCompany()){
            case "chevrolet":
                imglogo.setImageResource(R.drawable.chevrolet);
                break;
            case "mitsubishi":
                imglogo.setImageResource(R.drawable.mitsubishi);
                break;
            case "landrover":
                imglogo.setImageResource(R.drawable.landrover);
                break;
            case "audi":
                imglogo.setImageResource(R.drawable.audi);
                break;
            case "bmw":
                imglogo.setImageResource(R.drawable.bmw);
                break;
            case "cadillac":
                imglogo.setImageResource(R.drawable.cadillac);
                break;
            case "ford":
                imglogo.setImageResource(R.drawable.ford);
                break;
            case "gmc":
                imglogo.setImageResource(R.drawable.gmc);
                break;
            case "honda":
                imglogo.setImageResource(R.drawable.honda);
                break;
            case "hyundai":
                imglogo.setImageResource(R.drawable.hyundai);
                break;
            case "kia":
                imglogo.setImageResource(R.drawable.kia);
                break;
            case "lexus":
                imglogo.setImageResource(R.drawable.lexus);
                break;
            case "fiat":
                imglogo.setImageResource(R.drawable.fiat);
                break;
            case "mazda":
                imglogo.setImageResource(R.drawable.mazda);
                break;
            case "mercedesbenz":
                imglogo.setImageResource(R.drawable.mercedesbenz);
                break;
            case "minicoper":
                imglogo.setImageResource(R.drawable.minicooper);
                break;
            case "nissan":
                imglogo.setImageResource(R.drawable.nissancar);
                break;
            case "suzuki":
                imglogo.setImageResource(R.drawable.suzuki);
                break;
            case "toyota":
                imglogo.setImageResource(R.drawable.toyota);
                break;
            case "volkswagen":
                imglogo.setImageResource(R.drawable.volkswagen);
                break;
            case "peugeot":
                imglogo.setImageResource(R.drawable.peugeot);
                break;
            case "renault":
                imglogo.setImageResource(R.drawable.renault);
                break;
        }

        company = (TextView) newview.findViewById(R.id.vehiclecompany);
        company.setText(car.getCompany());
        model = (TextView) newview.findViewById(R.id.vehiclemodel);
        model.setText(car.getModel());
        serial = (TextView) newview.findViewById(R.id.vehicleserial);
        serial.setText(car.getSerial());
        registrnum = (TextView) newview.findViewById(R.id.vehicleregistr);
        int reg = car.getRegistration();
        registrnum.setText(String.valueOf(reg));

        car_year = (TextView) newview.findViewById(R.id.vehicle_car_year);
        car_type = (TextView) newview.findViewById(R.id.vehicle_car_type);

        car_year.setText(String.valueOf(car.getYear()));
        car_type.setText(car.getCar_type());

        cardelete = (Button) newview.findViewById(R.id.deletecar);
        carUpdate = (Button) newview.findViewById(R.id.updatecar);

        cardelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deletealert = new AlertDialog.Builder(getActivity());
                deletealert.setTitle("Confirm delete!!!");
                deletealert.setIcon(R.drawable.delete);
                deletealert.setMessage("If you delete you will lose all information about this car,are you sure? ");
                deletealert.setCancelable(false);

                deletealert.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteCar(car.getId());
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),MainActivity.class);
                        getActivity().startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().finish();
                    }
                });
                deletealert.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"delete canceled....",Toast.LENGTH_LONG).show();
                    }
                });


                AlertDialog dialog = deletealert.create();
                dialog.show();







            }
        });

        carUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(),Update_car.class);
                intent.putExtra("cartoupdate",car);
                getActivity().startActivity(intent);


            }
        });



        return newview;
    }
}
