package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.stream.Collectors;

public class EquiposController implements Initializable {

    private static SessionFactory sf;
    public EquiposController equiposController;
    @FXML
    public JFXComboBox<String> comboTarifas;
    Task tarea;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<SuministrosView> dataTable;
    @FXML
    private TableColumn<SuministrosView, StringProperty> code;
    @FXML
    private TableColumn<SuministrosView, StringProperty> descrip;
    @FXML
    private TableColumn<SuministrosView, StringProperty> um;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> mn;
    private SuministrosView suministroView;
    private ArrayList<Recursos> listSuministros;
    private ObservableList<SuministrosView> suministrosViewsList;
    private Recursos equipo;
    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    @FXML
    private JFXButton btn_add;
    @FXML
    private TableColumn<SuministrosView, DoubleProperty> cmoe;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    private ProgressDialog stage;
    private int count;
    private int batchSize = 50;

    public void deleteEquipo(Integer id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Recursos recur = session.get(Recursos.class, id);
            session.delete(recur);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        equiposController = this;

        ObservableList<String> tarifasLst = FXCollections.observableList(utilCalcSingelton.salarioList.stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasLst);
    }

    private ObservableList<SuministrosView> reloadSuministros(String tipo) {
        ObservableList<SuministrosView> suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList.addAll(suministrosViewsList.parallelStream().filter(suministrosView -> suministrosView.getPertene().trim().equals(tipo)).collect(Collectors.toList()));
        return suministrosViewObservableList;
    }

    /**
     * Bloques de métodos que continen operaciones de bases de datos
     */

    //Este metodo devuelve un la lista con todos los elementos de la tabla equipos del precons
    public ObservableList<SuministrosView> getPreconsEquipos() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listSuministros = new ArrayList<Recursos>();
            suministrosViewsList = FXCollections.observableArrayList();
            listSuministros = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '3'").getResultList();
            for (Recursos suminist : listSuministros) {
                if (suminist.getSalario() != null) {
                    suministrosViewsList.add(new SuministrosView(suminist.getId(), suminist.getCodigo(), suminist.getDescripcion(), suminist.getUm(), suminist.getPeso(), Math.round(suminist.getPreciomn() * 100d) / 100d, Math.round(suminist.getPreciomlc() * 100d) / 100d, suminist.getGrupoescala(), suminist.getTipo(), suminist.getPertenece(), Math.round(suminist.getSalario() * 100d) / 100d, utilCalcSingelton.createCheckBox(suminist.getActive())));
                } else {
                    suministrosViewsList.add(new SuministrosView(suminist.getId(), suminist.getCodigo(), suminist.getDescripcion(), suminist.getUm(), suminist.getPeso(), Math.round(suminist.getPreciomn() * 100d) / 100d, Math.round(suminist.getPreciomlc() * 100d) / 100d, suminist.getGrupoescala(), suminist.getTipo(), suminist.getPertenece(), 0.0, utilCalcSingelton.createCheckBox(suminist.getActive())));
                }
            }
            tx.commit();
            session.close();
            return suministrosViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void handleEquipoAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoEquipo.fxml"));
            Parent proot = loader.load();


            NuevoEquipoController controller = loader.getController();
            controller.pasarController(equiposController);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleUpdateEquipoAction(ActionEvent event) {

        suministroView = dataTable.getSelectionModel().getSelectedItem();

        equipo = new Recursos();
        listSuministros.forEach(items -> {
            if (items.getId() == suministroView.getId()) {
                equipo = items;
            }
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarEquipo.fxml"));
            Parent proot = loader.load();


            ActualizarEquipoController controller = loader.getController();
            controller.pasarParametros(equiposController, equipo);

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                loadData();

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void checkUser(Usuarios user) {

        if (user.getGruposId() != 1) {

            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);
        }

    }

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(550);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        cmoe.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("salario"));
        cmoe.setPrefWidth(100);
        suministrosViewsList = FXCollections.observableArrayList();
        suministrosViewsList = getPreconsEquipos();
        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(suministrosViewsList, p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });

    }

    public void handleCleanViewByResolt(ActionEvent event) {
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        code.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("descripcion"));
        descrip.setPrefWidth(550);
        um.setCellValueFactory(new PropertyValueFactory<SuministrosView, StringProperty>("um"));
        um.setPrefWidth(50);
        mn.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("preciomn"));
        mn.setPrefWidth(100);
        cmoe.setCellValueFactory(new PropertyValueFactory<SuministrosView, DoubleProperty>("salario"));
        cmoe.setPrefWidth(100);

        FilteredList<SuministrosView> filteredData = new FilteredList<SuministrosView>(reloadSuministros(tag.trim()), p -> true);

        SortedList<SuministrosView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(suministrosView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = suministrosView.toString().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    public void handleDeleteEquipo(ActionEvent event) {
        suministroView = dataTable.getSelectionModel().getSelectedItem();
        deleteEquipo(suministroView.getId());
        loadData();
    }

    private Recursos getRecursoTypeEquipo(SuministrosView suministrosView) {
        return listSuministros.parallelStream().filter(item -> item.getCodigo().trim().equals(suministrosView.getCodigo().trim()) && item.getPertenece().trim().equals(suministrosView.getPertene().trim())).findFirst().orElse(null);
    }

    public void handleUpdateDatosSalario(ActionEvent event) {
        if (comboTarifas.getValue() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el fichero con los datos");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {

                tarea = createTimeMesage();
                ProgressDialog dialog = new ProgressDialog(tarea);
                dialog.setContentText("Analizando el fichero, por favor espere...");
                dialog.setTitle("Espere...");
                new Thread(tarea).start();
                dialog.showAndWait();

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
                    List<Recursos> recursosList = new ArrayList<>();

                    while (iterator.hasNext()) {
                        Row nextRow = iterator.next();
                        for (SuministrosView item : dataTable.getItems()) {
                            if (item.getCodigo().trim().equals(nextRow.getCell(0).toString().trim())) {
                                Recursos recursos = getRecursoTypeEquipo(item);
                                if (recursos != null) {
                                    recursos.setSalario(Double.parseDouble(nextRow.getCell(1).toString()));
                                    recursosList.add(recursos);
                                }
                            }
                        }
                    }

                    updateSalarioRecurso(recursosList);

                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
                    alert.showAndWait();
                }

                tarea = createTime(Math.toIntExact(50));
                stage = new ProgressDialog(tarea);
                stage.setContentText("Verificando datos importados...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

                loadData();
                handleCleanViewByResolt(event);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Seleccione la instrucción presupuestaria ");
            alert.showAndWait();
        }
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

    public void updateSalarioRecurso(List<Recursos> recursosArrayList) {
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
            int opt1 = session.createQuery(" UPDATE Recursos SET salario = 0.0 WHERE salario = null ").executeUpdate();

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


}
