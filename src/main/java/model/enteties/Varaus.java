package model.enteties;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "huone_varaus")
public class Varaus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int varaus_id;

    @Column(name = "huone_maara")
    private int huone_maara;

    @Column(name = "alku_pvm")
    @Temporal(TemporalType.DATE)  // Määritellään, että käytetään päivämäärää
    private Date alku_pvm;

    @Column(name = "loppu_pvm")
    @Temporal(TemporalType.DATE)  // Määritellään, että käytetään päivämäärää
    private Date loppu_pvm;

    @Column(name = "huone_id")
    private int huoneId;

    @Column(name = "lasku_id")
    private int laskuId;

    // Parametrillinen konstruktori
    public Varaus(int varaus_id, int huone_maara, Date alkuPvm, Date loppuPvm, int huoneId, int laskuId) {
        this.varaus_id = varaus_id;
        this.huone_maara = huone_maara;
        this.alku_pvm = alkuPvm;
        this.loppu_pvm = loppuPvm;
        this.huoneId = huoneId;
        this.laskuId = laskuId;
    }

    // Oletuskonstruktori
    public Varaus() {
    }

    // Getterit ja setterit
    public int getVaraus_id() {
        return varaus_id;
    }

    public int getHuone_maara() {
        return huone_maara;
    }

    public Date getAlku_pvm() {
        return alku_pvm;
    }

    public Date getLoppu_pvm() {
        return loppu_pvm;
    }

    public int getHuoneId() {
        return huoneId;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public void setHuone_maara(int huoneMaara) {
        this.huone_maara = huoneMaara;
    }

    public void setAlku_pvm(Date alkuPvm) {
        this.alku_pvm = alkuPvm;
    }

    public void setLoppu_pvm(Date loppuPvm) {
        this.loppu_pvm = loppuPvm;
    }

    public void setHuoneId(int huoneId) {
        this.huoneId = huoneId;
    }

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public void start() {
    }
}

