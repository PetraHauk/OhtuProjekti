package app;

import controller.KayttajaController;
import controller.LoginController;

import java.util.Scanner;

public class KayttajaHaku {

    private KayttajaController controller = new KayttajaController();
    private LoginController loginController = new LoginController();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        // Add options for registration, login, and other functionalities
        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Rekisteröidy");
            System.out.println("2. Kirjaudu sisään");
            System.out.println("3. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character from the buffer

            switch (valinta) {
//                case 1: // Registration
//                    System.out.println("Anna etunimi:");
//                    String etunimi = scanner.nextLine();
//                    System.out.println("Anna sukunimi:");
//                    String sukunimi = scanner.nextLine();
//                    System.out.println("Anna sähköposti:");
//                    String sposti = scanner.nextLine();
//                    System.out.println("Anna puhelinnumero:");
//                    String puh = scanner.nextLine();
//                    System.out.println("Anna salasana:");
//                    String salasana = scanner.nextLine();
//
//                    // Set the fields in the LoginController using the exposed getter methods
//                    loginController.getEtunimiField().setText(etunimi);
//                    loginController.getSukunimiField().setText(sukunimi);
//                    loginController.getSpostiField().setText(sposti);
//                    loginController.getPuhField().setText(puh);
//                    loginController.getSalasanaField().setText(salasana);
//
//                    // Call the register method without parameters
//                    loginController.handleRegister();
//                    break;

//                case 2: // Login
//                    System.out.println("Anna sähköposti:");
//                    String loginemail = scanner.nextLine();
//                    System.out.println("Anna salasana:");
//                    String password = scanner.nextLine();
//
//                    // Set the fields in the LoginController using the exposed getter methods
//                    loginController.getEmailIdField().setText(loginemail);
//                    loginController.getPasswordField().setText(password);
//
//                    // Call the login method without parameters
//                    loginController.handleLogin();
//                    break;

                case 3: // Exit
                    System.out.println("Lopetetaan ohjelma.");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Virheellinen valinta, yritä uudelleen.");
                    break;
            }
        }
    }
}
