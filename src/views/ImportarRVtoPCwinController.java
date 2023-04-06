package views;

import AccessMigration.PostreSqlOperation;
import AccessMigration.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ImportarRVtoPCwinController implements Initializable {

    RenglonVarianteObraController unidadesObraObraController;
    ArrayList<UnidadObraPCW> toCreateList;
    ArrayList<Renglonvariante> renglonvarianteArrayList;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
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
    /*
    @FXML
    private TableColumn<UOPCWINView, DoubleProperty> uoCant;
*/
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
    private String especifica;
    private DataBaseAccessImport databaseEstructure = DataBaseAccessImport.getInstance();
    private String path;
    private String renglonesPath;
    private ArrayList<UnidadObraPCW> unidadObraPCWArrayList;
    private ArrayList<RenglonVariantePCW> renglonVarianteViewArrayList;
    private ArrayList<SumBajoEspRVPCW> sumBajoEspPCWArrayList;
    private Double costoMaterial;
    private PostreSqlOperation postreSqlOperation = PostreSqlOperation.getInstance();
    private Map<UnidadObraPCW, List<RenglonVariantePCW>> unidadRenglonesMap;
    private Map<UnidadObraPCW, List<SumBajoEspRVPCW>> sUnidadObraPCWListMap;
    private ArrayList<CalPrecSuminist> componentesArrayList;
    private ArrayList<SuminstrosPCW> suminstrosPCWArrayList;
    private List<Bajoespecificacionrv> bajoespecificacionList;
    private ArrayList<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private List<Bajoespecificacionrv> tem;
    private CalPrecSuminist sumc;
    private List<Renglonnivelespecifico> renglonnivelespecificoList;

    public Obra getObra(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Obra ob = session.get(Obra.class, idUO);
            tx.commit();
            session.close();
            return ob;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obra;
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

    public void cargarDatos(RenglonVarianteObraController unidadesObra, int obraImp, Empresaconstructora Empresa, int idZona, int idObjetos, int idNivel, int idEspecialidad, int idSubespecialidad) {
        idObra = obraImp;
        empresa = Empresa.getId();
        zona = idZona;
        objeto = idObjetos;
        nivel = idNivel;
        especialid = idEspecialidad;
        subesp = idSubespecialidad;
        unidadesObraObraController = unidadesObra;
        obra = getObra(obraImp);
        empresaconstructora = Empresa;

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
    }

    public void handleCargarEspecialidad(ActionEvent event) {
        String[] partZon = selectZonas.getValue().split(" - ");
        String[] partObjetos = selectObjetos.getValue().split(" - ");
        String[] partNivel = selectNivel.getValue().split(" - ");
        selectEspecialidad.getItems().clear();
        selectEspecialidad.setItems(databaseEstructure.getEspecialidadesList(partZon[0], partObjetos[0], partNivel[0]));
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
        unidadObraPCWArrayList = databaseEstructure.getNivelesEspecificosPCWArrayList();

        uopcwinViewObservableList = FXCollections.observableArrayList();
        uopcwinViewObservableList = getDatosTableUo(unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0]) && unidadObraPCW.getEspecialidad().contentEquals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().contentEquals(partSub[0])).collect(Collectors.toList()));

        uoCode.setCellValueFactory(new PropertyValueFactory<UOPCWINView, JFXCheckBox>("codeBox"));
        //uoCant.setCellValueFactory(new PropertyValueFactory<UOPCWINView, DoubleProperty>("cant"));
        uoCosto.setCellValueFactory(new PropertyValueFactory<UOPCWINView, DoubleProperty>("costo"));

        tableUnidad.getItems().setAll(uopcwinViewObservableList);

        renglonVarianteViewArrayList = new ArrayList<>();
        renglonVarianteViewArrayList = databaseEstructure.getRenglonVarianteRVPCWArrayList();

        sumBajoEspPCWArrayList = new ArrayList<>();
        sumBajoEspPCWArrayList = databaseEstructure.getSumBajoEspPCWRVArrayList();

    }

    private ObservableList<UOPCWINView> getDatosTableUo(List<UnidadObraPCW> unidadObraPCWArrayList) {
        ObservableList<UOPCWINView> uopcwinViews = FXCollections.observableArrayList();
        uopcwinViews.addAll(unidadObraPCWArrayList.parallelStream().map(unidadObraPCW -> {
            UOPCWINView item = new UOPCWINView(new JFXCheckBox(unidadObraPCW.getCode() + " " + unidadObraPCW.getDescrip()), unidadObraPCW.getCantidad(), unidadObraPCW.getCostMaterial() + unidadObraPCW.getCostMano() + unidadObraPCW.getCostEquipo());
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
            costoMaterial = (Double) session.createQuery("SELECT SUM(costo) FROM Bajoespecificacionrv WHERE nivelespecificoId =: unId").setParameter("unId", idUO).getSingleResult();
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

    private void updateUnidadObra(Nivelespecifico unidad) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Nivelespecifico unidadobra = session.get(Nivelespecifico.class, unidad.getId());
            unidadobra.getRenglonnivelespecificosById().size();

            double costtMaterialInRV = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmaterial).reduce(0.0, Double::sum);
            double costMano = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmano).reduce(0.0, Double::sum);
            double costEquip = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostequipo).reduce(0.0, Double::sum);
            double salario = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getSalario).reduce(0.0, Double::sum);
            double material = getUOCostoMaterial(unidadobra.getId());

            unidadobra.setCostoequipo(costEquip);
            unidadobra.setCostomano(costMano);
            unidadobra.setCostomaterial(costtMaterialInRV + material);
            unidadobra.setSalario(salario);

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

        String[] codeEs = code.split(" ");
        return unidadObraPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) && unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0]) && unidadObraPCW.getEspecialidad().contentEquals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().contentEquals(partSub[0]) && unidadObraPCW.getCode().contentEquals(codeEs[0])).findFirst().orElse(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableUnidad.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                especifica = null;
                String[] partEmp = selectEmpresa.getValue().split(" - ");
                String[] partZon = selectZonas.getValue().split(" - ");
                String[] partObjetos = selectObjetos.getValue().split(" - ");
                String[] partNivel = selectNivel.getValue().split(" - ");
                String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
                String[] partSub = selectSubespecialidad.getValue().split(" - ");
                String[] uo = newSelection.getCodeBox().getText().split(" ");
                especifica = uo[0];
                uorVtoPCWinObservableList = FXCollections.observableArrayList();
                uorVtoPCWinObservableList.addAll(renglonVarianteViewArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().trim().equals(partEmp[0].trim()) && unidadObraPCW.getZona().trim().equals(partZon[0].trim()) & unidadObraPCW.getObjeto().trim().equals(partObjetos[0].trim()) && unidadObraPCW.getNivel().trim().equals(partNivel[0].trim()) && unidadObraPCW.getEspecialidad().trim().equals(partEspecialidad[0].trim()) && unidadObraPCW.getSubEspecialidad().trim().equals(partSub[0].trim()) && unidadObraPCW.getCodeUO().trim().equals(uo[0].trim())).map(renglonVariantePCW -> {
                    UORVtoPCWin ren = new UORVtoPCWin(renglonVariantePCW.getCodeRV(), renglonVariantePCW.getCantidad(), renglonVariantePCW.getCostMaterial() + renglonVariantePCW.getCostMano() + renglonVariantePCW.getCostEquipo());
                    return ren;
                }).collect(Collectors.toList()));
                loadDataRV(uorVtoPCWinObservableList);

            }


        });

        tableRV.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String[] partEmp = selectEmpresa.getValue().split(" - ");
                String[] partZon = selectZonas.getValue().split(" - ");
                String[] partObjetos = selectObjetos.getValue().split(" - ");
                String[] partNivel = selectNivel.getValue().split(" - ");
                String[] partEspecialidad = selectEspecialidad.getValue().split(" - ");
                String[] partSub = selectSubespecialidad.getValue().split(" - ");

                uosuMtoPCWinObservableList = FXCollections.observableArrayList();
                uosuMtoPCWinObservableList.addAll(sumBajoEspPCWArrayList.parallelStream().filter(unidadObraPCW -> unidadObraPCW.getEmpresa().contentEquals(partEmp[0]) && unidadObraPCW.getZona().contentEquals(partZon[0]) & unidadObraPCW.getObjeto().contentEquals(partObjetos[0]) && unidadObraPCW.getNivel().contentEquals(partNivel[0]) && unidadObraPCW.getEspecialidad().contentEquals(partEspecialidad[0]) && unidadObraPCW.getSubEspecialidad().contentEquals(partSub[0]) && unidadObraPCW.getCodeUO().contentEquals(especifica) && unidadObraPCW.getCodeRV().contentEquals(newSelection.getCode())).map(renglonVariantePCW -> {
                    UOSUMtoPCWin ren = new UOSUMtoPCWin(renglonVariantePCW.getSuminis(), renglonVariantePCW.getCantidad(), renglonVariantePCW.getCosto());
                    return ren;
                }).collect(Collectors.toList()));
                loadDataSuministro(uosuMtoPCWinObservableList);

            }
        });

    }

    private List<RenglonVariantePCW> getRenglones(UnidadObraPCW unidadObra) {
        return renglonVarianteViewArrayList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().contentEquals(unidadObra.getEmpresa()) && renglonVariantePCW.getZona().contentEquals(unidadObra.getZona()) && renglonVariantePCW.getObjeto().contentEquals(unidadObra.getObjeto()) && renglonVariantePCW.getNivel().contentEquals(unidadObra.getNivel()) && renglonVariantePCW.getEspecialidad().contentEquals(unidadObra.getEspecialidad()) && renglonVariantePCW.getSubEspecialidad().contentEquals(unidadObra.getSubEspecialidad()) && renglonVariantePCW.getCodeUO().contentEquals(unidadObra.getCode())).collect(Collectors.toList());
    }

    private Nivelespecifico getUnidadObra(String code) {
        return postreSqlOperation.getNivelespecificoArrayList().parallelStream().filter(unidadobra -> unidadobra.getObraId() == idObra && unidadobra.getEmpresaconstructoraId() == empresa && unidadobra.getZonasId() == zona && unidadobra.getObjetosId() == objeto && unidadobra.getNivelId() == nivel && unidadobra.getEspecialidadesId() == especialid && unidadobra.getSubespecialidadesId() == subesp && unidadobra.getCodigo().trim().contentEquals(code.trim())).findFirst().orElse(new Nivelespecifico());
    }

    private String normaRV(Obra obra) {
        String Tnorma = null;
        if (obra.getSalarioId() == 1) {
            Tnorma = "T30";
        } else if (obra.getSalarioId() == 2) {
            Tnorma = "T15";
        }
        return Tnorma;
    }

    private List<SumBajoEspRVPCW> getSumBajoEspPCWList(UnidadObraPCW unidadObra) {
        return sumBajoEspPCWArrayList.parallelStream().filter(renglonVariantePCW -> renglonVariantePCW.getEmpresa().trim().equals(unidadObra.getEmpresa().trim()) && renglonVariantePCW.getZona().equals(unidadObra.getZona().trim()) && renglonVariantePCW.getObjeto().equals(unidadObra.getObjeto().trim()) && renglonVariantePCW.getNivel().equals(unidadObra.getNivel().trim()) && renglonVariantePCW.getEspecialidad().trim().equals(unidadObra.getEspecialidad().trim()) && renglonVariantePCW.getSubEspecialidad().trim().equals(unidadObra.getSubEspecialidad().trim()) && renglonVariantePCW.getCodeUO().trim().contentEquals(unidadObra.getCode().trim())).collect(Collectors.toList());
    }

    public void handleAddUnidadObra(ActionEvent event) {
        toCreateList = new ArrayList<>();
        unidadRenglonesMap = new HashMap<>();
        sUnidadObraPCWListMap = new HashMap<>();
        for (UOPCWINView items : tableUnidad.getItems()) {
            if (items.getCodeBox().isSelected() == true) {
                toCreateList.add(getUnidadObraPCW(items.getCodeBox().getText()));
            }
        }

        ArrayList<Nivelespecifico> unidadobraArrayList = new ArrayList<>();
        unidadobraArrayList = postreSqlOperation.getNivelEspecificoListImpor(idObra, empresa, zona, objeto, nivel, especialid, subesp, toCreateList);
        postreSqlOperation.createNivelEspecifico(unidadobraArrayList);


        for (UnidadObraPCW unid : toCreateList) {
            unidadRenglonesMap.put(unid, getRenglones(unid));
            sUnidadObraPCWListMap.put(unid, getSumBajoEspPCWList(unid));
        }

        renglonvarianteArrayList = new ArrayList<>();
        renglonvarianteArrayList = postreSqlOperation.getRenglonvarianteArrayList(normaRV(obra));

        for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
            sheckRenglones(items.getValue());
        }

        ArrayList<Renglonnivelespecifico> unidadobrarenglonArrayList = new ArrayList<>();
        for (Map.Entry<UnidadObraPCW, List<RenglonVariantePCW>> items : unidadRenglonesMap.entrySet()) {
            unidadobrarenglonArrayList.addAll(postreSqlOperation.getNiUnidadobrarenglonList(getUnidadObra(items.getKey().getCode()), items.getValue()));
        }

        postreSqlOperation.createNivelEspecificoRenglones(unidadobrarenglonArrayList);

        if (flagSuministros.isSelected() == false) {
            suminstrosPCWArrayList = new ArrayList<>();
            suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();
            suminstrosPCWArrayList.size();

            componentesArrayList = new ArrayList<>();
            componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();

            List<SumBajoEspRVPCW> noSuministPresentList = new ArrayList<>();
            for (SumBajoEspRVPCW b : sumBajoEspPCWArrayList) {
                CalPrecSuminist calPrecSuminist = getIsPresentInCalPreC(b);
                if (calPrecSuminist == null) {
                    noSuministPresentList.add(b);
                }

            }

            createCalPrecSuministros(noSuministPresentList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));
            componentesArrayList = new ArrayList<>();
            componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();


            renglonnivelespecificoList = new ArrayList<>();
            renglonnivelespecificoList = postreSqlOperation.getRenglonnivelespecificoList();


            List<Bajoespecificacionrv> bajoespecificacionrvList = new ArrayList<>();
            for (Map.Entry<UnidadObraPCW, List<SumBajoEspRVPCW>> items : sUnidadObraPCWListMap.entrySet()) {
                bajoespecificacionrvList.addAll(createNajoEspecificacion(getUnidadObra(items.getKey().getCode()), items.getValue()));
            }

            postreSqlOperation.createBajoespecificacionRV(bajoespecificacionrvList);
        }


        finalCheckUnidad(toCreateList);
        unidadesObraObraController.handleCargardatos(event);

        Thread t1 = new CalForRenglonVariante(unidadesObraObraController, empresaconstructora, obra);
        t1.start();
    }

    private void finalCheckUnidad(ArrayList<UnidadObraPCW> toCreateList) {
        for (UnidadObraPCW uo : toCreateList) {
            Nivelespecifico unidadobra = getUnidadObra(uo.getCode());
            updateUnidadObra(unidadobra);
        }
    }

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);
    }

    private void sheckRenglones(List<RenglonVariantePCW> rnglonesPCWList) {
        for (RenglonVariantePCW rnglonesPCW : rnglonesPCWList) {
            Renglonvariante r = rvToSugestion(rnglonesPCW.getCodeRV(), obra.getSalarioId());
            if (r == null) {
                postreSqlOperation.createRnglonVarianteFromPCWin(rnglonesPCW.getCodeRV(), obra.getSalarioId());
            }
        }
    }

    private CalPrecSuminist getIsPresentInCalPreC(SumBajoEspRVPCW bajoEspecificacionPCW) {
        return componentesArrayList.parallelStream().filter(calPrecSuminist -> calPrecSuminist.getCodig().trim().equals(bajoEspecificacionPCW.getSuminis().trim())).findFirst().orElse(null);

    }

    private void createCalPrecSuministros(List<SumBajoEspRVPCW> collect) {
        recursosArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        for (SumBajoEspRVPCW baj : collect) {
            SuminstrosPCW suminstrosPCW = geSuminstrosPCW(baj.getSuminis());

            if (suminstrosPCW != null && suminstrosPCW.getTipo().trim().equals("1")) {
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
            } else if (suminstrosPCW != null && suminstrosPCW.getTipo().trim().equals("S")) {
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
        return suminstrosPCWArrayList.parallelStream().filter(suminstrosPCW -> suminstrosPCW.getCodeSuministro().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private int idRenglon(int idN, String code) {
        return renglonnivelespecificoList.parallelStream().filter(renglonnivelespecifico -> renglonnivelespecifico.getNivelespecificoId() == idN && renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo().trim().equals(code.trim())).findFirst().map(Renglonnivelespecifico::getRenglonvarianteId).orElse(0);
    }

    private List<Bajoespecificacionrv> createNajoEspecificacion(Nivelespecifico niv, List<SumBajoEspRVPCW> valueList) {
        tem = new ArrayList<>();
        for (SumBajoEspRVPCW ba : valueList) {
            sumc = getIsPresentInCalPreC(ba);
            Bajoespecificacionrv bajoespecificacion = new Bajoespecificacionrv();
            bajoespecificacion.setNivelespecificoId(niv.getId());
            int idR = idRenglon(niv.getId(), ba.getCodeRV());
            bajoespecificacion.setRenglonvarianteId(idR);
            bajoespecificacion.setIdsuministro(sumc.getId());
            bajoespecificacion.setCantidad(ba.getCantidad());
            bajoespecificacion.setCosto(ba.getCosto());
            bajoespecificacion.setTipo(sumc.getTipo());

            tem.add(bajoespecificacion);
        }

        return tem;
    }


}
