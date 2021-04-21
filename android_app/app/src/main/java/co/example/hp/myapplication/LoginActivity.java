package co.example.hp.myapplication;


import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import co.example.hp.myapplication.data.DataRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.rest.VolleySingleton;
import co.example.hp.myapplication.ui_login.LoggedInUserView;
import co.example.hp.myapplication.ui_login.LoginFormState;
import co.example.hp.myapplication.ui_login.LoginResult;
import co.example.hp.myapplication.ui_login.LoginViewModel;

public class LoginActivity extends AppCompatActivity {


    private LoginViewModel loginViewModel;
    private DatabaseHelper databaseHelper;





    public static final String TAG = "MyCAR", API_BASE = "http://192.168.43.66:8000/serverAPI/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Log.d("////////////////////////////////////init","true");


        VolleySingleton.getInstance(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        databaseHelper = new DatabaseHelper(this);


        //User_Info userInfo = databaseHelper.getFirstUser();
        //Toast.makeText(getApplicationContext(),userInfo.getFirstname()+"  "+userInfo.getEmail(), Toast.LENGTH_SHORT).show();
        /*if(databaseHelper.therIsUserIn()){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        /*
       CheckUser checkUser = new CheckUser(databaseHelper);
       checkUser.execute();

         */


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = (Button) findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView goToRegister = (TextView) findViewById(R.id.go_to_register_now);





        loginButton.setEnabled(true);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {

                   int id = databaseHelper.getUserToLogin(usernameEditText.getText().toString());




                    if (usernameEditText.getText().toString().isEmpty()){

                    }else {

                        if (databaseHelper.userExistLocaly(usernameEditText.getText().toString())){
                        saveUserState(id);
                        if (SplashActivity.sharedPreferences_FCM_token.getBoolean("FCM_token",false)==false){

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("register",true);
                            startActivity(intent);
                            finish();


                        }else {
                            updateUiWithUser(loginResult.getSuccess());
//                  Commenting following lines, as may return after logout.
                            setResult(Activity.RESULT_OK);
//                    Complete and destroy login activity once successful
                            finish();
                        }



                        }else {
                            createLocalUserIn("emp","emp",usernameEditText.getText().toString());
                            loadingProgressBar.setVisibility(View.VISIBLE);

                            updateUiWithNewUser();

                        }
                    }



                }
            }
        });





        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };


        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });




        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        VolleySingleton.getInstance(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        databaseHelper = new DatabaseHelper(this);

    }

    private void updateUiWithUser(LoggedInUserView model) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void saveUserState(int idUser) {
        databaseHelper.updateUserState(idUser,"nn");
        //Toast.makeText(getApplicationContext(), String.valueOf(idUser), Toast.LENGTH_SHORT).show();


    }

    private void createLocalUserIn(String fn,String ln,String e) {

        databaseHelper.registerUser(fn,ln,e,"nn");

    }

    private void updateUiWithNewUser() {


        Intent intent = new Intent(this, DownloadDataLoadingActivity.class);
        startActivity(intent);
        finish();
    }


    /*public class CheckUser extends AsyncTask<Void, Void, Boolean> {
        DatabaseHelper databaseHelper;

        public CheckUser(DatabaseHelper databaseHelper){
            this.databaseHelper = databaseHelper;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean b = databaseHelper.therIsUserIn();


            return b;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result){
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

     */
}
