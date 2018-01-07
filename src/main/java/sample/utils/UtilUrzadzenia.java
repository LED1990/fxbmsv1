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
}
