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
import sample.utils.UtilZapisUstawien;

import java.io.File;
import java.net.URL;
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
    private ChoiceBox choiceBoxwybierzPoziom;
    @FXML
    private ChoiceBox choiceBoxEdytujUrzadzenie;
    @FXML
    private ChoiceBox choiceBoxwyborTypUrzadzenia;
    @FXML
    private VBox kontenerPoziom; //kontener w ktorym są opcje dotyczące poziomu
    @FXML
    private HBox kontenerWyborUrzadzeniaDoEdycji;
    @FXML
    private Button przyciskDodajPlan;
    @FXML
    private Button przyciskDodajUrzadzenie;
    @FXML
    private Button przyciskZapisz;
    @FXML
    private Button przyciskEdytujUrzadzenie;
    @FXML
    private TextField textFieldNazwaUrzadzenia;
    @FXML
    private TextField textFieldnazwaPoziomu;
    @FXML
    private TextField textFieldEditPort;

    private ObservableList observableListaPoziomow;
    private boolean flagaDodajEdytujUrzadzenie;//flaga do wykrywania czy wcisnieto dodaj czy edytuj urzadzenie true-dodaj false-edytuj

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (UtilZapisUstawien.sprawdzCzyPlikXmlIstnieje()) {
            LOGGER.info("informacje o obudynku: " + Budynek.getInstance().toString());
        } else {
//        LOGGER.info("aktualna wartość ilosci poziomów: " + choiceBoxIloscPoziomow.getValue());
            kontenerPoziom.setVisible(choiceBoxIloscPoziomow.getValue() != null);//ukrywanie edycji poziomów az do wybrania ilosci poziomow
            inicjalizacjaListyPoziomow();//oraz inicjalizacja pola pola wyboru poziomu
            inicjalizacjaWyborPoziomu();
            inicjalizacjaPolaEdycjiNazwyPoziomu();
            inicjalizacjaPrzyciskuDodaniaPlanuPoziomu();
            inicjalizacjaPrzyciskDoodajUrzadzenie();
            inicjalizacjaWyboruTypuUrzadzenia();
            inicjalizacjaPrzyciskZapisz();
            inicjalizacjaPrzyciskEdytujUrzadzenie();
            inicjalizacjaChoiceBoxEdytujUrzadzenie();
            zablokujElement(przyciskDodajPlan);//blokada przycisku jezeli nie ma wybranego poziomu do edycji
            zablokujElement(przyciskDodajUrzadzenie);//blokada dodawania urzadzenia jezeli poziom nie jest wybrany
            zablokujElement(przyciskEdytujUrzadzenie);//blokada edycji urzadzen jezeli poziom nie jest wybrany
            ukryjElement(kontenerWyborUrzadzeniaDoEdycji);//ukrycie okna wyboru urzadzenia do edycji
            flagaDodajEdytujUrzadzenie = true;
        }
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
                textFieldnazwaPoziomu.setText(UtilPoziom.zwrocNazwePoziomu(choiceBoxwybierzPoziom));
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
                int poziomDoEdycji = UtilPoziom.zwrocIndexPoziomu(choiceBoxwybierzPoziom);
                UtilPoziom.zmienNazwePoziomu(choiceBoxwybierzPoziom, textFieldnazwaPoziomu);
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

    private void inicjalizacjaPrzyciskEdytujUrzadzenie() {
        przyciskEdytujUrzadzenie.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            pokazElement(kontenerWyborUrzadzeniaDoEdycji);
            flagaDodajEdytujUrzadzenie = false;
            UtilPoziom.aktualizujNazwyUrzadzenNaPoziomie(choiceBoxwybierzPoziom, choiceBoxEdytujUrzadzenie);
            przyciskZapisz.setText("Zapisz zmiany");
        });
    }

    private void inicjalizacjaChoiceBoxEdytujUrzadzenie() {
        choiceBoxEdytujUrzadzenie.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: tutaj trzeba ustawic wszystkie prametry urzadzenia
            //pobranie i uzupełnienie danych oo urządzeniu
            List<Urzadzenie> listaUrzadzenNaPooziomie = UtilPoziom.zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom);
            Urzadzenie urzadzenie = UtilUrzadzenia.zwrocUrzadzeniePoNazwie(choiceBoxEdytujUrzadzenie, listaUrzadzenNaPooziomie);
            textFieldNazwaUrzadzenia.setText(urzadzenie.getNazwa());
            textFieldEditPort.setText(String.valueOf(urzadzenie.getPort()));
            choiceBoxwyborTypUrzadzenia.setValue(urzadzenie.getTypUrzadzenia());
        });
    }

    private void inicjalizacjaPrzyciskZapisz() {
        //obsluga przycisku zapisujacego zminy edytoowanego lub dodawanego urzadzenia
        przyciskZapisz.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (flagaDodajEdytujUrzadzenie) {
                //dodawanie nowego urzadzenia
                //TODO: dodać walidacje pól - czy puste id...!!!!!!!!!!!!!
                int rozmiarListyUrzadzen = UtilPoziom.zwrocLiczbeUrzadzenNaPoziomie(choiceBoxwybierzPoziom);
                int id = rozmiarListyUrzadzen != 0 ? rozmiarListyUrzadzen + 1 : 1;
                String nazwaUrzadzenia = textFieldNazwaUrzadzenia.getText();
                int portUrzadzenia = Integer.parseInt(textFieldEditPort.getText());//TODO: jezeli to pole zostaje puste aplikacja sie wyjebie
                //dodanie nowego urzadzenia do listy
                Urzadzenie urzadzenie = UtilUrzadzenia.utworzUrzadzenie(id, portUrzadzenia, nazwaUrzadzenia, (TypUrzadzenia) choiceBoxwyborTypUrzadzenia.getValue());//utworzenie nowego urzadzenia
                UtilPoziom.dodajUrzadzenieNaPoziomie(urzadzenie, choiceBoxwybierzPoziom);
                odblokujElement(przyciskEdytujUrzadzenie);
//                UtilPoziom.wyswietlWszytskieUrzadzeniaNaPoziomie(choiceBoxwybierzPoziom);
            } else {
                //edycja urzadzenia
                //TODO: trzeba bedzie dodać walidacje na wypadek pustych pól itd
                List<Urzadzenie> listaUrzadzenNaPooziomie = UtilPoziom.zwrocListeUrzadzenNaPoziomie(choiceBoxwybierzPoziom);
                String nazwa = textFieldNazwaUrzadzenia.getText();
                int port = Integer.parseInt(textFieldEditPort.getText());
                TypUrzadzenia typUrzadzenia = (TypUrzadzenia) choiceBoxwyborTypUrzadzenia.getValue();
                UtilUrzadzenia.aktualizujDaneOurzadzeniu(choiceBoxEdytujUrzadzenie, choiceBoxwybierzPoziom, nazwa, port, typUrzadzenia);
                UtilPoziom.wyswietlWszytskieUrzadzeniaNaPoziomie(choiceBoxwybierzPoziom);
                UtilZapisUstawien.zapiszZmianyDoJson();
                UtilZapisUstawien.zapiszZmianyDoXml();
            }
        });
    }

    private void inicjalizacjaWyboruTypuUrzadzenia() {
        choiceBoxwyborTypUrzadzenia.getItems().setAll(FXCollections.observableArrayList(TypUrzadzenia.values()));
    }
}
