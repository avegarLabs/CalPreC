package views;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import models.UtilCalcSingelton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class InformacionController implements Initializable {

    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private AnchorPane description;
    @FXML
    private Label label_title;
    @FXML
    private Label compilationInfo;
    @FXML
    private JFXTextArea descripText;
    private String[] datosUpdate;
    private String filePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarDatos(String[] datos) {
        datosUpdate = new String[3];
        datosUpdate = datos;
        compilationInfo.setText(datos[0]);
        descripText.setText(datos[1]);
        filePath = datos[2];
    }


    public void decompress(ActionEvent event) throws Exception {
        ZipFile zf = new ZipFile(new File(filePath));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione la ubicación del fichero");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CalPreC (*.exe)", "*.exe");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName("CalPreC.exe");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (zf) {
                Enumeration<? extends ZipEntry> entries = zf.entries();
                for (ZipEntry ze : Collections.list(entries)) {
                    System.out.printf("Inflating %s (compressed: %s, size: %s, ratio: %.2f)%n", ze.getName(), ze.getCompressedSize(), ze.getSize(), (double) ze.getSize() / ze.getCompressedSize());
                    InputStream is = zf.getInputStream(ze);
                    FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                    try (is; fos) {
                        fos.write(is.readAllBytes());
                    }
                }
                utilCalcSingelton.showAlert("Actualización descargada!!!", 1);
            }
        }
    }

    public void actionClose(ActionEvent e) {
        Platform.exit();
        System.exit(0);
    }

}
