package view.sivut;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class OutputGenerator {
    public void generateOutput(String selectedLanguage, Label languageLabel, Button frontPageButton, Button showRoomsButton, Button showCustomersButton, Button showVarauksetButton, Button checkInButton, Button checkOutButton, Button logoutButton, Button adminButton) {
        Locale locale;

        // Create locale based on language code
        switch (selectedLanguage) {
            case "English":
                locale = new Locale("en", "US");
                break;
            case "Suomi":
                locale = new Locale("fi", "FI");
                break;
            case "中文":
                locale = new Locale("zh", "CN");
                break;
            default:
                System.out.println("Error: Unsupported language code.");
                return;
        }

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
            languageLabel.setText(resourceBundle.getString("kieliLabelText"));
            languageLabel.setFont(new Font("Arial Unicode MS", 14)); // Ensure font supports special characters
            frontPageButton.setText(resourceBundle.getString("frontPageButtonText"));
            showRoomsButton.setText(resourceBundle.getString("showRoomsButtonText"));
            showCustomersButton.setText(resourceBundle.getString("showCustomersButtonText"));
            showVarauksetButton.setText(resourceBundle.getString("showVarauksetButtonText"));
            checkInButton.setText(resourceBundle.getString("checkInButtonText"));
            checkOutButton.setText(resourceBundle.getString("checkOutButtonText"));
            logoutButton.setText(resourceBundle.getString("logoutButtonText"));

            if (adminButton != null) {
                adminButton.setText(resourceBundle.getString("adminButtonText"));
            }
        } catch (MissingResourceException e) {
            System.out.println("Error: One or more keys not found in the resource bundle.");
        }
    }

}