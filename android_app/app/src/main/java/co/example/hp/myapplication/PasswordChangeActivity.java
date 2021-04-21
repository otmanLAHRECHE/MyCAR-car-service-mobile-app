package co.example.hp.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.data.VolleyCallBack;
import co.example.hp.myapplication.database.DatabaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PasswordChangeActivity extends AppCompatActivity {
    private EditText oldPassword,newPassword;
    private LoginRepository loginRepository;
    private LinearLayout linearLayout;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginRepository = new LoginRepository(null);
        databaseHelper = new DatabaseHelper(this);
        linearLayout = this.findViewById(R.id.progress_password_change);

        oldPassword = this.findViewById(R.id.textview_password_change_old);
        newPassword = this.findViewById(R.id.textview_password_change_new);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_updates, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_updates:
                if (oldPassword.getText().toString().equals(newPassword.getText().toString()) || oldPassword.getText().toString().length()<6 || newPassword.getText().toString().length()<6){
                    Toast.makeText(getApplicationContext(),"there is a problem with your passwords",Toast.LENGTH_LONG).show();
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                    loginRepository.updateUserPassword(newPassword.getText().toString(), oldPassword.getText().toString(), SplashActivity.sharedPreferences.getString("authtoken", "empty"), new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            linearLayout.setVisibility(View.GONE);
                            Intent intent = new Intent(PasswordChangeActivity.this,Profile_details.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });


                }

        }

        return super.onOptionsItemSelected(item);
    }

}