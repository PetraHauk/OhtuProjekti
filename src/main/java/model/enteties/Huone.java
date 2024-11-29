
package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "huone")
public class Huone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    private int huoneId;

    @Column(name = "huone_nro")
    private int huoneNro;

    @Column(name = "huone_tyyppi_fi")
    private String huoneTyyppiFi;

    @Column(name = "huone_tyyppi_en")
    private String huoneTyyppiEn;

    @Column(name = "huone_tyyppi_ru")
    private String huoneTyyppiRu;

    @Column(name = "huone_tyyppi_zh")
    private String huoneTyyppiZh;

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

    public Huone(int huoneId, int huoneNro,
                 String huoneTyyppiFi, String huoneTyyppiEn, String huoneTyyppiRu, String huone_tyyppi_zh,
                 String huone_tila_fi, String huone_tila_en, String huone_tila_ru, String huone_tila_zh,
                 double huone_hinta, int hotelli_id) {
        this.huoneId = huoneId;
        this.huoneNro = huoneNro;
        this.huoneTyyppiFi = huoneTyyppiFi;
        this.huoneTyyppiEn = huoneTyyppiEn;
        this.huoneTyyppiRu = huoneTyyppiRu;
        this.huoneTyyppiZh = huone_tyyppi_zh;
        this.huone_tila_fi = huone_tila_fi;
        this.huone_tila_en = huone_tila_en;
        this.huone_tila_ru = huone_tila_ru;
        this.huone_tila_zh = huone_tila_zh;
        this.huone_hinta = huone_hinta;
        this.hotelli_id = hotelli_id;
    }

    public Huone() {
    }

    public int getHuoneId() {
        return huoneId;
    }

    public int getHuoneNro() {
        return huoneNro;
    }

    public double getHuone_hinta() {
        return huone_hinta;
    }

    public int getHotelli_id() {
        return hotelli_id;
    }

    public void setHuoneNro(int huone_nro) {
        this.huoneNro = huone_nro;
    }

    public String getHuoneTyyppiFi() {
        return huoneTyyppiFi;
    }

    public String getHuoneTyyppiEn() {
        return huoneTyyppiEn;
    }

    public String getHuoneTyyppiRu() {
        return huoneTyyppiRu;
    }

    public String getHuoneTyyppiZh() {
        return huoneTyyppiZh;
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

    public void setHuoneTyyppiFi(String huone_tyyppi_fi) {
        this.huoneTyyppiFi = huone_tyyppi_fi;
    }

    public void setHuoneTyyppiEn(String huone_tyyppi_en) {
        this.huoneTyyppiEn = huone_tyyppi_en;
    }

    public void setHuoneTyyppiRu(String huone_tyyppi_ru) {
        this.huoneTyyppiRu = huone_tyyppi_ru;
    }

    public void setHuoneTyyppiZh(String huone_tyyppi_zh) {
        this.huoneTyyppiZh = huone_tyyppi_zh;
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

    public String getHuoneTila(String selectedlanguage) {
        switch (selectedlanguage) {
            case "Suomi": return getHuone_tila_fi();
            case "English": return getHuone_tila_en();
            case "россия": return getHuone_tila_ru();
            case "中文": return getHuone_tila_zh();
            default: return null;
        }
    }

    public String getHuoneTyyppi(String selectedlanguage) {
        switch (selectedlanguage) {
            case "Suomi": return getHuoneTyyppiFi();
            case "English": return getHuoneTyyppiEn();
            case "россия": return getHuoneTyyppiRu();
            case "中文": return getHuoneTyyppiZh();
            default: return null;
        }
    }

}
