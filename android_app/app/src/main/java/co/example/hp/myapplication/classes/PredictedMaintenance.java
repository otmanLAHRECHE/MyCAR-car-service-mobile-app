package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class PredictedMaintenance implements Serializable {
    private String id_car;
    private Long date;
    private String predictedMaintenance;


    public PredictedMaintenance(){

    }
    public PredictedMaintenance(Long date,String predictedMaintenance) {
        this.date = date;
        this.predictedMaintenance = predictedMaintenance;
    }

    public String getId_car() {
        return id_car;
    }

    public void setId_car(String id_car) {
        this.id_car = id_car;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getPredictedMaintenance() {
        return predictedMaintenance;
    }

    public void setPredictedMaintenance(String predictedMaintenance) {
        this.predictedMaintenance = predictedMaintenance;
    }
}
