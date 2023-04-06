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

public class DuplicarNivelesController implements Initializable {

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
    private TableView<EspecialidadesView> tableObjetos;

    @FXML
    private TableColumn<EspecialidadesView, StringProperty> code;

    @FXML
    private TableColumn<EspecialidadesView, StringProperty> descrip;

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

    private Obra obraImp;
    private Empresaconstructora empresaImp;
    private Zonas zonaImp;
    private Objetos objetoImp;
    private Nivel nivetImp;

    private ModelsImportViewSingelton modelsImportViewSingelton;

    private List<ObraImpModel> obraImpModelList;
    private ObservableList<String> listObras;

    private List<EmpImpModel> empImpModelList;
    private ObservableList<String> listEmpresa;

    private List<ZonaImpModel> zonaImpModelList;
    private ObservableList<String> zoStringObservableList;

    private List<ObjetoImpModel> objetoImpModelList;
    private ObservableList<String> objetosStringObservableList;


    @FXML
    private JFXTextField zonasfield;

    @FXML
    private JFXTextField nivelCode;
    private ObraImpModel obraModel;
    private ZonaImpModel zonaImpModel;
    private NivelImpModel niv;
    private ObservableList<EspecialidadesView> nivelViewObservableList;
    private EmpImpModel empImpModel;
    private List<Unidadobra> unidadobraList;
    private List<Nivelespecifico> nivelespecificoList;
    private EmpImpModel impModel;
    private ObjetoImpModel objetoImpModel;
    private int idNivel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelsImportViewSingelton = ModelsImportViewSingelton.getInstance();

    }

    public void pasarDatos(Obra obra, Empresaconstructora empresaconstructora, Zonas zonas, Objetos objetos, Nivel nivel) {
        obraImp = obra;
        empresaImp = empresaconstructora;
        zonaImp = zonas;
        objetoImp = objetos;
        nivetImp = nivel;

        fieldobra.setText(obraImp.getCodigo() + " " + obraImp.getDescripion());
        fieldempresa.setText(empresaImp.getCodigo() + " " + empresaImp.getDescripcion());
        zonasfield.setText(zonaImp.getCodigo() + " " + zonaImp.getDesripcion());
        objetosField.setText(objetoImp.getCodigo() + " " + objetoImp.getDescripcion());

        nivelCode.setText(nivel.getCodigo() + " - " + nivel.getDescripcion());

        obraImpModelList = new ArrayList<>();
        if (obraImp.getTipo().equals("UO")) {
            obraImpModelList = modelsImportViewSingelton.getObrasList();
        } else if (obraImp.getTipo().equals("RV")) {
            obraImpModelList = modelsImportViewSingelton.getObrasListRV();
        }
        listObras = FXCollections.observableArrayList();
        listObras.setAll(obraImpModelList.parallelStream().map(obraImpModel -> obraImpModel.getCode()).collect(Collectors.toList()));
        comboObras.setItems(listObras);

        ActionEvent event = new ActionEvent();
        handleCargarNivel(event);
    }

    public void handleCargarEmpresa(ActionEvent event) {
        empImpModelList = new ArrayList<>();
        obraModel = obraImpModelList.parallelStream().filter(obraImpModel -> obraImpModel.getCode().trim().equals(comboObras.getValue().trim())).findFirst().orElse(null);

        empImpModelList = modelsImportViewSingelton.getEmpresaList(obraModel.getId());
        listEmpresa = FXCollections.observableArrayList();
        listEmpresa.setAll(empImpModelList.parallelStream().map(empImpModel -> empImpModel.getCode()).collect(Collectors.toList()));
        comboEmpresa.setItems(listEmpresa);

    }

    public void handleCargarZonas(ActionEvent event) {
        zonaImpModelList = new ArrayList<>();
        zonaImpModelList = modelsImportViewSingelton.getZonasList(obraModel.getId());
        zoStringObservableList = FXCollections.observableArrayList();
        zoStringObservableList.setAll(zonaImpModelList.parallelStream().map(zonaImpModel1 -> zonaImpModel1.getCode()).collect(Collectors.toList()));
        comboZonas.setItems(zoStringObservableList);
    }

    public void handleCargarObjetos(ActionEvent event) {
        objetoImpModelList = new ArrayList<>();
        zonaImpModel = zonaImpModelList.parallelStream().filter(zonaImpModel1 -> zonaImpModel1.getCode().equals(comboZonas.getValue())).findFirst().orElse(null);
        objetoImpModelList = modelsImportViewSingelton.getObjetosList(zonaImpModel.getId());

        objetosStringObservableList = FXCollections.observableArrayList();
        objetosStringObservableList.setAll(objetoImpModelList.parallelStream().map(objetoImpModel -> objetoImpModel.getCode()).collect(Collectors.toList()));
        comboObjetos.setItems(objetosStringObservableList);
    }

    public void handleCargarNivel(ActionEvent event) {
        code.setCellValueFactory(new PropertyValueFactory<EspecialidadesView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<EspecialidadesView, StringProperty>("descripcion"));
        nivelViewObservableList = FXCollections.observableArrayList();
        nivelViewObservableList = modelsImportViewSingelton.getEspecialidadView(obraImp.getId(), empresaImp.getId(), zonaImp.getId(), objetoImp.getId(), nivetImp.getId(), obraImp.getTipo().trim());

        tableObjetos.getItems().setAll(nivelViewObservableList);
    }

    private NivelImpModel getNivel(String code) {
        return modelsImportViewSingelton.getNivelList(objetoImp.getId()).parallelStream().filter(zonas -> zonas.getCode().substring(0, 2).equals(code)).findFirst().orElse(new NivelImpModel(0, "NULL"));
    }

    public void handleImportarComponentes(ActionEvent event) {
        if (obraImp.getTipo().equals("UO")) {
            String[] nivFiel = nivelCode.getText().split(" - ");
            if (getNivel(nivFiel[0].trim()).getCode().equals("NULL")) {
                Nivel nivel = new Nivel();
                nivel.setCodigo(nivFiel[0].trim());
                nivel.setDescripcion(nivFiel[1].trim());
                nivel.setObjetosId(objetoImp.getId());

                idNivel = modelsImportViewSingelton.createNivel(nivel);

                if (checkuo.isSelected()) {
                    impModel = empImpModelList.parallelStream().filter(empImpModel -> empImpModel.getCode().trim().equals(comboEmpresa.getValue().trim())).findFirst().orElse(null);
                    unidadobraList = new ArrayList<>();
                    unidadobraList = modelsImportViewSingelton.getUnidadobraByNivelesList(obraModel.getId(), impModel.getId(), zonaImpModel.getId(), objetoImp.getId(), nivetImp.getId());
                    unidadobraList.size();
                    try {
                        crateUnidadObraAndEstructura(unidadobraList);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("OK!!");
                        alert.setContentText("El Nivel: " + nivFiel[0].trim() + " ha sido importado ");
                        alert.showAndWait();

                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error!!");
                        alert.setContentText("Error al importar el nivel: " + nivFiel[0].trim());
                        alert.showAndWait();
                    }
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Cuidado!!");
                alert.setContentText(" El nivel " + nivFiel[0].trim() + " esta declarado en la obra " + obraImp.getDescripion());
                alert.showAndWait();
            }
        } else if (obraImp.getTipo().equals("RV")) {
            String[] nivFiel = nivelCode.getText().split(" - ");
            if (getNivel(zonasfield.getText().trim()).getCode().equals("NULL")) {
                Nivel nivel = new Nivel();
                nivel.setCodigo(nivFiel[0].trim());
                nivel.setDescripcion(nivFiel[1].trim());
                nivel.setObjetosId(objetoImp.getId());

                idNivel = modelsImportViewSingelton.createNivel(nivel);

                if (checkuo.isSelected()) {
                    impModel = empImpModelList.parallelStream().filter(empImpModel -> empImpModel.getCode().trim().equals(comboEmpresa.getValue().trim())).findFirst().orElse(null);
                    nivelespecificoList = new ArrayList<>();
                    nivelespecificoList = modelsImportViewSingelton.getNivelEspecificoByNivelesList(obraModel.getId(), impModel.getId(), zonaImpModel.getId(), objetoImp.getId(), nivetImp.getId());
                    nivelespecificoList.size();
                    try {
                        crateNivelEspecificoAndEstructura(nivelespecificoList);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("OK!!");
                        alert.setContentText("El Nivel: " + nivFiel[0].trim() + " ha sido importado ");
                        alert.showAndWait();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Error!!");
                        alert.setContentText("Error al importar el nivel: " + nivFiel[0].trim());
                        alert.showAndWait();
                    }
                }


            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Cuidado!!");
                alert.setContentText(" El nivel " + nivFiel[0].trim() + " esta declarado en la obra " + obraImp.getDescripion());
                alert.showAndWait();
            }
        }

    }

    private void crateNivelEspecificoAndEstructura(List<Nivelespecifico> nivelespecificoList) {

        for (Nivelespecifico nivelespecifico : nivelespecificoList) {
            buildNIvelEspecificoEstructure(nivelespecifico);
        }

    }

    private void buildNIvelEspecificoEstructure(Nivelespecifico nivelespecifico) {
        Nivelespecifico nivel = new Nivelespecifico();
        nivel.setObraId(obraImp.getId());
        nivel.setEmpresaconstructoraId(empresaImp.getId());
        nivel.setZonasId(zonaImp.getId());
        nivel.setObjetosId(objetoImp.getId());
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
        newUnidad.setObraId(obraImp.getId());
        newUnidad.setEmpresaconstructoraId(empresaImp.getId());
        newUnidad.setZonasId(zonaImp.getId());
        newUnidad.setObjetosId(objetoImp.getId());
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
        newUnidad.setSalariocuc(unidadobra.getSalariocuc());
        newUnidad.setGrupoejecucionId(unidadobra.getGrupoejecucionId());
        newUnidad.setIdResolucion(unidadobra.getIdResolucion());

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
