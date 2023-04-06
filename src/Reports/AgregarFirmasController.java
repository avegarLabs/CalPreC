package Reports;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Firmas;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import views.EntidadController;

import java.net.URL;
import java.util.ResourceBundle;

public class AgregarFirmasController implements Initializable {

    private static SessionFactory sf;
    public EntidadController entidadController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXButton btn_close;


    /**
     * Metodo para actualizar cuadrilla
     */
    public Integer createFirma() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idF = null;
        try {
            tx = session.beginTransaction();
            Firmas firmas = new Firmas();
            firmas.setName(field_codigo.getText());
            firmas.setCargo(text_descripcion.getText());
            idF = (Integer) session.save(firmas);
            tx.commit();
            session.close();
            return idF;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return idF;
    }


    @FXML
    void handeleUpdateAction(ActionEvent event) {
        if (field_codigo.getText() != " " && text_descripcion.getText() != null) {
            Integer id = createFirma();

            if (id != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ok");
                alert.setContentText("Datos Insertados!!");
                alert.showAndWait();
            }
            clearField();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Complete elo campos del formulario");
            alert.showAndWait();
        }


    }


    public void clearField() {

        field_codigo.clear();
        text_descripcion.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleClose(ActionEvent event) {

        entidadController.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
