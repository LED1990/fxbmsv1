package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import sample.models.Budynek;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private ObservableList oobservableListaPoziomow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        LOGGER.info("aktualna wartość ilosci poziomów: " + iloscPoziomow.getValue());
        kontenerPoziom.setVisible(iloscPoziomow.getValue() != null);//ukrywanie edycji poziomów az do wybrania ilosci poziomow
        incjalizacjaListyPoziomow();//oraz inicjalizacja pola pola wyboru poziomu
        edycjaNazwyPoziomu();

    }

    private void incjalizacjaListyPoziomow() {

        //dodanie listenera ktory sprawdza czy zostala wprowadzona liczba poziomow
        tempPoziom = 0;
        iloscPoziomow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                LOGGER.info("nowa wartosc: " + iloscPoziomow.getValue());
            kontenerPoziom.setVisible(iloscPoziomow.getValue() != null);//pokazanie okna edycji poziomow
            //stworzenie defaultowych poziomow
            List<String> tempListaPoziomow;
            if (tempPoziom == 0) {
                //pierwszy raz wprowadzono ilosc poziomow
                tempListaPoziomow = IntStream.range(0, Integer.parseInt(iloscPoziomow.getValue().toString())).mapToObj(i -> "Poziom " + i).collect(Collectors.toList());
                Budynek.getInstance().setPoziomy(tempListaPoziomow);
                tempPoziom = Integer.parseInt(iloscPoziomow.getValue().toString());//aktualna liczba poziomow
            } else {
                //obsluga zmniejszania i zwiekszania liczby poziomow
                if (tempPoziom < Integer.parseInt(iloscPoziomow.getValue().toString())) {
                    int iter = Budynek.getInstance().getPoziomy().size();
                    int iloscIteracji = Budynek.getInstance().getPoziomy().size() + (Integer.parseInt(iloscPoziomow.getValue().toString()) - tempPoziom);
                    int i = iter;
                    while (i < iloscIteracji) {
                        Budynek.getInstance().getPoziomy().add("Poziom " + i);
                        i++;
                    }
                    tempPoziom = Budynek.getInstance().getPoziomy().size();
                }
                if (tempPoziom > Integer.parseInt(iloscPoziomow.getValue().toString())) {
                    int iter = Budynek.getInstance().getPoziomy().size();
                    int iloscIteracji = Budynek.getInstance().getPoziomy().size() - (tempPoziom - Integer.parseInt(iloscPoziomow.getValue().toString()));
                    int i = iter;
                    while (i > iloscIteracji) {
                        Budynek.getInstance().getPoziomy().remove(i - 1);
                        i--;
                    }
                    tempPoziom = Budynek.getInstance().getPoziomy().size();
                }
            }
            //wygenerowanie pola wyboru poziomu do edycji
            oobservableListaPoziomow = FXCollections.observableArrayList(Budynek.getInstance().getPoziomy());
            wybierzPoziom.setItems(oobservableListaPoziomow);
//                for (String x : Budynek.getInstance().getPoziomy()
//                        ) {
//                    LOGGER.info("nazwa poziomuu: " + x);
//                }
        });
    }

    private void edycjaNazwyPoziomu() {
        //dodany listener który sprawdza czy zostal wybrany inny poziom do edycji
        wybierzPoziom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //wyswietlanie danych o aktualnym poziomie
            if (wybierzPoziom.getValue() != null) {
                LOGGER.info("zmieniono poziom do edycji ==============================");
                nazwaPoziomu.setText(Budynek.getInstance().getPoziomy().get(Budynek.getInstance().getPoziomy().indexOf(wybierzPoziom.getValue())));
            }
        });
        //dodanie focusa do pola edycji nazwy poziomu
        nazwaPoziomu.focusedProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (oldValue1) {
                //nazwa poziomu zostala zmieniona a pole edycji zostalo opuszczone
                int poziomDoedycji = Budynek.getInstance().getPoziomy().indexOf(wybierzPoziom.getValue());
                Budynek.getInstance().getPoziomy().set(poziomDoedycji, nazwaPoziomu.getText());//update nazwy pozomu w liscie obiektu budynku
                oobservableListaPoziomow.set(poziomDoedycji,nazwaPoziomu.getText());//update nazw poziomow do edycji
                wybierzPoziom.setValue(nazwaPoziomu.getText());//update nazwy w polu wybory edytowanego poziomu
            }
        });
    }

    public int getTempPoziom() {
        return tempPoziom;
    }
}
