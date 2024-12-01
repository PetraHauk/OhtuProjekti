package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "lasku")
public class Lasku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    @Column(name = "lasku_id")
    private int laskuId;

    @Column(name = "maksu_status")
    private String maksuStatus;

    @Column(name = "varaus_muoto")
    private String varausMuoto;

    @Column(name = "valuutta")
    private String valuutta;

    @Column(name = "asiakas_id")
    private int asiakasId;


    // Parametrillinen konstruktori
    public Lasku(int laskuId, String maksuStatus, String varausMuoto, String valuutta, int asiakasId) {
        this.laskuId = laskuId;
        this.maksuStatus = maksuStatus;
        this.varausMuoto = varausMuoto;
        this.valuutta = valuutta;
        this.asiakasId = asiakasId;
    }


    public Lasku() {
    }

    // Getterit ja setterit
    public int getLaskuId() {
        return laskuId;
    }

    public String getMaksuStatus() {
        return maksuStatus;
    }

    public String getVarausMuoto() {
        return varausMuoto;
    }

    public String getValuutta() {
        return valuutta;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setMaksuStatus(String maksuStatus) {
        this.maksuStatus = maksuStatus;
    }

    public void setVarausMuoto(String varausMuoto) {
        this.varausMuoto = varausMuoto;
    }

    public void setValuutta(String valuutta) {
        this.valuutta = valuutta;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }


}




