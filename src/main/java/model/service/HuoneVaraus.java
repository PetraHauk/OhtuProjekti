package model.service;

public class HuoneVaraus {
    private int varausId;
    private int huoneId;
    private int asiakasId;
    private String alkuPvm;
    private String loppuPvm;

    public HuoneVaraus(int varausId, int huoneId, int asiakasId, String alkuPvm, String loppuPvm) {
        this.varausId = varausId;
        this.huoneId = huoneId;
        this.asiakasId = asiakasId;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
    }

    public int getVarausId() {
        return varausId;
    }

    public int getHuoneId() {
        return huoneId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public String getAlkuPvm() {
        return alkuPvm;
    }

    public String getLoppuPvm() {
        return loppuPvm;
    }
}
