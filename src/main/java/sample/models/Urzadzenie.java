package sample.models;

import sample.constans.BaudRate;

import java.util.Map;

/**
 * Created by artur on 2017-12-29.
 */
public abstract class Urzadzenie {
    private int id;
    private int port;
    private BaudRate baudrate;
    private Map<String, Float> listaZmiennych;

    public Urzadzenie() {
    }

    public Urzadzenie(int id, int port, BaudRate baudrate, Map<String, Float> listaZmiennych) {
        this.id = id;
        this.port = port;
        this.baudrate = baudrate;
        this.listaZmiennych = listaZmiennych;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public BaudRate getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(BaudRate baudrate) {
        this.baudrate = baudrate;
    }

    public Map<String, Float> getListaZmiennych() {
        return listaZmiennych;
    }

    public void setListaZmiennych(Map<String, Float> listaZmiennych) {
        this.listaZmiennych = listaZmiennych;
    }
}
