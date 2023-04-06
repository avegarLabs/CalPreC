package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Despachoscl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class CantidadController implements Initializable {


    private static SessionFactory sf;
    public ValidarDespachosController validarDespachosController;
    @FXML
    private JFXTextField field_codigo;
    private Despachoscl des;
    @FXML
    private JFXButton toClose;
    private int ind;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleModificar(ActionEvent event) {
        des.setCantidad(Double.parseDouble(field_codigo.getText()));
        updateDespachos(des);
        try {
            var item = validarDespachosController.tableSum.getItems().get(ind);
            item.setCant(Double.parseDouble(field_codigo.getText()));
            validarDespachosController.tableSum.getItems().set(ind, item);
            Stage stage = (Stage) toClose.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public void pasarDatos(ValidarDespachosController validar, Despachoscl despachoscl, int selectedIndex) {
        des = despachoscl;
        field_codigo.setText(String.valueOf(des.getCantidad()));
        validarDespachosController = validar;
        ind = selectedIndex;
    }

    private void updateDespachos(Despachoscl despachosclList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Despachoscl despachoscl = session.get(Despachoscl.class, despachosclList.getId());
            despachoscl.setCantidad(Double.parseDouble(field_codigo.getText()));
            session.update(despachoscl);
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
