package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class AdminGUI extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO(); // Assuming this DAO exists
    private TableView<Kayttaja> userTable;  // Declare the userTable at class level
    private String adminEmail; // Store admin email for validation



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Panel - User Management");

        // Initialize Table to Display Users
        userTable = new TableView<>();  // Initialize the userTable here
        TableColumn<Kayttaja, Integer> idColumn = new TableColumn<>("Kayttaja ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("kayttajaId")); // Use "kayttajaId"

        // Create columns for etunimi, sukunimi, and puh
        TableColumn<Kayttaja, String> etunimiColumn = new TableColumn<>("Etunimi");
        etunimiColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Kayttaja, String> sukunimiColumn = new TableColumn<>("Sukunimi");
        sukunimiColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Kayttaja, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));

        TableColumn<Kayttaja, String> puhColumn = new TableColumn<>("Puh");
        puhColumn.setCellValueFactory(new PropertyValueFactory<>("puh")); // Add the phone number column

        TableColumn<Kayttaja, String> roleColumn = new TableColumn<>("Rooli");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("rooli"));

        // Add all columns to the userTable
        userTable.getColumns().addAll(idColumn, etunimiColumn, sukunimiColumn, emailColumn, puhColumn, roleColumn);
        userTable.setItems(FXCollections.observableArrayList(loadUsers())); // Set the items

        // Form to update user information
        TextField idField = new TextField();
        idField.setPromptText("Käyttäjän ID");

        TextField etunimiField = new TextField();
        etunimiField.setPromptText("Etunimi");

        TextField sukunimiField = new TextField();
        sukunimiField.setPromptText("Sukunimi");

        TextField spostiField = new TextField();
        spostiField.setPromptText("Sposti");

        TextField puhField = new TextField();
        puhField.setPromptText("Puh");

        ComboBox<String> rooliComboBox = new ComboBox<>(FXCollections.observableArrayList("1", "2", "3"));
        rooliComboBox.setPromptText("Rooli");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Salasana");

        Button addUserButton = new Button("+");
        addUserButton.setOnAction(e -> openAddUserDialog());


        Button updateUserButton = new Button("Päivitä käyttäjä");
        updateUserButton.setOnAction(e -> updateUserById(
                Integer.parseInt(idField.getText()),
                etunimiField.getText(),
                sukunimiField.getText(),
                spostiField.getText(),
                puhField.getText(),
                rooliComboBox.getValue(),
                passwordField.getText())
        );

        // Delete Button
        Button deleteUserButton = new Button("Poista käyttäjä");
        deleteUserButton.setOnAction(e -> deleteUser());

        VBox form = new VBox(10, new Label("Muokkaa käyttäjä tietoja"), idField, etunimiField, sukunimiField, spostiField, puhField, rooliComboBox, passwordField, updateUserButton, deleteUserButton);
        form.setPadding(new Insets(10));

        // Layout
        HBox topBar = new HBox(10, addUserButton);
        topBar.setPadding(new Insets(10));
        VBox layout = new VBox(10, topBar, userTable, form);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add event handler for row selection
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Kayttaja selectedUser = newSelection;
                idField.setText(String.valueOf(selectedUser.getKayttajaId()));
                etunimiField.setText(selectedUser.getEtunimi());
                sukunimiField.setText(selectedUser.getSukunimi());
                spostiField.setText(selectedUser.getSposti());
                puhField.setText(selectedUser.getPuh());
                rooliComboBox.setValue(selectedUser.getRooli());
                // Password should not be shown for security reasons
            }
        });
        this.adminEmail = UserSession.getUsername();
    }

    // Load users from the database
    private List<Kayttaja> loadUsers() {
        return kayttajaDAO.findAllKayttaja(); // This should retrieve all users
    }

    // Update user information by ID
    private void openAddUserDialog() {
        // Create a new dialog or window for adding the user
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Lisää uusi käyttäjä");

        // Create input fields for the new user
        TextField etunimiField = new TextField();
        etunimiField.setPromptText("Etunimi");

        TextField sukunimiField = new TextField();
        sukunimiField.setPromptText("Sukunimi");

        TextField spostiField = new TextField();
        spostiField.setPromptText("Sähköposti");

        TextField puhField = new TextField();
        puhField.setPromptText("Puhelinnumero");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Salasana");

        ComboBox<String> rooliComboBox = new ComboBox<>(FXCollections.observableArrayList("1", "2", "3"));
        rooliComboBox.setPromptText("Rooli");

        Button addUserSubmitButton = new Button("Lisää käyttäjä");
        addUserSubmitButton.setOnAction(e -> {
            handleAddUser(etunimiField.getText(), sukunimiField.getText(), spostiField.getText(), puhField.getText(), passwordField.getText(), rooliComboBox.getValue(), addUserStage);
        });

        // Layout for the form
        VBox addUserForm = new VBox(10, new Label("Täytä uuden käyttäjän tiedot"), etunimiField, sukunimiField, spostiField, puhField, passwordField, rooliComboBox, addUserSubmitButton);
        addUserForm.setPadding(new Insets(20));

        // Create a new scene and set it to the stage
        Scene addUserScene = new Scene(addUserForm, 400, 400);
        addUserStage.setScene(addUserScene);
        addUserStage.show();
    }
    private void handleAddUser(String etunimi, String sukunimi, String sposti, String puh, String salasana, String rooli, Stage addUserStage) {
        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty() || rooli == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lisääminen epäonnistui");
            alert.setContentText("Täytä kaikki kohdat.");
            alert.showAndWait();
        } else if (kayttajaDAO.onkoEmailOlemassa(sposti)) {  // Check if the email already exists
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lisääminen epäonnistui");
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
            kayttaja.setSalasana(hashattuSalasana);  // Store the hashed password
            kayttaja.setRooli(rooli);  // Set the role

            // Persist the new user to the database
            kayttajaDAO.persist(kayttaja);

            // Show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Käyttäjä lisätty");
            alert.setContentText("Käyttäjä " + etunimi + " " + sukunimi + " on lisätty onnistuneesti!");
            alert.showAndWait();

            // Close the add user stage
            addUserStage.close();

            // Reload the user table with the new user added
            userTable.setItems(FXCollections.observableArrayList(loadUsers()));
        }
    }

    private void updateUserById(int id, String etunimi, String sukunimi, String sposti, String puh, String rooli, String salasana) {
        kayttajaDAO.updateKayttajaById(id, etunimi, sukunimi, sposti, puh, rooli, salasana); // Use your provided method

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Käyttäjän tietojen päivitys");
        alert.setHeaderText(null);
        alert.setContentText("Käyttäjän tiedot päivitetty onnistuneesti!");
        alert.showAndWait();

        // Reload the user table with updated information
        userTable.setItems(FXCollections.observableArrayList(loadUsers()));  // Now we use the class-level userTable
    }


    private void deleteUser() {
        Kayttaja selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showWarning("Valinta puuttuu", "Valitse poistettavaksi käyttäjä.");
            return;
        }

        // Check if the selected user is an admin (role 3)
        if ("3".equals(selectedUser.getRooli())) {
            showWarning("Poisto estetty", "Admin käyttäjää (rooli 3) ei voi poistaa.");
            return;
        }

        // Confirm deletion
        if (showConfirmation("Vahvista poisto", "Oletko varma, että haluat poistaa käyttäjän " + selectedUser.getEtunimi() + " " + selectedUser.getSukunimi() + "?")) {
            // Create a custom dialog for password input
            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Syötä salasana");
            passwordDialog.setHeaderText("Syötä adminin salasana vahvistaaksesi käyttäjän poistamisen.");

            // Create PasswordField
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Salasana");

            // Add the PasswordField to the dialog
            VBox dialogPaneContent = new VBox();
            dialogPaneContent.getChildren().add(passwordField);
            passwordDialog.getDialogPane().setContent(dialogPaneContent);

            // Add OK and Cancel buttons
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            passwordDialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

            // Handle the response from the dialog
            passwordDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return passwordField.getText(); // Return the entered password
                }
                return null; // Cancel button was pressed
            });

            // Show the dialog and handle the password
            passwordDialog.showAndWait().ifPresent(password -> {
                if (validatePassword(password)) {
                    kayttajaDAO.removeById(selectedUser.getKayttajaId());
                    showInfo("Käyttäjä poistettu", "Käyttäjä poistettu onnistuneesti!");
                    userTable.setItems(FXCollections.observableArrayList(loadUsers()));
                } else {
                    showError("Virhe", "Väärä salasana! Poisto peruttu.");
                }
            });
        }
    }



    // Validate admin password
    private boolean validatePassword(String password) {
        String savedHashedPassword = kayttajaDAO.findPasswordByEmail(adminEmail);
        return savedHashedPassword != null && BCrypt.checkpw(password, savedHashedPassword);
    }


    // Utility methods for alerts
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(message);
        confirmationAlert.setContentText("Tätä toimintoa ei voida peruuttaa!");
        confirmationAlert.setHeaderText(null);
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
