package co.example.hp.myapplication.ui_login;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import co.example.hp.myapplication.R;
import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.data.LoginDataSource;
import co.example.hp.myapplication.data.LoginRepository;
import co.example.hp.myapplication.data.Result;

public class LoginViewModel extends ViewModel {


    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LiveData<LoginResult> loginResult;
    private LoginRepository loginRepository;

    public LoginViewModel() {
        super();
        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loginResult = Transformations.map(loginRepository.getResult(), new Function<Result, LoginResult>() {
            @Override
            public LoginResult apply(Result res) {
                return res instanceof Result.Success ? new LoginResult(new LoggedInUserView(((Result.Success<LoggedInUser>) res).getData().getUsername())) : new LoginResult(R.string.login_failed);
            }
        });
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username, final String password) {
        // can be launched in a separate asynchronous job

        loginRepository.login(username, password);
    }



    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 4;
    }
}
