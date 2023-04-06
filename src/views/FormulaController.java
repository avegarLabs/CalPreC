package views;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.Conceptosgasto;
import models.ConnectionModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class FormulaController implements Initializable {


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

    private Integer id;
    private int idResol;

    public Integer addConcepto(Conceptosgasto conceptosgasto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer concepto = null;

        try {
            tx = session.beginTransaction();
            concepto = (Integer) session.save(conceptosgasto);

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
        id = null;
    }

    public void handleAddConcepto(ActionEvent event) {

        conceptosgasto = new Conceptosgasto();
        conceptosgasto.setDescripcion(conceptField.getText());
        conceptosgasto.setCoeficiente(Double.parseDouble(coefiField.getText()));
        conceptosgasto.setFormula(formulaField.getText());
        conceptosgasto.setCalcular(1);
        conceptosgasto.setPertence(idResol);

        id = addConcepto(conceptosgasto);

        if (id != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados correctamente!");
            alert.showAndWait();
        }

    }

    public void setidResolucion(int idRes) {
        idResol = idRes;
    }

}
