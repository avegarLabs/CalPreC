package views;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Subconcepto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ListConceptosController implements Initializable {

    @FXML
    private TableView<Subconcepto> dataTable;
    @FXML
    private TableColumn<Subconcepto, String> code;
    private ObservableList<Subconcepto> datos;


    public ObservableList<Subconcepto> getConceptosGastos(int idCon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            ObservableList<Subconcepto> list = FXCollections.observableArrayList();
            list.addAll(session.createQuery("FROM Subconcepto WHERE conceptoId =: idC").setParameter("idC", idCon).getResultList());

            tx.commit();
            session.close();
            return list;
        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public void deleteConceptos(Subconcepto subconcepto) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(subconcepto);
            tx.commit();
            session.close();

        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void loadData(int idConcepto) {
        code.setCellValueFactory(new PropertyValueFactory<Subconcepto, String>("descripcion"));
        datos = FXCollections.observableArrayList();
        datos = getConceptosGastos(idConcepto);
        dataTable.getItems().setAll(datos);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void deleteConcepts(ActionEvent event) {
        try {
            deleteConceptos(dataTable.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Error al eliminar el concepto");
            alert.showAndWait();
        }


    }

    public void editConcepto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSubConceptoConceptos.fxml"));
            Parent proot = loader.load();

            UpdateSubFormulaController controller = (UpdateSubFormulaController) loader.getController();
            controller.cargarConcepto(dataTable.getSelectionModel().getSelectedItem());

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

    public void editListSubConcepto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListSubConceptos.fxml"));
            Parent proot = loader.load();

            ListSubConceptosController controller = (ListSubConceptosController) loader.getController();
            controller.loadData(dataTable.getSelectionModel().getSelectedItem());

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
