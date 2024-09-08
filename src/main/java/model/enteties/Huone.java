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
        this.tila = "Vapaa";
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

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public void setHuoneNro(int numero) {
        this.numero = numero;
    }

    public void setHuoneenTyyppi(String huoneenTyyppi) {
        this.huoneenTyyppi = huoneenTyyppi;
    }

    public void setTila(String tila) {
        this.tila = tila;
    }
}
