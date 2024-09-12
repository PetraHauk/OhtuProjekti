package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "huone")
public class Huone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int huone_id;

    @Column(name = "huone_nro")
    private int huone_nro;

    @Column(name = "huone_tyyppi")
    private String huone_tyyppi;

    @Column(name = "huone_tila")
    private String huone_tila;

    @Column(name = "huone_hinta")
    private double huone_hinta;

    @Column(name = "hotelli_id")
    private int hotelli_id;

    public Huone(int huone_id, int huone_nro, String huone_tyyppi, String huone_tila, double huone_hinta, int hotelli_id) {
        this.huone_id = huone_id;
        this.huone_nro = huone_nro;
        this.huone_tyyppi = huone_tyyppi;
        this.huone_tila = huone_tila;
        this.huone_hinta = huone_hinta;
        this.hotelli_id = hotelli_id;
    }


    public Huone() {
    }

    public int getHuone_id() {
        return huone_id;
    }

    public int getHuone_nro() {
        return huone_nro;
    }

    public String getHuone_tyyppi() {
        return huone_tyyppi;
    }

    public String getHuone_tila() {
        return huone_tila;
    }

    public double getHuone_hinta() {
        return huone_hinta;
    }

    public int getHotelli_id() {
        return hotelli_id;
    }

    public void setHuone_nro(int huone_nro) {
        this.huone_nro = huone_nro;
    }

    public void setHuone_tyyppi(String huone_tyyppi) {
        this.huone_tyyppi = huone_tyyppi;
    }

    public void setHuone_tila(String huone_tila) {
        this.huone_tila = huone_tila;
    }

    public void setHuone_hinta(double huone_hinta) {
        this.huone_hinta = huone_hinta;
    }
}


