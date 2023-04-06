package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Grupoejecucion;
import models.Nivelespecifico;
import models.UniddaObraView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NuevoNivelEspecificoController implements Initializable {

    private static SessionFactory sf;
    RenglonVarianteObraController renglonVarianteObraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXComboBox<String> comboGrupos;
    @FXML
    private JFXButton btn_add;
    private Integer IdObra;
    private Integer idEmp;
    private Integer idZon;
    private Integer idObj;
    private Integer idNiv;
    private Integer idEsp;
    private Integer idSub;
    private ArrayList<Grupoejecucion> grupoejecucionArrayList;
    private ObservableList<String> listGrupos;
    private Grupoejecucion grupoejecucion;
    private Nivelespecifico nivel;
    private Integer fieldLenth = 2;

    @FXML
    private JFXButton btn_close;


    public Integer AddNivelEspecifico(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer unidId = null;
        try {
            tx = session.beginTransaction();

            unidId = (Integer) session.save(nivelespecifico);


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidId;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private UniddaObraView checkCode(String code) {
        return renglonVarianteObraController.dataTable.getItems().parallelStream().filter(uniddaObraView -> uniddaObraView.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == fieldLenth) {
            field_descripcion.requestFocus();

        }
    }

    @FXML
    public void addNewNivelEspecifico(ActionEvent event) {
        if (checkCode(field_codigo.getText()) == null && field_codigo.getText().trim().length() == 2) {
            nivel = new Nivelespecifico();
            nivel.setCodigo(field_codigo.getText());
            nivel.setDescripcion(field_descripcion.getText());
            nivel.setObraId(IdObra);
            nivel.setEmpresaconstructoraId(idEmp);
            nivel.setZonasId(idZon);
            nivel.setObjetosId(idObj);
            nivel.setNivelId(idNiv);
            nivel.setEspecialidadesId(idEsp);
            nivel.setSubespecialidadesId(idSub);
            Integer id = AddNivelEspecifico(nivel);

            renglonVarianteObraController.handleCargardatos(event);
            clearField();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alerta");
            alert.setContentText("Error al declarar el nivel especifico: " + field_codigo.getText());
            alert.showAndWait();
        }


    }

    public void clearField() {
        field_codigo.clear();
        field_descripcion.clear();
    }


    public void pasarParametros(RenglonVarianteObraController controller, int idObra, int idEmpresa, int idZona, int idObjetos, int idNivel, int idEspecialidad, int idSubespecialidad) {

        renglonVarianteObraController = controller;

        IdObra = idObra;
        idEmp = idEmpresa;
        idZon = idZona;
        idObj = idObjetos;
        idNiv = idNivel;
        idEsp = idEspecialidad;
        idSub = idSubespecialidad;
    }

    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }


}
