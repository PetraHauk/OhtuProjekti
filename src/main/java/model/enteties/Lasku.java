package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "lasku")
public class Lasku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int lasku_id;

    @Column(name = "maksu_status")
    private String maksu_status;

    @Column(name = "varaus_muoto")
    private String varaus_muoto;

    @Column(name = "valuutta")
    private String valuutta;

    @Column(name = "asiakas_id")
    private int asiakas_id;


    // Parametrillinen konstruktori
    public Lasku(int lasku_id, String maksu_status, String varaus_muoto, String valuutta, int asiakas_id) {
        this.lasku_id = lasku_id;
        this.maksu_status = maksu_status;
        this.varaus_muoto = varaus_muoto;
        this.valuutta = valuutta;
        this.asiakas_id = asiakas_id;
    }

    // Getterit ja setterit
    public int getLaskuId() {
        return lasku_id;
    }

    public String getMaksuStatus() {
        return maksu_status;
    }

    public String getVarausMuoto() {
        return varaus_muoto;
    }

    public String getValuutta() {
        return valuutta;
    }

    public int getAsiakasId() {
        return asiakas_id;
    }

    public void setMaksuStatus(String maksu_status) {
        this.maksu_status = maksu_status;
    }

    public void setVarausMuoto(String varaus_muoto) {
        this.varaus_muoto = varaus_muoto;
    }

    public void setValuutta(String valuutta) {
        this.valuutta = valuutta;
    }

    public void setAsiakasId(int asiakas_id) {
        this.asiakas_id = asiakas_id;
    }
}