package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJGroupLabel;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class CertificarRenglonVarianteController implements Initializable {

    CertificarRenglonVarianteController certificarRenglonVarianteController;
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
    private int nivelEspId;
    private int brigadaId;
    private int grupoId;
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
    @FXML
    private JFXCheckBox checkActive;
    private Planrenglonvariante planificacion;
    private ArrayList<CertificacionRenglonVariante> planificacionArrayList;
    private ObservableList<PlanRenglonVarianteView> planificaPlanRenglonVarianteViewObservableList;
    private PlanRenglonVarianteView planRenglonVarianteView;
    private ArrayList<Renglonnivelespecifico> renglonnivelespecificoArrayList;
    private ArrayList<Renglonrecursos> recursosArrayList;
    private ArrayList<Renglonjuego> juegoproductoArrayList;
    private ArrayList<Renglonsemielaborados> semielaboradosrecursosArrayList;
    private ObservableList<PlanMaterialesRVView> planMaterialesRVViewObservableList;
    private PlanMaterialesRVView planMaterialesRVView;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionrvArrayList;
    private ArrayList<Certificacionrecrv> certificacionrecrvArrayList;
    private Double[] datosRecursos;
    private Integer IdReg;
    private Integer idObra;

    private ObservableList<DetalleCertificaReportView> detalleCertificaViewObservableList;
    private DetalleCertificaReportView detalleCertificaView;

    private Map parametros;
    private LocalDate date;
    private Empresa empresa;
    private Obra obra;

    @FXML
    private MenuItem menuDetalle;

    @FXML
    private MenuItem menucambiar;

    @FXML
    private JFXCheckBox checkExp;

    @FXML
    private MenuItem menuMotCambio;

    private Double materialesPlanificado;

    private String detalle;
    private Certificacionrecrv certificacionrecrv;
    private Bajoespecificacionrv bajo;
    /**
     * Cantidad de los recursos para calcular el disponible
     */

    private double cantPresup;
    private boolean recFlag;
    private Brigadaconstruccion brigadaCont;
    private StringBuilder label;

    private double getCostMaterialPlan(int nivelespecificoId, int renglonvarianteId) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            materialesPlanificado = 0.0;
            Query querydatosplan = session.createQuery(" SELECT SUM(plrv.costo) FROM Certificacionrecrv plrv INNER JOIN Recursos rec ON plrv.recursoId = rec.id WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev AND rec.tipo = '1'").setParameter("idNiv", nivelespecificoId).setParameter("idRev", renglonvarianteId);
            if (querydatosplan.getSingleResult() != null) {
                materialesPlanificado = (Double) querydatosplan.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return materialesPlanificado;

    }

    public ObservableList<PlanRenglonVarianteView> getPlanificacionNivelEspecifico(int idNivel) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            costoTotalRV = 0.0;
            planificaPlanRenglonVarianteViewObservableList = FXCollections.observableArrayList();
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: id").setParameter("id", idNivel).getResultList();
            for (Renglonnivelespecifico renglonnivelespecifico : renglonnivelespecificoArrayList) {
                planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);
                if (planificacionRenglon.getCantPlanif() == 0) {
                    checkBox = new JFXCheckBox();
                    checkBox.setText(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo());
                    checkBox.setSelected(false);
                    costoTotalRV = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    planificaPlanRenglonVarianteViewObservableList.add(new PlanRenglonVarianteView(renglonnivelespecifico.getRenglonvarianteId(), checkBox, renglonnivelespecifico.getCantidad(), renglonnivelespecifico.getCantidad(), 0.0, 0.0, 0.0, 0.0, renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), costoTotalRV));
                } else {
                    checkBox = new JFXCheckBox();
                    checkBox.setText(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo());
                    checkBox.setSelected(true);
                    costoTotalRV = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    planificaPlanRenglonVarianteViewObservableList.add(planRenglonVarianteView = new PlanRenglonVarianteView(renglonnivelespecifico.getRenglonvarianteId(), checkBox, renglonnivelespecifico.getCantidad(), renglonnivelespecifico.getCantidad() - planificacionRenglon.getCantPlanif(), planificacionRenglon.getCantPlanif(), planificacionRenglon.getCostMatPlan(), Math.round(planificacionRenglon.getCostManoPlan() * 100d) / 100d, planificacionRenglon.getCostEquipPlan(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), costoTotalRV));
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

    public PlanificacionRenglon getPlanificacionNivelEspecificoRenglon(Renglonnivelespecifico renglonnivelespecifico) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> dataList = session.createQuery(" SELECT SUM(plrv.cantidad), SUM(plrv.costomaterial), SUM(plrv.costomano), SUM(plrv.costoequipo), SUM(plrv.salario) FROM CertificacionRenglonVariante plrv INNER JOIN Renglonvariante rv ON plrv.renglonvarianteId = rv.id WHERE plrv.nivelespecificoId =: idNiv AND plrv.renglonvarianteId =: idRev").setParameter("idNiv", renglonnivelespecifico.getNivelespecificoId()).setParameter("idRev", renglonnivelespecifico.getRenglonvarianteId()).getResultList();
            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            costManoCert = 0.0;
            costEquipoCert = 0.0;
            costSalarioCert = 0.0;
            materialesPlanificado = 0.0;

            for (Object[] row : dataList) {
                materialesPlanificado = getCostMaterialPlan(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId());
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

    /**
     * Para los recursos cambiados en las certificaciones
     */
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    private ObservableList<PlanMaterialesRVView> getComponetesRV(Renglonnivelespecifico renglonnivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planMaterialesRVViewObservableList = FXCollections.observableArrayList();
            costoTotalRV = 0.0;
            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);

            recursosArrayList = (ArrayList<Renglonrecursos>) session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId =: idRV").setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId());
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

                } else if (renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("J")) {
                    Juegoproducto juegoproducto = session.get(Juegoproducto.class, renglonrecursos.getRecursosId());
                    costoTotalRV = Math.round(renglonrecursos.getCantidas() * juegoproducto.getPreciomn() * 100d) / 100d;
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(juegoproducto.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), juegoproducto.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, renglonrecursos.getCantidas(), Math.round(renglonrecursos.getCantidas() - cantidadCert * 100d) / 100d, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, renglonrecursos.getCantidas(), 0.0, 0.0, costoTotalRV, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }
                } else if (renglonrecursos.getRecursosByRecursosId().getTipo().contentEquals("S")) {
                    Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, renglonrecursos.getRecursosId());
                    costoTotalRV = Math.round(renglonrecursos.getCantidas() * suministrossemielaborados.getPreciomn() * 100d) / 100d;
                    checkBox1 = new JFXCheckBox();
                    checkBox1.setText(suministrossemielaborados.getCodigo());
                    checkBox1.setSelected(true);
                    if (planificacionRenglon.getCantPlanif() != 0) {
                        datosRecursos = new Double[0];
                        datosRecursos = getCostosCertificadosRec(renglonnivelespecifico.getNivelespecificoId(), renglonnivelespecifico.getRenglonvarianteId(), suministrossemielaborados.getId());
                        cantidadCert = datosRecursos[0];
                        costMaterialCert = datosRecursos[1];

                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, renglonrecursos.getCantidas(), Math.round(renglonrecursos.getCantidas() - cantidadCert * 100d) / 100d, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, renglonrecursos.getCantidas(), 0.0, 0.0, costoTotalRV, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
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

    private Double[] getCostosCertificadosRec(int nivelespecificoId, int renglonvarianteId, int idRec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            datosRecursos = new Double[2];
            List<Object[]> dataList = session.createQuery(" SELECT SUM(plrv.cantidad), SUM(plrv.costo) FROM Certificacionrecrv plrv WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev AND plrv.recursoId =: idRec ").setParameter("idNiv", nivelespecificoId).setParameter("idRev", renglonvarianteId).setParameter("idRec", idRec).getResultList();
            for (Object[] row : dataList) {
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

    private ObservableList<PlanMaterialesRVView> getMaterialesRVBajoEsp(Renglonnivelespecifico renglonnivelespecifico) {

        planMaterialesRVViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE renglonvarianteId =: idRV AND nivelespecificoId =: idNE");
            query.setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId());
            query.setParameter("idNE", renglonnivelespecifico.getNivelespecificoId());


            cantidadCert = 0.0;
            costMaterialCert = 0.0;
            planificacionRenglon = getPlanificacionNivelEspecificoRenglon(renglonnivelespecifico);


            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionrvArrayList.forEach(bajoespecificacionrv -> {
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

                        planMaterialesRVView = new PlanMaterialesRVView(recursos.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - cantidadCert, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, recursos.getDescripcion(), recursos.getPreciomn());
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

                        planMaterialesRVView = new PlanMaterialesRVView(juegoproducto.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - cantidadCert, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, juegoproducto.getDescripcion(), juegoproducto.getPreciomn());
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

                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - cantidadCert, Math.round(cantidadCert * 100d) / 100d, Math.round(costMaterialCert * 100d) / 100d, suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    } else {
                        planMaterialesRVView = new PlanMaterialesRVView(suministrossemielaborados.getId(), checkBox1, bajoespecificacionrv.getCantidad(), 0.0, 0.0, bajoespecificacionrv.getCosto(), suministrossemielaborados.getDescripcion(), suministrossemielaborados.getPreciomn());
                        planMaterialesRVViewObservableList.add(planMaterialesRVView);
                    }

                }
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planMaterialesRVViewObservableList;
    }

    public Certificacionrecrv getCertificaionMaterialInRV(Integer idN, Integer idR, Integer idSu) {
        certificacionrecrvArrayList = new ArrayList<>();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("SELECT SUM(cer.cantidad), SUM(cer.costo), cer.renglonId, cer.nivelespId, cer.recursoId  FROM Certificacionrecrv cer INNER JOIN Recursos rec ON cer.recursoId = rec.id WHERE cer.nivelespId =: idNE AND cer.renglonId =: idRV AND cer.recursoId =: idRe GROUP BY  cer.renglonId, cer.nivelespId, cer.recursoId");
            query.setParameter("idNE", idN);
            query.setParameter("idRV", idR);
            query.setParameter("idRe", idSu);


            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();

                certificacionrecrv = new Certificacionrecrv();
                certificacionrecrv.setId(0);
                if (row[0] == null) {
                    certificacionrecrv.setCantidad(0.0);
                    certificacionrecrv.setCosto(0.0);
                    certificacionrecrv.setFini(Date.valueOf(LocalDate.now()));
                    certificacionrecrv.setFfin(Date.valueOf(LocalDate.now()));
                    certificacionrecrv.setCertificacionId(0);
                    certificacionrecrv.setRenglonId(idR);
                    certificacionrecrv.setNivelespId(idN);
                    certificacionrecrv.setCmateriales(0.0);
                    certificacionrecrv.setCmano(0.0);
                    certificacionrecrv.setCequipo(0.0);
                    certificacionrecrv.setSalario(0.0);
                    certificacionrecrv.setSalariocuc(0.0);
                    certificacionrecrv.setRecursoId(idSu);

                } else if (row[0] != null) {
                    certificacionrecrv.setCantidad(Double.parseDouble(row[0].toString()));

                    certificacionrecrv.setCosto(Double.parseDouble(row[1].toString()));
                    certificacionrecrv.setFini(Date.valueOf(LocalDate.now()));
                    certificacionrecrv.setFfin(Date.valueOf(LocalDate.now()));
                    certificacionrecrv.setCertificacionId(0);
                    certificacionrecrv.setRenglonId(idR);

                    certificacionrecrv.setNivelespId(idN);
                    certificacionrecrv.setCmateriales(0.0);
                    certificacionrecrv.setCmano(0.0);
                    certificacionrecrv.setCequipo(0.0);
                    certificacionrecrv.setSalario(0.0);
                    certificacionrecrv.setSalariocuc(0.0);
                    certificacionrecrv.setRecursoId(idSu);
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

        return certificacionrecrv;
    }

    public ArrayList<Certificacionrecrv> getListadoMaterialesCertificados(Renglonnivelespecifico renglonnivelespecifico) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            certificacionrecrvArrayList = new ArrayList<>();
            List<Object[]> listData = session.createQuery("SELECT SUM(cer.cantidad), SUM(cer.costo), cer.renglonId, cer.nivelespId, cer.recursoId, cer.tipo FROM Certificacionrecrv cer WHERE cer.nivelespId =: idNE AND cer.renglonId =: idRV GROUP BY  cer.renglonId, cer.nivelespId, cer.recursoId, cer.tipo").setParameter("idNE", renglonnivelespecifico.getNivelespecificoId()).setParameter("idRV", renglonnivelespecifico.getRenglonvarianteId()).getResultList();
            for (Object[] row : listData) {
                Certificacionrecrv certificacionrecrv = new Certificacionrecrv();
                certificacionrecrv.setId(0);
                certificacionrecrv.setCantidad(Double.parseDouble(row[0].toString()));
                certificacionrecrv.setCosto(Double.parseDouble(row[1].toString()));
                certificacionrecrv.setFini(Date.valueOf(LocalDate.now()));
                certificacionrecrv.setFfin(Date.valueOf(LocalDate.now()));
                certificacionrecrv.setCertificacionId(0);
                certificacionrecrv.setRenglonId(Integer.parseInt(row[2].toString()));
                certificacionrecrv.setNivelespId(Integer.parseInt(row[3].toString()));
                certificacionrecrv.setCmateriales(0.0);
                certificacionrecrv.setCmano(0.0);
                certificacionrecrv.setCequipo(0.0);
                certificacionrecrv.setSalario(0.0);
                certificacionrecrv.setSalariocuc(0.0);
                certificacionrecrv.setRecursoId(Integer.parseInt(row[4].toString()));
                certificacionrecrv.setTipo(row[5].toString());

                certificacionrecrvArrayList.add(certificacionrecrv);

            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacionrecrvArrayList;


    }

    private Double getCantidadPresupuestada(int nivelespecificoId, int renglonvarianteId, int idRec) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cantPresup = 0.0;
            Query querydatos = session.createQuery("SELECT cantidad FROM Bajoespecificacionrv  WHERE nivelespecificoId =: idNiv AND renglonvarianteId =: idRev AND idsuministro =: idRec").setParameter("idNiv", nivelespecificoId).setParameter("idRev", renglonvarianteId).setParameter("idRec", idRec);
            if (!querydatos.getResultList().isEmpty()) {
                cantPresup = (Double) querydatos.getResultList().get(0);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantPresup;
    }

    public ObservableList<PlanMaterialesRVView> getPlanMaterialesRVViewObservableListo(int idNivel, int idRVa) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            planMaterialesRVViewObservableList = FXCollections.observableArrayList();
            List<Bajoespecificacionrv> dataList = (ArrayList<Bajoespecificacionrv>) session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: id AND renglonvarianteId =: idR").setParameter("id", idNivel).setParameter("idR", idRVa).getResultList();
            planMaterialesRVViewObservableList = getMaterialesCambiadosCertificaciones(dataList);
            tx.commit();
            session.close();
            return planMaterialesRVViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    private ObservableList<PlanMaterialesRVView> getMaterialesCambiadosCertificaciones(List<Bajoespecificacionrv> bajoespecificacionrvList) {
        planMaterialesRVViewObservableList = FXCollections.observableArrayList();
        for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionrvList) {
            if (bajoespecificacionrv.getTipo().trim().equals("1")) {
                Recursos rec = utilCalcSingelton.getSuministro(bajoespecificacionrv.getIdsuministro());
                checkBox1 = new JFXCheckBox();
                checkBox1.setText(rec.getCodigo());
                if (planRenglonVarianteView.getCantCertificada() != 0.0) {
                    checkBox1.setSelected(true);
                }
                double calcCantCertf = clacSumCertCant(bajoespecificacionrv, planRenglonVarianteView);
                planMaterialesRVViewObservableList.add(new PlanMaterialesRVView(rec.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - calcCantCertf, calcCantCertf, calcCantCertf * rec.getPreciomn(), rec.getDescripcion(), rec.getPreciomn()));
            } else if (bajoespecificacionrv.getTipo().trim().equals("J")) {
                Juegoproducto rec = utilCalcSingelton.getJuegoProductoById(bajoespecificacionrv.getIdsuministro());
                checkBox1 = new JFXCheckBox();
                checkBox1.setText(rec.getCodigo());
                if (planMaterialesRVView.getCantCertificada() != 0.0) {
                    checkBox1.setSelected(true);
                }
                double calcCantCertf = clacSumCertCant(bajoespecificacionrv, planRenglonVarianteView);
                planMaterialesRVViewObservableList.add(new PlanMaterialesRVView(rec.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - calcCantCertf, calcCantCertf, calcCantCertf * rec.getPreciomn(), rec.getDescripcion(), rec.getPreciomn()));
            } else if (bajoespecificacionrv.getTipo().trim().equals("S")) {
                Suministrossemielaborados rec = utilCalcSingelton.getSuministroSemielaboradoById(bajoespecificacionrv.getIdsuministro());
                checkBox1 = new JFXCheckBox();
                checkBox1.setText(rec.getCodigo());
                if (planMaterialesRVView.getCantCertificada() != 0.0) {
                    checkBox1.setSelected(true);
                }
                double calcCantCertf = clacSumCertCant(bajoespecificacionrv, planRenglonVarianteView);
                planMaterialesRVViewObservableList.add(new PlanMaterialesRVView(rec.getId(), checkBox1, bajoespecificacionrv.getCantidad(), bajoespecificacionrv.getCantidad() - calcCantCertf, calcCantCertf, calcCantCertf * rec.getPreciomn(), rec.getDescripcion(), rec.getPreciomn()));
            }
        }
        return planMaterialesRVViewObservableList;
    }

    private double clacSumCertCant(Bajoespecificacionrv bajoespecificacionrv, PlanRenglonVarianteView planMaterialesRVView) {
        double mult = bajoespecificacionrv.getCantidad() * planMaterialesRVView.getCantCertificada();
        return new BigDecimal(String.format("%.4f", mult / planMaterialesRVView.getCantRV())).doubleValue();
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

    public Integer addPlanificacion(Planificacion planif) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer certId = null;
        try {
            tx = session.beginTransaction();
            certId = (Integer) session.save(planif);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certId;
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

    public void deleteCertificacion(int idCert) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Integer op = session.createQuery(" DELETE FROM Certificacionrecrv where renglonId =:idP").setParameter("idP", idCert).executeUpdate();
            Integer op2 = session.createQuery(" DELETE FROM CertificacionRenglonVariante where renglonvarianteId =:idP").setParameter("idP", idCert).executeUpdate();

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

    public void loadDatosCertificaciones(Integer idN) {
        codeRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, JFXCheckBox>("codigoRV"));
        volumenRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("cantRV"));
        disponibleRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("disponible"));
        cantidadcertif.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("cantCertificada"));
        costMatRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoMaterial"));
        costManoRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoMano"));
        costEquipoRenglon.setCellValueFactory(new PropertyValueFactory<PlanRenglonVarianteView, DoubleProperty>("costoEquipo"));

        planificaPlanRenglonVarianteViewObservableList = FXCollections.observableArrayList();
        planificaPlanRenglonVarianteViewObservableList = getPlanificacionNivelEspecifico(idN);
        tablePlanificacionRenglon.getItems().setAll(planificaPlanRenglonVarianteViewObservableList);

    }

    public void loadMaterialesCertificaciones(Integer idNivel, Integer IdReg) {

        codigoNateriales.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, JFXCheckBox>("codigoRV"));
        volumenMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("cantRV"));
        disponibleMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("disponible"));
        cantCertMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("cantCertificada"));
        costMaterial.setCellValueFactory(new PropertyValueFactory<PlanMaterialesRVView, DoubleProperty>("costoMaterial"));

        planMaterialesRVViewObservableList = FXCollections.observableArrayList();
        planMaterialesRVViewObservableList = getPlanMaterialesRVViewObservableListo(idNivel, IdReg);
        tablePlanificacionMateriales.getItems().setAll(planMaterialesRVViewObservableList);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        empresa = getEmpresa();

        certificarRenglonVarianteController = this;

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

    public void handleCertificar(ActionEvent event) {
        planRenglonVarianteView = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();
        if (combo_Brigada.getValue() == null || combogrupo.getValue() == null || dateini.getValue() == null || datehasta.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Complete los valores de formulario antes de Certificar!");
            alert.showAndWait();

        } else {

            if (checkActive.isSelected() == true) {
                String[] codeCuadriPart = combocuadrilla.getValue().split(" - ");
                String code = codeCuadriPart[0];

                cuadrillaconstruccionsArrayList.forEach(cuadrillaconstruccion -> {
                    if (cuadrillaconstruccion.getCodigo().contentEquals(code)) {
                        cuadrillaId = cuadrillaconstruccion.getId();
                    }
                });
            } else {
                String[] codeCuadriPart = combogrupo.getValue().split(" - ");
                Grupoconstruccion grupoconstruccion = grupoconstruccionArrayList.parallelStream().filter(item -> item.getCodigo().trim().equals(codeCuadriPart[0])).findFirst().get();
                cuadrillaId = grupoconstruccion.getCuadrillaconstruccionsById().stream().findFirst().get().getId();
            }
            LocalDate dateDes = dateini.getValue();
            LocalDate dateHast = datehasta.getValue();
            desdeDate = Date.valueOf(dateDes);
            hastaData = Date.valueOf(dateHast);

            try {
                String[] codeCuadriPart = combogrupo.getValue().split(" - ");
                grupoId = grupoconstruccionArrayList.parallelStream().filter(item -> item.getCodigo().trim().equals(codeCuadriPart[0])).findFirst().map(Grupoconstruccion::getId).get();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("CertificaRV.fxml"));
                Parent proot = loader.load();
                AddCertificaRenglonVarianteController controller = loader.getController();
                controller.cargarDatos(certificarRenglonVarianteController, nivelespecificoModel.getId(), planRenglonVarianteView.getId(), brigadaId, grupoId, cuadrillaId, desdeDate, hastaData, planRenglonVarianteView.getDisponible());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

                stage.setOnCloseRequest(event1 -> {
                    loadDatosCertificaciones(nivelEspId);

                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void cargarUnidadeObra(int idNEsp, int idEm, int idZon, int idOb, int idNi, int idEs, int idSu, Integer idobraI) {

        nivelEspId = idNEsp;
        brigadasList = listOfBrigadas(idEm);
        combo_Brigada.setItems(brigadasList);

        idEmp = idEm;
        idZona = idZon;
        idObj = idOb;
        idNiv = idNi;
        idEsp = idEs;
        idSub = idSu;
        idObra = idobraI;


        loadDatosCertificaciones(nivelEspId);

        nivelespecificoModel = getNivel(idNEsp);
        labelTitle.setText("Certificaciones en el nivel especifico " + nivelespecificoModel.getCodigo());

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

    public void handleDetallecertRec(ActionEvent event) {
        planMaterialesRVView = tablePlanificacionMateriales.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetalleCertificarRenglonVariantesRecurso.fxml"));
            Parent proot = loader.load();
            DetalleCertificarRenglonVarianteRecursoController controller = loader.getController();
            controller.cargarDatos(nivelEspId, planRenglonVarianteView.getId(), planMaterialesRVView.getId());


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

    public void handleCambiarSuministro(ActionEvent event) {

        var cert = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();
        planMaterialesRVView = tablePlanificacionMateriales.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CambioSuministroCertificacion.fxml"));
            Parent proot = loader.load();
            CambioSuministroCertificacionController controller = loader.getController();
            controller.cargarDatostoChange(certificarRenglonVarianteController, nivelespecificoModel, cert.getId(), planMaterialesRVView.getId(), planMaterialesRVView.getDisponible(), desdeDate, hastaData, idObra);
            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public ObservableList<DetalleCertificaReportView> getDetalleCertificacionRecursos(Integer niv, Integer reng) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> recursosList = session.createQuery(" SELECT plrv.recursoId, plrv.cantidad, plrv.costo, plrv.fini, plrv.ffin, plrv.tipo FROM Certificacionrecrv plrv  WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev and plrv.tipo != '2' OR plrv.tipo != '3'").setParameter("idNiv", niv).setParameter("idNiv", niv).setParameter("idRev", reng).getResultList();
            for (Object[] row : recursosList) {
                if (row[5].toString().startsWith("1")) {
                    Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[1].toString()), recursos.getPreciomn(), Double.parseDouble(row[2].toString()), row[3].toString(), row[4].toString()));
                } else if (row[5].toString().startsWith("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[1].toString()), recursos.getPreciomn(), Double.parseDouble(row[2].toString()), row[3].toString(), row[4].toString()));
                } else if (row[5].toString().startsWith("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                    label = new StringBuilder().append(recursos.getCodigo()).append(" ").append(recursos.getDescripcion());
                    detalleCertificaViewObservableList.add(new DetalleCertificaReportView(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), label.toString(), Double.parseDouble(row[1].toString()), recursos.getPreciomn(), Double.parseDouble(row[2].toString()), row[3].toString(), row[4].toString()));
                }
            }


            tx.commit();
            session.close();
            return detalleCertificaViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public Obra getObra(int idOb) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obra = session.get(Obra.class, idOb);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obra;
    }

    public Empresa getEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresa = session.get(Empresa.class, 1);


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresa;
    }

    public void handleCreateReportListado(ActionEvent event) {

        var cert = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();

        detalleCertificaViewObservableList = FXCollections.observableArrayList();
        detalleCertificaViewObservableList = getDetalleCertificacionRecursos(nivelEspId, cert.getId());


        date = LocalDate.now();
        parametros = new HashMap<>();
        obra = getObra(idObra);
        parametros.put("obraName", obra.getCodigo() + " " + obra.getDescripion());
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("empresa", empresa.getNombre());

        try {
            if (checkExp.isSelected() == false) {
                DynamicReport dr = createReporteListadodeCertificaciones();
                JRDataSource ds = new JRBeanCollectionDataSource(detalleCertificaViewObservableList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                JasperViewer.viewReport(jp, false);
            } else {
                DynamicReport dr = createReporteListadodeCertificaciones();
                JRDataSource ds = new JRBeanCollectionDataSource(detalleCertificaViewObservableList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                exportarExcel(jp);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }


    public DynamicReport createReporteListadodeCertificaciones() throws ClassNotFoundException {


        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(Color.white);
        headerStyle.setTextColor(Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(10, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);

        Style groupVariables = new Style("groupVariables");
        groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        groupVariables.setTextColor(Color.BLUE);
        groupVariables.setBorderBottom(Border.THIN());
        groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
        groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                // .setPageSizeAndOrientation(Page.Page_A4_Portrait())
                .setTemplateFile("templete/reportCertificacionGeneral.jrxml");


        AbstractColumn columngruopEmpresa = ColumnBuilder.getNew()
                .setColumnProperty("label", String.class.getName())
                .setTitle(" ").setWidth(20)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();


        AbstractColumn columnaFi = ColumnBuilder.getNew()
                .setColumnProperty("fini", String.class.getName())
                .setTitle("F. Inicio").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaFf = ColumnBuilder.getNew()
                .setColumnProperty("ffin", String.class.getName())
                .setTitle("F. Fin").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("um", String.class.getName())
                .setTitle("UM").setWidth(7)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("cantidad", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
        AbstractColumn columnaprecio = ColumnBuilder.getNew()
                .setColumnProperty("precio", Double.class.getName())
                .setTitle("Precio").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnacost = ColumnBuilder.getNew()
                .setColumnProperty("costo", Double.class.getName())
                .setTitle("Costo").setWidth(14).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        GroupBuilder gb1 = new GroupBuilder();


        Style glabelStyle = new StyleBuilder(false).setFont(Font.ARIAL_SMALL)
                .setHorizontalAlign(HorizontalAlign.JUSTIFY).setBorderTop(Border.THIN())
                .setStretchWithOverflow(false)
                .build();

        DJGroupLabel glabel1 = new DJGroupLabel("Cantidad", glabelStyle);
        DJGroupLabel glabel2 = new DJGroupLabel("Costo", glabelStyle);

        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columngruopEmpresa)
                .addFooterVariable(columnaCantidad, DJCalculation.SUM, headerVariables, null, glabel1)
                .addFooterVariable(columnacost, DJCalculation.SUM, headerVariables, null, glabel2)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS_AND_COLUMN_NAME)

                .build();


        drb.addGroup(g1);

        drb.addColumn(columngruopEmpresa);
        drb.addColumn(columnaFi);
        drb.addColumn(columnaFf);
        drb.addColumn(columnaum);
        drb.addColumn(columnaCantidad);
        drb.addColumn(columnaprecio);
        drb.addColumn(columnacost);


        drb.setUseFullPageWidth(true);


        DynamicReport dr = drb.build();


        return dr;

    }


    public void handleShowMotivoCambio(ActionEvent event) {

        planMaterialesRVView = tablePlanificacionMateriales.getSelectionModel().getSelectedItem();

        if (planMaterialesRVView.getCodigoRV().isSelected() == false) {
            detalle = null;
            //detalle = getDetalleCambio(nivelEspId, planRenglonVarianteView.getId(), planMaterialesRVView.getId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Motivo del Cambio");
            alert.setContentText(detalle);
            alert.showAndWait();
        }
    }

    public void exportarExcel(JasperPrint jp) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {

            try {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jp));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file.getAbsolutePath()));
                exporter.exportReport();

            } catch (JRException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void handleDescertificar(ActionEvent event) {
        var certific = tablePlanificacionRenglon.getSelectionModel().getSelectedItem();

        deleteCertificacion(certific.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Certificación eliminada");
        alert.setContentText(detalle);
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



