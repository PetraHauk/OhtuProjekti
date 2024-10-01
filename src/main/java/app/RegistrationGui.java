package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;


public class RegistrationGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rekisteröinti");

        // Create UI elements
        TextField etunimiField = new TextField();
        etunimiField.setPromptText("Etunimi");
        etunimiField.getStyleClass().add("text-field");

        TextField sukunimiField = new TextField();
        sukunimiField.setPromptText("Sukunimi");
        sukunimiField.getStyleClass().add("text-field");

        TextField spostiField = new TextField();
        spostiField.setPromptText("Email");
        spostiField.getStyleClass().add("text-field");

        TextField puhField = new TextField();
        puhField.setPromptText("Puh");
        puhField.getStyleClass().add("text-field");

        PasswordField salasanaField = new PasswordField();
        salasanaField.setPromptText("Salasana");
        salasanaField.getStyleClass().add("password-field");

        Button registerButton = new Button("Rekisteröidy");
        registerButton.getStyleClass().add("button-one");

        Button backButton = new Button("Kirjautumiseen");
        backButton.getStyleClass().add("button-two");

        // Layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20)); // Add padding around the VBox
        vbox.getChildren().addAll(etunimiField, sukunimiField, spostiField, puhField, salasanaField, registerButton, backButton);

        // Event Handlers
        registerButton.setOnAction(e -> handleRegister(
                etunimiField.getText(),
                sukunimiField.getText(),
                spostiField.getText(),
                puhField.getText(),
                salasanaField.getText(),
                primaryStage
        ));

        backButton.setOnAction(e -> {
            primaryStage.close();
            new LoginGui().start(new Stage()); // Return to login page
        });

        // Set Scene with preferred size
        Scene scene = new Scene(vbox, 400, 400); // Increase the size of the scene
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void handleRegister(String etunimi, String sukunimi, String sposti, String puh, String salasana, Stage primaryStage) {
        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Rekisteröinti epäonnistui");
            alert.setContentText("Täytä kaikki kohdat.");
            alert.showAndWait();
        } else if (kayttajaDAO.onkoEmailOlemassa(sposti)) {  // Check if the email already exists
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Rekisteröinti epäonnistui");
            alert.setContentText("Sähköpostiosoite on jo rekisteröity.");
            alert.showAndWait();
        } else {
            // Hash the password using BCrypt
            String hashattuSalasana = BCrypt.hashpw(salasana, BCrypt.gensalt());

            // Create a new Kayttaja object and set the hashed password
            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setEtunimi(etunimi);
            kayttaja.setSukunimi(sukunimi);
            kayttaja.setSposti(sposti);
            kayttaja.setPuh(puh);
            kayttaja.setSalasana(hashattuSalasana); // Store the hashed password
            kayttaja.setRooli("1");


            kayttajaDAO.persist(kayttaja);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rekisteröinti onnistui");
            alert.setContentText("Tervetuloa, " + etunimi + " " + sukunimi + "!");
            alert.showAndWait();

            // Open login page and close the current stage
            new LoginGui().start(new Stage());
            primaryStage.close(); // Close the registration stage
        }
    }

    // Add this setter method
    public void setKayttajaDAO(KayttajaDAO kayttajaDAO) {
        this.kayttajaDAO = kayttajaDAO;
    }

    public static void main(String[] args) { launch(args);}
}