package co.example.hp.myapplication.data;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.example.hp.myapplication.LoginActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.rest.MyJsonArrayRequest;
import co.example.hp.myapplication.rest.MyJsonObjectRequest;
import co.example.hp.myapplication.rest.VolleySingleton;

import static co.example.hp.myapplication.LoginActivity.API_BASE;
import static co.example.hp.myapplication.LoginActivity.TAG;
import static java.lang.Thread.currentThread;

public class DownloadDataRepository {

    private static volatile DownloadDataRepository instance;

    private final Map<String, String> headers = new HashMap<>();

    public JSONArray responseToReturn;
    public JSONObject responseToReturnObject;

    public Map<String, String> getHeaders() {
        return headers;
    }

    private VolleySingleton volleySingleton;

    public DownloadDataRepository(Context context,String authtoken){

        volleySingleton = VolleySingleton.getInstance(context);
        headers.put("Authorization", "Token " + authtoken);
    }

    public static DownloadDataRepository getInstance(Context context,String authtoken){
        if (instance == null) {
            synchronized (DownloadDataRepository.class) {
                instance = new DownloadDataRepository(context,authtoken);
            }
        }
        return instance;
    }

    private void checkThread() {
        Log.d(TAG, Looper.myLooper()==Looper.getMainLooper() ? "On UI thread" : "On thread: "+currentThread().getName());
    }


    public void downloadCars(final VolleyCallBack volleyCallBack){
        final String url = API_BASE+"cars_me/";


        //final Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Token " + authToken);


        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading cars", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }


    public void downloadServiceCenters(final VolleyCallBack volleyCallBack){
        final String url = API_BASE+"service_center_me/";

        //final Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Token " + authToken);


        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading service centers", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }


    public void downloadVerifications(Car car,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"verification_car/"+car.getId().toString()+"/";

        //final Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Token " + authToken);

       /* Gson gson = new Gson();
        JSONObject jsonObject ;
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject = new JSONObject(gson.toJson(car));
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }

    public void downloadReparations(Car car,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"reparation_car/"+car.getId().toString()+"/";

        //final Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Token " + authToken);

        /*
        Gson gson = new Gson();
        JSONObject jsonObject ;
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject = new JSONObject(gson.toJson(car));
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }


    public void downloadPredictedMaintenanceByCar(Car car,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"predicted_maintenance_car/"+car.getId().toString()+"/";


        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }

    public void downloadAllPredictedMaintenance(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"predicted_maintenance_me/";



        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading recommendation", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }

    public void downloadLastPredictedMaintenance(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"predicted_maintenance_me/";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "all");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturnObject = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }


    public void downloadRandomAdvice(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"advice_random/";


        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturnObject = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }


    public void downloadAllAdvices(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"advices/";

        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading verifications", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }


    public void downloadTradeHistory(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"trade_me/";

        MyJsonArrayRequest myJsonArrayRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturn = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading Trade history", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonArrayRequest);

    }


    public void downloadBuyerKey(final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"get_buyer_key/";


        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturnObject = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading buyer key", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }


    public void sellCar(Car car,int buyer_key,final VolleyCallBack volleyCallBack) {
        final String url = API_BASE+"trade_me/";

        Gson gson = new Gson();
        JSONObject objectSend = new JSONObject() ;
        try {
            JSONObject jsonObjectCar = new JSONObject(gson.toJson(car));
            objectSend.put("car",jsonObjectCar);
            objectSend.put("id_user_buyer",buyer_key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, objectSend, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        checkThread();
                        responseToReturnObject = response;

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in downloading buyer key", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);

    }








}
