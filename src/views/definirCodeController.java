package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.UniddaObraToIMportView;

import java.net.URL;
import java.util.ResourceBundle;

public class definirCodeController implements Initializable {

    ImportarUOInCalPreCController importar;

    @FXML
    private JFXTextField labelUser;

    @FXML
    private JFXButton btnAction;

    @FXML
    private Label labelText;
    private UniddaObraToIMportView uniddaObraToIMportView;
    private UniddaObraToIMportView old;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void cargarDatos(ImportarUOInCalPreCController controller, UniddaObraToIMportView uo) {
        importar = controller;
        labelText.setText(" Modificar código: " + uo.getCodigo().getText());
        uniddaObraToIMportView = uo;
        old = uo;
    }


    public void handleAddNewCode(ActionEvent event) {

        if (labelUser.getText().length() == 7) {
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setText(labelUser.getText());
            checkBox.setSelected(true);
            uniddaObraToIMportView.setCodigo(checkBox);

            importar.updateResorces(old, uniddaObraToIMportView);

            Stage stage = (Stage) btnAction.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al definir el código");
            alert.setContentText("El código debe contener solo 7 dígitos");
            alert.showAndWait();
        }
    }

}

