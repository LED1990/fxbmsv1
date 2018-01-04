package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.models.Budynek;
import sample.models.Poziom;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by artur on 2017-12-29.
 */
public class DeveloperView implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DeveloperView.class.getName());
    @FXML
    private ChoiceBox iloscPoziomow;
    @FXML
    private VBox kontenerPoziom; //kontener w ktorym są opcje dotyczące poziomu
    @FXML
    private ChoiceBox wybierzPoziom;
    @FXML
    private TextField nazwaPoziomu;

    private int tempPoziom; //zmienna do okreslenia czy liczba poziomow sie zwiekszyla czy zmniejszyla
    private ObservableList observableListaPoziomow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        LOGGER.info("aktualna wartość ilosci poziomów: " + iloscPoziomow.getValue());
        kontenerPoziom.setVisible(iloscPoziomow.getValue() != null);//ukrywanie edycji poziomów az do wybrania ilosci poziomow
        incjalizacjaListyPoziomow();//oraz inicjalizacja pola pola wyboru poziomu
        edycjaNazwyPoziomu();

    }

    private void incjalizacjaListyPoziomow() {
        tempPoziom = 0;
        //dodanie listenera ktory sprawdza czy zostala wprowadzona liczba poziomow
        iloscPoziomow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            kontenerPoziom.setVisible(iloscPoziomow.getValue() != null);//pokazanie okna edycji poziomow
            if (tempPoziom == 0) {
                //pierwszy raz wprowadzono ilosc poziomow
                List<Poziom> tempListaPoziomow = new ArrayList<>();
                for (int i = 0; i < Integer.parseInt(iloscPoziomow.getValue().toString()); i++) {
                    tempListaPoziomow.add(new Poziom("Poziom " + i));
                }
                Budynek.getInstance().setListaPoziomy(tempListaPoziomow);
                tempPoziom = Integer.parseInt(iloscPoziomow.getValue().toString());//aktualna liczba poziomow
            } else {
                //obsluga zmniejszania i zwiekszania liczby poziomow
                if (tempPoziom < Integer.parseInt(iloscPoziomow.getValue().toString())) {
                    //liczba poziomow zostala zwiekszona
                    int iter = Budynek.getInstance().getListaPoziomy().size();
                    int iloscIteracji = Budynek.getInstance().getListaPoziomy().size() + (Integer.parseInt(iloscPoziomow.getValue().toString()) - tempPoziom);
                    int i = iter;
                    while (i < iloscIteracji) {
                        Budynek.getInstance().getListaPoziomy().add(new Poziom("Poziom " + i));
                        i++;
                    }
                    tempPoziom = Budynek.getInstance().getListaPoziomy().size();
                }
                if (tempPoziom > Integer.parseInt(iloscPoziomow.getValue().toString())) {
                    //zmniejszono liczbe poziomow
                    int iter = Budynek.getInstance().getListaPoziomy().size();
                    int iloscIteracji = Budynek.getInstance().getListaPoziomy().size() - (tempPoziom - Integer.parseInt(iloscPoziomow.getValue().toString()));
                    int i = iter;
                    while (i > iloscIteracji) {
                        Budynek.getInstance().getListaPoziomy().remove(i - 1);
                        i--;
                    }
                    tempPoziom = Budynek.getInstance().getListaPoziomy().size();
                }
            }
            //wygenerowanie pola wyboru poziomu do edycji
            List<String> nazwyPoziomow = new ArrayList<>();
            for (Poziom x : Budynek.getInstance().getListaPoziomy()) {
                nazwyPoziomow.add(x.getNazwa());
            }
            observableListaPoziomow = FXCollections.observableArrayList(nazwyPoziomow);
            wybierzPoziom.setItems(observableListaPoziomow);
        });
    }

    private void edycjaNazwyPoziomu() {
        //dodany listener który sprawdza czy zostal wybrany inny poziom do edycji
        wybierzPoziom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //wyswietlanie danych o aktualnym poziomie
            if (wybierzPoziom.getValue() != null) {
//                LOGGER.info("zmieniono poziom do edycji ==============================" + wybierzPoziom.getValue());
                nazwaPoziomu.setText(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getNazwa());
            }
        });
        //dodanie focusa do pola edycji nazwy poziomu
        nazwaPoziomu.focusedProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (oldValue1) {
                int poziomDoEdycji = Budynek.getInstance().getListaPoziomy().indexOf(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get());
                Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().setNazwa(nazwaPoziomu.getText());//update nazwy poziomy w obiekcie klasy Poziom
                observableListaPoziomow.set(poziomDoEdycji, nazwaPoziomu.getText());//update nazw poziomow do edycji
                wybierzPoziom.setValue(nazwaPoziomu.getText());//update nazwy w polu wybory edytowanego poziomu
//                for (Poziom x : Budynek.getInstance().getListaPoziomy()) {
//                    LOGGER.info("------- nazwy poziomow: " + x.getNazwa());
//                }
            }
        });
    }

    public int getTempPoziom() {
        return tempPoziom;
    }
}
