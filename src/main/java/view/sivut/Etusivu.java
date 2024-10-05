package view.sivut;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Etusivu {

    public VBox createEtusivu() {
        VBox etusivuInfo = new VBox(10);
        etusivuInfo.getStyleClass().add("info");
        Label etusivuOtsikkoLabel = new Label("Etusivu");
        etusivuOtsikkoLabel.getStyleClass().add("otsikko");
        Label etusivuKuvausLabel = new Label("Täältä voit hallita hotellin huoneita ja asiakkaita sekä tehdä check-in ja check-out toimintoja.");
        etusivuKuvausLabel.setWrapText(true);
        etusivuKuvausLabel.setMaxWidth(400);
        etusivuInfo.getChildren().addAll(etusivuOtsikkoLabel, etusivuKuvausLabel);
        return etusivuInfo;
    }
}
