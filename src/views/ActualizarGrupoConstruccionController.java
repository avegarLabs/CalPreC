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
import models.Grupoconstruccion;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ActualizarGrupoConstruccionController implements Initializable {

    private static SessionFactory sf;
    public GrupoConstruccionController grupoConstruccionController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXComboBox<String> combo_brigada;
    @FXML
    private JFXTextField field_empresa;
    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ArrayList<Brigadaconstruccion> brigadaconstruccionArrayList;

    private ObservableList<String> empresaList;
    private ObservableList<String> brigadaList;
    private Brigadaconstruccion brigadaconstruccion;

    private int idgrupo;
    private Brigadaconstruccion brigada;

    @FXML
    private JFXButton btnClose;


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<String> getEmpresasForm() {

        //empresaconstructorasList = new ArrayList<Empresaconstructora>();
        empresaList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();
            empresaconstructorasList.forEach(empresa -> {
                empresaList.add(empresa.getDescripcion());
            });

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
            brigadaconstruccionArrayList.forEach(brigada -> {
                if (brigada.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion().contains(empresa)) {
                    brigadaList.add(brigada.getDescripcion());
                }
            });

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return brigadaList;
    }

    /**
     * Metodo para actualizar grupo
     */
    public void updateGrupo(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Grupoconstruccion grupoconstruccion = session.get(Grupoconstruccion.class, id);
            grupoconstruccion.setCodigo(field_codigo.getText());
            grupoconstruccion.setDescripcion(text_descripcion.getText());
            brigadaconstruccionArrayList.forEach(g -> {
                if (g.getDescripcion().contains(combo_brigada.getValue())) {
                    brigada = g;

                }

            });
            grupoconstruccion.setBrigadaconstruccionByBrigadaconstruccionId(brigada);
            session.update(grupoconstruccion);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // String emp = field_empresa.getText();


    }

    public void pasarParametros(GrupoConstruccionController controller, Grupoconstruccion grupoconstruccionToUpdate) {

        grupoConstruccionController = controller;

        field_codigo.setText(grupoconstruccionToUpdate.getCodigo());
        text_descripcion.setText(grupoconstruccionToUpdate.getDescripcion());
        field_empresa.setText(grupoconstruccionToUpdate.getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
        combo_brigada.setItems(getBrigadaConstruccionForm(field_empresa.getText()));
        combo_brigada.getSelectionModel().select(grupoconstruccionToUpdate.getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion());

        idgrupo = grupoconstruccionToUpdate.getId();

    }

    @FXML
    public void handeleUpdateAction(ActionEvent event) {
        updateGrupo(idgrupo);

        grupoConstruccionController.loadData();

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }

    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        grupoConstruccionController.loadData();

    }

}
