package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SuplementarUnidadObraController implements Initializable {

    public static List<Empresaobratarifa> empresaobratarifaList;
    public SuplementarUnidadObraController nuevaUnidadObraController;
    public Empresaobrasalario empresaobrasalario;
    public List<Renglonrecursos> listRecursos;
    public UnidadesObraObraController obraObraController;
    public double valorEscala;
    public double salarioCalc;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_close;
    private Integer inputLength = 7;
    private ArrayList<Unidadobra> listCheck;
    private Grupoejecucion grupoejecucion;
    private Especialidades especialidades;
    @FXML
    private Label title;
    private List<Unidadobrarenglon> unidadobrarenglonList;
    private List<Bajoespecificacion> bajoespecificacionList;
    private List<Certificacionrecuo> certificacionrecuoList;
    private Unidadobra unidadobra;
    private int batchSizeUOR = 10;
    private int countUOR;
    private int batchbajo = 10;
    private int countbajo;
    private Renglonvariante renglonvariante;
    private List<Unidadobrarenglon> unidadobrarengToSuplement;
    private List<Bajoespecificacion> bajoespecificacionsToSuplement;
    private List<Unidadobrarenglon> unidadobrarengTorecalc;
    private List<Bajoespecificacion> bajoespecificacionsToRecalc;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public ArrayList<Unidadobra> getUnidadObraList() {

        listCheck = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listCheck = (ArrayList<Unidadobra>) session.createQuery("From Unidadobra ").list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listCheck;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonList(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobrarenglon> unidadobrarenglons = new ArrayList<>();
            unidadobrarenglons = session.createQuery(" FROM Unidadobrarenglon WHERE unidadobraId =: idU ").setParameter("idU", id).getResultList();

            tx.commit();
            session.close();
            return unidadobrarenglons;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacion> getBajoespecificacionList(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacion> bajoespecificacions = new ArrayList<>();
            bajoespecificacions = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: idU ").setParameter("idU", id).getResultList();

            tx.commit();
            session.close();
            return bajoespecificacions;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacionrecuo> getCertificacionrecuoList(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Certificacionrecuo> certificacionrecuos = new ArrayList<>();
            certificacionrecuos = session.createQuery(" FROM Certificacionrecuo WHERE unidadobraId =: idU ").setParameter("idU", id).getResultList();

            tx.commit();
            session.close();
            return certificacionrecuos;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Recursos getRecursos(int idR) {
        Recursos recursos = null;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            recursos = session.get(Recursos.class, idR);

            tx.commit();
            session.close();
            return recursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return recursos;
    }

    public Suministrossemielaborados getSuministrossemielaborados(int idSumin) {
        Suministrossemielaborados suministrossemielaborados = null;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            suministrossemielaborados = session.get(Suministrossemielaborados.class, idSumin);

            tx.commit();
            session.close();
            return suministrossemielaborados;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrossemielaborados;
    }

    public Juegoproducto getJuegoproducto(int idJue) {
        Juegoproducto juegoproducto = null;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            juegoproducto = session.get(Juegoproducto.class, idJue);

            tx.commit();
            session.close();
            return juegoproducto;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return juegoproducto;
    }

    public Especialidades getEspecialidades(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidades = session.get(Especialidades.class, id);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return especialidades;
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

    public Integer AddUnidadObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer unidId = null;
        try {
            tx = session.beginTransaction();

            unidId = (Integer) session.save(unidadobra);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidId;
    }

    public void updateUnidadObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nuevaUnidadObraController = this;

    }

    public void createUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

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

    public void createBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {

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
            alert.setContentText("Error al crear los RV");
            alert.showAndWait();
        }

    }

    public void updateBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {

                Integer op1 = session.createQuery("DELETE FROM  Bajoespecificacion  WHERE unidadobraId =: idU AND idSuministro=: idS").setParameter("idU", bajoespecificacion.getUnidadobraId()).setParameter("idS", bajoespecificacion.getIdSuministro()).executeUpdate();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    @FXML
    public void addNewUnidad(ActionEvent event) {

        Unidadobra unidadobraToSup = conformUnidadObraToSuplement();
        Unidadobra unidadobra = getUnidadObraList().parallelStream().filter(uni -> uni.getCodigo().equals(unidadobraToSup.getCodigo()) && uni.getEmpresaconstructoraId() == unidadobraToSup.getEmpresaconstructoraId() && uni.getZonasId() == unidadobraToSup.getZonasId() && uni.getObjetosId() == unidadobraToSup.getObjetosId() && uni.getNivelId() == unidadobraToSup.getNivelId() && uni.getEspecialidadesId() == unidadobraToSup.getEspecialidadesId() && uni.getSubespecialidadesId() == unidadobraToSup.getSubespecialidadesId()).findFirst().orElse(null);

        if (unidadobra != null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("El código de la unidad de obra ya existe!!");
            alert.showAndWait();
        } else {
            if (unidadobraToSup.getCodigo().length() == 7) {
                Integer id = AddUnidadObra(unidadobraToSup);
                if (id != null) {

                    List<Unidadobrarenglon> datosRen = new ArrayList<>();
                    for (Unidadobrarenglon unidadobrarenglon : unidadobrarengToSuplement) {
                        unidadobrarenglon.setUnidadobraId(id);
                        datosRen.add(unidadobrarenglon);
                    }
                    createUnidadesObraRenglones(datosRen);

                    List<Bajoespecificacion> datosBajo = new ArrayList<>();
                    for (Bajoespecificacion bajoespecificacion : bajoespecificacionsToSuplement) {
                        bajoespecificacion.setUnidadobraId(id);
                        datosBajo.add(bajoespecificacion);
                    }
                    createBajoespecificacion(datosBajo);
                    obraObraController.handleCargardatos(event);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Información");
                alert.setContentText("El código de la unidad de obra debe tener 7 dígitos");
                alert.showAndWait();
            }
        }

        Unidadobra uo = conformUnidadObraToRecal();
        updateUnidadObra(uo);
        updateUnidadesObraRenglones(unidadobrarengTorecalc);
        updateBajoespecificacion(bajoespecificacionsToRecalc);
        createBajoespecificacion(bajoespecificacionsToRecalc);

    }

    private Unidadobra conformUnidadObraToSuplement() {

        double cant = getcantCertfificada(unidadobra);
        double disp = unidadobra.getCantidad() - cant;
        double cMaterial = bajoespecificacionListSuplement().parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
        double cMano = unidadobrarenglonListSuplement().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
        double cEquip = unidadobrarenglonListSuplement().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
        double cMatR = unidadobrarenglonListSuplement().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
        double cSalario = unidadobrarenglonListSuplement().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);


        double total = cMaterial + cMatR + cMano + cEquip;
        double unitario = total / disp;

        Unidadobra newUnidad = new Unidadobra();
        newUnidad.setObraId(unidadobra.getObraId());
        newUnidad.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
        newUnidad.setZonasId(unidadobra.getZonasId());
        newUnidad.setObjetosId(unidadobra.getObjetosId());
        newUnidad.setNivelId(unidadobra.getNivelId());
        newUnidad.setEspecialidadesId(unidadobra.getEspecialidadesId());
        newUnidad.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
        newUnidad.setCodigo(field_codigo.getText());
        newUnidad.setDescripcion(field_descripcion.getText());
        newUnidad.setUm(unidadobra.getUm());
        newUnidad.setCantidad(disp);
        newUnidad.setCostototal(total);
        newUnidad.setCostoequipo(cEquip);
        newUnidad.setCostomano(cMano);
        newUnidad.setCostoMaterial(cMaterial + cMatR);
        newUnidad.setCostounitario(unitario);
        newUnidad.setRenglonbase(unidadobra.getRenglonbase());
        newUnidad.setSalario(cSalario);
        newUnidad.setSalariocuc(unidadobra.getSalariocuc());
        if (unidadobra.getGrupoejecucionId() == null) {
            newUnidad.setGrupoejecucionId(1);
        } else {
            newUnidad.setGrupoejecucionId(unidadobra.getGrupoejecucionId());
        }
        newUnidad.setIdResolucion(unidadobra.getObraByObraId().getSalarioId());

        return newUnidad;
    }

    private Unidadobra conformUnidadObraToRecal() {

        double cant = getcantCertfificada(unidadobra);
        double cMaterial = bajoespecificacionListtoRecalc().parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);
        double cMano = unidadobrarenglonListTorecalc().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
        double cEquip = unidadobrarenglonListTorecalc().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
        double cMatR = unidadobrarenglonListTorecalc().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
        double cSalario = unidadobrarenglonListTorecalc().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);


        double total = cMaterial + cMatR + cMano + cEquip;
        double unitario = total / cant;

        Unidadobra newUnidad = new Unidadobra();
        newUnidad.setId(unidadobra.getId());
        newUnidad.setObraId(unidadobra.getObraId());
        newUnidad.setEmpresaconstructoraId(unidadobra.getEmpresaconstructoraId());
        newUnidad.setZonasId(unidadobra.getZonasId());
        newUnidad.setObjetosId(unidadobra.getObjetosId());
        newUnidad.setNivelId(unidadobra.getNivelId());
        newUnidad.setEspecialidadesId(unidadobra.getEspecialidadesId());
        newUnidad.setSubespecialidadesId(unidadobra.getSubespecialidadesId());
        newUnidad.setCodigo(unidadobra.getCodigo());
        newUnidad.setDescripcion(unidadobra.getDescripcion());
        newUnidad.setUm(unidadobra.getUm());
        newUnidad.setCantidad(cant);
        newUnidad.setCostototal(total);
        newUnidad.setCostoequipo(cEquip);
        newUnidad.setCostomano(cMano);
        newUnidad.setCostoMaterial(cMaterial + cMatR);
        newUnidad.setCostounitario(unitario);
        newUnidad.setRenglonbase(unidadobra.getRenglonbase());
        newUnidad.setSalario(cSalario);
        newUnidad.setSalariocuc(unidadobra.getSalariocuc());
        if (unidadobra.getGrupoejecucionId() == null) {
            newUnidad.setGrupoejecucionId(1);
        } else {
            newUnidad.setGrupoejecucionId(unidadobra.getGrupoejecucionId());
        }
        newUnidad.setIdResolucion(unidadobra.getObraByObraId().getSalarioId());

        return newUnidad;
    }

    public void pasarParametros(UnidadesObraObraController unidadesObraObraController, Unidadobra unidad) {

        unidadobra = utilCalcSingelton.getUnidadobra(unidad.getId());
        title.setText("Suplementar UO: " + unidadobra.getCodigo());

        especialidades = getEspecialidades(unidadobra.getEspecialidadesId());

        bajoespecificacionList = new ArrayList<>();
        bajoespecificacionList = unidadobra.getUnidadobrabajoespecificacionById().stream().collect(Collectors.toList());

        unidadobrarenglonList = new ArrayList<>();
        unidadobrarenglonList = unidadobra.getUnidadobrarenglonsById().stream().collect(Collectors.toList());

        certificacionrecuoList = new ArrayList<>();
        certificacionrecuoList = getCertificacionrecuoList(unidadobra.getId());

        obraObraController = unidadesObraObraController;

        field_descripcion.setText(unidadobra.getDescripcion());

        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId(), unidadobra.getObraByObraId().getTarifaId());

    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void showGenericAsociation(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgrupacionEspecificaSuplementar.fxml"));
            Parent proot = loader.load();

            AgrupacionEspecificaSuplementarController controller = loader.getController();
            controller.pasarEspecialidad(nuevaUnidadObraController, especialidades.getCodigo());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();

            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void unidadCode(String code) {
        field_codigo.setText(code);

    }

    public void handleCloseWindows(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    public double getValorTarifa(String grupo) {
        valorEscala = 0;
        if (grupo.equals("II")) {
            valorEscala = empresaobrasalario.getIi();
        } else if (grupo.equals("III")) {
            valorEscala = empresaobrasalario.getIii();
        } else if (grupo.equals("IV")) {
            valorEscala = empresaobrasalario.getIv();
        } else if (grupo.equals("V")) {
            valorEscala = empresaobrasalario.getV();
        } else if (grupo.equals("VI")) {
            valorEscala = empresaobrasalario.getVi();
        } else if (grupo.equals("VII")) {
            valorEscala = empresaobrasalario.getVii();
        } else if (grupo.equals("VIII")) {
            valorEscala = empresaobrasalario.getViii();
        } else if (grupo.equals("IX")) {
            valorEscala = empresaobrasalario.getIx();
        } else if (grupo.equals("X")) {
            valorEscala = empresaobrasalario.getX();
        }
        return valorEscala;
    }

    public List<Renglonrecursos> getRecursosinRV(int idR) {

        listRecursos = new LinkedList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            renglonvariante = session.get(Renglonvariante.class, idR);
            listRecursos = renglonvariante.getRenglonrecursosById();
            listRecursos.size();

            tx.commit();
            session.close();
            return listRecursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    /*
        private double calcSalarioRV(int idReng) {
            listRecursos = new LinkedList<>();
            listRecursos = getRecursosinRV(idReng);
            salarioCalc = 0.0;
            for (Renglonrecursos item : listRecursos) {
                if (item.getRecursosByRecursosId().getTipo().contentEquals("2")) {
                    salarioCalc += item.getCantidas() * utilCalcSingelton;
                }
            }

            return salarioCalc;
        }
    */
    private List<Unidadobrarenglon> unidadobrarenglonListSuplement() {
        unidadobrarengToSuplement = new ArrayList<>();
        for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonList) {
            double cant = getCantidadRVCertificada(unidadobrarenglon.getCantRv());
            double cantDispo = unidadobrarenglon.getCantRv() - cant;
            Unidadobrarenglon newUORV = new Unidadobrarenglon();
            newUORV.setRenglonvarianteId(unidadobrarenglon.getRenglonvarianteId());
            newUORV.setCantRv(cantDispo);
            newUORV.setCostMano(cantDispo * utilCalcSingelton.calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()));
            newUORV.setCostEquip(cantDispo * unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCostequip());
            if (unidadobrarenglon.getConMat().startsWith("1")) {
                newUORV.setCostMat(cantDispo * unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCostomat());
                newUORV.setConMat("1");
            } else if (unidadobrarenglon.getConMat().startsWith("0")) {
                newUORV.setCostMat(0.0);
                newUORV.setConMat("0");
            }

            newUORV.setSalariomn(cantDispo * utilCalcSingelton.calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()));
            newUORV.setSalariocuc(0.0);
            newUORV.setUnidadobraId(1);

            unidadobrarengToSuplement.add(newUORV);

        }

        return unidadobrarengToSuplement;
    }

    private List<Bajoespecificacion> bajoespecificacionListSuplement() {
        bajoespecificacionsToSuplement = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
            double cant = getCantidadSuministroCertificada(bajoespecificacion.getCantidad());
            Bajoespecificacion newBajo = new Bajoespecificacion();
            double cantDispo = bajoespecificacion.getCantidad() - cant;
            newBajo.setCantidad(cantDispo);
            newBajo.setIdSuministro(bajoespecificacion.getIdSuministro());
            newBajo.setSumrenglon(0);
            if (bajoespecificacion.getTipo().startsWith("1")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cantDispo * getRecursos(bajoespecificacion.getIdSuministro()).getPreciomn());
            } else if (bajoespecificacion.getTipo().startsWith("S")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cantDispo * getSuministrossemielaborados(bajoespecificacion.getIdSuministro()).getPreciomn());
            } else if (bajoespecificacion.getTipo().startsWith("J")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cantDispo * getJuegoproducto(bajoespecificacion.getIdSuministro()).getPreciomn());
            }

            newBajo.setUnidadobraId(1);
            bajoespecificacionsToSuplement.add(newBajo);
        }
        return bajoespecificacionsToSuplement;
    }

    private List<Unidadobrarenglon> unidadobrarenglonListTorecalc() {
        unidadobrarengTorecalc = new ArrayList<>();
        for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonList) {
            double cant = getCantidadRVCertificada(unidadobrarenglon.getCantRv());
            Unidadobrarenglon newUORV = new Unidadobrarenglon();
            newUORV.setRenglonvarianteId(unidadobrarenglon.getRenglonvarianteId());
            newUORV.setCantRv(cant);
            double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
            newUORV.setCostMano(cant * costoMano);
            newUORV.setCostEquip(cant * unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCostequip());
            if (unidadobrarenglon.getConMat().startsWith("1")) {
                newUORV.setCostMat(cant * unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCostomat());
                newUORV.setConMat("1");
            } else if (unidadobrarenglon.getConMat().startsWith("0")) {
                newUORV.setCostMat(0.0);
                newUORV.setConMat("0");
            }

            newUORV.setSalariomn(cant * utilCalcSingelton.calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()));
            newUORV.setSalariocuc(0.0);
            newUORV.setUnidadobraId(unidadobra.getId());

            unidadobrarengTorecalc.add(newUORV);

        }

        return unidadobrarengTorecalc;
    }

    private List<Bajoespecificacion> bajoespecificacionListtoRecalc() {
        bajoespecificacionsToRecalc = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
            double cant = getCantidadSuministroCertificada(bajoespecificacion.getCantidad());
            Bajoespecificacion newBajo = new Bajoespecificacion();
            newBajo.setCantidad(cant);
            newBajo.setIdSuministro(bajoespecificacion.getIdSuministro());
            newBajo.setSumrenglon(0);

            if (bajoespecificacion.getTipo().startsWith("1")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cant * getRecursos(bajoespecificacion.getIdSuministro()).getPreciomn());
            } else if (bajoespecificacion.getTipo().startsWith("S")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cant * getSuministrossemielaborados(bajoespecificacion.getIdSuministro()).getPreciomn());

            } else if (bajoespecificacion.getTipo().startsWith("J")) {
                newBajo.setTipo(bajoespecificacion.getTipo());
                newBajo.setCosto(cant * getJuegoproducto(bajoespecificacion.getIdSuministro()).getPreciomn());
            }

            newBajo.setUnidadobraId(unidadobra.getId());
            bajoespecificacionsToRecalc.add(newBajo);
        }
        return bajoespecificacionsToRecalc;
    }


    private Double getCantidadRVCertificada(double vol) {
        double cantCertUO = unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        double cal = vol * cantCertUO;
        return cal / unidadobra.getCantidad();
    }

    private Double getCantidadSuministroCertificada(double vol) {
        double cantCertUO = unidadobra.getCertificacionsById().parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        double cal = vol * cantCertUO;
        return cal / unidadobra.getCantidad();
    }

}
