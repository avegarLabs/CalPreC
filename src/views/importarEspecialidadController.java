package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class importarEspecialidadController implements Initializable {

    @FXML
    private JFXComboBox<String> comboObras;

    @FXML
    private JFXComboBox<String> comboEmpresa;

    @FXML
    private JFXComboBox<String> comboZonas;

    @FXML
    private JFXComboBox<String> comboObjetos;

    @FXML
    private JFXTextField objetosField;


    @FXML
    private JFXButton btnClose;

    @FXML
    private TableView<SubDuplicateModel> tableObjetos;

    @FXML
    private TableColumn<SubDuplicateModel, JFXCheckBox> code;

    @FXML
    private TableColumn<SubDuplicateModel, StringProperty> descrip;

    @FXML
    private JFXCheckBox checkuo;

    @FXML
    private JFXCheckBox checkrv;

    @FXML
    private JFXCheckBox checksum;

    @FXML
    private JFXCheckBox checkplan;

    @FXML
    private JFXCheckBox checkcertif;

    @FXML
    private JFXTextField fieldobra;

    @FXML
    private JFXTextField fieldempresa;

    @FXML
    private JFXTextField espCode;

    private Obra obraImp;
    private Empresaconstructora empresaImp;
    private Zonas zonaImp;
    private Objetos objetoImp;
    private Nivel nivetImp;
    private Especialidades especialImp;

    private ModelsImportViewSingelton modelsImportViewSingelton;

    private List<ObraImpModel> obraImpModelList;
    private ObservableList<String> listObras;

    private List<EmpImpModel> empImpModelList;
    private ObservableList<String> listEmpresa;

    private List<ZonaImpModel> zonaImpModelList;
    private ObservableList<String> zoStringObservableList;

    private List<ObjetoImpModel> objetoImpModelList;
    private ObservableList<String> objetosStringObservableList;

    private List<NivelImpModel> nivelImpModelList;
    private ObservableList<String> nivelStringObservableList;

    @FXML
    private JFXComboBox<String> comboNivel;

    @FXML
    private JFXComboBox<String> comboEspecialidad;

    @FXML
    private JFXTextField zonasfield;

    @FXML
    private JFXTextField nivelCode;
    private ObraImpModel obraModel;
    private ZonaImpModel zonaImpModel;
    private NivelImpModel niv;
    private ObservableList<SubDuplicateModel> nivelViewObservableList;
    private EmpImpModel empImpModel;
    private List<Unidadobra> unidadobraList;
    private List<Nivelespecifico> nivelespecificoList;
    private ObjetoImpModel objetoImpModel;
    private EspecialidadImpModel especialidadImpModel;
    private List<SubespecialidadImpModel> subespecialidadImpModels;
    private int idNivel;
    private int idObra;
    private int idObjeto;
    private int idZona;
    private int idEspecialidad;
    private int idEmpresa;

    private ObservableList<String> especialidadesObservableList;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelsImportViewSingelton = ModelsImportViewSingelton.getInstance();

    }

    public void pasarDatos(Obra obra, Empresaconstructora empresaconstructora, Zonas zonas, Objetos objetos, Nivel nivel) {
        obraModel = new ObraImpModel(obra.getId(), obra.getCodigo());
        //obraImp = obra;
        empImpModel = new EmpImpModel(empresaconstructora.getId(), empresaconstructora.getCodigo());
        //empresaImp = empresaconstructora;
        zonaImpModel = new ZonaImpModel(zonas.getId(), zonas.getCodigo());
        //zonaImp = zonas;
        objetoImpModel = new ObjetoImpModel(objetos.getId(), objetos.getCodigo());
        //objetoImp = objetos;
        niv = new NivelImpModel(nivel.getId(), nivel.getCodigo());
        // nivetImp = nivel;


        fieldobra.setText(obra.getCodigo() + " " + obra.getDescripion());
        fieldempresa.setText(empresaconstructora.getCodigo() + " " + empresaconstructora.getDescripcion());
        zonasfield.setText(zonas.getCodigo() + " " + zonas.getDesripcion());
        objetosField.setText(objetos.getCodigo() + " " + objetos.getDescripcion());
        nivelCode.setText(nivel.getCodigo() + " - " + nivel.getDescripcion());

        obraImpModelList = new ArrayList<>();
        if (obra.getTipo().equals("UO")) {
            listObras = FXCollections.observableArrayList();
            listObras.addAll(reportProjectStructureSingelton.getObrasList());
            comboObras.setItems(listObras);
        } else if (obra.getTipo().equals("RV")) {
            listObras = FXCollections.observableArrayList();
            listObras.addAll(reportProjectStructureSingelton.getObrasListRV());
            comboObras.setItems(listObras);
        }
        especialidadesObservableList = FXCollections.observableArrayList();
        especialidadesObservableList.addAll(utilCalcSingelton.getAllEspecialidades().stream().map(Especialidades::toString).collect(Collectors.toList()));
        comboEspecialidad.setItems(especialidadesObservableList);

    }

    public void handleCargarEmpresa(ActionEvent event) {
        obraImp = reportProjectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue().trim().trim())).findFirst().get();

        listEmpresa = FXCollections.observableArrayList();
        listEmpresa.setAll(reportProjectStructureSingelton.getEmpresaList(obraImp.getId()));
        comboEmpresa.setItems(listEmpresa);
    }

    public void handleCargarZonas(ActionEvent event) {
        empresaImp = reportProjectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresa.getValue().trim())).findFirst().get();
        zoStringObservableList = FXCollections.observableArrayList();
        zoStringObservableList.addAll(reportProjectStructureSingelton.getZonasList(obraImp.getId()));
        comboZonas.setItems(zoStringObservableList);
    }

    public void handleCargarObjetos(ActionEvent event) {
        zonaImp = reportProjectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue())).findFirst().get();
        objetosStringObservableList = FXCollections.observableArrayList();
        objetosStringObservableList.setAll(reportProjectStructureSingelton.getObjetosList(zonaImp.getId()));
        comboObjetos.setItems(objetosStringObservableList);
    }

    public void handleCargarNivel(ActionEvent event) {
        objetoImp = reportProjectStructureSingelton.objetosArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObjetos.getValue())).findFirst().get();
        nivelStringObservableList = FXCollections.observableArrayList();
        nivelStringObservableList.setAll(reportProjectStructureSingelton.getNivelList(objetoImp.getId()));
        comboNivel.setItems(nivelStringObservableList);
    }

    public void cargarUO(ActionEvent event) {
        if (obraImp.getTipo().equals("UO")) {
            unidadobraList = new ArrayList<>();
            unidadobraList = modelsImportViewSingelton.getUnidadobraByEspecialidadList(obraImp.getId(), empresaImp.getId(), zonaImp.getId(), objetoImp.getId(), nivetImp.getId(), especialImp.getId());
        } else if (obraImp.getTipo().equals("RV")) {
            nivelespecificoList = new ArrayList<>();
            nivelespecificoList = modelsImportViewSingelton.getNivelEspecificoByEspecialidadList(obraImp.getId(), empresaImp.getId(), zonaImp.getId(), objetoImp.getId(), nivetImp.getId(), especialImp.getId());
        }
    }

    public void handleCargarSub(ActionEvent event) {
        especialImp = utilCalcSingelton.especialidadesList.parallelStream().filter(especialidades -> especialidades.toString().trim().equals(comboEspecialidad.getValue())).findFirst().get();
        nivetImp = reportProjectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue())).findFirst().get();
        code.setCellValueFactory(new PropertyValueFactory<SubDuplicateModel, JFXCheckBox>("datosBox"));
        descrip.setCellValueFactory(new PropertyValueFactory<SubDuplicateModel, StringProperty>("descripcion"));
        nivelViewObservableList = FXCollections.observableArrayList();
        nivelViewObservableList = modelsImportViewSingelton.getSubEspView(obraImp.getId(), empresaImp.getId(), zonaImp.getId(), objetoImp.getId(), nivetImp.getId(), especialImp.getId());
        tableObjetos.getItems().setAll(nivelViewObservableList);
        cargarUO(event);

    }

    private List<Unidadobra> filterUnidadObra(int idEs, int idSub) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getEspecialidadesId() == idEs && unidadobra.getSubespecialidadesId() == idSub).collect(Collectors.toList());
    }

    private List<Nivelespecifico> filterNivelEspeci(int idEs, int idSub) {
        return nivelespecificoList.parallelStream().filter(unidadobra -> unidadobra.getEspecialidadesId() == idEs && unidadobra.getSubespecialidadesId() == idSub).collect(Collectors.toList());
    }

    public void handleImportarComponentes(ActionEvent event) {
        idNivel = niv.getId();
        List<SubDuplicateModel> selectedItems = tableObjetos.getItems().parallelStream().filter(subDuplicateModel -> subDuplicateModel.getDatosBox().isSelected()).collect(Collectors.toList());
        if (obraImp.getTipo().equals("UO")) {
            try {
                for (SubDuplicateModel subDuplicateModel : selectedItems) {
                    if (modelsImportViewSingelton.getUnidadesToCheck(obraModel.getId(), empImpModel.getId(), zonaImpModel.getId(), objetoImpModel.getId(), idNivel, especialImp.getId(), subDuplicateModel.getIntegerProperty()).size() == 0) {
                        crateUnidadObraAndEstructura(filterUnidadObra(especialImp.getId(), subDuplicateModel.getIntegerProperty()));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Cuidado!!");
                        alert.setContentText(" La subespecialidad " + subDuplicateModel.getDescripcion() + " ya contiene datos en  " + obraModel.getCode());
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error!!");
                alert.setContentText("Error al duplicar los datos");
                alert.showAndWait();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("OK!!");
            alert.setContentText(" Importación Treminada ");
            alert.showAndWait();

        } else if (obraImp.getTipo().equals("RV")) {
            try {
                for (SubDuplicateModel subDuplicateModel : tableObjetos.getItems().parallelStream().filter(subDuplicateModel -> subDuplicateModel.getDatosBox().isSelected()).collect(Collectors.toList())) {
                    if (modelsImportViewSingelton.getNivelToCheck(obraModel.getId(), empImpModel.getId(), zonaImpModel.getId(), objetoImpModel.getId(), idNivel, especialImp.getId(), subDuplicateModel.getIntegerProperty()).size() == 0) {
                        crateNivelEspecificoAndEstructura(filterNivelEspeci(especialImp.getId(), subDuplicateModel.getIntegerProperty()));
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Cuidado!!");
                        alert.setContentText(" La subespecialidad " + subDuplicateModel.getDescripcion() + " ya contiene datos en  " + obraImp.getDescripion());
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error!!");
                alert.setContentText("Error al duplicar los datos");
                alert.showAndWait();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("OK!!");
            alert.setContentText(" Importación Treminada ");
            alert.showAndWait();
        }
    }


    private void crateNivelEspecificoAndEstructura(List<Nivelespecifico> nivelespecificoList) {

        for (Nivelespecifico nivelespecifico : nivelespecificoList) {
            buildNIvelEspecificoEstructure(nivelespecifico);
        }

    }

    private void buildNIvelEspecificoEstructure(Nivelespecifico nivelespecifico) {
        Nivelespecifico nivel = new Nivelespecifico();
        nivel.setObraId(obraModel.getId());
        nivel.setEmpresaconstructoraId(empImpModel.getId());
        nivel.setZonasId(zonaImpModel.getId());
        nivel.setObjetosId(objetoImpModel.getId());
        nivel.setNivelId(idNivel);
        nivel.setEspecialidadesId(nivelespecifico.getEspecialidadesId());
        nivel.setSubespecialidadesId(nivelespecifico.getSubespecialidadesId());
        nivel.setCodigo(nivelespecifico.getCodigo());
        nivel.setDescripcion(nivelespecifico.getDescripcion());
        nivel.setCostoequipo(nivelespecifico.getCostoequipo());
        nivel.setCostomano(nivelespecifico.getCostomano());
        nivel.setCostomaterial(nivelespecifico.getCostomaterial());
        nivel.setSalario(nivelespecifico.getSalario());

        Integer idN = modelsImportViewSingelton.createNivelEspecifico(nivel);

        List<Renglonnivelespecifico> listRenglonnivelespecificoList = new ArrayList<>();
        if (checkrv.isSelected()) {
            List<Renglonnivelespecifico> renglonnivelespecificoList = new ArrayList<>();
            renglonnivelespecificoList = modelsImportViewSingelton.getRenglonNiRenglonnivelespecificoList(nivelespecifico);
            listRenglonnivelespecificoList.addAll(createListRenglonnivelespecificoList(renglonnivelespecificoList, idN));

        }
        modelsImportViewSingelton.createRenglonnivelEspecificoList(listRenglonnivelespecificoList);


        List<Bajoespecificacionrv> bajoespecificacionsBajoespecificacionrvs = new ArrayList<>();
        if (checksum.isSelected()) {
            List<Bajoespecificacionrv> listBajoespecif = new ArrayList<>();
            listBajoespecif = modelsImportViewSingelton.getBajoespecificacionListBajoespecificacionrvs(nivelespecifico);
            bajoespecificacionsBajoespecificacionrvs.addAll(createBajoEspeciciBajoespecificacionrvs(listBajoespecif, idN));
        }
        modelsImportViewSingelton.createBajoespecificacionrvList(bajoespecificacionsBajoespecificacionrvs);


        if (checkplan.isSelected()) {
            List<Planrenglonvariante> planificacions = modelsImportViewSingelton.getPlanrenglonvarianteList(nivelespecifico);
            for (Planrenglonvariante planificacion : planificacions) {
                List<Planrecrv> planrecuoList = modelsImportViewSingelton.getPlanrecrvList(nivelespecifico, planificacion);
                Integer idPlan = modelsImportViewSingelton.createPlanificacionRV(confortPlanrenglonvariante(idN, planificacion));

                List<Planrecrv> listPlanrec = new ArrayList<>();
                for (Planrecrv planrecuo : planrecuoList) {
                    listPlanrec.add(conformPlanrecrv(idN, idPlan, planrecuo));
                }
                modelsImportViewSingelton.createPlanrecrv(listPlanrec);
            }
        }

        if (checkcertif.isSelected()) {
            List<CertificacionRenglonVariante> certificacions = modelsImportViewSingelton.getCertificacionRenglonVarianteList(nivelespecifico);
            for (CertificacionRenglonVariante certificacion : certificacions) {
                List<Certificacionrecrv> certificacionrecuoList = modelsImportViewSingelton.getCertificacionrecrvList(nivelespecifico, certificacion);
                Integer idCert = modelsImportViewSingelton.createCertificacionRV(conformCertificacionRenglonVariante(idN, certificacion));

                List<Certificacionrecrv> listCertificacionrecuos = new ArrayList<>();
                for (Certificacionrecrv certificacionrecrv : certificacionrecuoList) {
                    listCertificacionrecuos.add(conformCertificacionrecuoRV(idN, idCert, certificacionrecrv));
                }
                modelsImportViewSingelton.createCertificacionRV(listCertificacionrecuos);
            }
        }

    }

    private List<Renglonnivelespecifico> createListRenglonnivelespecificoList(List<Renglonnivelespecifico> unidadobrarenglonList, Integer idN) {
        List<Renglonnivelespecifico> list = new ArrayList<>();
        for (Renglonnivelespecifico listRenglone : unidadobrarenglonList) {
            Renglonnivelespecifico unidadobrarenglon = new Renglonnivelespecifico();
            unidadobrarenglon.setNivelespecificoId(idN);
            unidadobrarenglon.setRenglonvarianteId(listRenglone.getRenglonvarianteId());
            unidadobrarenglon.setCantidad(listRenglone.getCantidad());
            unidadobrarenglon.setCostmaterial(listRenglone.getCostmaterial());
            unidadobrarenglon.setCostmano(listRenglone.getCostmano());
            unidadobrarenglon.setCostequipo(listRenglone.getCostequipo());
            unidadobrarenglon.setConmat(listRenglone.getConmat());
            unidadobrarenglon.setSalario(listRenglone.getSalario());
            unidadobrarenglon.setSalariocuc(listRenglone.getSalariocuc());
            list.add(unidadobrarenglon);
        }
        return list;
    }

    private List<Bajoespecificacionrv> createBajoEspeciciBajoespecificacionrvs(List<Bajoespecificacionrv> listBajoespecif, Integer idU) {
        List<Bajoespecificacionrv> bajoespecificacionList = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacion : listBajoespecif) {
            Bajoespecificacionrv bajoespecificacion1 = new Bajoespecificacionrv();
            bajoespecificacion1.setNivelespecificoId(idU);
            bajoespecificacion1.setRenglonvarianteId(bajoespecificacion.getRenglonvarianteId());
            bajoespecificacion1.setCantidad(bajoespecificacion.getCantidad());
            bajoespecificacion1.setCosto(bajoespecificacion.getCosto());
            bajoespecificacion1.setTipo(bajoespecificacion.getTipo());
            bajoespecificacion1.setIdsuministro(bajoespecificacion.getIdsuministro());
            bajoespecificacionList.add(bajoespecificacion1);
        }
        return bajoespecificacionList;
    }

    private Planrecrv conformPlanrecrv(Integer idN, Integer idPlan, Planrecrv planrecuo) {
        Planrecrv planrecuo1 = new Planrecrv();
        planrecuo1.setNivelespId(idN);
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

    private Planrenglonvariante confortPlanrenglonvariante(int idN, Planrenglonvariante planificacion) {
        Planrenglonvariante planificacion1 = new Planrenglonvariante();
        planificacion1.setNivelespecificoId(idN);
        planificacion1.setRenglonvarianteId(planificacion.getRenglonvarianteId());
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostomaterial(planificacion.getCostomaterial());
        planificacion1.setCostomano(planificacion.getCostomano());
        planificacion1.setCostoequipo(planificacion.getCostoequipo());
        planificacion1.setSalario(planificacion.getSalario());
        planificacion1.setSalariocuc(planificacion.getSalariocuc());
        return planificacion1;
    }

    private CertificacionRenglonVariante conformCertificacionRenglonVariante(Integer idN, CertificacionRenglonVariante planificacion) {
        CertificacionRenglonVariante planificacion1 = new CertificacionRenglonVariante();
        planificacion1.setNivelespecificoId(idN);
        planificacion1.setRenglonvarianteId(planificacion.getRenglonvarianteId());
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostomaterial(planificacion.getCostomaterial());
        planificacion1.setCostomano(planificacion.getCostomano());
        planificacion1.setCostoequipo(planificacion.getCostoequipo());
        planificacion1.setSalario(planificacion.getSalario());
        planificacion1.setSalariocuc(planificacion.getSalariocuc());
        return planificacion1;
    }

    private Certificacionrecrv conformCertificacionrecuoRV(Integer idUni, Integer idPlan, Certificacionrecrv planrecuo) {
        Certificacionrecrv planrecuo1 = new Certificacionrecrv();
        planrecuo1.setNivelespId(idUni);
        planrecuo1.setRenglonId(planrecuo.getRenglonId());
        planrecuo1.setRecursoId(planrecuo.getRecursoId());
        planrecuo1.setCantidad(planrecuo.getCantidad());
        planrecuo1.setCosto(planrecuo.getCosto());
        planrecuo1.setCertificacionId(idPlan);
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


    private void crateUnidadObraAndEstructura(List<Unidadobra> unidadobraList) {
        for (Unidadobra unidadobra : unidadobraList) {
            buildUnidadObraEstructure(unidadobra);
        }

    }

    private void buildUnidadObraEstructure(Unidadobra unidadobra) {

        Unidadobra newUnidad = new Unidadobra();
        newUnidad.setObraId(obraModel.getId());
        newUnidad.setEmpresaconstructoraId(empImpModel.getId());
        newUnidad.setZonasId(zonaImpModel.getId());
        newUnidad.setObjetosId(objetoImpModel.getId());
        newUnidad.setNivelId(idNivel);
        newUnidad.setEspecialidadesId(unidadobra.getEspecialidadesId());
        newUnidad.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
        newUnidad.setCodigo(unidadobra.getCodigo());
        newUnidad.setDescripcion(unidadobra.getDescripcion());
        newUnidad.setUm(unidadobra.getUm());
        newUnidad.setCantidad(unidadobra.getCantidad());
        newUnidad.setCostototal(unidadobra.getCostototal());
        newUnidad.setCostoequipo(unidadobra.getCostoequipo());
        newUnidad.setCostomano(unidadobra.getCostomano());
        newUnidad.setCostoMaterial(unidadobra.getCostoMaterial());
        newUnidad.setCostounitario(unidadobra.getCostounitario());
        newUnidad.setRenglonbase(unidadobra.getRenglonbase());
        newUnidad.setSalario(unidadobra.getSalario());
        newUnidad.setSalariocuc(0.0);
        newUnidad.setIdResolucion(obraImp.getSalarioId());
        newUnidad.setGrupoejecucionId(1);

        Integer idU = modelsImportViewSingelton.createUnidadObra(newUnidad);


        List<Unidadobrarenglon> listUnidadObraRenglon = new ArrayList<>();
        if (checkrv.isSelected()) {
            List<Unidadobrarenglon> unidadobrarenglonList = new ArrayList<>();
            unidadobrarenglonList = modelsImportViewSingelton.getUnidadobrarenglonArrayList(unidadobra);
            listUnidadObraRenglon.addAll(createListUnidadRenglon(unidadobrarenglonList, idU));

        }
        modelsImportViewSingelton.createUnidadesObraRenglones(listUnidadObraRenglon);

        List<Bajoespecificacion> bajoespecificacions = new ArrayList<>();
        if (checksum.isSelected()) {
            List<Bajoespecificacion> listBajoespecif = new ArrayList<>();
            listBajoespecif = modelsImportViewSingelton.getBajoespecificacionList(unidadobra);
            bajoespecificacions.addAll(createBajoEspeciciosNew(listBajoespecif, idU));
        }
        modelsImportViewSingelton.createBajoespecificacion(bajoespecificacions);


        if (checkplan.isSelected()) {
            List<Planificacion> planificacions = modelsImportViewSingelton.getUnidaPlanificacions(unidadobra);
            for (Planificacion planificacion : planificacions) {
                List<Planrecuo> planrecuoList = modelsImportViewSingelton.getPlanrecuoList(unidadobra, planificacion);
                Integer idPlan = modelsImportViewSingelton.createPlanificacion(confortPlanificacion(idU, planificacion));

                List<Planrecuo> listPlanrec = new ArrayList<>();
                for (Planrecuo planrecuo : planrecuoList) {
                    listPlanrec.add(conformPlarecuo(idU, idPlan, planrecuo));
                }
                modelsImportViewSingelton.createPlanrecuo(listPlanrec);
            }
        }

        if (checkcertif.isSelected()) {
            List<Certificacion> certificacions = modelsImportViewSingelton.getUnidaCertificacions(unidadobra);
            for (Certificacion certificacion : certificacions) {
                List<Certificacionrecuo> certificacionrecuoList = modelsImportViewSingelton.getCertificacionrecuoList(unidadobra, certificacion);
                Integer idCert = modelsImportViewSingelton.createCertificacionUO(conformCertificacion(idU, certificacion));

                List<Certificacionrecuo> listCertificacionrecuos = new ArrayList<>();
                for (Certificacionrecuo certificacionrecuo : certificacionrecuoList) {
                    listCertificacionrecuos.add(conformCertificacionrecuo(idU, idCert, certificacionrecuo));
                }
                modelsImportViewSingelton.createCertifrecuo(listCertificacionrecuos);
            }
        }


    }

    private List<Bajoespecificacion> createBajoEspeciciosNew(List<Bajoespecificacion> listBajoespecif, Integer idU) {
        List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : listBajoespecif) {
            Bajoespecificacion bajoespecificacion1 = new Bajoespecificacion();
            bajoespecificacion1.setUnidadobraId(idU);
            bajoespecificacion1.setRenglonvarianteId(0);
            bajoespecificacion1.setCantidad(bajoespecificacion.getCantidad());
            bajoespecificacion1.setCosto(bajoespecificacion.getCosto());
            bajoespecificacion1.setTipo(bajoespecificacion.getTipo());
            bajoespecificacion1.setSumrenglon(0);
            bajoespecificacion1.setIdSuministro(bajoespecificacion.getIdSuministro());
            bajoespecificacionList.add(bajoespecificacion1);
        }
        return bajoespecificacionList;
    }

    private List<Unidadobrarenglon> createListUnidadRenglon(List<Unidadobrarenglon> unidadobrarenglonList, Integer idU) {
        List<Unidadobrarenglon> list = new ArrayList<>();
        for (Unidadobrarenglon listRenglone : unidadobrarenglonList) {
            Unidadobrarenglon unidadobrarenglon = new Unidadobrarenglon();
            unidadobrarenglon.setUnidadobraId(idU);
            unidadobrarenglon.setUnidadobraId(idU);
            unidadobrarenglon.setRenglonvarianteId(listRenglone.getRenglonvarianteId());
            unidadobrarenglon.setRenglonvarianteId(listRenglone.getRenglonvarianteId());
            unidadobrarenglon.setCantRv(listRenglone.getCantRv());
            unidadobrarenglon.setCostMat(listRenglone.getCostMat());
            unidadobrarenglon.setCostMano(listRenglone.getCostMano());
            unidadobrarenglon.setCostEquip(listRenglone.getCostEquip());
            unidadobrarenglon.setConMat(listRenglone.getConMat());
            unidadobrarenglon.setSalariomn(listRenglone.getSalariomn());
            unidadobrarenglon.setSalariocuc(listRenglone.getSalariocuc());
            list.add(unidadobrarenglon);
        }
        return list;
    }

    private Planificacion confortPlanificacion(int idUni, Planificacion planificacion) {

        Planificacion planificacion1 = new Planificacion();
        planificacion1.setUnidadobraId(idUni);
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostomaterial(planificacion.getCostomaterial());
        planificacion1.setCostomano(planificacion.getCostomano());
        planificacion1.setCostoequipo(planificacion.getCostoequipo());
        planificacion1.setCostosalario(planificacion.getCostosalario());
        planificacion1.setCostsalariocuc(planificacion.getCostsalariocuc());
        return planificacion1;
    }

    private Planrecuo conformPlarecuo(Integer idUni, Integer idPlan, Planrecuo planrecuo) {
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

    private Certificacionrecuo conformCertificacionrecuo(Integer idUni, Integer idPlan, Certificacionrecuo planrecuo) {
        Certificacionrecuo planrecuo1 = new Certificacionrecuo();
        planrecuo1.setUnidadobraId(idUni);
        planrecuo1.setRenglonId(planrecuo.getRenglonId());
        planrecuo1.setRecursoId(planrecuo.getRecursoId());
        planrecuo1.setCantidad(planrecuo.getCantidad());
        planrecuo1.setCosto(planrecuo.getCosto());
        planrecuo1.setCertificacionId(idPlan);
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

    private Certificacion conformCertificacion(Integer idUni, Certificacion planificacion) {
        Certificacion planificacion1 = new Certificacion();
        planificacion1.setUnidadobraId(idUni);
        planificacion1.setBrigadaconstruccionId(planificacion.getBrigadaconstruccionId());
        planificacion1.setGrupoconstruccionId(planificacion.getGrupoconstruccionId());
        planificacion1.setCuadrillaconstruccionId(planificacion.getCuadrillaconstruccionId());
        planificacion1.setDesde(planificacion.getDesde());
        planificacion1.setHasta(planificacion.getHasta());
        planificacion1.setCantidad(planificacion.getCantidad());
        planificacion1.setCostmaterial(planificacion.getCostmaterial());
        planificacion1.setCostmano(planificacion.getCostmano());
        planificacion1.setCostequipo(planificacion.getCostequipo());
        planificacion1.setSalario(planificacion.getSalario());
        planificacion1.setCucsalario(planificacion.getCucsalario());
        return planificacion1;
    }
}
