package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ConnectionModel;
import models.Subsubconcepto;
import models.Subsubsubconcepto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ListSubsubConceptosController implements Initializable {

    @FXML
    private TableView<Subsubsubconcepto> dataTable;
    @FXML
    private TableColumn<Subsubsubconcepto, String> code;
    private ObservableList<Subsubsubconcepto> datos;


    public void deleteConceptos(Subsubsubconcepto subconcepto) {
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

    public void loadData(Subsubconcepto subconcepto) {
        code.setCellValueFactory(new PropertyValueFactory<Subsubsubconcepto, String>("descripcion"));
        datos = FXCollections.observableArrayList();
        datos.addAll(subconcepto.getSubsubconceptosById());
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


}


