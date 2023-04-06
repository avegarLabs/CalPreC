package views;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import models.BrigadaConstructoraView;
import models.Brigadaconstruccion;
import models.ConnectionModel;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrigadaConstructoraController implements Initializable {

    private static SessionFactory sf;
    public Brigadaconstruccion brigadaconstruccionToUpdate;
    public int idBrigada;
    BrigadaConstructoraController brigadaConstructoraController;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<BrigadaConstructoraView> dataTable_brigada;
    @FXML
    private TableColumn<BrigadaConstructoraView, StringProperty> code;
    @FXML
    private TableColumn<BrigadaConstructoraView, StringProperty> descrip;
    @FXML
    private TableColumn<BrigadaConstructoraView, StringProperty> empres;
    @FXML
    private JFXButton btn_add;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionsList;
    private ObservableList<BrigadaConstructoraView> observableBrigadaConstructoraViewsList;
    private BrigadaConstructoraView brigadaConstructoraView;
    @FXML
    private MenuItem option1;

    @FXML
    private MenuItem option2;


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<BrigadaConstructoraView> getBrigadaCont() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaconstruccionsList = new ArrayList<Brigadaconstruccion>();
            observableBrigadaConstructoraViewsList = FXCollections.observableArrayList();
            brigadaconstruccionsList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion ").list();
            for (Brigadaconstruccion brigada : brigadaconstruccionsList) {
                observableBrigadaConstructoraViewsList.add(new BrigadaConstructoraView(brigada.getId(), brigada.getCodigo(), brigada.getDescripcion(), brigada.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion()));
            }

            tx.commit();
            session.close();
            return observableBrigadaConstructoraViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /* Method to DELETE an Brigada de Contruccion from the records */
    public void deleteBrigada(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Brigadaconstruccion brigadaconstruccion = session.get(Brigadaconstruccion.class, id);

            session.delete(brigadaconstruccion);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    void handleButtonAction(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaBrigada.fxml"));
            Parent proot = loader.load();

            NuevaBrigadaController controller = loader.getController();
            controller.sendController(brigadaConstructoraController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void loadDatosBrigada() {
        code.setCellValueFactory(new PropertyValueFactory<BrigadaConstructoraView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<BrigadaConstructoraView, StringProperty>("descripcion"));
        descrip.setPrefWidth(250);
        empres.setCellValueFactory(new PropertyValueFactory<BrigadaConstructoraView, StringProperty>("empresaCode"));
        empres.setPrefWidth(200);

        ObservableList<BrigadaConstructoraView> datos = FXCollections.observableArrayList();
        datos = getBrigadaCont();


        FilteredList<BrigadaConstructoraView> filteredData = new FilteredList<BrigadaConstructoraView>(datos, p -> true);

        SortedList<BrigadaConstructoraView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_brigada.comparatorProperty());

        dataTable_brigada.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(brigadaConstructoraView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = brigadaConstructoraView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        brigadaConstructoraController = this;
        loadDatosBrigada();


    }


    public void handleBrigadaUpdateAction(ActionEvent event) {

        brigadaconstruccionToUpdate = brigadaconstruccionsList.parallelStream().filter(br -> br.getDescripcion().equals(dataTable_brigada.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateBrigada.fxml"));
            Parent proot = loader.load();
            ActualizarBrigadaController actualizarBrigadaController = loader.getController();
            actualizarBrigadaController.pasarParametros(brigadaConstructoraController, brigadaconstruccionToUpdate);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleBrigadaDeleteAction(ActionEvent event) {

        brigadaConstructoraView = dataTable_brigada.getSelectionModel().getSelectedItem();
        deleteBrigada(brigadaConstructoraView.getId());
        loadDatosBrigada();
    }

    public void checkUser(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {

            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);

        }
    }
}
