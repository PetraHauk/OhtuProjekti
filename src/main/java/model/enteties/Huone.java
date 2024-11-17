
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

    @Column(name = "huone_tyyppi_fi")
    private String huone_tyyppi_fi;

    @Column(name = "huone_tyyppi_en")
    private String huone_tyyppi_en;

    @Column(name = "huone_tyyppi_ru")
    private String huone_tyyppi_ru;

    @Column(name = "huone_tyyppi_zh")
    private String huone_tyyppi_zh;

    @Column(name = "huone_tila_en")
    private String huone_tila_en;

    @Column(name = "huone_tila_ru")
    private String huone_tila_ru;

    @Column(name = "huone_tila_zh")
    private String huone_tila_zh;

    @Column(name = "huone_tila_fi")
    private String huone_tila_fi;

    @Column(name = "huone_hinta")
    private double huone_hinta;

    @Column(name = "hotelli_id")
    private int hotelli_id;

    public Huone(int huone_id, int huone_nro,
                 String huone_tyyppi_fi,  String huone_tyyppi_en,  String huone_tyyppi_ru, String huone_tyyppi_zh,
                 String huone_tila_fi, String huone_tila_en, String huone_tila_ru, String huone_tila_zh,
                 double huone_hinta, int hotelli_id) {
        this.huone_id = huone_id;
        this.huone_nro = huone_nro;
        this.huone_tyyppi_fi = huone_tyyppi_fi;
        this.huone_tyyppi_en = huone_tyyppi_en;
        this.huone_tyyppi_ru = huone_tyyppi_ru;
        this.huone_tyyppi_zh = huone_tyyppi_zh;
        this.huone_tila_fi = huone_tila_fi;
        this.huone_tila_en = huone_tila_en;
        this.huone_tila_ru = huone_tila_ru;
        this.huone_tila_zh = huone_tila_zh;
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

    public double getHuone_hinta() {
        return huone_hinta;
    }

    public int getHotelli_id() {
        return hotelli_id;
    }

    public void setHuone_nro(int huone_nro) {
        this.huone_nro = huone_nro;
    }

    public String getHuone_tyyppi_fi() {
        return huone_tyyppi_fi;
    }

    public String getHuone_tyyppi_en() {
        return huone_tyyppi_en;
    }

    public String getHuone_tyyppi_ru() {
        return huone_tyyppi_ru;
    }

    public String getHuone_tyyppi_zh() {
        return huone_tyyppi_zh;
    }

    public String getHuone_tila_fi() {
        return huone_tila_fi;
    }

    public String getHuone_tila_en() {
        return huone_tila_en;
    }

    public String getHuone_tila_ru() {
        return huone_tila_ru;
    }

    public String getHuone_tila_zh() {
        return huone_tila_zh;
    }

    public void setHuone_tyyppi_fi(String huone_tyyppi_fi) {
        this.huone_tyyppi_fi = huone_tyyppi_fi;
    }

    public void setHuone_tyyppi_en(String huone_tyyppi_en) {
        this.huone_tyyppi_en = huone_tyyppi_en;
    }

    public void setHuone_tyyppi_ru(String huone_tyyppi_ru) {
        this.huone_tyyppi_ru = huone_tyyppi_ru;
    }

    public void setHuone_tyyppi_zh(String huone_tyyppi_zh) {
        this.huone_tyyppi_zh = huone_tyyppi_zh;
    }

    public void setHuone_tila_fi(String huone_tila_fi) {
        this.huone_tila_fi = huone_tila_fi;
    }

    public void setHuone_tila_en(String huone_tila_en) {
        this.huone_tila_en = huone_tila_en;
    }

    public void setHuone_tila_ru(String huone_tila_ru) {
        this.huone_tila_ru = huone_tila_ru;
    }

    public void setHuone_tila_zh(String huone_tila_zh) {
        this.huone_tila_zh = huone_tila_zh;
    }


    public void setHuone_hinta(double huone_hinta) {
        this.huone_hinta = huone_hinta;
    }

    public void setHotelli_id(int hotelli_id) {
        this.hotelli_id = hotelli_id;
    }
}
