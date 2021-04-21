package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class Service_center implements Serializable {
    private String id;
    private int phoneNumber;
    private String name;
    private String location;


    public Service_center(){

    }

    public Service_center(String id, int phoneNumber, String name, String location) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
