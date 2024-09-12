package app;

import controller.LaskuController;
import model.enteties.Lasku;

import java.util.Scanner;

public class LaskuHaku {
    LaskuController controller = new LaskuController();
    Scanner scanner = new Scanner(System.in);

    public void start() {
        
        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi lasku");
            System.out.println("2. Hae lasku ID:llä");
            System.out.println("3. Päivitä lasku ID:llä");
            System.out.println("4. Poista id:llä");
            System.out.println("5. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character from the buffer

            switch (valinta) {
                case 1:
                    System.out.println("Anna maksu_status:");
                    String maksu_status = scanner.nextLine();
                    System.out.println("Anna varaus_muoto:");
                    String varaus_muoto = scanner.nextLine();
                    System.out.println("Anna valuutta:");
                    String valuutta = scanner.nextLine();

                    controller.addLasku(maksu_status, varaus_muoto, valuutta);
                    System.out.println("lasku lisätty onnistuneesti!");
                    break;

                case 2:
                    System.out.println("Anna laskun ID:");
                    int idHaku = scanner.nextInt();
                    controller.findLaskuById(idHaku);
                    break;
                case 3:
                    System.out.println("Anna laskun ID:");
                    int idPaivitys = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Anna maksu_status:");
                    String maksu_statusPaivitys = scanner.nextLine();

                    System.out.println("Anna varaus_muoto:");
                    String varaus_muotoPaivitys = scanner.nextLine();

                    System.out.println("Anna valuutta:");
                    String valuuttaPaivitys = scanner.nextLine();
                   controller.updateLaskuById(idPaivitys, maksu_statusPaivitys, varaus_muotoPaivitys, valuuttaPaivitys);
                    break;

                case 4:
                    System.out.println("Anna laskun ID:");
                    int idPoisto = scanner.nextInt();
                    controller.removeLaskuById(idPoisto);
                    break;
                default:
                    System.out.println("Virheellinen valinta, yritä uudelleen.");
                    break;
            }

        }

    }
}

