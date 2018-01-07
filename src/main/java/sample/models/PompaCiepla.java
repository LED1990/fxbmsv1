package sample.models;

import sample.constans.BaudRate;
import sample.constans.TypUrzadzenia;

import java.util.Map;

/**
 * Created by artur on 2017-12-29.
 */

public class PompaCiepla extends Urzadzenie {
    //TODO: co ma pompa ciepła

    public PompaCiepla() {
    }

    public PompaCiepla(int id, int portUrzadzenia, String nazwaUrzadzenia, TypUrzadzenia typUrzadzenia) {
        super(id, portUrzadzenia, nazwaUrzadzenia, typUrzadzenia);
    }
}
