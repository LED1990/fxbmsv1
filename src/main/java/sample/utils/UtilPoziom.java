package sample.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.models.Budynek;
import sample.models.Poziom;
import sample.models.Urzadzenie;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by artur on 2018-01-07.
 */
public class UtilPoziom {
    private static final Logger LOGGER = Logger.getLogger(Poziom.class.getName());

    public static void inicjalizujListeUrzadzen(List<Urzadzenie> listaUrzadzen, ChoiceBox choiceBoxwybierzPoziom) {
        LOGGER.info("inicjalizacja listy urzadzen");
        Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().setListaUrzadzen(listaUrzadzen);
    }

    public static void dodajUrzadzenieNaPoziomie(Urzadzenie urzadzenie, ChoiceBox choiceBoxwybierzPoziom) {
        LOGGER.info("dodanie urzadzenia na danym poziomie");
        zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom).add(urzadzenie);
    }

    public static void wyswietlWszytskieUrzadzeniaNaPoziomie(ChoiceBox choiceBoxwybierzPoziom) {
        LOGGER.info("urzadzenia poziomu " + choiceBoxwybierzPoziom.getValue().toString());
        for (Urzadzenie u : zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom)) {
            LOGGER.info(u.toString());
        }
    }

    public static void aktualizujNazwyUrzadzenNaPoziomie(ChoiceBox choiceBoxwybierzPoziom, ChoiceBox choiceBoxEdytujUrzadzenie) {
        List<String> nazwyUrzadzen = new ArrayList<>();
        for (Urzadzenie x : zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom)) {
            nazwyUrzadzen.add(x.getNazwa());
        }
        ObservableList observableNazwyUrzadzen = FXCollections.observableArrayList(nazwyUrzadzen);
        choiceBoxEdytujUrzadzenie.setItems(observableNazwyUrzadzen);
    }

    public static boolean sprawdzCzyPoziomMaUrzadzenia(ChoiceBox choiceBoxwybierzPoziom) {
        return zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom) != null ? !zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom).isEmpty() : false;
    }

    public static int zwrocLiczbeUrzadzenNaPoziomie(ChoiceBox choiceBoxwybierzPoziom) {
        List<Urzadzenie> urzadzeniaLista = zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom);
        if (urzadzeniaLista != null) {
            if (urzadzeniaLista.isEmpty()) {
                LOGGER.info("lista urzadzen dla danego poziomu jest pusta");
                return 0;
            } else {
                LOGGER.info("poziom poosiada liste urzadzen");
                return urzadzeniaLista.size();
            }
        } else {
            LOGGER.info("lista urzadzen dla danego pooziomu jest NULL");
            UtilPoziom.inicjalizujListeUrzadzen(new ArrayList<>(), choiceBoxwybierzPoziom);
            return 0;
        }
    }

    public static String zwrocNazwePoziomu(ChoiceBox choiceBoxwybierzPoziom) {
        return Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().getNazwa();
    }

    public static int zwrocIndexPoziomu(ChoiceBox choiceBoxwybierzPoziom) {
        return Budynek.getInstance().getListaPoziomy().indexOf(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get());
    }

    public static void zmienNazwePoziomu(ChoiceBox choiceBoxwybierzPoziom, TextField textFieldnazwaPoziomu) {
        Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().setNazwa(textFieldnazwaPoziomu.getText());

    }

    private static List<Urzadzenie> zwrocListeUrzadzenNaPoziomie(ChoiceBox choiceBoxwybierzPoziom) {
        return Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().getListaUrzadzen();
    }
}
