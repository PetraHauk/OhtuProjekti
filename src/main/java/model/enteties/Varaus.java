package model.enteties;

public class Varaus {

    private static int varausIdCounter = 1;
    private Huone huone;
    private Asiakas asiakas;
    private String alkuPvm;
    private String loppuPvm;


    public Varaus(Huone huone, Asiakas asiakas, String alkuPvm, String loppuPvm) {
        varausIdCounter++;
        this.huone = huone;
        this.asiakas = asiakas;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
    }

    public Huone getHuone() {
        return huone;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public String getAlkuPvm() {
        return alkuPvm;
    }

    public String getLoppuPvm() {
        return loppuPvm;
    }
}
