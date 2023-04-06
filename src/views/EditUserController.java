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

public class EditUserController implements Initializable {

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
    private Integer idUser;
    private Usuarios usuarios;


    public ObservableList<String> getGruposList() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listGrupos = FXCollections.observableArrayList();
            grupoArrayList = (ArrayList<Grupos>) session.createQuery("from Grupos ").getResultList();
            for (Grupos grupo : grupoArrayList) {
                listGrupos.add(grupo.getNombre());
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listGrupos;
    }

    public void updateUsuario(Usuarios usuarios) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Usuarios us = session.get(Usuarios.class, idUser);
            if (us != null) {
                us.setUsuario(usuarios.getUsuario());
                us.setContrasena(usuarios.getContrasena());
                us.setNombre(usuarios.getNombre());
                us.setCargo(usuarios.getCargo());
                us.setDpto(usuarios.getDpto());
                us.setGruposId(usuarios.getGruposId());
                session.update(us);

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listGrupos = getGruposList();
        cmbogrupo.getItems().setAll(listGrupos);
    }

    public void handleAddUsuario(javafx.event.ActionEvent event) {


        var grup = grupoArrayList.stream().filter(g -> g.getNombre().equals(cmbogrupo.getValue())).findFirst().orElse(null);


        if (password.getText().length() <= 10 || user.getText() != null) {
            usuarios = new Usuarios();
            usuarios.setUsuario(user.getText());
            usuarios.setNombre(name.getText());
            usuarios.setCargo(cargo.getText());
            usuarios.setDpto(depto.getText());
            usuarios.setContrasena(password.getText());
            usuarios.setGruposId(grup.getId());

            updateUsuario(usuarios);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Datos del usuario actualizado satisfactoriamente!");
            alert.showAndWait();

            user.clear();
            name.clear();
            cargo.clear();
            depto.clear();
            password.clear();
            cmbogrupo.getItems().clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Error al actualizar los datos del usuario!");
            alert.showAndWait();
        }


    }

    public void pasarUser(Usuarios usuar) {

        idUser = usuar.getId();
        user.setText(usuar.getUsuario());
        password.setText(usuar.getContrasena());
        name.setText(usuar.getNombre());
        cargo.setText(usuar.getCargo());
        depto.setText(usuar.getDpto());

        var grup = grupoArrayList.stream().filter(g -> g.getNombre().equals(usuar.getGruposByGruposId().getNombre())).findFirst().orElse(null);
        cmbogrupo.getSelectionModel().select(grup.getNombre());


    }


}
