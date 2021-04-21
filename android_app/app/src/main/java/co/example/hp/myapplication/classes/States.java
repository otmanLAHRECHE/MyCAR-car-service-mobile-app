package co.example.hp.myapplication.classes;

import java.io.Serializable;

public class States implements Serializable {
    private String id;
    private String engineState,
                    lightsState,
                    tiresState,
                    airConditioningState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEngineState() {
        return engineState;
    }

    public void setEngineState(String engineState) {
        this.engineState = engineState;
    }

    public String getLightsState() {
        return lightsState;
    }

    public void setLightsState(String lightsState) {
        this.lightsState = lightsState;
    }

    public String getTiresState() {
        return tiresState;
    }

    public void setTiresState(String tiresState) {
        this.tiresState = tiresState;
    }

    public String getAirConditioningState() {
        return airConditioningState;
    }

    public void setAirConditioningState(String airConditioningState) {
        this.airConditioningState = airConditioningState;
    }
}
