package co.example.hp.myapplication.classes;

public class Engine_oil {
    private String id;
    private String type;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private double nextOdometer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getNextOdometer() {
        return nextOdometer;
    }

    public void setNextOdometer(double nextOdometer) {
        this.nextOdometer = nextOdometer;
    }
}
