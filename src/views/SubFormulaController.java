package views;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.ConnectionModel;
import models.Subconcepto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class SubFormulaController implements Initializable {


    private static SessionFactory sf;
    @FXML
    private Label name;

    @FXML
    private JFXTextArea conceptField;

    @FXML
    private Subconcepto conceptosgasto;
    private int idC;

    public Integer addConcepto(Subconcepto subconcepto) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer concepto = null;

        try {
            tx = session.beginTransaction();
            concepto = (Integer) session.save(subconcepto);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return concepto;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleAddConcepto(ActionEvent event) {

        conceptosgasto = new Subconcepto();
        conceptosgasto.setDescripcion(conceptField.getText());
        conceptosgasto.setValor(0.0);
        conceptosgasto.setConceptoId(idC);

        Integer id = addConcepto(conceptosgasto);

        if (id != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados correctamente!");
            alert.showAndWait();

        }

    }

    public void pasarConcepto(int idConcepto) {
        idC = idConcepto;
    }


}
