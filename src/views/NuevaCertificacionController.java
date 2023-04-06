package views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class NuevaCertificacionController implements Initializable {

    private static SessionFactory sf;
    Task tarea;
    @FXML
    private JFXComboBox<String> combo_Brigada;
    @FXML
    private JFXComboBox<String> combogrupo;
    @FXML
    private JFXComboBox<String> combocuadrilla;
    @FXML
    private JFXTextField fieldcodigouo;
    @FXML
    private JFXTextField fieldcantidad;
    @FXML
    private Label labelDispo;
    @FXML
    private JFXDatePicker dateini;
    @FXML
    private JFXDatePicker datehasta;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObservableList<String> listUO;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private ObservableList<String> brigadasList;
    private ObservableList<String> grupoList;
    private ObservableList<String> cuadrilaList;
    private Unidadobra unidadobraModel;
    private int unidadObraId;
    private int brigadaId;
    private int grupoId;
    private int cuadrillaId;
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
    private java.sql.Date desde;
    private java.sql.Date hasta;
    private double cantidadCert;
    private Certificacion certificacion;
    private ArrayList<Certificacion> certificacionArrayList;
    private ArrayList<Bajoespecificacion> listofMateriales;
    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valToPlan;
    private double valCostCertBajo;
    private Bajoespecificacion bajo;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private Double[] valoresCert;
    private Double[] valoresCertRV;

    /**
     * Nuevo en la certificaciones
     */
    @FXML
    private JFXCheckBox checkActive;
    private Integer inputLength = 7;

    private Cuadrillaconstruccion cuadrillaconst;
    private Grupoconstruccion grupoconst;

    public Double getCantCertificadaRV(int idUnidadObra, int idRVar) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR");
            query.setParameter("id", idUnidadObra);
            query.setParameter("idR", idRVar);


            if (query.getSingleResult() != null) {
                cantCertRecBajo = (Double) query.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantCertRecBajo;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonArrayList(int unidadId) {

        unidadobrarenglonArrayList = new LinkedList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idUni ");
            query.setParameter("idUni", unidadId);

            unidadobrarenglonArrayList = query.getResultList();

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

    public Double getCostCertificadaBajo(int idUnidadObra, int idRecurso) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            valCostCertBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(costo) FROM Certificacionrecuo WHERE unidadobraId =: id AND recursoId =: idR");
            query.setParameter("id", idUnidadObra);
            query.setParameter("idR", idRecurso);


            if (query.getSingleResult() != null) {
                valCostCertBajo = (Double) query.getSingleResult();
            }

            tx.commit();
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
            Query query = session.createQuery("SELECT SUM(cmateriales), SUM(cmano), SUM(cequipo), SUM(salario), SUM(salariocuc) FROM Certificacionrecuo WHERE unidadobraId =: id AND renglonId =: idR");
            query.setParameter("id", idUnidadObra);
            query.setParameter("idR", idReng);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
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

    public Double[] getValoresCertUO(Integer idUO) {
        valoresCert = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidadobra(:idU)");
            query.setParameter("idU", idUO);


            List<Object[]> recuo = ((NativeQuery) query).list();
            for (Object[] row : recuo) {
                if (row[0] == null) {
                    valoresCert[0] = 0.0;

                } else {
                    valoresCert[0] = Double.parseDouble(row[0].toString());
                }

                if (row[1] == null) {
                    valoresCert[1] = 0.0;
                } else {
                    valoresCert[1] = Double.parseDouble(row[1].toString());
                }

                if (row[2] == null) {
                    valoresCert[2] = 0.0;
                } else {
                    valoresCert[2] = Double.parseDouble(row[2].toString());
                }

                if (row[3] == null) {
                    valoresCert[3] = 0.0;
                } else {
                    valoresCert[3] = Double.parseDouble(row[3].toString());
                }

                if (row[4] == null) {
                    valoresCert[4] = 0.0;
                } else {
                    valoresCert[4] = Double.parseDouble(row[4].toString());
                }

                if (row[5] == null) {
                    valoresCert[5] = 0.0;
                } else {
                    valoresCert[5] = Double.parseDouble(row[5].toString());
                }

            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valoresCert;
    }

    private void planificaRenglonVariante(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP) {


        unidadobrarenglonArrayList = new LinkedList<>();
        unidadobrarenglonArrayList = getUnidadobrarenglonArrayList(unidadObraId);
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
            planrecuo.setUnidadobraId(unidadObraId);
            planrecuo.setRenglonId(item.getRenglonvarianteId());
            planrecuo.setRecursoId(0);
            planrecuo.setCertificacionId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = Double.parseDouble(fieldcantidad.getText()) * dispRecBajo / valToPlan;
            planrecuo.setCantidad(Math.round(cantidadCert * 1000d) / 1000d);
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

            Integer idPlanR = savePlaRec(planrecuo);
        }

    }

    private void planificaRecursos(int unidadId, Integer idcertP, Date desdeDateP, Date hastaDataP) {


        listofMateriales = new ArrayList<>();
        listofMateriales = getSuministrosBajoEsp(unidadObraId);

        for (Bajoespecificacion item : listofMateriales) {
            cantCertRecBajo = 0.0;
            valCostCertBajo = 0.0;
            valCM = 0.0;
            costMaterial = 0.0;
            cantCertRecBajo = getCantCertificadaBajo(unidadId, item.getIdSuministro());
            valCostCertBajo = getCostCertificadaBajo(unidadId, item.getIdSuministro());
            dispRecBajo = item.getCantidad() - cantCertRecBajo;
            Certificacionrecuo planrecuo = new Certificacionrecuo();
            planrecuo.setUnidadobraId(unidadObraId);
            planrecuo.setRenglonId(0);
            planrecuo.setRecursoId(item.getIdSuministro());
            planrecuo.setCertificacionId(idcertP);
            cantidadCert = 0.0;
            cantidadCert = Double.parseDouble(fieldcantidad.getText()) * dispRecBajo / valToPlan;
            planrecuo.setCantidad(Math.round(cantidadCert * 1000d) / 1000d);
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

            Integer idPlanR = savePlaRec(planrecuo);

        }
    }

    private Integer savePlaRec(Certificacionrecuo planrecuo) {

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

    private ObservableList<String> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        listUO = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);
            unidadobraArrayList = (ArrayList<Unidadobra>) ((org.hibernate.query.Query) query).list();
            for (Unidadobra unidadobra : unidadobraArrayList) {
                listUO.add(unidadobra.getCodigo() + " - " + unidadobra.getDescripcion());
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listUO;
    }

    public ArrayList<Certificacion> getCertificaciones() {
        certificacionArrayList = new ArrayList<Certificacion>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionArrayList = (ArrayList<Certificacion>) session.createQuery("FROM Certificacion ").list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacionArrayList;
    }

    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: empId");
            query.setParameter("empId", idEmp);
            brigadaconstruccionsArrayList = (ArrayList<Brigadaconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccionsArrayList) {
                brigadasList.add(brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadasList;
    }

    public ObservableList<String> listOfGrupos(int idBrigada) {
        grupoList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: briId");
            query.setParameter("briId", idBrigada);
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Grupoconstruccion grupoconstruccion : grupoconstruccionArrayList) {
                grupoList.add(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoList;
    }

    public ObservableList<String> listOfCuadrilla(int idGrupo) {
        cuadrilaList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: grupId");
            query.setParameter("grupId", idGrupo);
            cuadrillaconstruccionsArrayList = (ArrayList<Cuadrillaconstruccion>) ((org.hibernate.query.Query) query).list();
            for (Cuadrillaconstruccion cuadrillaconstruccion : cuadrillaconstruccionsArrayList) {
                cuadrilaList.add(cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cuadrilaList;
    }

    public Integer addCertificacion(Certificacion certificacion) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer certId = null;
        try {
            tx = session.beginTransaction();
            certId = (Integer) session.save(certificacion);

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

        checkActive.setOnMouseClicked(event -> {
            if (checkActive.isSelected() == true) {
                combocuadrilla.setDisable(false);
            } else if (checkActive.isSelected() == false) {
                combocuadrilla.setDisable(true);
            }

        });

    }

    public void cargarUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        listUO = getUnidadesObra(idObra, idEmp, idZona, idObj, idNiv, idEsp, idSub);
        //TextFields.bindAutoCompletion(fieldcodigouo, listUO);

        brigadasList = listOfBrigadas(idEmp);
        combo_Brigada.setItems(brigadasList);

        certificacionArrayList = getCertificaciones();


    }

    public void keyTypeCode(KeyEvent event) {
        if (fieldcodigouo.getText().length() == inputLength) {
            for (Unidadobra unidadobra : unidadobraArrayList) {
                if (unidadobra.getCodigo().contentEquals(fieldcodigouo.getText())) {

                    unidadobraModel = unidadobra;
                    fieldcantidad.requestFocus();

                    valoresCert = new Double[6];
                    valoresCert = getValoresCertUO(unidadobra.getId());

                    costMaterialCert = 0.0;
                    costManoCert = 0.0;
                    costEquipoCert = 0.0;
                    costCUCSalarioCert = 0.0;
                    costSalarioCert = 0.0;
                    costMaterialCert = valoresCert[1];
                    costManoCert = valoresCert[2];
                    costEquipoCert = valoresCert[3];
                    costCUCSalarioCert = valoresCert[5];
                    costSalarioCert = valoresCert[4];

                    double val = unidadobra.getCantidad() - valoresCert[0];
                    if (val == 0.0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error");
                        alert.setContentText("Cantidad a certificar no disponible!");
                        alert.showAndWait();
                        fieldcantidad.setDisable(true);
                    } else {
                        labelDispo.setText(String.valueOf(Math.round(val * 100d) / 100d));
                    }
                }
            }

        }
    }

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");
        String code = codeBriPart[0];

        for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccionsArrayList) {
            if (brigadaconstruccion.getCodigo().contentEquals(code)) {
                brigadaId = brigadaconstruccion.getId();
                grupoList = listOfGrupos(brigadaconstruccion.getId());
                combogrupo.setItems(grupoList);
            }

        }
    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");
        String code = codeGrupPart[0];

        for (Grupoconstruccion grupoconstruccion : grupoconstruccionArrayList) {
            if (grupoconstruccion.getCodigo().contentEquals(code)) {
                grupoId = grupoconstruccion.getId();
                cuadrilaList = listOfCuadrilla(grupoconstruccion.getId());
                combocuadrilla.setItems(cuadrilaList);
            }

        }
    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(1000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
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

    public ArrayList<Bajoespecificacion> getSuministrosBajoEsp(int idUnidadObra) {

        listofMateriales = new ArrayList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: id");
            query.setParameter("id", idUnidadObra);

            ArrayList<Bajoespecificacion> matrialesInPresupuesto = (ArrayList<Bajoespecificacion>) query.getResultList();
            matrialesInPresupuesto.forEach(items -> {

                //bajo = getResourseChange(idUnidadObra, items.getIdSuministro());

                if (bajo != null) {
                    listofMateriales.add(bajo);
                } else if (bajo == null) {
                    listofMateriales.add(items);
                }

                bajo = null;
            });


            Query queryIns = session.createQuery("SELECT uo.id, rec.id, SUM(uor.cantRv * rvr.cantidas), rec.preciomn, rec.tipo FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.id =: idU AND rec.tipo != '1' GROUP BY uo.id, rec.id, rec.preciomn, rec.tipo ");
            queryIns.setParameter("idU", idUnidadObra);

            for (Iterator it = ((org.hibernate.query.Query) queryIns).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                Integer idUO = Integer.parseInt(row[0].toString());
                Integer idRec = Integer.parseInt(row[1].toString());
                Double cant = Double.parseDouble(row[2].toString());
                Double prec = Double.parseDouble(row[3].toString());
                String tip = row[4].toString();

                bajo = new Bajoespecificacion();
                bajo.setId(1);
                bajo.setUnidadobraId(idUO);
                bajo.setRenglonvarianteId(1);
                bajo.setIdSuministro(idRec);
                bajo.setCantidad(Math.round(cant) * 100d / 100d);
                bajo.setCosto(Math.round(cant * prec) * 100d / 100d);
                bajo.setTipo(tip);
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

    /*
    private Bajoespecificacion getResourseChange(int idUnidadObra, int idSuministro) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query queryIns = session.createQuery("SELECT crc.unidadId, rec.id, crc.cantidad,  crc.precio, rec.tipo FROM Cambioscertificacionesuo crc INNER JOIN Recursos rec ON crc.recChangeId = rec.id WHERE crc.unidadId =: idUn AND crc.recursoId =: idRe ");
            queryIns.setParameter("idUn", idUnidadObra);
            queryIns.setParameter("idRe", idSuministro);

            for (Iterator it = ((org.hibernate.query.Query) queryIns).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                Integer idUO = Integer.parseInt(row[0].toString());
                Integer idRec = Integer.parseInt(row[1].toString());
                Double cant = Double.parseDouble(row[2].toString());
                Double prec = Double.parseDouble(row[3].toString());
                String tip = row[4].toString();

                bajo = new Bajoespecificacion();
                bajo.setId(1);
                bajo.setUnidadobraId(idUO);
                bajo.setRenglonvarianteId(1);
                bajo.setIdSuministro(idRec);
                bajo.setCantidad(Math.round(cant) * 100d / 100d);
                bajo.setCosto(Math.round(cant * prec) * 100d / 100d);
                bajo.setTipo(tip);
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
*/
    public Double getCantCertificadaBajo(int idUnidadObra, int idRecurso) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantCertRecBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo WHERE unidadobraId =: id AND recursoId =: idR");
            query.setParameter("id", idUnidadObra);
            query.setParameter("idR", idRecurso);


            if (query.getSingleResult() != null) {
                cantCertRecBajo = (Double) query.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantCertRecBajo;
    }

    public void handleAddCertificacion(ActionEvent event) {

        if (checkActive.isSelected() == true) {

            if (Double.parseDouble(fieldcantidad.getText()) > Double.parseDouble(labelDispo.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Cantidad a certificar mayor que la cantidad disponible!");
                alert.showAndWait();

            } else if (combo_Brigada.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Brigada de Contrucción!");
                alert.showAndWait();
            } else if (combogrupo.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar un Grupo de Contrucción!");
                alert.showAndWait();
            } else if (combocuadrilla.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Cuadrilla de Contrucción!");
                alert.showAndWait();
            } else if (dateini.getValue() == null || datehasta.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar periódo de tiempo para la certificación!");
                alert.showAndWait();
            } else {

                tarea = createTimeMesage();
                ProgressDialog dialog = new ProgressDialog(tarea);
                dialog.setContentText("Certificando elementos en la unidad de obra... espere");
                dialog.setTitle("Espere...");
                new Thread(tarea).start();
                dialog.showAndWait();

                unidadObraId = unidadobraModel.getId();

                if (checkActive.isSelected()) {
                    String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                    cuadrillaconst = cuadrillaconstruccionsArrayList.stream().filter(c -> c.getCodigo().contentEquals(codeCuadriPart[0])).findFirst().get();
                } else {
                    String[] partGrupo = combogrupo.getValue().split(" - ");
                    grupoconst = grupoconstruccionArrayList.parallelStream().filter(item -> item.getCodigo().equals(partGrupo[0].toString())).findFirst().get();
                    cuadrillaconst = grupoconst.getCuadrillaconstruccionsById().parallelStream().collect(Collectors.toList()).get(0);
                }
                valCM = 0.0;
                valCMan = 0.0;
                valEquip = 0.0;
                valSal = 0.0;
                valCucSal = 0.0;

                valToPlan = 0.0;
                valToPlan = unidadobraModel.getCantidad() - valoresCert[0];

                valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
                valCMan = unidadobraModel.getCostomano() - costManoCert;
                valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
                valSal = unidadobraModel.getSalario() - costSalarioCert;
                valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

                costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;

                String pattern = "dd/MM/yyyy";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate dateDes = dateini.getValue();
                LocalDate dateHast = datehasta.getValue();
                desde = Date.valueOf(dateDes);
                hasta = Date.valueOf(dateHast);


                certificacion = new Certificacion();
                certificacion.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
                certificacion.setCostmano(Math.round(costMano * 100d) / 100d);
                certificacion.setCostequipo(Math.round(costEquipo * 100d) / 100d);
                certificacion.setSalario(Math.round(costSalario * 100d) / 100d);
                certificacion.setCucsalario(Math.round(costCUCSalario * 100d) / 100d);
                certificacion.setBrigadaconstruccionId(brigadaId);
                certificacion.setGrupoconstruccionId(grupoconst.getId());
                certificacion.setCuadrillaconstruccionId(cuadrillaconst.getId());
                certificacion.setUnidadobraId(unidadObraId);
                certificacion.setDesde(desde);
                certificacion.setHasta(hasta);
                certificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


                Integer idcert = addCertificacion(certificacion);

                Thread t1 = new CertificarUORenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
                t1.start();

                Thread t2 = new CertificaRecursosRenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
                t2.start();

                if (idcert != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Datos insertados satisfactoriamente!");
                    alert.showAndWait();
                }

                fieldcodigouo.clear();
                labelDispo.setText(" ");
                fieldcantidad.clear();


            }
        } else if (checkActive.isSelected() == false) {
            if (Double.parseDouble(fieldcantidad.getText()) > Double.parseDouble(labelDispo.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Cantidad a certificar mayor que la cantidad disponible!");
                alert.showAndWait();

            } else if (combo_Brigada.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Brigada de Contrucción!");
                alert.showAndWait();
            } else if (combogrupo.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar un Grupo de Contrucción!");
                alert.showAndWait();

            } else if (dateini.getValue() == null || datehasta.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar periódo de tiempo para la certificación!");
                alert.showAndWait();
            } else {

                tarea = createTimeMesage();
                ProgressDialog dialog = new ProgressDialog(tarea);
                dialog.setContentText("Certificando elementos en la unidad de obra... espere");
                dialog.setTitle("Espere...");
                new Thread(tarea).start();
                dialog.showAndWait();
/*
                String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                String code = codeCuadriPart[0];
*/
                unidadObraId = unidadobraModel.getId();

                if (checkActive.isSelected()) {
                    String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                    cuadrillaconst = cuadrillaconstruccionsArrayList.stream().filter(c -> c.getCodigo().contentEquals(codeCuadriPart[0])).findFirst().get();
                } else {
                    String[] partGrupo = combogrupo.getValue().split(" - ");
                    grupoconst = grupoconstruccionArrayList.parallelStream().filter(item -> item.getCodigo().equals(partGrupo[0].toString())).findFirst().get();
                    cuadrillaconst = grupoconst.getCuadrillaconstruccionsById().parallelStream().collect(Collectors.toList()).get(0);
                }

                valCM = 0.0;
                valCMan = 0.0;
                valEquip = 0.0;
                valSal = 0.0;
                valCucSal = 0.0;

                valToPlan = 0.0;
                valToPlan = unidadobraModel.getCantidad() - valoresCert[0];

                valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
                valCMan = unidadobraModel.getCostomano() - costManoCert;
                valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
                valSal = unidadobraModel.getSalario() - costSalarioCert;
                valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

                costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;

                String pattern = "dd/MM/yyyy";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate dateDes = dateini.getValue();
                LocalDate dateHast = datehasta.getValue();
                desde = Date.valueOf(dateDes);
                hasta = Date.valueOf(dateHast);


                certificacion = new Certificacion();
                certificacion.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
                certificacion.setCostmano(Math.round(costMano * 100d) / 100d);
                certificacion.setCostequipo(Math.round(costEquipo * 100d) / 100d);
                certificacion.setSalario(Math.round(costSalario * 100d) / 100d);
                certificacion.setCucsalario(Math.round(costCUCSalario * 100d) / 100d);
                certificacion.setBrigadaconstruccionId(brigadaId);
                certificacion.setGrupoconstruccionId(grupoconst.getId());
                certificacion.setCuadrillaconstruccionId(cuadrillaconst.getId());
                certificacion.setUnidadobraId(unidadObraId);
                certificacion.setDesde(desde);
                certificacion.setHasta(hasta);
                certificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));


                Integer idcert = addCertificacion(certificacion);

                Thread t1 = new CertificarUORenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
                t1.start();

                Thread t2 = new CertificaRecursosRenglonesThread(unidadObraId, idcert, desde, hasta, Double.parseDouble(fieldcantidad.getText()), valToPlan);
                t2.start();

                if (idcert != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Datos insertados satisfactoriamente!");
                    alert.showAndWait();
                }

                fieldcodigouo.clear();
                labelDispo.setText(" ");
                fieldcantidad.clear();


            }
        }

    }

}
