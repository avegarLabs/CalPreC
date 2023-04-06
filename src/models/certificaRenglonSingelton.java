package models;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class certificaRenglonSingelton {

    private static certificaRenglonSingelton instancia = null;
    public Integer idUnid;
    public Integer idCer;
    public Date ini;
    public Date fin;
    public Double cantidad;
    public Double valToPlan;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private ArrayList<Certificacionrecuo> certificacionrecuoArrayList;
    private Double[] valoresCertRV;
    private double costMaterial;
    private double costMano;
    private double costEquipo;
    private double costSalario;
    private double costCUCSalario;
    private double valCM;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;
    private Date desdeDate;
    private Date hastaData;
    private double cantidadCert;
    private double cantidadPlan;
    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valCostCertBajo;
    private int count;
    private int batchSize = 20;


    private certificaRenglonSingelton() {
    }

    public static certificaRenglonSingelton getInstance() {
        if (instancia == null) {
            instancia = new certificaRenglonSingelton();
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

        certificacionrecuoArrayList = new ArrayList<>();
        certificacionrecuoArrayList = certificaRenglonVariante(idUnid, idCer, ini, fin, cantidad, valToPlan);
        savePlaRec(certificacionrecuoArrayList);
    }

    public ArrayList<Certificacionrecuo> certificaRenglonVariante(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, Double cant, Double valToPlan) {

        certificacionrecuoArrayList = new ArrayList<>();
        unidadobrarenglonArrayList = new LinkedList<>();
        unidadobrarenglonArrayList = getUnidadobrarenglonArrayList(unidadId);
        valoresCertRV = new Double[5];
        for (Unidadobrarenglon item : unidadobrarenglonArrayList) {
            cantCertRecBajo = 0.0;
            valCostCertBajo = 0.0;
            valCM = 0.0;
            valCMan = 0.0;
            valEquip = 0.0;
            valSal = 0.0;
            valCucSal = 0.0;
            costMaterial = 0.0;
            costEquipo = 0.0;
            costMano = 0.0;
            costSalario = 0.0;
            costCUCSalario = 0.0;
            cantCertRecBajo = getCantCertificadaRV(unidadId, item.getRenglonvarianteId());
            valoresCertRV = getCostosCertificadosRV(unidadId, item.getRenglonvarianteId());
            dispRecBajo = item.getCantRv() - cantCertRecBajo;
            Certificacionrecuo planrecuo = new Certificacionrecuo();
            planrecuo.setUnidadobraId(unidadId);
            planrecuo.setRenglonId(item.getRenglonvarianteId());
            planrecuo.setRecursoId(0);
            planrecuo.setCertificacionId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = cant * dispRecBajo / valToPlan;
            planrecuo.setCantidad(Math.round(cantidadCert * 10000d) / 10000d);
            if (item.getConMat().contentEquals("0 ")) {
                valCMan = item.getCostMano() - valoresCertRV[1];
                valEquip = item.getCostEquip() - valoresCertRV[2];
                valSal = item.getSalariomn() - valoresCertRV[3];
                valCucSal = item.getSalariocuc() - valoresCertRV[4];
                costMano = valCMan * cantidadCert / dispRecBajo;
                costEquipo = valEquip * cantidadCert / dispRecBajo;
                costSalario = valSal * cantidadCert / dispRecBajo;
                costCUCSalario = valCucSal * cantidadCert / dispRecBajo;
                planrecuo.setCosto(0.0);
                planrecuo.setCmateriales(0.0);
                planrecuo.setCmano(costMano);
                planrecuo.setCequipo(costEquipo);
                planrecuo.setSalario(costSalario);
                planrecuo.setSalariocuc(costCUCSalario);
            } else if (item.getConMat().contentEquals("1 ")) {
                valCM = item.getCostMat() - valoresCertRV[0];
                valCMan = item.getCostMano() - valoresCertRV[1];
                valEquip = item.getCostEquip() - valoresCertRV[2];
                valSal = item.getSalariomn() - valoresCertRV[3];
                valCucSal = item.getSalariocuc() - valoresCertRV[4];
                costMaterial = valCM * cantidadCert / dispRecBajo;
                costMano = valCMan * cantidadCert / dispRecBajo;
                costEquipo = valEquip * cantidadCert / dispRecBajo;
                costSalario = valSal * cantidadCert / dispRecBajo;
                costCUCSalario = valCucSal * cantidadCert / dispRecBajo;
                planrecuo.setCosto(0.0);
                planrecuo.setCmateriales(0.0);
                planrecuo.setCmano(costMano);
                planrecuo.setCequipo(costEquipo);
                planrecuo.setSalario(costSalario);
                planrecuo.setSalariocuc(costCUCSalario);
            }
            planrecuo.setFini(desdeDateP);
            planrecuo.setFfin(hastaDataP);
            planrecuo.setTipo("RV");
            certificacionrecuoArrayList.add(planrecuo);
        }
        return certificacionrecuoArrayList;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonArrayList(int unidadId) {

        unidadobrarenglonArrayList = new LinkedList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            unidadobrarenglonArrayList = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUni ").setParameter("idUni", unidadId).getResultList();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobrarenglonArrayList;
    }

    public Double getCantCertificadaRV(int idUnidadObra, int idRVar) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR").setParameter("id", idUnidadObra).setParameter("idR", idRVar);

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

    public Double[] getCostosCertificadosRV(int idUnidadObra, int idReng) {

        valoresCertRV = new Double[5];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            valCostCertBajo = 0.0;
            List<Certificacionrecuo> valoresCerts = session.createQuery(" FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR").setParameter("id", idUnidadObra).setParameter("idR", idReng).getResultList();
            valoresCertRV[0] = valoresCerts.parallelStream().map(Certificacionrecuo::getCmateriales).reduce(0.0, Double::sum);
            valoresCertRV[1] = valoresCerts.parallelStream().map(Certificacionrecuo::getCmano).reduce(0.0, Double::sum);
            valoresCertRV[2] = valoresCerts.parallelStream().map(Certificacionrecuo::getCequipo).reduce(0.0, Double::sum);
            valoresCertRV[3] = valoresCerts.parallelStream().map(Certificacionrecuo::getSalario).reduce(0.0, Double::sum);
            valoresCertRV[4] = valoresCerts.parallelStream().map(Certificacionrecuo::getSalario).reduce(0.0, Double::sum);
            tx.commit();
            session.close();
            return valoresCertRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valoresCertRV;
    }

    private void savePlaRec(ArrayList<Certificacionrecuo> list) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            count = 0;
            for (Certificacionrecuo cer : list) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(cer);
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


}
