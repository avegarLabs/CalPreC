package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Nivelespecifico;
import models.UniddaObraView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class ModificarNivelEspecificoController implements Initializable {

    private static SessionFactory sf;
    RenglonVarianteObraController renglonVarianteObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXComboBox<String> comboGrupos;
    @FXML
    private JFXButton btn_add;
    private Integer fieldLenth = 2;

    @FXML
    private JFXButton btn_close;
    private Nivelespecifico nivelespecifico;

    public void updateNivelEspecifico(String code, String description) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Nivelespecifico niv = session.get(Nivelespecifico.class, nivelespecifico.getId());
            niv.setCodigo(code);
            niv.setDescripcion(description);
            session.update(niv);
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

    private UniddaObraView checkCode(String code) {
        return renglonVarianteObraController.dataTable.getItems().parallelStream().filter(uniddaObraView -> uniddaObraView.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == fieldLenth) {
            field_descripcion.requestFocus();

        }
    }

    @FXML
    public void addUpdateNivelEspecifico(ActionEvent event) {

        updateNivelEspecifico(field_codigo.getText(), field_descripcion.getText());
        renglonVarianteObraController.handleCargardatos(event);
        clearField();


    }

    public void clearField() {
        field_codigo.clear();
        field_descripcion.clear();
    }

    public void pasarParametros(RenglonVarianteObraController controller, Nivelespecifico nivel) {

        renglonVarianteObraController = controller;
        nivelespecifico = nivel;
        field_codigo.setText(nivelespecifico.getCodigo());
        field_descripcion.setText(nivelespecifico.getDescripcion());

    }

    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


}
