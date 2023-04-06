package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.ConceptosGastosView;
import models.ConnectionModel;
import models.Empresagastos;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarFormulaController implements Initializable {

    private static SessionFactory sf;
    private ConceptosController controller;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField concepto;
    @FXML
    private JFXTextField corficiente;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField formula;
    private int idEmp;
    private int idConcpt;
    @FXML
    private JFXCheckBox calcular;
    @FXML
    private JFXTextField fielporcient;
    @FXML
    private Label labeld;
    private Empresagastos empresagastos;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        calcular.setOnMouseClicked(event -> {
            if (calcular.isSelected()) {
                if (idConcpt == 4) {
                    formula.setText("1+2+3");
                } else if (idConcpt == 5) {
                    formula.setText("1");
                } else if (idConcpt == 7) {
                    formula.setText("6");
                }
            }
        });

    }

    private void actualizarConcepto(int idConcpt, int idEmp) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresagastos = (Empresagastos) session.createQuery("FROM Empresagastos WHERE empresaconstructoraId =: idEmpresa AND conceptosgastoId =: idConcep").setParameter("idEmpresa", idEmp).setParameter("idConcep", idConcpt).getSingleResult();
            if (empresagastos != null) {
                empresagastos.setCoeficiente(Double.parseDouble(corficiente.getText()));
                empresagastos.setFormula(formula.getText());
                if (!fielporcient.getText().isEmpty()) {
                    empresagastos.setPorciento(Double.parseDouble(fielporcient.getText()));
                }
                if (calcular.isSelected() == true) {
                    empresagastos.setCalcular(1);
                } else {
                    empresagastos.setCalcular(0);
                }
                session.persist(empresagastos);
            }


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


    public void cargarDatos(ConceptosController conceptosController, ConceptosGastosView conceptosGastosView, int idEmpresa) {

        controller = conceptosController;
        idEmp = idEmpresa;
        idConcpt = conceptosGastosView.getId();
        concepto.setText(conceptosGastosView.getDescripcion());
        corficiente.setText(String.valueOf(conceptosGastosView.getCoeficiente()));
        formula.setText(conceptosGastosView.getFormula().trim());
        boolean test = conceptosGastosView.getCalc().isSelected();
        if (test == true) {
            calcular.setSelected(true);
        }

        if (idConcpt == 4 || idConcpt == 5 || idConcpt == 7) {
            labeld.setVisible(true);
            fielporcient.setVisible(true);
            fielporcient.setText(String.valueOf(conceptosGastosView.getPorciento()));

        }

    }

    public void handleActializar(ActionEvent event) {

        actualizarConcepto(idConcpt, idEmp);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos Actualizados!");
        alert.showAndWait();

        Stage stage = (Stage) btn_add.getScene().getWindow();
        stage.close();

        controller.loadData();
    }


}
