package app;

// Custom class to store LaskuData for the table
public class LaskuData {
    private final int laskuId;
    private final String maksuStatus;
    private final String varausMuoto;
    private final String valuutta;
    private final String alkuPvm;
    private final String loppuPvm;
    private final int paivat;
    private final double hinta;
    private final double kokonaishinta;

    public LaskuData(int laskuId, String maksuStatus, String varausMuoto, String valuutta, String alkuPvm,
                     String loppuPvm, int paivat, double hinta, double kokonaishinta) {
        this.laskuId = laskuId;
        this.maksuStatus = maksuStatus;
        this.varausMuoto = varausMuoto;
        this.valuutta = valuutta;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.paivat = paivat;
        this.hinta = hinta;
        this.kokonaishinta = kokonaishinta;
    }

    // Getter methods
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

    public String getAlkuPvm() {
        return alkuPvm;
    }

    public String getLoppuPvm() {
        return loppuPvm;
    }

    public int getPaivat() {
        return paivat;
    }

    public double getHinta() {
        return hinta;
    }

    public double getKokonaishinta() {
        return kokonaishinta;
    }
}

