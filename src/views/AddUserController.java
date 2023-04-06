package views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import models.ConnectionModel;
import models.Grupos;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXTextField user;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField cargo;
    @FXML
    private JFXTextField depto;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXComboBox<String> cmbogrupo;
    private ArrayList<Grupos> grupoArrayList;
    private ObservableList<String> listGrupos;
    private Integer idGrupo;
    private Usuarios usuarios;

    public ObservableList<String> getGruposList() {
        listGrupos = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            grupoArrayList = (ArrayList<Grupos>) session.createQuery("from Grupos ").list();
            grupoArrayList.forEach(grupo -> {
                listGrupos.add(grupo.getNombre());
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listGrupos;
    }

    public Integer addUsuario(Usuarios usuarios) {
        listGrupos = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer userId = null;
        try {
            tx = session.beginTransaction();

            userId = (Integer) session.save(usuarios);


            tx.commit();
        } catch (Exception ex) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        } finally {
            session.close();
        }
        return userId;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listGrupos = getGruposList();
        cmbogrupo.getItems().setAll(listGrupos);
    }

    public void handleAddUsuario(javafx.event.ActionEvent event) {
        String group = cmbogrupo.getValue();
        grupoArrayList.forEach(grupo -> {
            if (grupo.getNombre().contentEquals(group)) {
                idGrupo = grupo.getId();
            }
        });


        usuarios = new Usuarios();
        usuarios.setUsuario(user.getText());
        usuarios.setNombre(name.getText());
        usuarios.setCargo(cargo.getText());
        usuarios.setDpto(depto.getText());
        usuarios.setContrasena(password.getText());
        usuarios.setGruposId(idGrupo);


        Integer id = addUsuario(usuarios);

        if (id != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Usuario creado satisfactoriamente!");
            alert.showAndWait();
        }


        user.clear();
        name.clear();
        cargo.clear();
        depto.clear();
        password.clear();
        cmbogrupo.getItems().clear();


    }


}
