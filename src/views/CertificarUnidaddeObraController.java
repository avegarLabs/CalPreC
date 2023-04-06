package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJGroupLabel;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class CertificarUnidaddeObraController implements Initializable {

    public CertificarUnidaddeObraController certificarUnidaddeObraController;

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
    private TableView<CertificacionUnidadObraView> tableCertificaciones;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> cant;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> desde;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> hasta;
    @FXML
    private TableColumn<CertificacionUnidadObraView, IntegerProperty> brigada;
    @FXML
    private TableColumn<CertificacionUnidadObraView, IntegerProperty> grupo;
    @FXML
    private TableColumn<CertificacionUnidadObraView, IntegerProperty> cuadrilla;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> materiales;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> mano;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> equipo;
    @FXML
    private JFXCheckBox checkExp;
    private List<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private List<Grupoconstruccion> grupoconstruccionArrayList;
    private ObservableList<String> cuadrilaList;
    private List<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private ObservableList<String> brigadasList;
    private ObservableList<String> grupoList;

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
    private java.sql.Date desdeDate;
    private java.sql.Date hastaData;
    private double cantidadCert;
    private Certificacion certificacion;
    private List<Certificacion> certificacionArrayList;
    private ObservableList<CertificacionUnidadObraView> certificacionUnidadObraViewObservableList;
    private CertificacionUnidadObraView certificacionUnidadObraView;
    private ArrayList<Bajoespecificacion> listofMateriales;
    private double cantCertRecBajo;
    private double dispRecBajo;
    private double valToPlan;
    private double valCostCertBajo;
    private Bajoespecificacion bajo;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private Double[] valoresCert;
    private Double[] valoresCertRV;
    private ObservableList<DetalleCertificaReportView> detalleCertificaViewObservableList;
    private DetalleCertificaReportView detalleCertificaView;
    private Map parametros;
    private LocalDate date;
    private Empresa empresa;
    private Obra obra;
    @FXML
    private JFXCheckBox checkActive;
    private Runtime garbache;
    private double costMateSemi;
    private double costMateJueg;
    private Brigadaconstruccion brigadaCont;
    private Grupoconstruccion grupoconst;
    private Cuadrillaconstruccion cuadrillaconst;
    private StringBuilder label;
    private Planificacion plan;
    private int batchPlanrec = 20;
    private int countplan;
    private static NumberFormat formatoDecimal = NumberFormat.getInstance();

    @FXML
    private ContextMenu menuOptiosn;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public ObservableList<CertificacionUnidadObraView> getCertificaciones(Unidadobra unidadobra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            certificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
            costMaterial = 0.0;
            certificacionArrayList = (List<Certificacion>) session.createQuery("FROM  Certificacion where unidadobraId =: id").setParameter("id", unidadobra.getId()).getResultList();
            for (Certificacion certifica : certificacionArrayList) {
                certificacionUnidadObraViewObservableList.add(new CertificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostmaterial(), certifica.getCostmano(), certifica.getCostequipo(), certifica.getSalario(), certifica.getCucsalario(), certifica.getDesde().toString(), certifica.getHasta().toString(), 0.0, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), certifica.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo()));
            }

            tx.commit();
            session.close();
            return certificacionUnidadObraViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaconstruccionsArrayList = (List<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: id").setParameter("id", idEmp).getResultList();
            brigadasList.addAll(brigadaconstruccionsArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return brigadasList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> listOfGrupos(int idBrigada) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoList = FXCollections.observableArrayList();
            Brigadaconstruccion brigadaconstruccion = session.get(Brigadaconstruccion.class, idBrigada);
            grupoconstruccionArrayList = (List<Grupoconstruccion>) brigadaconstruccion.getGrupoconstruccionsById();
            grupoList.addAll(grupoconstruccionArrayList.parallelStream().map(grupoconstruccion -> grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion()).collect(Collectors.toList()));


            tx.commit();
            session.close();
            return grupoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> listOfCuadrilla(int idGrupo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuadrilaList = FXCollections.observableArrayList();
            Grupoconstruccion grupoconstruccion = session.get(Grupoconstruccion.class, idGrupo);
            cuadrillaconstruccionsArrayList = (List<Cuadrillaconstruccion>) grupoconstruccion.getCuadrillaconstruccionsById();
            cuadrilaList.addAll(cuadrillaconstruccionsArrayList.parallelStream().map(cuadrillaconstruccion -> cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return cuadrilaList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
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

    public Unidadobra getUnidad(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobraModel = session.get(Unidadobra.class, idUO);

            tx.commit();
            session.close();
            return unidadobraModel;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobraModel;
    }

    public void deleteCertificacion(int idCert) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacion = (Certificacion) session.createQuery("FROM Certificacion WHERE id =: val").setParameter("val", idCert).getSingleResult();
            if (certificacion != null) {
                int opd = session.createQuery("DELETE FROM Certificacionrecuo WHERE certificacionId =: idc").setParameter("idc", certificacion.getId()).executeUpdate();
                session.delete(certificacion);

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

    public Certificacion getCertificacion(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacion = new Certificacion();
            certificacion = (Certificacion) session.createQuery("FROM Certificacion WHERE id =: val").setParameter("val", id).getSingleResult();


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacion;
    }

    private int idCuadrilla;

    public void loadDatosCertificaciones() {

        cant.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("cantidad"));
        desde.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("desde"));
        hasta.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("hasta"));
        brigada.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, IntegerProperty>("idBrigada"));
        grupo.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, IntegerProperty>("idGrupo"));
        cuadrilla.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, IntegerProperty>("idCuadrilla"));
        materiales.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costmaterial"));
        mano.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costmano"));
        equipo.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costequipo"));
    }

    private UsuariosDAO usuariosDAO;

    private certificaRenglonSingelton renglonSingelton = certificaRenglonSingelton.getInstance();

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

    private CertificaRecursosRenglonesSingel certificaRecursosRenglonesSingel = CertificaRecursosRenglonesSingel.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        certificarUnidaddeObraController = this;

        empresa = getEmpresa();
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

    public void cargarUnidadeObra(int idUObra) {
        formatoDecimal.setMaximumFractionDigits(4);
        usuariosDAO = UsuariosDAO.getInstance();
        unidadobraModel = new Unidadobra();
        unidadobraModel = getUnidad(idUObra);
        unidadObraId = idUObra;
        brigadasList = listOfBrigadas(unidadobraModel.getEmpresaconstructoraByEmpresaconstructoraId().getId());
        if (brigadasList.size() > 0) {
            combo_Brigada.setItems(brigadasList);

            combo_Brigada.getSelectionModel().select(brigadasList.get(0));
            String[] codeBriPart = brigadasList.get(0).split(" - ");
            brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
            grupoList = listOfGrupos(brigadaCont.getId());
            combogrupo.setItems(grupoList);
            combogrupo.getSelectionModel().select(grupoList.get(0));
        }


        loadDatosCertificaciones();
        certificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        certificacionUnidadObraViewObservableList = getCertificaciones(unidadobraModel);
        tableCertificaciones.getItems().setAll(certificacionUnidadObraViewObservableList);


        labelTitle.setText("Certificar unidad de obra " + unidadobraModel.getCodigo());
        fieldcodigouo.setText(unidadobraModel.getCodigo());

        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;

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

        double val = unidadobraModel.getCantidad() - getUnidad(unidadobraModel.getId()).getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        System.out.println("Valor: " + val);
        if (new BigDecimal(String.format("%.4f", val)).doubleValue() > 0) {
            labelDispo.setText(Double.toString(new BigDecimal(String.format("%.4f", val)).doubleValue()));
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() < 0) {
            labelDispo.setText("0.0");
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() == 0) {
            btnAdd.setDisable(true);
        }
        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : menuOptiosn.getItems()) {
                item.setDisable(true);
            }
        }

    }

    public Certificacion getCertfificacionDatosToCheCk(Certificacion certificItem) {
        System.out.println(certificItem.getCuadrillaconstruccionId());
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Certificacion> datos = session.createQuery(" FROM Certificacion WHERE unidadobraId =: idU ").setParameter("idU", certificItem.getUnidadobraId()).getResultList();
            Certificacion certif = datos.parallelStream().filter(item -> item.getBrigadaconstruccionId() == certificItem.getBrigadaconstruccionId() && item.getGrupoconstruccionId() == certificItem.getGrupoconstruccionId() && String.valueOf(item.getCuadrillaconstruccionId()).equals(String.valueOf(certificItem.getCuadrillaconstruccionId())) && item.getDesde().compareTo(certificItem.getDesde()) == 0 && item.getHasta().compareTo(certificItem.getHasta()) == 0).findFirst().orElse(null);

            tx.commit();
            session.close();
            return certif;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Certificacion();
    }

    public void handleAddCertificacion(ActionEvent event) {
        if (usuariosDAO.usuario.getGruposId() == 6) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Operación Incorrecta");
            alert.setContentText("Su grupo de usuario no cuenta con permisos necesarios para realizar esta operación");
            alert.showAndWait();
        } else {
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
                /*
            } else if (combocuadrilla.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setContentText("Debe especificar una Cuadrilla de Contrucción!");
                alert.showAndWait();

                 */
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


                valToPlan = 0.0;
                valToPlan = unidadobraModel.getCantidad() - valoresCert[0];
                double valCM = unidadobraModel.getCostoMaterial() - costMaterialCert;
                double valCMan = unidadobraModel.getCostomano() - costManoCert;
                double valEquip = unidadobraModel.getCostoequipo() - costEquipoCert;
                double valSal = unidadobraModel.getSalario() - costSalarioCert;
                double valCucSal = unidadobraModel.getSalariocuc() - costCUCSalarioCert;

                double costMaterial = valCM * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                double costMano = valCMan * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                double costEquipo = valEquip * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                double costSalario = valSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;
                double costCUCSalario = valCucSal * Double.parseDouble(fieldcantidad.getText()) / valToPlan;

                LocalDate dateDes = dateini.getValue();
                LocalDate dateHast = datehasta.getValue();
                desdeDate = Date.valueOf(dateDes);
                hastaData = Date.valueOf(dateHast);

                certificacion = new Certificacion();
                certificacion.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
                certificacion.setCostmano(Math.round(costMano * 100d) / 100d);
                certificacion.setCostequipo(Math.round(costEquipo * 100d) / 100d);
                certificacion.setSalario(Math.round(costSalario * 100d) / 100d);
                certificacion.setCucsalario(Math.round(costCUCSalario * 100d) / 100d);
                certificacion.setBrigadaconstruccionId(brigadaCont.getId());
                certificacion.setGrupoconstruccionId(grupoconst.getId());
                certificacion.setCuadrillaconstruccionId(cuadrillaconst.getId());
                certificacion.setUnidadobraId(unidadObraId);
                certificacion.setDesde(desdeDate);
                certificacion.setHasta(hastaData);
                certificacion.setCantidad(Double.parseDouble(fieldcantidad.getText()));

                Certificacion certificaionControl = getCertfificacionDatosToCheCk(certificacion);

                if (certificaionControl == null) {
                    //System.out.println(" ggfgfgfgfgfgfgfgfgfg ");
                    saveCertificacionInBD(certificacion);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(" Ya existe una certificación con la misma relación de brigada, grupo y cuadrilla en este mes. Escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores ");

                    ButtonType buttonAgregar = new ButtonType("Sumar");
                    ButtonType buttonSobre = new ButtonType("Sobreescribir");
                    ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                    alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == buttonAgregar) {
                        sumarCertificacion(certificaionControl, certificacion);
                    } else if (result.get() == buttonSobre) {
                        sobreEscribirCertificacion(certificaionControl, certificacion);

                    }
                }
            }
        }
    }

    public void sumarCertificacion(Certificacion certificaionControl, Certificacion certificacion) {
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

        if (volumenTotal <= unidadobraModel.getCantidad()) {
            Certificacion certificacionSuma = new Certificacion();
            certificacionSuma.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
            certificacionSuma.setCostmano(Math.round(costMano * 100d) / 100d);
            certificacionSuma.setCostequipo(Math.round(costEquipo * 100d) / 100d);
            certificacionSuma.setSalario(Math.round(costSalario * 100d) / 100d);
            certificacionSuma.setCucsalario(Math.round(costCUCSalario * 100d) / 100d);
            certificacionSuma.setBrigadaconstruccionId(certificacion.getBrigadaconstruccionId());
            certificacionSuma.setGrupoconstruccionId(certificacion.getGrupoconstruccionId());
            certificacionSuma.setCuadrillaconstruccionId(certificacion.getCuadrillaconstruccionId());
            certificacionSuma.setUnidadobraId(unidadObraId);
            certificacionSuma.setDesde(desdeDate);
            certificacionSuma.setHasta(hastaData);
            certificacionSuma.setCantidad(volumenTotal);

            deleteCertificacion(certificaionControl.getId());
            saveCertificacionInBD(certificacionSuma);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No existen volumenes disponibles para certificar!");
            alert.showAndWait();
        }
    }

    public void sobreEscribirCertificacion(Certificacion certificaionControl, Certificacion certificacion) {
        if (certificacion.getCantidad() <= unidadobraModel.getCantidad()) {
            deleteCertificacion(certificaionControl.getId());
            saveCertificacionInBD(certificacion);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No existen volumenes disponibles para certificar!");
            // alert.setContentText("");
            alert.showAndWait();
        }
    }


    public void saveCertificacionInBD(Certificacion certificacion) {
        Integer idcert = addCertificacion(certificacion);
        renglonSingelton.cargarDatos(certificacion.getUnidadobraId(), idcert, certificacion.getDesde(), certificacion.getHasta(), certificacion.getCantidad(), Double.valueOf(valToPlan));
        certificaRecursosRenglonesSingel.CargarDatos(unidadObraId, idcert, desdeDate, hastaData, certificacion.getCantidad(), Double.valueOf(valToPlan));

        if (idcert != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();
        }

        fieldcantidad.clear();

        loadDatosCertificaciones();
        certificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        certificacionUnidadObraViewObservableList = getCertificaciones(unidadobraModel);
        tableCertificaciones.getItems().setAll(certificacionUnidadObraViewObservableList);

        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;
        valoresCert = new Double[6];
        valoresCert = getValoresCertUO(unidadObraId);
        cantidadCert = valoresCert[0];
        costMaterialCert = valoresCert[1];
        costManoCert = valoresCert[2];
        costEquipoCert = valoresCert[3];
        costCUCSalarioCert = valoresCert[5];
        costSalarioCert = valoresCert[4];

        double val = unidadobraModel.getCantidad() - getUnidad(unidadobraModel.getId()).getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        System.out.println("Valor: " + val);
        if (new BigDecimal(String.format("%.4f", val)).doubleValue() > 0) {
            labelDispo.setText(Double.toString(new BigDecimal(String.format("%.4f", val)).doubleValue()));
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() < 0) {
            labelDispo.setText("0.0");
        } else if (new BigDecimal(String.format("%.4f", val)).doubleValue() == 0) {
            btnAdd.setDisable(true);
        }
    }

    public void handleDeleteCertificaciones(ActionEvent event) {
        renglonSingelton = certificaRenglonSingelton.getInstance();
        formatoDecimal.setMaximumFractionDigits(4);
        certificacionUnidadObraView = tableCertificaciones.getSelectionModel().getSelectedItem();

        deleteCertificacion(certificacionUnidadObraView.getId());

        loadDatosCertificaciones();
        certificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        certificacionUnidadObraViewObservableList = getCertificaciones(unidadobraModel);
        tableCertificaciones.getItems().setAll(certificacionUnidadObraViewObservableList);

        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;
        valoresCert = new Double[6];
        valoresCert = getValoresCertUO(unidadObraId);
        cantidadCert = valoresCert[0];
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

    public void handleDetallecertRec(ActionEvent event) {

        certificacionUnidadObraView = tableCertificaciones.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetalleCertificarUnidadORecurso.fxml"));
            Parent proot = loader.load();
            DetalleCertificarUnidadOController controller = loader.getController();
            controller.cargarDatos(certificacionUnidadObraView.getId(), unidadobraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<DetalleCertificaReportView> getDetalleCertificacionRecursos(Integer niv) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            detalleCertificaViewObservableList = FXCollections.observableArrayList();
            List<Object[]> recursosList = session.createQuery(" SELECT plrv.recursoId, plrv.tipo, plrv.cantidad, plrv.costo, plrv.fini, plrv.ffin FROM Certificacionrecuo plrv  WHERE plrv.unidadobraId =: idNiv ").setParameter("idNiv", niv).getResultList();
            for (Object[] row : recursosList) {
                if (row[1].toString().contains("1")) {
                    Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[2].toString()), recursos.getPreciomn(), Double.parseDouble(row[3].toString()), row[4].toString(), row[5].toString()));
                } else if (row[1].toString().contains("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[2].toString()), recursos.getPreciomn(), Double.parseDouble(row[3].toString()), row[4].toString(), row[5].toString()));

                } else if (row[1].toString().contains("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[2].toString()), recursos.getPreciomn(), Double.parseDouble(row[3].toString()), row[4].toString(), row[5].toString()));
                }
            }
            tx.commit();
            session.close();
            return detalleCertificaViewObservableList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public Obra getObra(int idOb) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obra = session.get(Obra.class, idOb);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obra;
    }

    public Empresa getEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresa = session.get(Empresa.class, 1);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresa;
    }

    public void handleCreateReportListado(ActionEvent event) {

        detalleCertificaViewObservableList = FXCollections.observableArrayList();
        detalleCertificaViewObservableList = getDetalleCertificacionRecursos(unidadObraId);


        date = LocalDate.now();
        parametros = new HashMap<>();
        //obra = getObra(unidadobraModel.getObraByObraId());
        parametros.put("obraName", unidadobraModel.getObraByObraId().getCodigo() + " " + unidadobraModel.getObraByObraId().getDescripion());
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("empresa", empresa.getNombre());
        parametros.put("image", "templete/logoReport.jpg");

        try {
            if (checkExp.isSelected() == false) {
                DynamicReport dr = createReporteListadodeCertificaciones();
                JRDataSource ds = new JRBeanCollectionDataSource(detalleCertificaViewObservableList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                JasperViewer.viewReport(jp, false);
                garbache = Runtime.getRuntime();
                garbache.gc();
            } else {
                DynamicReport dr = createReporteListadodeCertificaciones();
                JRDataSource ds = new JRBeanCollectionDataSource(detalleCertificaViewObservableList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                exportarExcel(jp);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public DynamicReport createReporteListadodeCertificaciones() throws ClassNotFoundException {


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                // .setPageSizeAndOrientation(Page.Page_A4_Portrait())
                .setTemplateFile("templete/reportCertificacionGeneral.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("label", String.class.getName())
                .setTitle(" ").setWidth(20)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columnaFi = ColumnBuilder.getNew()
                .setColumnProperty("fini", String.class.getName())
                .setTitle("F. Inicio").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaFf = ColumnBuilder.getNew()
                .setColumnProperty("ffin", String.class.getName())
                .setTitle("F. Fin").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacost = ColumnBuilder.getNew()
                .setColumnProperty("costo", Double.class.getName())
                .setTitle("Costo").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        GroupBuilder gb1 = new GroupBuilder();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Cantidad", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Costo", glabelStyle);

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnaCantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnacost, DJCalculation.SUM, headerVariables, null, glabel2)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        drb.addGroup(g1);

        drb.addColumn(columngruopEmpresa);
        drb.addColumn(columnaFi);
        drb.addColumn(columnaFf);
        drb.addColumn(columnaum);
        drb.addColumn(columnaCantidad);
        drb.addColumn(columnaprecio);
        drb.addColumn(columnacost);


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }

    public void exportarExcel(JasperPrint jp) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {

            try {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
                exporter.exportReport();

                garbache = Runtime.getRuntime();
                garbache.gc();

            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void handleUsarComoPlan(ActionEvent event) {

        try {
            Certificacion certif = getCertificacion(tableCertificaciones.getSelectionModel().getSelectedItem().getId());
            Planificacion planificacion = getPlanificación(unidadobraModel.getId(), certif.getDesde(), certif.getHasta());


            if (planificacion == null) {
                try {
                    List<Certificacionrecuo> certificacionrecuoList = getCertificacionrecuoList(unidadobraModel, certif);
                    Integer idPlan = createPlanificacion(confortPlanificacion(unidadobraModel.getId(), certif));

                    List<Planrecuo> listPlanrec = new ArrayList<>();
                    for (Certificacionrecuo certificacionrecuo : certificacionrecuoList) {
                        listPlanrec.add(conformPlarecuo(unidadobraModel.getId(), idPlan, certificacionrecuo));
                    }
                    createPlanrecuo(listPlanrec);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("OK!");
                    alert.setContentText(" Certificación definida como plan ");
                    alert.showAndWait();

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setContentText(" Error al definir la certificación seleccionada como plan");
                    alert.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText(" La Unidad de Obra presenta volumenes planificados en este intervalo de tiempo");
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Ha ocurrido un error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private Planificacion getPlanificación(int id, Date desde, Date hasta) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            plan = null;
            List<Planificacion> list = session.createQuery("FROM Planificacion WHERE unidadobraId =: idU AND desde =: cerD AND  hasta =: cerH").setParameter("idU", id).setParameter("cerD", desde).setParameter("cerH", hasta).getResultList();
            if (list.size() > 0) {
                plan = list.get(0);
            }

            tx.commit();
            session.close();
            return plan;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return plan;
    }

    private Planificacion confortPlanificacion(int idUni, Certificacion planificacion) {

        Planificacion planificacion1 = new Planificacion();
        planificacion1.setUnidadobraId(idUni);
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostomaterial(planificacion.getCostmaterial());
        planificacion1.setCostomano(planificacion.getCostmano());
        planificacion1.setCostoequipo(planificacion.getCostequipo());
        planificacion1.setCostosalario(planificacion.getSalario());
        planificacion1.setCostsalariocuc(planificacion.getCucsalario());
        return planificacion1;
    }

    public int createPlanificacion(Planificacion planificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(planificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public List<Certificacionrecuo> getCertificacionrecuoList(Unidadobra unidadobra, Certificacion certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Certificacionrecuo> certificacionrecuoList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionrecuoList = session.createQuery("FROM Certificacionrecuo WHERE unidadobraId =: idUn AND certificacionId =: idPl ").setParameter("idUn", unidadobra.getId()).setParameter("idPl", certificacion.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private Planrecuo conformPlarecuo(Integer idUni, Integer idPlan, Certificacionrecuo planrecuo) {
        Planrecuo planrecuo1 = new Planrecuo();
        planrecuo1.setUnidadobraId(idUni);
        planrecuo1.setRenglonId(planrecuo.getRenglonId());
        planrecuo1.setRecursoId(planrecuo.getRecursoId());
        planrecuo1.setCantidad(planrecuo.getCantidad());
        planrecuo1.setCosto(planrecuo.getCosto());
        planrecuo1.setPlanId(idPlan);
        planrecuo1.setTipo(planrecuo.getTipo());
        planrecuo1.setFini(planrecuo.getFini());
        planrecuo1.setFfin(planrecuo.getFfin());
        planrecuo1.setCmateriales(planrecuo.getCmateriales());
        planrecuo1.setCmano(planrecuo.getCmano());
        planrecuo1.setCequipo(planrecuo.getCequipo());
        planrecuo1.setSalario(planrecuo.getSalario());
        planrecuo1.setSalariocuc(planrecuo.getSalariocuc());
        return planrecuo1;
    }

    public void createPlanrecuo(List<Planrecuo> planrecuoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Planrecuo planrecuo : planrecuoList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(planrecuo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    public void handleTranferirCertificacion(ActionEvent event) {
        Certificacion certificacion = certificacionArrayList.parallelStream().filter(plan -> plan.getId() == tableCertificaciones.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TranferirCertificacion.fxml"));
            Parent proot = loader.load();
            TransferirCertificacionController controller = loader.getController();
            controller.sentController(certificacion, brigadaconstruccionsArrayList, certificarUnidaddeObraController);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                loadDatosCertificaciones();
                certificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
                certificacionUnidadObraViewObservableList = getCertificaciones(unidadobraModel);
                tableCertificaciones.getItems().setAll(certificacionUnidadObraViewObservableList);

            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
