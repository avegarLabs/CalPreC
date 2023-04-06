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
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Recursos;
import models.Salario;
import models.UtilCalcSingelton;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevoManoController implements Initializable {

    private static SessionFactory sf;
    public ManoObraController manoObraCont;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_um;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_preciomn;
    @FXML
    private JFXTextField field_preciomlc;
    @FXML
    private JFXComboBox<String> combo_grupo;
    @FXML
    private JFXButton btn_close;

    public ObservableList<String> Grupos() {
        ObservableList<String> grupos = FXCollections.observableArrayList("II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII");

        return grupos;
    }

    public Integer addManoPropia(Recursos recursos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer manoID = null;
        try {
            tx = session.beginTransaction();
            manoID = (Integer) session.save(recursos);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return manoID;
    }

    @FXML
    public void addManoAction(ActionEvent event) {

        Recursos sumuni = new Recursos();
        sumuni.setCodigo(field_codigo.getText());

        sumuni.setDescripcion(text_descripcion.getText());
        sumuni.setUm(field_um.getText());
        sumuni.setPeso(0.0);
        sumuni.setPreciomn(Double.parseDouble(field_preciomn.getText()));
        sumuni.setPreciomlc(0.0);
        sumuni.setMermaprod(0.0);
        sumuni.setMermatrans(0.0);
        sumuni.setMermamanip(0.0);
        sumuni.setOtrasmermas(0.0);
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(manoObraCont.comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        sumuni.setPertenece(tag);
        sumuni.setTipo("2");
        sumuni.setGrupoescala(combo_grupo.getValue());
        sumuni.setCostomat(0.0);
        sumuni.setCostmano(0.0);
        sumuni.setCostequip(0.0);
        sumuni.setHa(0.0);
        sumuni.setHe(0.0);
        sumuni.setHo(0.0);
        sumuni.setCemento(0.0);
        sumuni.setArido(0.0);
        sumuni.setAsfalto(0.0);
        sumuni.setCarga(0.0);
        sumuni.setPrefab(0.0);
        sumuni.setCet(0.0);
        sumuni.setCpo(0.0);
        sumuni.setCpe(0.0);
        sumuni.setOtra(0.0);

        Integer id = addManoPropia(sumuni);

        if (id != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Mano de obra agregada satisfactoriamente!");
            alert.showAndWait();
        }

        manoObraCont.loadData();
        clearFields();

    }


    public void clearFields() {

        field_codigo.clear();
        text_descripcion.clear();
        field_preciomn.clear();
        //  field_preciomlc.clear();
        combo_grupo.getSelectionModel().clearSelection();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void parametros(ManoObraController controller) {

        manoObraCont = controller;
        combo_grupo.setItems(Grupos());
        field_um.setText("hh");
    }

    public void handleClose(ActionEvent event) {

        manoObraCont.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}
