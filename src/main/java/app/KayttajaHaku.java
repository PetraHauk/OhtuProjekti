package app;

import model.enteties.Kayttaja;
import controller.KayttajaController;

import java.util.Scanner;

public class KayttajaHaku {

    private KayttajaController controller = new KayttajaController();
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        //Lisää käyttäjä ja hae, päivitä, poista

        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi käyttäjä");
            System.out.println("2. Hae käyttäjä ID:llä");
            System.out.println("3. Päivitä käyttäjä ID:llä");
            System.out.println("4. Vaihta salasana sähköpostilla");
            System.out.println("5. Poista käyttäjä ID:llä");
            System.out.println("6. Hae salasana sähköpostilla");
            System.out.println("7. Lopeta");

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
                    System.out.println("Anna rooli:");
                    String rooli = scanner.nextLine();
                    System.out.println("Anna salasana:");
                    String salasana = scanner.nextLine();

                    controller.lisaaKayttaja(etunimi, sukunimi, sposti, puh, rooli, salasana);
                    System.out.println("Käyttäjä lisätty onnistuneesti!");
                    break;

                case 2:
                    System.out.println("Anna käyttäjän ID:");
                    int idHaku = scanner.nextInt();
                    Kayttaja kayttaja = controller.haeKayttajaById(idHaku);
                    if (kayttaja != null) {
                        System.out.println("Käyttäjä löytyi:");
                        System.out.println("Etunimi: " + kayttaja.getEtunimi());
                        System.out.println("Sukunimi: " + kayttaja.getSukunimi());
                        System.out.println("Sähköposti: " + kayttaja.getSposti());
                        System.out.println("Puhelin: " + kayttaja.getPuh());
                        System.out.println("Rooli: " + kayttaja.getRooli());
                        System.out.println("Salasana: " + kayttaja.getSalasana());
                    } else {
                        System.out.println("Käyttäjää ei löytynyt ID:llä " + idHaku);
                    }
                    break;

                case 3:
                    System.out.println("Anna käyttäjän ID päivittääksesi tiedot:");
                    int idPaivitys = scanner.nextInt();
                    scanner.nextLine(); // Clear the newline character from the buffer

                    System.out.println("Anna uusi etunimi:");
                    String uusiEtunimi = scanner.nextLine();
                    System.out.println("Anna uusi sukunimi:");
                    String uusiSukunimi = scanner.nextLine();
                    System.out.println("Anna uusi sähköposti:");
                    String uusiSposti = scanner.nextLine();
                    System.out.println("Anna uusi puhelinnumero:");
                    String uusiPuh = scanner.nextLine();
                    System.out.println("Anna uusi rooli:");
                    String uusiRooli = scanner.nextLine();
                    System.out.println("Anna uusi salasana:");
                    String uusiSalasana = scanner.nextLine();

                    controller.paivitaKayttaja(idPaivitys, uusiEtunimi, uusiSukunimi, uusiSposti, uusiPuh, uusiRooli, uusiSalasana);
                    System.out.println("Käyttäjän tiedot päivitetty onnistuneesti!");
                    break;


                case 4:
                    System.out.println("Anna sähköposti vaihtaaksesi salasana:");
                    String kayttajaSposti = scanner.nextLine();
                    System.out.println("Anna uusi salasana:");
                    String newPassword = scanner.nextLine();
                    controller.vaihdaSalasana(kayttajaSposti, newPassword);

                    break;

                case 5:

                    System.out.println("Anna käyttäjän ID poistaaksesi käyttäjän:");
                    int idPoisto = scanner.nextInt();
                    controller.poistaKayttaja(idPoisto);
                    break;

                case 6:
                    System.out.println("Anna sähköposti:");
                    String email = scanner.nextLine();
                    Kayttaja kayttajaSalasana = controller.haeSalasanaSpostilla(email);
                    if (kayttajaSalasana != null) {
                        System.out.println("Käyttäjä löytyi:");
                        System.out.println("Salasana: " + kayttajaSalasana.getSalasana());
                    } else {
                        System.out.println("Käyttäjää ei löytynyt sähköpostilla " + email);
                    }
                    break;

                case 7:
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
