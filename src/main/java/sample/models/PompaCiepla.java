package sample.models;

import sample.constans.BaudRate;

import java.util.Map;

/**
 * Created by artur on 2017-12-29.
 */

public class PompaCiepla extends Urzadzenie {
    //TODO: co ma pompa ciepła

    public PompaCiepla() {
    }

    public PompaCiepla(int id, int port, BaudRate baudrate, Map<String, Float> listaZmiennych, String nazwa) {
        super(id, port, nazwa, baudrate, listaZmiennych);
    }
}
