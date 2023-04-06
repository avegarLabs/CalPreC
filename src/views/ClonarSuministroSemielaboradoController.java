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

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClonarSuministroSemielaboradoController implements Initializable {

    private static String pertenece;
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
    private Suministrossemielaborados suministrossemielaborados;
    private Semielaboradosrecursos semielaboradosrecursos;
    @FXML
    private JFXButton btn_Ok;
    @FXML
    private JFXTextField fieldCodeJP;
    private ObservableList<ComponentesSuministrosCompuestosView> datos;
    private double valnull = 0.0;
    private int idjuego;
    private Boolean check;
    @FXML
    private Pane paneForm;
    private int idObra;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ObservableList<SuministrosView> suministrosViewObservableList;

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
            Query query = session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId = :id");
            query.setParameter("id", id);
            suministrosArrayList = (ArrayList<Semielaboradosrecursos>) ((org.hibernate.query.Query) query).list();
            suministrosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), 0.0, suministro.getCantidad(), suministro.getUsos(), "precons", sum.getTipo(), sum.getPeso());
                observableList.add(compuestosView);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return observableList;
    }

    public void handleLLenarTextarea(ActionEvent event) {

        String[] part = fieldCodigo.getText().split(" - ");

        tempSuminitro = null;
        tempSuminitro = observableGlobalList.parallelStream().filter(sum -> sum.getCodigo().equals(part[0])).findFirst().orElse(null);
        fieldCodigo.clear();
        fieldCodigo.setText(part[0]);
        textDescrip.setText(tempSuminitro.getDescripcion());


    }

    public void loadComponetes() {
        code.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        cantidad.setPrefWidth(80);
        usos.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("uso"));
        usos.setPrefWidth(50);
    }

    /**
     * Este meto es para pasar el recurso de suministro compuesto a suministros solo si el usuario desea
     *
     * @param 'Suminitro Compuesto'
     */
    public Integer addSuministroSemielaborado(Suministrossemielaborados suministro) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer suministroID = null;
        try {
            tx = session.beginTransaction();
            suministroID = (Integer) session.save(suministro);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministroID;
    }

    public void addSuministroSemielaboradoRecursoa(Semielaboradosrecursos semielaboradosrecursos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(semielaboradosrecursos);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    //Este metodo devuelve todos los elemntos de la table suminitros para llenar el
    public ObservableList<ComponentesSuministrosCompuestosView> getAllSuminitros(String pert) {
        observableGlobalList = FXCollections.observableArrayList();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadComponetes();
        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                insumomn.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomn()));
            }
        });

    }

    private SuministrosView getRecursosByCode(String code) {
        return suministrosViewObservableList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void pasarParametros(SuministrosSemielaboradosController controller, Suministrossemielaborados productossemielaborados) {
        pertenece = productossemielaborados.getPertenec();
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


    public void updatePrecio(ActionEvent event) {
        double valMN = dataTable.getItems().parallelStream().map(componentes -> componentes.getCantidad() * componentes.getPreciomn()).reduce(0.0, Double::sum);
        labelMN.setText(" ");
        labelMN.setText(String.valueOf(valMN));
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

    public Boolean myFlag(ComponentesSuministrosCompuestosView suministrosCompuestosView) {
        flag = false;
        dataTable.getItems().forEach(dat -> {
            if (dat.getCodigo().contentEquals(fieldCodigo.getText())) {
                flag = true;
            }
        });
        return flag;
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


    public void handleModificarMenu(ActionEvent event) {
        if (!dataTable.getSelectionModel().getSelectedItem().getCodigo().contentEquals(fieldCodigo.getText())) {
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

    public void handleNewSuministroSemielaborado(ActionEvent event) {

        suministrossemielaborados = new Suministrossemielaborados();
        suministrossemielaborados.setCodigo(fieldCodeJP.getText());
        suministrossemielaborados.setDescripcion(labelDescrip.getText());
        suministrossemielaborados.setUm(labelUM.getText());
        suministrossemielaborados.setPreciomn(Double.parseDouble(labelMN.getText()));

        if (labelpeso.getText() != null) {
            suministrossemielaborados.setPeso(Double.parseDouble(labelpeso.getText()));
        } else {
            suministrossemielaborados.setPeso(0.0);
        }

        suministrossemielaborados.setMermaprod(0.0);
        suministrossemielaborados.setMermamanip(0.0);
        suministrossemielaborados.setMermatrans(0.0);
        suministrossemielaborados.setOtrasmermas(0.0);
        suministrossemielaborados.setCarga(0.0);
        suministrossemielaborados.setCemento(0.0);
        suministrossemielaborados.setArido(0.0);
        suministrossemielaborados.setAsfalto(0.0);
        suministrossemielaborados.setCostequip(0.0);
        suministrossemielaborados.setCostmano(0.0);
        suministrossemielaborados.setCostomat(0.0);
        suministrossemielaborados.setHa(0.0);
        suministrossemielaborados.setHo(0.0);
        suministrossemielaborados.setHe(0.0);
        if (idObra != 0) {
            suministrossemielaborados.setPertenec(String.valueOf(idObra));
        } else if (idObra == 0) {
            suministrossemielaborados.setPertenec(pertenece);
        }

        if (getRecursosByCode(suministrossemielaborados.getCodigo()) == null) {
            Integer inserted = addSuministroSemielaborado(suministrossemielaborados);
            if (inserted != null && dataTable.getItems() != null) {
                dataTable.getItems().forEach(dataTable -> {
                    semielaboradosrecursos = new Semielaboradosrecursos();
                    semielaboradosrecursos.setSuministrossemielaboradosId(inserted);
                    semielaboradosrecursos.setRecursosId(dataTable.getId());
                    semielaboradosrecursos.setCantidad(dataTable.getCantidad());
                    semielaboradosrecursos.setUsos(dataTable.getUso());
                    addSuministroSemielaboradoRecursoa(semielaboradosrecursos);
                });

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Suministros semielaborado  creado correctamente");
                alert.showAndWait();

                Stage stage = (Stage) btn_Ok.getScene().getWindow();
                stage.close();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Este código  de suminitro ya existe ne la base de datos!");
            alert.showAndWait();
        }

    }

    public void clearField() {
        labelDescrip.setText("");
        labelMN.setText("");
        labelpeso.setText("");
        fieldCodeJP.setText("");
        dataTable.getItems().clear();
    }

    public void clearPanelField() {
        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
    }


}
