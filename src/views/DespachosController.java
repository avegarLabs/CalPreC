package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DespachosController implements Initializable {
    private static SessionFactory sf;
    @FXML
    private JFXTextField field_codigo;

    @FXML
    private JFXTextField field_um;

    @FXML
    private JFXTextArea text_descripcion;

    @FXML
    private JFXTextField field_preciomn;

    @FXML
    private JFXTextField field_preciomlc;

    @FXML
    private JFXButton btn_close;

    @FXML
    private TableView<DespachosConfirm> tableSum;

    @FXML
    private TableColumn<DespachosConfirm, String> code;

    @FXML
    private TableColumn<DespachosConfirm, String> descrip;

    @FXML
    private TableColumn<DespachosConfirm, String> um;

    @FXML
    private TableColumn<DespachosConfirm, Double> cant;

    private int obra;
    private int empresa;
    private int zona;
    private int objeto;
    private int especialidad;
    private List<CartaLimiteModelView> materialesList;
    private CartaLimiteController controller;
    private CartaLimiteModelView carta;
    private int countsub = 0;
    private int batchSub = 500;

    @FXML
    private TextArea fielPrecio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarController(List<CartaLimiteModelView> list, int idOb, int idEmp, int idZon, int idObj, int idEs, ArrayList<Despachoscl> despachoscls, CartaLimiteController cartaLimiteController) {
        materialesList = new ArrayList<>();
        materialesList = list;

        obra = idOb;
        empresa = idEmp;
        zona = idZon;
        objeto = idObj;
        especialidad = idEs;

        int numero = despachoscls.parallelStream().filter(despacho -> despacho.getObraId() == idOb && despacho.getEspecialidadId() == idEs).collect(Collectors.toList()).size();
        field_codigo.setText(String.valueOf(LocalDate.now().getYear()).substring(2, 4) + "-" + idOb + "E" + idEs + 00 + numero);

        List<String> sugestionsList = new ArrayList<>();
        materialesList.sort(Comparator.comparing(CartaLimiteModelView::getCode));
        sugestionsList.addAll(materialesList.parallelStream().filter(carta -> carta.getDisp() > 0).map(carta -> carta.getCode().trim() + " / " + carta.getDescrip().trim()).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(field_um, sugestionsList).setPrefWidth(470);


        controller = cartaLimiteController;
    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    private CartaLimiteModelView getCartaLimiteModelView(String code) {
        return materialesList.parallelStream().filter(carta -> carta.getCode().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void handleSelectSuministros(ActionEvent event) {
        if (field_um.getText() != null) {
            String[] valSelection = field_um.getText().split(" / ");
            field_um.setText(" ");
            carta = getCartaLimiteModelView(valSelection[0]);
            text_descripcion.setText(carta.getCode() + " / " + carta.getDescrip());
            field_preciomlc.setText(String.valueOf(carta.getDisp()));
            fielPrecio.setText(String.valueOf(carta.getPrecio()) + " / " + carta.getUm());
        }
    }

    public void handleCargarTable(ActionEvent event) {
        double val = Double.parseDouble(field_preciomlc.getText());
        double canti = Double.parseDouble(field_preciomn.getText());
        if (canti > val) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("La cantidad a despacher es mayor a la disponible");
            alert.showAndWait();

        } else {
            code.setCellValueFactory(new PropertyValueFactory<DespachosConfirm, String>("code"));
            descrip.setCellValueFactory(new PropertyValueFactory<DespachosConfirm, String>("descrip"));
            um.setCellValueFactory(new PropertyValueFactory<DespachosConfirm, String>("um"));
            cant.setCellValueFactory(new PropertyValueFactory<DespachosConfirm, Double>("cantidad"));
            field_preciomn.setText(" ");
            tableSum.getItems().add(new DespachosConfirm(carta.getId(), carta.getCode(), carta.getDescrip(), carta.getUm(), canti));
        }

    }

    private List<Recursos> recursosList;

    List<Recursos> getRecursosList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            List<Recursos> recursos = new ArrayList<>();
            recursos = session.createQuery("FROM  Recursos ").getResultList();
            tx.commit();
            session.close();
            return recursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private int getRecursoId(String code) {
        return recursosList.parallelStream().filter(recursos -> recursos.getCodigo().trim().equals(code.trim())).findFirst().map(Recursos::getId).orElse(0);
    }

    public void handleDespacharCartaLimite(ActionEvent event) {

        List<Despachoscl> despachosclList = new ArrayList<>();
        for (DespachosConfirm item : tableSum.getItems()) {
            Despachoscl despachoscl = new Despachoscl();
            despachoscl.setVale(field_codigo.getText());
            despachoscl.setObraId(obra);
            despachoscl.setEmpresaId(empresa);
            despachoscl.setZonaId(zona);
            despachoscl.setObjetoId(objeto);
            despachoscl.setEspecialidadId(especialidad);
            despachoscl.setSuministroId(item.getIdSuministro());
            despachoscl.setCantidad(item.getCantidad());
            despachoscl.setFecha(Date.valueOf(LocalDate.now()));
            despachoscl.setUsuario(controller.user.getUsuario());
            despachoscl.setEspecialista(controller.user.getNombre());
            despachoscl.setEstado("0");
            despachoscl.setValido("0");
            despachosclList.add(despachoscl);
        }

        try {
            saveDespachos(despachosclList);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("OK!");
            alert.setContentText("Vale " + field_codigo.getText() + " insertado correctamente");
            alert.showAndWait();

            controller.handleGetMateriales(event);
            closeWindow(event);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Error al procesar el vale: " + field_codigo.getText());
            alert.showAndWait();
        }


/*
        recursosList = new ArrayList<>();
        recursosList = getRecursosList();
        List<Despachoscl> despachosclList = new ArrayList<>();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {


            FileInputStream inputStream = null;

            try {
                inputStream = new FileInputStream(new File(file.getAbsolutePath()));

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }

            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(inputStream);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {


                Sheet firstSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = firstSheet.iterator();


                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    Despachoscl despachoscl = new Despachoscl();
                    despachoscl.setVale(nextRow.getCell(6).toString().trim());
                    despachoscl.setObraId(Integer.parseInt(nextRow.getCell(0).toString().substring(0, 3)));
                    despachoscl.setEmpresaId(Integer.parseInt(nextRow.getCell(1).toString().substring(0, 2)));
                    despachoscl.setZonaId(Integer.parseInt(nextRow.getCell(2).toString().substring(0, 3)));
                    despachoscl.setObjetoId(Integer.parseInt(nextRow.getCell(3).toString().substring(0, 3)));
                    despachoscl.setEspecialidadId(Integer.parseInt(nextRow.getCell(4).toString().substring(0, 1)));
                    despachoscl.setSuministroId(getRecursoId(nextRow.getCell(5).toString().trim()));
                    despachoscl.setCantidad(Double.parseDouble((nextRow.getCell(7).toString().trim())));
                    despachoscl.setFecha(Date.valueOf(LocalDate.now()));
                    despachoscl.setUsuario(nextRow.getCell(8).toString().trim());
                    despachoscl.setEspecialista(nextRow.getCell(9).toString().trim());
                    despachoscl.setEstado("0");
                    despachoscl.setValido(nextRow.getCell(11).toString().trim());
                    despachosclList.add(despachoscl);
                }
            } catch (Exception ex) {
               ex.printStackTrace();
                // Alert alert = new Alert(Alert.AlertType.WARNING);
                //alert.setTitle("Error!");
                //alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
                //alert.showAndWait();
            }

            //despachosclList.size();
            saveDespachos(despachosclList);

        }
*/

    }

    public void deleteSuministInTable(ActionEvent event) {
        tableSum.getItems().remove(tableSum.getSelectionModel().getSelectedItem());
    }


    private void saveDespachos(List<Despachoscl> despachosclList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countsub = 0;
            for (Despachoscl sub : despachosclList) {
                countsub++;
                if (countsub > 0 && countsub % batchSub == 0) {
                    session.flush();
                    session.clear();
                }
                session.persist(sub);
            }

            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Error al procesar el vale: " + field_codigo.getText());
            alert.showAndWait();
        }
    }
}



