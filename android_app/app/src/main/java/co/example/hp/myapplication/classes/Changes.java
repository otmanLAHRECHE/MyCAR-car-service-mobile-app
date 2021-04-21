package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class Changes implements Serializable {
    private String id;
    private boolean oil_filter,air_filter,cabine_filter,brakes,transmission_oil,light_replace,wheel_alignment,battery_replace,tires_change,fuel_filter_change,glass_change,mount_and_balance;

    public boolean isLight_replace() {
        return light_replace;
    }

    public void setLight_replace(boolean light_replace) {
        this.light_replace = light_replace;
    }

    public boolean isWheel_alignment() {
        return wheel_alignment;
    }

    public void setWheel_alignment(boolean wheel_alignment) {
        this.wheel_alignment = wheel_alignment;
    }

    public boolean isBattery_replace() {
        return battery_replace;
    }

    public void setBattery_replace(boolean battery_replace) {
        this.battery_replace = battery_replace;
    }

    public boolean isTires_change() {
        return tires_change;
    }

    public void setTires_change(boolean tires_change) {
        this.tires_change = tires_change;
    }

    public boolean isFuel_filter_change() {
        return fuel_filter_change;
    }

    public void setFuel_filter_change(boolean fuel_filter_change) {
        this.fuel_filter_change = fuel_filter_change;
    }

    public boolean isGlass_change() {
        return glass_change;
    }

    public void setGlass_change(boolean glass_change) {
        this.glass_change = glass_change;
    }

    public boolean isMount_balance() {
        return mount_and_balance;
    }

    public void setMount_balance(boolean mount_balance) {
        this.mount_and_balance = mount_balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOil_filter() {
        return oil_filter;
    }

    public void setOil_filter(boolean oil_filter) {
        this.oil_filter = oil_filter;
    }

    public boolean isAir_filter() {
        return air_filter;
    }

    public void setAir_filter(boolean air_filter) {
        this.air_filter = air_filter;
    }

    public boolean isCabine_filter() {
        return cabine_filter;
    }

    public void setCabine_filter(boolean cabine_filter) {
        this.cabine_filter = cabine_filter;
    }

    public boolean isBrakes() {
        return brakes;
    }

    public void setBrakes(boolean brakes) {
        this.brakes = brakes;
    }

    public boolean isTransmission_oil() {
        return transmission_oil;
    }

    public void setTransmission_oil(boolean transmission_oil) {
        this.transmission_oil = transmission_oil;
    }
}
