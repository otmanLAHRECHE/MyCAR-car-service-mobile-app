package co.example.hp.myapplication.ui_register;

import android.util.Patterns;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import co.example.hp.myapplication.R;
import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.classes.RegistredUser;
import co.example.hp.myapplication.data.LoginDataSource;
import co.example.hp.myapplication.data.RegisterRepository;
import co.example.hp.myapplication.data.Result;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private LiveData<RegisterResult> registerResult;
    private RegisterRepository registerRepository;


    public RegisterViewModel() {
        super();
        registerRepository = RegisterRepository.getInstance(new LoginDataSource());
        registerResult = Transformations.map(registerRepository.getResult(), new Function<Result, RegisterResult>() {
            @Override
            public RegisterResult apply(Result res) {
                return res instanceof Result.Success ? new RegisterResult(new RegistredUserView(((Result.Success<RegistredUser>) res).getData().getUsername())) : new RegisterResult(R.string.login_failed);
            }
        });
    }



    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(final String firstname,final String lastname,final String email, final String password) {
        // can be launched in a separate asynchronous job

        registerRepository.register(firstname,lastname,email, password);
    }

    public void registerDataChanged(String firstname,String lastname,String email, String password) {
        if (!isFirstNameValid(firstname)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null,null,null));
        }
        if (!isLastNameValid(lastname)) {
            registerFormState.setValue(new RegisterFormState(null,R.string.invalid_username, null,null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null,null, R.string.invalid_password, null));
        }   else if (!isEmailAddrValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, null,null,R.string.invalid_email));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isFirstNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }
    private boolean isLastNameValid(String username) {
        if (username == null) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }

    private boolean isEmailAddrValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        } else {
            return !email.trim().isEmpty();
        }
    }



}
