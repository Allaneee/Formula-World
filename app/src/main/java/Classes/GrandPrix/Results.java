package Classes.GrandPrix;


import Classes.Driver;

public class Results {
    private String grandPrixId;
    private int position;
    private Driver driver;

    public Results(String grandPrixId, int position, Driver driver) {
        this.grandPrixId = grandPrixId;
        this.position = position;
        this.driver = driver;
    }

    public Results() {

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
        return "Results{" +
                "grandPrixId='" + grandPrixId + '\'' +
                ", position=" + position +
                ", driver=" + driver +
                '}';
    }

}
