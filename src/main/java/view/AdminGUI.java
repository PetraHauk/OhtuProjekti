package view;

import model.service.LocaleManager;
import model.service.UserSession;
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
import java.util.Locale;
import java.util.ResourceBundle;


public class AdminGUI extends Application {

    KayttajaDAO kayttajaDAO = new KayttajaDAO();
    TableView<Kayttaja> userTable;
    private String adminEmail; // Store admin email for validation
    private ResourceBundle bundle;


    @Override
    public void start(Stage primaryStage) {
        //Locale locale = Locale.getDefault();  // Change to preferred locale, e.g., new Locale("fi")
        Locale locale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", locale);

        primaryStage.setTitle(bundle.getString("admin_panel_title"));

        // Initialize Table to Display Users
        userTable = new TableView<>();  // Initialize the userTable here
        TableColumn<Kayttaja, Integer> idColumn = new TableColumn<>(bundle.getString("user_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("kayttajaId"));

        TableColumn<Kayttaja, String> etunimiColumn = new TableColumn<>(bundle.getString("first_name"));
        etunimiColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Kayttaja, String> sukunimiColumn = new TableColumn<>(bundle.getString("last_name"));
        sukunimiColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Kayttaja, String> emailColumn = new TableColumn<>(bundle.getString("email"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));

        TableColumn<Kayttaja, String> puhColumn = new TableColumn<>(bundle.getString("phone"));
        puhColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));

        TableColumn<Kayttaja, String> roleColumn = new TableColumn<>(bundle.getString("role"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("rooli"));

        userTable.getColumns().addAll(idColumn, etunimiColumn, sukunimiColumn, emailColumn, puhColumn, roleColumn);
        loadAndDisplayUsers();


        // Form to update user information
        TextField idField = new TextField();
        idField.setPromptText(bundle.getString("label.user.id"));
        idField.setId("idField");

        TextField etunimiField = new TextField();
        etunimiField.setId("etunimiField");
        etunimiField.setPromptText(bundle.getString("label.firstname"));

        TextField sukunimiField = new TextField();
        sukunimiField.setId("sukunimiField");
        sukunimiField.setPromptText(bundle.getString("label.lastname"));

        TextField spostiField = new TextField();
        spostiField.setId("spostiField");
        spostiField.setPromptText(bundle.getString("label.email"));

        TextField puhField = new TextField();
        puhField.setId("puhField");
        puhField.setPromptText(bundle.getString("label.phone"));

        ComboBox<String> rooliComboBox = new ComboBox<>(FXCollections.observableArrayList(
                bundle.getString("role.cleaner"),
                bundle.getString("role.worker"),
                bundle.getString("role.admin")
        ));
        rooliComboBox.setPromptText(bundle.getString("label.role"));
        rooliComboBox.setId("rooliComboBox");

        PasswordField passwordField = new PasswordField();
        passwordField.setId("salasanaField");
        passwordField.setPromptText(bundle.getString("label.password"));

        Button addUserButton = new Button(bundle.getString("button.add.user"));
        addUserButton.setId("addUserButton");
        addUserButton.setOnAction(e -> openAddUserDialog());


        Button updateUserButton = new Button(bundle.getString("button.update.user"));
        updateUserButton.setId("updateUserButton");
        updateUserButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            String etunimi = etunimiField.getText();
            String sukunimi = sukunimiField.getText();
            String puh = puhField.getText();
            String rooli = rooliComboBox.getValue();
            String sposti = spostiField.getText();
            String salasana = passwordField.getText();

            // Update user details except email and password
            kayttajaDAO.updateKayttajaById(id, etunimi, sukunimi, puh, rooli);

            // Update email if provided
            if (!sposti.isEmpty()) {
                kayttajaDAO.updateEmailById(id, sposti);
            }

            // Update password if provided
            if (!salasana.isEmpty()) {
                kayttajaDAO.changePasswordByEmail(sposti, salasana);
            }

            // Refresh the user table
            loadAndDisplayUsers();
        });
                // Delete Button
        Button deleteUserButton = new Button(bundle.getString("button.delete.user"));
        deleteUserButton.setOnAction(e -> deleteUser());

        // Form layout with labels
        VBox form = new VBox(10,
                new Label(bundle.getString("label.edit.user")),
                new Label(bundle.getString("label.user.id")), idField,
                new Label(bundle.getString("label.firstname")), etunimiField,
                new Label(bundle.getString("label.lastname")), sukunimiField,
                new Label(bundle.getString("label.email")), spostiField,
                new Label(bundle.getString("label.phone")), puhField,
                new Label(bundle.getString("label.role")), rooliComboBox,
                new Label(bundle.getString("label.password")), passwordField,
                updateUserButton, deleteUserButton);
        form.setPadding(new Insets(10));

        // Layout
        HBox topBar = new HBox(10, addUserButton);
        topBar.setPadding(new Insets(10));
        VBox layout = new VBox(10, topBar, userTable, form);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 800);
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
    List<Kayttaja> loadUsers() {
        return kayttajaDAO.findAllKayttaja(); // This should retrieve all users
    }

    // Update user information by ID
    private void openAddUserDialog() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle(bundle.getString("addUser.title"));

        TextField etunimiField = new TextField();
        etunimiField.setId("firstnameField");
        etunimiField.setPromptText(bundle.getString("addUser.firstName"));

        TextField sukunimiField = new TextField();
        sukunimiField.setId("lastnameField");
        sukunimiField.setPromptText(bundle.getString("addUser.lastName"));

        TextField spostiField = new TextField();
        spostiField.setId("emailField");
        spostiField.setPromptText(bundle.getString("addUser.email"));

        TextField puhField = new TextField();
        puhField.setId("phoneField");
        puhField.setPromptText(bundle.getString("addUser.phone"));

        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        passwordField.setPromptText(bundle.getString("addUser.password"));

        ComboBox<String> rooliComboBox = new ComboBox<>(FXCollections.observableArrayList(
                bundle.getString("role.cleaner"),
                bundle.getString("role.worker"),
                bundle.getString("role.admin")
        ));
        rooliComboBox.setPromptText(bundle.getString("addUser.role"));
        rooliComboBox.setId("roleComboBox");

        Button addUserSubmitButton = new Button(bundle.getString("addUser.submitButton"));
        addUserSubmitButton.setId("addUserSubmitButton");
        addUserSubmitButton.setOnAction(e ->
                handleAddUser(
                        etunimiField.getText(),
                        sukunimiField.getText(),
                        spostiField.getText(),
                        puhField.getText(),
                        passwordField.getText(),
                        rooliComboBox.getValue(),
                        addUserStage
                )
        );

        VBox addUserForm = new VBox(10,
                new Label(bundle.getString("addUser.formTitle")),
                new Label(bundle.getString("addUser.firstNameLabel")), etunimiField,
                new Label(bundle.getString("addUser.lastNameLabel")), sukunimiField,
                new Label(bundle.getString("addUser.emailLabel")), spostiField,
                new Label(bundle.getString("addUser.phoneLabel")), puhField,
                new Label(bundle.getString("addUser.passwordLabel")), passwordField,
                new Label(bundle.getString("addUser.roleLabel")), rooliComboBox,
                addUserSubmitButton
        );
        addUserForm.setPadding(new Insets(20));

        Scene addUserScene = new Scene(addUserForm, 400, 500);
        addUserStage.setScene(addUserScene);
        addUserStage.show();
    }
    private void handleAddUser(String etunimi, String sukunimi, String sposti, String puh, String salasana, String rooli, Stage addUserStage) {


        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty() || rooli == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("addUser.errorTitle"));
            alert.setContentText(bundle.getString("addUser.fillAllFields"));
            alert.showAndWait();
        } else if (kayttajaDAO.onkoEmailOlemassa(sposti)) {  // Check if the email already exists
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("addUser.errorTitle"));
            alert.setContentText(bundle.getString("addUser.emailExists"));
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
            alert.setTitle(bundle.getString("addUser.successTitle"));
            alert.setContentText(String.format(bundle.getString("addUser.successMessage"), etunimi, sukunimi));
            alert.showAndWait();

            // Close the add user stage
            addUserStage.close();

            // Reload the user table with the new user added
            loadAndDisplayUsers();
        }
    }


    void updateUserById(int id, String etunimi, String sukunimi, String puh, String rooli) {



        // Update the user information in the database
        kayttajaDAO.updateKayttajaById(id, etunimi, sukunimi, puh, rooli);

        // Show an information alert after successful update
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("updateUser.title"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString("updateUser.successMessage"));
        alert.showAndWait();

        // Reload the user table with updated information
        loadAndDisplayUsers();
    }



    void deleteUser() {

        Kayttaja selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showWarning(bundle.getString("deleteUser.noSelectionTitle"), bundle.getString("deleteUser.noSelectionMessage"));
            return;
        }

        // Check if the selected user is an admin
        if ("Admin".equals(selectedUser.getRooli())) {
            showWarning(bundle.getString("deleteUser.adminDeletionBlockedTitle"), bundle.getString("deleteUser.adminDeletionBlockedMessage"));
            return;
        }

        // Confirm deletion
        String confirmMessage = String.format(bundle.getString("deleteUser.confirmMessage"), selectedUser.getEtunimi(), selectedUser.getSukunimi());
        if (showConfirmation(bundle.getString("deleteUser.confirmTitle"), confirmMessage)) {
            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle(bundle.getString("deleteUser.passwordDialogTitle"));
            passwordDialog.setHeaderText(bundle.getString("deleteUser.passwordDialogHeader"));

            // Create PasswordField
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText(bundle.getString("deleteUser.passwordPrompt"));

            // Add PasswordField to dialog
            VBox dialogPaneContent = new VBox();
            dialogPaneContent.getChildren().add(passwordField);
            passwordDialog.getDialogPane().setContent(dialogPaneContent);

            // Add OK and Cancel buttons
            ButtonType okButton = new ButtonType(bundle.getString("okButton"), ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType(bundle.getString("cancelButton"), ButtonBar.ButtonData.CANCEL_CLOSE);
            passwordDialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

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
                    showInfo(bundle.getString("deleteUser.successTitle"), bundle.getString("deleteUser.successMessage"));
                    loadAndDisplayUsers();
                } else {
                    showError(bundle.getString("deleteUser.errorTitle"), bundle.getString("deleteUser.errorMessage"));
                }
            });
        }
    }



    // Validate admin password
    boolean validatePassword(String password) {
        String savedHashedPassword = kayttajaDAO.findPasswordByEmail(adminEmail);
        return savedHashedPassword != null && BCrypt.checkpw(password, savedHashedPassword);
    }


    // Utility methods for alerts
    private void showWarning(String titleKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(bundle.getString(titleKey));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(messageKey));
        alert.showAndWait();
    }

    private void showInfo(String titleKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString(titleKey));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(messageKey));
        alert.showAndWait();
    }


    private void showError(String titleKey, String messageKey) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString(titleKey));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(messageKey));
        alert.showAndWait();
    }

    private boolean showConfirmation(String titleKey, String messageKey) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(bundle.getString(titleKey));
        confirmationAlert.setHeaderText(bundle.getString(messageKey));
        confirmationAlert.setContentText(bundle.getString("alert_confirm_delete_warning"));
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    private void loadAndDisplayUsers() {
        userTable.setItems(FXCollections.observableArrayList(loadUsers())); // Load users here
    }



    public static void main(String[] args) {
        launch(args);
    }
}
