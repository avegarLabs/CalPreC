package views;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import models.ConnectionModel;
import models.Empresa;
import models.Nomencladorempresa;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddUEmpresaController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXTextField reup;
    @FXML
    private JFXTextArea nombre;
    @FXML
    private JFXTextField comercial;
    private Empresa empresa;
    private ArrayList<Nomencladorempresa> nomencladorempresaArrayList;
    private ObservableList<String> listeEmpresa;
    private Nomencladorempresa nomencladorempresa;

    public ObservableList<String> getListaEmpresa() {
        listeEmpresa = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nomencladorempresaArrayList = (ArrayList<Nomencladorempresa>) session.createQuery("FROM Nomencladorempresa ").list();
            for (Nomencladorempresa nomencladorempresa : nomencladorempresaArrayList) {
                listeEmpresa.add(nomencladorempresa.getReup() + " - " + nomencladorempresa.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listeEmpresa;
    }

    public Integer addEmpresa(Empresa empresa) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer emp = null;
        try {
            tx = session.beginTransaction();
            emp = (Integer) session.save(empresa);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return emp;
    }

    public void handleDefineEmpresa() {
        String code = reup.getText().substring(0, 5);

        nomencladorempresa = nomencladorempresaArrayList.stream().filter(n -> n.getReup().contentEquals(code)).findFirst().orElse(null);

        reup.clear();
        reup.setText(nomencladorempresa.getReup());
        nombre.setText(nomencladorempresa.getDescripcion());


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getListaEmpresa();
        TextFields.bindAutoCompletion(reup, listeEmpresa);

    }


    public void handleAddEmpresa(ActionEvent event) {

        empresa = new Empresa();
        empresa.setReup(reup.getText());
        empresa.setNombre(nombre.getText());
        empresa.setComercial(comercial.getText());

        Integer id = addEmpresa(empresa);

        if (id != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Empresa registrada!");
            alert.showAndWait();

            reup.clear();
            nombre.clear();
            comercial.clear();

        }

    }


}
