package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Entidad;
import models.Nomencladorempresa;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InsertarEntidadController implements Initializable {

    private static SessionFactory sf;
    public NuevaObraController entidadController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXButton btn_close;
    private int idEntidad;
    private ArrayList<Nomencladorempresa> nomencladorempresaArrayList;
    private ObservableList<String> listeEmpresa;
    private Entidad newEntidad;

    public ObservableList<String> getListaEmpresa() {
        listeEmpresa = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nomencladorempresaArrayList = (ArrayList<Nomencladorempresa>) session.createQuery("FROM Nomencladorempresa ").list();
            for (Nomencladorempresa me : nomencladorempresaArrayList) {
                listeEmpresa.add(me.getReup() + " - " + me.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listeEmpresa;
    }

    /**
     * Metodo para actualizar cuadrilla
     */
    public void saveEntidad(Entidad entidad) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.persist(entidad);

            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void handleDefineEmpresa() {
        String code = field_codigo.getText().substring(0, 5);
        field_codigo.clear();
        field_codigo.setText(code);

        Nomencladorempresa ne = nomencladorempresaArrayList.stream().filter(e -> e.getReup().equals(code)).findFirst().orElse(null);
        text_descripcion.setText(ne.getDescripcion());
    }

    @FXML
    void handeleUpdateAction(ActionEvent event) {

        newEntidad = new Entidad();
        newEntidad.setCodigo(field_codigo.getText());
        newEntidad.setDescripcion(text_descripcion.getText());
        saveEntidad(newEntidad);

        entidadController.combo_entidad.setItems(entidadController.getEntidadesList());
        entidadController.combo_entidad.getSelectionModel().select(field_codigo.getText() + " - " + text_descripcion.getText());
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        getListaEmpresa();
        TextFields.bindAutoCompletion(field_codigo, listeEmpresa);
    }

    public void pasarParametros(NuevaObraController controller) {
        entidadController = controller;
    }

    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
