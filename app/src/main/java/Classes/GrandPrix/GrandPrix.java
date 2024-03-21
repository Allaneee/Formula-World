package Classes.GrandPrix;

import java.util.List;

import Classes.GrandPrix.Results;

public class GrandPrix {
    private String circuitId;
    private String season;
    private int round;
    private String url;
    private String raceName;
    private Circuit circuit;
    private String date;
    private String time;
    private Practice firstPractice;
    private Practice secondPractice;
    private Practice thirdPractice;
    private Practice qualifying;

    private List<Results> results;

    // Constructeur, getters et setters

    public GrandPrix(String season, int round, String url, String raceName, Circuit circuit, String date, String time, Practice firstPractice, Practice secondPractice, Practice thirdPractice, Practice qualifying, List<Results> results) {
        this.season = season;
        this.round = round;
        this.url = url;
        this.raceName = raceName;
        this.circuit = circuit;
        this.date = date;
        this.time = time;
        this.firstPractice = firstPractice;
        this.secondPractice = secondPractice;
        this.thirdPractice = thirdPractice;
        this.qualifying = qualifying;
        this.results = results; // Initialiser le champ des résultats
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
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

    public Practice getFirstPractice() {
        return firstPractice;
    }

    public void setFirstPractice(Practice firstPractice) {
        this.firstPractice = firstPractice;
    }

    public Practice getSecondPractice() {
        return secondPractice;
    }

    public void setSecondPractice(Practice secondPractice) {
        this.secondPractice = secondPractice;
    }

    public Practice getThirdPractice() {
        return thirdPractice;
    }

    public void setThirdPractice(Practice thirdPractice) {
        this.thirdPractice = thirdPractice;
    }

    public Practice getQualifying() {
        return qualifying;
    }

    public void setQualifying(Practice qualifying) {
        this.qualifying = qualifying;
    }


    @Override
    public String toString() {
        return "GrandPrix{" +
                "season='" + season + '\'' +
                ", round=" + round +
                ", url='" + url + '\'' +
                ", raceName='" + raceName + '\'' +
                ", circuit=" + circuit +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", firstPractice=" + firstPractice +
                ", secondPractice=" + secondPractice +
                ", thirdPractice=" + thirdPractice +
                ", qualifying=" + qualifying +
                ", results=" + results +
                '}';
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }
}





