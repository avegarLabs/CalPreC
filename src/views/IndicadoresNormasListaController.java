package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class IndicadoresNormasListaController implements Initializable {


    @FXML
    private Labeled label_title;

    @FXML
    private JFXComboBox<String> field_codigo;

    @FXML
    private TableView<TableIndicadorNorma> tableCodes;

    @FXML
    private TableColumn<TableIndicadorNorma, String> codeRV;

    @FXML
    private TableColumn<TableIndicadorNorma, String> descripRV;

    @FXML
    private TableColumn<TableIndicadorNorma, String> umRV;

    @FXML
    private JFXComboBox<String> listResolt;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();


    @FXML
    private JFXButton btn_add;

    private IndicadoresRespository indicadoresRespository;
    private int pertenece;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indicadoresRespository = new IndicadoresRespository();

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(utilCalcSingelton.getSalarios().parallelStream().filter(salario -> !salario.getDescripcion().equals("Resolución 30")).map(Salario::getDescripcion).collect(Collectors.toList()));
        listResolt.setItems(observableList);
    }

    public void addInticadotestoList(ActionEvent e) {
        pertenece = 0;
        ObservableList<String> listIndicadoresCreated = FXCollections.observableArrayList();
        if (listResolt.getValue().equals("Resolución 86")) {
            pertenece = 2;
            List<String> filterlist = indicadoresRespository.getIndicadoresGroupList(pertenece).parallelStream().filter(item -> item.getPertenece() == pertenece).map(IndicadorGrup::getDescipcion).collect(Collectors.toList());
            listIndicadoresCreated.addAll(filterlist);
            field_codigo.setItems(listIndicadoresCreated);
        } else if (listResolt.getValue().equals("Resolución 266")) {
            pertenece = 3;
            List<String> filterlist = indicadoresRespository.getIndicadoresGroupList(pertenece).parallelStream().filter(item -> item.getPertenece() == pertenece).map(IndicadorGrup::getDescipcion).collect(Collectors.toList());
            listIndicadoresCreated.addAll(filterlist);
            field_codigo.setItems(listIndicadoresCreated);
        }
    }

    public void handlePopulatedTable(ActionEvent event) {
        codeRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("code"));
        descripRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("descrip"));
        umRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("um"));
        IndicadorGrup indicator = indicadoresRespository.indicadorGrupList.parallelStream().filter(item -> item.getDescipcion().trim().equals(field_codigo.getValue())).findFirst().get();

        ObservableList<TableIndicadorNorma> tableIndicadorNormaObservableList = FXCollections.observableArrayList();
        tableIndicadorNormaObservableList = indicadoresRespository.getNormasGroupList(indicator.getId());
        tableCodes.setItems(tableIndicadorNormaObservableList);
    }

    public void deleteAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar el indicados ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar) {
            IndicadorGrup indicator = indicadoresRespository.indicadorGrupList.parallelStream().filter(item -> item.getDescipcion().trim().equals(field_codigo.getValue())).findFirst().get();
            indicadoresRespository.deleteIndicadores(indicator.getId());
        }
    }

    public void deleteNormasToTable(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar el indicados ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar) {
            IndicadorGrup indicator = indicadoresRespository.indicadorGrupList.parallelStream().filter(item -> item.getDescipcion().trim().equals(field_codigo.getValue())).findFirst().get();
            NormasInd norma = indicator.getNormasById().parallelStream().filter(item -> item.getCodenorna().trim().equals(tableCodes.getSelectionModel().getSelectedItem().getCode())).findFirst().get();
            indicadoresRespository.deleteNormas(norma.getId());

            handlePopulatedTable(event);
        }
    }


}
