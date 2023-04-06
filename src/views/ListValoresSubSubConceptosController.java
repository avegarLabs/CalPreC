package views;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ListValoresSubSubConceptosController implements Initializable {

    @FXML
    private TableView<SubconceptoValuesTable> dataTable;

    @FXML
    private TableColumn<SubconceptoValuesTable, String> code;

    @FXML
    private TableColumn<SubconceptoValuesTable, String> valor;

    @FXML
    private TableColumn<SubconceptoValuesTable, String> salario;

    private ObservableList<SubconceptoValuesTable> datos;

    @FXML
    private JFXButton btn_calcular;


    private Obra obraSend;
    private int idEmp;
    private int count;
    private int batchSize = 20;
    private ListValoresSubConceptosController listValoresSubConceptosController;
    private Subsubconcepto concepto;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    private static List<SubconceptoValuesTable> getSubconceptos(Subsubconcepto conceptosgasto, Obra obra, int idEmpresa) {
        List<SubconceptoValuesTable> datosList = new ArrayList<>();
        for (Subsubsubconcepto subconcepto : conceptosgasto.getSubsubconceptosById()) {
            double valor = obra.getEmpresaobrasubsubsubconceptoCollectionById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubsubsubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubsubsubconcepto::getValor).orElse(0.0);
            double salario = obra.getEmpresaobrasubsubsubconceptoCollectionById().parallelStream().filter(empresaobrasubconcepto -> empresaobrasubconcepto.getEmpresaconstructoraId() == idEmpresa && empresaobrasubconcepto.getSubsubsubconceptoId() == subconcepto.getId()).findFirst().map(Empresaobrasubsubsubconcepto::getValor).orElse(0.0);
            datosList.add(new SubconceptoValuesTable(subconcepto.getId(), subconcepto.getDescripcion(), String.valueOf(valor), String.valueOf(salario)));
        }
        return datosList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadData(ListValoresSubConceptosController controller, Subsubconcepto conceptosgasto, Obra obra, int idEmpresa) {
        obraSend = obra;
        idEmp = idEmpresa;
        concepto = conceptosgasto;
        listValoresSubConceptosController = controller;
        code.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("descrip"));
        valor.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("valor"));
        salario.setCellValueFactory(new PropertyValueFactory<SubconceptoValuesTable, String>("salario"));

        valor.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        valor.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
        });

        salario.setCellFactory(TextFieldTableCell.<SubconceptoValuesTable>forTableColumn());
        salario.setOnEditCommit((TableColumn.CellEditEvent<SubconceptoValuesTable, String> t) -> {
            ((SubconceptoValuesTable) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValor(t.getNewValue());
        });

        datos = FXCollections.observableArrayList();
        datos.addAll(getSubconceptos(conceptosgasto, obra, idEmpresa));
        dataTable.getItems().setAll(datos);
        dataTable.setEditable(true);

    }

    public void handleCalcular(ActionEvent event) {
        List<Empresaobrasubsubsubconcepto> empresaobrasubconceptos = new ArrayList<>();
        for (SubconceptoValuesTable item : dataTable.getItems()) {
            empresaobrasubconceptos.add(createEmpresaobrasubconcepto(item));
        }

        SubconceptoValuesTable sub = new SubconceptoValuesTable(listValoresSubConceptosController.dataTable.getSelectionModel().getSelectedItem().getId(), listValoresSubConceptosController.dataTable.getSelectionModel().getSelectedItem().getDescrip(), String.valueOf(utilCalcSingelton.getValuesOfSUBsubSubConcepts(concepto, obraSend, idEmp)), String.valueOf(utilCalcSingelton.getValuesOfSUBsubSubConceptsSalario(concepto, obraSend, idEmp)));
        listValoresSubConceptosController.dataTable.getItems().remove(listValoresSubConceptosController.dataTable.getSelectionModel().getSelectedItem());
        listValoresSubConceptosController.dataTable.getItems().add(sub);
        listValoresSubConceptosController.dataTable.getItems().stream().sorted(Comparator.comparing(SubconceptoValuesTable::getId));

        addEmpresaobrasubconceptos(empresaobrasubconceptos);
        Stage stage = (Stage) btn_calcular.getScene().getWindow();
        stage.close();
    }

    private Empresaobrasubsubsubconcepto createEmpresaobrasubconcepto(SubconceptoValuesTable item) {
        Empresaobrasubsubsubconcepto empresaobrasubconcepto = new Empresaobrasubsubsubconcepto();
        empresaobrasubconcepto.setObraId(obraSend.getId());
        empresaobrasubconcepto.setEmpresaconstructoraId(idEmp);
        empresaobrasubconcepto.setSubsubsubconceptoId(item.getId());
        empresaobrasubconcepto.setValor(Double.parseDouble(item.getValor()));
        empresaobrasubconcepto.setSalario(Double.parseDouble(item.getSalario()));
        return empresaobrasubconcepto;
    }

    public void addEmpresaobrasubconceptos(List<Empresaobrasubsubsubconcepto> empresaobrasubconceptos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;

            for (Empresaobrasubsubsubconcepto eo : empresaobrasubconceptos) {
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

}
