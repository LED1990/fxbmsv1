package sample.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
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
