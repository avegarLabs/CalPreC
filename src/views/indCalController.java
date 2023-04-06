package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class indCalController implements Initializable {

    @FXML
    private Label label_title;

    @FXML
    private JFXCheckBox Presup;

    @FXML
    private JFXCheckBox planif;

    @FXML
    private JFXCheckBox certif;

    @FXML
    private JFXDatePicker ini;

    @FXML
    private JFXDatePicker fin;

    @FXML
    private JFXComboBox<String> listObras;

    @FXML
    private TableView<RenglonesIndModel> tableRenglones;

    @FXML
    private TableColumn<RenglonesIndModel, String> emp;

    @FXML
    private TableColumn<RenglonesIndModel, String> zon;

    @FXML
    private TableColumn<RenglonesIndModel, String> obj;

    @FXML
    private TableColumn<RenglonesIndModel, String> niv;

    @FXML
    private TableColumn<RenglonesIndModel, String> esp;

    @FXML
    private TableColumn<RenglonesIndModel, String> uo;

    @FXML
    private TableColumn<RenglonesIndModel, String> rv;

    @FXML
    private TableColumn<RenglonesIndModel, String> descrp;

    @FXML
    private TableColumn<RenglonesIndModel, String> um;

    @FXML
    private TableColumn<RenglonesIndModel, Double> vol;

    @FXML
    private TableColumn<RenglonesIndModel, Double> cost;


    @FXML
    private TableView<InsumoIndicadores> tableMano;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoEmp;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoZona;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoObjeto;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoNivel;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoEspecialidad;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoCode;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoDesc;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoUm;

    @FXML
    private TableColumn<InsumoIndicadores, String> manoGEscala;

    @FXML
    private TableColumn<InsumoIndicadores, Double> manoCant;


    @FXML
    private TableView<InsumoIndicadores> tableMateriales;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaE;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaZon;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaObj;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaNiv;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaNiv1;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaCode;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaDesc;

    @FXML
    private TableColumn<InsumoIndicadores, String> materiaUm;

    @FXML
    private TableColumn<InsumoIndicadores, Double> materiaCant;

    @FXML
    private TableColumn<InsumoIndicadores, Double> materiaPrec;


    @FXML
    private JFXCheckBox pendientes;

    @FXML
    private JFXButton calcular;

    @FXML
    private JFXComboBox<String> listEmpresas;

    @FXML
    private JFXComboBox<String> listZonas;

    @FXML
    private JFXComboBox<String> listObjetos;


    private IndicadoresRespository indicadoresRespository;
    private ObservableList<String> obrasListObservableList;
    private ObservableList<String> empresasListObservableList;
    private ObservableList<String> zonasListObservableList;
    private ObservableList<String> objetosListObservableList;
    private ObservableList<RenglonesIndModel> renglonesIndModelObservableList;
    private ObservableList<InsumoIndicadores> insumoIndicadoresObservableList;
    private ObservableList<InsumoIndicadores> materialesInsumoIndicadoresObservableList;

    private ReportProjectStructureSingelton singelton = ReportProjectStructureSingelton.getInstance();
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    private String CONSULTA_PRESUPUESTORV = "SELECT r.id, SUM(ur.cantRv) as vol, SUM(ur.costMat + ur.costMano + ur.costEquip) as costo, uo.id FROM Unidadobra uo INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id WHERE  ";
    private String CONSULTA_PRESUPUESTOMANO = "SELECT rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, SUM(ur.cantRv * rvrc.cantidas), rec.grupoescala, e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes FROM Unidadobra uo  INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN Renglonrecursos  rvrc ON r.id = rvrc.renglonvarianteId INNER JOIN Recursos  rec ON rvrc.recursosId = rec.id WHERE ";
    private String CONSULTA_PRESUPUESTORECURSOS = "SELECT SUM(bajo.cantidad), bajo.idSuministro, bajo.tipo, e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes  FROM Unidadobra uo INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId WHERE ";


    private String CONSULTA_PLANRV = " SELECT r.id, SUM(ur.cantRv * cert.cantidad / uo.cantidad), SUM(ur.costMat * cert.cantidad / uo.cantidad + ur.costMano * cert.cantidad / uo.cantidad + ur.costEquip * cert.cantidad / uo.cantidad) as costo, uo.id FROM Unidadobra uo INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN Planificacion cert ON cert.unidadobraId = uo.id WHERE ";
    private String CONSULTA_PLANMANO = "SELECT rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, SUM(ur.cantRv * cert.cantidad / uo.cantidad * rvrc.cantidas),  rec.grupoescala, e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes FROM Unidadobra uo INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN Renglonrecursos  rvrc ON r.id = rvrc.renglonvarianteId INNER JOIN Recursos rec ON rvrc.recursosId = rec.id INNER JOIN Planificacion cert ON cert.unidadobraId = uo.id WHERE ";
    private String CONSULTA_PLANRECURSOS = "SELECT SUM(bajo.cantidad * ctr.cantidad / uo.cantidad), bajo.idSuministro, bajo.tipo,  e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes FROM Unidadobra uo INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId INNER JOIN Planificacion ctr ON uo.id = ctr.unidadobraId WHERE ";

    private String CONSULTA_CERTIFICARV = " SELECT r.id, SUM(ur.cantRv * cert.cantidad / uo.cantidad), SUM(ur.costMat * cert.cantidad / uo.cantidad + ur.costMano * cert.cantidad / uo.cantidad + ur.costEquip * cert.cantidad / uo.cantidad) as costo, uo.id FROM Unidadobra uo INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN  Certificacion cert ON cert.unidadobraId = uo.id WHERE ";
    private String CONSULTA_CERTIFICAMANO = "SELECT rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, SUM(ur.cantRv * cert.cantidad / uo.cantidad * rvrc.cantidas),  rec.grupoescala, e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes FROM Unidadobra uo INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN Renglonrecursos  rvrc ON r.id = rvrc.renglonvarianteId INNER JOIN Recursos  rec ON rvrc.recursosId = rec.id INNER JOIN  Certificacion cert ON cert.unidadobraId = uo.id WHERE ";
    private String CONSULTA_CERTIFICARECURSOS = "SELECT SUM(bajo.cantidad * ctr.cantidad / uo.cantidad), bajo.idSuministro, bajo.tipo, e.codigo as emp, e.descripcion as edesc, z.codigo as zon, z.desripcion as cdes, o.codigo as obj, o.descripcion as odesc, n.codigo as niv, n.descripcion as ndesc, esp.codigo as espec, esp.descripcion as espDes  FROM Unidadobra uo INNER JOIN Empresaconstructora e ON uo.empresaconstructoraId = e.id INNER JOIN Zonas z ON uo.zonasId = z.id INNER JOIN Objetos o ON uo.objetosId = o.id INNER JOIN Nivel n ON uo.nivelId = n.id INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id INNER JOIN Bajoespecificacion bajo ON uo.id = bajo.unidadobraId INNER JOIN  Certificacion ctr ON uo.id = ctr.unidadobraId WHERE ";

    //private String CONSULTA_Pendientes = "SELECT r.id, SUM(ur.cantRv * cert.cantidad / uo.cantidad), ur.conMat FROM Unidadobra uo INNER JOIN Unidadobrarenglon ur ON uo.id = ur.unidadobraId INNER JOIN Renglonvariante r ON ur.renglonvarianteId = r.id INNER JOIN Certificacion cert ON cert.unidadobraId = uo.id WHERE ";

    private Obra obra;
    private Empresaconstructora empresaconstructora;
    private Zonas zonas;
    private Objetos objetos;
    private ObservableList<RenglonesIndModel> renglonesIndModelObservableListCert;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indicadoresRespository = new IndicadoresRespository();
        List<Obra> obraList = indicadoresRespository.getObrasList();
        obrasListObservableList = FXCollections.observableArrayList();
        obrasListObservableList.addAll(obraList.parallelStream().map(Obra::toString).collect(Collectors.toList()));
        listObras.setItems(obrasListObservableList);

        LocalDate t1 = LocalDate.now();
        LocalDate inicio = t1.with(firstDayOfMonth());
        LocalDate finalD = t1.with(lastDayOfMonth());

        ini.setValue(inicio);
        fin.setValue(finalD);

        Presup.setOnAction(event -> {
            if (Presup.isSelected()) {
                planif.setSelected(false);
                certif.setSelected(false);
                pendientes.setSelected(false);
                ini.setDisable(true);
                fin.setDisable(true);
                calcular.setDisable(true);


                tableRenglones.getItems().clear();
                tableMateriales.getItems().clear();
                tableMano.getItems().clear();

                if (listObras.getValue() != null) {
                    obra = indicadoresRespository.obrasList.parallelStream().filter(item -> item.toString().trim().equals(listObras.getValue().trim())).findFirst().get();
                    String queryVal = CONSULTA_PRESUPUESTORV + " uo.obraId = " + obra.getId() + " GROUP BY r.id, uo.id ";
                    renglonesIndModelObservableList = FXCollections.observableArrayList();
                    renglonesIndModelObservableList = indicadoresRespository.getRenglonesToIndicators(queryVal);
                    llenaTableRenglones(renglonesIndModelObservableList);


                    String queryMano = CONSULTA_PRESUPUESTOMANO + "  uo.obraId = " + obra.getId() + " AND rec.tipo = '2' GROUP BY rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, rec.grupoescala, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                    insumoIndicadoresObservableList = FXCollections.observableArrayList();
                    insumoIndicadoresObservableList = indicadoresRespository.getCalcManoObra(queryMano);
                    llenaTableManoObra(insumoIndicadoresObservableList);


                    String queryMateriales = CONSULTA_PRESUPUESTORECURSOS + " uo.obraId = " + obra.getId() + " GROUP BY bajo.idSuministro, bajo.tipo, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                    materialesInsumoIndicadoresObservableList = FXCollections.observableArrayList();
                    materialesInsumoIndicadoresObservableList = indicadoresRespository.getCalcrecursos(queryMateriales);
                    llenaTableRecursos(materialesInsumoIndicadoresObservableList);

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Indique la obra!!!");
                    alert.setContentText("");
                    alert.showAndWait();
                }
            }
        });

        planif.setOnAction(event -> {
            Presup.setSelected(false);
            certif.setSelected(false);
            ini.setDisable(false);
            fin.setDisable(false);
            pendientes.setSelected(false);
            calcular.setDisable(false);

            tableRenglones.getItems().clear();
            tableMateriales.getItems().clear();
            tableMano.getItems().clear();
        });

        certif.setOnAction(event -> {
            Presup.setSelected(false);
            planif.setSelected(false);
            ini.setDisable(false);
            fin.setDisable(false);
            pendientes.setSelected(false);
            calcular.setDisable(false);

            tableRenglones.getItems().clear();
            tableMateriales.getItems().clear();
            tableMano.getItems().clear();
        });

        pendientes.setOnAction(event -> {
            Presup.setSelected(false);
            planif.setSelected(false);
            ini.setDisable(true);
            fin.setDisable(true);
            certif.setSelected(false);
            calcular.setDisable(false);

            tableRenglones.getItems().clear();
            tableMateriales.getItems().clear();
            tableMano.getItems().clear();
        });
    }

    public void calcularAction(Event event) {

        if (planif.isSelected()) {
            LocalDate dateDes = ini.getValue();
            LocalDate dateHast = fin.getValue();
            Date desdeDate = Date.valueOf(dateDes);
            Date hastaData = Date.valueOf(dateHast);

            if (listObras.getValue() != null) {
                obra = indicadoresRespository.obrasList.parallelStream().filter(item -> item.toString().trim().equals(listObras.getValue())).findFirst().get();
                String queryVal = CONSULTA_PLANRV + " uo.obraId = " + obra.getId() + " AND cert.desde >= '" + desdeDate.toString() + "' AND cert.hasta <= '" + hastaData.toString() + "' GROUP BY r.id, uo.id ";
                renglonesIndModelObservableList = FXCollections.observableArrayList();
                System.out.println(queryVal);
                renglonesIndModelObservableList = indicadoresRespository.getRenglonesToIndicators(queryVal);
                llenaTableRenglones(renglonesIndModelObservableList);


                String queryMano = CONSULTA_PLANMANO + " uo.obraId = " + obra.getId() + " AND cert.desde >= '" + desdeDate.toString() + "' AND cert.hasta <= '" + hastaData.toString() + "' AND rec.tipo = '2' GROUP BY rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, rec.grupoescala, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                insumoIndicadoresObservableList = FXCollections.observableArrayList();
                insumoIndicadoresObservableList = indicadoresRespository.getCalcManoObra(queryMano);
                llenaTableManoObra(insumoIndicadoresObservableList);


                String queryMateriales = CONSULTA_PLANRECURSOS + " uo.obraId = " + obra.getId() + " AND ctr.desde >= '" + desdeDate.toString() + "' AND ctr.hasta <= '" + hastaData.toString() + "' GROUP BY bajo.idSuministro, bajo.tipo, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                materialesInsumoIndicadoresObservableList = FXCollections.observableArrayList();
                materialesInsumoIndicadoresObservableList = indicadoresRespository.getCalcrecursos(queryMateriales);
                llenaTableRecursos(materialesInsumoIndicadoresObservableList);


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Indique la obra!!!");
                alert.setContentText("");
                alert.showAndWait();
            }
        }

        if (certif.isSelected()) {

            LocalDate dateDes = ini.getValue();
            LocalDate dateHast = fin.getValue();
            Date desdeDate = Date.valueOf(dateDes);
            Date hastaData = Date.valueOf(dateHast);

            if (listObras.getValue() != null) {
                obra = indicadoresRespository.obrasList.parallelStream().filter(item -> item.toString().trim().equals(listObras.getValue())).findFirst().get();
                String queryVal = CONSULTA_CERTIFICARV + " uo.obraId = " + obra.getId() + " AND cert.desde >= '" + desdeDate.toString() + "' AND cert.hasta <= '" + hastaData.toString() + "' GROUP BY r.id, uo.id ";
                renglonesIndModelObservableList = FXCollections.observableArrayList();
                renglonesIndModelObservableList = indicadoresRespository.getRenglonesToIndicators(queryVal);
                llenaTableRenglones(renglonesIndModelObservableList);

                String queryMano = CONSULTA_CERTIFICAMANO + " uo.obraId = " + obra.getId() + " AND cert.desde >= '" + desdeDate.toString() + "' AND cert.hasta <= '" + hastaData.toString() + "' AND rec.tipo = '2' GROUP BY rec.id, rec.codigo, rec.descripcion, rec.um, rec.preciomn, rec.grupoescala, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                insumoIndicadoresObservableList = FXCollections.observableArrayList();
                insumoIndicadoresObservableList = indicadoresRespository.getCalcManoObra(queryMano);
                llenaTableManoObra(insumoIndicadoresObservableList);

                String queryMateriales = CONSULTA_CERTIFICARECURSOS + " uo.obraId = " + obra.getId() + " AND ctr.desde >= '" + desdeDate.toString() + "' AND ctr.hasta <= '" + hastaData.toString() + "' GROUP BY bajo.idSuministro, bajo.tipo, e.codigo, e.descripcion, z.codigo, z.desripcion, o.codigo, o.descripcion, n.codigo, n.descripcion, esp.codigo, esp.descripcion ";
                materialesInsumoIndicadoresObservableList = FXCollections.observableArrayList();
                materialesInsumoIndicadoresObservableList = indicadoresRespository.getCalcrecursos(queryMateriales);
                llenaTableRecursos(materialesInsumoIndicadoresObservableList);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Indique la obra!!!");
                alert.setContentText("");
                alert.showAndWait();
            }
        }

        /*
        if (pendientes.isSelected()) {
            if (listObras.getValue() != null) {
                obra = indicadoresRespository.obrasList.parallelStream().filter(item -> item.toString().trim().equals(listObras.getValue())).findFirst().get();
                String queryPend = CONSULTA_Pendientes + " uo.obraId = " + obra.getId() + " GROUP BY r.id, ur.conMat ";
                renglonesIndModelObservableListCert = FXCollections.observableArrayList();
                renglonesIndModelObservableListCert = indicadoresRespository.getRenglonesToIndicatorsCertificacionPendientes(queryPend);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Indique la obra!!!");
                alert.setContentText("");
                alert.showAndWait();
            }
            ObservableList<RenglonesIndModel> renglonesIndModelObservableListCertFinal = FXCollections.observableArrayList();
            renglonesIndModelObservableListCertFinal = getRenglonesPendientesIndc(renglonesIndModelObservableList, renglonesIndModelObservableListCert);
            llenaTableRenglones(renglonesIndModelObservableListCertFinal);
        }
        */

    }

    /**
     * private ObservableList<RenglonesIndModel> getRenglonesPendientesIndc(ObservableList<RenglonesIndModel> renglonesIndModelObservableList, ObservableList<RenglonesIndModel> renglonesIndModelObservableListCert) {
     * ObservableList<RenglonesIndModel> renglonesIndModelsFinal = FXCollections.observableArrayList();
     * for (RenglonesIndModel renglonesIndModel : renglonesIndModelObservableList) {
     * //  RenglonesIndModel item = renglonesIndModelObservableListCert.parallelStream().filter(i -> i.getGrupo().trim().equals(renglonesIndModel.getGrupo().trim()) && i.getSubGrupo().trim().equals(renglonesIndModel.getSubGrupo().trim()) && i.getCode().trim().equals(renglonesIndModel.getCode().trim())).findFirst().orElse(null);
     * /*if (item != null) {
     * double volPen = renglonesIndModel.getVol() - item.getVol();
     * double costPend = renglonesIndModel.getCosto() - item.getCosto();
     * renglonesIndModel.setVol(new BigDecimal(String.format("%.2f", volPen)).doubleValue());
     * renglonesIndModel.setCosto(new BigDecimal(String.format("%.2f", costPend)).doubleValue());
     * renglonesIndModelsFinal.add(renglonesIndModel);
     * } else {
     * renglonesIndModelsFinal.add(renglonesIndModel);
     * }
     * }
     * return renglonesIndModelsFinal;
     * }
     */
    private void llenaTableRenglones(ObservableList<RenglonesIndModel> renglonesIndModelObservableList) {
        emp.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("empresa"));
        zon.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("zona"));
        obj.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("objeto"));
        niv.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("nivel"));
        esp.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("especialidad"));
        uo.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("uo"));
        rv.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("code"));
        descrp.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("descrip"));
        um.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("um"));
        vol.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, Double>("vol"));
        cost.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, Double>("costo"));
        tableRenglones.getItems().addAll(renglonesIndModelObservableList);
    }

    private void llenaTableManoObra(ObservableList<InsumoIndicadores> IndModelObservableList) {
        manoEmp.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("empresa"));
        manoZona.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("zona"));
        manoObjeto.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("objeto"));
        manoNivel.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("nivel"));
        manoEspecialidad.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("especialidad"));
        manoCode.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("code"));
        manoDesc.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("description"));
        manoUm.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("um"));
        manoGEscala.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("escala"));
        manoCant.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, Double>("cantidad"));
        tableMano.getItems().addAll(IndModelObservableList);
    }

    public void addLlenarEmpresasZonas(ActionEvent event) {
        Obra obra = indicadoresRespository.obrasList.parallelStream().filter(item -> item.toString().trim().equals(listObras.getValue())).findFirst().get();
        empresasListObservableList = FXCollections.observableArrayList();
        empresasListObservableList.addAll(singelton.getEmpresaList(obra.getId()).stream().filter(e -> !e.trim().equals("Todas")).collect(Collectors.toList()));
        listEmpresas.setItems(empresasListObservableList);
        zonasListObservableList = FXCollections.observableArrayList();
        zonasListObservableList.addAll(singelton.getZonasList(obra.getId()).stream().filter(e -> !e.trim().equals("Todas")).collect(Collectors.toList()));
        listZonas.setItems(zonasListObservableList);
    }

    public void addLlenarObjetos(ActionEvent event) {
        Zonas zon = singelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(listZonas.getValue())).findFirst().get();
        objetosListObservableList = FXCollections.observableArrayList();
        objetosListObservableList.addAll(singelton.getObjetosList(zon.getId()).stream().filter(e -> !e.trim().equals("Todos")).collect(Collectors.toList()));
        listObjetos.setItems(objetosListObservableList);
    }


    private void llenaTableRecursos(ObservableList<InsumoIndicadores> recursosIndModelObservableList) {
        materiaE.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("empresa"));
        materiaZon.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("zona"));
        materiaObj.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("objeto"));
        materiaNiv.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("nivel"));
        materiaNiv1.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("especialidad"));
        materiaPrec.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, Double>("precio"));
        materiaCode.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("code"));
        materiaDesc.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("description"));
        materiaUm.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, String>("um"));
        materiaCant.setCellValueFactory(new PropertyValueFactory<InsumoIndicadores, Double>("cantidad"));
        tableMateriales.getItems().addAll(recursosIndModelObservableList);
    }


    public void actionExportarExcelRenglones(ActionEvent event) {
        createExcelFileV1(tableRenglones.getItems());
    }


    public void actionExportarExcelMano(ActionEvent event) {
        createExcelFileV2(tableMano.getItems(), 1);
    }


    public void actionExportarExcelRecursos(ActionEvent event) {
        createExcelFileV2(tableMateriales.getItems(), 2);
    }


    private void createExcelFileV1(ObservableList<RenglonesIndModel> renglonesIndModelObservableList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Indicadores Actividades");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 1;

            for (RenglonesIndModel cl : renglonesIndModelObservableList) {
                data.put("1", new Object[]{"Empresa", "Zona", "Objeto", "Nivel", "Especialidad", "Unidad Obra", "RV", "Descripción", "U/M", "Cantidad", "Costo"});

                countrow++;
                Object[] datoscl = new Object[11];
                datoscl[0] = cl.getEmpresa();
                datoscl[1] = cl.getZona();
                datoscl[2] = cl.getObjeto();
                datoscl[3] = cl.getNivel();
                datoscl[4] = cl.getEspecialidad();
                datoscl[5] = cl.getUo();
                datoscl[6] = cl.getCode();
                datoscl[7] = cl.getDescrip();
                datoscl[8] = cl.getUm();
                datoscl[9] = cl.getVol();
                datoscl[10] = cl.getCosto();
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

    private void createExcelFileV2(ObservableList<InsumoIndicadores> insumosIndModelObservableList, int type) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Insumos");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 1;

            if (type == 1) {

                for (InsumoIndicadores cl : insumosIndModelObservableList) {
                    data.put("1", new Object[]{"Empresa", "Zona", "Objeto", "Nivel", "Especialidad", "Código", "Descripción", "U/M", "G.Escala", "Cantidad"});
                    countrow++;
                    Object[] datoscl = new Object[10];
                    datoscl[0] = cl.getEmpresa();
                    datoscl[1] = cl.getZona();
                    datoscl[2] = cl.getObjeto();
                    datoscl[3] = cl.getNivel();
                    datoscl[4] = cl.getEspecialidad();
                    datoscl[5] = cl.getCode();
                    datoscl[6] = cl.getDescription();
                    datoscl[7] = cl.getUm();
                    datoscl[8] = cl.getEscala();
                    datoscl[9] = cl.getCantidad();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            } else if (type == 2) {
                for (InsumoIndicadores cl : insumosIndModelObservableList) {
                    data.put("1", new Object[]{"Empresa", "Zona", "Objeto", "Nivel", "Especialidad", "Código", "Descripción", "U/M", "Cantidad", "Precio"});
                    countrow++;
                    Object[] datoscl = new Object[10];
                    datoscl[0] = cl.getEmpresa();
                    datoscl[1] = cl.getZona();
                    datoscl[2] = cl.getObjeto();
                    datoscl[3] = cl.getNivel();
                    datoscl[4] = cl.getEspecialidad();
                    datoscl[5] = cl.getCode();
                    datoscl[6] = cl.getDescription();
                    datoscl[7] = cl.getUm();
                    datoscl[8] = cl.getCantidad();
                    datoscl[9] = cl.getPrecio();
                    data.put(String.valueOf(countrow++), datoscl);
                }
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

    public void addIndicadoresToCalc(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndicadoresNormas.fxml"));
            Parent proot = loader.load();

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

    public void showResumenView(ActionEvent event) {
        List<RenglonesIndModel> resumenList = tableRenglones.getItems().parallelStream().filter(item -> item.getCode().trim().equals("->")).collect(Collectors.toList());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndicadoresNormasResumen.fxml"));
            Parent proot = loader.load();

            IndicadoresNormasResumenController controler = (IndicadoresNormasResumenController) loader.getController();
            controler.addRCtoTable(resumenList);

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
