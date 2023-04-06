package views;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import models.ConnectionModel;
import models.Subsubsubconcepto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class SubSubSubFormulaController implements Initializable {


    @FXML
    private JFXTextArea conceptField;
    private int idC;

    public Integer addConcepto(Subsubsubconcepto subconcepto) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer concepto = null;

        try {
            tx = session.beginTransaction();
            concepto = (Integer) session.save(subconcepto);

            tx.commit();
            session.close();
            return concepto;
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

    public void handleAddConcepto() {
        Subsubsubconcepto conceptosgasto = new Subsubsubconcepto();
        conceptosgasto.setDescripcion(conceptField.getText());
        conceptosgasto.setValor(0.0);
        conceptosgasto.setSubconceptoId(idC);

        Integer id = addConcepto(conceptosgasto);

        if (id != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos insertados correctamente!");
            alert.showAndWait();

            conceptField.clear();

        }

    }

    public void pasarConcepto(int idConcepto) {
        idC = idConcepto;
    }


}
