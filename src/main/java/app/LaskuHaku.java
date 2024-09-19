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
            System.out.println("3. Hae asiakas ID:llä");
            System.out.println("4. Päivitä lasku ID:llä");
            System.out.println("5. Poista id:llä");
            System.out.println("6. Lopeta");

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

                    controller.addLasku(maksu_status, varaus_muoto, valuutta, 0);
                    System.out.println("lasku lisätty onnistuneesti!");
                    break;

                case 2:
                    System.out.println("Anna laskun ID:");
                    int idHaku = scanner.nextInt();
                    controller.findLaskuById(idHaku);
                    break;

                case 3:
                    System.out.println("Anna asiakas ID:");
                    int asiakas_idHaku = scanner.nextInt();
                    controller.findLaskuByAsiakasId(asiakas_idHaku);
                    break;

                case 4:
                    System.out.println("Anna laskun ID:");
                    int idPaivitys = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Anna maksu_status:");
                    String maksu_statusPaivitys = scanner.nextLine();

                    System.out.println("Anna varaus_muoto:");
                    String varaus_muotoPaivitys = scanner.nextLine();

                    System.out.println("Anna valuutta:");
                    String valuuttaPaivitys = scanner.nextLine();

                    System.out.println("Anna asiakas_id:");
                    int asiakas_idPaivitys = scanner.nextInt();
                    controller.updateLaskuById(idPaivitys, maksu_statusPaivitys, varaus_muotoPaivitys, valuuttaPaivitys, asiakas_idPaivitys);
                    break;

                case 5:
                    System.out.println("Anna laskun ID:");
                    int idPoisto = scanner.nextInt();
                    controller.removeLaskuById(idPoisto);
                    break;

                case 6:
                    System.out.println("Ohjelma lopetetaan.");
                    return;
                default:
                    System.out.println("Virheellinen valinta, yritä uudelleen.");
                    break;
            }
        }
    }
}

