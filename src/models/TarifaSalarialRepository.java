package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TarifaSalarialRepository {
    public static TarifaSalarialRepository instancia = null;
    public ObservableList<TarifaSalarial> tarifaSalarialObservableList;
    private int count;
    private int batchSize = 20;


    private TarifaSalarialRepository() {
    }

    public static TarifaSalarialRepository getInstance() {
        if (instancia == null) {
            instancia = new TarifaSalarialRepository();
        }
        return instancia;
    }


    public ObservableList<TarifaSalarial> getAllTarifasSalarial() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            tarifaSalarialObservableList = FXCollections.observableArrayList();
            List<TarifaSalarial> tarifaSalarialList = session.createQuery(" FROM TarifaSalarial ").getResultList();
            tarifaSalarialObservableList.addAll(tarifaSalarialList);

            tx.commit();
            session.close();
            return tarifaSalarialObservableList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }


    public void saveAllEscalas(List<GruposEscalas> gruposEscalasList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (GruposEscalas grupos : gruposEscalasList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(grupos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAllEscalas(List<GruposEscalas> gruposEscalasList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (GruposEscalas grupos : gruposEscalasList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(grupos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Integer addNuevaTarifa(TarifaSalarial tarifa) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;
        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(tarifa);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }


    public void deleteTarifaSalarial(TarifaSalarial tarifa) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = obsession.beginTransaction();
            Integer op1 = obsession.createQuery(" DELETE FROM GruposEscalas WHERE tarifaId =: idT").setParameter("idT", tarifa.getId()).executeUpdate();
            obsession.remove(tarifa);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
    }

    public void updateTarifa(TarifaSalarial tarifa) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = obsession.beginTransaction();
            obsession.update(tarifa);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
    }


    public TarifaSalarial getTarifaById(int idTarifa) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = obsession.beginTransaction();
            TarifaSalarial tarifaSalarial = obsession.get(TarifaSalarial.class, idTarifa);
            trx.commit();
            obsession.close();
            return tarifaSalarial;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return new TarifaSalarial();
    }

    public void saveAllConceptsCalc(List<TarConceptos> gruposEscalasList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (TarConceptos grupos : gruposEscalasList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(grupos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
