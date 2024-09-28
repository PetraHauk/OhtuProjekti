package app;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Lasku;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class AnnaMain {

    public static void main(String[] args) {
       //KayttajaHaku kayttajaHaku = new KayttajaHaku();
        //kayttajaHaku.start();

        //HotelliHaku hotelliHaku = new HotelliHaku();
        //hotelliHaku.start();

        //HuoneHaku huoneHaku = new HuoneHaku();
       //huoneHaku.start();

        //VarausHaku varausHaku = new VarausHaku();
       // varausHaku.start();

        LaskuHaku laskuHaku = new LaskuHaku();
        laskuHaku.start();

        //AsiakasHaku asiakasHaku = new AsiakasHaku();
        //asiakasHaku.start();

    }
}





