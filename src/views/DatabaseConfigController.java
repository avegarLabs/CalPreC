package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConfigManager;
import models.PgsqlConnection;
import models.UtilCalcSingelton;

import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseConfigController implements Initializable {

    ConfigManager configManager = ConfigManager.getInstance();
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXTextField server;
    @FXML
    private JFXButton btnClose;
    @FXML
    private JFXTextField port;
    @FXML
    private JFXTextField database;
    private PgsqlConnection pgsqlConnection;
    private String baseUrl = "jdbc" + ":" + "postgresql" + ":" + "//";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server.setText("localhost");
        port.setText("5432");
        database.setText("calprecR38");
    }

    @FXML
    void addConfigurationAction(ActionEvent event) {
        pgsqlConnection = new PgsqlConnection("PostgreSQL", baseUrl + server.getText().trim(), database.getText().trim(), port.getText().trim());

        String sDirectory = System.getProperty("user.dir");
        //String path = sDirectory.replace("\\src", "\\resources\\config.properties");
        String path = sDirectory + "\\resources\\config.properties";

        boolean resp = configManager.writeConfigFile(pgsqlConnection, path);

        if (resp) {
            utilCalcSingelton.showAlert("Configuración termina \n" + "Inicie otra vez la aplicación para utilizar los cambios", 1);
            Platform.exit();
            System.exit(0);
        } else {
            utilCalcSingelton.showAlert("Error \n" + "Revise sus datos de configuración", 3);
        }
    }

    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}