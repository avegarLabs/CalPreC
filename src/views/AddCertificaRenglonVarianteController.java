package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;


public class AddCertificaRenglonVarianteController implements Initializable {

    private static SessionFactory sf;
    CertificarRenglonVarianteController certificarRenglonVarianteController;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField fieldCantidad;
    private Date desdeDate;
    private Date hastaData;
    private int idNivelE;
    private int idRV;
    private int idBrig;
    private int idGrupo;
    private Integer idCuadrill;
    private Renglonnivelespecifico renglonnivelespecifico;
    private CertificacionRenglonVariante planrenglonvariante;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionrvArrayList;
    private double cantidad;
    private double costMaterial;
    private double costEquipo;
    private double costMano;
    private double salaraio;
    private double salaraiocuc;
    private double costMaterialRN;
    private double costEquipoRN;
    private double costManoRN;
    private double salaraioRN;
    private double salaraiocucRN;
    private ArrayList<Bajoespecificacionrv> listofMateriales;
    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valToPlan;
    private double valCostCertBajo;
    private Bajoespecificacionrv bajo;
    private List<Renglonnivelespecifico> unidadobrarenglonArrayList;
    private Double[] valoresCert;
    private Double[] valoresCertRV;
    @FXML
    private JFXButton btnClose;
    private double valCM;
    private double cantidadCert;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;
    private double costSalario;
    private double costSalarioCert;
    private double costCUCSalario;

    public Renglonnivelespecifico getNivelRenglon(int idNiv, int idRengV) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonnivelespecifico = (Renglonnivelespecifico) session.createQuery("FROM Renglonnivelespecifico WHERE renglonvarianteId =: idRV AND nivelespecificoId =: idNi").setParameter("idRV", idRengV).setParameter("idNi", idNiv).getSingleResult();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonnivelespecifico;
    }

    public Double getCoatoMaterialesBajoEsp(Renglonnivelespecifico renglonnivel) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) session.createQuery("FROM Bajoespecificacionrv WHERE renglonvarianteId =: idRV AND nivelespecificoId =: idNi").setParameter("idRV", renglonnivel.getRenglonvarianteId()).setParameter("idNi", renglonnivel.getNivelespecificoId()).getResultList();
            costMaterial = 0.0;

            for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionrvArrayList) {
                costMaterial += bajoespecificacionrv.getCosto();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return costMaterial;
    }

    public Integer addPlanificacion(CertificacionRenglonVariante planif) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer certId = null;
        try {
            tx = session.beginTransaction();
            certId = (Integer) session.save(planif);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

                //System.out.println("Viendo calculos: " + idRec + " ****** " + cant + " ******* " + prec + " **** " + cant * prec );
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

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

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

    private Bajoespecificacionrv getResourseChange(int idUnidadObra, int idRe, int idSuministro) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

         /*   List<Object[]> queryIns = session.createQuery("SELECT crc.nivelId, rec.id, crc.cantidad,  crc.precio, rec.tipo FROM Cambioscertificacionesrv crc INNER JOIN Recursos rec ON crc.recChangeId = rec.id WHERE crc.nivelId =: idUn AND crc.renglonId =: idR AND crc.recursoId =: idRe ").setParameter("idUn", idUnidadObra).setParameter("idR", idRe).setParameter("idRe", idSuministro).getResultList();
            for (Object[] row : queryIns) {
                bajo = new Bajoespecificacionrv();
                bajo.setId(1);
                bajo.setNivelespecificoId(Integer.parseInt(row[0].toString()));
                bajo.setRenglonvarianteId(idRe);
                bajo.setIdsuministro(Integer.parseInt(row[1].toString()));
                bajo.setCantidad(Math.round(Double.parseDouble(row[2].toString())) * 100d / 100d);
                bajo.setCosto(Math.round(Double.parseDouble(row[2].toString()) * Double.parseDouble(row[3].toString())) * 100d / 100d);
                bajo.setTipo(row[4].toString());
            }*/
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

    @FXML
    public void handlePlanRenglon(ActionEvent event) {
        cantidad = Double.parseDouble(fieldCantidad.getText());

        if (idCuadrill != null) {

            planrenglonvariante = new CertificacionRenglonVariante();
            planrenglonvariante.setCantidad(cantidad);
            planrenglonvariante.setNivelespecificoId(idNivelE);
            planrenglonvariante.setRenglonvarianteId(idRV);
            planrenglonvariante.setBrigadaconstruccionId(idBrig);
            planrenglonvariante.setGrupoconstruccionId(idGrupo);
            planrenglonvariante.setCuadrillaconstruccionId(idCuadrill);
            planrenglonvariante.setDesde(desdeDate);
            planrenglonvariante.setHasta(hastaData);

            if (renglonnivelespecifico.getConmat().contentEquals("1 ")) {
                costMaterialRN = renglonnivelespecifico.getCostmaterial();
            } else {
                costMaterialRN = getCoatoMaterialesBajoEsp(renglonnivelespecifico);
            }

            cantCertRecBajo = getCantCertificadaRV(idNivelE, idRV);
            valoresCertRV = getCostosCertificadosRV(idNivelE, idRV);


            valToPlan = renglonnivelespecifico.getCantidad() - cantCertRecBajo;

            if (cantidad > valToPlan) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("El volumen disponible es manor que la cantidad especificada");
                alert.showAndWait();

            } else {
                costManoRN = renglonnivelespecifico.getCostmano() - valoresCertRV[1];
                costEquipoRN = renglonnivelespecifico.getCostequipo() - valoresCertRV[2];
                salaraioRN = renglonnivelespecifico.getSalario() - valoresCertRV[3];
                salaraiocucRN = renglonnivelespecifico.getSalariocuc() - valoresCertRV[4];

                costMaterial = costMaterialRN * cantidad / valToPlan;
                costMano = costManoRN * cantidad / valToPlan;
                costEquipo = costEquipoRN * cantidad / valToPlan;
                salaraio = salaraioRN * cantidad / valToPlan;
                salaraiocuc = salaraiocucRN * cantidad / valToPlan;

                planrenglonvariante.setCostomaterial(Math.round(costMaterial) * 1000d / 1000d);
                planrenglonvariante.setCostomano(Math.round(costMano) * 1000d / 1000d);
                planrenglonvariante.setCostoequipo(Math.round(costEquipo) * 1000d / 1000d);
                planrenglonvariante.setSalario(Math.round(salaraio) * 1000d / 1000d);
                planrenglonvariante.setSalariocuc(Math.round(salaraiocuc) * 1000d / 1000d);

                Integer id = addPlanificacion(planrenglonvariante);

                //  planificaRenglonVariante(idNivelE, id, desdeDate, hastaData, idRV);

                Thread th2 = new CertificarRVRecursosThread(idNivelE, id, idRV, desdeDate, hastaData, cantidad, valToPlan);
                th2.start();
                // planificaRecursos(idNivelE, id, desdeDate, hastaData, idRV);


                certificarRenglonVarianteController.loadDatosCertificaciones(idNivelE);
                certificarRenglonVarianteController.loadMaterialesCertificaciones(idNivelE, idRV);
                clearField();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Certificación terminada");
                alert.showAndWait();

                Stage stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }

        } else if (idCuadrill == null) {
            planrenglonvariante = new CertificacionRenglonVariante();
            planrenglonvariante.setCantidad(cantidad);
            planrenglonvariante.setNivelespecificoId(idNivelE);
            planrenglonvariante.setRenglonvarianteId(idRV);
            planrenglonvariante.setBrigadaconstruccionId(idBrig);
            planrenglonvariante.setGrupoconstruccionId(idGrupo);
            planrenglonvariante.setCuadrillaconstruccionId(idCuadrill);
            planrenglonvariante.setDesde(desdeDate);
            planrenglonvariante.setHasta(hastaData);

            if (renglonnivelespecifico.getConmat().contentEquals("1 ")) {
                costMaterialRN = renglonnivelespecifico.getCostmaterial();
            } else {
                costMaterialRN = getCoatoMaterialesBajoEsp(renglonnivelespecifico);
            }

            cantCertRecBajo = getCantCertificadaRV(idNivelE, idRV);
            valoresCertRV = getCostosCertificadosRV(idNivelE, idRV);


            valToPlan = renglonnivelespecifico.getCantidad() - cantCertRecBajo;

            if (cantidad > valToPlan) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("El volumen disponible es manor que la cantidad especificada");
                alert.showAndWait();

            } else {
                costManoRN = renglonnivelespecifico.getCostmano() - valoresCertRV[1];
                costEquipoRN = renglonnivelespecifico.getCostequipo() - valoresCertRV[2];
                salaraioRN = renglonnivelespecifico.getSalario() - valoresCertRV[3];
                salaraiocucRN = renglonnivelespecifico.getSalariocuc() - valoresCertRV[4];

                costMaterial = costMaterialRN * cantidad / valToPlan;
                costMano = costManoRN * cantidad / valToPlan;
                costEquipo = costEquipoRN * cantidad / valToPlan;
                salaraio = salaraioRN * cantidad / valToPlan;
                salaraiocuc = salaraiocucRN * cantidad / valToPlan;

                planrenglonvariante.setCostomaterial(Math.round(costMaterial) * 1000d / 1000d);
                planrenglonvariante.setCostomano(Math.round(costMano) * 1000d / 1000d);
                planrenglonvariante.setCostoequipo(Math.round(costEquipo) * 1000d / 1000d);
                planrenglonvariante.setSalario(Math.round(salaraio) * 1000d / 1000d);
                planrenglonvariante.setSalariocuc(Math.round(salaraiocuc) * 1000d / 1000d);

                Integer id = addPlanificacion(planrenglonvariante);

                Thread th2 = new CertificarRVRecursosThread(idNivelE, id, idRV, desdeDate, hastaData, cantidad, valToPlan);
                th2.start();


                certificarRenglonVarianteController.loadDatosCertificaciones(idNivelE);
                certificarRenglonVarianteController.loadMaterialesCertificaciones(idNivelE, idRV);
                clearField();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Certificación terminada");
                alert.showAndWait();

                Stage stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }
        }

    }

    private void planificaRecursos(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, int idRV) {

        listofMateriales = new ArrayList<>();
        listofMateriales = getSuministrosBajoEsp(unidadId, idRV);

        for (Bajoespecificacionrv item : listofMateriales) {

            System.out.println("Costo de los suministros: " + item.getCosto());
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
            cantidadCert = 0.0;
            cantidadCert = Double.parseDouble(fieldCantidad.getText()) * dispRecBajo / valToPlan;
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

            Integer idPlanR = savePlaRec(planrecrv);

        }

    }

    private void planificaRenglonVariante(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP, int idRV) {

        unidadobrarenglonArrayList = new LinkedList<>();
        unidadobrarenglonArrayList = getUnidadobrarenglonArrayList(unidadId, idRV);
        valoresCertRV = new Double[5];

        for (Renglonnivelespecifico item : unidadobrarenglonArrayList) {
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
            System.out.println("CANTIDAD CERTIFICADA: " + cantCertRecBajo);
            valoresCertRV = getCostosCertificadosRV(unidadId, item.getRenglonvarianteId());
            dispRecBajo = item.getCantidad() - cantCertRecBajo;
            System.out.println("DISPONIBLE: " + dispRecBajo);
            Certificacionrecrv planrecuo = new Certificacionrecrv();
            planrecuo.setNivelespId(unidadId);
            planrecuo.setRenglonId(item.getRenglonvarianteId());
            planrecuo.setRecursoId(0);
            planrecuo.setCertificacionId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = Double.parseDouble(fieldCantidad.getText()) * dispRecBajo / valToPlan;
            System.out.println("CANTIDAD: " + cantidadCert);
            planrecuo.setCantidad(cantidadCert);
            if (item.getConmat().contentEquals("0 ")) {
                valCMan = item.getCostmano() - valoresCertRV[1];
                valEquip = item.getCostequipo() - valoresCertRV[2];
                valSal = item.getSalario() - valoresCertRV[3];
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
            } else if (item.getConmat().contentEquals("1 ")) {
                valCM = item.getCostmaterial() - valoresCertRV[0];
                valCMan = item.getCostmano() - valoresCertRV[1];
                valEquip = item.getCostequipo() - valoresCertRV[2];
                valSal = item.getSalario() - valoresCertRV[3];
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

            Integer idPlanR = savePlaRec(planrecuo);
        }

    }


    private Integer savePlaRec(Certificacionrecrv planrecuo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idPlan = null;
        try {
            tx = session.beginTransaction();

            idPlan = (Integer) session.save(planrecuo);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idPlan;
    }

    public void clearField() {
        fieldCantidad.clear();
    }


    public void cargarDatos(CertificarRenglonVarianteController controller, int nivelEspecifico,
                            int renglonVariante, int brigada, int grupo, Integer cuadrilla, Date desde, Date hasta, double dispo) {
        certificarRenglonVarianteController = controller;
        idNivelE = nivelEspecifico;
        idRV = renglonVariante;
        idBrig = brigada;
        idCuadrill = cuadrilla;
        idGrupo = grupo;
        desdeDate = desde;
        hastaData = hasta;
        fieldCantidad.setText(String.valueOf(dispo));
        renglonnivelespecifico = getNivelRenglon(idNivelE, idRV);
    }


    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }


}
