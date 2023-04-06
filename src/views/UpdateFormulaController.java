package views;

import com.jfoenix.controls.JFXTextField;
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
import models.Conceptosgasto;
import models.ConnectionModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateFormulaController implements Initializable {


    private static SessionFactory sf;
    @FXML
    private Label name;
    @FXML
    private JFXTextField conceptField;
    @FXML
    private JFXTextField coefiField;
    @FXML
    private JFXTextField formulaField;
    private Conceptosgasto conceptosgasto;

    public void updateConcepto(int idConcepto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer concepto = null;

        try {
            tx = session.beginTransaction();

            conceptosgasto = session.get(Conceptosgasto.class, idConcepto);
            if (conceptosgasto != null) {

                conceptosgasto.setDescripcion(conceptField.getText());
                conceptosgasto.setCoeficiente(Double.parseDouble(coefiField.getText()));
                conceptosgasto.setFormula(formulaField.getText());

                session.update(conceptosgasto);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public Conceptosgasto getConcepto(int idConcepto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer concepto = null;

        try {
            tx = session.beginTransaction();

            conceptosgasto = session.get(Conceptosgasto.class, idConcepto);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return conceptosgasto;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void cargarConcepto(Integer idC) {

        conceptosgasto = getConcepto(idC);

        conceptField.setText(conceptosgasto.getDescripcion());
        coefiField.setText(String.valueOf(conceptosgasto.getCoeficiente()));
        formulaField.setText(conceptosgasto.getFormula());
    }


    public void handleUpdateConcepto(ActionEvent event) {

        updateConcepto(conceptosgasto.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos insertados correctamente!");
        alert.showAndWait();

    }

    public void handleActionAddSubconcepto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SubConceptos.fxml"));
            Parent proot = loader.load();

            SubFormulaController controller = (SubFormulaController) loader.getController();
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
