package sample.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import sample.models.Budynek;
import sample.models.Poziom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by artur on 2018-01-07.
 */
@Data
@AllArgsConstructor
public class UtilBudynek {
    private static void utworzListePoziomow(int liczbaPoziomow) {
        List<Poziom> tempListaPoziomow = new ArrayList<>();
        for (int i = 0; i < liczbaPoziomow; i++) {
            tempListaPoziomow.add(new Poziom("Poziom " + i, i + 1));
        }
        Budynek.getInstance().setListaPoziomy(tempListaPoziomow);
    }

    public static void dodajNowePoziomy(int aktualnieWybranaLiiczbaPoziomow, int liczbaPoziomowBudynku) {
        if (Budynek.getInstance().getLiczbaPoziomow() == 0) {
            UtilBudynek.utworzListePoziomow(aktualnieWybranaLiiczbaPoziomow);
            Budynek.getInstance().setLiczbaPoziomow(aktualnieWybranaLiiczbaPoziomow);//aktualizacja liczby poziomow w budynku
        } else {
            int iter = Budynek.getInstance().getListaPoziomy().size();
            int iloscIteracji = Budynek.getInstance().getListaPoziomy().size() + (aktualnieWybranaLiiczbaPoziomow - liczbaPoziomowBudynku);
            int i = iter;
            while (i < iloscIteracji) {
                Budynek.getInstance().getListaPoziomy().add(new Poziom("Poziom " + i, i + 1));
                i++;
            }
            Budynek.getInstance().setLiczbaPoziomow(aktualnieWybranaLiiczbaPoziomow);//aktualizacja liczby poziomow w budynku
        }
    }

    public static void usunPoziomy(int aktualnieWybranaLiiczbaPoziomow, int liczbaPoziomowBudynku) {
        int iter = Budynek.getInstance().getListaPoziomy().size();
        int iloscIteracji = Budynek.getInstance().getListaPoziomy().size() - (liczbaPoziomowBudynku - aktualnieWybranaLiiczbaPoziomow);
        int i = iter;
        while (i > iloscIteracji) {
            Budynek.getInstance().getListaPoziomy().remove(i - 1);
            i--;
        }
        Budynek.getInstance().setLiczbaPoziomow(aktualnieWybranaLiiczbaPoziomow);//aktualizacja liczby poziomow w budynku
    }

    public static ObservableList aktualizujNazwyPoziomow(ChoiceBox choiceBoxwybierzPoziom){
        List<String> nazwyPoziomow = new ArrayList<>();
        for (Poziom x : Budynek.getInstance().getListaPoziomy()) {
            nazwyPoziomow.add(x.getNazwa());
        }
        ObservableList observableListaPoziomow = FXCollections.observableArrayList(nazwyPoziomow);
        choiceBoxwybierzPoziom.setItems(observableListaPoziomow);
        return observableListaPoziomow;
    }

}
