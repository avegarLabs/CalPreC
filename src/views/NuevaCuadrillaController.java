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
import models.*;
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
import java.util.stream.Collectors;

public class NuevaCuadrillaController implements Initializable {

    private static SessionFactory sf;
    public CuadrillaContructoraController cuadrillaContructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_empresa;
    @FXML
    private JFXComboBox<String> combo_brigada;
    @FXML
    private JFXComboBox<String> combo_grupo;
    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;

    private ObservableList<String> empresaList;
    private ObservableList<String> brigadaList;
    private ObservableList<String> grupoList;
    private Grupoconstruccion grupoconstruccion;

    @FXML
    private JFXButton btnClose;


    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionArrayList;
    private int batchbajorv = 20;
    private int countbajorv;

    public ArrayList<Cuadrillaconstruccion> getCuadrillaconstruccionArrayList() {
        cuadrillaconstruccionArrayList = new ArrayList<>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuadrillaconstruccionArrayList = (ArrayList<Cuadrillaconstruccion>) session.createQuery("FROM Cuadrillaconstruccion ").list();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return cuadrillaconstruccionArrayList;
    }

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasForm() {

        empresaList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            for (Empresaconstructora empresa : empresaconstructorasList) {
                empresaList.add(empresa.getDescripcion());
            }

            tx.commit();
            session.close();
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
                    brigadaList.add(brigada.getDescripcion());
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

    public ObservableList<String> getGrupoConstruccionForm(String brigada) {

        grupoconstruccionArrayList = new ArrayList<Grupoconstruccion>();
        grupoList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Grupoconstruccion> gruposList = session.createQuery("FROM Grupoconstruccion ").getResultList();
            grupoconstruccionArrayList.addAll(gruposList.parallelStream().filter(grupo -> grupo.getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion().equals(brigada) && grupo.getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().equals(combo_empresa.getValue())).collect(Collectors.toList()));
            grupoList.addAll(grupoconstruccionArrayList.parallelStream().map(grupoconstruccion -> grupoconstruccion.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return grupoList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public List<Grupoconstruccion> getGrupoConstruccionFormList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoconstruccionArrayList = new ArrayList<>();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion ").list();

            tx.commit();
            session.close();
            return grupoconstruccionArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void createCuadrillaList(List<Cuadrillaconstruccion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajorv = 0;

            for (Cuadrillaconstruccion bajoespecificacion : bajoespecificacionList) {
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


    private boolean checkCuadrilla(Cuadrillaconstruccion cuadrillaconstruccion) {

        cuadrillaconstruccionArrayList = new ArrayList<>();
        cuadrillaconstruccionArrayList = getCuadrillaconstruccionArrayList();

        return cuadrillaconstruccionArrayList.stream().filter(o -> o.getCodigo().equals(cuadrillaconstruccion.getCodigo()) && o.getGrupoconstruccionId() == cuadrillaconstruccion.getGrupoconstruccionId()).findFirst().isPresent();
    }


    /**
     * Metodo para agregar una nueva Cuadrilla
     */
    public Integer AddCuadrillaConstrucion(Cuadrillaconstruccion cuadrillaconstruccion) {
        Session cuasession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer cuad = 0;

        try {
            trx = cuasession.beginTransaction();

            boolean res = checkCuadrilla(cuadrillaconstruccion);

            if (res == false) {
                cuad = (Integer) cuasession.save(cuadrillaconstruccion);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la cuadrilla especificado ya existe en este grupo!!! ");
                alert.showAndWait();
            }

            trx.commit();
            cuasession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Error");
            alert.setContentText(he.getMessage());
            alert.showAndWait();

        } finally {
            cuasession.close();
        }
        return cuad;
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == 3) {
            text_descripcion.requestFocus();
        }
    }


    @FXML
    void addCuadrillaAction(ActionEvent event) {
        String cod = field_codigo.getText();
        String descrip = text_descripcion.getText();
        grupoconstruccion = grupoconstruccionArrayList.parallelStream().filter(item -> item.getDescripcion().trim().equals(combo_grupo.getValue().trim())).findFirst().get();
        if (cod != null && descrip != null && grupoconstruccion != null) {
            Cuadrillaconstruccion cuadrillaconstruccion = new Cuadrillaconstruccion();
            cuadrillaconstruccion.setCodigo(cod);
            cuadrillaconstruccion.setDescripcion(descrip);
            cuadrillaconstruccion.setGrupoconstruccionId(grupoconstruccion.getId());
            int cuaId = AddCuadrillaConstrucion(cuadrillaconstruccion);
            clearField();
            cuadrillaContructoraController.loadData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Debe completar los campos del formularios para crear la cuadrilla de construcción");
            alert.showAndWait();
        }

    }

    public void clearField() {
        field_codigo.clear();
        text_descripcion.clear();

    }

    @FXML
    public void createBrigadaList(ActionEvent event) {
        String emp = combo_empresa.getValue();
        combo_brigada.setItems(getBrigadaConstruccionForm(emp));

    }

    @FXML
    public void createGrupoList(ActionEvent event) {
        String brigada = combo_brigada.getValue();
        combo_grupo.setItems(getGrupoConstruccionForm(brigada));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        combo_empresa.setItems(getEmpresasForm());
    }

    public void sentController(CuadrillaContructoraController cuadrillaCont) {

        cuadrillaContructoraController = cuadrillaCont;

        List<Grupoconstruccion> grupoconstruccions = new ArrayList<>();
        grupoconstruccions = getGrupoConstruccionFormList();
        grupoconstruccions.size();
    }


    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        cuadrillaContructoraController.loadData();
    }

    private Grupoconstruccion getGrupoEj(String codeE, String codeB, String codeGrup) {
        return grupoconstruccionArrayList.parallelStream().filter(grupoconstruccion1 -> grupoconstruccion1.getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getCodigo().trim().equals(codeE) && grupoconstruccion1.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo().trim().equals(codeB) && grupoconstruccion1.getCodigo().trim().equals(codeGrup)).findFirst().orElse(null);

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
                List<Cuadrillaconstruccion> cuadrillaconstruccions = new ArrayList<>();
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    Grupoconstruccion grupoconstruccion = getGrupoEj(nextRow.getCell(0).toString().trim(), nextRow.getCell(1).toString().trim(), nextRow.getCell(2).toString().trim());
                    Cuadrillaconstruccion cuadrillaconstruccion = new Cuadrillaconstruccion();
                    cuadrillaconstruccion.setCodigo(nextRow.getCell(3).toString().trim());
                    cuadrillaconstruccion.setDescripcion(nextRow.getCell(4).toString());
                    cuadrillaconstruccion.setGrupoconstruccionId(grupoconstruccion.getId());
                    cuadrillaconstruccions.add(cuadrillaconstruccion);
                }

                createCuadrillaList(cuadrillaconstruccions);
                cuadrillaContructoraController.loadData();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}