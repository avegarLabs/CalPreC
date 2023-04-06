package views;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Subconcepto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateSubFormulaController implements Initializable {


    private static SessionFactory sf;
    @FXML
    private Label name;
    @FXML
    private JFXTextArea decrip;

    private Subconcepto conceptosgasto;

    public void updateConcepto(Subconcepto conceptosgasto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            conceptosgasto.setDescripcion(decrip.getText().trim());
            session.update(conceptosgasto);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void cargarConcepto(Subconcepto subconcepto) {
        conceptosgasto = new Subconcepto();
        conceptosgasto = subconcepto;
        decrip.setText(subconcepto.getDescripcion());

    }


    public void handleUpdateConcepto(ActionEvent event) {
        try {
            updateConcepto(conceptosgasto);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados correctamente!");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Datos error al actualizar los datos!");
            alert.showAndWait();
        }
    }

    public void handleActionAddSubSubconcepto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SubSubConceptos.fxml"));
            Parent proot = loader.load();

            SubSubFormulaController controller = (SubSubFormulaController) loader.getController();
            controller.pasarConcepto(conceptosgasto.getId());

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
