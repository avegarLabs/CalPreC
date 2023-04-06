package views;

import com.jfoenix.controls.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class PlanificarUnidaddeObraController implements Initializable {

    public PlanificarUnidaddeObraController planificarUnidaddeObraController;
    Task tarea;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private Label labelTitle;
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
    @FXML
    private TableView<PlanificacionUnidadObraView> tablePlanificacion;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> cant;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> desde;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> hasta;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, IntegerProperty> brigada;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, IntegerProperty> grupo;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, IntegerProperty> cuadrilla;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> materiales;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> mano;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> equipo;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> certificado;
    private List<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private List<Grupoconstruccion> grupoconstruccionArrayList;
    private List<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
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
    private Date desdeDate;
    private Date hastaData;
    private double cantidadCert;
    private double cantidadPlan;
    private Planificacion planificacion;
    private List<Planificacion> planificacionArrayList;
    private ObservableList<PlanificacionUnidadObraView> planificacionUnidadObraViewObservableList;
    private PlanificacionUnidadObraView planificacionUnidadObraView;
    private ArrayList<Bajoespecificacion> listofMateriales;
    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valToPlan;
    private double valCostCertBajo;
    private Bajoespecificacion bajo;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private Double[] valoresCert;
    private Double[] valoresCertRV;
    @FXML
    private JFXCheckBox checkActive;
    @FXML
    private JFXButton btn_close;
    private Alert alertprocess;
    private Runtime garbache;
    private Brigadaconstruccion brigadaCont;
    private Grupoconstruccion grupoconst;
    private Cuadrillaconstruccion cuadrillaConst;
    private static NumberFormat formatoDecimal = NumberFormat.getInstance();

    @FXML
    private ContextMenu menuOptios;

    public ObservableList<PlanificacionUnidadObraView> getPlanificacionUnidadObra(Unidadobra unidadobra) {
        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantidadPlan = 0.0;
            planificacionArrayList = (List<Planificacion>) session.createQuery("FROM Planificacion WHERE unidadobraId =: id").setParameter("id", unidadobra.getId()).getResultList();
            for (Planificacion certifica : planificacionArrayList) {
                if (certifica.getCuadrillaconstruccionId() == null) {
                    cantidadPlan = getCantCertificada(certifica.getUnidadobraId(), certifica.getDesde(), certifica.getHasta());
                    planificacionUnidadObraView = new PlanificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostomaterial(), certifica.getCostomano(), certifica.getCostoequipo(), certifica.getCostosalario(), certifica.getCostsalariocuc(), certifica.getDesde().toString(), certifica.getHasta().toString(), cantidadPlan, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), " ", certifica.getCantidad() - cantidadPlan);
                    planificacionUnidadObraViewObservableList.add(planificacionUnidadObraView);
                } else {
                    cantidadPlan = getCantCertificada(certifica.getUnidadobraId(), certifica.getDesde(), certifica.getHasta());
                    planificacionUnidadObraView = new PlanificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostomaterial(), certifica.getCostomano(), certifica.getCostoequipo(), certifica.getCostosalario(), certifica.getCostsalariocuc(), certifica.getDesde().toString(), certifica.getHasta().toString(), cantidadPlan, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), certifica.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo(), certifica.getCantidad() - cantidadPlan);
                    planificacionUnidadObraViewObservableList.add(planificacionUnidadObraView);
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
        return planificacionUnidadObraViewObservableList;
    }

    public Double[] getValoresCertUO(Integer idUO) {
        valoresCert = new Double[6];

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> recuo = session.createSQLQuery(" SELECT * FROM get_values_certificates_unidadobra(:idU)").setParameter("idU", idUO).getResultList();
            for (Object[] row : recuo) {
                if (row[0] == null) {
                    valoresCert[0] = 0.0;
                } else if (row[0] != null) {
                    valoresCert[0] = Double.parseDouble(row[0].toString());
                }

                if (row[1] == null) {
                    valoresCert[1] = 0.0;
                } else if (row[1] != null) {
                    valoresCert[1] = Double.parseDouble(row[1].toString());
                }
                if (row[2] == null) {
                    valoresCert[2] = 0.0;
                } else if (row[2] != null) {
                    valoresCert[2] = Double.parseDouble(row[2].toString());
                }
                if (row[3] == null) {
                    valoresCert[3] = 0.0;
                } else if (row[3] != null) {
                    valoresCert[3] = Double.parseDouble(row[3].toString());
                }
                if (row[4] == null) {
                    valoresCert[4] = 0.0;
                } else if (row[4] != null) {
                    valoresCert[4] = Double.parseDouble(row[4].toString());
                }
                if (row[5] == null) {
                    valoresCert[5] = 0.0;
                } else if (row[5] != null) {
                    valoresCert[5] = Double.parseDouble(row[5].toString());
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
        return valoresCert;
    }

    /**
     * Para determinar la cantidad certificad en un intervalo de tiempo
     *
     * @param idUnidadObra
     * @return
     */

    public Double getCantCertificada(int idUnidadObra, Date desde, Date hasta) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantidadPlan = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id AND desde >=: ini AND hasta <=: fin").setParameter("id", idUnidadObra).setParameter("ini", desde).setParameter("fin", hasta);
            if (query.getSingleResult() != null) {
                cantidadPlan = (Double) query.getSingleResult();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantidadPlan;
    }

    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            brigadaconstruccionsArrayList = (List<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion where empresaconstructoraId=: id").setParameter("id", idEmp).getResultList();
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

            Brigadaconstruccion brigadaconstruccion = session.get(Brigadaconstruccion.class, idBrigada);
            grupoconstruccionArrayList = (List<Grupoconstruccion>) brigadaconstruccion.getGrupoconstruccionsById();
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

            Grupoconstruccion grupoconstruccion = session.get(Grupoconstruccion.class, idGrupo);
            cuadrillaconstruccionsArrayList = (List<Cuadrillaconstruccion>) grupoconstruccion.getCuadrillaconstruccionsById();
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

    public Integer addPlanificacion(Planificacion planif) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer certId = null;
        try {
            tx = session.beginTransaction();
            certId = (Integer) session.save(planif);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certId;
    }

    public Unidadobra getUnidad(int idUO) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobraModel = session.get(Unidadobra.class, idUO);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobraModel;
    }

    public void deletePlanificacion(int idCert) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Integer op1 = session.createQuery("DELETE FROM Planrecuo WHERE planId =: id").setParameter("id", idCert).executeUpdate();
            Integer op2 = session.createQuery(" DELETE FROM Planificacion WHERE id =: idP ").setParameter("idP", idCert).executeUpdate();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private int idCuadrilla;

    public void loadDatosCertificaciones() {

        cant.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("cantidad"));
        desde.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("desde"));
        hasta.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("hasta"));
        brigada.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, IntegerProperty>("idBrigada"));
        grupo.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, IntegerProperty>("idGrupo"));
        cuadrilla.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, IntegerProperty>("idCuadrilla"));
        materiales.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costmaterial"));
        mano.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costmano"));
        equipo.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costequipo"));
        certificado.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("disponible"));
    }

    public Double getCantCertificada(int idUnidadObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantidadPlan = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id ").setParameter("id", idUnidadObra);
            if (query.getSingleResult() != null) {
                cantidadPlan = (Double) query.getSingleResult();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantidadPlan;
    }

    private UsuariosDAO usuariosDAO;

    private planificaRenglonSingleton renglonSingelton = planificaRenglonSingleton.getInstance();

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");

        brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
        grupoList = listOfGrupos(brigadaCont.getId());
        combogrupo.setItems(grupoList);

        garbache = Runtime.getRuntime();
        garbache.gc();

    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");

        grupoconst = grupoconstruccionArrayList.stream().filter(g -> g.getCodigo().contentEquals(codeGrupPart[0])).findFirst().orElse(null);
        cuadrilaList = listOfCuadrilla(grupoconst.getId());
        combocuadrilla.setItems(cuadrilaList);

        garbache = Runtime.getRuntime();
        garbache.gc();

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

    private PlanificarRecursosRenglones planificarRecursosRenglones = PlanificarRecursosRenglones.getInstance();

    public void cargarUnidadeObra(Unidadobra unidad) {
        formatoDecimal.setMaximumFractionDigits(4);
        usuariosDAO = UsuariosDAO.getInstance();
        unidadObraId = unidad.getId();
        unidadobraModel = unidad;
        brigadasList = listOfBrigadas(unidadobraModel.getEmpresaconstructoraByEmpresaconstructoraId().getId());
        combo_Brigada.setItems(brigadasList);

        combo_Brigada.getSelectionModel().select(brigadasList.get(0));
        String[] codeBriPart = brigadasList.get(0).split(" - ");
        brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
        grupoList = listOfGrupos(brigadaCont.getId());
        combogrupo.setItems(grupoList);
        combogrupo.getSelectionModel().select(grupoList.get(0));


        loadDatosCertificaciones();
        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(unidadobraModel);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);

        labelTitle.setText("Planificar unidad de obra " + unidadobraModel.getCodigo());
        fieldcodigouo.setText(unidadobraModel.getCodigo());

        valoresCert = new Double[6];
        valoresCert = getValoresCertUO(unidadObraId);

        if (valoresCert != null) {

            cantidadCert = 0.0;
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
        } else {
            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            costManoCert = 0.0;
            costEquipoCert = 0.0;
            costCUCSalarioCert = 0.0;
            costSalarioCert = 0.0;
        }
        double val = unidadobraModel.getCantidad() - unidadobraModel.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        if (new BigDecimal(String.format("%.4f", val)).doubleValue() > 0) {
            labelDispo.setText(Double.toString(new BigDecimal(String.format("%.4f", val)).doubleValue()));
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() < 0) {
            labelDispo.setText("0.0");
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() == 0) {
            btnAdd.setDisable(true);
        }
        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : menuOptios.getItems()) {
                item.setDisable(true);
            }
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planificarUnidaddeObraController = this;
        checkActive.setOnMouseClicked(event -> {
            if (checkActive.isSelected() == true) {
                combocuadrilla.setDisable(false);
                String[] codeGrupPart = combogrupo.getValue().split(" - ");

                grupoconst = grupoconstruccionArrayList.stream().filter(g -> g.getCodigo().contentEquals(codeGrupPart[0])).findFirst().orElse(null);
                cuadrilaList = listOfCuadrilla(grupoconst.getId());
                combocuadrilla.setItems(cuadrilaList);

                garbache = Runtime.getRuntime();
                garbache.gc();
            } else if (checkActive.isSelected() == false) {
                combocuadrilla.setDisable(true);
                combocuadrilla.getSelectionModel().clearSelection();
            }

        });

        LocalDate t1 = LocalDate.now();
        LocalDate inicio = t1.with(firstDayOfMonth());
        LocalDate fin = t1.with(lastDayOfMonth());

        dateini.setValue(inicio);
        datehasta.setValue(fin);


    }

    public Planificacion getCertfificacionDatosToCheCk(Planificacion certificItem) {
        System.out.println(certificItem.getCuadrillaconstruccionId());
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Planificacion> datos = session.createQuery(" FROM Planificacion WHERE unidadobraId =: idU ").setParameter("idU", certificItem.getUnidadobraId()).getResultList();
            Planificacion certif = datos.parallelStream().filter(item -> item.getBrigadaconstruccionId() == certificItem.getBrigadaconstruccionId() && item.getGrupoconstruccionId() == certificItem.getGrupoconstruccionId() && String.valueOf(item.getCuadrillaconstruccionId()).equals(String.valueOf(certificItem.getCuadrillaconstruccionId())) && item.getDesde().compareTo(certificItem.getDesde()) == 0 && item.getHasta().compareTo(certificItem.getHasta()) == 0).findFirst().orElse(null);

            tx.commit();
            session.close();
            return certif;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Planificacion();
    }

    public void handleAddCertificacion(ActionEvent event) {

        if (usuariosDAO.usuario.getGruposId() == 6) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Operación Incorrecta");
            alert.setContentText("Su grupo de usuario no cuenta con permisos necesarios para realizar esta operación");
            alert.showAndWait();
        } else {
            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("Calculando elementos en la unidad de obra... espere");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();

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

                if (checkActive.isSelected()) {
                    String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                    idCuadrilla = cuadrillaconstruccionsArrayList.stream().filter(c -> c.getCodigo().contentEquals(codeCuadriPart[0])).findFirst().map(Cuadrillaconstruccion::getId).get();
                } else {
                    String[] partGrupo = combogrupo.getValue().split(" - ");
                    grupoconst = grupoconstruccionArrayList.parallelStream().filter(item -> item.getCodigo().equals(partGrupo[0].toString())).findFirst().get();
                    idCuadrilla = grupoconst.getCuadrillaconstruccionsById().parallelStream().collect(Collectors.toList()).get(0).getId();
                }

                valCM = 0.0;
                valCMan = 0.0;
                valEquip = 0.0;
                valSal = 0.0;
                valCucSal = 0.0;

                valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
                valCMan = unidadobraModel.getCostomano() - costManoCert;
                valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
                valSal = unidadobraModel.getSalario() - costSalarioCert;
                valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

                costMaterial = 0.0;
                costMano = 0.0;
                costEquipo = 0.0;
                costSalario = 0.0;
                costCUCSalario = 0.0;

                valToPlan = 0.0;
                valToPlan = unidadobraModel.getCantidad() - valoresCert[0];


                costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;

                String pattern = "dd/MM/yyyy";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                LocalDate dateDes = dateini.getValue();
                LocalDate dateHast = datehasta.getValue();
                desdeDate = Date.valueOf(dateDes);
                hastaData = Date.valueOf(dateHast);


                planificacion = new Planificacion();
                planificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
                planificacion.setCostomano(Math.round(costMano * 100d) / 100d);
                planificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
                planificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
                planificacion.setCostsalariocuc(Math.round(costCUCSalario * 100d) / 100d);
                planificacion.setBrigadaconstruccionId(brigadaCont.getId());
                planificacion.setGrupoconstruccionId(grupoconst.getId());
                planificacion.setCuadrillaconstruccionId(idCuadrilla);
                planificacion.setUnidadobraId(unidadObraId);
                planificacion.setDesde(desdeDate);
                planificacion.setHasta(hastaData);
                planificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));

                Planificacion certificaionControl = getCertfificacionDatosToCheCk(planificacion);
                //certificaionControl.getCantidad();

                if (certificaionControl == null) {
                    saveCertificacionInBD(planificacion);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(" Ya existe una certificación con la misma relación de brigada, grupo y cuadrilla en este mes. Escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores ");

                    ButtonType buttonAgregar = new ButtonType("Sumar");
                    ButtonType buttonSobre = new ButtonType("Sobreescribir");
                    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == buttonAgregar) {
                        sumarCertificacion(certificaionControl, planificacion);
                    } else if (result.get() == buttonSobre) {
                        sobreEscribirCertificacion(certificaionControl, planificacion);

                    }
                }
            }
        }
    }

    public void sumarCertificacion(Planificacion certificaionControl, Planificacion certificacion) {
        double volumenAnterior = certificaionControl.getCantidad();
        double volumenNuevo = certificacion.getCantidad();
        double volumenTotal = volumenAnterior + volumenNuevo;

        double valToPlan = unidadobraModel.getCantidad() - valoresCert[0];
        double valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
        double valCMan = unidadobraModel.getCostomano() - costManoCert;
        double valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
        double valSal = unidadobraModel.getSalario() - costSalarioCert;
        double valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

        double costMaterial = valCM * volumenTotal / valToPlan;
        double costMano = valCMan * volumenTotal / valToPlan;
        double costEquipo = valEquip * volumenTotal / valToPlan;
        double costSalario = valSal * volumenTotal / valToPlan;
        double costCUCSalario = valCucSal * volumenTotal / valToPlan;

        LocalDate dateDes = dateini.getValue();
        LocalDate dateHast = datehasta.getValue();
        desdeDate = Date.valueOf(dateDes);
        hastaData = Date.valueOf(dateHast);

        if (volumenTotal <= Double.parseDouble(labelDispo.getText())) {

            Planificacion certificacionSuma = new Planificacion();
            certificacionSuma.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            certificacionSuma.setCostomano(Math.round(costMano * 100d) / 100d);
            certificacionSuma.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            certificacionSuma.setCostosalario(Math.round(costSalario * 100d) / 100d);
            certificacionSuma.setCostsalariocuc(Math.round(costCUCSalario * 100d) / 100d);
            certificacionSuma.setBrigadaconstruccionId(certificacion.getBrigadaconstruccionId());
            certificacionSuma.setGrupoconstruccionId(certificacion.getGrupoconstruccionId());
            certificacionSuma.setCuadrillaconstruccionId(certificacion.getCuadrillaconstruccionId());
            certificacionSuma.setUnidadobraId(unidadObraId);
            certificacionSuma.setDesde(desdeDate);
            certificacionSuma.setHasta(hastaData);
            certificacionSuma.setCantidad(volumenTotal);

            deletePlanificacion(certificaionControl.getId());
            saveCertificacionInBD(certificacionSuma);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No existen volumenes disponibles para certificar!");
            // alert.setContentText("");
            alert.showAndWait();
        }
    }

    public void sobreEscribirCertificacion(Planificacion certificaionControl, Planificacion certificacion) {
        if (certificacion.getCantidad() <= Double.parseDouble(labelDispo.getText())) {
            deletePlanificacion(certificaionControl.getId());
            saveCertificacionInBD(certificacion);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No existen volumenes disponibles para certificar!");
            // alert.setContentText("");
            alert.showAndWait();
        }
    }


    public void saveCertificacionInBD(Planificacion certificacion) {
        Integer idcert = addPlanificacion(certificacion);
        renglonSingelton.cargarDatos(unidadObraId, idcert, desdeDate, hastaData, certificacion.getCantidad(), Double.valueOf(valToPlan));
        planificarRecursosRenglones.cargarDatos(unidadObraId, idcert, desdeDate, hastaData, certificacion.getCantidad(), Double.valueOf(valToPlan));

        if (idcert != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();
        }

        fieldcantidad.clear();

        loadDatosCertificaciones();
        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(unidadobraModel);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);


        valoresCert = new Double[6];
        valoresCert = getValoresCertUO(unidadObraId);
        cantidadCert = 0.0;
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

        double val = unidadobraModel.getCantidad() - getUnidad(unidadobraModel.getId()).getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        if (new BigDecimal(String.format("%.4f", val)).doubleValue() > 0) {
            labelDispo.setText(Double.toString(new BigDecimal(String.format("%.4f", val)).doubleValue()));
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() < 0) {
            labelDispo.setText("0.0");
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() == 0) {
            btnAdd.setDisable(true);
        }
    }


    public void handleDeleteCertificaciones(ActionEvent event) {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();


        deletePlanificacion(planificacionUnidadObraView.getId());
        System.out.println("Para eliminar: " + planificacionUnidadObraView.getId());

        loadDatosCertificaciones();
        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(unidadobraModel);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);

        valoresCert = new Double[6];
        valoresCert = getValoresCertUO(unidadObraId);
        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;
        costMaterialCert = valoresCert[1];
        costManoCert = valoresCert[2];
        costEquipoCert = valoresCert[3];
        costCUCSalarioCert = valoresCert[4];
        costSalarioCert = valoresCert[4];


        double val = unidadobraModel.getCantidad() - valoresCert[0];
        labelDispo.setText(String.valueOf(val));
        if (val == 0) {
            btnAdd.setDisable(true);
        }

    }

    public void handleAjustarPlan(ActionEvent event) {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjustarPlan.fxml"));
            Parent proot = loader.load();
            AjustarPlanController controller = loader.getController();
            controller.cargarUnidadesObra(planificacionUnidadObraView, unidadobraModel.getCodigo(), planificacionUnidadObraView.getPendientes());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadDatosCertificaciones();
                planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
                planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(unidadobraModel);
                tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);

            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void handleTranferirPlan(ActionEvent event) {
        Planificacion planificacion = planificacionArrayList.parallelStream().filter(plan -> plan.getId() == tablePlanificacion.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TranferirPlan.fxml"));
            Parent proot = loader.load();
            TransferirPlanController controller = loader.getController();
            controller.sentController(planificacion, brigadaconstruccionsArrayList, planificarUnidaddeObraController);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                loadDatosCertificaciones();
                planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
                planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(unidadobraModel);
                tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);

            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
