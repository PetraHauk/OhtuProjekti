package app;

import model.enteties.*;
import controller.HoteliController;

import java.util.Scanner;

public class HoteliHaku {
    HoteliController hoteli_controller = new HoteliController();
    Scanner scanner = new Scanner(System.in);

    public void start() {
        //lisää hoteli ja hae
        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi hoteli");
            System.out.println("2. Hae hoteli ID:llä");
            //System.out.println("3. Poista id:llä");
            System.out.println("4. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character from the buffer

            switch (valinta) {
                case 1:
                    System.out.println("Anna hoteli nimi:");
                    String nimi = scanner.nextLine();
                    System.out.println("Anna osoite:");
                    String osoite = scanner.nextLine();
                    System.out.println("Anna kaupunki:");
                    String kaupunki = scanner.nextLine();
                    System.out.println("Anna puhelinnumero:");
                    String puh = scanner.nextLine();
                    System.out.println("Anna maa:");
                    String maa = scanner.nextLine();

                    hoteli_controller.lisaaHoteli(nimi, osoite, kaupunki, puh, maa);
                    System.out.println("Hoteli lisätty onnistuneesti!");
                    break;

                case 2:
                    System.out.println("Anna hotellin ID:");
                    int idHaku = scanner.nextInt();
                    Hoteli hotelli = hoteli_controller.haeHoteliById(idHaku);
                    if (hotelli != null) {
                        System.out.println("Hotelli löytyi:");
                        System.out.println("Nimi: " + hotelli.getNimi());
                        System.out.println("Osoite: " + hotelli.getOsoite());
                        System.out.println("Kaupunki: " + hotelli.getKaupunki());
                        System.out.println("Puhelin: " + hotelli.getPuh());
                        System.out.println("Maa: " + hotelli.getMaa());
                    } else {
                        System.out.println("Hotellia ei löytynyt ID:llä " + idHaku);
                    }
                    break;

                /*case 3:
                    System.out.println("Anna hotellin ID:");
                    int idPoisto = scanner.nextInt();
                     hoteli_controller.poistaHoteli(idPoisto);

                    break;

                 */
                case 4:
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
