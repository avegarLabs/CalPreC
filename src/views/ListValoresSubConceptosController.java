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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ListValoresSubConceptosController implements Initializable {
    private static Subconcepto subConcept;
    @FXML
    public TableView<SubconceptoValuesTable> dataTable;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> code;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> valor;
    @FXML
    public TableColumn<SubconceptoValuesTable, String> salario;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    private ObservableList<SubconceptoValuesTable> datos;
    private Obra obraSend;
    private int idEmp;
    private int count;
    private int batchSize = 20;
    private ListValoresSubConceptosController listValoresSubConceptosController;
    @FXML
    private JFXButton btn_calcular;
    private Subconcepto concepto;
    private ListValoresConceptosController valoresConceptosController;

    private static List<SubconceptoValuesTable> getSubconceptos(Subconcepto conceptosgasto, Obra obra, int idEmpresa) {
        subConcept = conceptosgasto;
        List<SubconceptoValuesTable> datosList = new ArrayList<>();
        for (Subsubconcepto subconcepto : conceptosgasto.getSubSubConceptosById()) {
            double valor = obra.getEmpresaobrasubsubconceptosById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubsubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubsubconcepto::getValor).orElse(0.0);
            double salario = obra.getEmpresaobrasubsubconceptosById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubsubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubsubconcepto::getSalario).orElse(0.0);
            datosList.add(new SubconceptoValuesTable(subconcepto.getId(), subconcepto.getDescripcion(), String.valueOf(valor), String.valueOf(salario
            )));
        }
        return datosList;
    }

    public void loadData(ListValoresConceptosController valoresController, Subconcepto conceptosgasto, Obra obra, int idEmpresa) {
        concepto = conceptosgasto;
        obraSend = obra;
        idEmp = idEmpresa;
        code.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("descrip"));
        valor.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("valor"));
        salario.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("salario"));
        valoresConceptosController = valoresController;

        valor.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        valor.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            Subsubconcepto subconcepto = subConcept.getSubSubConceptosById().parallelStream().filter(c -> c.getId() == ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId()).findFirst().orElse(null);
            if (subconcepto.getSubsubconceptosById().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Seleccione la opción Desglozar costos para indicar el costo de cada uno de los componentes de este concepto de gasto");
                alert.showAndWait();
            } else if (subconcepto.getSubsubconceptosById().size() == 0) {
                ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
            }
        });

        salario.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        salario.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            Subsubconcepto subconcepto = subConcept.getSubSubConceptosById().parallelStream().filter(c -> c.getId() == ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId()).findFirst().orElse(null);
            if (subconcepto.getSubsubconceptosById().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Seleccione la opción Desglozar costos para indicar el costo de cada uno de los componentes de este concepto de gasto");
                alert.showAndWait();
            } else if (subconcepto.getSubsubconceptosById().size() == 0) {
                ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
            }
        });

        datos = FXCollections.observableArrayList();
        datos.addAll(getSubconceptos(conceptosgasto, obra, idEmpresa));
        dataTable.getItems().setAll(datos);
        dataTable.setEditable(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listValoresSubConceptosController = this;
    }

    public void handleCalcular(ActionEvent event) {
        List<Empresaobrasubsubconcepto> empresaobrasubconceptos = new ArrayList<>();
        for (SubconceptoValuesTable item : dataTable.getItems()) {
            empresaobrasubconceptos.add(createEmpresaobrasubconcepto(item));
        }
        addEmpresaobrasubconceptos(empresaobrasubconceptos);

        SubconceptoValuesTable sub = new SubconceptoValuesTable(valoresConceptosController.dataTable.getSelectionModel().getSelectedItem().getId(), valoresConceptosController.dataTable.getSelectionModel().getSelectedItem().getDescrip(), String.valueOf(utilCalcSingelton.getValuesOfsubSubConcepts(concepto, obraSend, idEmp)), String.valueOf(utilCalcSingelton.getValuesOfsubSubConceptsSalario(concepto, obraSend, idEmp)));
        valoresConceptosController.dataTable.getItems().remove(valoresConceptosController.dataTable.getSelectionModel().getSelectedItem());
        valoresConceptosController.dataTable.getItems().add(sub);
        valoresConceptosController.dataTable.getItems().stream().sorted(Comparator.comparing(SubconceptoValuesTable::getId));

        Stage stage = (Stage) btn_calcular.getScene().getWindow();
        stage.close();
    }

    private Empresaobrasubsubconcepto createEmpresaobrasubconcepto(SubconceptoValuesTable item) {
        Empresaobrasubsubconcepto empresaobrasubconcepto = new Empresaobrasubsubconcepto();
        empresaobrasubconcepto.setObraId(obraSend.getId());
        empresaobrasubconcepto.setEmpresaconstructoraId(idEmp);
        empresaobrasubconcepto.setSubsubconceptoId(item.getId());
        empresaobrasubconcepto.setValor(Double.parseDouble(item.getValor()));
        empresaobrasubconcepto.setSalario(Double.parseDouble(item.getSalario()));
        return empresaobrasubconcepto;
    }

    public void addEmpresaobrasubconceptos(List<Empresaobrasubsubconcepto> empresaobrasubconceptos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Empresaobrasubsubconcepto eo : empresaobrasubconceptos) {
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
        Subsubconcepto subconcepto = subConcept.getSubSubConceptosById().parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListValoresSubSubConceptos.fxml"));
            Parent proot = loader.load();

            ListValoresSubSubConceptosController controller = (ListValoresSubSubConceptosController) loader.getController();
            controller.loadData(listValoresSubConceptosController, subconcepto, obraSend, idEmp);

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
