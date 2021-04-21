package co.example.hp.myapplication;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;
import co.example.hp.myapplication.rest.VolleySingleton;
import co.example.hp.myapplication.ui_register.RegisterFormState;
import co.example.hp.myapplication.ui_register.RegisterResult;
import co.example.hp.myapplication.ui_register.RegisterViewModel;
import co.example.hp.myapplication.ui_register.RegistredUserView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gcm.client.GcmHelper;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        VolleySingleton.getInstance(this);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        databaseHelper = new DatabaseHelper(this);

        final EditText first_name = findViewById(R.id.first_name_reg);
        final EditText last_name = findViewById(R.id.last_name_reg);
        final EditText emailEditText = findViewById(R.id.username_reg);
        final EditText passwordEditText = findViewById(R.id.password_reg);
        final Button registerButton = (Button) findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading_reg);


        registerButton.setEnabled(true);

        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getFirstnameError() != null) {
                    first_name.setError(getString(registerFormState.getFirstnameError()));
                }
                if (registerFormState.getLastnameError() != null) {
                    last_name.setError(getString(registerFormState.getLastnameError()));
                }
                if (registerFormState.getEmaiError() != null) {
                    emailEditText.setError(getString(registerFormState.getEmaiError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    Toast.makeText(getApplicationContext(), "stack", Toast.LENGTH_SHORT).show();
                    return;

                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showRegisterFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    Toast.makeText(getApplicationContext(), first_name.getText().toString()+" "+last_name.getText().toString()+" "+emailEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                    createLocalUser(first_name.getText().toString(),last_name.getText().toString(),emailEditText.getText().toString());




                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("You need to verify your G-mail account");


                    builder.setNegativeButton("Ok", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateUiWithUser();
                            finish();
                            //dialog.cancel();
                        }
                    });
                    builder.show();
                    /*MaterialDialog mDialog = new MaterialDialog.Builder(getParent())
                            .setTitle("Email verification")
                            .setMessage("You need to access to your email and confirm the register")
                            .setCancelable(false)
                            .setPositiveButton("Go to G-mail", new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Intent mailClient = new Intent(Intent.ACTION_VIEW);
                                    mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
                                    startActivity(mailClient);
                                }
                            })
                            .setNegativeButton("Cancel", new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();

                    mDialog.show();

                    */

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
                registerViewModel.registerDataChanged(first_name.getText().toString(),last_name.getText().toString(),
                        emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        };


        first_name.addTextChangedListener(afterTextChangedListener);
        last_name.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.register(first_name.getText().toString(),last_name.getText().toString(),
                            emailEditText.getText().toString(),passwordEditText.getText().toString());
                }
                return false;
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(first_name.getText().toString(),last_name.getText().toString(),
                        emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });








    }


    private void updateUiWithUser() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void createLocalUser(String fn,String ln,String e) {

        databaseHelper.registerUser(fn,ln,e,"out");



    }
}
