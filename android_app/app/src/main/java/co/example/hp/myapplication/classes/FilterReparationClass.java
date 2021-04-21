package co.example.hp.myapplication.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class FilterReparationClass implements Serializable {
    private Car car;
    private String order;
    private String dueDate;
    private Long startDate;
    private Long endDate;

    public String getWhatRepared() {
        return whatRepared;
    }

    public void setWhatRepared(String whatRepared) {
        this.whatRepared = whatRepared;
    }

    private String whatRepared;

    public FilterReparationClass(Car car, String order, String dueDate, Long startDate, Long endDate, ArrayList<Service_center> service_centerArrayList, String dueOdometer, double odometer,String whatRepared) {

        this.car = car;
        this.order = order;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.service_centerArrayList = service_centerArrayList;
        this.dueOdometer = dueOdometer;
        this.odometer = odometer;
        this.whatRepared = whatRepared;
    }


    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
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

    private ArrayList<Service_center> service_centerArrayList;
    private String dueOdometer;
    private double odometer;
}
