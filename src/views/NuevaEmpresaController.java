package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class NuevaEmpresaController implements Initializable {

    private static SessionFactory sf;
    public EmpresaConstructoraController empresaConstructoraController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextArea text_descripcion;
    @FXML
    private JFXTextField field_c731;
    @FXML
    private JFXTextField field_c822;
    @FXML
    private JFXTextField field_pbruta;
    private ArrayList<Conceptosgasto> conceptosgastoArrayList;
    private Empresagastos empresagastos;
    private Empresaconstructora empresaconstructora;
    @FXML
    private JFXButton btnClose;

    private ArrayList<Empresaconstructora> empresaconstructorasList;
    private ArrayList<Nomencladorempresa> nomencladorempresaArrayList;
    private ObservableList<String> listeEmpresa;
    private Nomencladorempresa nomencladorempresa;

    @FXML
    private JFXTextField coefField;

    public ArrayList<Empresaconstructora> getEmpresas() {

        empresaconstructorasList = new ArrayList<Empresaconstructora>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructorasList = (ArrayList<Empresaconstructora>) session.createQuery("FROM Empresaconstructora ").list();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresaconstructorasList;
    }

    private boolean checkEmpresa(Empresaconstructora empresaconstructora) {

        empresaconstructorasList = new ArrayList<>();
        empresaconstructorasList = getEmpresas();
        return empresaconstructorasList.stream().filter(o -> o.getCodigo().equals(empresaconstructora.getCodigo())).findFirst().isPresent();
    }

    /**
     * Metodo para agregar una nuerva empresa
     */
    public Integer AddEmpresaConstructora(Empresaconstructora empresaconstructora) {

        Session empsession = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;
        Integer emp = 0;

        try {
            trx = empsession.beginTransaction();


            boolean res = checkEmpresa(empresaconstructora);
            if (res == false) {
                emp = (Integer) empsession.save(empresaconstructora);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la empresa especificada ya existe!!! ");
                alert.showAndWait();
            }
            trx.commit();
            empsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            empsession.close();
        }
        return emp;
    }

    public ObservableList<String> getListaEmpresa() {
        listeEmpresa = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nomencladorempresaArrayList = (ArrayList<Nomencladorempresa>) session.createQuery("FROM Nomencladorempresa ").list();
            for (Nomencladorempresa me : nomencladorempresaArrayList) {
                listeEmpresa.add(me.getReup() + " - " + me.getDescripcion());
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

    public void handleDefineEmpresa() {
        String code = field_codigo.getText().substring(0, 5);
        nomencladorempresa = nomencladorempresaArrayList.stream().filter(n -> n.getReup().contentEquals(code)).findFirst().orElse(null);
        field_codigo.clear();
        field_codigo.setText(nomencladorempresa.getReup());
        text_descripcion.setText(nomencladorempresa.getDescripcion());


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getListaEmpresa();
        TextFields.bindAutoCompletion(field_codigo, listeEmpresa);

    }


    @FXML
    public void addEmpresaAction(ActionEvent event) {

        String cod = field_codigo.getText();
        String desc = text_descripcion.getText();
        String c731 = field_c731.getText();
        String c822 = field_c822.getText();
        String pbruta = field_pbruta.getText();

        if (cod.isEmpty() || desc.isEmpty() || c731.isEmpty() || c822.isEmpty() || pbruta.isEmpty()) {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Información");
            alert.setContentText("Complete los valores del formulario");
            alert.showAndWait();

        } else {
            Empresaconstructora ec = new Empresaconstructora();
            ec.setCodigo(cod);
            ec.setDescripcion(desc);
            ec.setCuenta731(Double.parseDouble(c731));
            ec.setCuenta822(Double.parseDouble(c822));
            ec.setPbruta(Double.parseDouble(pbruta));
            Integer id = AddEmpresaConstructora(ec);

            if (id != null) {
                addConceptosEmpresa(id);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Nueva empresa creada!");
                alert.showAndWait();

                empresaConstructoraController.loadData();
                clearField();
            }


        }

    }

    public void clearField() {
        field_codigo.clear();
        text_descripcion.clear();
        field_c731.clear();
        field_c822.clear();
        field_pbruta.clear();
    }


    public void addConceptosEmpresa(Integer id) {


        Session empsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;


        try {
            trx = empsession.beginTransaction();
            conceptosgastoArrayList = new ArrayList<Conceptosgasto>();
            empresaconstructora = empsession.get(Empresaconstructora.class, id);

            conceptosgastoArrayList = (ArrayList<Conceptosgasto>) empsession.createQuery("FROM Conceptosgasto ").list();

            for (Conceptosgasto conceptosgasto : conceptosgastoArrayList) {

                empresagastos = new Empresagastos();
                empresagastos.setEmpresaconstructoraId(empresaconstructora.getId());
                empresagastos.setConceptosgastoId(conceptosgasto.getId());
                empresagastos.setFormula(conceptosgasto.getFormula());
                empresagastos.setCoeficiente(conceptosgasto.getCoeficiente());
                empresagastos.setCalcular(conceptosgasto.getCalcular());
                empresagastos.setPorciento(1.0);
                empsession.persist(empresagastos);

            }


            trx.commit();
            empsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            empsession.close();
        }
    }


    public void setPasarController(EmpresaConstructoraController empresaController) {
        empresaConstructoraController = empresaController;
    }

    public void handleClose(ActionEvent event) {

        empresaConstructoraController.loadData();
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void calcAction(ActionEvent event) {
        double sum = Double.parseDouble(field_c731.getText()) + Double.parseDouble(field_c822.getText());
        double coef = Double.parseDouble(field_pbruta.getText()) / sum;
        double result = Math.round(coef * 100d) / 100d;
        coefField.setVisible(true);
        coefField.setText(String.valueOf(result));
    }


}
