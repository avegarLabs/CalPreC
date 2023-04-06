package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ImportarSuministrosGeneralController implements Initializable {

    private static SessionFactory sf;
    private static String pert;
    public SuministrosController suministrosController;
    Task tarea;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private TableView<ObrasToImportSuministros> tableEmpresaToImport;
    @FXML
    private TableColumn<ObrasToImportSuministros, JFXCheckBox> empresName;
    @FXML
    private TableColumn<ObrasToImportSuministros, StringProperty> empresDescrip;
    @FXML
    private ListView<String> listFileRow;
    @FXML
    private ListView<String> listDatabaseRow;
    @FXML
    private ListView<String> listMaping;
    @FXML
    private Label labelPath;
    private ArrayList<Obra> obraArrayList;
    private ObservableList<ObrasToImportSuministros> obrasFormTableObservableList;
    private ObrasToImportSuministros newObrasFormTable;
    private JFXCheckBox checkBox;
    private ObservableList<ObrasToImportSuministros> datos;
    private ObservableList<String> nameRows;
    private int idObra;
    private ArrayList<Recursos> recursosArrayList;
    private FileInputStream inputStream;
    private Workbook workbook;
    private Sheet sheet;
    private Iterator iterator;
    @FXML
    private Pane paneProcess;
    @FXML
    private JFXProgressBar progressBar;
    private ArrayList<Recursos> newRecursosArrayList;
    private int batchSize = 25;
    private int count;
    private ProgressDialog stage;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ObservableList<SuministrosView> suministrosViewObservableList;


    private List<Recursos> recursosList;
    private List<Recursos> recursosToUpdate;

    public List<Recursos> getRecursosList(int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            recursosList = new ArrayList<>();
            recursosList = session.createQuery("FROM  Recursos WHERE  pertenece is null ").getResultList();
            tx.commit();
            session.close();
            return recursosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void addSuministro(ArrayList<Recursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            count = 0;
            for (Recursos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Suministros");
            alert.setContentText("Ha ocurrido un error al importar los suministros, posibles causas: El suministros ya existe en la base de detos o los datos del fichero excel no estan completos!");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Recursos> list = new ArrayList<>();
        list = getRecursosList(1);
        list.size();
    }

    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < val; i++) {
                    Thread.sleep(val / 2);
                    updateProgress(i + 1, val);
                }
                return true;
            }
        };
    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(2000);
                return true;
            }
        };
    }

    public void updateSuministro(List<Recursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            count = 0;
            for (Recursos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.update(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Suministros");
            alert.setContentText("Ha ocurrido un error al importar los suministros, posibles causas: El suministros ya existe en la base de detos o los datos del fichero excel no estan completos!");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    private SuministrosView getRecursosByCode(String code) {
        return suministrosViewObservableList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void loadFile(ActionEvent event) {
        nameRows = FXCollections.observableArrayList();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            labelPath.setText(file.getAbsolutePath());

            tarea = createTimeMesage();
            ProgressDialog dialog = new ProgressDialog(tarea);
            dialog.setContentText("Analizando el fichero, por favor espere...");
            dialog.setTitle("Espere...");
            new Thread(tarea).start();
            dialog.showAndWait();

            FileInputStream inputStream = null;
            nameRows = FXCollections.observableArrayList();

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
                newRecursosArrayList = new ArrayList<>();
                recursosToUpdate = new ArrayList<>();
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    if (!nextRow.getCell(0).toString().contentEquals("Código") && !nextRow.getCell(1).toString().contentEquals("Descripción") && !nextRow.getCell(2).toString().contentEquals("UM") && !nextRow.getCell(3).toString().contentEquals("Precio MN")) {
                        Recursos sumuni = new Recursos();
                        sumuni.setCodigo(nextRow.getCell(0).toString().trim());
                        sumuni.setDescripcion(nextRow.getCell(1).toString().trim());
                        sumuni.setUm(nextRow.getCell(2).toString().trim());
                        sumuni.setPeso(0.0);
                        sumuni.setPreciomn(Double.parseDouble(nextRow.getCell(3).toString()));
                        sumuni.setPreciomlc(0.0);
                        sumuni.setMermaprod(0.0);
                        sumuni.setMermatrans(0.0);
                        sumuni.setMermamanip(0.0);
                        sumuni.setOtrasmermas(0.0);
                        sumuni.setPertenece(pert);
                        sumuni.setTipo("1");
                        sumuni.setGrupoescala("I");
                        sumuni.setCostomat(0.0);
                        sumuni.setCostmano(0.0);
                        sumuni.setCostequip(0.0);
                        sumuni.setHa(0.0);
                        sumuni.setHe(0.0);
                        sumuni.setHo(0.0);
                        sumuni.setCemento(0.0);
                        sumuni.setArido(0.0);
                        sumuni.setAsfalto(0.0);
                        sumuni.setCarga(0.0);
                        sumuni.setPrefab(0.0);
                        sumuni.setCet(0.0);
                        sumuni.setCpo(0.0);
                        sumuni.setCpe(0.0);
                        sumuni.setOtra(0.0);
                        sumuni.setActive(1);

                        if (getRecursosByCode(sumuni.getCodigo()) == null) {
                            newRecursosArrayList.add(sumuni);

                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error!");
                            alert.setContentText("Suministros: " + sumuni.getCodigo() + " ya esta presente en al base de datos");
                            alert.showAndWait();
                        }
                    }
                }
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
                alert.showAndWait();
            }
            addSuministro(newRecursosArrayList);
            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

            suministrosController.loadData();
            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();


        }
    }

    public void passPerteneceSUM(String pertenece, SuministrosController controller) {
        suministrosController = controller;
        pert = pertenece;
        suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList = utilCalcSingelton.getSuministrosViewObservableList(pert);
    }


}
