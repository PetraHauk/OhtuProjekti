package model.enteties;

import jakarta.persistence.*;

@Entity
@Table(name = "hoteli")
public class Hoteli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    //@Column(name = "hoteli_id")
    private int hoteli_id;

    @Column(name = "nimi")
    private String nimi;

    @Column(name = "kaupunki")
    private String kaupunki;

    @Column(name = "osoite")
    private String osoite;

    @Column(name = "puh")
    private String puh;

    @Column(name = "maa")
    private String maa;

    public Hoteli(int hoteli_id, String nimi, String osoite, String kaupunki, String puh, String maa) {
        this.hoteli_id = hoteli_id;
        this.nimi = nimi;
        this.kaupunki = kaupunki;
        this.osoite = osoite;
        this.puh = puh;
        this.maa = maa;
    }

    public Hoteli() {
    }

    public int getHoteli_id() {
        return hoteli_id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public String getOsoite() {
        return osoite;
    }

    public String getPuh() {
        return puh;
    }

    public String getMaa() {
        return maa;
    }

}



