package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class LocalizarOptionsRVController implements Initializable {


    @FXML
    private Label label_title;

    @FXML
    private JFXCheckBox checkSum;

    @FXML
    private JFXCheckBox checkInsumo;

    @FXML
    private JFXCheckBox checkRvar;

    @FXML
    private JFXTextField fielSearch;

    @FXML
    private JFXButton btnExecute;

    private Integer idObra;

    private String tipo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        checkSum.setOnMouseClicked(event -> {
            if (checkSum.isSelected() == true) {
                fielSearch.setPromptText("Siministro");
                tipo = null;
                tipo = "Sum";
                checkInsumo.setSelected(false);
                checkRvar.setSelected(false);
            }
        });

        checkInsumo.setOnMouseClicked(event -> {
            if (checkInsumo.isSelected() == true) {
                fielSearch.setPromptText("Insumo");
                tipo = null;
                tipo = "Ins";
                checkSum.setSelected(false);
                checkRvar.setSelected(false);
            }
        });

        checkRvar.setOnMouseClicked(event -> {
            if (checkRvar.isSelected() == true) {
                fielSearch.setPromptText("Renglón Variante");
                tipo = null;
                tipo = "Rv";
                checkInsumo.setSelected(false);
                checkSum.setSelected(false);
            }
        });
    }


    public void pasarIdObra(Integer idO) {
        idObra = null;
        idObra = idO;
    }


    public void handleGetSuministros(ActionEvent event) {

        if (fielSearch.getText().isEmpty() == true) {

        } else if (fielSearch.getText().isEmpty() == false && checkSum.isSelected() == true || checkRvar.isSelected() == true) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchResponseRV.fxml"));
                Parent proot = loader.load();

                SearchResponseRVController searchResponseController = loader.getController();
                searchResponseController.pasarDatos(idObra, fielSearch.getText(), tipo);

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();

                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (fielSearch.getText().isEmpty() == false && checkInsumo.isSelected() == true) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("searchResponseInsumo.fxml"));
                Parent proot = loader.load();

                SearchResponseInsumoController searchResponseController = loader.getController();
                searchResponseController.pasarDatos(idObra, fielSearch.getText());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();
            } catch (Exception ex) {

            }
        }

    }


}

