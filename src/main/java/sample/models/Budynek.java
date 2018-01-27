package sample.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by artur on 2017-12-29.
 * Budynek jest singletonem bo bedzie tylko jeden!
 */
@Data
@XmlRootElement(name = "BMS")
public class Budynek {

    //mapa poziomow reprezentowana przez numer i nazwe
    private List<Poziom> listaPoziomy;

    private int liczbaPoziomow;

    private static Budynek ourInstance = new Budynek();

    public static Budynek getInstance() {
        return ourInstance;
    }

    private Budynek() {
    }
}
