package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConceptTarifasSalarialBaseController implements Initializable {


    @FXML
    private JFXTextArea tarDescripcion;

    @FXML
    private JFXTextField moneda;


    @FXML
    private TableView<TarConceptos> tableGroup;

    @FXML
    private TableColumn<TarConceptos, String> grupCode;

    @FXML
    private TableColumn<TarConceptos, Double> grupValor;

    @FXML
    private JFXTextArea tarDescripcion1;

    @FXML
    private JFXTextField moneda1;


    @FXML
    private TableView<TarConceptos> tableGroup1;

    @FXML
    private TableColumn<TarConceptos, String> grupCode1;

    @FXML
    private TableColumn<TarConceptos, Double> grupValor1;


    @FXML
    private JFXButton btnUpdate;

    private String falg;
    private TarConceptos selectedGrupo;
    private int grupoIndex;

    private TarifasSalarialController configuracion;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private double cpais;

    @FXML
    private JFXTextField cuba_total;

    @FXML
    private JFXTextField resumen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        falg = "Save";
    }

    public void passDataToConfiguracion(double costoPais, TarifasSalarialController configuracionController, GruposEscalas grupo) {
        configuracion = configuracionController;
        cpais = costoPais;
        if (grupo == null) {
            addDataToEmptyList();
        } else {
            addDataToEmptyListByGroup(grupo);
        }
    }

    private void addDataToEmptyListByGroup(GruposEscalas grupo) {
        loadData();
        loadData1();
        //  tableGroup.setItems(createDataByGroup(grupo.getTarConceptosCollection().parallelStream().filter(item -> item.getTipo() == 2).collect(Collectors.toList())));
        // tableGroup1.setItems(createDataByGroup(grupo.getTarConceptosCollection().parallelStream().filter(item -> item.getTipo() == 3).collect(Collectors.toList())));
        llenaCampoTotal();
        llenaCampoCuba();
    }

    private ObservableList<TarConceptos> createDataByGroup(List<GrupoEscalaConceptos> list) {
        ObservableList<TarConceptos> tempList = FXCollections.observableArrayList();
        for (GrupoEscalaConceptos tarConceptosBases : list) {
            TarConceptos tarConceptos = new TarConceptos();
            TarConceptosBases conceptosBases = getConceptosBasesByID(tarConceptosBases.getConceptoId());
            tarConceptos.setId(conceptosBases.getId());
            tarConceptos.setDescripcion(conceptosBases.getDescripcion());
            tarConceptos.setValor(tarConceptosBases.getValor());
            tarConceptos.setTarifaId(1);
            tempList.add(tarConceptos);
        }
        return tempList;
    }

    private TarConceptosBases getConceptosBasesByID(int idC) {
        if (utilCalcSingelton.tarConceptosBasesList == null) {
            List<TarConceptosBases> temp = utilCalcSingelton.getTarConceptosBasesList();
            temp.size();
        }

        return utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getId() == idC).findFirst().get();
    }


    private void addDataToEmptyList() {
        if (utilCalcSingelton.tarConceptosBasesList == null) {
            List<TarConceptosBases> temp = utilCalcSingelton.getTarConceptosBasesList();
            temp.size();
        }
        loadData();
        loadData1();
        tableGroup.setItems(createDataFromBase(utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getState() == 2).collect(Collectors.toList())));
        tableGroup1.setItems(createDataBaseTarita(utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getState() == 3).collect(Collectors.toList())));

    }

    private ObservableList<TarConceptos> createDataFromBase(List<TarConceptosBases> list) {
        ObservableList<TarConceptos> tempList = FXCollections.observableArrayList();
        for (TarConceptosBases tarConceptosBases : list) {
            TarConceptos tarConceptos = new TarConceptos();
            tarConceptos.setId(tarConceptosBases.getId());
            tarConceptos.setDescripcion(tarConceptosBases.getDescripcion());
            tarConceptos.setValor(0.0);
            tarConceptos.setTarifaId(1);
            tempList.add(tarConceptos);

        }
        return tempList;
    }

    private ObservableList<TarConceptos> createDataBaseTarita(List<TarConceptosBases> list) {
        ObservableList<TarConceptos> tempList = FXCollections.observableArrayList();
        double costCuba = tableGroup.getItems().parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);
        double costCal = cpais + costCuba;
        double porVac = Double.parseDouble(configuracion.vacaciones.getText()) / 100 * costCal;
        for (TarConceptosBases tarConceptosBases : list) {
            TarConceptos tarConceptos = new TarConceptos();
            tarConceptos.setId(tarConceptosBases.getId());
            tarConceptos.setDescripcion(tarConceptosBases.getDescripcion());
            if (tarConceptosBases.getDescripcion().trim().equals("Costo Total")) {
                tarConceptos.setValor(costCal);
                tarConceptos.setTarifaId(1);
                tempList.add(tarConceptos);
            } else if (tarConceptosBases.getDescripcion().trim().equals("Vacaciones")) {
                tarConceptos.setValor(porVac);
                tarConceptos.setTarifaId(1);
                tempList.add(tarConceptos);
            } else {
                tarConceptos.setValor(0.0);
                tarConceptos.setTarifaId(1);
                tempList.add(tarConceptos);
            }

        }
        return tempList;
    }

    private void loadData() {
        grupCode.setCellValueFactory(new PropertyValueFactory<TarConceptos, String>("descripcion"));
        grupValor.setCellValueFactory(new PropertyValueFactory<TarConceptos, Double>("valor"));
    }

    private void loadData1() {
        grupCode1.setCellValueFactory(new PropertyValueFactory<TarConceptos, String>("descripcion"));
        grupValor1.setCellValueFactory(new PropertyValueFactory<TarConceptos, Double>("valor"));
    }

    public void createNewTarifaAction(ActionEvent event) {
        if (falg.equals("Save")) {
            if (!moneda.getText().isEmpty() && !tarDescripcion.getText().isEmpty()) {
                TarConceptos tarConceptos = new TarConceptos();
                tarConceptos.setDescripcion(tarDescripcion.getText().trim());
                tarConceptos.setValor(Double.parseDouble(moneda.getText()));
                tarConceptos.setTarifaId(1);
                tableGroup.getItems().add(tarConceptos);
                loadData();
                cleanFields();
                tableGroup1.setItems(createDataBaseTarita(utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getState() == 3).collect(Collectors.toList())));
                llenaCampoCuba();
            } else {
                catchErrorInForm();
            }
        } else if (falg.equals("Update")) {
            if (!moneda.getText().isEmpty() && !tarDescripcion.getText().isEmpty()) {
                updateIntable(tarDescripcion.getText().trim(), Double.parseDouble(moneda.getText()));
                loadData();
                cleanFields();
                tableGroup1.setItems(createDataBaseTarita(utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getState() == 3).collect(Collectors.toList())));
                llenaCampoCuba();
            } else {
                catchErrorInForm();
            }
        }
    }

    private void llenaCampoCuba() {
        double valCuba = tableGroup.getItems().parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);
        cuba_total.setText(String.valueOf(new BigDecimal(String.format("%.2f", valCuba)).doubleValue()));
    }


    public void createNewTarifaAction1(ActionEvent event) {
        if (falg.equals("Save")) {
            if (!moneda1.getText().isEmpty() && !tarDescripcion1.getText().isEmpty()) {
                TarConceptos tarConceptos = new TarConceptos();
                tarConceptos.setDescripcion(tarDescripcion1.getText().trim());
                tarConceptos.setValor(Double.parseDouble(moneda1.getText()));
                tarConceptos.setTarifaId(1);
                tableGroup1.getItems().add(tarConceptos);
                loadData1();
                cleanFields1();
                llenaCampoTotal();
            } else {
                catchErrorInForm();
            }
        } else if (falg.equals("Update")) {
            if (!moneda1.getText().isEmpty() && !tarDescripcion1.getText().isEmpty()) {
                updateIntable1(tarDescripcion1.getText().trim(), Double.parseDouble(moneda1.getText()));
                loadData1();
                cleanFields1();
                llenaCampoTotal();
            } else {
                catchErrorInForm();
            }
        }
    }

    private void llenaCampoTotal() {
        double valTotal = tableGroup1.getItems().parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);
        resumen.setText(String.valueOf(new BigDecimal(String.format("%.2f", valTotal)).doubleValue()));
    }

    private void catchErrorInForm() {
        moneda.setFocusColor(Color.RED);
        tarDescripcion.setFocusColor(Color.RED);
        utilCalcSingelton.showAlert("Complete los campos del fomulario para crear el nuevo concepto para la tarifa!!!", 2);
    }

    private void cleanFields() {
        tarDescripcion.clear();
        moneda.clear();
    }

    private void cleanFields1() {
        tarDescripcion1.clear();
        moneda1.clear();
    }


    private void updateIntable(String grupDes, double ini) {
        var select = tableGroup.getItems().get(grupoIndex);
        select.setDescripcion(grupDes);
        select.setValor(ini);
        tableGroup.getItems().set(grupoIndex, select);
        cleanFields();

        falg = "Save";
    }

    public void updateGrupoIntable(ActionEvent event) {
        falg = "Update";
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        grupoIndex = tableGroup.getSelectionModel().getSelectedIndex();
        tarDescripcion.setText(selectedGrupo.getDescripcion());
        moneda.setText(String.valueOf(selectedGrupo.getValor()));
    }

    public void handleRemoveToTable(ActionEvent event) {
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        tableGroup.getItems().remove(selectedGrupo);
        loadData();
    }

    private void updateIntable1(String grupDes, double ini) {
        var select = tableGroup1.getItems().get(grupoIndex);
        select.setDescripcion(grupDes);
        select.setValor(ini);
        tableGroup1.getItems().set(grupoIndex, select);
        cleanFields1();
        falg = "Save";
    }

    public void updateGrupoIntable1(ActionEvent event) {
        falg = "Update";
        selectedGrupo = tableGroup1.getSelectionModel().getSelectedItem();
        grupoIndex = tableGroup1.getSelectionModel().getSelectedIndex();
        tarDescripcion1.setText(selectedGrupo.getDescripcion());
        moneda1.setText(String.valueOf(selectedGrupo.getValor()));
    }

    public void handleRemoveToTable1(ActionEvent event) {
        selectedGrupo = tableGroup1.getSelectionModel().getSelectedItem();
        tableGroup.getItems().remove(selectedGrupo);
        loadData1();
    }

    public void closeWindows(ActionEvent event) {
        double calFinal = tableGroup1.getItems().parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);
        configuracion.addDataToFieldsForm(calFinal, tableGroup.getItems(), tableGroup1.getItems());
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();
    }


}
