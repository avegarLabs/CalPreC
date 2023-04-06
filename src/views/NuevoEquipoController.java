package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NuevoEquipoController implements Initializable {
    private static SessionFactory sf;
    public EquiposController equiposCont;
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
    private JFXTextField field_cpo;
    @FXML
    private JFXTextField field_cpe;
    @FXML
    private JFXTextField field_CET;
    @FXML
    private JFXTextField field_otras;
    private Integer idEquip;
    private ArrayList<Recursos> equiposList;
    @FXML
    private JFXButton btn_close;

    public Integer addEquipos(Recursos equipospropios) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer equipID = null;

        try {
            tx = session.beginTransaction();
            equipID = (Integer) session.save(equipospropios);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return equipID;
    }

    @FXML
    public void addEquipoAction(ActionEvent event) {

        String codigo = field_codigo.getText();
        String descripcion = text_descripcion.getText();
        String tipo = "3";
        double preciomn = Double.parseDouble(field_preciomn.getText());
        double preciomlc = 0.0;
        double cpo = Double.parseDouble(field_cpo.getText());
        double cpe = Double.parseDouble(field_cpe.getText());
        double cet = Double.parseDouble(field_CET.getText());
        double otra = Double.parseDouble(field_otras.getText());

        Recursos sumuni = new Recursos();
        sumuni.setCodigo(codigo);

        sumuni.setDescripcion(descripcion);
        sumuni.setUm(field_um.getText());
        sumuni.setPeso(0.0);
        sumuni.setPreciomn(preciomn);
        sumuni.setPreciomlc(preciomlc);
        sumuni.setMermaprod(0.0);
        sumuni.setMermatrans(0.0);
        sumuni.setMermamanip(0.0);
        sumuni.setOtrasmermas(0.0);
        String tag = utilCalcSingelton.salarioList.parallelStream().filter(item -> item.getDescripcion().trim().equals(equiposCont.comboTarifas.getValue().trim())).findFirst().map(Salario::getTag).get();
        sumuni.setPertenece(tag);
        sumuni.setTipo(tipo);
        sumuni.setGrupoescala("I");
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
        sumuni.setCet(cet);
        sumuni.setCpo(cpo);
        sumuni.setCpe(cpe);
        sumuni.setOtra(otra);


        idEquip = addEquipos(sumuni);

        if (idEquip != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Equipo agregado satisfactoriamente!");
            alert.showAndWait();
        }

        clearField();
        equiposCont.loadData();

    }

    public void clearField() {
        field_codigo.clear();
        text_descripcion.clear();
        field_preciomn.clear();
        // field_preciomlc.clear();
        field_cpo.clear();
        field_cpe.clear();
        field_CET.clear();
        field_otras.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        field_um.setText("he");
    }

    public void pasarController(EquiposController equipos) {

        equiposCont = equipos;
    }

    public void closeWindow(ActionEvent event) {

        equiposCont.loadData();
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

}



