package models;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class UtilCalcSingelton {

    public static UtilCalcSingelton instancia = null;
    private static List<Empresaobraconceptoscoeficientes> unidadoEmpresaobraconceptoscoeficientesList;
    private static List<Unidadobrarenglon> unidadobrarenglonList;
    private static Empresaobrasalario empresaobrasalario;
    private static double valorEscala;
    private static double salarioCalc;
    public List<Renglonvariante> renglonvarianteList;
    public static List<Empresaobratarifa> empresaobratarifaList;
    public List<Recursos> catListResources;
    private int countUOR;
    private Task tarea;
    private certificaRenglonSingelton renglonSingelton;
    private planificaRenglonSingleton planificaRenglon;
    private CertificaRecursosRenglonesSingel certificaRecursosRenglonesSingel;
    private PlanificarRecursosRenglones planificarRecursos;
    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    private ReportProjectStructureSingelton eps = ReportProjectStructureSingelton.getInstance();
    public List<TarConceptosBases> tarConceptosBasesList;
    public List<Juegoproducto> catListJuegos;
    public List<Suministrossemielaborados> catListSemielaborados;
    public List<Subgrupo> catSubgrupoList;
    public List<Renglonvariante> catRenglonesList;
    private int batchSizeUOR = 500;

    private UtilCalcSingelton() {
    }

    public static UtilCalcSingelton getInstance() {
        if (instancia == null) {
            instancia = new UtilCalcSingelton();
        }

        return instancia;
    }

    private static List<Empresaobraconceptoscoeficientes> getUnidadoEmpresaobraconceptoscoeficientesList(int idObra, int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadoEmpresaobraconceptoscoeficientesList = new ArrayList<>();
            unidadoEmpresaobraconceptoscoeficientesList = session.createQuery(" from Empresaobraconceptoscoeficientes where obraId =: idO and empresaconstructoraId =: idEm and conceptosgastoId < 4 ").setParameter("idO", idObra).setParameter("idEm", idEmp).getResultList();
            tx.commit();
            session.close();
            return unidadoEmpresaobraconceptoscoeficientesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private static List<Renglonnivelespecifico> datosRenglonNivelList;
    private boolean flag;
    public ArrayList<Recursos> listRecursos;
    public ArrayList<Juegoproducto> listJuego;
    public ArrayList<Suministrossemielaborados> listSuministrossemielaborados;
    public ObservableList<SuministrosView> suministrosViewObservableList;

    public static Empresaobrasalario getEmpresaobrasalario(int idO, int idEmpresa) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaobrasalario = (Empresaobrasalario) session.createQuery(" FROM Empresaobrasalario WHERE obraId =: idObra AND empresaconstructoraId =: idEmp").setParameter("idObra", idO).setParameter("idEmp", idEmpresa).getSingleResult();
            tx.commit();
            session.close();
            return empresaobrasalario;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaobrasalario;
    }

    public static List<Empresaobratarifa> getEmpresaobratarifaList(int idO, int idEmpresa, int idTarifa) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaobratarifaList = new ArrayList<>();
            empresaobratarifaList = session.createQuery(" FROM Empresaobratarifa WHERE obraId =: idObra AND empresaconstructoraId =: idEmp AND tarifaId =: idTarif ").setParameter("idObra", idO).setParameter("idEmp", idEmpresa).setParameter("idTarif", idTarifa).getResultList();
            tx.commit();
            session.close();
            return empresaobratarifaList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public static List<Certificacionrecuo> certificacionrecuoList;
    public static List<Planrecuo> planrecuoList;
    public static List<objectSelect> selectList;
    private Certificacion cert;

    public List<Planificacion> recalculaPlanificacion(Unidadobra unidadobra) {
        List<Planificacion> updatesPlanificacionList = new ArrayList<>();
        unidadobra.getPlanificacionsById().size();
        for (Planificacion plan : unidadobra.getPlanificacionsById()) {
            if (createPlanificacion(unidadobra, plan) != null) {
                updatesPlanificacionList.add(createPlanificacion(unidadobra, plan));
            }
        }
        return updatesPlanificacionList;
    }


    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(1000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }

    public static List<Unidadobra> unidadobras;

    public static List<Unidadobra> getUnidadobrasList(int idObra, int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobras = new ArrayList<>();
            unidadobras = session.createQuery(" from Unidadobra where obraId =: idO and empresaconstructoraId =: idEm").setParameter("idO", idObra).setParameter("idEm", idEmp).getResultList();
            tx.commit();
            session.close();
            return unidadobras;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private static Juegoproducto getJuegoproducto(int idSum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Juegoproducto recursos = session.get(Juegoproducto.class, idSum);
            tx.commit();
            session.close();
            return recursos;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new Juegoproducto();
    }


    public void deleteBajoRV(int idBajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Bajoespecificacionrv bajo = session.get(Bajoespecificacionrv.class, idBajo);
            session.delete(bajo);

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    private List<Unidadobra> unidadobraList;

    public void setUpdateValoresRenglones(List<Nivelespecifico> unidadobraList) {
        List<Renglonnivelespecifico> unidadobrarenglons = new ArrayList<>();
        List<Bajoespecificacionrv> listOfSuministros = new ArrayList<>();

        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Recalculando Presupuesto... por favor, espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        for (Nivelespecifico unidadobra : unidadobraList) {
            unidadobrarenglons.addAll(getRecalculaUORenglonRV(unidadobra));
            listOfSuministros.addAll(recalculaSuministrosInUORV(unidadobra));
        }

        createUnidadesObraRenglonesRV(unidadobrarenglons);
        updatebajoEspecificacionListRV(listOfSuministros);

        for (Nivelespecifico unidadobra : unidadobraList) {
            updateNivelEspecifico(unidadobra);
        }
    }


    private static List<Certificacionrecuo> getCertificacionrecuoList(int idUO, int idCert) {
        return certificacionrecuoList.parallelStream().filter(item -> item.getUnidadobraId() == idUO && item.getCertificacionId() == idCert && item.getTipo().trim().equals("RV")).collect(Collectors.toList());

    }

    private static List<Planrecuo> getPlanrecuoList(int idUO, int idCert) {
        return planrecuoList.parallelStream().filter(item -> item.getUnidadobraId() == idUO && item.getPlanId() == idCert && item.getTipo().trim().equals("RV")).collect(Collectors.toList());

    }

    public List<Certificacion> recalculaCertificaciones(Unidadobra unidadobra) {
        List<Certificacion> updatesCertificacionList = new ArrayList<>();
        unidadobra.getCertificacionsById().size();
        for (Certificacion certificacion : unidadobra.getCertificacionsById()) {
            updatesCertificacionList.add(createCertificacionUO(unidadobra, certificacion));

        }
        return updatesCertificacionList;
    }

    public void setUpdateValoresCertPlan(List<Unidadobra> unidadobraList) {
        List<Certificacion> listOfCertificaciones = new ArrayList<>();
        List<Certificacionrecuo> certificacionrecuorvList = new ArrayList<>();
        List<Planificacion> listOfPlanificaciones = new ArrayList<>();
        List<Planrecuo> listOfPlanrecrvList = new ArrayList<>();
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Recalculando Planes y Certificaciones... por favor, espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        for (Unidadobra unidadobra : unidadobraList) {
            listOfCertificaciones.addAll(recalculaCertificaciones(unidadobra));
            certificacionrecuorvList.addAll(recalculaCertifRecRV(unidadobra));
        }
        updateCertificacionesList(listOfCertificaciones);
        updateCertificacionesrecRVList(certificacionrecuorvList);

        for (Unidadobra unidadobra : unidadobraList) {
            listOfPlanificaciones.addAll(recalculaPlanificacion(unidadobra));
            listOfPlanrecrvList.addAll(recalculaPlanRV(unidadobra));
        }
        updatePlanificacionList(listOfPlanificaciones);
        updatePlanificacionRVList(listOfPlanrecrvList);


    }

    private List<Planrecuo> recalculaPlanRV(Unidadobra unidadobra) {
        List<Planrecuo> datosList = new ArrayList<>();
        for (Planificacion certificacion : unidadobra.getPlanificacionsById()) {
            List<Planrecuo> tempList = new ArrayList<>();
            tempList = getPlanrecuoList(unidadobra.getId(), certificacion.getId());
            datosList.addAll(recalValuesRVPLan(unidadobra, tempList));
        }
        return datosList;
    }

    private List<Certificacionrecuo> recalculaCertifRecRV(Unidadobra unidadobra) {
        List<Certificacionrecuo> datosList = new ArrayList<>();
        for (Certificacion certificacion : unidadobra.getCertificacionsById()) {
            List<Certificacionrecuo> tempList = new ArrayList<>();
            tempList = getCertificacionrecuoList(unidadobra.getId(), certificacion.getId());
            datosList.addAll(recalValuesRV(unidadobra, tempList));
        }
        return datosList;
    }

    private List<Certificacionrecuo> recalValuesRV(Unidadobra unidadobra, List<Certificacionrecuo> tempList) {
        List<Certificacionrecuo> listRecalculada = new ArrayList<>();
        Empresaobrasalario empresaobrasalario = getEmpresaobrasalario(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
        List<Empresaobraconceptoscoeficientes> list = getUnidadoEmpresaobraconceptoscoeficientesList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
        list.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
        double cmano = list.parallelStream().filter(eoc -> eoc.getObraId() == unidadobra.getObraId() && eoc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        double cequipo = list.parallelStream().filter(eoc -> eoc.getObraId() == unidadobra.getObraId() && eoc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        for (Certificacionrecuo certificacionrecuo : tempList) {
            Renglonvariante renglonvariante = unidadobra.getUnidadobrarenglonsById().parallelStream().filter(item -> item.getRenglonvarianteId() == certificacionrecuo.getRenglonId()).map(Unidadobrarenglon::getRenglonvarianteByRenglonvarianteId).findFirst().orElse(null);
            if (renglonvariante != null) {
                double costoMano = calcCostoManoRVinEmpresaObra(renglonvariante);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double valMano = certificacionrecuo.getCantidad() * costoMano * cmano;
                double valEquip = certificacionrecuo.getCantidad() * costoEq * cequipo;
                certificacionrecuo.setCmano(Math.round(valMano * 100d) / 100d);
                certificacionrecuo.setCequipo(Math.round(valEquip * 100d) / 100d);
                if (certificacionrecuo.getCmateriales() != 0.0) {
                    double costMateriales = certificacionrecuo.getCantidad() * renglonvariante.getCostomat();
                    certificacionrecuo.setCmateriales(costMateriales);
                }
                listRecalculada.add(certificacionrecuo);
            } else {
                certificacionrecuo.setCantidad(0.0);
                certificacionrecuo.setCmano(0.0);
                certificacionrecuo.setCequipo(0.0);
                certificacionrecuo.setCmateriales(0.0);
                certificacionrecuo.setSalario(0.0);
                listRecalculada.add(certificacionrecuo);
            }
        }
        return listRecalculada;
    }

    private static Suministrossemielaborados getSuministroSemi(int idSum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, idSum);
            tx.commit();
            session.close();
            return recursos;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new Suministrossemielaborados();
    }


    private double costMaterialBajo;


    private List<Bajoespecificacion> getListSuminsitrosBajoEspecificacion(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
            bajoespecificacionList = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: idU ").setParameter("idU", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    private List<Bajoespecificacionrv> getListSuminsitrosBajoEspecificacion(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacionrv> bajoespecificacionList = new ArrayList<>();
            bajoespecificacionList = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU ").setParameter("idU", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
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

                session.update(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static double getValorTarifa(String grupo) {
        valorEscala = empresaobratarifaList.parallelStream().filter(item -> item.getGrupo().trim().equals(grupo.trim())).findFirst().map(Empresaobratarifa::getEscala).get();
        return valorEscala;
    }

    public static double getValorCosto(String grupo) {
        valorEscala = empresaobratarifaList.parallelStream().filter(item -> item.getGrupo().trim().equals(grupo.trim())).findFirst().map(Empresaobratarifa::getTarifa).get();
        return valorEscala;
    }

    public void updatebajoEspecificacionList(List<Bajoespecificacion> unidadobrarenglonArray) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacion unidadobrarenglon : unidadobrarenglonArray) {
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

    public void updatebajoEspecificacionListRV(List<Bajoespecificacionrv> unidadobrarenglonArray) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacionrv unidadobrarenglon : unidadobrarenglonArray) {
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

    private void updateUnidadObra(Unidadobra unidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Unidadobra unidadobra = session.get(Unidadobra.class, unidad.getId());
            unidadobra.getUnidadobrarenglonsById().size();
            if (unidadobra.getUnidadobrarenglonsById().size() > 0) {
                double costtMaterialInRV = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
                double costMano = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
                double costEquip = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
                double salario = unidadobra.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);
                double costoMaterialBajo = getListSuminsitrosBajoEspecificacion(unidadobra).parallelStream().map(Bajoespecificacion::getCosto).reduce(0.0, Double::sum);

                double total = costtMaterialInRV + costoMaterialBajo + costMano + costEquip;
                unidadobra.setCostototal(Math.round(total * 100d) / 100d);
                unidadobra.setCostoMaterial(costoMaterialBajo + costtMaterialInRV);
                unidadobra.setCostomano(Math.round(costMano * 100d) / 100d);
                unidadobra.setCostoequipo(Math.round(costEquip * 100d) / 100d);
                double unitario = total / unidadobra.getCantidad();
                unidadobra.setCostounitario(Math.round(unitario * 100d) / 100d);
                unidadobra.setSalario(salario);
                unidadobra.setSalariocuc(0.0);
                session.update(unidadobra);
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

    public void createUnidadesObraRenglonesRV(List<Renglonnivelespecifico> unidadobrarenglonArray) {
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

                session.update(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Planrecuo> recalValuesRVPLan(Unidadobra unidadobra, List<Planrecuo> tempList) {
        List<Planrecuo> listRecalculada = new ArrayList<>();
        Empresaobrasalario empresaobrasalario = getEmpresaobrasalario(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
        List<Empresaobraconceptoscoeficientes> list = getUnidadoEmpresaobraconceptoscoeficientesList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
        list.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
        double cmano = list.parallelStream().filter(eoc -> eoc.getObraId() == unidadobra.getObraId() && eoc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        double cequipo = list.parallelStream().filter(eoc -> eoc.getObraId() == unidadobra.getObraId() && eoc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        for (Planrecuo certificacionrecuo : tempList) {
            Renglonvariante renglonvariante = unidadobra.getUnidadobrarenglonsById().parallelStream().filter(item -> item.getRenglonvarianteId() == certificacionrecuo.getRenglonId()).map(Unidadobrarenglon::getRenglonvarianteByRenglonvarianteId).findFirst().orElse(null);
            if (renglonvariante != null) {
                double costoMano = calcCostoManoRVinEmpresaObra(renglonvariante);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double valMano = certificacionrecuo.getCantidad() * costoMano * cmano;
                double valEquip = certificacionrecuo.getCantidad() * costoEq * cequipo;
                certificacionrecuo.setCmano(Math.round(valMano * 100d) / 100d);
                certificacionrecuo.setCequipo(Math.round(valEquip * 100d) / 100d);
                if (certificacionrecuo.getCmateriales() != 0.0) {
                    double costMateriales = certificacionrecuo.getCantidad() * renglonvariante.getCostomat();
                    certificacionrecuo.setCmateriales(costMateriales);
                }
                listRecalculada.add(certificacionrecuo);
            } else {
                certificacionrecuo.setCantidad(0.0);
                certificacionrecuo.setCmano(0.0);
                certificacionrecuo.setCequipo(0.0);
                certificacionrecuo.setCmateriales(0.0);
                certificacionrecuo.setSalario(0.0);
                listRecalculada.add(certificacionrecuo);
            }
        }
        return listRecalculada;
    }

    public void updateCertificacionesList(List<Certificacion> certificacionList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Certificacion certificacion : certificacionList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(certificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlanificacionList(List<Planificacion> planificacionList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Planificacion planificacion : planificacionList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(planificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateCertificacionesrecRVList(List<Certificacionrecuo> certificacionList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Certificacionrecuo certificacion : certificacionList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(certificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlanificacionRVList(List<Planrecuo> planificacionList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Planrecuo planificacion : planificacionList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(planificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Certificacion createCertificacionUO(Unidadobra unidadobraModel, Certificacion certificacion) {
        double valCM = 0.0;
        double valCMan = 0.0;
        double valEquip = 0.0;
        double valSal = 0.0;
        double valToPlan = 0.0;
        valToPlan = unidadobraModel.getCantidad() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(certificacion.getDesde())).map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        if (valToPlan > 0.0) {
            cert = certificacion;
            valCM = unidadobraModel.getCostoMaterial() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(certificacion.getDesde())).map(Certificacion::getCostmaterial).reduce(0.0, Double::sum);
            valCMan = unidadobraModel.getCostomano() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(certificacion.getDesde())).map(Certificacion::getCostmano).reduce(0.0, Double::sum);
            valEquip = unidadobraModel.getCostoequipo() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(certificacion.getDesde())).map(Certificacion::getCostequipo).reduce(0.0, Double::sum);
            valSal = unidadobraModel.getSalario() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(certificacion.getDesde())).map(Certificacion::getSalario).reduce(0.0, Double::sum);

            double costMaterial = valCM * certificacion.getCantidad() / valToPlan;
            double costMano = valCMan * certificacion.getCantidad() / valToPlan;
            double costEquipo = valEquip * certificacion.getCantidad() / valToPlan;
            double costSalario = valSal * certificacion.getCantidad() / valToPlan;

            certificacion.setCostmaterial(Math.round(costMaterial * 100d) / 100d);
            certificacion.setCostmano(Math.round(costMano * 100d) / 100d);
            certificacion.setCostequipo(Math.round(costEquipo * 100d) / 100d);
            certificacion.setSalario(Math.round(costSalario * 100d) / 100d);
            certificacion.setCucsalario(0.0);
        }
        return cert;
    }

    public Planificacion createPlanificacion(Unidadobra unidadobraModel, Planificacion planificacion) {
        planificaRenglon = planificaRenglonSingleton.getInstance();
        planificarRecursos = PlanificarRecursosRenglones.getInstance();
        Planificacion plan = new Planificacion();
        double valCM = 0.0;
        double valCMan = 0.0;
        double valEquip = 0.0;
        double valSal = 0.0;

        double valToPlan = 0.0;
        valToPlan = unidadobraModel.getCantidad() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(planificacion.getDesde())).map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        if (valToPlan > 0.0) {
            plan = planificacion;
            valCM = unidadobraModel.getCostoMaterial() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(planificacion.getDesde())).map(Certificacion::getCostmaterial).reduce(0.0, Double::sum);
            valCMan = unidadobraModel.getCostomano() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(planificacion.getDesde())).map(Certificacion::getCostmano).reduce(0.0, Double::sum);
            valEquip = unidadobraModel.getCostoequipo() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(planificacion.getDesde())).map(Certificacion::getCostequipo).reduce(0.0, Double::sum);
            valSal = unidadobraModel.getSalario() - unidadobraModel.getCertificacionsById().parallelStream().filter(certif -> certif.getDesde().before(planificacion.getDesde())).map(Certificacion::getSalario).reduce(0.0, Double::sum);


            double costMaterial = valCM * planificacion.getCantidad() / valToPlan;
            double costMano = valCMan * planificacion.getCantidad() / valToPlan;
            double costEquipo = valEquip * planificacion.getCantidad() / valToPlan;
            double costSalario = valSal * planificacion.getCantidad() / valToPlan;


            planificacion.setCostomaterial(Math.round(costMaterial * 100d) / 100d);
            planificacion.setCostomano(Math.round(costMano * 100d) / 100d);
            planificacion.setCostoequipo(Math.round(costEquipo * 100d) / 100d);
            planificacion.setCostosalario(Math.round(costSalario * 100d) / 100d);
            planificacion.setCostsalariocuc(0.0);

            //planificaRenglon.cargarDatos(planificacion.getUnidadobraId(), planificacion.getId(), planificacion.getDesde(), planificacion.getHasta(), planificacion.getCantidad(), valToPlan);
            //planificarRecursos.cargarDatos(unidadobraModel.getId(), planificacion.getId(), planificacion.getDesde(), planificacion.getHasta(), planificacion.getCantidad(), valToPlan);

        } else {
            plan = null;
        }
        return plan;

    }

    public double calcSalarioRV(Renglonvariante renglonvariante) {
        salarioCalc = 0.0;
        salarioCalc = renglonvariante.getRenglonrecursosById().parallelStream().filter(recursos -> recursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * getValorTarifa(renglonrecursos.getRecursosByRecursosId().getGrupoescala())).reduce(0.0, Double::sum);
        return salarioCalc;
    }

    private static double cantCertItem;
    public List<Recursos> resultList;
    public List<Juegoproducto> juegoproductoList;
    public List<Suministrossemielaborados> suministrossemielaboradosArrayList;
    public List<Empresaobrasalario> empresaobrasalarioList;
    public List<Salario> salarioList;
    private int count;
    private int batchSize = 10;
    private int batchbajo = 100;
    private int countbajo;

    public void createEmpresaobratarifa(List<Empresaobratarifa> unidadobrarenglonArray) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Empresaobratarifa unidadobrarenglon : unidadobrarenglonArray) {
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

    public static double getCantCertItemRV(int uoid, int idItem, int tipo) {
        cantCertItem = 0.0;
        if (tipo == 1) {
            cantCertItem = certificacionrecuoList.parallelStream().filter(cert -> cert.getUnidadobraId() == uoid && cert.getRenglonId() == idItem).map(Certificacionrecuo::getCantidad).reduce(0.0, Double::sum);
        } else if (tipo == 2) {
            cantCertItem = certificacionrecuoList.parallelStream().filter(cert -> cert.getUnidadobraId() == uoid && cert.getRecursoId() == idItem).map(Certificacionrecuo::getCantidad).reduce(0.0, Double::sum);
        }

        return cantCertItem;
    }

    public List<Recursos> recursosEquiposList;

    public List<Unidadobrarenglon> getRecalculaUORenglon(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobrarenglonList = new ArrayList<>();
            Unidadobra unidad = session.get(Unidadobra.class, unidadobra.getId());
            List<Empresaobraconceptoscoeficientes> list = getUnidadoEmpresaobraconceptoscoeficientesList(unidad.getObraId(), unidad.getEmpresaconstructoraId());
            list.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
            double cmano = list.parallelStream().filter(eoc -> eoc.getObraId() == unidad.getObraId() && eoc.getEmpresaconstructoraId() == unidad.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            double cequipo = list.parallelStream().filter(eoc -> eoc.getObraId() == unidad.getObraId() && eoc.getEmpresaconstructoraId() == unidad.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            List<Empresaobratarifa> empresaobratarifaList = getEmpresaobratarifaList(unidad.getObraId(), unidad.getEmpresaconstructoraId(), unidad.getObraByObraId().getTarifaId());
            if (unidad.getUnidadobrarenglonsById().size() > 0) {
                for (Unidadobrarenglon unidadobrarenglon : unidad.getUnidadobrarenglonsById()) {
                    if (unidadobrarenglon.getConMat().trim().equals("0")) {
                        //double costoMano = new BigDecimal(String.format("%.2f", calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()))).doubleValue();
                        double costoMano = calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        //double costoEq = new BigDecimal(String.format("%.2f", unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum))).doubleValue();
                        double costoEq = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);

                        double valMano = unidadobrarenglon.getCantRv() * costoMano * cmano;
                        double valEquip = unidadobrarenglon.getCantRv() * costoEq * cequipo;
                        unidadobrarenglon.setCostMano(Math.round(valMano * 100d) / 100d);
                        unidadobrarenglon.setCostEquip(Math.round(valEquip * 100d) / 100d);
                        double salario = calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        unidadobrarenglon.setSalariomn(salario * unidadobrarenglon.getCantRv());
                        unidadobrarenglonList.add(unidadobrarenglon);
                    } else if (unidadobrarenglon.getConMat().trim().equals("1")) {
                        //double costoMano = new BigDecimal(String.format("%.2f", calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()))).doubleValue();
                        double costoMano = calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        //double costoEq = new BigDecimal(String.format("%.2f", unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum))).doubleValue();
                        double costoEq = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);

                        double costMater = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double costMaterSemi = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double costMaterJueg = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double valMano = unidadobrarenglon.getCantRv() * costoMano * cmano;
                        double valEquip = unidadobrarenglon.getCantRv() * costoEq * cequipo;
                        //double mat = new BigDecimal(String.format("%.2f", costMater + costMaterSemi + costMaterJueg)).doubleValue();
                        double mat = costMater + costMaterSemi + costMaterJueg;
                        unidadobrarenglon.setCostMat(mat * unidadobrarenglon.getCantRv());
                        unidadobrarenglon.setCostMano(Math.round(valMano * 100d) / 100d);
                        unidadobrarenglon.setCostEquip(Math.round(valEquip * 100d) / 100d);
                        //double salario = new BigDecimal(String.format("%.2f", calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()))).doubleValue();
                        double salario = calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        unidadobrarenglon.setSalariomn(salario * unidadobrarenglon.getCantRv());
                        unidadobrarenglonList.add(unidadobrarenglon);
                    }
                }
            }
            tx.commit();
            session.close();
            return unidadobrarenglonList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Renglonvariante> getAllRenglones() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonvarianteList = new ArrayList<>();
            renglonvarianteList = session.createQuery(" from Renglonvariante ").getResultList();

            tx.commit();
            session.close();
            return renglonvarianteList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacionrecuo> getAllCertificacionesRec(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionrecuoList = new ArrayList<>();
            List<Object[]> datos = session.createQuery(" from Certificacionrecuo cert inner join Unidadobra uo On cert.unidadobraId = uo.id WHERE uo.obraId =: id ").setParameter("id", idObra).getResultList();
            for (Object[] dato : datos) {
                certificacionrecuoList.add((Certificacionrecuo) dato[0]);
            }

            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planrecuo> getAllPlanifuoRv(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planrecuoList = new ArrayList<>();
            List<Object[]> datos = session.createQuery(" from Planrecuo cert inner join Unidadobra uo On cert.unidadobraId = uo.id WHERE uo.obraId =: id ").setParameter("id", idObra).getResultList();
            for (Object[] dato : datos) {
                planrecuoList.add((Planrecuo) dato[0]);
            }

            tx.commit();
            session.close();
            return planrecuoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }


    public ObservableList<SuministrosView> listSuministros;
    private double salario;

    public List<Empresaobrasalario> getEmpresaObraSalarios() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaobrasalarioList = new ArrayList<>();
            empresaobrasalarioList = session.createQuery("FROM Empresaobrasalario ").getResultList();

            tx.commit();
            session.close();
            return empresaobrasalarioList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    public List<Salario> getSalarios() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salarioList = new ArrayList<>();
            salarioList = session.createQuery("FROM Salario ORDER BY id ASC ").getResultList();

            tx.commit();
            session.close();
            return salarioList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void fixesSalarioTable() {
        if (salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Resolución 30")).findFirst().map(Salario::getDescripcion).orElse(null) != null) {
            List<Salario> temp = new ArrayList<>();
            Salario sal = salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Resolución 86")).findFirst().get();
            sal.setTag("T15");
            temp.add(sal);
            Salario prII = salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Resolución 30")).findFirst().get();
            prII.setDescripcion("Precons II");
            prII.setTag("I");
            temp.add(prII);
            Salario r266 = salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals("Resolución 266")).findFirst().orElse(null);
            if (r266 != null) {
                r266.setTag("R266");
                temp.add(r266);
            }
            updateSalarioList(temp);
        }
    }

    private void updateSalarioList(List<Salario> temp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Salario sal : temp) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(sal);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSalario(Salario sal) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(sal);
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Integer addNuevaObra(Obra obra) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(obra);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public Integer addNuevoPlan(Planificacion plan) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(plan);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public Integer addNuevoPlanRenglon(Planrenglonvariante plan) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(plan);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public Integer addNuevaCertificacion(Certificacion certificacion) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(certificacion);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public Integer addNuevaCertificacionRenglon(CertificacionRenglonVariante certificacion) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(certificacion);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public void caddNuevaObraEmpresa(List<Empresaobra> empresaobraList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Empresaobra eo : empresaobraList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }
    }

    public void createListPlarec(List<Planrecuo> empresaobraList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Planrecuo eo : empresaobraList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }
    }

    public void createListPlarecRenglon(List<Planrecrv> empresaobraList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Planrecrv eo : empresaobraList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }
    }

    public void createListCertific(List<Certificacionrecuo> empresaobraList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Certificacionrecuo eo : empresaobraList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }
    }

    public void createListCertificaRecRenglon(List<Certificacionrecrv> empresaobraList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Certificacionrecrv eo : empresaobraList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }
    }

    public void caddNuevaEmpresObraConceptos(List<Empresaobraconcepto> empresaobraconceptoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Empresaobraconcepto eo : empresaobraconceptoList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public void caddNuevoCoeficienteEquipo(List<Coeficientesequipos> coeficientesequiposList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Coeficientesequipos eo : coeficientesequiposList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public Integer createZona(Zonas zonas) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(zonas);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public List<Objetos> getObjetosbyZona(int idZon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Zonas z = session.get(Zonas.class, idZon);
            z.getObjetosById().size();

            tx.commit();
            session.close();
            return z.getObjetosById().stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Integer createObjetos(Objetos objetos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(objetos);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public List<Nivel> getNivelbyOb(int idObj) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Objetos objetos = session.get(Objetos.class, idObj);
            objetos.getNivelsById().size();

            tx.commit();
            session.close();
            return objetos.getNivelsById().stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void createNiveles(List<Nivel> nivelList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Nivel niv : nivelList) {
                countUOR++;
                if (countUOR > 0 && countUOR % countUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(niv);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Error al importar el Nivel");
            alert.showAndWait();
        }
    }

    public List<Zonas> getZonasListFull(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Zonas> list = new ArrayList<>();
            list = session.createQuery("FROM Zonas WHERE obraId =: idObra").setParameter("idObra", idObra).getResultList();

            tx.commit();
            session.close();
            return list;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Objetos> getObjetosListFull(int idObr) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Objetos> objetosList = new ArrayList<>();
            List<Tuple> results = session.createQuery("SELECT obj.id, obj.codigo, obj.descripcion, obj.zonasId FROM Objetos obj INNER JOIN Zonas zon ON obj.zonasId = zon.id WHERE zon.obraId =: idOb", Tuple.class).setParameter("idOb", idObr).getResultList();
            for (Tuple result : results) {
                Objetos obj = new Objetos();
                obj.setId(Integer.parseInt(result.get(0).toString()));
                obj.setCodigo(result.get(1).toString());
                obj.setDescripcion(result.get(2).toString());
                obj.setZonasId(Integer.parseInt(result.get(3).toString()));
                objetosList.add(obj);
            }
            tx.commit();
            session.close();
            return objetosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Nivel> getNivelesByObra(int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Nivel> nivelList = new ArrayList<>();
            List<Tuple> results = session.createQuery("SELECT niv.id, niv.codigo, niv.descripcion, niv.objetosId FROM Nivel niv INNER JOIN Objetos obj ON niv.objetosId = obj.id INNER JOIN Zonas zon ON  obj.zonasId = zon.id WHERE zon.obraId =: idZ", Tuple.class).setParameter("idZ", idOb).getResultList();
            for (Tuple result : results) {
                Nivel nivel = new Nivel();
                nivel.setId(Integer.parseInt(result.get(0).toString()));
                nivel.setCodigo(result.get(1).toString());
                nivel.setDescripcion(result.get(2).toString());
                nivel.setObjetosId(Integer.parseInt(result.get(3).toString()));
                nivelList.add(nivel);
            }

            tx.commit();
            session.close();
            return nivelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void insertEmpresaobraconceptoscoeficientes
            (List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoefList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Empresaobraconceptoscoeficientes datos : empresaobraconceptoscoefList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(datos);
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

    public Entidad getEntidad(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Entidad entidad = (Entidad) session.createQuery(" from Entidad WHERE codigo =: ent").setParameter("ent", code).getSingleResult();
            tx.commit();
            session.close();
            return entidad;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Entidad();
    }

    public Inversionista getInversionista(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Inversionista inversionista = (Inversionista) session.createQuery(" from Inversionista WHERE codigo =: ent").setParameter("ent", code).getSingleResult();
            tx.commit();
            session.close();
            return inversionista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Inversionista();
    }

    public List<Especialidades> especialidadesList;

    public List<Especialidades> getAllEspecialidades() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesList = session.createQuery(" from Especialidades ").getResultList();
            tx.commit();
            session.close();
            return especialidadesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Renglonnivelespecifico> getRecalculaUORenglonRV(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            datosRenglonNivelList = new ArrayList<>();
            Nivelespecifico unidad = session.get(Nivelespecifico.class, unidadobra.getId());
            List<Empresaobraconceptoscoeficientes> list = getUnidadoEmpresaobraconceptoscoeficientesList(unidad.getObraId(), unidad.getEmpresaconstructoraId());
            list.parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
            double cmano = list.parallelStream().filter(eoc -> eoc.getObraId() == unidad.getObraId() && eoc.getEmpresaconstructoraId() == unidad.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            double cequipo = list.parallelStream().filter(eoc -> eoc.getObraId() == unidad.getObraId() && eoc.getEmpresaconstructoraId() == unidad.getEmpresaconstructoraId() && eoc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
            List<Empresaobratarifa> empresaobratarifaList = getEmpresaobratarifaList(unidad.getObraId(), unidad.getEmpresaconstructoraId(), unidad.getObraByObraId().getTarifaId());
            if (unidad.getRenglonnivelespecificosById().size() > 0) {
                for (Renglonnivelespecifico unidadobrarenglon : unidad.getRenglonnivelespecificosById()) {
                    if (unidadobrarenglon.getConmat().trim().equals("0")) {
                        double costoMano = calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        double costoEq = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double valMano = unidadobrarenglon.getCantidad() * costoMano * cmano;
                        double valEquip = unidadobrarenglon.getCantidad() * costoEq * cequipo;
                        unidadobrarenglon.setCostmano(Math.round(valMano * 100d) / 100d);
                        unidadobrarenglon.setCostequipo(Math.round(valEquip * 100d) / 100d);
                        double salario = new BigDecimal(String.format("%.2f", calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()))).doubleValue();
                        unidadobrarenglon.setSalario(salario * unidadobrarenglon.getCantidad());
                        datosRenglonNivelList.add(unidadobrarenglon);
                    } else if (unidadobrarenglon.getConmat().trim().equals("1")) {
                        double costoMano = calcCostoManoRVinEmpresaObra(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId());
                        double costoEq = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double costMater = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double costMaterSemi = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double costMaterJueg = unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                        double valMano = unidadobrarenglon.getCantidad() * costoMano * cmano;
                        double valEquip = unidadobrarenglon.getCantidad() * costoEq * cequipo;
                        double mat = costMater + costMaterSemi + costMaterJueg;
                        unidadobrarenglon.setCostmaterial(mat * unidadobrarenglon.getCantidad());
                        unidadobrarenglon.setCostmano(Math.round(valMano * 100d) / 100d);
                        unidadobrarenglon.setCostequipo(Math.round(valEquip * 100d) / 100d);
                        double salario = new BigDecimal(String.format("%.2f", calcSalarioRV(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId()))).doubleValue();
                        unidadobrarenglon.setSalario(salario * unidadobrarenglon.getCantidad());
                        datosRenglonNivelList.add(unidadobrarenglon);
                    }
                }
            }
            tx.commit();
            session.close();
            return datosRenglonNivelList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void createEmpresaObraSalarios(List<Empresaobrasalario> nivelList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Empresaobrasalario niv : nivelList) {
                countUOR++;
                if (countUOR > 0 && countUOR % countUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(niv);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Error al importar el Nivel");
            alert.showAndWait();
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

    public void updatesUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

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

    public void persistNivelRenglones(List<Renglonnivelespecifico> unidadobrarenglonArray) {

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

    public void persistBajoespecificacionRenglon(List<Bajoespecificacionrv> bajoespecificacionList) {
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

    public int createEspecialidad(JSONObject object) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            Especialidades especialidades = new Especialidades();
            especialidades.setCodigo(String.valueOf(object.get("code").toString()));
            especialidades.setDescripcion(String.valueOf(object.get("descrip").toString()));
            id = (Integer) session.save(especialidades);

            tx.commit();
            session.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public void sabelistSubespecialidades(List<Subespecialidades> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Subespecialidades unidadobrarenglon : subespecialidadesList) {
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

    public List<objectSelect> getRecursos() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            selectList = new ArrayList<>();
            List<Recursos> recursosList = session.createQuery("FROM Recursos WHERE tipo ='1'").getResultList();
            List<Suministrossemielaborados> suministrossemielaboradosList = session.createQuery("FROM Suministrossemielaborados WHERE pertenec = null ").getResultList();
            List<Juegoproducto> juegoproductoList = session.createQuery("FROM Juegoproducto WHERE pertenece = null ").getResultList();
            selectList.addAll(recursosList.parallelStream().map(item -> {
                objectSelect obj = new objectSelect(item.getId(), item.getCodigo());
                return obj;
            }).collect(Collectors.toList()));
            selectList.addAll(suministrossemielaboradosList.parallelStream().map(item -> {
                objectSelect obj = new objectSelect(item.getId(), item.getCodigo());
                return obj;
            }).collect(Collectors.toList()));
            selectList.addAll(juegoproductoList.parallelStream().map(item -> {
                objectSelect obj = new objectSelect(item.getId(), item.getCodigo());
                return obj;
            }).collect(Collectors.toList()));

            trx.commit();
            session.close();
            return selectList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public int createBrigada(JSONObject object, int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idB = null;
        try {
            tx = session.beginTransaction();
            Brigadaconstruccion brigada = new Brigadaconstruccion();
            brigada.setCodigo(String.valueOf(object.get("code").toString()));
            brigada.setDescripcion(String.valueOf(object.get("descrip").toString()));
            brigada.setEmpresaconstructoraId(id);
            idB = (Integer) session.save(brigada);
            tx.commit();
            session.close();
            return idB;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idB;
    }

    public void cuadrillaConstruccion(List<Cuadrillaconstruccion> cuadrillaconstruccions) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Cuadrillaconstruccion unidadobrarenglon : cuadrillaconstruccions) {
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

    public int createGrupo(Grupoconstruccion grupos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(grupos);

            tx.commit();
            session.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    public void saverecursosList(List<Recursos> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Recursos unidadobrarenglon : subespecialidadesList) {
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

    private int batchSizeRec = 500;

    public void deleterecursosList(List<Recursos> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Recursos unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeRec == 0) {
                    session.flush();
                    session.clear();
                }
                int op1 = session.createQuery(" DELETE FROM Recursos  WHERE id =: idRec ").setParameter("idRec", unidadobrarenglon.getId()).executeUpdate();
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRecurBajoUOList(List<Bajoespecificacion> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacion unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeRec == 0) {
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

    public void updateRecurBajoRVList(List<Bajoespecificacionrv> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacionrv unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeRec == 0) {
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

    public void savejuegoList(List<Juegoproducto> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Juegoproducto unidadobrarenglon : subespecialidadesList) {
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

    public void saveJuegoRecursosList(List<Juegorecursos> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Juegorecursos unidadobrarenglon : subespecialidadesList) {
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

    public void saveSemielaboradosList(List<Suministrossemielaborados> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Suministrossemielaborados unidadobrarenglon : subespecialidadesList) {
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

    public void saveSemielaboradosRecursosList(List<Semielaboradosrecursos> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Semielaboradosrecursos unidadobrarenglon : subespecialidadesList) {
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

    public void saveRenglonesList(List<Renglonvariante> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Renglonvariante unidadobrarenglon : subespecialidadesList) {
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

    public void saveRenglonesRecursosList(List<Renglonrecursos> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Renglonrecursos unidadobrarenglon : subespecialidadesList) {
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

    public void saveRenglonesJuegoList(List<Renglonjuego> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Renglonjuego unidadobrarenglon : subespecialidadesList) {
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

    public void saveRenglonesSemielaboradosList(List<Renglonsemielaborados> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;
            for (Renglonsemielaborados unidadobrarenglon : subespecialidadesList) {
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

    public void updateObrasList(List<Obra> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Obra unidadobrarenglon : subespecialidadesList) {
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


    public void deleteBajoEspecificacionList(List<Bajoespecificacion> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacion unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBajoEspecificacionRenglonList(List<Bajoespecificacionrv> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Bajoespecificacionrv unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUnidadDeObraRenglon(List<Unidadobrarenglon> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobrarenglon unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUnidadObra(List<Unidadobra> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobra unidadobrarenglon : subespecialidadesList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Brigadaconstruccion> getBrigadabyEmpresa(int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Brigadaconstruccion> brigadaconstruccionList = new ArrayList<>();
            brigadaconstruccionList = session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: idE").setParameter("idE", idEmp).getResultList();

            tx.commit();
            session.close();
            return brigadaconstruccionList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CertificacionRenglonVariante> getCertificacionRenglonVarianteList(int idNiv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<CertificacionRenglonVariante> brigadaconstruccionList = new ArrayList<>();
            brigadaconstruccionList = session.createQuery("FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idE").setParameter("idE", idNiv).getResultList();

            tx.commit();
            session.close();
            return brigadaconstruccionList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void deleteBajoEsp(Bajoespecificacion bajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bajo);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteBajoEspRenglon(Bajoespecificacionrv bajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bajo);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void setUpdateValores(List<Unidadobra> unidadobraList) {
        List<Unidadobrarenglon> unidadobrarenglons = new ArrayList<>();
        List<Bajoespecificacion> listOfSuministros = new ArrayList<>();

        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Recalculando Presupuesto... por favor, espere");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        for (Unidadobra unidadobra : unidadobraList) {
            unidadobrarenglons.addAll(getRecalculaUORenglon(unidadobra));
            listOfSuministros.addAll(recalculaSuministrosInUO(unidadobra));
        }
        createUnidadesObraRenglones(unidadobrarenglons);
        updatebajoEspecificacionList(listOfSuministros);

        for (Unidadobra unidadobra : unidadobraList) {
            updateUnidadObra(unidadobra);
        }
    }

    private List<Bajoespecificacion> recalculaSuministrosInUO(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
            List<Bajoespecificacion> updateList = new ArrayList<>();
            bajoespecificacionList = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: idU ").setParameter("idU", unidadobra.getId()).getResultList();
            if (bajoespecificacionList.size() > 0) {
                for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
                    if (bajoespecificacion.getTipo().trim().equals("1")) {
                        if (getSuministro(bajoespecificacion.getIdSuministro()) != null) {
                            double recPrec = getSuministro(bajoespecificacion.getIdSuministro()).getPreciomn();
                            double calc = bajoespecificacion.getCantidad() * recPrec;
                            bajoespecificacion.setCosto(Math.round(calc * 100d) / 100d);
                            updateList.add(bajoespecificacion);
                        } else {
                            deleteBajoEsp(bajoespecificacion);
                        }
                    } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                        if (getSuministroSemi(bajoespecificacion.getIdSuministro()) != null) {
                            double recPrec = getSuministroSemi(bajoespecificacion.getIdSuministro()).getPreciomn();
                            double calc = bajoespecificacion.getCantidad() * recPrec;
                            bajoespecificacion.setCosto(Math.round(calc * 100d) / 100d);
                            updateList.add(bajoespecificacion);
                        } else {
                            deleteBajoEsp(bajoespecificacion);
                        }
                    } else if (bajoespecificacion.getTipo().trim().equals("J")) {
                        if (getJuegoproducto(bajoespecificacion.getIdSuministro()) != null) {
                            double recPrec = getJuegoproducto(bajoespecificacion.getIdSuministro()).getPreciomn();
                            double calc = bajoespecificacion.getCantidad() * recPrec;
                            bajoespecificacion.setCosto(Math.round(calc * 100d) / 100d);
                            updateList.add(bajoespecificacion);
                        } else {
                            deleteBajoEsp(bajoespecificacion);
                        }
                    }
                }
            }
            tx.commit();
            session.close();
            return updateList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<Bajoespecificacionrv> recalculaSuministrosInUORV(Nivelespecifico unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacionrv> bajoespecificacionList = new ArrayList<>();
            List<Bajoespecificacionrv> updateList = new ArrayList<>();
            bajoespecificacionList = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU ").setParameter("idU", unidadobra.getId()).getResultList();
            if (bajoespecificacionList.size() > 0) {
                for (Bajoespecificacionrv bajoespecificacion : bajoespecificacionList) {
                    if (bajoespecificacion.getTipo() != null) {
                        if (bajoespecificacion.getTipo().trim().equals("1")) {
                            if (getSuministro(bajoespecificacion.getIdsuministro()) != null) {
                                double recPrec = getSuministro(bajoespecificacion.getIdsuministro()).getPreciomn();
                                double calc = bajoespecificacion.getCantidad() * recPrec;
                                bajoespecificacion.setCosto(Math.round(calc * 100d) / 100d);
                                updateList.add(bajoespecificacion);
                            } else {
                                deleteBajoRV(bajoespecificacion.getId());
                            }
                        } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                            if (getSuministroSemi(bajoespecificacion.getIdsuministro()) != null) {
                                double recPrec = getSuministroSemi(bajoespecificacion.getIdsuministro()).getPreciomn();
                                double calc = bajoespecificacion.getCantidad() * recPrec;
                                bajoespecificacion.setCosto(Math.round(calc * 100d) / 100d);
                                updateList.add(bajoespecificacion);
                            } else {
                                deleteBajoRV(bajoespecificacion.getId());
                            }
                        }
                    } else {
                        deleteBajoRV(bajoespecificacion.getId());
                    }
                }
            }
            tx.commit();
            session.close();
            return updateList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private double getCostoMaterialBajoEspecificacion(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            costMaterialBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(bajo.costo) FROM Bajoespecificacionrv bajo INNER JOIN Renglonnivelespecifico re ON bajo.nivelespecificoId = re.nivelespecificoId AND bajo.renglonvarianteId = re.renglonvarianteId WHERE bajo.nivelespecificoId = : idNivel").setParameter("idNivel", id);

            if (query.getResultList().get(0) != null) {
                costMaterialBajo = (Double) query.getResultList().get(0);
            } else {
                costMaterialBajo = 0.0;
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

        return costMaterialBajo;
    }


    private void updateNivelEspecifico(Nivelespecifico unidad) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Nivelespecifico unidadobra = session.get(Nivelespecifico.class, unidad.getId());
            unidadobra.getRenglonnivelespecificosById().size();
            if (unidadobra.getRenglonnivelespecificosById().size() > 0) {
                double costtMaterialInRV = unidadobra.getRenglonnivelespecificosById().parallelStream().filter(item -> item.getConmat().trim().equals("1")).map(Renglonnivelespecifico::getCostmaterial).reduce(0.0, Double::sum);
                double costMano = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostmano).reduce(0.0, Double::sum);
                double costEquip = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getCostequipo).reduce(0.0, Double::sum);
                double salario = unidadobra.getRenglonnivelespecificosById().parallelStream().map(Renglonnivelespecifico::getSalario).reduce(0.0, Double::sum);
                double costoMaterialBajo = getCostoMaterialBajoEspecificacion(unidadobra.getId());

                unidadobra.setCostomaterial(costoMaterialBajo + costtMaterialInRV);
                unidadobra.setCostomano(Math.round(costMano * 100d) / 100d);
                unidadobra.setCostoequipo(Math.round(costEquip * 100d) / 100d);
                unidadobra.setSalario(salario);
                session.update(unidadobra);
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

    public Empresaconstructora getEmpresaconstructora(String code) {
        // System.out.println(code);
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            try {
                Empresaconstructora empresaconstructora = (Empresaconstructora) session.createQuery(" from Empresaconstructora WHERE codigo =: ent").setParameter("ent", code.trim()).getSingleResult();

                tx.commit();
                session.close();
                return empresaconstructora;

            } catch (NullPointerException e) {
                System.out.println(" yyyyyyy " + code);
            }


        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Empresaconstructora();
    }

    public boolean getBAjoBajoespecificacionList(int idSum) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> datos = session.createQuery(" FROM Bajoespecificacion ").getResultList();
            List<Bajoespecificacion> filterList = datos.parallelStream().filter(item -> item.getIdSuministro() == idSum).collect(Collectors.toList());
            flag = false;
            if (filterList.size() > 0) {
                flag = true;
            }


            tx.commit();
            session.close();
            return flag;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return flag;
    }

    public boolean getBAjoBajoespecificacionrvList(int idSum) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Bajoespecificacionrv> datos = session.createQuery(" FROM Bajoespecificacionrv ").getResultList();
            List<Bajoespecificacionrv> filterList = datos.parallelStream().filter(item -> item.getIdsuministro() == idSum).collect(Collectors.toList());
            flag = false;
            if (filterList.size() > 0) {
                flag = true;
            }
            tx.commit();
            session.close();
            return flag;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return flag;
    }

    public static Empresaobraconcepto createEOC(int idE, int idOb, int idC, double valor, double sal) {
        Empresaobraconcepto empresaobraconcepto = new Empresaobraconcepto();
        empresaobraconcepto.setEmpresaconstructoraId(idE);
        empresaobraconcepto.setObraId(idOb);
        empresaobraconcepto.setConceptosgastoId(idC);
        empresaobraconcepto.setValor(valor);
        empresaobraconcepto.setSalario(sal);
        return empresaobraconcepto;
    }

    public double calcCostoManoRVinEmpresaObra(Renglonvariante renglonvariante) {
        double valCosto = 0.0;
        for (Renglonrecursos rrc : renglonvariante.getRenglonrecursosById()) {
            if (rrc.getRecursosByRecursosId().getTipo().trim().equals("2")) {
                valCosto += rrc.getCantidas() * getValorCosto(rrc.getRecursosByRecursosId().getGrupoescala()) / rrc.getUsos();
            }
        }
        return valCosto;
    }

    public double calcCostoEquipoRVinEmpresaObra(Renglonvariante renglonvariante) {
        double valCosto = 0.0;
        for (Renglonrecursos rrc : renglonvariante.getRenglonrecursosById()) {
            if (rrc.getRecursosByRecursosId().getTipo().trim().equals("3")) {
                valCosto += rrc.getCantidas() * rrc.getRecursosByRecursosId().getPreciomn() / rrc.getUsos();
            }
        }
        return valCosto;
    }

    private Unidadobra uo;
    private Renglonvariante rc;

    public Unidadobra getUnidadobra(int idU) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            uo = null;
            uo = (Unidadobra) session.get(Unidadobra.class, idU);
            tx.commit();
            session.close();
            return uo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return uo;
    }

    public Renglonvariante getRenglonvariante(int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            rc = null;
            rc = (Renglonvariante) session.get(Renglonvariante.class, idR);
            tx.commit();
            session.close();
            return rc;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return rc;
    }


    public void getSemileaboradosEmptyStructure() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Suministrossemielaborados> list = session.createQuery(" FROM Suministrossemielaborados ").getResultList();
            for (Suministrossemielaborados suministrossemielaborados : list) {
                if (suministrossemielaborados.getSemielaboradosrecursosById().size() == 0) {
                    if (eps.getObra(Integer.parseInt(suministrossemielaborados.getPertenec().trim())) == null) {
                        getBajoSemileaboradosEmptyStructure(suministrossemielaborados.getId());
                    } else {
                        Suministrossemielaborados suministro = list.parallelStream().filter(item -> item.getCodigo().trim().equals(suministrossemielaborados.getCodigo())).findFirst().get();
                        updateBajoSemileaboradosEmptyStructure(suministrossemielaborados.getId(), suministro.getId());
                    }
                }
            }

            tx.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getBajoSemileaboradosEmptyStructure(int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> list = session.createQuery(" FROM Bajoespecificacion WHERE idSuministro =: ids").setParameter("ids", idS).getResultList();
            if (list.size() == 0) {
                System.out.println("Borrando suministro semielaborado: " + idS);
                deleteSemielaborados(idS);
            } else {
                for (Bajoespecificacion item : list) {
                    System.out.println(item.getUnidadobraByUnidadobraId().getCodigo() + " -- " + item.getIdSuministro() + " -- " + item.getCantidad());
                    deleteBajoEsp(item);
                }
                System.out.println("Borrando suministro semielaborado: " + idS);
                deleteSemielaborados(idS);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBajoSemileaboradosEmptyStructure(int idS, int idChange) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> list = session.createQuery(" FROM Bajoespecificacion WHERE idSuministro =: ids").setParameter("ids", idS).getResultList();
            for (Bajoespecificacion item : list) {
                int op1 = session.createQuery(" UPDATE Bajoespecificacion SET idSuministro =: idChenge WHERE idSuministro =: idSe ").setParameter("idChenge", idChange).setParameter("idSe", idS).executeUpdate();
            }
            deleteSemielaborados(idS);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSemielaborados(int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op1 = session.createQuery(" DELETE FROM Suministrossemielaborados WHERE id =: idSe ").setParameter("idSe", idS).executeUpdate();
            tx.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listToSugestionSuministros(int tipo) {
        System.out.println("datatatata: " + tipo);

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listSuministros = FXCollections.observableArrayList();
            List<Recursos> list = new ArrayList<>();
            List<Juegoproducto> list2 = new ArrayList<>();
            List<Suministrossemielaborados> list3 = new ArrayList<>();
            if (tipo == 1) {
                list = session.createQuery("FROM Recursos WHERE pertenece != 'R266' and active = 1 ").getResultList();
                list2 = session.createQuery("FROM Juegoproducto WHERE pertenece != 'R266'").getResultList();
                list3 = session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'R266'").getResultList();
            } else if (tipo > 1) {
                list = session.createQuery("FROM Recursos WHERE pertenece != null ").getResultList();
                list2 = session.createQuery("FROM Juegoproducto WHERE pertenece != null ").getResultList();
                list3 = session.createQuery("FROM Suministrossemielaborados WHERE pertenec != null ").getResultList();
            }
            for (Recursos recursos : list) {
                listSuministros.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, recursos.getGrupoescala(), recursos.getTipo(), recursos.getPertenece(), 0.0, createCheckBox(recursos.getActive())));
            }
            for (Juegoproducto recursos : list2) {
                listSuministros.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, "I", "J", recursos.getPertenece(), 0.0, createCheckBox(1)));
            }
            for (Suministrossemielaborados recursos : list3) {
                listSuministros.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, "I", "S", recursos.getPertenec(), 0.0, createCheckBox(1)));
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

    public ObservableList<SuministrosView> getSuministrosToCheck(String tag) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            suministrosViewObservableList = FXCollections.observableArrayList();
            listRecursos = (ArrayList<Recursos>) session.createQuery("FROM Recursos where  tipo = '1' and pertenece =: pert and active = 1").setParameter("pert", tag).getResultList();
            listJuego = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece =: pert ").setParameter("pert", tag).getResultList();
            listSuministrossemielaborados = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec =: pert ").setParameter("pert", tag).getResultList();

            for (Recursos recursos : listRecursos) {
                suministrosViewObservableList.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, recursos.getGrupoescala(), recursos.getTipo(), recursos.getPertenece(), 0.0, createCheckBox(recursos.getActive())));
            }
            for (Juegoproducto juego : listJuego) {
                suministrosViewObservableList.add(new SuministrosView(juego.getId(), juego.getCodigo(), juego.getDescripcion(), juego.getUm(), juego.getPeso(), juego.getPreciomn(), juego.getPreciomlc(), "I", "J", juego.getPertenece(), 0.0, createCheckBox(1)));
            }
            for (Suministrossemielaborados sumini : listSuministrossemielaborados) {
                suministrosViewObservableList.add(new SuministrosView(sumini.getId(), sumini.getCodigo(), sumini.getDescripcion(), sumini.getUm(), sumini.getPeso(), sumini.getPreciomn(), sumini.getPreciomlc(), "I", "S", sumini.getPertenec(), 0.0, createCheckBox(1)));
            }
            tx.commit();
            session.close();
            return suministrosViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<SuministrosView> getSuministrosViewObservableList(String tag) {
        System.out.println("check: " + tag);
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            suministrosViewObservableList = FXCollections.observableArrayList();
            listRecursos = (ArrayList<Recursos>) session.createQuery("FROM Recursos where  tipo = '1' and pertenece != 'I' and active = 1  ").getResultList();
            listJuego = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece != 'I' ").getResultList();
            listSuministrossemielaborados = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec != 'I' ").list();

            if (tag.trim().equals("R266")) {
                List<Recursos> tempRecList = listRecursos.parallelStream().filter(item -> !item.getPertenece().trim().startsWith("T")).collect(Collectors.toList());
                for (Recursos recursos : tempRecList) {
                    suministrosViewObservableList.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, recursos.getGrupoescala(), recursos.getTipo(), recursos.getPertenece(), 0.0, createCheckBox(recursos.getActive())));
                }
                List<Juegoproducto> tempJuegList = listJuego.parallelStream().filter(item -> !item.getPertenece().trim().startsWith("T")).collect(Collectors.toList());
                for (Juegoproducto juego : tempJuegList) {
                    suministrosViewObservableList.add(new SuministrosView(juego.getId(), juego.getCodigo(), juego.getDescripcion(), juego.getUm(), juego.getPeso(), juego.getPreciomn(), juego.getPreciomlc(), "I", "J", juego.getPertenece(), 0.0, createCheckBox(1)));
                }
                List<Suministrossemielaborados> tempSemiList = listSuministrossemielaborados.parallelStream().filter(item -> !item.getPertenec().trim().startsWith("T")).collect(Collectors.toList());
                for (Suministrossemielaborados sumini : tempSemiList) {
                    suministrosViewObservableList.add(new SuministrosView(sumini.getId(), sumini.getCodigo(), sumini.getDescripcion(), sumini.getUm(), sumini.getPeso(), sumini.getPreciomn(), sumini.getPreciomlc(), "I", "S", sumini.getPertenec(), 0.0, createCheckBox(1)));
                }
            } else if (tag.trim().startsWith("T")) {
                List<Recursos> tempRecList = listRecursos.parallelStream().filter(item -> !item.getPertenece().trim().equals("R266")).collect(Collectors.toList());
                for (Recursos recursos : tempRecList) {
                    suministrosViewObservableList.add(new SuministrosView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, recursos.getGrupoescala(), recursos.getTipo(), recursos.getPertenece(), 0.0, createCheckBox(recursos.getActive())));
                }
                List<Juegoproducto> tempJuegList = listJuego.parallelStream().filter(item -> !item.getPertenece().trim().equals("R266")).collect(Collectors.toList());
                for (Juegoproducto juego : tempJuegList) {
                    suministrosViewObservableList.add(new SuministrosView(juego.getId(), juego.getCodigo(), juego.getDescripcion(), juego.getUm(), juego.getPeso(), juego.getPreciomn(), juego.getPreciomlc(), "I", "J", juego.getPertenece(), 0.0, createCheckBox(1)));
                }
                List<Suministrossemielaborados> tempSemiList = listSuministrossemielaborados.parallelStream().filter(item -> !item.getPertenec().equals("R266")).collect(Collectors.toList());
                for (Suministrossemielaborados sumini : tempSemiList) {
                    suministrosViewObservableList.add(new SuministrosView(sumini.getId(), sumini.getCodigo(), sumini.getDescripcion(), sumini.getUm(), sumini.getPeso(), sumini.getPreciomn(), sumini.getPreciomlc(), "I", "S", sumini.getPertenec(), 0.0, createCheckBox(1)));
                }
            }
            tx.commit();
            session.close();
            return suministrosViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<UniddaObraToIMportView> uniddaObraViewObservableList;

    public double getManodeObraEquipo(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salario = 0.0;
            List<Tuple> tupleList = session.createQuery(" SELECT ur.cantRv * rvr.cantidas, rec.salario FROM Unidadobrarenglon ur INNER JOIN Unidadobra  uo ON ur.unidadobraId = uo.id INNER JOIN Renglonrecursos rvr ON ur.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId = :idOb AND rec.tipo = '3' ", Tuple.class).setParameter("idOb", obra.getId()).getResultList();
            for (Tuple tuple : tupleList) {
                salario += Double.parseDouble(tuple.get(0).toString()) * Double.parseDouble(tuple.get(1).toString());
            }
            tx.commit();
            session.close();
            return salario;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salario;
    }

    public double getManodeObraEquipoCertificaciones(Obra obra, int idEmp, Date desde, Date hasta) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salario = 0.0;
            List<Tuple> tupleList = session.createQuery(" SELECT ur.cantRv * cert.cantidad / uo.cantidad * rvr.cantidas as cantidad, rec.salario FROM Unidadobrarenglon ur INNER JOIN Unidadobra uo ON ur.unidadobraId = uo.id INNER JOIN Certificacion cert ON uo.id = cert.unidadobraId INNER JOIN Renglonrecursos rvr ON ur.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId = :idOb AND uo.empresaconstructoraId =: idEm AND cert.desde >=: desd AND cert.hasta <=: hast AND rec.tipo = '3' ", Tuple.class).setParameter("idOb", obra.getId()).setParameter("idEm", idEmp).setParameter("desd", desde).setParameter("hast", hasta).getResultList();
            for (Tuple tuple : tupleList) {
                salario += Double.parseDouble(tuple.get(0).toString()) * Double.parseDouble(tuple.get(1).toString());
            }
            tx.commit();
            session.close();
            return salario;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salario;
    }


    public double getManodeObraEquipoRV(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salario = 0.0;
            List<Tuple> tupleList = session.createQuery(" SELECT ur.cantidad * rvr.cantidas, rec.salario FROM Renglonnivelespecifico ur INNER JOIN Nivelespecifico  uo ON ur.nivelespecificoId = uo.id INNER JOIN Renglonrecursos rvr ON ur.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId = :idOb AND rec.tipo = '3' ", Tuple.class).setParameter("idOb", obra.getId()).getResultList();
            for (Tuple tuple : tupleList) {
                salario += Double.parseDouble(tuple.get(0).toString()) * Double.parseDouble(tuple.get(1).toString());
            }
            tx.commit();
            session.close();
            return salario;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salario;
    }

    public double getValuesOfSubConcepts(Conceptosgasto conceptosgasto, Obra obra, int idEmpresa) {
        double val = 0.0;
        for (Subconcepto subconcepto : conceptosgasto.getSubConceptosById()) {
            val += subconcepto.getEmpresaobrasubconceptosById().parallelStream().filter(item -> item.getObraId() == obra.getId() && item.getEmpresaconstructoraId() == idEmpresa).map(Empresaobrasubconcepto::getValor).findFirst().orElse(0.0);
        }
        return val;
    }

    public double getSalarioOfSubConcepts(Conceptosgasto conceptosgasto, Obra obra, int idEmpresa) {
        double val = 0.0;
        for (Subconcepto subconcepto : conceptosgasto.getSubConceptosById()) {
            val += subconcepto.getEmpresaobrasubconceptosById().parallelStream().filter(item -> item.getObraId() == obra.getId() && item.getEmpresaconstructoraId() == idEmpresa).map(Empresaobrasubconcepto::getSalario).findFirst().orElse(0.0);
        }
        return val;
    }

    public double getValuesOfsubSubConcepts(Subconcepto subsubconcepto, Obra obraSend, int idEmp) {
        double val = 0.0;
        for (Subsubconcepto subsubsubconcepto : subsubconcepto.getSubSubConceptosById()) {
            val += subsubsubconcepto.getEmpresaobrasubsubconceptosById().parallelStream().filter(item -> item.getObraId() == obraSend.getId() && item.getEmpresaconstructoraId() == idEmp).map(Empresaobrasubsubconcepto::getValor).findFirst().orElse(0.0);
        }
        return val;
    }

    public double getValuesOfsubSubConceptsSalario(Subconcepto subsubconcepto, Obra obraSend, int idEmp) {
        double val = 0.0;
        for (Subsubconcepto subsubsubconcepto : subsubconcepto.getSubSubConceptosById()) {
            val += subsubsubconcepto.getEmpresaobrasubsubconceptosById().parallelStream().filter(item -> item.getObraId() == obraSend.getId() && item.getEmpresaconstructoraId() == idEmp).map(Empresaobrasubsubconcepto::getSalario).findFirst().orElse(0.0);
        }
        return val;
    }

    public double getValuesOfSUBsubSubConcepts(Subsubconcepto subsubconcepto, Obra obraSend, int idEmp) {
        double val = 0.0;
        for (Subsubsubconcepto subsubsubconcepto : subsubconcepto.getSubsubconceptosById()) {
            val += subsubsubconcepto.getEmpresaobrasubsubsubconceptoCollectionById().parallelStream().filter(item -> item.getObraId() == obraSend.getId() && item.getEmpresaconstructoraId() == idEmp).map(Empresaobrasubsubsubconcepto::getValor).findFirst().orElse(0.0);
        }
        return val;
    }

    public double getValuesOfSUBsubSubConceptsSalario(Subsubconcepto subsubconcepto, Obra obraSend, int idEmp) {
        double val = 0.0;
        for (Subsubsubconcepto subsubsubconcepto : subsubconcepto.getSubsubconceptosById()) {
            val += subsubsubconcepto.getEmpresaobrasubsubsubconceptoCollectionById().parallelStream().filter(item -> item.getObraId() == obraSend.getId() && item.getEmpresaconstructoraId() == idEmp).map(Empresaobrasubsubsubconcepto::getSalario).findFirst().orElse(0.0);
        }
        return val;
    }

    public List<Bajoespecificacion> getBajoespecificacionUOList(int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> list = session.createQuery(" FROM Bajoespecificacion WHERE idSuministro =: id ").setParameter("id", idS).getResultList();
            tx.commit();
            session.close();
            return list;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacion> getBajoespecificacionUOByCodeList(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> datosList = session.createQuery(" FROM Bajoespecificacion bajo INNER JOIN Recursos rec  ON bajo.idSuministro = rec.id WHERE  rec.codigo =: codeg ", Tuple.class).setParameter("codeg", code.trim()).getResultList();
            List<Bajoespecificacion> bajoespecificacionList = new ArrayList<>();
            for (Tuple tuple : datosList) {
                bajoespecificacionList.add((Bajoespecificacion) tuple.get(0));
            }
            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacion> getBajoespecificacionRVList(int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacion> list = session.createQuery(" FROM Bajoespecificacionrv WHERE idsuministro =: id ").setParameter("id", idS).getResultList();
            tx.commit();
            session.close();
            return list;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacionrv> getBajoespecificacionRVByCodeList(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> datosList = session.createQuery(" FROM Bajoespecificacionrv bajo INNER JOIN Recursos rec  ON bajo.idsuministro = rec.id WHERE  rec.codigo =: codeg ", Tuple.class).setParameter("codeg", code.trim()).getResultList();
            List<Bajoespecificacionrv> bajoespecificacionList = new ArrayList<>();
            for (Tuple tuple : datosList) {
                bajoespecificacionList.add((Bajoespecificacionrv) tuple.get(0));
            }
            tx.commit();
            session.close();
            return bajoespecificacionList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Suministrossemielaborados getSuministroSemielaboradoById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, id);
            tx.commit();
            session.close();
            return suministrossemielaborados;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Suministrossemielaborados();
    }

    public Juegoproducto getJuegoProductoById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Juegoproducto juegoproducto = session.get(Juegoproducto.class, id);
            tx.commit();
            session.close();
            return juegoproducto;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Juegoproducto();
    }

    public void addBajoEspecificacion(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacion bajo = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: unidad AND idSuministro =: suministro").setParameter("unidad", bajoespecificacion.getUnidadobraId()).setParameter("suministro", bajoespecificacion.getIdSuministro());

            try {
                bajo = (Bajoespecificacion) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + getCodeOfSuministro(bajoespecificacion.getTipo(), bajoespecificacion.getIdSuministro()) + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonAgregar) {
                    sumarCantidadesSuministroUO(bajoespecificacion);
                } else if (result.get() == buttonSobre) {
                    sobreEscribirSuministroUnidadUO(bajoespecificacion);
                }


            } else {
                idBe = (Integer) session.save(bajoespecificacion);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    private String getCodeOfSuministro(String type, int idSuministro) {
        String code = " ";
        if (type.trim().equals("1")) {
            code = getSuministro(idSuministro).getCodigo();
        } else if (type.trim().equals("S")) {
            code = getSuministroSemielaboradoById(idSuministro).getCodigo();
        } else if (type.trim().equals("J")) {
            code = getSuministroSemielaboradoById(idSuministro).getCodigo();
        }
        return code;
    }

    private void sobreEscribirSuministroUnidadUO(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
            if (bajo != null) {
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCosto());
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void sumarCantidadesSuministroUO(Bajoespecificacion bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv").setParameter("idUni", bajoespecificacion.getUnidadobraId()).setParameter("idRv", bajoespecificacion.getIdSuministro());

            Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
            if (bajo != null) {
                double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                bajo.setCantidad(cantidad);
                bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void addBajoEspecificacionRV(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacionrv bajo = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: unidad AND idsuministro =: suministro AND renglonvarianteId =: idR ").setParameter("unidad", bajoespecificacion.getNivelespecificoId()).setParameter("suministro", bajoespecificacion.getIdsuministro()).setParameter("idR", bajoespecificacion.getRenglonvarianteId());
            try {
                bajo = (Bajoespecificacionrv) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + getCodeOfSuministro(bajoespecificacion.getTipo(), bajoespecificacion.getIdsuministro()) + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");
                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonAgregar) {
                    sumarCantidadesSuministro(bajoespecificacion);
                } else if (result.get() == buttonSobre) {
                    sobreEscribirSuministroUnidad(bajoespecificacion);
                }
            } else {
                idBe = (Integer) session.save(bajoespecificacion);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void sobreEscribirSuministroUnidad(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idRen");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idRen", bajoespecificacion.getRenglonvarianteId());

            Bajoespecificacionrv bajo = (Bajoespecificacionrv) query.getSingleResult();
            if (bajo != null) {
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCosto());
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void sumarCantidadesSuministro(Bajoespecificacionrv bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idReng");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idReng", bajoespecificacion.getRenglonvarianteId());

            Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());

            Bajoespecificacionrv bajo = (Bajoespecificacionrv) query.getSingleResult();
            if (bajo != null) {
                double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                bajo.setCantidad(cantidad);
                bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
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

    public void showAlert(String msg, int type) {
        if (type == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("OK!!");
            alert.setContentText(msg);
            alert.showAndWait();
        } else if (type == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!!");
            alert.setContentText(msg);
            alert.showAndWait();
        }
    }

    public List<Obra> getObrasList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Obra> lista = new ArrayList<>();
            lista = session.createQuery(" FROM Obra ").getResultList();
            tx.commit();
            session.close();
            return lista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Sobregrupo getSobreGrupo(String element) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Sobregrupo sobregrupo = (Sobregrupo) session.createQuery(" FROM Sobregrupo WHERE codigo =: cod").setParameter("cod", element).getSingleResult();
            tx.commit();
            session.close();
            return sobregrupo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Sobregrupo();
    }

    public Grupo getGrupo(String element) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Grupo grupo = (Grupo) session.createQuery(" FROM Grupo WHERE codigog =: cod").setParameter("cod", element).getSingleResult();
            tx.commit();
            session.close();
            return grupo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Grupo();
    }

    public Subgrupo getSubgrupo(String element) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Subgrupo subgrupo = (Subgrupo) session.createQuery(" FROM Subgrupo WHERE codigosub =: cod").setParameter("cod", element).getSingleResult();
            tx.commit();
            session.close();
            return subgrupo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Subgrupo();
    }

    public List<Empresaobratarifa> getEmpresaobratarifaObservableList(int idO, int idEmpr, int idTar) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Empresaobratarifa> lista = new ArrayList<>();
            lista = session.createQuery(" FROM Empresaobratarifa WHERE obraId =: idObra AND empresaconstructoraId =: idEmp AND tarifaId =: idTarif ").setParameter("idObra", idO).setParameter("idEmp", idEmpr).setParameter("idTarif", idTar).getResultList();
            tx.commit();
            session.close();
            return lista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Empresaobrasalario> getEmpresaobrasalarioList(int idO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Empresaobrasalario> lista = new ArrayList<>();
            lista = session.createQuery("FROM Empresaobrasalario WHERE  obraId =: idOb").setParameter("idOb", idO).getResultList();
            tx.commit();
            session.close();
            return lista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void pracheObra() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int id = tarifaSalarialRepository.tarifaSalarialObservableList.get(0).getId();
            String query = "UPDATE Obra SET tarifaId = " + id;
            int opt1 = session.createQuery(query).executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }


    public void updateDatabaseEstructure() {
        setActiveResources();
        if (tarifaSalarialRepository.getAllTarifasSalarial().size() == 0) {
            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("CalPreC modificará la estructura de la base de datos..." + "\n" + " por favor espere a que el proceso termine, será informado de cualquier eventualidad!!!");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();
            List<Salario> list = getSalarios();
            createListTarifas(list);
            pracheObra();
            createModificactionsObra();
            showConfirmDialog();
        }
    }

    public void setActiveResources() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Recursos> list = new ArrayList<>();
            list = session.createSQLQuery(" SELECT * FROM Recursos WHERE active ISNULL ").getResultList();
            if (list.size() > 0) {
                int opt1 = session.createQuery(" UPDATE Recursos  set  active = 1 ").executeUpdate();
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

    public void showConfirmDialog() {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Proceso terminado!!!");
        dialog.setTitle("OK");
        new Thread(tarea).start();
        dialog.showAndWait();
    }

    private void createModificactionsObra() {
        List<Obra> listadoobras = getObrasList();
        List<Obra> temp = new ArrayList<>();
        for (Obra obra : listadoobras) {
            int idTarifa = getTarifaByDescrip(obra.getSalarioBySalarioId());
            obra.setTarifaId(idTarifa);
            temp.add(obra);
        }
        updateObrasList(temp);
        createObrasEmpresaTarifas();
    }

    private void createObrasEmpresaTarifas() {
        List<Obra> temp = new ArrayList<>();
        temp = getObrasList();
        for (Obra obra : temp) {
            List<Empresaobrasalario> list = getEmpresaobrasalarioList(obra.getId());
            asignateTarifasToObra(list, obra.getTarifaSalarialByTarifa());
        }
    }

    private void asignateTarifasToObra(List<Empresaobrasalario> list, TarifaSalarial tarifaSalarialByTarifa) {
        for (Empresaobrasalario empresaobrasalario1 : list) {
            addNewEmpresaObraTarifa(empresaobrasalario1.getObraId(), empresaobrasalario1.getEmpresaconstructoraId(), tarifaSalarialByTarifa);
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
            empresaobratarifaList.add(empresaobratarifa);
        }
        createEmpresaobratarifa(empresaobratarifaList);
    }

    private int getTarifaByDescrip(Salario salario) {
        return tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getDescripcion().trim().equals(salario.getDescripcion().trim())).findFirst().map(TarifaSalarial::getId).get();
    }

    private void createListTarifas(List<Salario> list) {
        for (Salario salario : list) {
            TarifaSalarial tarifaSalarial = new TarifaSalarial();
            if (salario.getId() == 1) {
                tarifaSalarial.setCode("PRCII");
            } else if (salario.getId() == 2) {
                tarifaSalarial.setCode("R29");
            } else if (salario.getId() == 3) {
                tarifaSalarial.setCode("R38");
            }
            tarifaSalarial.setDescripcion(salario.getDescripcion());
            tarifaSalarial.setMoneda("CUP");
            tarifaSalarial.setNomina(salario.getNomina());
            tarifaSalarial.setAntiguedad(salario.getAntiguedad());
            tarifaSalarial.setAutEspecial(salario.getAutesp());
            tarifaSalarial.setVacaciones(salario.getVacaciones());
            tarifaSalarial.setSegSocial(salario.getSeguro());

            int idT = tarifaSalarialRepository.addNuevaTarifa(tarifaSalarial);
            createGruposEscalas(idT, salario);
        }

    }

    private void createGruposEscalas(int idT, Salario salario) {
        List<GruposEscalas> gruposEscalasList = new ArrayList<>();
        GruposEscalas escalas1 = new GruposEscalas();
        escalas1.setTarifaId(idT);
        escalas1.setGrupo("II");
        escalas1.setValorBase(salario.getIi());
        escalas1.setValor(salario.getGtii());
        gruposEscalasList.add(escalas1);

        GruposEscalas escalas2 = new GruposEscalas();
        escalas2.setTarifaId(idT);
        escalas2.setGrupo("III");
        escalas2.setValorBase(salario.getIii());
        escalas2.setValor(salario.getGtiii());
        gruposEscalasList.add(escalas2);

        GruposEscalas escalas3 = new GruposEscalas();
        escalas3.setTarifaId(idT);
        escalas3.setGrupo("IV");
        escalas3.setValorBase(salario.getIv());
        escalas3.setValor(salario.getGtiv());
        gruposEscalasList.add(escalas3);

        GruposEscalas escalas4 = new GruposEscalas();
        escalas4.setTarifaId(idT);
        escalas4.setGrupo("V");
        escalas4.setValorBase(salario.getV());
        escalas4.setValor(salario.getGtv());
        gruposEscalasList.add(escalas4);

        GruposEscalas escalas5 = new GruposEscalas();
        escalas5.setTarifaId(idT);
        escalas5.setGrupo("VI");
        escalas5.setValorBase(salario.getVi());
        escalas5.setValor(salario.getGtvi());
        gruposEscalasList.add(escalas5);

        GruposEscalas escalas6 = new GruposEscalas();
        escalas6.setTarifaId(idT);
        escalas6.setGrupo("VII");
        escalas6.setValorBase(salario.getVii());
        escalas6.setValor(salario.getGtvii());
        gruposEscalasList.add(escalas6);

        GruposEscalas escalas7 = new GruposEscalas();
        escalas7.setTarifaId(idT);
        escalas7.setGrupo("VIII");
        escalas7.setValorBase(salario.getViii());
        escalas7.setValor(salario.getGtviii());
        gruposEscalasList.add(escalas7);

        GruposEscalas escalas8 = new GruposEscalas();
        escalas8.setTarifaId(idT);
        escalas8.setGrupo("IX");
        escalas8.setValorBase(salario.getIx());
        escalas8.setValor(salario.getGtix());
        gruposEscalasList.add(escalas8);

        GruposEscalas escalas9 = new GruposEscalas();
        escalas9.setTarifaId(idT);
        escalas9.setGrupo("X");
        escalas9.setValorBase(salario.getX());
        escalas9.setValor(salario.getGtx());
        gruposEscalasList.add(escalas9);

        GruposEscalas escalas10 = new GruposEscalas();
        escalas10.setTarifaId(idT);
        escalas10.setGrupo("XI");
        escalas10.setValorBase(salario.getXi());
        escalas10.setValor(salario.getGtxi());
        gruposEscalasList.add(escalas1);

        GruposEscalas escalas11 = new GruposEscalas();
        escalas11.setTarifaId(idT);
        escalas11.setGrupo("XII");
        escalas11.setValorBase(salario.getXii());
        escalas11.setValor(salario.getGtxii());
        gruposEscalasList.add(escalas11);

        GruposEscalas escalas12 = new GruposEscalas();
        escalas12.setTarifaId(idT);
        escalas12.setGrupo("XIII");
        escalas12.setValorBase(salario.getXiii());
        escalas12.setValor(salario.getGtxiii());
        gruposEscalasList.add(escalas12);

        GruposEscalas escalas13 = new GruposEscalas();
        escalas13.setTarifaId(idT);
        escalas13.setGrupo("XIV");
        escalas13.setValorBase(salario.getXiv());
        escalas13.setValor(salario.getGtxiv());
        gruposEscalasList.add(escalas13);

        GruposEscalas escalas14 = new GruposEscalas();
        escalas14.setTarifaId(idT);
        escalas14.setGrupo("XV");
        escalas14.setValorBase(salario.getXv());
        escalas14.setValor(salario.getGtxv());
        gruposEscalasList.add(escalas14);

        GruposEscalas escalas15 = new GruposEscalas();
        escalas15.setTarifaId(idT);
        escalas15.setGrupo("XVI");
        escalas15.setValorBase(salario.getXvi());
        escalas15.setValor(salario.getGtxvi());
        gruposEscalasList.add(escalas15);

        tarifaSalarialRepository.saveAllEscalas(gruposEscalasList);
        tarifaSalarialRepository.getAllTarifasSalarial();

    }

    public List<TarConceptosBases> getTarConceptosBasesList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            tarConceptosBasesList = new ArrayList<>();
            //tarConceptosBasesList = session.createQuery("FROM TarConceptosBase ").getResultList();
            tx.commit();
            session.close();
            return tarConceptosBasesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public JFXCheckBox createCheckBox(int val) {
        JFXCheckBox jfxCheckBox = new JFXCheckBox();
        if (val == 1) {
            jfxCheckBox.setSelected(true);
        } else {
            jfxCheckBox.setSelected(false);
        }
        return jfxCheckBox;
    }

    public void updateRecursosState(List<Recursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Recursos recursos : recursosArrayList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(recursos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<EspecialidadesValorIte> getValorEspecialidad(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<EspecialidadesValorIte> list = new ArrayList<>();
            List<Tuple> tupleList = session.createQuery("SELECT esp.id, esp.codigo, esp.descripcion, SUM(uo.costoMaterial), SUM(uo.costomano), SUM(uo.costoequipo) FROM Unidadobra uo INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =:idO group by esp.id, esp.codigo, esp.codigo, esp.descripcion", Tuple.class).setParameter("idO", idObra).getResultList();
            for (Tuple tuple : tupleList) {
                double suma = Double.parseDouble(tuple.get(3).toString()) + Double.parseDouble(tuple.get(4).toString()) + Double.parseDouble(tuple.get(5).toString());
                list.add(new EspecialidadesValorIte(tuple.get(1).toString(), tuple.get(2).toString(), suma));
            }
            tx.commit();
            session.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<GruposEscalas> gruposEscalasList;

    public TarifaSalarial getTarifaById(int idTarifa) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            TarifaSalarial tarifaSalarial = session.get(TarifaSalarial.class, idTarifa);
            tx.commit();
            session.close();
            return tarifaSalarial;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new TarifaSalarial();
    }

    public List<GruposEscalas> getGruposEscalasByTarifa(int idTarifa) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            gruposEscalasList = new ArrayList<>();
            gruposEscalasList = session.createQuery(" FROM GruposEscalas WHERE tarifaId =: idT ").setParameter("idT", idTarifa).getResultList();
            tx.commit();
            session.close();
            return gruposEscalasList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public double getValorManoDeObraInRV(Renglonvariante renglonvariante, Recursos recursos) {
        if (gruposEscalasList == null) {
            List<GruposEscalas> gruposEscalas = getGruposEscalasByTarifa(renglonvariante.getRs());
            gruposEscalas.size();
        }
        return gruposEscalasList.parallelStream().filter(item -> item.getGrupo().trim().equals(recursos.getGrupoescala())).findFirst().map(GruposEscalas::getValor).get();
    }

    public double getValorManoDeObraInRVUsinTarifa(TarifaSalarial tarifaSalarial, String grupo) {
        if (gruposEscalasList == null) {
            List<GruposEscalas> gruposEscalas = getGruposEscalasByTarifa(tarifaSalarial.getId());
            gruposEscalas.size();
        }
        return gruposEscalasList.parallelStream().filter(item -> item.getGrupo().trim().equals(grupo)).findFirst().map(GruposEscalas::getValor).get();
    }

    public void cleanUOinObra(Obra obra) {
        List<Unidadobra> unidadobrasList = obra.getUnidadobrasById().parallelStream().filter(item -> item.getRenglonbase() == null).collect(Collectors.toList());
        if (unidadobrasList.size() > 0) {
            for (Unidadobra unidadobra : unidadobrasList) {
                if (unidadobra.getUnidadobrabajoespecificacionById().size() > 0) {
                    deleteBajoEspecificacionList(unidadobra.getUnidadobrabajoespecificacionById().stream().collect(Collectors.toList()));
                }

                if (unidadobra.getUnidadobrarenglonsById().size() > 0) {
                    deleteUnidadDeObraRenglon(unidadobra.getUnidadobrarenglonsById().stream().collect(Collectors.toList()));
                }
            }
            deleteUnidadObra(unidadobrasList);
        }
    }

    public static List<Bajoespecificacionrv> getBajoespecificacionrvList(int idUO, int idRen) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacionrv> renglonDatos = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUO AND renglonvarianteId =: idR").setParameter("idUO", idUO).setParameter("idR", idRen).getResultList();
            tx.commit();
            session.close();
            return renglonDatos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Bajoespecificacionrv getBajoespecificacionrv(int idUO, int idRen, int idRec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Bajoespecificacionrv renglonDatos = (Bajoespecificacionrv) session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUO AND renglonvarianteId =: idR AND idsuministro =: idS").setParameter("idUO", idUO).setParameter("idR", idRen).setParameter("idS", idRec).getSingleResult();
            tx.commit();
            session.close();
            return renglonDatos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Bajoespecificacionrv();
    }

    public List<Bajoespecificacionrv> getBajoespecificacionrv(int idOb, int idRec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacionrv> bajoespecificacionrvList = new ArrayList<>();
            List<Object[]> datosUnidadBajo = session.createQuery("FROM Bajoespecificacionrv bajo INNER JOIN Nivelespecifico uo ON bajo.nivelespecificoId = uo.id WHERE uo.obraId =: idO AND bajo.idsuministro =: idS ").setParameter("idS", idRec).setParameter("idO", idOb).getResultList();
            for (Object[] row : datosUnidadBajo) {
                bajoespecificacionrvList.add((Bajoespecificacionrv) row[0]);
            }
            tx.commit();
            session.close();
            return bajoespecificacionrvList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }


    public void getElementToCompElements(String type, String tag) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (type.equals("I")) {
                catListResources = new ArrayList<>();
                catListResources = session.createQuery("FROM Recursos WHERE pertenece =: cat").setParameter("cat", tag).getResultList();
                System.out.println("Insumos importados: " + catListResources.size());
            } else if (type.equals("J")) {
                catListJuegos = new ArrayList<>();
                catListJuegos = session.createQuery(" FROM Juegoproducto WHERE pertenece =: cat").setParameter("cat", tag).getResultList();
                System.out.println("Juegos importados: " + catListJuegos.size());
            } else if (type.equals("S")) {
                catListSemielaborados = new ArrayList<>();
                catListSemielaborados = session.createQuery("From Suministrossemielaborados WHERE pertenec =: cat").setParameter("cat", tag).getResultList();
                System.out.println("Insumos importados: " + catListSemielaborados.size());
            } else if (type.equals("G")) {
                catSubgrupoList = new ArrayList<>();
                catSubgrupoList = session.createQuery("FROM Subgrupo ").getResultList();
            } else if (type.equals("R")) {
                catRenglonesList = new ArrayList<>();
                catRenglonesList = session.createQuery("FROM Renglonvariante WHERE rs =: cat").setParameter("cat", Integer.parseInt(tag)).getResultList();
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


    public void reviewRCTree() {
        FileReader fileReader = null;
        JSONParser parser = new JSONParser();
        try {
            fileReader = new FileReader(new File("Util/Grupos.json"));
            Object object = parser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray insumolist = (JSONArray) jsonObject.get("Grupos");
            for (Object o : insumolist) {
                JSONObject item = (JSONObject) o;
                String code = "";
                if (item.get("Tipo").toString().trim().equals("1")) {
                    code = item.get("Codigo").toString().trim().substring(0, 2);
                    try {
                        System.out.println(getSobreGrupo(code.trim()).getCodigo() + " OK");
                    } catch (NoResultException e) {
                        Sobregrupo sobregrupo = new Sobregrupo();
                        sobregrupo.setCodigo(code.trim());
                        sobregrupo.setDescipcion(item.get("Descripcion").toString().trim());
                        Integer id = addRCTreeG1(sobregrupo);
                        System.out.println("Fail Type 1 " + id);
                    }
                } else if (item.get("Tipo").toString().trim().equals("2")) {
                    code = item.get("Codigo").toString().trim().substring(0, 3);
                    try {
                        System.out.println(getGrupo(code.trim()).getCodigog() + " OK");
                    } catch (NoResultException e) {
                        Grupo grupo = new Grupo();
                        grupo.setCodigog(code);
                        grupo.setDescripciong(item.get("Descripcion").toString().trim());
                        grupo.setSobregrupoId(getSobreGrupo(code.substring(0, 2).trim()).getId());
                        Integer id = addRCTreeG2(grupo);
                        System.out.println("Fail Type 2 " + id);
                    }
                } else if (item.get("Tipo").toString().trim().equals("3")) {
                    code = item.get("Codigo").toString().trim();
                    try {
                        System.out.println(getSubgrupo(code.trim()).getCodigosub() + " OK");
                    } catch (NoResultException e) {
                        Subgrupo subgrupo = new Subgrupo();
                        subgrupo.setCodigosub(code);
                        subgrupo.setDescripcionsub(item.get("Descripcion").toString().trim());
                        subgrupo.setGrupoId(getGrupo(code.substring(0, 3).trim()).getId());
                        Integer id = addRCTreeG3(subgrupo);
                        System.out.println("Fail Type 3 " + code);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterSequenceRCTreeG1() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("SELECT setval('sobregrupo_id_seq', (SELECT MAX(id) FROM sobregrupo))").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void alterSequenceRCTreeG2() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("SELECT setval('grupo_id_seq', (SELECT MAX(id) FROM grupo))").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void alterSequenceRCTreeG3() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("SELECT setval('subgrupo_id_seq', (SELECT MAX(id) FROM subgrupo))").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void alterTableTagColumn() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            Integer op1 = session.createSQLQuery("alter table salario alter column tag type character varying(5);").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public Recursos getSuministro(int idSum) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos recursos = (Recursos) session.createQuery("FROM Recursos WHERE id =: idR").setParameter("idR", idSum).getSingleResult();
            tx.commit();
            session.close();
            return recursos;
        } catch (
                Exception e) {

            System.out.println(idSum);
        }
        return new Recursos();
    }

    public List<Recursos> getRecursosEquiposList(String tag) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recursosEquiposList = new ArrayList<>();
            if (tag.trim().equals("T15")) {
                recursosEquiposList = session.createQuery("FROM Recursos WHERE pertenece = 'R266' ").getResultList();
            } else {
                recursosEquiposList = session.createQuery("FROM Recursos WHERE pertenece =: res ").setParameter("res", tag).getResultList();
            }
            tx.commit();
            session.close();
            return recursosEquiposList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Integer addRCTreeG1(Sobregrupo sg) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(sg);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }


    public Grupoconstruccion getGrupoConstruccion(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Grupoconstruccion grupo = (Grupoconstruccion) session.createQuery("FROM Grupoconstruccion WHERE id =: idG").setParameter("idG", id).getSingleResult();
            tx.commit();
            session.close();
            return grupo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Grupoconstruccion();
    }

    private Integer addRCTreeG2(Grupo gp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(gp);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    private Integer addRCTreeG3(Subgrupo sgp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(sgp);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public void showRVEstructure(int tag) {
        getElementToCompElements("R", String.valueOf(tag));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar fichero de comprobación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Datos");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 2;
            for (Renglonvariante renglonvariante : catRenglonesList) {
                double costoMano = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMaterial = costMater + costMaterSemi + costMaterJueg;
                data.put("2", new Object[]{"RV", "Costo Material RV", "Costo Material Cal", "Costo Mano RV", "Costo Mano Cal", "Costo Equipo RV", "Costo Equipo Cal"});
                countrow++;
                Object[] datoscl = new Object[7];
                datoscl[0] = renglonvariante.getCodigo();
                datoscl[1] = renglonvariante.getCostomat();
                datoscl[2] = Double.toString(new BigDecimal(String.format("%.2f", costMaterial)).doubleValue());
                datoscl[3] = renglonvariante.getCostmano();
                datoscl[4] = Double.toString(new BigDecimal(String.format("%.2f", costoMano)).doubleValue());
                datoscl[5] = renglonvariante.getCostequip();
                datoscl[6] = Double.toString(new BigDecimal(String.format("%.2f", costoEq)).doubleValue());
                data.put(String.valueOf(countrow++), datoscl);
            }
            Set<String> keySet = data.keySet();
            int rownum = 0;
            for (String key : keySet) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;

                for (Object obj : objects) {
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Double)
                        cell.setCellValue((Double) obj);
                }
            }
            try {
                // this Writes the workbook gfgcontribute
                FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
                workbook.write(out);
                out.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trabajo Terminado");
                alert.setContentText("Fichero dispobible en: " + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            /*

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Datos");

            Map<String, Object[]> data = new HashMap<>();
            data.put("1", new Object[]{"Insumos: " + catListResources.size(), " ", " ", " "});
            data.put("2", new Object[]{"Juego de Productos: " + catListJuegos.size(), " ", " ", ""});
            data.put("3", new Object[]{"Suministros Semielaborados: " + catListSemielaborados.size(), " ", " ", " "});
            data.put("4", new Object[]{"RC", "Costo Material", "Costo Mano Obra", "Costo Equipo"});
            int countrow = 5;
            for (Renglonvariante renglonvariante : catRenglonesList) {
                countrow++;
                Object[] datoscl = new Object[4];
                datoscl[0] = renglonvariante.getCodigo();
                datoscl[1] = renglonvariante.getCostomat();
                datoscl[2] = renglonvariante.getCostmano();
                datoscl[3] = renglonvariante.getCostequip();
                data.put(String.valueOf(countrow++), datoscl);
            }
            Set<String> keySet = data.keySet();
            int rownum = 0;
            for (String key : keySet) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;

                for (Object obj : objects) {
                    Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Double)
                        cell.setCellValue((Double) obj);
                }
            }
            try {
                // this Writes the workbook gfgcontribute
                FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
                workbook.write(out);
                out.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trabajo Terminado");
                alert.setContentText("Fichero dispobible en: " + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    public void fixesInsumos() {
        List<Recursos> tempList = new ArrayList<>();
        for (Renglonvariante renglonvariante : renglonvarianteList) {
            for (Renglonrecursos r : renglonvariante.getRenglonrecursosById()) {
                tempList.add(EnderasaInsumos(r.getRecursosId(), renglonvariante.getRs()));
            }
        }
        updateRecursosState(tempList);
    }

    private Recursos EnderasaInsumos(int idR, int tag) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos recursos = (Recursos) session.createQuery("FROM Recursos WHERE id =: idRec").setParameter("idRec", idR).getSingleResult();
            if (tag == 2) {
                recursos.setPertenece("T15");
            } else if (tag == 3) {
                recursos.setPertenece("R266");
            } else if (tag == 5) {
                recursos.setPertenece("TR5");
            }
            tx.commit();
            session.close();
            return recursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Recursos();
    }

    public void clearNullInEquipments() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            Integer op1 = session.createSQLQuery("UPDATE recursos set salario = 0 WHERE salario ISNULL").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void clearBajoEspecificacioUO() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            Integer op1 = session.createSQLQuery("DELETE FROM bajoespecificacion WHERE id_suministro = 0").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void persistListEmpresaObraTarifa(List<Empresaobratarifa> empresaobratarifaList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Empresaobratarifa eot : empresaobratarifaList) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.saveOrUpdate(eot);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTarifaEmpresaObra(int idO, int idEm) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            Integer op1 = session.createQuery("DELETE FROM Empresaobratarifa WHERE obraId =: ob AND empresaconstructoraId =: emp").setParameter("ob", idO).setParameter("emp", idEm).executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void updateTarifaEmpresaObra() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op1 = session.createSQLQuery("UPDATE empresaobratarifa SET tarifa_inc = 0 WHERE tarifa_inc isNull ").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public List<Object[]> getRowsQueryResults(String queryString) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> objectList = session.createQuery(queryString).getResultList();

            tx.commit();
            session.close();
            return objectList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Object[]> getRowsQueryResultsNative(String queryString) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> objectList = session.createSQLQuery(queryString).getResultList();

            tx.commit();
            session.close();
            return objectList;
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public ObservableList<UniddaObraToIMportView> getUniddaObraViewObservableList(int idObra, int idEmp, int idZonas, int idObjetos, int idNivel, int idEsp, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            uniddaObraViewObservableList = FXCollections.observableArrayList();
            unidadobraList = new ArrayList<>();

            unidadobraList = session.createQuery(" FROM Unidadobra WHERE obraId=:idO AND empresaconstructoraId=:idEmp AND zonasId=:idZ AND objetosId=:idOb AND nivelId=:nivId AND especialidadesId=:idEsp AND subespecialidadesId=:idSub").setParameter("idO", idObra).setParameter("idEmp", idEmp).setParameter("idZ", idZonas).setParameter("idOb", idObjetos).setParameter("nivId", idNivel).setParameter("idEsp", idEsp).setParameter("idSub", idSub).getResultList();
            for (Unidadobra unidadobra : unidadobraList) {
                JFXCheckBox jfxCheckBox = new JFXCheckBox(unidadobra.getCodigo());
                uniddaObraViewObservableList.add(new UniddaObraToIMportView(unidadobra.getId(), jfxCheckBox, unidadobra.getDescripcion(), unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId()));
            }
            tx.commit();
            session.close();
            return uniddaObraViewObservableList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

}
