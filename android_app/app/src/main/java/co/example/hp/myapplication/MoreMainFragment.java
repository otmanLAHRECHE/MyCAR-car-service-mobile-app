package co.example.hp.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.database.DatabaseHelper;

public class MoreMainFragment extends Fragment {
    private TextView fn_ln,email;
    private Button viewButton,logout;
    private DatabaseHelper databaseHelper;
    private CardView sync,contact_us,more_about_app,trade,settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_main, container, false);






        databaseHelper = new DatabaseHelper(getContext());

        fn_ln = view.findViewById(R.id.textview_firstname_lastname_more);
        email = view.findViewById(R.id.textview_email_more);
        viewButton = view.findViewById(R.id.view_profile_more);
        logout = view.findViewById(R.id.profile_logout);
        sync = (CardView) view.findViewById(R.id.more_sync_data);
        settings = (CardView) view.findViewById(R.id.more_settings);
        trade = (CardView) view.findViewById(R.id.more_trade);
        contact_us = (CardView) view.findViewById(R.id.more_contact);
        more_about_app = (CardView) view.findViewById(R.id.more_more_about_app);

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = LoginActivity.API_BASE+"contact/";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        more_about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = LoginActivity.API_BASE+"my_car/";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        trade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MycarTrade.class);
                startActivity(intent);
            }
        });

        fn_ln.setText(databaseHelper.getUserLogedIn().getFirstname());
        email.setText(databaseHelper.getUserLogedIn().getEmail());

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Profile_details.class);
                startActivity(intent);
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SyncDataActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SettingsGlobalActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.d("/////////////////////////////////////////////////////////////delete_auth","ok");
                        Log.d("/////////////////////////////////////////////////////////////auth",String.valueOf(SplashActivity.sharedPreferences.getString("authtoken","empty")));

                        /*
                        Intent intent = new Intent();
                        intent.setClass(getActivity(),LoginActivity.class);
                        getActivity().startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().finish();

                         */
                        getActivity().finish();
                        System.exit(0);
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

            }
        });




        return view;
    }
}
