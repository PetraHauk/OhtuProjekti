package view.sivut;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import model.enteties.Asiakas;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AsiakasOutput {
    LanguageGenerator languageGenerator = new LanguageGenerator();

    public void creatAsiakkaatOutPut(String selectedLanguage,
                                     Label asiakkaatOtsikkoLabel,
                                     Button addCustomerButton) {
        Locale locale = languageGenerator.generateLanguage(selectedLanguage);
        ResourceBundle resourceBundle;

        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {
            asiakkaatOtsikkoLabel.setText(resourceBundle.getString("asiakkaatOtsikkoLabelText"));
            addCustomerButton.setText(resourceBundle.getString("addCustomerButtonText"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
        }
    }

    public void openAddCustomerWindowOutput(String selectedLanguage,
                                            Stage addCustomerStage,
                                            Label firstNameLabel,
                                            Label lastNameLabel,
                                            Label emailLabel,
                                            Label phoneLabel,
                                            Label henkiloMaaraLabel,
                                            Label huomioLabel,
                                            Button saveButton,
                                            Button cancelButton) {

        Locale locale = languageGenerator.generateLanguage(selectedLanguage);
        ResourceBundle resourceBundle;

        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {
            addCustomerStage.setTitle(resourceBundle.getString("addCustomerStageTitle"));
            firstNameLabel.setText(resourceBundle.getString("firstNameLabelText"));
            lastNameLabel.setText(resourceBundle.getString("lastNameLabelText"));
            emailLabel.setText(resourceBundle.getString("emailLabelText"));
            phoneLabel.setText(resourceBundle.getString("phoneLabelText"));
            henkiloMaaraLabel.setText(resourceBundle.getString("henkiloMaaraLabelText"));
            huomioLabel.setText(resourceBundle.getString("huomioLabelText"));

            saveButton.setText(resourceBundle.getString("saveButtonText"));
            cancelButton.setText(resourceBundle.getString("cancelButtonText"));

        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
        }
    }

    public void actionBututtons(String selectedLanguage,
                                Button editButton,
                                Button deleteButton
    ) {

        Locale locale = languageGenerator.generateLanguage(selectedLanguage);
        ResourceBundle resourceBundle;

        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {
            editButton.setText(resourceBundle.getString("editButtonText"));
            deleteButton.setText(resourceBundle.getString("deleteButtonText"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
        }
    }

    public void createCustomerTable(String selectedLanguage,
                                    TableColumn<Asiakas, ?> idColumn,
                                    TableColumn<Asiakas, ?> firstNameColumn,
                                    TableColumn<Asiakas, ?> lastNameColumn,
                                    TableColumn<Asiakas, ?> emailColumn,
                                    TableColumn<Asiakas, ?> phoneColumn,
                                    TableColumn<Asiakas, ?> henkiloMaaraColumn,
                                    TableColumn<Asiakas, ?> huomioColumn,
                                    TableColumn<Asiakas, ?> actionColumn
                                    ) {

        Locale locale = languageGenerator.generateLanguage(selectedLanguage);
        ResourceBundle resourceBundle;

        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {

            idColumn.setText(resourceBundle.getString("idColumnText"));
            firstNameColumn.setText(resourceBundle.getString("firstNameColumnText"));
            lastNameColumn.setText(resourceBundle.getString("lastNameColumnText"));
            emailColumn.setText(resourceBundle.getString("emailColumnText"));
            phoneColumn.setText(resourceBundle.getString("phoneColumnText"));
            henkiloMaaraColumn.setText(resourceBundle.getString("henkiloMaaraColumnText"));
            huomioColumn.setText(resourceBundle.getString("huomioColumnText"));
            actionColumn.setText(resourceBundle.getString("actionColumnText"));
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
        }
    }

    public void muokkaaasukasta(String selectedLanguage,
                                Stage muokkaaAsiakasStage,
                                Label firstNameLabel,
                                Label lastNameLabel,
                                Label emailLabel,
                                Label phoneLabel,
                                Label henkiloMaaraLabel,
                                Label huomioLabel,
                                Button saveButton,
                                Button cancelButton) {

        Locale locale = languageGenerator.generateLanguage(selectedLanguage);
        ResourceBundle resourceBundle;

        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {
            muokkaaAsiakasStage.setTitle(resourceBundle.getString("muokkaaAsiakasStageTitle"));
            firstNameLabel.setText(resourceBundle.getString("firstNameLabelText"));
            lastNameLabel.setText(resourceBundle.getString("lastNameLabelText"));
            emailLabel.setText(resourceBundle.getString("emailLabelText"));
            phoneLabel.setText(resourceBundle.getString("phoneLabelText"));
            henkiloMaaraLabel.setText(resourceBundle.getString("henkiloMaaraLabelText"));
            huomioLabel.setText(resourceBundle.getString("huomioLabelText"));

            saveButton.setText(resourceBundle.getString("MuokkaaAddButtonText"));
            cancelButton.setText(resourceBundle.getString("cancelButtonText"));

        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
        }
    }

}
