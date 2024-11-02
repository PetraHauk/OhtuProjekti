package view;

import javafx.scene.control.*;
import model.service.UserSession;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoginGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();
    private static Locale currentLocale = Locale.getDefault(); // Shared locale for both Login and Registration
    private ResourceBundle bundle;
    private Label loginLabel, emailLabel, passwordLabel;
    private Button loginButton, registerButton;

    @Override
    public void start(Stage primaryStage) {
        // Load the ResourceBundle with the current locale
        bundle = ResourceBundle.getBundle("messages", currentLocale);

        primaryStage.setTitle(bundle.getString("title.login"));

        // Language Selection ComboBox
        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("English", "Svenska", "中文", "Suomi");
        languageSelector.setValue(getLanguageName(currentLocale)); // Default selection based on currentLocale
        languageSelector.setOnAction(e -> updateLanguage(languageSelector.getValue(), primaryStage));

        loginLabel = new Label(bundle.getString("label.login"));
        loginLabel.getStyleClass().add("otsikko");

        // Create UI elements with localized text
        emailLabel = new Label(bundle.getString("label.email"));
        TextField emailField = new TextField();
        emailField.setId("emailField");
        emailField.setPromptText(bundle.getString("label.email"));

        passwordLabel = new Label(bundle.getString("label.password"));
        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        passwordField.setPromptText(bundle.getString("label.password"));

        loginButton = new Button(bundle.getString("button.login"));
        registerButton = new Button(bundle.getString("button.register"));

        // Layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(languageSelector, loginLabel, emailLabel, emailField, passwordLabel, passwordField, loginButton, registerButton);

        // Event Handlers
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText(), primaryStage));
        registerButton.setOnAction(e -> {
            primaryStage.close();
            new RegistrationGui().start(new Stage());
        });

        // Set Scene
        Scene scene = new Scene(vbox, 300, 350);
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String email, String password, Stage primaryStage) {
        String savedHashedPassword = kayttajaDAO.findPasswordByEmail(email);

        if (savedHashedPassword != null && BCrypt.checkpw(password, savedHashedPassword)) {
            String rooli = kayttajaDAO.findRooliByEmail(email);
            UserSession.setUsername(email);
            UserSession.setRooli(rooli);

            primaryStage.close();
            new OhjelmistoGUI().start(new Stage());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("alert.login.failed.title"));
            alert.setContentText(bundle.getString("alert.login.failed.message"));
            alert.showAndWait();
        }
    }

    private void updateLanguage(String selectedLanguage, Stage primaryStage) {
        switch (selectedLanguage) {
            case "English":
                currentLocale = new Locale("en", "GB");
                break;
            case "Svenska":
                currentLocale = new Locale("sv", "SE");
                break;
            case "中文":
                currentLocale = new Locale("zh", "CN");
                break;
            case "Suomi":
                currentLocale = new Locale("fi", "FI");
                break;
        }

        // Reload the ResourceBundle
        bundle = ResourceBundle.getBundle("messages", currentLocale);

        // Update text for UI elements
        primaryStage.setTitle(bundle.getString("title.login"));
        loginLabel.setText(bundle.getString("label.login"));
        emailLabel.setText(bundle.getString("label.email"));
        passwordLabel.setText(bundle.getString("label.password"));
        loginButton.setText(bundle.getString("button.login"));
        registerButton.setText(bundle.getString("button.register"));
    }

    private String getLanguageName(Locale locale) {
        if (locale.equals(new Locale("en", "GB"))) {
            return "English";
        } else if (locale.equals(new Locale("sv", "SE"))) {
            return "Svenska";
        } else if (locale.equals(new Locale("zh", "CN"))) {
            return "中文";
        } else if (locale.equals(new Locale("fi", "FI"))) {
            return "Suomi";
        }
        return "English"; // Default fallback
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
