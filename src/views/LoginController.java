package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static SessionFactory sf;
    public UsuariosDAO usuariosDAO;
    @FXML
    private JFXTextField labelUser;
    @FXML
    private JFXPasswordField labelpasword;
    @FXML
    private JFXButton btnAction;
    private Usuarios usuario;
    private Runtime garbache;
    private CreateLogFile createLogFile;
    private Session session;
    private Registro registro;
    private static int time = 43200000;
    @FXML
    private ProgressBar progresBar;


    @FXML
    private JFXButton btnClose;


    public Registro getRegistro() {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            if (session.get(Registro.class, 1) == null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterForm.fxml"));
                    Parent proot = loader.load();


                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                registro = new Registro();
            } else {
                registro = session.get(Registro.class, 1);
            }
            tx.commit();
            session.close();
            return registro;

        } catch (NoResultException re) {


        } finally {
            session.close();
        }
        return registro;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showIndicatorBayState(0);
    }

    UtilCalcSingelton util = UtilCalcSingelton.getInstance();

    public void handleLoginUser(ActionEvent event) {
        List<Renglonvariante> renglonvarianteList = new ArrayList<>();
        renglonvarianteList = util.getAllRenglones();
        util.updateDatabaseEstructure();
        util.clearNullInEquipments();
        util.clearBajoEspecificacioUO();
        util.updateTarifaEmpresaObra();
        List<Salario> list = util.getSalarios();
        if (list.parallelStream().filter(item -> item.getDescripcion().trim().equals("Resolución 86")).findFirst().isPresent()) {
            util.alterTableTagColumn();
            util.fixesSalarioTable();
        }
        // util.fixesInsumos();
        //showRVEstructure(renglonvarianteList);
        int dias = 0;
        registro = new Registro();
        registro = getRegistro();
        if (registro != null && registro.getCode() == null) {
            LocalDate date = LocalDate.now();
            dias = (int) ((Date.valueOf(date).getTime() - registro.getFecha().getTime()) / time);


            if (dias >= 30) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Ha vencido el período de prueba en el CalPreC");
                alert.showAndWait();

                Platform.exit();
                System.exit(0);
            } else {
                var user = labelUser.getText();
                var pass = labelpasword.getText();

                usuariosDAO = UsuariosDAO.getInstance();
                usuario = usuariosDAO.getUsuario(user, pass);

                if (usuario != null) {
                    showIndicatorBayState(1);
                    try {

                        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("CartasLimites.fxml"));
                        Parent proot = loader.load();

                        CartaLimiteController controller = loader.getController();
                        controller.checkUsuario(usuario);


                        Stage stage = new Stage();
                        stage.setScene(new Scene(proot));
                        stage.show();*/

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                        Parent proot = loader.load();

                        Controller controller = loader.getController();
                        controller.sendUser();

                        Stage stage = new Stage();
                        stage.setScene(new Scene(proot));
                        stage.setTitle("Cálculo del Presupuesto de Construcción 2022.8.1");
                        stage.setResizable(false);
                        stage.setMaximized(true);

                        labelUser.clear();
                        labelpasword.clear();

                        garbache = Runtime.getRuntime();
                        garbache.gc();

                        stage.show();
                        Stage stage1 = (Stage) btnAction.getScene().getWindow();
                        stage1.close();


                    } catch (Exception ex) {
                        createLogFile = new CreateLogFile();
                        createLogFile.createLogMessage(ex.getMessage());

                    }

                }
            }
        } else if (registro != null && registro.getCode() != null) {
            var user = labelUser.getText();
            var pass = labelpasword.getText();

            usuariosDAO = UsuariosDAO.getInstance();
            usuario = usuariosDAO.getUsuario(user, pass);

            if (usuario != null) {
                showIndicatorBayState(1);

                try {

                    /*FXMLLoader loader = new FXMLLoader(getClass().getResource("CartasLimites.fxml"));
                    Parent proot = loader.load();

                    CartaLimiteController controller = loader.getController();
                    controller.checkUsuario(usuario);


                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.show();*/


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                    Parent proot = loader.load();

                    Controller controller = loader.getController();
                    controller.sendUser();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.setTitle("Cálculo del Presupuesto de Construcción 2022.8.1");
                    //stage.setMaximized(true);
                    stage.show();

                    labelUser.clear();
                    labelpasword.clear();

                    garbache = Runtime.getRuntime();
                    garbache.gc();


                    Stage stage1 = (Stage) btnAction.getScene().getWindow();
                    stage1.close();


                } catch (Exception ex) {
                    createLogFile = new CreateLogFile();
                    createLogFile.createLogMessage(ex.getMessage());

                    /*Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(this.getClass().getName() + ex.getMessage());
                    alert.showAndWait();
                    */

                }

            }


        }

    }

    public void hadleCloseLogin(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void showIndicatorBayState(int state) {
        if (state == 1) {
            progresBar.setVisible(true);
            System.out.println("Estado visible");
        } else if (state == 0) {
            progresBar.setVisible(false);
            System.out.println("Estado oculto");
        }

    }


}

