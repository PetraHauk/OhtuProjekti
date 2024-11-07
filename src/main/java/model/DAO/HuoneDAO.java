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
        String selectedColumn = LocaleManager.getLocalColumnName(selectedlanguage, "huone_tyyppi");

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
                                String huone_tyyppi,
                                String huone_tila,
                                double huone_hinta) {
        EntityManager em = MariaDbConnection.getInstance();

        // Hanki käännetyt huone tyyppi ja tila
        List<String> huoneTypeList = LocaleManager.getLocalizedTyyppiInput(huone_tyyppi);
        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput(huone_tila);
        System.out.println("Localized Room Type List: " + huoneTypeList);
        System.out.println("Localized Room Status List: " + huoneTilaList);


        // Tarkistetaan, että lista ei ole null ja että se sisältää tarvittavat elementit
        String huoneTypeFi = huoneTypeList.get(0) ;
        String huoneTypeEn = huoneTypeList.get(1);
        String huoneTypeRu = huoneTypeList.get(2);
        String huoneTypeZh = huoneTypeList.get(3);

        String huoneTilaFi = huoneTilaList.get(0);
        String huoneTilaEn = huoneTilaList.get(1);
        String huoneTilaRu = huoneTilaList.get(2);
        String huoneTilaZh = huoneTilaList.get(3);

        System.out.println("Localized Room Type Fi: " + huoneTypeFi);
        System.out.println("Localized Room Type En: " + huoneTypeEn);


        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_nro(huone_nro);

                huone.setHuone_tyyppi_fi(huoneTypeFi);
                huone.setHuone_tyyppi_en(huoneTypeEn);
                huone.setHuone_tyyppi_ru(huoneTypeRu);
                huone.setHuone_tyyppi_zh(huoneTypeZh);
                huone.setHuone_tila_fi(huoneTilaFi);
                huone.setHuone_tila_en(huoneTilaEn);
                huone.setHuone_tila_ru(huoneTilaRu);
                huone.setHuone_tila_zh(huoneTilaZh);
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
        String huone_tyyppi_fi = huoneTilaList.get(0);
        String huone_tila_en = huoneTilaList.get(1);
        String huone_tila_ru = huoneTilaList.get(2);
        String huone_tila_zh = huoneTilaList.get(3);

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

        String selectedColumn = LocaleManager.getLocalColumnName(selectedlanguage, "huone_tila");
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
        String selectedColumn = LocaleManager.getLocalColumnName(selectedlanguage, "huone_tyyppi");
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


