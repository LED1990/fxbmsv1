package sample.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.models.Budynek;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * Created by artur on 2018-01-27.
 */
public class UtilZapisUstawien {

    //TODO: te metody bedzie trzeba przerobic na prywatne!

    public static void zapiszZmianyDoJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("D:\\\\bms.json"), Budynek.getInstance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zapiszZmianyDoXml() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Budynek.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(Budynek.getInstance(), new File("D:\\\\bmsXml.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static boolean sprawdzCzyPlikJsonIstnieje() {
        File konfiguracja = new File("D:\\\\bms.json");
        if (konfiguracja.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Budynek budynek = objectMapper.readValue(konfiguracja, Budynek.class);
                Budynek.getInstance().setListaPoziomy(budynek.getListaPoziomy());
                Budynek.getInstance().setLiczbaPoziomow(budynek.getLiczbaPoziomow());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static boolean sprawdzCzyPlikXmlIstnieje() {
        File konfiguracja = new File("D:\\\\bmsXml.xml");
        if (konfiguracja.exists()){
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Budynek.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Budynek budynek = (Budynek) jaxbUnmarshaller.unmarshal(konfiguracja);
                Budynek.getInstance().setListaPoziomy(budynek.getListaPoziomy());
                Budynek.getInstance().setLiczbaPoziomow(budynek.getLiczbaPoziomow());
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
