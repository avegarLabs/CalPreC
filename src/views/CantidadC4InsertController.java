package views;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CantidadC4InsertController implements Initializable {


    private static SessionFactory sf;
    public double costo;
    public double salario;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_salario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarDatos(String code) {
        System.out.println(code);
    }


    public void handleSave(ActionEvent event) {

        costo = Double.parseDouble(field_codigo.getText());
        salario = Double.parseDouble(field_salario.getText());
    }


}
