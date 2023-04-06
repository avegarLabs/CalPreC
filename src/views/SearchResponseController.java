package views;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import models.ConnectionModel;
import models.SuministSearchView;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;


public class SearchResponseController implements Initializable {


    @FXML
    private TableView<SuministSearchView> dataTable;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> zona;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> objeto;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> nivel;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> especialidad;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> subespecialidad;

    @FXML
    private TableColumn<SuministSearchView, StringProperty> unidad;

    @FXML
    private TableColumn<SuministSearchView, DoubleProperty> cant;

    @FXML
    private TableColumn<SuministSearchView, DoubleProperty> costo;

    @FXML
    private Label label_title;

    @FXML
    private JFXCheckBox checkCantidad;

    @FXML
    private JFXCheckBox checkCosto;


    private ObservableList<SuministSearchView> viewObservableList;

    private double costoTotal;
    private String suministro;

    public ObservableList<SuministSearchView> getViewObservableList(int idOb, String codigo, String tipo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            if (tipo.equals("Sum")) {
                List<Object[]> datosSum = session.createQuery("SELECT zon.codigo as zonC, obj.codigo as objC, niv.codigo as nivC, esp.codigo as espC, sub.codigo as subC, uo.codigo as uoC, bajo.cantidad, bajo.costo FROM Bajoespecificacion bajo LEFT JOIN Unidadobra uo ON bajo.unidadobraId = uo.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Nivel niv ON uo.nivelId = niv.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id LEFT JOIN Recursos rec ON  bajo.idSuministro = rec.id WHERE rec.codigo =: cod AND uo.obraId =: idO GROUP BY zon.codigo, obj.codigo, niv.codigo, esp.codigo, sub.codigo, uo.codigo, bajo.cantidad, bajo.costo").setParameter("idO", idOb).setParameter("cod", codigo).getResultList();

                if (datosSum.size() > 0) {
                    for (Object[] row : datosSum) {
                        viewObservableList.add(new SuministSearchView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString())));
                    }
                } else {
                    List<Object[]> datosSemi = session.createQuery("SELECT zon.codigo as zonC, obj.codigo as objC, niv.codigo as nivC, esp.codigo as espC, sub.codigo as subC, uo.codigo as uoC, bajo.cantidad, bajo.costo FROM Bajoespecificacion bajo LEFT JOIN Unidadobra uo ON bajo.unidadobraId = uo.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Nivel niv ON uo.nivelId = niv.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id LEFT JOIN Suministrossemielaborados rec ON bajo.idSuministro = rec.id WHERE rec.codigo =: cod AND uo.obraId =: idO GROUP BY zon.codigo, obj.codigo, niv.codigo, esp.codigo, sub.codigo, uo.codigo, bajo.cantidad, bajo.costo").setParameter("idO", idOb).setParameter("cod", codigo.trim()).getResultList();
                    for (Object[] row : datosSemi) {
                        viewObservableList.add(new SuministSearchView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(), Double.parseDouble(row[6].toString()), Double.parseDouble(row[7].toString())));
                    }
                }
            } else if (tipo.equals("Rv")) {
                List<Object[]> datosSum = session.createQuery("SELECT zon.codigo as zonC, obj.codigo as objC, niv.codigo as nivC, esp.codigo as espC, sub.codigo as subC, uo.codigo as uoC, bajo.cantRv, bajo.costMat, bajo.costMano, bajo.costEquip FROM Unidadobrarenglon bajo LEFT JOIN Unidadobra uo ON bajo.unidadobraId = uo.id LEFT JOIN Zonas zon ON uo.zonasId = zon.id LEFT JOIN Objetos obj ON uo.objetosId = obj.id LEFT JOIN Nivel niv ON uo.nivelId = niv.id LEFT JOIN Especialidades esp ON uo.especialidadesId = esp.id LEFT JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id LEFT JOIN Renglonvariante rec ON  bajo.renglonvarianteId = rec.id WHERE rec.codigo =: cod AND uo.obraId =: idO GROUP BY zon.codigo, obj.codigo, niv.codigo, esp.codigo, sub.codigo, uo.codigo, bajo.cantRv, bajo.costMat, bajo.costMano, bajo.costEquip").setParameter("idO", idOb).setParameter("cod", codigo).getResultList();
                for (Object[] row : datosSum) {
                    costoTotal = 0.0;
                    costoTotal = Double.parseDouble(row[7].toString()) + Double.parseDouble(row[8].toString()) + Double.parseDouble(row[9].toString());
                    viewObservableList.add(new SuministSearchView(row[0].toString(), row[1].toString(), row[2].toString(), row[3].toString(), row[4].toString(), row[5].toString(), Double.parseDouble(row[6].toString()), costoTotal));
                }
            }
            tx.commit();
            session.close();
            return viewObservableList;

        } catch (Exception ex) {

        }

        return FXCollections.emptyObservableList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkCantidad.setOnMouseClicked(event -> {
            if (checkCantidad.isSelected() == true) {
                cant.setVisible(true);
            } else if (checkCantidad.isSelected() == false) {
                cant.setVisible(false);
            }
        });

        checkCosto.setOnMouseClicked(event -> {
            if (checkCosto.isSelected() == true) {
                costo.setVisible(true);
            } else if (checkCosto.isSelected() == false) {
                costo.setVisible(false);
            }
        });

    }

    public void pasarDatos(int id, String codeS, String tip) {
        suministro = null;
        suministro = codeS;
        if (tip.equals("Sum")) {
            label_title.setText("Búsqueda del Suministro: " + codeS);
        } else if (tip.equals("Rv")) {
            label_title.setText("Búsqueda del Renglón Variante: " + codeS);
        } else if (tip.equals("Ins")) {
            label_title.setText("Búsqueda del Insumo: " + codeS);
        }

        viewObservableList = FXCollections.observableArrayList();
        viewObservableList = getViewObservableList(id, codeS, tip);
        if (viewObservableList.size() > 0) {
            zona.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("zona"));
            objeto.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("objeto"));
            nivel.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("nivel"));
            especialidad.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("especialidad"));
            subespecialidad.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("subespecialidad"));
            unidad.setCellValueFactory(new PropertyValueFactory<SuministSearchView, StringProperty>("unidadObra"));
            cant.setCellValueFactory(new PropertyValueFactory<SuministSearchView, DoubleProperty>("cant"));
            costo.setCellValueFactory(new PropertyValueFactory<SuministSearchView, DoubleProperty>("cost"));
            dataTable.setItems(viewObservableList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setContentText("Suministro: " + codeS + " no encontrado ");
            alert.showAndWait();
        }

    }


    public void handleExportToExcel(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar exportación");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Datos");

            Map<String, Object[]> data = new HashMap<>();
            int countrow = 2;

            if (checkCantidad.isSelected() == false && checkCosto.isSelected() == false) {
                for (SuministSearchView cl : dataTable.getItems()) {
                    data.put("1", new Object[]{suministro, " ", " ", " ", " ", " "});
                    data.put("2", new Object[]{"Zona", "Objetos", "Nivel", "Especialidad", "Subespecialidad", "UO"});

                    countrow++;
                    Object[] datoscl = new Object[6];
                    datoscl[0] = cl.getZona();
                    datoscl[1] = cl.getObjeto();
                    datoscl[2] = cl.getNivel();
                    datoscl[3] = cl.getEspecialidad();
                    datoscl[4] = cl.getSubespecialidad();
                    datoscl[5] = cl.getUnidadObra();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            } else if (checkCantidad.isSelected() == true && checkCosto.isSelected() == false) {
                for (SuministSearchView cl : dataTable.getItems()) {
                    data.put("1", new Object[]{suministro, " ", " ", " ", " ", " ", " "});
                    data.put("2", new Object[]{"Zona", "Objetos", "Nivel", "Especialidad", "Subespecialidad", "UO", "Cantidad"});
                    countrow++;
                    Object[] datoscl = new Object[7];
                    datoscl[0] = cl.getZona();
                    datoscl[1] = cl.getObjeto();
                    datoscl[2] = cl.getNivel();
                    datoscl[3] = cl.getEspecialidad();
                    datoscl[4] = cl.getSubespecialidad();
                    datoscl[5] = cl.getUnidadObra();
                    datoscl[6] = cl.getCant();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            } else if (checkCantidad.isSelected() == true && checkCosto.isSelected() == true) {
                for (SuministSearchView cl : dataTable.getItems()) {
                    data.put("1", new Object[]{suministro, " ", " ", " ", " ", " ", " ", " "});
                    data.put("2", new Object[]{"Zona", "Objetos", "Nivel", "Especialidad", "Subespecialidad", "UO", "Cantidad", "Costo"});
                    countrow++;
                    Object[] datoscl = new Object[8];
                    datoscl[0] = cl.getZona();
                    datoscl[1] = cl.getObjeto();
                    datoscl[2] = cl.getNivel();
                    datoscl[3] = cl.getEspecialidad();
                    datoscl[4] = cl.getSubespecialidad();
                    datoscl[5] = cl.getUnidadObra();
                    datoscl[6] = cl.getCant();
                    datoscl[7] = cl.getCost();
                    data.put(String.valueOf(countrow++), datoscl);
                }
            }


            Set<String> keySet = data.keySet();
            int rownum = 0;
            for (String key : keySet) {

                Row row = sheet.createRow(rownum++);
                Object[] objects = data.get(key);
                int cellnum = 0;

                for (Object obj : objects) {
                    Cell cell = row.createCell(cellnum++);
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

