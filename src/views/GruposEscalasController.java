package views;

import Reports.BuildDynamicReport;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class GruposEscalasController implements Initializable {

    List<RCreportModel> rCreportModelList = new ArrayList<>();
    @FXML
    private Label tarifaLabel;
    @FXML
    private JFXTextField grupo;
    @FXML
    private JFXTextField valor;
    @FXML
    private TableView<GruposEscalas> tableGroup;
    @FXML
    private TableColumn<GruposEscalas, String> grupCode;
    @FXML
    private TableColumn<GruposEscalas, Double> tarifa;
    @FXML
    private TableColumn<GruposEscalas, Double> grupValor;
    private GrupoEscalaRepository grupoEscalaRepository = GrupoEscalaRepository.getInstance();
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXTextField valIni;

    private String falg;
    private GruposEscalas selectedGrupo;
    private int grupoIndex;
    private TarifaSalarial tarifaSalarial;
    private ConfiguracionController controller;
    private ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    private BuildDynamicReport bdr;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        falg = "Save";

    }

    public void passDataToConfiguration(ConfiguracionController configuracionController, TarifaSalarial tarifa) {
        controller = configuracionController;
        tarifaSalarial = tarifa;
        tarifaLabel.setText(tarifa.getCode());
        ObservableList<GruposEscalas> gruposEscalasObservableList = FXCollections.observableArrayList();
        gruposEscalasObservableList.addAll(tarifaSalarial.getGruposEscalasCollection());
        tableGroup.setItems(gruposEscalasObservableList);
        loadData();
    }

    private void loadData() {
        grupCode.setCellValueFactory(new PropertyValueFactory<GruposEscalas, String>("grupo"));
        tarifa.setCellValueFactory(new PropertyValueFactory<GruposEscalas, Double>("valorBase"));
        grupValor.setCellValueFactory(new PropertyValueFactory<GruposEscalas, Double>("valor"));
    }

    public void addGrupoToList(ActionEvent event) {
        if (!grupo.getText().isEmpty() && !valor.getText().isEmpty() && !valIni.getText().isEmpty()) {
            if (falg.equals("Save")) {
                saveInTable(grupo.getText(), valIni.getText(), valor.getText());
                loadData();
            } else if (falg.equals("Update")) {
                updateIntable(grupo.getText(), valIni.getText(), valor.getText());
                loadData();
            }
        } else {
            utilCalcSingelton.showAlert("Complete los campos del fomulario para crear los grupos de la tarifa salarial!!!", 2);
        }
    }

    private double calcValorTarifa(double valorTar) {
        double porAE = tarifaSalarial.getAutEspecial() / 100 * valorTar;
        double porAnt = tarifaSalarial.getAntiguedad() / 100 * valorTar;
        double porVac = tarifaSalarial.getVacaciones() / 100 * valorTar;
        double porNom = tarifaSalarial.getNomina() / 100 * valorTar;
        double porSeg = tarifaSalarial.getSegSocial() / 100 * valorTar;
        double calculo = valorTar + porAE + porAnt + porVac + porNom + porSeg;
        return new BigDecimal(String.format("%.2f", calculo)).doubleValue();
    }

    public void calcCostoTarifa(ActionEvent event) {
        double val = calcValorTarifa(Double.parseDouble(valIni.getText()));
        valor.setText(String.valueOf(val));
    }

    private void saveInTable(String group, String ini, String value) {
        GruposEscalas gruposEscalas = new GruposEscalas();
        gruposEscalas.setGrupo(group);
        gruposEscalas.setValorBase(Double.parseDouble(ini));
        gruposEscalas.setValor(Double.parseDouble(value));
        gruposEscalas.setTarifaId(tarifaSalarial.getId());
        grupoEscalaRepository.addGrupoEscala(gruposEscalas);
        tableGroup.getItems().add(gruposEscalas);
        grupo.clear();
        valor.clear();
        valIni.clear();
        controller.showGruposEscalasAction(tarifaSalarial);
    }

    private void updateIntable(String grupDes, String ini, String value) {
        var select = tableGroup.getItems().get(grupoIndex);
        select.setGrupo(grupDes);
        select.setValorBase(Double.parseDouble(ini));
        select.setValor(Double.parseDouble(value));
        grupoEscalaRepository.updateGrupoEscala(select);
        tableGroup.getItems().set(grupoIndex, select);
        grupo.clear();
        valor.clear();
        valIni.clear();
        falg = "Save";
        controller.showGruposEscalasAction(tarifaSalarial);
    }

    public void updateGrupoIntable(ActionEvent event) {
        falg = "Update";
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        grupoIndex = tableGroup.getSelectionModel().getSelectedIndex();
        grupo.setText(selectedGrupo.getGrupo());
        valIni.setText(String.valueOf(selectedGrupo.getValorBase()));
        valor.setText(String.valueOf(selectedGrupo.getValor()));
    }

    public void handleRemoveToTable(ActionEvent event) {
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        grupoEscalaRepository.removeGrupoEscala(selectedGrupo);
        tableGroup.getItems().remove(selectedGrupo);
        loadData();
    }

    public void printRenglonReport(ActionEvent event) {
        var grupos = tableGroup.getSelectionModel().getSelectedItem();
        bdr = new BuildDynamicReport();
        Map parametros = new HashMap();
        parametros.put("empresa", structureSingelton.getEmpresa().getNombre().trim());
        parametros.put("image", "templete/logoReport.jpg");
        parametros.put("reportName", "Tarifa salarial Grupo: " + grupos.getGrupo());
        parametros.put("costoTotal", "Costo Directo: " + grupos.getValorBase() + " " + grupos.getGrupoByEscala().getMoneda() + "/mes");
        parametros.put("tarifa", "Tarifa Horaria: " + grupos.getValor() + " " + grupos.getGrupoByEscala().getMoneda() + "/hh");
        //parametros.put("horas", "Horas Mes: " + grupos.getGrupoByEscala().getHoras());

        rCreportModelList = new ArrayList<>();
        rCreportModelList = createListTorepotT(grupos);
        try {
            DynamicReport dr = bdr.createreportTarifaTotal();
            JRDataSource ds = new JRBeanCollectionDataSource(rCreportModelList);
            JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
            JasperViewer.viewReport(jp, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            utilCalcSingelton.showAlert(" Ocurrio un error al mostrar el reporte!!!", 2);
        }
    }

    private List<RCreportModel> createListTorepotT(GruposEscalas gruposEscalas) {
        List<RCreportModel> temp = new ArrayList<>();
        for (TarConceptos conceptos : gruposEscalas.getGrupoByEscala().getTarConceptosCollection()) {
            temp.add(new RCreportModel("COSTOS PAIS MERCADO OBJETIVO", " ", conceptos.getDescripcion().trim(), "usd/mes", 0.0, 0.0, new BigDecimal(String.format("%.2f", conceptos.getValor())).doubleValue()));
        }
        /*
        for (GrupoEscalaConceptos gruposConceptos : gruposEscalas.getTarConceptosCollection()) {
            TarConceptosBases conceptosBases = getConceptosBasesByID(gruposConceptos.getConceptoId());
            if (gruposConceptos.getTipo() == 2) {
                temp.add(new RCreportModel("COSTOS EN CUBA", " ", conceptosBases.getDescripcion().trim(), "usd/mes", 0.0, 0.0, new BigDecimal(String.format("%.2f", gruposConceptos.getValor())).doubleValue()));
            } else if (gruposConceptos.getTipo() == 3) {
                temp.add(new RCreportModel("BASES DE ESTIMACION TARIFA HORARIA USD/HH ", " ", conceptosBases.getDescripcion().trim(), "usd/mes", 0.0, 0.0, new BigDecimal(String.format("%.2f", gruposConceptos.getValor())).doubleValue()));
            }
        }
        */

        return temp;
    }

    private TarConceptosBases getConceptosBasesByID(int idC) {
        if (utilCalcSingelton.tarConceptosBasesList == null) {
            List<TarConceptosBases> temp = utilCalcSingelton.getTarConceptosBasesList();
            temp.size();
        }

        return utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getId() == idC).findFirst().get();
    }

}
