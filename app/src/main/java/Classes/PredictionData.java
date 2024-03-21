package Classes;

public class PredictionData {
    private String text;
    private int backgroundColor;

    public PredictionData(String text, int backgroundColor) {
        this.text = text;
        this.backgroundColor = backgroundColor;
    }

    public String getText() {
        return text;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
