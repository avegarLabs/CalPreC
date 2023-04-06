package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Entidad;
import models.EntidadView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarEntidadController implements Initializable {

    private static SessionFactory sf;
    public EntidadController entidadController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXButton btn_close;
    private int idEntidad;

    /**
     * Metodo para actualizar cuadrilla
     */
    public void updateEntidad(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Entidad entidad = session.get(Entidad.class, id);
            entidad.setCodigo(field_codigo.getText());
            entidad.setDescripcion(text_descripcion.getText());
            session.update(entidad);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    void handeleUpdateAction(ActionEvent event) {
        updateEntidad(idEntidad);
        entidadController.loadData();

        clearField();
    }


    public void clearField() {

        field_codigo.clear();
        text_descripcion.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(EntidadController controller, EntidadView entidadToChange) {
        entidadController = controller;

        field_codigo.setText(entidadToChange.getCodigo());
        text_descripcion.setText(entidadToChange.getDescripcion());
        idEntidad = entidadToChange.getId();


    }

    public void handleClose(ActionEvent event) {

        entidadController.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
