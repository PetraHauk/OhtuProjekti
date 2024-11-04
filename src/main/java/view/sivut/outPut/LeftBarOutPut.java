package view.sivut.outPut;

import javafx.scene.control.Button;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import model.service.LocaleManager;

public class LeftBarOutPut {

    public void generateOutput(String selectedLanguage, Button frontPageButton, Button showRoomsButton, Button showCustomersButton, Button showVarauksetButton, Button checkInButton, Button checkOutButton, Button logOutButton, Button adminButton) {
        Locale locale = LocaleManager.getCurrentLocale();
        ResourceBundle resourceBundle;

        // Load resource bundle
        try {
            resourceBundle = ResourceBundle.getBundle("messages", locale);
        } catch (MissingResourceException e) {
            System.out.println("Error: Resource bundle not found for locale: " + locale);
            return;
        }

        // Get language-specific messages from resource bundle
        try {
            frontPageButton.setText(resourceBundle.getString("frontPageButtonText"));
            showRoomsButton.setText(resourceBundle.getString("showRoomsButtonText"));
            showCustomersButton.setText(resourceBundle.getString("showCustomersButtonText"));
            showVarauksetButton.setText(resourceBundle.getString("showVarauksetButtonText"));
            checkInButton.setText(resourceBundle.getString("checkInButtonText"));
            checkOutButton.setText(resourceBundle.getString("checkOutButtonText"));
            logOutButton.setText(resourceBundle.getString("logoutButtonText"));

            if (adminButton != null) {
                adminButton.setText(resourceBundle.getString("adminButtonText"));
            }
        } catch (MissingResourceException e) {
            System.out.println("Error: One or more keys not found in the resource bundle.");
        }
    }


}