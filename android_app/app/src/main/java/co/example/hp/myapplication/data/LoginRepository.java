package co.example.hp.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import co.example.hp.myapplication.LoginActivity;
import co.example.hp.myapplication.SplashActivity;
import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.rest.MyJsonObjectRequest;
import co.example.hp.myapplication.rest.VolleySingleton;

import static co.example.hp.myapplication.LoginActivity.API_BASE;
import static co.example.hp.myapplication.LoginActivity.TAG;
import static java.lang.Thread.currentThread;

public class LoginRepository {


    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    public JSONObject jsonObjectReturn ;



    private MutableLiveData<Result> result = new MutableLiveData<>();
    private final Map<String, String> headers = new HashMap<>();

    // private constructor : singleton access
    public LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            synchronized (LoginRepository.class) {
                instance = new LoginRepository(dataSource);
            }
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    private void checkThread() {
        Log.d(TAG, Looper.myLooper()==Looper.getMainLooper() ? "On UI thread" : "On thread: "+currentThread().getName());
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        headers.put("Authorization", "Token " + user.getAuthToken());
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    public LiveData<Result> getResult() {
        return result;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void login(final String username, String password) {
        // handle login

        final String url = API_BASE+"auth/token/login/";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", username);
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
                        String authToken = response.optString("auth_token");
                        if (!authToken.isEmpty()) {
                            Result<LoggedInUser> res = dataSource.login(username, authToken);
                            if (res instanceof Result.Success) {
                                setLoggedInUser(((Result.Success<LoggedInUser>) res).getData());
                                result.setValue(res);

                                SplashActivity.sharedPreferences.edit().putString("authtoken",authToken).commit();
                                Log.d("/////////////////////////////////////////////////auth_in_shared",SplashActivity.sharedPreferences.getString("authtoken","empty"));
                            } else {
                                result.setValue(new Result.Error(new IOException("Error logging in 1")));
                            }
                        } else {
                            result.setValue(new Result.Error(new IOException("Error logging in 2")));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "login didn't work", error);
                        result.setValue(new Result.Error(error));
                    }
                });
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }

    public void restoreLogin(final String username, final String authToken,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"auth/users/me/";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString()+"fnkehbfubfruhefuberhfbeurfhbeurfhbuebfeubfhueeeeeeeeeeeeeeeeeeeeeebfeufeubfeubfeubfuebfuebfffffffffffffffeubfeubfueeeeeeeeeeeeeefeubfeffffffffe");
                        checkThread();
                        jsonObjectReturn = response;
                        volleyCallBack.onSuccess();
                        /*if (response.optString("username").equals(username)) {
                            Result<LoggedInUser> res = dataSource.login(username, authToken);
                            if (res instanceof Result.Success) {
                                setLoggedInUser(((Result.Success<LoggedInUser>) res).getData());
                                result.setValue(res);
                            }
                        }*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "restore login didn't work", error);
                        jsonObjectReturn = null;
                        //result.setValue(new Result.Error(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }

    public void logout(final String authToken) {
        final String url = API_BASE+"auth/token/logout/";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, "logout response: "+response.toString());
                            dataSource.logout();
                            user = null;
                            headers.clear();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "logout didn't work", error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }


    public void updateUser(final String first_name,final String last_name, final String authToken,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"auth/users/me/";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        JSONObject jsonObject = new JSONObject();
        try {
            if (first_name != null){ jsonObject.put("first_name", first_name);}
            if (last_name != null){ jsonObject.put("last_name", last_name);}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.PATCH, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        checkThread();
                        jsonObjectReturn = response;
                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "update login didn't work", error);
                        jsonObjectReturn = null;
                        //result.setValue(new Result.Error(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }

    public void updateUserEmail(final String new_email,final String password, final String authToken,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"auth/users/set_email/";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_email",new_email);
            jsonObject.put("current_password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        checkThread();
                        jsonObjectReturn = response;
                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "update login didn't work", error);
                        jsonObjectReturn = null;
                        //result.setValue(new Result.Error(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {


                try {
                    String json = new String(
                            response.data,
                            "UTF-8"
                    );

                    if (json.length() == 0) {
                        return Response.success(
                                null,
                                HttpHeaderParser.parseCacheHeaders(response)
                        );
                    }
                    else {
                        return super.parseNetworkResponse(response);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }


            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }

    public void updateUserPassword(final String new_password,final String password, final String authToken,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"auth/users/set_password/";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token " + authToken);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("new_password",new_password);
            jsonObject.put("current_password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        checkThread();
                        jsonObjectReturn = response;
                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "update login didn't work", error);
                        jsonObjectReturn = null;
                        //result.setValue(new Result.Error(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {


                try {
                    String json = new String(
                            response.data,
                            "UTF-8"
                    );

                    if (json.length() == 0) {
                        return Response.success(
                                null,
                                HttpHeaderParser.parseCacheHeaders(response)
                        );
                    }
                    else {
                        return super.parseNetworkResponse(response);
                    }
                }
                catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }


            }
        }
        ;
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }


}
