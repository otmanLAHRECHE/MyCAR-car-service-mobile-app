package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class ReparationToUpload implements Serializable {
    private String id;
    private Car car;
    private Service_center service_center;
    private long date;
    private double odometer;
    private String whatRepared;
    private String note;

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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Service_center getService_center() {
        return service_center;
    }

    public void setService_center(Service_center service_center) {
        this.service_center = service_center;
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

    public String getWhatRepared() {
        return whatRepared;
    }

    public void setWhatRepared(String whatRepared) {
        this.whatRepared = whatRepared;
    }
}
