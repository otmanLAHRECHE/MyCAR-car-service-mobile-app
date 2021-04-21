package co.example.hp.myapplication.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

import co.example.hp.myapplication.classes.Car;
import co.example.hp.myapplication.classes.Service_center;

public class SharedRef {
    SharedPreferences shared;

    public SharedRef(Context context) {
        this.shared = context.getSharedPreferences("ref",Context.MODE_PRIVATE);

    }

    public void saveServiceCenter(Service_center sc){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("id",sc.getId());
        editor.putString("name",sc.getName());
        editor.putString("location",sc.getLocation());
        editor.putInt("phonenumber",sc.getPhoneNumber());
        editor.commit();



    }



    public String[] loadServiceCenter(){
        String [] table = {String.valueOf(shared.getString("id","0")),shared.getString("name","no service center"),shared.getString("location","no location"),String.valueOf(shared.getInt("phonenumber",0))};
        return table;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deletefile(Context context){
        context.deleteSharedPreferences("ref");

    }
}
