package co.example.hp.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import co.example.hp.myapplication.classes.User_Info;
import co.example.hp.myapplication.data.LoginDataSource;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.rest.VolleySingleton;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {
    private TextView username,email;
    private DatabaseHelper databaseHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet_profile, container, false);


        databaseHelper = new DatabaseHelper(this.getContext());


        NavigationView navigationView = view.findViewById(R.id.navigation_view_bottom);

        username = (TextView) view.findViewById(R.id.textView_username);
        email = (TextView) view.findViewById(R.id.textView_email);

        User_Info user_info = new User_Info(databaseHelper.getUserLogedIn().getFirstname(),databaseHelper.getUserLogedIn().getEmail());

        //Toast.makeText(view.getContext(), user_info.getFirstname()+""+user_info.getEmail()+"khgkhh", Toast.LENGTH_SHORT).show();
        Toast.makeText(view.getContext(),SplashActivity.sharedPreferences.getString("authtoken","empty") , Toast.LENGTH_SHORT).show();
        username.setText(user_info.getFirstname());

        email.setText(user_info.getEmail());

        VolleySingleton.getInstance(getContext());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav1_profile:
                        Toast.makeText(getContext(), "profile", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav2_logout:
                        AlertDialog.Builder deletealert = new AlertDialog.Builder(getActivity());
                        deletealert.setTitle("Confirm logout!!!");
                        deletealert.setIcon(R.drawable.logout);
                        deletealert.setMessage("Are you sure you want to logout? ");
                        deletealert.setCancelable(false);

                        deletealert.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                int id = databaseHelper.getUserLogedInId();
                                databaseHelper.updateUserState(id,"out");


                                LoginRepository.getInstance(null).logout(SplashActivity.sharedPreferences.getString("authtoken","empty"));
                                SplashActivity.sharedPreferences.edit().remove("authtoken").commit();



                                Intent intent = new Intent();
                                intent.setClass(getActivity(),LoginActivity.class);
                                getActivity().startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().finish();
                            }
                        });
                        deletealert.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // Toast.makeText(getActivity(),"delete canceled....",Toast.LENGTH_LONG).show();
                            }
                        });


                        AlertDialog dialog = deletealert.create();
                        dialog.show();
                        return true;



                }

                return true;
            }
        });





        return view;
    }

}
