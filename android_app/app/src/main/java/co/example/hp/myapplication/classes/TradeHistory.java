package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class TradeHistory implements Serializable {
    private Car car;
    private Long date;
    private int id_buyer;
    private String role;

    public TradeHistory() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getId_buyer() {
        return id_buyer;
    }

    public void setId_buyer(int id_buyer) {
        this.id_buyer = id_buyer;
    }
}
