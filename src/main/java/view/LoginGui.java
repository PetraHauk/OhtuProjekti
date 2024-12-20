package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.service.LocaleManager;
import model.service.UserSession;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Locale;
import java.util.ResourceBundle;


public class LoginGui extends Application {

    private static final  Logger logger = Logger.getLogger(LoginGui.class.getName());
    private static final String LABEL_EMAIL = "label.email";
    private static final String LABEL_PASSWORD = "label.password";
    private KayttajaDAO kayttajaDAO = new KayttajaDAO();
    private ResourceBundle bundle;
    private Label loginLabel;
    private Label emailLabel;
    private Label passwordLabel;
    private Button loginButton;
    private Button registerButton;
    private TextField emailField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        // Load the ResourceBundle from LocaleManager
        bundle = LocaleManager.getBundle();
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        }
        primaryStage.setTitle(bundle.getString("title.login"));

        // Language Selection ComboBox
        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("English", "中文", "Suomi", "россия");
        languageSelector.setValue(LocaleManager.getLanguageName()); // Default selection
        languageSelector.setOnAction(e -> updateLanguage(languageSelector.getValue(), primaryStage));

        // Labels and Buttons
        loginLabel = new Label(bundle.getString("label.login"));
        emailLabel = new Label(bundle.getString(LABEL_EMAIL));
        emailField = new TextField();
        emailField.setId("emailField"); // Set the id for the emailField
        emailField.setPromptText(bundle.getString(LABEL_EMAIL));

        passwordLabel = new Label(bundle.getString(LABEL_PASSWORD));
        passwordField = new PasswordField();
        passwordField.setId("passwordField"); // Set the id for the passwordField
        passwordField.setPromptText(bundle.getString(LABEL_PASSWORD));

        loginButton = new Button(bundle.getString("button.login"));
        loginButton.setId("loginButton"); // Set the id for the loginButton
        registerButton = new Button(bundle.getString("button.register"));
        registerButton.setId("registerButton"); // Set the id for the registerButton

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
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe("Localized alert title: " + bundle.getString("alert.login.failed.title"));
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("alert.login.failed.title"));
            alert.setContentText(bundle.getString("alert.login.failed.message"));
            alert.showAndWait();
        }
    }

    private void updateLanguage(String selectedLanguage, Stage primaryStage) {
        LocaleManager.setLocale(selectedLanguage);
        bundle = LocaleManager.getBundle();
        refreshUI(primaryStage);
    }

    private void refreshUI(Stage primaryStage) {
        primaryStage.setTitle(bundle.getString("title.login"));
        loginLabel.setText(bundle.getString("label.login"));
        emailLabel.setText(bundle.getString(LABEL_EMAIL));
        passwordLabel.setText(bundle.getString(LABEL_PASSWORD));
        loginButton.setText(bundle.getString("button.login"));
        registerButton.setText(bundle.getString("button.register"));
        emailField.setPromptText(bundle.getString(LABEL_EMAIL));
        passwordField.setPromptText(bundle.getString(LABEL_PASSWORD));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
