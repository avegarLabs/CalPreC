package views;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ListValoresConceptosController implements Initializable {

    private static Conceptosgasto concept;
    @FXML
    public TableView<SubconceptoValuesTable> dataTable;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> code;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> valor;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> salario;
    private ListValoresConceptosController valoresConceptosController;
    @FXML
    private JFXButton btn_calcular;

    private UtilCalcSingelton util = UtilCalcSingelton.getInstance();

    private ObservableList<SubconceptoValuesTable> datos;
    private Obra obraSend;
    private int idEmp;
    private int count;
    private int batchSize = 20;
    private Conceptosgasto concepto;
    private ActualizarConceptoEmpresaObraController actualizarConceptoEmpresaObraController;

    private static List<SubconceptoValuesTable> getSubconceptos(Conceptosgasto conceptosgasto, Obra obra, int idEmpresa) {
        concept = conceptosgasto;
        List<SubconceptoValuesTable> datosList = new ArrayList<>();
        for (Subconcepto subconcepto : conceptosgasto.getSubConceptosById()) {
            double valor = obra.getEmpresaobrasubconceptosById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubconcepto::getValor).orElse(0.0);
            double salario = obra.getEmpresaobrasubconceptosById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubconcepto::getSalario).orElse(0.0);
            datosList.add(new SubconceptoValuesTable(subconcepto.getId(), subconcepto.getDescripcion(), String.valueOf(valor), String.valueOf(salario)));
        }
        return datosList;
    }

    public void loadData(ActualizarConceptoEmpresaObraController controller, Conceptosgasto conceptosgasto, Obra obra, int idEmpresa) {
        actualizarConceptoEmpresaObraController = controller;
        obraSend = obra;
        idEmp = idEmpresa;
        concepto = conceptosgasto;
        code.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("descrip"));
        valor.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("valor"));
        salario.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("salario"));

        valor.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        valor.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            Subconcepto subconcepto = concept.getSubConceptosById().parallelStream().filter(c -> c.getId() == ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId()).findFirst().orElse(null);
            if (subconcepto.getSubSubConceptosById().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Seleccione la opción Desglozar costos para indicar el costo de cada uno de los componentes de este concepto de gasto");
                alert.showAndWait();
            } else if (subconcepto.getSubSubConceptosById().size() == 0) {
                ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
            }
        });
        salario.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        salario.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            Subconcepto subconcepto = concept.getSubConceptosById().parallelStream().filter(c -> c.getId() == ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId()).findFirst().orElse(null);
            if (subconcepto.getSubSubConceptosById().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Seleccione la opción Desglozar costos para indicar el costo de cada uno de los componentes de este concepto de gasto");
                alert.showAndWait();
            } else if (subconcepto.getSubSubConceptosById().size() == 0) {
                ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setSalario(t.getNewValue());
            }
        });

        datos = FXCollections.observableArrayList();
        datos.addAll(getSubconceptos(conceptosgasto, obra, idEmpresa));
        dataTable.getItems().setAll(datos);
        dataTable.setEditable(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        valoresConceptosController = this;
    }

    public void handleCalcular(ActionEvent event) {
        List<Empresaobrasubconcepto> empresaobrasubconceptos = new ArrayList<>();
        for (SubconceptoValuesTable item : dataTable.getItems()) {
            empresaobrasubconceptos.add(createEmpresaobrasubconcepto(item));
        }
        addEmpresaobrasubconceptos(empresaobrasubconceptos);

        if (concepto.getId() == 25) {
            double alquilerEquipo = util.getValuesOfSubConcepts(concepto, obraSend, idEmp);
            double equipos = Double.parseDouble(actualizarConceptoEmpresaObraController.valor.getText()) + alquilerEquipo;
            actualizarConceptoEmpresaObraController.valor.setText(String.format("%.2f", equipos));

        } else {
            actualizarConceptoEmpresaObraController.valor.setText(String.format("%.2f", util.getValuesOfSubConcepts(concepto, obraSend, idEmp)));
        }
        if (concepto.getDescripcion().trim().equals("Otros Gastos Directos del Proceso Productivo")) {
            actualizarConceptoEmpresaObraController.salario = util.getSalarioOfSubConcepts(concepto, obraSend, idEmp);
        }

        Stage stage = (Stage) btn_calcular.getScene().getWindow();
        stage.close();
    }

    private Empresaobrasubconcepto createEmpresaobrasubconcepto(SubconceptoValuesTable item) {
        Empresaobrasubconcepto empresaobrasubconcepto = new Empresaobrasubconcepto();
        empresaobrasubconcepto.setObraId(obraSend.getId());
        empresaobrasubconcepto.setEmpresaconstructoraId(idEmp);
        empresaobrasubconcepto.setSubconceptoId(item.getId());
        empresaobrasubconcepto.setValor(Double.parseDouble(item.getValor()));
        empresaobrasubconcepto.setSalario(Double.parseDouble(item.getSalario()));
        return empresaobrasubconcepto;
    }

    public void addEmpresaobrasubconceptos(List<Empresaobrasubconcepto> empresaobrasubconceptos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Empresaobrasubconcepto eo : empresaobrasubconceptos) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.saveOrUpdate(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public void handleDesglozarCostos(ActionEvent event) {
        Subconcepto subconcepto = concept.getSubConceptosById().parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListValoresSubConceptos.fxml"));
            Parent proot = loader.load();

            ListValoresSubConceptosController controller = (ListValoresSubConceptosController) loader.getController();
            controller.loadData(valoresConceptosController, subconcepto, obraSend, idEmp);

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
