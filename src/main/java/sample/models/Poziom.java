package sample.models;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Poziom {
    private int id;
    private String nazwa;
    private List<Urzadzenie> listaUrzadzen;
    private Image mapaPoziomu;

    public Poziom() {
    }

    public Poziom(String nazwa, int id) {
        this.nazwa = nazwa;
        this.id = id;
    }


}
