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
import models.Grupoconstruccion;
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

public class NuevoGrupoConstruccionController implements Initializable {

    private static SessionFactory sf;
    public GrupoConstruccionController construccionController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_empresa;
    @FXML
    private JFXComboBox<String> combo_brigada;
    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;


    private ObservableList<String> empresaList;
    private ObservableList<String> brigadaList;
    private Brigadaconstruccion brigadaconstruccion;

    @FXML
    private JFXButton btnClose;
    private int batchbajorv = 20;
    private int countbajorv;

    public ArrayList<Grupoconstruccion> getGrupoconstruccionArrayList() {
        grupoconstruccionArrayList = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion ").list();


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoconstruccionArrayList;
    }

    public void createGrupoConstList(List<Grupoconstruccion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajorv = 0;

            for (Grupoconstruccion bajoespecificacion : bajoespecificacionList) {
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


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasForm() {

        //empresaconstructorasList = new ArrayList<Empresaconstructora>();
        empresaList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            empresaconstructorasList.forEach(empresa -> {
                empresaList.add(empresa.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaList;
    }

    public ObservableList<String> getBrigadaConstruccionForm(String empresa) {

        brigadaconstruccionArrayList = new ArrayList<Brigadaconstruccion>();
        brigadaList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion ").list();
            for (Brigadaconstruccion brigada : brigadaconstruccionArrayList) {
                if (brigada.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().contains(empresa)) {
                    brigadaList.add(brigada.getCodigo() + " - " + brigada.getDescripcion());
                }
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadaList;
    }

    public List<Brigadaconstruccion> getBrigadaconstruccionList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaList = FXCollections.observableArrayList();
            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion ").list();

            tx.commit();
            session.close();
            return brigadaconstruccionArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }


    private boolean checkGrupo(Grupoconstruccion grupoconstruccion) {
        grupoconstruccionArrayList = new ArrayList<>();
        grupoconstruccionArrayList = getGrupoconstruccionArrayList();
        return grupoconstruccionArrayList.stream().filter(o -> o.getCodigo().equals(grupoconstruccion.getCodigo()) && o.getBrigadaconstruccionId() == grupoconstruccion.getBrigadaconstruccionId()).findFirst().isPresent();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == 3) {
            text_descripcion.requestFocus();
        }
    }


    /**
     * Metodo para agregar un nuevo grupo
     */
    public Integer AddGrupoConstrucion(Grupoconstruccion grupoconstruccion) {
        Session grsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer gru = 0;

        try {
            trx = grsession.beginTransaction();

            boolean res = checkGrupo(grupoconstruccion);

            if (res == false) {
                gru = (Integer) grsession.save(grupoconstruccion);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código del grupo especificado ya existe en esta brigada!!! ");
                alert.showAndWait();
            }

            trx.commit();
            grsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            grsession.close();
        }
        return gru;
    }


    @FXML
    void addGrupoConstAction(ActionEvent event) {

        String code = field_codigo.getText();
        String descrip = text_descripcion.getText();
        String[] brigad = combo_brigada.getValue().split(" - ");

        Empresaconstructora emp = empresaconstructorasList.stream().filter(em -> em.getDescripcion().equals(combo_empresa.getValue())).findFirst().orElse(null);


        for (Brigadaconstruccion br : brigadaconstruccionArrayList) {
            if (br.getEmpresaconstructoraId() == emp.getId() && br.getCodigo().contentEquals(brigad[0])) {
                brigadaconstruccion = br;
            }
        }


        if (code != null && descrip != null && brigadaconstruccion != null) {
            Grupoconstruccion grupoconstruccion = new Grupoconstruccion();
            grupoconstruccion.setCodigo(code);
            grupoconstruccion.setDescripcion(descrip);
            grupoconstruccion.setBrigadaconstruccionId(brigadaconstruccion.getId());

            int idNew = AddGrupoConstrucion(grupoconstruccion);

            construccionController.loadData();

            clearField();
        }
    }

    public void clearField() {

        field_codigo.clear();
        text_descripcion.clear();
        combo_brigada.getSelectionModel().clearSelection();
    }

    @FXML
    public void createBrigadaList(ActionEvent event) {
        String emp = combo_empresa.getValue();
        combo_brigada.setItems(getBrigadaConstruccionForm(emp));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_empresa.setItems(getEmpresasForm());

        List<Brigadaconstruccion> list = new ArrayList<>();
        list = getBrigadaconstruccionList();
        list.size();

    }


    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void sendController(GrupoConstruccionController controller) {

        construccionController = controller;

    }

    public Brigadaconstruccion getBrigadaconstruccionFil(String codeEm, String codeBr) {
        return brigadaconstruccionArrayList.parallelStream().filter(brigada -> brigada.getEmpresaconstructoraByEmpresaconstructoraId().getCodigo().trim().equals(codeEm) && brigada.getCodigo().trim().equals(codeBr)).findFirst().orElse(null);
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
                List<Grupoconstruccion> grupoconstruccions = new ArrayList<>();
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    Brigadaconstruccion brigadaconstruccionn = getBrigadaconstruccionFil(nextRow.getCell(0).toString().trim(), nextRow.getCell(1).toString().trim());
                    Grupoconstruccion grupoconstruccion = new Grupoconstruccion();
                    grupoconstruccion.setCodigo(nextRow.getCell(2).toString().trim());
                    grupoconstruccion.setDescripcion(nextRow.getCell(3).toString());
                    grupoconstruccion.setBrigadaconstruccionId(brigadaconstruccionn.getId());
                    grupoconstruccions.add(grupoconstruccion);
                }

                createGrupoConstList(grupoconstruccions);
                construccionController.loadData();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

}
