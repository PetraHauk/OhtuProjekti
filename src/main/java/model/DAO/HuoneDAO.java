package model.DAO;
import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;
import java.util.List;
import model.service.LocaleManager;

public class HuoneDAO {

    String selectedlanguage = LocaleManager.getLanguageName();
    public void persist(Huone huone) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(huone);
        em.getTransaction().commit();
    }

    public List<Huone> haeHuoneetByHotelliId(int hotelli_id) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.hotelli_id = :hotelli_id", Huone.class)
                    .setParameter("hotelli_id", hotelli_id)
                    .getResultList();
            if (!huoneet.isEmpty()) {
                return huoneet;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Huone findByRoomId(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                return huone;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Huone findByRoomNro(int huone_nro) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Huone huone = em.find(Huone.class, huone_nro);
            if (huone != null) {
                return huone;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }


    public List<Huone> findByHuoneTila(String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;

        // Valitse sarake nimen perusteella
        String selectedColumn;
        switch (selectedlanguage) {
            case "en":
                selectedColumn = "huone_tila_en";
                break;
            case "ru":
                selectedColumn = "huone_tila_ru";
                break;
            case "zh":
                selectedColumn = "huone_tila_zh";
                break;
            default:
                selectedColumn = "huone_tila_fi";
        }

        try {
            // Luo dynaaminen kysely käyttäen `selectedColumn`
            String queryStr = "SELECT h FROM Huone h WHERE h." + selectedColumn + " = :huone_tila";
            huoneet = em.createQuery(queryStr, Huone.class)
                    .setParameter("huone_tila", huone_tila)
                    .getResultList();

            if (!huoneet.isEmpty()) {
                return huoneet;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }


    public List<Huone> findByTyyppi(String huone_tyyppi) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;

        // Valitse sarake nimen perusteella
        String selectedColumn = LocaleManager.getLocalizedColumn(selectedlanguage, "huone_tyyppi");

        try {
            String queryStr = "SELECT h FROM Huone h WHERE h." + selectedColumn + " = :huone_tyyppi";
            huoneet = em.createQuery(queryStr, Huone.class)
                    .setParameter("huone_tyyppi", huone_tyyppi)
                    .getResultList();
            if (!huoneet.isEmpty()) {
                return huoneet;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void updateHuoneById(int id, int huone_nro,
                                String huone_tyyppi_fi,
                                String huone_tyyppi_en,
                                String huone_tyyppi_ru,
                                String huone_tyyppi_zh,
                                String huone_tila_fi,
                                String huone_tila_en,
                                String huone_tila_ru,
                                String huone_tila_zh,
                                double huone_hinta) {
        EntityManager em = MariaDbConnection.getInstance();
//        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput(selectedlanguage); String huoneTilaFi = (huoneTilaList != null && huoneTilaList.size() > 0) ? huoneTilaList.get(0) : "";
//        huone_tila_fi = (huoneTilaList != null && huoneTilaList.size() > 1) ? huoneTilaList.get(0) : "";
//        huone_tila_en = (huoneTilaList != null && huoneTilaList.size() > 1) ? huoneTilaList.get(1) : "";
//        huone_tila_ru = (huoneTilaList != null && huoneTilaList.size() > 2) ? huoneTilaList.get(2) : "";
//        huone_tila_zh = (huoneTilaList != null && huoneTilaList.size() > 3) ? huoneTilaList.get(3) : "";


        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_nro(huone_nro);

                huone.setHuone_tyyppi_fi(huone_tyyppi_fi);
                huone.setHuone_tyyppi_en(huone_tyyppi_en);
                huone.setHuone_tyyppi_ru(huone_tyyppi_ru);
                huone.setHuone_tyyppi_zh(huone_tyyppi_zh);
                huone.setHuone_tila_fi(huone_tila_fi);
                huone.setHuone_tila_en(huone_tila_en);
                huone.setHuone_tila_ru(huone_tila_ru);
                huone.setHuone_tila_zh(huone_tila_zh);
                huone.setHuone_hinta(huone_hinta);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateHuoneTilaById(int id, String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();

        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput(selectedlanguage); String huoneTilaFi = (huoneTilaList != null && huoneTilaList.size() > 0) ? huoneTilaList.get(0) : "";
        String huone_tyyppi_fi = (huoneTilaList != null && huoneTilaList.size() > 1) ? huoneTilaList.get(0) : "";
        String huone_tila_en = (huoneTilaList != null && huoneTilaList.size() > 1) ? huoneTilaList.get(1) : "";
        String huone_tila_ru = (huoneTilaList != null && huoneTilaList.size() > 2) ? huoneTilaList.get(2) : "";
        String huone_tila_zh = (huoneTilaList != null && huoneTilaList.size() > 3) ? huoneTilaList.get(3) : "";

        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_tila_fi(huoneTilaFi);
                huone.setHuone_tila_en(huone_tila_en);
                huone.setHuone_tila_ru(huone_tila_ru);
                huone.setHuone_tila_zh(huone_tila_zh);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeById(int huone_id) {
        VarausDAO varausDAO = new VarausDAO(); // Create an instance of VarausDAO

        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();

            // First, remove all related reservations
            varausDAO.removeByHuoneId(huone_id);

            Huone huone = em.find(Huone.class, huone_id);
            if (huone != null) {
                em.remove(huone);
                System.out.println("Huone poistettu id:llä " + huone_id);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + huone_id);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Rollback in case of an error
            }
            System.out.println("Failed to remove huone ID: " + huone_id);
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void UpdateHuoneHintaById(int id, double huone_hinta) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_hinta(huone_hinta);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void UpdateHuoneTilaById(int id, String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();

        String selectedColumn = LocaleManager.getLocalizedColumn(selectedlanguage, "huone_tila");
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_tila_fi(selectedColumn);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void UpdateHuoneTyyppiById(int id, String huone_tyyppi) {
        EntityManager em = MariaDbConnection.getInstance();
        String selectedColumn = LocaleManager.getLocalizedColumn(selectedlanguage, "huone_tyyppi");
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_tyyppi_fi(selectedColumn);
            } else {
                System.out.println("Huonetta ei löytynyt id:llä " + id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}


