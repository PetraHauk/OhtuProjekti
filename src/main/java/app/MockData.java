package app;

import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Varaus;

import java.util.ArrayList;
import java.util.List;

public class MockData {

    private static List<Varaus> mockReservations = new ArrayList<>();
    private static List<Huone> rooms = new ArrayList<>();
    private static List<Asiakas> customers = new ArrayList<>();

    public static List<Varaus> getMockReservations() {
        return mockReservations;
    }

    public static void addReservation(Varaus varaus) {
        mockReservations.add(varaus);
    }


    public static List<Asiakas> getMockUsers() {
        customers.add(new Asiakas("John", "Doe", "0401234567", "example@sähköposti.com"));
        customers.add(new Asiakas("Jane", "Doe", "0401234567", "example@sähköposti.com"));
        customers.add(new Asiakas("John", "Smith", "0401234567", "example@sähköposti.com"));
        customers.add(new Asiakas("Jane", "Smith", "0401234567", "example@sähköposti.com"));
        return customers;
    }

    public static void addAsiakas(Asiakas asiakas) {
        customers.add(asiakas);
    }

    public static List<Huone> getMockRooms() {
        rooms.add(new Huone(101, 100.0, "Standard"));
        rooms.add(new Huone(102, 200.0, "Suite"));
        rooms.add(new Huone(103, 200.0, "Suite"));
        rooms.add(new Huone(104, 100.0, "Standard"));
        rooms.add(new Huone(105, 200.0, "Suite"));
        rooms.add(new Huone(106, 100.0, "Standard"));
        return rooms;
    }

    public static void addHuone(Huone huone) {
        rooms.add(huone);
    }

    public static void removeHuone(Huone huone) {
        rooms.remove(huone);
    }
}
