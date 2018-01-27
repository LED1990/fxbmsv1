package sample.utils;

import javafx.scene.control.ChoiceBox;
import sample.constans.TypUrzadzenia;
import sample.models.Budynek;
import sample.models.PompaCiepla;
import sample.models.Rooftop;
import sample.models.Urzadzenie;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by artur on 2018-01-07.
 * klasa do manipulacji urzadzeniami - tworzenie nowego, usuwanie
 */
public class UtilUrzadzenia {
    private static final Logger LOGGER = Logger.getLogger(Urzadzenie.class.getName());

    public static Urzadzenie utworzUrzadzenie(int id, int portUrzadzenia, String nazwaUrzadzenia, TypUrzadzenia typUrzadzenia) {
        Urzadzenie urzadzenie = null;
        switch (typUrzadzenia) {
            case ROOFTOP:
                LOGGER.info("Dodano nowego rooftopa");
                urzadzenie = new Rooftop(id, portUrzadzenia, nazwaUrzadzenia, typUrzadzenia);
                break;
            case POMPACIEPLA:
                LOGGER.info("dodano nową pompe ciepła");
                urzadzenie = new PompaCiepla(id, portUrzadzenia, nazwaUrzadzenia, typUrzadzenia);
                break;
        }
        return urzadzenie;
    }

    public static Urzadzenie zwrocUrzadzeniePoNazwie(ChoiceBox choiceBoxEdytujUrzadzenie, List<Urzadzenie> listaUrzadzenNaPoziomie) {
        return listaUrzadzenNaPoziomie.stream().filter(o -> o.getNazwa().equals(choiceBoxEdytujUrzadzenie.getValue())).findFirst().get();
    }

    public static void aktualizujDaneOurzadzeniu(ChoiceBox choiceBoxEdytujUrzadzenie, ChoiceBox choiceBoxwybierzPoziom, String nazwa, int port, TypUrzadzenia typUrzadzenia) {
        List<Urzadzenie> listaUrzadzen = Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().getListaUrzadzen();
        Urzadzenie urzadzenie = listaUrzadzen.stream().filter(o -> o.getNazwa().equals(choiceBoxEdytujUrzadzenie.getValue())).findFirst().get();
        urzadzenie.setNazwa(nazwa);
        urzadzenie.setPort(port);
        urzadzenie.setTypUrzadzenia(typUrzadzenia);
    }
}
