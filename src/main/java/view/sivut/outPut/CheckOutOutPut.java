package view.sivut.outPut;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import model.enteties.Asiakas;
import model.service.LocaleManager;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CheckOutOutPut {
    LocaleManager localeManager = new LocaleManager();
    Locale local = localeManager.getCurrentLocale();

    public void creatAsiakanEtunimi(String selectedLanguage, Label asiakasEtunimiLabel) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            asiakasEtunimiLabel.setText(resourceBundle.getString("asiakasEtunimiLabel.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }

    public void asiakasSukunimi(String selectedLanguage, Label asiakasSukunimiLabel) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            asiakasSukunimiLabel.setText(resourceBundle.getString("asiakasSukunimiLabel.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }

     public void haeTulostaButtos(String selectedLanguage,
                        Button tulostaButton,
                        Button haeLaskutButton) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            tulostaButton.setText(resourceBundle.getString("tulostaButton.text"));
            haeLaskutButton.setText(resourceBundle.getString("haeLaskutButton.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }

    public void maksattavaLaskuOtsikko(String selectedLanguage,
                                       Label maksattavaLaskuOtsikko,
                                       Label loppuHintaLabel,
                                       Button maksuButton,
                                       Button printButton) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            maksattavaLaskuOtsikko.setText(resourceBundle.getString("maksattavaLaskuOtsikko.text"));
            loppuHintaLabel.setText(resourceBundle.getString("loppuHintaLabel.text"));
            maksuButton.setText(resourceBundle.getString("button.pay"));
            printButton.setText(resourceBundle.getString("button.print"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }

    public void hotelliInfo(String selectedLanguage,
                            Label hotelliNimiLabel,
                            Label hotelliOsoiteLabel,
                            Label hotelliKaupunkiLabel,
                            Label hotelliMaaLabel,
                            Label hotelliPuhLabel) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {

            hotelliNimiLabel.setText(resourceBundle.getString("hotelliNimiLabel.text"));
            hotelliOsoiteLabel.setText(resourceBundle.getString("hotelliOsoiteLabel.text"));
            hotelliKaupunkiLabel.setText(resourceBundle.getString("hotelliKaupunkiLabel.text"));
            hotelliMaaLabel.setText(resourceBundle.getString("hotelliMaaLabel.text"));
            hotelliPuhLabel.setText(resourceBundle.getString("hotelliPuhLabel.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }

    public void kuitissaAsiakasInfo(String selectedLanguage, Label asiakasNimetLabel, Label maksuPvmLabel) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            asiakasNimetLabel.setText(resourceBundle.getString("asiakasNimetLabel.text"));
            maksuPvmLabel.setText(resourceBundle.getString("maksuPvmLabel.text"));

        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }

    }

    public void tuittiTable(String selectedlanguage, TableColumn<Asiakas, ?> laskuIdColumn,
                            TableColumn<Asiakas, ?> huoneNroColumn, TableColumn<Asiakas, ?> huoneTyyppiColumn,
                            TableColumn<Asiakas, ?> maksuStatusColumn,
                            TableColumn<Asiakas, ?> varausMuotoColumn,
                            TableColumn<Asiakas, ?> alkuPvmColumn,
                            TableColumn<Asiakas, ?> loppuPvmColumn,
                            TableColumn<Asiakas, ?>  paivatColumn,
                            TableColumn<Asiakas, ?> hintaColumn,
                            TableColumn<Asiakas, ?>summaColumn) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            laskuIdColumn.setText(resourceBundle.getString("laskuIdColumn.text"));
            huoneNroColumn.setText(resourceBundle.getString("huoneNroColumn.text"));
            huoneTyyppiColumn.setText(resourceBundle.getString("huoneTyyppiColumn.text"));
            maksuStatusColumn.setText(resourceBundle.getString("maksuStatusColumn.text"));
            varausMuotoColumn.setText(resourceBundle.getString("varausMuotoColumn.text"));
            alkuPvmColumn.setText(resourceBundle.getString("alkuPvmColumn.text"));
            loppuPvmColumn.setText(resourceBundle.getString("loppuPvmColumn.text"));
            paivatColumn.setText(resourceBundle.getString("paivatColumn.text"));
            hintaColumn.setText(resourceBundle.getString("hintaColumn.text"));
            summaColumn.setText(resourceBundle.getString("summaColumn.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }

    }

    public void alerts(String selectedLanguage,
                       Alert laskuEiValittu,
                       Alert laskuMaksettu,
                       Alert asiakasNotFound
                       ) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle("messages", local);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
            return;
        }

        try {
            laskuEiValittu.setContentText(resourceBundle.getString("alert.laskuEiValittu"));
            laskuMaksettu.setContentText(resourceBundle.getString("alert.laskuMaksettu"));
            asiakasNotFound.setContentText(resourceBundle.getString("asiakasNotFound.text"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + local);
        }
    }
}


