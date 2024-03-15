package Classes.GrandPrix;

public class Practice {
    private String date;
    private String time;

    // Constructeur, getters et setters


    public Practice(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Practice{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}
