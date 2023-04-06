package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SuministrosSemielaboradosController implements Initializable {

    public SuministrosSemielaboradosController controller;

    public ArrayList<Suministrossemielaborados> productossemielaboradosList;
    public ObservableList<SuministrosSemielaboradosView> semielaboradosViewsObservableList;
    public Suministrossemielaborados selectedProductossemielaborados;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private Label label_title;
    @FXML
    private JFXCheckBox checkagregados;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<SuministrosSemielaboradosView> dataTable;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> code;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> descrip;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, StringProperty> um;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, DoubleProperty> mn;
    @FXML
    private TableColumn<SuministrosSemielaboradosView, DoubleProperty> peso;
    @FXML
    private JFXComboBox<String> comboTarifas;
    private String pertenece;

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(450);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        peso.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("peso"));
        peso.setPrefWidth(100);

        ObservableList<SuministrosSemielaboradosView> datos = getPreconsProductosSemielaborados();

        FilteredList<SuministrosSemielaboradosView> filteredData = new FilteredList<SuministrosSemielaboradosView>(datos, p -> true);

        SortedList<SuministrosSemielaboradosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

    }

    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasLst);

        controller = this;
    }

    /**
     * Bloques de métodos que continen operaciones de bases de datos
     */
    //Este metodo devuelve un la lista con todos loe elementos de la table suministros Compuestos del precons
    public ObservableList<SuministrosSemielaboradosView> getPreconsProductosSemielaborados() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            semielaboradosViewsObservableList = FXCollections.observableArrayList();
            productossemielaboradosList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec !=null ").list();
            for (Suministrossemielaborados suminist : productossemielaboradosList) {
                semielaboradosViewsObservableList.add(new SuministrosSemielaboradosView(suminist.getId(), suminist.getCodigo(), suminist.getDescripcion(), suminist.getUm(), Math.round(suminist.getPreciomn() * 100d) / 100d, Math.round(suminist.getPreciomlc() * 100d) / 100d, Math.round(suminist.getPeso() * 100d) / 100d, suminist.getPertenec()));
            }
            tx.commit();
            session.close();
            return semielaboradosViewsObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public void checkUsuario(Usuarios usuarios) {

        if (usuarios.getGruposId() != 1) {
            btn_add.setDisable(true);
        }
    }

    @FXML
    public void handleViewSumnistroComponentes() {
        selectedProductossemielaborados = productossemielaboradosList.parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposicionSemilebaorado.fxml"));
            Parent proot = loader.load();
            ComposicionSemielaboradoController composicionSemielaboradoController = loader.getController();
            composicionSemielaboradoController.pasarParametros(controller, selectedProductossemielaborados);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setTitle("Composición del suministro semielaborado: " + selectedProductossemielaborados.getCodigo());
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event -> {
                loadData();
                comboTarifas.getSelectionModel().clearSelection();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleViewNuevoSuministrosSemielaborado() {


        if (comboTarifas.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Seleccione una resolución para crear el nuevo semielaborado");
            alert.showAndWait();

        } else {
            String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
            pertenece = tag;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoSemilebaorado.fxml"));
                Parent proot = loader.load();
                NuevoSemielaboradoController controller = loader.getController();
                System.out.println(pertenece);
                controller.pasarParametros(0, pertenece);
                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.setResizable(false);
                stage.show();

                stage.setOnCloseRequest(event -> {
                    loadData();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public void handleCleanViewByResolt(ActionEvent event) {
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        code.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(450);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        peso.setCellValueFactory(new PropertyValueFactory<SuministrosSemielaboradosView, DoubleProperty>("peso"));
        peso.setPrefWidth(100);


        FilteredList<SuministrosSemielaboradosView> filteredData = new FilteredList<SuministrosSemielaboradosView>(reloadSuministros(tag.trim()), p -> true);
        SortedList<SuministrosSemielaboradosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    private ObservableList<SuministrosSemielaboradosView> reloadSuministros(String tipo) {
        ObservableList<SuministrosSemielaboradosView> suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList.addAll(semielaboradosViewsObservableList.parallelStream().filter(suministrosView -> suministrosView.getPertenece().trim().equals(tipo)).collect(Collectors.toList()));
        return suministrosViewObservableList;
    }

    public void handleOptimizar(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("CalPreC realizará adecuaciones en los suministros semielaborados en la base de datos, por favor espere");
        alert.showAndWait();
        try {
            utilCalcSingelton.getSemileaboradosEmptyStructure();

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setContentText("Trabajo terminádo!!!");
            alerta.showAndWait();

        } catch (Exception e) {
            Alert alerta1 = new Alert(Alert.AlertType.ERROR);
            alerta1.setContentText("Error al optomizar los suministros !!!" + e.getMessage());
            alerta1.showAndWait();
        }

    }

    @FXML
    public void handleViewClonarSuministroSemielaborado() {
        selectedProductossemielaborados = productossemielaboradosList.parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClonarSuministroSemielaborado.fxml"));
            Parent proot = loader.load();
            ClonarSuministroSemielaboradoController clonarSuministroSemielaboradoController = loader.getController();
            clonarSuministroSemielaboradoController.pasarParametros(controller, selectedProductossemielaborados);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setTitle("Clonar suministro semielaborado (" + selectedProductossemielaborados.getCodigo() + ")");
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event -> {
                loadData();
                comboTarifas.getSelectionModel().clearSelection();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
