package sample.models;

import java.util.List;
import java.util.Map;

/**
 * Created by artur on 2017-12-29.
 * Budynek jest singletonem bo bedzie tylko jeden!
 */
public class Budynek {

    //mapa poziomow reprezentowana przez numer i nazwe
    private List<String> poziomy;

    private static Budynek ourInstance = new Budynek();

    public static Budynek getInstance() {
        return ourInstance;
    }

    private Budynek() {
    }

    public List<String> getPoziomy() {
        return poziomy;
    }

    public void setPoziomy(List<String> poziomy) {
        this.poziomy = poziomy;
    }
}
