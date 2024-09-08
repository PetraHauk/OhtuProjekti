package app;

import controller.VarausController;
import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Varaus;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;

public class MainPetra {
    public static void main(String[] args) {

        // Fetch mock users, rooms, and reservations
        List<Asiakas> users = MockData.getMockUsers();
        List<Huone> rooms = MockData.getMockRooms();

        /*
        System.out.println("Users:");
        for (Asiakas user : users) {
            System.out.println(user.getEtunimi() + " " + user.getSukunimi());
        }

        System.out.println("\nRooms:");
        for (Huone room : rooms) {
            System.out.println(room.getHuoneNro() + " | " + room.getHinta() + " | " + room.getHuoneenTyyppi());
        }

         */

        // Test the reservation system
        VarausController controller = new VarausController();
        Asiakas john = users.get(0);
        Huone room1 = rooms.get(0);

        Asiakas jane = users.get(1);
        Huone room2 = rooms.get(3);

        Asiakas john2 = users.get(2);

        // Display available rooms before creating the reservation
        System.out.println("\nAvailable Rooms (before reservation):");
        List<Huone> availableRooms = controller.vapaatHuoneet(LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 15));
        for (Huone availableRoom : availableRooms) {
            System.out.println("Room " + availableRoom.getHuoneNro() + " (" + availableRoom.getHuoneenTyyppi() + ")");
        }

        // Create a reservation
        controller.luoVaraus(room1, john, LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 15));

        List<Varaus> reservations = MockData.getMockReservations();
        System.out.println("\nReservations:");
        for (Varaus varaus : reservations) {
            System.out.println("Room: " + varaus.getHuone().getHuoneNro() + " | User: " + varaus.getAsiakas().getEtunimi() + " | Dates: " + varaus.getAlkuPvm() + " to " + varaus.getLoppuPvm());
        }

        controller.luoVaraus(room2, jane, LocalDate.of(2024, 9, 5), LocalDate.of(2024, 9, 10));

        System.out.println("\nReservations:");
        for (Varaus varaus : reservations) {
            System.out.println("Room: " + varaus.getHuone().getHuoneNro() + " | User: " + varaus.getAsiakas().getEtunimi() + " | Dates: " + varaus.getAlkuPvm() + " to " + varaus.getLoppuPvm());
        }

        // Display available rooms after creating the reservation
        System.out.println("\nAvailable Rooms (after reservation):");
        availableRooms = controller.vapaatHuoneet(LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 15));
        for (Huone availableRoom : availableRooms) {
            System.out.println("Room " + availableRoom.getHuoneNro() + " (" + availableRoom.getHuoneenTyyppi() + ")");
        }

        /*

        // Cancel a reservation
        controller.peruVaraus(reservations.get(0));

        System.out.println("\nReservations:");
        for (Varaus varaus : reservations) {
            System.out.println("Room: " + varaus.getHuone().getHuoneNro() + " | User: " + varaus.getAsiakas().getEtunimi() + " | Dates: " + varaus.getAlkuPvm() + " to " + varaus.getLoppuPvm());
        }

        // Display available rooms after cancelling the reservation
        System.out.println("\nAvailable Rooms (after cancelling reservation):");
        availableRooms = controller.vapaatHuoneet(LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 15));
        for (Huone availableRoom : availableRooms) {
            System.out.println("Room " + availableRoom.getHuoneNro() + " (" + availableRoom.getHuoneenTyyppi() + ")");
        }

        // Modify a reservation
        controller.muokkaaVarausta(reservations.get(0), room1, LocalDate.of(2024, 9, 6), LocalDate.of(2024, 9, 10));

        System.out.println("\nReservations:");
        for (Varaus varaus : reservations) {
            System.out.println("Room: " + varaus.getHuone().getHuoneNro() + " | User: " + varaus.getAsiakas().getEtunimi() + " | Dates: " + varaus.getAlkuPvm() + " to " + varaus.getLoppuPvm());
        }

        // Display available rooms after modifying the reservation
        System.out.println("\nAvailable Rooms (after modifying reservation):");
        availableRooms = controller.vapaatHuoneet(LocalDate.of(2024, 9, 5), LocalDate.of(2024, 9, 12));
        for (Huone availableRoom : availableRooms) {
            System.out.println("Room " + availableRoom.getHuoneNro() + " (" + availableRoom.getHuoneenTyyppi() + ")");
        }

         */
    }

}



