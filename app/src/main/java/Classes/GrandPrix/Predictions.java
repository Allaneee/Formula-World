package Classes.GrandPrix;

import java.util.HashMap;

import Classes.Driver;

public class Predictions {
    private String grandPrixId;
    private int position;
    private Driver driver;

    public Predictions(String grandPrixId, int position, Driver driver) {
        this.grandPrixId = grandPrixId;
        this.position = position;
        this.driver = driver;
    }

    public Predictions() {

    }

    public String getGrandPrixId() {
        return grandPrixId;
    }

    public void setGrandPrixId(String grandPrixId) {
        this.grandPrixId = grandPrixId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "Predictions{" +
                "grandPrixId='" + grandPrixId + '\'' +
                ", position=" + position +
                ", driver=" + driver +
                '}';
    }

}
