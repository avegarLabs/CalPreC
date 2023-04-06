package views;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.SuministrosView;
import models.UtilCalcSingelton;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchSuminitrosUpdateControllerAdd implements Initializable {

    BajoEspecificacionControllerComponetUpdateAdd suministrosInRVControllerSearch;
    @FXML
    private VBox box;
    @FXML
    private TableView<SuministrosView> dataTable;
    @FXML
    private TableColumn<SuministrosView, StringProperty> code;
    @FXML
    private TableColumn<SuministrosView, StringProperty> descrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> um;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> costomn;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> costomlc;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> peso;
    @FXML
    private JFXTextField filter;
    private SuministrosView suministrosView;
    private ObservableList<SuministrosView> suministrosViewObservableList;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTable.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {

                        suministrosView = dataTable.getSelectionModel().getSelectedItem();
                        suministrosInRVControllerSearch.getSuminitroResult(suministrosView);
                        Stage stage = (Stage) box.getScene().getWindow();
                        stage.close();
                    }
                }
        );

    }

    public void loadDatos() {
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        costomn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        peso.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("peso"));

    }


    public void pasController(BajoEspecificacionControllerComponetUpdateAdd suministrosInRVController) {

        loadDatos();
        suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList = utilCalcSingelton.getSuministrosViewObservableList(suministrosInRVController.obraToChek.getSalarioBySalarioId().getTag());
        dataTable.getItems().setAll(suministrosViewObservableList);

        suministrosInRVControllerSearch = suministrosInRVController;
        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(suministrosViewObservableList, p -> true);
        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }
}
