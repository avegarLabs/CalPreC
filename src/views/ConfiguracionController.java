package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConfiguracionController implements Initializable {

    private static SessionFactory sf;
    public ConfiguracionController configuracionController;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    Task tarea;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private TableView<UsuariosView> tableUsers;
    @FXML
    private TableColumn<UsuariosView, StringProperty> user;
    @FXML
    private TableColumn<UsuariosView, StringProperty> name;
    @FXML
    private TableColumn<UsuariosView, StringProperty> cargo;
    @FXML
    private TableColumn<UsuariosView, StringProperty> dpto;
    @FXML
    private TableColumn<UsuariosView, StringProperty> grupo;
    @FXML
    private TableView<EmpresaConfigView> tableEmpresaConf;
    @FXML
    private TableView<ConceptosGastosView> tableConceptos;
    @FXML
    private TableColumn<ConceptosGastosView, StringProperty> indice;
    @FXML
    private TableColumn<ConceptosGastosView, StringProperty> concepto;
    @FXML
    private TableColumn<ConceptosGastosView, DoubleProperty> coeficiente;
    @FXML
    private TableColumn<ConceptosGastosView, StringProperty> formula;
    @FXML
    private TableColumn<EmpresaConfigView, StringProperty> codeEmp;
    @FXML
    private TableColumn<EmpresaConfigView, StringProperty> nombEmp;
    @FXML
    private TableColumn<EmpresaConfigView, StringProperty> comercialEmp;
    private ArrayList<Usuarios> usuariosArrayList;
    private ObservableList<UsuariosView> usuariosViewObservableList;
    private UsuariosView usuariosView;
    private ObservableList<EmpresaConfigView> empresaConfigViewObservableList;
    private EmpresaConfigView empresaConfigView;
    private ArrayList<Salario> salarioArrayList;
    private JFXButton btnsalary;
    @FXML
    private VBox flowpane;
    @FXML
    private BorderPane contentPane;
    private ArrayList<Conceptosgasto> conceptosgastoArrayList;
    private ObservableList<ConceptosGastosView> gastosViewObservableList;
    private ConceptosGastosView conceptosGastosView;
    private JFXCheckBox checkBox;
    private Usuarios usuario;
    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    @FXML
    private TableView<TarifaSalarial> tableTarifas;
    @FXML
    private TableColumn<TarifaSalarial, String> colTarifas;
    @FXML
    private JFXComboBox<String> comboResol;
    private ObservableList<TarifasView> tarifasViewObservableList;
    @FXML
    private JFXComboBox<String> comboOperations;
    private ProgressDialog stage;
    private int count;
    private int batchSize = 50;

    public ObservableList<UsuariosView> getUsuariosList() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            usuariosViewObservableList = FXCollections.observableArrayList();
            usuariosArrayList = (ArrayList<Usuarios>) session.createQuery("FROM Usuarios ").getResultList();
            for (Usuarios usuarios : usuariosArrayList) {
                usuariosViewObservableList.add(new UsuariosView(usuarios.getId(), usuarios.getUsuario(), usuarios.getNombre(), usuarios.getCargo(), usuarios.getDpto(), usuarios.getGruposByGruposId().getNombre()));
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return usuariosViewObservableList;
    }

    public void deleteUsuarios(Integer idUser) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Integer op1 = session.createQuery("DELETE FROM Usuarios WHERE id =: idUse").setParameter("idUse", idUser).executeUpdate();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Usuarios getUsuario(Integer id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            usuario = null;
            usuario = session.get(Usuarios.class, id);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return usuario;
    }

    public ObservableList<EmpresaConfigView> getEmpresa() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            empresaConfigViewObservableList = FXCollections.observableArrayList();

            var empresa = session.get(Empresa.class, 1);
            empresaConfigViewObservableList.add(new EmpresaConfigView(empresa.getId(), empresa.getReup(), empresa.getNombre(), empresa.getComercial()));

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaConfigViewObservableList;
    }

    public ObservableList<TarifasView> getSalario() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            tarifasViewObservableList = FXCollections.observableArrayList();
            salarioArrayList = new ArrayList<>();
            salarioArrayList = (ArrayList<Salario>) session.createQuery("FROM Salario ").getResultList();
            for (Salario salario : salarioArrayList) {
                tarifasViewObservableList.add(new TarifasView(salario.getId(), salario.getDescripcion()));
            }

            tx.commit();
            session.close();
            return tarifasViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<ConceptosGastosView> getConceptosgastos() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            ObservableList<ConceptosGastosView> conceptosGastosViews = FXCollections.observableArrayList();
            conceptosgastoArrayList = (ArrayList<Conceptosgasto>) session.createQuery("FROM Conceptosgasto ORDER BY code").getResultList();
            for (Conceptosgasto conceptosgasto : conceptosgastoArrayList) {

                if (conceptosgasto.getCalcular() == 1) {
                    checkBox = new JFXCheckBox();
                    checkBox.setSelected(true);
                } else if (conceptosgasto.getCalcular() == 1) {
                    checkBox = new JFXCheckBox();
                    checkBox.setSelected(false);
                }
                conceptosGastosViews.add(new ConceptosGastosView(conceptosgasto.getId(), conceptosgasto.getCode(), conceptosgasto.getDescripcion(), conceptosgasto.getCoeficiente(), conceptosgasto.getFormula(), 0.0, checkBox, conceptosgasto.getPertence()));
            }

            tx.commit();
            session.close();
            return conceptosGastosViews;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public void deleteConcepto(int idCon) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Conceptosgasto conceptosgasto = session.get(Conceptosgasto.class, idCon);

            if (conceptosgasto != null) {
                int op1 = session.createQuery(" DELETE FROM Subconcepto WHERE conceptoId =: idC ").setParameter("idC", conceptosgasto.getId()).executeUpdate();
                session.delete(conceptosgasto);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void datosUsuarios() {
        user.setCellValueFactory(new PropertyValueFactory<UsuariosView, StringProperty>("usuario"));
        name.setCellValueFactory(new PropertyValueFactory<UsuariosView, StringProperty>("nombre"));
        cargo.setCellValueFactory(new PropertyValueFactory<UsuariosView, StringProperty>("cargo"));
        dpto.setCellValueFactory(new PropertyValueFactory<UsuariosView, StringProperty>("dpto"));
        grupo.setCellValueFactory(new PropertyValueFactory<UsuariosView, StringProperty>("grupo"));

        usuariosViewObservableList = FXCollections.observableArrayList();
        usuariosViewObservableList = getUsuariosList();
        tableUsers.getItems().setAll(usuariosViewObservableList);
    }

    public void datosEmpresa() {
        codeEmp.setCellValueFactory(new PropertyValueFactory<EmpresaConfigView, StringProperty>("reup"));
        nombEmp.setCellValueFactory(new PropertyValueFactory<EmpresaConfigView, StringProperty>("nombre"));
        comercialEmp.setCellValueFactory(new PropertyValueFactory<EmpresaConfigView, StringProperty>("comercial"));

        empresaConfigViewObservableList = FXCollections.observableArrayList();
        empresaConfigViewObservableList = getEmpresa();
        tableEmpresaConf.getItems().setAll(empresaConfigViewObservableList);
    }

    public void deleteSalario(int idCon) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salaraio = session.get(Salario.class, idCon);

            if (salaraio != null) {
                session.delete(salaraio);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void datosSalarios() {
        colTarifas.setCellValueFactory(new PropertyValueFactory<TarifaSalarial, String>("code"));
        tableTarifas.getItems().setAll(tarifaSalarialRepository.getAllTarifasSalarial());
    }

    public void actionShowInformation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TarifaSalarialUpdate.fxml"));
            Parent proot = loader.load();

            TarifasSalarialUpdateController controller = (TarifasSalarialUpdateController) loader.getController();
            controller.setDatoToView(tableTarifas.getSelectionModel().getSelectedItem(), configuracionController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosEmpresa();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void datosConceptos() {
        ActionEvent event = new ActionEvent();
        filterConcptByresol(event);
    }

    public void handleAddUser(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoUsuario.fxml"));
            Parent proot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosUsuarios();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleAddEmpresa(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmpresaConfig.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosEmpresa();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleAddConcepto(ActionEvent event) {
        try {
            Salario resol = utilCalcSingelton.salarioList.parallelStream().filter(sal -> sal.getDescripcion().trim().equals(comboResol.getValue().trim())).findFirst().orElse(null);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Conceptos.fxml"));
            Parent proot = loader.load();

            FormulaController controller = (FormulaController) loader.getController();
            controller.setidResolucion(resol.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosConceptos();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handlUpdateConcepto(ActionEvent event) {
        conceptosGastosView = tableConceptos.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateConceptoConceptos.fxml"));
            Parent proot = loader.load();
            UpdateFormulaController controller = loader.getController();
            controller.cargarConcepto(conceptosGastosView.getId());
            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosConceptos();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleDeleteConcepto(ActionEvent event) {
        conceptosGastosView = tableConceptos.getSelectionModel().getSelectedItem();
        deleteConcepto(conceptosGastosView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Concepto eliminado correctamente!");
        alert.showAndWait();
        datosConceptos();

    }

    public void handleDeleteUser(ActionEvent event) {
        var user = tableUsers.getSelectionModel().getSelectedItem();

        deleteUsuarios(user.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Usuario: " + user.getUsuario() + " eliminado!");
        alert.showAndWait();


        datosUsuarios();

    }

    public void handleUpdateUser(ActionEvent event) {
        usuario = getUsuario(tableUsers.getSelectionModel().getSelectedItem().getId());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateUsuario.fxml"));
            Parent proot = loader.load();

            EditUserController controller = loader.getController();
            controller.pasarUser(usuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosUsuarios();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleUpdateEmpresa(ActionEvent event) {
        var empresa = empresaConfigViewObservableList.stream().filter(e -> e.getReup().equals(tableEmpresaConf.getSelectionModel().getSelectedItem().getReup())).findFirst().orElse(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateEmpresaConfig.fxml"));
            Parent proot = loader.load();

            UpdateEmpresaController controller = loader.getController();
            controller.pasarEmpresa(empresa);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosEmpresa();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleRegisterWindows(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterForm.fxml"));
            Parent proot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleNewTarifaAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TarifaSalarial.fxml"));
            Parent proot = loader.load();

            TarifasSalarialController controller = (TarifasSalarialController) loader.getController();
            controller.passDataToConfiguracion(configuracionController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleDeleteTarifa(ActionEvent event) {
        try {
            tarifaSalarialRepository.deleteTarifaSalarial(tableTarifas.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Tarifa eliminada correctamente!");
            alert.showAndWait();
            datosSalarios();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Error al eliminar la tarifa indicada!");
            alert.showAndWait();
        }

    }

    public void filterConcptByresol(ActionEvent event) {
        if (comboResol.getValue().trim().equals("Resolución 30")) {
            indice.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("code"));
            concepto.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("descripcion"));
            coeficiente.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, DoubleProperty>("coeficiente"));
            formula.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("formula"));

            gastosViewObservableList = FXCollections.observableArrayList();
            gastosViewObservableList.addAll(getConceptosgastos().parallelStream().filter(cg -> cg.getPertenece() == 1).collect(Collectors.toList()));
            tableConceptos.getItems().clear();
            tableConceptos.getItems().setAll(gastosViewObservableList);
        } else if (comboResol.getValue().trim().equals("Resolución 266")) {
            indice.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("code"));
            concepto.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("descripcion"));
            coeficiente.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, DoubleProperty>("coeficiente"));
            formula.setCellValueFactory(new PropertyValueFactory<ConceptosGastosView, StringProperty>("formula"));

            gastosViewObservableList = FXCollections.observableArrayList();
            gastosViewObservableList.addAll(getConceptosgastos().parallelStream().filter(cg -> cg.getPertenece() == 3).collect(Collectors.toList()));
            tableConceptos.getItems().clear();
            tableConceptos.getItems().setAll(gastosViewObservableList);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configuracionController = this;
        datosUsuarios();
        datosEmpresa();
        datosSalarios();

        tableTarifas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                ActionEvent event1 = new ActionEvent();
                showGruposEscalasAction(tableTarifas.getSelectionModel().getSelectedItem());

            }
        });


        ObservableList<String> resolucionesList = FXCollections.observableArrayList();
        resolucionesList.addAll(utilCalcSingelton.getSalarios().stream().map(Salario::getDescripcion).collect(Collectors.toList()));
        comboResol.setItems(resolucionesList);
        comboResol.getSelectionModel().select(resolucionesList.get(0));
        datosConceptos();

        ObservableList<String> listOperaciones = FXCollections.observableArrayList("Importar Conceptos", "Importar SubConceptos", "Importar SubSubConceptps", "Importar SubSubSubConceptos");
        comboOperations.setItems(listOperaciones);
    }

    public void showGruposEscalasAction(TarifaSalarial tarifaSalarial) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GrupoEscalas.fxml"));
            Parent proot = loader.load();

            GruposEscalasController controller = loader.getController();
            controller.passDataToConfiguration(configuracionController, tarifaSalarial);
            contentPane.setCenter(proot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleListConcepto(ActionEvent event) {
        conceptosGastosView = tableConceptos.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListConceptos.fxml"));
            Parent proot = loader.load();

            ListConceptosController controller = loader.getController();
            controller.loadData(conceptosGastosView.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                datosConceptos();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleImportsOperations(ActionEvent event) {
        if (comboOperations.getValue().equals("Importar Conceptos")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el fichero con concetos a importar");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                ceateConceptosList(file.getAbsolutePath());
            }
        } else if (comboOperations.getValue().equals("Importar SubConceptos")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el fichero con subconceptos a importar");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                ceateSubConceptosList(file.getAbsolutePath());
            }
        } else if (comboOperations.getValue().equals("Importar SubSubConceptps")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el fichero con subsubconceptos a importar");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                ceateSubSubConceptosList(file.getAbsolutePath());
            }
        } else if (comboOperations.getValue().equals("Importar SubSubSubConceptos")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione el fichero con subsubsubsubconceptos a importar");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                ceateSubSubSubConceptosList(file.getAbsolutePath());
            }

        }
    }

    private void ceateSubSubSubConceptosList(String absolutePath) {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Analizando el fichero, por favor espere...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();
        try {
            addSubSubSubConceptos(absolutePath);
            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void ceateSubSubConceptosList(String absolutePath) {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Analizando el fichero, por favor espere...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();
        try {
            addSubSubConceptos(absolutePath);
            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void ceateSubConceptosList(String absolutePath) {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Analizando el fichero, por favor espere...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        FileInputStream inputStream = null;

        try {

            addSubConceptos(absolutePath);


            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Ocurrio un error al importar los datos: " + ex.getMessage());
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

    private void ceateConceptosList(String absolutePath) {
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Analizando el fichero, por favor espere...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        try {
            addConceptos(absolutePath);
            tarea = createTime(Math.toIntExact(50));
            stage = new ProgressDialog(tarea);
            stage.setContentText("Contabilizando suministros importados...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addConceptos(String path) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("copy conceptosgasto (id, descripcion, coeficiente, formula, calcular, pertenece, code) FROM '" + path + "' DELIMITER ',' CSV").executeUpdate();
            /* session.createSQLQuery("copy tarconceptosbases (id, descripcion, owner, state, valor) FROM '" + path + "' DELIMITER ',' CSV").executeUpdate();*/
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Conceptos");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    public void addSubConceptos(String path) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("copy subconcepto (id, conceptoid, descripcion, valor ) FROM '" + path + "' DELIMITER ',' CSV").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Conceptos");
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    public void addSubSubConceptos(String path) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("copy subsubconcepto (id, descripcion, subconceptoid, valor ) FROM '" + path + "' DELIMITER ',' CSV").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Conceptos");
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    public void addSubSubSubConceptos(String path) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createSQLQuery("copy subsubsubconcepto (id, descripcion, subconceptoid, valor ) FROM '" + path + "' DELIMITER ',' CSV").executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Conceptos");
            alert.showAndWait();
        } finally {
            session.close();
        }
    }

    public void handleClearConptos(ActionEvent event) {
        deleteAllConcptsHierarchy();
        datosConceptos();

    }

    public void deleteAllConcptsHierarchy() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Integer opt8 = session.createQuery("DELETE FROM Empresaobraconcepto ").executeUpdate();
            Integer opt9 = session.createQuery("DELETE FROM Empresaobrasubconcepto ").executeUpdate();
            Integer opt7 = session.createQuery("DELETE FROM Empresaobrasubsubconcepto ").executeUpdate();
            Integer opt6 = session.createQuery("DELETE FROM Empresaobrasubsubsubconcepto ").executeUpdate();
            Integer opt78 = session.createQuery("DELETE FROM Empresaobraconceptoglobal ").executeUpdate();
            Integer opt90 = session.createQuery("DELETE FROM Empresagastos ").executeUpdate();
            Integer op1 = session.createQuery("DELETE FROM Subsubsubconcepto ").executeUpdate();
            Integer op2 = session.createQuery("DELETE FROM Subsubconcepto ").executeUpdate();
            Integer op3 = session.createQuery("DELETE FROM Subconcepto ").executeUpdate();
            Integer op4 = session.createQuery("DELETE FROM Conceptosgasto ").executeUpdate();

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al borrar Conceptos");
            alert.showAndWait();


        } finally {
            session.close();
        }
    }

}