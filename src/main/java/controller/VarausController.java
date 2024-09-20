package controller;
/*
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//import app.MockData;
import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Varaus;

public class VarausController {

    public void luoVaraus(Huone huone, Asiakas asiakas, LocalDate alkuPvm, LocalDate loppuPvm) {
        if (huone == null || asiakas == null || alkuPvm == null || loppuPvm == null) {
            System.out.println("Invalid reservation data");
            return;
        }

        if (alkuPvm.isAfter(loppuPvm)) {
            System.out.println("Invalid reservation dates");
            return;
        }

        Varaus varaus = new Varaus(huone, asiakas, alkuPvm, loppuPvm);

        MockData.addReservation(varaus); // Lisätään varaus tietokantaan

        huone.setHuone_tila("Varattu");

        // Debug: Print confirmation of reservation
        // System.out.println("Reservation created for Room: " + huone.getHuoneNro() + " from " + alkuPvm + " to " + loppuPvm);
    }

    public void peruVaraus(Varaus varaus) {
        if (varaus == null) {
            System.out.println("Invalid reservation data");
            return;
        }

        MockData.getMockReservations().remove(varaus); // Poistetaan varaus tietokannasta

        varaus.getHuone().setHuone_tila("Vapaa");

        // Debug: Print confirmation of cancellation
        // System.out.println("Reservation cancelled for Room: " + varaus.getHuone().getHuoneNro() + " from " + varaus.getAlkuPvm() + " to " + varaus.getLoppuPvm());
    }

    public void muokkaaVarausta(Varaus varaus, Huone uusiHuone, LocalDate alkuPvm, LocalDate loppuPvm){
        if (varaus == null || alkuPvm == null || loppuPvm == null) {
            System.out.println("Invalid reservation data");
            return;
        }

        if (alkuPvm.isAfter(loppuPvm)) {
            System.out.println("Invalid reservation dates");
            return;
        }

        varaus.getHuone().setHuone_tila("Vapaa");
        varaus.setHuone(uusiHuone);
        varaus.setAlkuPvm(alkuPvm);
        varaus.setLoppuPvm(loppuPvm);
        varaus.getHuone().setHuone_tila("Varattu");
    }

    public List<Huone> vapaatHuoneet(LocalDate alkuPvm, LocalDate loppuPvm) {
        List<Huone> huoneet = MockData.getMockRooms();  // Haetaan kaikki huoneet Tietokannasta
        List<Huone> vapaatHuoneet = new ArrayList<>();

        if (huoneet != null) {
            for (Huone huone : huoneet) {
                if (huoneVapaa(huone, alkuPvm, loppuPvm)) {
                    vapaatHuoneet.add(huone);
                }
            }
        }
        return vapaatHuoneet;
    }

    public boolean huoneVapaa(Huone huone, LocalDate alkuPvm, LocalDate loppuPvm) {
        List<Varaus> varaukset = MockData.getMockReservations();  // Haetaan kaikki varaukset Tietokannasta

        if (varaukset != null) {
            for (Varaus varaus : varaukset) {
                if (alkuPvm.isBefore(varaus.getLoppuPvm()) && loppuPvm.isAfter(varaus.getAlkuPvm().minusDays(1))){
                    if (varaus.getHuone().getHuone_nro() == huone.getHuone_nro()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}

 */

