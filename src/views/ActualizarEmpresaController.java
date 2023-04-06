package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Empresaconstructora;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;


public class ActualizarEmpresaController implements Initializable {

    private static SessionFactory sf;
    public EmpresaConstructoraController constructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_c731;
    @FXML
    private JFXTextField field_c822;
    @FXML
    private JFXTextField field_pbruta;
    private int id_empresa;

    @FXML
    private JFXTextField coefField;

    private Empresaconstructora empresaconstructora;

    @FXML
    private JFXButton btnClose;


    /* Method to UPDATE Empresa */
    public void updateEmpresa(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Empresaconstructora empresaconstructora = session.get(Empresaconstructora.class, id);
            empresaconstructora.setCodigo(field_codigo.getText());
            empresaconstructora.setDescripcion(text_descripcion.getText());
            empresaconstructora.setCuenta731(Double.parseDouble(field_c731.getText()));
            empresaconstructora.setCuenta822(Double.parseDouble(field_c822.getText()));
            empresaconstructora.setPbruta(Double.parseDouble(field_pbruta.getText()));
            session.update(empresaconstructora);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    void addUpdateAction(ActionEvent event) {

        updateEmpresa(id_empresa);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos Actualizados!");
        alert.showAndWait();

        constructoraController.loadData();
    }

    public void pasarParametros(EmpresaConstructoraController controller, Empresaconstructora empresaconstructoraToUpdate) {

        constructoraController = controller;
        empresaconstructora = empresaconstructoraToUpdate;
        field_codigo.setText(empresaconstructoraToUpdate.getCodigo());
        text_descripcion.setText(empresaconstructoraToUpdate.getDescripcion());
        field_c731.setText(String.valueOf(empresaconstructoraToUpdate.getCuenta731()));
        field_c822.setText(String.valueOf(empresaconstructoraToUpdate.getCuenta822()));
        field_pbruta.setText(String.valueOf(empresaconstructoraToUpdate.getPbruta()));
        id_empresa = empresaconstructoraToUpdate.getId();

        if (empresaconstructoraToUpdate.getCuenta731() > 1 && empresaconstructoraToUpdate.getCuenta822() > 1 && empresaconstructoraToUpdate.getPbruta() > 1) {
            double sum = empresaconstructoraToUpdate.getCuenta731() + empresaconstructoraToUpdate.getCuenta822();
            double dif = empresaconstructoraToUpdate.getPbruta() / sum;
            double result = Math.round(dif * 100d) / 100d;
            coefField.setVisible(true);
            coefField.setText(String.valueOf(result));
        }


    }

    public void handleShowGastos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConceptosGastos.fxml"));
            Parent proot = loader.load();

            ConceptosController conceptosController = loader.getController();
            conceptosController.cargarEmpresa(empresaconstructora);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleClose(ActionEvent event) {


        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
        constructoraController.loadData();
    }

    public void calcAction(ActionEvent event) {
        double sum = Double.parseDouble(field_c731.getText()) + Double.parseDouble(field_c822.getText());
        double coef = Double.parseDouble(field_pbruta.getText()) / sum;
        double result = Math.round(coef * 100d) / 100d;
        coefField.setVisible(true);
        coefField.setText(String.valueOf(result));
    }


}
