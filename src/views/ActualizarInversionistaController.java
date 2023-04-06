package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Inversionista;
import models.InversionistaView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarInversionistaController implements Initializable {

    private static SessionFactory sf;
    public InversionistaController inversionistaContro;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    private int idInversionista;
    @FXML
    private JFXButton btnClose;

    /**
     * Metodo para actualizar cuadrilla
     */
    public void updateEntidad(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Inversionista inversionista = session.get(Inversionista.class, id);
            inversionista.setCodigo(field_codigo.getText());
            inversionista.setDescripcion(text_descripcion.getText());
            session.update(inversionista);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    void handeleUpdateAction(ActionEvent event) {

        updateEntidad(idInversionista);
        inversionistaContro.loadData();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(InversionistaController inversionistaCont, InversionistaView inversionistaView) {

        inversionistaContro = inversionistaCont;
        field_codigo.setText(inversionistaView.getCodigo());
        text_descripcion.setText(inversionistaView.getDescripcion());
        idInversionista = inversionistaView.getId();


    }

    public void closeHandle(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }
}
