package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Recursos;
import models.SuministrosView;
import models.UtilCalcSingelton;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevoSuministroFromBajoUpdateController implements Initializable {

    private static SessionFactory sf;
    BajoEspecificacionControllerComponetUpdate bajoEspecificacionControllerComponetSum;
    @FXML
    private JFXTextField field_codigo;

    @FXML
    private JFXTextField field_um;

    @FXML
    private JFXTextArea text_descripcion;

    @FXML
    private JFXTextField field_peso;

    @FXML
    private JFXTextField field_precioTotal;

    @FXML
    private JFXTextField field_preciomlc;

    @FXML
    private JFXTextField text_mermaprod;

    @FXML
    private JFXTextField text_manipulacion;

    @FXML
    private JFXTextField text_transporte;

    @FXML
    private JFXTextField text_otras;
    private Integer idOb;
    private SuministrosView suministrosView;
    @FXML
    private JFXButton btn_add;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Metodo add para insetar un nuevo suministro
     */


    public Integer addSuministro(Recursos suministro) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer suministroID = null;

        try {
            tx = session.beginTransaction();
            suministroID = (Integer) session.save(suministro);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministroID;
    }


    public void addSuministroAction(javafx.event.ActionEvent event) {

        Recursos sumuni = new Recursos();
        sumuni.setCodigo(field_codigo.getText());

        sumuni.setDescripcion(text_descripcion.getText());
        sumuni.setUm(field_um.getText());
        sumuni.setPeso(Double.parseDouble(field_peso.getText()));
        sumuni.setPreciomn(Double.parseDouble(field_precioTotal.getText()));
        sumuni.setPreciomlc(0.0);
        sumuni.setMermaprod(Double.parseDouble(text_mermaprod.getText()));
        sumuni.setMermatrans(Double.parseDouble(text_transporte.getText()));
        sumuni.setMermamanip(Double.parseDouble(text_manipulacion.getText()));
        sumuni.setOtrasmermas(Double.parseDouble(text_otras.getText()));
        sumuni.setPertenece(String.valueOf(idOb));
        sumuni.setTipo("1");
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
        sumuni.setCet(0.0);
        sumuni.setCpo(0.0);
        sumuni.setCpe(0.0);
        sumuni.setOtra(0.0);
        sumuni.setActive(1);

        Integer nuevo = addSuministro(sumuni);

        if (nuevo != null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Suministros agregado satisfactoriamente!");
            alert.showAndWait();

            suministrosView = new SuministrosView(sumuni.getId(), sumuni.getCodigo(), sumuni.getDescripcion(), sumuni.getUm(), sumuni.getPeso(), sumuni.getPreciomn(), 0.0, sumuni.getGrupoescala(), sumuni.getTipo(), sumuni.getPertenece(), 0.0, utilCalcSingelton.createCheckBox(sumuni.getActive()));
        }
        field_codigo.clear();
        text_descripcion.clear();
        field_um.clear();
        field_peso.clear();
        field_precioTotal.clear();
        //field_preciomlc.clear();
        text_mermaprod.clear();
        text_transporte.clear();
        text_manipulacion.clear();
        text_otras.clear();

        bajoEspecificacionControllerComponetSum.getSuminitroResult(suministrosView);
        Stage stage = (Stage) btn_add.getScene().getWindow();
        stage.close();

    }


    public void pasarParametros(BajoEspecificacionControllerComponetUpdate bajoEspecificacionControllerComponet, String code, String descrip, String um, String precio) {

        bajoEspecificacionControllerComponetSum = bajoEspecificacionControllerComponet;
        field_codigo.setText(code);
        text_descripcion.setText(descrip);
        field_um.setText(um);
        field_precioTotal.setText(precio);


        field_peso.setText("0");
        //field_preciomlc.setText("0");
        text_mermaprod.setText("0");
        text_transporte.setText("0");
        text_manipulacion.setText("0");
        text_otras.setText("0");

        idOb = bajoEspecificacionControllerComponetSum.idObra;


    }

}
