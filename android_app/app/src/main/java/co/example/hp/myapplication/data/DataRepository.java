package co.example.hp.myapplication.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.example.hp.myapplication.LoginActivity;
import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Changes;
import co.example.hp.myapplication.classes.Engine_oil;
import co.example.hp.myapplication.classes.ReparationToUpload;
import co.example.hp.myapplication.classes.Service_center;
import co.example.hp.myapplication.classes.States;
import co.example.hp.myapplication.classes.Verification;
import co.example.hp.myapplication.classes.VerificationToUpload;
import co.example.hp.myapplication.rest.GsonRequest;
import co.example.hp.myapplication.rest.MyJsonObjectRequest;
import co.example.hp.myapplication.rest.VolleySingleton;

import static co.example.hp.myapplication.LoginActivity.TAG;

public class DataRepository {

    private static volatile DataRepository instance;

    private final Map<String, String> headers = new HashMap<>();

    public Object responseTrack;

    public Map<String, String> getHeaders() {
        return headers;
    }

    private VolleySingleton volleySingleton;


    public DataRepository(Context context,String authtoken) {


        Log.d("/////////////////////////////////////////////data_repo",authtoken);
        volleySingleton = VolleySingleton.getInstance(context);
        headers.put("Authorization", "Token " + authtoken);
    }


    public static DataRepository getInstance(Context context,String authtoken) {

        Log.d("/////////////////////////////////////////////data_inst",authtoken);
        if (instance == null) {
            synchronized (DataRepository.class) {
                instance = new DataRepository(context,authtoken);
            }
        }
        return instance;
    }

    public void fetchAllCars() {
        GsonRequest<Car[]> gsonRequest = new GsonRequest<>(Request.Method.GET, LoginActivity.API_BASE+"cars_me/", null,
                new Response.Listener<Car[]>(){
                    @Override
                    public void onResponse(Car[] response) {
//                        for (HealthEntry item : response) {
//                            ITEM_MAP.put(item.id, item);
//                        }

//                        allEntries.setValue(Arrays.asList(response));
                        //insert(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error parsing data :)", error);
                    }
                }, Car[].class, LoginRepository.getInstance(null).getHeaders());
        volleySingleton.addToRequestQueue(gsonRequest);
    }


    public void uploadCar(Car car, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(car));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Car> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "cars_me/", jsonObject,
                new Response.Listener<Car>() {
                    @Override
                    public void onResponse(Car response) {
                        Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading car !", error);
                    }
                }, Car.class, headers);
        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void updateCar(Car car, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(car));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Car> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "cars_me/"+car.getId().toString()+"/", jsonObject,
                new Response.Listener<Car>() {
                    @Override
                    public void onResponse(Car response) {
                        Log.d(TAG, "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh"+response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error updating car !", error);
                    }
                }, Car.class, headers);
        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void uploadServiceCenter(Service_center service_center, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(service_center));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Service_center> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "service_center_me/", jsonObject,
                new Response.Listener<Service_center>() {
                    @Override
                    public void onResponse(Service_center response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading service center!", error);
                    }
                }, Service_center.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }

    public void updateServiceCenter(Service_center service_center, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(service_center));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Service_center> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "service_center_me/"+service_center.getId()+"/", jsonObject,
                new Response.Listener<Service_center>() {
                    @Override
                    public void onResponse(Service_center response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading service center!", error);
                    }
                }, Service_center.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }

    public void uploadEngineOil(Engine_oil engine_oil, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(engine_oil));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Engine_oil> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "engine_oil_me/", jsonObject,
                new Response.Listener<Engine_oil>() {
                    @Override
                    public void onResponse(Engine_oil response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading engine oil!", error);
                    }
                }, Engine_oil.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }



    public void updateEngineOil(Engine_oil engine_oil, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(engine_oil));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Engine_oil> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "engine_oil_me/"+engine_oil.getId()+"/", jsonObject,
                new Response.Listener<Engine_oil>() {
                    @Override
                    public void onResponse(Engine_oil response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading engine oil!", error);
                    }
                }, Engine_oil.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }


    public void uploadChange(Changes changes, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(changes));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Changes> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "changes_me/", jsonObject,
                new Response.Listener<Changes>() {
                    @Override
                    public void onResponse(Changes response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading change!", error);
                    }
                }, Changes.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }

    public void updateChange(Changes changes, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(changes));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<Changes> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "changes_me/"+changes.getId()+"/", jsonObject,
                new Response.Listener<Changes>() {
                    @Override
                    public void onResponse(Changes response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading change!", error);
                    }
                }, Changes.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);

    }


    public void uploadStatus(States states, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(states));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<States> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "status_me/", jsonObject,
                new Response.Listener<States>() {
                    @Override
                    public void onResponse(States response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading status!", error);
                    }
                }, States.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void updateStatus(States states, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(states));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<States> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "status_me/"+states.getId()+"/", jsonObject,
                new Response.Listener<States>() {
                    @Override
                    public void onResponse(States response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading status!", error);
                    }
                }, States.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void uploadVerification(VerificationToUpload verificationToUpload, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(verificationToUpload));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<VerificationToUpload> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "verification_me/", jsonObject,
                new Response.Listener<VerificationToUpload>() {
                    @Override
                    public void onResponse(VerificationToUpload response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading verification!", error);
                    }
                }, VerificationToUpload.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }

    public void updateVerification(VerificationToUpload verificationToUpload, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(verificationToUpload));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<VerificationToUpload> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "verification_me/", jsonObject,
                new Response.Listener<VerificationToUpload>() {
                    @Override
                    public void onResponse(VerificationToUpload response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading verification!", error);
                    }
                }, VerificationToUpload.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }


    public void uploadReparation(ReparationToUpload reparationToUpload, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(reparationToUpload));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<ReparationToUpload> gsonRequest = new GsonRequest<>(Request.Method.POST, LoginActivity.API_BASE + "reparation_me/", jsonObject,
                new Response.Listener<ReparationToUpload>() {
                    @Override
                    public void onResponse(ReparationToUpload response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading reparation!", error);
                    }
                }, ReparationToUpload.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }


    public void updateReparation(ReparationToUpload reparationToUpload, final VolleyCallBack volleyCallBack) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(reparationToUpload));
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        GsonRequest<ReparationToUpload> gsonRequest = new GsonRequest<>(Request.Method.PUT, LoginActivity.API_BASE + "reparation_me/", jsonObject,
                new Response.Listener<ReparationToUpload>() {
                    @Override
                    public void onResponse(ReparationToUpload response) {
                        Log.d(TAG, response.toString());
                        responseTrack = (Object) response;

                        volleyCallBack.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error uploading reparation!", error);
                    }
                }, ReparationToUpload.class, headers);

        volleySingleton.addToRequestQueue(gsonRequest);
    }






    public void sendFCMToken(String registration_id, final VolleyCallBack volleyCallBack) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("registration_id",registration_id);
            jsonObject.put("type","android");
            jsonObject.put("active",true);
        } catch (JSONException e) {
            Log.d(TAG, "", e);
        }
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, LoginActivity.API_BASE + "devices/", jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in send FCM token", error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };
        VolleySingleton.getInstance(null).addToRequestQueue(myJsonObjectRequest);
    }

    public void updateFCMToken(String registration_id, final VolleyCallBack volleyCallBack) {



        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest
                (Request.Method.POST, LoginActivity.API_BASE + "update_device/"+registration_id+"/", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        volleyCallBack.onSuccess();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "problem in send FCM token", error);
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
