package views;

import javafx.scene.control.Alert;
import models.ConnectionModel;
import models.Usuarios;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;

public class UsuariosDAO {

    public static UsuariosDAO instancia = null;
    public Usuarios usuario;
    private Session session;


    private UsuariosDAO() {

    }

    public static UsuariosDAO getInstance() {
        if (instancia == null) {
            instancia = new UsuariosDAO();
        }
        return instancia;
    }


    public Usuarios getUsuario(String user, String password) {

        try {
            session = ConnectionModel.createAppConnection().openSession();
            Transaction tx = null;

            tx = session.beginTransaction();

            javax.persistence.Query query = session.createQuery("FROM Usuarios WHERE usuario =: user AND contrasena =: pass").setParameter("user", user).setParameter("pass", password);
            if (query.getSingleResult() != null) {
                usuario = (Usuarios) query.getSingleResult();
            }
            tx.commit();
            session.close();
            return usuario;
        } catch (NoResultException re) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Usuario o Contraseña Incorrecta!");
            alert.showAndWait();

        } finally {
            session.close();
        }

        return usuario;


    }


}
