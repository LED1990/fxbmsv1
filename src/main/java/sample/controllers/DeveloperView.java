package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import sample.constans.TypUrzadzenia;
import sample.models.Budynek;
import sample.models.Urzadzenie;
import sample.utils.UtilBudynek;
import sample.utils.UtilPoziom;
import sample.utils.UtilUrzadzenia;

import java.io.File;
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
    private ChoiceBox choiceBoxIloscPoziomow;
    @FXML
    private VBox kontenerPoziom; //kontener w ktorym są opcje dotyczące poziomu
    @FXML
    private ChoiceBox choiceBoxwybierzPoziom;
    @FXML
    private TextField textFieldnazwaPoziomu;
    @FXML
    private Button przyciskDodajPlan;
    @FXML
    private Button przyciskDodajUrzadzenie;
    @FXML
    private ChoiceBox choiceBoxEdytujUrzadzenie;
    @FXML
    private Button przyciskEdytujUrzadzenie;
    @FXML
    private HBox kontenerWyborUrzadzeniaDoEdycji;
    @FXML
    private TextField textFieldNazwaUrzadzenia;
    @FXML
    private TextField textFieldEditPort;
    @FXML
    private ChoiceBox choiceBoxwyborTypUrzadzenia;
    @FXML
    private Button przyciskZapisz;

    private ObservableList observableListaPoziomow;
    private boolean flagaDodajEdytujUrzadzenie;//flaga do wykrywania czy wcisnieto dodaj czy edytuj urzadzenie true-dodaj false-edytuj

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        LOGGER.info("aktualna wartość ilosci poziomów: " + choiceBoxIloscPoziomow.getValue());
        kontenerPoziom.setVisible(choiceBoxIloscPoziomow.getValue() != null);//ukrywanie edycji poziomów az do wybrania ilosci poziomow
        inicjalizacjaListyPoziomow();//oraz inicjalizacja pola pola wyboru poziomu
        inicjalizacjaWyborPoziomu();
        inicjalizacjaPolaEdycjiNazwyPoziomu();
        inicjalizacjaPrzyciskuDodaniaPlanuPoziomu();
        inicjalizacjaEdycjiUrzadzenia();
        inicjalizacjaPrzyciskDoodajUrzadzenie();
        inicjalizacjaWyboruTypuUrzadzenia();
        inicjalizacjaPrzyciskZapisz();
        zablokujElement(przyciskDodajPlan);//blokada przycisku jezeli nie ma wybranego poziomu do edycji
        zablokujElement(przyciskDodajUrzadzenie);//blokada dodawania urzadzenia jezeli poziom nie jest wybrany
        zablokujElement(przyciskEdytujUrzadzenie);//blokada edycji urzadzen jezeli poziom nie jest wybrany
        ukryjElement(kontenerWyborUrzadzeniaDoEdycji);//ukrycie okna wyboru urzadzenia do edycji
        flagaDodajEdytujUrzadzenie = true;
    }

    private void zablokujElement(Node element) {
        element.setDisable(true);
    }

    private void odblokujElement(Node element) {
        element.setDisable(false);
    }

    private void ukryjElement(Node element) {
        element.setVisible(false);
    }

    private void pokazElement(Node element) {
        element.setVisible(true);
    }

    /**
     * inicjalizacja Choice boxa który służy do wyboru poziomu który ma być edytowany
     */
    private void inicjalizacjaWyborPoziomu() {
        choiceBoxwybierzPoziom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (choiceBoxwybierzPoziom.getValue() != null) {
                odblokujElement(przyciskDodajPlan);//odblokowanie przycisku dodawania mapy poziomu
                odblokujElement(przyciskDodajUrzadzenie);//odblokowanie przycisku dodawania urzadzenia
                if (UtilPoziom.sprawdzCzyPoziomMaUrzadzenia(choiceBoxwybierzPoziom)) {
                    odblokujElement(przyciskEdytujUrzadzenie);
                } else {
                    zablokujElement(przyciskEdytujUrzadzenie);
                }
                textFieldnazwaPoziomu.setText(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().getNazwa());
            } else {
                zablokujElement(przyciskDodajPlan);//blokada przycisku jezeli nie ma wybranego poziomu do edycji
                zablokujElement(przyciskDodajUrzadzenie);
            }
        });
    }

    /**
     * pole edycji nazwy poziomu, gdy zostanie zmieniona jego wartosc i utraci fokus, zostanie uaktualniona nazwa w polu wyboru poziomu do edycji
     */
    private void inicjalizacjaPolaEdycjiNazwyPoziomu() {
        //dodanie focusa do pola edycji nazwy poziomu
        textFieldnazwaPoziomu.focusedProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (oldValue1) {
                int poziomDoEdycji = Budynek.getInstance().getListaPoziomy().indexOf(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get());
                Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().setNazwa(textFieldnazwaPoziomu.getText());//update nazwy poziomy w obiekcie klasy Poziom
                observableListaPoziomow.set(poziomDoEdycji, textFieldnazwaPoziomu.getText());//update nazw poziomow do edycji
                choiceBoxwybierzPoziom.setValue(textFieldnazwaPoziomu.getText());//update nazwy w polu wyboru edytowanego poziomu
            }
        });
    }

    /**
     * inicjalizacja choiceboxa który służy określenia liczby poziomów w budynku
     */
    private void inicjalizacjaListyPoziomow() {
        //dodanie listenera ktory sprawdza czy zmieniona liczba poziomow
        choiceBoxIloscPoziomow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            kontenerPoziom.setVisible(choiceBoxIloscPoziomow.getValue() != null);//pokazanie okna edycji poziomow
            int liczbaPoziomowBudynku = Budynek.getInstance().getLiczbaPoziomow();
            int aktualnieWybranaLiczaPoziomow = Integer.parseInt(choiceBoxIloscPoziomow.getValue().toString());
            if (liczbaPoziomowBudynku < aktualnieWybranaLiczaPoziomow) {
                UtilBudynek.dodajNowePoziomy(aktualnieWybranaLiczaPoziomow, liczbaPoziomowBudynku);
            }
            if (liczbaPoziomowBudynku > aktualnieWybranaLiczaPoziomow) {
                UtilBudynek.usunPoziomy(aktualnieWybranaLiczaPoziomow, liczbaPoziomowBudynku);
            }
            observableListaPoziomow = UtilBudynek.aktualizujNazwyPoziomow(choiceBoxwybierzPoziom);
        });
    }


    private void inicjalizacjaPrzyciskuDodaniaPlanuPoziomu() {
        przyciskDodajPlan.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FileChooser fileChooser = new FileChooser();
            //dodanie blokady rozszerzen do wyboru plikow
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            fileChooser.setTitle("Wybierz mapę poziomu");
            File mapaPoziomuFile = fileChooser.showOpenDialog(null);
            if (mapaPoziomuFile != null) {
                Image image = new Image(mapaPoziomuFile.toURI().toString());
                Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().setMapaPoziomu(image);
            }
        });
    }

    private void inicjalizacjaPrzyciskDoodajUrzadzenie() {
        przyciskDodajUrzadzenie.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ukryjElement(kontenerWyborUrzadzeniaDoEdycji);//ukrycie pola wyboru urzadzenia do edycji
            przyciskZapisz.setText("Dodaj");//TODO: internacjonalizacja +
            flagaDodajEdytujUrzadzenie = true;
            //TODO: przyszłosćiowo moze dodać czyszczenie pol ktore mozna edytowac
        });
    }

    private void inicjalizacjaEdycjiUrzadzenia() {
        pokazElement(kontenerWyborUrzadzeniaDoEdycji);

    }

    private void inicjalizacjaPrzyciskZapisz() {
        //obsluga przycisku zapisujacego zminy edytoowanego lub dodawanego urzadzenia
        przyciskZapisz.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (flagaDodajEdytujUrzadzenie) {
                //dodawanie nowego urzadzenia
                //TODO: dodać walidacje pól - czy puste id...!!!!!!!!!!!!!
                //obliczenie nowego id dla urzadzenia
                int id;
                //sparwdzenie czy lista urzadzen jest null
                int rozmiarListyUrzadzen = UtilPoziom.zwrocLiczbeUrzadzenNaPoziomie(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(choiceBoxwybierzPoziom.getValue())).findFirst().get().getListaUrzadzen(),choiceBoxwybierzPoziom);
                id = rozmiarListyUrzadzen != 0 ? rozmiarListyUrzadzen + 1 : 1;
                String nazwaUrzadzenia = textFieldNazwaUrzadzenia.getText();
                int portUrzadzenia = Integer.parseInt(textFieldEditPort.getText());//TODO: jezeli to pole zostaje puste aplikacja sie wyjebie
                //dodanie nowego urzadzenia do listy
                Urzadzenie urzadzenie = UtilUrzadzenia.utworzUrzadzenie(id, portUrzadzenia, nazwaUrzadzenia, (TypUrzadzenia) choiceBoxwyborTypUrzadzenia.getValue());//utworzenie nowego urzadzenia
                UtilPoziom.dodajUrzadzenieNaPoziomie(urzadzenie, choiceBoxwybierzPoziom);
                odblokujElement(przyciskEdytujUrzadzenie);
//                UtilPoziom.wyswietlWszytskieUrzadzeniaNaPoziomie(choiceBoxwybierzPoziom);
            }else {
                //edycja urzadzenia
            }
        });
    }

    private void inicjalizacjaWyboruTypuUrzadzenia() {
        choiceBoxwyborTypUrzadzenia.getItems().setAll(FXCollections.observableArrayList(TypUrzadzenia.values()));
    }


}
