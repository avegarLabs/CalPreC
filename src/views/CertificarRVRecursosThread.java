package views;

import models.Bajoespecificacionrv;
import models.Certificacionrecrv;
import models.ConnectionModel;
import models.Renglonnivelespecifico;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CertificarRVRecursosThread extends Thread {

    private static SessionFactory sf;
    public Integer idUnid;
    public Integer idCer;
    public Integer idreng;
    public Date ini;
    public Date fin;
    public Double cantidad;
    public Double valToPlan;
    private ArrayList<Bajoespecificacionrv> listofMateriales;
    private List<Renglonnivelespecifico> unidadobrarenglonArrayList;
    private ArrayList<Certificacionrecrv> certificacionrecuoArrayList;
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
    private Bajoespecificacionrv bajo;

    public CertificarRVRecursosThread(int unidadId, Integer idcertP, Integer idRV, Date desdeDateP, Date hastaDataP, Double cant, Double valTo) {
        idUnid = unidadId;
        idCer = idcertP;
        idreng = idRV;
        ini = desdeDateP;
        fin = hastaDataP;
        cantidad = cant;
        valToPlan = valTo;

    }

    @Override
    public void run() {
        certificacionrecuoArrayList = new ArrayList<>();
        certificacionrecuoArrayList = certificaRenglonVariante(idUnid, idCer, idreng, ini, fin, cantidad, valToPlan);
        savePlaRec(certificacionrecuoArrayList);

    }

    private ArrayList<Certificacionrecrv> certificaRenglonVariante(int unidadId, Integer idcertP, int idRV, Date desdeDateP, Date hastaDataP, Double cant, Double valToPlan) {
        certificacionrecuoArrayList = new ArrayList<>();
        unidadobrarenglonArrayList = new LinkedList<>();

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

            System.out.println("RECURSOS  " + item.getIdsuministro() + " ***** " + dispRecBajo);

            Certificacionrecrv planrecrv = new Certificacionrecrv();
            planrecrv.setNivelespId(unidadId);
            planrecrv.setRenglonId(item.getRenglonvarianteId());
            planrecrv.setRecursoId(item.getIdsuministro());
            planrecrv.setCertificacionId(idcertP);
            planrecrv.setTipo(item.getTipo());
            cantidadCert = 0.0;
            cantidadCert = cant * dispRecBajo / valToPlan;
            System.out.println("Cantidad material: " + item.getCosto() + " - " + item.getTipo());
            planrecrv.setCantidad(cantidadCert);
            valCM = item.getCosto() - valCostCertBajo;
            System.out.println("Costo Material: " + item.getCosto());
            costMaterial = valCM * cantidadCert / dispRecBajo;
            System.out.println("Mostrando el costo del Material " + item.getIdsuministro() + " **** " + costMaterial);
            planrecrv.setCosto(costMaterial);
            planrecrv.setCmateriales(0.0);
            planrecrv.setCmano(0.0);
            planrecrv.setCequipo(0.0);
            planrecrv.setSalario(0.0);
            planrecrv.setSalariocuc(0.0);
            planrecrv.setFini(desdeDateP);
            planrecrv.setFfin(hastaDataP);

            certificacionrecuoArrayList.add(planrecrv);

        }
        return certificacionrecuoArrayList;
    }

    /***
     *
     * Para realizar la planificación en la unidad de obra
     *
     * El primer metodo devuelve todos los suminitros bajo especificación
     *
     * El segundo metodo devuelve las cantidades certificadas de cade recurso
     *
     */
    public ArrayList<Bajoespecificacionrv> getSuministrosBajoEsp(int idUnidadObra, int idReng) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            listofMateriales = new ArrayList<>();
            ArrayList<Bajoespecificacionrv> matrialesInPresupuesto = (ArrayList<Bajoespecificacionrv>) session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: id AND renglonvarianteId =: idreng").setParameter("id", idUnidadObra).setParameter("idreng", idReng).getResultList();

            for (Bajoespecificacionrv items : matrialesInPresupuesto) {
                listofMateriales.add(items);
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

                //System.out.println("Viendo calculos: " + idRec + " ****** " + cant + " ******* " + prec + " **** " + cant * prec );
            }

            tx.commit();
            session.close();

        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listofMateriales;
    }

/*
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
*/

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

    public Double getCantCertificadaRV(int idNiv, int idRVar) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            javax.persistence.Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecrv WHERE  nivelespId=: id AND renglonId =: idR AND recursoId = 0").setParameter("id", idNiv).setParameter("idR", idRVar);

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


    public List<Renglonnivelespecifico> getUnidadobrarenglonArrayList(int unidadId, int idRe) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            unidadobrarenglonArrayList = new LinkedList<>();
            unidadobrarenglonArrayList = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idUni AND renglonvarianteId =: idRva ").setParameter("idUni", unidadId).setParameter("idRva", idRe).getResultList();

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

    public Double getCostCertificadaBajo(int idNiv, int idReng, int idRec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            valCostCertBajo = 0.0;
            javax.persistence.Query query = session.createQuery("SELECT SUM(costo) FROM Certificacionrecrv WHERE nivelespId =: id AND renglonId =: idR AND recursoId =: idRec").setParameter("id", idNiv).setParameter("idR", idReng).setParameter("idRec", idRec);
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

    public Double[] getCostosCertificadosRV(int idUnidadObra, int idReng) {

        valoresCertRV = new Double[5];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            valCostCertBajo = 0.0;
            List<Object[]> queryDatos = session.createQuery("SELECT SUM(cmateriales), SUM(cmano), SUM(cequipo), SUM(salario), SUM(salariocuc) FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR AND recursoId = 0").setParameter("id", idUnidadObra).setParameter("idR", idReng).getResultList();

            for (Object[] row : queryDatos) {

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


    private void savePlaRec(ArrayList<Certificacionrecrv> list) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            count = 0;
            for (Certificacionrecrv cer : list) {
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
