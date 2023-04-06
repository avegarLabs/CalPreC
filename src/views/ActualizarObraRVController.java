package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.Tuple;
import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ActualizarObraRVController implements Initializable {

    private static SessionFactory sf;
    public ActualizarObraRVController actualizarObraRVController;
    Task tarea;
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
    private ObservableList<String> listEntidad;
    private ObservableList<String> listInversionista;
    private ObservableList<String> listTipoObra;
    private ObservableList<String> listSalario;
    private ArrayList<Entidad> entidadArrayList;
    private ArrayList<Integer> empresasIdInObrasList;
    private ArrayList<Inversionista> inversionistaArrayList;
    private ArrayList<Empresaobra> empresaobraArrayList;
    private ArrayList<Tipoobra> tipoobrasArrayList;
    private ArrayList<Salario> salarioArrayList;
    private ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    private ObservableList<NewObrasFormTable> obrasFormTableObservableList;
    private NewObrasFormTable newObrasFormTable;
    private ArrayList<Renglonjuego> renglonjuegosArrayList;
    private ObservableList<JuegoProductoView> juegoProductoViewObservableList;
    private JuegoProductoView juegoProductoView;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ObservableList<SuministrosSemielaboradosView> suministrosSemielaboradosViewObservableList;
    private SuministrosSemielaboradosView semielaboradosView;
    private ArrayList<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<ObraRenglonesEmpresaView> renglonVarianteViewObservableList;
    private ObraRenglonesEmpresaView renglonVarianteView;
    private JFXCheckBox checkBox;
    private ObservableList<NewObrasFormTable> datos;
    private ObservableList<SuministrosView> datosSuministros;
    private ObservableList<SuministrosView> datosSumistrosRV;
    private ObservableList<ManoObraView> datosMano;
    private ObservableList<EquiposView> datosEquipos;
    private ObservableList<JuegoProductoView> datosJuego;
    private ObservableList<SuministrosSemielaboradosView> datosSemielaborados;
    private ObservableList<ObraRenglonesEmpresaView> datosRenglonVariante;
    private ArrayList<Nivelespecifico> unidadobraArrayList;
    private ArrayList<Renglonnivelespecifico> unidadobrarenglonsArrayList;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ObservableList<ManoObraView> manoObraViewObservableList;
    private ManoObraView manoObraView;
    private ObservableList<EquiposView> equiposViewObservableList;
    private EquiposView equipos;
    private ArrayList<Semielaboradosrecursos> semielaboradosrecursosArrayList;
    private ArrayList<Juegorecursos> juegorecursosArrayList;
    private ArrayList<Zonas> zonasArrayList;
    private ArrayList<Objetos> objetosArrayList;
    private ArrayList<Nivel> nivelArrayList;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private Subespecialidades subespecialidades;
    private Map parametros;
    private LocalDate date;
    @FXML
    private JFXTreeView treeView;
    private TreeItem treeItem;
    private Empresa empresa;
    private Date fecha;
    private Boolean flag;
    private Integer version;
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
    public Obra obra;
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
    private StringBuilder queryBuilder;
    private int count;
    private int batchSize = 10;
    private ArrayList<Empresaobraconceptoglobal> temp;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public String getConceptName(int idConcept) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Conceptosgasto conceptosgasto = session.get(Conceptosgasto.class, idConcept);
            if (conceptosgasto != null) {
                conceptName = conceptosgasto.getDescripcion();
            }

            tx.commit();
            session.close();
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
                if (global.getConceptoId() == 20) {
                    controlPresupResumenViews.add(new ControlPresupResumenView(global.getDescripcion(), Math.round(global.getValor()) * 1000d / 1000d, global.getFecha().toString()));
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


        listEntidad = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            entidadArrayList = (ArrayList<Entidad>) session.createQuery("FROM Entidad ").list();
            entidadArrayList.forEach(entidad -> {
                listEntidad.add(entidad.getCodigo() + " - " + entidad.getDescripcion());
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listEntidad;
    }

    public ObservableList<String> getListInversionista() {


        listInversionista = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            inversionistaArrayList = (ArrayList<Inversionista>) session.createQuery("FROM Inversionista ").list();
            inversionistaArrayList.forEach(inversionista -> {
                listInversionista.add(inversionista.getCodigo() + " - " + inversionista.getDescripcion());
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listInversionista;
    }

    public ObservableList<String> getListTipoObra() {


        listTipoObra = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            tipoobrasArrayList = (ArrayList<Tipoobra>) session.createQuery("FROM Tipoobra ").list();
            tipoobrasArrayList.forEach(tipoObra -> {
                listTipoObra.add(tipoObra.getCodigo() + " - " + tipoObra.getDescripcion());
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listTipoObra;
    }

    public ObservableList<String> getListSalario() {


        listSalario = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            salarioArrayList = (ArrayList<Salario>) session.createQuery("FROM Salario ").list();
            salarioArrayList.forEach(salario -> {
                listSalario.add(salario.getDescripcion());
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listSalario;
    }

    public ObservableList<NewObrasFormTable> getListEmpresas() {


        obrasFormTableObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            empresaconstructoraArrayList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            empresaconstructoraArrayList.forEach(empresas -> {
                checkBox = new JFXCheckBox();
                checkBox.setText(empresas.getCodigo());
                newObrasFormTable = new NewObrasFormTable(empresas.getId(), checkBox, empresas.getDescripcion());
                obrasFormTableObservableList.add(newObrasFormTable);
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obrasFormTableObservableList;
    }

    public ArrayList<Empresaconstructora> getListOfEmpresasInObra(int idObra) {
        empresaconstructoraArrayList = new ArrayList<Empresaconstructora>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Empresaobra WHERE obraId =: idOb ");
            query.setParameter("idOb", idObra);

            ArrayList<Empresaobra> empresaobraArrayList = (ArrayList<Empresaobra>) ((org.hibernate.query.Query) query).list();
            empresaobraArrayList.forEach(empresaobra -> {

                Empresaconstructora empresaconstructora = session.get(Empresaconstructora.class, empresaobra.getEmpresaconstructoraId());

                empresaconstructoraArrayList.add(empresaconstructora);
            });


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


        return empresaconstructoraArrayList;
    }

    public Obra getObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            obra = session.get(Obra.class, id);


            trx.commit();
            session.close();
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

    private String numberPatterns = "[+-]?\\d*(\\.\\d+)?";

    private List<SuministrosView> listRecBajoEsp(int idO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            List<SuministrosView> recursosBajoList = new ArrayList<>();
            List<Tuple> datosList = session.createQuery("SELECT rec.id, rec.codigo, rec.descripcion, rec.um, rec.peso, rec.preciomn, rec.preciomlc, rec.tipo, rec.pertenece FROM Bajoespecificacionrv bajo INNER JOIN Nivelespecifico uo ON bajo.nivelespecificoId = uo.id INNER JOIN Recursos rec ON bajo.idsuministro = rec.id WHERE uo.obraId =: id and rec.pertenece !=: idPer GROUP BY rec.id, rec.codigo, rec.descripcion, rec.um, rec.peso, rec.preciomn, rec.preciomlc, rec.tipo", Tuple.class).setParameter("id", idO).setParameter("idPer", String.valueOf(idO)).getResultList();
            for (Tuple tuple : datosList) {
                double val = 0.0;
                String code = tuple.get(1).toString().trim();
                if (tuple.get(6) == null) {
                    val = 0.0;
                } else {
                    val = Double.parseDouble(tuple.get(6).toString());
                }
                if (tuple.get(8).toString().trim().matches(numberPatterns)) {
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


    private double getImporteEscala(String grupo) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salario = session.get(Salario.class, 1);
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
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return importe;
    }

    public ObservableList<SuministrosView> getSuministrosRV(int id) {

        suministrosViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivelespecifico WHERE obraId =: idObra AND costomaterial >: valor");
            query.setParameter("idObra", id);
            query.setParameter("valor", 0.0);

            unidadobraArrayList = (ArrayList<Nivelespecifico>) ((org.hibernate.query.Query) query).list();
            unidadobraArrayList.forEach(unidadobra -> {
                Query query1 = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.um, rc.peso, rc.preciomn, rc.preciomlc, rc.grupoescala, rc.tipo, SUM(bajo.cantidad), SUM(bajo.costo) FROM Renglonnivelespecifico ur INNER JOIN Bajoespecificacionrv bajo ON ur.nivelespecificoId = bajo.nivelespecificoId INNER JOIN Recursos rc ON bajo.idsuministro = rc.id WHERE ur.nivelespecificoId =: idUO AND ur.conmat =: val group by rc.id, rc.codigo, rc.descripcion, rc.um, rc.peso, rc.preciomn, rc.preciomlc, rc.grupoescala, rc.tipo");

                query1.setParameter("idUO", unidadobra.getId());
                query1.setParameter("val", "0");

                for (Iterator it = ((org.hibernate.query.Query) query1).iterate(); it.hasNext(); ) {
                    Object[] row = (Object[]) it.next();
                    String idrc = row[0].toString();
                    String code = row[1].toString();
                    String descrip = row[2].toString();
                    String um = row[3].toString();
                    String peso = row[4].toString();
                    String preciomn = row[5].toString();
                    String preciomlc = row[6].toString();
                    String grupo = row[7].toString();
                    String tipo = row[8].toString();

                    suministros = new SuministrosView(Integer.parseInt(idrc), code, descrip, um, Double.parseDouble(peso), Double.parseDouble(preciomn), Double.parseDouble(preciomlc), grupo, tipo, "I", 0.0, utilCalcSingelton.createCheckBox(1));
                    suministrosViewObservableList.add(suministros);

                }


            });

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrosViewObservableList;
    }


    public ObservableList<ManoObraView> getManoObra(int id) {

        manoObraViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();


            Query query = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uo.obraId =: idOb AND rc.tipo =: tipo GROUP BY rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId");
            query.setParameter("idOb", id);
            query.setParameter("tipo", "2");

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idmano = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String grupo = row[3].toString();
                String precio = row[4].toString();
                String um = row[5].toString();

                importe = getImporteEscala(grupo);

                manoObraView = new ManoObraView(Integer.parseInt(idmano), code, descrip, grupo, Double.parseDouble(precio), importe, um, row[6].toString());

                manoObraViewObservableList.add(manoObraView);


            }

            manoObraViewObservableList.forEach(manoObraView1 -> {
                System.out.println(manoObraView1.getDescripcion());
            });


            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return manoObraViewObservableList;
    }

    /**
     * Datos de los Equipos
     */
    public ObservableList<EquiposView> getEquiposView(int id) {

        equiposViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            importe = 0.0;
            //recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getTipo(), recursos.getPreciomn(), recursos.getPreciomlc(), recursos.getUm()
            Query query = session.createQuery("SELECT rc.id, rc.codigo, rc.descripcion, rc.tipo, rc.preciomn, rc.preciomlc, rc.um, rc.grupoescala, uo.empresaconstructoraId FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rc ON rvr.recursosId = rc.id WHERE uo.obraId =: idOb AND rc.tipo =: tipo GROUP BY rc.id, rc.codigo, rc.descripcion, rc.grupoescala, rc.preciomn, rc.um, uo.empresaconstructoraId");
            query.setParameter("idOb", id);
            query.setParameter("tipo", "3");

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idequip = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String tipo = row[3].toString();
                String precio = row[4].toString();
                String preciomlc = row[5].toString();
                String um = row[6].toString();
                String grupo = row[7].toString();
                System.out.println(grupo);

                importe = getImporteEscala(grupo);
                equipos = new EquiposView(Integer.parseInt(idequip), code, descrip, tipo, Double.parseDouble(precio), Double.parseDouble(preciomlc), um, grupo, importe, row[8].toString());
                equiposViewObservableList.add(equipos);


            }

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return equiposViewObservableList;
    }

    /**
     * Datos de los juego de Productos
     */
    public ObservableList<JuegoProductoView> getJuegoProductoView(int id) {

        juegoProductoViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();


            //juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPeso(), juegoproducto.getPreciomn(), juegoproducto.getPreciomlc()
            Query query = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenece FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico our ON uo.id = our.nivelespecificoId INNER JOIN Renglonjuego rj ON our.renglonvarianteId = rj.renglonvarianteId INNER JOIN Juegoproducto jp ON rj.juegoproductoId = jp.id WHERE uo.obraId =: idOb AND our.conmat = '1' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc");
            query.setParameter("idOb", id);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idjg = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String peso = row[4].toString();
                String precio = row[5].toString();
                String preciomlc = row[6].toString();

                juegoProductoView = new JuegoProductoView(Integer.parseInt(idjg), code, descrip, um, Double.parseDouble(peso), Double.parseDouble(precio), Double.parseDouble(preciomlc), row[7].toString());
                juegoProductoViewObservableList.add(juegoProductoView);
            }


            Query query1 = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenece FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico our ON uo.id = our.nivelespecificoId INNER JOIN Bajoespecificacionrv bajo ON uo.id = bajo.nivelespecificoId INNER JOIN Juegoproducto jp ON bajo.idsuministro = jp.id WHERE uo.obraId =:idOB AND our.conmat = '0' AND bajo.tipo = 'J' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc");
            query1.setParameter("idOB", id);

            for (Iterator it = ((org.hibernate.query.Query) query1).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String idjg = row[0].toString();
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                String peso = row[4].toString();
                String precio = row[5].toString();
                String preciomlc = row[6].toString();

                juegoProductoView = new JuegoProductoView(Integer.parseInt(idjg), code, descrip, um, Double.parseDouble(peso), Double.parseDouble(precio), Double.parseDouble(preciomlc), row[7].toString());
                juegoProductoViewObservableList.add(juegoProductoView);
            }

            Query query2 = session.createQuery("FROM Juegoproducto WHERE pertenece =: idObra");
            query2.setParameter("idObra", String.valueOf(id));

            ArrayList<Juegoproducto> juegoproductosArrayList = (ArrayList<Juegoproducto>) ((org.hibernate.query.Query) query2).list();
            juegoproductosArrayList.forEach(juegoproducto -> {
                juegoProductoView = new JuegoProductoView(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getDescripcion(), juegoproducto.getPeso(), juegoproducto.getPreciomn(), juegoproducto.getPreciomlc(), juegoproducto.getPertenece());
                juegoProductoViewObservableList.add(juegoProductoView);
            });


            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return juegoProductoViewObservableList;
    }

    /**
     * Datos de los suministros semielaborados
     */
    public ObservableList<SuministrosSemielaboradosView> getSuministrosSemielaboradosView(int id) {

        suministrosSemielaboradosViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            List<Object[]> dataList = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenec FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico our ON uo.id = our.nivelespecificoId INNER JOIN Renglonjuego rj ON our.renglonvarianteId = rj.renglonvarianteId INNER JOIN Suministrossemielaborados jp ON rj.juegoproductoId = jp.id WHERE uo.obraId =: idOb AND our.conmat = '1' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOb", id).getResultList();
            for (Object[] row : dataList) {
                suministrosSemielaboradosViewObservableList.add(new SuministrosSemielaboradosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[6].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, row[7].toString()));

            }

            List<Object[]> dataList2 = session.createQuery("SELECT jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc, jp.pertenec FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico our ON uo.id = our.nivelespecificoId INNER JOIN Bajoespecificacionrv bajo ON uo.id = bajo.idsuministro INNER JOIN Suministrossemielaborados jp ON bajo.idsuministro = jp.id WHERE uo.obraId =:idOB AND our.conmat = '0' AND bajo.tipo = 'S' GROUP BY jp.id, jp.codigo, jp.descripcion, jp.um, jp.peso, jp.preciomn, jp.preciomlc").setParameter("idOB", id).getResultList();
            for (Object[] row : dataList2) {
                suministrosSemielaboradosViewObservableList.add(new SuministrosSemielaboradosView(Integer.parseInt(row[0].toString()), row[1].toString(), row[2].toString(), row[3].toString(), Math.round(Double.parseDouble(row[5].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[6].toString()) * 100d) / 100d, Math.round(Double.parseDouble(row[4].toString()) * 100d) / 100d, row[7].toString()));
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

    /**
     * Datos de los Renglones variantes
     */
    public ObservableList<ObraRenglonesEmpresaView> getDatosRenglonVariante(int id) {

        renglonVarianteViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("SELECT ec.codigo, ec.descripcion, rv.codigo, rv.descripcion, rv.um, SUM(uor.cantidad), SUM(uor.costmaterial), SUM(uor.costmano), SUM(uor.costequipo) FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id INNER JOIN Empresaconstructora ec ON uo.empresaconstructoraId = ec.id WHERE uo.obraId =: idOb GROUP BY rv.id, ec.codigo, ec.descripcion, rv.codigo, rv.descripcion, rv.um");
            query.setParameter("idOb", id);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                String ecCodigo = row[0].toString();
                String ecDescripcion = row[1].toString();
                String rvCodigo = row[2].toString();
                String rvDescripcion = row[3].toString();
                String rvUM = row[4].toString();
                String rvCant = row[5].toString();
                String rvMat = row[6].toString();
                String rvMano = row[7].toString();
                String rvEqui = row[8].toString();

                double total = Double.parseDouble(rvMat) + Double.parseDouble(rvMano) + Double.parseDouble(rvEqui);

                renglonVarianteView = new ObraRenglonesEmpresaView(ecCodigo, ecDescripcion, rvCodigo, rvDescripcion, rvUM, Double.parseDouble(rvCant), Double.parseDouble(rvMat), Double.parseDouble(rvMano), Double.parseDouble(rvEqui), Math.round(total * 100d) / 100d);

                renglonVarianteViewObservableList.add(renglonVarianteView);


            }

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonVarianteViewObservableList;
    }

    /**
     * Valores de todsa los indicadores por empresas en la obra solicitada
     */
    public ArrayList<Empresaobraconcepto> getDatosEmpresaObra(int idObra) {

        empresaobraconceptoArrayList = new ArrayList<Empresaobraconcepto>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Empresaobraconcepto WHERE obraId =: idOb ORDER BY conceptosgastoId");
            query.setParameter("idOb", idObra);
            empresaobraconceptoArrayList = (ArrayList<Empresaobraconcepto>) ((org.hibernate.query.Query) query).list();


            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaobraconceptoArrayList;
    }

    /**
     * Para el calculo de los indicadores de gastos
     */
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

        empresaobraconceptoglobalArrayList = new ArrayList<PresupControlModel>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            List<Object[]> datosPresupuesto = session.createQuery("SELECT eoc.obraId, eoc.conceptosgastoId, SUM(eoc.valor) FROM Empresaobraconcepto eoc WHERE eoc.obraId =: idob GROUP BY eoc.obraId, eoc.conceptosgastoId order by eoc.conceptosgastoId").setParameter("idob", idObra).getResultList();
            for (Object[] row : datosPresupuesto) {
                if (row[0] == null) {
                    empresaobraconceptoglobalArrayList.add(new PresupControlModel(0, 0, 0.0));
                } else if (row[0] != null) {
                    String idobra = row[0].toString();
                    String idConcepto = row[1].toString();
                    String valor = row[2].toString();
                    empresaobraconceptoglobalArrayList.add(new PresupControlModel(Integer.parseInt(idobra), Integer.parseInt(idConcepto), Double.parseDouble(valor)));
                }

            }

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaobraconceptoglobalArrayList;
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
        subespecialidadesArrayList = new ArrayList<Subespecialidades>();
        ArrayList<TreeItem> items = new ArrayList<TreeItem>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivelespecifico WHERE obraId =: idOb");
            query.setParameter("idOb", idObra);

            unidadobraArrayList = (ArrayList<Nivelespecifico>) ((org.hibernate.query.Query) query).list();

            unidadobraArrayList.forEach(unidadobra -> {
                Subespecialidades subespecialidades = session.get(Subespecialidades.class, unidadobra.getSubespecialidadesId());

                subespecialidadesArrayList.add(subespecialidades);

            });

            HashSet<Subespecialidades> hs = new HashSet<>();
            hs.addAll(subespecialidadesArrayList);

            subespecialidadesArrayList.clear();
            subespecialidadesArrayList.addAll(hs);

            subespecialidadesArrayList.forEach(subespecialidades -> {
                TreeItem<String> itemSub = new TreeItem(subespecialidades.getDescripcion());
                items.add(itemSub);
            });


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

            Query query = session.createQuery("FROM Subespecialidades WHERE descripcion =: descrip");
            query.setParameter("descrip", description);
            subespecialidades = (Subespecialidades) query.getSingleResult();


            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return subespecialidades;
    }

    private ObservableList<UOSubespecialidadesValoresView> getUniddadporSubE(int idObr, int idSub) {

        uoSubespecialidadesValoresViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivelespecifico WHERE obraId =: idOb AND subespecialidadesId =: idSE");
            query.setParameter("idOb", idObr);
            query.setParameter("idSE", idSub);

            unidadobraArrayList = (ArrayList<Nivelespecifico>) ((org.hibernate.query.Query) query).list();

            unidadobraArrayList.forEach(unidadobra -> {
                subespecialidadesValoresView = new UOSubespecialidadesValoresView(unidadobra.getId(), unidadobra.getCodigo() + " - " + unidadobra.getDescripcion(), " ", 0.0, unidadobra.getCostomaterial(), unidadobra.getCostomano(), unidadobra.getCostoequipo());
                uoSubespecialidadesValoresViewObservableList.add(subespecialidadesValoresView);

            });


            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return uoSubespecialidadesValoresViewObservableList;
    }

    public ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    private Salario salarioVal;
    private Empresaobrasalario empresaobrasalario;
    private Salario salario;
    private int count1;
    private int batchSize1 = 10;
    private List<Empresagastos> empresagastosList;
    private List<Empresaobraconceptoscoeficientes> empresaobraconceptoscoeficientesList;

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

    public void loadDatosSuministros() {

        sumCode.setCellValueFactory(new PropertyValueFactory<SuministrosView, String>("codigo"));
        sumDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        sumUm.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        sumMn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        // sumMlc.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomlc"));

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

    public void loadDatosSuministrosRV() {

        sumRvCode.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        sumRVDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        sumRvUm.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        sumRvMn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        // sumRvMlc.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomlc"));


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
        // juegoMlc.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("preciomlc"));
    }

    public void loadDatosSemielaborados() {
        semiCode.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("codigo"));
        semiDescrip.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("descripcion"));
        semiUm.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("um"));
        semiMn.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomn"));
        //  semiMlc.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomlc"));
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
                Thread.sleep(2000);
                return true;
            }
        };
    }

    public void AddNuevaObraEmpresaSalario(Empresaobrasalario empresaobrasalario) {
        Session ompOsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = ompOsession.beginTransaction();
            ompOsession.persist(empresaobrasalario);
            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empresa = getEmpresa();
        actualizarObraRVController = this;

        loadDatosEmpresas();
        datos = FXCollections.observableArrayList();
        datos = getListEmpresas();
        tableEmpresa.getItems().setAll(datos);
        combo_entidad.setItems(getEntidadesList());
        combo_inversionista.setItems(getListInversionista());
        combo_tipoObra.setItems(getListTipoObra());
        combo_Salario.setItems(getListSalario());
        // checkSum.setSelected(true);

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


                empresaconstructoraArrayList = getListOfEmpresasInObra(obra.getId());
                empresaconstructoraArrayList.forEach(empresaconstructora -> {
                    tableEmpresa.getItems().forEach(table -> {

                        if (table.getId() == empresaconstructora.getId()) {

                            table.getEmpresa().setSelected(true);
                        }
                    });

                });
            }
        });


        TableCostoUnidades.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && TableCostoUnidades.getSelectionModel() != null) {
                subespecialidadesValoresView = TableCostoUnidades.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DesgloseRV.fxml"));
                    Parent proot = loader.load();
                    DesgloseRVController controller = loader.getController();
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
                        System.out.println(salario);
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

    public void pasarDatosObra(ObrasView obrasView) throws Exception {

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

        empresaobraconceptoArrayList = new ArrayList<Empresaobraconcepto>();
        empresaobraconceptoArrayList = getDatosEmpresaObra(obra.getId());

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

        var globalc20 = getSumDatosGlobal(obra.getId()).parallelStream().filter(g -> g.getConceptoId() == 20).findFirst().orElse(null);
        if (globalc20 == null) {
            labelMonto.setText(String.valueOf(0.0));
        } else {
            labelMonto.setText(String.valueOf(globalc20.getValor()));
        }

        /**
         * para modificar los coeficientes y agregar los valores...
         */

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarSuminitrosRV.fxml"));
            Parent proot = loader.load();
            ImportarSuministrosRVController controller = loader.getController();
            controller.pasarParametros(actualizarObraRVController, obra.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DynamicReport createControlPresupReport() throws ClassNotFoundException {

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
        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
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
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegend("Grand Total")
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(false)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportObraRV.jrxml");


        AbstractColumn columnaemp = ColumnBuilder.getNew()
                .setColumnProperty("ecDescripcion", String.class.getName())
                .setTitle("Empresa Constructora").setWidth(50)
                .setStyle(titleStyle).setHeaderStyle(titleStyle)
                .build();

        AbstractColumn columnauo = ColumnBuilder.getNew()
                .setColumnProperty("rvCodigo", String.class.getName())
                .setTitle("Código").setWidth(10)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnadescrip = ColumnBuilder.getNew()
                .setColumnProperty("rvDescrip", String.class.getName())
                .setTitle("Descripción").setWidth(30)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaum = ColumnBuilder.getNew()
                .setColumnProperty("rvUM", String.class.getName())
                .setTitle("UM").setWidth(5)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaCantidad = ColumnBuilder.getNew()
                .setColumnProperty("rvCant", Double.class.getName())
                .setTitle("Cant.").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaCosto = ColumnBuilder.getNew()
                .setColumnProperty("rvTotal", Double.class.getName())
                .setTitle("Total").setWidth(15).setPattern("$ 0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
/*
        AbstractColumn columnaavence = ColumnBuilder.getNew()
                .setColumnProperty("avance", Double.class.getName())
                .setTitle("Avance").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnavalorEjecutado = ColumnBuilder.getNew()
                .setColumnProperty("valorEjecutado", Double.class.getName())
                .setTitle("Ejec($)").setWidth(10).setPattern("$ 0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn columnaValporEjec = ColumnBuilder.getNew()
                .setColumnProperty("porEjecutar", Double.class.getName())
                .setTitle("Disp($)").setWidth(15).setPattern("$ 0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn columnaPorcientoEJc = ColumnBuilder.getNew()
                .setColumnProperty("porciento", Double.class.getName())
                .setTitle("%").setWidth(10).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();
*/
        // drb.addGlobalHeaderVariable(columnAmount,
        // ColumnsGroupVariableOperation.SUM,headerVariables);
        //		drb.addGlobalHeaderVariable(columnaQuantity, ColumnsGroupVariableOperation.SUM,headerVariables);
        //		drb.addGlobalFooterVariable(columnAmount, ColumnsGroupVariableOperation.SUM,headerVariables);
        //		drb.addGlobalFooterVariable(columnaQuantity, ColumnsGroupVariableOperation.SUM,headerVariables);

        GroupBuilder gb1 = new GroupBuilder();

        //		 define the criteria column to group by (columnState)
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columnaemp)
                .addFooterVariable(columnaCosto, DJCalculation.SUM, headerVariables)
                .setGroupLayout(GroupLayout.VALUE_IN_HEADER_WITH_HEADERS)
                .build();

        GroupBuilder gb2 = new GroupBuilder(); // Create another group (using another column as criteria)
        DJGroup g2 = gb2.setCriteriaColumn((PropertyColumn) columnauo) // and we add the same operations for the columnAmount and
                .addFooterVariable(columnaCantidad, DJCalculation.SUM) // columnaQuantity columns
                // .addFooterVariable(columnaQuantity, DJCalculation.SUM)
                .build();

        drb.addColumn(columnaemp);
        drb.addColumn(columnauo);
        drb.addColumn(columnadescrip);
        drb.addColumn(columnaum);
        drb.addColumn(columnaCantidad);
        drb.addColumn(columnaCosto);
        /*
        drb.addColumn(columnaavence);
        drb.addColumn(columnavalorEjecutado);
        drb.addColumn(columnaValporEjec);
        drb.addColumn(columnaPorcientoEJc);
*/

        drb.addGroup(g1); // add group g1
        //		drb.addGroup(g2); // add group g2

        date = LocalDate.now();


        drb.setUseFullPageWidth(true);
        parametros = new HashMap<>();
        parametros.put("obraName", labelObraName.getText());
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("empresa", empresa.getNombre());
        parametros.put("comercial", empresa.getComercial());


        DynamicReport dr = drb.build();


        return dr;

    }

    public void handlePrintReportRV(ActionEvent event) {
        try {
            DynamicReport dr = createControlPresupReport();
            JRDataSource ds = new JRBeanCollectionDataSource(datosRenglonVariante);
            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
            JasperViewer.viewReport(jp);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
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
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegend("Grand Total")
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setOddRowBackgroundStyle(oddRowStyle)
                .setTemplateFile("templete/reportControlPresupuestosConcept.jrxml");


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

        date = LocalDate.now();
        parametros = new HashMap<>();
        parametros.put("obraName", obra.getDescripion());
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("empresa", empresa.getNombre());
        parametros.put("comercial", empresa.getComercial());

        globalReportModelArrayList = new ArrayList<ControlVersionResults>();
        globalReportModelArrayList = getGlobalReportModelArrayList(obra.getId(), version);


        try {

            DynamicReport dr2 = createControlPresupReportGlobal(version);
            JRDataSource ds2 = new JRBeanCollectionDataSource(globalReportModelArrayList);
            JasperReport jr2 = DynamicJasperHelper.generateJasperReport(dr2, new ClassicLayoutManager(), parametros);
            JasperPrint jp2 = JasperFillManager.fillReport(jr2, parametros, ds2);

            JasperViewer.viewReport(jp2);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();

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

    public void updateInversionista(Inversionista inversionista) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Session ompOsession = sf.openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();

            obra.setInversionistaId(inversionista.getId());
            ompOsession.update(obra);

            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    public void updateEntidad(Entidad entidad) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Session ompOsession = sf.openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();

            obra.setEntidadId(entidad.getId());
            ompOsession.update(obra);

            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }


    public void updateTipoObra(Tipoobra tipoobra) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Session ompOsession = sf.openSession();
        Transaction trx = null;

        try {
            trx = ompOsession.beginTransaction();

            obra.setTipoobraId(tipoobra.getId());
            ompOsession.update(obra);

            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            ompOsession.close();
        }

    }

    public void handleUpdateInversionista(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");

        alert.setContentText(" Esta seguro de establecer el cambio de inversionista de la obra ");

        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {

            String[] parI = combo_inversionista.getSelectionModel().getSelectedItem().split(" - ");
            Inversionista inversionista = inversionistaArrayList.stream().filter(inv -> inv.getCodigo().equals(parI[0])).findFirst().orElse(null);

            updateInversionista(inversionista);
        }

        if (result.get() == buttonTypeCancel) {

            combo_inversionista.getSelectionModel().select(obra.getInversionistaByInversionistaId().getCodigo() + " - " + obra.getInversionistaByInversionistaId().getDescripcion());
        }

    }

    public void handleUpdateEntidad(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");

        alert.setContentText(" Esta seguro de establecer el cambio de entidad de la obra ");

        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {

            String[] parI = combo_entidad.getValue().split(" - ");
            Entidad entidad = entidadArrayList.stream().filter(inv -> inv.getCodigo().equals(parI[0])).findFirst().orElse(null);

            updateEntidad(entidad);

            combo_entidad.getSelectionModel().clearSelection();
            combo_entidad.getSelectionModel().select(entidad.getCodigo() + " - " + entidad.getDescripcion());
        }

        if (result.get() == buttonTypeCancel) {

            combo_entidad.getSelectionModel().select(obra.getEntidadByEntidadId().getCodigo() + " - " + obra.getEntidadByEntidadId().getDescripcion());
        }
    }


    public void handleUpdateTipoObra(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");

        alert.setContentText(" Esta seguro de establecer el cambio del tipo de obra ");

        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonAgregar) {

            String[] parI = combo_tipoObra.getValue().split(" - ");
            Tipoobra tipo = tipoobrasArrayList.stream().filter(inv -> inv.getCodigo().equals(parI[0])).findFirst().orElse(null);

            updateTipoObra(tipo);

            combo_tipoObra.getSelectionModel().clearSelection();
            combo_tipoObra.getSelectionModel().select(tipo.getCodigo() + " - " + tipo.getDescripcion());
        }

        if (result.get() == buttonTypeCancel) {

            combo_tipoObra.getSelectionModel().select(obra.getTipoobraByTipoobraId().getCodigo() + " - " + obra.getTipoobraByTipoobraId().getDescripcion());
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

    public void handleSearchOptions(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LocalizarComponentsRVView.fxml"));
            Parent proot = loader.load();

            LocalizarOptionsRVController controller = loader.getController();
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

    private List<Bajoespecificacion> datosBajo;
    private List<Bajoespecificacionrv> datosRV;

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

    public void handleSuministrosPropioAction(ActionEvent event) {

        var suministroView = tableSum.getSelectionModel().getSelectedItem();

        if (suministroView.getPertene().trim().equals("R266") || suministroView.getPertene().trim().equals("I")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cambio de Suministros");
            alert.setContentText("Este suministro no se puede modificar, pues forma parte de la estructura de los renglones vaiantes");
            alert.showAndWait();
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("updateSuminitroRVBajo.fxml"));
                Parent proot = loader.load();
                ActualizarSuministroBajoRVController actualizarSuministroController = loader.getController();
                actualizarSuministroController.pasarParametros(actualizarObraRVController, suministroView);

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

    public List<Bajoespecificacionrv> getBAjoBajoespecificacionrvList(int idSum) {

        Session session = ConnectionModel.createAppConnection().openSession();
        DecimalFormat df = new DecimalFormat("#0.00");

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            datosRV = new ArrayList<>();
            datosRV = session.createQuery(" FROM Bajoespecificacionrv WHERE idsuministro =: idS").setParameter("idS", idSum).getResultList();

            tx.commit();
            session.close();
            return datosRV;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return datosRV;
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

    public void handleCambiarRecursos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CambioSuministroCertificacionVG.fxml"));
            Parent proot = loader.load();
            CambioSuministroCertificacionVGController controller = loader.getController();
            controller.cargarDatostoChange(tableSum.getSelectionModel().getSelectedItem().getId(), obra.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
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


}


