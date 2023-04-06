package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Grupoejecucion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarGrupoEjecucionController implements Initializable {

    private static SessionFactory sf;
    private GrupoEjecucionController grupoEjecucionController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    private int idGrupo;
    @FXML
    private JFXButton btnClose;

    /**
     * Metodo para actualizar grupo de ejecicion
     */
    public void updateGrupoEjecucion(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Grupoejecucion grupo = session.get(Grupoejecucion.class, id);
            grupo.setCodigo(field_codigo.getText());
            grupo.setDescripcion(text_descripcion.getText());
            session.update(grupo);
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


        updateGrupoEjecucion(idGrupo);
        grupoEjecucionController.loadData();

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(GrupoEjecucionController controller, Grupoejecucion grupoejecucionToChange) {
        grupoEjecucionController = controller;
        field_codigo.setText(grupoejecucionToChange.getCodigo());
        text_descripcion.setText(grupoejecucionToChange.getDescripcion());
        idGrupo = grupoejecucionToChange.getId();


    }


    public void handleClose(ActionEvent event) {

        grupoEjecucionController.loadData();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


}
