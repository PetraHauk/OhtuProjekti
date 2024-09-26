package app;

import model.enteties.Asiakas;
import controller.AsiakasController;

import java.util.List;
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
            System.out.println("4. Hae kaikki asiakkaat");
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
                    Asiakas asiakas = controller.findByEmail(spostiHaku);
                    printAsiakas(asiakas);
                    break;
                case 3:
                    System.out.println("Anna Etunimi:");
                    String etunimiHaku = scanner.nextLine();
                    System.out.println("Anna Sukunimi:");
                    String sukunimiHaku = scanner.nextLine();
                    List<Asiakas> asiakasByNimet =controller.findIdByNimet(etunimiHaku, sukunimiHaku);
                    for (Asiakas a : asiakasByNimet) {
                        printAsiakas(a);
                    }
                    break;
                case 4:
                    List <Asiakas> asiakkaat = controller.findAllAsiakkaat();
                    for (Asiakas a : asiakkaat) {
                        printAsiakas(a);
                    }
                    break;
                case 5:
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
                case 6:
                    System.out.println("Lopetetaan ohjelma.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Virheellinen valinta, yritä uudelleen.");
                    break;
            }
        }
    }

    public void printAsiakas(Asiakas asiakas) {
        System.out.println("Asiakas ID: " + asiakas.getAsiakasId());
        System.out.println("Etunimi: " + asiakas.getEtunimi());
        System.out.println("Sukunimi: " + asiakas.getSukunimi());
        System.out.println("Sähköposti: " + asiakas.getSposti());
        System.out.println("Puhelin: " + asiakas.getPuh());
        System.out.println("Henkilömäärä: " + asiakas.getHenkiloMaara());
        System.out.println("Huomio: " + asiakas.getHuomio());
        System.out.println();
    }
}

