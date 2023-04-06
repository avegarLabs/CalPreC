package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Recursos;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ActualizarEquipoController implements Initializable {
    private static SessionFactory sf;
    public EquiposController equiposController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_um;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_preciomn;
    @FXML
    private JFXTextField field_cmoe;
    @FXML
    private JFXTextField field_cpo;
    @FXML
    private JFXTextField field_cpe;
    @FXML
    private JFXTextField field_CET;
    @FXML
    private JFXTextField field_otras;
    private int idEquipo;

    @FXML
    private JFXButton btn_close;


    private List<Recursos> recursosList;
    private List<Recursos> updatesRecursos;
    private int countsub = 0;
    private int batchSub = 500;

    /**
     * Method to UPDATE EquipoPropio
     */
    public void updateEquipoPropio(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos updateEquipo = session.get(Recursos.class, id);
            updateEquipo.setCodigo(field_codigo.getText());
            updateEquipo.setDescripcion(text_descripcion.getText());
            updateEquipo.setTipo("3");
            updateEquipo.setUm(field_um.getText());
            updateEquipo.setPreciomn(Double.parseDouble(field_preciomn.getText()));
            updateEquipo.setSalario(Double.parseDouble(field_cmoe.getText()));
            updateEquipo.setPreciomlc(0.0);
            updateEquipo.setCpo(Double.parseDouble(field_cpo.getText()));
            updateEquipo.setCpe(Double.parseDouble(field_cpe.getText()));
            updateEquipo.setCet(Double.parseDouble(field_CET.getText()));
            updateEquipo.setOtra(Double.parseDouble(field_otras.getText()));
            session.update(updateEquipo);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        field_um.setText("he");
    }

    private List<Recursos> getRecursosList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Recursos> recursos = new ArrayList<>();
            recursos = session.createQuery("FROM Recursos WHERE tipo = '3'").getResultList();

            tx.commit();
            session.close();
            return recursos;
        } catch (
                HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }


    public void handleCloseWindow(ActionEvent event) {

        equiposController.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();

    }

    @FXML
    void updateEquipoAction(ActionEvent event) {
        //  updatesRecursos = new ArrayList<>();
        updateEquipoPropio(idEquipo);
        equiposController.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
/*
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

                    Recursos recursos = getRecursoCode(nextRow.getCell(0).toString().trim());
                    if (recursos != null) {
                        recursos.setPreciomn(Double.parseDouble(nextRow.getCell(1).toString()));
                        recursos.setDescripcion(recursos.getDescripcion().trim());
                        updatesRecursos.add(recursos);
                    }


                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Alert alert = new Alert(Alert.AlertType.WARNING);
                //alert.setTitle("Error!");
                //alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
                //alert.showAndWait();
            }

           // updatesRecursos.size();
            saveDespachos(updatesRecursos);

        }
*/
    }

    public void pasarParametros(EquiposController controller, Recursos equipo) {
        // recursosList = new ArrayList<>();
        //recursosList = getRecursosList();
        equiposController = controller;

        field_codigo.setText(equipo.getCodigo());
        text_descripcion.setText(equipo.getDescripcion());
        field_preciomn.setText(String.valueOf(equipo.getPreciomn()));
        // field_preciomlc.setText(String.valueOf(equipo.getPreciomlc()));
        field_cpo.setText(String.valueOf(equipo.getCpo()));
        field_cpe.setText(String.valueOf(equipo.getCpe()));
        field_CET.setText(String.valueOf(equipo.getCet()));
        field_otras.setText(String.valueOf(equipo.getOtra()));
        field_cmoe.setText(String.valueOf(equipo.getSalario()));
        idEquipo = equipo.getId();

    }

}
