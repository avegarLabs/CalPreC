package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Recursos;
import models.SuministrosView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarSuministroController implements Initializable {

    private static SessionFactory sf;
    public SuministrosController suministrosCont;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_um;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_peso;
    @FXML
    private JFXTextField field_precioTotal;
    @FXML
    private JFXTextField field_preciomlc;
    @FXML
    private JFXTextField text_mermaprod;
    @FXML
    private JFXTextField text_manipulacion;
    @FXML
    private JFXTextField text_transporte;
    @FXML
    private JFXTextField text_otras;
    private int id;

    private Recursos recurso;

    @FXML
    private JFXButton btnClose;


    public Recursos getRecursos(Integer id) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recurso = session.get(Recursos.class, id);

            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return recurso;

    }


    /**
     * Method to UPDATE SuministroPropio
     */

    public void updateSuministrosPropio(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos updateSuministros = session.get(Recursos.class, id);
            updateSuministros.setCodigo(field_codigo.getText());
            updateSuministros.setDescripcion(text_descripcion.getText());
            updateSuministros.setUm(field_um.getText());
            updateSuministros.setPeso(Double.parseDouble(field_peso.getText()));
            updateSuministros.setPreciomn(Double.parseDouble(field_precioTotal.getText()));
            // updateSuministros.setPreciomlc(Double.parseDouble(field_preciomlc.getText()));
            updateSuministros.setMermaprod(Double.parseDouble(text_mermaprod.getText()));
            updateSuministros.setMermatrans(Double.parseDouble(text_transporte.getText()));
            updateSuministros.setMermamanip(Double.parseDouble(text_manipulacion.getText()));
            updateSuministros.setOtrasmermas(Double.parseDouble(text_otras.getText()));
            session.update(updateSuministros);
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


    public void pasarParametros(SuministrosController controller, SuministrosView suministros) {
        suministrosCont = controller;

        recurso = new Recursos();
        recurso = getRecursos(suministros.getId());

        field_codigo.setText(recurso.getCodigo());
        field_peso.setText(String.valueOf(recurso.getPeso()));
//        field_preciomlc.setText(String.valueOf(recurso.getPreciomlc()));
        field_precioTotal.setText(String.valueOf(recurso.getPreciomn()));
        field_um.setText(recurso.getUm());
        text_descripcion.setText(recurso.getDescripcion());
        text_manipulacion.setText(String.valueOf(recurso.getMermamanip()));
        text_mermaprod.setText(String.valueOf(recurso.getMermaprod()));
        text_transporte.setText(String.valueOf(recurso.getMermatrans()));
        text_otras.setText(String.valueOf(recurso.getOtrasmermas()));
        id = recurso.getId();

    }


    public void clearFields() {

        field_codigo.clear();
        text_descripcion.clear();
        field_um.clear();
        field_peso.clear();
        field_precioTotal.clear();
        //  field_preciomlc.clear();
        text_mermaprod.clear();
        text_transporte.clear();
        text_manipulacion.clear();
        text_otras.clear();

    }


    public void updateSuminitrosAction(ActionEvent event) {

        updateSuministrosPropio(id);
        suministrosCont.loadData();

        clearFields();

    }


    public void handleClose(ActionEvent event) {

        suministrosCont.loadData();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }
}
