package co.example.hp.myapplication.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterVerificationClass implements Serializable {
    private Car car;
    private Boolean engineOil;

    public Boolean getEngineOil() {
        return engineOil;
    }

    public void setEngineOil(Boolean engineOil) {
        this.engineOil = engineOil;
    }

    public FilterVerificationClass(Car car, String order, String dueDate, Long startDate, Long endDate, ArrayList<Service_center> service_centerArrayList, String dueOdometer, double odometer, boolean engineOil, Changes changes, States states) {

        this.car = car;
        this.order = order;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.service_centerArrayList = service_centerArrayList;
        this.dueOdometer = dueOdometer;
        this.odometer = odometer;
        this.changes = changes;
        this.states = states;
        this.engineOil = engineOil;
    }


    private String order;
    private String dueDate;
    private Long startDate;
    private Long endDate;
    private ArrayList<Service_center> service_centerArrayList;
    private String dueOdometer;
    private double odometer;
    private Changes changes;
    private States states;

    public FilterVerificationClass(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public States getStates() {
        return states;
    }

    public void setStates(States states) {
        this.states = states;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Service_center> getService_centerArrayList() {
        return service_centerArrayList;
    }

    public void setService_centerArrayList(ArrayList<Service_center> service_centerArrayList) {
        this.service_centerArrayList = service_centerArrayList;
    }

    public String getDueOdometer() {
        return dueOdometer;
    }

    public void setDueOdometer(String dueOdometer) {
        this.dueOdometer = dueOdometer;
    }

    public double getOdometer() {
        return odometer;
    }

    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    public Changes getChanges() {
        return changes;
    }

    public void setChanges(Changes changes) {
        this.changes = changes;
    }
}
