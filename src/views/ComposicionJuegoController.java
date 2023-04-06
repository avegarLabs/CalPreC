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

public class ComposicionJuegoController implements Initializable {


    private int idSuministro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableList;
    private ComponentesSuministrosCompuestosView compuestosView;
    private ArrayList<Recursos> arraySuministrosList;
    private ArrayList<Juegorecursos> suministrosArrayList;
    private ComponentesSuministrosCompuestosView tempSuminitro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableGlobalList;


    /*


    private ArrayList<Composicionjuegopropiosumprecons> arrayCompPreconsList;
    private ArrayList<Composicionjuegopropio> arrayJuegoPropioList;

    private ArrayList<Suministrospropios> arrayListSuministrospropiosList;


    private Juegoproductopropios juegoproductopropios;
    private Suministros sumComponent;
    private Suministrospropios sumpropComponent;
    private Composicionjuegopropiosumprecons composicionjuegopropio;
    private Composicionjuegopropio composicionjuegosumpropio;
    private ArrayList<Composicionjuegopropiosumprecons> allComponetes;
*/
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

    private int idObra;


    private Juegoproducto juegoproducto;
    private Juegorecursos juegorecursos;

    private ObservableList<ComponentesSuministrosCompuestosView> datos;
    private double valnull = 0.0;
    private int idjuego;
    private Boolean check;

    @FXML
    private Pane paneForm;

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesJuego(int id) {


        observableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Juegorecursos WHERE juegoproductoId = :id");
            query.setParameter("id", id);
            suministrosArrayList = (ArrayList<Juegorecursos>) ((org.hibernate.query.Query) query).list();
            suministrosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "precons", sum.getTipo(), sum.getPeso());
                observableList.add(compuestosView);


            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return observableList;
    }

    //Este metodo devuelve todos los elemntos de la table suminitros para llenar el
    public ObservableList<ComponentesSuministrosCompuestosView> getAllSuminitros() {
        observableGlobalList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '1'").list();
            arraySuministrosList.forEach(sum1 -> {
                compuestosView = new ComponentesSuministrosCompuestosView(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, 0.0, sum1.getPertenece(), sum1.getTipo(), sum1.getPeso());
                observableGlobalList.add(compuestosView);
            });


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return observableGlobalList;
    }


    public Integer addJuegoProductoPropio(Juegoproducto juegoproducto) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer newJuego = null;

        try {
            tx = session.beginTransaction();
            newJuego = (Integer) session.save(juegoproducto);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return newJuego;
    }


    /**
     * Agregar la composición a pertir de suministros
     */
    public void addComposucionJuegoProductoPrecons(Juegorecursos juegorecursos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.persist(juegorecursos);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


    public void loadComponetes() {
        code.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        cantidad.setPrefWidth(80);


    }

    public void handleLLenarTextarea(ActionEvent event) {

        String[] partCode = fieldCodigo.getText().split(" - ");

        observableGlobalList.forEach(suministros -> {
            if (suministros.getCodigo().contentEquals(partCode[0])) {
                fieldCodigo.clear();
                fieldCodigo.setText(partCode[0]);
                textDescrip.setText(suministros.getDescripcion());
                tempSuminitro = suministros;
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadComponetes();
        getAllSuminitros();
        labelUM.setText("jg");

        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                insumomn.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomn()));
                // isumomlc.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomlc()));
            }
        });
        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo() + " - " + global.getDescripcion());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion);

    }

    public void pasarParametros(JuegoProductoView juegoproducto) {

        if (juegoproducto != null) {
            idSuministro = juegoproducto.getId();
            fieldCodeJP.setText(juegoproducto.getCodigo());
            labelDescrip.setText(juegoproducto.getDescripcion());
            labelMN.setText(String.valueOf(juegoproducto.getPreciomn()));
            //  labelMlC.setText(String.valueOf(juegoproducto.getPreciomlc()));
            labelpeso.setText(String.valueOf(juegoproducto.getPeso()));

            datos = FXCollections.observableArrayList();
            datos = getComponetesJuego(idSuministro);
            dataTable.getItems().setAll(datos);

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

    public void showFormToInsert(ActionEvent event) {
        paneForm.setVisible(true);
        btn_hecho.setDisable(false);
        btn_update.setDisable(true);


    }


    public void showFormToUpdate() {
        paneForm.setVisible(true);
        btn_update.setDisable(false);
        btn_hecho.setDisable(true);
        index = dataTable.getSelectionModel().getSelectedIndex();
        compuestosView = dataTable.getSelectionModel().getSelectedItem();
        fieldCodigo.setText(compuestosView.getCodigo());
        textDescrip.setText(compuestosView.getDescripcion());
        fieldCantidad.setText(String.valueOf(compuestosView.getCantidad()));
    }

    public void handleNuevoComponent(ActionEvent event) {

        compuestosView = new ComponentesSuministrosCompuestosView(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), tempSuminitro.getPreciomlc(), Double.parseDouble(fieldCantidad.getText()), 0.0, tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso());
        check = myFlag(compuestosView);

        if (check == false) {
            //datos.add(compuestosView);
            dataTable.getItems().add(compuestosView);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El recurso " + compuestosView.getDescripcion() + " ya es parte del juego");
            alert.showAndWait();

        }

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();

    }


    public void handleModificarMenu(ActionEvent event) {
        compuestosView.setCantidad(Double.parseDouble(fieldCantidad.getText()));
        dataTable.getItems().set(index, compuestosView);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
    }

    public void handleEliminarMenu(ActionEvent event) {
        compuestosView = dataTable.getSelectionModel().getSelectedItem();

        dataTable.getItems().remove(compuestosView);

    }

}
