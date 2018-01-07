package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import sample.models.Poziom;
import sample.models.Urzadzenie;
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
    private ChoiceBox iloscPoziomow;
    @FXML
    private VBox kontenerPoziom; //kontener w ktorym są opcje dotyczące poziomu
    @FXML
    private ChoiceBox wybierzPoziom;
    @FXML
    private TextField nazwaPoziomu;
    @FXML
    private Button dodajPlan;
    @FXML
    private Button przyciskDodajUrzadzenie;
    @FXML
    private ChoiceBox edytujUrzadzenie;
    @FXML
    private Button przyciskEdytujUrzadzenie;
    @FXML
    private HBox wyborUrzadzeniaDoEdycji;
    @FXML
    private TextField editNazwaUrzadzenia;
    @FXML
    private TextField editPort;
    @FXML
    private ChoiceBox wyborTypUrzadzenia;
    @FXML
    private Button przyciskZapisz;

    private int tempPoziom; //zmienna do okreslenia czy liczba poziomow sie zwiekszyla czy zmniejszyla
    private ObservableList observableListaPoziomow;
    private boolean flagaDodajEdytujUrzadzenie;//flaga do wykrywania czy wcisnieto dodaj czy edytuj urzadzenie true-dodaj false-edytuj

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        LOGGER.info("aktualna wartość ilosci poziomów: " + iloscPoziomow.getValue());
        kontenerPoziom.setVisible(iloscPoziomow.getValue() != null);//ukrywanie edycji poziomów az do wybrania ilosci poziomow
        incjalizacjaListyPoziomow();//oraz inicjalizacja pola pola wyboru poziomu
        inicjalizacjaEdycjaNazwyPoziomu();
        inicjalizacjaPrzyciskuDodaniaPlanuPoziomu();
        inicjalizacjaEdycjiUrzadzenia();
        inicjalizacjaPrzyciskDoodajUrzadzenie();
        inicjalizacjaWyboruTypuUrzadzenia();
        inicjalizacjaPrzyciskZapisz();
        dodajPlan.setDisable(true);//blokada przycisku jezeli nie ma wybranego poziomu do edycji
        przyciskDodajUrzadzenie.setDisable(true);//blokada dodawania urzadzenia jezeli poziom nie jest wybrany
        przyciskEdytujUrzadzenie.setDisable(true);//blokada edycji urzadzen jezeli poziom nie jest wybrany
        flagaDodajEdytujUrzadzenie = true;
        wyborUrzadzeniaDoEdycji.setVisible(false);//ukrycie okna wyboru urzadzenia do edycji
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
                    tempListaPoziomow.add(new Poziom("Poziom " + i, i + 1));
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
                        Budynek.getInstance().getListaPoziomy().add(new Poziom("Poziom " + i, i + 1));
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

    private void inicjalizacjaEdycjaNazwyPoziomu() {
        //dodany listener który sprawdza czy zostal wybrany inny poziom do edycji
        wybierzPoziom.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //wyswietlanie danych o aktualnym poziomie
            if (wybierzPoziom.getValue() != null) {
                dodajPlan.setDisable(false);//odblokowanie przycisku dodawania mapy poziomu
                przyciskDodajUrzadzenie.setDisable(false);//odblokowanie przycisku dodawania urzadzenia
                sprawdzCzyPoziomMaUrzadzenia();//
//                LOGGER.info("zmieniono poziom do edycji ==============================" + wybierzPoziom.getValue());
                nazwaPoziomu.setText(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getNazwa());
            } else {
                dodajPlan.setDisable(true);//b9lokada przycisku jezeli nie ma wybranego poziomu do edycji
                przyciskDodajUrzadzenie.setDisable(true);
            }
        });
        //dodanie focusa do pola edycji nazwy poziomu
        nazwaPoziomu.focusedProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (oldValue1) {
                int poziomDoEdycji = Budynek.getInstance().getListaPoziomy().indexOf(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get());
                Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().setNazwa(nazwaPoziomu.getText());//update nazwy poziomy w obiekcie klasy Poziom
                observableListaPoziomow.set(poziomDoEdycji, nazwaPoziomu.getText());//update nazw poziomow do edycji
                wybierzPoziom.setValue(nazwaPoziomu.getText());//update nazwy w polu wyboru edytowanego poziomu
//                for (Poziom x : Budynek.getInstance().getListaPoziomy()) {
//                    LOGGER.info("------- nazwy poziomow: " + x.getNazwa());
//                }
            }
        });
    }

    private void inicjalizacjaPrzyciskuDodaniaPlanuPoziomu() {
        dodajPlan.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            FileChooser fileChooser = new FileChooser();
            //dodanie blokady rozszerzen do wyboru plikow
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            fileChooser.setTitle("Wybierz mapę poziomu");
            File mapaPoziomuFile = fileChooser.showOpenDialog(null);
            if (mapaPoziomuFile != null) {
                Image image = new Image(mapaPoziomuFile.toURI().toString());
//                    ImageView iv = new ImageView(image);
                Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().setMapaPoziomu(image);
//                    testImage.setImage(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getMapaPoziomu());
            }
        });
    }

    private void inicjalizacjaPrzyciskDoodajUrzadzenie() {
        przyciskDodajUrzadzenie.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            LOGGER.info("czy istnieje lista urzadzen?: " + Budynek.getInstance().getListaPoziomy().get(1).getListaUrzadzen());
            wyborUrzadzeniaDoEdycji.setVisible(false);//ukrycie pola wyboru urzadzenia do edycji
            przyciskZapisz.setText("Dodaj urzadzenie");//TODO: internacjonalizacja +
            flagaDodajEdytujUrzadzenie = true;
            //TODO: przyszłosćiowo moze dodać czyszczenie pol ktore mozna edytowac
        });
    }

    private void inicjalizacjaEdycjiUrzadzenia() {
        wyborUrzadzeniaDoEdycji.setVisible(true);

    }

    private boolean sprawdzCzyPoziomMaUrzadzenia() {
        if (Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getListaUrzadzen() != null) {
            //sprawdzenie czy lista urzadzen nie jest pusta
            if (!Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getListaUrzadzen().isEmpty()) {
                przyciskEdytujUrzadzenie.setDisable(false);//odblokowanie mozliwosci edycji urzadzen
                return true;
            } else {
                przyciskEdytujUrzadzenie.setDisable(true);//blookowanie mozliwosci edycji urzadzen
                return false;
            }
        } else {
            przyciskEdytujUrzadzenie.setDisable(true);//blokowanie mozliwosci edycji urzadzen
            return false;
        }
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
                int rozmiarListyUrzadzen = sprawdzCzyListaUrzadzenIstnieje(Budynek.getInstance().getListaPoziomy().stream().filter(o -> o.getNazwa().equals(wybierzPoziom.getValue())).findFirst().get().getListaUrzadzen());
                id = rozmiarListyUrzadzen != 0 ? rozmiarListyUrzadzen + 1 : 1;
                String nazwaUrzadzenia = editNazwaUrzadzenia.getText();
                int portUrzadzenia = Integer.parseInt(editPort.getText());//TODO: jezeli to pole zostaje puste aplikacja sie wyjebie
                //dodanie nowego urzadzenia do listy
                Urzadzenie urzadzenie = UtilUrzadzenia.utworzUrzadzenie(id, portUrzadzenia, nazwaUrzadzenia, (TypUrzadzenia) wyborTypUrzadzenia.getValue());//utworzenie nowego urzadzenia
                UtilPoziom.dodajUrzadzenieNaPoziomie(urzadzenie, wybierzPoziom);
//                UtilPoziom.wyswietlWszytskieUrzadzeniaNaPoziomie(wybierzPoziom);
            }
        });
    }

    private void inicjalizacjaWyboruTypuUrzadzenia() {
        wyborTypUrzadzenia.getItems().setAll(FXCollections.observableArrayList(TypUrzadzenia.values()));
    }

    private int sprawdzCzyListaUrzadzenIstnieje(List<Urzadzenie> urzadzeniaLista) {
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
            UtilPoziom.inicjalizujListeUrzadzen(new ArrayList<>(), wybierzPoziom);
            return 0;
        }
    }

    public int getTempPoziom() {
        return tempPoziom;
    }
}
