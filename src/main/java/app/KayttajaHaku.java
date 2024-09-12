package app;

import model.enteties.Kayttaja;
import controller.KayttajaController;
import java.util.Scanner;

public class KayttajaHaku {

    private KayttajaController controller = new KayttajaController();
    private Scanner scanner = new Scanner(System.in);

    public void start() {

        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi käyttäjä");
            System.out.println("2. Hae käyttäjä ID:llä");
            System.out.println("3. Päivitä sposti käyttäjä ID:llä");
            System.out.println("4. Vaihta salasana sähköpostilla");
            System.out.println("5. Poista käyttäjä ID:llä");
            System.out.println("6. Hae salasana sähköpostilla");
            System.out.println("7. Päivitä käyttäjän tiedot");
            System.out.println("8. Lopeta");

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
                    String rooli = scanner.nextLine();
                    System.out.println("Anna salasana:");
                    String salasana = scanner.nextLine();
                    controller.lisaaKayttaja(etunimi, sukunimi, sposti, puh, rooli, salasana);

                    break;

                case 2:
                    System.out.println("Anna käyttäjän ID:");
                    int idHaku = scanner.nextInt();
                    controller.haeKayttajaById(idHaku);
                    break;

                case 3:
                    System.out.println("Anna käyttäjän ID päivittääksesi sposti:");
                    int idPaivitys = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline character from the buffer

                    System.out.println("Anna uusi sähköposti:");
                    String uusiSposti = scanner.nextLine();
                    controller.paivitaSpostiById(idPaivitys, uusiSposti);
                    break;
                case 4:
                    System.out.println("Anna sähköposti vaihtaaksesi salasana:");
                    String kayttajaSposti = scanner.nextLine();
                    System.out.println("Anna uusi salasana:");
                    String newPassword = scanner.nextLine();
                    controller.vaihdaSalasanaPostilla(kayttajaSposti, newPassword);
                    break;
                case 5:
                    System.out.println("Anna käyttäjän ID poistaaksesi käyttäjän:");
                    int idPoisto = scanner.nextInt();
                    controller.poistaKayttaja(idPoisto);
                    break;
                case 6:
                    System.out.println("Anna sähköposti hakeaksesi salasana:");
                    String spostiHaku = scanner.nextLine();
                    String salasanaHaku = controller.haeSalasanaSpostilla(spostiHaku);
                    if (salasanaHaku != null) {
                        System.out.println("Salasana löytyi:");
                        System.out.println("Salasana: " + salasanaHaku);
                    } else {
                        System.out.println("Salasanaa ei löytynyt sähköpostilla: " + spostiHaku);
                    }
                    break;
                case 7:
                    System.out.println("Anna käyttäjän ID päivittääksesi tiedot:");
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
                    System.out.println("Anna rooli:");
                    String rooliPaivitys = scanner.nextLine();
                    System.out.println("Anna salasana:");
                    String salasanaPaivitys = scanner.nextLine();
                    controller.paivitaKayttaja(idPaivitysTiedot, etunimiPaivitys, sukunimiPaivitys, spostiPaivitys, puhPaivitys, rooliPaivitys, salasanaPaivitys);
                    break;
                case 8:
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

