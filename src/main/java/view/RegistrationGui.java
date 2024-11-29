package view;

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

import java.util.Locale;
import java.util.ResourceBundle;
import model.service.LocaleManager;

public class RegistrationGui extends Application {

    private static final String TEXT_FIELD = "text-field";
    private KayttajaDAO kayttajaDAO = new KayttajaDAO();
    private ResourceBundle bundle;


    @Override
    public void start(Stage primaryStage) {
        // Use the current locale from LoginGui
        Locale locale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", locale);

        primaryStage.setTitle(bundle.getString("title.registration"));

        // Create UI elements with localized text
        TextField etunimiField = new TextField();
        etunimiField.setPromptText(bundle.getString("label.firstname"));
        etunimiField.getStyleClass().add(TEXT_FIELD);

        TextField sukunimiField = new TextField();
        sukunimiField.setPromptText(bundle.getString("label.lastname"));
        sukunimiField.getStyleClass().add(TEXT_FIELD);

        TextField spostiField = new TextField();
        spostiField.setPromptText(bundle.getString("label.email"));
        spostiField.getStyleClass().add(TEXT_FIELD);

        TextField puhField = new TextField();
        puhField.setPromptText(bundle.getString("label.phone"));
        puhField.getStyleClass().add(TEXT_FIELD);

        PasswordField salasanaField = new PasswordField();
        salasanaField.setPromptText(bundle.getString("label.password"));
        salasanaField.getStyleClass().add("password-field");

        Button registerButton = new Button(bundle.getString("button.register"));
        registerButton.getStyleClass().add("button-one");

        Button backButton = new Button(bundle.getString("button.back"));
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
        boolean isTestMode = Boolean.getBoolean("test.mode");

        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty()) {
            if (!isTestMode) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("alert.registration.failed.title"));
                alert.setContentText(bundle.getString("alert.registration.failed.empty_fields"));
                alert.showAndWait();
            }
        } else if (kayttajaDAO.onkoEmailOlemassa(sposti)) {  // Check if the email already exists
            if (!isTestMode) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("alert.registration.failed.title"));
                alert.setContentText(bundle.getString("alert.registration.failed.email_exists"));
                alert.showAndWait();
            }
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
            kayttaja.setRooli("normaali");

            kayttajaDAO.persist(kayttaja);

            if (!isTestMode) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(bundle.getString("alert.registration.success.title"));
                alert.setContentText(bundle.getString("alert.registration.success.message").replace("{0}", etunimi).replace("{1}", sukunimi));
                alert.showAndWait();
            }

            // Open login page and close the current stage
            new LoginGui().start(new Stage());
            primaryStage.close(); // Close the registration stage
        }
    }

    // Add this setter method
    public void setKayttajaDAO(KayttajaDAO kayttajaDAO) {
        this.kayttajaDAO = kayttajaDAO;
    }

    public static void main(String[] args) { launch(args); }
}
