package app;

import controller.HuoneController;
import model.enteties.Huone;
import java.util.Scanner;

public class HuoneHaku {

    private HuoneController controller = new HuoneController();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        //Lisää huone ja hae, päivitä, poista

        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi huone");
            System.out.println("2. Hae huone_ID:llä");
            System.out.println("3. Hae huoneen tyypillä");
            System.out.println("4. Hae huoneen tilalla");
            System.out.println("5. Päivitä huoneen tiedot huone_ID:llä");
            System.out.println("6. Poista huone");
            System.out.println("7. Hae huoneet hotelli_id:llä");
            System.out.println("8. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Tyhjennä puskuri

            switch (valinta) {
                case 1:
                    System.out.println("Anna huoneen numero:");
                    int huone_nro = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Anna huoneen tyyppi:");
                    String huone_tyyppi = scanner.nextLine();
                    System.out.println("Anna huoneen tila:");
                    String huone_tila = scanner.nextLine();
                    System.out.println("Anna huoneen hinta:");
                    double huone_hinta = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Anna hotelli ID:");
                    int hotelli_id = scanner.nextInt();
                    scanner.nextLine();

                    controller.lisaaHuone(huone_nro, huone_tyyppi, huone_tila, huone_hinta, hotelli_id);
                    System.out.println("Huone lisätty onnistuneesti!");
                    break;

                case 2:
                    System.out.println("Anna huoneen ID:");
                    int idHaku = scanner.nextInt();
                    scanner.nextLine();
                    controller.findHuoneById(idHaku);

                    break;

                case 3:
                    System.out.println("Anna huoneen tyyppi:");
                    String tyyppiHaku = scanner.nextLine();
                    controller.findHuoneByTyyppi(tyyppiHaku);
                    break;

                case 4:
                    System.out.println("Anna huoneen tila:");
                    String tilaHaku = scanner.nextLine();
                    controller.findHuoneByTila(tilaHaku);
                    break;

                case 5:
                    System.out.println("Anna huoneen ID:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Anna uusi huoneen numero:");
                    int uusiHuoneNro = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Anna uusi huoneen tyyppi:");
                    String uusiHuoneTyyppi = scanner.nextLine();
                    scanner.nextLine();
                    System.out.println("Anna uusi huoneen tila:");
                    String uusiHuoneTila = scanner.nextLine();
                    scanner.nextLine();
                    System.out.println("Anna uusi huoneen hinta:");
                    double uusiHuoneHinta = scanner.nextDouble();
                    scanner.nextLine();
                    controller.updateHuoneById(id, uusiHuoneNro, uusiHuoneTyyppi, uusiHuoneTila, uusiHuoneHinta);
                    break;

                case 6:
                    System.out.println("Anna poistettavan huoneen ID:");
                    int id3 = scanner.nextInt();
                    scanner.nextLine();
                    controller.deleteHuone(id3);
                    break;
                case 7:
                    System.out.println("Anna hotelli ID:");
                    int hotelliId = scanner.nextInt();
                    scanner.nextLine();
                    controller.FindHuoneetByHoteliId(hotelliId);
                    break;

                case 8:
                    System.out.println("Ohjelma lopetetaan.");
                    return;
            }
        }
    }


            /*

            if (valinta == 8) {
                break;
            } else if (valinta == 1) {
                System.out.println("Anna huoneen numero:");
                int huone_nro = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Anna huoneen tyyppi:");
                String huone_tyyppi = scanner.nextLine();
                System.out.println("Anna huoneen tila:");
                String huone_tila = scanner.nextLine();
                System.out.println("Anna huoneen hinta:");
                double huone_hinta = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Anna hotelli ID:");
                int hotelli_id = scanner.nextInt();
                scanner.nextLine();

                controller.lisaaHuone(huone_nro, huone_tyyppi, huone_tila, huone_hinta, hotelli_id);

            } else if (valinta == 2) {
                System.out.println("Anna huoneen ID:");
                int idHaku = scanner.nextInt();
                scanner.nextLine();
                Huone huone = controller.findHuoneById(idHaku);
                if (huone != null) {
                    printHuone(huone);
                } else {
                    System.out.println("Huonetta ei löytynyt ID:llä " + idHaku);
                }
            } else if (valinta == 3) {
                System.out.println("Anna huoneen tyyppi:");
                String tyyppiHaku = scanner.nextLine();
                Huone huone = controller.findHuoneByTyyppi(tyyppiHaku);
                if (huone != null) {
                    printHuone(huone);
                } else {
                    System.out.println("Huonetta ei löytynyt tyypillä " + tyyppiHaku);
                }
            } else if (valinta == 4) {
                System.out.println("Anna huoneen tila:");
                String tilaHaku = scanner.nextLine();
                Huone huone = controller.findHuoneByTila(tilaHaku);
                if (huone != null) {
                    printHuone(huone);
                } else {
                    System.out.println("Huonetta ei löytynyt tilalla " + tilaHaku);
                }
            } else if (valinta == 5) {
                System.out.println("Anna huoneen ID:");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Anna uusi hinta:");
                double uusiHinta = scanner.nextDouble();
                scanner.nextLine();
                controller.updateHuoneHintaById(id, uusiHinta);
            } else if (valinta == 6) {
                System.out.println("Anna huoneen ID:");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Anna uusi tila:");
                String uusiTila = scanner.nextLine();
                controller.updateHuoneTilaById(id, uusiTila);
            } else if (valinta == 7) {
                System.out.println("Anna poistettavan huoneen ID:");
                int id = scanner.nextInt();
                scanner.nextLine();
                controller.deleteHuone(id);
            } else {
                System.out.println("Virheellinen valinta!");
            }
        }
    }

             */

    public void printHuone(Huone huone) {
        System.out.println("Huoneen numero: " + huone.getHuone_nro());
        System.out.println("Huoneen tyyppi: " + huone.getHuone_tyyppi());
        System.out.println("Huoneen tila: " + huone.getHuone_tila());
        System.out.println("Huoneen hinta: " + huone.getHuone_hinta());
        System.out.println("Hotelli ID: " + huone.getHotelli_id());
    }
}
