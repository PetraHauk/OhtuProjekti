package model.enteties;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "huone_varaus")
public class Varaus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int varaus_id;

    @Column(name = "huone_maara")
    private int huoneMaara;

    @Column(name = "alku_pvm")
    private LocalDate alkuPvm;

    @Column(name = "loppu_pvm")
    private LocalDate loppuPvm;

    @Column(name = "huone_id")
    private int huoneId;

    @Column(name = "lasku_id")
    private int laskuId;

    // Parametrillinen konstruktori
    public Varaus(int varaus_id, int huone_maara, LocalDate alkuPvm, LocalDate loppuPvm, int huoneId, int laskuId) {
        this.varaus_id = varaus_id;
        this.huoneMaara = huone_maara;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.huoneId = huoneId;
        this.laskuId = laskuId;
    }

    public Varaus() {
    }

    // Getterit ja setterit
    public int getVarausId() {
        return varaus_id;
    }

    public int getHuoneMaara() {
        return huoneMaara;
    }

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    public int getHuoneId() {
        return huoneId;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public void setHuoneMaara() {
        this.huoneMaara = huoneMaara;
    }

    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

}



