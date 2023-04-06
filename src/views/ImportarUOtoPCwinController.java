package views;

import AccessMigration.DatabaseEstructure;
import AccessMigration.PostreSqlOperation;
import AccessMigration.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class ImportarUOtoPCwinController implements Initializable {

    UnidadesObraObraController unidadesObraObraController;
    ArrayList<UnidadObraPCW> toCreateList;
    ArrayList<Renglonvariante> renglonvarianteArrayList;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXComboBox<String> selectEmpresa;
    @FXML
    private JFXComboBox<String> selectZonas;
    @FXML
    private JFXComboBox<String> selectObjetos;
    @FXML
    private JFXComboBox<String> selectNivel;
    @FXML
    private JFXComboBox<String> selectEspecialidad;
    @FXML
    private JFXComboBox<String> selectSubespecialidad;
    @FXML
    private TableView<UOPCWINView> tableUnidad;
    @FXML
    private TableColumn<UOPCWINView, JFXCheckBox> uoCode;
    @FXML
    private TableColumn<UOPCWINView, DoubleProperty> uoCant;
    @FXML
    private TableColumn<UOPCWINView, DoubleProperty> uoCosto;
    @FXML
    private TableView<UORVtoPCWin> tableRV;
    @FXML
    private TableColumn<UORVtoPCWin, StringProperty> rvCode;
    @FXML
    private TableColumn<UORVtoPCWin, DoubleProperty> rvCant;
    @FXML
    private TableColumn<UORVtoPCWin, DoubleProperty> rvCosto;
    @FXML
    private TableView<UOSUMtoPCWin> tableSuminist;
    @FXML
    private TableColumn<UOSUMtoPCWin, StringProperty> sumCode;
    @FXML
    private TableColumn<UOSUMtoPCWin, DoubleProperty> sumCant;
    @FXML
    private TableColumn<UOSUMtoPCWin, DoubleProperty> sumCosto;
    @FXML
    private Label pathMDB;
    @FXML
    private JFXCheckBox flagSuministros;
    private ObservableList<UOPCWINView> uopcwinViewObservableList;
    private ObservableList<UORVtoPCWin> uorVtoPCWinObservableList;
    private ObservableList<UOSUMtoPCWin> uosuMtoPCWinObservableList;
    private int empresa;
    private int zona;
    private int objeto;
    private int nivel;
    private int especialid;
    private int subesp;
    private int idObra;
    private Obra obra;
    private Empresaconstructora empresaconstructora;
    private String tipoCarga;
    private DataBaseAccessImport databaseEstructure = DataBaseAccessImport.getInstance();
    private DatabaseEstructure databaseEstructure1 = DatabaseEstructure.getInstance();
    private String path;
    private String renglonesPath;
    private ArrayList<UnidadObraPCW> unidadObraPCWArrayList;
    private ArrayList<RenglonVariantePCW> renglonVarianteViewArrayList;
    private List<SumBajoEspPCW> sumBajoEspPCWArrayList;
    private List<PlanPCW> planPCWList;
    private List<CertificacionPCW> certificacionPCWList;
    private List<UnidadObraPCW> generalUnidadObraPCWList;
    private Double costoMaterial;
    private List<Brigadaconstruccion> brigadaconstruccionList;
    private List<Grupoconstruccion> grupoconstruccionList;
    private List<Cuadrillaconstruccion> cuadrillaconstruccionList;
    private PostreSqlOperation postreSqlOperation = PostreSqlOperation.getInstance();
    private Map<UnidadObraPCW, List<RenglonVariantePCW>> unidadRenglonesMap;
    private Map<UnidadObraPCW, List<SumBajoEspPCW>> sUnidadObraPCWListMap;
    private Map<UnidadObraPCW, List<CertificacionPCW>> certificacionListUnidadObra;
    private Map<UnidadObraPCW, List<PlanPCW>> palnListUnidadObra;
    private ArrayList<CalPrecSuminist> componentesArrayList;
    private ArrayList<SuminstrosPCW> suminstrosPCWArrayList;
    private List<Bajoespecificacion> bajoespecificacionList;
    private ArrayList<Unidadobra> unidadobraArrayListFull;
    private ArrayList<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private List<Bajoespecificacion> tem;
    private CalPrecSuminist sumc;

    Task tarea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableUnidad.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String[] partEmp = selectEmpresa.getValue().split(" - ");
                String[] partZon = selectZonas.getValue().split(" - ");
                String[] partObjetos = selectObjetos.getValue().split(" - ");
                String[] partNivel = selectNivel.getValue().split(" - ");
                String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
                String[] partSub = selectSubespecialidad.getValue().split(" - ");
                String uo = newSelection.getCodeBox().getText();
                uorVtoPCWinObservableList = FXCollections.observableArrayList();
                uorVtoPCWinObservableList.addAll(renglonVarianteViewArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0]) && unidadObraPCW.getEspecialidad().contentEquals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().contentEquals(partSub[0]) && unidadObraPCW.getCodeUO().contentEquals(uo)).map(renglonVariantePCW -> {
                    UORVtoPCWin ren = new UORVtoPCWin(renglonVariantePCW.getCodeRV(), renglonVariantePCW.getCantidad(), renglonVariantePCW.getCostMaterial() + renglonVariantePCW.getCostMano() + renglonVariantePCW.getCostEquipo());
                    return ren;
                }).collect(Collectors.toList()));
                loadDataRV(uorVtoPCWinObservableList);

                uosuMtoPCWinObservableList = FXCollections.observableArrayList();
                uosuMtoPCWinObservableList.addAll(sumBajoEspPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().trim().equals(partEmp[0]) && unidadObraPCW.getZona().trim().equals(partZon[0]) & unidadObraPCW.getObjeto().trim().equals(partObjetos[0]) && unidadObraPCW.getNivel().trim().equals(partNivel[0]) && unidadObraPCW.getEspecialidad().trim().equals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().trim().equals(partSub[0]) && unidadObraPCW.getCodeUO().trim().equals(tableUnidad.getSelectionModel().getSelectedItem().getCodeBox().getText().trim())).map(renglonVariantePCW -> {
                    UOSUMtoPCWin ren = new UOSUMtoPCWin(renglonVariantePCW.getSuminis(), renglonVariantePCW.getCantidad(), renglonVariantePCW.getCosto());
                    return ren;
                }).collect(Collectors.toList()));
                loadDataSuministro(uosuMtoPCWinObservableList);
            }
        });

    }

    private void loadDataRV(ObservableList<UORVtoPCWin> uorVtoPCWinObservableList) {
        rvCode.setCellValueFactory(new PropertyValueFactory<UORVtoPCWin, StringProperty>("code"));
        rvCant.setCellValueFactory(new PropertyValueFactory<UORVtoPCWin, DoubleProperty>("cant"));
        rvCosto.setCellValueFactory(new PropertyValueFactory<UORVtoPCWin, DoubleProperty>("costo"));
        tableRV.getItems().setAll(uorVtoPCWinObservableList);

    }

    private void loadDataSuministro(ObservableList<UOSUMtoPCWin> uosuMtoPCWins) {
        sumCode.setCellValueFactory(new PropertyValueFactory<UOSUMtoPCWin, StringProperty>("code"));
        sumCant.setCellValueFactory(new PropertyValueFactory<UOSUMtoPCWin, DoubleProperty>("cant"));
        sumCosto.setCellValueFactory(new PropertyValueFactory<UOSUMtoPCWin, DoubleProperty>("costo"));
        tableSuminist.getItems().setAll(uosuMtoPCWins);

    }

    public void cargarDatosSimple(UnidadesObraObraController unidadesObra, Obra obraImp, Empresaconstructora emp, Zonas zonas, Objetos objetos, Nivel niveles, Especialidades especialidades, Subespecialidades subespecialidades) {
        idObra = obraImp.getId();
        empresa = emp.getId();
        zona = zonas.getId();
        objeto = objetos.getId();
        nivel = niveles.getId();
        especialid = especialidades.getId();
        subesp = subespecialidades.getId();
        unidadesObraObraController = unidadesObra;
        obra = obraImp;
        empresaconstructora = emp;

        tipoCarga = "Simple";

        if (databaseEstructure.pathdb != null) {
            pathMDB.setText(databaseEstructure.pathdb);
            selectEmpresa.setItems(databaseEstructure.getEmpresasList());
        }

    }

    public void cargarDatosEspecialidad(UnidadesObraObraController unidadesObra, Obra obraImp, Empresaconstructora emp, Zonas zonas, Objetos objetos, Nivel niveles, Especialidades especialidades, Subespecialidades subespecialidades) {
        idObra = obraImp.getId();
        empresa = emp.getId();
        zona = zonas.getId();
        objeto = objetos.getId();
        nivel = niveles.getId();
        if (especialidades == null && subespecialidades == null) {
            especialid = 0;
            subesp = 0;
        }
        unidadesObraObraController = unidadesObra;
        obra = obraImp;
        empresaconstructora = emp;

        selectEspecialidad.setDisable(true);
        selectSubespecialidad.setDisable(true);

        tipoCarga = "Moderada";

        if (databaseEstructure.pathdb != null) {
            pathMDB.setText(databaseEstructure.pathdb);
            selectEmpresa.setItems(databaseEstructure.getEmpresasList());
        }

    }

    public void cargarDatosNivelObjetos(UnidadesObraObraController unidadesObra, Obra obraImp, Empresaconstructora emp, Zonas zonas, Objetos objetos, Nivel niveles, Especialidades especialidades, Subespecialidades subespecialidades) {
        idObra = obraImp.getId();
        empresa = emp.getId();
        zona = zonas.getId();
        objeto = objetos.getId();
        if (especialidades == null && subespecialidades == null) {
            especialid = 0;
            subesp = 0;
        }
        unidadesObraObraController = unidadesObra;
        obra = obraImp;
        empresaconstructora = emp;

        selectNivel.setDisable(true);
        selectEspecialidad.setDisable(true);
        selectSubespecialidad.setDisable(true);

        tipoCarga = "Full";

        if (databaseEstructure.pathdb != null) {
            pathMDB.setText(databaseEstructure.pathdb);
            selectEmpresa.setItems(databaseEstructure.getEmpresasList());
        }

    }

    public void handleCargarMDB(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo .mdb");
        File file = fileChooser.showOpenDialog(null);

        path = null;
        renglonesPath = null;
        if (file != null && file.getAbsolutePath().endsWith(".mdb")) {
            pathMDB.setText(file.getAbsolutePath());

            path = "//" + file.getAbsolutePath().replace("\\", "//");
            renglonesPath = path.replace(path.substring(path.length() - 12), "Renglones.mdb");

            databaseEstructure.pathdb = path;
            databaseEstructure.pathdbRV = renglonesPath;
            databaseEstructure1.pathdbRV = renglonesPath;

            selectEmpresa.setItems(databaseEstructure.getEmpresasList());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Información");
            alert.setContentText("Fichero incorrecto, cargue un fichero .mdb");
            alert.showAndWait();
        }
    }

    public void handleActionCargarZona(ActionEvent event) {
        selectZonas.getItems().clear();
        selectZonas.setItems(databaseEstructure.getZonasList());
    }

    public void handleCargarObjetos(ActionEvent event) {
        String[] partZon = selectZonas.getValue().split(" - ");
        selectObjetos.getItems().clear();
        selectObjetos.setItems(databaseEstructure.getObjetosList(partZon[0]));

    }

    public void handleCargarNivel(ActionEvent event) {
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        selectNivel.getItems().clear();
        selectNivel.setItems(databaseEstructure.getNivelList(partZon[0], partObjetos[0]));

        String[] partEmp = selectEmpresa.getValue().split(" - ");

        unidadObraPCWArrayList = new ArrayList<>();
        unidadObraPCWArrayList = databaseEstructure.getUnidadObraPCWArrayList();

        generalUnidadObraPCWList = new ArrayList<>();
        generalUnidadObraPCWList = unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().trim().contentEquals(partEmp[0]) && unidadObraPCW.getZona().trim().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().trim().contentEquals(partObjetos[0])).collect(Collectors.toList());

        generalUnidadObraPCWList.size();

        renglonVarianteViewArrayList = new ArrayList<>();
        renglonVarianteViewArrayList = databaseEstructure.getRenglonVariantePCWArrayList();

        sumBajoEspPCWArrayList = new ArrayList<>();
        sumBajoEspPCWArrayList = databaseEstructure.getSumBajoEspPCWArrayList();

        certificacionPCWList = new ArrayList<>();
        certificacionPCWList = databaseEstructure.getCertificacionPCWList();

        planPCWList = new ArrayList<>();
        planPCWList = databaseEstructure.getPlanPCWList();

        brigadaconstruccionList = new ArrayList<>();
        brigadaconstruccionList = getBrigadaconstruccionList();

        grupoconstruccionList = new ArrayList<>();
        grupoconstruccionList = getGrupoconstruccionList();

        cuadrillaconstruccionList = new ArrayList<>();
        cuadrillaconstruccionList = getCuadrillaconstruccionList();

    }

    public void handleCargarEspecialidad(ActionEvent event) {
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNivel = selectNivel.getValue().split(" - ");
        selectEspecialidad.getItems().clear();
        selectEspecialidad.setItems(databaseEstructure.getEspecialidadesList(partZon[0], partObjetos[0], partNivel[0]));


        String[] partEmp = selectEmpresa.getValue().split(" - ");

        unidadObraPCWArrayList = new ArrayList<>();
        unidadObraPCWArrayList = databaseEstructure.getUnidadObraPCWArrayList();

        generalUnidadObraPCWList = new ArrayList<>();
        generalUnidadObraPCWList = unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0])).collect(Collectors.toList());

        generalUnidadObraPCWList.size();


        renglonVarianteViewArrayList = new ArrayList<>();
        renglonVarianteViewArrayList = databaseEstructure.getRenglonVariantePCWArrayList();

        sumBajoEspPCWArrayList = new ArrayList<>();
        sumBajoEspPCWArrayList = databaseEstructure.getSumBajoEspPCWArrayList();

        certificacionPCWList = new ArrayList<>();
        certificacionPCWList = databaseEstructure.getCertificacionPCWList();

        planPCWList = new ArrayList<>();
        planPCWList = databaseEstructure.getPlanPCWList();

        brigadaconstruccionList = new ArrayList<>();
        brigadaconstruccionList = getBrigadaconstruccionList();

        grupoconstruccionList = new ArrayList<>();
        grupoconstruccionList = getGrupoconstruccionList();

        cuadrillaconstruccionList = new ArrayList<>();
        cuadrillaconstruccionList = getCuadrillaconstruccionList();

    }

    public void handleCargarSubespecialidad(ActionEvent event) {
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNivel = selectNivel.getValue().split(" - ");
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        selectSubespecialidad.getItems().clear();
        selectSubespecialidad.setItems(databaseEstructure.getSubEspecialidadesList(partZon[0], partObjetos[0], partNivel[0], partEspecialidad[0]));
    }

    public void handleCargarUnidadesObra(ActionEvent event) {
        String[] partEmp = selectEmpresa.getValue().split(" - ");
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNivel = selectNivel.getValue().split(" - ");
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        String[] partSub = selectSubespecialidad.getValue().split(" - ");

        unidadObraPCWArrayList = new ArrayList<>();
        unidadObraPCWArrayList = databaseEstructure.getUnidadObraPCWArrayList();

        uopcwinViewObservableList = FXCollections.observableArrayList();
        uopcwinViewObservableList = getDatosTableUo(unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0]) && unidadObraPCW.getEspecialidad().contentEquals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().contentEquals(partSub[0])).collect(Collectors.toList()));


        uoCode.setCellValueFactory(new PropertyValueFactory<UOPCWINView, JFXCheckBox>("codeBox"));
        uoCant.setCellValueFactory(new PropertyValueFactory<UOPCWINView, DoubleProperty>("cant"));
        uoCosto.setCellValueFactory(new PropertyValueFactory<UOPCWINView, DoubleProperty>("costo"));

        tableUnidad.getItems().setAll(uopcwinViewObservableList);

        renglonVarianteViewArrayList = new ArrayList<>();
        renglonVarianteViewArrayList = databaseEstructure.getRenglonVariantePCWArrayList();

        sumBajoEspPCWArrayList = new ArrayList<>();
        sumBajoEspPCWArrayList = databaseEstructure.getSumBajoEspPCWArrayList();

    }

    private ObservableList<UOPCWINView> getDatosTableUo(List<UnidadObraPCW> unidadObraPCWArrayList) {
        ObservableList<UOPCWINView> uopcwinViews = FXCollections.observableArrayList();
        uopcwinViews.addAll(unidadObraPCWArrayList.parallelStream().map(unidadObraPCW -> {
            UOPCWINView item = new UOPCWINView(new JFXCheckBox(unidadObraPCW.getCode()), unidadObraPCW.getCantidad(), unidadObraPCW.getCostMaterial() + unidadObraPCW.getCostMano() + unidadObraPCW.getCostEquipo());
            return item;
        }).collect(Collectors.toList()));

        return uopcwinViews;
    }

    public Double getUOCostoMaterial(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            costoMaterial = 0.0;
            costoMaterial = (Double) session.createQuery("SELECT SUM(costo) FROM Bajoespecificacion WHERE unidadobraId =: unId").setParameter("unId", idUO).getSingleResult();
            if (costoMaterial == null) {
                costoMaterial = 0.0;
            }

            tx.commit();
            session.close();
            return costoMaterial;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return costoMaterial;
    }

    private List<Brigadaconstruccion> getBrigadaconstruccionList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Brigadaconstruccion> brigadaList = new ArrayList<>();
            brigadaList = session.createQuery(" FROM Brigadaconstruccion ").getResultList();

            tx.commit();
            session.close();
            return brigadaList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Grupoconstruccion> getGrupoconstruccionList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Grupoconstruccion> listGrup = new ArrayList<>();
            listGrup = session.createQuery(" FROM Grupoconstruccion ").getResultList();

            tx.commit();
            session.close();
            return listGrup;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Cuadrillaconstruccion> getCuadrillaconstruccionList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Cuadrillaconstruccion> listCuad = new ArrayList<>();
            listCuad = session.createQuery(" FROM Cuadrillaconstruccion ").getResultList();

            tx.commit();
            session.close();
            return listCuad;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private void updateUnidadObra(Unidadobra unidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Unidadobra unidadobra = session.get(Unidadobra.class, unidad.getId());
            unidadobra.getUnidadobrarenglonsById().size();

            double costtMaterialInRV = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
            double costMano = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
            double costEquip = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
            double salario = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);
            double salariocuc = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariocuc).reduce(0.0, Double::sum);
            double material = getUOCostoMaterial(unidadobra.getId());

            double total = costtMaterialInRV + material + costMano + costEquip;
            unidadobra.setCostototal(total);
            unidadobra.setCostoMaterial(material + costtMaterialInRV);
            unidadobra.setCostomano(costMano);
            unidadobra.setCostoequipo(costEquip);
            unidadobra.setCostounitario(total / unidadobra.getCantidad());
            unidadobra.setSalario(salario);
            unidadobra.setSalariocuc(salariocuc);
            session.update(unidadobra);

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    private UnidadObraPCW getUnidadObraPCW(String code) {
        String[] partEmp = selectEmpresa.getValue().split(" - ");
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNivel = selectNivel.getValue().split(" - ");
        String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
        String[] partSub = selectSubespecialidad.getValue().split(" - ");
        return unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().trim().equals(partEmp[0].trim()) && unidadObraPCW.getZona().trim().equals(partZon[0].trim()) && unidadObraPCW.getObjeto().trim().equals(partObjetos[0].trim()) && unidadObraPCW.getNivel().trim().equals(partNivel[0].trim()) && unidadObraPCW.getEspecialidad().trim().equals(partEspecialidad[0].trim()) && unidadObraPCW.getSubEspecialidad().trim().equals(partSub[0].trim()) && unidadObraPCW.getCode().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private Unidadobra getUnidadObra(String code) {
        return unidadobraArrayListFull.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idObra && unidadobra.getEmpresaconstructoraId() == empresa && unidadobra.getZonasId() == zona && unidadobra.getObjetosId() == objeto && unidadobra.getNivelId() == nivel && unidadobra.getEspecialidadesId() == especialid && unidadobra.getSubespecialidadesId() == subesp && unidadobra.getCodigo().trim().contentEquals(code.trim())).findFirst().orElse(null);
    }

    private Unidadobra getUnidadObraPCW(UnidadObraPCW unid) {
        return unidadobraArrayListFull.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idObra && unidadobra.getEmpresaconstructoraId() == empresa && unidadobra.getZonasId() == zona && unidadobra.getObjetosId() == objeto && unidadobra.getNivelId() == nivel && unidadobra.getEspecialidadesByEspecialidadesId().getCodigo().trim().equals(unid.getEspecialidad().trim()) && unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo().trim().endsWith(unid.getSubEspecialidad().trim().substring(1)) && unidadobra.getCodigo().trim().contentEquals(unid.getCode().trim())).findFirst().orElse(null);
    }

    private Unidadobra getUnidadObraPCWObj(UnidadObraPCW unid) {
        return unidadobraArrayListFull.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idObra && unidadobra.getEmpresaconstructoraId() == empresa && unidadobra.getZonasId() == zona && unidadobra.getObjetosId() == objeto && unidadobra.getNivelByNivelId().getCodigo().trim().equals(unid.getNivel().trim()) && unidadobra.getEspecialidadesByEspecialidadesId().getCodigo().trim().equals(unid.getEspecialidad().trim()) && unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo().trim().endsWith(unid.getSubEspecialidad().trim().substring(1)) && unidadobra.getCodigo().trim().equals(unid.getCode().trim())).findFirst().orElse(null);
    }

    private List<RenglonVariantePCW> getRenglones(UnidadObraPCW unidadObra) {
        return renglonVarianteViewArrayList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().trim().contentEquals(unidadObra.getEmpresa().trim()) && renglonVariantePCW.getZona().trim().contentEquals(unidadObra.getZona().trim()) && renglonVariantePCW.getObjeto().trim().contentEquals(unidadObra.getObjeto().trim()) && renglonVariantePCW.getNivel().trim().contentEquals(unidadObra.getNivel().trim()) && renglonVariantePCW.getEspecialidad().trim().contentEquals(unidadObra.getEspecialidad().trim()) && renglonVariantePCW.getSubEspecialidad().trim().contentEquals(unidadObra.getSubEspecialidad().trim()) && renglonVariantePCW.getCodeUO().trim().contentEquals(unidadObra.getCode().trim())).collect(Collectors.toList());
    }

    private List<SumBajoEspPCW> getSumBajoEspPCWList(UnidadObraPCW unidadObra) {
        return sumBajoEspPCWArrayList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().trim().contentEquals(unidadObra.getEmpresa().trim()) && renglonVariantePCW.getZona().trim().contentEquals(unidadObra.getZona().trim()) && renglonVariantePCW.getObjeto().trim().contentEquals(unidadObra.getObjeto().trim()) && renglonVariantePCW.getNivel().trim().contentEquals(unidadObra.getNivel().trim()) && renglonVariantePCW.getEspecialidad().trim().contentEquals(unidadObra.getEspecialidad().trim()) && renglonVariantePCW.getSubEspecialidad().trim().contentEquals(unidadObra.getSubEspecialidad().trim()) && renglonVariantePCW.getCodeUO().trim().contentEquals(unidadObra.getCode().trim())).collect(Collectors.toList());
    }


    private List<PlanPCW> getPlanesUo(UnidadObraPCW unidadObra) {
        return planPCWList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().contentEquals(unidadObra.getEmpresa()) && renglonVariantePCW.getZona().contentEquals(unidadObra.getZona()) && renglonVariantePCW.getObjeto().contentEquals(unidadObra.getObjeto()) && renglonVariantePCW.getNivel().contentEquals(unidadObra.getNivel()) && renglonVariantePCW.getEspecialidad().contentEquals(unidadObra.getEspecialidad()) && renglonVariantePCW.getSubEspecialidad().contentEquals(unidadObra.getSubEspecialidad()) && renglonVariantePCW.getCodeUO().contentEquals(unidadObra.getCode())).collect(Collectors.toList());
    }

    private List<CertificacionPCW> getCertificacionUo(UnidadObraPCW unidadObra) {
        return certificacionPCWList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().contentEquals(unidadObra.getEmpresa()) && renglonVariantePCW.getZona().contentEquals(unidadObra.getZona()) && renglonVariantePCW.getObjeto().contentEquals(unidadObra.getObjeto()) && renglonVariantePCW.getNivel().contentEquals(unidadObra.getNivel()) && renglonVariantePCW.getEspecialidad().contentEquals(unidadObra.getEspecialidad()) && renglonVariantePCW.getSubEspecialidad().contentEquals(unidadObra.getSubEspecialidad()) && renglonVariantePCW.getCodeUO().contentEquals(unidadObra.getCode())).collect(Collectors.toList());
    }


    @FXML
    private Label status;
    private ProgressDialog stage;

    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < 100; i++) {
                    Thread.sleep(100 / 2);
                    updateProgress(i + 1, 100);
                }
                return true;
            }
        };
    }

    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);
    }


    public void handleAddUnidadObra(ActionEvent event) {
        if (tipoCarga.equals("Moderada")) {

            unidadRenglonesMap = new HashMap<>();
            sUnidadObraPCWListMap = new HashMap<>();
            palnListUnidadObra = new HashMap<>();
            certificacionListUnidadObra = new HashMap<>();
            ArrayList<Unidadobra> unidadobraArrayList = new ArrayList<>();
            List<Especialidades> especialidadesList = new ArrayList<>();
            especialidadesList = postreSqlOperation.getEspecialidades();
            especialidadesList.size();
            List<Subespecialidades> subespecialidadesList = new ArrayList<>();
            subespecialidadesList = postreSqlOperation.getSubespecialidades();
            subespecialidadesList.size();
            unidadobraArrayList = postreSqlOperation.getUnidadobraListImporGeneral(idObra, empresa, zona, objeto, nivel, generalUnidadObraPCWList);
            postreSqlOperation.createUnidadesObra(unidadobraArrayList);

            tarea = createTime(unidadobraArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Unidades de Obra");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            unidadobraArrayListFull = new ArrayList<>();
            unidadobraArrayListFull = postreSqlOperation.getUnidadobraArrayList();

            for (UnidadObraPCW unid : generalUnidadObraPCWList) {
                unidadRenglonesMap.put(unid, getRenglones(unid));
                List<SumBajoEspPCW> listaSum = new ArrayList<>();
                listaSum = getSumBajoEspPCWList(unid);
                if (listaSum != null) {
                    sUnidadObraPCWListMap.put(unid, listaSum);
                }

                List<PlanPCW> planPCWS = new ArrayList<>();
                planPCWS = getPlanesUo(unid);
                if (planPCWS != null) {
                    palnListUnidadObra.put(unid, planPCWS);
                }

                List<CertificacionPCW> certificacionPCWS = new ArrayList<>();
                certificacionPCWS = getCertificacionUo(unid);
                if (certificacionPCWList != null) {
                    certificacionListUnidadObra.put(unid, certificacionPCWS);
                }

            }
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                items.getValue().forEach(it -> {
                    Renglonvariante r = rvToSugestion(it.getCodeRV(), obra.getSalarioId());
                    if (r == null) {
                        postreSqlOperation.createRnglonVarianteFromPCWin(it.getCodeRV(), obra.getSalarioId());
                    }
                });

            }

            utilCalcSingelton.getAllRenglones();

            ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                unidadobrarenglonArrayList.addAll(postreSqlOperation.getUnidadobrarenglonToImportArrayList(getUnidadObraPCW(items.getKey()), items.getValue()));
            }

            postreSqlOperation.createUnidadesObraRenglones(unidadobrarenglonArrayList);

            tarea = createTime(unidadobrarenglonArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Calculando Renglones Variantes");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            if (flagSuministros.isSelected() == false) {

                suminstrosPCWArrayList = new ArrayList<>();
                suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();

                List<SumBajoEspPCW> noSuministPresentList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        for (SumBajoEspPCW b : items.getValue()) {
                            CalPrecSuminist calPrecSuminist = getIsPresentInCalPreC(b);
                            if (calPrecSuminist == null) {
                                noSuministPresentList.add(b);
                            }
                        }
                    }
                }

                createCalPrecSuministros(noSuministPresentList.stream().distinct().collect(Collectors.toList()));

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();
                bajoespecificacionList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        bajoespecificacionList.addAll(createNajoEspecificacion(getUnidadObraPCW(items.getKey()), items.getValue()));
                    }
                }

                postreSqlOperation.createBajoespecificacion(bajoespecificacionList);

                tarea = createTime(bajoespecificacionList.size());
                stage = new ProgressDialog(tarea);
                stage.setContentText("Calculando Suministros");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }

            List<Certificacion> fullCertificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<CertificacionPCW>> items : certificacionListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullCertificacionList.addAll(createCertificacion(getUnidadObraPCW(items.getKey()), items.getValue()));
                }
            }

            for (Certificacion certificacion : fullCertificacionList) {
                postreSqlOperation.createCertificacion(certificacion);
            }

            tarea = createTime(fullCertificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Calculando Certificaciones");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            List<Planificacion> fullPlanificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<PlanPCW>> items : palnListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullPlanificacionList.addAll(createPlanificacion(getUnidadObraPCW(items.getKey()), items.getValue()));
                }
            }

            for (Planificacion planificacion : fullPlanificacionList) {
                postreSqlOperation.createPlanificacion(planificacion);
            }

            tarea = createTime(fullPlanificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Calculando Planificación");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            finalCheckUnidadByNivel(generalUnidadObraPCWList);
            Thread thread = new CalForUnidadObra(unidadesObraObraController, empresaconstructora, obra);
            thread.start();

        } else if (tipoCarga.equals("Simple")) {
            toCreateList = new ArrayList<>();
            unidadRenglonesMap = new HashMap<>();
            sUnidadObraPCWListMap = new HashMap<>();
            palnListUnidadObra = new HashMap<>();
            certificacionListUnidadObra = new HashMap<>();
            for (UOPCWINView items : tableUnidad.getItems()) {
                if (items.getCodeBox().isSelected() == true) {
                    toCreateList.add(getUnidadObraPCW(items.getCodeBox().getText()));
                }
            }
            ArrayList<Unidadobra> unidadobraArrayList = new ArrayList<>();
            unidadobraArrayList = postreSqlOperation.getUnidadobraListImpor(idObra, empresa, zona, objeto, nivel, especialid, subesp, toCreateList);
            postreSqlOperation.createUnidadesObra(unidadobraArrayList);

            tarea = createTime(unidadobraArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Unidades de Obra");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            unidadobraArrayListFull = new ArrayList<>();
            unidadobraArrayListFull = postreSqlOperation.getUnidadobraArrayList();


            for (UnidadObraPCW unid : toCreateList) {
                unidadRenglonesMap.put(unid, getRenglones(unid));
                List<SumBajoEspPCW> listaSum = new ArrayList<>();
                listaSum = getSumBajoEspPCWList(unid);
                if (listaSum != null) {
                    sUnidadObraPCWListMap.put(unid, listaSum);
                }

                List<PlanPCW> planPCWS = new ArrayList<>();
                planPCWS = getPlanesUo(unid);
                if (planPCWS != null) {
                    palnListUnidadObra.put(unid, planPCWS);
                }

                List<CertificacionPCW> certificacionPCWS = new ArrayList<>();
                certificacionPCWS = getCertificacionUo(unid);
                if (certificacionPCWList != null) {
                    certificacionListUnidadObra.put(unid, certificacionPCWS);
                }

            }
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                items.getValue().forEach(it -> {
                    Renglonvariante r = rvToSugestion(it.getCodeRV(), obra.getSalarioId());
                    if (r == null) {
                        postreSqlOperation.createRnglonVarianteFromPCWin(it.getCodeRV(), obra.getSalarioId());
                    }
                });

            }
            utilCalcSingelton.getAllRenglones();

            ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                unidadobrarenglonArrayList.addAll(postreSqlOperation.getUnidadobrarenglonToImportArrayList(getUnidadObra(items.getKey().getCode()), items.getValue()));
            }

            postreSqlOperation.createUnidadesObraRenglones(unidadobrarenglonArrayList);

            tarea = createTime(unidadobrarenglonArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Calculando Renglones Variantes");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            if (flagSuministros.isSelected() == false) {

                suminstrosPCWArrayList = new ArrayList<>();
                suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();

                System.out.println(componentesArrayList.size());

                List<SumBajoEspPCW> noSuministPresentList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        for (SumBajoEspPCW b : items.getValue()) {
                            CalPrecSuminist calPrecSuminist = getIsPresentInCalPreC(b);
                            if (calPrecSuminist == null) {
                                noSuministPresentList.add(b);
                            }
                        }
                    }
                }

                createCalPrecSuministros(noSuministPresentList.stream().distinct().collect(Collectors.toList()));

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();
                bajoespecificacionList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        bajoespecificacionList.addAll(createNajoEspecificacion(getUnidadObra(items.getKey().getCode()), items.getValue()));
                    }
                }

                postreSqlOperation.createBajoespecificacion(bajoespecificacionList);

                tarea = createTime(bajoespecificacionList.size());
                stage = new ProgressDialog(tarea);
                stage.setContentText("Calculando Suministros");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }


            List<Certificacion> fullCertificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<CertificacionPCW>> items : certificacionListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullCertificacionList.addAll(createCertificacion(getUnidadObra(items.getKey().getCode()), items.getValue()));
                }
            }

            for (Certificacion certificacion : fullCertificacionList) {
                postreSqlOperation.createCertificacion(certificacion);
            }

            tarea = createTime(fullCertificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Certificaciones");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            List<Planificacion> fullPlanificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<PlanPCW>> items : palnListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullPlanificacionList.addAll(createPlanificacion(getUnidadObra(items.getKey().getCode()), items.getValue()));
                }
            }

            for (Planificacion planificacion : fullPlanificacionList) {
                postreSqlOperation.createPlanificacion(planificacion);
            }

            tarea = createTime(fullCertificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Planificaciones");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            finalCheckUnidad(toCreateList);
            unidadesObraObraController.handleCargardatos(event);
            Thread thread = new CalForUnidadObra(unidadesObraObraController, empresaconstructora, obra);
            thread.start();

        } else if (tipoCarga.equals("Full")) {
            unidadRenglonesMap = new HashMap<>();
            sUnidadObraPCWListMap = new HashMap<>();
            palnListUnidadObra = new HashMap<>();
            certificacionListUnidadObra = new HashMap<>();
            ArrayList<Unidadobra> unidadobraArrayList = new ArrayList<>();
            List<Especialidades> especialidadesList = new ArrayList<>();
            especialidadesList = postreSqlOperation.getEspecialidades();
            especialidadesList.size();
            List<Subespecialidades> subespecialidadesList = new ArrayList<>();
            subespecialidadesList = postreSqlOperation.getSubespecialidades();
            subespecialidadesList.size();

            List<Nivel> nivelList = new ArrayList<>();
            nivelList = postreSqlOperation.getNivelArrayList();

            unidadobraArrayList = postreSqlOperation.getUnidadobraListImporGeneralObjeto(idObra, empresa, zona, objeto, generalUnidadObraPCWList);
            unidadobraArrayList.size();

            postreSqlOperation.createUnidadesObra(unidadobraArrayList);

            tarea = createTime(unidadobraArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Unidades de Obra");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            unidadobraArrayListFull = new ArrayList<>();
            unidadobraArrayListFull = postreSqlOperation.getUnidadobraArrayList();
            unidadobraArrayListFull.size();


            for (UnidadObraPCW unid : generalUnidadObraPCWList) {
                unidadRenglonesMap.put(unid, getRenglones(unid));
                List<SumBajoEspPCW> listaSum = new ArrayList<>();
                listaSum = getSumBajoEspPCWList(unid);
                if (listaSum != null) {
                    sUnidadObraPCWListMap.put(unid, listaSum);
                }

                List<PlanPCW> planPCWS = new ArrayList<>();
                planPCWS = getPlanesUo(unid);
                if (planPCWS != null) {
                    palnListUnidadObra.put(unid, planPCWS);
                }

                List<CertificacionPCW> certificacionPCWS = new ArrayList<>();
                certificacionPCWS = getCertificacionUo(unid);
                if (certificacionPCWList != null) {
                    certificacionListUnidadObra.put(unid, certificacionPCWS);
                }

            }
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                items.getValue().forEach(it -> {
                    Renglonvariante r = rvToSugestion(it.getCodeRV(), obra.getSalarioId());
                    if (r == null) {
                        postreSqlOperation.createRnglonVarianteFromPCWin(it.getCodeRV(), obra.getSalarioId());
                    }
                });

            }

            utilCalcSingelton.getAllRenglones();


            ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
                unidadobrarenglonArrayList.addAll(postreSqlOperation.getUnidadobrarenglonToImportArrayList(getUnidadObraPCWObj(items.getKey()), items.getValue()));
            }
            unidadobrarenglonArrayList.size();

            postreSqlOperation.createUnidadesObraRenglones(unidadobrarenglonArrayList);

            tarea = createTime(unidadobrarenglonArrayList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Calculando Renglones Variantes");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            if (flagSuministros.isSelected() == false) {

                suminstrosPCWArrayList = new ArrayList<>();
                suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();
                System.out.println(componentesArrayList.size());

                List<SumBajoEspPCW> noSuministPresentList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        for (SumBajoEspPCW b : items.getValue()) {
                            CalPrecSuminist calPrecSuminist = getIsPresentInCalPreC(b);
                            if (calPrecSuminist == null) {
                                noSuministPresentList.add(b);
                            }
                        }
                    }
                }

                createCalPrecSuministros(noSuministPresentList.stream().distinct().collect(Collectors.toList()));

                componentesArrayList = new ArrayList<>();
                componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();
                bajoespecificacionList = new ArrayList<>();
                for (Map.Entry<UnidadObraPCW, List<SumBajoEspPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                    if (items.getValue().size() > 0) {
                        bajoespecificacionList.addAll(createNajoEspecificacion(getUnidadObraPCWObj(items.getKey()), items.getValue()));
                    }
                }

                postreSqlOperation.createBajoespecificacion(bajoespecificacionList);

                tarea = createTime(bajoespecificacionList.size());
                stage = new ProgressDialog(tarea);
                stage.setContentText("Calculando Suministros");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }

            List<Certificacion> fullCertificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<CertificacionPCW>> items : certificacionListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullCertificacionList.addAll(createCertificacion(getUnidadObraPCWObj(items.getKey()), items.getValue()));
                }
            }

            for (Certificacion certificacion : fullCertificacionList) {
                postreSqlOperation.createCertificacion(certificacion);
            }

            tarea = createTime(fullCertificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Certificaciones");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            List<Planificacion> fullPlanificacionList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<PlanPCW>> items : palnListUnidadObra.entrySet()) {
                if (items.getValue().size() > 0) {
                    fullPlanificacionList.addAll(createPlanificacion(getUnidadObraPCWObj(items.getKey()), items.getValue()));
                }
            }

            for (Planificacion planificacion : fullPlanificacionList) {
                postreSqlOperation.createPlanificacion(planificacion);
            }

            tarea = createTime(fullCertificacionList.size());
            stage = new ProgressDialog(tarea);
            stage.setContentText("Creando Planificaciones");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            finalCheckUnidadObj(generalUnidadObraPCWList);
            // unidadesObraObraController.handleCargardatos(event);
            Thread thread = new CalForUnidadObra(unidadesObraObraController, empresaconstructora, obra);
            thread.start();

        }
    }


    private void finalCheckUnidad(List<UnidadObraPCW> toCreateList) {
        for (UnidadObraPCW uo : toCreateList) {
            Unidadobra unidadobra = getUnidadObra(uo.getCode());
            updateUnidadObra(unidadobra);

        }

    }

    private void finalCheckUnidadByNivel(List<UnidadObraPCW> toCreateList) {
        for (UnidadObraPCW uo : toCreateList) {
            Unidadobra unidadobra = getUnidadObraPCW(uo);
            updateUnidadObra(unidadobra);

        }

    }

    private void finalCheckUnidadObj(List<UnidadObraPCW> toCreateList) {
        for (UnidadObraPCW uo : toCreateList) {
            Unidadobra unidadobra = getUnidadObraPCWObj(uo);
            updateUnidadObra(unidadobra);

        }

    }

    private CalPrecSuminist getIsPresentInCalPreC(SumBajoEspPCW bajoEspecificacionPCW) {
        return componentesArrayList.parallelStream().filter(calPrecSuminist -> calPrecSuminist.getCodig().trim().toUpperCase().equals(bajoEspecificacionPCW.getSuminis().trim().toUpperCase())).findFirst().orElse(null);

    }

    private void createCalPrecSuministros(List<SumBajoEspPCW> collect) {
        recursosArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        for (SumBajoEspPCW baj : collect) {
            SuminstrosPCW suminstrosPCW = geSuminstrosPCW(baj.getSuminis());
            if (suminstrosPCW.getTipo().trim().equals("1")) {
                Recursos sumuni = new Recursos();
                sumuni.setCodigo(suminstrosPCW.getCodeSuministro());
                sumuni.setDescripcion(suminstrosPCW.getDescrip());
                sumuni.setUm(suminstrosPCW.getUm());
                sumuni.setPeso(suminstrosPCW.getPeso());
                sumuni.setPreciomn(suminstrosPCW.getMn());
                sumuni.setPreciomlc(suminstrosPCW.getMlc());
                sumuni.setMermaprod(0.0);
                sumuni.setMermatrans(0.0);
                sumuni.setMermamanip(0.0);
                sumuni.setOtrasmermas(0.0);
                sumuni.setPertenece(String.valueOf(idObra));
                sumuni.setTipo("1");
                sumuni.setGrupoescala("I");
                sumuni.setCostomat(0.0);
                sumuni.setCostmano(0.0);
                sumuni.setCostequip(0.0);
                sumuni.setHa(0.0);
                sumuni.setHe(0.0);
                sumuni.setHo(0.0);
                sumuni.setCemento(0.0);
                sumuni.setArido(0.0);
                sumuni.setAsfalto(0.0);
                sumuni.setCarga(0.0);
                sumuni.setPrefab(0.0);
                sumuni.setCet(0.0);
                sumuni.setCpo(0.0);
                sumuni.setCpe(0.0);
                sumuni.setOtra(0.0);

                recursosArrayList.add(sumuni);
            } else if (suminstrosPCW.getTipo().trim().equals("S")) {

                Suministrossemielaborados suministrossemielaborados = new Suministrossemielaborados();
                suministrossemielaborados.setCodigo(suminstrosPCW.getCodeSuministro());
                suministrossemielaborados.setDescripcion(suminstrosPCW.getDescrip());
                suministrossemielaborados.setUm(suminstrosPCW.getUm());
                suministrossemielaborados.setPreciomn(suminstrosPCW.getMn());
                suministrossemielaborados.setPreciomlc(suminstrosPCW.getMlc());
                suministrossemielaborados.setPeso(suminstrosPCW.getPeso());
                suministrossemielaborados.setMermaprod(0.0);
                suministrossemielaborados.setMermamanip(0.0);
                suministrossemielaborados.setMermatrans(0.0);
                suministrossemielaborados.setOtrasmermas(0.0);
                suministrossemielaborados.setCarga(0.0);
                suministrossemielaborados.setCemento(0.0);
                suministrossemielaborados.setArido(0.0);
                suministrossemielaborados.setAsfalto(0.0);
                suministrossemielaborados.setCostequip(0.0);
                suministrossemielaborados.setCostmano(0.0);
                suministrossemielaborados.setCostomat(0.0);
                suministrossemielaborados.setHa(0.0);
                suministrossemielaborados.setHo(0.0);
                suministrossemielaborados.setHe(0.0);
                suministrossemielaborados.setPertenec(String.valueOf(idObra));
                suministrossemielaboradosArrayList.add(suministrossemielaborados);

            }

        }
        postreSqlOperation.createRecursos(recursosArrayList);
        postreSqlOperation.createSuministrosSemielaborados(suministrossemielaboradosArrayList);
        postreSqlOperation.createComposicionSemiPCW(suministrossemielaboradosArrayList);
    }

    public SuminstrosPCW geSuminstrosPCW(String code) {
        return suminstrosPCWArrayList.parallelStream().filter(suminstrosPCW -> suminstrosPCW.getCodeSuministro().equals(code)).findFirst().orElse(null);
    }

    private List<Bajoespecificacion> createNajoEspecificacion(Unidadobra key, List<SumBajoEspPCW> valueList) {
        tem = new ArrayList<>();
        for (SumBajoEspPCW ba : valueList) {
            sumc = getIsPresentInCalPreC(ba);
            if (sumc != null) {
                Bajoespecificacion bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(key.getId());
                bajoespecificacion.setIdSuministro(sumc.getId());
                bajoespecificacion.setRenglonvarianteId(0);
                bajoespecificacion.setCantidad(ba.getCantidad());
                bajoespecificacion.setCosto(ba.getCosto());
                bajoespecificacion.setTipo(sumc.getTipo());
                bajoespecificacion.setSumrenglon(0);
                tem.add(bajoespecificacion);
            } else {
                System.out.println(" ----- " + ba.getSuminis());
            }
        }

        return tem;
    }

    private Brigadaconstruccion getBrigadaConstruccion(int idEmpresa, String code) {
        return brigadaconstruccionList.parallelStream().filter(brigadaconstruccion -> brigadaconstruccion.getEmpresaconstructoraId() == idEmpresa && brigadaconstruccion.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private Grupoconstruccion getGrupoconstruccion(int idBrig, String codeg) {
        return grupoconstruccionList.parallelStream().filter(grupoconstruccion -> grupoconstruccion.getBrigadaconstruccionId() == idBrig && grupoconstruccion.getCodigo().trim().equals(codeg.trim())).findFirst().orElse(null);
    }

    private Cuadrillaconstruccion getCuadrillaContruccion(int Grup, String codeCuad) {
        return cuadrillaconstruccionList.parallelStream().filter(cuadrillaconstruccion -> cuadrillaconstruccion.getGrupoconstruccionId() == Grup && cuadrillaconstruccion.getCodigo().trim().equals(codeCuad.trim())).findFirst().orElse(null);

    }


    private List<Certificacion> createCertificacion(Unidadobra unid, List<CertificacionPCW> certificacionPCWS) {
        List<Certificacion> certificacionList = new ArrayList<>();
        for (CertificacionPCW certificacionPCW : certificacionPCWS) {
            int idBrig = getBrigadaConstruccion(unid.getEmpresaconstructoraId(), certificacionPCW.getBrigada()).getId();
            int idGru = getGrupoconstruccion(idBrig, certificacionPCW.getGrupo()).getId();
            int idCuad = getCuadrillaContruccion(idGru, certificacionPCW.getCuadrilla()).getId();

            Certificacion certificacion = new Certificacion();
            certificacion.setCostmaterial(certificacionPCW.getCostomaterial());
            certificacion.setCostmano(certificacionPCW.getCostomano());
            certificacion.setCostequipo(certificacionPCW.getCostoequipo());
            certificacion.setSalario(certificacionPCW.getSalario());
            certificacion.setCucsalario(certificacionPCW.getSalariocuc());
            certificacion.setBrigadaconstruccionId(idBrig);
            certificacion.setGrupoconstruccionId(idGru);
            certificacion.setCuadrillaconstruccionId(idCuad);
            certificacion.setUnidadobraId(unid.getId());
            certificacion.setDesde(Date.valueOf(certificacionPCW.getDesde()));
            certificacion.setHasta(Date.valueOf(certificacionPCW.getHasta()));
            certificacion.setCantidad(certificacionPCW.getCantidda());
            certificacionList.add(certificacion);
        }

        return certificacionList;
    }

    private List<Planificacion> createPlanificacion(Unidadobra unid, List<PlanPCW> certificacionPCWS) {
        List<Planificacion> planificacionList = new ArrayList<>();
        for (PlanPCW certificacionPCW : certificacionPCWS) {
            int idBrig = getBrigadaConstruccion(unid.getEmpresaconstructoraId(), certificacionPCW.getBrigada()).getId();
            int idGru = getGrupoconstruccion(idBrig, certificacionPCW.getGrupo()).getId();
            int idCuad = getCuadrillaContruccion(idGru, certificacionPCW.getCuadrilla()).getId();
            Planificacion planificacion = new Planificacion();
            planificacion.setCostomaterial(certificacionPCW.getCostomaterial());
            planificacion.setCostomano(certificacionPCW.getCostomano());
            planificacion.setCostoequipo(certificacionPCW.getCostoequipo());
            planificacion.setCostosalario(certificacionPCW.getSalario());
            planificacion.setCostsalariocuc(certificacionPCW.getSalariocuc());
            planificacion.setBrigadaconstruccionId(idBrig);
            planificacion.setGrupoconstruccionId(idGru);
            planificacion.setCuadrillaconstruccionId(idCuad);
            planificacion.setUnidadobraId(unid.getId());
            planificacion.setDesde(Date.valueOf(certificacionPCW.getDesde()));
            planificacion.setHasta(Date.valueOf(certificacionPCW.getHasta()));
            planificacion.setCantidad(certificacionPCW.getCantidda());
            planificacionList.add(planificacion);
        }

        return planificacionList;
    }


}
