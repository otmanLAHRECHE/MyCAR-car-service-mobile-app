package co.example.hp.myapplication.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Repare implements Serializable {
    private String id;
    private long date;
    private double odometer;
    private String whatRepared;
    private String note;

    public String getWhatRepared() {
        return whatRepared;
    }

    public void setWhatRepared(String whatRepared) {
        this.whatRepared = whatRepared;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }


}
