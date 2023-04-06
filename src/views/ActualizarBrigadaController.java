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
import javafx.stage.Stage;
import models.Brigadaconstruccion;
import models.ConnectionModel;
import models.Empresaconstructora;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ActualizarBrigadaController implements Initializable {
    private static SessionFactory sf;
    public BrigadaConstructoraController brigadaConstructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_empresa;
    @FXML
    private ObservableList<String> descripcionList;
    private ArrayList<Empresaconstructora> arrayEmpresa;
    private Empresaconstructora empConst;
    private int Idbrigada;


    @FXML
    private JFXButton btnClose;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arrayEmpresa = new ArrayList<Empresaconstructora>();
            descripcionList = FXCollections.observableArrayList();
            arrayEmpresa = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").getResultList();
            descripcionList.addAll(arrayEmpresa.parallelStream().map(emp -> emp.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return descripcionList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        //return new ArrayList();
        return FXCollections.emptyObservableList();
    }

    /* Method to UPDATE Brigada */
    public void updateBrigada(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Brigadaconstruccion brigadaconstruccion = session.get(Brigadaconstruccion.class, id);
            brigadaconstruccion.setCodigo(field_codigo.getText());
            brigadaconstruccion.setDescripcion(text_descripcion.getText());
            empConst = arrayEmpresa.parallelStream().filter(emp -> emp.getDescripcion().equals(combo_empresa.getValue())).findFirst().orElse(null);
            brigadaconstruccion.setEmpresaconstructoraByEmpresaconstructoraId(empConst);
            session.update(brigadaconstruccion);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    void addBrigadaAction(ActionEvent event) {

        updateBrigada(Idbrigada);

        brigadaConstructoraController.loadDatosBrigada();

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        combo_empresa.setItems(getEmpresasList());

    }

    public void pasarParametros(BrigadaConstructoraController controller, Brigadaconstruccion brigadaconstruccion) {

        brigadaConstructoraController = controller;
        field_codigo.setText(brigadaconstruccion.getCodigo());
        text_descripcion.setText(brigadaconstruccion.getDescripcion());
        combo_empresa.getSelectionModel().select(brigadaconstruccion.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        Idbrigada = brigadaconstruccion.getId();


    }

    public void handleClose(ActionEvent event) {

        brigadaConstructoraController.loadDatosBrigada();

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

}
