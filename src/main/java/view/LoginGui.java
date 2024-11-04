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

import java.util.ResourceBundle;

public class LoginGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();
    private ResourceBundle bundle;
    private Label loginLabel, emailLabel, passwordLabel;
    private Button loginButton, registerButton;
    private TextField emailField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        // Load the ResourceBundle from LocaleManager
        bundle = LocaleManager.getBundle();
        primaryStage.setTitle(bundle.getString("title.login"));

        // Language Selection ComboBox
        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("English", "Svenska", "中文", "Suomi", "россия");
        languageSelector.setValue(LocaleManager.getLanguageName()); // Default selection
        languageSelector.setOnAction(e -> updateLanguage(languageSelector.getValue(), primaryStage));

        // Labels and Buttons
        loginLabel = new Label(bundle.getString("label.login"));
        emailLabel = new Label(bundle.getString("label.email"));
        emailField = new TextField();
        emailField.setPromptText(bundle.getString("label.email"));

        passwordLabel = new Label(bundle.getString("label.password"));
        passwordField = new PasswordField();
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
        LocaleManager.setLocale(selectedLanguage);
        bundle = LocaleManager.getBundle();
        refreshUI(primaryStage);
    }

    private void refreshUI(Stage primaryStage) {
        primaryStage.setTitle(bundle.getString("title.login"));
        loginLabel.setText(bundle.getString("label.login"));
        emailLabel.setText(bundle.getString("label.email"));
        passwordLabel.setText(bundle.getString("label.password"));
        loginButton.setText(bundle.getString("button.login"));
        registerButton.setText(bundle.getString("button.register"));
        emailField.setPromptText(bundle.getString("label.email"));
        passwordField.setPromptText(bundle.getString("label.password"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
