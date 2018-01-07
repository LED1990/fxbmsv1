package sample.utils;

import javafx.scene.control.ChoiceBox;
import sample.models.Budynek;
import sample.models.Poziom;
import sample.models.Urzadzenie;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by artur on 2018-01-07.
 */
public class UtilPoziom {
    private static final Logger LOGGER = Logger.getLogger(Poziom.class.getName());

    public static void inicjalizujListeUrzadzen(List<Urzadzenie> listaUrzadzen, ChoiceBox poziom) {
        LOGGER.info("inicjalizacja listy urzadzen");
        Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(poziom.getValue())).findFirst().get().setListaUrzadzen(listaUrzadzen);
    }

    public static void dodajUrzadzenieNaPoziomie(Urzadzenie urzadzenie, ChoiceBox poziom) {
        LOGGER.info("dodanie urzadzenia na danym poziomie");
        Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(poziom.getValue())).findFirst().get().getListaUrzadzen().add(urzadzenie);
    }

    public static void wyswietlWszytskieUrzadzeniaNaPoziomie(ChoiceBox poziom) {
        LOGGER.info("urzadzenia poziomu " + poziom.getValue().toString());
        for (Urzadzenie u : Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(poziom.getValue())).findFirst().get().getListaUrzadzen()) {
            LOGGER.info(u.toString());
        }
    }
}
