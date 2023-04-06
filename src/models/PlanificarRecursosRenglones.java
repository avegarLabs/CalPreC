package models;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PlanificarRecursosRenglones {

    private static PlanificarRecursosRenglones instancia = null;
    public Integer idUnid;
    public Integer idCer;
    public Date ini;
    public Date fin;
    public Double cantidad;
    public Double valToPlan;
    private ArrayList<Bajoespecificacion> listofMateriales;
    private Bajoespecificacion bajo;
    private ArrayList<Planrecuo> planrecuoArrayList;
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

    private PlanificarRecursosRenglones() {
    }

    public static PlanificarRecursosRenglones getInstance() {
        if (instancia == null) {
            instancia = new PlanificarRecursosRenglones();
        }
        return instancia;
    }

    public void cargarDatos(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, Double cant, Double valTo) {
        idUnid = unidadId;
        idCer = idcertP;
        ini = desdeDateP;
        fin = hastaDataP;
        cantidad = cant;
        valToPlan = valTo;

        planrecuoArrayList = new ArrayList<>();
        planrecuoArrayList = planificarecursosVariante(idUnid, idCer, ini, fin, cantidad, valToPlan);
        savePlaRec(planrecuoArrayList);

    }


    public ArrayList<Planrecuo> planificarecursosVariante(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, Double cant, Double valToPlan) {

        planrecuoArrayList = new ArrayList<>();
        listofMateriales = new ArrayList<>();
        listofMateriales = getSuministrosBajoEsp(unidadId);

        for (Bajoespecificacion item : listofMateriales) {
            cantCertRecBajo = 0.0;
            valCostCertBajo = 0.0;
            valCM = 0.0;
            costMaterial = 0.0;
            cantCertRecBajo = getCantCertificadaBajo(unidadId, item.getIdSuministro());
            valCostCertBajo = getCostCertificadaBajo(unidadId, item.getIdSuministro());
            dispRecBajo = item.getCantidad() - cantCertRecBajo;
            Planrecuo planrecuo = new Planrecuo();
            planrecuo.setUnidadobraId(unidadId);
            planrecuo.setRenglonId(0);
            planrecuo.setRecursoId(item.getIdSuministro());
            planrecuo.setPlanId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = cant * dispRecBajo / valToPlan;
            planrecuo.setCantidad(Math.round(cantidadCert * 10000d) / 10000d);
            valCM = item.getCosto() - valCostCertBajo;
            costMaterial = valCM * cantidadCert / dispRecBajo;
            planrecuo.setCosto(Math.round(costMaterial * 100d) / 100d);
            planrecuo.setCmateriales(0.0);
            planrecuo.setCmano(0.0);
            planrecuo.setCequipo(0.0);
            planrecuo.setSalario(0.0);
            planrecuo.setSalariocuc(0.0);
            planrecuo.setFini(desdeDateP);
            planrecuo.setFfin(hastaDataP);
            planrecuo.setTipo(item.getTipo());

            planrecuoArrayList.add(planrecuo);

        }
        return planrecuoArrayList;
    }


    public ArrayList<Bajoespecificacion> getSuministrosBajoEsp(int idUnidadObra) {

        listofMateriales = new ArrayList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            listofMateriales = new ArrayList<>();
            listofMateriales = (ArrayList<Bajoespecificacion>) session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: id AND sumrenglon != 2").setParameter("id", idUnidadObra).getResultList();

            List<Object[]> noMatreialesList = session.createQuery("SELECT uo.id, rec.id, SUM(uor.cantRv * rvr.cantidas), rec.preciomn, rec.tipo FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.id =: idU AND rec.tipo != '1' GROUP BY uo.id, rec.id, rec.preciomn, rec.tipo ").setParameter("idU", idUnidadObra).getResultList();
            for (Object[] row : noMatreialesList) {
                bajo = new Bajoespecificacion();
                bajo.setId(1);
                bajo.setUnidadobraId(Integer.parseInt(row[0].toString()));
                bajo.setRenglonvarianteId(1);
                bajo.setIdSuministro(Integer.parseInt(row[1].toString()));
                bajo.setCantidad(Math.round(Double.parseDouble(row[2].toString())) * 1000d / 1000d);
                bajo.setCosto(Math.round(Double.parseDouble(row[2].toString()) * Double.parseDouble(row[3].toString())) * 100d / 100d);
                bajo.setTipo(row[4].toString());
                bajo.setSumrenglon(1);

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

    private Bajoespecificacion getResourseChange(int idUnidadObra, int idSuministro) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            List<Object[]> materialesChange = session.createQuery("SELECT crc.unidadId, rec.id, crc.cantidad,  crc.precio, rec.tipo FROM Cambioscertificacionesuo crc INNER JOIN Recursos rec ON crc.recChangeId = rec.id WHERE crc.unidadId =: idUn AND crc.recursoId =: idRe ").setParameter("idUn", idUnidadObra).setParameter("idRe", idSuministro).getResultList();
            for (Object[] row : materialesChange) {
                bajo = new Bajoespecificacion();
                bajo.setId(1);
                bajo.setUnidadobraId(Integer.parseInt(row[0].toString()));
                bajo.setRenglonvarianteId(1);
                bajo.setIdSuministro(Integer.parseInt(row[1].toString()));
                bajo.setCantidad(Math.round(Double.parseDouble(row[2].toString())) * 1000d / 1000d);
                bajo.setCosto(Math.round(Double.parseDouble(row[2].toString()) * Double.parseDouble(row[3].toString())) * 100d / 100d);
                bajo.setTipo(row[4].toString());
                bajo.setSumrenglon(1);
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

    public Double getCantCertificadaBajo(int idUnidadObra, int idRecurso) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo WHERE unidadobraId =: id AND recursoId =: idR").setParameter("id", idUnidadObra).setParameter("idR", idRecurso);


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


    private void savePlaRec(ArrayList<Planrecuo> planrecuoArrayList) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            count = 0;
            for (Planrecuo planrecuo : planrecuoArrayList) {
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

    public Double getCostCertificadaBajo(int idUnidadObra, int idRecurso) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            valCostCertBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(costo) FROM Certificacionrecuo WHERE unidadobraId =: id AND recursoId =: idR").setParameter("id", idUnidadObra).setParameter("idR", idRecurso);

            if (query.getSingleResult() != null) {
                valCostCertBajo = (Double) query.getSingleResult();
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
