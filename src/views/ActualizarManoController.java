package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ActualizarManoController implements Initializable {
    private static SessionFactory sf;
    public ManoObraController manoObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_um;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_preciomn;
    @FXML
    private JFXTextField field_preciomlc;
    @FXML
    private JFXComboBox<String> combo_grupo;
    private int idMano;

    @FXML
    private JFXButton btn_close;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Method to UPDATE EquipoPropio
     */
    public void updateManoPropio(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Recursos updateMano = session.get(Recursos.class, id);
            updateMano.setCodigo(field_codigo.getText());
            updateMano.setDescripcion(text_descripcion.getText());
            updateMano.setUm(field_um.getText());
            updateMano.setPreciomn(Double.parseDouble(field_preciomn.getText()));
            updateMano.setPreciomlc(Double.parseDouble(field_preciomlc.getText()));
            session.update(updateMano);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public ObservableList<String> Grupos() {
        ObservableList<String> grupos = FXCollections.observableArrayList("II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII");

        return grupos;
    }

    public void pasarParametros(ManoObraController manoObraCont, SuministrosView mano) {
        manoObraController = manoObraCont;

        field_codigo.setText(mano.getCodigo());
        text_descripcion.setText(mano.getDescripcion());
        field_preciomn.setText(String.valueOf(mano.getPreciomn()));
        field_preciomlc.setText(String.valueOf(mano.getPreciomlc()));
        combo_grupo.setItems(Grupos());
        combo_grupo.getSelectionModel().select(mano.getEscala());
        idMano = mano.getId();

    }


    @FXML
    public void uppdateManoAction(ActionEvent event) {
        updateManoPropio(idMano);
        manoObraController.loadData();

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    public void handleClose(ActionEvent event) {

        manoObraController.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


}
