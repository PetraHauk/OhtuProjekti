package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "hotelli")
public class Hotelli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int hotelli_id;

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

    public Hotelli(int hotelli_id, String nimi, String osoite, String kaupunki, String puh, String maa) {
        this.hotelli_id = hotelli_id;
        this.nimi = nimi;
        this.kaupunki = kaupunki;
        this.osoite = osoite;
        this.puh = puh;
        this.maa = maa;
    }

    public Hotelli() {
    }

    public int getHotelli_id() {
        return hotelli_id;
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