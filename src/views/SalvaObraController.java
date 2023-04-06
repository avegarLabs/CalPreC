package views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SalvaObraController implements Initializable {
    private static List<objectSelect> selectList;
    @FXML
    private TableView<ExportarObra> dataTable;
    @FXML
    private TableColumn<ExportarObra, JFXCheckBox> code;
    @FXML
    private TableColumn<ExportarObra, String> descrip;
    @FXML
    private TableColumn<ExportarObra, String> um;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField fildSearch;
    private ObservableList<ExportarObra> exportarObraObservableList;
    private List<Certificacionrecuo> listCertRecUO;
    private List<Certificacionrecrv> listCertRecRenglon;
    private List<Planrecuo> listPlanRevUO;
    private List<Planrecrv> listPlanRevRenglon;
    private List<Obra> obraList;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private List<SuministrosView> suministrosViewList;

    private static JSONObject createUnidadesJSONObject(Unidadobra unidadobra) throws UnsupportedEncodingException {
        JSONObject uoObject = new JSONObject();
        if (unidadobra.getUm() != null) {
            uoObject.put("id", unidadobra.getId());
            uoObject.put("codigo", unidadobra.getCodigo().trim());
            uoObject.put("descripcion", new String(unidadobra.getDescripcion().trim().getBytes(), "UTF-8"));
            uoObject.put("cantidad", unidadobra.getCantidad());
            uoObject.put("costototal", unidadobra.getCostototal());
            uoObject.put("costounitario", unidadobra.getCostounitario());
            uoObject.put("salario", unidadobra.getSalario());
            uoObject.put("especialidadesId", unidadobra.getEspecialidadesByEspecialidadesId().getCodigo());
            uoObject.put("nivelId", unidadobra.getNivelByNivelId().getCodigo());
            uoObject.put("objetosId", unidadobra.getObjetosByObjetosId().getCodigo());
            uoObject.put("obraId", unidadobra.getObraId());
            uoObject.put("empresaconstructoraId", unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
            uoObject.put("grupoejecucionId", 1);
            uoObject.put("subespecialidadesId", unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo());
            uoObject.put("um", unidadobra.getUm().trim());
            uoObject.put("renglonbase", unidadobra.getRenglonbase());
            uoObject.put("costoMaterial", unidadobra.getCostoMaterial());
            uoObject.put("costomano", unidadobra.getCostomano());
            uoObject.put("costoequipo", unidadobra.getCostoequipo());
            uoObject.put("idResolucion", unidadobra.getIdResolucion());
            uoObject.put("idzona", unidadobra.getZonasByZonasId().getCodigo());
        }
        return uoObject;
    }

    private static JSONObject createNivelJSONObject(Nivel nivel) throws Exception {
        JSONObject nObject = new JSONObject();
        nObject.put("id", nivel.getId());
        nObject.put("code", nivel.getCodigo());
        nObject.put("desc", new String(nivel.getDescripcion().trim().getBytes(), "UTF-8"));
        nObject.put("ido", nivel.getObjetosId());
        return nObject;
    }

    private static JSONObject createCertificacionRenglonJSONObject(CertificacionRenglonVariante planificacion) {
        JSONObject certifica = new JSONObject();
        certifica.put("id", planificacion.getId());
        certifica.put("cantidad", planificacion.getCantidad());
        certifica.put("costomaterial", planificacion.getCostomaterial());
        certifica.put("costoequipo", planificacion.getCostoequipo());
        certifica.put("costomano", planificacion.getCostomano());
        certifica.put("costosalario", planificacion.getSalario());
        certifica.put("desde", planificacion.getDesde().toString());
        certifica.put("hasta", planificacion.getHasta().toString());
        certifica.put("unidadobraId", planificacion.getNivelespecificoId());
        certifica.put("brigadaconstruccionId", planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo());
        certifica.put("grupoconstruccionId", planificacion.getGrupoconstruccionByGrupoconstruccionId().getCodigo());
        certifica.put("cuadrillaconstruccionId", planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo());
        certifica.put("idRenglon", planificacion.getRenglonvarianteId());
        return certifica;
    }

    private static JSONObject createCertificacionUnoidadJSONObject(Certificacion planificacion) {
        JSONObject certifica = new JSONObject();
        certifica.put("id", planificacion.getId());
        certifica.put("cantidad", planificacion.getCantidad());
        certifica.put("costomaterial", planificacion.getCostmaterial());
        certifica.put("costoequipo", planificacion.getCostequipo());
        certifica.put("costomano", planificacion.getCostmano());
        certifica.put("costosalario", planificacion.getSalario());
        certifica.put("desde", planificacion.getDesde().toString());
        certifica.put("hasta", planificacion.getHasta().toString());
        certifica.put("unidadobraId", planificacion.getUnidadobraId());
        certifica.put("brigadaconstruccionId", planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo());
        certifica.put("grupoconstruccionId", planificacion.getGrupoconstruccionByGrupoconstruccionId().getCodigo());
        certifica.put("cuadrillaconstruccionId", planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo());
        return certifica;
    }

    private static JSONObject createPlanRenglonJSONObject(Planrenglonvariante planificacion) {
        JSONObject plan = new JSONObject();
        plan.put("id", planificacion.getId());
        plan.put("cantidad", planificacion.getCantidad());
        plan.put("costomaterial", planificacion.getCostomaterial());
        plan.put("costoequipo", planificacion.getCostoequipo());
        plan.put("costomano", planificacion.getCostomano());
        plan.put("costosalario", planificacion.getSalario());
        plan.put("desde", planificacion.getDesde().toString());
        plan.put("hasta", planificacion.getHasta().toString());
        plan.put("unidadobraId", planificacion.getNivelespecificoId());
        plan.put("brigadaconstruccionId", planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo());
        plan.put("grupoconstruccionId", planificacion.getGrupoconstruccionByGrupoconstruccionId().getCodigo());
        plan.put("cuadrillaconstruccionId", planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo());
        plan.put("idRenglon", planificacion.getRenglonvarianteId());
        return plan;
    }

    private JSONObject createPlanRecUnoidadJSONObject(Planrecuo rec) {
        JSONObject plarec = new JSONObject();
        plarec.put("id", rec.getId());
        plarec.put("unidadobraId", rec.getUnidadobraId());
        plarec.put("renglonId", rec.getRenglonId());
        if (rec.getRecursoId() != 0) {
            plarec.put("recursoId", getCodeSuministro(rec.getRecursoId(), rec.getTipo()).getCodigo());
        } else {
            plarec.put("recursoId", rec.getRecursoId());
        }
        plarec.put("cantidad", rec.getCantidad());
        plarec.put("costo", rec.getCosto());
        plarec.put("fini", rec.getFini().toString());
        plarec.put("ffin", rec.getFini().toString());
        plarec.put("planId", rec.getPlanId());
        plarec.put("cmateriales", rec.getCmateriales());
        plarec.put("cmano", rec.getCmano());
        plarec.put("cequipo", rec.getCequipo());
        plarec.put("salario", rec.getSalario());
        plarec.put("tipo", rec.getTipo());
        return plarec;
    }

    private static JSONObject createNivelRenglonJSONObject(Renglonnivelespecifico unidadobrarenglon) {
        JSONObject rengObject = new JSONObject();
        rengObject.put("uoId", unidadobrarenglon.getNivelespecificoId());
        rengObject.put("rvId", unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo());
        rengObject.put("cantRv", unidadobrarenglon.getCantidad());
        rengObject.put("costMat", unidadobrarenglon.getCostmaterial());
        rengObject.put("costMano", unidadobrarenglon.getCostmano());
        rengObject.put("costEquip", unidadobrarenglon.getCostequipo());
        rengObject.put("conMat", unidadobrarenglon.getConmat());
        rengObject.put("salariomn", unidadobrarenglon.getSalario());
        return rengObject;
    }

    private static JSONObject createPlanUnoidadJSONObject(Planificacion planificacion) {
        JSONObject plan = new JSONObject();
        plan.put("id", planificacion.getId());
        plan.put("cantidad", planificacion.getCantidad());
        plan.put("costomaterial", planificacion.getCostomaterial());
        plan.put("costoequipo", planificacion.getCostoequipo());
        plan.put("costomano", planificacion.getCostomano());
        plan.put("costosalario", planificacion.getCostosalario());
        plan.put("desde", planificacion.getDesde().toString());
        plan.put("hasta", planificacion.getHasta().toString());
        plan.put("unidadobraId", planificacion.getUnidadobraId());
        plan.put("brigadaconstruccionId", planificacion.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo());
        plan.put("grupoconstruccionId", planificacion.getGrupoconstruccionByGrupoconstruccionId().getCodigo());
        plan.put("cuadrillaconstruccionId", planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo());
        return plan;
    }

    private static JSONObject createNivelEspecificoJSONObject(Nivelespecifico unidadobra) {
        JSONObject uoObject = new JSONObject();
        uoObject.put("id", unidadobra.getId());
        uoObject.put("codigo", unidadobra.getCodigo().trim());
        uoObject.put("descripcion", unidadobra.getDescripcion().trim());
        uoObject.put("salario", unidadobra.getSalario());
        uoObject.put("especialidadesId", unidadobra.getEspecialidadesByEspecialidadesId().getCodigo());
        uoObject.put("nivelId", unidadobra.getNivelByNivelId().getCodigo());
        uoObject.put("objetosId", unidadobra.getObjetosByObjetosId().getCodigo());
        uoObject.put("obraId", unidadobra.getObraId());
        uoObject.put("empresaconstructoraId", unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
        uoObject.put("subespecialidadesId", unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo());
        uoObject.put("costoMaterial", unidadobra.getCostomaterial());
        uoObject.put("costomano", unidadobra.getCostomano());
        uoObject.put("costoequipo", unidadobra.getCostoequipo());
        uoObject.put("idzona", unidadobra.getZonasByZonasId().getCodigo());
        return uoObject;
    }

    private JSONObject createCertificacionRecUnoidadJSONObject(Certificacionrecuo rec) {
        JSONObject plarec = new JSONObject();
        plarec.put("id", rec.getId());
        plarec.put("unidadobraId", rec.getUnidadobraId());
        plarec.put("renglonId", rec.getRenglonId());
        if (rec.getRecursoId() != 0) {
            plarec.put("recursoId", getCodeSuministro(rec.getRecursoId(), rec.getTipo()).getCodigo());
        } else {
            plarec.put("recursoId", rec.getRecursoId());
        }
        plarec.put("cantidad", rec.getCantidad());
        plarec.put("costo", rec.getCosto());
        plarec.put("fini", rec.getFini().toString());
        plarec.put("ffin", rec.getFini().toString());
        plarec.put("certificacionId", rec.getCertificacionId());
        plarec.put("cmateriales", rec.getCmateriales());
        plarec.put("cmano", rec.getCmano());
        plarec.put("cequipo", rec.getCequipo());
        plarec.put("salario", rec.getSalario());
        plarec.put("tipo", rec.getTipo());
        return plarec;
    }

    private JSONObject createCertificacionRecRenglonJSONObject(Certificacionrecrv rec) {
        JSONObject plarec = new JSONObject();
        plarec.put("id", rec.getId());
        plarec.put("unidadobraId", rec.getNivelespId());
        plarec.put("renglonId", rec.getRenglonId());
        if (rec.getRecursoId() != 0) {
            plarec.put("recursoId", getCodeSuministro(rec.getRecursoId(), rec.getTipo()).getCodigo());
        } else {
            plarec.put("recursoId", rec.getRecursoId());
        }
        plarec.put("cantidad", rec.getCantidad());
        plarec.put("costo", rec.getCosto());
        plarec.put("fini", rec.getFini().toString());
        plarec.put("ffin", rec.getFini().toString());
        plarec.put("certificacionId", rec.getCertificacionId());
        plarec.put("cmateriales", rec.getCmateriales());
        plarec.put("cmano", rec.getCmano());
        plarec.put("cequipo", rec.getCequipo());
        plarec.put("salario", rec.getSalario());
        plarec.put("tipo", rec.getTipo());
        return plarec;
    }

    private static JSONObject createUnidadesRenglonJSONObject(Unidadobrarenglon unidadobrarenglon) {
        JSONObject rengObject = new JSONObject();
        rengObject.put("uoId", unidadobrarenglon.getUnidadobraId());
        rengObject.put("rvId", unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo());
        rengObject.put("cantRv", unidadobrarenglon.getCantRv());
        rengObject.put("costMat", unidadobrarenglon.getCostMat());
        rengObject.put("costMano", unidadobrarenglon.getCostMano());
        rengObject.put("costEquip", unidadobrarenglon.getCostEquip());
        rengObject.put("conMat", unidadobrarenglon.getConMat());
        rengObject.put("salariomn", unidadobrarenglon.getSalariomn());
        return rengObject;
    }

    private JSONObject createPlanRecRenglonJSONObject(Planrecrv rec) {
        JSONObject plarec = new JSONObject();
        plarec.put("id", rec.getId());
        plarec.put("unidadobraId", rec.getNivelespId());
        plarec.put("renglonId", rec.getRenglonId());
        if (rec.getRecursoId() != 0) {
            plarec.put("recursoId", getCodeSuministro(rec.getRecursoId(), rec.getTipo()).getCodigo());
        } else {
            plarec.put("recursoId", rec.getRecursoId());
        }
        plarec.put("cantidad", rec.getCantidad());
        plarec.put("costo", rec.getCosto());
        plarec.put("fini", rec.getFini().toString());
        plarec.put("ffin", rec.getFini().toString());
        plarec.put("planId", rec.getPlanId());
        plarec.put("cmateriales", rec.getCmateriales());
        plarec.put("cmano", rec.getCmano());
        plarec.put("cequipo", rec.getCequipo());
        plarec.put("salario", rec.getSalario());
        plarec.put("tipo", rec.getTipo());
        return plarec;
    }

    private JSONObject createBajoUnoidadJSONObject(Bajoespecificacion bajoespecificacion) {

        if (getCodeSuministro(bajoespecificacion.getIdSuministro(), bajoespecificacion.getTipo()) == null) {
            System.out.println(bajoespecificacion.getIdSuministro() + " <---> " + bajoespecificacion.getTipo());
        }

        JSONObject bajoObject = new JSONObject();
        bajoObject.put("id", bajoespecificacion.getId());
        bajoObject.put("unidadobraId", bajoespecificacion.getUnidadobraId());
        bajoObject.put("idSuministro", getCodeSuministro(bajoespecificacion.getIdSuministro(), bajoespecificacion.getTipo()).getCodigo());
        bajoObject.put("cantidad", bajoespecificacion.getCantidad());
        bajoObject.put("costo", bajoespecificacion.getCosto());
        bajoObject.put("tipo", bajoespecificacion.getTipo());
        bajoObject.put("sumrenglon", 0);
        return bajoObject;
    }

    private JSONObject createBajoRenglonJSONObject(Bajoespecificacionrv bajoespecificacion) {
        JSONObject bajoObject = new JSONObject();
        bajoObject.put("id", bajoespecificacion.getId());
        bajoObject.put("unidadobraId", bajoespecificacion.getNivelespecificoId());
        bajoObject.put("idSuministro", getCodeSuministro(bajoespecificacion.getIdsuministro(), bajoespecificacion.getTipo()).getCodigo());
        bajoObject.put("idRenglon", bajoespecificacion.getRenglonvarianteId());
        bajoObject.put("cantidad", bajoespecificacion.getCantidad());
        bajoObject.put("costo", bajoespecificacion.getCosto());
        bajoObject.put("tipo", bajoespecificacion.getTipo());
        return bajoObject;
    }

    private static JSONObject createObjetosJSONObject(Objetos objetos) throws Exception {
        JSONObject oObject = new JSONObject();
        oObject.put("id", objetos.getId());
        oObject.put("code", objetos.getCodigo());
        oObject.put("desc", new String(objetos.getDescripcion().trim().getBytes(), "UTF-8"));
        oObject.put("idz", objetos.getZonasId());
        return oObject;
    }

    private static JSONObject createZonaJSONObject(Zonas zonas) throws Exception {
        JSONObject zObject = new JSONObject();
        zObject.put("id", zonas.getId());
        zObject.put("code", zonas.getCodigo());
        zObject.put("desc", new String(zonas.getDesripcion().trim().getBytes(), "UTF-8"));
        return zObject;
    }

    private SuministrosView getCodeSuministro(int id, String tipo) {
        return utilCalcSingelton.listSuministros.parallelStream().filter(item -> item.getId() == id && item.getTipo().trim().equals(tipo.trim())).findFirst().orElse(null);
    }

    private ObservableList<ExportarObra> getExportarObraObservableList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            exportarObraObservableList = FXCollections.observableArrayList();
            obraList = new ArrayList<>();
            obraList = session.createQuery("FROM Obra ").getResultList();
            for (Obra obra : obraList) {
                JFXCheckBox check = new JFXCheckBox();
                check.setText(obra.getCodigo());
                exportarObraObservableList.add(new ExportarObra(check, obra.getDescripion(), obra.getTipo()));
            }
            trx.commit();
            session.close();
            return exportarObraObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private List<Certificacionrecuo> getListCertRecUO() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            listCertRecUO = new ArrayList<>();
            listCertRecUO = session.createQuery("FROM Certificacionrecuo ").getResultList();

            trx.commit();
            session.close();
            return listCertRecUO;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Certificacionrecrv> getListCertRecRV() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            listCertRecRenglon = new ArrayList<>();
            listCertRecRenglon = session.createQuery("FROM Certificacionrecrv ").getResultList();

            trx.commit();
            session.close();
            return listCertRecRenglon;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Planrecuo> getListPlanRevUO() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            listPlanRevUO = new ArrayList<>();
            listPlanRevUO = session.createQuery("FROM Planrecuo ").getResultList();

            trx.commit();
            session.close();
            return listPlanRevUO;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private List<Planrecrv> getListPlanRevRenglon() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            listPlanRevRenglon = new ArrayList<>();
            listPlanRevRenglon = session.createQuery("FROM Planrecuo ").getResultList();

            trx.commit();
            session.close();
            return listPlanRevRenglon;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        code.setCellValueFactory(new PropertyValueFactory<ExportarObra, JFXCheckBox>("empresa"));
        descrip.setCellValueFactory(new PropertyValueFactory<ExportarObra, String>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<ExportarObra, String>("tipo"));

        ObservableList<ExportarObra> datos = FXCollections.observableArrayList();
        datos = getExportarObraObservableList();


        FilteredList<ExportarObra> filteredData = new FilteredList<ExportarObra>(datos, p -> true);

        SortedList<ExportarObra> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        fildSearch.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }
                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

        List<Certificacionrecuo> certificacionrecuoList = getListCertRecUO();
        certificacionrecuoList.size();

        List<Planrecuo> planrecuoList = getListPlanRevUO();
        planrecuoList.size();


        List<Certificacionrecrv> certificacionrecrvs = getListCertRecRV();
        certificacionrecrvs.size();

        List<Planrecrv> planrecrvs = getListPlanRevRenglon();
        planrecrvs.size();


    }

    private List<Certificacionrecuo> getListCertificacionrecuoList(int idUO, int idCert) {
        return listCertRecUO.parallelStream().filter(item -> item.getUnidadobraId() == idUO && item.getCertificacionId() == idCert).collect(Collectors.toList());
    }

    private List<Certificacionrecrv> getListCertificacionrecrvList(int idUO, int idCert) {
        return listCertRecRenglon.parallelStream().filter(item -> item.getRenglonId() == idUO && item.getCertificacionId() == idCert).collect(Collectors.toList());
    }

    private List<Planrecuo> getListPlanrecuoList(int idUO, int idPlan) {
        return listPlanRevUO.parallelStream().filter(item -> item.getUnidadobraId() == idUO && item.getPlanId() == idPlan).collect(Collectors.toList());
    }

    private List<Planrecrv> getListPlanrecrvList(int idUO, int idPlan) {
        return listPlanRevRenglon.parallelStream().filter(item -> item.getNivelespId() == idUO && item.getPlanId() == idPlan).collect(Collectors.toList());
    }

    public void handleExportarObra(ActionEvent event) throws Exception {
        String code = dataTable.getItems().parallelStream().filter(item -> item.getEmpresa().isSelected()).map(item -> item.getEmpresa().getText()).findFirst().orElse("no");
        Obra obra = obraList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
        if (obra.getTipo().equals("UO")) {
            System.out.println(obra.getSalarioId());
            utilCalcSingelton.listToSugestionSuministros(obra.getSalarioId());
            JSONObject obraJson = new JSONObject();
            obraJson.put("id", obra.getId());
            obraJson.put("code", obra.getCodigo());
            obraJson.put("descrip", new String(obra.getDescripion().trim().getBytes(), "UTF-8"));
            obraJson.put("tipo", obra.getTipo());
            obraJson.put("entidad", obra.getEntidadByEntidadId().getCodigo());
            obraJson.put("tipoobra", obra.getTipoobraId());
            obraJson.put("inversionista", obra.getInversionistaByInversionistaId().getCodigo());
            obraJson.put("salario", obra.getSalarioId());
            obraJson.put("tarifa", obra.getTarifaId());

            org.json.simple.JSONArray empresaObraArray = new org.json.simple.JSONArray();
            for (Empresaobra empresaobra : obra.getEmpresaobrasById()) {
                empresaObraArray.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
            }
            obraJson.put("obraEmpresas", empresaObraArray);

            org.json.simple.JSONArray obraZonas = new org.json.simple.JSONArray();
            org.json.simple.JSONArray objetosZonas = new org.json.simple.JSONArray();
            org.json.simple.JSONArray nivelObjetos = new JSONArray();
            for (Zonas zonas : obra.getZonasById()) {
                obraZonas.add(createZonaJSONObject(zonas));
                for (Objetos objetos : zonas.getObjetosById()) {
                    objetosZonas.add(createObjetosJSONObject(objetos));
                    for (Nivel nivel : objetos.getNivelsById()) {
                        nivelObjetos.add(createNivelJSONObject(nivel));
                    }
                }
            }
            obraJson.put("zonas", obraZonas);
            obraJson.put("objetos", objetosZonas);
            obraJson.put("niveles", nivelObjetos);

            JSONArray obraUnidades = new JSONArray();
            JSONArray obraUnidadesRenglones = new JSONArray();
            JSONArray obraBajoEspecificacion = new JSONArray();
            JSONArray certificacionUOs = new JSONArray();
            JSONArray certificaRec = new JSONArray();
            JSONArray planificacionUOs = new JSONArray();
            JSONArray planificaRec = new JSONArray();
            List<Brigadaconstruccion> brigadaconstruccions = new ArrayList<>();
            List<Grupoconstruccion> grupoconstruccions = new ArrayList<>();
            List<Cuadrillaconstruccion> cuadrillaconstruccions = new ArrayList<>();
            suministrosViewList = new ArrayList<>();
            for (Unidadobra unidadobra : obra.getUnidadobrasById()) {
                obraUnidades.add(createUnidadesJSONObject(unidadobra));
                for (Unidadobrarenglon unidadobrarenglon : unidadobra.getUnidadobrarenglonsById()) {
                    obraUnidadesRenglones.add(createUnidadesRenglonJSONObject(unidadobrarenglon));
                }
                for (Bajoespecificacion bajoespecificacion : unidadobra.getUnidadobrabajoespecificacionById()) {
                    obraBajoEspecificacion.add(createBajoUnoidadJSONObject(bajoespecificacion));
                    suministrosViewList.add(getCodeSuministro(bajoespecificacion.getIdSuministro(), bajoespecificacion.getTipo()));
                }
                if (unidadobra.getPlanificacionsById().size() > 0) {
                    for (Planificacion planificacion : unidadobra.getPlanificacionsById()) {
                        planificacionUOs.add(createPlanUnoidadJSONObject(planificacion));
                        brigadaconstruccions.add(planificacion.getBrigadaconstruccionByBrigadaconstruccionId());
                        grupoconstruccions.add(planificacion.getGrupoconstruccionByGrupoconstruccionId());
                        cuadrillaconstruccions.add(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId());
                        for (Planrecuo rec : getListPlanrecuoList(unidadobra.getId(), planificacion.getId())) {
                            planificaRec.add(createPlanRecUnoidadJSONObject(rec));
                        }
                    }
                    obraJson.put("plan", planificacionUOs);
                    obraJson.put("planRec", planificaRec);
                }
                if (unidadobra.getCertificacionsById().size() > 0) {
                    for (Certificacion planificacion : unidadobra.getCertificacionsById()) {
                        certificacionUOs.add(createCertificacionUnoidadJSONObject(planificacion));
                        brigadaconstruccions.add(planificacion.getBrigadaconstruccionByBrigadaconstruccionId());
                        grupoconstruccions.add(planificacion.getGrupoconstruccionByGrupoconstruccionId());
                        cuadrillaconstruccions.add(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId());
                        for (Certificacionrecuo rec : getListCertificacionrecuoList(unidadobra.getId(), planificacion.getId())) {
                            System.out.println(planificacion.getCantidad() + " -- " + planificacion.getUnidadobraByUnidadobraId().getCodigo());

                            certificaRec.add(createCertificacionRecUnoidadJSONObject(rec));
                        }
                    }
                    obraJson.put("certificacion", certificacionUOs);
                    obraJson.put("certificaRec", certificaRec);
                }
            }
            obraJson.put("unidades", obraUnidades);
            obraJson.put("renglones", obraUnidadesRenglones);
            obraJson.put("suministros", obraBajoEspecificacion);

            JSONArray empresaObraCoeficientesArray = new JSONArray();
            for (Coeficientesequipos coeficientesequipos : obra.getCoeficientesequiposById()) {
                empresaObraCoeficientesArray.add(createEmpresaObraCoeficientesJSONObject(coeficientesequipos));
            }
            obraJson.put("coeficientes", empresaObraCoeficientesArray);

            JSONArray obraEspecialidades = new JSONArray();
            JSONArray obraSubespecialidades = new JSONArray();
            List<Especialidades> especialidadesArrayList = new ArrayList<>();
            List<Subespecialidades> subespecialidadesList = new ArrayList<>();
            for (Unidadobra unidadobra : obra.getUnidadobrasById()) {
                especialidadesArrayList.add(unidadobra.getEspecialidadesByEspecialidadesId());
                subespecialidadesList.add(unidadobra.getSubespecialidadesBySubespecialidadesId());
            }
            for (Especialidades especialidades : especialidadesArrayList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                obraEspecialidades.add(createEspecialidaadObject(especialidades));
            }

            for (Subespecialidades subespecialidades : subespecialidadesList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                obraSubespecialidades.add(createObraSubespecialidad(subespecialidades));
            }
            obraJson.put("especialidades", obraEspecialidades);
            obraJson.put("subespecialidades", obraSubespecialidades);

            JSONArray brigadas = new JSONArray();
            if (brigadaconstruccions.size() > 0) {
                for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    brigadas.add(createbrigad(brigadaconstruccion));
                }
            }

            obraJson.put("brigadas", brigadas);
            JSONArray grupos = new JSONArray();
            if (grupoconstruccions.size() > 0) {
                for (Grupoconstruccion grupo : grupoconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    grupos.add(createGrupos(grupo));
                }
            }
            obraJson.put("grupos", grupos);
            JSONArray cuadrillas = new JSONArray();
            if (cuadrillaconstruccions.size() > 0) {
                for (Cuadrillaconstruccion grupo : cuadrillaconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    cuadrillas.add(createCuadrilla(grupo));
                }
            }

            obraJson.put("cuadrillas", cuadrillas);
            JSONArray suminiJsonArray = new JSONArray();
            for (SuministrosView suministrosView : suministrosViewList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                suminiJsonArray.add(createSumini(suministrosView));
            }

            obraJson.put("suministrosBajo", suminiJsonArray);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar exportación");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
                    fileWriter.write(obraJson.toJSONString());
                    fileWriter.flush();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(obra.getDescripion() + " exportada");
                    alert.setContentText("Fichero .json dispobible en: " + file.getAbsolutePath());
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (obra.getTipo().equals("RV")) {
            utilCalcSingelton.listToSugestionSuministros(obra.getSalarioId());
            JSONObject obraJson = new JSONObject();
            obraJson.put("id", obra.getId());
            obraJson.put("code", obra.getCodigo());
            obraJson.put("descrip", obra.getDescripion());
            obraJson.put("tipo", obra.getTipo());
            obraJson.put("entidad", obra.getEntidadByEntidadId().getCodigo());
            obraJson.put("tipoobra", obra.getTipoobraId());
            obraJson.put("inversionista", obra.getInversionistaByInversionistaId().getCodigo());
            obraJson.put("salario", obra.getSalarioId());
            obraJson.put("tarifa", obra.getTarifaId());

            org.json.simple.JSONArray empresaObraArray = new org.json.simple.JSONArray();
            for (Empresaobra empresaobra : obra.getEmpresaobrasById()) {
                empresaObraArray.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
            }
            obraJson.put("obraEmpresas", empresaObraArray);

            org.json.simple.JSONArray obraZonas = new org.json.simple.JSONArray();
            org.json.simple.JSONArray objetosZonas = new org.json.simple.JSONArray();
            org.json.simple.JSONArray nivelObjetos = new JSONArray();
            for (Zonas zonas : obra.getZonasById()) {
                obraZonas.add(createZonaJSONObject(zonas));
                for (Objetos objetos : zonas.getObjetosById()) {
                    objetosZonas.add(createObjetosJSONObject(objetos));
                    for (Nivel nivel : objetos.getNivelsById()) {
                        nivelObjetos.add(createNivelJSONObject(nivel));
                    }
                }
            }
            obraJson.put("zonas", obraZonas);
            obraJson.put("objetos", objetosZonas);
            obraJson.put("niveles", nivelObjetos);


            JSONArray obraUnidades = new JSONArray();
            JSONArray obraUnidadesRenglones = new JSONArray();
            JSONArray obraBajoEspecificacion = new JSONArray();
            JSONArray certificacionUOs = new JSONArray();
            JSONArray certificaRec = new JSONArray();
            JSONArray planificacionUOs = new JSONArray();
            JSONArray planificaRec = new JSONArray();
            List<Brigadaconstruccion> brigadaconstruccions = new ArrayList<>();
            List<Grupoconstruccion> grupoconstruccions = new ArrayList<>();
            List<Cuadrillaconstruccion> cuadrillaconstruccions = new ArrayList<>();
            suministrosViewList = new ArrayList<>();
            for (Nivelespecifico unidadobra : obra.getNivelespecificosById()) {
                obraUnidades.add(createNivelEspecificoJSONObject(unidadobra));
                for (Renglonnivelespecifico unidadobrarenglon : unidadobra.getRenglonnivelespecificosById()) {
                    obraUnidadesRenglones.add(createNivelRenglonJSONObject(unidadobrarenglon));
                }
                for (Bajoespecificacionrv bajoespecificacion : unidadobra.getBajoespecificacionrvsById()) {
                    obraBajoEspecificacion.add(createBajoRenglonJSONObject(bajoespecificacion));
                    suministrosViewList.add(getCodeSuministro(bajoespecificacion.getIdsuministro(), bajoespecificacion.getTipo()));
                }

                if (unidadobra.getPlanrenglonvariantesById().size() > 0) {
                    for (Planrenglonvariante planificacion : unidadobra.getPlanrenglonvariantesById()) {
                        planificacionUOs.add(createPlanRenglonJSONObject(planificacion));
                        brigadaconstruccions.add(planificacion.getBrigadaconstruccionByBrigadaconstruccionId());
                        grupoconstruccions.add(planificacion.getGrupoconstruccionByGrupoconstruccionId());
                        cuadrillaconstruccions.add(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId());
                        for (Planrecrv rec : getListPlanrecrvList(unidadobra.getId(), planificacion.getId())) {
                            planificaRec.add(createPlanRecRenglonJSONObject(rec));
                        }

                    }
                    obraJson.put("plan", planificacionUOs);
                    obraJson.put("planRec", planificaRec);
                }
                if (utilCalcSingelton.getCertificacionRenglonVarianteList(unidadobra.getId()).size() > 0) {
                    for (CertificacionRenglonVariante planificacion : utilCalcSingelton.getCertificacionRenglonVarianteList(unidadobra.getId())) {
                        certificacionUOs.add(createCertificacionRenglonJSONObject(planificacion));
                        brigadaconstruccions.add(planificacion.getBrigadaconstruccionByBrigadaconstruccionId());
                        grupoconstruccions.add(planificacion.getGrupoconstruccionByGrupoconstruccionId());
                        cuadrillaconstruccions.add(planificacion.getCuadrillaconstruccionByCuadrillaconstruccionId());
                        for (Certificacionrecrv rec : getListCertificacionrecrvList(unidadobra.getId(), planificacion.getId())) {
                            certificaRec.add(createCertificacionRecRenglonJSONObject(rec));
                        }
                    }
                    obraJson.put("certificacion", certificacionUOs);
                    obraJson.put("certificaRec", certificaRec);
                }

            }
            obraJson.put("unidades", obraUnidades);
            obraJson.put("renglones", obraUnidadesRenglones);
            obraJson.put("suministros", obraBajoEspecificacion);

            JSONArray empresaObraCoeficientesArray = new JSONArray();
            for (Coeficientesequipos coeficientesequipos : obra.getCoeficientesequiposById()) {
                empresaObraCoeficientesArray.add(createEmpresaObraCoeficientesJSONObject(coeficientesequipos));
            }
            obraJson.put("coeficientes", empresaObraCoeficientesArray);

            JSONArray obraEspecialidades = new JSONArray();
            JSONArray obraSubespecialidades = new JSONArray();
            List<Especialidades> especialidadesArrayList = new ArrayList<>();
            List<Subespecialidades> subespecialidadesList = new ArrayList<>();
            for (Nivelespecifico unidadobra : obra.getNivelespecificosById()) {
                especialidadesArrayList.add(unidadobra.getEspecialidadesByEspecialidadesId());
                subespecialidadesList.add(unidadobra.getSubespecialidadesBySubespecialidadesId());
            }
            for (Especialidades especialidades : especialidadesArrayList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                obraEspecialidades.add(createEspecialidaadObject(especialidades));
            }

            for (Subespecialidades subespecialidades : subespecialidadesList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                obraSubespecialidades.add(createObraSubespecialidad(subespecialidades));
            }

            obraJson.put("especialidades", obraEspecialidades);
            obraJson.put("subespecialidades", obraSubespecialidades);

            JSONArray brigadas = new JSONArray();
            if (brigadaconstruccions.size() > 0) {
                for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    brigadas.add(createbrigad(brigadaconstruccion));
                }
            }

            obraJson.put("brigadas", brigadas);
            JSONArray grupos = new JSONArray();
            if (grupoconstruccions.size() > 0) {
                for (Grupoconstruccion grupo : grupoconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    grupos.add(createGrupos(grupo));
                }
            }
            obraJson.put("grupos", grupos);
            JSONArray cuadrillas = new JSONArray();
            if (cuadrillaconstruccions.size() > 0) {
                for (Cuadrillaconstruccion grupo : cuadrillaconstruccions.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                    cuadrillas.add(createCuadrilla(grupo));
                }
            }

            obraJson.put("cuadrillas", cuadrillas);
            JSONArray suminiJsonArray = new JSONArray();
            for (SuministrosView suministrosView : suministrosViewList.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList())) {
                suminiJsonArray.add(createSumini(suministrosView));
            }

            obraJson.put("suministrosBajo", suminiJsonArray);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar exportación");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
                    fileWriter.write(obraJson.toJSONString());
                    fileWriter.flush();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(obra.getDescripion() + " exportada");
                    alert.setContentText("Fichero .json dispobible en: " + file.getAbsolutePath());
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object createSumini(SuministrosView suministrosView) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", suministrosView.getId());
        espec.put("code", suministrosView.getCodigo().trim());
        espec.put("descrip", new String(suministrosView.getDescripcion().trim().getBytes(), "UTF-8"));
        espec.put("um", suministrosView.getUm().trim());
        espec.put("precio", suministrosView.getPreciomn());
        espec.put("tipo", suministrosView.getTipo());
        espec.put("pertenece", suministrosView.getPertene());
        return espec;
    }

    private JSONObject createCuadrilla(Cuadrillaconstruccion grupo) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", grupo.getId());
        espec.put("code", grupo.getCodigo().trim());
        espec.put("descrip", new String(grupo.getDescripcion().trim().getBytes(), "UTF-8"));
        espec.put("idE", grupo.getGrupoconstruccionByGrupoconstruccionId().getCodigo());
        return espec;
    }

    private JSONObject createGrupos(Grupoconstruccion grupo) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", grupo.getId());
        espec.put("code", grupo.getCodigo().trim());
        espec.put("descrip", new String(grupo.getDescripcion().trim().getBytes(), "UTF-8"));
        espec.put("idE", grupo.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo());
        return espec;

    }

    private JSONObject createbrigad(Brigadaconstruccion brigadaconstruccion) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", brigadaconstruccion.getId());
        espec.put("code", brigadaconstruccion.getCodigo().trim());
        espec.put("descrip", new String(brigadaconstruccion.getDescripcion().trim().getBytes(), "UTF-8"));
        espec.put("idE", brigadaconstruccion.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
        return espec;
    }

    private Object createObraSubespecialidad(Subespecialidades subespecialidades) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", subespecialidades.getId());
        espec.put("code", subespecialidades.getCodigo());
        espec.put("descrip", new String(subespecialidades.getDescripcion().trim().getBytes(), "UTF-8"));
        espec.put("idE", subespecialidades.getEspecialidadesId());
        return espec;
    }

    private Object createEspecialidaadObject(Especialidades especialidades) throws Exception {
        JSONObject espec = new JSONObject();
        espec.put("id", especialidades.getId());
        espec.put("code", especialidades.getCodigo());
        espec.put("descrip", new String(especialidades.getDescripcion().trim().getBytes(), "UTF-8"));
        return espec;
    }

    private JSONObject createEmpresaObraCoeficientesJSONObject(Coeficientesequipos coeficientesequipos) {
        JSONObject coeficienteoBRA = new JSONObject();
        coeficienteoBRA.put("empresaconstructoraId", coeficientesequipos.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo());
        coeficienteoBRA.put("recursosId", coeficientesequipos.getRecursosId());
        coeficienteoBRA.put("obraId", coeficientesequipos.getObraByObraId().getId());
        coeficienteoBRA.put("cpo", coeficientesequipos.getCpo());
        coeficienteoBRA.put("cpe", coeficientesequipos.getCpe());
        coeficienteoBRA.put("cet", coeficientesequipos.getCet());
        coeficienteoBRA.put("otra", coeficientesequipos.getOtra());
        coeficienteoBRA.put("salario", coeficientesequipos.getSalario());
        return coeficienteoBRA;
    }


}
