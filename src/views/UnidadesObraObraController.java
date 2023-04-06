package views;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UnidadesObraObraController implements Initializable {

    public UnidadesObraObraController unidadesObraObraController;
    @FXML
    public TextArea textDescrp;
    @FXML
    public Label labelUM;
    @FXML
    public Label labelCant;
    @FXML
    public Label labelcostU;
    @FXML
    public Label labelCosT;
    @FXML
    public Label labelSal;

    @FXML
    public TableView<UORVTableView> tableRenglones;
    @FXML
    public TableView<BajoEspecificacionView> tableSuministros;
    @FXML
    public Label labelRVCant;
    @FXML
    public Label labelPeso;
    @FXML
    public Label labelRVum;
    public double sumCostoTotal;
    public ArrayList<Empresagastos> empresagastosArrayList;
    public ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList;
    public ArrayList<Double> valoresGastDirectos;
    public ArrayList<Double> valoresGastDirectosFinal;
    public Obra obra;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private Label label_title;
    @FXML
    private JFXComboBox<String> coboEmpresas;
    @FXML
    private JFXComboBox<String> comboZonas;
    @FXML
    private JFXComboBox<String> comboObjetos;
    @FXML
    private JFXComboBox<String> comboNivel;
    @FXML
    private JFXComboBox<String> comboEspecialidad;
    @FXML
    private JFXComboBox<String> comboSubEspecialidad;
    @FXML
    private JFXButton addZonas;
    @FXML
    private JFXButton addObjetos;
    @FXML
    private JFXButton addNivel;
    @FXML
    private JFXButton addEspecialidad;
    @FXML
    private JFXButton addSubespecialidad;
    @FXML
    private JFXButton btnHide;
    @FXML
    private TableView<UniddaObraView> dataTable;
    @FXML
    private TableColumn<UniddaObraView, String> code;
    @FXML
    private TableColumn<UniddaObraView, StringProperty> descripcion;
    @FXML
    private Pane paneCalc;
    @FXML
    private TextArea labelUodescrip;
    @FXML
    private Label labelCode;
    @FXML
    private TableColumn<UORVTableView, String> RVcode;
    @FXML
    private TableColumn<UORVTableView, DoubleProperty> RVcant;
    @FXML
    private TableColumn<UORVTableView, DoubleProperty> RVCosto;
    @FXML
    private TableColumn<BajoEspecificacionView, StringProperty> SumCode;
    @FXML
    private TableColumn<BajoEspecificacionView, DoubleProperty> SumCant;
    @FXML
    private TableColumn<BajoEspecificacionView, DoubleProperty> SumCost;
    @FXML
    private JFXComboBox<String> comboRenglones;
    @FXML
    private Label labelTipoCost;
    @FXML
    private Label renbase;
    @FXML
    private JFXButton btnadd;
    @FXML
    private JFXButton btnimppcw;
    private ArrayList<Empresaobra> empresaobraArrayList;
    private ArrayList<Empresaconstructora> empresaArrayList;
    private ObservableList<String> empresasList;
    private Empresaconstructora empresaconstructoraModel;
    private ArrayList<Zonas> zonasArrayList;
    private ObservableList<String> zonaList;
    private Zonas zonasModel;
    private ArrayList<Objetos> objetosArrayList;
    private ObservableList<String> objetosList;
    private Objetos objetosModel;
    private ArrayList<Nivel> nivelArrayList;
    private ObservableList<String> nivelesList;
    private Nivel nivelModel;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ObservableList<String> especialidadesList;
    private Especialidades especialidadesModel;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private ObservableList<String> subEspecialiadesList;
    private Subespecialidades subespecialidadesModel;
    private List<Unidadobra> unidadobraArrayList;
    private ObservableList<UniddaObraView> unidadObraViewObservableList;
    private UniddaObraView unidadObraView;
    private int IdObra;
    private int IdRV;
    private ObservableList<UniddaObraView> datos;
    private boolean falg;
    private boolean falg1;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private double costosRV;
    private double CostoTotal;
    private double costoMaterial;
    private Double costoMatRV;
    private Double costomatBe;
    private Double costoMano;
    private Double costoEquipo;
    private double costo;
    private double costRV;
    private ObservableList<UORVTableView> uorvTableViewsList;
    private UORVTableView uorvTableView;
    private ObservableList<UORVTableView> datosRV;
    private String codeRB;
    private Unidadobra unidadobra;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ObservableList<ManoObraRenglonVariante> manoobraList;
    private double importe;
    private double cantUO;
    private double valGrupo;
    private double sal;
    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private ObservableList<BajoEspecificacionView> bajoEspecificacionViewObservableList;
    private BajoEspecificacionView bajoEspecificacionView;
    private ObservableList<BajoEspecificacionView> datosBE;
    private Unidadobrarenglon unidadobrarenglon;
    private Renglonvariante renglonBase;
    private ObraEmpresaNiveleEspecificosCostos datosCostos;
    private Empresaobraconcepto empresaobraconcepto;
    private int tipo;
    private HashSet filt = new HashSet();
    private Runtime garbache;
    private Double total;
    private int batchSize = 5;
    private int count;
    private ReportProjectStructureSingelton reportProjectStructureSingelton;
    private Map parametros;
    @FXML
    private ContextMenu uoMenu;
    @FXML
    private ContextMenu sumiMenu;
    @FXML
    private ContextMenu rvMenu;
    @FXML
    private JFXButton btnPlan;
    @FXML
    private JFXButton btnCertif;
    @FXML
    private JFXButton btnAfct;
    @FXML
    private JFXButton btnImCalp;
    private Obra viewObra;
    private UsuariosDAO usuariosDAO;

    public Obra getObra(int Id) {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            obra = session.get(Obra.class, Id);

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

    /**
     * Funcionalidades para llenar los elementos de la vista
     */

    public ObservableList<String> getListEmpresas(Obra obra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresasList = FXCollections.observableArrayList();
            empresaArrayList = new ArrayList<Empresaconstructora>();
            for (Empresaobra empresaobra : obra.getEmpresaobrasById()) {
                var unit = obra.getUnidadobrasById().parallelStream().filter(item -> item.getEmpresaconstructoraId() == empresaobra.getEmpresaconstructoraId()).findFirst().orElse(null);
                empresaArrayList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId());
                if (unit != null) {
                    empresasList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim() + "   ");
                } else {
                    empresasList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim());
                }
            }
            tx.commit();
            session.close();
            return empresasList;
        } catch (Exception he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(he.toString());
            alert.showAndWait();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Metodos para mostrar lis materiales suminitrados bajo especificacion a la unida de obra
     */

    public ObservableList<BajoEspecificacionView> getSumBajoEspecificacion(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            bajoEspecificacionViewObservableList = FXCollections.observableArrayList();
            bajoespecificacionArrayList = new ArrayList<>();
            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: id ").setParameter("id", idUO).getResultList();
            bajoespecificacionArrayList.sort(Comparator.comparing(Bajoespecificacion::getIdSuministro));
            for (Bajoespecificacion item : bajoespecificacionArrayList) {
                if (item.getTipo().trim().equals("1") || item.getTipo().trim().equals("2") || item.getTipo().trim().equals("3")) {
                    Recursos recursos = session.get(Recursos.class, item.getIdSuministro());
                    if (recursos != null) {
                        bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), new BigDecimal(String.format("%.2f", recursos.getPreciomn())).doubleValue(), recursos.getUm(), new BigDecimal(String.format("%.2f", recursos.getPeso())).doubleValue(), new BigDecimal(String.format("%.4f", item.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", item.getCosto())).doubleValue(), item.getTipo()));
                    }
                } else if (item.getTipo().trim().equals("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, item.getIdSuministro());
                    if (recursos != null) {
                        bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), new BigDecimal(String.format("%.2f", recursos.getPreciomn())).doubleValue(), recursos.getUm(), new BigDecimal(String.format("%.2f", recursos.getPeso())).doubleValue(), new BigDecimal(String.format("%.4f", item.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", item.getCosto())).doubleValue(), item.getTipo()));
                    }
                } else if (item.getTipo().trim().equals("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, item.getIdSuministro());
                    if (recursos != null) {
                        bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(item.getId(), item.getUnidadobraId(), item.getIdSuministro(), item.getRenglonvarianteId(), recursos.getCodigo(), recursos.getDescripcion(), new BigDecimal(String.format("%.2f", recursos.getPreciomn())).doubleValue(), recursos.getUm(), new BigDecimal(String.format("%.2f", recursos.getPeso())).doubleValue(), new BigDecimal(String.format("%.4f", item.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", item.getCosto())).doubleValue(), item.getTipo()));
                    }
                }
            }
            tx.commit();
            session.close();
            return bajoEspecificacionViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * Calculo del costo material de la unida de obra
     *
     * @param idUO
     * @return
     */

    public Double getCostoMaterial(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costRV = 0.0;
        costoMaterial = 0.0;
        costoMatRV = 0.0;
        costomatBe = 0.0;
        try {

            tx = session.beginTransaction();
            if (obra.getDefcostmat() == 1) {
                Query querymrv = session.createQuery("SELECT SUM(costMat) FROM Unidadobrarenglon where unidadobraId = :id ").setParameter("id", idUO);
                if (querymrv.getSingleResult() != null) {
                    costoMatRV = (Double) querymrv.getSingleResult();
                    costoMaterial = costoMatRV;
                }
            } else if (obra.getDefcostmat() == 0) {
                Query querybajo = session.createQuery(" SELECT SUM(costo) FROM Bajoespecificacion where unidadobraId = :id ").setParameter("id", idUO);
                if (querybajo.getSingleResult() != null) {
                    costomatBe = (Double) querybajo.getSingleResult();
                    costoMaterial = costomatBe;
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
        return costoMaterial;
    }

    /**
     * Calculo del costo de la mano de obra
     *
     * @param idUO
     * @return
     */
    public Double getCostoMano(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costoMano = 0.0;

        try {
            tx = session.beginTransaction();

            Query queryMano = session.createQuery("SELECT SUM(ur.costMano) FROM Unidadobra uo INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId WHERE uo.id =: unidad");
            queryMano.setParameter("unidad", idUO);

            if (queryMano.getSingleResult() != null) {
                costoMano = (Double) queryMano.getSingleResult();
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return Math.round(costoMano * 100d) / 100d;
    }

    /**
     * Calculo del costo de la mano de obra
     *
     * @param idUO
     * @return
     */
    public Double[] getCostosUnidad(int idUO) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Double[] datos = new Double[4];

        try {
            tx = session.beginTransaction();

            Unidadobra unid = session.get(Unidadobra.class, idUO);
            if (unid != null) {
                unid.getUnidadobrarenglonsById().size();
                datos[0] = unid.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMano).reduce(0.0, Double::sum);
                datos[1] = unid.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostEquip).reduce(0.0, Double::sum);
                datos[2] = unid.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getSalariomn).reduce(0.0, Double::sum);
                datos[3] = unid.getUnidadobrarenglonsById().parallelStream().map(Unidadobrarenglon::getCostMat).reduce(0.0, Double::sum);
            }
            tx.commit();
            session.close();
            return datos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return datos;
    }

    //Double.toString(new BigDecimal( String.format("%.2f", renglon.getCostmano())).doubleValue())
    public ObservableList<UORVTableView> getUoRVRelation(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            uorvTableViewsList = FXCollections.observableArrayList();
            unidadobrarenglonArrayList = (List<Unidadobrarenglon>) session.get(Unidadobra.class, unidadobra.getId()).getUnidadobrarenglonsById();
            unidadobrarenglonArrayList.sort(Comparator.comparing(Unidadobrarenglon::getRenglonvarianteId));
            for (Unidadobrarenglon renglonnivelespecifico : unidadobrarenglonArrayList) {
                if (renglonnivelespecifico.getConMat().trim().equals("1")) {
                    double total = renglonnivelespecifico.getCostMat() + renglonnivelespecifico.getCostMano() + renglonnivelespecifico.getCostEquip();
                    uorvTableViewsList.add(new UORVTableView(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getId(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo().trim().concat("  "), new BigDecimal(String.format("%.4f", renglonnivelespecifico.getCantRv())).doubleValue(), new BigDecimal(String.format("%.2f", total)).doubleValue(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getPeso(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getUm(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostomat() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostmano() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostequip(), "1", sal, 0.0, renglonnivelespecifico.getConMat(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getDescripcionsub()));
                } else if (renglonnivelespecifico.getConMat().trim().equals("0")) {
                    double total = renglonnivelespecifico.getCostMat() + renglonnivelespecifico.getCostMano() + renglonnivelespecifico.getCostEquip();
                    uorvTableViewsList.add(new UORVTableView(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getId(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo().trim(), new BigDecimal(String.format("%.4f", renglonnivelespecifico.getCantRv())).doubleValue(), new BigDecimal(String.format("%.2f", total)).doubleValue(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getPeso(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getUm(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostmano() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostequip(), "1", sal, 0.0, renglonnivelespecifico.getConMat(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getDescripcionsub()));
                }
            }
            for (UORVTableView tableView : uorvTableViewsList) {
                if (tableView.getCodeRV().trim().equals(unidadobra.getRenglonbase())) {
                    tableView.setCodeRV("*" + tableView.getCodeRV());
                }
            }
            tx.commit();
            session.close();
            return uorvTableViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListZonas(int emp) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonaList = FXCollections.observableArrayList();
            zonasArrayList = (ArrayList<Zonas>) session.createQuery("FROM Zonas where obraId = :id ").setParameter("id", obra.getId()).getResultList();
            zonasArrayList.sort(Comparator.comparing(Zonas::getCodigo));
            System.out.println(zonasArrayList.size());
            for (Zonas zon : zonasArrayList) {
                var zonUnit = obra.getUnidadobrasById().parallelStream().filter(u -> u.getEmpresaconstructoraId() == emp && u.getZonasId() == zon.getId()).findFirst().orElse(null);
                if (zonUnit != null) {
                    zonaList.add(zon.toString().trim() + "   ");
                } else {
                    zonaList.add(zon.toString().trim());
                }

            }
            tx.commit();
            session.close();
            return zonaList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getListObjetos(int idZona, int emp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosList = FXCollections.observableArrayList();
            objetosArrayList = new ArrayList<>();
            objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos where zonasId = :id ").setParameter("id", idZona).getResultList();
            objetosArrayList.sort(Comparator.comparing(Objetos::getCodigo));
            System.out.println(objetosArrayList.size());
            for (Objetos objetos : objetosArrayList) {
                System.out.println(objetos.getCodigo());
                var unitObj = obra.getUnidadobrasById().parallelStream().filter(u -> u.getEmpresaconstructoraId() == emp && u.getObjetosId() == objetos.getId()).findFirst().orElse(null);
                if (unitObj != null) {
                    objetosList.add(objetos.toString().trim() + "   ");
                } else {
                    objetosList.add(objetos.toString().trim());
                }
            }

            tx.commit();
            session.close();
            return objetosList;
        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getNivelList(int id, int emp, int zon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelesList = FXCollections.observableArrayList();
            nivelArrayList = (ArrayList<Nivel>) session.createQuery("FROM Nivel where objetosId = :id ").setParameter("id", id).getResultList();
            nivelArrayList.sort(Comparator.comparing(Nivel::getCodigo));
            for (Nivel nivel : nivelArrayList) {
                var nivUnit = obra.getUnidadobrasById().parallelStream().filter(u -> u.getEmpresaconstructoraId() == emp && u.getZonasId() == zon && u.getNivelId() == nivel.getId()).findFirst().orElse(null);
                if (nivUnit != null) {
                    nivelesList.add(nivel.toString().trim() + "   ");
                } else {
                    nivelesList.add(nivel.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return nivelesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getEspecialidades(int emp, int zon, int obj, int niv) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesList = FXCollections.observableArrayList();
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("from Especialidades ").getResultList();
            especialidadesArrayList.sort(Comparator.comparing(Especialidades::getCodigo));
            for (Especialidades especialiades : especialidadesArrayList) {
                var espUnit = obra.getUnidadobrasById().parallelStream().filter(u -> u.getEmpresaconstructoraId() == emp && u.getZonasId() == zon && u.getObjetosId() == obj && u.getNivelId() == niv && u.getEspecialidadesId() == especialiades.getId()).findFirst().orElse(null);
                if (espUnit != null) {
                    especialidadesList.add(especialiades.toString().trim() + "   ");
                } else {
                    especialidadesList.add(especialiades.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return especialidadesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private Double getCantidadCertificada(int idUnidad) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Certificacion> datos = session.createQuery("  FROM Certificacion  WHERE unidadobraId =: id").setParameter("id", idUnidad).getResultList();
            total = datos.parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
            tx.commit();
            session.close();
            return total;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return total;
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

    private ObservableList<UniddaObraView> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadObraViewObservableList = FXCollections.observableArrayList();
            unidadobraArrayList = (List<Unidadobra>) session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub").setParameter("idObra", idObra).setParameter("idEmp", idEmp).
                    setParameter("idZona", idZona).setParameter("idObjeto", idObj).setParameter("idNivel", idNiv).setParameter("idEspecilidad", idEsp).setParameter("idSub", idSub).getResultList();
            unidadobraArrayList.sort(Comparator.comparing(Unidadobra::getCodigo));
            for (Unidadobra unidadobra : unidadobraArrayList) {
                double totalCert = getcantCertfificada(unidadobra);
                if (new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue() != 0 && new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue() == new BigDecimal(String.format("%.4f", totalCert)).doubleValue()) {
                    unidadObraViewObservableList.add(new UniddaObraView(unidadobra.getId(), unidadobra.getCodigo().concat("(*)"), unidadobra.getDescripcion().trim(), unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId()));
                } else {
                    unidadObraViewObservableList.add(new UniddaObraView(unidadobra.getId(), unidadobra.getCodigo(), unidadobra.getDescripcion().trim(), unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId()));

                }

            }
            tx.commit();
            session.close();
            return unidadObraViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return FXCollections.emptyObservableList();
    }

    public void deleteUnidadObra(ObservableList<UniddaObraView> list) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            count = 0;
            for (UniddaObraView unid : list) {
                count++;
                if (count > 0 && count % batchSize == 0) {
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

    public Unidadobra initializeUnidadObra(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobra = session.get(Unidadobra.class, id);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return unidadobra;
    }

    public boolean AddRenglonBase(int idUO, String code, String um, double cantidad) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra unidadobra = session.get(Unidadobra.class, idUO);
            if (unidadobra != null) {
                if (code.startsWith("*")) {
                    unidadobra.setRenglonbase(code.substring(1, 7));
                } else {
                    unidadobra.setRenglonbase(code);
                }
                unidadobra.setUm(um);
                unidadobra.setCantidad(cantidad);
                session.update(unidadobra);
                falg = true;
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
        return falg;
    }

    public void completeValuesUO(int idUO, double costoTotal, double costoUnitario, double salario,
                                 double costoMaterial, double costoMano, double costoEquipo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobra = session.get(Unidadobra.class, idUO);
            if (unidadobra != null) {
                unidadobra.setCostototal(costoTotal);
                unidadobra.setCostounitario(costoUnitario);
                unidadobra.setSalario(salario);
                unidadobra.setCostoMaterial(costoMaterial);
                unidadobra.setCostomano(costoMano);
                unidadobra.setCostoequipo(costoEquipo);
                session.update(unidadobra);
                falg = true;
            }

            tx.commit();
            session.close();
        } catch (Exception wx) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Actualizando valores de la unidad de obra ");
            alert.setContentText(wx.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    public void deleteUnidadObreRenglon(int idUo, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            Integer dUR = session.createQuery("DELETE FROM Unidadobrarenglon WHERE unidadobraId =: idU AND renglonvarianteId =: idR").setParameter("idU", idUo).setParameter("idR", idRV).executeUpdate();

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


    }

    private void deleteBajoEspecificacion(List<Bajoespecificacion> bajoespecificacions) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            count = 0;
            for (Bajoespecificacion unid : bajoespecificacions) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.delete(unid);
            }

            trx.commit();
            session.close();

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
    }

    private ObservableList<String> getListSubEspecialidad(int id, int empID, int zonID, int objID, int nivID) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subEspecialiadesList = FXCollections.observableArrayList();
            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) session.createQuery("FROM Subespecialidades where especialidadesId = :id ").setParameter("id", id).getResultList();
            subespecialidadesArrayList.sort(Comparator.comparing(Subespecialidades::getCodigo));
            for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                var subUnit = obra.getUnidadobrasById().parallelStream().filter(u -> u.getEmpresaconstructoraId() == empID && u.getZonasId() == zonID && u.getObjetosId() == objID && u.getNivelId() == nivID && u.getEspecialidadesId() == id && u.getSubespecialidadesId() == subespecialidades.getId()).findFirst().orElse(null);
                if (subUnit != null) {
                    subEspecialiadesList.add(subespecialidades.toString().trim().concat(" "));
                } else {
                    subEspecialiadesList.add(subespecialidades.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return subEspecialiadesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void handleLenaObjetosList(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        comboObjetos.getSelectionModel().clearSelection();
        comboObjetos.setItems(getListObjetos(zonasModel.getId(), empresaconstructoraModel.getId()));
    }

    public void handleLlenarNivelList(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);


        comboNivel.getSelectionModel().clearSelection();
        comboEspecialidad.getSelectionModel().clearSelection();
        comboSubEspecialidad.getSelectionModel().clearSelection();

        dataTable.getItems().clear();
        dataTable.setVisible(false);

        objetosModel = objetosArrayList.parallelStream().filter(o -> o.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        comboNivel.getSelectionModel().clearSelection();
        comboNivel.setItems(getNivelList(objetosModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId()));


    }

    public void handleLlenarEspecialidad(ActionEvent event) {
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        comboEspecialidad.getSelectionModel().clearSelection();

        dataTable.getItems().clear();
        dataTable.setVisible(false);

        comboEspecialidad.getItems().removeAll();

        comboEspecialidad.setItems(getEspecialidades(empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
    }

    public void handlrLlenarSubsepecialidad(ActionEvent event) {
        dataTable.getItems().clear();
        dataTable.setVisible(false);

        especialidadesModel = especialidadesArrayList.parallelStream().filter(e -> e.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);
        comboSubEspecialidad.getSelectionModel().clearSelection();

        comboSubEspecialidad.setItems(getListSubEspecialidad(especialidadesModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
        comboSubEspecialidad.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith(" ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

    }

    public void handleCrearZonas(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Zonas.fxml"));
            Parent proot = loader.load();
            ZonaController controller = loader.getController();
            controller.pasarZona(obra, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboZonas.setItems(getListZonas(empresaconstructoraModel.getId()));
                if (zonaList.size() > 0) {
                    comboZonas.getSelectionModel().select(zonaList.get(0));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleAgregarObjetos(ActionEvent event) {
        zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Objetos.fxml"));
            Parent proot = loader.load();
            ObjetosController controller = loader.getController();
            controller.pasarZona(zonasModel, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboObjetos.setItems(getListObjetos(zonasModel.getId(), empresaconstructoraModel.getId()));
                if (objetosList.size() > 0) {
                    comboObjetos.getSelectionModel().select(objetosList.get(0));
                }
                obra = getObra(zonasModel.getObraId());
                handleLenaObjetosList(event);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleAgregarNivel(ActionEvent event) {
        objetosModel = objetosArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Niveles.fxml"));
            Parent proot = loader.load();
            NivelesController controller = loader.getController();
            controller.pasarObjeto(objetosModel, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
                comboNivel.setItems(getNivelList(objetosModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId()));
                if (nivelesList.size() > 0) {
                    comboNivel.getSelectionModel().select(nivelesList.get(0));
                    obra = null;
                    obra = getObra(zonasModel.getObraId());
                    handleLlenarNivelList(event);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleAgregarEspecialidad(ActionEvent event) {

        try {
            objetosModel = objetosArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("Especialidades.fxml"));
            Parent proot = loader.load();

            EspacialidadesController controller = loader.getController();
            controller.pasarDatos(obra, empresaconstructoraModel, zonasModel, objetosModel, nivelModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


            stage.setOnCloseRequest(event1 -> {

                comboEspecialidad.setItems(getEspecialidades(empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
                if (especialidadesList.size() > 0) {
                    comboEspecialidad.getSelectionModel().select(especialidadesList.get(0));
                }
                garbache = Runtime.getRuntime();
                garbache.gc();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addElementTableRV(Unidadobra unidadobra) {

        RVcode.setCellValueFactory(new PropertyValueFactory<UORVTableView, String>("codeRV"));
        RVcant.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("cant"));
        RVCosto.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("costoTotal"));
        datosRV = FXCollections.observableArrayList();
        datosRV = getUoRVRelation(unidadobra);

        tableRenglones.getItems().setAll(datosRV);

        RVcode.setCellFactory(new Callback<TableColumn<UORVTableView, String>, TableCell<UORVTableView, String>>() {
            @Override
            public TableCell<UORVTableView, String> call(TableColumn<UORVTableView, String> param) {
                return new TableCell<UORVTableView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("  ")) {
                                this.setTextFill(Color.DARKRED);
                                setStyle("-fx-font-size:12");
                                setStyle("-fx-font-weight:900");
                                setText(item.toString());
                            } else {
                                setText(item.toString());
                            }

                        }
                    }
                };
            }
        });
    }

    public void addElementTableSuministros(int idUO) {
        SumCode.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, StringProperty>("codigo"));
        SumCant.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("cantidadBe"));
        SumCost.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("costoBe"));

        datosBE = FXCollections.observableArrayList();
        datosBE = getSumBajoEspecificacion(idUO);
        tableSuministros.setItems(datosBE);

    }

    public void handleAgregarSubespecialidad(ActionEvent event) {
        especialidadesModel = especialidadesArrayList.parallelStream().filter(e -> e.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Subespecialidades.fxml"));
            Parent proot = loader.load();
            SubespecialiadadesController controller = loader.getController();
            controller.pasarEspecialidad(especialidadesModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                comboSubEspecialidad.setItems(getListSubEspecialidad(especialidadesModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
                comboSubEspecialidad.getSelectionModel().select(subEspecialiadesList.get(0));
                garbache = Runtime.getRuntime();
                garbache.gc();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void clearFiels() {
        tableRenglones.getItems().clear();
        tableSuministros.getItems().clear();
        renbase.setText(" ");
        labelcostU.setText(" ");
        labelCosT.setText(" ");
        labelSal.setText(" ");
        labelCant.setText(" ");
        labelRVCant.setText(" ");
        labelPeso.setText(" ");
        labelRVum.setText(" ");
        textDescrp.setText(" ");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usuariosDAO = UsuariosDAO.getInstance();
        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : uoMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : rvMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : sumiMenu.getItems()) {
                item.setDisable(true);
            }

            btnadd.setDisable(true);
            btnImCalp.setDisable(true);
            btnimppcw.setDisable(true);
            btnAfct.setDisable(true);
            btnPlan.setDisable(true);
            btnCertif.setDisable(true);
            addNivel.setDisable(true);
            addObjetos.setDisable(true);
            addZonas.setDisable(true);
            addEspecialidad.setDisable(true);
            addSubespecialidad.setDisable(true);
        } else if (usuariosDAO.usuario.getGruposId() == 7) {
            for (MenuItem item : uoMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : rvMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : sumiMenu.getItems()) {
                item.setDisable(true);
            }

            btnadd.setDisable(true);
            btnImCalp.setDisable(true);
            btnimppcw.setDisable(true);
            btnAfct.setDisable(true);
            addNivel.setDisable(true);
            addObjetos.setDisable(true);
            addZonas.setDisable(true);
            addEspecialidad.setDisable(true);
            addSubespecialidad.setDisable(true);
        }

        TableView.TableViewSelectionModel selectionModel = dataTable.getSelectionModel();
        TableView.TableViewSelectionModel selectionModel2 = tableSuministros.getSelectionModel();
        TableView.TableViewSelectionModel selectionModel3 = tableRenglones.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel2.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel3.setSelectionMode(SelectionMode.MULTIPLE);

        unidadesObraObraController = this;

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {

                if (paneCalc.isVisible() == false) {
                    unidadObraView = dataTable.getSelectionModel().getSelectedItems().get(0);
                    total = 0.0;
                    total = getCantidadCertificada(unidadObraView.getId());
                    unidadobra = new Unidadobra();
                    unidadobra = initializeUnidadObra(unidadObraView.getId());

                    paneCalc.setVisible(true);
                    clearFiels();

                    labelCode.setText(unidadObraView.getCodigo());
                    labelUodescrip.setText(unidadObraView.getDescripcion());
                    // labelUM.setText(" ");

                    if (unidadobra.getRenglonbase() != null) {
                        addElementTableRV(unidadobra);//procedure store
                        addElementTableSuministros(unidadobra.getId());//procedure store
                        renbase.setText(unidadobra.getRenglonbase());

                        labelCant.setText(Double.toString(new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue()));
                        labelUM.setText(unidadobra.getUm());
                        labelcostU.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostounitario())).doubleValue()));
                        labelCosT.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostototal())).doubleValue()));
                        sumCostoTotal = Math.round(unidadobra.getCostototal() * 100d) / 100d;
                        labelSal.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getSalario())).doubleValue()));
                        // labelCUC.setText("0.0");

                    }
                } else if (paneCalc.isVisible() == true && renbase.getText() == " " && tableRenglones.getItems().isEmpty() == false) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("ERROR");
                    alert.setContentText("Debe seleccionar un renglón base para la unida de obra!");
                    alert.showAndWait();
                } else if (paneCalc.isVisible() == true && renbase.getText() != null && uorvTableViewsList.size() > 0) {
                    unidadObraView = dataTable.getSelectionModel().getSelectedItems().get(0);
                    total = 0.0;
                    total = getCantidadCertificada(unidadObraView.getId());

                    unidadobra = new Unidadobra();
                    unidadobra = initializeUnidadObra(unidadObraView.getId());

                    paneCalc.setVisible(true);
                    clearFiels();
                    labelCode.setText(unidadObraView.getCodigo());
                    labelUodescrip.setText(unidadObraView.getDescripcion());
                    // labelUM.setText(" ");

                    if (unidadobra.getRenglonbase() != null) {
                        addElementTableRV(unidadobra);//procedure store
                        addElementTableSuministros(unidadobra.getId());//procedure store

                        renbase.setText(unidadobra.getRenglonbase());

                        labelCant.setText(Double.toString(new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue()));
                        labelUM.setText(unidadobra.getUm());
                        labelcostU.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostounitario())).doubleValue()));
                        labelCosT.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostototal())).doubleValue()));
                        sumCostoTotal = Math.round(unidadobra.getCostototal() * 100d) / 100d;
                        labelSal.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getSalario())).doubleValue()));
                        //labelCUC.setText("0.0");
                    }


                } else if (paneCalc.isVisible() == true && tableRenglones.getItems().isEmpty() == true) {
                    unidadObraView = dataTable.getSelectionModel().getSelectedItems().get(0);

                    total = 0.0;
                    total = getCantidadCertificada(unidadObraView.getId());

                    unidadobra = new Unidadobra();
                    unidadobra = initializeUnidadObra(unidadObraView.getId());

                    paneCalc.setVisible(true);
                    clearFiels();
                    labelCode.setText(unidadObraView.getCodigo());
                    labelUodescrip.setText(unidadObraView.getDescripcion());
                    //  labelUM.setText(" ");

                    if (unidadobra.getRenglonbase() != null) {
                        addElementTableRV(unidadobra);//procedure store
                        addElementTableSuministros(unidadobra.getId());//procedure store

                        renbase.setText(unidadobra.getRenglonbase());
                        labelCant.setText(Double.toString(new BigDecimal(String.format("%.4f", unidadobra.getCantidad())).doubleValue()));
                        labelUM.setText(unidadobra.getUm());
                        labelcostU.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostounitario())).doubleValue()));
                        labelCosT.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getCostototal())).doubleValue()));
                        sumCostoTotal = Math.round(unidadobra.getCostototal() * 100d) / 100d;
                        labelSal.setText(Double.toString(new BigDecimal(String.format("%.2f", unidadobra.getSalario())).doubleValue()));
                        // labelCUC.setText("0.0");
                    }

                }
            }

        });
        tableRenglones.getSelectionModel().

                selectedItemProperty().

                addListener((obs, oldSelection, newSelection) ->

                {
                    if (newSelection != null) {
                        if (newSelection.getConMat().equals("1 ")) {
                            labelTipoCost.setText("SI");
                        } else {

                            labelTipoCost.setText("NO");
                        }

                        labelRVCant.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getCostoTotalRV() * 100d) / 100d));
                        labelPeso.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getPeso() * 100d) / 100d));
                        labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
                        textDescrp.setText(tableRenglones.getSelectionModel().getSelectedItem().getSobreG() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getSubGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getDescripcion());
                    }
                });

        tableSuministros.getSelectionModel().

                selectedItemProperty().

                addListener((obs, oldSelection, newSelection) ->

                {
                    labelRVCant.setText(String.valueOf(tableSuministros.getSelectionModel().getSelectedItem().getPrecio()));
                    labelPeso.setText(String.valueOf(Math.round(tableSuministros.getSelectionModel().getSelectedItem().getPeso() * 100d) / 100d));
                    labelRVum.setText(tableSuministros.getSelectionModel().getSelectedItem().getUm());
                    textDescrp.setText(tableSuministros.getSelectionModel().getSelectedItem().getDescripcion());

                });

        coboEmpresas.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("   ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }


                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboZonas.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("   ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }


                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboObjetos.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("   ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }


                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboNivel.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("   ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboEspecialidad.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("   ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });


        tableRenglones.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ActionEvent event1 = new ActionEvent();
                handleActualizarRV(event1);
            }
        });

        tableSuministros.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ActionEvent event1 = new ActionEvent();
                haddleUpadteBajoEspecificacion(event1);
            }
        });

    }

    public void handleSelectEmpresaAction(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        System.out.println(empresaconstructoraModel.toString());
        comboZonas.setItems(getListZonas(empresaconstructoraModel.getId()));
    }

    public void definirObra(Obra obrav) {
        viewObra = obrav;
        obra = obrav;
        IdObra = obra.getId();
        tipo = obra.getTipoobraId();
        label_title.setText(obrav.getCodigo() + " - " + obrav.getDescripion());
        coboEmpresas.setItems(getListEmpresas(obra));

    }

    public void handleCloseAction(ActionEvent event) {

        if (renbase.getText() == " " && tableRenglones.getItems().isEmpty() == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Debe seleccionar un renglón base para la unidad de obra!");
            alert.showAndWait();

        } else if (renbase.getText() != " " && tableRenglones.getItems().isEmpty() == false) {
            sumCostoTotal = 0.0;
            unidadobra = null;
            paneCalc.setVisible(false);

        } else if (renbase.getText() == " " && tableRenglones.getItems().isEmpty()) {
            sumCostoTotal = 0.0;
            unidadobra = null;
            paneCalc.setVisible(false);

        } else if (renbase.getText() != " " && tableRenglones.getItems().isEmpty()) {
            sumCostoTotal = 0.0;
            unidadobra = null;
            paneCalc.setVisible(false);
        }

    }

    public void handleCargardatos(ActionEvent event) {

        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
        objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

        code.setCellValueFactory(new PropertyValueFactory<UniddaObraView, String>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<UniddaObraView, StringProperty>("descripcion"));
        dataTable.getItems().clear();

        datos = FXCollections.observableArrayList();
        datos = getUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());

        dataTable.getItems().addAll(datos);

        code.setCellFactory(new Callback<TableColumn<UniddaObraView, String>, TableCell<UniddaObraView, String>>() {
            @Override
            public TableCell<UniddaObraView, String> call(TableColumn<UniddaObraView, String> param) {
                return new TableCell<UniddaObraView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("(*)")) {
                                setTextFill(Color.DARKRED);
                                setStyle("-fx-font-size:12");
                                setStyle("-fx-font-weight:900");
                                setText(item.toString());
                            } else {
                                this.setTextFill(Color.BLACK);
                                setText(item.toString());
                            }
                        }
                    }
                };
            }
        });


        if (!dataTable.getItems().isEmpty()) {
            dataTable.setVisible(true);
        }

    }

    public void handleAgregarUO(ActionEvent event) {

        if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() != null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() != null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearUnidaddeObra.fxml"));
                Parent proot = loader.load();
                NuevaUnidadObraController controller = loader.getController();
                controller.pasarParametros(unidadesObraObraController, obra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Información");
            alert.setContentText("Complete la estructura de la obra");
            alert.showAndWait();
        }

    }

    public void handleDeleteUo(ActionEvent event) {
        ObservableList<UniddaObraView> list = FXCollections.observableArrayList();
        list = dataTable.getSelectionModel().getSelectedItems();
        ObservableList<UniddaObraView> toDeleteUnidad = FXCollections.observableArrayList();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar las unidades de obras ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar)
            for (UniddaObraView uniddaObraView : list) {
                double calc = getCantidadCertificada(uniddaObraView.getId()).doubleValue();
                if (calc == 0.0) {
                    toDeleteUnidad.add(uniddaObraView);
                } else {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Información");
                    alert.setContentText("No se puede eliminar unidades de obra con volumenes certificacdos");
                    alert.showAndWait();
                }
            }
        deleteUnidadObra(toDeleteUnidad);
        // calcularElementosUO();
        Thread thread = new CalForUnidadObra(this.unidadesObraObraController, this.empresaconstructoraModel, this.obra);
        thread.start();

        handleCargardatos(event);
        if (dataTable.getItems().size() == 0) {
            dataTable.setVisible(false);
        }
    }

    public void handleNewRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        double calc = getCantidadCertificada(unidadObraView.getId());

        if (calc == 0.0) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToUnidadObra.fxml"));
                Parent proot = loader.load();
                RenVarianteToUnidadObraController controller = loader.getController();
                controller.addUnidadObra(unidadesObraObraController, unidadobra);


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

                stage.setOnCloseRequest(event1 -> {
                    calcularElementosUO();
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
            alert.showAndWait();
        }
    }

    public double cantUN(int id) {
        return initializeUnidadObra(id).getCantidad();
    }

    public void calcularElementosUO() {
        sumCostoTotal = 0.0;
        costoMaterial = 0.0;

        if (!tableRenglones.getItems().isEmpty()) {
            addElementTableSuministros(unidadobra.getId());
            costoMaterial = getCostoMaterial(unidadObraView.getId());
            Double[] valores = getCostosUnidad(unidadObraView.getId());
            sumCostoTotal = costoMaterial + valores[0] + valores[1] + valores[3];
            labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));

            if (unidadobra.getRenglonbase() != null) {
                labelCant.setText(String.valueOf(cantUN(unidadobra.getId())));
                labelSal.setText(String.valueOf(Math.round(valores[2] * 100d) / 100d));
                labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / Double.parseDouble(labelCant.getText()) * 100d) / 100d));
                completeValuesUO(unidadObraView.getId(), Math.round(sumCostoTotal * 100d) / 100d, Double.parseDouble(labelcostU.getText()), valores[2], costoMaterial + valores[3], valores[0], valores[1]);

                Thread thread = new CalForUnidadObra(unidadesObraObraController, empresaconstructoraModel, obra);
                thread.start();

            }

        }

    }

    /**
     * para definir el renglon base y calcular los demas parametros
     *
     * @param event
     */
    public void handleDefinirRenglonBase(ActionEvent event) {
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
        costoMaterial = getCostoMaterial(unidadObraView.getId());
        Double[] valores = getCostosUnidad(unidadObraView.getId());

        sumCostoTotal = costoMaterial + valores[0] + valores[1] + valores[3];

        labelCant.setText(Double.toString(new BigDecimal(String.format("%.4f", uorvTableView.getCant())).doubleValue()));
        labelUM.setText(uorvTableView.getUm());
        labelcostU.setText(Double.toString(new BigDecimal(String.format("%.2f", sumCostoTotal / uorvTableView.getCant())).doubleValue()));
        labelCosT.setText(Double.toString(new BigDecimal(String.format("%.2f", sumCostoTotal)).doubleValue()));
        sumCostoTotal = Math.round(sumCostoTotal * 100d) / 100d;
        labelSal.setText(Double.toString(new BigDecimal(String.format("%.2f", valores[2])).doubleValue()));


        falg = AddRenglonBase(unidadObraView.getId(), uorvTableView.getCodeRV().trim(), uorvTableView.getUm(), uorvTableView.getCant());
        completeValuesUO(unidadObraView.getId(), Math.round(sumCostoTotal * 100d) / 100d, Double.parseDouble(labelcostU.getText()), Math.round(valores[2] * 100d) / 100d, Math.round(costoMaterial * 100d) / 100d + valores[3], valores[0], valores[1]);

        Thread thread = new CalForUnidadObra(unidadesObraObraController, empresaconstructoraModel, obra);
        thread.start();

        renbase.setText(uorvTableView.getCodeRV());

    }

    /**
     * para definir los suministros bajo especificacion
     */

    public void haddleBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        double calc = getCantidadCertificada(unidadObraView.getId());

        if (calc == 0.0) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacion.fxml"));
                Parent proot = loader.load();
                BajoEspecificacionController controller = loader.getController();
                controller.pasarUnidaddeObra(unidadesObraObraController, unidadObraView);


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();


                stage.setOnCloseRequest(event1 -> {
                    calcularElementosUO();
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
            alert.showAndWait();
        }

    }

    public void haddleUpadteBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();
        double calc = getCantidadCertificada(unidadObraView.getId());

        if (calc == 0.0) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSuministroBajoEspecificacion.fxml"));
                Parent proot = loader.load();
                UpdateBajoEspecificacionController controller = loader.getController();
                controller.pasarUnidaddeObra(unidadesObraObraController, unidadObraView, bajoEspecificacionView.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();


                stage.setOnCloseRequest(event1 -> {
                    calcularElementosUO();
                });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
            alert.showAndWait();
        }
    }

    public void handleActualizarRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
        double calc = getCantidadCertificada(unidadObraView.getId());
        if (calc == 0.0) {
            if (tableRenglones.getSelectionModel().getSelectedItem().getCodeRV().trim().startsWith("*")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Cuidado,  Usted modificará el renglón base de la unidad de obra. De ser necesiario realice ajustes en volumenes de los renglones y suministros");
                ButtonType buttonAgregar = new ButtonType("Aceptar");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonAgregar) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToUnidadObraUpdate.fxml"));
                        Parent proot = loader.load();
                        RenVarianteToUnidadObraUpdateController controller = loader.getController();
                        controller.addUpdateRvToUnidadO(unidadesObraObraController, unidadobra, uorvTableView.getId(), true);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(proot));
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.toFront();
                        stage.show();
                        stage.setOnCloseRequest(event1 -> {
                            calcularElementosUO();
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToUnidadObraUpdate.fxml"));
                    Parent proot = loader.load();
                    RenVarianteToUnidadObraUpdateController controller = loader.getController();
                    controller.addUpdateRvToUnidadO(unidadesObraObraController, unidadobra, uorvTableView.getId(), false);


                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.toFront();
                    stage.show();


                    stage.setOnCloseRequest(event1 -> {
                        calcularElementosUO();
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
            alert.showAndWait();
        }


    }

    public void handleDeleteRV(ActionEvent event) {
        ObservableList<UORVTableView> list = FXCollections.observableArrayList();
        list = tableRenglones.getSelectionModel().getSelectedItems();
        double calc = getCantidadCertificada(this.unidadObraView.getId()).doubleValue();
        if (calc == 0.0D) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Esta seguro de eliminar los renglones variantes seleccionados ");
            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                for (UORVTableView tableView : list)
                    deleteUnidadObreRenglon(this.unidadObraView.getId(), tableView.getId());
                tableRenglones.getItems().removeAll(list);
            }
            labelRVCant.setText(" ");
            labelPeso.setText(" ");
            labelRVum.setText(" ");
            textDescrp.setText(" ");
            calcularElementosUO();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
            alert.showAndWait();
        }
    }

    private Bajoespecificacion getBajoEspecificacion(int id) {
        return bajoespecificacionArrayList.parallelStream().filter(bajoespecificacion -> bajoespecificacion.getId() == id).findFirst().orElse(null);
    }

    public void handleDeleteSumistro(ActionEvent event) {
        ObservableList<BajoEspecificacionView> list = FXCollections.observableArrayList();
        list = tableSuministros.getSelectionModel().getSelectedItems();
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        double calc = getCantidadCertificada(unidadObraView.getId()).doubleValue();
        if (calc == 0.0D) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Esta seguro de eliminar los suministros seleccionados ");
            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                List<Bajoespecificacion> bajoespecificacions = new ArrayList<>();
                for (BajoEspecificacionView especificacionView : list)
                    bajoespecificacions.add(getBajoEspecificacion(especificacionView.getId()));
                deleteBajoEspecificacion(bajoespecificacions);
                calcularElementosUO();
                labelRVCant.setText(" ");
                labelPeso.setText(" ");
                labelRVum.setText(" ");
                textDescrp.setText(" ");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Unidad de Obra con volumenes certificados!");
        }
    }

    public void handleMemoriaView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemoriaDescriptiva.fxml"));
            Parent proot = loader.load();
            MemoriaDescriptivaController controller = loader.getController();
            controller.definirUOToMemoria(unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            garbache = Runtime.getRuntime();
            garbache.gc();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleNewCertificacion(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CertificarUnidaddeObra.fxml"));
            Parent proot = loader.load();
            CertificarUnidaddeObraController controller = loader.getController();
            controller.cargarUnidadeObra(unidadObraView.getId());


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

    public void handleNewPlan(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlanificarUnidaddeObra.fxml"));
            Parent proot = loader.load();
            PlanificarUnidaddeObraController controller = loader.getController();
            controller.cargarUnidadeObra(unidadobra);


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

    public void actionHandleDuplica(ActionEvent event) {

        try {
            unidadobra = initializeUnidadObra(dataTable.getSelectionModel().getSelectedItem().getId());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarUO.fxml"));
            Parent proot = loader.load();
            DuplicarUOController controller = loader.getController();
            controller.llenaLabels(unidadesObraObraController, unidadobra, unidadobraArrayList);


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

    public void handleModificarUO(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItems().get(0);
        unidadobra = initializeUnidadObra(unidadObraView.getId());
        double calc = getCantidadCertificada(unidadobra.getId());
//        if (calc == 0.0) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarUnidaddeObra.fxml"));
            Parent proot = loader.load();

            ActualizarUnidadObraController controller = loader.getController();
            controller.pasarParametros(unidadobra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            garbache = Runtime.getRuntime();
            garbache.gc();

            stage.setOnCloseRequest(event1 -> {
                handleCargardatos(event);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // } else {
        //   Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // alert.setTitle("Información");
        // alert.setContentText("No se permite actualizar unidades de obra con cantidades certificadas");
        // alert.showAndWait();
        // }

    }

    public void handleActionImport(ActionEvent event) {

        if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() != null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() != null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarUOtoPCwin.fxml"));
                Parent proot = loader.load();

                ImportarUOtoPCwinController controller = loader.getController();
                controller.cargarDatosSimple(unidadesObraObraController, obra, empresaconstructoraModel, zonasModel, objetosModel, nivelModel, especialidadesModel, subespecialidadesModel);

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() == null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() == null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarUOtoPCwin.fxml"));
                Parent proot = loader.load();

                ImportarUOtoPCwinController controller = loader.getController();
                controller.cargarDatosEspecialidad(unidadesObraObraController, obra, empresaconstructoraModel, zonasModel, objetosModel, nivelModel, null, null);

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() == null && comboObjetos.getValue() != null && comboNivel.getValue() == null && comboZonas.getValue() != null && comboEspecialidad.getValue() == null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarUOtoPCwin.fxml"));
                Parent proot = loader.load();

                ImportarUOtoPCwinController controller = loader.getController();
                controller.cargarDatosNivelObjetos(unidadesObraObraController, obra, empresaconstructoraModel, zonasModel, objetosModel, null, null, null);

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
    }

    public void handleAfectarCantidades(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("afectarView.fxml"));
            Parent proot = loader.load();

            AfectarController controller = loader.getController();
            controller.pasarDatos(unidadesObraObraController, unidadobra, datosRV, bajoEspecificacionViewObservableList);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {

        }

    }

    public void handleActionImportInCalPreC(ActionEvent event) {

        if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() != null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() != null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarUOInCalperC.fxml"));
                Parent proot = loader.load();

                ImportarUOInCalPreCController controller = loader.getController();
                controller.cargarDatos(unidadesObraObraController, obra, empresaconstructoraModel, zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Información");
            alert.setContentText("Complete la estructura de la obra");
            alert.showAndWait();
        }
    }

    public void suplementarUnidadesObra(ActionEvent event) {
        unidadobra = initializeUnidadObra(dataTable.getSelectionModel().getSelectedItem().getId());
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("SuplementarUnidaddeObra.fxml"));
            Parent proot = loader.load();
            SuplementarUnidadObraController controller = loader.getController();
            controller.pasarParametros(unidadesObraObraController, unidadobra);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                handleCargardatos(event);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private List<UnidadReport> getUnidadReportList() {
        List<UnidadReport> unidadReportList = new ArrayList<>();

        for (Unidadobrarenglon ur : unidadobrarenglonArrayList) {
            double total = ur.getCostMat() + ur.getCostMano() + ur.getCostEquip();
            double unitario = total / ur.getCantRv();
            unidadReportList.add(new UnidadReport(ur.getRenglonvarianteByRenglonvarianteId().getCodigo(), ur.getRenglonvarianteByRenglonvarianteId().getDescripcion(), ur.getRenglonvarianteByRenglonvarianteId().getUm(), ur.getCantRv(), unitario, ur.getCostMat(), ur.getCostMano(), ur.getCostEquip(), ur.getSalariomn()));
        }

        if (tableSuministros.getItems().size() > 0) {
            for (BajoEspecificacionView item : tableSuministros.getItems()) {
                unidadReportList.add(new UnidadReport(item.getCodigo(), item.getDescripcion(), item.getUm(), item.getCantidadBe(), item.getPrecio(), item.getCostoBe(), 0.0, 0.0, 0.0));
            }
        }
        return unidadReportList;

    }

    public void imprimirUnidad(ActionEvent event) {

        List<UnidadReport> unidadReports = new ArrayList<>();
        unidadReports = getUnidadReportList();

        printReport(unidadReports, unidadobra);

    }

    public void printReport(List<UnidadReport> list, Unidadobra unidadobra) {
        parametros = new HashMap();
        reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();
        Empresa empresa = reportProjectStructureSingelton.getEmpresa();
        LocalDate date = LocalDate.now();
        parametros.put("obraName", obra.getCodigo() + " " + obra.getDescripion());
        parametros.put("empresa", empresa.getNombre());
        parametros.put("empresac", unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo() + " " + unidadobra.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        parametros.put("zona", unidadobra.getZonasByZonasId().getCodigo() + " " + unidadobra.getZonasByZonasId().getDesripcion());
        parametros.put("objeto", unidadobra.getObjetosByObjetosId().getCodigo() + " " + unidadobra.getObjetosByObjetosId().getDescripcion());
        parametros.put("nivel", unidadobra.getNivelByNivelId().getCodigo() + " " + unidadobra.getNivelByNivelId().getDescripcion());
        parametros.put("especialidad", unidadobra.getEspecialidadesByEspecialidadesId().getCodigo() + " " + unidadobra.getEspecialidadesByEspecialidadesId().getDescripcion());
        parametros.put("subesp", unidadobra.getSubespecialidadesBySubespecialidadesId().getCodigo() + " " + unidadobra.getSubespecialidadesBySubespecialidadesId().getDescripcion());
        parametros.put("unidad", unidadobra.getCodigo() + "    " + unidadobra.getDescripcion().trim() + "     " + unidadobra.getCantidad() + " / " + unidadobra.getUm() + "  -  " + Math.round(unidadobra.getCostototal() * 100d) / 100d);
        parametros.put("fecha", DateTimeFormatter.ofPattern("dd/MM/YYYY").format(date));
        parametros.put("image", "templete/logoReport.jpg");
        try {
            DynamicReport dr = createReportUnidad();
            JRDataSource ds = new JRBeanCollectionDataSource(list);
            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void keyEventRefresh(KeyEvent event) {
        if (event.getCode() == KeyCode.F5) {
            definirObra(viewObra);

            comboZonas.getSelectionModel().clearSelection();
            comboObjetos.getSelectionModel().clearSelection();
            comboNivel.getSelectionModel().clearSelection();
            comboEspecialidad.getSelectionModel().clearSelection();
            comboSubEspecialidad.getSelectionModel().clearSelection();
            dataTable.setVisible(false);

        }
    }

    public void refreshSuministrosAaction(ActionEvent event) {
        addElementTableSuministros(unidadobra.getId());
        calcularElementosUO();
    }

    public void handleCargarHojaCalculo(ActionEvent event) {
        Renglonvariante renglonvariante = unidadobrarenglonArrayList.parallelStream().filter(item -> item.getRenglonvarianteByRenglonvarianteId().getId() == tableRenglones.getSelectionModel().getSelectedItem().getId()).findFirst().map(item -> item.getRenglonvarianteByRenglonvarianteId()).get();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculo.fxml"));
            Parent proot = loader.load();
            HojadeCalcauloController controller = loader.getController();
            controller.LlenarDatosRV(renglonvariante, obra, empresaconstructoraModel.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private DynamicReport createReportUnidad() {
        Style detailStyle = new Style();
        detailStyle.setVerticalAlign(VerticalAlign.TOP);

        Style headerStyle = new Style();
        headerStyle.setFont(Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorderBottom(Border.PEN_1_POINT());
        headerStyle.setBorderTop(Border.PEN_1_POINT());
        headerStyle.setBackgroundColor(java.awt.Color.white);
        headerStyle.setTextColor(java.awt.Color.DARK_GRAY);
        headerStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setTransparency(Transparency.OPAQUE);

        Style headerVariables = new Style();
        headerVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
        //headerVariables.setBorderBottom(Border.THIN());
        headerVariables.setHorizontalAlign(HorizontalAlign.JUSTIFY);
        headerVariables.setVerticalAlign(VerticalAlign.MIDDLE);


        Style titleStyle = new Style();
        titleStyle.setFont(new Font(12, Font._FONT_ARIAL, true));
        titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);

        Style importeStyle = new Style();
        importeStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        oddRowStyle.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        oddRowStyle.setTransparency(Transparency.OPAQUE);


        DynamicReportBuilder drb = new DynamicReportBuilder();
        Integer margin = new Integer(10);
        drb
                .setTitleStyle(titleStyle)
                .setDetailHeight(new Integer(15)).setLeftMargin(margin)
                .setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
                .setPrintBackgroundOnOddRows(false)
                .setGrandTotalLegendStyle(headerVariables)
                .setDefaultStyles(titleStyle, null, headerStyle, detailStyle)
                .setPrintColumnNames(true)
                .setTemplateFile("templete/reportUnidadObra.jrxml")
                .setUseFullPageWidth(true)
                .setGrandTotalLegend("Totales Unidad Obra")
                .setOddRowBackgroundStyle(oddRowStyle);


        AbstractColumn code = ColumnBuilder.getNew()
                .setColumnProperty("codigRV", String.class.getName())
                .setTitle("Código").setWidth(17)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn descip = ColumnBuilder.getNew()
                .setColumnProperty("descripRV", String.class.getName())
                .setTitle("Descripción").setWidth(43)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();


        AbstractColumn um = ColumnBuilder.getNew()
                .setColumnProperty("umRV", String.class.getName())
                .setTitle("U/M").setWidth(6)
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn cant = ColumnBuilder.getNew()
                .setColumnProperty("cantrv", Double.class.getName())
                .setTitle("Cant.").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn unitario = ColumnBuilder.getNew()
                .setColumnProperty("costUnitario", Double.class.getName())
                .setTitle("Costo U").setWidth(12).setPattern("0.0000")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn mater = ColumnBuilder.getNew()
                .setColumnProperty("materialrv", Double.class.getName())
                .setTitle("C. Mat").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn mano = ColumnBuilder.getNew()
                .setColumnProperty("manorv", Double.class.getName())
                .setTitle("C. Mano").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn equip = ColumnBuilder.getNew()
                .setColumnProperty("equiporv", Double.class.getName())
                .setTitle("C. Equip").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        AbstractColumn salario = ColumnBuilder.getNew()
                .setColumnProperty("salariorv", Double.class.getName())
                .setTitle("Salario").setWidth(12).setPattern("0.00")
                .setStyle(detailStyle).setHeaderStyle(headerStyle)
                .build();

        drb.addColumn(code);
        drb.addColumn(descip);
        drb.addColumn(um);
        drb.addColumn(cant);
        drb.addColumn(unitario);
        drb.addColumn(mater);
        drb.addColumn(mano);
        drb.addColumn(equip);
        drb.addColumn(salario);

        drb.addGlobalFooterVariable(mater, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(mano, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(equip, DJCalculation.SUM, headerVariables);
        drb.addGlobalFooterVariable(salario, DJCalculation.SUM, headerVariables);

        DynamicReport dr = drb.build();
        return dr;

    }

    public void handleDesglozarAction(ActionEvent event) {
        var recurso = tableSuministros.getSelectionModel().getSelectedItem();
        if (recurso.getTipo().trim().equals("S") || recurso.getTipo().trim().equals("J")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Esta seguro de desglozar el recurso:  " + recurso.getCodigo() + ". Esta opción agregará a la unidad de obra como suministros bajo especificación los insumos que componen el recurso selccionado, tenga cuidado porque se obvian la mano de obra y los equipos ");
            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                if (recurso.getTipo().trim().equals("S")) {
                    var semielaborado = utilCalcSingelton.getSuministroSemielaboradoById(recurso.getIdSum());
                    createListOfComponentesInSemielaborado(semielaborado.getSemielaboradosrecursosById().parallelStream().filter(item -> item.getRecursosByRecursosId().getTipo().trim().equals("1")).collect(Collectors.toList()), recurso);

                } else if (recurso.getTipo().trim().equals("J")) {
                    var juego = utilCalcSingelton.getJuegoProductoById(recurso.getIdSum());
                    createListOfComponentesInJuego(juego.getJuegorecursosById(), recurso);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setContentText("No se permite desglozar insumos");
            alert.showAndWait();
        }
    }

    private void createListOfComponentesInJuego(Collection<Juegorecursos> juegorecursosById, BajoEspecificacionView recurso) {
        int idUO = recurso.getIdUo();
        List<Bajoespecificacion> listOfRecursos = new ArrayList<>();
        List<Bajoespecificacion> listOfRecursosDelete = new ArrayList<>();
        listOfRecursosDelete.add(getBajoEspecificacion(recurso.getId()));
        for (Juegorecursos semielaboradosrecursos : juegorecursosById) {
            Bajoespecificacion bajo = new Bajoespecificacion();
            bajo.setUnidadobraId(recurso.getIdUo());
            bajo.setIdSuministro(semielaboradosrecursos.getRecursosId());
            bajo.setRenglonvarianteId(0);
            bajo.setSumrenglon(0);
            double cant = recurso.getCantidadBe() * semielaboradosrecursos.getCantidad();
            bajo.setCantidad(cant);
            bajo.setCosto(cant * semielaboradosrecursos.getRecursosByRecursosId().getPreciomn());
            bajo.setTipo(semielaboradosrecursos.getRecursosByRecursosId().getTipo().trim());
            utilCalcSingelton.addBajoEspecificacion(bajo);
        }
        deleteBajoEspecificacion(listOfRecursosDelete);
        addElementTableSuministros(idUO);
        calcularElementosUO();
    }


    private void createListOfComponentesInSemielaborado(List<Semielaboradosrecursos> collect, BajoEspecificacionView recurso) {
        int idUO = recurso.getIdUo();
        List<Bajoespecificacion> listOfRecursosDelete = new ArrayList<>();
        listOfRecursosDelete.add(getBajoEspecificacion(recurso.getId()));
        for (Semielaboradosrecursos semielaboradosrecursos : collect) {
            Bajoespecificacion bajo = new Bajoespecificacion();
            bajo.setUnidadobraId(recurso.getIdUo());
            bajo.setIdSuministro(semielaboradosrecursos.getRecursosId());
            bajo.setRenglonvarianteId(0);
            bajo.setSumrenglon(0);
            double cant = recurso.getCantidadBe() * semielaboradosrecursos.getCantidad();
            bajo.setCantidad(cant);
            bajo.setCosto(cant * semielaboradosrecursos.getRecursosByRecursosId().getPreciomn());
            bajo.setTipo(semielaboradosrecursos.getRecursosByRecursosId().getTipo().trim());
            utilCalcSingelton.addBajoEspecificacion(bajo);
        }
        deleteBajoEspecificacion(listOfRecursosDelete);
        addElementTableSuministros(idUO);
        calcularElementosUO();
    }


}





