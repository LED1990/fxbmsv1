package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by artur on 2017-12-29.
 */
public class MainView implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

    @FXML
    private DeveloperView developerController;

    @FXML
    private MenuView menuController;

    public MainView() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("main view metoda initialize");
    }

}
