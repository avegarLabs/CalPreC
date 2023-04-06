package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class SuplementarObra implements Initializable {
    public static List<Empresaobratarifa> empresaobratarifaList;
    private static double coeficienteMano;
    private static double coeficienteEquipo;
    private static Map<Unidadobra, Double> uoVolumenDispoMap;
    private static Map<Unidadobrarenglon, Double> urVolumenDispoMap;
    private static Map<Bajoespecificacion, Double> bajoVolumenDispoMap;
    private static List<Empresagastos> empresagastosList;
    private UtilCalcSingelton util = UtilCalcSingelton.getInstance();
    private ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    @FXML
    private JFXComboBox<String> newObra;
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private TextArea text;
    private int batchSizeUOR = 100;
    private int countUOR;
    private int batchbajo = 100;
    private int countbajo;
    private List<Unidadobra> unidadobraList;
    private List<Nivelespecifico> nivelespecificoList;
    private Obra obraToMod;
    private List<Bajoespecificacion> bajoespecificacionList;
    private List<Bajoespecificacionrv> bajoespecificacionrvList;
    private List<Bajoespecificacionrv> bajoespecificacionrvListCal;
    private List<Unidadobrarenglon> unidadobrarenglonList;
    private List<Renglonnivelespecifico> renglonnivelespecificoList;
    private List<Unidadobra> unidadobraListRecal;
    private List<Nivelespecifico> nivelespecificoListRecal;
    private Empresaobrasalario empresaobrasalario;
    private List<Unidadobra> firstUOList;
    private List<Unidadobra> secondUOList;
    private Obra updateObra;
    private Task tarea;
    private ProgressDialog stage;
    private ProgressDialog dialog;
    private Obra newObra266;
    private List<Zonas> createdZonasList;
    private List<Objetos> createdObjetosList;
    private List<Nivel> createdNivelList;
    private List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoeficientesList;
    private int idZona;
    private List<Nivel> list;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXComboBox<String> comboTarifas;
    private ObservableList<TarifaSalarial> tarifaSalarialList;
    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    private ObservableList<String> tarifaSalarialStringList;
    private int idSalario;

    private static double getVolumenUo(Unidadobra uo) {
        return uoVolumenDispoMap.entrySet().parallelStream().filter(unidadobraDoubleEntry -> unidadobraDoubleEntry.getKey().equals(uo)).map(unidadobraDoubleEntry -> unidadobraDoubleEntry.getValue()).findFirst().orElse(0.0);
    }

    private static double getVolumenUORV(Unidadobrarenglon ur) {
        return urVolumenDispoMap.entrySet().parallelStream().filter(unidadobrarenglonDoubleEntry -> unidadobrarenglonDoubleEntry.getKey().equals(ur)).map(unidadobrarenglonDoubleEntry -> unidadobrarenglonDoubleEntry.getValue()).findFirst().orElse(0.0);
    }

    private static double getVolumenBajo(Bajoespecificacion bajoespecificacion) {
        return bajoVolumenDispoMap.entrySet().parallelStream().filter(bajoespecificacionDoubleEntry -> bajoespecificacionDoubleEntry.getKey().equals(bajoespecificacion)).map(bajoespecificacionDoubleEntry -> bajoespecificacionDoubleEntry.getValue()).findFirst().orElse(0.0);
    }

    private static Obra getObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Obra obra = session.get(Obra.class, id);

            tx.commit();
            session.close();
            return obra;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Obra();
    }

    private static Unidadobra getUnidadobra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Unidadobra obra = session.get(Unidadobra.class, id);
            tx.commit();
            session.close();
            return obra;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Unidadobra();
    }

    private static Nivelespecifico getNivelEspecifico(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Nivelespecifico nivelespecifico = session.get(Nivelespecifico.class, id);
            tx.commit();
            session.close();
            return nivelespecifico;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Nivelespecifico();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text.setText("CalPreC creará una nueva obra, en esta se calculará los costos de cada uno de los elementos del presupuesto  " + " \n " + "en función de los valores de la resolución 266 ");
        text.setEditable(false);

        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        newObra.setItems(tarifasLst);

    }

    public void pasarDatos(Obra obra) {
        tarifaSalarialList = FXCollections.observableArrayList();
        tarifaSalarialStringList = FXCollections.observableArrayList();
        if (tarifaSalarialRepository.tarifaSalarialObservableList == null) {
            tarifaSalarialList = tarifaSalarialRepository.getAllTarifasSalarial();
        }
        tarifaSalarialStringList.addAll(tarifaSalarialRepository.tarifaSalarialObservableList.stream().map(TarifaSalarial::getCode).collect(Collectors.toSet()));
        comboTarifas.setItems(tarifaSalarialStringList);
        obraToMod = obra;
        if (obra.getTipo().equals("UO")) {
            unidadobraList = new ArrayList<>();
            unidadobraList = obra.getUnidadobrasById().stream().collect(Collectors.toList());
            unidadobraList.size();

            List<Certificacionrecuo> list = new ArrayList<>();
            list = util.getAllCertificacionesRec(obra.getId());
            list.size();

            List<Empresaobrasalario> empresaobrasalarioList = new ArrayList<>();
            empresaobrasalarioList = util.getEmpresaObraSalarios();
            empresaobrasalarioList.size();

            List<Salario> listSal = new ArrayList<>();
            listSal = util.getSalarios();
            listSal.size();

            List<objectSelect> listRec = new ArrayList<>();
            listRec = util.getRecursos();
            listRec.size();


        } else if (obra.getTipo().equals("RV")) {
            nivelespecificoList = new ArrayList<>();
            nivelespecificoList = obra.getNivelespecificosById().stream().collect(Collectors.toList());
            nivelespecificoList.size();

            List<Empresaobrasalario> empresaobrasalarioList = new ArrayList<>();
            empresaobrasalarioList = util.getEmpresaObraSalarios();
            empresaobrasalarioList.size();

            List<Salario> listSal = new ArrayList<>();
            listSal = util.getSalarios();
            listSal.size();

            List<objectSelect> listRec = new ArrayList<>();
            listRec = util.getRecursos();
            listRec.size();
        }

    }

    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < val; i++) {
                    Thread.sleep(val / 2);
                    updateProgress(i + 1, val);
                }
                return true;
            }
        };
    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(2000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }

    public void suplementarObraCalc(ActionEvent event) {
        if (obraToMod.getTipo().trim().equals("UO")) {
            idSalario = util.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(newObra.getValue().trim())).findFirst().map(Salario::getId).orElse(0);
            createNewObraByR266(idSalario);
        } else if (obraToMod.getTipo().trim().equals("RV")) {
            idSalario = util.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(newObra.getValue().trim())).findFirst().map(Salario::getId).orElse(0);
            createNewObraByRVR266(idSalario);
        }
    }

    private void createNewObraByRVR266(int idSalario) {
        util.listToSugestionSuministros(idSalario);

        tarea = createTimeMesage();
        dialog = new ProgressDialog(tarea);
        dialog.setContentText("Creando nueva obra... por favor espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        Obra newObra = new Obra();
        int lastCharacter = Integer.parseInt(String.valueOf(obraToMod.getCodigo().charAt(5)));
        int newLast = 0;
        if (lastCharacter != 9) {
            newLast = lastCharacter + 1;
        }
        newObra.setCodigo(obraToMod.getCodigo().substring(0, 5).concat(String.valueOf(newLast)));
        newObra.setDescripion(obraToMod.getDescripion().concat("(R266)"));
        newObra.setTipo("RV");
        newObra.setEntidadId(obraToMod.getEntidadId());
        newObra.setInversionistaId(obraToMod.getInversionistaId());
        newObra.setTipoobraId(obraToMod.getTipoobraId());

        var tarifa = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getCode().trim().equals(comboTarifas.getValue().trim())).findFirst().get();
        newObra.setTarifaId(tarifa.getId());

        newObra.setSalarioId(idSalario);
        newObra.setDefcostmat(0);

        Integer id = util.addNuevaObra(newObra);
        newObra266 = getObra(id);

        obraToMod.getEmpresaobrasById().size();
        createEmprasaObra(obraToMod.getEmpresaobrasById(), newObra266.getId());

        obraToMod.getCoeficientesequiposById().size();
        createEmpresaCoeficienteObra(obraToMod.getCoeficientesequiposById(), newObra266.getId());

        empresagastosList = new ArrayList<>();
        empresagastosList = structureSingelton.getEmpresagastosList();

        List<Empresaobrasalario> empresaobrasalarios = new ArrayList<>();
        for (Empresaobra empresaobra : obraToMod.getEmpresaobrasById()) {
            addNewEmpresaObraTarifa(newObra266.getId(), empresaobra.getEmpresaconstructoraId(), tarifa);


            List<Empresagastos> empresagastos = new ArrayList<>();
            empresagastos = empresagastosList.parallelStream().filter(eg -> eg.getEmpresaconstructoraId() == empresaobra.getEmpresaconstructoraId()).collect(Collectors.toList());
            empresaobraconceptoscoeficientesList = new ArrayList<>();
            empresaobraconceptoscoeficientesList = empresagastos.parallelStream().map(emg -> {
                Empresaobraconceptoscoeficientes eocc = new Empresaobraconceptoscoeficientes();
                eocc.setEmpresaconstructoraId(empresaobra.getEmpresaconstructoraId());
                eocc.setConceptosgastoId(emg.getConceptosgastoId());
                eocc.setObraId(newObra266.getId());
                eocc.setCoeficiente(emg.getCoeficiente());
                return eocc;
            }).collect(Collectors.toList());
        }
        //  updateEmpresaObraSalario(empresaobrasalarios);
        util.insertEmpresaobraconceptoscoeficientes(empresaobraconceptoscoeficientesList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));

        obraToMod.getZonasById().size();
        createZonas(obraToMod.getZonasById(), id);

        createdZonasList = new ArrayList<>();
        createdZonasList = util.getZonasListFull(id);

        createdObjetosList = new ArrayList<>();
        createdObjetosList = util.getObjetosListFull(id);

        createdNivelList = new ArrayList<>();
        createdNivelList = util.getNivelesByObra(id);

        renglonnivelespecificoList = new ArrayList<>();
        bajoespecificacionrvList = new ArrayList<>();
        nivelespecificoListRecal = new ArrayList<>();

        tarea = createTimeMesage();
        dialog = new ProgressDialog(tarea);
        dialog.setContentText("Calculando volumenes de la obra con la nueva tarifa... por favor espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        for (Nivelespecifico uo : nivelespecificoList) {
            coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == uo.getObraId() && eocc.getEmpresaconstructoraId() == uo.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == uo.getObraId() && eocc.getEmpresaconstructoraId() == uo.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            Nivelespecifico unidad = new Nivelespecifico();
            unidad = createNewNivelNewObra(uo, newObra266);

            if (unidad != null) {
                Integer idUo = saveNivelEspecifico(unidad);
                renglonnivelespecificoList.addAll(newNiveRenglonnivelespecificoList266(uo, idUo, idSalario));
                bajoespecificacionrvList.addAll(newBajoEspecificacionRVListR266(uo, idUo, idSalario));
            }
        }


        persistNivelEspecificoRenglones(renglonnivelespecificoList);
        if (bajoespecificacionrvList.size() > 0) {
            persistBajoespecificacionrvList(bajoespecificacionrvList);
        }

        newObra266 = getObra(id);

        for (Nivelespecifico unidadobra : newObra266.getNivelespecificosById()) {
            nivelespecificoListRecal.add(conformNivelEspecificoToRecal(unidadobra));
        }
        updateNivelEspecifico(nivelespecificoListRecal);

        tarea = createTime(50);
        stage = new ProgressDialog(tarea);
        stage.setContentText("Recalculando obra " + newObra266.getDescripion());
        stage.setTitle("Espere...");
        new Thread(tarea).start();
        stage.showAndWait();

        deleteObras(obraToMod.getId());

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
            //empresaobratarifa.setTarifaInc(0.0);
            empresaobratarifaList.add(empresaobratarifa);
        }
        utilCalcSingelton.createEmpresaobratarifa(empresaobratarifaList);
    }

    private void createNewObraByR266(int idSal) {
        util.listToSugestionSuministros(idSalario);

        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Calculando volumenes de la obra... por favor espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        bajoespecificacionList = new ArrayList<>();
        unidadobrarenglonList = new ArrayList<>();
        unidadobraListRecal = new ArrayList<>();
        uoVolumenDispoMap = new HashMap<>();
        urVolumenDispoMap = new HashMap<>();
        bajoVolumenDispoMap = new HashMap<>();
        firstUOList = new ArrayList<>();
        secondUOList = new ArrayList<>();
        empresagastosList = new ArrayList<>();
        empresagastosList = structureSingelton.getEmpresagastosList();
        for (Unidadobra unidadobra : unidadobraList) {
            double dispo = unidadobra.getCantidad() - unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
            uoVolumenDispoMap.put(unidadobra, dispo);
            if (unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum) > 0.0) {
                coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
                coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
                empresaobratarifaList = new ArrayList<>();
                empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId(), unidadobra.getObraByObraId().getTarifaId());


                unidadobrarenglonList.addAll(unidadobrarenglonListTorecalc(unidadobra));
                if (unidadobra.getUnidadobrabajoespecificacionById().size() > 0) {
                    bajoespecificacionList.addAll(bajoespecificacionListtoRecalc(unidadobra));
                }
                firstUOList.add(unidadobra);
            } else if (unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum) == 0.0) {
                secondUOList.add(unidadobra);
                for (Unidadobrarenglon unidadobrarenglon : unidadobra.getUnidadobrarenglonsById()) {
                    double cantcer = util.getCantCertItemRV(unidadobrarenglon.getUnidadobraId(), unidadobrarenglon.getRenglonvarianteId(), 1);
                    double cant = unidadobrarenglon.getCantRv() - cantcer;
                    urVolumenDispoMap.put(unidadobrarenglon, cant);
                }

                for (Bajoespecificacion bajoespecificacion : unidadobra.getUnidadobrabajoespecificacionById()) {
                    double cantcert = util.getCantCertItemRV(bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 2);
                    double cant = bajoespecificacion.getCantidad() - cantcert;
                    bajoVolumenDispoMap.put(bajoespecificacion, cant);
                }
            }
        }
        updateUnidadesObraRenglones(unidadobrarenglonList);
        if (bajoespecificacionList.size() > 0) {
            updateBajoespecificacion(bajoespecificacionList);
        }
        for (Unidadobra unidadobra : firstUOList) {
            unidadobraListRecal.add(conformUnidadObraToRecal(unidadobra));
        }
        updateUnidadesObra(unidadobraListRecal);
        List<Unidadobra> listToDelete = new ArrayList<>();
        listToDelete.addAll(secondUOList);
        deleteUnidadObra(listToDelete);

        tarea = createTimeMesage();
        dialog = new ProgressDialog(tarea);
        dialog.setContentText("Creando nueva obra... por favor espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        Obra newObra = new Obra();
        int lastCharacter = Integer.parseInt(String.valueOf(obraToMod.getCodigo().charAt(5)));
        int newLast = 0;
        if (lastCharacter != 9) {
            newLast = lastCharacter + 1;
        }
        newObra.setCodigo(obraToMod.getCodigo().substring(0, 5).concat(String.valueOf(newLast)));
        newObra.setDescripion(obraToMod.getDescripion().concat("(R266)"));
        newObra.setTipo("UO");
        newObra.setEntidadId(obraToMod.getEntidadId());
        newObra.setInversionistaId(obraToMod.getInversionistaId());
        newObra.setTipoobraId(obraToMod.getTipoobraId());

        newObra.setSalarioId(idSalario);
        newObra.setDefcostmat(0);

        var tarifa = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getCode().trim().equals(comboTarifas.getValue().trim())).findFirst().get();
        newObra.setTarifaId(tarifa.getId());

        Integer id = util.addNuevaObra(newObra);
        newObra266 = getObra(id);


        obraToMod.getEmpresaobrasById().size();
        createEmprasaObra(obraToMod.getEmpresaobrasById(), newObra266.getId());

        obraToMod.getCoeficientesequiposById().size();
        createEmpresaCoeficienteObra(obraToMod.getCoeficientesequiposById(), newObra266.getId());

        List<Empresaobrasalario> empresaobrasalarios = new ArrayList<>();
        for (Empresaobra empresaobra : obraToMod.getEmpresaobrasById()) {
            addNewEmpresaObraTarifa(newObra266.getId(), empresaobra.getEmpresaconstructoraId(), tarifa);

            List<Empresagastos> empresagastos = new ArrayList<>();
            empresagastos = empresagastosList.parallelStream().filter(eg -> eg.getEmpresaconstructoraId() == empresaobra.getEmpresaconstructoraId()).collect(Collectors.toList());
            empresaobraconceptoscoeficientesList = new ArrayList<>();
            empresaobraconceptoscoeficientesList = empresagastos.parallelStream().map(emg -> {
                Empresaobraconceptoscoeficientes eocc = new Empresaobraconceptoscoeficientes();
                eocc.setEmpresaconstructoraId(empresaobra.getEmpresaconstructoraId());
                eocc.setConceptosgastoId(emg.getConceptosgastoId());
                eocc.setObraId(newObra266.getId());
                eocc.setCoeficiente(emg.getCoeficiente());
                return eocc;
            }).collect(Collectors.toList());
        }

        util.insertEmpresaobraconceptoscoeficientes(empresaobraconceptoscoeficientesList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));

        obraToMod.getZonasById().size();
        createZonas(obraToMod.getZonasById(), id);

        createdZonasList = new ArrayList<>();
        createdZonasList = util.getZonasListFull(id);

        createdObjetosList = new ArrayList<>();
        createdObjetosList = util.getObjetosListFull(id);

        createdNivelList = new ArrayList<>();
        createdNivelList = util.getNivelesByObra(id);

        bajoespecificacionList.clear();
        unidadobrarenglonList.clear();
        unidadobraListRecal.clear();

        tarea = createTimeMesage();
        dialog = new ProgressDialog(tarea);
        dialog.setContentText("Calculando volumenes de la obra con la nueva tarifa... por favor espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        if (firstUOList.size() > 0) {
            for (Unidadobra unidadobra : firstUOList) {
                System.out.println(" &&&&&&& " + unidadobra.getCantidad());
                coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
                coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);

                Unidadobra unidad = new Unidadobra();
                unidad = createNewUONewObra(unidadobra, newObra266);

                if (unidad.getCantidad() > 0) {
                    if (unidad != null) {
                        Integer idUo = saveUnidadobra(unidad);
                        unidadobrarenglonList.addAll(newUnidadObraRenglon266(unidadobra, idUo, idSal));
                        if (unidadobra.getUnidadobrabajoespecificacionById().size() > 0) {
                            bajoespecificacionList.addAll(newBajoEspecificacionListR266(unidadobra, idUo));
                        }
                    }
                }
            }
        }

        if (secondUOList.size() > 0) {
            for (Unidadobra uo : secondUOList) {
                coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == uo.getObraId() && eocc.getEmpresaconstructoraId() == uo.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
                coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == uo.getObraId() && eocc.getEmpresaconstructoraId() == uo.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
                Unidadobra unidad = new Unidadobra();
                unidad = createNewUONewObra(uo, newObra266);
                if (unidad != null) {
                    Integer idUo = saveUnidadobra(unidad);
                    unidadobrarenglonList.addAll(newUnidadObraRenglon266(uo, idUo, idSal));
                    if (uo.getUnidadobrabajoespecificacionById().size() > 0) {
                        bajoespecificacionList.addAll(newBajoEspecificacionListR266(uo, idUo));
                    }
                }
            }
        }

        persistUnidadesObraRenglones(unidadobrarenglonList);
        if (bajoespecificacionList.size() > 0) {
            persistBajoespecificacion(bajoespecificacionList);
        }

        newObra266 = getObra(id);
        deleteUnidadObra(listToDelete);

        for (Unidadobra unidadobra : newObra266.getUnidadobrasById()) {
            unidadobraListRecal.add(reclValuesNewUO(unidadobra));
        }
        updateUnidadesObra(unidadobraListRecal);

        tarea = createTime(50);
        stage = new ProgressDialog(tarea);
        stage.setContentText("Recalculando obra " + obraToMod.getDescripion());
        stage.setTitle("Espere...");
        new Thread(tarea).start();
        stage.showAndWait();

    }

    /*
    private Unidadobra createNewUO(Unidadobra unidadobra, int type) {
        Unidadobra uoNew = new Unidadobra();
        if (type == 1) {
            int lastCharacter = Integer.parseInt(String.valueOf(unidadobra.getCodigo().charAt(6)));
            int newLast = 0;
            if (lastCharacter != 9) {
                newLast = lastCharacter + 1;
            }
            uoNew.setCodigo(unidadobra.getCodigo().substring(0, 6).concat(String.valueOf(newLast)));
        } else if (type == 2) {
            uoNew.setCodigo(unidadobra.getCodigo());
        }
        Renglonvariante renglonvariante = getRenglonvarianteR266(unidadobra.getRenglonbase().trim(), updateObra.getSalarioId());
        uoNew.setDescripcion(unidadobra.getDescripcion().trim().concat("(R266)"));
        uoNew.setUm(renglonvariante.getUm());
        uoNew.setRenglonbase(unidadobra.getRenglonbase());
        uoNew.setCantidad(getVolumenUo(unidadobra));
        uoNew.setIdResolucion(renglonvariante.getRs());
        uoNew.setGrupoejecucionId(unidadobra.getGrupoejecucionId());
        uoNew.setNivelId(unidadobra.getNivelId());
        uoNew.setObraId(unidadobra.getObraId());
        uoNew.setObjetosId(unidadobra.getObjetosId());
        uoNew.setZonasId(unidadobra.getZonasId());
        uoNew.setEspecialidadesId(unidadobra.getEspecialidadesId());
        uoNew.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
        uoNew.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
        return uoNew;
    }
*/
    private Unidadobra createNewUONewObra(Unidadobra unidadobra, Obra obra) {
        Renglonvariante renglonvariante = getRenglonvarianteR266(unidadobra.getRenglonbase().trim(), obra.getSalarioId());
        Unidadobra newUnidad = null;
        if (renglonvariante == null) {
            System.out.println("Unidad no creada: " + unidadobra.getCodigo() + " Reng - Base " + unidadobra.getRenglonbase());
        } else {
            newUnidad = new Unidadobra();
            newUnidad.setObraId(obra.getId());
            newUnidad.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
            int idZ = getIdZona(unidadobra.getZonasByZonasId().getCodigo());
            newUnidad.setZonasId(idZ);
            int idObj = getObjecto(unidadobra.getObjetosByObjetosId().getCodigo(), idZ);
            newUnidad.setObjetosId(idObj);
            int idNiv = getIdNiv(unidadobra.getNivelByNivelId().getCodigo().trim(), idObj);
            newUnidad.setNivelId(idNiv);
            newUnidad.setEspecialidadesId(unidadobra.getEspecialidadesId());
            newUnidad.setSubespecialidadesId(unidadobra.getSubespecialidadesId());

            newUnidad.setDescripcion(unidadobra.getDescripcion());
            newUnidad.setCodigo(unidadobra.getCodigo());
            newUnidad.setUm(renglonvariante.getUm());
            newUnidad.setCantidad(getVolumenUo(unidadobra));
            newUnidad.setRenglonbase(renglonvariante.getCodigo());
            newUnidad.setSalariocuc(0.0);
            newUnidad.setIdResolucion(obra.getSalarioId());
            newUnidad.setGrupoejecucionId(unidadobra.getGrupoejecucionId());

        }
        return newUnidad;
    }

    private Nivelespecifico createNewNivelNewObra(Nivelespecifico nivelespecifico, Obra obra) {
        Nivelespecifico newUnidad = null;
        newUnidad = new Nivelespecifico();
        newUnidad.setObraId(obra.getId());
        newUnidad.setEmpresaconstructoraId(nivelespecifico.getEmpresaconstructoraId());
        int idZ = getIdZona(nivelespecifico.getZonasByZonasId().getCodigo());
        newUnidad.setZonasId(idZ);
        int idObj = getObjecto(nivelespecifico.getObjetosByObjetosId().getCodigo(), idZ);
        newUnidad.setObjetosId(idObj);
        int idNiv = getIdNiv(nivelespecifico.getNivelByNivelId().getCodigo().trim(), idObj);
        newUnidad.setNivelId(idNiv);
        newUnidad.setEspecialidadesId(nivelespecifico.getEspecialidadesId());
        newUnidad.setSubespecialidadesId(nivelespecifico.getSubespecialidadesId());
        newUnidad.setDescripcion(nivelespecifico.getDescripcion());
        newUnidad.setCodigo(nivelespecifico.getCodigo());
        return newUnidad;
    }


    private Unidadobra conformUnidadObraToRecal(Unidadobra unidadobra) {

        double cert = unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        double cMaterial = unidadobra.getUnidadobrabajoespecificacionById().parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
        double cMano = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
        double cEquip = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
        double cMatR = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
        double cSalario = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);

        double total = cMaterial + cMatR + cMano + cEquip;
        double unitario = total / cert;

        unidadobra.setCantidad(cert);
        unidadobra.setCostototal(total);
        unidadobra.setCostoequipo(cEquip);
        unidadobra.setCostomano(cMano);
        unidadobra.setCostoMaterial(cMaterial + cMatR);
        unidadobra.setCostounitario(unitario);
        unidadobra.setSalario(cSalario);
        return unidadobra;
    }

    private Nivelespecifico conformNivelEspecificoToRecal(Nivelespecifico unidadobra) {

        double cMano = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmano).reduce(0.0, Double::sum);
        double cEquip = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostequipo).reduce(0.0, Double::sum);
        double cMatR = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmaterial).reduce(0.0, Double::sum);
        double cSalario = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getSalario).reduce(0.0, Double::sum);

        unidadobra.setCostoequipo(cEquip);
        unidadobra.setCostomano(cMano);
        unidadobra.setCostomaterial(cMatR);
        unidadobra.setSalario(cSalario);
        return unidadobra;
    }

    private Unidadobra reclValuesNewUO(Unidadobra unidadobra) {
        Unidadobra uo = getUnidadobra(unidadobra.getId());
        uo.getUnidadobrarenglonsById().size();
        uo.getUnidadobrabajoespecificacionById().size();

        double cMaterial = uo.getUnidadobrabajoespecificacionById().parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
        double cMano = uo.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
        double cEquip = uo.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
        double cMatR = uo.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
        double cSalario = uo.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);

        double total = cMaterial + cMatR + cMano + cEquip;
        double unitario = total / uo.getCantidad();

        uo.setCantidad(unidadobra.getCantidad());
        uo.setCostototal(total);
        uo.setCostoequipo(cEquip);
        uo.setCostomano(cMano);
        uo.setCostoMaterial(cMaterial + cMatR);
        uo.setCostounitario(unitario);
        uo.setSalario(cSalario);
        return uo;
    }

    private List<Unidadobrarenglon> unidadobrarenglonListTorecalc(Unidadobra unidadobra) {
        List<Unidadobrarenglon> unidadobrarengTorecalc = new ArrayList<>();
        for (Unidadobrarenglon unidadobrarenglon : unidadobra.getUnidadobrarenglonsById()) {
            double cantcer = util.getCantCertItemRV(unidadobrarenglon.getUnidadobraId(), unidadobrarenglon.getRenglonvarianteId(), 1);

            double costoMano = util.calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
            double costoEq = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            double costMater = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            double costMaterSemi = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn()).reduce(0.0, Double::sum);
            double costMaterJueg = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn()).reduce(0.0, Double::sum);
            double cant = unidadobrarenglon.getCantRv() - cantcer;
            urVolumenDispoMap.put(unidadobrarenglon, cant);

            unidadobrarenglon.setCantRv(cantcer);
            unidadobrarenglon.setCostMano(cantcer * costoMano);
            unidadobrarenglon.setCostEquip(cantcer * costoEq);
            if (unidadobrarenglon.getConMat().trim().equals("1")) {
                double mat = costMater + costMaterSemi + costMaterJueg;
                unidadobrarenglon.setCostMat(cantcer * mat);
                unidadobrarenglon.setConMat("1");
            } else if (unidadobrarenglon.getConMat().trim().equals("0")) {
                unidadobrarenglon.setCostMat(0.0);
                unidadobrarenglon.setConMat("0");
            }
            unidadobrarenglon.setSalariomn(cantcer * util.calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()));
            unidadobrarenglon.setSalariocuc(0.0);
            unidadobrarengTorecalc.add(unidadobrarenglon);
        }

        return unidadobrarengTorecalc;
    }

    private List<Bajoespecificacion> bajoespecificacionListtoRecalc(Unidadobra unidadobra) {
        util.listToSugestionSuministros(unidadobra.getObraByObraId().getSalarioId());
        List<Bajoespecificacion> bajoespecificacionsToRecalc = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : unidadobra.getUnidadobrabajoespecificacionById()) {
            double cantcert = util.getCantCertItemRV(bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 2);
            double cant = bajoespecificacion.getCantidad() - cantcert;
            bajoVolumenDispoMap.put(bajoespecificacion, cant);
            bajoespecificacion.setCantidad(cantcert);
            bajoespecificacion.setTipo(bajoespecificacion.getTipo());
            bajoespecificacion.setCosto(cantcert * util.listSuministros.parallelStream().filter(recursos -> recursos.getId() == bajoespecificacion.getIdSuministro()).map(SuministrosView::getPreciomn).findFirst().get());
            bajoespecificacionsToRecalc.add(bajoespecificacion);
        }
        return bajoespecificacionsToRecalc;
    }

    private Renglonvariante getRenglonvarianteR266(String codeRV, int tag) {
        Renglonvariante renglon = util.renglonvarianteList.parallelStream().filter(renglonvariante -> renglonvariante.getCodigo().trim().equals(codeRV.trim()) & renglonvariante.getRs() == tag).findFirst().orElse(null);
        if (renglon == null) {
            System.out.println(codeRV);
        }
        return renglon;
    }

    private List<Unidadobrarenglon> newUnidadObraRenglon266(Unidadobra unidadobra, int idNUO, int idSalar) {
        List<Unidadobrarenglon> unidadobrarengTorecalc = new ArrayList<>();
        Unidadobra uo = getUnidadobra(idNUO);
        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(uo.getObraId(), uo.getEmpresaconstructoraId(), uo.getObraByObraId().getTarifaId());
        for (Unidadobrarenglon unidadobrarenglon : unidadobra.getUnidadobrarenglonsById()) {
            Renglonvariante renglonvariante = getRenglonvarianteR266(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo(), idSalar);
            if (renglonvariante == null) {
                System.out.println(renglonvariante.getCodigo() + " - " + " no existe!!");
            } else {
                renglonvariante.getRenglonrecursosById().size();
                renglonvariante.getRenglonsemielaboradosById().size();
                renglonvariante.getRenglonjuegosById().size();
                double costoMano = util.calcCostoManoRVinEmpresaObra(renglonvariante);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn()).reduce(0.0, Double::sum);
                double cant = getVolumenUORV(unidadobrarenglon);

                System.out.println("Imprimiendo Cantidad 1: " + cant + " --- " + renglonvariante.getRs());

                Unidadobrarenglon newUORV = new Unidadobrarenglon();
                newUORV.setRenglonvarianteId(renglonvariante.getId());
                newUORV.setCantRv(cant);
                newUORV.setCostMano(cant * costoMano);
                newUORV.setCostEquip(cant * costoEq);
                if (unidadobrarenglon.getConMat().trim().equals("1")) {
                    double mat = costMater + costMaterSemi + costMaterJueg;
                    newUORV.setCostMat(cant * mat);
                    newUORV.setConMat("1");
                } else if (unidadobrarenglon.getConMat().trim().equals("0")) {
                    newUORV.setCostMat(0.0);
                    newUORV.setConMat("0");
                }

                newUORV.setSalariomn(cant * util.calcSalarioRV(renglonvariante));
                newUORV.setSalariocuc(0.0);
                newUORV.setUnidadobraId(idNUO);
                unidadobrarengTorecalc.add(newUORV);
            }
        }

        return unidadobrarengTorecalc;
    }

    private List<Renglonnivelespecifico> newNiveRenglonnivelespecificoList266(Nivelespecifico unidadobra, int idNUO, int idSalar) {
        List<Renglonnivelespecifico> unidadobrarengTorecalc = new ArrayList<>();
        Nivelespecifico nivelespecifico = getNivelEspecifico(idNUO);
        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(nivelespecifico.getObraId(), nivelespecifico.getEmpresaconstructoraId(), nivelespecifico.getObraByObraId().getTarifaId());
        for (Renglonnivelespecifico unidadobrarenglon : unidadobra.getRenglonnivelespecificosById()) {
            Renglonvariante renglonvariante = getRenglonvarianteR266(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo(), idSalar);
            if (renglonvariante == null) {
                System.out.println(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo() + " - " + " no existe!!");
            } else {
                renglonvariante.getRenglonrecursosById().size();
                renglonvariante.getRenglonsemielaboradosById().size();
                renglonvariante.getRenglonjuegosById().size();
                double costoMano = util.calcCostoManoRVinEmpresaObra(renglonvariante);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn()).reduce(0.0, Double::sum);
                double cant = unidadobrarenglon.getCantidad();

                Renglonnivelespecifico newUORV = new Renglonnivelespecifico();
                newUORV.setCantidad(cant);
                newUORV.setRenglonvarianteId(renglonvariante.getId());
                newUORV.setCostmano(cant * costoMano);
                newUORV.setCostequipo(cant * costoEq);
                if (unidadobrarenglon.getConmat().trim().equals("1")) {
                    double mat = costMater + costMaterSemi + costMaterJueg;
                    newUORV.setCostmaterial(cant * mat);
                    newUORV.setConmat("1");
                } else if (unidadobrarenglon.getConmat().trim().equals("0")) {
                    newUORV.setCostmaterial(0.0);
                    newUORV.setConmat("0");
                }
                newUORV.setSalario(cant * util.calcSalarioRV(renglonvariante));
                newUORV.setSalariocuc(0.0);
                newUORV.setNivelespecificoId(idNUO);
                unidadobrarengTorecalc.add(newUORV);
            }
        }
        return unidadobrarengTorecalc;
    }


    private String getCodeSuministro(Bajoespecificacion bajoespecificacion) {
        return util.listSuministros.parallelStream().filter(item -> item.getId() == bajoespecificacion.getIdSuministro() && item.getTipo().trim().equals(bajoespecificacion.getTipo())).findFirst().map(SuministrosView::getCodigo).orElse(" - ");
    }

    private String getCodeSuministroRV(Bajoespecificacionrv bajoespecificacion) {
        return util.listSuministros.parallelStream().filter(item -> item.getId() == bajoespecificacion.getIdsuministro() && item.getTipo().trim().equals(bajoespecificacion.getTipo())).findFirst().map(SuministrosView::getCodigo).orElse(" - ");
    }

    private SuministrosView getSuministroToView(String code, int idO) {
        SuministrosView suministrosView = null;
        List<SuministrosView> list = util.listSuministros.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).collect(Collectors.toList());
        for (SuministrosView view : list) {
            if (view.getPertene() != null) {
                if (view.getPertene().trim().equals("R266")) {
                    suministrosView = view;
                    return suministrosView;
                } else if (view.getPertene().trim().equals(String.valueOf(idO))) {
                    suministrosView = view;
                    return suministrosView;
                } else if (view.getPertene().trim().equals("I")) {
                    suministrosView = view;
                    return suministrosView;
                } else if (view.getPertene() != null) {
                    suministrosView = view;
                    return suministrosView;
                }
            }
        }
        return suministrosView;
    }

    private List<Bajoespecificacion> newBajoEspecificacionListR266(Unidadobra unidadobra, int idNUO) {
        List<Bajoespecificacion> bajoespecificacionsToRecalc = new ArrayList<>();
        unidadobra.getUnidadobrabajoespecificacionById().size();
        for (Bajoespecificacion bajoespecificacion : unidadobra.getUnidadobrabajoespecificacionById()) {
            double cant = getVolumenBajo(bajoespecificacion);
            SuministrosView suministrosView = getSuministroToView(getCodeSuministro(bajoespecificacion), unidadobra.getObraId());
            if (suministrosView != null) {
                Bajoespecificacion bajo = new Bajoespecificacion();
                bajo.setCantidad(cant);
                bajo.setCosto(cant * suministrosView.getPreciomn());
                bajo.setIdSuministro(suministrosView.getId());
                bajo.setTipo(suministrosView.getTipo());
                bajo.setUnidadobraId(idNUO);
                bajoespecificacionsToRecalc.add(bajo);
            } else {
                System.out.println(bajoespecificacion.getIdSuministro());
                System.out.println(getCodeSuministro(bajoespecificacion));
            }
        }
        return bajoespecificacionsToRecalc;
    }

    private List<Bajoespecificacionrv> newBajoEspecificacionRVListR266(Nivelespecifico unidadobra, int idNUO, int idSal) {
        List<Bajoespecificacionrv> bajoespecificacionsToRecalc = new ArrayList<>();
        unidadobra.getBajoespecificacionrvsById().size();
        for (Bajoespecificacionrv bajoespecificacion : unidadobra.getBajoespecificacionrvsById()) {
            Renglonvariante renglonvariante = getRenglonvarianteR266(bajoespecificacion.getRenglonvarianteByRenglonvarianteId().getCodigo(), idSal);
            SuministrosView suministrosView = getSuministroToView(getCodeSuministroRV(bajoespecificacion), unidadobra.getObraId());
            if (suministrosView != null) {
                Bajoespecificacionrv bajo = new Bajoespecificacionrv();
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCantidad() * suministrosView.getPreciomn());
                bajo.setIdsuministro(suministrosView.getId());
                bajo.setTipo(suministrosView.getTipo());
                bajo.setNivelespecificoId(idNUO);
                bajo.setRenglonvarianteId(renglonvariante.getId());
                bajoespecificacionsToRecalc.add(bajo);
            }
        }
        return bajoespecificacionsToRecalc;
    }

    public void updateUnidadesObra(List<Unidadobra> unidadobras) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobra unidad : unidadobras) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(unidad);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNivelEspecifico(List<Nivelespecifico> unidadobras) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Nivelespecifico unidad : unidadobras) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(unidad);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajoespecificacion");
            alert.showAndWait();
        }

    }

    public void updateEmpresaObraSalario(List<Empresaobrasalario> listSalarios) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Empresaobrasalario salario : listSalarios) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(salario);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText(" Empresaobrasalario error");
            alert.showAndWait();
        }

    }

    public void deleteEmpresaObraSalario(Empresaobrasalario empresaobrasalario) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.delete(empresaobrasalario);

            tx.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateObra(Obra obra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.update(obra);

            tx.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUnidadObra(List<Unidadobra> list) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            countUOR = 0;
            for (Unidadobra unid : list) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                Unidadobra unidadobra = session.get(Unidadobra.class, unid.getId());
                if (unidadobra != null) {

                    int op1 = session.createQuery("DELETE FROM Unidadobrarenglon WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op2 = session.createQuery("DELETE FROM Bajoespecificacion WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op3 = session.createQuery("DELETE FROM Planificacion WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op4 = session.createQuery("DELETE FROM Certificacion WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op5 = session.createQuery("DELETE FROM Memoriadescriptiva WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op6 = session.createQuery("DELETE FROM Certificacionrecuo WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                    int op7 = session.createQuery("DELETE FROM Planrecuo WHERE unidadobraId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();

                    session.delete(unidadobra);
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
    }

    public Integer saveUnidadobra(Unidadobra unidadobra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(unidadobra);

            tx.commit();
            session.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public Integer saveNivelEspecifico(Nivelespecifico unidadobra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(unidadobra);

            tx.commit();
            session.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public void persistUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void persistNivelEspecificoRenglones(List<Renglonnivelespecifico> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Renglonnivelespecifico unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void persistBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajoespecificación");
            alert.showAndWait();
        }

    }

    public void persistBajoespecificacionrvList(List<Bajoespecificacionrv> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Bajoespecificacionrv bajoespecificacion : bajoespecificacionList) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajoespecificación");
            alert.showAndWait();
        }

    }

    private void createEmprasaObra(Collection<Empresaobra> empresaobrasById, Integer id) {
        List<Empresaobra> empresaobras = new ArrayList<>();
        for (Empresaobra empresaobra : empresaobrasById) {
            Empresaobra eo = new Empresaobra();
            eo.setObraId(id);
            eo.setEmpresaconstructoraId(empresaobra.getEmpresaconstructoraId());
            empresaobras.add(eo);
        }

        util.caddNuevaObraEmpresa(empresaobras);
    }

    /*
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
        util.caddNuevaEmpresObraConceptos(empresaobraconceptos);
    }
*/
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

        util.caddNuevoCoeficienteEquipo(coeficientesequipos);
    }

    private void createZonas(Collection<Zonas> zonasById, Integer id) {
        idZona = 0;
        for (Zonas zon : zonasById) {
            Zonas zonas = new Zonas();
            zonas.setObraId(id);
            zonas.setCodigo(zon.getCodigo());
            zonas.setDesripcion(zon.getDesripcion());
            idZona = util.createZona(zonas);
            createObjetosByZona(idZona, zon);
        }

    }

    private void createObjetosByZona(int idZona, Zonas zon) {
        List<Objetos> objetosList = util.getObjetosbyZona(zon.getId());
        for (Objetos items : objetosList) {
            Objetos objetos = new Objetos();
            objetos.setZonasId(idZona);
            objetos.setCodigo(items.getCodigo());
            objetos.setDescripcion(items.getDescripcion());

            int idObj = util.createObjetos(objetos);
            List<Nivel> nivelList = util.getNivelbyOb(items.getId());
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
        util.createNiveles(list);
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


    public void handleBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


    private void deleteObras(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Obra obra = session.get(Obra.class, id);
            if (obra != null) {

                int delete1 = session.createQuery("DELETE FROM Empresaobrasalario WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete2 = session.createQuery("DELETE FROM Empresaobra WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete3 = session.createQuery("DELETE FROM Empresaobraconcepto WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int delete4 = session.createQuery("DELETE FROM Empresaobraconceptoglobal WHERE obraId =: id").setParameter("id", obra.getId()).executeUpdate();
                int dalete5 = session.createQuery("DELETE FROM Coeficientesequipos WHERE obraId =: idOb").setParameter("idOb", obra.getId()).executeUpdate();
                int delete6 = session.createQuery("DELETE FROM Empresaobraconceptoscoeficientes WHERE obraId =: idOb").setParameter("idOb", obra.getId()).executeUpdate();

                deleteRecursosBajo(obra.getId());
                deleteAllCompontes(obra.getId());


            }
            session.delete(obra);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void deleteRecursosBajo(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String queryStr = "select bajo.id, bajo.nivelespecifico__id from bajoespecificacionrv bajo inner join nivelespecifico uo on bajo.nivelespecifico__id = uo.id WHERE uo.obra__id =" + id;
            Query query = session.createSQLQuery(queryStr);
            List<Object[]> dataList = query.getResultList();
            for (Object[] row : dataList) {
                int op1 = session.createQuery("Delete from Bajoespecificacionrv WHERE id=:idS").setParameter("idS", Integer.parseInt(row[0].toString())).executeUpdate();
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

    private void deleteAllCompontes(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> unidadobras = session.createQuery("FROM Nivelespecifico WHERE obraId =: id").setParameter("id", id).getResultList();
            for (Nivelespecifico uo : unidadobras) {
                int op1 = session.createQuery("DELETE FROM Renglonnivelespecifico WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op2 = session.createQuery("DELETE FROM Planrenglonvariante WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op3 = session.createQuery("DELETE FROM Planrecrv WHERE nivelespId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op4 = session.createQuery("DELETE FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idU").setParameter("idU", uo.getId()).executeUpdate();
                int op5 = session.createQuery("DELETE FROM Certificacionrecrv WHERE nivelespId =: idU").setParameter("idU", uo.getId()).executeUpdate();

                session.delete(uo);

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


}
