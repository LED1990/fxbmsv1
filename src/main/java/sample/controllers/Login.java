package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.constans.Const;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by artur on 2017-12-28.
 */
public class Login implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    @FXML
    private TextField haslo;
    @FXML
    private ChoiceBox uzytkownik;

    public Login() {
        LOGGER.info("domyslny konstruktor logowania!");
    }

    @FXML
    private void sprawdzHaslo(ActionEvent event) throws IOException {
        Parent mainView = FXMLLoader.load(getClass().getResource("/views/main_view.fxml"));
        Scene mainViewScene = new Scene(mainView);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow(); // poobranie informacji o Stage
        String aktualnyUzytkownik = uzytkownik.getValue().toString();
        switch (Uzytkownik.valueOf(aktualnyUzytkownik)) {
            case Administrator:
//                TODO: kod dla admina
                break;
            case Developer:
                if (haslo.getText().equals(Const.DEV_PASS)) {
                    LOGGER.info("haslo poprawne");
                    window.setScene(mainViewScene);
                    window.show();
                } else {
//                    TODO: jezeli uzytkownik pooda zle haslo trzeba cos na ekran wyswietlac!!!
                    LOGGER.info("zle haslo trzeba dodac cos na ekranie jeszcze");
                }
                break;
            case Użytkownik:
//                TODO: kod dla uzytkownika
                break;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("inicjalizacja logowania!");
        List<String> listaUzytkownikow = new ArrayList<>();
        //uzupelnianie listy mozliwych uzytkownikow
        listaUzytkownikow.add(Uzytkownik.Użytkownik.getDescription());
        listaUzytkownikow.add(Uzytkownik.Administrator.getDescription());
        listaUzytkownikow.add(Uzytkownik.Developer.getDescription());

        //przekazanie listy uzytkownikow na ekran co chiceBoxa
        ObservableList observableList = FXCollections.observableList(listaUzytkownikow);
        uzytkownik.setItems(observableList);
        uzytkownik.setValue(Uzytkownik.Użytkownik.getDescription());
    }


    //ta klasa z uzytkownikami jesttylko tutaj bo niegdzie indziej nie bedzie potrzebna
    private enum Uzytkownik {
        Administrator("Administrator"),
        Developer("Developer"),
        Użytkownik("Użytkownik");

        private String opis;

        Uzytkownik(String opisEnum) {
            opis = opisEnum;
        }

        public String getDescription() {
            return opis;
        }
    }
}
