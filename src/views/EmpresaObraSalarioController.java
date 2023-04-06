package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Empresaobrasalario;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class EmpresaObraSalarioController implements Initializable {

    private static SessionFactory sf;
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
    private Label labelEq;
    @FXML
    private Label name;
    @FXML
    private JFXButton btn_close;
    private Empresaobrasalario salarioModel;

    public Empresaobrasalario getSalario(Integer idEmpresa, Integer idObra, Integer idSalario) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            javax.persistence.Query query = session.createQuery("FROM Empresaobrasalario WHERE empresaconstructoraId =: idEmp AND obraId =: idObra AND salarioId =: idSalario");
            query.setParameter("idEmp", idEmpresa);
            query.setParameter("idObra", idObra);
            query.setParameter("idSalario", idSalario);

            salarioModel = (Empresaobrasalario) query.getSingleResult();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return salarioModel;
    }


    private void updateSalario(int idEm, int idOb, int idSala) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("FROM Empresaobrasalario WHERE empresaconstructoraId =: idEmp AND obraId =: idObra AND salarioId =: idSalario").setParameter("idEmp", idEm).setParameter("idObra", idOb).setParameter("idSalario", idSala);
            salarioModel = (Empresaobrasalario) query.getSingleResult();

            if (salarioModel != null) {
                salarioModel.setIi(Double.parseDouble(g2.getText()));
                salarioModel.setIii(Double.parseDouble(g3.getText()));
                salarioModel.setIv(Double.parseDouble(g4.getText()));
                salarioModel.setV(Double.parseDouble(g5.getText()));
                salarioModel.setVi(Double.parseDouble(g6.getText()));
                salarioModel.setVii(Double.parseDouble(g7.getText()));
                salarioModel.setViii(Double.parseDouble(g8.getText()));
                salarioModel.setIx(Double.parseDouble(g9.getText()));
                salarioModel.setX(Double.parseDouble(g10.getText()));
                salarioModel.setX(Double.parseDouble(g11.getText()));
                salarioModel.setXii(Double.parseDouble(g12.getText()));

                salarioModel.setAutoesp(Double.parseDouble(esp.getText()));
                salarioModel.setAntig(Double.parseDouble(ant.getText()));
                salarioModel.setVacaiones(Double.parseDouble(vac.getText()));
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
                salarioModel.setGtxiii(0.0);
                salarioModel.setGtxiv(0.0);
                salarioModel.setGtxv(0.0);
                salarioModel.setGtxi(0.0);

                session.update(salarioModel);


            }
            tx.commit();
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


    public void showSalario(int idEm, int idOb, int idSala) {

        System.out.println("Empresa: " + idEm);
        System.out.println("ObraPCW: " + idOb);
        System.out.println("Salario: " + idSala);

        salarioModel = getSalario(idEm, idOb, idSala);

        name.setText("Ajuste de Tarifas Salariales");

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


        esp.setText(String.valueOf(salarioModel.getAutoesp()));
        ant.setText(String.valueOf(salarioModel.getAntig()));
        vac.setText(String.valueOf(salarioModel.getVacaiones()));
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


        updateSalario(salarioModel.getEmpresaconstructoraId(), salarioModel.getObraId(), salarioModel.getSalarioId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Datos actualizados!");
        alert.showAndWait();

        showSalario(salarioModel.getEmpresaconstructoraId(), salarioModel.getObraId(), salarioModel.getSalarioId());


    }

    public void handleCloseOperations(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


}
