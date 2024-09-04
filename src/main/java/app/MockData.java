package app;

import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Varaus;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<Asiakas> getMockUsers() {
        List<Asiakas> users = new ArrayList<>();
        users.add(new Asiakas("John", "Doe", "0401234567", "example@sähköposti.com"));
        users.add(new Asiakas("Jane", "Doe", "0401234567", "example@sähköposti.com"));
        return users;
    }

    public static List<Huone> getMockRooms() {
        List<Huone> rooms = new ArrayList<>();
        rooms.add(new Huone(101, 100.0, "Standard"));
        rooms.add(new Huone(102, 200.0, "Suite"));
        return rooms;
    }

    public static List<Varaus> getMockReservations() {
        List<Varaus> reservations = new ArrayList<>();
        reservations.add(new Varaus(getMockRooms().get(0), getMockUsers().get(0), "2021-01-01", "2021-01-08"));
        reservations.add(new Varaus(getMockRooms().get(1), getMockUsers().get(1), "2021-01-01", "2021-01-02"));
        return reservations;


}
}
