package app;

import java.time.LocalDate;

// Custom class to store LaskuData for the table
public class LaskuData {
    private final int laskuId;
    private final int hotelliId;
    private final int huoneId;
    private final String etunimi;
    private final String sukunimi;
    private final String huoneTyyppi;
    private final String maksuStatus;
    private final String varausMuoto;
    private final String valuutta;
    private final LocalDate alkuPvm;
    private final LocalDate loppuPvm;
    private final int paivat;
    private final double hinta;
    private final double summa;

    public LaskuData(int laskuId, int hotelliId, int huoneId, String etunimi, String sukunimi, String huoneTyyppi, String maksuStatus, String varausMuoto, String valuutta, LocalDate alkuPvm,
                     LocalDate loppuPvm, int paivat, double hinta, double summa) {
        this.laskuId = laskuId;
        this.hotelliId = hotelliId;
        this.huoneId = huoneId;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.huoneTyyppi = huoneTyyppi;
        this.maksuStatus = maksuStatus;
        this.varausMuoto = varausMuoto;
        this.valuutta = valuutta;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.paivat = paivat;
        this.hinta = hinta;
        this.summa = summa;
    }

    // Getter methods
    public int getLaskuId() {
        return laskuId;
    }
    public  int getHotelliId() {
        return hotelliId;
    }
    public int getHuoneId() {
        return huoneId;
    }
    public String getEtunimi() {
        return etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public String getHuoneTyyppi() {
        return huoneTyyppi;
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

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    public int getPaivat() {
        return paivat;
    }

    public double getHinta() {
        return hinta;
    }

    public double getSumma() {
        return summa;
    }
}

