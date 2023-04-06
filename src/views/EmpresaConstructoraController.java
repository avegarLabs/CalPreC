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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.EmpresaConstructoraView;
import models.Empresaconstructora;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class EmpresaConstructoraController implements Initializable {

    private static SessionFactory sf;
    public EmpresaConstructoraController empresaConstructoraController;
    public Empresaconstructora empresaconstructoraToUpdate;
    public int idEmpresa;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private TableView<EmpresaConstructoraView> dataTable_Empresa;
    @FXML
    private TableColumn<EmpresaConstructoraView, StringProperty> code;
    @FXML
    private TableColumn<EmpresaConstructoraView, StringProperty> descrip;
    @FXML
    private TableColumn<EmpresaConstructoraView, StringProperty> c731;
    @FXML
    private TableColumn<EmpresaConstructoraView, StringProperty> c822;
    @FXML
    private TableColumn<EmpresaConstructoraView, StringProperty> pbruta;
    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ObservableList<EmpresaConstructoraView> observableEmpresaConstructoraViewsList;
    private EmpresaConstructoraView empresaConstructoraView;
    @FXML
    private MenuItem option1;

    @FXML
    private MenuItem option2;


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<EmpresaConstructoraView> getEmpresas() {

        empresaconstructorasList = new ArrayList<Empresaconstructora>();
        observableEmpresaConstructoraViewsList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").getResultList();
            for (Empresaconstructora empresa : empresaconstructorasList) {
                observableEmpresaConstructoraViewsList.add(new EmpresaConstructoraView(empresa.getId(), empresa.getCodigo(), empresa.getDescripcion(), empresa.getCuenta731(), empresa.getCuenta822(), empresa.getPbruta()));
            }

            tx.commit();
            session.close();
            return observableEmpresaConstructoraViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /* Method to DELETE an Empresa from the records */
    public void deleteEmpresa(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Empresaconstructora empresa = session.get(Empresaconstructora.class, id);

            Query query = session.createQuery("DELETE FROM Empresagastos where empresaconstructoraId =: id").setParameter("id", empresa.getId());
            query.executeUpdate();

            Query query1 = session.createQuery("DELETE FROM Empresaobraconcepto WHERE empresaconstructoraId =: idEm").setParameter("idEm", empresa.getId());
            query1.executeUpdate();

            //  Query query2 = session.createQuery("")

            session.delete(empresa);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void loadData() {

        code.setCellValueFactory(new PropertyValueFactory<EmpresaConstructoraView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<EmpresaConstructoraView, StringProperty>("descripcion"));
        descrip.setPrefWidth(225);
        c731.setCellValueFactory(new PropertyValueFactory<EmpresaConstructoraView, StringProperty>("cuenta731"));
        c731.setPrefWidth(100);
        c822.setCellValueFactory(new PropertyValueFactory<EmpresaConstructoraView, StringProperty>("cuenta822"));
        c822.setPrefWidth(100);
        pbruta.setCellValueFactory(new PropertyValueFactory<EmpresaConstructoraView, StringProperty>("pbruta"));
        pbruta.setPrefWidth(100);

        ObservableList<EmpresaConstructoraView> datos = FXCollections.observableArrayList();
        datos = getEmpresas();


        FilteredList<EmpresaConstructoraView> filteredData = new FilteredList<EmpresaConstructoraView>(datos, p -> true);

        SortedList<EmpresaConstructoraView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_Empresa.comparatorProperty());

        dataTable_Empresa.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(empresaConstructoraView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = empresaConstructoraView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        empresaConstructoraController = this;
        loadData();

    }

    public void handleButtonAction(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaEmpresa.fxml"));
            Parent proot = loader.load();

            NuevaEmpresaController controller = loader.getController();
            controller.setPasarController(empresaConstructoraController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleEmpresaUpdateAction(ActionEvent event) {

        empresaconstructorasList.forEach(empresa -> {
            if (empresa.getCodigo().contains(dataTable_Empresa.getSelectionModel().getSelectedItem().getCodigo())) {
                empresaconstructoraToUpdate = empresa;
            }
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarEmpresa.fxml"));
            Parent proot = loader.load();
            ActualizarEmpresaController actualizarEmpresaController = loader.getController();
            actualizarEmpresaController.pasarParametros(empresaConstructoraController, empresaconstructoraToUpdate);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                loadData();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void handleEmpresaDeleteAction(ActionEvent event) {
        empresaConstructoraView = dataTable_Empresa.getSelectionModel().getSelectedItem();
        deleteEmpresa(empresaConstructoraView.getId());
        loadData();
    }


    public void checkUser(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {

            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);
        }
    }


}
