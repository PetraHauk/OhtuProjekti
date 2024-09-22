package app;

import controller.VarausController;
import model.enteties.Kayttaja;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class VarausHaku {
    private VarausController controller = new VarausController();
    private Scanner scanner = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void start() {
        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi varaus");
            System.out.println("2. Hae varaus_ID:llä");
            System.out.println("3. Hae lasku_ID:llä");
            System.out.println("4. Päivitä varauksen kesto varaus_ID:llä");
            System.out.println("5. Poista varaus");
            System.out.println("6. Hae kaikki varaukset");
            System.out.println("7. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Tyhjennä puskuri

            switch (valinta) {
                case 1:
                    try {
                        System.out.println("Anna huoneen määrä:");
                        int huone_maara = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Anna alkupäivämäärä (pp.kk.vvvv):");
                        String alkuPvmStr = scanner.nextLine();
                        LocalDate alkuPvm = LocalDate.parse(alkuPvmStr, formatter);

                        System.out.println("Anna loppupäivämäärä (pp.kk.vvvv):");
                        String loppuPvmStr = scanner.nextLine();
                        LocalDate loppuPvm = LocalDate.parse(loppuPvmStr, formatter);

                        System.out.println("Anna huone_ID:");
                        int huone_id = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Anna lasku_ID:");
                        int lasku_id = scanner.nextInt();
                        scanner.nextLine();

                        controller.AddVaraus(huone_maara, alkuPvm, loppuPvm, huone_id, lasku_id);
                        System.out.println("Varaus lisätty onnistuneesti!");

                    } catch (DateTimeParseException e) {
                        System.out.println("Virhe päivämäärän syötössä. Käytä muotoa pp.kk.vvvv.");
                    }
                    break;

                case 2:
                    System.out.println("Anna varauksen ID:");
                    int idHaku = scanner.nextInt();
                    scanner.nextLine();
                    controller.findByVarausId(idHaku);
                    break;

                case 3:
                    System.out.println("Anna laskun ID:");
                    int lasku_id_haku = scanner.nextInt();
                    scanner.nextLine();
                    controller.findByLaskuId(lasku_id_haku);
                    break;

                case 4:
                    try {
                        System.out.println("Anna varauksen ID:");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Tyhjennä puskuri

                        System.out.println("Anna uusi huoneen määrä:");
                        int huone_maara_uusi = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Anna uusi alkupäivämäärä (pp.kk.vvvv):");
                        String alku_pvm_uusi = scanner.nextLine();
                        LocalDate uusiAlkuPvm = LocalDate.parse(alku_pvm_uusi, formatter); // Muunnetaan LocalDateksi

                        System.out.println("Anna uusi loppupäivämäärä (pp.kk.vvvv):");
                        String loppu_pvm_uusi = scanner.nextLine();
                        LocalDate uusiLoppuPvm = LocalDate.parse(loppu_pvm_uusi, formatter); // Muunnetaan LocalDateksi

                        // Päivitä varauksen kesto
                        controller.updateVarausById(id, huone_maara_uusi, uusiAlkuPvm, uusiLoppuPvm);
                        System.out.println("Varauksen kesto päivitetty onnistuneesti!");

                    } catch (DateTimeParseException e) {
                        System.out.println("Virhe päivämäärän syötössä. Käytä muotoa pp.kk.vvvv.");
                    }
                    break;

                case 5:
                    System.out.println("Anna varauksen ID:");
                    int idPoisto = scanner.nextInt();
                    scanner.nextLine();
                    controller.RemoveVaraus(idPoisto);
                    break;

                case 6:
                    List<Varaus> varaukset =  controller.findAllVaraukset();
                    for (Varaus varaus : varaukset) {
                        printVaraus(varaus);
                    }
                case 7:
                    return;

                default:
                    System.out.println("Virheellinen valinta. Yritä uudelleen.");
                    break;
            }
        }
    }

    public void printVaraus(Varaus varaus) {
        System.out.println("varaus_id: " + varaus.getVarausId());
        System.out.println("Huonemäärä: " + varaus.getHuoneMaara());
        System.out.println("AlkuPvm: " + varaus.getAlkuPvm());
        System.out.println("loppuPvm: " + varaus.getLoppuPvm());
        System.out.println("Huone id: " + varaus.getHuoneId());
        System.out.println("lasku id: " + varaus.getLaskuId());
        System.out.println(" ");
    }
}
