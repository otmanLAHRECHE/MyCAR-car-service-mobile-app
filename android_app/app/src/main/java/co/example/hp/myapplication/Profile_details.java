package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.User_Info;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Profile_details extends AppCompatActivity {
    private TextView firstNameHeader,firstName,lastName,email;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);

        firstNameHeader = this.findViewById(R.id.first_name_header_details);
        firstName = this.findViewById(R.id.first_name_details);
        lastName = this.findViewById(R.id.last_name_details);
        email = this.findViewById(R.id.email_details);

        User_Info user_info = databaseHelper.getUserLogedIn();

        firstNameHeader.setText(user_info.getFirstname());
        firstName.setText(user_info.getFirstname());
        lastName.setText(user_info.getLastname());
        email.setText(user_info.getEmail());




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent1 = new Intent(Profile_details.this,MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
                return true;
            case R.id.edit_profile:
                Intent intent = new Intent(this,ProfileEdit.class);
                startActivity(intent);
                return true;
            case R.id.change_password_profile:
                Intent intent2 = new Intent(this,PasswordChangeActivity.class);
                startActivity(intent2);
                return true;
            case R.id.log_out_profile:
                AlertDialog.Builder deletealert = new AlertDialog.Builder(Profile_details.this);
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

                        /*
                        Intent intent = new Intent();
                        intent.setClass(Profile_details.this,LoginActivity.class);
                        Profile_details.this.startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Profile_details.this.finish();

                         */
                        finish();
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
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile_details.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("more","more");
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
}