package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class PlanObrasController implements Initializable {


    @FXML
    private Label label_title;

    @FXML
    private JFXComboBox<String> comboObras;

    @FXML
    private JFXComboBox<String> comboEmpresas;

    @FXML
    private JFXComboBox<String> comboZonas;

    @FXML
    private JFXComboBox<String> comboObjetos;

    @FXML
    private JFXComboBox<String> comboNivel;

    @FXML
    private JFXComboBox<String> comboEspecialidad;

    @FXML
    private JFXComboBox<String> comboSubespecialidad;

    @FXML
    private TableView<PlanMainViews> tableUnidades;

    private static ValCertificadosUO valores;

    @FXML
    private TableColumn<PlanMainViews, StringProperty> descripField;

    @FXML
    private TableColumn<PlanMainViews, StringProperty> umField;

    @FXML
    private TableColumn<PlanMainViews, DoubleProperty> presupField;
    @FXML
    private TableColumn<PlanMainViews, String> codeFirld;

    @FXML
    private TableColumn<PlanMainViews, DoubleProperty> certifField;

    @FXML
    private TableColumn<PlanMainViews, DoubleProperty> disponible;

    private ReportProjectStructureSingelton reportProjectStructureSingelton;

    private ObservableList<PlanMainViews> planMainViewsObservableList;
    private Unidadobra unidad;

    @FXML
    private JFXButton recalc;

    @FXML
    private JFXComboBox<String> comboBrigada;

    @FXML
    private JFXComboBox<String> comboGrupo;
    @FXML
    private TableColumn<PlanMainViews, String> planField;
    @FXML
    private JFXComboBox<String> comboCuadrilla;
    @FXML
    private JFXCheckBox checkCuadrilla;
    private ObservableList<String> cuadrilaList;


    @FXML
    private JFXDatePicker dateDesde;

    @FXML
    private JFXDatePicker dateHasta;
    private java.sql.Date desdeSDate;
    private java.sql.Date hastaSData;

    @FXML
    private JFXComboBox<String> comboCalC;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();


    private Double getcantPlanificada(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        double cant = 0.0;
        try {

            LocalDate dateDes = dateDesde.getValue();
            LocalDate dateHast = dateHasta.getValue();
            desdeSDate = Date.valueOf(dateDes);
            hastaSData = Date.valueOf(dateHast);
            tx = session.beginTransaction();
            Query query = session.createQuery("SELECT SUM(cantidad) from Planificacion where unidadobraId=: idU AND desde >=: desd AND hasta <=: hast ").setParameter("idU", unidadobra.getId()).setParameter("desd", desdeSDate).setParameter("hast", hastaSData);
            if (query.getResultList().get(0) == null) {
                cant = 0.0;
            } else {
                cant = (Double) query.getResultList().get(0);
            }
            tx.commit();
            session.close();

            return cant;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cant;
    }

    private Double getcantCertfificada(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Certificacion> certificacionList = session.createQuery("from Certificacion where unidadobraId=: idU").setParameter("idU", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionList.parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }

    private List<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private List<Unidadobra> unidadobraList;

    private Unidadobra getUnidadObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidad = new Unidadobra();
            unidad = session.get(Unidadobra.class, id);

            tx.commit();
            session.close();
            return unidad;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return unidad;
    }

    private ObservableList<String> brigadasList;

    private List<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private ObservableList<String> grupoList;

    public void handleZonasEmpresa(ActionEvent event) {
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().equals(comboObras.getValue().trim().trim())).findFirst().get();
        if (comboZonas.getItems().size() > 0) {
            comboZonas.getItems().clear();
            comboZonas.getSelectionModel().clearSelection();
        }
        comboZonas.getItems().setAll(reportProjectStructureSingelton.getZonasList(obra.getId()).parallelStream().filter(st -> !st.equals("Todas")).collect(Collectors.toList()));

        comboEmpresas.getSelectionModel().clearSelection();

        comboEmpresas.getItems().setAll(reportProjectStructureSingelton.getEmpresaList(obra.getId()));

        comboObjetos.getSelectionModel().clearSelection();

        comboNivel.getSelectionModel().clearSelection();

        comboEspecialidad.getSelectionModel().clearSelection();
        comboEspecialidad.getItems().setAll(reportProjectStructureSingelton.getEspecialidadesList().parallelStream().filter(st -> !st.equals("Todas")).collect(Collectors.toList()));
        comboSubespecialidad.getSelectionModel().clearSelection();
    }

    public void handleObjetos(ActionEvent event) {
        Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
        comboObjetos.getSelectionModel().clearSelection();
        comboNivel.getSelectionModel().clearSelection();
        comboEspecialidad.getSelectionModel().clearSelection();
        comboSubespecialidad.getSelectionModel().clearSelection();
        comboObjetos.getItems().setAll(reportProjectStructureSingelton.getObjetosList(zonas.getId()).parallelStream().filter(st -> !st.equals("Todos")).collect(Collectors.toList()));

    }

    public void handleNivel(ActionEvent event) {
        Objetos objetos = reportProjectStructureSingelton.objetosArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();
        comboNivel.getSelectionModel().clearSelection();
        comboEspecialidad.getSelectionModel().clearSelection();
        comboSubespecialidad.getSelectionModel().clearSelection();
        comboNivel.getItems().setAll(reportProjectStructureSingelton.getNivelList(objetos.getId()).parallelStream().filter(st -> !st.equals("Todos")).collect(Collectors.toList()));

    }

    public void handleSubespecilaidad(ActionEvent event) {
        Especialidades especialidades = reportProjectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().get();
        comboSubespecialidad.getItems().clear();
        comboSubespecialidad.getSelectionModel().clearSelection();
        comboSubespecialidad.getItems().setAll(reportProjectStructureSingelton.getSubespecialidadesObservableList(especialidades.getId()).parallelStream().filter(st -> !st.equals("Todas")).collect(Collectors.toList()));

    }

    private planificaRenglonSingleton renglonSingelton = planificaRenglonSingleton.getInstance();

    private PlanificarRecursosRenglones planificarRecursosRenglones = PlanificarRecursosRenglones.getInstance();

    private UsuariosDAO usuariosDAO;

    public static ValCertificadosUO getValoresCertificadosUO(int idU) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            double cant;
            double mate;
            double mano;
            double equip;
            double sal;
            Tuple tuple = session.createQuery(" SELECT SUM(cantidad), SUM(costmaterial), SUM(costmano), SUM(costequipo), SUM(salario) from Certificacion where unidadobraId=: idU ", Tuple.class).setParameter("idU", idU).getSingleResult();
            if (tuple.get(0) == null) {
                cant = 0.0;
            } else {
                cant = Double.parseDouble(tuple.get(0).toString());
            }
            if (tuple.get(1) == null) {
                mate = 0.0;
            } else {
                mate = Double.parseDouble(tuple.get(1).toString());
            }
            if (tuple.get(2) == null) {
                mano = 0.0;
            } else {
                mano = Double.parseDouble(tuple.get(2).toString());
            }
            if (tuple.get(3) == null) {
                equip = 0.0;
            } else {
                equip = Double.parseDouble(tuple.get(3).toString());
            }
            if (tuple.get(4) == null) {
                sal = 0.0;
            } else {
                sal = Double.parseDouble(tuple.get(4).toString());
            }
            valores = new ValCertificadosUO(Math.round(cant * 10000d) / 10000d, Math.round(mate * 10000d) / 10000d, Math.round(mano * 10000d) / 10000d, Math.round(equip * 10000d) / 10000d, Math.round(sal * 10000d) / 10000d);

            tx.commit();
            session.close();
            return valores;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return valores;
    }

    private ObservableList<PlanMainViews> getPlanMainViewsObservableList(int ob, int emp, int zon, int obj, int niv, int esp, int sub) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            this.planMainViewsObservableList = FXCollections.observableArrayList();
            this.unidadobraList = new ArrayList<>();
            this.unidadobraList = session.createQuery("FROM Unidadobra WHERE obraId =: idO and empresaconstructoraId =: idem and zonasId=:idz and objetosId=:idob and nivelId=: nii and especialidadesId=:ides and subespecialidadesId=:sub").setParameter("idO", Integer.valueOf(ob)).setParameter("idem", Integer.valueOf(emp)).setParameter("idz", Integer.valueOf(zon)).setParameter("idob", Integer.valueOf(obj)).setParameter("nii", Integer.valueOf(niv)).setParameter("ides", Integer.valueOf(esp)).setParameter("sub", Integer.valueOf(sub)).getResultList();
            for (Unidadobra unidadobra : this.unidadobraList) {
                double totalPlan = getcantPlanificada(unidadobra).doubleValue();
                double totalCert = getcantCertfificada(unidadobra).doubleValue();
                double dispo = new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue() - new BigDecimal(String.format("%.4f", Double.valueOf(totalCert))).doubleValue();
                if (unidadobra.getCantidad() != 0.0D && totalCert == unidadobra.getCantidad()) {
                    planMainViewsObservableList.add(new PlanMainViews(unidadobra.getId(), unidadobra.getCodigo().concat("(*)"), unidadobra.getDescripcion(), unidadobra.getUm(), new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue(), Double.toString(new BigDecimal(String.format("%.4f", totalPlan)).doubleValue()), new BigDecimal(String.format("%.4f", totalCert)).doubleValue(), new BigDecimal(String.format("%.4f", dispo)).doubleValue()));
                } else {
                    planMainViewsObservableList.add(new PlanMainViews(unidadobra.getId(), unidadobra.getCodigo(), unidadobra.getDescripcion(), unidadobra.getUm(), new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue(), Double.toString(new BigDecimal(String.format("%.4f", totalPlan)).doubleValue()), new BigDecimal(String.format("%.4f", totalCert)).doubleValue(), new BigDecimal(String.format("%.4f", dispo)).doubleValue()));
                }
            }
            tx.commit();
            session.close();
            return this.planMainViewsObservableList;
        } catch (HibernateException he) {
            if (tx != null)
                tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private Unidadobra getUnidadobra(int id) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getId() == id).findFirst().orElse(null);
    }

    private double dispo;
    private double valToPlan;
    private Unidadobra unidadobraModel;

    public void handleCargarUnidades(ActionEvent event) {
        Obra obra = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue().trim().trim())).findFirst().get();
        Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();
        Zonas zonas = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
        Objetos objetos = reportProjectStructureSingelton.objetosArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();
        Nivel nivel = reportProjectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();
        Especialidades especialidades = reportProjectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().get();
        Subespecialidades subespecialidades = reportProjectStructureSingelton.subespecialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboSubespecialidad.getValue().trim())).findFirst().get();


        codeFirld.setCellValueFactory(new PropertyValueFactory<PlanMainViews, String>("code"));
        descripField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, StringProperty>("descrip"));
        umField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, StringProperty>("um"));
        presupField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, DoubleProperty>("presp"));
        planField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, String>("plan"));
        planField.setCellFactory(TextFieldTableCell.<PlanMainViews>forTableColumn());
        planField.setOnEditCommit((TableColumn.CellEditEvent<PlanMainViews, String> t) -> {
            ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPlan(t.getNewValue());
            String valCant = ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).getPlan();
            int idUO = ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
            dispo = ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDisp();
            if (Double.parseDouble(valCant) > dispo) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operación Incorrecta");
                alert.setContentText("La cantidad especificada es mayor que la cantidad disponible");
                ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPlan("0.0");
                alert.showAndWait();
            } else if (dispo == 0.0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Operación Incorrecta");
                alert.setContentText("No se puede certificar unidades de obra sin volumenes disponibles");
                ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPlan("0.0");
                alert.showAndWait();
            } else {
                if (usuariosDAO.usuario.getGruposId() == 6) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Operación Incorrecta");
                    alert.setContentText("Su grupo de usuario no cuenta con permisos necesarios para realizar esta operación");
                    alert.showAndWait();
                    ((PlanMainViews) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPlan("0.0");
                } else {
                    Unidadobra unidadobra = getUnidadobra(idUO);
                    createPlanificacion(unidadobra, Double.parseDouble(valCant));
                    handleCargarUnidades(event);
                }
            }
        });
        certifField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, DoubleProperty>("certf"));
        disponible.setCellValueFactory(new PropertyValueFactory<PlanMainViews, DoubleProperty>("disp"));

        if (tableUnidades.getItems().size() > 0) {
            tableUnidades.getItems().clear();
        }
        tableUnidades.getItems().setAll(getPlanMainViewsObservableList(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId()));
        tableUnidades.setEditable(true);

        codeFirld.setCellFactory(new Callback<TableColumn<PlanMainViews, String>, TableCell<PlanMainViews, String>>() {
            @Override
            public TableCell<PlanMainViews, String> call(TableColumn<PlanMainViews, String> param) {
                return new TableCell<PlanMainViews, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("(*)")) {
                                this.setTextFill(Color.RED);
                                setText(item.toString());
                            } else {
                                this.setTextFill(Color.BLACK);
                                setText(item.toString());
                            }
                        }
                    }
                };
            }
        });
    }

    private Planificacion getCertfificacionDatosToCheCk(Planificacion certificItem) {
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

    private void createPlanificacion(Unidadobra unidadobra, double cantidad) {
        ValCertificadosUO valores = getValoresCertificadosUO(unidadobra.getId());
        unidadobraModel = unidadobra;
        double valCM = 0.0;
        double valCMan = 0.0;
        double valEquip = 0.0;
        double valSal = 0.0;

        valToPlan = 0.0;
        valToPlan = unidadobraModel.getCantidad() - valores.getCant();

        valCM = unidadobraModel.getCostoMaterial() - valores.getMat();
        valCMan = unidadobraModel.getCostomano() - valores.getMano();
        valEquip = unidadobraModel.getCostoequipo() - valores.getEquip();
        valSal = unidadobraModel.getSalario() - valores.getSalario();


        double costMaterial = valCM * cantidad / valToPlan;
        double costMano = valCMan * cantidad / valToPlan;
        double costEquipo = valEquip * cantidad / valToPlan;
        double costSalario = valSal * cantidad / valToPlan;

        LocalDate dateDes = dateDesde.getValue();
        LocalDate dateHast = dateHasta.getValue();
        desdeSDate = Date.valueOf(dateDes);
        hastaSData = Date.valueOf(dateHast);

        Brigadaconstruccion brigadaCont = brigadaconstruccionsArrayList.parallelStream().filter(brg -> brg.toString().trim().equals(comboBrigada.getValue().trim())).findFirst().orElse(null);
        Grupoconstruccion grupoconst = brigadaCont.getGrupoconstruccionsById().parallelStream().filter(brg -> brg.toString().trim().equals(comboGrupo.getValue().trim())).findFirst().orElse(null);

        Planificacion certificacion = new Planificacion();
        certificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
        certificacion.setCostomano(Math.round(costMano * 100d) / 100d);
        certificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
        certificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
        certificacion.setCostsalariocuc(0.0);
        certificacion.setBrigadaconstruccionId(brigadaCont.getId());
        certificacion.setGrupoconstruccionId(grupoconst.getId());
        if (!checkCuadrilla.isSelected()) {
            certificacion.setCuadrillaconstruccionId(grupoconst.getCuadrillaconstruccionsById().parallelStream().collect(Collectors.toList()).get(0).getId());
        } else if (checkCuadrilla.isSelected()) {
            Cuadrillaconstruccion cuadrillaconstruccion = grupoconst.getCuadrillaconstruccionsById().parallelStream().filter(cuad -> cuad.toString().trim().equals(comboCuadrilla.getValue().trim())).findFirst().orElse(null);
            certificacion.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());
        }

        certificacion.setUnidadobraId(unidadobraModel.getId());
        certificacion.setDesde(desdeSDate);
        certificacion.setHasta(hastaSData);
        certificacion.setCantidad(cantidad);

        Planificacion certificaionControl = getCertfificacionDatosToCheCk(certificacion);

        if (certificaionControl == null) {
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

    public void sumarCertificacion(Planificacion certificaionControl, Planificacion certificacion) {
        double volumenAnterior = certificaionControl.getCantidad();
        double volumenNuevo = certificacion.getCantidad();
        double volumenTotal = volumenAnterior + volumenNuevo;

        ValCertificadosUO valores = getValoresCertificadosUO(unidadobraModel.getId());
        double valCM = 0.0;
        double valCMan = 0.0;
        double valEquip = 0.0;
        double valSal = 0.0;

        valToPlan = 0.0;
        valToPlan = unidadobraModel.getCantidad() - valores.getCant();

        valCM = unidadobraModel.getCostoMaterial() - valores.getMat();
        valCMan = unidadobraModel.getCostomano() - valores.getMano();
        valEquip = unidadobraModel.getCostoequipo() - valores.getEquip();
        valSal = unidadobraModel.getSalario() - valores.getSalario();

        double costMaterial = valCM * volumenTotal / valToPlan;
        double costMano = valCMan * volumenTotal / valToPlan;
        double costEquipo = valEquip * volumenTotal / valToPlan;
        double costSalario = valSal * volumenTotal / valToPlan;

        if (volumenTotal <= dispo) {

            Planificacion certificacionSuma = new Planificacion();
            certificacionSuma.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            certificacionSuma.setCostomano(Math.round(costMano * 100d) / 100d);
            certificacionSuma.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            certificacionSuma.setCostosalario(Math.round(costSalario * 100d) / 100d);
            certificacionSuma.setCostsalariocuc(0.0);
            certificacionSuma.setBrigadaconstruccionId(certificacion.getBrigadaconstruccionId());
            certificacionSuma.setGrupoconstruccionId(certificacion.getGrupoconstruccionId());
            certificacionSuma.setCuadrillaconstruccionId(certificacion.getCuadrillaconstruccionId());
            certificacionSuma.setUnidadobraId(certificacion.getUnidadobraId());
            certificacionSuma.setDesde(certificacion.getDesde());
            certificacionSuma.setHasta(certificacion.getHasta());
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
        if (certificacion.getCantidad() <= dispo) {
            deletePlanificacion(certificaionControl.getId());
            saveCertificacionInBD(certificacion);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No existen volumenes disponibles para certificar!");
            // alert.setContentText("");
            alert.showAndWait();
        }
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


    public void saveCertificacionInBD(Planificacion certificacion) {

        Integer idcert = addPlanificacion(certificacion);
        System.out.println(valToPlan);
        renglonSingelton.cargarDatos(certificacion.getUnidadobraId(), idcert, certificacion.getDesde(), certificacion.getHasta(), certificacion.getCantidad(), valToPlan);
        planificarRecursosRenglones.cargarDatos(certificacion.getUnidadobraId(), idcert, desdeSDate, hastaSData, certificacion.getCantidad(), valToPlan);


        if (idcert != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados satisfactoriamente!");
            alert.showAndWait();
        }
    }

    public void saveCertificacionInBDUpload(Planificacion certificacion) {

        try {
            Integer idcert = addPlanificacion(certificacion);
            renglonSingelton.cargarDatos(certificacion.getUnidadobraId(), idcert, certificacion.getDesde(), certificacion.getHasta(), certificacion.getCantidad(), valToPlan);
            planificarRecursosRenglones.cargarDatos(certificacion.getUnidadobraId(), idcert, desdeSDate, hastaSData, certificacion.getCantidad(), valToPlan);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    private List<Grupoconstruccion> grupoconstruccionArrayList;

    public void handleNewPlan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlanificarUnidaddeObra.fxml"));
            Parent proot = loader.load();
            PlanificarUnidaddeObraController controller = loader.getController();
            controller.cargarUnidadeObra(getUnidadObra(tableUnidades.getSelectionModel().getSelectedItem().getId()));


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                handleCargarUnidades(event);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();
        comboObras.getItems().setAll(reportProjectStructureSingelton.getObrasList());
        usuariosDAO = UsuariosDAO.getInstance();
        LocalDate t1 = LocalDate.now();
        LocalDate inicio = t1.with(firstDayOfMonth());
        LocalDate fin = t1.with(lastDayOfMonth());

        dateDesde.setValue(inicio);
        dateHasta.setValue(fin);

        checkCuadrilla.setOnMouseClicked(event -> {
            if (checkCuadrilla.isSelected()) {
                comboCuadrilla.setDisable(false);
                Grupoconstruccion grupoconst = grupoconstruccionArrayList.stream().filter(g -> g.toString().trim().equals(comboGrupo.getValue().trim())).findFirst().orElse(null);
                cuadrilaList = listOfCuadrilla(grupoconst.getId());
                comboCuadrilla.setItems(cuadrilaList);
            } else if (!checkCuadrilla.isSelected()) {
                comboCuadrilla.setDisable(true);
                comboCuadrilla.getSelectionModel().clearSelection();
            }
        });


        ObservableList<String> tarifasLst = FXCollections.observableArrayList("Recalcular Plan", "Importar .xlsx");
        comboCalC.setItems(tarifasLst);

    }

    public void handleCargarBrigadaAndGrupo(ActionEvent event) {
        Empresaconstructora empresaconstructora = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();
        brigadasList = listOfBrigadas(empresaconstructora.getId());
        comboBrigada.setItems(brigadasList);
        comboBrigada.getSelectionModel().select(brigadasList.get(0));
        Brigadaconstruccion brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.toString().trim().equals(comboBrigada.getValue().trim())).findFirst().orElse(null);
        grupoList = listOfGrupos(brigadaCont.getId());
        comboGrupo.setItems(grupoList);
        comboGrupo.getSelectionModel().select(grupoList.get(0));
    }

    public void handleLlenaGrupoList(ActionEvent event) {
        Brigadaconstruccion brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.toString().trim().equals(comboBrigada.getValue().trim())).findFirst().get();
        grupoList = listOfGrupos(brigadaCont.getId());
        comboGrupo.setItems(grupoList);
        comboGrupo.getSelectionModel().select(grupoList.get(0));
    }

    public ObservableList<String> listOfBrigadas(int idEmp) {
        brigadasList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaconstruccionsArrayList = (List<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: id").setParameter("id", idEmp).getResultList();
            brigadasList.addAll(brigadaconstruccionsArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.toString().trim()).collect(Collectors.toList()));

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
            grupoList.addAll(grupoconstruccionArrayList.parallelStream().map(grupoconstruccion -> grupoconstruccion.toString().trim()).collect(Collectors.toList()));


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
            cuadrilaList.addAll(cuadrillaconstruccionsArrayList.parallelStream().map(cuadrillaconstruccion -> cuadrillaconstruccion.toString().trim()).collect(Collectors.toList()));

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


    public void reloadPlan(ActionEvent event) {
        if (comboCalC.getValue().trim().equals("Recalcular Plan")) {
            List<Planificacion> planificacionList = new ArrayList<>();
            planificacionList = getPlanesList();
            deleteDatosPlanificacion();
            createUploadPlanes(planificacionList);
        }
    }


    public List<Planificacion> getPlanesList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LocalDate dateDes = dateDesde.getValue();
            LocalDate dateHast = dateHasta.getValue();
            desdeSDate = Date.valueOf(dateDes);
            hastaSData = Date.valueOf(dateHast);

            List<Planificacion> datosPlanes = session.createQuery(" FROM Planificacion WHERE desde =: valDesd AND hasta =: valHas ").setParameter("valDesd", desdeSDate).setParameter("valHas", hastaSData).getResultList();

            tx.commit();
            session.close();
            return datosPlanes;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void deleteDatosPlanificacion() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            LocalDate dateDes = dateDesde.getValue();
            LocalDate dateHast = dateHasta.getValue();
            desdeSDate = Date.valueOf(dateDes);
            hastaSData = Date.valueOf(dateHast);

            int op1 = session.createQuery(" DELETE Planrecuo WHERE ffin =: valDesd AND ffin =: valHas ").setParameter("valDesd", desdeSDate).setParameter("valHas", hastaSData).executeUpdate();
            int op2 = session.createQuery(" DELETE Planificacion WHERE desde =: valDesd AND hasta =: valHas ").setParameter("valDesd", desdeSDate).setParameter("valHas", hastaSData).executeUpdate();

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void createUploadPlanes(List<Planificacion> list) {

        for (Planificacion planificacion : list) {
            unidadobraModel = utilCalcSingelton.getUnidadobra(planificacion.getUnidadobraId());
            ValCertificadosUO valores = getValoresCertificadosUO(unidadobraModel.getId());
            double valCM = 0.0;
            double valCMan = 0.0;
            double valEquip = 0.0;
            double valSal = 0.0;

            valToPlan = 0.0;
            valToPlan = unidadobraModel.getCantidad() - valores.getCant();

            valCM = unidadobraModel.getCostoMaterial() - valores.getMat();
            valCMan = unidadobraModel.getCostomano() - valores.getMano();
            valEquip = unidadobraModel.getCostoequipo() - valores.getEquip();
            valSal = unidadobraModel.getSalario() - valores.getSalario();


            double costMaterial = valCM * planificacion.getCantidad() / valToPlan;
            double costMano = valCMan * planificacion.getCantidad() / valToPlan;
            double costEquipo = valEquip * planificacion.getCantidad() / valToPlan;
            double costSalario = valSal * planificacion.getCantidad() / valToPlan;

            LocalDate dateDes = dateDesde.getValue();
            LocalDate dateHast = dateHasta.getValue();
            desdeSDate = Date.valueOf(dateDes);
            hastaSData = Date.valueOf(dateHast);

            Planificacion certificacionSuma = new Planificacion();
            certificacionSuma.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            certificacionSuma.setCostomano(Math.round(costMano * 100d) / 100d);
            certificacionSuma.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            certificacionSuma.setCostosalario(Math.round(costSalario * 100d) / 100d);
            certificacionSuma.setCostsalariocuc(0.0);
            certificacionSuma.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
            certificacionSuma.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
            certificacionSuma.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
            certificacionSuma.setUnidadobraId(planificacion.getUnidadobraId());
            certificacionSuma.setDesde(planificacion.getDesde());
            certificacionSuma.setHasta(planificacion.getHasta());
            certificacionSuma.setCantidad(planificacion.getCantidad());
            try {
                saveCertificacionInBDUpload(certificacionSuma);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Información");
        alert.setContentText("TERMINEEEE");
        alert.showAndWait();


    }


}
