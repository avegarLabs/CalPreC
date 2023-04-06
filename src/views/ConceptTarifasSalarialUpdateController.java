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
import models.TarConceptos;
import models.TarConceptosBases;
import models.UtilCalcSingelton;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ConceptTarifasSalarialUpdateController implements Initializable {


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
    private JFXButton btnUpdate;

    private String falg;
    private TarConceptos selectedGrupo;
    private int grupoIndex;

    private TarifasSalarialUpdateController configuracion;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        falg = "Save";
    }

    public void passDataToConfiguracion(TarifasSalarialUpdateController configuracionController) {
        configuracion = configuracionController;
        loadData();
        ObservableList<TarConceptos> tarConceptosObservableList = FXCollections.observableArrayList();
        tarConceptosObservableList.addAll(configuracion.tarifa.getTarConceptosCollection());
        tableGroup.setItems(tarConceptosObservableList);

    }

    /*
    private void addDataToEmptyList() {
        if (utilCalcSingelton.tarConceptosBasesList != null) {
            loadData();
            tableGroup.setItems(createDataFromBase(utilCalcSingelton.tarConceptosBasesList.parallelStream().filter(item -> item.getState() == 1).collect(Collectors.toList())));
        } else {
            loadData();
            List<TarConceptosBases> temp = utilCalcSingelton.getTarConceptosBasesList();
            temp.size();
            tableGroup.setItems(createDataFromBase(temp.parallelStream().filter(item -> item.getState() == 1).collect(Collectors.toList())));
        }
    }
*/
    private ObservableList<TarConceptos> createDataFromBase(List<TarConceptosBases> list) {
        ObservableList<TarConceptos> tempList = FXCollections.observableArrayList();
        for (TarConceptosBases tarConceptosBases : list) {
            TarConceptos tarConceptos = new TarConceptos();
            tarConceptos.setDescripcion(tarConceptosBases.getDescripcion());
            tarConceptos.setValor(0.0);
            tarConceptos.setTarifaId(1);
            tempList.add(tarConceptos);

        }
        return tempList;
    }

    private void loadData() {
        grupCode.setCellValueFactory(new PropertyValueFactory<TarConceptos, String>("descripcion"));
        grupValor.setCellValueFactory(new PropertyValueFactory<TarConceptos, Double>("valor"));
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
            } else {
                catchErrorInForm();
            }
        } else if (falg.equals("Update")) {
            if (!moneda.getText().isEmpty() && !tarDescripcion.getText().isEmpty()) {
                updateIntable(tarDescripcion.getText().trim(), Double.parseDouble(moneda.getText()));
                loadData();
                cleanFields();
            } else {
                catchErrorInForm();
            }
        }

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

    public void closeWindows(ActionEvent event) {
        configuracion.addElementsToListConcepts(tableGroup.getItems());
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();
    }


}
