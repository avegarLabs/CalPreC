package views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class PlanificarRenglonVarianteController implements Initializable {

    private static SessionFactory sf;
    public double materialesPlanificado;
    public double cantCert;
    public double cantMaterial;
    PlanificarRenglonVarianteController planificarRenglonVarianteController;
    @FXML
    private Label labelTitle;
    @FXML
    private TextArea textDescripcion;
    @FXML
    private Label labelCosto;
    @FXML
    private JFXComboBox<String> combo_Brigada;
    @FXML
    private JFXComboBox<String> combogrupo;
    @FXML
    private JFXComboBox<String> combocuadrilla;
    @FXML
    private JFXDatePicker dateini;
    @FXML
    private JFXDatePicker datehasta;
    @FXML
    private TableView<PlanRenglonVarianteView> tablePlanificacionRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, JFXCheckBox> codeRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> volumenRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> disponibleRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> cantidadcertif;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> costMatRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> costManoRenglon;
    @FXML
    private TableColumn<PlanRenglonVarianteView, DoubleProperty> costEquipoRenglon;
    @FXML
    private TableView<PlanMaterialesRVView> tablePlanificacionMateriales;
    @FXML
    private TableColumn<PlanMaterialesRVView, JFXCheckBox> codigoNateriales;
    @FXML
    private TableColumn<PlanMaterialesRVView, DoubleProperty> volumenMaterial;
    @FXML
    private TableColumn<PlanMaterialesRVView, DoubleProperty> disponibleMaterial;
    @FXML
    private TableColumn<PlanMaterialesRVView, DoubleProperty> cantCertMaterial;
    @FXML
    private TableColumn<PlanMaterialesRVView, DoubleProperty> costMaterial;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionsArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;
    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionsArrayList;
    private ObservableList<String> brigadasList;
    private ObservableList<String> grupoList;
    private ObservableList<String> cuadrilaList;
    private Nivelespecifico nivelespecificoModel;
    private Integer nivelEspId;
    private Integer brigadaId;
    private Integer grupoId;
    private Integer cuadrillaId;
    private double costMaterialCert;
    private double costMano;
    private double costManoCert;
    private double costEquipo;
    private double costEquipoCert;
    private double costSalario;
    private double costSalarioCert;
    private double costCUCSalario;
    private double costCUCSalarioCert;
    private double valCM;
    private double valCMan;
    private double valEquip;
    private double valSal;
    private double valCucSal;
    private double costoTotalRV;
    private PlanificacionRenglon planificacionRenglon;
    private Date desdeDate;
    private Date hastaData;
    private double cantidadCert;
    private int IdObra;
    private int idEmp;
    private int idZona;
    private int idObj;
    private int idNiv;
    private int idEsp;
    private int idSub;
    private JFXCheckBox checkBox;
    private JFXCheckBox checkBox1;
    private Planrenglonvariante planificacion;
    private ArrayList<Planrenglonvariante> planificacionArrayList;
    private ObservableList<PlanRenglonVarianteView> planificaPlanRenglonVarianteViewObservableList;
    private PlanRenglonVarianteView planRenglonVarianteView;
    private ArrayList<Renglonnivelespecifico> renglonnivelespecificoArrayList;
    private ArrayList<Renglonrecursos> recursosArrayList;
    private ArrayList<Renglonjuego> juegoproductoArrayList;
    private ArrayList<Renglonsemielaborados> semielaboradosrecursosArrayList;
    private ObservableList<PlanMaterialesRVView> planMaterialesRVViewObservableList;
    private PlanMaterialesRVView planMaterialesRVView;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionrvArrayList;
    @FXML
    private JFXCheckBox checkActive;
    private Double[] datosRecursos;
    private Brigadaconstruccion brigadaCont;

    public ObservableList<PlanRenglonVarianteView> getPlanificacionNivelEspecifico(int idNivel) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planificaPlanRenglonVarianteViewObservableList = FXCollections.observableArrayList();
            costoTotalRV = 0.0;
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: id").setParameter("id", idNivel).getResultList();
            for (Renglonnivelespecifico renglonnivelespecifico : renglonnivelespecificoArrayList) {
                planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);
                if (planificacionRenglon.getCantPlanif() == 0) {
                    checkBox = new JFXCheckBox();
                    checkBox.setText(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo());
                    checkBox.setSelected(false);
                    cantCert = getTotalCertificada(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                    costoTotalRV = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    planRenglonVarianteView = new PlanRenglonVarianteView(renglonnivelespecifico.getRenglonvarianteId(), checkBox, renglonnivelespecifico.getCantidad(), renglonnivelespecifico.getCantidad() - cantCert, 0.0, 0.0, 0.0, 0.0, renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), costoTotalRV);
                    planificaPlanRenglonVarianteViewObservableList.add(planRenglonVarianteView);

                } else {
                    checkBox = new JFXCheckBox();
                    checkBox.setText(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo());
                    checkBox.setSelected(true);
                    cantCert = 0.0;
                    cantCert = getTotalCertificada(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                    costoTotalRV = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    planRenglonVarianteView = new PlanRenglonVarianteView(renglonnivelespecifico.getRenglonvarianteId(), checkBox, renglonnivelespecifico.getCantidad(), renglonnivelespecifico.getCantidad() - cantCert, planificacionRenglon.getCantPlanif(), planificacionRenglon.getCostMatPlan(), Math.round(planificacionRenglon.getCostManoPlan() * 1000d) / 1000d, planificacionRenglon.getCostEquipPlan(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), costoTotalRV);
                    planificaPlanRenglonVarianteViewObservableList.add(planRenglonVarianteView);
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
        return planificaPlanRenglonVarianteViewObservableList;
    }

    private double getCostMaterialPlan(int nivelespecificoId, int renglonvarianteId) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            materialesPlanificado = 0.0;
            Query querydatosplan = session.createQuery(" SELECT SUM(plrv.costo) FROM Planrecrv plrv INNER JOIN Recursos rec ON plrv.recursoId = rec.id WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev AND rec.tipo = '1'").setParameter("idNiv", nivelespecificoId).setParameter("idRev", renglonvarianteId);

            if (querydatosplan.getSingleResult() != null) {
                materialesPlanificado = (Double) querydatosplan.getSingleResult();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return materialesPlanificado;

    }

    public double getTotalCertificada(Integer idNivel, Integer idRbg) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT SUM(cantidad) FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idNiV AND renglonvarianteId =: idRen").setParameter("idNiV", idNivel).setParameter("idRen", idRbg);
            cantCert = 0.0;
            if (query.getSingleResult() != null) {
                cantCert = (Double) query.getSingleResult();
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantCert;
    }

    public PlanificacionRenglon getPlanificacionNivelEspecificoRenglon(Renglonnivelespecifico renglonnivelespecifico) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> querydatosplan = session.createQuery(" SELECT SUM(plrv.cantidad), SUM(plrv.costomaterial), SUM(plrv.costomano), SUM(plrv.costoequipo), SUM(plrv.salario) FROM Planrenglonvariante plrv INNER JOIN Renglonvariante rv ON plrv.renglonvarianteId = rv.id WHERE plrv.nivelespecificoId =: idNiv AND plrv.renglonvarianteId =: idRev").setParameter("idNiv", renglonnivelespecifico.getNivelespecificoId()).setParameter("idRev", renglonnivelespecifico.getRenglonvarianteId()).getResultList();
            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            costManoCert = 0.0;
            costEquipoCert = 0.0;
            costSalarioCert = 0.0;
            materialesPlanificado = 0.0;

            for (Object[] row : querydatosplan) {
                //  materialesPlanificado = getCostMaterialPlan(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                if (row[0] != null) {
                    cantidadCert = Double.parseDouble(row[0].toString());
                }
                if (row[1] != null) {
                    costMaterialCert = Double.parseDouble(row[1].toString());
                }

                if (row[2] != null) {
                    costManoCert = Double.parseDouble(row[2].toString());
                }
                if (row[3] != null) {
                    costEquipoCert = Double.parseDouble(row[3].toString());
                }
                if (row[4] != null) {
                    costSalarioCert = Double.parseDouble(row[4].toString());
                }
                planificacionRenglon = new PlanificacionRenglon(cantidadCert, costMaterialCert, costManoCert, costEquipoCert, costSalarioCert);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planificacionRenglon;
    }

    public ObservableList<PlanMaterialesRVView> getPlanMaterialesRVViewObservableListo(int idNivel, int idRVa) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planMaterialesRVViewObservableList = FXCollections.observableArrayList();
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: id AND renglonvarianteId =: idR").setParameter("id", idNivel).setParameter("idR", idRVa).getResultList();
            costoTotalRV = 0.0;
            for (Renglonnivelespecifico renglonnivelespecifico : renglonnivelespecificoArrayList) {
                if (renglonnivelespecifico.getConmat().contentEquals("0 ")) {
                    planMaterialesRVViewObservableList = getMaterialesRVBajoEsp(renglonnivelespecifico);
                } else if (renglonnivelespecifico.getConmat().contentEquals("1 ")) {
                    planMaterialesRVViewObservableList = getComponetesRV(renglonnivelespecifico);
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
        return planMaterialesRVViewObservableList;
    }

    private Double[] getCostosCertificadosRec(int nivelespecificoId, int renglonvarianteId, int idRec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            datosRecursos = new Double[2];
            List<Object[]> querydatosplan = session.createQuery(" SELECT SUM(plrv.cantidad), SUM(plrv.costo) FROM Planrecrv plrv WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev AND plrv.recursoId =: idRec ").setParameter("idNiv", nivelespecificoId).setParameter("idRev", renglonvarianteId).setParameter("idRec", idRec).getResultList();

            for (Object[] row : querydatosplan) {
                datosRecursos[0] = Double.parseDouble(row[0].toString());
                datosRecursos[1] = Double.parseDouble(row[1].toString());

            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return datosRecursos;
    }

    private ObservableList<PlanMaterialesRVView> getComponetesRV(Renglonnivelespecifico renglonnivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planMaterialesRVViewObservableList = FXCollections.observableArrayList();
            recursosArrayList = (ArrayList<Renglonrecursos>) session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId =: idRV").setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId()).getResultList();

            costoTotalRV = 0.0;
            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);

            for (Renglonrecursos renglonrecursos : recursosArrayList) {
                if (renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("1")) {
                    Recursos recursos = session.get(Recursos.class, renglonrecursos.getRecursosId());
                    costoTotalRV = Math.round(renglonrecursos.getCantidas() * recursos.getPreciomn() * 100d) / 100d;
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(recursos.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), recursos.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        planMaterialesRVView = new PlanMaterialesRVView(recursos.getId(), checkBox1, renglonrecursos.getCantidas(), Math.round(renglonrecursos.getCantidas() - cantidadCert * 100d) / 100d, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, recursos.getDescripcion(), recursos.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(recursos.getId(), checkBox1, renglonrecursos.getCantidas(), 0.0, 0.0, costoTotalRV, recursos.getDescripcion(), recursos.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }

                }

            }

            costoTotalRV = 0.0;
            juegoproductoArrayList = (ArrayList<Renglonjuego>) session.createQuery("FROM Renglonjuego WHERE renglonvarianteId =: idRV").setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId()).getResultList();
            for (Renglonjuego renglonjuego : juegoproductoArrayList) {
                Juegoproducto juegoproducto = session.get(Juegoproducto.class, renglonjuego.getJuegoproductoId());
                costoTotalRV = Math.round(renglonjuego.getCantidad() * juegoproducto.getPreciomn() * 100d) / 100d;
                checkBox1 = new JFXCheckBox();
                checkBox1.setText(juegoproducto.getCodigo());
                checkBox1.setSelected(true);
                if (planificacionRenglon.getCantPlanif() != 0) {
                    datosRecursos = new Double[0];
                    datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), juegoproducto.getId());
                    cantidadCert = datosRecursos[0];
                    costMaterialCert = datosRecursos[1];

                    planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, renglonjuego.getCantidad(), Math.round(renglonjuego.getCantidad() - cantidadCert * 100d) / 100d, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                    planMaterialesRVViewObservableList.add(planMaterialesRVView);
                } else {
                    planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, renglonjuego.getCantidad(), 0.0, 0.0, costoTotalRV, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                    planMaterialesRVViewObservableList.add(planMaterialesRVView);
                }
            }

            semielaboradosrecursosArrayList = (ArrayList<Renglonsemielaborados>) session.createQuery("FROM Renglonsemielaborados WHERE renglonvarianteId =: idRV").setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId()).getResultList();
            costoTotalRV = 0.0;
            for (Renglonsemielaborados renglonsemielaborados : semielaboradosrecursosArrayList) {
                Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, renglonsemielaborados.getSuministrossemielaboradosId());
                costoTotalRV = Math.round(renglonsemielaborados.getCantidad() * suministrossemielaborados.getPreciomn() * 100d) / 100d;
                checkBox1 = new JFXCheckBox();
                checkBox1.setText(suministrossemielaborados.getCodigo());
                checkBox1.setSelected(true);
                if (planificacionRenglon.getCantPlanif() != 0) {
                    datosRecursos = new Double[0];
                    datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), suministrossemielaborados.getId());
                    cantidadCert = datosRecursos[0];
                    costMaterialCert = datosRecursos[1];

                    planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, renglonsemielaborados.getCantidad(), Math.round(renglonsemielaborados.getCantidad() - cantidadCert * 100d) / 100d, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                    planMaterialesRVViewObservableList.add(planMaterialesRVView);
                } else {
                    planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, renglonsemielaborados.getCantidad(), 0.0, 0.0, costoTotalRV, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                    planMaterialesRVViewObservableList.add(planMaterialesRVView);
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
        return planMaterialesRVViewObservableList;


    }

    private ObservableList<PlanMaterialesRVView> getMaterialesRVBajoEsp(Renglonnivelespecifico renglonnivelespecifico) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planMaterialesRVViewObservableList = FXCollections.observableArrayList();
            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) session.createQuery("FROM Bajoespecificacionrv WHERE renglonvarianteId =: idRV AND nivelespecificoId =: idNE").setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId()).setParameter("idNE", renglonnivelespecifico.getNivelespecificoId()).getResultList();

            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);

            for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionrvArrayList) {
                if (bajoespecificacionrv.getTipo().contentEquals("1")) {
                    Recursos recursos = session.get(Recursos.class, bajoespecificacionrv.getIdsuministro());
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(recursos.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), recursos.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        cantCert = getTotalCertificada(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                        cantMaterial = bajoespecificacionrv.getCantidad() * cantCert / renglonnivelespecifico.getCantidad();

                        double disp = bajoespecificacionrv.getCantidad() - cantMaterial;

                        planMaterialesRVView = new PlanMaterialesRVView(recursos.getId(), checkBox1, bajoespecificacionrv.getCantidad(), Math.round(disp) * 1000d / 1000d, Math.round(cantidadCert * 1000d) / 1000d, Math.round(costMaterialCert * 1000d) / 1000d, recursos.getDescripcion(), recursos.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(recursos.getId(), checkBox1, bajoespecificacionrv.getCantidad(), 0.0, 0.0, bajoespecificacionrv.getCosto(), recursos.getDescripcion(), recursos.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }


                } else if (bajoespecificacionrv.getTipo().contentEquals("J")) {
                    Juegoproducto juegoproducto = session.get(Juegoproducto.class, bajoespecificacionrv.getIdsuministro());
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(juegoproducto.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), juegoproducto.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        cantCert = getTotalCertificada(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                        cantMaterial = bajoespecificacionrv.getCantidad() * cantCert / renglonnivelespecifico.getCantidad();

                        double disp = bajoespecificacionrv.getCantidad() - cantMaterial;
                        planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, bajoespecificacionrv.getCantidad(), Math.round(disp) * 1000d / 1000d, Math.round(cantidadCert * 1000d) / 1000d, Math.round(costMaterialCert * 100d) / 100d, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, bajoespecificacionrv.getCantidad(), 0.0, 0.0, bajoespecificacionrv.getCosto(), juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }

                } else if (bajoespecificacionrv.getTipo().contentEquals("S")) {
                    Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, bajoespecificacionrv.getIdsuministro());
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(suministrossemielaborados.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), suministrossemielaborados.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        cantCert = getTotalCertificada(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
                        cantMaterial = bajoespecificacionrv.getCantidad() * cantCert / renglonnivelespecifico.getCantidad();
                        double disp = bajoespecificacionrv.getCantidad() - cantMaterial;
                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, bajoespecificacionrv.getCantidad(), Math.round(disp) * 1000d / 1000d, Math.round(cantidadCert * 1000d) / 1000d, Math.round(costMaterialCert * 100d) / 100d, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, bajoespecificacionrv.getCantidad(), 0.0, 0.0, bajoespecificacionrv.getCosto(), suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }


                }
            }
            tx.commit();
            session.close();
            return planMaterialesRVViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> listOfBrigadas(int idEmp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadasList = FXCollections.observableArrayList();
            brigadaconstruccionsArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion WHERE empresaconstructoraId =: empId").setParameter("empId", idEmp).getResultList();
            for (Brigadaconstruccion brigadaconstruccion : brigadaconstruccionsArrayList) {
                brigadasList.add(brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion());
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadasList;
    }

    public ObservableList<String> listOfGrupos(int idBrigada) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoList = FXCollections.observableArrayList();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion WHERE brigadaconstruccionId =: briId").setParameter("briId", idBrigada).getResultList();
            for (Grupoconstruccion grupoconstruccion : grupoconstruccionArrayList) {
                grupoList.add(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoList;
    }

    public ObservableList<String> listOfCuadrilla(int idGrupo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuadrilaList = FXCollections.observableArrayList();
            cuadrillaconstruccionsArrayList = (ArrayList<Cuadrillaconstruccion>) session.createQuery("FROM Cuadrillaconstruccion WHERE grupoconstruccionId =: grupId").setParameter("grupId", idGrupo).getResultList();
            for (Cuadrillaconstruccion cuadrillaconstruccion : cuadrillaconstruccionsArrayList) {
                cuadrilaList.add(cuadrillaconstruccion.getCodigo() + " - " + cuadrillaconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cuadrilaList;
    }

    public Nivelespecifico getNivel(int idNiv) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivelespecificoModel = session.get(Nivelespecifico.class, idNiv);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return nivelespecificoModel;
    }

    public void deletePlanificacion(int idCert) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Integer op = session.createQuery(" DELETE FROM Planrecrv where renglonId =:idP").setParameter("idP", idCert).executeUpdate();
            Integer op2 = session.createQuery(" DELETE FROM Planrenglonvariante where renglonvarianteId =:idP").setParameter("idP", idCert).executeUpdate();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_Brigada.getValue().split(" - ");
        String code = codeBriPart[0];

        Brigadaconstruccion brig = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().equals(code)).findFirst().orElse(null);
        brigadaId = brig.getId();
        grupoList = listOfGrupos(brigadaId);
        combogrupo.setItems(grupoList);
    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupPart = combogrupo.getValue().split(" - ");
        String code = codeGrupPart[0];

        Grupoconstruccion group = grupoconstruccionArrayList.stream().filter(g -> g.getCodigo().equals(code)).findFirst().orElse(null);
        grupoId = group.getId();
        cuadrilaList = listOfCuadrilla(grupoId);
        combocuadrilla.setItems(cuadrilaList);
    }

    public void loadDatosCertificaciones(Integer nivelId) {
        codeRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, JFXCheckBox>("codigoRV"));
        volumenRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("cantRV"));
        disponibleRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("disponible"));
        cantidadcertif.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("cantCertificada"));
        costMatRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoMaterial"));
        costManoRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoMano"));
        costEquipoRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoEquipo"));

        planificaPlanRenglonVarianteViewObservableList = FXCollections.observableArrayList();
        planificaPlanRenglonVarianteViewObservableList = getPlanificacionNivelEspecifico(nivelId);
        tablePlanificacionRenglon.getItems().setAll(planificaPlanRenglonVarianteViewObservableList);

    }

    public void loadMaterialesCertificaciones(Integer idNiv, Integer idReV) {
        codigoNateriales.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, JFXCheckBox>("codigoRV"));
        volumenMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("cantRV"));
        disponibleMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("disponible"));
        cantCertMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("cantCertificada"));
        costMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("costoMaterial"));

        planMaterialesRVViewObservableList = FXCollections.observableArrayList();
        planMaterialesRVViewObservableList = getPlanMaterialesRVViewObservableListo(idNiv, idReV);
        tablePlanificacionMateriales.getItems().setAll(planMaterialesRVViewObservableList);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        planificarRenglonVarianteController = this;

        tablePlanificacionRenglon.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            planRenglonVarianteView = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();
            textDescripcion.setText(planRenglonVarianteView.getDescripcion());
            labelCosto.setText(String.valueOf(planRenglonVarianteView.getCostoTotal()));


            loadMaterialesCertificaciones(nivelespecificoModel.getId(), planRenglonVarianteView.getId());


        });

        checkActive.setOnMouseClicked(event -> {
            if (checkActive.isSelected() == true) {
                combocuadrilla.setDisable(false);
                String[] codeGrupPart = combogrupo.getValue().split(" - ");
                String code = codeGrupPart[0];

                Grupoconstruccion group = grupoconstruccionArrayList.stream().filter(g -> g.getCodigo().equals(code)).findFirst().orElse(null);
                grupoId = group.getId();
                cuadrilaList = listOfCuadrilla(grupoId);
                combocuadrilla.setItems(cuadrilaList);
            } else if (checkActive.isSelected() == false) {
                combocuadrilla.setDisable(true);
                combocuadrilla.getSelectionModel().clearSelection();
            }

        });

        LocalDate t1 = LocalDate.now();
        LocalDate inicio = t1.with(firstDayOfMonth());
        LocalDate fin = t1.with(lastDayOfMonth());

        dateini.setValue(inicio);
        datehasta.setValue(fin);

    }

    public void handlePlanificar(ActionEvent event) {
        planRenglonVarianteView = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();

        if (combo_Brigada.getValue() == null || combogrupo.getValue() == null || dateini.getValue() == null || datehasta.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Complete los valores de formulario antes de planificar!");
            alert.showAndWait();

        } else {


            LocalDate dateDes = dateini.getValue();
            LocalDate dateHast = datehasta.getValue();
            desdeDate = Date.valueOf(dateDes);
            hastaData = Date.valueOf(dateHast);

            if (checkActive.isSelected() == true) {
                String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                String code = codeCuadriPart[0];

                cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                    if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
                        cuadrillaId = cuadrillaconstruccion.getId();
                    }
                });

            } else if (checkActive.isSelected() == false) {
                cuadrillaId = null;
            }


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PlanificarRV.fxml"));
                Parent proot = loader.load();
                AddPlanRenglonVarianteController controller = loader.getController();
                controller.cargarDatos(planificarRenglonVarianteController, nivelespecificoModel.getId(), planRenglonVarianteView.getId(), brigadaId, grupoId, cuadrillaId, desdeDate, hastaData);


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.show();

                stage.setOnCloseRequest(event1 -> {
                    loadDatosCertificaciones(nivelEspId);

                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }

    public void cargarUnidadeObra(int idNEsp, int idEm, int idZon, int idOb, int idNi, int idEs, int idSu) {

        nivelEspId = idNEsp;
        brigadasList = listOfBrigadas(idEm);
        combo_Brigada.setItems(brigadasList);

        idEmp = idEm;
        idZona = idZon;
        idObj = idOb;
        idNiv = idNi;
        idEsp = idEs;
        idSub = idSu;

        loadDatosCertificaciones(idNEsp);

        nivelespecificoModel = getNivel(idNEsp);
        labelTitle.setText("Planificaciones en el nivel especifico " + nivelespecificoModel.getCodigo());

        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;

        brigadasList = listOfBrigadas(nivelespecificoModel.getEmpresaconstructoraByEmpresaconstructoraId().getId());
        if (brigadasList.size() > 0) {
            combo_Brigada.setItems(brigadasList);

            combo_Brigada.getSelectionModel().select(brigadasList.get(0));
            String[] codeBriPart = brigadasList.get(0).split(" - ");
            brigadaCont = brigadaconstruccionsArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
            brigadaId = brigadaCont.getId();
            grupoList = listOfGrupos(brigadaCont.getId());
            combogrupo.setItems(grupoList);
            combogrupo.getSelectionModel().select(grupoList.get(0));
        }


    }


    public void handleDescertificar(ActionEvent event) {
        var certific = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();

        deletePlanificacion(certific.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Certificación eliminada");
        // alert.setContentText(detalle);
        alert.showAndWait();

        loadDatosCertificaciones(nivelEspId);

        cantidadCert = 0.0;
        costMaterialCert = 0.0;
        costManoCert = 0.0;
        costEquipoCert = 0.0;
        costCUCSalarioCert = 0.0;
        costSalarioCert = 0.0;

    }


}
