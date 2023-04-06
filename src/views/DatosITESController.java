package views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import models.*;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DatosITESController implements Initializable {

    public Obra obraModel;
    public ObservableList<String> taFinObservableList;
    @FXML
    private JFXTextArea especific;
    @FXML
    private JFXTextField yeard;
    @FXML
    private JFXTextField catalogo;
    @FXML
    private JFXTextField totalcgpo;
    @FXML
    private JFXTextField ocg;
    @FXML
    private JFXTextField ci;
    @FXML
    private JFXTextField gf;
    @FXML
    private JFXTextField precio;
    @FXML
    private JFXTextField utilidades;
    @FXML
    private JFXTextField gt;
    @FXML
    private JFXTextField tgo;
    @FXML
    private JFXTextField tcg;
    @FXML
    private JFXComboBox<String> units;
    @FXML
    private JFXTextField cant;

    @FXML
    private TableView<UnidadFin> tableUnits;
    @FXML
    private TableColumn<UnidadFin, String> fieldUnit;
    @FXML
    private TableColumn<UnidadFin, Double> fieldCant;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private double materiales;
    private double mano;
    private double equipos;
    private double aDouble;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taFinObservableList = FXCollections.observableArrayList();
        taFinObservableList.add("Camas - U");
        taFinObservableList.add("Habitaciones - U");
        taFinObservableList.add("Metros Cúbicos  - M3");
        taFinObservableList.add("Metros Cuadrádos - M2");
        taFinObservableList.add("Metros - M");
        units.setItems(taFinObservableList);
    }

    public void pasarDatos(Obra obra) {
        obraModel = obra;
        catalogo.setText(obra.getSalarioBySalarioId().getDescripcion().trim());

        try {
            materiales = getCostos(obraModel, 1);
            mano = getCostos(obraModel, 2);
            equipos = getCostos(obraModel, 3);
            calcConcepGastos();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(" Obra: " + obra.getCodigo() + " contienen errores. Revise sus datos por favor");
            alert.showAndWait();
        }

    }

    private void calcConcepGastos() {
        double val = 0.0;
        double c07 = 0.0;
        double c04 = 0.0;
        double c05 = 0.0;
        double c06 = 0.0;
        double c09 = 0.0;
        double c10 = 0.0;
        double c11 = 0.0;
        double c12 = 0.0;
        double c13 = 0.0;
        double c14 = 0.0;
        double c15 = 0.0;
        double c17 = 0.0;
        double c08 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 29).map(item -> item.getValor()).reduce(0.0, Double::sum);
        c04 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 26).map(item -> item.getValor()).reduce(0.0, Double::sum);
        c05 = materiales + mano + equipos + c04;
        c06 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 39).map(item -> item.getValor()).reduce(0.0, Double::sum);
        c07 = c06 + c05;
        totalcgpo.setText(String.valueOf(new BigDecimal(String.format("%.2f", c07)).doubleValue()));

        c09 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 30).map(item -> item.getValor()).reduce(0.0, Double::sum);
        ci.setText(String.valueOf(new BigDecimal(String.format("%.2f", c09)).doubleValue()));

        c10 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 31).map(item -> item.getValor()).reduce(0.0, Double::sum);
        ocg.setText(String.valueOf(new BigDecimal(String.format("%.2f", c10)).doubleValue()));

        c11 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 32).map(item -> item.getValor()).reduce(0.0, Double::sum);
        gf.setText(String.valueOf(new BigDecimal(String.format("%.2f", c11)).doubleValue()));

        c12 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 33).map(item -> item.getValor()).reduce(0.0, Double::sum);
        gt.setText(String.valueOf(new BigDecimal(String.format("%.2f", c12)).doubleValue()));

        c13 = c08 + c10 + c11 + c12;
        tgo.setText(String.valueOf(new BigDecimal(String.format("%.2f", c13)).doubleValue()));

        c14 = c05 + c09 + c10 + c11 + c12;
        tcg.setText(String.valueOf(new BigDecimal(String.format("%.2f", c14)).doubleValue()));

        c15 = obraModel.getEmpresaobraconceptosById().parallelStream().filter(item -> item.getConceptosgastoId() == 37).map(item -> item.getValor()).reduce(0.0, Double::sum);
        if (c15 < 0 || c15 == 0.0) {
            double rest = c14 - materiales - c13;
            c15 = 0.15 * rest;
            utilidades.setText(String.valueOf(new BigDecimal(String.format("%.2f", c15)).doubleValue()));
        } else {
            utilidades.setText(String.valueOf(new BigDecimal(String.format("%.2f", c15)).doubleValue()));
        }
        c17 = c13 + c14 + c15;
        precio.setText(String.valueOf(new BigDecimal(String.format("%.2f", c17)).doubleValue()));

    }

    public void generatedEvent(ActionEvent event) throws UnsupportedEncodingException {
        if (especific.getText() != null) {
            JSONObject uoObject = new JSONObject();
            uoObject.put("codigo", obraModel.getCodigo().trim());
            uoObject.put("descripcion", new String(obraModel.getDescripion().trim().getBytes(), "UTF-8"));
            uoObject.put("especificacion", new String(especific.getText().trim().getBytes(), "UTF-8"));
            uoObject.put("annos", yeard.getText().trim());
            uoObject.put("catalogo", catalogo.getText());
            uoObject.put("totalcgpo", totalcgpo.getText());
            uoObject.put("indirectos", ci.getText());
            uoObject.put("otroscg", ocg.getText());
            uoObject.put("financieros", gf.getText());
            uoObject.put("tributarios", gt.getText());
            uoObject.put("totalgo", tgo.getText());
            uoObject.put("totalcg", tcg.getText());
            uoObject.put("utilidades", utilidades.getText());
            uoObject.put("precio", precio.getText());
            uoObject.put("tipo", new String(obraModel.getTipoobraByTipoobraId().getDescripcion().trim().getBytes(), "UTF-8"));
            uoObject.put("costoMateriales", materiales);
            uoObject.put("costoManoObra", mano);
            uoObject.put("costoEquipos", equipos);

            org.json.simple.JSONArray valoresPorEspepecialidades = new org.json.simple.JSONArray();
            valoresPorEspepecialidades.addAll(createValoeresPorEspecialidadesList(obraModel));
            uoObject.put("valoresEspecialidades", valoresPorEspepecialidades);

            org.json.simple.JSONArray valoresPorUnidades = new org.json.simple.JSONArray();
            valoresPorUnidades.addAll(createValoresUnits());
            uoObject.put("unidadesFin", valoresPorUnidades);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar exportación");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
                    fileWriter.write(uoObject.toJSONString());
                    fileWriter.flush();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(obraModel.getDescripion() + " exportada");
                    alert.setContentText("Fichero .json dispobible en: " + file.getAbsolutePath());
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No puede export el fichero para el sistema ITE sin las especificaciones de la obra");
            alert.showAndWait();
        }
    }

    private org.json.simple.JSONArray createValoeresPorEspecialidadesList(Obra obra) throws UnsupportedEncodingException {
        org.json.simple.JSONArray valorestemp = new org.json.simple.JSONArray();
        List<EspecialidadesValorIte> especialidadesValorIteList = utilCalcSingelton.getValorEspecialidad(obra.getId());
        for (EspecialidadesValorIte especialidadesValorIte : especialidadesValorIteList) {
            JSONObject valorObject = new JSONObject();
            valorObject.put("code", especialidadesValorIte.getEspCode());
            valorObject.put("description", new String(especialidadesValorIte.getDescCode().trim().getBytes(), "UTF-8"));
            valorObject.put("valor", especialidadesValorIte.getValorTotal());
            valorestemp.add(valorObject);
        }
        return valorestemp;
    }

    private org.json.simple.JSONArray createValoresUnits() throws UnsupportedEncodingException {
        org.json.simple.JSONArray valorestemp = new org.json.simple.JSONArray();
        for (UnidadFin item : tableUnits.getItems()) {
            JSONObject valorObject = new JSONObject();
            valorObject.put("unidad", item.getName());
            valorObject.put("catidad", item.getCantidad());
            valorestemp.add(valorObject);
        }
        return valorestemp;
    }

    private double getCostos(Obra obra, int type) {
        double valor = 0.0;
        if (type == 1) {
            valor = obra.getUnidadobrasById().parallelStream().map(Unidadobra::getCostoMaterial).reduce(0.0, Double::sum);
        } else if (type == 2) {
            valor = obra.getUnidadobrasById().parallelStream().map(Unidadobra::getCostomano).reduce(0.0, Double::sum);
        } else {
            valor = obra.getUnidadobrasById().parallelStream().map(Unidadobra::getCostoequipo).reduce(0.0, Double::sum);
        }
        return valor;
    }

    public void loadData() {
        fieldUnit.setCellValueFactory(new PropertyValueFactory<UnidadFin, String>("name"));
        fieldCant.setCellValueFactory(new PropertyValueFactory<UnidadFin, Double>("cantidad"));
    }

    public void populatedUnitTable(ActionEvent event) {
        UnidadFin unidadFin = new UnidadFin(units.getValue(), Double.parseDouble(cant.getText()));
        tableUnits.getItems().add(unidadFin);
        loadData();
        clearFields();
    }

    public void clearFields() {
        units.getSelectionModel().clearSelection();
        cant.clear();
    }


}
