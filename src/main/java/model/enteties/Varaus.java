package model.enteties;

import java.time.LocalDate;

public class Varaus {

    private static int varausIdCounter = 1;
    private Huone huone;
    private Asiakas asiakas;
    private LocalDate alkuPvm;
    private LocalDate loppuPvm;


    public Varaus(Huone huone, Asiakas asiakas, LocalDate alkuPvm, LocalDate loppuPvm) {
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

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    public void setHuone(Huone huone) {
        this.huone = huone;
    }

    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }
}
