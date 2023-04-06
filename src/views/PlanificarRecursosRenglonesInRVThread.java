package views;

import models.Bajoespecificacionrv;
import models.ConnectionModel;
import models.Planrecrv;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PlanificarRecursosRenglonesInRVThread extends Thread {

    private static SessionFactory sf;
    public Integer idUnid;
    public Integer idCer;
    public Integer idreng;
    public Date ini;
    public Date fin;
    public Double cantidad;
    public Double valToPlan;
    private EntityManager manager;
    private ArrayList<Bajoespecificacionrv> listofMateriales;
    private Bajoespecificacionrv bajo;


    private ArrayList<Planrecrv> planrecuoArrayList;


    private double costMaterial;
    private double costMaterialCert;


    private double valCM;

    private Date desdeDate;
    private Date hastaData;

    private double cantidadCert;
    private double cantidadPlan;

    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valCostCertBajo;

    private int count;
    private int batchSize = 10;

    public PlanificarRecursosRenglonesInRVThread(int unidadId, Integer idcertP, Integer idReng, Date desdeDateP, Date hastaDataP, Double cant, Double valTo) {
        idUnid = unidadId;
        idCer = idcertP;
        idreng = idReng;
        ini = desdeDateP;
        fin = hastaDataP;
        cantidad = cant;
        valToPlan = valTo;

    }

    @Override
    public void run() {
        planrecuoArrayList = new ArrayList<>();
        planrecuoArrayList = planificaRecursos(idUnid, idCer, idreng, ini, fin, cantidad, valToPlan);
        savePlaRec(planrecuoArrayList);

    }

    private ArrayList<Planrecrv> planificaRecursos(int unidadId, Integer idcertP, int idRV, Date desdeDateP, Date hastaDataP, double cantidad, double valToPlan) {
        planrecuoArrayList = new ArrayList<>();
        listofMateriales = new ArrayList<>();
        listofMateriales = getSuministrosBajoEsp(unidadId, idRV);

        for (Bajoespecificacionrv item : listofMateriales) {

            cantCertRecBajo = 0.0;
            valCostCertBajo = 0.0;
            valCM = 0.0;
            costMaterial = 0.0;
            cantCertRecBajo = getCantCertificadaBajo(unidadId, item.getRenglonvarianteId(), item.getIdsuministro());
            valCostCertBajo = getCostCertificadaBajo(unidadId, item.getRenglonvarianteId(), item.getIdsuministro());
            dispRecBajo = item.getCantidad() - cantCertRecBajo;
            System.out.println(dispRecBajo + " ** " + item.getCantidad() + " - " + cantCertRecBajo);
            Planrecrv planrecrv = new Planrecrv();
            planrecrv.setNivelespId(unidadId);
            planrecrv.setRenglonId(item.getRenglonvarianteId());
            planrecrv.setRecursoId(item.getIdsuministro());
            planrecrv.setPlanId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = cantidad * dispRecBajo / valToPlan;
            System.out.println(cantidad + " * " + dispRecBajo + " / " + valToPlan + " --> " + item.getTipo());
            planrecrv.setCantidad(cantidadCert);
            valCM = item.getCosto() - valCostCertBajo;
            costMaterial = valCM * cantidadCert / dispRecBajo;
            System.out.println(" Costo Material : " + costMaterial + " *-* " + item.getTipo());
            planrecrv.setCosto(costMaterial);
            planrecrv.setCmateriales(0.0);
            planrecrv.setCmano(0.0);
            planrecrv.setCequipo(0.0);
            planrecrv.setSalario(0.0);
            planrecrv.setSalariocuc(0.0);
            planrecrv.setFini(desdeDateP);
            planrecrv.setFfin(hastaDataP);
            planrecrv.setTipo(item.getTipo());

            planrecuoArrayList.add(planrecrv);

        }
        return planrecuoArrayList;
    }


    public ArrayList<Bajoespecificacionrv> getSuministrosBajoEsp(int idUnidadObra, int idReng) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listofMateriales = new ArrayList<>();
            ArrayList<Bajoespecificacionrv> matrialesInPresupuesto = (ArrayList<Bajoespecificacionrv>) session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: id AND renglonvarianteId =: idreng").setParameter("id", idUnidadObra).setParameter("idreng", idReng).getResultList();

            for (Bajoespecificacionrv items : matrialesInPresupuesto) {
                bajo = getResourseChange(idUnidadObra, idReng, items.getIdsuministro());

                if (bajo != null) {
                    listofMateriales.add(bajo);
                } else if (bajo == null) {
                    listofMateriales.add(items);
                }

                bajo = null;

            }

            List<Object[]> queryIns = session.createQuery(" SELECT uo.id, rec.id, uor.renglonvarianteId, SUM(uor.cantidad * rvr.cantidas), rec.preciomn, rec.tipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.id =: idU AND uor.renglonvarianteId =: idRen AND rec.tipo != '1' GROUP BY uo.id, rec.id, rec.preciomn, rec.tipo, uor.renglonvarianteId ").setParameter("idU", idUnidadObra).setParameter("idRen", idReng).getResultList();
            for (Object[] row : queryIns) {
                bajo = new Bajoespecificacionrv();
                bajo.setId(1);
                bajo.setNivelespecificoId(Integer.parseInt(row[0].toString()));
                bajo.setRenglonvarianteId(Integer.parseInt(row[2].toString()));
                bajo.setIdsuministro(Integer.parseInt(row[1].toString()));
                bajo.setCantidad(Double.parseDouble(row[3].toString()));
                bajo.setCosto(Double.parseDouble(row[3].toString()) * Double.parseDouble(row[4].toString()));
                bajo.setTipo(row[5].toString());
                listofMateriales.add(bajo);

            }

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listofMateriales;
    }

    private Bajoespecificacionrv getResourseChange(int idUnidadObra, int idRe, int idSuministro) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            List<Object[]> queryIns = session.createQuery("SELECT crc.nivelId, rec.id, crc.cantidad,  crc.precio, rec.tipo FROM Cambioscertificacionesrv crc INNER JOIN Recursos rec ON crc.recChangeId = rec.id WHERE crc.nivelId =: idUn AND crc.renglonId =: idR AND crc.recursoId =: idRe ").setParameter("idUn", idUnidadObra).setParameter("idR", idRe).setParameter("idRe", idSuministro).getResultList();
            for (Object[] row : queryIns) {
                bajo = new Bajoespecificacionrv();
                bajo.setId(1);
                bajo.setNivelespecificoId(Integer.parseInt(row[0].toString()));
                bajo.setRenglonvarianteId(idRe);
                bajo.setIdsuministro(Integer.parseInt(row[1].toString()));
                bajo.setCantidad(Math.round(Double.parseDouble(row[2].toString())) * 100d / 100d);
                bajo.setCosto(Math.round(Double.parseDouble(row[2].toString()) * Double.parseDouble(row[3].toString())) * 100d / 100d);
                bajo.setTipo(row[4].toString());

            }

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return bajo;

    }

    public Double getCantCertificadaBajo(int idniv, int idReng, int idRecurso) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            javax.persistence.Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecrv WHERE nivelespId =: id AND renglonId =: idR AND recursoId =: idRec").setParameter("id", idniv).setParameter("idR", idReng).setParameter("idRec", idRecurso);

            if (query.getSingleResult() != null) {
                cantCertRecBajo = (Double) query.getSingleResult();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantCertRecBajo;
    }

    private void savePlaRec(ArrayList<Planrecrv> planrecuoArrayList) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            count = 0;
            for (Planrecrv planrecuo : planrecuoArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(planrecuo);

            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public Double getCostCertificadaBajo(int idNiv, int idReng, int idRec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            valCostCertBajo = 0.0;
            javax.persistence.Query query = session.createQuery("SELECT SUM(costo) FROM Certificacionrecrv WHERE nivelespId =: id AND renglonId =: idR AND recursoId =: idRec").setParameter("id", idNiv).setParameter("idR", idReng).setParameter("idRec", idRec);

            if (query.getSingleResult() != null) {
                valCostCertBajo = (Double) query.getSingleResult();
            } else {
                valCostCertBajo = 0.0;
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valCostCertBajo;
    }

}
