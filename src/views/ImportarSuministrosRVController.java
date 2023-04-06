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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Obra;
import models.ObrasToImportSuministros;
import models.Recursos;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ImportarSuministrosRVController implements Initializable {

    private static SessionFactory sf;
    public ActualizarObraRVController actualizarObraController;
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

    private List<Recursos> recursosList;


    public List<Recursos> getRecursosList(int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            recursosList = new ArrayList<>();
            recursosList = session.createQuery("FROM  Recursos WHERE  pertenece =: id").setParameter("id", String.valueOf(idOb)).getResultList();
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

    public ObservableList<ObrasToImportSuministros> getListObras() {


        obrasFormTableObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            obraArrayList = (ArrayList<Obra>) session.createQuery("FROM Obra ").list();
            obraArrayList.forEach(obra -> {


                checkBox = new JFXCheckBox();
                checkBox.setText(obra.getCodigo());
                newObrasFormTable = new ObrasToImportSuministros(obra.getId(), checkBox, obra.getDescripion());

                obrasFormTableObservableList.add(newObrasFormTable);


            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return obrasFormTableObservableList;
    }

    public ArrayList<Recursos> getListRecursos(int idObra) {

        recursosArrayList = new ArrayList<Recursos>();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Recursos WHERE pertenece = : id");
            query.setParameter("id", String.valueOf(idObra));

            recursosArrayList = (ArrayList<Recursos>) ((org.hibernate.query.Query) query).list();


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return recursosArrayList;
    }

    public Integer getImport(Recursos recursos, int Idnew) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            recursos.setPertenece(String.valueOf(Idnew));
            id = (Integer) session.save(recursos);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public void addSuministro(ArrayList<Recursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;


        try {
            tx = session.beginTransaction();
            count = 0;
            for (Recursos recursos : recursosArrayList) {
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

    public void loadDatosEmpresas() {
        empresName.setCellValueFactory(new PropertyValueFactory<ObrasToImportSuministros, JFXCheckBox>("obra"));
        empresDescrip.setCellValueFactory(new PropertyValueFactory<ObrasToImportSuministros, StringProperty>("descripcion"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDatosEmpresas();
        datos = FXCollections.observableArrayList();
        datos = getListObras();
        tableEmpresaToImport.getItems().setAll(datos);


    }

    public void pasarParametros(ActualizarObraRVController obraController, int id) {
        idObra = id;
        actualizarObraController = obraController;

        List<Recursos> list = new ArrayList<>();
        list = getRecursosList(idObra);
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

    private int getRecursoId(String code) {
        return recursosList.parallelStream().filter(recursos -> recursos.getCodigo().trim().equals(code.trim())).findFirst().map(Recursos::getId).orElse(0);
    }

    public void loadFile(javafx.event.ActionEvent event) {
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
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            newRecursosArrayList = new ArrayList<>();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (!nextRow.getCell(0).toString().contentEquals("Código") && !nextRow.getCell(1).toString().contentEquals("Descripción") && !nextRow.getCell(2).toString().contentEquals("UM") && !nextRow.getCell(3).toString().contentEquals("Precio MN") && !nextRow.getCell(4).toString().contentEquals("MLC")) {

                    Recursos sumuni = new Recursos();
                    sumuni.setCodigo(nextRow.getCell(0).toString());
                    sumuni.setDescripcion(nextRow.getCell(1).toString());
                    sumuni.setUm(nextRow.getCell(2).toString());
                    sumuni.setPeso(0.0);
                    sumuni.setPreciomn(Double.parseDouble(nextRow.getCell(3).toString()));
                    sumuni.setPreciomlc(Double.parseDouble(nextRow.getCell(4).toString()));
                    sumuni.setMermaprod(0.0);
                    sumuni.setMermatrans(0.0);
                    sumuni.setMermamanip(0.0);
                    sumuni.setOtrasmermas(0.0);
                    sumuni.setPertenece(String.valueOf(idObra));
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

                    if (getRecursoId(sumuni.getCodigo()) == 0) {
                        newRecursosArrayList.add(sumuni);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error!");
                        alert.setContentText("El suministro: " + sumuni.getCodigo() + " ya esta presente en la obra");
                        alert.showAndWait();
                    }
                }

            }

            addSuministro(newRecursosArrayList);

            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


            actualizarObraController.loadDatosSuministros();
            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();


        }

    }


    public void handleImportSumToObra(ActionEvent event) {
        tableEmpresaToImport.getItems().forEach(tableEmpresaToImport -> {
            if (tableEmpresaToImport.getObra().isSelected() == true) {
                recursosArrayList = getListRecursos(tableEmpresaToImport.getId());
            }

            if (recursosArrayList != null) {
                recursosArrayList.forEach(recursos -> {
                    Integer id = getImport(recursos, idObra);
                    if (id != null) {
                        System.out.println(id);
                    }
                });
            }
        });
    }
}
