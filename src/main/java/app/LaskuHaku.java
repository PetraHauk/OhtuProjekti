package app;

import controller.LaskuController;
import model.enteties.Lasku;

import java.util.List;
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
            System.out.println("4. Hae kaikki laskut");
            System.out.println("5. Päivitä lasku ID:llä");
            System.out.println("6. Poista id:llä");
            System.out.println("7. Lopeta");

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
                    Lasku laskuByLaskuId = controller.findLaskuById(idHaku);
                    printLasku(laskuByLaskuId);
                    break;

                case 3:
                    System.out.println("Anna asiakas ID:");
                    int asiakas_idHaku = scanner.nextInt();
                    Lasku laskuByAsiakasId = (Lasku) controller.findLaskuByAsiakasId(asiakas_idHaku);
                    printLasku(laskuByAsiakasId);
                    break;
                case 4:
                    List<Lasku> laskut = controller.findAllLaskut();
                    for (Lasku lasku : laskut) {
                        printLasku(lasku);
                    }
                    break;

                case 5:
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

                case 6:
                    System.out.println("Anna laskun ID:");
                    int idPoisto = scanner.nextInt();
                    controller.removeLaskuById(idPoisto);
                    break;

                case 7:
                    System.out.println("Ohjelma lopetetaan.");
                    return;
                default:
                    System.out.println("Virheellinen valinta, yritä uudelleen.");
                    break;
            }
        }
    }

    public void printLasku(Lasku lasku) {
        if (lasku != null) {
            System.out.println("Lasku ID: " + lasku.getLaskuId());
            System.out.println("Asiakas ID: " + lasku.getAsiakasId());
            System.out.println("Maksu status: " + lasku.getMaksuStatus());
            System.out.println("Valuutta: " + lasku.getValuutta());
            System.out.println("Varaus muoto: " + lasku.getVarausMuoto());
            System.out.println();
        } else {
            System.out.println("Laskua ei löytynyt.");
        }
    }

}

