package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ComposicionSemielaboradoController implements Initializable {

    private int idSuministro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableList;
    private ComponentesSuministrosCompuestosView compuestosView;
    private ArrayList<Recursos> arraySuministrosList;
    private ArrayList<Semielaboradosrecursos> suministrosArrayList;
    private ComponentesSuministrosCompuestosView tempSuminitro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableGlobalList;

    private boolean flag;
    private int index;

    private SessionFactory sf;

    @FXML
    private Label label_title;

    @FXML
    private Pane content_pane;

    @FXML
    private TableView<ComponentesSuministrosCompuestosView> dataTable;

    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, String> code;

    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> cantidad;

    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> usos;

    @FXML
    private Label insumodescrip;

    @FXML
    private Label insumoum;

    @FXML
    private Label insumomn;

    @FXML
    private Label isumomlc;

    @FXML
    private JFXButton btn_modificar;

    @FXML
    private JFXTextField labelUM;

    @FXML
    private JFXTextField labelMN;

    @FXML
    private JFXTextField labelMlC;

    @FXML
    private JFXTextField labelpeso;

    @FXML
    private JFXTextArea labelDescrip;

    @FXML
    private JFXTextField fieldCodigo;

    @FXML
    private JFXTextArea textDescrip;

    @FXML
    private JFXTextField fieldCantidad;

    @FXML
    private JFXTextField fieldUsos;

    @FXML
    private JFXButton btn_hecho;

    @FXML
    private JFXButton btn_update;


    @FXML
    private JFXTextField fieldCodeJP;

    private ObservableList<ComponentesSuministrosCompuestosView> datos;

    @FXML
    private Pane paneForm;
    private ComponentesSuministrosCompuestosView componentCompuestoView;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ObservableList<SuministrosView> suministrosViewObservableList;
    private Boolean check;
    private SuministrosSemielaboradosController loadController;

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesSemielaborados(int id) {
        observableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, id);
            for (Semielaboradosrecursos semielaboradosrecursos : suministrossemielaborados.getSemielaboradosrecursosById()) {
                observableList.add(new ComponentesSuministrosCompuestosView(semielaboradosrecursos.getRecursosByRecursosId().getId(), semielaboradosrecursos.getRecursosByRecursosId().getCodigo(), semielaboradosrecursos.getRecursosByRecursosId().getDescripcion(), semielaboradosrecursos.getRecursosByRecursosId().getUm(), semielaboradosrecursos.getRecursosByRecursosId().getPreciomn(), 0.0, semielaboradosrecursos.getCantidad(), semielaboradosrecursos.getUsos(), semielaboradosrecursos.getRecursosByRecursosId().getPertenece(), semielaboradosrecursos.getRecursosByRecursosId().getTipo(), semielaboradosrecursos.getRecursosByRecursosId().getPeso()));
            }
            tx.commit();
            session.close();
            return observableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private ComponentesSuministrosCompuestosView createCoponent(String code) {
        return observableGlobalList.parallelStream().filter(comp -> comp.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void saveComponet(Semielaboradosrecursos semielaboradosrecursos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(semielaboradosrecursos);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void saveRemove(int idSemi, int idRec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Semielaboradosrecursos semielaboradosrecursos = (Semielaboradosrecursos) session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId =: idS AND recursosId =: idR ").setParameter("idS", idSemi).setParameter("idR", idRec).getSingleResult();
            if (semielaboradosrecursos != null) {
                session.delete(semielaboradosrecursos);
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

    public void updateSuministroSemielaborados(int idSemielaborados) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados suministrossemielaborados = session.get(Suministrossemielaborados.class, idSemielaborados);
            double val = Double.parseDouble(labelMN.getText().trim());
            suministrossemielaborados.setPreciomn(Math.round(val * 100d) / 100d);
            suministrossemielaborados.setUm(labelUM.getText().trim());
            suministrossemielaborados.setCodigo(fieldCodeJP.getText().trim());
            suministrossemielaborados.setDescripcion(labelDescrip.getText().trim());
            session.update(suministrossemielaborados);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updatePrecio(ActionEvent event) {
        double valMN = dataTable.getItems().parallelStream().map(componentes -> componentes.getCantidad() * componentes.getPreciomn()).reduce(0.0, Double::sum);
        labelMN.setText(" ");
        labelMN.setText(String.valueOf(valMN));
        updateSuministroSemielaborados(idSuministro);
    }

    public void loadComponetes() {
        code.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        cantidad.setPrefWidth(80);
        usos.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("uso"));
        usos.setPrefWidth(50);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComponetes();
        labelUM.setText("S");
        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                insumomn.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomn()));
            }
        });


    }

    public ObservableList<ComponentesSuministrosCompuestosView> getAllSuminitros(String pert) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            observableGlobalList = FXCollections.observableArrayList();
            if (pert.trim().equals("I")) {
                arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE  pertenece != 'R266'").list();
                for (Recursos sum1 : arraySuministrosList) {
                    observableGlobalList.add(new ComponentesSuministrosCompuestosView(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, 0.0, "precons", sum1.getTipo(), sum1.getPeso()));
                }
            } else if (pert.trim().equals("R266")) {
                arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE  pertenece != 'I'").list();
                for (Recursos sum1 : arraySuministrosList) {
                    observableGlobalList.add(new ComponentesSuministrosCompuestosView(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, 0.0, "precons", sum1.getTipo(), sum1.getPeso()));
                }
            } else if (pert.trim().startsWith("TR")) {
                arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece != 'I' OR pertenece != 'R266'").list();
                for (Recursos sum1 : arraySuministrosList) {
                    observableGlobalList.add(new ComponentesSuministrosCompuestosView(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, 0.0, "precons", sum1.getTipo(), sum1.getPeso()));
                }
            }

            tx.commit();
            session.close();
            return observableGlobalList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void updateCompuesto(int idSemi, int idRec, ComponentesSuministrosCompuestosView componentCompuestoView) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Semielaboradosrecursos semielaboradosrecursos = new Semielaboradosrecursos();
            semielaboradosrecursos.setSuministrossemielaboradosId(idSemi);
            semielaboradosrecursos.setRecursosId(componentCompuestoView.getId());
            semielaboradosrecursos.setCantidad(componentCompuestoView.getCantidad());
            semielaboradosrecursos.setUsos(componentCompuestoView.getUso());
            session.saveOrUpdate(semielaboradosrecursos);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void handleLLenarTextarea(ActionEvent event) {

        String[] part = fieldCodigo.getText().split(" - ");

        tempSuminitro = null;
        tempSuminitro = observableGlobalList.parallelStream().filter(sum -> sum.getCodigo().equals(part[0])).findFirst().orElse(null);
        fieldCodigo.clear();
        fieldCodigo.setText(part[0]);
        textDescrip.setText(tempSuminitro.getDescripcion());
    }

    public void pasarParametros(SuministrosSemielaboradosController controller, Suministrossemielaborados productossemielaborados) {
        loadController = controller;
        idSuministro = productossemielaborados.getId();
        fieldCodeJP.setText(productossemielaborados.getCodigo());
        labelDescrip.setText(productossemielaborados.getDescripcion());
        labelUM.setText(productossemielaborados.getUm());
        labelMN.setText(String.valueOf(productossemielaborados.getPreciomn()));
        labelpeso.setText(String.valueOf(productossemielaborados.getPeso()));
        datos = FXCollections.observableArrayList();
        datos = getComponetesSemielaborados(idSuministro);
        dataTable.getItems().setAll(datos);

        getAllSuminitros(productossemielaborados.getPertenec());

        ArrayList<String> sugestion = new ArrayList<String>();
        sugestion.addAll(observableGlobalList.parallelStream().map(global -> global.getCodigo() + " - " + global.getDescripcion()).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(fieldCodigo, sugestion);

        suministrosViewObservableList = FXCollections.observableArrayList();
        suministrosViewObservableList = utilCalcSingelton.getSuministrosViewObservableList(productossemielaborados.getPertenec());
        suministrosViewObservableList.size();


    }

    public void showFormToInsert(ActionEvent event) {
        paneForm.setVisible(true);
        btn_update.setDisable(true);
        btn_hecho.setDisable(false);
    }

    public void closeFormToInsert(ActionEvent event) {
        paneForm.setVisible(false);
        btn_update.setDisable(true);
    }

    public void showFormToUpdate() {
        paneForm.setVisible(true);
        btn_hecho.setDisable(true);
        btn_update.setDisable(false);

        index = dataTable.getSelectionModel().getSelectedIndex();
        compuestosView = dataTable.getSelectionModel().getSelectedItem();
        fieldCodigo.setText(compuestosView.getCodigo());
        textDescrip.setText(compuestosView.getDescripcion());
        fieldCantidad.setText(String.valueOf(compuestosView.getCantidad()));
        fieldUsos.setText(String.valueOf(compuestosView.getUso()));
    }

    public void handleNuevoComponent(ActionEvent event) {
        compuestosView = new ComponentesSuministrosCompuestosView(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), 0.0, Double.parseDouble(fieldCantidad.getText()), Double.parseDouble(fieldUsos.getText()), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso());
        check = myFlag(compuestosView);

        if (check == false) {
            dataTable.getItems().add(compuestosView);
            updatePrecio(event);
            clearPanelField();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El recurso " + compuestosView.getDescripcion() + " ya es parte del semielaborado");
            alert.showAndWait();
        }
    }

    public Boolean myFlag(ComponentesSuministrosCompuestosView suministrosCompuestosView) {
        flag = false;
        dataTable.getItems().forEach(dat -> {
            if (dat.getCodigo().contentEquals(fieldCodigo.getText())) {
                flag = true;
            }
        });
        return flag;
    }


    public void handleModificarMenu(ActionEvent event) {
        if (dataTable.getSelectionModel().getSelectedItem().getCodigo().contentEquals(tempSuminitro.getCodigo())) {
            compuestosView = new ComponentesSuministrosCompuestosView(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), 0.0, Double.parseDouble(fieldCantidad.getText()), Double.parseDouble(fieldUsos.getText()), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso());
            dataTable.getItems().set(index, compuestosView);
        } else {
            compuestosView.setCantidad(Double.parseDouble(fieldCantidad.getText()));
            compuestosView.setUso(Double.parseDouble(fieldUsos.getText()));
            dataTable.getItems().set(index, compuestosView);
        }
        updatePrecio(event);
        clearPanelField();
    }

    public void handleEliminarMenu(ActionEvent event) {
        compuestosView = dataTable.getSelectionModel().getSelectedItem();
        datos.remove(compuestosView);
        dataTable.getItems().remove(compuestosView);
        updatePrecio(event);
    }

    public void handleUpdateSemielaborado(ActionEvent event) {
        try {
            updateSuministroSemielaborados(idSuministro);
            loadController.loadData();
            Stage stage = (Stage) btn_hecho.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText(" Error al actualizar los datos ");
            alert.showAndWait();
        }


    }

    public void clearPanelField() {
        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
    }


}
