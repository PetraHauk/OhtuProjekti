package model.enteties;
import jakarta.persistence.*;

@Entity
@Table(name = "kayttaja")
public class Kayttaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tämä mahdollistaa automaattisen ID:n generoinnin
    @Column(name = "kayttaja_id")
    private int kayttajaId;

    @Column(name = "etunimi")
    private String etunimi;

    @Column(name = "sukunimi")
    private String sukunimi;

    @Column(name = "sposti")
    private String sposti;

    @Column(name = "puh")
    private String puh;

    @Column(name = "rooli")
    private String rooli;

    @Column(name = "salasana")
    private String salasana;

    public Kayttaja(int kayttajaId, String etunimi, String sukunimi, String sposti, String puh, String rooli, String salasana) {
        this.kayttajaId = kayttajaId;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.sposti = sposti;
        this.puh = puh;
        this.rooli = rooli;
        this.salasana = salasana;
    }

    public Kayttaja() {
    }

    public int getKayttajaId() {
        return kayttajaId;
    }

    public void setKayttajaId(int kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

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

    public String getRooli() {
        return rooli;
    }
    public void setRooli(String rooli) {
        this.rooli = rooli;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }
}