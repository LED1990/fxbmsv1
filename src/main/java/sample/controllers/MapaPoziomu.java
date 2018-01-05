package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.models.Budynek;
import sample.models.Poziom;
import sample.utils.DragResize;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MapaPoziomu implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MapaPoziomu.class.getName());

    @FXML
    private Button generujMenu;
    @FXML
    private TabPane tabPanePoziomy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generujPoziomy();
    }

    private void generujPoziomy() {
        //dodanie listenera do przycisku generowania widoku glownego
        generujMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //dodawanie i usuwanie poziomow (zakladek w widoku poziomow)
            if (Budynek.getInstance() != null && Budynek.getInstance().getListaPoziomy() != null) {
                int taby, poziomy, count;//zmienne pomocnicze do porownywania liczby poziomow stowrzonych z tym do stworzenia lub usuniecia
                taby = tabPanePoziomy.getTabs().size();//aktualna liczba wyswietlanych poziomow
                poziomy = Budynek.getInstance().getListaPoziomy().size();//aktualna lista poziomow w klasie budynek
                count = 0;
                if (tabPanePoziomy.getTabs().size() < Budynek.getInstance().getListaPoziomy().size()) {
                    for (Poziom p : Budynek.getInstance().getListaPoziomy()) {
                        if ((poziomy - (poziomy - taby)) > count) {
                            //aktualizacja nazw poziomow
                            tabPanePoziomy.getTabs().get(count).setText(p.getNazwa());
                            //aktualizacja map poziomow
                            tabPanePoziomy.getTabs().get(count).setContent(wygenerujMape(p.getMapaPoziomu()));
                        } else {
                            //dodawanie nowych poziomow
                            tabPanePoziomy.getTabs().add(generujZakladkiPoziomow(p.getNazwa(), p.getMapaPoziomu()));
                        }
                        count++;
                    }
                } else {
                    if (tabPanePoziomy.getTabs().size() > Budynek.getInstance().getListaPoziomy().size()) {
                        tabPanePoziomy.getTabs().remove((taby - (taby - poziomy)), taby);//usuwanie poziomow
                        int index = 0;
                        for (Tab t : tabPanePoziomy.getTabs()) {
                            //aktualizacja nazw poziomow
                            t.setText(Budynek.getInstance().getListaPoziomy().get(index).getNazwa());
                            //aktualizacja map poziomow
                            t.setContent(wygenerujMape(Budynek.getInstance().getListaPoziomy().get(index).getMapaPoziomu()));
                            index++;
                        }
                    } else {
                        int index = 0;
                        for (Poziom p : Budynek.getInstance().getListaPoziomy()) {
                            //sama aktualizacja nazw poziomów
                            tabPanePoziomy.getTabs().get(index).setText(p.getNazwa());
                            //aktualizacja map poziomow
                            tabPanePoziomy.getTabs().get(index).setContent(wygenerujMape(p.getMapaPoziomu()));
                            index++;
                        }
                    }
                }
            }
        });
    }

    private Tab generujZakladkiPoziomow(String nazwaPoziomu, Image image) {
        //generowanie zakladek z nazwami poziomow na glownym widoku
        Tab poziom = new Tab();
        Rectangle rectangle = wygenerujMape(image);
        if (image != null) {
            rectangle.setFill(new ImagePattern(image));
            poziom.setContent(rectangle);
        }
        poziom.setText(nazwaPoziomu);
        return poziom;
    }

    private Rectangle wygenerujMape(Image image) {
        //generowanie prostokąta ktory mozna zmieniac przy p omocy myszki
        Rectangle rectangle = new Rectangle();
        //TODO to bedzie trzeba inaczej rozwiazac - automatyczne wypelnianie ekranu itp
        rectangle.setHeight(100);
        rectangle.setWidth(100);
        DragResize.makeResizable(rectangle, null);
        if (image != null) {
            rectangle.setFill(new ImagePattern(image));
            return rectangle;
        }
        return null;
    }
}
