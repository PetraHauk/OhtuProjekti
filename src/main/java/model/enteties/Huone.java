package model.enteties;

import jakarta.persistence.*;

@Entity
@Table(name = "huone")
public class Huone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "huone_id")
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

    @Column(name = "huone_tila_fi")
    private String huoneTilaFi;

    @Column(name = "huone_tila_en")
    private String huoneTilaEn;

    @Column(name = "huone_tila_ru")
    private String huoneTilaRu;

    @Column(name = "huone_tila_zh")
    private String huoneTilaZh;

    @Column(name = "huone_hinta")
    private double huoneHinta;

    @Column(name = "hotelli_id")
    private int hotelliId;

    // Default constructor
    public Huone() {}

    // Parameterized constructor
    public Huone(int huoneId, int huoneNro,
                 String huoneTyyppiFi, String huoneTyyppiEn, String huoneTyyppiRu, String huoneTyyppiZh,
                 String huoneTilaFi, String huoneTilaEn, String huoneTilaRu, String huoneTilaZh,
                 double huoneHinta, int hotelliId) {
        this.huoneId = huoneId;
        this.huoneNro = huoneNro;
        this.huoneTyyppiFi = huoneTyyppiFi;
        this.huoneTyyppiEn = huoneTyyppiEn;
        this.huoneTyyppiRu = huoneTyyppiRu;
        this.huoneTyyppiZh = huoneTyyppiZh;
        this.huoneTilaFi = huoneTilaFi;
        this.huoneTilaEn = huoneTilaEn;
        this.huoneTilaRu = huoneTilaRu;
        this.huoneTilaZh = huoneTilaZh;
        this.huoneHinta = huoneHinta;
        this.hotelliId = hotelliId;
    }

    // Getters
    public int getHuoneId() {
        return huoneId;
    }

    public int getHuoneNro() {
        return huoneNro;
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

    public String getHuoneTilaFi() {
        return huoneTilaFi;
    }

    public String getHuoneTilaEn() {
        return huoneTilaEn;
    }

    public String getHuoneTilaRu() {
        return huoneTilaRu;
    }

    public String getHuoneTilaZh() {
        return huoneTilaZh;
    }

    public double getHuoneHinta() {
        return huoneHinta;
    }

    public int getHotelliId() {
        return hotelliId;
    }

    public void setHuoneNro(int huoneNro) {
        this.huoneNro = huoneNro;
    }
    public void setHuoneTyyppiFi(String huoneTyyppiFi) {
        this.huoneTyyppiFi = huoneTyyppiFi;
    }

    public void setHuoneTyyppiEn(String huoneTyyppiEn) {
        this.huoneTyyppiEn = huoneTyyppiEn;
    }

    public void setHuoneTyyppiRu(String huoneTyyppiRu) {
        this.huoneTyyppiRu = huoneTyyppiRu;
    }

    public void setHuoneTyyppiZh(String huoneTyyppiZh) {
        this.huoneTyyppiZh = huoneTyyppiZh;
    }

    public void setHuoneTilaFi(String huoneTilaFi) {
        this.huoneTilaFi = huoneTilaFi;
    }

    public void setHuoneTilaEn(String huoneTilaEn) {
        this.huoneTilaEn = huoneTilaEn;
    }

    public void setHuoneTilaRu(String huoneTilaRu) {
        this.huoneTilaRu = huoneTilaRu;
    }

    public void setHuoneTilaZh(String huoneTilaZh) {
        this.huoneTilaZh = huoneTilaZh;
    }

    public void setHuoneHinta(double huoneHinta) {
        this.huoneHinta = huoneHinta;
    }

    public void setHotelliId(int hotelliId) {
        this.hotelliId = hotelliId;
    }

    // Multi-language getters
    public String getHuoneTila(String language) {
        return switch (language) {
            case "Suomi" -> getHuoneTilaFi();
            case "English" -> getHuoneTilaEn();
            case "россия" -> getHuoneTilaRu();
            case "中文" -> getHuoneTilaZh();
            default -> null;
        };
    }

    public String getHuoneTyyppi(String language) {
        return switch (language) {
            case "Suomi" -> getHuoneTyyppiFi();
            case "English" -> getHuoneTyyppiEn();
            case "россия" -> getHuoneTyyppiRu();
            case "中文" -> getHuoneTyyppiZh();
            default -> null;
        };
    }
}
