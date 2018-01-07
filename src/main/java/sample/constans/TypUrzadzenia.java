package sample.constans;

/**
 * Created by artur on 2018-01-06.
 */
public enum TypUrzadzenia {

    ROOFTOP("Rooftop"),
    POMPACIEPLA("Pompa ciepła");

    private String baud;

    TypUrzadzenia(String baud) {
        this.baud = baud;
    }
    public String typUrzadzenia(){
        return baud;
    }
}
