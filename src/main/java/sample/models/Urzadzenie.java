package sample.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import sample.constans.BaudRate;
import sample.constans.TypUrzadzenia;

import java.util.Map;

/**
 * Created by artur on 2017-12-29.
 */
@AllArgsConstructor
@Data
public abstract class Urzadzenie {
    private int id;
    private int port;
    private TypUrzadzenia typUrzadzenia;
    private String nazwa;
    private BaudRate baudrate;
    private Map<String, Float> listaZmiennych;

    public Urzadzenie() {
    }

    public Urzadzenie(int id,int portUrzadzenia, String nazwaUrzadzenia, TypUrzadzenia typUrzadzenia) {
        this.id=id;
        this.nazwa=nazwaUrzadzenia;
        this.typUrzadzenia=typUrzadzenia;
        this.port=portUrzadzenia;
    }


}
