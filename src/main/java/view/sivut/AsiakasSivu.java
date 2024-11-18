package view.sivut;

import controller.AsiakasController;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Asiakas;
import model.service.LocaleManager;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AsiakasSivu {
    private AsiakasController asiakasController;
    private ResourceBundle bundle;

    public void setAsiakasController(AsiakasController asiakasController) {
        this.asiakasController = asiakasController;
    }
    // Constructor to initialize dependencies
    public AsiakasSivu() {
        asiakasController = new AsiakasController();

        Locale currentLocale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public VBox createAsiakkaat() {
        VBox asiakkaatInfo = new VBox(10);
        asiakkaatInfo.getStyleClass().add("info");
        Label asiakkaatOtsikkoLabel = new Label(bundle.getString("asiakkaatOtsikkoLabelText"));
        asiakkaatOtsikkoLabel.getStyleClass().add("otsikko");
        Button addCustomerButton = new Button(bundle.getString("addCustomerButtonText"));

        TableView<Asiakas> customerTable = createCustomerTable();

        addCustomerButton.setOnAction(e -> openAddCustomerWindow(customerTable));
        populateCustomerTable(customerTable);

        asiakkaatInfo.getChildren().addAll(asiakkaatOtsikkoLabel, customerTable, addCustomerButton);
        return asiakkaatInfo;
    }

    private void openAddCustomerWindow(TableView<Asiakas> customerTable) {
        Stage addCustomerStage = new Stage();
        addCustomerStage.setTitle(bundle.getString("addCustomerStageTitle"));

        BorderPane borderPane = new BorderPane();
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.setPadding(new Insets(20));

        Label firstNameLabel = new Label(bundle.getString("firstNameLabelText"));
        TextField firstNameField = new TextField();
        firstNameField.setPromptText(bundle.getString("firstNameLabelText"));

        Label lastNameLabel = new Label(bundle.getString("lastNameLabelText"));
        TextField lastNameField = new TextField();
        lastNameField.setPromptText(bundle.getString("lastNameLabelText"));

        Label emailLabel = new Label(bundle.getString("emailLabelText"));
        TextField emailField = new TextField();
        emailField.setPromptText(bundle.getString("emailLabelText"));

        Label phoneLabel = new Label(bundle.getString("phoneLabelText"));
        TextField phoneField = new TextField();
        phoneField.setPromptText(bundle.getString("phoneLabelText"));

        Label henkiloMaaraLabel = new Label(bundle.getString("henkiloMaaraLabelText"));
        TextField henkiloMaaraField = new TextField();
        henkiloMaaraField.setPromptText(bundle.getString("henkiloMaaraLabelText"));

        Label huomioLabel = new Label(bundle.getString("huomioLabelText"));
        TextField huomioField = new TextField();
        huomioField.setPromptText(bundle.getString("huomioLabelText"));


        Button saveButton = new Button(bundle.getString("saveButtonText"));
        Button cancelButton = new Button(bundle.getString("cancelButtonText"));

        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        saveButton.setOnAction(e -> {
            try {
                asiakasController.addAsiakas(
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        Integer.parseInt(henkiloMaaraField.getText()),
                        huomioField.getText()
                );
                addCustomerStage.close();
                populateCustomerTable(customerTable);  // Päivitä asiakastaulukko
            } catch (Exception ex) {
                System.err.println(bundle.getString("addCustomerErrorText" )+ ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> addCustomerStage.close());

        borderPane.setCenter(formLayout);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(10));

        Scene scene = new Scene(borderPane, 400, 460);
        addCustomerStage.setScene(scene);
        addCustomerStage.show();
    }

    // Populate the customer table. Runs it in a thread and shows loading indicator.
    void populateCustomerTable(TableView<Asiakas> customerTable) {
        customerTable.setPlaceholder(new Label(bundle.getString("loadingText")));
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        VBox loadingBox = new VBox(loadingIndicator);
        loadingBox.setAlignment(Pos.CENTER);


        Task<List<Asiakas>> fetchCustomersTask = new Task<>() {
            @Override
            protected List<Asiakas> call() throws Exception {
                return asiakasController.findAllAsiakkaat();
            }
        };

        fetchCustomersTask.setOnSucceeded(event -> {
            List<Asiakas> customers = fetchCustomersTask.getValue();
            if (customers != null && !customers.isEmpty()) {
                customerTable.getItems().setAll(customers);
            } else {
                customerTable.setPlaceholder(new Label(bundle.getString("loadingText")));
            }
            loadingIndicator.setVisible(false);
        });

        fetchCustomersTask.setOnFailed(event -> {
            Throwable exception = fetchCustomersTask.getException();
            if (exception != null) {
                System.err.println("Failed to fetch customers: " + exception.getMessage());
                exception.printStackTrace();  // For deeper insight during debugging
            }
            customerTable.setPlaceholder(new Label(bundle.getString("FaildToFtechCustomerText")));
        });


        new Thread(fetchCustomersTask).start();
    }
    // Method to create the Customer table view
    TableView<Asiakas> createCustomerTable() {
        TableView<Asiakas> customerTable = new TableView<>();
        // Set the width of the table
        customerTable.setPrefWidth(940);
        customerTable.setPrefHeight(400);

        TableColumn<Asiakas, Integer> idColumn = new TableColumn<>(bundle.getString("idColumnText"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));


        TableColumn<Asiakas, String> firstNameColumn = new TableColumn<>(bundle.getString("firstNameLabelText"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Asiakas, String> lastNameColumn = new TableColumn<>(bundle.getString("lastNameLabelText"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Asiakas, String> emailColumn = new TableColumn<>(bundle.getString("emailLabelText"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));
        emailColumn.setMinWidth(150);

        TableColumn<Asiakas, String> phoneColumn = new TableColumn<>(bundle.getString("phoneLabelText"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));
        phoneColumn.setMinWidth(100);

        TableColumn<Asiakas, Integer> henkiloMaaraColumn = new TableColumn<>(bundle.getString("henkiloMaaraLabelText"));
        henkiloMaaraColumn.setCellValueFactory(new PropertyValueFactory<>("henkiloMaara"));

        TableColumn<Asiakas, String> huomioColumn = new TableColumn<>(bundle.getString("huomioLabelText"));
        huomioColumn.setCellValueFactory(new PropertyValueFactory<>("huomio"));
        huomioColumn.setMinWidth(200);

        // Create the "Actions" column for edit/delete
        TableColumn<Asiakas, Void> actionColumn = new TableColumn<>(bundle.getString("actionColumnText"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(bundle.getString("editButtonText"));
            private final Button deleteButton = new Button(bundle.getString("deleteButtonText"));
            private final HBox actionButtons = new HBox(editButton, deleteButton);

            {
                actionButtons.setSpacing(10);
                actionButtons.setAlignment(Pos.CENTER);

                // Muokkaa-painikeen toiminnallisuus
                editButton.setOnAction(event -> {
                    Asiakas asiakas = getTableView().getItems().get(getIndex());
                    openMuokkaaAsiakasWindow(asiakas, getTableView()); // Välitetään taulukko muokkausikkunaan
                });


                // Poista-painikeen toiminnallisuus
                deleteButton.setOnAction(event -> {
                    Asiakas asiakas = getTableView().getItems().get(getIndex());
                    poistaAsiakas(asiakas);
                    getTableView().getItems().remove(asiakas); // Poistetaan asiakas listasta
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Get localized button text
                    setGraphic(actionButtons);
                }
            }
        });

        actionColumn.setMinWidth(150);
        customerTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn, henkiloMaaraColumn, huomioColumn, actionColumn);

        return customerTable;
    }

    private void openMuokkaaAsiakasWindow(Asiakas asiakas, TableView<Asiakas> customerTable) {
        Stage muokkaaAsiakasStage = new Stage();
        muokkaaAsiakasStage.setTitle(bundle.getString("muokkaaAsiakasStageTitle"));

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label firstNameLabel = new Label(bundle.getString("firstNameLabelText"));
        TextField firstNameField = new TextField();
        firstNameField.setText(asiakas.getEtunimi());

        Label lastNameLabel = new Label(bundle.getString("lastNameLabelText"));
        TextField lastNameField = new TextField();
        lastNameField.setText(asiakas.getSukunimi());

        Label emailLabel = new Label(bundle.getString("emailLabelText"));
        TextField emailField = new TextField();
        emailField.setText(asiakas.getSposti());

        Label phoneLabel = new Label(bundle.getString("phoneLabelText"));
        TextField phoneField = new TextField();
        phoneField.setText(asiakas.getPuh());

        Label henkiloMaaraLabel = new Label(bundle.getString("henkiloMaaraLabelText"));
        TextField henkiloMaaraField = new TextField();
        henkiloMaaraField.setText(String.valueOf(asiakas.getHenkiloMaara()));

        Label huomioLabel = new Label(bundle.getString("huomioLabelText"));
        TextField huomioField = new TextField();
        huomioField.setText(asiakas.getHuomio());

        Button saveButton = new Button(bundle.getString("MuokkaaAddButtonText"));
        Button cancelButton = new Button(bundle.getString("cancelButtonText"));

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        // HBox save and cancel buttons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Toiminnallisuus napille
        saveButton.setOnAction(e -> {
            // Päivitetään asiakastiedot
            asiakasController.paivitaAsiakas(
                    asiakas.getAsiakasId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    Integer.parseInt(henkiloMaaraField.getText()), // Muunnetaan teksti kokonaisluvuksi
                    huomioField.getText()
            );

            // Päivitetään taulukko
            populateCustomerTable(customerTable);
            muokkaaAsiakasStage.close();
        });

        cancelButton.setOnAction(e -> muokkaaAsiakasStage.close());

        // Asetetaan lomake ja painikkeet BorderPaneen
        borderPane.setCenter(formLayout);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(10)); // Marginaali nappeihin

        // Aseta BorderPane kohtaukseksi ja näytä ikkuna
        Scene scene = new Scene(borderPane, 400, 500);
        muokkaaAsiakasStage.setScene(scene);
        muokkaaAsiakasStage.show(); // Tämä avaa muokkausikkunan
    }

    private void poistaAsiakas(Asiakas asiakas) {
        asiakasController.poistaAsiakas(asiakas.getAsiakasId());
    }

}
