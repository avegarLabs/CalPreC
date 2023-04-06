package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Registro;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private JFXPasswordField labelpasword;
    @FXML
    private JFXButton btnAction;
    @FXML
    private JFXCheckBox evaluarCheck;
    private Date desdeDate;

    public void setRegisterApp(Registro registerApp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.persist(registerApp);

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

        evaluarCheck.setOnMouseClicked(event -> {
            if (evaluarCheck.isSelected() == true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Evaluar CalPreC");
                alert.setContentText("Usted dispone de 30 días para evaluar la aplicación CalPreC!");
                alert.showAndWait();

                labelpasword.setDisable(true);
            }
        });


    }

    public void handleRegister() {

        if (evaluarCheck.isSelected() == true) {
            LocalDate now = LocalDate.now();

            Registro registro = new Registro();
            registro.setId(1);
            registro.setFecha(Date.valueOf(now));
            registro.setTipo("Prueba");

            setRegisterApp(registro);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("CalPreC");
            alert.setContentText("Sistema en evaluación");
            alert.showAndWait();

            Stage stage = (Stage) btnAction.getScene().getWindow();
            stage.close();

        } else {
            LocalDate now = LocalDate.now();

            Registro registro = new Registro();
            registro.setId(1);
            registro.setFecha(Date.valueOf(now));
            registro.setTipo("Producción");
            registro.setCode(labelpasword.getText());

            if (registro.getCode().contains("VERTICE") && registro.getCode().contains("CALPRECV01") && registro.getCode().length() == 25) {
                setRegisterApp(registro);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CalPreC");
                alert.setContentText("Sistema registrado correctamente!");
                alert.showAndWait();

                Stage stage = (Stage) btnAction.getScene().getWindow();
                stage.close();

                Platform.exit();
                System.exit(0);

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("CalPreC");
                alert.setContentText("Código de Activación Incorrecto!");
                alert.showAndWait();
            }


        }

    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
