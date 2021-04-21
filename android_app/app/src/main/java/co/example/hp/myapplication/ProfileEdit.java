package co.example.hp.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.classes.User_Info;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProfileEdit extends AppCompatActivity {
    private EditText firstName,lastName,email;
    private DatabaseHelper databaseHelper;
    private String email_change,firstnameChange,lastnameChange;
    private LoginRepository loginRepository;
    private String password_entred;
    private LinearLayout linearLayout;
    private User_Info user_info;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginRepository = new LoginRepository(null);



        databaseHelper = new DatabaseHelper(this);

        linearLayout = this.findViewById(R.id.progress_profile_edit);

        firstName = this.findViewById(R.id.textview_profile_firstname_edit);
        lastName = this.findViewById(R.id.textview_profile_lastname_edit);
        email = this.findViewById(R.id.textview_profile_email_edit);


        user_info = databaseHelper.getUserLogedIn();
        id = databaseHelper.getUserLogedInId();


        firstnameChange = user_info.getFirstname().toString();
        lastnameChange = user_info.getLastname().toString();
        email_change = user_info.getEmail().toString();


        firstName.setText(user_info.getFirstname());
        lastName.setText(user_info.getLastname());
        email.setText(user_info.getEmail());




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_updates, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_updates:
                if (firstnameChange.equals(firstName.getText().toString())  && lastnameChange.equals(lastName.getText().toString())  && email_change.equals(email.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Nothing has changed",Toast.LENGTH_LONG).show();
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                    if (!email_change.equals(email.getText().toString())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Enter your MyCAR password");

                        final EditText input = new EditText(this);

                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                password_entred = input.getText().toString();
                                if (isValid(password_entred)){
                                    loginRepository.updateUserEmail(email.getText().toString(), password_entred, SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                                        @Override
                                        public void onSuccess() {

                                            if (!firstnameChange.equals(firstName.getText().toString())  && !lastnameChange.equals(lastName.getText().toString())){
                                                loginRepository.updateUser(firstName.getText().toString(), lastName.getText().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        linearLayout.setVisibility(View.GONE);
                                                        databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                                        Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });

                                            }else if (!firstnameChange.equals(firstName.getText().toString())){
                                                loginRepository.updateUser(firstName.getText().toString(), null, SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {

                                                        linearLayout.setVisibility(View.GONE);
                                                        databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                                        Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });

                                            }else if (!lastnameChange.equals(lastName.getText().toString())){
                                                loginRepository.updateUser(null, lastName.getText().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {

                                                        linearLayout.setVisibility(View.GONE);
                                                        databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                                        Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });

                                            }
                                            else {
                                                linearLayout.setVisibility(View.GONE);
                                                databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                                Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                linearLayout.setVisibility(View.GONE);
                                            }

                                        }
                                    });
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();


                    }else if (!firstnameChange.equals(firstName.getText().toString())  && !lastnameChange.equals(lastName.getText().toString())){
                        loginRepository.updateUser(firstName.getText().toString(), lastName.getText().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                                linearLayout.setVisibility(View.GONE);
                                databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }else if (!firstnameChange.equals(firstName.getText().toString())){
                        loginRepository.updateUser(firstName.getText().toString(), null, SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                                linearLayout.setVisibility(View.GONE);
                                databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        });

                    }else if (!lastnameChange.equals(lastName.getText().toString())){
                        loginRepository.updateUser(null, lastName.getText().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                            @Override
                            public void onSuccess() {

                                linearLayout.setVisibility(View.GONE);
                                databaseHelper.updateUserEmail(id,firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                                Intent intent = new Intent(ProfileEdit.this,Profile_details.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isValid(String password){
        if (password.length()>6){
            return true;
        }else return false;
    }





}