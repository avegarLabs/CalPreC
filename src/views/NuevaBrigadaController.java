package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Brigadaconstruccion;
import models.ConnectionModel;
import models.Empresaconstructora;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


public class NuevaBrigadaController implements Initializable {

    private static SessionFactory sf;
    @FXML
    public JFXButton btnClose;
    BrigadaConstructoraController brigadaConstructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_empresa;
    @FXML
    private ObservableList<String> descripcionList;
    private ArrayList<Empresaconstructora> arrayEmpresa;
    private ArrayList<Brigadaconstruccion> arrayBrigadaconstruccions;
    private Empresaconstructora empConst;
    private int batchbajorv = 20;
    private int countbajorv;

    public ArrayList<Brigadaconstruccion> getBrigadadeConstruccion() {

        arrayBrigadaconstruccions = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arrayBrigadaconstruccions = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion ").list();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return arrayBrigadaconstruccions;
    }

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasList() {


        descripcionList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arrayEmpresa = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            arrayEmpresa.forEach(empresa -> {
                descripcionList.add(empresa.getDescripcion());
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return descripcionList;
    }

    public List<Empresaconstructora> getEmpresaconstructoraList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arrayEmpresa = new ArrayList<>();
            arrayEmpresa = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();

            tx.commit();
            session.close();
            return arrayEmpresa;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private boolean checkBrigada(Brigadaconstruccion brigadaconstruccion) {
        arrayBrigadaconstruccions = new ArrayList<>();
        arrayBrigadaconstruccions = getBrigadadeConstruccion();

        return arrayBrigadaconstruccions.stream().filter(o -> o.getCodigo().equals(brigadaconstruccion.getCodigo()) && o.getEmpresaconstructoraId() == brigadaconstruccion.getEmpresaconstructoraId()).findFirst().isPresent();
    }

    public void createBrigadaConstList(List<Brigadaconstruccion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajorv = 0;

            for (Brigadaconstruccion bajoespecificacion : bajoespecificacionList) {
                countbajorv++;
                if (countbajorv > 0 && countbajorv % batchbajorv == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == 3) {
            text_descripcion.requestFocus();
        }
    }


    /**
     * Metodo para agregar una nueva brigada
     */
    public Integer AddBrigadaConstructora(Brigadaconstruccion brigadaconstruccion) {
        Session brsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer bri = 0;

        try {
            trx = brsession.beginTransaction();

            boolean res = checkBrigada(brigadaconstruccion);

            if (res == false) {
                bri = (Integer) brsession.save(brigadaconstruccion);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la brigada especificada ya existe en esta empresa!!! ");
                alert.showAndWait();
            }

            trx.commit();
            brsession.close();
            brsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            brsession.close();
        }
        return bri;
    }


    @FXML
    void addBrigadaAction(ActionEvent event) {

        String cod = field_codigo.getText();
        String descrip = text_descripcion.getText();
        String empresa = combo_empresa.getValue();

        arrayEmpresa.forEach(e -> {
            if (e.getDescripcion().contains(empresa)) {
                empConst = e;

            }

        });

        if (cod != null && descrip != null && empConst != null) {
            Brigadaconstruccion brigadaconstruccion = new Brigadaconstruccion();
            brigadaconstruccion.setCodigo(cod);
            brigadaconstruccion.setDescripcion(descrip);
            brigadaconstruccion.setEmpresaconstructoraId(empConst.getId());

            Integer newId = AddBrigadaConstructora(brigadaconstruccion);


            if (newId != null) {

                brigadaConstructoraController.loadDatosBrigada();
                clearField();
            }
        }

    }

    public void clearField() {

        field_codigo.clear();
        text_descripcion.clear();
        combo_empresa.getSelectionModel().clearSelection();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        combo_empresa.setItems(getEmpresasList());

        List<Empresaconstructora> empresaconstructoraList = new ArrayList<>();
        empresaconstructoraList = getEmpresaconstructoraList();
        empresaconstructoraList.size();

    }

    public void sendController(BrigadaConstructoraController brigadaConstructora) {
        brigadaConstructoraController = brigadaConstructora;

    }


    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        brigadaConstructoraController.loadDatosBrigada();
    }

    public Empresaconstructora getEmpresaconstructoraFil(String codeEm) {
        return arrayEmpresa.parallelStream().filter(empresaconstructora -> empresaconstructora.getCodigo().trim().equals(codeEm.trim())).findFirst().orElse(null);
    }

    public void handleLoadExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indique la ubicación del fichero excels");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Workbook workbook = null;
            FileInputStream inputStreamRV = null;

            try {
                inputStreamRV = new FileInputStream(new File(file.getAbsolutePath()));
                workbook = new XSSFWorkbook(inputStreamRV);
                Sheet firstSheet = workbook.getSheetAt(0);
                List<Brigadaconstruccion> brigadaconstruccions = new ArrayList<>();
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    Empresaconstructora empresaconstructora = getEmpresaconstructoraFil(nextRow.getCell(0).toString().trim());
                    Brigadaconstruccion brigadaconstruccion = new Brigadaconstruccion();
                    brigadaconstruccion.setCodigo(nextRow.getCell(1).toString().trim());
                    brigadaconstruccion.setDescripcion(nextRow.getCell(2).toString());
                    brigadaconstruccion.setEmpresaconstructoraId(empresaconstructora.getId());
                    brigadaconstruccions.add(brigadaconstruccion);
                }

                createBrigadaConstList(brigadaconstruccions);
                brigadaConstructoraController.loadDatosBrigada();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
