package app;

import model.enteties.Asiakas;
import controller.AsiakasController;
import java.util.Scanner;

public class AsiakasHaku {
    private AsiakasController controller = new AsiakasController();
    private Scanner scanner = new Scanner(System.in);

    public void start() {

        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi asiakas");
            System.out.println("2. Hae asiakas spostilla");
            System.out.println("3. Hae asiakas nimellä");
            System.out.println("4. Päivitä asiakasn tiedot");
            System.out.println("5. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character from the buffer

            switch (valinta) {
                case 1:
                    System.out.println("Anna etunimi:");
                    String etunimi = scanner.nextLine();

                    System.out.println("Anna sukunimi:");
                    String sukunimi = scanner.nextLine();

                    System.out.println("Anna sähköposti:");
                    String sposti = scanner.nextLine();

                    System.out.println("Anna puhelinnumero:");
                    String puh = scanner.nextLine();

                    System.out.println("Anna henkilö määrä:");
                    int hMaara= scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Anna huomio tiedot:");
                    String huomio = scanner.nextLine();
                    scanner.nextLine();

                    controller.addAsiakas(etunimi, sukunimi, sposti, puh, hMaara, huomio);
                    break;

                case 2:
                    System.out.println("Anna sposti:");
                    String spostiHaku = scanner.nextLine();
                    controller.findByEmail(spostiHaku);
                    break;
                case 3:
                    System.out.println("Anna Etunimi:");
                    String etunimiHaku = scanner.nextLine();
                    System.out.println("Anna Sukunimi:");
                    String sukunimiHaku = scanner.nextLine();
                    controller.findByNimet(etunimiHaku, sukunimiHaku);
                    break;
                case 4:
                    System.out.println("Anna asiakasn ID päivittääksesi tiedot:");
                    int idPaivitysTiedot = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline character from the buffer

                    System.out.println("Anna etunimi:");
                    String etunimiPaivitys = scanner.nextLine();

                    System.out.println("Anna sukunimi:");
                    String sukunimiPaivitys = scanner.nextLine();

                    System.out.println("Anna sähköposti:");
                    String spostiPaivitys = scanner.nextLine();

                    System.out.println("Anna puhelinnumero:");
                    String puhPaivitys = scanner.nextLine();

                    System.out.println("Anna henkilö määrä:");
                    int hMaaraPaivitys = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Anna huomio tiedot:");
                    String huomioPaivitys = scanner.nextLine();
                    controller.paivitaAsiakas(idPaivitysTiedot, etunimiPaivitys, sukunimiPaivitys, spostiPaivitys, puhPaivitys, hMaaraPaivitys, huomioPaivitys);
                    break;
                case 5:
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

