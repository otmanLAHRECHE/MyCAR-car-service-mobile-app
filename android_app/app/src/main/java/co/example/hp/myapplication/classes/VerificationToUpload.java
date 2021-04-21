package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class VerificationToUpload implements Serializable {
    private String id;
    private Car car;
    private Engine_oil engine_oil;
    private Service_center service_center;
    private States states;
    private Changes changes;
    private long date;
    private double odometer;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Engine_oil getEngine_oil() {
        return engine_oil;
    }

    public void setEngine_oil(Engine_oil engine_oil) {
        this.engine_oil = engine_oil;
    }

    public Service_center getService_center() {
        return service_center;
    }

    public void setService_center(Service_center service_center) {
        this.service_center = service_center;
    }

    public States getStates() {
        return states;
    }

    public void setStates(States states) {
        this.states = states;
    }

    public Changes getChanges() {
        return changes;
    }

    public void setChanges(Changes changes) {
        this.changes = changes;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

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





    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
