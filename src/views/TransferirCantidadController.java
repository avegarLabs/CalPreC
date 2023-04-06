package views;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import models.ConnectionModel;
import models.Despachoscl;
import models.Recursos;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TransferirCantidadController implements Initializable {


    private static SessionFactory sf;

    @FXML
    private JFXTextField field_codigo;
    private Despachoscl des;

    @FXML
    private TextArea descrip;
    private List<Recursos> recursosList;
    private Recursos rec;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    List<Recursos> getRecursosList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Recursos> recursos = new ArrayList<>();
            recursos = session.createQuery("FROM  Recursos ").getResultList();
            tx.commit();
            session.close();
            return recursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private Recursos getRecursoId(String code) {
        return recursosList.parallelStream().filter(recursos -> recursos.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void cargarSuministros(ActionEvent event) {
        rec = getRecursoId(field_codigo.getText().trim());
        if (rec != null) {
            descrip.setText(rec.getDescripcion() + " ( " + String.valueOf(rec.getPreciomn()) + " / " + rec.getUm() + " )");
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Suministro no encontrado");
            alert.showAndWait();

        }

    }

    public void handleModificar(ActionEvent event) {
        updateDespachos(rec);
    }

    public void pasarDatos(Despachoscl despachoscl) {
        recursosList = new ArrayList<>();
        recursosList = getRecursosList();
        des = despachoscl;
        des.getId();
    }

    private void updateDespachos(Recursos recursos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Despachoscl despa = (Despachoscl) session.createQuery(" FROM Despachoscl Where id =: idD ").setParameter("idD", des.getId()).getSingleResult();
            despa.setSuministroId(recursos.getId());
            session.update(despa);

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
