package views;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
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

public class ManoObraController implements Initializable {

    private static SessionFactory sf;
    public ManoObraController manoObraController;
    @FXML
    public JFXComboBox<String> comboTarifas;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<SuministrosView> dataTable;
    @FXML
    private TableColumn<SuministrosView, StringProperty> code;
    @FXML
    private TableColumn<SuministrosView, StringProperty> descrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> um;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> mn;
    @FXML
    private TableColumn<SuministrosView, StringProperty> escala;
    private ArrayList<Recursos> manoList;
    private ObservableList<SuministrosView> manoViewsList;
    private SuministrosView mano;
    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    @FXML
    private JFXButton btnadd;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    /**
     * Bloques de métodos que continen operaciones de bases de datos
     */
    public ObservableList<SuministrosView> getPreconsMano() {

        manoViewsList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            manoList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '2'").getResultList();
            for (Recursos manoobra : manoList) {
                manoViewsList.add(new SuministrosView(manoobra.getId(), manoobra.getCodigo(), manoobra.getDescripcion(), manoobra.getUm(), manoobra.getPeso(), Math.round(manoobra.getPreciomn() * 100d) / 100d, Math.round(manoobra.getPreciomlc() * 100d) / 100d, manoobra.getGrupoescala(), manoobra.getTipo(), manoobra.getPertenece(), 0.0, utilCalcSingelton.createCheckBox(manoobra.getActive())));
            }
            tx.commit();
            session.close();
            return manoViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    public void deleteManoObra(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos rec = session.get(Recursos.class, id);
            session.remove(rec);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void loadData() {

        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(450);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        escala.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("escala"));
        escala.setPrefWidth(50);

        manoViewsList = FXCollections.observableArrayList();
        manoViewsList = getPreconsMano();

        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(manoViewsList, p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasLst);

        manoObraController = this;
        loadData();
    }

    private ObservableList<SuministrosView> reloadSuministros(String tipo) {
        ObservableList<SuministrosView> suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList.addAll(manoViewsList.parallelStream().filter(suministrosView -> suministrosView.getPertene().trim().equals(tipo)).collect(Collectors.toList()));
        return suministrosViewObservableList;
    }


    public void handleCleanViewByResolt(ActionEvent event) {
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(450);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        escala.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("escala"));
        escala.setPrefWidth(50);

        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(reloadSuministros(tag.trim()), p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
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


    public void checkUser(Usuarios user) {

        if (user.getGruposId() != 1) {
            btnadd.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);
        }
    }

    public void handleManoPropioAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoManoObra.fxml"));
            Parent proot = loader.load();

            NuevoManoController manoController = loader.getController();
            manoController.parametros(manoObraController);


            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleDeleteMano(ActionEvent event) {

        mano = dataTable.getSelectionModel().getSelectedItem();

        deleteManoObra(mano.getId());

        loadData();
    }


    public void handleUpdateManoPropioAction(ActionEvent event) {

        mano = dataTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarManoObra.fxml"));
            Parent proot = loader.load();

            ActualizarManoController actualizarManoController = loader.getController();
            actualizarManoController.pasarParametros(manoObraController, mano);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}


