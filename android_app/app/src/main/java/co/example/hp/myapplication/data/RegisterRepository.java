package co.example.hp.myapplication.data;

import android.os.Looper;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.classes.RegistredUser;
import co.example.hp.myapplication.rest.MyJsonObjectRequest;
import co.example.hp.myapplication.rest.VolleySingleton;

import static co.example.hp.myapplication.LoginActivity.API_BASE;
import static co.example.hp.myapplication.LoginActivity.TAG;
import static java.lang.Thread.currentThread;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private LoginDataSource dataSource;

    private RegistredUser user = null;

    private MutableLiveData<Result> result = new MutableLiveData<>();
    private final Map<String, String> headers = new HashMap<>();

    private RegisterRepository(LoginDataSource dataSource) {this.dataSource = dataSource;}



    private void setRegistredUser(RegistredUser user) {
        this.user = user;
    }

    public static RegisterRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            synchronized (LoginRepository.class) {
                instance = new RegisterRepository(dataSource);
            }
        }
        return instance;
    }

    private void checkThread() {
        Log.d(TAG, Looper.myLooper()==Looper.getMainLooper() ? "On UI thread" : "On thread: "+currentThread().getName());
    }

    public LiveData<Result> getResult() {
        return result;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }




    public void register(final String first_name,final String last_name,String email, String password) {

        final String url = API_BASE+"auth/users/";


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("first_name", first_name);
            jsonObject.put("last_name", last_name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        String username = response.optString("email");
                        System.out.println(username);
                        if (!username.isEmpty()) {
                            Result<RegistredUser> res = dataSource.register(username);
                            if (res instanceof Result.Success) {
                                setRegistredUser(((Result.Success<RegistredUser>) res).getData());
                                result.setValue(res);
                            } else {
                                result.setValue(new Result.Error(new IOException("Error register  1")));
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "register didn't work", error);
                        result.setValue(new Result.Error(error));
                    }
                });
        myJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }


}
