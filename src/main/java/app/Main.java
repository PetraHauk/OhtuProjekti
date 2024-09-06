package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;


public class Main {

    public static void main(String[] args) {
        KayttajaHaku kayttajaHaku = new KayttajaHaku();
        kayttajaHaku.start();

        //HoteliHaku hotelliHaku = new HoteliHaku();
        //hotelliHaku.start();
    }
}

/*



public class Main {
    public static void main(String[] args) {

        List<Asiakas> users = MockData.getMockUsers();
        List<Huone> rooms = MockData.getMockRooms();
        List<Varaus> reservations = MockData.getMockReservations();

        System.out.println("Users:");
        for (Asiakas user : users) {
            System.out.println(user.getEtunimi() + " " + user.getSukunimi());
        }

        System.out.println("Rooms:");
        for (Huone room : rooms) {
            System.out.println(room.getHuoneNro() + " | " + room.getHinta() + " | " + room.getHuoneenTyyppi());
        }

        System.out.println("Reservations:");
        for (Varaus reservation : reservations) {
            System.out.println(reservation.getHuone().getHuoneNro() +
                    " | " + reservation.getAsiakas().getEtunimi() +
                    " | " + reservation.getAlkuPvm() +
                    " | " + reservation.getLoppuPvm() +
                    " | " + reservation.getHuone().getTila());
        }
    }

 */


