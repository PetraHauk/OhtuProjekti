package model.enteties;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "huone_varaus")
public class Varaus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int varaus_id;

    @Column(name = "alku_pvm")
    private LocalDate alkuPvm;

    @Column(name = "loppu_pvm")
    private LocalDate loppuPvm;

    @Column(name = "huone_id", nullable = true)
    private Integer huoneId;

    @Column(name = "lasku_id")
    private int laskuId;

    @Transient
    private String nimi;

    @Transient
    private Huone huone;



    // Parametrillinen konstruktori
    public Varaus(int varaus_id, LocalDate alkuPvm, LocalDate loppuPvm, Integer huoneId, int laskuId) {
        this.varaus_id = varaus_id;
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

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    public Integer getHuoneId() {
        return huoneId;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    public void setHuoneId(Integer huoneId) {
        this.huoneId = huoneId;
    }

    public void setHuone(Huone huone) {
        this.huone = huone;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public int getHuoneNro() {
        return huone != null ? huone.getHuoneNro() : 0;
    }

    public String getNimi() {
        return nimi;
    }
}