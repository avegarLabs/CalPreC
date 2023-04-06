package AccessMigration;

import AccessMigration.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import models.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class ImportMDBCController implements Initializable {

    @FXML
    private TableView<EmpresaToImportView> dataTable;

    @FXML
    private TableColumn<EmpresaToImportView, StringProperty> code;

    @FXML
    private TableColumn<EmpresaToImportView, StringProperty> descrip;

    @FXML
    private TableColumn<EmpresaToImportView, String> simil;

    @FXML
    private Label label_title;

    @FXML
    private JFXComboBox<String> tipoSelect;

    @FXML
    private JFXComboBox<String> entidadSelect;

    @FXML
    private JFXComboBox<String> inversionistaSelect;

    @FXML
    private JFXComboBox<String> salarioSelect;

    @FXML
    private JFXButton btn_carga;

    @FXML
    private JFXCheckBox checkEstructura;

    private ObservableList<EmpresaToImportView> empresaToImportViewObservableList;
    private ObservableList<String> entidadObservableList;
    private ObservableList<String> inversionistasObservableList;
    private ObservableList<String> salarioObservableList;

    private List<Entidad> entidadList;
    private List<Inversionista> inversionistaList;
    private List<Salario> salarioList;

    private ArrayList<EstructUOCalP> datosEstruct;
    private ArrayList<EspAndSubRelation> espAndSubRelations;
    private CalPrecIdComponentes calPrecIdComponentes;
    private List<Unidadobra> unidadobraArrayList;
    private ArrayList<UnidadRnglonesPCW> unidadRnglonesPCWSList;
    private DatabaseEstructure databaseEstructure;
    private List<UnidadRnglonesPCW> unidadRnglonesPCWArrayList;
    private PostreSqlOperation postreSqlOperation;
    private ArrayList<Renglonvariante> renglonvarianteArrayList;
    private ArrayList<SuminstrosPCW> suminstrosPCWArrayList;
    private List<BajoEspecificacionPCW> bajoEspecificacionPCWArrayList;

    private List<EmpresaToImportView> selectedEmpresa;
    private List<Empresaobra> empresaobrasList;
    private List<Empresaobrasalario> empresaobrasalarioList;
    private Empresaobra empresaobra;
    private Empresaobrasalario empresaobrasalario;
    private ArrayList<CalPrecSuminist> componentesArrayList;
    private ArrayList<BajoEspecificacionPCW> suministrosNoEncontrados;

    private JFXCheckBox checkBox;
    private Integer idOb;
    private List<Bajoespecificacion> bajoespecificacionList;
    private String Tnorma;
    private ArrayList<EspecialidadesSubEstructPCW> especialidadesSubEstructPCWArrayList;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private Map<Especialidades, List<Subespecialidades>> especialidadesListMap2;
    private Map<Especialidades, List<Subespecialidades>> especialidadesListMapTemp;
    private Map<EstructuraPCWUnidad, List<UnidadRnglonesPCW>> estructuraPCWRngListMap;
    private Map<EstructuraPCWUnidad, List<BajoEspecificacionPCW>> bajoEstructuraPCWUnidadListMap;
    private List<EstructuraPCWUnidad> estructuraPCWUnidadArrayList;
    private boolean reload;
    private List<Bajoespecificacion> tem;
    private CalPrecSuminist sumc;
    private ArrayList<Recursos> recursosArrayList;
    private List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private Map<String, List<EspecialidadesSubEstructPCW>> stringListMap;
    private boolean isSub;
    private ArrayList<EspecialidadesSubEstructPCW> subEstructPCWList;
    private EspAndSubRelation espAndSub;
    private List<UnidadRnglonesPCW> listUnidadReng;
    private List<BajoEspecificacionPCW> sumBEficacionPCWArrayList;
    private EmpresaToImportView empresaToImportView;

    private ObservableList<EmpresaToImportView> getEmpresaToImportViewObservableList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaToImportViewObservableList = FXCollections.observableArrayList();
            List<Empresaconstructora> empresaconstructoraList = session.createQuery("FROM Empresaconstructora ").getResultList();
            empresaToImportViewObservableList.addAll(empresaconstructoraList.parallelStream().map(empresaconstructora -> {
                EmpresaToImportView empresa = new EmpresaToImportView(empresaconstructora.getId(), checkBox = new JFXCheckBox(empresaconstructora.getCodigo()),
                        empresaconstructora.getDescripcion(), " ");
                return empresa;
            }).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return empresaToImportViewObservableList;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getEntidadObservableList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            entidadObservableList = FXCollections.observableArrayList();
            entidadList = new ArrayList<>();
            entidadList = session.createQuery("FROM Entidad ").getResultList();
            entidadObservableList.addAll(entidadList.parallelStream().map(entidad -> entidad.getCodigo() + " - " + entidad.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return entidadObservableList;

        } catch (Exception ex) {

        }

        return FXCollections.observableArrayList();
    }

    private ObservableList<String> getInversionistasObservableList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            inversionistasObservableList = FXCollections.observableArrayList();
            inversionistaList = new ArrayList<>();
            inversionistaList = session.createQuery("FROM Inversionista ").getResultList();
            inversionistasObservableList.addAll(inversionistaList.parallelStream().map(entidad -> entidad.getCodigo() + " - " + entidad.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return inversionistasObservableList;

        } catch (Exception ex) {

        }

        return FXCollections.observableArrayList();
    }

    private ObservableList<String> getSalarioObservableList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            salarioObservableList = FXCollections.observableArrayList();
            salarioList = new ArrayList<>();
            salarioList = session.createQuery("FROM Salario ").getResultList();
            salarioObservableList.addAll(salarioList.parallelStream().map(Salario::getDescripcion).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return salarioObservableList;

        } catch (Exception ex) {

        }

        return FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> tipoList = FXCollections.observableArrayList();
        tipoList.add("Unidad de Obra");
        tipoList.add("Rengón Variante");
        tipoSelect.setItems(tipoList);

        entidadSelect.setItems(getEntidadObservableList());
        inversionistaSelect.setItems(getInversionistasObservableList());
        salarioSelect.setItems(getSalarioObservableList());

        code.setCellValueFactory(new PropertyValueFactory<EmpresaToImportView, StringProperty>("code"));
        descrip.setCellValueFactory(new PropertyValueFactory<EmpresaToImportView, StringProperty>("descrip"));
        simil.setCellValueFactory(new PropertyValueFactory<EmpresaToImportView, String>("simil"));
        simil.setCellFactory(TextFieldTableCell.<EmpresaToImportView>forTableColumn());
        simil.setOnEditCommit((TableColumn.CellEditEvent<EmpresaToImportView, String> t) -> {
            ((EmpresaToImportView) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSimil(t.getNewValue());
        });
        dataTable.setItems(getEmpresaToImportViewObservableList());
        dataTable.setEditable(true);
    }

    public void handleImportMDBAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo .mdb");
        File file = fileChooser.showOpenDialog(null);

        if (file != null && file.getAbsolutePath().endsWith(".mdb")) {

            if (tipoSelect.getValue().trim().equals("Unidad de Obra")) {
                importObraUO(file.getAbsolutePath());

            } else if (tipoSelect.getValue().trim().equals("Rengón Variante")) {
                importObraRV(file.getAbsolutePath());

            }


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Información");
            alert.setContentText("Fichero incorrecto, cargue un fichero .mdb");
            alert.showAndWait();
        }

    }

    public void importObraUO(String adsPath) {
        String path = "//" + adsPath.replace("\\", "//");
        String renglonesPath = path.replace(path.substring(path.length() - 12), "Renglones.mdb");

        databaseEstructure = DatabaseEstructure.getInstance();
        databaseEstructure.pathdb = path;
        databaseEstructure.pathdbRV = renglonesPath;
        postreSqlOperation = PostreSqlOperation.getInstance();

        if (checkEstructura.isSelected() == true) {

            List<EstructuraPCW> datos = databaseEstructure.getEstructuraToUnidadO().parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
            datos.sort(Comparator.comparing(EstructuraPCW::getZona));

            unidadRnglonesPCWSList = new ArrayList<>();
            unidadRnglonesPCWSList = databaseEstructure.getUnidadRnglonesArrayList();

            estructuraPCWUnidadArrayList = new ArrayList<>();
            estructuraPCWUnidadArrayList = getEstructuraRVunidad();

            estructuraPCWRngListMap = new HashMap<>();
            for (EstructuraPCWUnidad est : estructuraPCWUnidadArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                estructuraPCWRngListMap.put(est, getEstRV(est));
            }

            suminstrosPCWArrayList = new ArrayList<>();
            suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();

            bajoEspecificacionPCWArrayList = new ArrayList<>();
            bajoEspecificacionPCWArrayList = databaseEstructure.getBajoEspecificacionPCWArrayList();


            bajoEstructuraPCWUnidadListMap = new HashMap<>();
            for (EstructuraPCWUnidad est : estructuraPCWUnidadArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                bajoEstructuraPCWUnidadListMap.put(est, getEstbajo(est));
            }

            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList = postreSqlOperation.getSubespecialidades();

            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList = postreSqlOperation.getEspecialidadesArrayList();

            especialidadesSubEstructPCWArrayList = new ArrayList<>();
            especialidadesSubEstructPCWArrayList = databaseEstructure.getEspecialidadesSubEstructPCWS();
            especialidadesSubEstructPCWArrayList.sort(Comparator.comparing(EspecialidadesSubEstructPCW::getDescEsp));

            List<String> especialidadesName = especialidadesSubEstructPCWArrayList.parallelStream().map(EspecialidadesSubEstructPCW::getDescEsp).collect(Collectors.toList());
            List<String> oklist = new ArrayList<>();
            for (String e : especialidadesName.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                System.out.println("ESPECIALIDAD: " + e);
                if (e.endsWith(" ")) {
                    oklist.add(e.substring(0, e.indexOf(" ")));
                } else if (e.startsWith(" ")) {
                    oklist.add(e.substring(1));
                } else {
                    oklist.add(e);
                }
            }

            List<EspecialidadesSubEstructPCW> ListEstructuraOK = new ArrayList<>();
            for (String st : oklist.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                checkEspecialidad(st);
                ListEstructuraOK.addAll(getOkStructura(st));
            }

            especialidadesListMapTemp = new HashMap<>();
            especialidadesListMapTemp = getEspecialidadesListMap2();

            espAndSubRelations = new ArrayList<>();
            for (EspecialidadesSubEstructPCW e : ListEstructuraOK.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                espAndSubRelations.add(getIdsEspAndSub(e));
            }

            espAndSubRelations.removeIf(espAndSubRelation1 -> espAndSubRelation1 == null);

            List<String> zonasCodeList = datos.parallelStream().map(EstructuraPCW::getZona).distinct().collect(Collectors.toList());
            List<ZonasPCW> zonasList = new ArrayList<>();

            List<Recursos> fullListRec = postreSqlOperation.getFullRecursosArrayList();

            String[] partEnt = entidadSelect.getValue().trim().split(" - ");
            String[] partInv = inversionistaSelect.getValue().trim().split(" - ");

            int idEnt = entidadList.parallelStream().filter(entidad -> entidad.getCodigo().equals(partEnt[0])).findFirst().map(Entidad::getId).orElse(null);
            int idInv = inversionistaList.parallelStream().filter(inversionista -> inversionista.getCodigo().equals(partInv[0])).findFirst().map(Inversionista::getId).orElse(null);
            Salario sal = salarioList.parallelStream().filter(salario -> salario.getDescripcion().equals(salarioSelect.getValue().trim())).findFirst().orElse(null);

            ObraPCW obraPCW = databaseEstructure.getObraPCW();
            idOb = postreSqlOperation.createObra(obraPCW, idEnt, idInv, sal.getId());

            selectedEmpresa = dataTable.getItems().parallelStream().filter(empresaToImportView -> empresaToImportView.getCode().isSelected() == true).collect(Collectors.toList());

            empresaobrasList = new ArrayList<>();
            empresaobrasalarioList = new ArrayList<>();
            for (EmpresaToImportView emp : selectedEmpresa) {

                empresaobra = new Empresaobra();
                empresaobra.setEmpresaconstructoraId(emp.getId());
                empresaobra.setObraId(idOb);

                empresaobrasList.add(empresaobra);

                empresaobrasalario = new Empresaobrasalario();
                empresaobrasalario.setEmpresaconstructoraId(emp.getId());
                empresaobrasalario.setObraId(idOb);
                empresaobrasalario.setSalarioId(sal.getId());

                empresaobrasalario.setIi(sal.getIi());
                empresaobrasalario.setIii(sal.getIii());
                empresaobrasalario.setIv(sal.getIv());
                empresaobrasalario.setV(sal.getV());
                empresaobrasalario.setVi(sal.getVi());
                empresaobrasalario.setVii(sal.getVii());
                empresaobrasalario.setViii(sal.getViii());
                empresaobrasalario.setIx(sal.getIx());
                empresaobrasalario.setX(sal.getX());
                empresaobrasalario.setXi(sal.getXi());
                empresaobrasalario.setXii(sal.getXii());
                empresaobrasalario.setXiii(sal.getXiii());
                empresaobrasalario.setXiv(sal.getXiv());
                empresaobrasalario.setXv(sal.getXv());
                empresaobrasalario.setXvi(sal.getXvi());

                empresaobrasalario.setAutoesp(sal.getAutesp());
                empresaobrasalario.setAntig(sal.getAntiguedad());
                empresaobrasalario.setVacaiones(sal.getVacaciones());
                empresaobrasalario.setNomina(sal.getNomina());
                empresaobrasalario.setSeguro(sal.getSeguro());

                empresaobrasalario.setGtii(sal.getGtii());
                empresaobrasalario.setGtiii(sal.getGtiii());
                empresaobrasalario.setGtiv(sal.getGtiv());
                empresaobrasalario.setGtv(sal.getGtv());
                empresaobrasalario.setGtvi(sal.getGtvi());
                empresaobrasalario.setGtvii(sal.getGtvii());
                empresaobrasalario.setGtviii(sal.getGtviii());
                empresaobrasalario.setGtix(sal.getGtix());
                empresaobrasalario.setGtx(sal.getGtx());
                empresaobrasalario.setGtxi(sal.getGtxi());
                empresaobrasalario.setGtxii(sal.getGtxii());
                empresaobrasalario.setGtxiii(sal.getGtxiii());
                empresaobrasalario.setGtxi(sal.getGtix());
                empresaobrasalario.setGtxv(sal.getGtxv());
                empresaobrasalario.setGtxi(sal.getGtxi());

                empresaobrasalarioList.add(empresaobrasalario);

            }

            postreSqlOperation.createEmpresaObra(empresaobrasList);
            postreSqlOperation.createEmpresaObraSalario(empresaobrasalarioList);


            for (String items : zonasCodeList) {
                zonasList.add(databaseEstructure.getZonas(items));
            }

            postreSqlOperation.createZonas(idOb, zonasList);

            List<ObjetosPCW> objetosArrayList = new ArrayList<>();
            for (ZonasPCW zonas : zonasList) {
                objetosArrayList.addAll(databaseEstructure.getObjetos(zonas.getCode()));
            }

            postreSqlOperation.createObjetos(idOb, objetosArrayList);

            List<NivelPCW> nivelArrayList = new ArrayList<>();
            for (ObjetosPCW objetos : objetosArrayList) {
                nivelArrayList.addAll(databaseEstructure.getNiveles(objetos.getZona(), objetos.getCode()));
            }

            postreSqlOperation.createNiveles(idOb, nivelArrayList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Trabajo terminado");
            alert.setContentText("La obra: " + obraPCW.getDescrip() + " ha sido restaurada para PostgreSQL");
            alert.showAndWait();


        }
    /*
    datosEstruct = new ArrayList<>();
    Map<EstructuraPCW, List<UnidadesObraPCW>> datosEstructuraPCWListMap = new HashMap<>();
    for (EstructuraPCW est : datos.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
        datosEstruct.add(postreSqlOperation.getEstructUOCalPS(est));
       datosEstructuraPCWListMap.put(est, databaseEstructure.getUnidad(est));
    }

    List<Unidadobra> unidadobraArrayListToCreate = new ArrayList<>();
    for (Map.Entry<EstructuraPCW, List<UnidadesObraPCW>> items : datosEstructuraPCWListMap.entrySet()) {
        calPrecIdComponentes = getEstIdes(items.getKey());
        if(calPrecIdComponentes != null) {
            unidadobraArrayListToCreate.addAll(postreSqlOperation.getUnidadobraList(idOb, calPrecIdComponentes, items.getValue().trim()));
        }
    }

    postreSqlOperation.createUnidadesObra(unidadobraArrayListToCreate.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));


    unidadobraArrayList = new ArrayList<>();
    unidadobraArrayList = postreSqlOperation.getUnidadobraArrayList().parallelStream().filter(uo -> uo.getObraId() == idOb).collect(Collectors.toList());


    Map<Unidadobra, List<UnidadRnglonesPCW>> unidadobraRengListMap = new HashMap<>();
    Map<Unidadobra, List<BajoEspecificacionPCW>> bajoUnidadobraListMap = new HashMap<>();
    for (Unidadobra u : unidadobraArrayList) {
        unidadobraRengListMap.put(u, getUoRen(u));
        bajoUnidadobraListMap.put(u, getBajoToU(u));

    }


    Tnorma = null;
    if (salarioSelect.getValue().trim().equals("Resolución 30")) {
        Tnorma = "T30";
    } else if (salarioSelect.getValue().trim().equals("Resolución 15")) {
        Tnorma = "T15";
    }

    renglonvarianteArrayList = new ArrayList<>();
    renglonvarianteArrayList = postreSqlOperation.getRenglonvarianteArrayList(Tnorma);
    for (Map.Entry<Unidadobra, List<UnidadRnglonesPCW>> items : unidadobraRengListMap.entrySet()) {
        if (items.getValue().trim().size() > 0) {
            sheckRenglones(items.getValue().trim());
        }
    }


    ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList = new ArrayList<>();
    for (Map.Entry<Unidadobra, List<UnidadRnglonesPCW>> items : unidadobraRengListMap.entrySet()) {
        if (items.getValue().trim().size() > 0) {
            unidadobrarenglonArrayList.addAll(postreSqlOperation.getUnidadobrarenglonArrayList(items.getKey(), items.getValue().trim()));
        }
    }

    postreSqlOperation.createUnidadesObraRenglones(unidadobrarenglonArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));


    componentesArrayList = new ArrayList<>();
    componentesArrayList = postreSqlOperation.getCalPrecSuministArrayList();

    List<BajoEspecificacionPCW> noSuministPresentList = new ArrayList<>();
    for (Map.Entry<Unidadobra, List<BajoEspecificacionPCW>> items : bajoUnidadobraListMap.entrySet()) {

        if (items.getValue().trim().size() > 0) {
            for (BajoEspecificacionPCW b : items.getValue().trim()) {
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
    for (Map.Entry<Unidadobra, List<BajoEspecificacionPCW>> items : bajoUnidadobraListMap.entrySet()) {
        if (items.getValue().trim().size() > 0) {
            bajoespecificacionList.addAll(createNajoEspecificacion(items.getKey(), items.getValue().trim()));
        }
    }

    postreSqlOperation.createBajoespecificacion(bajoespecificacionList);

    CalcConceptosUO calcConceptosUO = CalcConceptosUO.getInstance();
    calcConceptosUO.calcValoresdeGastos(selectedEmpresa, idOb);
*/


    }

    public void importObraRV(String adsPath) {
        String path = "//" + adsPath.replace("\\", "//");
        String renglonesPath = path.replace(path.substring(path.length() - 12), "Renglones.mdb");

        databaseEstructure = DatabaseEstructure.getInstance();
        databaseEstructure.pathdb = path;
        databaseEstructure.pathdbRV = renglonesPath;
        postreSqlOperation = PostreSqlOperation.getInstance();

        if (checkEstructura.isSelected() == true) {

            List<EstructuraPCW> datos = databaseEstructure.getEstructuraToRV().parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
            datos.sort(Comparator.comparing(EstructuraPCW::getZona));
/*
            unidadRnglonesPCWSList = new ArrayList<>();
            unidadRnglonesPCWSList = databaseEstructure.getUnidadRnglonesArrayList();

            estructuraPCWUnidadArrayList = new ArrayList<>();
            estructuraPCWUnidadArrayList = getEstructuraRVunidad();

            estructuraPCWRngListMap = new HashMap<>();
            for (EstructuraPCWUnidad est : estructuraPCWUnidadArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                estructuraPCWRngListMap.put(est, getEstRV(est));
            }

            suminstrosPCWArrayList = new ArrayList<>();
            suminstrosPCWArrayList = databaseEstructure.getSuminstrosPCWArrayList();

            bajoEspecificacionPCWArrayList = new ArrayList<>();
            bajoEspecificacionPCWArrayList = databaseEstructure.getBajoEspecificacionPCWArrayList();


            bajoEstructuraPCWUnidadListMap = new HashMap<>();
            for (EstructuraPCWUnidad est : estructuraPCWUnidadArrayList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                bajoEstructuraPCWUnidadListMap.put(est, getEstbajo(est));
            }

            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList = postreSqlOperation.getSubespecialidades();

            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList = postreSqlOperation.getEspecialidadesArrayList();

            especialidadesSubEstructPCWArrayList = new ArrayList<>();
            especialidadesSubEstructPCWArrayList = databaseEstructure.getEspecialidadesSubEstructPCWS();
            especialidadesSubEstructPCWArrayList.sort(Comparator.comparing(EspecialidadesSubEstructPCW::getDescEsp));

            List<String> especialidadesName = especialidadesSubEstructPCWArrayList.parallelStream().map(EspecialidadesSubEstructPCW::getDescEsp).collect(Collectors.toList());
            List<String> oklist = new ArrayList<>();
            for (String e : especialidadesName.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                if (e.endsWith(" ")) {
                    oklist.add(e.substring(0, e.indexOf(" ")));
                } else if (e.startsWith(" ")) {
                    oklist.add(e.substring(1));
                } else {
                    oklist.add(e);
                }
            }

            List<EspecialidadesSubEstructPCW> ListEstructuraOK = new ArrayList<>();
            for (String st : oklist.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                checkEspecialidad(st);
                ListEstructuraOK.addAll(getOkStructura(st));
            }

            especialidadesListMapTemp = new HashMap<>();
            especialidadesListMapTemp = getEspecialidadesListMap2();

            espAndSubRelations = new ArrayList<>();
            for (EspecialidadesSubEstructPCW e : ListEstructuraOK.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                espAndSubRelations.add(getIdsEspAndSub(e));
            }

            espAndSubRelations.removeIf(espAndSubRelation1 -> espAndSubRelation1 == null);
*/
            List<String> zonasCodeList = datos.parallelStream().map(EstructuraPCW::getZona).distinct().collect(Collectors.toList());
            List<ZonasPCW> zonasList = new ArrayList<>();

            List<Recursos> fullListRec = postreSqlOperation.getFullRecursosArrayList();

            String[] partEnt = entidadSelect.getValue().trim().split(" - ");
            String[] partInv = inversionistaSelect.getValue().trim().split(" - ");

            int idEnt = entidadList.parallelStream().filter(entidad -> entidad.getCodigo().equals(partEnt[0])).findFirst().map(Entidad::getId).orElse(null);
            int idInv = inversionistaList.parallelStream().filter(inversionista -> inversionista.getCodigo().equals(partInv[0])).findFirst().map(Inversionista::getId).orElse(null);
            Salario sal = salarioList.parallelStream().filter(salario -> salario.getDescripcion().equals(salarioSelect.getValue().trim())).findFirst().orElse(null);

            ObraPCW obraPCW = databaseEstructure.getObraPCW();
            idOb = postreSqlOperation.createObraRV(obraPCW, idEnt, idInv, sal.getId());

            selectedEmpresa = dataTable.getItems().parallelStream().filter(empresaToImportView -> empresaToImportView.getCode().isSelected() == true).collect(Collectors.toList());

            empresaobrasList = new ArrayList<>();
            empresaobrasalarioList = new ArrayList<>();
            for (EmpresaToImportView emp : selectedEmpresa) {


                empresaobra = new Empresaobra();
                empresaobra.setEmpresaconstructoraId(emp.getId());
                empresaobra.setObraId(idOb);

                empresaobrasList.add(empresaobra);

                empresaobrasalario = new Empresaobrasalario();
                empresaobrasalario.setEmpresaconstructoraId(emp.getId());
                empresaobrasalario.setObraId(idOb);
                empresaobrasalario.setSalarioId(sal.getId());

                empresaobrasalario.setIi(sal.getIi());
                empresaobrasalario.setIii(sal.getIii());
                empresaobrasalario.setIv(sal.getIv());
                empresaobrasalario.setV(sal.getV());
                empresaobrasalario.setVi(sal.getVi());
                empresaobrasalario.setVii(sal.getVii());
                empresaobrasalario.setViii(sal.getViii());
                empresaobrasalario.setIx(sal.getIx());
                empresaobrasalario.setX(sal.getX());
                empresaobrasalario.setXi(sal.getXi());
                empresaobrasalario.setXii(sal.getXii());
                empresaobrasalario.setXiii(sal.getXiii());
                empresaobrasalario.setXiv(sal.getXiv());
                empresaobrasalario.setXv(sal.getXv());
                empresaobrasalario.setXvi(sal.getXvi());

                empresaobrasalario.setAutoesp(sal.getAutesp());
                empresaobrasalario.setAntig(sal.getAntiguedad());
                empresaobrasalario.setVacaiones(sal.getVacaciones());
                empresaobrasalario.setNomina(sal.getNomina());
                empresaobrasalario.setSeguro(sal.getSeguro());

                empresaobrasalario.setGtii(sal.getGtii());
                empresaobrasalario.setGtiii(sal.getGtiii());
                empresaobrasalario.setGtiv(sal.getGtiv());
                empresaobrasalario.setGtv(sal.getGtv());
                empresaobrasalario.setGtvi(sal.getGtvi());
                empresaobrasalario.setGtvii(sal.getGtvii());
                empresaobrasalario.setGtviii(sal.getGtviii());
                empresaobrasalario.setGtix(sal.getGtix());
                empresaobrasalario.setGtx(sal.getGtx());
                empresaobrasalario.setGtxi(sal.getGtxi());
                empresaobrasalario.setGtxii(sal.getGtxii());
                empresaobrasalario.setGtxiii(sal.getGtxiii());
                empresaobrasalario.setGtxi(sal.getGtix());
                empresaobrasalario.setGtxv(sal.getGtxv());
                empresaobrasalario.setGtxi(sal.getGtxi());

                empresaobrasalarioList.add(empresaobrasalario);

            }

            postreSqlOperation.createEmpresaObra(empresaobrasList);
            postreSqlOperation.createEmpresaObraSalario(empresaobrasalarioList);


            for (String items : zonasCodeList) {
                zonasList.add(databaseEstructure.getZonas(items));
            }

            postreSqlOperation.createZonas(idOb, zonasList);

            List<ObjetosPCW> objetosArrayList = new ArrayList<>();
            for (ZonasPCW zonas : zonasList) {
                objetosArrayList.addAll(databaseEstructure.getObjetos(zonas.getCode()));
            }

            postreSqlOperation.createObjetos(idOb, objetosArrayList);

            List<NivelPCW> nivelArrayList = new ArrayList<>();
            for (ObjetosPCW objetos : objetosArrayList) {
                nivelArrayList.addAll(databaseEstructure.getNiveles(objetos.getZona(), objetos.getCode()));
            }

            postreSqlOperation.createNiveles(idOb, nivelArrayList);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Trabajo terminado");
            alert.setContentText("La obra: " + obraPCW.getDescrip() + " ha sido restaurada para PostgreSQL");
            alert.showAndWait();


        }

    }

    private List<EspecialidadesSubEstructPCW> getOkStructura(String st) {
        return especialidadesSubEstructPCWArrayList.parallelStream().filter(especialidadesSubEstructPCW -> especialidadesSubEstructPCW.getDescEsp().contains(st)).collect(Collectors.toList());
    }

    private List<BajoEspecificacionPCW> getEstbajo(EstructuraPCWUnidad est) {
        return bajoEspecificacionPCWArrayList.parallelStream().filter(bajo -> bajo.getEmp().contentEquals(est.getEmpresa()) && bajo.getZona().contentEquals(est.getZona()) && bajo.getObjet().contentEquals(est.getObjeto()) && bajo.getNiv().contentEquals(est.getNivel()) && bajo.getEsp().contentEquals(est.getEspecialidad()) && bajo.getSup().contentEquals(est.getCodSub()) && bajo.getUo().contentEquals(est.getCodeUnidad())).collect(Collectors.toList());
    }

    private List<EstructuraPCWUnidad> getEstructuraRVunidad() {
        return unidadRnglonesPCWSList.parallelStream().map(est -> {
            EstructuraPCWUnidad estructuraPCWUnidad = new EstructuraPCWUnidad(est.getEmpresa(), est.getZona(), est.getObj(), est.getNiv(), est.getEsp(), est.getSub(), est.getSub(), est.getUo());
            return estructuraPCWUnidad;
        }).collect(Collectors.toList());

    }

    private List<UnidadRnglonesPCW> getEstRV(EstructuraPCWUnidad est) {
        return unidadRnglonesPCWSList.parallelStream().filter(unidadRnglonesPCW -> unidadRnglonesPCW.getEmpresa().contentEquals(est.getEmpresa()) && unidadRnglonesPCW.getZona().contentEquals(est.getZona()) && unidadRnglonesPCW.getObj().contentEquals(est.getObjeto()) && unidadRnglonesPCW.getNiv().contentEquals(est.getNivel()) && unidadRnglonesPCW.getEsp().contentEquals(est.getEspecialidad()) && unidadRnglonesPCW.getSub().contentEquals(est.getCodSub()) && unidadRnglonesPCW.getUo().contentEquals(est.getCodeUnidad())).collect(Collectors.toList());
    }

    private List<Bajoespecificacion> createNajoEspecificacion(Unidadobra key, List<BajoEspecificacionPCW> valueList) {
        tem = new ArrayList<>();
        for (BajoEspecificacionPCW ba : valueList) {
            sumc = getIsPresentInCalPreC(ba);
            Bajoespecificacion bajoespecificacion = new Bajoespecificacion();
            bajoespecificacion.setUnidadobraId(key.getId());
            bajoespecificacion.setIdSuministro(sumc.getId());
            bajoespecificacion.setRenglonvarianteId(0);
            bajoespecificacion.setCantidad(ba.getCant());
            bajoespecificacion.setCosto(ba.getCosto());
            bajoespecificacion.setTipo(sumc.getTipo());
            bajoespecificacion.setSumrenglon(0);

            tem.add(bajoespecificacion);
        }

        return tem;
    }

    private void createCalPrecSuministros(List<BajoEspecificacionPCW> collect) {
        recursosArrayList = new ArrayList<>();
        suministrossemielaboradosArrayList = new ArrayList<>();
        for (BajoEspecificacionPCW baj : collect) {
            SuminstrosPCW suminstrosPCW = geSuminstrosPCW(baj.suminist);
            if (suminstrosPCW.getTipo().equals("1")) {
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
                sumuni.setPertenece(String.valueOf(idOb));
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
            } else if (suminstrosPCW.getTipo().equals("S")) {

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
                suministrossemielaborados.setPertenec(String.valueOf(idOb));

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

    /*
    private void sheckRenglones(List<UnidadRnglonesPCW> rnglonesPCWList) {
        for (UnidadRnglonesPCW rnglonesPCW : rnglonesPCWList) {
            Renglonvariante r = postreSqlOperation.getRenglonvariante(rnglonesPCW.getRv());
            if (r == null) {
                postreSqlOperation.createRnglonVarianteFromPCWin(rnglonesPCW.getRv(), Tnorma);
            }

        }

    }
*/
    private Map<Especialidades, List<Subespecialidades>> getEspecialidadesListMap2() {
        especialidadesListMap2 = new HashMap<>();
        especialidadesListMap2 = postreSqlOperation.getEstructUOCalPSEtoS();
        return especialidadesListMap2;
    }

    private Especialidades getEspecialidadesToMatch(String desc) {
        return postreSqlOperation.getEspecialidades().parallelStream().filter(espe -> StringUtils.startsWithIgnoreCase(espe.getDescripcion(), desc.substring(0, 3)) == true && StringUtils.containsIgnoreCase(espe.getDescripcion(), desc)).findFirst().orElse(null);

    }

    private void checkEspecialidad(String espDesc) {
        if (espDesc.length() > 0) {
            Especialidades temp = getEspecialidadesToMatch(espDesc);
            stringListMap = new HashMap<>();
            if (temp != null) {
                stringListMap.put(espDesc, getListEspecialidadesSubEstruct(espDesc).stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));
                needSubEspecialidad(stringListMap);
            }
        }
    }

    private void needSubEspecialidad(Map<String, List<EspecialidadesSubEstructPCW>> datos) {
        List<EspecialidadesSubEstructPCW> list = new ArrayList<>();
        for (Map.Entry<String, List<EspecialidadesSubEstructPCW>> items : datos.entrySet()) {
            Especialidades tempEs = getEspecialidadesToMatch(items.getKey());
            for (EspecialidadesSubEstructPCW e : items.getValue()) {
                if (!checkSubespecialidad(e.getDescSub())) {
                    list.add(e);
                }
            }
            createSubesp(tempEs, list);
        }
    }

    private boolean checkSubespecialidad(String descSub) {
        isSub = true;
        Subespecialidades sub = postreSqlOperation.getSubespecialidades().parallelStream().filter(espe -> StringUtils.startsWithIgnoreCase(espe.getDescripcion(), descSub.substring(0, 3)) == true && StringUtils.containsIgnoreCase(espe.getDescripcion(), descSub)).findFirst().orElse(new Subespecialidades());
        if (sub.getId() == 0) {
            isSub = false;
        }
        return isSub;
    }

    private void createSubesp(Especialidades esp, List<EspecialidadesSubEstructPCW> list) {
        int count = 0;
        Especialidades epl = postreSqlOperation.getEspecialidad(esp.getCodigo());
        List<Subespecialidades> list1 = new ArrayList<>();
        count = epl.getSubespecialidadesById().size();
        for (EspecialidadesSubEstructPCW e : list) {
            count++;
            Subespecialidades subespecialidades = new Subespecialidades();
            subespecialidades.setCodigo(esp.getCodigo() + String.valueOf(count));
            if (e.getDescSub().length() >= 100) {
                subespecialidades.setDescripcion(e.getDescSub().substring(0, 50));
            } else {
                subespecialidades.setDescripcion(e.getDescSub());
            }
            subespecialidades.setEspecialidadesId(esp.getId());
            list1.add(subespecialidades);

        }
        postreSqlOperation.createSubespecialidadList(list1);
    }

    private List<EspecialidadesSubEstructPCW> getListEspecialidadesSubEstruct(String desc) {

        subEstructPCWList = new ArrayList<>();
        subEstructPCWList = (ArrayList<EspecialidadesSubEstructPCW>) especialidadesSubEstructPCWArrayList.parallelStream().filter(es -> es.getDescEsp().contains(desc)).collect(Collectors.toSet()).stream().collect(Collectors.toList());

        return subEstructPCWList;
    }

    private EspAndSubRelation getIdsEspAndSub(EspecialidadesSubEstructPCW est) {
        String descEs = null;
        if (est.getDescEsp().startsWith(" ")) {
            descEs = est.getDescEsp().substring(1);
        } else if (est.getDescEsp().endsWith(" ")) {
            descEs = est.getDescEsp().substring(0, est.getDescEsp().indexOf(" "));
        } else {
            descEs = est.getDescEsp();
        }
        /*
        for (Map.Entry<Especialidades, List<Subespecialidades>> items : especialidadesListMapTemp.entrySet()) {
            if (StringUtils.containsIgnoreCase(items.getKey().getDescripcion(), descEs) == true && StringUtils.compareIgnoreCase(items.getKey().getDescripcion(), descEs) >= 0) {
                Subespecialidades sub = items.getValue().trim().parallelStream().filter(subespecialidades -> StringUtils.equalsIgnoreCase(subespecialidades.getDescripcion(), est.getDescSub())).findFirst().orElse(null);
                if (sub != null) {
                    return new EspAndSubRelation(est.getCodeEsp(), items.getKey().getCodigo(), items.getKey().getId(), items.getKey().getDescripcion(), est.getCodeSub(), sub.getCodigo(), sub.getId(), sub.getDescripcion());
                }
            }
        }
        */

        return null;
    }

    private CalPrecIdComponentes getEstIdes(EstructuraPCW estructuraPCW) {
        CalPrecIdComponentes calPrecIdCompon = null;
        EstructUOCalP estructUOCalP = datosEstruct.parallelStream().filter(estruc -> estruc.getZona().equals(estructuraPCW.getZona()) && estruc.getObjeto().equals(estructuraPCW.getObjeto()) && estruc.getNivel().equals(estructuraPCW.getNivel())).findFirst().orElse(null);
        EspAndSubRelation espAndSub = getEspAndSubRelation(estructuraPCW);
        if (espAndSub != null) {
            calPrecIdCompon = new CalPrecIdComponentes(getEmpresaToImportView(0, estructuraPCW.getEmpresa()).getId(), estructUOCalP.getZonId(), estructUOCalP.getObjId(), estructUOCalP.getNivId(), espAndSub.getIdEsp(), espAndSub.getIdSub());
        }
        return calPrecIdCompon;
    }

    private List<UnidadRnglonesPCW> getUoRen(Unidadobra unidadobra) {
        listUnidadReng = new ArrayList<>();
        for (Map.Entry<EstructuraPCWUnidad, List<UnidadRnglonesPCW>> items : estructuraPCWRngListMap.entrySet()) {
            espAndSub = getEspAndSubRelationUnidad(items.getKey());
            if (espAndSub != null) {
                Unidadobra unidad = checkEstructure(items.getKey(), espAndSub);
                if (unidad != null) {
                    if (unidadobra.getCodigo().contentEquals(unidad.getCodigo())) {
                        listUnidadReng.addAll(items.getValue());
                    }
                }
            }
        }
        return listUnidadReng;

    }

    private Unidadobra checkEstructure(EstructuraPCWUnidad estructuraPCWUnidad, EspAndSubRelation espAndSubRelation) {
        return unidadobraArrayList.parallelStream().filter(unidadobra -> unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getId() == getEmpresaToImportView(0, estructuraPCWUnidad.getEmpresa()).getId() && unidadobra.getZonasByZonasId().getCodigo().contentEquals(estructuraPCWUnidad.getZona()) && unidadobra.getObjetosByObjetosId().getCodigo().contentEquals(estructuraPCWUnidad.getObjeto()) && unidadobra.getNivelByNivelId().getCodigo().contentEquals(estructuraPCWUnidad.getNivel()) && unidadobra.getEspecialidadesByEspecialidadesId().getCodigo().contentEquals(espAndSubRelation.getCodeEsp()) && unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo().contentEquals(espAndSubRelation.getCodeSub()) && unidadobra.getCodigo().contentEquals(estructuraPCWUnidad.getCodeUnidad())).findFirst().orElse(null);
    }

    private EspAndSubRelation getEspAndSubRelationUnidad(EstructuraPCWUnidad estructuraPCW) {
        return espAndSubRelations.parallelStream().distinct().filter(espAndSubRelation -> StringUtils.equalsIgnoreCase(espAndSubRelation.getCodEspPCW(), estructuraPCW.getEspecialidad()) && StringUtils.equalsIgnoreCase(espAndSubRelation.getCodSubPCW(), estructuraPCW.getCodSub())).findFirst().orElse(null);
    }

    private EspAndSubRelation getEspAndSubRelation(EstructuraPCW estructuraPCW) {
        return espAndSubRelations.parallelStream().distinct().filter(espAndSubRelation -> StringUtils.equalsIgnoreCase(espAndSubRelation.getCodEspPCW(), estructuraPCW.getEspecialidad()) && StringUtils.equalsIgnoreCase(espAndSubRelation.getCodSubPCW(), estructuraPCW.getCodSub())).findFirst().orElse(null);
    }

    private List<BajoEspecificacionPCW> getBajoToU(Unidadobra unidadobra) {
        sumBEficacionPCWArrayList = new ArrayList<>();
        for (Map.Entry<EstructuraPCWUnidad, List<BajoEspecificacionPCW>> items : bajoEstructuraPCWUnidadListMap.entrySet()) {
            espAndSub = getEspAndSubRelationUnidad(items.getKey());
            if (espAndSub != null) {
                Unidadobra unidad = checkEstructure(items.getKey(), espAndSub);
                if (unidad != null) {
                    if (unidadobra.getCodigo().contentEquals(unidad.getCodigo())) {
                        sumBEficacionPCWArrayList.addAll(items.getValue());
                    }
                }
            }
        }
        return sumBEficacionPCWArrayList;
    }

    private EmpresaToImportView getEmpresaToImportView(int idEmp, String code) {
        if (idEmp != 0 && code == null) {
            empresaToImportView = selectedEmpresa.parallelStream().filter(empresaTo -> empresaTo.getId() == idEmp).findFirst().orElse(null);
        } else if (idEmp == 0 && code != null) {
            empresaToImportView = selectedEmpresa.parallelStream().filter(empresaTo -> empresaTo.getSimil().equals(code)).findFirst().orElse(null);
        }

        return empresaToImportView;
    }

    private CalPrecSuminist getIsPresentInCalPreC(BajoEspecificacionPCW bajoEspecificacionPCW) {
        return componentesArrayList.parallelStream().filter(calPrecSuminist -> calPrecSuminist.getCodig().equals(bajoEspecificacionPCW.getSuminist())).findFirst().orElse(null);

    }


}

