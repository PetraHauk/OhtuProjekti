package app;

import controller.VarausController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class VarausHaku {
    private VarausController controller = new VarausController();
    private Scanner scanner = new Scanner(System.in);
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");  // Päivämääräformaatti

    public void start() {
        while (true) {
            System.out.println("Valitse toiminto:");
            System.out.println("1. Lisää uusi varaus");
            System.out.println("2. Hae varaus_ID:llä");
            System.out.println("3. Hae lasku_ID:llä");
            System.out.println("4. Päivitä varauksen kesto varaus_ID:llä");
            System.out.println("5. Poista varaus");
            System.out.println("6. Lopeta");

            int valinta = scanner.nextInt();
            scanner.nextLine(); // Tyhjennä puskuri

            switch (valinta) {
                case 1:
                    try {
                        System.out.println("Anna huoneen määrä:");
                        int huone_maara = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Anna alkupäivämäärä (pp.kk.vvvv):");
                        String alkuPvmInput = scanner.nextLine();
                        Date alku_pvm = formatter.parse(alkuPvmInput);  // Muunnetaan Dateksi

                        System.out.println("Anna loppupäivämäärä (pp.kk.vvvv):");
                        String loppuPvmInput = scanner.nextLine();
                        Date loppu_pvm = formatter.parse(loppuPvmInput);  // Muunnetaan Dateksi

                        System.out.println("Anna huone_ID:");
                        int huone_id = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Anna lasku_ID:");
                        int lasku_id = scanner.nextInt();
                        scanner.nextLine();

                        controller.AddVaraus(huone_maara, alku_pvm, loppu_pvm, huone_id, lasku_id);
                        System.out.println("Varaus lisätty onnistuneesti!");
                    } catch (ParseException e) {
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
                        scanner.nextLine();

                        System.out.println("Anna uusi alkupäivämäärä (pp.kk.vvvv):");
                        String alku_pvm_uusi = scanner.nextLine();
                        Date uusiAlkuPvm = formatter.parse(alku_pvm_uusi);

                        System.out.println("Anna uusi loppupäivämäärä (pp.kk.vvvv):");
                        String loppu_pvm_uusi = scanner.nextLine();
                        Date uusiLoppuPvm = formatter.parse(loppu_pvm_uusi);

                        controller.updateVarausDurationById(id, uusiAlkuPvm, uusiLoppuPvm);
                    } catch (ParseException e) {
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
                    return;

                default:
                    System.out.println("Virheellinen valinta. Yritä uudelleen.");
                    break;
            }
        }
    }
}
