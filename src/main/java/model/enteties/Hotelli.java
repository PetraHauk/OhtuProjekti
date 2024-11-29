package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "hotelli")
public class Hotelli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int hotelliId;

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

    public Hotelli(int hotelliId, String nimi, String osoite, String kaupunki, String puh, String maa) {
        this.hotelliId = hotelliId;
        this.nimi = nimi;
        this.kaupunki = kaupunki;
        this.osoite = osoite;
        this.puh = puh;
        this.maa = maa;
    }

    public Hotelli() {
    }

    public int getHotelliId() {
        return hotelliId;
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

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public void setPuh(String puh) {
        this.puh = puh;
    }

    public void setMaa(String maa) {
        this.maa = maa;
    }

    @Override
    public String toString() {
        return "Hotelli{" +
                "hotelli_id=" + hotelliId +
                ", nimi='" + nimi + '\'' +
                ", kaupunki='" + kaupunki + '\'' +
                ", osoite='" + osoite + '\'' +
                ", puh='" + puh + '\'' +
                ", maa='" + maa + '\'' +
                '}';
    }

}