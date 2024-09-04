package model.enteties;

public class Asiakas {

    private static int asiakasIdCounter = 1;

    private String etunimi;
    private String sukunimi;
    private String puhelin;
    private String email;


    public Asiakas(String etunimi, String sukunimi, String puhelin, String email) {
        asiakasIdCounter++;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.puhelin = puhelin;
        this.email = email;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public String getPuhelin() {
        return puhelin;
    }

    public String getEmail() {
        return email;
    }
}
