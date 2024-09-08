package controller;

import model.DAO.KayttajaDAO;
import model.enteties.Kayttaja;
import org.mindrot.jbcrypt.BCrypt;

public class KayttajaController {
    private KayttajaDAO kayttajaDAO;

    public KayttajaController() {
        kayttajaDAO = new KayttajaDAO();
    }

    public void lisaaKayttaja(String etunimi, String sukunimi, String sposti, String puh, String rooli, String salasana) {
        // Hash the password using BCrypt
        String hashattuSalasana = BCrypt.hashpw(salasana, BCrypt.gensalt());

        Kayttaja kayttaja = new Kayttaja(0, etunimi, sukunimi, sposti, puh, rooli, hashattuSalasana);// kayttajaId is set to 0, as it should be auto-generated by the DB
        kayttajaDAO.persist(kayttaja);
    }

    public Kayttaja haeKayttajaById(int id) {
        return kayttajaDAO.findById(id);
    }

    public String haeSalasanaSpostilla(String sposti) {
        return kayttajaDAO.findPasswordByEmail(sposti);
    }

    public void paivitaSpostiById(int id, String sposti) {
        kayttajaDAO.updateEmailById(id, sposti);
    }

    public void vaihdaSalasanaPostilla(String sposti, String newPassword) {
        kayttajaDAO.changePasswordByEmail(sposti, newPassword);
    }

    public void poistaKayttaja(int id) {
        kayttajaDAO.removeById(id);
    }
}
