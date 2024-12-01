package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "asiakas")
public class Asiakas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    @Column(name = "asiakas_id")
    private int asiakasId;

    @Column(name = "etunimi")
    private String etunimi;

    @Column(name = "sukunimi")
    private String sukunimi;

    @Column(name = "sposti")
    private String sposti;

    @Column(name = "puh")
    private String puh;

    @Column(name = "henkilo_maara")
    private int henkiloMaara;

    @Column(name = "huomio")
    private String huomio;

    public Asiakas(int asiakasId, String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
        this.asiakasId = asiakasId;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.sposti = sposti;
        this.puh = puh;
        this.henkiloMaara = henkiloMaara;
        this.huomio = huomio;
    }

    public Asiakas() {
    }

    public int getAsiakasId() {
        return asiakasId;
    }

//    public void setAsiakasId(int asiakasId) {
//        this.asiakasId = asiakasId;
//    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getSposti() {
        return sposti;
    }

    public void setSposti(String sposti) {
        this.sposti = sposti;
    }

    public String getPuh() {
        return puh;
    }

    public void setPuh(String puh) {
        this.puh = puh;
    }

    public int getHenkiloMaara() {
        return henkiloMaara;
    }

    public void setHenkiloMaara(int henkiloMaara) {
        this.henkiloMaara = henkiloMaara;
    }

    public String getHuomio() {
        return huomio;
    }

    public void setHuomio(String huomio) {
        this.huomio = huomio;
    }

    public boolean isEmpty() {
        return false;
    }
}
