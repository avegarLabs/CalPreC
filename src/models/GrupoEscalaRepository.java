package models;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GrupoEscalaRepository {

    public static GrupoEscalaRepository instancia = null;
    private int count;
    private int batchSize = 20;

    private GrupoEscalaRepository() {
    }

    public static GrupoEscalaRepository getInstance() {
        if (instancia == null) {
            instancia = new GrupoEscalaRepository();
        }
        return instancia;
    }

    public void addGrupoEscala(GruposEscalas gruposEscalas) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;
        try {
            trx = obsession.beginTransaction();
            obsession.persist(gruposEscalas);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
    }

    public void updateGrupoEscala(GruposEscalas gruposEscalas) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;
        try {
            trx = obsession.beginTransaction();
            obsession.update(gruposEscalas);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
    }

    public void removeGrupoEscala(GruposEscalas gruposEscalas) {
        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;
        try {
            trx = obsession.beginTransaction();
            obsession.remove(gruposEscalas);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
    }


}
