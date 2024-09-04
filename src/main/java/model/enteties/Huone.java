package model.enteties;

public class Huone {

    private static int huoneIdCounter = 1;
    private int numero;
    private double hinta;
    private String huoneenTyyppi;
    private String tila;


    public Huone(int numero, double hinta, String huoneenTyyppi) {
        this.numero = numero;
        this.hinta = hinta;
        this.huoneenTyyppi = huoneenTyyppi;
        this.tila = updateVaraus();
    }

    public double getHinta() {
        return hinta;
    }

    public int getHuoneNro() {
        return numero;
    }

    public String getHuoneenTyyppi() {
        return huoneenTyyppi;
    }

    public String getTila() {
        return tila;
    }

    public String updateVaraus() {
        if (this.tila == null) {
            return "Vapaa";
        } else {
            return "Varattu";
        }
    }

}
