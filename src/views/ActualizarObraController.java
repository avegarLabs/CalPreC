package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.jfoenix.controls.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.Tuple;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.toList;

public class ActualizarObraController implements Initializable {
    public ActualizarObraController controller;
    public Obra obra;
    private Task tarea;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_entidad;
    @FXML
    private JFXComboBox<String> combo_inversionista;
    @FXML
    private JFXComboBox<String> combo_tipoObra;
    @FXML
    private JFXComboBox<String> combo_Salario;
    @FXML
    private TableView<NewObrasFormTable> tableEmpresa;
    @FXML
    private TableColumn<NewObrasFormTable, JFXCheckBox> empresaCode;
    @FXML
    private TableColumn<NewObrasFormTable, StringProperty> empresaDescrip;
    @FXML
    private Label labelObraName;
    @FXML
    private AnchorPane estructuraPane;
    @FXML
    private Pane paneRenglones;
    @FXML
    private JFXComboBox<String> renglonesEmpresas;
    @FXML
    private TableView<ObraRenglonesEmpresaView> tableRenglones;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, StringProperty> rengCode;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, StringProperty> rengDescrip;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, StringProperty> rengUm;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, DoubleProperty> renCostMat;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, DoubleProperty> renCostMano;
    @FXML
    private TableColumn<ObraRenglonesEmpresaView, DoubleProperty> renCostEquip;
    @FXML
    private Pane paneSemiel;
    @FXML
    private TableView<SuministrosSemielaboradosView> tableSemielab;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> semiCode;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> semiDescrip;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> semiUm;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, DoubleProperty> semiMn;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, DoubleProperty> semiMlc;
    @FXML
    private Pane paneJuegos;
    @FXML
    private TableView<JuegoProductoView> tableJuego;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> juegoCode;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> juegoDescrip;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> juegoUm;
    @FXML
    private TableColumn<JuegoProductoView, DoubleProperty> juegoMn;
    @FXML
    private TableColumn<JuegoProductoView, DoubleProperty> juegoMlc;
    @FXML
    private Pane paneEquipos;
    @FXML
    private JFXComboBox<String> equiposEmpresas;
    @FXML
    private TableView<EquiposView> tableEquipos;
    @FXML
    private TableColumn<EquiposView, StringProperty> equipCode;
    @FXML
    private TableColumn<EquiposView, StringProperty> equipDescrip;
    @FXML
    private TableColumn<EquiposView, StringProperty> equipUm;
    @FXML
    private TableColumn<EquiposView, DoubleProperty> equipMn;
    @FXML
    private TableColumn<EquiposView, DoubleProperty> equipSalario;
    @FXML
    private Pane paneMano;
    @FXML
    private JFXComboBox<String> empresaMano;
    @FXML
    private TableView<ManoObraView> tableSalario;
    @FXML
    private TableColumn<ManoObraView, StringProperty> manoCode;
    @FXML
    private TableColumn<ManoObraView, StringProperty> manoDescrip;
    @FXML
    private TableColumn<ManoObraView, StringProperty> manoUm;
    @FXML
    private TableColumn<ManoObraView, DoubleProperty> manoPrecio;
    @FXML
    private TableColumn<ManoObraView, DoubleProperty> manoSalario;
    @FXML
    private Pane paneSuministros;
    @FXML
    private TableView<SuministrosView> tableSum;
    @FXML
    private TableColumn<SuministrosView, String> sumCode;
    @FXML
    private TableColumn<SuministrosView, StringProperty> sumDescrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> sumUm;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> sumMn;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> sumMlc;
    @FXML
    private TableView<SuministrosView> tableSumRV;
    @FXML
    private TableColumn<SuministrosView, StringProperty> sumRvCode;
    @FXML
    private TableColumn<SuministrosView, StringProperty> sumRVDescrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> sumRvUm;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> sumRvMn;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> sumRvMlc;
    @FXML
    private JFXCheckBox checkSum;
    @FXML
    private JFXCheckBox checkMano;
    @FXML
    private JFXCheckBox checkEquipos;
    @FXML
    private JFXCheckBox checkJuego;
    @FXML
    private JFXCheckBox checkSemi;
    @FXML
    private JFXCheckBox checkReng;
    @FXML
    private JFXTextField filter;
    private ObservableList<String> listInversionista;
    private ObservableList<String> listTipoObra;
    private ObservableList<String> listSalario;
    private ArrayList<Entidad> entidadArrayList;
    private ArrayList<Inversionista> inversionistaArrayList;
    private ArrayList<Tipoobra> tipoobrasArrayList;
    private ArrayList<Salario> salarioArrayList;
    private ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    private ObservableList<NewObrasFormTable> obrasFormTableObservableList;
    private NewObrasFormTable newObrasFormTable;
    private ObservableList<JuegoProductoView> juegoProductoViewObservableList;
    private JuegoProductoView juegoProductoView;
    private ObservableList<SuministrosSemielaboradosView> suministrosSemielaboradosViewObservableList;
    private ObservableList<ObraRenglonesEmpresaView> renglonVarianteViewObservableList;
    private JFXCheckBox checkBox;
    private ObservableList<NewObrasFormTable> datos;
    private ObservableList<SuministrosView> datosSuministros;
    private ObservableList<SuministrosView> datosSumistrosRV;
    private ObservableList<ManoObraView> datosMano;
    private ObservableList<EquiposView> datosEquipos;
    private ObservableList<JuegoProductoView> datosJuego;
    private ObservableList<SuministrosSemielaboradosView> datosSemielaborados;
    private ObservableList<ObraRenglonesEmpresaView> datosRenglonVariante;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObservableList<ManoObraView> manoObraViewObservableList;
    private ObservableList<EquiposView> equiposViewObservableList;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private Subespecialidades subespecialidades;
    private Map parametros;
    private LocalDate date;
    @FXML
    private JFXTreeView treeView;
    private TreeItem treeItem;
    private Empresa empresa;
    private java.sql.Date fecha;
    private Integer version;
    private Empresaobrasalario empresaobrasalario;
    private Salario salario;
    @FXML
    private TableView<UOSubespecialidadesValoresView> TableCostoUnidades;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, StringProperty> codeUO;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, StringProperty> uoUm;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, DoubleProperty> uoCantidad;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, DoubleProperty> uoCostMat;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, DoubleProperty> uoCostMano;
    @FXML
    private TableColumn<UOSubespecialidadesValoresView, DoubleProperty> uoCostEquip;
    // private Entidad entidad;
    private Empresaobra newEmpresaobra;
    private boolean active;
    private ArrayList<Recursos> recursosArrayList;
    private ObservableList<SuministrosView> suministrosViewObservableList;
    private SuministrosView suministros;
    private double importe;
    private ObservableList<GastosIndView> gastosIndViewObservableList;
    private GastosIndView gastosIndView;
    private ArrayList<Empresagastos> empresagastosArrayList;
    private ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList;
    private ObservableList<UOSubespecialidadesValoresView> uoSubespecialidadesValoresViewObservableList;
    private UOSubespecialidadesValoresView subespecialidadesValoresView;
    @FXML
    private TableView<GastosIndView> gatosTable;
    @FXML
    private TableColumn<GastosIndView, StringProperty> concepto;
    @FXML
    private TableColumn<GastosIndView, DoubleProperty> coeficiente;
    @FXML
    private TableColumn<GastosIndView, DoubleProperty> valor;
    private ObservableList<GastosIndView> datosConceptos;
    private Empresaobraconceptoglobal empresaobraconceptoglobal;
    private PresupControlModel pePresupControlModel;
    private ArrayList<PresupControlModel> empresaobraconceptoglobalArrayList;
    private Empresaobra nueEmpresaobra;
    @FXML
    private TableView<ControlPresupResumenView> tableControl;
    @FXML
    private TableColumn<ControlPresupResumenView, StringProperty> presup;
    @FXML
    private TableColumn<ControlPresupResumenView, DoubleProperty> valorvers;
    @FXML
    private TableColumn<ControlPresupResumenView, StringProperty> fechavers;
    private double montoPresup;
    @FXML
    private Label labelMonto;
    private ArrayList<Empresaobraconceptoglobal> versionsDatos;
    private ControlPresupResumenView controlVersionResults;
    private ObservableList<ControlPresupResumenView> controlPresupResumenViews;
    private ArrayList<ControlVersionResults> globalReportModelArrayList;
    private ControlVersionResults globalReportModel;
    private String conceptName;
    private ActualizarObraController actualizarObraController;
    private StringBuilder queryBuilder;

    @FXML
    private TableView<ResumenCostosList> tableResumen;

    @FXML
    private TableColumn<ResumenCostosList, String> indice;

    @FXML
    private TableColumn<ResumenCostosList, String> presup1;

    @FXML
    private TableColumn<ResumenCostosList, String> valorvers1;

    @FXML
    private JFXComboBox<String> empresacombo;

    @FXML
    private JFXDatePicker ini;

    @FXML
    private JFXDatePicker finD;

    private Date dateDesde;
    private Date dateHasta;

    public ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private List<Empresagastos> empresagastosList;
    private List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoeficientesList;

    /**
     * Datos de los Renglones variantes
     */
    private Double total;
    private Salario salarioVal;
    private int count;
    private int batchSize = 10;
    private ArrayList<Empresaobraconceptoglobal> temp;

    public String getConceptName(int idConcept) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Conceptosgasto conceptosgasto = session.get(Conceptosgasto.class, idConcept);

            tx.commit();
            session.close();
            return conceptosgasto.getDescripcion();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return conceptName;
    }

    public ArrayList<ControlVersionResults> getGlobalReportModelArrayList(int idObra, Integer version) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            globalReportModelArrayList = new ArrayList<ControlVersionResults>();
            if (version == 1) {
                String subquery = "SELECT concepto_id, Base FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1') AS empresaobraconceptoglobal(concepto_id integer, Base double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                System.out.println(queryBuilder.toString());

                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    conceptName = getConceptName(Integer.parseInt(row[0].toString()));
                    globalReportModelArrayList.add(new ControlVersionResults(conceptName, Double.parseDouble(row[1].toString()), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                }

            } else if (version == 2) {
                String subquery = "SELECT concepto_id, Base, V2 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    conceptName = getConceptName(Integer.parseInt(row[0].toString()));
                    globalReportModel = new ControlVersionResults(conceptName, Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0., 0.0);
                    globalReportModelArrayList.add(globalReportModel);
                }
            } else if (version == 3) {
                String subquery = "SELECT concepto_id, Base, V2, V3 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);
                }
            } else if (version == 4) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);


                }

            } else if (version == 5) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4, V5 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision, V5 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), 0.0, 0.0, 0.0, 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);

                }
            } else if (version == 6) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4, V5, V6 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision, V5 double precision, V6 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), 0.0, 0.0, 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);

                }
            } else if (version == 7) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4, V5, V6, V7 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision, V5 double precision, V6 double precision, V7 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString()), 0.0, 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);

                }
            } else if (version == 8) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4, V5, V6, V7, V8 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision, V5 double precision, V6 double precision, V7 double precision, V8 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString()), Double.parseDouble(row[8].toString()), 0.0, 0.0);
                    globalReportModelArrayList.add(globalReportModel);

                }

            } else if (version == 9) {
                String subquery = "SELECT concepto_id, Base, V2, V3, V4, V5, V6, V7, V8, V9, V10 FROM CROSSTAB('select concepto_id, descripcion, valor from empresaobraconceptoglobal ";
                String endsubString = "Order By 1, 2, 3') AS empresaobraconceptoglobal(concepto_id integer, Base double precision , V2 double precision, V3 double precision, V4 double precision, V5 double precision, V6 double precision, V7 double precision, V8 double precision, V9 double precision, V10 double precision)";
                queryBuilder = new StringBuilder().append(subquery).append("where obra_id = ").append(idObra).append(" ").append(endsubString);
                List<Object[]> empData = session.createSQLQuery(queryBuilder.toString()).getResultList();
                for (Object[] row : empData) {
                    globalReportModel = new ControlVersionResults(getConceptName(Integer.parseInt(row[0].toString())), Double.parseDouble(row[1].toString()), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString()), Double.parseDouble(row[8].toString()), Double.parseDouble(row[9].toString()), Double.parseDouble(row[10].toString()));
                    globalReportModelArrayList.add(globalReportModel);
                }
            }

            tx.commit();
            session.close();
            return globalReportModelArrayList;
        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    public ObservableList<ControlPresupResumenView> getControlPresupResumenViews(int idobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            controlPresupResumenViews = FXCollections.observableArrayList();
            versionsDatos = (ArrayList<Empresaobraconceptoglobal>) session.createQuery("FROM Empresaobraconceptoglobal  WHERE obraId =: idObra ").setParameter("idObra", idobra).getResultList();
            for (Empresaobraconceptoglobal global : versionsDatos) {
                if (obra.getSalarioId() == 1 || obra.getSalarioId() == 2) {
                    if (global.getConceptoId() == 20) {
                        controlPresupResumenViews.add(new ControlPresupResumenView(global.getDescripcion(), global.getValor(), global.getFecha().toString()));
                    }
                } else if (obra.getSalarioId() == 3) {
                    if (global.getConceptoId() == getLastConcept()) {
                        controlPresupResumenViews.add(new ControlPresupResumenView(global.getDescripcion(), global.getValor(), global.getFecha().toString()));
                    }
                }
            }
            tx.commit();
            session.close();
            return controlPresupResumenViews;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void datosResumen(int idOBra) {

        presup.setCellValueFactory(new PropertyValueFactory<ControlPresupResumenView, StringProperty>("presup"));
        valorvers.setCellValueFactory(new PropertyValueFactory<ControlPresupResumenView, DoubleProperty>("valor"));
        fechavers.setCellValueFactory(new PropertyValueFactory<ControlPresupResumenView, StringProperty>("fecha"));

        controlPresupResumenViews = FXCollections.observableArrayList();
        controlPresupResumenViews = getControlPresupResumenViews(idOBra);

        tableControl.getItems().setAll(controlPresupResumenViews);

    }

    public Empresa getEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresa = session.get(Empresa.class, 1);

            tx.commit();
            session.close();
            return empresa;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresa;
    }

    /**
     * Para llenar elemntos del formulario de la parte general
     */
    public ObservableList<String> getEntidadesList() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ObservableList<String> listEntidad = FXCollections.observableArrayList();
            entidadArrayList = (ArrayList<Entidad>) session.createQuery("FROM Entidad ").getResultList();
            for (Entidad entidad : entidadArrayList) {
                listEntidad.add(entidad.getCodigo() + " - " + entidad.getDescripcion());
            }
            tx.commit();
            session.close();
            return listEntidad;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListInversionista() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            listInversionista = FXCollections.observableArrayList();
            inversionistaArrayList = (ArrayList<Inversionista>) session.createQuery("FROM Inversionista ").getResultList();
            for (Inversionista inversionista : inversionistaArrayList) {
                listInversionista.add(inversionista.getCodigo() + " - " + inversionista.getDescripcion());
            }

            tx.commit();
            session.close();
            return listInversionista;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListTipoObra() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listTipoObra = FXCollections.observableArrayList();
            tipoobrasArrayList = (ArrayList<Tipoobra>) session.createQuery("FROM Tipoobra ").list();
            for (Tipoobra tipoObra : tipoobrasArrayList) {
                listTipoObra.add(tipoObra.getCodigo() + " - " + tipoObra.getDescripcion());
            }

            tx.commit();
            session.close();
            return listTipoObra;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListSalario() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listSalario = FXCollections.observableArrayList();
            salarioArrayList = (ArrayList<Salario>) session.createQuery("FROM Salario ").list();
            for (Salario salario : salarioArrayList) {
                listSalario.add(salario.getDescripcion());
            }

            tx.commit();
            session.close();
            return listSalario;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<NewObrasFormTable> getListEmpresas() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obrasFormTableObservableList = FXCollections.observableArrayList();
            empresaconstructoraArrayList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").getResultList();
            for (Empresaconstructora empresas : empresaconstructoraArrayList) {
                checkBox = new JFXCheckBox();
                checkBox.setText(empresas.getCodigo());
                newObrasFormTable = new NewObrasFormTable(empresas.getId(), checkBox, empresas.getDescripcion());
                obrasFormTableObservableList.add(newObrasFormTable);
            }

            tx.commit();
            session.close();
            return obrasFormTableObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ArrayList<Empresaconstructora> getListOfEmpresasInObra(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructoraArrayList = new ArrayList<Empresaconstructora>();
            ArrayList<Empresaobra> empresaobraArrayList = (ArrayList<Empresaobra>) session.createQuery("FROM Empresaobra WHERE obraId =: idOb ").setParameter("idOb", idObra).getResultList();
            for (Empresaobra empresaobra : empresaobraArrayList) {
                empresaconstructoraArrayList.add(session.get(Empresaconstructora.class, empresaobra.getEmpresaconstructoraId()));
            }

            tx.commit();
            session.close();
            return empresaconstructoraArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public Obra getObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            obra = session.get(Obra.class, id);

            trx.commit();
            session.close();
            return obra;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obra;
    }

    /**
     * Suministros en la obra
     */

    private List<SuministrosView> listRecBajoEsp(int idO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            List<SuministrosView> recursosBajoList = new ArrayList<>();
            List<Tuple> datosList = session.createQuery("SELECT rec.id, rec.codigo, rec.descripcion, rec.um, rec.peso, rec.preciomn, rec.preciomlc, rec.tipo, rec.pertenece FROM Bajoespecificacion bajo INNER JOIN Unidadobra uo ON bajo.unidadobraId = uo.id INNER JOIN Recursos rec ON bajo.idSuministro = rec.id WHERE uo.obraId =: id  GROUP BY rec.id, rec.codigo, rec.descripcion, rec.um, rec.peso, rec.preciomn, rec.preciomlc, rec.tipo", Tuple.class).setParameter("id", idO).getResultList();
            for (Tuple tuple : datosList) {
                double val = 0.0;
                String code = tuple.get(1).toString().trim();
                if (tuple.get(6) == null) {
                    val = 0.0;
                } else {
                    val = Double.parseDouble(tuple.get(6).toString());
                }
                if (tuple.get(8).toString().trim().equals("R266")) {
                    recursosBajoList.add(new SuministrosView(Integer.parseInt(tuple.get(0).toString()), code.concat("  "), tuple.get(2).toString(), tuple.get(3).toString(), Double.parseDouble(tuple.get(4).toString()), Double.parseDouble(tuple.get(5).toString()), val, "I", tuple.get(7).toString(), tuple.get(8).toString(), 0.0, utilCalcSingelton.createCheckBox(1)));
                } else {
                    recursosBajoList.add(new SuministrosView(Integer.parseInt(tuple.get(0).toString()), code.trim(), tuple.get(2).toString(), tuple.get(3).toString(), Double.parseDouble(tuple.get(4).toString()), Double.parseDouble(tuple.get(5).toString()), val, "I", tuple.get(7).toString(), tuple.get(8).toString(), 0.0, utilCalcSingelton.createCheckBox(1)));
                }
            }

            trx.commit();
            session.close();
            return recursosBajoList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ObservableList<SuministrosView> getSuminsitros(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            suministrosViewObservableList = FXCollections.observableArrayList();
            List<SuministrosView> datosList = new ArrayList<>();
            recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece =:idObra ").setParameter("idObra", String.valueOf(id)).getResultList();
            for (Recursos recursos : recursosArrayList) {
                datosList.add(new SuministrosView(recursos.getId(), recursos.getCodigo().trim(), recursos.getDescripcion(), recursos.getUm(), recursos.getPeso(), recursos.getPreciomn(), 0.0, recursos.getGrupoescala(), recursos.getTipo(), recursos.getPertenece(), 0.0, utilCalcSingelton.createCheckBox(recursos.getActive())));
            }
            datosList.addAll(listRecBajoEsp(id));
            datosList.stream().collect(Collectors.toSet()).stream().collect(toList());
            suministrosViewObservableList.addAll(datosList);

            trx.commit();
            session.close();
            return suministrosViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    private double getImporteEscala(String grupo) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salario = session.get(Salario.class, obra.getSalarioId());
            if (grupo.contentEquals("II")) {
                importe = salario.getIi();
            }
            if (grupo.contentEquals("III")) {
                importe = salario.getIii();
            }
            if (grupo.contentEquals("IV")) {
                importe = salario.getIv();
            }
            if (grupo.contentEquals("V")) {
                importe = salario.getV();
            }
            if (grupo.contentEquals("VI")) {
                importe = salario.getVi();
            }
            if (grupo.contentEquals("VII")) {
                importe = salario.getVii();
            }
            if (grupo.contentEquals("VIII")) {
                importe = salario.getViii();
            }
            if (grupo.contentEquals("IX")) {
                importe = salario.getIx();
            }
            if (grupo.contentEquals("X")) {
                importe = salario.getX();
            }
            if (grupo.contentEquals("XI")) {
                importe = salario.getXi();
            }
            if (grupo.contentEquals("XII")) {
                importe = salario.getXii();
            }

            tx.commit();
            session.close();
            return importe;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }

    public ObservableList<SuministrosView> getSuministrosRV(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            suministrosViewObservableList = FXCollections.observableArrayList();
            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra WHERE obraId =: idObra AND costoMaterial >: valor").setParameter("idObra", id).setParameter("valor", 0.0).getResultList();
            for (Unidadobra unidadobra : unidadobraArrayList) {
                List<Object[]> objetosList = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.um, rc.peso, rc.preciomn, rc.preciomlc, rc.grupoescala, rc.tipo, SUM(bajo.cantidad), SUM(bajo.costo) FROM Unidadobrarenglon ur INNER JOIN Bajoespecificacion bajo ON ur.unidadobraId = bajo.unidadobraId INNER JOIN Recursos rc ON bajo.idSuministro = rc.id WHERE ur.unidadobraId =: idUO AND ur.conMat =: val group by rc.id, rc.codigo, rc.descripcion, rc.um, rc.peso, rc.preciomn, rc.preciomlc, rc.grupoescala, rc.tipo").setParameter("idUO", unidadobra.getId()).setParameter("val", "0").getResultList();
                for (Object[] row : objetosList) {
                    suministrosViewObservableList.add(new SuministrosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[7].toString()), row[7].toString(), row[8].toString(), "I", 0.0, utilCalcSingelton.createCheckBox(1)));
                }
            }
            trx.commit();
            session.close();
            return suministrosViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<ManoObraView> getManoObra(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            manoObraViewObservableList = FXCollections.observableArrayList();
            List<Object[]> listMano = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uo.obraId =: idOb AND rc.tipo =: tipo GROUP BY rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId").setParameter("idOb", id).setParameter("tipo", "2").getResultList();
            for (Object[] row : listMano) {
                manoObraViewObservableList.add(new ManoObraView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Double.parseDouble(row[4].toString()), getImporteEscala(row[3].toString()), row[5].toString(), row[6].toString()));
            }
            trx.commit();
            session.close();
            return manoObraViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Datos de los Equipos
     */

    public ObservableList<EquiposView> getEquiposView(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            equiposViewObservableList = FXCollections.observableArrayList();
            List<Object[]> listEquipo = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.tipo, rc.preciomn, rc.preciomlc, rc.um, rc.grupoescala, uo.empresaconstructoraId FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uo.obraId =: idOb AND rc.tipo =: tipo GROUP BY rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId").setParameter("idOb", id).setParameter("tipo", "3").getResultList();
            for (Object[] row : listEquipo) {
                equiposViewObservableList.add(new EquiposView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), row[6].toString(), row[7].toString(), getImporteEscala(row[7].toString()), row[8].toString()));
            }
            trx.commit();
            session.close();
            return equiposViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Datos de los juego de Productos
     */
    public ObservableList<JuegoProductoView> getJuegoProductoView(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            juegoProductoViewObservableList = FXCollections.observableArrayList();
            List<Object[]> listJuego = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenece FROM Unidadobra uo INNER JOIN Unidadobrarenglon our ON uo.id = our.unidadobraId INNER JOIN Renglonjuego rj ON our.renglonvarianteId = rj.renglonvarianteId INNER JOIN Juegoproducto jp ON rj.juegoproductoId = jp.id WHERE uo.obraId =: idOb AND our.conMat = '1' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOb", id).getResultList();

            for (Object[] row : listJuego) {
                juegoProductoViewObservableList.add(new JuegoProductoView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), row[7].toString()));

            }


            List<Object[]> listjuegos2 = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenece FROM Unidadobra uo INNER JOIN Unidadobrarenglon our ON uo.id = our.unidadobraId INNER JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId INNER JOIN Juegoproducto jp ON bajo.idSuministro = jp.id WHERE uo.obraId =:idOB AND our.conMat = '0' AND bajo.tipo = 'J' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOB", id).getResultList();
            for (Object[] row : listjuegos2) {
                juegoProductoViewObservableList.add(new JuegoProductoView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), row[7].toString()));
            }

            ArrayList<Juegoproducto> juegoproductosArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece =: idObra").setParameter("idObra", String.valueOf(id)).getResultList();

            for (Juegoproducto juegoproducto : juegoproductosArrayList) {
                juegoProductoViewObservableList.add(new JuegoProductoView(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getDescripcion(), juegoproducto.getPeso(), juegoproducto.getPreciomn(), juegoproducto.getPreciomlc(), juegoproducto.getPertenece()));

            }

            trx.commit();
            session.close();
            return juegoProductoViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Datos de los suministros semielaborados
     */
    public ObservableList<SuministrosSemielaboradosView> getSuministrosSemielaboradosView(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            suministrosSemielaboradosViewObservableList = FXCollections.observableArrayList();
            List<Object[]> dataList = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenec FROM Unidadobra uo INNER JOIN Unidadobrarenglon our ON uo.id = our.unidadobraId INNER JOIN Renglonjuego rj ON our.renglonvarianteId = rj.renglonvarianteId INNER JOIN Suministrossemielaborados jp ON rj.juegoproductoId = jp.id WHERE uo.obraId =: idOb AND our.conMat = '1' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOb", id).getResultList();

            for (Object[] row : dataList) {
                suministrosSemielaboradosViewObservableList.add(new SuministrosSemielaboradosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[6].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, row[7].toString()));

            }

            List<Object[]> dataList2 = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenec FROM Unidadobra uo INNER JOIN Unidadobrarenglon our ON uo.id = our.unidadobraId INNER JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId INNER JOIN Suministrossemielaborados jp ON bajo.idSuministro = jp.id WHERE uo.obraId =:idOB AND our.conMat = '0' AND bajo.tipo = 'S' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOB", id).getResultList();
            for (Object[] row : dataList2) {
                if (row[7] == null) {
                    suministrosSemielaboradosViewObservableList.add(new SuministrosSemielaboradosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Math.round(Double.parseDouble(row[5].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[6].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, "I"));
                } else {
                    suministrosSemielaboradosViewObservableList.add(new SuministrosSemielaboradosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Math.round(Double.parseDouble(row[5].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[6].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, row[7].toString()));
                }
            }

            trx.commit();
            session.close();
            return suministrosSemielaboradosViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<ObraRenglonesEmpresaView> getDatosRenglonVariante(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            renglonVarianteViewObservableList = FXCollections.observableArrayList();
            List<Object[]> resultList = session.createQuery("SELECT ec.codigo, ec.descripcion, rv.codigo, rv.descripcion, rv.um, SUM(uor.cantRv), SUM(uor.costMat), SUM(uor.costMano), SUM(uor.costEquip) FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id INNER JOIN Empresaconstructora ec ON uo.empresaconstructoraId = ec.id WHERE uo.obraId =: idOb GROUP BY rv.id, ec.codigo, ec.descripcion, rv.codigo, rv.descripcion, rv.um").setParameter("idOb", id).getResultList();

            for (Object[] row : resultList) {
                total = Double.parseDouble(row[6].toString()) + Double.parseDouble(row[7].toString()) + Double.parseDouble(row[8].toString());
                renglonVarianteViewObservableList.add(new ObraRenglonesEmpresaView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString()), Double.parseDouble(row[8].toString()), Math.round(total * 100d) / 100d));
            }

            trx.commit();
            session.close();
            return renglonVarianteViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Para el calculo de los indicadores de gastos
     */
    //new BigDecimal(String.format("%.4f", uorvTableView.getCant())).doubleValue()
    public ObservableList<GastosIndView> getDatosIndicadoresEmpresaObra(int idEmpresa) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            obra = structureSingelton.getObra(obra.getId());
            gastosIndViewObservableList = FXCollections.observableArrayList();
            empresaobraconceptoArrayList = new ArrayList<>();
            empresaobraconceptoArrayList.addAll(obra.getEmpresaobraconceptosById().parallelStream().filter(empresaobraconcepto -> empresaobraconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobraconcepto.getConceptosgastoByConceptosgastoId().getPertence() == obra.getSalarioId()).collect(Collectors.toList()));
            for (Empresaobraconcepto empresaobraconcepto : empresaobraconceptoArrayList) {
                gastosIndViewObservableList.add(new GastosIndView(empresaobraconcepto.getConceptosgastoByConceptosgastoId().getCode(), empresaobraconcepto.getConceptosgastoByConceptosgastoId().getDescripcion(), structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == obra.getId() && eocc.getEmpresaconstructoraId() == idEmpresa && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0), String.format("%.2f", empresaobraconcepto.getValor()), empresaobraconcepto.getEmpresaconstructoraId(), empresaobraconcepto.getObraId(), empresaobraconcepto.getConceptosgastoId()));
            }
            trx.commit();
            session.close();
            gastosIndViewObservableList.sort(Comparator.comparing(GastosIndView::getCode));
            return gastosIndViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Para el calculo de los valores para el control del presupuesto
     */

    public ArrayList<PresupControlModel> getSumDatosGlobal(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            empresaobraconceptoglobalArrayList = new ArrayList<>();
            List<Object[]> datosEmpresa = session.createQuery("SELECT eoc.obraId, eoc.conceptosgastoId, SUM(eoc.valor) FROM Empresaobraconcepto eoc WHERE eoc.obraId =: idob GROUP BY eoc.obraId, eoc.conceptosgastoId order by eoc.conceptosgastoId").setParameter("idob", idObra).getResultList();
            for (Object[] row : datosEmpresa) {
                empresaobraconceptoglobalArrayList.add(new PresupControlModel(Integer.parseInt(row[0].toString()), Integer.parseInt(row[1].toString()), Double.parseDouble(row[2].toString())));

            }

            trx.commit();
            session.close();
            return empresaobraconceptoglobalArrayList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void loadDatosEmpresas() {
        empresaCode.setCellValueFactory(new PropertyValueFactory<NewObrasFormTable, JFXCheckBox>("empresa"));
        empresaDescrip.setCellValueFactory(new PropertyValueFactory<NewObrasFormTable, StringProperty>("descripcion"));
    }

    /**
     * test para crear un treeview
     */
    public ArrayList<TreeItem> getProducts() {
        ArrayList<TreeItem> products = new ArrayList<TreeItem>();

        TreeItem zonas = new TreeItem("Zonas");
        TreeItem objetos = new TreeItem("Objetos");
        TreeItem nivel = new TreeItem("Nivel");
        TreeItem especialidades = new TreeItem("Especialidades");
        TreeItem subespecialidades = new TreeItem("Subespecialidades");

        zonas.getChildren().setAll(objetos);
        objetos.getChildren().setAll(nivel);
        nivel.getChildren().addAll(especialidades);
        especialidades.getChildren().addAll(subespecialidades);
        subespecialidades.getChildren().addAll(getObraSubespecialidades(obra.getId()));

        products.add(zonas);
        return products;
    }

    private ArrayList<TreeItem> getObraSubespecialidades(int idObra) {
        ArrayList<TreeItem> items = new ArrayList<TreeItem>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            subespecialidadesArrayList = new ArrayList<Subespecialidades>();

            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra WHERE obraId =: idOb").setParameter("idOb", idObra).getResultList();
            for (Unidadobra unidadobra : unidadobraArrayList) {
                subespecialidadesArrayList.add(session.get(Subespecialidades.class, unidadobra.getSubespecialidadesId()));

            }

            HashSet<Subespecialidades> hs = new HashSet<>();
            hs.addAll(subespecialidadesArrayList);

            subespecialidadesArrayList.clear();
            subespecialidadesArrayList.addAll(hs);

            for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                TreeItem<String> itemSub = new TreeItem(subespecialidades.getDescripcion());
                items.add(itemSub);
            }
            trx.commit();
            session.close();

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return items;

    }

    public Subespecialidades getSubespecialidades(String description) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            subespecialidades = (Subespecialidades) session.createQuery("FROM Subespecialidades WHERE descripcion like :descrip").setParameter("descrip", "%" + description + "%").getSingleResult();

            trx.commit();
            session.close();
            return subespecialidades;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return subespecialidades;
    }

    private ObservableList<UOSubespecialidadesValoresView> getUniddadporSubE(int idObr, int idSub) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            uoSubespecialidadesValoresViewObservableList = FXCollections.observableArrayList();
            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra WHERE obraId =: idOb AND subespecialidadesId =: idSE").setParameter("idOb", idObr).setParameter("idSE", idSub).getResultList();

            for (Unidadobra unidadobra : unidadobraArrayList) {
                uoSubespecialidadesValoresViewObservableList.add(new UOSubespecialidadesValoresView(unidadobra.getId(), unidadobra.getCodigo() + " - " + unidadobra.getDescripcion(), unidadobra.getUm(), unidadobra.getCantidad(), unidadobra.getCostoMaterial(), unidadobra.getCostomano(), unidadobra.getCostoequipo()));
            }

            trx.commit();
            session.close();
            return uoSubespecialidadesValoresViewObservableList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void AddNuevaObraEmpresa(Empresaobra empresaobra) {
        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;


        try {
            trx = ompOsession.beginTransaction();
            ompOsession.persist(empresaobra);
            trx.commit();
            ompOsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    public void loadDatosUOValores() {

        codeUO.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, StringProperty>("descripcion"));
        uoUm.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, StringProperty>("um"));
        uoCantidad.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, DoubleProperty>("cantida"));
        uoCostMat.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, DoubleProperty>("materiales"));
        uoCostMano.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, DoubleProperty>("mano"));
        uoCostEquip.setCellValueFactory(new PropertyValueFactory<UOSubespecialidadesValoresView, DoubleProperty>("equipos"));
    }

    public Salario getSalario(Integer idSalario) {

        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();

            salarioVal = ompOsession.get(Salario.class, idSalario);

            trx.commit();
            ompOsession.close();
            return salarioVal;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

        return salarioVal;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actualizarObraController = this;

        empresa = getEmpresa();
        controller = this;
        loadDatosEmpresas();
        datos = FXCollections.observableArrayList();
        datos = getListEmpresas();
        tableEmpresa.getItems().setAll(datos);
        combo_entidad.setItems(getEntidadesList());
        combo_inversionista.setItems(getListInversionista());
        combo_tipoObra.setItems(getListTipoObra());
        combo_Salario.setItems(getListSalario());
        tableEmpresa.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            if (tableEmpresa.getSelectionModel().getSelectedItem().getEmpresa().isSelected() == true) {
                loadDataEmpresaGasto();
                datosConceptos = FXCollections.observableArrayList();
                datosConceptos = getDatosIndicadoresEmpresaObra(tableEmpresa.getSelectionModel().getSelectedItem().getId());
                gatosTable.getItems().setAll(datosConceptos);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Va a incluir la " + tableEmpresa.getSelectionModel().getSelectedItem().getDescripcion() + " en la obra ");

                ButtonType buttonAgregar = new ButtonType("Agregar");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonAgregar) {
                    newEmpresaobra = new Empresaobra();
                    newEmpresaobra.setObraId(obra.getId());
                    newEmpresaobra.setEmpresaconstructoraId(tableEmpresa.getSelectionModel().getSelectedItem().getId());
                    AddNuevaObraEmpresa(newEmpresaobra);

                    addNewEmpresaObraTarifa(obra.getId(), tableEmpresa.getSelectionModel().getSelectedItem().getId(), obra.getTarifaSalarialByTarifa());

                    List<Empresagastos> empresagastos = new ArrayList<>();
                    empresagastos = empresagastosList.parallelStream().filter(eg -> eg.getEmpresaconstructoraId() == tableEmpresa.getSelectionModel().getSelectedItem().getId()).collect(toList());
                    empresaobraconceptoscoeficientesList = new ArrayList<>();
                    empresaobraconceptoscoeficientesList = empresagastos.parallelStream().map(emg -> {
                        Empresaobraconceptoscoeficientes eocc = new Empresaobraconceptoscoeficientes();
                        eocc.setEmpresaconstructoraId(tableEmpresa.getSelectionModel().getSelectedItem().getId());
                        eocc.setConceptosgastoId(emg.getConceptosgastoId());
                        eocc.setObraId(obra.getId());
                        eocc.setCoeficiente(emg.getCoeficiente());
                        return eocc;
                    }).collect(toList());
                    insertEmpresaobraconceptoscoeficientes(empresaobraconceptoscoeficientesList);
                }
                empresaconstructoraArrayList = getListOfEmpresasInObra(obra.getId());
                for (Empresaconstructora empresaconstructora : empresaconstructoraArrayList) {
                    for (NewObrasFormTable table : tableEmpresa.getItems()) {
                        if (table.getId() == empresaconstructora.getId()) {
                            table.getEmpresa().setSelected(true);
                        }
                    }

                }
            }
        });


        TableCostoUnidades.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2 && TableCostoUnidades.getSelectionModel() != null) {
                subespecialidadesValoresView = TableCostoUnidades.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DesgloseUO.fxml"));
                    Parent proot = loader.load();
                    DesgloseController controller = loader.getController();
                    controller.pasarDatos(subespecialidadesValoresView.getId());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    // stage.setResizable(false);


                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        gatosTable.setOnMouseClicked(event ->
        {
            if (event.getClickCount() == 2) {
                if (gatosTable.getSelectionModel().getSelectedItem().getConcepto().trim().equals("Gastos Indirectos")) {
                    try {
                        String concep = gatosTable.getSelectionModel().getSelectedItem().getConcepto();
                        String c2 = gatosTable.getItems().parallelStream().filter(item -> item.getConcepto().trim().equals("Mano de Obra")).map(GastosIndView::getValor).findFirst().get().toString();
                        String c6 = gatosTable.getItems().parallelStream().filter(item -> item.getConcepto().trim().equals("Gastos Asociados a la Producción de la Obra")).map(GastosIndView::getValor).findFirst().get().toString();
                        String c8 = gatosTable.getItems().parallelStream().filter(item -> item.getConcepto().trim().equals("Gastos Generales y de Administración")).map(GastosIndView::getValor).findFirst().get().toString();
                        double cmoe = utilCalcSingelton.getManodeObraEquipo(obra);
                        double salario = empresaobraconceptoArrayList.parallelStream().filter(item -> item.getObraId() == obra.getId() && item.getEmpresaconstructoraId() == gatosTable.getSelectionModel().getSelectedItem().getIdEmpresa() && item.getConceptosgastoByConceptosgastoId().getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")).map(Empresaobraconcepto::getSalario).findFirst().get();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarConceptoEmpresaObraGIndirecto.fxml"));
                        Parent proot = loader.load();
                        ActualizarConceptoEmpresaObraIndirectoController controller = loader.getController();
                        controller.pasarDatos(concep.trim(), Double.parseDouble(c2), cmoe, salario, Double.parseDouble(c6), Double.parseDouble(c8));

                        Stage stage = new Stage();
                        stage.setScene(new Scene(proot));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.show();


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (!gatosTable.getSelectionModel().getSelectedItem().getConcepto().trim().equals("Materiales") || !gatosTable.getSelectionModel().getSelectedItem().getConcepto().trim().equals("Mano de Obra")) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarConceptoEmpresaObra.fxml"));
                            Parent proot = loader.load();
                            ActualizarConceptoEmpresaObraController controller = loader.getController();
                            controller.getDatosConceptos(gatosTable.getSelectionModel().getSelectedItem());

                            Stage stage = new Stage();
                            stage.setScene(new Scene(proot));
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setResizable(false);
                            stage.show();

                            stage.setOnCloseRequest(event1 -> {
                                loadDataEmpresaGasto();
                                datosConceptos = FXCollections.observableArrayList();
                                datosConceptos = getDatosIndicadoresEmpresaObra(tableEmpresa.getSelectionModel().getSelectedItem().getId());
                                gatosTable.getItems().setAll(datosConceptos);
                            });

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
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
        utilCalcSingelton.createEmpresaobratarifa(empresaobratarifaList);
    }

    private double c6;
    private double c8;
    private double valSalario;
    private ObservableList<ResumenCostosList> datosResumenCostosListObservableList;
    private List<Conceptosgasto> list;
    private Double[] datosCertificacion;
    private double salarioCal;

    public void populateTableBiEmpresa(ActionEvent event) {
        loadResumenDatosCertificaciones();
        Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
        if (getResumenCertifInMonth(obra.getId(), empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue())).size() > 0) {
            datosResumenCostosListObservableList = getResumenCertifInMonth(obra.getId(), empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));
            datosResumenCostosListObservableList.sort(Comparator.comparing(ResumenCostosList::getIndice));
            tableResumen.getItems().setAll(datosResumenCostosListObservableList);
            tableResumen.setEditable(true);
        } else if (getResumenCertifInMonth(obra.getId(), empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue())).size() == 0) {
            datosCertificacion = getCostosDirectosCertificados(Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()), empresaconstructora);
            datosResumenCostosListObservableList = createResumenCertificacion(datosCertificacion, empresaconstructora, Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));
            datosResumenCostosListObservableList.sort(Comparator.comparing(ResumenCostosList::getIndice));
            tableResumen.getItems().setAll(datosResumenCostosListObservableList);
            tableResumen.setEditable(true);
        }
    }

    public void handleDeleteresumen(ActionEvent event) {
        Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
        deleteResumenCertifInMonth(obra.getId(), empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));
        populateTableBiEmpresa(event);
    }

    private void loadResumenDatosCertificaciones() {

        datosResumenCostosListObservableList = FXCollections.observableArrayList();
        indice.setCellValueFactory(new PropertyValueFactory<ResumenCostosList, String>("indice"));
        presup1.setCellValueFactory(new PropertyValueFactory<ResumenCostosList, String>("conceptos"));
        valorvers1.setCellValueFactory(new PropertyValueFactory<ResumenCostosList, String>("valor"));

        valorvers1.setCellFactory(TextFieldTableCell.<ResumenCostosList>forTableColumn());
        valorvers1.setOnEditCommit((TableColumn.CellEditEvent<ResumenCostosList, String> t) -> {
            if (((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).getIndice().trim().equals("03")) {
                double val = Double.parseDouble(t.getOldValue()) + Double.parseDouble(t.getNewValue());
                ((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(String.format("%.2f", val));
            }
            if (((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).getIndice().trim().equals("04")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("CantidadC4Insert.fxml"));
                    Parent proot = loader.load();
                    CantidadC4InsertController controller = (CantidadC4InsertController) loader.getController();
                    controller.pasarDatos(((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).getIndice());
                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();
                    stage.setOnCloseRequest(event1 -> {
                        ((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(String.format("%.2f", controller.costo));
                        valSalario = controller.salario;
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).getIndice().trim().equals("09")) {
                Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
                String concep = "Gastos Indirectos";
                String c2 = datosResumenCostosListObservableList.parallelStream().filter(item -> item.getIndice().trim().equals("02")).map(ResumenCostosList::getValor).findFirst().get();
                String c6S = datosResumenCostosListObservableList.parallelStream().filter(item -> item.getIndice().trim().equals("06")).map(ResumenCostosList::getValor).findFirst().get();
                String c8S = datosResumenCostosListObservableList.parallelStream().filter(item -> item.getIndice().trim().equals("08")).map(ResumenCostosList::getValor).findFirst().get();
                double cmoe = utilCalcSingelton.getManodeObraEquipoCertificaciones(obra, empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));
                salarioCal = 0.0;
                if (valSalario != 0.0) {
                    salarioCal = valSalario;
                } else if (valSalario == 0.0) {
                    salarioCal = getSalarioCalc(obra.getId(), empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarConceptoEmpresaObraGIndirecto.fxml"));
                    Parent proot = loader.load();
                    ActualizarConceptoEmpresaObraIndirectoController controller = loader.getController();
                    controller.pasarDatos(concep.trim(), Double.parseDouble(c2), cmoe, salarioCal, Double.parseDouble(c6S), Double.parseDouble(c8S));

                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();
                } catch (Exception e) {

                }
            } else {
                ((ResumenCostosList) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(String.format(t.getNewValue()));
            }
        });

    }

    public void handleRecalcularValores(ActionEvent event) {
        datosResumenCostosListObservableList = FXCollections.observableArrayList();
        double c1 = 0.0;
        double c2 = 0.0;
        double c3 = 0.0;
        double c4 = 0.0;
        double c5 = 0.0;
        double c7 = 0.0;
        double c9 = 0.0;
        double c10 = 0.0;
        double c11 = 0.0;
        double c12 = 0.0;
        double c13 = 0.0;
        double c14 = 0.0;
        double c15 = 0.0;
        double c16 = 0.0;
        double c17 = 0.0;
        Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
        double cmoe = utilCalcSingelton.getManodeObraEquipoCertificaciones(obra, empresaconstructora.getId(), Date.valueOf(ini.getValue()), Date.valueOf(finD.getValue()));

        for (ResumenCostosList item : tableResumen.getItems()) {
            if (item.getIndice().trim().equals("01")) {
                c1 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("02")) {
                c2 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("03")) {
                c3 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("04")) {
                c4 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("05")) {
                c5 = c1 + c2 + c3 + c4;
                item.setValor(String.valueOf(String.format("%.2f", c5)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("06")) {
                c6 = Double.parseDouble(item.getValor());
                item.setValor(String.valueOf(String.format("%.2f", c6)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("07")) {
                c7 = c5 + c6;
                item.setValor(String.valueOf(String.format("%.2f", c7)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("08")) {
                c8 = Double.parseDouble(item.getValor());
                item.setValor(String.valueOf(String.format("%.2f", c8)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("09")) {
                double ajuste = cmoe + c2 + valSalario;
                double normal = c8 + c6;
                if (ajuste > normal) {
                    c9 = normal;
                    item.setValor(String.valueOf(String.format("%.2f", c9)));
                    datosResumenCostosListObservableList.add(item);
                } else if (ajuste < normal) {
                    c9 = ajuste;
                    item.setValor(String.valueOf(String.format("%.2f", c9)));
                    datosResumenCostosListObservableList.add(item);
                }
            } else if (item.getIndice().trim().equals("10")) {
                c10 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("11")) {
                c11 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("12")) {
                c12 = Double.parseDouble(item.getValor());
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("13")) {
                c13 = c8 + c10 + c11 + c12;
                item.setValor(String.valueOf(String.format("%.2f", c13)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("14")) {
                c14 = c5 + c9 + c10 + c11 + c12;
                item.setValor(String.valueOf(String.format("%.2f", c14)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("16")) {
                double rest = c14 - c1 - c13;
                c15 = 0.15 * rest;
                item.setValor(String.valueOf(String.format("%.2f", c15)));
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("15")) {
                datosResumenCostosListObservableList.add(item);
            } else if (item.getIndice().trim().equals("17")) {
                c17 = c13 + c14 + c15;
                item.setValor(String.valueOf(String.format("%.2f", c17)));
                datosResumenCostosListObservableList.add(item);
            }
        }
        tableResumen.getItems().clear();
        tableResumen.setItems(datosResumenCostosListObservableList);
    }

    private ObservableList<ResumenCostosList> createResumenCertificacion(Double[] datos, Empresaconstructora empresaconstructora, Date d1, Date d2) {
        List<Conceptosgasto> list = getAllConceptos(obra.getSalarioId());
        ObservableList<ResumenCostosList> listObservableList = FXCollections.observableArrayList();
        double c1 = datos[0];
        double c2 = datos[1];
        double c3 = datos[2];
        double cmoeCert = utilCalcSingelton.getManodeObraEquipoCertificaciones(obra, empresaconstructora.getId(), d1, d2);
        double c5 = 0.0;
        double c7 = 0.0;
        double c14 = 0.0;
        double c15 = 0.0;
        double c9 = 0.0;
        c6 = 0.0;
        c8 = 0.0;
        if (obra.getSalarioId() == 3 || obra.getSalarioId() == 2) {
            for (Conceptosgasto conceptosgasto : list) {
                if (conceptosgasto.getDescripcion().trim().equals("Materiales")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c1))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Mano de Obra")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c2))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Uso de Equipos")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c3))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Costos Directos de Producción")) {
                    c5 = c1 + c2 + c3;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c5))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Gastos Asociados a la Producción de la Obra")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Total de Costos y Gastos de Producción de la Obra")) {
                    c7 = c5;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c7))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Gastos Generales y de Administración")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Gastos Indirectos")) {
                    c9 = cmoeCert + valSalario + c2;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c9))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Otros Conceptos de Gastos")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Gastos Financieros")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Gastos Tributarios")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Total de Gastos de la Obra")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Total de Costos y Gastos")) {
                    c14 = c5 + c9;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c14))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Impuestos sobre ventas autorizados por MFP")) {
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(0.0)));
                } else if (conceptosgasto.getDescripcion().trim().equals("Utilidad")) {
                    double rest = c14 - c1;
                    c15 = 0.15 * rest;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", c15))));
                } else if (conceptosgasto.getDescripcion().trim().equals("Precio del Servicio de Construcción y Montaje")) {
                    double total = c14 + c15;
                    listObservableList.add(new ResumenCostosList(conceptosgasto.getCode(), conceptosgasto.getDescripcion(), String.valueOf(String.format("%.2f", total))));
                }
            }
        }

        return listObservableList;
    }

    private List<Conceptosgasto> getAllConceptos(int pertenece) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            list = new ArrayList<>();
            list = session.createQuery(" FROM Conceptosgasto WHERE pertence =: val order by code ASC ").setParameter("val", pertenece).getResultList();
            trx.commit();
            session.close();
            list.sort(Comparator.comparing(Conceptosgasto::getCode));
            return list;

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();

    }

    private int getIdConcepto(String code) {
        return list.parallelStream().filter(item -> item.getCode().trim().equals(code.trim()) && item.getPertence() == obra.getSalarioId()).findFirst().map(Conceptosgasto::getId).get();
    }

    public void saveresumenConceptos(ActionEvent event) {
        LocalDate dateDes = ini.getValue();
        LocalDate dateHast = finD.getValue();
        List<Certificacionresumenconceptos> certificacionresumenconceptosList = new ArrayList<>();
        Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
        for (ResumenCostosList item : tableResumen.getItems()) {
            Certificacionresumenconceptos crcg = new Certificacionresumenconceptos();
            crcg.setIdConcepto(getIdConcepto(item.getIndice()));
            crcg.setIdObra(obra.getId());
            crcg.setIdEmpresa(empresaconstructora.getId());
            crcg.setValor(Double.parseDouble(item.getValor()));
            if (item.getIndice().trim().equals("04")) {
                crcg.setSalario(valSalario);
            } else {
                crcg.setSalario(0.0);
            }
            crcg.setFini(Date.valueOf(dateDes));
            crcg.setFfin(Date.valueOf(dateHast));
            certificacionresumenconceptosList.add(crcg);
        }
        addResumenConceptos(certificacionresumenconceptosList);
    }

    public void handleReportResumenPrint(ActionEvent event) {
        Empresaconstructora empresaconstructora = obra.getEmpresaobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim().equals(empresacombo.getValue())).findFirst().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId()).get();
        date = LocalDate.now();
        parametros = new HashMap<>();
        parametros.put("obraName", obra.getCodigo() + " " + obra.getDescripion() + "     " + "(Resumen de Conceptos Gastos Certificados)");
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("empresa", empresa.getNombre());
        parametros.put("comercial", empresa.getComercial());
        parametros.put("reportName", empresaconstructora.getDescripcion().trim());
        parametros.put("image", "templete/logoReport.jpg");

        try {
            DynamicReport dr = createResumenCostosByResumen();
            JRDataSource ds = new JRBeanCollectionDataSource(tableResumen.getItems().stream().collect(Collectors.toList()));
            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public DynamicReport createResumenCostosByResumen() {

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
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //		headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();

        Integer margin = (10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(15).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setTemplateFile("templete/report8Letter.jrxml")
                .setUseFullPageWidth(true)
                .setOddRowBackgroundStyle(oddRowStyle);

        AbstractColumn indice = ColumnBuilder.getNew()
                .setColumnProperty("indice", String.class.getName())
                .setTitle("No.").setWidth(5)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn materiales = ColumnBuilder.getNew()
                .setColumnProperty("conceptos", String.class.getName())
                .setTitle("Conceptos").setWidth(55)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn equipo = ColumnBuilder.getNew()
                .setColumnProperty("valor", String.class.getName())
                .setTitle("Valor").setWidth(12)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(indice);
        drb.addColumn(materiales);
        drb.addColumn(equipo);


        DynamicReport dr = drb.build();
        return dr;
    }

    public void addResumenConceptos(List<Certificacionresumenconceptos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Certificacionresumenconceptos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.saveOrUpdate(recursos);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al salvar el resumen");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    public ObservableList<ResumenCostosList> getResumenCertifInMonth(int idObra, int idEmpr, Date ini, Date fin) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ObservableList<ResumenCostosList> listOfConceptos = FXCollections.observableArrayList();
            List<Tuple> listOfData = session.createQuery("SELECT cg.code, cg.descripcion, crc.valor, crc.salario  FROM Certificacionresumenconceptos crc INNER JOIN Conceptosgasto cg ON cg.id = crc.idConcepto WHERE crc.idObra =: idO AND crc.idEmpresa =: idEm AND crc.fini =: fi AND crc.ffin =: ff", Tuple.class).setParameter("idO", idObra).setParameter("idEm", idEmpr).setParameter("fi", ini).setParameter("ff", fin).getResultList();
            for (Tuple item : listOfData) {
                listOfConceptos.add(new ResumenCostosList(item.get(0).toString(), item.get(1).toString(), item.get(2).toString()));
            }
            tx.commit();
            session.close();
            return listOfConceptos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al salvar el resumen");
            alert.showAndWait();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public double getSalarioCalc(int idObra, int idEmpr, Date ini, Date fin) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        double calc = 0.0;
        try {
            tx = session.beginTransaction();
            ObservableList<ResumenCostosList> listOfConceptos = FXCollections.observableArrayList();
            List<Tuple> listOfData = session.createQuery("SELECT crc.salario  FROM Certificacionresumenconceptos crc INNER JOIN Conceptosgasto cg ON cg.id = crc.idConcepto WHERE crc.idObra =: idO AND crc.idEmpresa =: idEm AND crc.fini =: fi AND crc.ffin =: ff AND cg.code = '04'", Tuple.class).setParameter("idO", idObra).setParameter("idEm", idEmpr).setParameter("fi", ini).setParameter("ff", fin).getResultList();
            for (Tuple item : listOfData) {
                calc = Double.parseDouble(item.get(0).toString());
            }
            tx.commit();
            session.close();
            return calc;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al salvar el resumen");
            alert.showAndWait();
        } finally {
            session.close();
        }
        return calc;
    }

    public void deleteResumenCertifInMonth(int idObra, int idEmpr, Date ini, Date fin) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op1 = session.createQuery("DELETE FROM Certificacionresumenconceptos WHERE idObra =: idO AND idEmpresa =: idEm AND fini =: fi AND ffin =: ff").setParameter("idO", idObra).setParameter("idEm", idEmpr).setParameter("fi", ini).setParameter("ff", fin).executeUpdate();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al salvar el resumen");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }


    private Double[] getCostosDirectosCertificados(Date ini, Date fin, Empresaconstructora empresaconstructora) {

        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();

            Double[] datos = new Double[3];
            List<Tuple> veloresCertificados = ompOsession.createQuery("SELECT SUM(cert.costmaterial), SUM(cert.costmano), SUM(cert.costequipo) FROM Certificacion cert inner join Unidadobra uo ON cert.unidadobraId = uo.id WHERE uo.obraId =: idO AND cert.desde >=: des And cert.hasta <=: hast AND uo.empresaconstructoraId =: idEMp", Tuple.class).setParameter("idO", obra.getId()).setParameter("des", ini).setParameter("hast", fin).setParameter("idEMp", empresaconstructora.getId()).getResultList();
            for (Tuple velor : veloresCertificados) {
                if (velor.get(0) == null) {
                    datos[0] = 0.0;
                } else {
                    datos[0] = Double.parseDouble(velor.get(0).toString());
                }

                if (velor.get(1) == null) {
                    datos[1] = 0.0;
                } else {
                    datos[1] = Double.parseDouble(velor.get(1).toString());
                }

                if (velor.get(2) == null) {
                    datos[2] = 0.0;
                } else {
                    datos[2] = Double.parseDouble(velor.get(2).toString());
                }
            }
            trx.commit();
            ompOsession.close();
            return datos;

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

        return new Double[3];
    }


    public void loadDatosSuministros() {

        sumCode.setCellValueFactory(new PropertyValueFactory<SuministrosView, String>("codigo"));
        sumDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        sumUm.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        sumMn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));

        datosSuministros = FXCollections.observableArrayList();
        datosSuministros = getSuminsitros(obra.getId());
        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(datosSuministros, p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableSum.comparatorProperty());

        tableSum.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(renglonVarianteView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = renglonVarianteView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

        sumCode.setCellFactory(new Callback<TableColumn<SuministrosView, String>, TableCell<SuministrosView, String>>() {
            @Override
            public TableCell<SuministrosView, String> call(TableColumn<SuministrosView, String> param) {
                return new TableCell<SuministrosView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("  ")) {
                                this.setTextFill(javafx.scene.paint.Color.web("#6610f2"));
                                setText(item.toString());
                            } else {
                                this.setTextFill(javafx.scene.paint.Color.BLACK);
                                setText(item.toString());
                            }
                        }
                    }
                };
            }
        });

    }

    public void handleGatSuministrosBajoespecificacion(ActionEvent event) {

        sumCode.setCellValueFactory(new PropertyValueFactory<SuministrosView, String>("codigo"));
        sumDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        sumUm.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        sumMn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));

        tableSum.getItems().removeAll();

        datosSuministros = FXCollections.observableArrayList();
        datosSuministros.addAll(listRecBajoEsp(obra.getId()).parallelStream().collect(Collectors.toSet()).stream().collect(Collectors.toList()));
        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(datosSuministros, p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableSum.comparatorProperty());

        tableSum.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(renglonVarianteView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = renglonVarianteView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

        sumCode.setCellFactory(new Callback<TableColumn<SuministrosView, String>, TableCell<SuministrosView, String>>() {
            @Override
            public TableCell<SuministrosView, String> call(TableColumn<SuministrosView, String> param) {
                return new TableCell<SuministrosView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("  ")) {
                                this.setTextFill(javafx.scene.paint.Color.web("#6610f2"));
                                setText(item.toString());
                            } else {
                                this.setTextFill(javafx.scene.paint.Color.BLACK);
                                setText(item.toString());
                            }
                        }
                    }
                };
            }
        });

    }


    public void loadDatosSuministrosRV() {

        sumRvCode.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        sumRVDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        sumRvUm.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        sumRvMn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        sumRvMlc.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomlc"));


    }

    public void loadDatosMano() {

        manoCode.setCellValueFactory(new PropertyValueFactory<ManoObraView, StringProperty>("codigo"));
        manoDescrip.setCellValueFactory(new PropertyValueFactory<ManoObraView, StringProperty>("descripcion"));
        manoUm.setCellValueFactory(new PropertyValueFactory<ManoObraView, StringProperty>("um"));
        manoPrecio.setCellValueFactory(new PropertyValueFactory<ManoObraView, DoubleProperty>("preciomn"));
        manoSalario.setCellValueFactory(new PropertyValueFactory<ManoObraView, DoubleProperty>("preciomlc"));
    }

    public void loadDatosEquipo() {
        equipCode.setCellValueFactory(new PropertyValueFactory<EquiposView, StringProperty>("codigo"));
        equipDescrip.setCellValueFactory(new PropertyValueFactory<EquiposView, StringProperty>("descripcion"));
        equipUm.setCellValueFactory(new PropertyValueFactory<EquiposView, StringProperty>("um"));
        equipMn.setCellValueFactory(new PropertyValueFactory<EquiposView, DoubleProperty>("preciomn"));
        equipSalario.setCellValueFactory(new PropertyValueFactory<EquiposView, DoubleProperty>("salario"));
    }

    public void loadDatosJuegoProducto() {
        juegoCode.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("codigo"));
        juegoDescrip.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("descripcion"));
        juegoUm.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("um"));
        juegoMn.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("preciomn"));
        juegoMlc.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("preciomlc"));
    }

    public void loadDatosSemielaborados() {
        semiCode.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("codigo"));
        semiDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("descripcion"));
        semiUm.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("um"));
        semiMn.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomn"));
        // semiMlc.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomlc"));
    }

    public void loadDatosRenglones() {
        rengCode.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, StringProperty>("rvCodigo"));
        rengDescrip.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, StringProperty>("rvDescrip"));
        rengUm.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, StringProperty>("rvUM"));
        renCostMat.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, DoubleProperty>("rvMat"));
        renCostMano.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, DoubleProperty>("rvMano"));
        renCostEquip.setCellValueFactory(new PropertyValueFactory<ObraRenglonesEmpresaView, DoubleProperty>("rvEquipo"));
    }

    public void loadDataEmpresaGasto() {
        concepto.setCellValueFactory(new PropertyValueFactory<GastosIndView, StringProperty>("concepto"));
        coeficiente.setCellValueFactory(new PropertyValueFactory<GastosIndView, DoubleProperty>("code"));
        valor.setCellValueFactory(new PropertyValueFactory<GastosIndView, DoubleProperty>("valor"));
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

    public void pasarDatosObra(Obra obrasView) throws Exception {

        /**
         * idea de solucion a los carteles de ejecución
         * Se lanza una ventana tipo modal que indica acción
         */

        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Calculando componentes de la obra...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();


        /**
         * se ejecutan los metodos para llenar la ventana
         */

        obra = getObra(obrasView.getId());

        treeView = new JFXTreeView();
        treeItem = new TreeItem(obra.getDescripion());

        treeView.setPrefWidth(estructuraPane.getPrefWidth() - 10);
        treeView.setPrefHeight(350);

        treeItem.getChildren().addAll(getProducts());
        treeView.setRoot(treeItem);
        estructuraPane.getChildren().add(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> seleted = newValue;
                subespecialidades = getSubespecialidades(newValue.getValue());
                loadDatosUOValores();
                TableCostoUnidades.getItems().clear();
                uoSubespecialidadesValoresViewObservableList = FXCollections.observableArrayList();
                uoSubespecialidadesValoresViewObservableList = getUniddadporSubE(obra.getId(), subespecialidades.getId());
                TableCostoUnidades.getItems().setAll(uoSubespecialidadesValoresViewObservableList);
            }


        });

        empresagastosList = new ArrayList<>();
        empresagastosList = structureSingelton.getEmpresagastosList();

        field_codigo.setText(obra.getCodigo());
        text_descripcion.setText(obra.getDescripion());

        labelObraName.setText(obra.getCodigo() + " - " + obra.getDescripion());

        var entidad = entidadArrayList.parallelStream().filter(ent -> ent.getId() == obra.getEntidadId()).findFirst().orElse(null);
        combo_entidad.getSelectionModel().select(entidad.getCodigo() + " - " + entidad.getDescripcion());

        var inversionista = inversionistaArrayList.parallelStream().filter(inv -> inv.getId() == obra.getInversionistaId()).findFirst().orElse(null);
        combo_inversionista.getSelectionModel().select(inversionista.getCodigo() + " - " + inversionista.getDescripcion());

        var tipoobra = tipoobrasArrayList.parallelStream().filter(tipo -> tipo.getId() == obra.getTipoobraId()).findFirst().orElse(null);
        combo_tipoObra.getSelectionModel().select(tipoobra.getCodigo() + " - " + tipoobra.getDescripcion());

        var salario = salarioArrayList.parallelStream().filter(sal -> sal.getId() == obra.getSalarioId()).findFirst().orElse(null);
        combo_Salario.getSelectionModel().select(salario.getDescripcion());

        empresaconstructoraArrayList = getListOfEmpresasInObra(obrasView.getId());
        ObservableList<String> listOfItems = FXCollections.observableArrayList();
        listOfItems.addAll(empresaconstructoraArrayList.parallelStream().map(empresaconstructora -> empresaconstructora.getCodigo() + " " + empresaconstructora.getDescripcion()).collect(toList()));
        renglonesEmpresas.setItems(listOfItems);
        empresaMano.setItems(listOfItems);
        equiposEmpresas.setItems(listOfItems);

        for (Empresaconstructora empresaconstructora : empresaconstructoraArrayList) {
            for (NewObrasFormTable table : tableEmpresa.getItems()) {
                if (table.getId() == empresaconstructora.getId()) {
                    table.getEmpresa().setSelected(true);
                }
            }
        }

        /**
         * suministros
         */
        loadDatosSuministros();
        datosResumen(obra.getId());

        if (obra.getSalarioId() == 1) {
            PresupControlModel globalc20 = getSumDatosGlobal(obra.getId()).parallelStream().filter(g -> g.getConceptoId() == 20).findFirst().orElse(null);
            if (globalc20 != null) {
                labelMonto.setText(String.valueOf(globalc20.getValor()));
            } else {
                labelMonto.setText("0.0");
            }
        } else if (obra.getSalarioId() == 3 || obra.getSalarioId() == 2) {
            PresupControlModel globalc20 = getSumDatosGlobal(obra.getId()).parallelStream().filter(g -> g.getConceptoId() == 38).findFirst().orElse(null);
            if (globalc20 != null) {
                labelMonto.setText(String.valueOf(globalc20.getValor()));
            } else {
                labelMonto.setText("0.0");
            }
        }

        /**
         * para modificar los coeficientes y agregar los valores...
         */
        ObservableList<String> empresasList = FXCollections.observableArrayList();
        empresasList.addAll(obra.getEmpresaobrasById().parallelStream().map(empresaobra -> empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().toString()).collect(Collectors.toSet()));
        empresacombo.setItems(empresasList);

        LocalDate t1 = LocalDate.now();
        LocalDate inicio = t1.with(firstDayOfMonth());
        LocalDate fin = t1.with(lastDayOfMonth());

        ini.setValue(inicio);
        finD.setValue(fin);
    }

    private int getLastConcept() {
        return obra.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoByConceptosgastoId().getDescripcion().trim().equals("Precio del Servicio de Construcción y Montaje")).map(Empresaobraconcepto::getConceptosgastoId).findFirst().get();

    }

    public void handleAddSuministrosAction(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitro.fxml"));
            Parent proot = loader.load();
            NuevoSuministroController controller = loader.getController();
            controller.pasarParametros(obra.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadDatosSuministros();
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hadlecheckEquipoObra(ActionEvent event) {
        String[] parEmp = equiposEmpresas.getValue().split(" ");
        var empresaconst = empresaconstructoraArrayList.stream().filter(e -> e.getCodigo().equals(parEmp[0])).findFirst().orElse(null);

        datosEquipos = FXCollections.observableArrayList();
        List<EquiposView> datosEquip = equiposViewObservableList.parallelStream().filter(equiposView -> equiposView.getIdEm().equals(String.valueOf(empresaconst.getId()))).collect(toList());
        datosEquipos.addAll(datosEquip);
        tableEquipos.getItems().clear();
        tableEquipos.getItems().setAll(datosEquipos);
    }

    public void hadlecheckManoObra(ActionEvent event) {
        String[] parEmp = empresaMano.getValue().split(" ");
        var empresaconst = empresaconstructoraArrayList.stream().filter(e -> e.getCodigo().equals(parEmp[0])).findFirst().orElse(null);

        datosMano = FXCollections.observableArrayList();
        List<ManoObraView> datosManoObraViews = manoObraViewObservableList.parallelStream().filter(manoObra -> manoObra.getIdEmp().equals(String.valueOf(empresaconst.getId()))).collect(toList());
        datosMano.addAll(datosManoObraViews);
        tableSalario.getItems().clear();
        tableSalario.getItems().setAll(datosMano);
    }

    public void hadlecheckRV(ActionEvent event) {
        String[] parEmp = renglonesEmpresas.getValue().split(" ");

        datosRenglonVariante = FXCollections.observableArrayList();
        var empresaconst = empresaconstructoraArrayList.stream().filter(e -> e.getCodigo().equals(parEmp[0])).findFirst().orElse(null);

        List<ObraRenglonesEmpresaView> datosRenglones = renglonVarianteViewObservableList.parallelStream().filter(mano -> mano.getEcCodigo().equals(empresaconst.getCodigo())).collect(toList());
        tableRenglones.getItems().clear();
        datosRenglonVariante.addAll(datosRenglones);
        tableRenglones.getItems().setAll(datosRenglonVariante);
    }

    public void handleViewMano(ActionEvent event) {
        if (checkMano.isSelected() == true) {
            /**
             * Panel de la mano de obra
             */
            loadDatosMano();
            datosMano = FXCollections.observableArrayList();
            datosMano.addAll(getManoObra(obra.getId()));
            tableSalario.getItems().setAll(datosMano);

            empresaMano.setVisible(true);
            renglonesEmpresas.setVisible(false);
            equiposEmpresas.setVisible(false);
            checkSum.setSelected(false);
            checkSemi.setSelected(false);
            checkJuego.setSelected(false);
            checkEquipos.setSelected(false);
            checkReng.setSelected(false);
            paneSuministros.toBack();
            paneEquipos.toBack();
            paneSemiel.toBack();
            paneJuegos.toBack();
            paneRenglones.toBack();
            paneMano.toFront();

        }
    }

    public void handleViewSum(ActionEvent event) {
        if (checkSum.isSelected() == true) {

            /**
             * Panel suministros
             */
            loadDatosSuministrosRV();
            datosSumistrosRV = FXCollections.observableArrayList();
            datosSumistrosRV.addAll(getSuministrosRV(obra.getId()));
            tableSumRV.getItems().setAll(datosSumistrosRV);

            empresaMano.setVisible(false);
            renglonesEmpresas.setVisible(false);
            equiposEmpresas.setVisible(false);
            checkMano.setSelected(false);
            checkSemi.setSelected(false);
            checkJuego.setSelected(false);
            checkEquipos.setSelected(false);
            checkReng.setSelected(false);
            paneMano.toBack();
            paneEquipos.toBack();
            paneSemiel.toBack();
            paneJuegos.toBack();
            paneRenglones.toBack();
            paneSuministros.toFront();

        }
    }

    public void handleViewEquipos(ActionEvent event) {
        if (checkEquipos.isSelected() == true) {

            loadDatosEquipo();
            datosEquipos = FXCollections.observableArrayList();
            datosEquipos.addAll(getEquiposView(obra.getId()));
            tableEquipos.getItems().setAll(datosEquipos);


            equiposEmpresas.setVisible(true);
            renglonesEmpresas.setVisible(false);
            empresaMano.setVisible(false);
            checkSum.setSelected(false);
            checkSemi.setSelected(false);
            checkJuego.setSelected(false);
            checkMano.setSelected(false);
            checkReng.setSelected(false);
            paneSuministros.toBack();
            paneEquipos.toFront();
            paneSemiel.toBack();
            paneJuegos.toBack();
            paneRenglones.toBack();
            paneMano.toBack();

        }
    }

    public void handleViewJuego(ActionEvent event) {
        if (checkJuego.isSelected() == true) {

            /**
             * Panel Juego de Producto
             */
            loadDatosJuegoProducto();
            datosJuego = FXCollections.observableArrayList();
            datosJuego = getJuegoProductoView(obra.getId());
            tableJuego.getItems().setAll(datosJuego);

            empresaMano.setVisible(false);
            renglonesEmpresas.setVisible(false);
            equiposEmpresas.setVisible(false);
            checkSum.setSelected(false);
            checkSemi.setSelected(false);
            checkEquipos.setSelected(false);
            checkMano.setSelected(false);
            checkReng.setSelected(false);
            paneSuministros.toBack();
            paneEquipos.toBack();
            paneSemiel.toBack();
            paneJuegos.toFront();
            paneRenglones.toBack();
            paneMano.toBack();

        }
    }

    public void handleViewSemielaborados(ActionEvent event) {
        if (checkSemi.isSelected() == true) {
            /**
             * Panel Suministros Semielaborados
             */
            loadDatosSemielaborados();
            datosSemielaborados = FXCollections.observableArrayList();
            datosSemielaborados = getSuministrosSemielaboradosView(obra.getId());
            tableSemielab.getItems().setAll(datosSemielaborados);

            empresaMano.setVisible(false);
            renglonesEmpresas.setVisible(false);
            equiposEmpresas.setVisible(false);
            checkSum.setSelected(false);
            checkJuego.setSelected(false);
            checkEquipos.setSelected(false);
            checkMano.setSelected(false);
            checkReng.setSelected(false);
            paneSuministros.toBack();
            paneEquipos.toBack();
            paneSemiel.toFront();
            paneJuegos.toBack();
            paneRenglones.toBack();
            paneMano.toBack();

        }
    }

    public void handleViewRenglones(ActionEvent event) {
        if (checkReng.isSelected() == true) {
            /**
             * Panel de los rengloges
             */
            loadDatosRenglones();
            datosRenglonVariante = FXCollections.observableArrayList();
            datosRenglonVariante = getDatosRenglonVariante(obra.getId());
            tableRenglones.getItems().setAll(datosRenglonVariante);

            renglonesEmpresas.setVisible(true);
            empresaMano.setVisible(false);
            equiposEmpresas.setVisible(false);
            checkSum.setSelected(false);
            checkJuego.setSelected(false);
            checkEquipos.setSelected(false);
            checkMano.setSelected(false);
            checkSemi.setSelected(false);
            paneSuministros.toBack();
            paneEquipos.toBack();
            paneSemiel.toBack();
            paneJuegos.toBack();
            paneRenglones.toFront();
            paneMano.toBack();

        }
    }

    public void handleImportarSumView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarSuminitros.fxml"));
            Parent proot = loader.load();
            ImportarSuministrosController controllerImp = loader.getController();
            controllerImp.pasarParametros(controller, obra.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addDatosGlobal(ArrayList<Empresaobraconceptoglobal> empresaobraconceptoglobal) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            for (Empresaobraconceptoglobal eog : empresaobraconceptoglobal) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eog);
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

    public Integer noVersion(int idObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        version = 0;

        try {
            tx = session.beginTransaction();
            Long val = (Long) session.createQuery("SELECT Count(obraId)FROM Empresaobraconceptoglobal where obraId =: id ").setParameter("id", idObra).getSingleResult();

            if (val != null) {
                version = val.intValue() / 20;
            } else {
                version = 0;

            }
            tx.commit();
            session.close();
            return version;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return version;

    }

    public void handleWriteGlobalData(ActionEvent event) {
        temp = new ArrayList<>();
        empresaobraconceptoglobalArrayList = new ArrayList<>();
        empresaobraconceptoglobalArrayList = getSumDatosGlobal(obra.getId());
        date = LocalDate.now();
        fecha = Date.valueOf(date);
        version = noVersion(obra.getId());

        for (PresupControlModel empObrCon : empresaobraconceptoglobalArrayList) {
            empresaobraconceptoglobal = new Empresaobraconceptoglobal();
            empresaobraconceptoglobal.setObraId(empObrCon.getObraId());
            empresaobraconceptoglobal.setConceptoId(empObrCon.getConceptoId());
            empresaobraconceptoglobal.setFecha(fecha);
            empresaobraconceptoglobal.setValor(empObrCon.getValor());

            if (version == 0) {
                empresaobraconceptoglobal.setPbase("1");
                empresaobraconceptoglobal.setDescripcion("Base");
            } else {
                empresaobraconceptoglobal.setPbase("0");
                empresaobraconceptoglobal.setDescripcion("Versión" + (version + 1));
            }

            temp.add(empresaobraconceptoglobal);
        }

        addDatosGlobal(temp);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos insertados satisfactoriamente!");
        alert.showAndWait();

        datosResumen(obra.getId());

    }


    public DynamicReport createControlPresupReportGlobal(Integer version) throws ClassNotFoundException {

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
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);

        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(20);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(true)
                .setGrandTotalLegend("Grand Total")
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/report8LandScape.jrxml");


        if (version == 1) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();


            drb.addColumn(columnauo);
            drb.addColumn(presupBase);


        } else if (version == 2) {

            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();


            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);

        } else if (version == 3) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();


            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
        } else if (version == 4) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
        } else if (version == 5) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);


        } else if (version == 6) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version5 = ColumnBuilder.getNew()
                    .setColumnProperty("v5", Double.class.getName())
                    .setTitle("Versión 6").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);
            drb.addColumn(Version5);
        } else if (version == 7) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version5 = ColumnBuilder.getNew()
                    .setColumnProperty("v5", Double.class.getName())
                    .setTitle("Versión 6").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version6 = ColumnBuilder.getNew()
                    .setColumnProperty("v6", Double.class.getName())
                    .setTitle("Versión 7").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();


            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);
            drb.addColumn(Version5);
            drb.addColumn(Version6);
        } else if (version == 8) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version5 = ColumnBuilder.getNew()
                    .setColumnProperty("v5", Double.class.getName())
                    .setTitle("Versión 6").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version6 = ColumnBuilder.getNew()
                    .setColumnProperty("v6", Double.class.getName())
                    .setTitle("Versión 7").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version7 = ColumnBuilder.getNew()
                    .setColumnProperty("v7", Double.class.getName())
                    .setTitle("Versión 8").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);
            drb.addColumn(Version5);
            drb.addColumn(Version6);
            drb.addColumn(Version7);
        } else if (version == 9) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version5 = ColumnBuilder.getNew()
                    .setColumnProperty("v5", Double.class.getName())
                    .setTitle("Versión 6").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version6 = ColumnBuilder.getNew()
                    .setColumnProperty("v6", Double.class.getName())
                    .setTitle("Versión 7").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version7 = ColumnBuilder.getNew()
                    .setColumnProperty("v7", Double.class.getName())
                    .setTitle("Versión 8").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version8 = ColumnBuilder.getNew()
                    .setColumnProperty("v8", Double.class.getName())
                    .setTitle("Versión 9").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);
            drb.addColumn(Version5);
            drb.addColumn(Version6);
            drb.addColumn(Version7);
            drb.addColumn(Version8);
        } else if (version == 10) {
            AbstractColumn columnauo = ColumnBuilder.getNew()
                    .setColumnProperty("nameConcepts", String.class.getName())
                    .setTitle("Conceptos").setWidth(20)
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn presupBase = ColumnBuilder.getNew()
                    .setColumnProperty("pb", Double.class.getName())
                    .setTitle("P. Base").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version1 = ColumnBuilder.getNew()
                    .setColumnProperty("v1", Double.class.getName())
                    .setTitle("Versión 2").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version2 = ColumnBuilder.getNew()
                    .setColumnProperty("v2", Double.class.getName())
                    .setTitle("Versión 3").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version3 = ColumnBuilder.getNew()
                    .setColumnProperty("v3", Double.class.getName())
                    .setTitle("Versión 4").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version4 = ColumnBuilder.getNew()
                    .setColumnProperty("v4", Double.class.getName())
                    .setTitle("Versión 5").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version5 = ColumnBuilder.getNew()
                    .setColumnProperty("v5", Double.class.getName())
                    .setTitle("Versión 6").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version6 = ColumnBuilder.getNew()
                    .setColumnProperty("v6", Double.class.getName())
                    .setTitle("Versión 7").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version7 = ColumnBuilder.getNew()
                    .setColumnProperty("v7", Double.class.getName())
                    .setTitle("Versión 8").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version8 = ColumnBuilder.getNew()
                    .setColumnProperty("v8", Double.class.getName())
                    .setTitle("Versión 9").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            AbstractColumn Version9 = ColumnBuilder.getNew()
                    .setColumnProperty("v9", Double.class.getName())
                    .setTitle("Versión 10").setWidth(10).setPattern("$ 0.00")
                    .setStyle(detailStyle).setHeaderStyle(headerStyle)
                    .build();

            drb.addColumn(columnauo);
            drb.addColumn(presupBase);
            drb.addColumn(Version1);
            drb.addColumn(Version2);
            drb.addColumn(Version3);
            drb.addColumn(Version4);
            drb.addColumn(Version5);
            drb.addColumn(Version6);
            drb.addColumn(Version7);
            drb.addColumn(Version8);
            drb.addColumn(Version9);
        }

        drb.setUseFullPageWidth(true);
        DynamicReport dr = drb.build();


        return dr;


    }


    public void handleShowPresupVersions(ActionEvent event) {

        version = 0;
        version = noVersion(obra.getId());

        date = LocalDate.now();
        parametros = new HashMap<>();
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("reportName", "Versiones del Presupuesto");
        parametros.put("empresa", empresa.getNombre());
        parametros.put("comercial", empresa.getComercial());
        parametros.put("image", "templete/logoReport.jpg");

        globalReportModelArrayList = new ArrayList<ControlVersionResults>();
        globalReportModelArrayList = getGlobalReportModelArrayList(obra.getId(), version);

        try {
            if (version < 4) {
                DynamicReport dr2 = createControlPresupReportGlobal(version);
                JRDataSource ds2 = new JRBeanCollectionDataSource(globalReportModelArrayList);
                JasperReport jr2 = DynamicJasperHelper.generateJasperReport(dr2, new ClassicLayoutManager(), parametros);
                JasperPrint jp2 = JasperFillManager.fillReport(jr2, parametros, ds2);
                JasperViewer.viewReport(jp2, false);
            } else {

                DynamicReport dr2 = createControlPresupReportGlobal(version);
                JRDataSource ds2 = new JRBeanCollectionDataSource(globalReportModelArrayList);
                JasperReport jr2 = DynamicJasperHelper.generateJasperReport(dr2, new ClassicLayoutManager(), parametros);
                JasperPrint jp2 = JasperFillManager.fillReport(jr2, parametros, ds2);
                exportarExcel(jp2);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();

        }


    }


    public void exportarExcel(JasperPrint jp) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Reporte");
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

    public void handleNuevoJuego(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoJuego.fxml"));
            Parent proot = loader.load();
            NuevoJuegoController controller = loader.getController();
            controller.sendIdObra(obra.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadDatosJuegoProducto();
                datosJuego = FXCollections.observableArrayList();
                datosJuego = getJuegoProductoView(obra.getId());
                tableJuego.getItems().setAll(datosJuego);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void handleModificarJuego(ActionEvent event) {

        juegoProductoView = tableJuego.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposicionJuegoObra.fxml"));
            Parent proot = loader.load();
            ComposicionJuegoObraController composicionJuegoController = loader.getController();
            composicionJuegoController.pasarParametros(juegoProductoView, obra.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadDatosJuegoProducto();
                datosJuego = FXCollections.observableArrayList();
                datosJuego = getJuegoProductoView(obra.getId());
                tableJuego.getItems().setAll(datosJuego);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleNuevoSemielaborado(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoSemilebaorado.fxml"));
            Parent proot = loader.load();
            NuevoSemielaboradoController controller = loader.getController();
            controller.pasarParametros(obra.getId(), "R266");


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleShowSalario(ActionEvent event) {
        System.out.println("aqui entro yo");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GrupoEscalasEmpresaObra.fxml"));
            Parent proot = loader.load();
            GruposEscalasEmpresaObraController controller = loader.getController();
            var empresaContructora = empresaconstructoraArrayList.parallelStream().filter(item -> item.getId() == tableEmpresa.getSelectionModel().getSelectedItem().getId()).findFirst().get();
            controller.passDataToObra(obra, empresaContructora, obra.getTarifaSalarialByTarifa());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteEmpresaToObra(int idEmp, int idOb, int idSal) {

        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;


        try {
            trx = ompOsession.beginTransaction();
            Query query = ompOsession.createQuery("FROM Empresaobra WHERE empresaconstructoraId =: idEm AND obraId =: idObr");
            query.setParameter("idEm", idEmp);
            query.setParameter("idObr", idOb);

            Empresaobra empresaobra = (Empresaobra) query.getSingleResult();

            if (empresaobra != null) {
                ompOsession.delete(empresaobra);
            }

            Query query1 = ompOsession.createQuery("FROM Empresaobrasalario WHERE empresaconstructoraId =: idEm AND obraId =: idObr AND salarioId =: idS");
            query1.setParameter("idEm", idEmp);
            query1.setParameter("idObr", idOb);
            query1.setParameter("idS", idSal);

            Empresaobrasalario empresaobrasalario = (Empresaobrasalario) query.getSingleResult();

            if (empresaobrasalario != null) {
                ompOsession.delete(empresaobrasalario);
            }

            Query query2 = ompOsession.createQuery("FROM Empresaobraconcepto WHERE empresaconstructoraId =: idE AND obraId =: idO");
            query2.setParameter("idE", idEmp);
            query2.setParameter("idO", idOb);

            ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList = (ArrayList<Empresaobraconcepto>) ((org.hibernate.query.Query) query2).list();
            empresaobraconceptoArrayList.forEach(datos -> {
                ompOsession.delete(datos);
            });


            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }
    }

    public void handleQuitarObra(ActionEvent event) {

        if (tableEmpresa.getSelectionModel().getSelectedItem().getEmpresa().isSelected() == true) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(" Desea proseguir con la operación");

            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonAgregar) {
                deleteEmpresaToObra(tableEmpresa.getSelectionModel().getSelectedItem().getId(), obra.getId(), obra.getSalarioId());
            }
            empresaconstructoraArrayList = getListOfEmpresasInObra(obra.getId());
            empresaconstructoraArrayList.forEach(empresaconstructora -> {
                tableEmpresa.getItems().forEach(table -> {
                    if (table.getId() == empresaconstructora.getId()) {
                        table.getEmpresa().setSelected(true);
                    }
                });
            });
        }
    }

    public void handleCoeficientesEquipo(ActionEvent event) {
        var equipo = tableEquipos.getSelectionModel().getSelectedItem();
        var empresa = empresaconstructoraArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.getId() == Integer.parseInt(equipo.getIdEm())).findFirst().orElse(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarCoeficientesEquipos.fxml"));
            Parent proot = loader.load();
            ActualizarCoeficientesEquipoController controller = loader.getController();
            controller.pasarParametros(equipo, empresa, obra, tableEquipos.getItems());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleSuministrosPropioAction(ActionEvent event) {

        var suministroView = tableSum.getSelectionModel().getSelectedItem();

        if (suministroView.getPertene().trim().equals("R266") || suministroView.getPertene().trim().equals("I")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cambio de Suministros");
            alert.setContentText("Este suministro no se puede modificar, pues forma parte de la estructura de los renglones vaiantes");
            alert.showAndWait();
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateSuminitroBajo.fxml"));
                Parent proot = loader.load();
                ActualizarSuministroBajoController actualizarSuministroController = loader.getController();
                actualizarSuministroController.pasarParametros(actualizarObraController, suministroView);

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private List<Bajoespecificacion> datosBajo;
    private List<Bajoespecificacionrv> datosRV;

    public List<Bajoespecificacion> getBAjoBajoespecificacionList(int idSum) {

        Session session = ConnectionModel.createAppConnection().openSession();
        DecimalFormat df = new DecimalFormat("#0.00");

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            datosBajo = new ArrayList<>();
            datosBajo = session.createQuery(" FROM Bajoespecificacion WHERE idSuministro =: idS").setParameter("idS", idSum).getResultList();

            tx.commit();
            session.close();
            return datosBajo;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return datosBajo;
    }


    public void handleActionDeleteSuministro(ActionEvent event) {

        if (!utilCalcSingelton.getBAjoBajoespecificacionList(tableSum.getSelectionModel().getSelectedItem().getId()) && !utilCalcSingelton.getBAjoBajoespecificacionrvList(tableSum.getSelectionModel().getSelectedItem().getId())) {
            deleteSuministroBajo(tableSum.getSelectionModel().getSelectedItem().getId());
            loadDatosSuministros();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(" El suministros a eliminar esta asociado a un determinado elemento del presupuesto!!!");
            alert.showAndWait();
        }

    }

    private void deleteSuministroBajo(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Recursos suministro = session.get(Recursos.class, id);
            session.delete(suministro);
            tx.commit();
            session.close();

        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Error al eliminar el suministros!!");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }


    public void handleSearchOptions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LocalizarComponentsView.fxml"));
            Parent proot = loader.load();

            LocalizarOptionsController controller = loader.getController();
            controller.pasarIdObra(obra.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleCambiarSuministrosRenglones(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cambiarSuministros.fxml"));
            Parent proot = loader.load();

            CambiarSuminstrosController controller = loader.getController();
            controller.pasarDatos(obra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleActualizarObra(ActionEvent event) {

        String cod = field_codigo.getText();
        String desc = text_descripcion.getText();
        String[] partInv = combo_inversionista.getValue().split(" - ");
        String[] partEnt = combo_entidad.getValue().split(" - ");
        String[] partTipoO = combo_tipoObra.getValue().split(" - ");

        Inversionista inv = inversionistaArrayList.parallelStream().filter(inversionista -> inversionista.getCodigo().trim().equals(partInv[0].trim())).findFirst().orElse(null);
        Entidad ent = entidadArrayList.parallelStream().filter(entidad -> entidad.getCodigo().trim().equals(partEnt[0].trim())).findFirst().orElse(null);
        Tipoobra tipo = tipoobrasArrayList.parallelStream().filter(tipoobra -> tipoobra.getCodigo().trim().equals(partTipoO[0].trim())).findFirst().orElse(null);

        updatedatosObra(obra, cod, desc, inv, ent, tipo);

        int idO = obra.getId();
        obra = null;
        obra = getObra(idO);
        labelObraName.setText(" ");
        labelObraName.setText(obra.getCodigo() + " - " + obra.getDescripion());

        field_codigo.clear();
        text_descripcion.clear();
        field_codigo.setText(obra.getCodigo());
        text_descripcion.setText(obra.getDescripion());

        combo_entidad.getSelectionModel().select(ent.getCodigo() + " - " + ent.getDescripcion());
        combo_inversionista.getSelectionModel().select(inv.getCodigo() + " - " + inv.getDescripcion());
        combo_tipoObra.getSelectionModel().select(tipo.getCodigo() + " - " + tipo.getDescripcion());

    }

    private void updatedatosObra(Obra obraC, String cod, String desc, Inversionista inv, Entidad ent, Tipoobra tipo) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Obra obraUpdate = (Obra) session.createQuery("FROM Obra WHERE id =:idO").setParameter("idO", obraC.getId()).getSingleResult();
            obraUpdate.setCodigo(cod);
            obraUpdate.setDescripion(desc);
            obraUpdate.setEntidadId(ent.getId());
            obraUpdate.setInversionistaId(inv.getId());
            obraUpdate.setTipoobraId(tipo.getId());
            session.update(obraUpdate);

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            // he.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error al Actualizar");
            alert.setContentText(he.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    private int count1;
    private int batchSize1 = 10;

    public void insertEmpresaobraconceptoscoeficientes(List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoefList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count1 = 0;
            for (Empresaobraconceptoscoeficientes datos : empresaobraconceptoscoefList) {
                count1++;
                if (count1 > 0 && count1 % batchSize1 == 0) {
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

    public void handleExportToExcel(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Datos");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 1;

            for (SuministrosView cl : tableSum.getItems()) {
                data.put("1", new Object[]{"Código", "Descripción", "U/M", "Precio"});

                countrow++;
                Object[] datoscl = new Object[4];
                datoscl[0] = cl.getCodigo();
                datoscl[1] = cl.getDescripcion();
                datoscl[2] = cl.getUm();
                datoscl[3] = cl.getPreciomn();
                data.put(String.valueOf(countrow++), datoscl);
            }
            Set<String> keySet = data.keySet();
            int rownum = 0;
            for (String key : keySet) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;

                for (Object obj : objects) {
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum++);
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
        }
    }


}


