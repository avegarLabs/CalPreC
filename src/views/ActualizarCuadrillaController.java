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
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ActualizarCuadrillaController implements Initializable {

    private static SessionFactory sf;
    public CuadrillaContructoraController cuadrillaContructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_empresa;
    @FXML
    private JFXTextField field_brigada;
    @FXML
    private JFXComboBox<String> combo_grupo;
    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;
    private ArrayList<Grupoconstruccion> grupoconstruccionArrayList;

    private ObservableList<String> empresaList;
    private ObservableList<String> brigadaList;
    private ObservableList<String> grupoList;
    private Grupoconstruccion grupoconstruccion;
    private int idCuadrilla;

    @FXML
    private JFXButton btnClose;


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasForm() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaList = FXCollections.observableArrayList();
            empresaconstructorasList = new ArrayList<>();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").getResultList();
            empresaList.addAll(empresaconstructorasList.parallelStream().map(empresaconstructora -> empresaconstructora.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return empresaList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaList;
    }

    public ObservableList<String> getBrigadaConstruccionForm(String empresa) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            brigadaconstruccionArrayList = new ArrayList<>();
            brigadaList = FXCollections.observableArrayList();
            brigadaconstruccionArrayList = (ArrayList<Brigadaconstruccion>) session.createQuery("FROM Brigadaconstruccion ").getResultList();
            brigadaList.addAll(brigadaconstruccionArrayList.parallelStream().filter(brigadaconstruccion -> brigadaconstruccion.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().equals(empresa)).map(brigadaconstruccion -> brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));
            /*
            brigadaconstruccionArrayList.forEach(brigada -> {
                  if (brigada.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().contains(empresa)) {

                }
            });
*/
            tx.commit();
            session.close();
            return brigadaList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getGrupoConstruccionForm(String brigada) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoconstruccionArrayList = new ArrayList<>();
            grupoList = FXCollections.observableArrayList();
            grupoconstruccionArrayList = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion ").getResultList();
            grupoList.addAll(grupoconstruccionArrayList.parallelStream().filter(grupoconstruccion -> grupoconstruccion.getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion().equals(brigada)).map(grupoconstruccion -> grupoconstruccion.getDescripcion()).collect(Collectors.toList()));

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


    /**
     * Metodo para agregar una nueva Cuadrilla
     */
    public Integer AddCuadrillaConstrucion(Cuadrillaconstruccion cuadrillaconstruccion) {

        Session cuasession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer cuad = 0;

        try {
            trx = cuasession.beginTransaction();
            cuad = (Integer) cuasession.save(cuadrillaconstruccion);
            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            cuasession.close();
        }
        return cuad;
    }

    /**
     * Metodo para actualizar cuadrilla
     */
    public void updateCuadrilla(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Cuadrillaconstruccion cuadrillaconstruccion = session.get(Cuadrillaconstruccion.class, id);
            cuadrillaconstruccion.setCodigo(field_codigo.getText());
            cuadrillaconstruccion.setDescripcion(text_descripcion.getText());
            grupoconstruccion = grupoconstruccionArrayList.parallelStream().filter(g -> g.getDescripcion().equals(combo_grupo.getValue())).findFirst().orElse(null);

            cuadrillaconstruccion.setGrupoconstruccionByGrupoconstruccionId(grupoconstruccion);
            session.update(cuadrillaconstruccion);
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
    public void updateCuadrillaAction(ActionEvent event) {


        updateCuadrilla(idCuadrilla);

        cuadrillaContructoraController.loadData();

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pasarParametros(CuadrillaContructoraController controller, Cuadrillaconstruccion cuadrillaconstruccionToUpdate) {

        cuadrillaContructoraController = controller;

        field_codigo.setText(cuadrillaconstruccionToUpdate.getCodigo());
        text_descripcion.setText(cuadrillaconstruccionToUpdate.getDescripcion());
        field_empresa.setText(cuadrillaconstruccionToUpdate.getGrupoconstruccionByGrupoconstruccionId().getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        field_brigada.setText(cuadrillaconstruccionToUpdate.getGrupoconstruccionByGrupoconstruccionId().getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion());
        combo_grupo.setItems(getGrupoConstruccionForm(field_brigada.getText()));
        combo_grupo.getSelectionModel().select(cuadrillaconstruccionToUpdate.getGrupoconstruccionByGrupoconstruccionId().getDescripcion());
        idCuadrilla = cuadrillaconstruccionToUpdate.getId();

    }


    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
        cuadrillaContructoraController.loadData();
    }


}
