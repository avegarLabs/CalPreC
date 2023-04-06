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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JuegodeProductosController implements Initializable {

    private static SessionFactory sf;
    public ArrayList<Juegoproducto> juegoList;
    public ObservableList<JuegoProductoView> observableList;
    public Juegoproducto juegoproducto;
    public JuegoProductoView juegoProductoView;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private TableView<JuegoProductoView> dataTablePropio;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> codeP;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> descripP;
    @FXML
    private TableColumn<JuegoProductoView, StringProperty> umP;
    @FXML
    private TableColumn<JuegoProductoView, DoubleProperty> mnp;
    @FXML
    private TableColumn<JuegoProductoView, DoubleProperty> pesoP;
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
    private JFXComboBox<String> comboTarifas;

    /**
     * Bloques de métodos que continen operaciones de bases de datos
     */

    //Este metodo devuelve un la lista con todos loe elementos de la table suministros Compuestos del precons
    public ObservableList<JuegoProductoView> getPreconsJuegoProd() {

        observableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            juegoList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto ").list();
            juegoList.forEach(juego -> {
                juegoProductoView = new JuegoProductoView(juego.getId(), juego.getCodigo(), juego.getDescripcion(), juego.getUm(), juego.getPeso(), juego.getPreciomn(), juego.getPreciomlc(), juego.getPertenece());
                observableList.add(juegoProductoView);
            });

            tx.commit();
            session.close();
            return observableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    @FXML
    public void handleViewSumnistroComponentes() {
        juegoProductoView = dataTablePropio.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposicionJuego.fxml"));
            Parent proot = loader.load();
            ComposicionJuegoController composicionJuegoController = loader.getController();
            composicionJuegoController.pasarParametros(juegoProductoView);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createTable() {
        codeP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("codigo"));
        descripP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("descripcion"));
        umP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("um"));
        mnp.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("preciomn"));
        pesoP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("peso"));

        ObservableList<JuegoProductoView> datos = getPreconsJuegoProd();
        dataTablePropio.getItems().setAll(datos);
    }

    public void handleCleanViewByResolt(ActionEvent event) {
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        codeP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("codigo"));
        descripP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("descripcion"));
        umP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, StringProperty>("um"));
        mnp.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("preciomn"));
        pesoP.setCellValueFactory(new PropertyValueFactory<JuegoProductoView, DoubleProperty>("peso"));
        FilteredList<JuegoProductoView> filteredData = new FilteredList<JuegoProductoView>(reloadSuministros(tag.trim()), p -> true);

        SortedList<JuegoProductoView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTablePropio.comparatorProperty());

        dataTablePropio.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(juegoProductoView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }
                String descrip = juegoProductoView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasLst);

        createTable();

        FilteredList<JuegoProductoView> filteredData = new FilteredList<JuegoProductoView>(getPreconsJuegoProd(), p -> true);

        SortedList<JuegoProductoView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTablePropio.comparatorProperty());

        dataTablePropio.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(juegoProductoView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = juegoProductoView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    private ObservableList<JuegoProductoView> reloadSuministros(String tipo) {
        ObservableList<JuegoProductoView> suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList.addAll(observableList.parallelStream().filter(suministrosView -> suministrosView.getPertenece().trim().equals(tipo)).collect(Collectors.toList()));
        return suministrosViewObservableList;
    }
}

