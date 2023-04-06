package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DuplicarObraController implements Initializable {
    @FXML
    private JFXButton btnClose;

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
    private JFXTextField fielDescripcion;

    @FXML
    private JFXTextField codeEmp;

    @FXML
    private JFXTextField newDescrip;

    private ModelsImportViewSingelton importViewSingelton;
    private Obra obraToDuplicate;
    private ObrasController controller;
    private List<Unidadobra> unidadobraList;
    private List<Zonas> createdZonasList;
    private List<Objetos> createdObjetosList;
    private List<Nivel> createdNivelList;
    private int idZona;
    private List<Nivel> list = new ArrayList<>();
    private int inputLength;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        importViewSingelton = ModelsImportViewSingelton.getInstance();
    }

    public void cargarObra(ObrasController obrasController, Obra obra) {
        controller = obrasController;
        obraToDuplicate = obra;
        fieldobra.setText(obra.getCodigo());
        fielDescripcion.setText(obra.getDescripion());
    }

    public void duplicateObra(ActionEvent event) {
        if (fieldobra.getText().equals(codeEmp.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("No puede duplicar la obra con el mismo código");
            alert.showAndWait();
        } else {
            Obra newObra = new Obra();
            newObra.setCodigo(codeEmp.getText());
            newObra.setDescripion(newDescrip.getText());
            newObra.setTipo("UO");
            newObra.setEntidadId(obraToDuplicate.getEntidadId());
            newObra.setInversionistaId(obraToDuplicate.getInversionistaId());
            newObra.setTipoobraId(obraToDuplicate.getTipoobraId());
            newObra.setSalarioId(obraToDuplicate.getSalarioId());
            newObra.setTarifaId(obraToDuplicate.getTarifaId());
            newObra.setDefcostmat(0);

            Integer id = importViewSingelton.addNuevaObra(newObra);

            obraToDuplicate.getEmpresaobrasById().size();
            createEmprasaObra(obraToDuplicate.getEmpresaobrasById(), id);
            obraToDuplicate.getEmpresaobraconceptosById().size();
            createEmmpresaObraConceptos(obraToDuplicate.getEmpresaobraconceptosById(), id);
            obraToDuplicate.getCoeficientesequiposById().size();
            createEmpresaCoeficienteObra(obraToDuplicate.getCoeficientesequiposById(), id);

            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = obraToDuplicate.getEmpresaobrasById().stream().collect(Collectors.toList());
            for (Empresaobra empresaobra : empresaobraList) {
                addNewEmpresaObraTarifa(id, empresaobra.getEmpresaconstructoraId(), obraToDuplicate.getTarifaSalarialByTarifa());
            }


            obraToDuplicate.getZonasById().size();
            createZonas(obraToDuplicate.getZonasById(), id);

            createdZonasList = new ArrayList<>();
            createdZonasList = importViewSingelton.getZonasListFull(id);

            createdObjetosList = new ArrayList<>();
            createdObjetosList = importViewSingelton.getObjetosListFull(id);

            createdNivelList = new ArrayList<>();
            createdNivelList = importViewSingelton.getNivelesByObra(id);

            if (checkuo.isSelected()) {
                try {
                    obraToDuplicate.getUnidadobrasById().size();
                    createUnidadObra(obraToDuplicate.getUnidadobrasById(), id);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("OK!!");
                    alert.setContentText("La Obra: " + newObra.getDescripion() + " ha sido Duplicada ");
                    alert.showAndWait();

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error!!");
                    alert.setContentText("Error al duplicar la obra: " + newObra.getDescripion());
                    alert.showAndWait();
                }

            }


        }
    }

    private void addNewEmpresaObraTarifa(int idObra, int idEmpresa, TarifaSalarial tarifa) {
        List<Empresaobratarifa> empresaobratarifaList = new ArrayList<>();
        for (GruposEscalas gruposEscalas : tarifa.getGruposEscalasCollection()) {
            Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
            empresaobratarifa.setEmpresaconstructoraId(idEmpresa);
            empresaobratarifa.setObraId(idObra);
            empresaobratarifa.setTarifaId(tarifa.getId());
            empresaobratarifa.setGrupo(gruposEscalas.getGrupo());
            empresaobratarifa.setEscala(gruposEscalas.getValorBase());
            empresaobratarifa.setTarifa(gruposEscalas.getValor());
            //  empresaobratarifa.setTarifaInc(0.0);
            empresaobratarifaList.add(empresaobratarifa);
        }
        utilCalcSingelton.createEmpresaobratarifa(empresaobratarifaList);
    }


    private int getIdZona(String code) {
        return createdZonasList.parallelStream().filter(zonas -> zonas.getCodigo().trim().equals(code.trim())).findFirst().map(Zonas::getId).orElse(0);
    }

    private int getObjecto(String code, int idZona) {
        return createdObjetosList.parallelStream().filter(objetos -> objetos.getCodigo().trim().equals(code) && objetos.getZonasId() == idZona).findFirst().map(Objetos::getId).orElse(0);
    }

    private int getIdNiv(String code, int iOb) {
        return createdNivelList.parallelStream().filter(nivel -> nivel.getObjetosId() == iOb && nivel.getCodigo().equals(code)).findFirst().map(Nivel::getId).orElse(0);
    }

    private void createUnidadObra(Collection<Unidadobra> unidadobrasById, Integer id) {

        for (Unidadobra unidadobra : unidadobrasById) {
            Unidadobra newUnidad = new Unidadobra();
            newUnidad.setObraId(id);
            newUnidad.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
            int idZ = getIdZona(unidadobra.getZonasByZonasId().getCodigo());
            newUnidad.setZonasId(idZ);
            int idObj = getObjecto(unidadobra.getObjetosByObjetosId().getCodigo(), idZ);
            newUnidad.setObjetosId(idObj);
            int idNiv = getIdNiv(unidadobra.getNivelByNivelId().getCodigo().trim(), idObj);
            newUnidad.setNivelId(idNiv);
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

            Integer idU = importViewSingelton.createUnidadObra(newUnidad);


            List<Unidadobrarenglon> listUnidadObraRenglon = new ArrayList<>();
            if (checkrv.isSelected()) {
                List<Unidadobrarenglon> unidadobrarenglonList = new ArrayList<>();
                unidadobrarenglonList = importViewSingelton.getUnidadobrarenglonArrayList(unidadobra);
                listUnidadObraRenglon.addAll(createListUnidadRenglon(unidadobrarenglonList, idU));

            }
            importViewSingelton.createUnidadesObraRenglones(listUnidadObraRenglon);

            List<Bajoespecificacion> bajoespecificacions = new ArrayList<>();
            if (checksum.isSelected()) {
                List<Bajoespecificacion> listBajoespecif = new ArrayList<>();
                listBajoespecif = importViewSingelton.getBajoespecificacionList(unidadobra);
                bajoespecificacions.addAll(createBajoEspeciciosNew(listBajoespecif, idU));
            }
            importViewSingelton.createBajoespecificacion(bajoespecificacions);


            if (checkplan.isSelected()) {
                List<Planificacion> planificacions = importViewSingelton.getUnidaPlanificacions(unidadobra);
                for (Planificacion planificacion : planificacions) {
                    List<Planrecuo> planrecuoList = importViewSingelton.getPlanrecuoList(unidadobra, planificacion);
                    Integer idPlan = importViewSingelton.createPlanificacion(confortPlanificacion(idU, planificacion));

                    List<Planrecuo> listPlanrec = new ArrayList<>();
                    for (Planrecuo planrecuo : planrecuoList) {
                        listPlanrec.add(conformPlarecuo(idU, idPlan, planrecuo));
                    }
                    importViewSingelton.createPlanrecuo(listPlanrec);
                }
            }

            if (checkcertif.isSelected()) {
                List<Certificacion> certificacions = importViewSingelton.getUnidaCertificacions(unidadobra);
                for (Certificacion certificacion : certificacions) {
                    List<Certificacionrecuo> certificacionrecuoList = importViewSingelton.getCertificacionrecuoList(unidadobra, certificacion);
                    Integer idCert = importViewSingelton.createCertificacionUO(conformCertificacion(idU, certificacion));

                    List<Certificacionrecuo> listCertificacionrecuos = new ArrayList<>();
                    for (Certificacionrecuo certificacionrecuo : certificacionrecuoList) {
                        listCertificacionrecuos.add(conformCertificacionrecuo(idU, idCert, certificacionrecuo));
                    }
                    importViewSingelton.createCertifrecuo(listCertificacionrecuos);
                }
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
            bajoespecificacion1.setSumrenglon(1);
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

    private void createZonas(Collection<Zonas> zonasById, Integer id) {
        idZona = 0;
        for (Zonas zon : zonasById) {
            Zonas zonas = new Zonas();
            zonas.setObraId(id);
            zonas.setCodigo(zon.getCodigo());
            zonas.setDesripcion(zon.getDesripcion());

            idZona = importViewSingelton.createZona(zonas);
            createObjetosByZona(idZona, zon);

        }

    }

    private void createObjetosByZona(int idZona, Zonas zon) {

        List<Objetos> objetosList = importViewSingelton.getObjetosbyZona(zon.getId());
        for (Objetos items : objetosList) {
            Objetos objetos = new Objetos();
            objetos.setZonasId(idZona);
            objetos.setCodigo(items.getCodigo());
            objetos.setDescripcion(items.getDescripcion());

            int idObj = importViewSingelton.createObjetos(objetos);
            List<Nivel> nivelList = importViewSingelton.getNivelbyOb(items.getId());
            createNivelesByObjetos(nivelList, idObj);

        }

    }

    private void createNivelesByObjetos(List<Nivel> nivelList, int idObj) {
        list = new ArrayList<>();
        for (Nivel parNiv : nivelList) {
            Nivel nivel = new Nivel();
            nivel.setObjetosId(idObj);
            nivel.setCodigo(parNiv.getCodigo());
            nivel.setDescripcion(parNiv.getDescripcion());
            list.add(nivel);
        }
        importViewSingelton.createNiveles(list);
    }

    private void createEmpresaCoeficienteObra(Collection<Coeficientesequipos> coeficientesequiposById, Integer id) {
        List<Coeficientesequipos> coeficientesequipos = new ArrayList<>();
        for (Coeficientesequipos coeficiente : coeficientesequiposById) {
            Coeficientesequipos coeficienteseq = new Coeficientesequipos();
            coeficienteseq.setObraId(id);
            coeficienteseq.setEmpresaconstructoraId(coeficiente.getEmpresaconstructoraId());
            coeficienteseq.setRecursosId(coeficiente.getRecursosId());
            coeficienteseq.setCpo(coeficiente.getCpo());
            coeficienteseq.setCpe(coeficiente.getCpe());
            coeficienteseq.setCet(coeficiente.getCet());
            coeficienteseq.setOtra(coeficiente.getOtra());
            coeficienteseq.setSalario(coeficiente.getSalario());
            coeficientesequipos.add(coeficienteseq);
        }

        importViewSingelton.caddNuevoCoeficienteEquipo(coeficientesequipos);
    }

    private void createEmmpresaObraConceptos(Collection<Empresaobraconcepto> empresaobraconceptosById, int id) {
        List<Empresaobraconcepto> empresaobraconceptos = new ArrayList<>();
        for (Empresaobraconcepto empresaobraconcepto : empresaobraconceptosById) {
            Empresaobraconcepto eoc = new Empresaobraconcepto();
            eoc.setObraId(id);
            eoc.setConceptosgastoId(empresaobraconcepto.getConceptosgastoId());
            eoc.setEmpresaconstructoraId(empresaobraconcepto.getEmpresaconstructoraId());
            eoc.setValor(empresaobraconcepto.getValor());
            empresaobraconceptos.add(eoc);
        }
        importViewSingelton.caddNuevaEmpresObraConceptos(empresaobraconceptos);
    }

    private void createEmprasaObra(Collection<Empresaobra> empresaobrasById, Integer id) {
        List<Empresaobra> empresaobras = new ArrayList<>();
        for (Empresaobra empresaobra : empresaobrasById) {
            Empresaobra eo = new Empresaobra();
            eo.setObraId(id);
            eo.setEmpresaconstructoraId(empresaobra.getEmpresaconstructoraId());
            empresaobras.add(eo);
        }

        importViewSingelton.caddNuevaObraEmpresa(empresaobras);
    }

    public void keyTypeCode(KeyEvent event) {
        inputLength = 6;
        if (codeEmp.getText().length() == inputLength) {
            newDescrip.requestFocus();
        }
    }

}
