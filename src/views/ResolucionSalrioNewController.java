package views;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import models.ConnectionModel;
import models.Salario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResolucionSalrioNewController implements Initializable {

    public ConfiguracionController controller;
    @FXML
    private Label name;

    @FXML
    private JFXTextField g2;

    @FXML
    private JFXTextField g3;

    @FXML
    private JFXTextField g4;

    @FXML
    private JFXTextField g5;

    @FXML
    private JFXTextField g6;

    @FXML
    private JFXTextField g7;

    @FXML
    private JFXTextField g8;

    @FXML
    private JFXTextField g9;

    @FXML
    private JFXTextField g10;

    @FXML
    private JFXTextField g11;

    @FXML
    private JFXTextField g12;

    @FXML
    private JFXTextField esp;

    @FXML
    private JFXTextField ant;

    @FXML
    private JFXTextField vac;

    @FXML
    private JFXTextField nom;

    @FXML
    private JFXTextField soc;

    @FXML
    private Label labelEq;

    @FXML
    private JFXTextField gt2;

    @FXML
    private JFXTextField gt3;

    @FXML
    private JFXTextField gt4;

    @FXML
    private JFXTextField gt5;

    @FXML
    private JFXTextField gt6;

    @FXML
    private JFXTextField gt7;

    @FXML
    private JFXTextField gt8;

    @FXML
    private JFXTextField gt9;

    @FXML
    private JFXTextField gt10;

    @FXML
    private JFXTextField gt11;

    @FXML
    private JFXTextField gt12;

    @FXML
    private JFXTextField descrip;

    private Salario salarioModel;
    private int max;
    private String tag;
    // private ConnectionModel connectionModel = ConnectionModel.getInstance();


    private Integer getMaxRowSalarios() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            max = 0;
            List<Salario> list = session.createQuery("FROM Salario ").getResultList();
            max = list.size() + 1;
            tx.commit();
            session.close();
            return max;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return max;
    }

    private void persistSalario() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            salarioModel = new Salario();
            salarioModel.setDescripcion(descrip.getText());
            salarioModel.setIi(Double.parseDouble(g2.getText()));
            salarioModel.setIii(Double.parseDouble(g3.getText()));
            salarioModel.setIv(Double.parseDouble(g4.getText()));
            salarioModel.setV(Double.parseDouble(g5.getText()));
            salarioModel.setVi(Double.parseDouble(g6.getText()));
            salarioModel.setVii(Double.parseDouble(g7.getText()));
            salarioModel.setViii(Double.parseDouble(g8.getText()));
            salarioModel.setIx(Double.parseDouble(g9.getText()));
            salarioModel.setX(Double.parseDouble(g10.getText()));
            salarioModel.setXi(Double.parseDouble(g11.getText()));
            salarioModel.setXii(Double.parseDouble(g12.getText()));
            salarioModel.setXiii(0);
            salarioModel.setXiv(0);
            salarioModel.setXv(0);
            salarioModel.setXvi(0);

            salarioModel.setAutesp(Double.parseDouble(esp.getText()));
            salarioModel.setAntiguedad(Double.parseDouble(ant.getText()));
            salarioModel.setVacaciones(Double.parseDouble(vac.getText()));
            salarioModel.setNomina(Double.parseDouble(nom.getText()));
            salarioModel.setSeguro(Double.parseDouble(soc.getText()));

            salarioModel.setGtii(Double.parseDouble(gt2.getText()));
            salarioModel.setGtiii(Double.parseDouble(gt3.getText()));
            salarioModel.setGtiv(Double.parseDouble(gt4.getText()));
            salarioModel.setGtv(Double.parseDouble(gt5.getText()));
            salarioModel.setGtvi(Double.parseDouble(gt6.getText()));
            salarioModel.setGtvii(Double.parseDouble(gt7.getText()));
            salarioModel.setGtviii(Double.parseDouble(gt8.getText()));
            salarioModel.setGtix(Double.parseDouble(gt9.getText()));
            salarioModel.setGtx(Double.parseDouble(gt10.getText()));
            salarioModel.setGtxi(Double.parseDouble(gt11.getText()));
            salarioModel.setGtxii(Double.parseDouble(gt12.getText()));
            salarioModel.setGtxiii(0);
            salarioModel.setGtxiv(0);
            salarioModel.setGtxv(0);
            salarioModel.setGtxi(0);
            salarioModel.setTag(tag);

            session.persist(salarioModel);

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

    public void loadDatoController(ConfiguracionController configuracionController) {
        controller = configuracionController;
        tag = null;
        tag = "T" + String.valueOf(getMaxRowSalarios());
    }


    public void handleUpdateTarifas(javafx.event.ActionEvent event) {

        try {

            if (!descrip.getText().isEmpty()) {
                persistSalario();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos Insertados Correctamente");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Indique la descripción de la tarifa");
                alert.showAndWait();

                descrip.setFocusColor(Color.DARKRED);
                descrip.requestFocus();
            }


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Erros al Actualizar los datos!");
            alert.showAndWait();
        }

        controller.datosSalarios();

    }


}
