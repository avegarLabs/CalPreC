package views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ActualizarConceptoEmpresaObraIndirectoController implements Initializable {
    @FXML
    private javafx.scene.control.Label label_title;

    @FXML
    private javafx.scene.control.Label valmo;

    @FXML
    private javafx.scene.control.Label valcdmoe;

    @FXML
    private javafx.scene.control.Label valco4;

    @FXML
    private javafx.scene.control.Label valtotal;

    @FXML
    private javafx.scene.control.Label concept6;

    @FXML
    private javafx.scene.control.Label concept8;

    @FXML
    private javafx.scene.control.Label valtotalconcepto;

    @FXML
    private javafx.scene.control.Label valgeneral;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarDatos(String conceptoGasto, double cdmo, double cmoeq, double salarios, double concept06, double concept08) {
        valmo.setText(String.valueOf(Math.round(cdmo * 100d) / 100d));
        valcdmoe.setText(String.valueOf(Math.round(cmoeq * 100d) / 100d));
        valco4.setText(String.valueOf(Math.round(salarios * 100d) / 100d));

        double cota = cdmo + cmoeq + salarios;
        valtotal.setText(String.format("%.2f", cota));

        concept6.setText(String.valueOf(concept06));
        concept8.setText(String.valueOf(concept08));
        double concepto = concept06 + concept08;
        valtotalconcepto.setText(String.format("%.2f", concepto));

        if (concepto > cota && concepto != 0) {
            valgeneral.setText(String.valueOf(cota));
        } else if (concepto < cota && concepto != 0) {
            valgeneral.setText(String.format("%.2f", concepto));
        } else {
            valgeneral.setText(String.format("%.2f", cota));
        }
    }

}
