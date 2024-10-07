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

import java.util.List;

public class AsiakasSivu {
    private AsiakasController asiakasController;

    public AsiakasSivu() {
        this.asiakasController = new AsiakasController();
    }

    public VBox createAsiakkaat() {
        VBox asiakkaatInfo = new VBox(10);
        asiakkaatInfo.getStyleClass().add("info");
        Label asiakkaatOtsikkoLabel = new Label("Asiakkaat");
        asiakkaatOtsikkoLabel.getStyleClass().add("otsikko");
        Button addCustomerButton = new Button("Lisää uusi asiakas");

        TableView<Asiakas> customerTable = createCustomerTable();

        addCustomerButton.setOnAction(e -> openAddCustomerWindow(customerTable));
        populateCustomerTable(customerTable);
        asiakkaatInfo.getChildren().addAll(asiakkaatOtsikkoLabel, customerTable, addCustomerButton);
        return asiakkaatInfo;
    }

    private void openAddCustomerWindow(TableView<Asiakas> customerTable) {
        Stage addCustomerStage = new Stage();
        addCustomerStage.setTitle("Lisää uusi asiakas");

        BorderPane borderPane = new BorderPane();
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT);
        formLayout.setPadding(new Insets(20));

        Label firstNameLabel = new Label("Etunimi:");
        TextField firstNameField = new TextField();
        Label lastNameLabel = new Label("Sukunimi:");
        TextField lastNameField = new TextField();
        Label emailLabel = new Label("Sähköposti:");
        TextField emailField = new TextField();
        Label phoneLabel = new Label("Puhelin:");
        TextField phoneField = new TextField();
        Label henkiloMaaraLabel = new Label("Henkilömäärä:");
        TextField henkiloMaaraField = new TextField();
        Label huomioLabel = new Label("Huomio:");
        TextField huomioField = new TextField();

        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        Button saveButton = new Button("Lisää uusi asiakas");
        Button cancelButton = new Button("Peruuta");

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
                System.err.println("Asiakkaan lisääminen epäonnistui: " + ex.getMessage());
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
    private void populateCustomerTable(TableView<Asiakas> customerTable) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        customerTable.getItems().clear();
        customerTable.setPlaceholder(loadingIndicator);

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
                customerTable.setPlaceholder(new Label("No customers found"));
            }
            loadingIndicator.setVisible(false);
        });

        fetchCustomersTask.setOnFailed(event -> {
            customerTable.setPlaceholder(new Label("Failed to load customer data"));
            System.err.println("Failed to fetch customers: " + fetchCustomersTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchCustomersTask).start();
    }
    // Method to create the Customer table view
    private TableView<Asiakas> createCustomerTable() {
        TableView<Asiakas> customerTable = new TableView<>();
        // Set the width of the table
        customerTable.setPrefWidth(950);
        customerTable.setPrefHeight(400);

        TableColumn<Asiakas, Integer> idColumn = new TableColumn<>("Asiakas ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));


        TableColumn<Asiakas, String> firstNameColumn = new TableColumn<>("Etunimi");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Asiakas, String> lastNameColumn = new TableColumn<>("Sukunimi");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Asiakas, String> emailColumn = new TableColumn<>("Sähköposti");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));
        emailColumn.setMinWidth(150);

        TableColumn<Asiakas, String> phoneColumn = new TableColumn<>("Puhelin");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));
        phoneColumn.setMinWidth(100);

        TableColumn<Asiakas, Integer> henkiloMaara = new TableColumn<>("Henkilömäärä");
        henkiloMaara.setCellValueFactory(new PropertyValueFactory<>("henkiloMaara"));

        TableColumn<Asiakas, String> huomio = new TableColumn<>("Huomio");
        huomio.setCellValueFactory(new PropertyValueFactory<>("huomio"));
        huomio.setMinWidth(230);

        // Create the "Actions" column for edit/delete
        TableColumn<Asiakas, Void> actionColumn = new TableColumn<>("Toiminnot");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Muokkaa");
            private final Button deleteButton = new Button("Poista");
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
                    setGraphic(actionButtons);
                }
            }
        });

        actionColumn.setMinWidth(150);

        customerTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn, henkiloMaara, huomio, actionColumn);

        return customerTable;
    }

    private void openMuokkaaAsiakasWindow(Asiakas asiakas, TableView<Asiakas> customerTable) {
        Stage muokkaaAsiakasStage = new Stage();
        muokkaaAsiakasStage.setTitle("Muokkaa asiakasta");

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label firstNameLabel = new Label("Etunimi:");
        TextField firstNameField = new TextField();
        firstNameField.setText(asiakas.getEtunimi());

        Label lastNameLabel = new Label("Sukunimi:");
        TextField lastNameField = new TextField();
        lastNameField.setText(asiakas.getSukunimi());

        Label emailLabel = new Label("Sähköposti:");
        TextField emailField = new TextField();
        emailField.setText(asiakas.getSposti());

        Label phoneLabel = new Label("Puhelin:");
        TextField phoneField = new TextField();
        phoneField.setText(asiakas.getPuh());

        Label henkiloMaaraLabel = new Label("Henkilömäärä:");
        TextField henkiloMaaraField = new TextField();
        henkiloMaaraField.setText(String.valueOf(asiakas.getHenkiloMaara()));

        Label huomioLabel = new Label("Huomio:");
        TextField huomioField = new TextField();
        huomioField.setText(asiakas.getHuomio());

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        Button saveButton = new Button("Tallenna muutokset");
        Button cancelButton = new Button("Peruuta");

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
