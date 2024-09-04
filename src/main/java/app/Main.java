package app;

import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Varaus;

import java.util.List;

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
}
