package sample.models;

import java.awt.*;
import java.util.List;

public class Poziom {
    private String nazwa;
    private List<Urzadzenie> listaUrzadzen;
    private Image mapaPoziomu;

    public Poziom() {
    }

    public Poziom(String nazwa) {
        this.nazwa = nazwa;
    }

    public Poziom(String nazwa, List<Urzadzenie> urzadzenia, Image mapaPoziomu) {
        this.nazwa = nazwa;
        this.listaUrzadzen = urzadzenia;
        this.mapaPoziomu = mapaPoziomu;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Urzadzenie> getListaUrzadzen() {
        return listaUrzadzen;
    }

    public void setListaUrzadzen(List<Urzadzenie> listaUrzadzen) {
        this.listaUrzadzen = listaUrzadzen;
    }

    public Image getMapaPoziomu() {
        return mapaPoziomu;
    }

    public void setMapaPoziomu(Image mapaPoziomu) {
        this.mapaPoziomu = mapaPoziomu;
    }
}
