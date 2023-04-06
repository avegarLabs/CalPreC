package views;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import models.RenglonesIndModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;

public class IndicadoresNormasResumenController implements Initializable {


    @FXML
    private Labeled label_title;

    @FXML
    private TableView<RenglonesIndModel> tableCodes;

    @FXML
    private TableColumn<RenglonesIndModel, String> descripRV;

    @FXML
    private TableColumn<RenglonesIndModel, Double> umRV;

    @FXML
    private JFXButton btn_add;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void addRCtoTable(List<RenglonesIndModel> indModelList) {
        descripRV.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, String>("descrip"));
        umRV.setCellValueFactory(new PropertyValueFactory<RenglonesIndModel, Double>("vol"));

        ObservableList<RenglonesIndModel> indModelObservableList = FXCollections.observableArrayList();
        indModelObservableList.addAll(indModelList);
        tableCodes.setItems(indModelObservableList);

    }

    public void actionHandleAction(ActionEvent event) {
        createExcelFile(tableCodes.getItems());
    }

    private void createExcelFile(ObservableList<RenglonesIndModel> insumosIndModelObservableList) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Total Indicadores");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 1;

            for (RenglonesIndModel cl : insumosIndModelObservableList) {
                data.put("1", new Object[]{"Descripción", "Volumen"});

                countrow++;
                Object[] datoscl = new Object[2];
                datoscl[0] = cl.getDescrip();
                datoscl[1] = cl.getVol();
                data.put(String.valueOf(countrow++), datoscl);
            }
            Set<String> keySet = data.keySet();
            int rownum = 0;
            for (String key : keySet) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;

                for (Object obj : objects) {
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum++);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Double)
                        cell.setCellValue((Double) obj);
                }
            }
            try {
                // this Writes the workbook gfgcontribute
                FileOutputStream out = new FileOutputStream(new File(file.getAbsolutePath()));
                workbook.write(out);
                out.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trabajo Terminado");
                alert.setContentText("Fichero dispobible en: " + file.getAbsolutePath());
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
