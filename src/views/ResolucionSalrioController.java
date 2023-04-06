package views;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import models.ConnectionModel;
import models.Salario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ResolucionSalrioController implements Initializable {

    private static SessionFactory sf;
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


    private Salario salarioModel;

    public Salario getSalario(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salarioModel = (Salario) session.get(Salario.class, id);

            tx.commit();
            session.close();
            return salarioModel;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Salario();
    }

    private void updateSalario(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Salario salariom = (Salario) session.get(Salario.class, id);
            if (salariom != null) {
                salariom.setIi(Double.parseDouble(g2.getText()));
                salariom.setIii(Double.parseDouble(g3.getText()));
                salariom.setIv(Double.parseDouble(g4.getText()));
                salariom.setV(Double.parseDouble(g5.getText()));
                salariom.setVi(Double.parseDouble(g6.getText()));
                salariom.setVii(Double.parseDouble(g7.getText()));
                salariom.setViii(Double.parseDouble(g8.getText()));
                salariom.setIx(Double.parseDouble(g9.getText()));
                salariom.setX(Double.parseDouble(g10.getText()));
                salariom.setXi(Double.parseDouble(g11.getText()));
                salariom.setXii(Double.parseDouble(g12.getText()));
                salariom.setXiii(0);
                salariom.setXiv(0);
                salariom.setXv(0);
                salariom.setXvi(0);

                salariom.setAutesp(Double.parseDouble(esp.getText()));
                salariom.setAntiguedad(Double.parseDouble(ant.getText()));
                salariom.setVacaciones(Double.parseDouble(vac.getText()));
                salariom.setNomina(Double.parseDouble(nom.getText()));
                salariom.setSeguro(Double.parseDouble(soc.getText()));

                salariom.setGtii(Double.parseDouble(gt2.getText()));
                salariom.setGtiii(Double.parseDouble(gt3.getText()));
                salariom.setGtiv(Double.parseDouble(gt4.getText()));
                salariom.setGtv(Double.parseDouble(gt5.getText()));
                salariom.setGtvi(Double.parseDouble(gt6.getText()));
                salariom.setGtvii(Double.parseDouble(gt7.getText()));
                salariom.setGtviii(Double.parseDouble(gt8.getText()));
                salariom.setGtix(Double.parseDouble(gt9.getText()));
                salariom.setGtx(Double.parseDouble(gt10.getText()));
                salariom.setGtxi(Double.parseDouble(gt11.getText()));
                salariom.setGtxii(Double.parseDouble(gt12.getText()));
                salariom.setGtxiii(0);
                salariom.setGtxiv(0);
                salariom.setGtxv(0);
                salariom.setGtxi(0);

                session.update(salariom);
            }
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


    public void showSalario(int id) {

        salarioModel = getSalario(id);
        name.setText(salarioModel.getDescripcion());
        g2.setText(String.valueOf(salarioModel.getIi()));
        g3.setText(String.valueOf(salarioModel.getIii()));
        g4.setText(String.valueOf(salarioModel.getIv()));
        g5.setText(String.valueOf(salarioModel.getV()));
        g6.setText(String.valueOf(salarioModel.getVi()));
        g7.setText(String.valueOf(salarioModel.getVii()));
        g8.setText(String.valueOf(salarioModel.getViii()));
        g9.setText(String.valueOf(salarioModel.getIx()));
        g10.setText(String.valueOf(salarioModel.getX()));
        g11.setText(String.valueOf(salarioModel.getXi()));
        g12.setText(String.valueOf(salarioModel.getXii()));

        esp.setText(String.valueOf(salarioModel.getAutesp()));
        ant.setText(String.valueOf(salarioModel.getAntiguedad()));
        vac.setText(String.valueOf(salarioModel.getVacaciones()));
        soc.setText(String.valueOf(salarioModel.getSeguro()));
        nom.setText(String.valueOf(salarioModel.getNomina()));

        gt2.setText(String.valueOf(salarioModel.getGtii()));
        gt3.setText(String.valueOf(salarioModel.getGtiii()));
        gt4.setText(String.valueOf(salarioModel.getGtiv()));
        gt5.setText(String.valueOf(salarioModel.getGtv()));
        gt6.setText(String.valueOf(salarioModel.getGtvi()));
        gt7.setText(String.valueOf(salarioModel.getGtvii()));
        gt8.setText(String.valueOf(salarioModel.getGtviii()));
        gt9.setText(String.valueOf(salarioModel.getGtix()));
        gt10.setText(String.valueOf(salarioModel.getGtx()));
        gt11.setText(String.valueOf(salarioModel.getGtxi()));
        gt12.setText(String.valueOf(salarioModel.getGtxii()));

    }


    public void handleUpdateTarifas(javafx.event.ActionEvent event) {

        updateSalario(salarioModel.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos actualizados!");
        alert.showAndWait();

        showSalario(salarioModel.getId());


    }


}
