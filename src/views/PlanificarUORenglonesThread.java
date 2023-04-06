package views;

import models.ConnectionModel;
import models.Planrecuo;
import models.Unidadobrarenglon;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlanificarUORenglonesThread extends Thread {

    private static SessionFactory sf;
    public Integer idUnid;
    public Integer idCer;
    public Date ini;
    public Date fin;
    public Double cantidad;
    public Double valToPlan;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private ArrayList<Planrecuo> planrecuoArrayList;
    private Double[] valoresCertRV;

    private double costMaterial;
    private double costMaterialCert;
    private double costMano;
    private double costManoCert;
    private double costEquipo;
    private double costEquipoCert;
    private double costSalario;
    private double costSalarioCert;
    private double costCUCSalario;
    private double costCUCSalarioCert;

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
    private int batchSize = 10;

    public PlanificarUORenglonesThread(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, Double cant, Double valTo) {
        idUnid = unidadId;
        idCer = idcertP;
        ini = desdeDateP;
        fin = hastaDataP;
        cantidad = cant;
        valToPlan = valTo;

    }

    @Override
    public void run() {
        planrecuoArrayList = new ArrayList<>();
        planrecuoArrayList = planificaRenglonVariante(idUnid, idCer, ini, fin, cantidad, valToPlan);
        savePlaRec(planrecuoArrayList);

    }

    public ArrayList<Planrecuo> planificaRenglonVariante(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, Double cant, Double valToPlan) {

        planrecuoArrayList = new ArrayList<Planrecuo>();
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
            Planrecuo planrecuo = new Planrecuo();
            planrecuo.setUnidadobraId(idUnid);
            planrecuo.setRenglonId(item.getRenglonvarianteId());
            planrecuo.setRecursoId(0);
            planrecuo.setPlanId(idcertP);
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

            planrecuoArrayList.add(planrecuo);

        }

        return planrecuoArrayList;
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
            List<Object[]> datosValores = session.createQuery("SELECT SUM(cmateriales), SUM(cmano), SUM(cequipo), SUM(salario), SUM(salariocuc) FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR").setParameter("id", idUnidadObra).setParameter("idR", idReng).getResultList();
            for (Object[] row : datosValores) {
                if (row[0] == null) {
                    valoresCertRV[0] = 0.0;
                } else if (row[0] != null) {
                    valoresCertRV[0] = Math.round(Double.parseDouble(row[0].toString()) * 100d) / 100d;
                }

                if (row[1] == null) {
                    valoresCertRV[1] = 0.0;
                } else if (row[1] != null) {
                    valoresCertRV[1] = Math.round(Double.parseDouble(row[1].toString()) * 100d) / 100d;
                }
                if (row[2] == null) {
                    valoresCertRV[2] = 0.0;
                } else if (row[2] != null) {
                    valoresCertRV[2] = Math.round(Double.parseDouble(row[2].toString()) * 100d) / 100d;
                }
                if (row[3] == null) {
                    valoresCertRV[3] = 0.0;
                } else if (row[3] != null) {
                    valoresCertRV[3] = Math.round(Double.parseDouble(row[3].toString()) * 100d) / 100d;
                }
                if (row[4] == null) {
                    valoresCertRV[4] = 0.0;
                } else if (row[4] != null) {
                    valoresCertRV[4] = Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d;
                }
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valoresCertRV;
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


}
